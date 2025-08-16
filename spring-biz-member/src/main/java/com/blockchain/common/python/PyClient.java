package com.blockchain.common.python;

import java.lang.foreign.Arena;
import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.Linker;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SymbolLookup;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandle;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.blockchain.common.util.JsonUtil;

public class PyClient implements AutoCloseable {

  // Static state for shared resources
  private static final Object STATIC_LOCK = new Object();
  private static Arena staticArena;
  private static Linker linker;
  private static SymbolLookup pythonLib;
  private static MemorySegment pyNone;
  private static MemorySegment pythonLibHandle;
  private static MethodHandle dlcloseHandle;
  private static MemorySegment globals;
  private static boolean initialized = false;
  private static int refCount = 0;

  // Per-instance state
  private final Arena arena;
  private final Map<String, MemorySegment> refCountedObjects = new HashMap<>();

  public PyClient() throws Throwable {
    this.arena = Arena.ofConfined();
    synchronized (STATIC_LOCK) {
      if (refCount == 0) {
        initializeStatic();
      }
      refCount++;
    }
  }

  private static void initializeStatic() throws Throwable {
    String osName = System.getProperty("os.name").toLowerCase();
    boolean isWindows = osName.contains("win");
    boolean isMac = osName.contains("mac");
    String pyLibPath = System.getProperty("pyLibPath");

    staticArena = Arena.ofConfined();
    linker = Linker.nativeLinker();

    // Get dl functions using platform-independent approach
    SymbolLookup loaderLookup = SymbolLookup.loaderLookup();
    MemorySegment dlopenAddr = loaderLookup.find("dlopen").orElse(null);
    MemorySegment dlsymAddr = loaderLookup.find("dlsym").orElse(null);
    MemorySegment dlcloseAddr = loaderLookup.find("dlclose").orElse(null);

    // If symbols not found via loader, try platform-specific approaches
    if (dlopenAddr == null || dlsymAddr == null || dlcloseAddr == null) {
      if (isWindows) {
        // Windows fallback
        String libName = "kernel32";
        SymbolLookup sysLib = SymbolLookup.libraryLookup(libName, staticArena);
        dlopenAddr = sysLib.find("LoadLibraryA").orElse(null);
        dlsymAddr = sysLib.find("GetProcAddress").orElse(null);
        dlcloseAddr = sysLib.find("FreeLibrary").orElse(null);
      } else {
        // macOS fallback
        if (isMac) {
          try {
            SymbolLookup sysLib = SymbolLookup.libraryLookup("libSystem", staticArena);
            dlopenAddr = sysLib.find("dlopen").orElse(null);
            dlsymAddr = sysLib.find("dlsym").orElse(null);
            dlcloseAddr = sysLib.find("dlclose").orElse(null);
          } catch (IllegalArgumentException e) {
            System.err.println("Warning: libSystem not found on macOS");
          }
        }

        // Linux/Unix fallback with multiple candidates
        if (dlopenAddr == null || dlsymAddr == null || dlcloseAddr == null) {
          List<String> candidateLibs = List.of("dl", "libdl.so.2", "libdl.so");
          for (String libName : candidateLibs) {
            try {
              SymbolLookup sysLib = SymbolLookup.libraryLookup(libName, staticArena);
              dlopenAddr = sysLib.find("dlopen").orElse(null);
              dlsymAddr = sysLib.find("dlsym").orElse(null);
              dlcloseAddr = sysLib.find("dlclose").orElse(null);

              if (dlopenAddr != null && dlsymAddr != null && dlcloseAddr != null) {
                break; // Found all symbols
              }
            } catch (IllegalArgumentException e) {
              // Ignore and try next candidate
              System.err.println("Library not found: " + libName);
            }
          }
        }
      }
    }

    // Final check if symbols were found
    if (dlopenAddr == null || dlsymAddr == null || dlcloseAddr == null) {
      throw new RuntimeException("Failed to find dl functions. Tried: " + (isWindows ? "kernel32" : isMac ? "libSystem" : "dl/libdl.so.2/libdl.so"));
    }

    // Create method handles
    MethodHandle dlopen = linker.downcallHandle(dlopenAddr, FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT));
    MethodHandle dlsym = linker.downcallHandle(dlsymAddr, FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS));
    dlcloseHandle = linker.downcallHandle(dlcloseAddr, FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS));

    // Load Python library
    int RTLD_GLOBAL = isMac ? 0x8 : 0x00100;
    int RTLD_LAZY = 0x00001;
    MemorySegment pathSeg = staticArena.allocateFrom(pyLibPath);
    MemorySegment handle = (MemorySegment) dlopen.invoke(pathSeg, RTLD_LAZY | RTLD_GLOBAL);

    if (handle.equals(MemorySegment.NULL)) {
      throw new RuntimeException("Failed to load library: " + pyLibPath);
    }

    pythonLibHandle = handle;

    // Create custom symbol lookup
    pythonLib = name -> {
      try {
        MemorySegment nameSeg = staticArena.allocateFrom(name);
        MemorySegment sym = (MemorySegment) dlsym.invoke(handle, nameSeg);
        return sym.equals(MemorySegment.NULL) ? Optional.empty() : Optional.of(sym);
      } catch (Throwable t) {
        return Optional.empty();
      }
    };

    // Initialize Python only once
    callVoidStatic("Py_Initialize");
    initialized = true;

    // Setup main module and globals
    MemorySegment mainModule = callAddressStatic("PyImport_AddModule", FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.ADDRESS), staticArena.allocateFrom("__main__"));
    globals = callAddressStatic("PyModule_GetDict", FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.ADDRESS), mainModule);

    // Register shutdown hook for final cleanup
    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      synchronized (STATIC_LOCK) {
        if (initialized) {
          try {
            callVoidStatic("Py_Finalize");
            if (!pythonLibHandle.equals(MemorySegment.NULL)) {
              int result = (int) dlcloseHandle.invoke(pythonLibHandle);
              if (result != 0) {
                System.err.println("Warning: dlclose failed");
              }
              pythonLibHandle = MemorySegment.NULL;
            }
          } catch (Throwable t) {
            t.printStackTrace();
          } finally {
            staticArena.close();
            initialized = false;
          }
        }
      }
    }));
  }

  // Static helper methods (callVoidStatic, callAddressStatic etc.)
  private static void callVoidStatic(String name) throws Throwable {
    MethodHandle handle = downcallHandleStatic(name, FunctionDescriptor.ofVoid());
    handle.invoke();
  }

  private static MemorySegment callAddressStatic(String name, FunctionDescriptor desc, Object... args) throws Throwable {
    MethodHandle handle = downcallHandleStatic(name, desc);
    return (MemorySegment) handle.invokeWithArguments(args);
  }

  private static MethodHandle downcallHandleStatic(String name, FunctionDescriptor desc) {
    MemorySegment addr = pythonLib.find(name).orElseThrow();
    return linker.downcallHandle(addr, desc);
  }

  private MemorySegment dlsym(MemorySegment handle, String name) throws Throwable {
    SymbolLookup libdl = SymbolLookup.libraryLookup("dl", arena);
    MethodHandle dlsym = linker.downcallHandle(libdl.find("dlsym").orElseThrow(), FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS));
    return (MemorySegment) dlsym.invoke(handle, arena.allocateFrom(name));
  }

  private MemorySegment dlopen(String filename, int flags) throws Throwable {
    SymbolLookup libdl = SymbolLookup.libraryLookup("dl", arena);
    MethodHandle dlopen = linker.downcallHandle(libdl.find("dlopen").orElseThrow(), FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT));
    return (MemorySegment) dlopen.invoke(arena.allocateFrom(filename), flags);
  }

  public String executeExpression(String pythonCode) throws Throwable {
    return executePythonCode(pythonCode, 258, null);
  }

  public String executeScript(String pythonCode) throws Throwable {
    return executePythonCode(pythonCode, 257, "result");
  }

  public String executeScript(String pythonCode, String resultVarName) throws Throwable {
    return executePythonCode(pythonCode, 257, resultVarName);
  }

  private String executePythonCode(String pythonCode, int mode, String resultVar) throws Throwable {
    if (!initialized)
      throw new IllegalStateException("Python interpreter not initialized");

    // Execute Python code
    MemorySegment codeSeg = arena.allocateFrom(pythonCode);
    MemorySegment result = callAddress("PyRun_String", FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS), codeSeg, mode,
        globals, globals);

    // Check for errors
    if (result.equals(MemorySegment.NULL)) {
      callVoid("PyErr_Print");
      return null;
    }

    // Handle different execution modes
    if (mode == 258) { // Expression mode
      try {
        return convertToString(result);
      } finally {
        decRef(result);
      }
    } else { // Script mode
      decRef(result);
      if (resultVar == null) {
        throw new IllegalArgumentException("Result variable name required for script mode");
      }

      // Get the result variable
      MemorySegment resultVarSeg = getGlobal(resultVar);
      if (resultVarSeg.equals(MemorySegment.NULL)) {
        throw new RuntimeException("Result variable '" + resultVar + "' not found");
      }

      try {
        return convertToString(resultVarSeg);
      } finally {
        decRef(resultVarSeg);
      }
    }
  }

  public void setGlobal(String varName, Object value) throws Throwable {
    MemorySegment pyObj = toPyObject(value);
    int result = callInt("PyDict_SetItemString", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS), globals, arena.allocateFrom(varName),
        pyObj);

    // Always decref after set - PyDict_SetItemString steals reference
    decRef(pyObj);

    if (result != 0) {
      throw new RuntimeException("Failed to set global: " + varName);
    }
  }

  public MemorySegment getGlobal(String varName) throws Throwable {
    MemorySegment var = callAddress("PyDict_GetItemString", FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS), globals, arena.allocateFrom(varName));

    if (var.equals(MemorySegment.NULL))
      return null;

    // Convert borrowed reference to owned reference
    incRef(var);
    return var;
  }

  private MemorySegment createPythonString(String value) throws Throwable {
    MemorySegment strSeg = arena.allocateFrom(value);
    MemorySegment pyStr = callAddress("PyUnicode_FromString", FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.ADDRESS), strSeg);
    // trackReference(pyStr);
    return pyStr;
  }

  private String convertToString(MemorySegment pyObject) throws Throwable {
    // First convert the object to its string representation
    MemorySegment pyStrObject = callAddress("PyObject_Str", FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.ADDRESS), pyObject);

    if (pyStrObject.equals(MemorySegment.NULL)) {
      return "<CONVERSION ERROR: PyObject_Str failed>";
    }

    try {
      // Now convert the string object to UTF-8 bytes
      MemorySegment bytes = callAddress("PyUnicode_AsUTF8String", FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.ADDRESS), pyStrObject);

      if (bytes.equals(MemorySegment.NULL)) {
        return "<CONVERSION ERROR: PyUnicode_AsUTF8String failed>";
      }

      try {
        // Get size
        MethodHandle sizeHandle = downcallHandle("PyBytes_Size", FunctionDescriptor.of(ValueLayout.JAVA_LONG, ValueLayout.ADDRESS));
        long size = (long) sizeHandle.invoke(bytes);

        if (size == 0)
          return "";

        // Get data pointer
        MemorySegment data = callAddress("PyBytes_AsString", FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.ADDRESS), bytes);

        // Create bounded segment
        MemorySegment bounded = data.reinterpret(size);
        return new String(bounded.toArray(ValueLayout.JAVA_BYTE), StandardCharsets.UTF_8);
      } finally {
        decRef(bytes);
      }
    } finally {
      decRef(pyStrObject);
    }
  }

  // Reference counting utilities
  private void trackReference(MemorySegment obj) {
    if (!obj.equals(MemorySegment.NULL)) {
      refCountedObjects.put(Long.toHexString(obj.address()), obj);
    }
  }

  private void incRef(MemorySegment obj) throws Throwable {
    if (!obj.equals(MemorySegment.NULL)) {
      callVoid("Py_IncRef", obj);
      trackReference(obj);
    }
  }

  private void decRef(MemorySegment obj) throws Throwable {
    if (!obj.equals(MemorySegment.NULL)) {
      callVoid("Py_DecRef", obj);
      refCountedObjects.remove(Long.toHexString(obj.address()));
    }
  }

  private void callVoid(String name) throws Throwable {
    MethodHandle handle = downcallHandle(name, FunctionDescriptor.ofVoid());
    handle.invoke();
  }

  private void callVoid(String name, MemorySegment arg) throws Throwable {
    MethodHandle handle = downcallHandle(name, FunctionDescriptor.ofVoid(ValueLayout.ADDRESS));
    handle.invoke(arg);
  }

  private MemorySegment callAddress(String name, FunctionDescriptor desc, Object... args) throws Throwable {
    MethodHandle handle = downcallHandle(name, desc);
    return (MemorySegment) handle.invokeWithArguments(args);
  }

  private int callInt(String name, FunctionDescriptor desc, Object... args) throws Throwable {
    MethodHandle handle = downcallHandle(name, desc);
    return (int) handle.invokeWithArguments(args);
  }

  private MethodHandle downcallHandle(String name, FunctionDescriptor desc) {
    MemorySegment addr = pythonLib.find(name).orElseThrow(() -> new RuntimeException("Missing Python function: " + name));
    return linker.downcallHandle(addr, desc);
  }

  private MemorySegment toPyObject(Object value) throws Throwable {
    if (value == null) {
      return getPyNone();
    } else if (value instanceof String) {
      return createPythonString((String) value);
    } else if (value instanceof Integer) {
      return callAddress("PyLong_FromLong", FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.JAVA_INT), (Integer) value);
    } else if (value instanceof Long) {
      return callAddress("PyLong_FromLongLong", FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.JAVA_LONG), (Long) value);
    } else if (value instanceof Double) {
      return callAddress("PyFloat_FromDouble", FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.JAVA_DOUBLE), (Double) value);
    } else if (value instanceof Boolean) {
      int boolVal = (Boolean) value ? 1 : 0;
      return callAddress("PyBool_FromLong", FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.JAVA_INT), boolVal);
    } else if (value instanceof List) {
      List<?> list = (List<?>) value;
      MemorySegment pyList = callAddress("PyList_New", FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.JAVA_LONG), (long) list.size());

      if (pyList.equals(MemorySegment.NULL)) {
        throw new RuntimeException("Failed to create list");
      }

      try {
        for (int i = 0; i < list.size(); i++) {
          Object element = list.get(i);
          MemorySegment pyItem = toPyObject(element); // Recursive call

          int result = callInt("PyList_SetItem", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_LONG, ValueLayout.ADDRESS), pyList, (long) i, pyItem);

          if (result != 0) {
            // Only decref if PyList_SetItem fails (doesn't steal reference)
            decRef(pyItem);
            throw new RuntimeException("Failed to set list item at index " + i);
          }
          // PyList_SetItem "steals" reference - no need to decref pyItem
        }
        return pyList;
      } catch (Throwable t) {
        decRef(pyList); // Clean up list on error
        throw t;
      }
    } else {
      throw new IllegalArgumentException("Unsupported type: " + value.getClass());
    }
  }

  private MemorySegment getPyNone() throws Throwable {
    if (pyNone == null) {
      pyNone = pythonLib.find("_Py_None").orElseThrow(() -> new RuntimeException("Cannot find Py_None symbol"));
    }
    incRef(pyNone);
    return pyNone;
  }

  @Override
  public void close() {
    synchronized (STATIC_LOCK) {
      refCount--;
    }
    // Clean only per-instance resources
    for (MemorySegment obj : refCountedObjects.values()) {
      try {
        callVoid("Py_DecRef", obj);
      } catch (Throwable t) {
        t.printStackTrace();
      }
    }
    refCountedObjects.clear();
    arena.close();
  }

  public static void main(String[] args) {
    String sentence = args.length > 0 ? args[0] : "dministration—now hangs in the balance BY Christiaan Hetzner February 19, 2025 Tech Protests against Elon Mu";
    String pyLibPath = args.length > 1 ? args[1] : "D:\\DEV\\python\\version\\python313.dll";
    System.setProperty("pyLibPath", pyLibPath);
    String rootFolder = "/d/dev";

    try (PyClient py = new PyClient()) {
      // Execute single expression
      String result1 = py.executeExpression("'Hello ' + 'World'");
      System.out.println("Expression result: " + result1);

      // Execute multi-line script
      String script = "import json\n" + "data = [['a','b'], ['c','d']]\n" + "result = json.dumps(data)";

      String result2 = py.executeScript(script);
      System.out.println("Script result: " + result2);

      // Execute with custom result variable
      String result3 = py.executeScript(script, "data");
      System.out.println("Custom variable result: " + result3);

      List<List<List<?>>> tripleNested = List.of(List.of(List.of(1, 2, 3), List.of("a", "b", "c")), List.of(List.of(true, false), List.of(1.1, 2.2)));
      py.setGlobal("triple_list", tripleNested);
      String result = py.executeScript("import json\n" + "result = json.dumps(triple_list, indent=2)", "result");
      System.out.println("Triple nested result:\n" + result);

      List<String> sentences = new ArrayList<>(1);
      sentences.add(sentence);
      sentences.add(sentence);
      sentences.add(sentence);
      py.setGlobal("sentences", sentences);
      result = py.executeScript("import os\nimport sys\nroot_folder='" + rootFolder
          + "'\ntarget_dir = os.path.join(root_folder, 'idea')\nsys.path.append(target_dir)\nfrom ner_refer_prod import ner\nresult=ner(sentences)");
      System.out.println("json result:\n" + result);
      List<List> mapResult = JsonUtil.json2List(result, List.class);
      System.out.println("map result:\n" + mapResult);

      // Set and get global variables
      py.setGlobal("name", "Alice");
      MemorySegment name = py.getGlobal("name");
      System.out.println("Global variable: " + name);
    } catch (Throwable t) {
      t.printStackTrace();
    }
  }
}