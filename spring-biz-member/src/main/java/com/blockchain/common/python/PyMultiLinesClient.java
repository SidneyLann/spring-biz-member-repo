//package com.blockchain.common.python;
//
//import java.lang.foreign.Arena;
//import java.lang.foreign.FunctionDescriptor;
//import java.lang.foreign.Linker;
//import java.lang.foreign.MemorySegment;
//import java.lang.foreign.SymbolLookup;
//import java.lang.foreign.ValueLayout;
//import java.lang.invoke.MethodHandle;
//import java.nio.charset.StandardCharsets;
//import java.nio.file.Path;
//
//public class PyMultiLinesClient {
//  public static void main(String[] args) throws Throwable {
//    String pythonDll = args.length > 0 ? args[0] : "D:\\DEV\\python\\version\\python313.dll";
//
//    try (Arena arena = Arena.ofConfined()) {
//      SymbolLookup pythonLib = SymbolLookup.libraryLookup(Path.of(pythonDll), arena);
//      Linker linker = Linker.nativeLinker();
//
//      // Initialize Python
//      callVoid(linker, pythonLib, "Py_Initialize");
//
//      // Create main module
//      MemorySegment mainModule = call(linker, pythonLib, "PyImport_AddModule", FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.ADDRESS), arena.allocateFrom("__main__"));
//
//      // Get main module's dictionary
//      MemorySegment globals = call(linker, pythonLib, "PyModule_GetDict", FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.ADDRESS), mainModule);
//
//      // Execute multi-line Python script
//      String pythonCode = "import json\n" + "data = [['a','b'], ['c','d']]\n" + "result = json.dumps(data)\n" + "result  # This ensures the result is available in globals";
//
//      // Run with Py_file_input mode (257) for multi-line scripts
//      MemorySegment result = call(linker, pythonLib, "PyRun_String", FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS),
//          arena.allocateFrom(pythonCode), 257, globals, globals);
//
//      // Check for errors
//      if (result.equals(MemorySegment.NULL)) {
//        callVoid(linker, pythonLib, "PyErr_Print");
//        return;
//      }
//
//      // The result of PyRun_String in file mode is usually None
//      // We need to get our actual result from the globals
//      callVoid(linker, pythonLib, "Py_DecRef", result);
//
//      // Get the result variable from globals
//      MemorySegment resultVar = getGlobal(arena, linker, pythonLib, globals, "result");
//      if (resultVar.equals(MemorySegment.NULL)) {
//        System.err.println("Result variable not found");
//        return;
//      }
//
//      // Convert to UTF-8 string
//      String resultStr = convertToString(linker, pythonLib, resultVar);
//      System.out.println("Python result: " + resultStr);
//
//      // Clean up
//      callVoid(linker, pythonLib, "Py_DecRef", resultVar);
//    }
//  }
//
//  private static MemorySegment getGlobal(Arena arena, Linker linker, SymbolLookup pythonLib, MemorySegment globals, String varName) throws Throwable {
//    // Allocate the variable name string in the arena
//    MemorySegment varNameSeg = arena.allocateFrom(varName);
//
//    // Get the item from the dictionary
//    MethodHandle pyDictGetItemString = downcallHandle(linker, pythonLib, "PyDict_GetItemString", FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS));
//
//    return (MemorySegment) pyDictGetItemString.invoke(globals, varNameSeg);
//  }
//
//  private static String convertToString(Linker linker, SymbolLookup pythonLib, MemorySegment pyObject) throws Throwable {
//    // Convert to UTF-8 bytes
//    MemorySegment bytes = call(linker, pythonLib, "PyUnicode_AsUTF8String", FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.ADDRESS), pyObject);
//
//    if (bytes.equals(MemorySegment.NULL)) {
//      return "<CONVERSION ERROR>";
//    }
//
//    try {
//      // Get size
//      MethodHandle sizeHandle = downcallHandle(linker, pythonLib, "PyBytes_Size", FunctionDescriptor.of(ValueLayout.JAVA_LONG, ValueLayout.ADDRESS));
//      long size = (long) sizeHandle.invoke(bytes);
//
//      if (size == 0)
//        return "";
//
//      // Get data pointer
//      MemorySegment data = call(linker, pythonLib, "PyBytes_AsString", FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.ADDRESS), bytes);
//
//      // Create bounded segment
//      MemorySegment bounded = data.reinterpret(size);
//      return new String(bounded.toArray(ValueLayout.JAVA_BYTE), StandardCharsets.UTF_8);
//    } finally {
//      callVoid(linker, pythonLib, "Py_DecRef", bytes);
//    }
//  }
//
//  private static void callVoid(Linker linker, SymbolLookup lookup, String name) throws Throwable {
//    MethodHandle handle = downcallHandle(linker, lookup, name, FunctionDescriptor.ofVoid());
//    handle.invoke();
//  }
//
//  private static void callVoid(Linker linker, SymbolLookup lookup, String name, MemorySegment arg) throws Throwable {
//    MethodHandle handle = downcallHandle(linker, lookup, name, FunctionDescriptor.ofVoid(ValueLayout.ADDRESS));
//    handle.invoke(arg);
//  }
//
//  private static MemorySegment call(Linker linker, SymbolLookup lookup, String name, FunctionDescriptor desc, Object... args) throws Throwable {
//    MethodHandle handle = downcallHandle(linker, lookup, name, desc);
//    return (MemorySegment) handle.invokeWithArguments(args);
//  }
//
//  private static MethodHandle downcallHandle(Linker linker, SymbolLookup lookup, String name, FunctionDescriptor desc) {
//    MemorySegment addr = lookup.find(name).orElseThrow(() -> new RuntimeException("Missing Python function: " + name));
//    return linker.downcallHandle(addr, desc);
//  }
//}