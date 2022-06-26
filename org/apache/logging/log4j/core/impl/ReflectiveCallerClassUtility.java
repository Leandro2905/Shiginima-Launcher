/*     */ package org.apache.logging.log4j.core.impl;
/*     */ 
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.core.util.Loader;
/*     */ import org.apache.logging.log4j.status.StatusLogger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ReflectiveCallerClassUtility
/*     */ {
/*  63 */   private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*     */   
/*     */   private static final boolean GET_CALLER_CLASS_SUPPORTED;
/*     */   
/*     */   private static final Method GET_CALLER_CLASS_METHOD;
/*     */   
/*     */   static final int JAVA_7U25_COMPENSATION_OFFSET;
/*     */   
/*     */   static {
/*  72 */     Method getCallerClass = null;
/*  73 */     int java7u25CompensationOffset = 0;
/*     */     
/*     */     try {
/*  76 */       ClassLoader loader = Loader.getClassLoader();
/*     */       
/*  78 */       Class<?> clazz = loader.loadClass("sun.reflect.Reflection");
/*  79 */       Method[] methods = clazz.getMethods();
/*  80 */       for (Method method : methods) {
/*  81 */         int modifier = method.getModifiers();
/*  82 */         Class<?>[] parameterTypes = method.getParameterTypes();
/*  83 */         if (method.getName().equals("getCallerClass") && Modifier.isStatic(modifier) && parameterTypes.length == 1 && parameterTypes[0] == int.class) {
/*     */           
/*  85 */           getCallerClass = method;
/*     */           
/*     */           break;
/*     */         } 
/*     */       } 
/*  90 */       if (getCallerClass == null) {
/*  91 */         LOGGER.info("sun.reflect.Reflection#getCallerClass does not exist.");
/*     */       } else {
/*  93 */         Object o = getCallerClass.invoke(null, new Object[] { Integer.valueOf(0) });
/*  94 */         if (o == null || o != clazz) {
/*  95 */           getCallerClass = null;
/*  96 */           LOGGER.warn("sun.reflect.Reflection#getCallerClass returned unexpected value of [{}] and is unusable. Will fall back to another option.", new Object[] { o });
/*     */         } else {
/*     */           
/*  99 */           o = getCallerClass.invoke(null, new Object[] { Integer.valueOf(1) });
/* 100 */           if (o == clazz) {
/* 101 */             java7u25CompensationOffset = 1;
/* 102 */             LOGGER.warn("sun.reflect.Reflection#getCallerClass is broken in Java 7u25. You should upgrade to 7u40. Using alternate stack offset to compensate.");
/*     */           }
/*     */         
/*     */         } 
/*     */       } 
/* 107 */     } catch (ClassNotFoundException e) {
/* 108 */       LOGGER.info("sun.reflect.Reflection is not installed.");
/* 109 */     } catch (IllegalAccessException e) {
/* 110 */       LOGGER.info("sun.reflect.Reflection#getCallerClass is not accessible.");
/* 111 */     } catch (InvocationTargetException e) {
/* 112 */       LOGGER.info("sun.reflect.Reflection#getCallerClass is not supported.");
/*     */     } 
/*     */     
/* 115 */     if (getCallerClass == null) {
/* 116 */       GET_CALLER_CLASS_SUPPORTED = false;
/* 117 */       GET_CALLER_CLASS_METHOD = null;
/* 118 */       JAVA_7U25_COMPENSATION_OFFSET = -1;
/*     */     } else {
/* 120 */       GET_CALLER_CLASS_SUPPORTED = true;
/* 121 */       GET_CALLER_CLASS_METHOD = getCallerClass;
/* 122 */       JAVA_7U25_COMPENSATION_OFFSET = java7u25CompensationOffset;
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
/*     */   public static boolean isSupported() {
/* 137 */     return GET_CALLER_CLASS_SUPPORTED;
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
/*     */   public static Class<?> getCaller(int depth) {
/* 149 */     if (!GET_CALLER_CLASS_SUPPORTED) {
/* 150 */       return null;
/*     */     }
/*     */     
/*     */     try {
/* 154 */       return (Class)GET_CALLER_CLASS_METHOD.invoke(null, new Object[] { Integer.valueOf(depth + 1 + JAVA_7U25_COMPENSATION_OFFSET) });
/* 155 */     } catch (IllegalAccessException ignore) {
/* 156 */       LOGGER.warn("Should not have failed to call getCallerClass.");
/* 157 */     } catch (InvocationTargetException ignore) {
/* 158 */       LOGGER.warn("Should not have failed to call getCallerClass.");
/*     */     } 
/* 160 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\impl\ReflectiveCallerClassUtility.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */