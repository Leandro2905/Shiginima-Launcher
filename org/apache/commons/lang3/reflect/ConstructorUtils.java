/*     */ package org.apache.commons.lang3.reflect;
/*     */ 
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Modifier;
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
/*     */ public class ConstructorUtils
/*     */ {
/*     */   public static <T> T invokeConstructor(Class<T> cls, Object... args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
/*  83 */     args = ArrayUtils.nullToEmpty(args);
/*  84 */     Class<?>[] parameterTypes = ClassUtils.toClass(args);
/*  85 */     return invokeConstructor(cls, args, parameterTypes);
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
/*     */   public static <T> T invokeConstructor(Class<T> cls, Object[] args, Class<?>[] parameterTypes) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
/* 111 */     args = ArrayUtils.nullToEmpty(args);
/* 112 */     parameterTypes = ArrayUtils.nullToEmpty(parameterTypes);
/* 113 */     Constructor<T> ctor = getMatchingAccessibleConstructor(cls, parameterTypes);
/* 114 */     if (ctor == null) {
/* 115 */       throw new NoSuchMethodException("No such accessible constructor on object: " + cls.getName());
/*     */     }
/*     */     
/* 118 */     return ctor.newInstance(args);
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
/*     */   public static <T> T invokeExactConstructor(Class<T> cls, Object... args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
/* 143 */     args = ArrayUtils.nullToEmpty(args);
/* 144 */     Class<?>[] parameterTypes = ClassUtils.toClass(args);
/* 145 */     return invokeExactConstructor(cls, args, parameterTypes);
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
/*     */   public static <T> T invokeExactConstructor(Class<T> cls, Object[] args, Class<?>[] parameterTypes) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
/* 171 */     args = ArrayUtils.nullToEmpty(args);
/* 172 */     parameterTypes = ArrayUtils.nullToEmpty(parameterTypes);
/* 173 */     Constructor<T> ctor = getAccessibleConstructor(cls, parameterTypes);
/* 174 */     if (ctor == null) {
/* 175 */       throw new NoSuchMethodException("No such accessible constructor on object: " + cls.getName());
/*     */     }
/*     */     
/* 178 */     return ctor.newInstance(args);
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
/*     */   public static <T> Constructor<T> getAccessibleConstructor(Class<T> cls, Class<?>... parameterTypes) {
/* 198 */     Validate.notNull(cls, "class cannot be null", new Object[0]);
/*     */     try {
/* 200 */       return getAccessibleConstructor(cls.getConstructor(parameterTypes));
/* 201 */     } catch (NoSuchMethodException e) {
/* 202 */       return null;
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
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> Constructor<T> getAccessibleConstructor(Constructor<T> ctor) {
/* 218 */     Validate.notNull(ctor, "constructor cannot be null", new Object[0]);
/* 219 */     return (MemberUtils.isAccessible(ctor) && isAccessible(ctor.getDeclaringClass())) ? ctor : null;
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
/*     */   public static <T> Constructor<T> getMatchingAccessibleConstructor(Class<T> cls, Class<?>... parameterTypes) {
/* 243 */     Validate.notNull(cls, "class cannot be null", new Object[0]);
/*     */ 
/*     */     
/*     */     try {
/* 247 */       Constructor<T> ctor = cls.getConstructor(parameterTypes);
/* 248 */       MemberUtils.setAccessibleWorkaround(ctor);
/* 249 */       return ctor;
/* 250 */     } catch (NoSuchMethodException e) {
/*     */       
/* 252 */       Constructor<T> result = null;
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 257 */       Constructor[] arrayOfConstructor = (Constructor[])cls.getConstructors();
/*     */ 
/*     */       
/* 260 */       for (Constructor<?> ctor : arrayOfConstructor) {
/*     */         
/* 262 */         if (ClassUtils.isAssignable(parameterTypes, ctor.getParameterTypes(), true)) {
/*     */           
/* 264 */           ctor = getAccessibleConstructor(ctor);
/* 265 */           if (ctor != null) {
/* 266 */             MemberUtils.setAccessibleWorkaround(ctor);
/* 267 */             if (result == null || MemberUtils.compareParameterTypes(ctor.getParameterTypes(), result.getParameterTypes(), parameterTypes) < 0) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */               
/* 273 */               Constructor<T> constructor = (Constructor)ctor;
/* 274 */               result = constructor;
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/* 279 */       return result;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean isAccessible(Class<?> type) {
/* 290 */     Class<?> cls = type;
/* 291 */     while (cls != null) {
/* 292 */       if (!Modifier.isPublic(cls.getModifiers())) {
/* 293 */         return false;
/*     */       }
/* 295 */       cls = cls.getEnclosingClass();
/*     */     } 
/* 297 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\commons\lang3\reflect\ConstructorUtils.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */