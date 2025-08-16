//package com.blockchain.common.python;
//
//import java.lang.foreign.Arena;
//import java.lang.foreign.FunctionDescriptor;
//import java.lang.foreign.Linker;
//import java.lang.foreign.MemorySegment;
//import java.lang.foreign.SymbolLookup;
//import java.lang.foreign.ValueLayout;
//import java.lang.invoke.MethodHandle;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class PythonListConverter {
//  private final Linker linker;
//  private final SymbolLookup pythonLib;
//  private final Arena arena;
//
//  private final MethodHandle pyListSize;
//  private final MethodHandle pyListGetItem;
//  private final MethodHandle pyDictGetItem;
//  private final MethodHandle pyUnicodeAsUTF8;
//  private final MethodHandle pyLongAsLong;
//  private final MethodHandle pyFloatAsDouble;
//  private final MethodHandle pyObjectIsTrue;
//
//  private final MethodHandle pyListCheck;
//  private final MethodHandle pyDictCheck;
//  private final MethodHandle pyUnicodeCheck;
//  private final MethodHandle pyLongCheck;
//  private final MethodHandle pyFloatCheck;
//  private final MethodHandle pyBoolCheck;
//
//  public PythonListConverter(Linker linker, SymbolLookup pythonLib, Arena arena) throws Throwable {
//    this.linker = linker;
//    this.pythonLib = pythonLib;
//    this.arena = arena;
//
//    // Initialize all method handles
//    this.pyListSize = createMethodHandle("PyList_Size", FunctionDescriptor.of(ValueLayout.JAVA_LONG, ValueLayout.ADDRESS));
//
//    this.pyListGetItem = createMethodHandle("PyList_GetItem", FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_LONG));
//
//    this.pyDictGetItem = createMethodHandle("PyDict_Items", FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_LONG));
//
//    this.pyUnicodeAsUTF8 = createMethodHandle("PyUnicode_AsUTF8", FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.ADDRESS));
//
//    this.pyLongAsLong = createMethodHandle("PyLong_AsLong", FunctionDescriptor.of(ValueLayout.JAVA_LONG, ValueLayout.ADDRESS));
//
//    this.pyFloatAsDouble = createMethodHandle("PyFloat_AsDouble", FunctionDescriptor.of(ValueLayout.JAVA_DOUBLE, ValueLayout.ADDRESS));
//
//    this.pyObjectIsTrue = createMethodHandle("PyObject_IsTrue", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
//
//    this.pyListCheck = createMethodHandle("PyList_Type", FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.ADDRESS));
//
//    this.pyDictCheck = createMethodHandle("PyDict_Type", FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.ADDRESS));
//
//    this.pyUnicodeCheck = createMethodHandle("PyUnicode_Type", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
//
//    this.pyLongCheck = createMethodHandle("PyLong_Type", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
//
//    this.pyFloatCheck = createMethodHandle("PyFloat_Type", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
//
//    this.pyBoolCheck = createMethodHandle("PyBool_Type", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
//  }
//
//  private MethodHandle createMethodHandle(String name, FunctionDescriptor desc) throws Throwable {
//    MemorySegment addr = pythonLib.find(name).orElseThrow(() -> new UnsatisfiedLinkError(name + " not found"));
//    return linker.downcallHandle(addr, desc);
//  }
//
//  public List<Object> convertPyListToJavaList(MemorySegment pyList) throws Throwable {
//    List<Object> javaList = new ArrayList<>();
//
//    long size = (long) pyListSize.invoke(pyList);
//    for (long i = 0; i < size; i++) {
//      MemorySegment item = (MemorySegment) pyListGetItem.invoke(pyList, i);
//      javaList.add(convertPyObjectToJava(item));
//    }
//
//    return javaList;
//  }
//
//  private Object convertPyObjectToJava(MemorySegment pyObject) throws Throwable {
//    if (pyObject.equals(MemorySegment.NULL)) {
//      return null;
//    }
//
//    if ((int) pyListCheck.invoke(pyObject) != 0) {
//      return convertPyListToJavaList(pyObject);
//    } else if ((int) pyDictCheck.invoke(pyObject) != 0) {
//      return convertPyDictToJavaMap(pyObject);
//    } else if ((int) pyUnicodeCheck.invoke(pyObject) != 0) {
//      MemorySegment utf8 = (MemorySegment) pyUnicodeAsUTF8.invoke(pyObject);
//      return utf8.getString(0);
//    } else if ((int) pyLongCheck.invoke(pyObject) != 0) {
//      return (long) pyLongAsLong.invoke(pyObject);
//    } else if ((int) pyFloatCheck.invoke(pyObject) != 0) {
//      return (double) pyFloatAsDouble.invoke(pyObject);
//    } else if ((int) pyBoolCheck.invoke(pyObject) != 0) {
//      return (int) pyObjectIsTrue.invoke(pyObject) != 0;
//    }
//    // Add more type conversions as needed
//
//    return null; // or throw exception for unsupported types
//  }
//
//  private Map<Object, Object> convertPyDictToJavaMap(MemorySegment pyDict) throws Throwable {
//    Map<Object, Object> result = new HashMap<>();
//
//    MemorySegment items = (MemorySegment) pyDictGetItem.invoke(pyDict);
//
//    // Convert each key-value pair
//    long size = (long) pyListSize.invoke(items);
//    for (long i = 0; i < size; i++) {
//      MemorySegment tuple = (MemorySegment) pyListGetItem.invoke(items, i);
//      MemorySegment key = (MemorySegment) pyListGetItem.invoke(tuple, 0);
//      MemorySegment value = (MemorySegment) pyListGetItem.invoke(tuple, 1);
//
//      result.put(convertPyObjectToJava(key), convertPyObjectToJava(value));
//    }
//
//    return result;
//  }
//
//  public <T> T convertPyObject(MemorySegment pyObject, Class<T> targetType) throws Throwable {
//    Object result = convertPyObjectToJava(pyObject);
//    return targetType.cast(result);
//  }
//}
