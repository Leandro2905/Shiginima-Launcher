/*     */ package com.google.gson.internal;
/*     */ 
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectStreamClass;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Method;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class UnsafeAllocator
/*     */ {
/*     */   public abstract <T> T newInstance(Class<T> paramClass) throws Exception;
/*     */   
/*     */   public static UnsafeAllocator create() {
/*     */     try {
/*  39 */       Class<?> unsafeClass = Class.forName("sun.misc.Unsafe");
/*  40 */       Field f = unsafeClass.getDeclaredField("theUnsafe");
/*  41 */       f.setAccessible(true);
/*  42 */       final Object unsafe = f.get((Object)null);
/*  43 */       final Method allocateInstance = unsafeClass.getMethod("allocateInstance", new Class[] { Class.class });
/*  44 */       return new UnsafeAllocator()
/*     */         {
/*     */           public <T> T newInstance(Class<T> c) throws Exception
/*     */           {
/*  48 */             return (T)allocateInstance.invoke(unsafe, new Object[] { c });
/*     */           }
/*     */         };
/*  51 */     } catch (Exception ignored) {
/*     */ 
/*     */       
/*     */       try {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  60 */         final Method newInstance = ObjectInputStream.class.getDeclaredMethod("newInstance", new Class[] { Class.class, Class.class });
/*     */         
/*  62 */         newInstance.setAccessible(true);
/*  63 */         return new UnsafeAllocator()
/*     */           {
/*     */             public <T> T newInstance(Class<T> c) throws Exception
/*     */             {
/*  67 */               return (T)newInstance.invoke((Object)null, new Object[] { c, Object.class });
/*     */             }
/*     */           };
/*  70 */       } catch (Exception exception) {
/*     */ 
/*     */         
/*     */         try {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*  79 */           Method getConstructorId = ObjectStreamClass.class.getDeclaredMethod("getConstructorId", new Class[] { Class.class });
/*     */           
/*  81 */           getConstructorId.setAccessible(true);
/*  82 */           final int constructorId = ((Integer)getConstructorId.invoke((Object)null, new Object[] { Object.class })).intValue();
/*  83 */           final Method newInstance = ObjectStreamClass.class.getDeclaredMethod("newInstance", new Class[] { Class.class, int.class });
/*     */           
/*  85 */           newInstance.setAccessible(true);
/*  86 */           return new UnsafeAllocator()
/*     */             {
/*     */               public <T> T newInstance(Class<T> c) throws Exception
/*     */               {
/*  90 */                 return (T)newInstance.invoke((Object)null, new Object[] { c, Integer.valueOf(this.val$constructorId) });
/*     */               }
/*     */             };
/*  93 */         } catch (Exception exception1) {
/*     */ 
/*     */ 
/*     */           
/*  97 */           return new UnsafeAllocator()
/*     */             {
/*     */               public <T> T newInstance(Class<T> c) {
/* 100 */                 throw new UnsupportedOperationException("Cannot allocate " + c);
/*     */               }
/*     */             };
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\gson\internal\UnsafeAllocator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */