/*     */ package org.apache.commons.lang3.reflect;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.lang.reflect.Type;
/*     */ import java.lang.reflect.TypeVariable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.apache.commons.lang3.ArrayUtils;
/*     */ import org.apache.commons.lang3.ClassUtils;
/*     */ import org.apache.commons.lang3.Validate;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MethodUtils
/*     */ {
/*     */   public static Object invokeMethod(Object object, String methodName) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
/*  94 */     return invokeMethod(object, methodName, ArrayUtils.EMPTY_OBJECT_ARRAY, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Object invokeMethod(Object object, String methodName, Object... args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
/* 122 */     args = ArrayUtils.nullToEmpty(args);
/* 123 */     Class<?>[] parameterTypes = ClassUtils.toClass(args);
/* 124 */     return invokeMethod(object, methodName, args, parameterTypes);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Object invokeMethod(Object object, String methodName, Object[] args, Class<?>[] parameterTypes) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
/* 150 */     parameterTypes = ArrayUtils.nullToEmpty(parameterTypes);
/* 151 */     args = ArrayUtils.nullToEmpty(args);
/* 152 */     Method method = getMatchingAccessibleMethod(object.getClass(), methodName, parameterTypes);
/*     */     
/* 154 */     if (method == null) {
/* 155 */       throw new NoSuchMethodException("No such accessible method: " + methodName + "() on object: " + object.getClass().getName());
/*     */     }
/*     */ 
/*     */     
/* 159 */     return method.invoke(object, args);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Object invokeExactMethod(Object object, String methodName) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
/* 183 */     return invokeExactMethod(object, methodName, ArrayUtils.EMPTY_OBJECT_ARRAY, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Object invokeExactMethod(Object object, String methodName, Object... args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
/* 206 */     args = ArrayUtils.nullToEmpty(args);
/* 207 */     Class<?>[] parameterTypes = ClassUtils.toClass(args);
/* 208 */     return invokeExactMethod(object, methodName, args, parameterTypes);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Object invokeExactMethod(Object object, String methodName, Object[] args, Class<?>[] parameterTypes) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
/* 234 */     args = ArrayUtils.nullToEmpty(args);
/* 235 */     parameterTypes = ArrayUtils.nullToEmpty(parameterTypes);
/* 236 */     Method method = getAccessibleMethod(object.getClass(), methodName, parameterTypes);
/*     */     
/* 238 */     if (method == null) {
/* 239 */       throw new NoSuchMethodException("No such accessible method: " + methodName + "() on object: " + object.getClass().getName());
/*     */     }
/*     */ 
/*     */     
/* 243 */     return method.invoke(object, args);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Object invokeExactStaticMethod(Class<?> cls, String methodName, Object[] args, Class<?>[] parameterTypes) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
/* 269 */     args = ArrayUtils.nullToEmpty(args);
/* 270 */     parameterTypes = ArrayUtils.nullToEmpty(parameterTypes);
/* 271 */     Method method = getAccessibleMethod(cls, methodName, parameterTypes);
/* 272 */     if (method == null) {
/* 273 */       throw new NoSuchMethodException("No such accessible method: " + methodName + "() on class: " + cls.getName());
/*     */     }
/*     */     
/* 276 */     return method.invoke(null, args);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Object invokeStaticMethod(Class<?> cls, String methodName, Object... args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
/* 306 */     args = ArrayUtils.nullToEmpty(args);
/* 307 */     Class<?>[] parameterTypes = ClassUtils.toClass(args);
/* 308 */     return invokeStaticMethod(cls, methodName, args, parameterTypes);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Object invokeStaticMethod(Class<?> cls, String methodName, Object[] args, Class<?>[] parameterTypes) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
/* 337 */     args = ArrayUtils.nullToEmpty(args);
/* 338 */     parameterTypes = ArrayUtils.nullToEmpty(parameterTypes);
/* 339 */     Method method = getMatchingAccessibleMethod(cls, methodName, parameterTypes);
/*     */     
/* 341 */     if (method == null) {
/* 342 */       throw new NoSuchMethodException("No such accessible method: " + methodName + "() on class: " + cls.getName());
/*     */     }
/*     */     
/* 345 */     return method.invoke(null, args);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Object invokeExactStaticMethod(Class<?> cls, String methodName, Object... args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
/* 369 */     args = ArrayUtils.nullToEmpty(args);
/* 370 */     Class<?>[] parameterTypes = ClassUtils.toClass(args);
/* 371 */     return invokeExactStaticMethod(cls, methodName, args, parameterTypes);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Method getAccessibleMethod(Class<?> cls, String methodName, Class<?>... parameterTypes) {
/*     */     try {
/* 389 */       return getAccessibleMethod(cls.getMethod(methodName, parameterTypes));
/*     */     }
/* 391 */     catch (NoSuchMethodException e) {
/* 392 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Method getAccessibleMethod(Method method) {
/* 405 */     if (!MemberUtils.isAccessible(method)) {
/* 406 */       return null;
/*     */     }
/*     */     
/* 409 */     Class<?> cls = method.getDeclaringClass();
/* 410 */     if (Modifier.isPublic(cls.getModifiers())) {
/* 411 */       return method;
/*     */     }
/* 413 */     String methodName = method.getName();
/* 414 */     Class<?>[] parameterTypes = method.getParameterTypes();
/*     */ 
/*     */     
/* 417 */     method = getAccessibleMethodFromInterfaceNest(cls, methodName, parameterTypes);
/*     */ 
/*     */ 
/*     */     
/* 421 */     if (method == null) {
/* 422 */       method = getAccessibleMethodFromSuperclass(cls, methodName, parameterTypes);
/*     */     }
/*     */     
/* 425 */     return method;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Method getAccessibleMethodFromSuperclass(Class<?> cls, String methodName, Class<?>... parameterTypes) {
/* 440 */     Class<?> parentClass = cls.getSuperclass();
/* 441 */     while (parentClass != null) {
/* 442 */       if (Modifier.isPublic(parentClass.getModifiers())) {
/*     */         try {
/* 444 */           return parentClass.getMethod(methodName, parameterTypes);
/* 445 */         } catch (NoSuchMethodException e) {
/* 446 */           return null;
/*     */         } 
/*     */       }
/* 449 */       parentClass = parentClass.getSuperclass();
/*     */     } 
/* 451 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Method getAccessibleMethodFromInterfaceNest(Class<?> cls, String methodName, Class<?>... parameterTypes) {
/* 472 */     for (; cls != null; cls = cls.getSuperclass()) {
/*     */ 
/*     */       
/* 475 */       Class<?>[] interfaces = cls.getInterfaces();
/* 476 */       for (int i = 0; i < interfaces.length; i++) {
/*     */         
/* 478 */         if (Modifier.isPublic(interfaces[i].getModifiers()))
/*     */           
/*     */           try {
/*     */ 
/*     */             
/* 483 */             return interfaces[i].getDeclaredMethod(methodName, parameterTypes);
/*     */           }
/* 485 */           catch (NoSuchMethodException e) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 492 */             Method method = getAccessibleMethodFromInterfaceNest(interfaces[i], methodName, parameterTypes);
/*     */             
/* 494 */             if (method != null)
/* 495 */               return method; 
/*     */           }  
/*     */       } 
/*     */     } 
/* 499 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Method getMatchingAccessibleMethod(Class<?> cls, String methodName, Class<?>... parameterTypes) {
/*     */     try {
/* 527 */       Method method = cls.getMethod(methodName, parameterTypes);
/* 528 */       MemberUtils.setAccessibleWorkaround(method);
/* 529 */       return method;
/* 530 */     } catch (NoSuchMethodException e) {
/*     */ 
/*     */       
/* 533 */       Method bestMatch = null;
/* 534 */       Method[] methods = cls.getMethods();
/* 535 */       for (Method method : methods) {
/*     */         
/* 537 */         if (method.getName().equals(methodName) && ClassUtils.isAssignable(parameterTypes, method.getParameterTypes(), true)) {
/*     */           
/* 539 */           Method accessibleMethod = getAccessibleMethod(method);
/* 540 */           if (accessibleMethod != null && (bestMatch == null || MemberUtils.compareParameterTypes(accessibleMethod.getParameterTypes(), bestMatch.getParameterTypes(), parameterTypes) < 0))
/*     */           {
/*     */ 
/*     */             
/* 544 */             bestMatch = accessibleMethod;
/*     */           }
/*     */         } 
/*     */       } 
/* 548 */       if (bestMatch != null) {
/* 549 */         MemberUtils.setAccessibleWorkaround(bestMatch);
/*     */       }
/* 551 */       return bestMatch;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Set<Method> getOverrideHierarchy(Method method, ClassUtils.Interfaces interfacesBehavior) {
/* 563 */     Validate.notNull(method);
/* 564 */     Set<Method> result = new LinkedHashSet<Method>();
/* 565 */     result.add(method);
/*     */     
/* 567 */     Class<?>[] parameterTypes = method.getParameterTypes();
/*     */     
/* 569 */     Class<?> declaringClass = method.getDeclaringClass();
/*     */     
/* 571 */     Iterator<Class<?>> hierarchy = ClassUtils.hierarchy(declaringClass, interfacesBehavior).iterator();
/*     */     
/* 573 */     hierarchy.next();
/* 574 */     label21: while (hierarchy.hasNext()) {
/* 575 */       Class<?> c = hierarchy.next();
/* 576 */       Method m = getMatchingAccessibleMethod(c, method.getName(), parameterTypes);
/* 577 */       if (m == null) {
/*     */         continue;
/*     */       }
/* 580 */       if (Arrays.equals((Object[])m.getParameterTypes(), (Object[])parameterTypes)) {
/*     */         
/* 582 */         result.add(m);
/*     */         
/*     */         continue;
/*     */       } 
/* 586 */       Map<TypeVariable<?>, Type> typeArguments = TypeUtils.getTypeArguments(declaringClass, m.getDeclaringClass());
/* 587 */       for (int i = 0; i < parameterTypes.length; i++) {
/* 588 */         Type childType = TypeUtils.unrollVariables(typeArguments, method.getGenericParameterTypes()[i]);
/* 589 */         Type parentType = TypeUtils.unrollVariables(typeArguments, m.getGenericParameterTypes()[i]);
/* 590 */         if (!TypeUtils.equals(childType, parentType)) {
/*     */           continue label21;
/*     */         }
/*     */       } 
/* 594 */       result.add(m);
/*     */     } 
/* 596 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Method[] getMethodsWithAnnotation(Class<?> cls, Class<? extends Annotation> annotationCls) {
/* 611 */     List<Method> annotatedMethodsList = getMethodsListWithAnnotation(cls, annotationCls);
/* 612 */     return annotatedMethodsList.<Method>toArray(new Method[annotatedMethodsList.size()]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static List<Method> getMethodsListWithAnnotation(Class<?> cls, Class<? extends Annotation> annotationCls) {
/* 627 */     Validate.isTrue((cls != null), "The class must not be null", new Object[0]);
/* 628 */     Validate.isTrue((annotationCls != null), "The annotation class must not be null", new Object[0]);
/* 629 */     Method[] allMethods = cls.getMethods();
/* 630 */     List<Method> annotatedMethods = new ArrayList<Method>();
/* 631 */     for (Method method : allMethods) {
/* 632 */       if (method.getAnnotation(annotationCls) != null) {
/* 633 */         annotatedMethods.add(method);
/*     */       }
/*     */     } 
/* 636 */     return annotatedMethods;
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\commons\lang3\reflect\MethodUtils.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */