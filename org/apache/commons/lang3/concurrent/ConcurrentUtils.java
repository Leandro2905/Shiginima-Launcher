/*     */ package org.apache.commons.lang3.concurrent;
/*     */ 
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.TimeUnit;
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
/*     */ public class ConcurrentUtils
/*     */ {
/*     */   public static ConcurrentException extractCause(ExecutionException ex) {
/*  63 */     if (ex == null || ex.getCause() == null) {
/*  64 */       return null;
/*     */     }
/*     */     
/*  67 */     throwCause(ex);
/*  68 */     return new ConcurrentException(ex.getMessage(), ex.getCause());
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
/*     */   public static ConcurrentRuntimeException extractCauseUnchecked(ExecutionException ex) {
/*  85 */     if (ex == null || ex.getCause() == null) {
/*  86 */       return null;
/*     */     }
/*     */     
/*  89 */     throwCause(ex);
/*  90 */     return new ConcurrentRuntimeException(ex.getMessage(), ex.getCause());
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
/*     */   public static void handleCause(ExecutionException ex) throws ConcurrentException {
/* 108 */     ConcurrentException cex = extractCause(ex);
/*     */     
/* 110 */     if (cex != null) {
/* 111 */       throw cex;
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
/*     */ 
/*     */   
/*     */   public static void handleCauseUnchecked(ExecutionException ex) {
/* 129 */     ConcurrentRuntimeException crex = extractCauseUnchecked(ex);
/*     */     
/* 131 */     if (crex != null) {
/* 132 */       throw crex;
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
/*     */   static Throwable checkedException(Throwable ex) {
/* 146 */     Validate.isTrue((ex != null && !(ex instanceof RuntimeException) && !(ex instanceof Error)), "Not a checked exception: " + ex, new Object[0]);
/*     */ 
/*     */     
/* 149 */     return ex;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void throwCause(ExecutionException ex) {
/* 159 */     if (ex.getCause() instanceof RuntimeException) {
/* 160 */       throw (RuntimeException)ex.getCause();
/*     */     }
/*     */     
/* 163 */     if (ex.getCause() instanceof Error) {
/* 164 */       throw (Error)ex.getCause();
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> T initialize(ConcurrentInitializer<T> initializer) throws ConcurrentException {
/* 184 */     return (initializer != null) ? initializer.get() : null;
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
/*     */   public static <T> T initializeUnchecked(ConcurrentInitializer<T> initializer) {
/*     */     try {
/* 202 */       return initialize(initializer);
/* 203 */     } catch (ConcurrentException cex) {
/* 204 */       throw new ConcurrentRuntimeException(cex.getCause());
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> V putIfAbsent(ConcurrentMap<K, V> map, K key, V value) {
/* 244 */     if (map == null) {
/* 245 */       return null;
/*     */     }
/*     */     
/* 248 */     V result = map.putIfAbsent(key, value);
/* 249 */     return (result != null) ? result : value;
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
/*     */   public static <K, V> V createIfAbsent(ConcurrentMap<K, V> map, K key, ConcurrentInitializer<V> init) throws ConcurrentException {
/* 274 */     if (map == null || init == null) {
/* 275 */       return null;
/*     */     }
/*     */     
/* 278 */     V value = map.get(key);
/* 279 */     if (value == null) {
/* 280 */       return putIfAbsent(map, key, init.get());
/*     */     }
/* 282 */     return value;
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
/*     */   public static <K, V> V createIfAbsentUnchecked(ConcurrentMap<K, V> map, K key, ConcurrentInitializer<V> init) {
/*     */     try {
/* 303 */       return createIfAbsent(map, key, init);
/* 304 */     } catch (ConcurrentException cex) {
/* 305 */       throw new ConcurrentRuntimeException(cex.getCause());
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> Future<T> constantFuture(T value) {
/* 326 */     return new ConstantFuture<T>(value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static final class ConstantFuture<T>
/*     */     implements Future<T>
/*     */   {
/*     */     private final T value;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     ConstantFuture(T value) {
/* 344 */       this.value = value;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean isDone() {
/* 354 */       return true;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public T get() {
/* 362 */       return this.value;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public T get(long timeout, TimeUnit unit) {
/* 371 */       return this.value;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean isCancelled() {
/* 380 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean cancel(boolean mayInterruptIfRunning) {
/* 389 */       return false;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\commons\lang3\concurrent\ConcurrentUtils.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */