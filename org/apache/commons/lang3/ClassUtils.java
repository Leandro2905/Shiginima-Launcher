/*      */ package org.apache.commons.lang3;
/*      */ 
/*      */ import java.lang.reflect.Method;
/*      */ import java.lang.reflect.Modifier;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collections;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedHashSet;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import org.apache.commons.lang3.mutable.MutableObject;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class ClassUtils
/*      */ {
/*      */   public static final char PACKAGE_SEPARATOR_CHAR = '.';
/*      */   
/*      */   public enum Interfaces
/*      */   {
/*   53 */     INCLUDE, EXCLUDE;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   64 */   public static final String PACKAGE_SEPARATOR = String.valueOf('.');
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final char INNER_CLASS_SEPARATOR_CHAR = '$';
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   74 */   public static final String INNER_CLASS_SEPARATOR = String.valueOf('$');
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   79 */   private static final Map<Class<?>, Class<?>> primitiveWrapperMap = new HashMap<Class<?>, Class<?>>();
/*      */   static {
/*   81 */     primitiveWrapperMap.put(boolean.class, Boolean.class);
/*   82 */     primitiveWrapperMap.put(byte.class, Byte.class);
/*   83 */     primitiveWrapperMap.put(char.class, Character.class);
/*   84 */     primitiveWrapperMap.put(short.class, Short.class);
/*   85 */     primitiveWrapperMap.put(int.class, Integer.class);
/*   86 */     primitiveWrapperMap.put(long.class, Long.class);
/*   87 */     primitiveWrapperMap.put(double.class, Double.class);
/*   88 */     primitiveWrapperMap.put(float.class, Float.class);
/*   89 */     primitiveWrapperMap.put(void.class, void.class);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   95 */   private static final Map<Class<?>, Class<?>> wrapperPrimitiveMap = new HashMap<Class<?>, Class<?>>();
/*      */   static {
/*   97 */     for (Class<?> primitiveClass : primitiveWrapperMap.keySet()) {
/*   98 */       Class<?> wrapperClass = primitiveWrapperMap.get(primitiveClass);
/*   99 */       if (!primitiveClass.equals(wrapperClass)) {
/*  100 */         wrapperPrimitiveMap.put(wrapperClass, primitiveClass);
/*      */       }
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  119 */     Map<String, String> m = new HashMap<String, String>();
/*  120 */     m.put("int", "I");
/*  121 */     m.put("boolean", "Z");
/*  122 */     m.put("float", "F");
/*  123 */     m.put("long", "J");
/*  124 */     m.put("short", "S");
/*  125 */     m.put("byte", "B");
/*  126 */     m.put("double", "D");
/*  127 */     m.put("char", "C");
/*  128 */     m.put("void", "V");
/*  129 */     Map<String, String> r = new HashMap<String, String>();
/*  130 */     for (Map.Entry<String, String> e : m.entrySet()) {
/*  131 */       r.put(e.getValue(), e.getKey());
/*      */     }
/*  133 */     abbreviationMap = Collections.unmodifiableMap(m);
/*  134 */     reverseAbbreviationMap = Collections.unmodifiableMap(r);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final Map<String, String> abbreviationMap;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final Map<String, String> reverseAbbreviationMap;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String getShortClassName(Object object, String valueIfNull) {
/*  159 */     if (object == null) {
/*  160 */       return valueIfNull;
/*      */     }
/*  162 */     return getShortClassName(object.getClass());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String getShortClassName(Class<?> cls) {
/*  176 */     if (cls == null) {
/*  177 */       return "";
/*      */     }
/*  179 */     return getShortClassName(cls.getName());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String getShortClassName(String className) {
/*  195 */     if (StringUtils.isEmpty(className)) {
/*  196 */       return "";
/*      */     }
/*      */     
/*  199 */     StringBuilder arrayPrefix = new StringBuilder();
/*      */ 
/*      */     
/*  202 */     if (className.startsWith("[")) {
/*  203 */       while (className.charAt(0) == '[') {
/*  204 */         className = className.substring(1);
/*  205 */         arrayPrefix.append("[]");
/*      */       } 
/*      */       
/*  208 */       if (className.charAt(0) == 'L' && className.charAt(className.length() - 1) == ';') {
/*  209 */         className = className.substring(1, className.length() - 1);
/*      */       }
/*      */       
/*  212 */       if (reverseAbbreviationMap.containsKey(className)) {
/*  213 */         className = reverseAbbreviationMap.get(className);
/*      */       }
/*      */     } 
/*      */     
/*  217 */     int lastDotIdx = className.lastIndexOf('.');
/*  218 */     int innerIdx = className.indexOf('$', (lastDotIdx == -1) ? 0 : (lastDotIdx + 1));
/*      */     
/*  220 */     String out = className.substring(lastDotIdx + 1);
/*  221 */     if (innerIdx != -1) {
/*  222 */       out = out.replace('$', '.');
/*      */     }
/*  224 */     return out + arrayPrefix;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String getSimpleName(Class<?> cls) {
/*  236 */     if (cls == null) {
/*  237 */       return "";
/*      */     }
/*  239 */     return cls.getSimpleName();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String getSimpleName(Object object, String valueIfNull) {
/*  252 */     if (object == null) {
/*  253 */       return valueIfNull;
/*      */     }
/*  255 */     return getSimpleName(object.getClass());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String getPackageName(Object object, String valueIfNull) {
/*  268 */     if (object == null) {
/*  269 */       return valueIfNull;
/*      */     }
/*  271 */     return getPackageName(object.getClass());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String getPackageName(Class<?> cls) {
/*  281 */     if (cls == null) {
/*  282 */       return "";
/*      */     }
/*  284 */     return getPackageName(cls.getName());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String getPackageName(String className) {
/*  297 */     if (StringUtils.isEmpty(className)) {
/*  298 */       return "";
/*      */     }
/*      */ 
/*      */     
/*  302 */     while (className.charAt(0) == '[') {
/*  303 */       className = className.substring(1);
/*      */     }
/*      */     
/*  306 */     if (className.charAt(0) == 'L' && className.charAt(className.length() - 1) == ';') {
/*  307 */       className = className.substring(1);
/*      */     }
/*      */     
/*  310 */     int i = className.lastIndexOf('.');
/*  311 */     if (i == -1) {
/*  312 */       return "";
/*      */     }
/*  314 */     return className.substring(0, i);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String getAbbreviatedName(Class<?> cls, int len) {
/*  330 */     if (cls == null) {
/*  331 */       return "";
/*      */     }
/*  333 */     return getAbbreviatedName(cls.getName(), len);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String getAbbreviatedName(String className, int len) {
/*  362 */     if (len <= 0) {
/*  363 */       throw new IllegalArgumentException("len must be > 0");
/*      */     }
/*  365 */     if (className == null) {
/*  366 */       return "";
/*      */     }
/*      */     
/*  369 */     int availableSpace = len;
/*  370 */     int packageLevels = StringUtils.countMatches(className, '.');
/*  371 */     String[] output = new String[packageLevels + 1];
/*  372 */     int endIndex = className.length() - 1;
/*  373 */     for (int level = packageLevels; level >= 0; level--) {
/*  374 */       int startIndex = className.lastIndexOf('.', endIndex);
/*  375 */       String part = className.substring(startIndex + 1, endIndex + 1);
/*  376 */       availableSpace -= part.length();
/*  377 */       if (level > 0)
/*      */       {
/*  379 */         availableSpace--;
/*      */       }
/*  381 */       if (level == packageLevels) {
/*      */         
/*  383 */         output[level] = part;
/*      */       }
/*  385 */       else if (availableSpace > 0) {
/*  386 */         output[level] = part;
/*      */       } else {
/*      */         
/*  389 */         output[level] = part.substring(0, 1);
/*      */       } 
/*      */       
/*  392 */       endIndex = startIndex - 1;
/*      */     } 
/*      */     
/*  395 */     return StringUtils.join((Object[])output, '.');
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static List<Class<?>> getAllSuperclasses(Class<?> cls) {
/*  408 */     if (cls == null) {
/*  409 */       return null;
/*      */     }
/*  411 */     List<Class<?>> classes = new ArrayList<Class<?>>();
/*  412 */     Class<?> superclass = cls.getSuperclass();
/*  413 */     while (superclass != null) {
/*  414 */       classes.add(superclass);
/*  415 */       superclass = superclass.getSuperclass();
/*      */     } 
/*  417 */     return classes;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static List<Class<?>> getAllInterfaces(Class<?> cls) {
/*  434 */     if (cls == null) {
/*  435 */       return null;
/*      */     }
/*      */     
/*  438 */     LinkedHashSet<Class<?>> interfacesFound = new LinkedHashSet<Class<?>>();
/*  439 */     getAllInterfaces(cls, interfacesFound);
/*      */     
/*  441 */     return new ArrayList<Class<?>>(interfacesFound);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void getAllInterfaces(Class<?> cls, HashSet<Class<?>> interfacesFound) {
/*  451 */     while (cls != null) {
/*  452 */       Class<?>[] interfaces = cls.getInterfaces();
/*      */       
/*  454 */       for (Class<?> i : interfaces) {
/*  455 */         if (interfacesFound.add(i)) {
/*  456 */           getAllInterfaces(i, interfacesFound);
/*      */         }
/*      */       } 
/*      */       
/*  460 */       cls = cls.getSuperclass();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static List<Class<?>> convertClassNamesToClasses(List<String> classNames) {
/*  479 */     if (classNames == null) {
/*  480 */       return null;
/*      */     }
/*  482 */     List<Class<?>> classes = new ArrayList<Class<?>>(classNames.size());
/*  483 */     for (String className : classNames) {
/*      */       try {
/*  485 */         classes.add(Class.forName(className));
/*  486 */       } catch (Exception ex) {
/*  487 */         classes.add(null);
/*      */       } 
/*      */     } 
/*  490 */     return classes;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static List<String> convertClassesToClassNames(List<Class<?>> classes) {
/*  506 */     if (classes == null) {
/*  507 */       return null;
/*      */     }
/*  509 */     List<String> classNames = new ArrayList<String>(classes.size());
/*  510 */     for (Class<?> cls : classes) {
/*  511 */       if (cls == null) {
/*  512 */         classNames.add(null); continue;
/*      */       } 
/*  514 */       classNames.add(cls.getName());
/*      */     } 
/*      */     
/*  517 */     return classNames;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isAssignable(Class<?>[] classArray, Class<?>... toClassArray) {
/*  559 */     return isAssignable(classArray, toClassArray, SystemUtils.isJavaVersionAtLeast(JavaVersion.JAVA_1_5));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isAssignable(Class<?>[] classArray, Class<?>[] toClassArray, boolean autoboxing) {
/*  595 */     if (!ArrayUtils.isSameLength((Object[])classArray, (Object[])toClassArray)) {
/*  596 */       return false;
/*      */     }
/*  598 */     if (classArray == null) {
/*  599 */       classArray = ArrayUtils.EMPTY_CLASS_ARRAY;
/*      */     }
/*  601 */     if (toClassArray == null) {
/*  602 */       toClassArray = ArrayUtils.EMPTY_CLASS_ARRAY;
/*      */     }
/*  604 */     for (int i = 0; i < classArray.length; i++) {
/*  605 */       if (!isAssignable(classArray[i], toClassArray[i], autoboxing)) {
/*  606 */         return false;
/*      */       }
/*      */     } 
/*  609 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isPrimitiveOrWrapper(Class<?> type) {
/*  623 */     if (type == null) {
/*  624 */       return false;
/*      */     }
/*  626 */     return (type.isPrimitive() || isPrimitiveWrapper(type));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isPrimitiveWrapper(Class<?> type) {
/*  640 */     return wrapperPrimitiveMap.containsKey(type);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isAssignable(Class<?> cls, Class<?> toClass) {
/*  675 */     return isAssignable(cls, toClass, SystemUtils.isJavaVersionAtLeast(JavaVersion.JAVA_1_5));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isAssignable(Class<?> cls, Class<?> toClass, boolean autoboxing) {
/*  706 */     if (toClass == null) {
/*  707 */       return false;
/*      */     }
/*      */     
/*  710 */     if (cls == null) {
/*  711 */       return !toClass.isPrimitive();
/*      */     }
/*      */     
/*  714 */     if (autoboxing) {
/*  715 */       if (cls.isPrimitive() && !toClass.isPrimitive()) {
/*  716 */         cls = primitiveToWrapper(cls);
/*  717 */         if (cls == null) {
/*  718 */           return false;
/*      */         }
/*      */       } 
/*  721 */       if (toClass.isPrimitive() && !cls.isPrimitive()) {
/*  722 */         cls = wrapperToPrimitive(cls);
/*  723 */         if (cls == null) {
/*  724 */           return false;
/*      */         }
/*      */       } 
/*      */     } 
/*  728 */     if (cls.equals(toClass)) {
/*  729 */       return true;
/*      */     }
/*  731 */     if (cls.isPrimitive()) {
/*  732 */       if (!toClass.isPrimitive()) {
/*  733 */         return false;
/*      */       }
/*  735 */       if (int.class.equals(cls)) {
/*  736 */         return (long.class.equals(toClass) || float.class.equals(toClass) || double.class.equals(toClass));
/*      */       }
/*      */ 
/*      */       
/*  740 */       if (long.class.equals(cls)) {
/*  741 */         return (float.class.equals(toClass) || double.class.equals(toClass));
/*      */       }
/*      */       
/*  744 */       if (boolean.class.equals(cls)) {
/*  745 */         return false;
/*      */       }
/*  747 */       if (double.class.equals(cls)) {
/*  748 */         return false;
/*      */       }
/*  750 */       if (float.class.equals(cls)) {
/*  751 */         return double.class.equals(toClass);
/*      */       }
/*  753 */       if (char.class.equals(cls)) {
/*  754 */         return (int.class.equals(toClass) || long.class.equals(toClass) || float.class.equals(toClass) || double.class.equals(toClass));
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*  759 */       if (short.class.equals(cls)) {
/*  760 */         return (int.class.equals(toClass) || long.class.equals(toClass) || float.class.equals(toClass) || double.class.equals(toClass));
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*  765 */       if (byte.class.equals(cls)) {
/*  766 */         return (short.class.equals(toClass) || int.class.equals(toClass) || long.class.equals(toClass) || float.class.equals(toClass) || double.class.equals(toClass));
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  773 */       return false;
/*      */     } 
/*  775 */     return toClass.isAssignableFrom(cls);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Class<?> primitiveToWrapper(Class<?> cls) {
/*  791 */     Class<?> convertedClass = cls;
/*  792 */     if (cls != null && cls.isPrimitive()) {
/*  793 */       convertedClass = primitiveWrapperMap.get(cls);
/*      */     }
/*  795 */     return convertedClass;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Class<?>[] primitivesToWrappers(Class<?>... classes) {
/*  809 */     if (classes == null) {
/*  810 */       return null;
/*      */     }
/*      */     
/*  813 */     if (classes.length == 0) {
/*  814 */       return classes;
/*      */     }
/*      */     
/*  817 */     Class<?>[] convertedClasses = new Class[classes.length];
/*  818 */     for (int i = 0; i < classes.length; i++) {
/*  819 */       convertedClasses[i] = primitiveToWrapper(classes[i]);
/*      */     }
/*  821 */     return convertedClasses;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Class<?> wrapperToPrimitive(Class<?> cls) {
/*  841 */     return wrapperPrimitiveMap.get(cls);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Class<?>[] wrappersToPrimitives(Class<?>... classes) {
/*  859 */     if (classes == null) {
/*  860 */       return null;
/*      */     }
/*      */     
/*  863 */     if (classes.length == 0) {
/*  864 */       return classes;
/*      */     }
/*      */     
/*  867 */     Class<?>[] convertedClasses = new Class[classes.length];
/*  868 */     for (int i = 0; i < classes.length; i++) {
/*  869 */       convertedClasses[i] = wrapperToPrimitive(classes[i]);
/*      */     }
/*  871 */     return convertedClasses;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isInnerClass(Class<?> cls) {
/*  884 */     return (cls != null && cls.getEnclosingClass() != null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Class<?> getClass(ClassLoader classLoader, String className, boolean initialize) throws ClassNotFoundException {
/*      */     try {
/*      */       Class<?> clazz;
/*  905 */       if (abbreviationMap.containsKey(className)) {
/*  906 */         String clsName = "[" + (String)abbreviationMap.get(className);
/*  907 */         clazz = Class.forName(clsName, initialize, classLoader).getComponentType();
/*      */       } else {
/*  909 */         clazz = Class.forName(toCanonicalName(className), initialize, classLoader);
/*      */       } 
/*  911 */       return clazz;
/*  912 */     } catch (ClassNotFoundException ex) {
/*      */       
/*  914 */       int lastDotIndex = className.lastIndexOf('.');
/*      */       
/*  916 */       if (lastDotIndex != -1) {
/*      */         try {
/*  918 */           return getClass(classLoader, className.substring(0, lastDotIndex) + '$' + className.substring(lastDotIndex + 1), initialize);
/*      */         
/*      */         }
/*  921 */         catch (ClassNotFoundException ex2) {}
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*  926 */       throw ex;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Class<?> getClass(ClassLoader classLoader, String className) throws ClassNotFoundException {
/*  943 */     return getClass(classLoader, className, true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Class<?> getClass(String className) throws ClassNotFoundException {
/*  958 */     return getClass(className, true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Class<?> getClass(String className, boolean initialize) throws ClassNotFoundException {
/*  973 */     ClassLoader contextCL = Thread.currentThread().getContextClassLoader();
/*  974 */     ClassLoader loader = (contextCL == null) ? ClassUtils.class.getClassLoader() : contextCL;
/*  975 */     return getClass(loader, className, initialize);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Method getPublicMethod(Class<?> cls, String methodName, Class<?>... parameterTypes) throws SecurityException, NoSuchMethodException {
/* 1005 */     Method declaredMethod = cls.getMethod(methodName, parameterTypes);
/* 1006 */     if (Modifier.isPublic(declaredMethod.getDeclaringClass().getModifiers())) {
/* 1007 */       return declaredMethod;
/*      */     }
/*      */     
/* 1010 */     List<Class<?>> candidateClasses = new ArrayList<Class<?>>();
/* 1011 */     candidateClasses.addAll(getAllInterfaces(cls));
/* 1012 */     candidateClasses.addAll(getAllSuperclasses(cls));
/*      */     
/* 1014 */     for (Class<?> candidateClass : candidateClasses) {
/* 1015 */       Method candidateMethod; if (!Modifier.isPublic(candidateClass.getModifiers())) {
/*      */         continue;
/*      */       }
/*      */       
/*      */       try {
/* 1020 */         candidateMethod = candidateClass.getMethod(methodName, parameterTypes);
/* 1021 */       } catch (NoSuchMethodException ex) {
/*      */         continue;
/*      */       } 
/* 1024 */       if (Modifier.isPublic(candidateMethod.getDeclaringClass().getModifiers())) {
/* 1025 */         return candidateMethod;
/*      */       }
/*      */     } 
/*      */     
/* 1029 */     throw new NoSuchMethodException("Can't find a public method for " + methodName + " " + ArrayUtils.toString(parameterTypes));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static String toCanonicalName(String className) {
/* 1041 */     className = StringUtils.deleteWhitespace(className);
/* 1042 */     if (className == null)
/* 1043 */       throw new NullPointerException("className must not be null."); 
/* 1044 */     if (className.endsWith("[]")) {
/* 1045 */       StringBuilder classNameBuffer = new StringBuilder();
/* 1046 */       while (className.endsWith("[]")) {
/* 1047 */         className = className.substring(0, className.length() - 2);
/* 1048 */         classNameBuffer.append("[");
/*      */       } 
/* 1050 */       String abbreviation = abbreviationMap.get(className);
/* 1051 */       if (abbreviation != null) {
/* 1052 */         classNameBuffer.append(abbreviation);
/*      */       } else {
/* 1054 */         classNameBuffer.append("L").append(className).append(";");
/*      */       } 
/* 1056 */       className = classNameBuffer.toString();
/*      */     } 
/* 1058 */     return className;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Class<?>[] toClass(Object... array) {
/* 1072 */     if (array == null)
/* 1073 */       return null; 
/* 1074 */     if (array.length == 0) {
/* 1075 */       return ArrayUtils.EMPTY_CLASS_ARRAY;
/*      */     }
/* 1077 */     Class<?>[] classes = new Class[array.length];
/* 1078 */     for (int i = 0; i < array.length; i++) {
/* 1079 */       classes[i] = (array[i] == null) ? null : array[i].getClass();
/*      */     }
/* 1081 */     return classes;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String getShortCanonicalName(Object object, String valueIfNull) {
/* 1095 */     if (object == null) {
/* 1096 */       return valueIfNull;
/*      */     }
/* 1098 */     return getShortCanonicalName(object.getClass().getName());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String getShortCanonicalName(Class<?> cls) {
/* 1109 */     if (cls == null) {
/* 1110 */       return "";
/*      */     }
/* 1112 */     return getShortCanonicalName(cls.getName());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String getShortCanonicalName(String canonicalName) {
/* 1125 */     return getShortClassName(getCanonicalName(canonicalName));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String getPackageCanonicalName(Object object, String valueIfNull) {
/* 1139 */     if (object == null) {
/* 1140 */       return valueIfNull;
/*      */     }
/* 1142 */     return getPackageCanonicalName(object.getClass().getName());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String getPackageCanonicalName(Class<?> cls) {
/* 1153 */     if (cls == null) {
/* 1154 */       return "";
/*      */     }
/* 1156 */     return getPackageCanonicalName(cls.getName());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String getPackageCanonicalName(String canonicalName) {
/* 1170 */     return getPackageName(getCanonicalName(canonicalName));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static String getCanonicalName(String className) {
/* 1190 */     className = StringUtils.deleteWhitespace(className);
/* 1191 */     if (className == null) {
/* 1192 */       return null;
/*      */     }
/* 1194 */     int dim = 0;
/* 1195 */     while (className.startsWith("[")) {
/* 1196 */       dim++;
/* 1197 */       className = className.substring(1);
/*      */     } 
/* 1199 */     if (dim < 1) {
/* 1200 */       return className;
/*      */     }
/* 1202 */     if (className.startsWith("L")) {
/* 1203 */       className = className.substring(1, className.endsWith(";") ? (className.length() - 1) : className.length());
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     }
/* 1209 */     else if (className.length() > 0) {
/* 1210 */       className = reverseAbbreviationMap.get(className.substring(0, 1));
/*      */     } 
/*      */     
/* 1213 */     StringBuilder canonicalClassNameBuffer = new StringBuilder(className);
/* 1214 */     for (int i = 0; i < dim; i++) {
/* 1215 */       canonicalClassNameBuffer.append("[]");
/*      */     }
/* 1217 */     return canonicalClassNameBuffer.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Iterable<Class<?>> hierarchy(Class<?> type) {
/* 1229 */     return hierarchy(type, Interfaces.EXCLUDE);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Iterable<Class<?>> hierarchy(final Class<?> type, Interfaces interfacesBehavior) {
/* 1241 */     final Iterable<Class<?>> classes = new Iterable<Class<?>>()
/*      */       {
/*      */         public Iterator<Class<?>> iterator()
/*      */         {
/* 1245 */           final MutableObject<Class<?>> next = new MutableObject(type);
/* 1246 */           return new Iterator<Class<?>>()
/*      */             {
/*      */               public boolean hasNext()
/*      */               {
/* 1250 */                 return (next.getValue() != null);
/*      */               }
/*      */ 
/*      */               
/*      */               public Class<?> next() {
/* 1255 */                 Class<?> result = (Class)next.getValue();
/* 1256 */                 next.setValue(result.getSuperclass());
/* 1257 */                 return result;
/*      */               }
/*      */ 
/*      */               
/*      */               public void remove() {
/* 1262 */                 throw new UnsupportedOperationException();
/*      */               }
/*      */             };
/*      */         }
/*      */       };
/*      */ 
/*      */     
/* 1269 */     if (interfacesBehavior != Interfaces.INCLUDE) {
/* 1270 */       return classes;
/*      */     }
/* 1272 */     return new Iterable<Class<?>>()
/*      */       {
/*      */         public Iterator<Class<?>> iterator()
/*      */         {
/* 1276 */           final Set<Class<?>> seenInterfaces = new HashSet<Class<?>>();
/* 1277 */           final Iterator<Class<?>> wrapped = classes.iterator();
/*      */           
/* 1279 */           return new Iterator<Class<?>>() {
/* 1280 */               Iterator<Class<?>> interfaces = Collections.<Class<?>>emptySet().iterator();
/*      */ 
/*      */               
/*      */               public boolean hasNext() {
/* 1284 */                 return (this.interfaces.hasNext() || wrapped.hasNext());
/*      */               }
/*      */ 
/*      */               
/*      */               public Class<?> next() {
/* 1289 */                 if (this.interfaces.hasNext()) {
/* 1290 */                   Class<?> nextInterface = this.interfaces.next();
/* 1291 */                   seenInterfaces.add(nextInterface);
/* 1292 */                   return nextInterface;
/*      */                 } 
/* 1294 */                 Class<?> nextSuperclass = wrapped.next();
/* 1295 */                 Set<Class<?>> currentInterfaces = new LinkedHashSet<Class<?>>();
/* 1296 */                 walkInterfaces(currentInterfaces, nextSuperclass);
/* 1297 */                 this.interfaces = currentInterfaces.iterator();
/* 1298 */                 return nextSuperclass;
/*      */               }
/*      */               
/*      */               private void walkInterfaces(Set<Class<?>> addTo, Class<?> c) {
/* 1302 */                 for (Class<?> iface : c.getInterfaces()) {
/* 1303 */                   if (!seenInterfaces.contains(iface)) {
/* 1304 */                     addTo.add(iface);
/*      */                   }
/* 1306 */                   walkInterfaces(addTo, iface);
/*      */                 } 
/*      */               }
/*      */ 
/*      */               
/*      */               public void remove() {
/* 1312 */                 throw new UnsupportedOperationException();
/*      */               }
/*      */             };
/*      */         }
/*      */       };
/*      */   }
/*      */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\commons\lang3\ClassUtils.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */