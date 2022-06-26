/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.util.concurrent.BlockingQueue;
/*     */ import java.util.concurrent.CountDownLatch;
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.Semaphore;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.TimeoutException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Beta
/*     */ public final class Uninterruptibles
/*     */ {
/*     */   public static void awaitUninterruptibly(CountDownLatch latch) {
/*  53 */     boolean interrupted = false;
/*     */     
/*     */     while (true) {
/*     */       try {
/*  57 */         latch.await();
/*     */         return;
/*  59 */       } catch (InterruptedException e) {
/*     */ 
/*     */       
/*     */       } finally {
/*     */         
/*  64 */         if (interrupted) {
/*  65 */           Thread.currentThread().interrupt();
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean awaitUninterruptibly(CountDownLatch latch, long timeout, TimeUnit unit) {
/*  77 */     boolean interrupted = false;
/*     */     try {
/*  79 */       long remainingNanos = unit.toNanos(timeout);
/*  80 */       long end = System.nanoTime() + remainingNanos;
/*     */ 
/*     */       
/*     */       while (true) {
/*     */         try {
/*  85 */           return latch.await(remainingNanos, TimeUnit.NANOSECONDS);
/*  86 */         } catch (InterruptedException e) {
/*  87 */           interrupted = true;
/*  88 */           remainingNanos = end - System.nanoTime();
/*     */         } 
/*     */       } 
/*     */     } finally {
/*  92 */       if (interrupted) {
/*  93 */         Thread.currentThread().interrupt();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void joinUninterruptibly(Thread toJoin) {
/* 102 */     boolean interrupted = false;
/*     */     
/*     */     while (true) {
/*     */       try {
/* 106 */         toJoin.join();
/*     */         return;
/* 108 */       } catch (InterruptedException e) {
/*     */ 
/*     */       
/*     */       } finally {
/*     */         
/* 113 */         if (interrupted) {
/* 114 */           Thread.currentThread().interrupt();
/*     */         }
/*     */       } 
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
/*     */   public static <V> V getUninterruptibly(Future<V> future) throws ExecutionException {
/* 133 */     boolean interrupted = false;
/*     */     
/*     */     while (true) {
/*     */       try {
/* 137 */         return future.get();
/* 138 */       } catch (InterruptedException e) {
/*     */ 
/*     */       
/*     */       } finally {
/*     */         
/* 143 */         if (interrupted) {
/* 144 */           Thread.currentThread().interrupt();
/*     */         }
/*     */       } 
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
/*     */   public static <V> V getUninterruptibly(Future<V> future, long timeout, TimeUnit unit) throws ExecutionException, TimeoutException {
/* 165 */     boolean interrupted = false;
/*     */     try {
/* 167 */       long remainingNanos = unit.toNanos(timeout);
/* 168 */       long end = System.nanoTime() + remainingNanos;
/*     */ 
/*     */       
/*     */       while (true) {
/*     */         try {
/* 173 */           return future.get(remainingNanos, TimeUnit.NANOSECONDS);
/* 174 */         } catch (InterruptedException e) {
/* 175 */           interrupted = true;
/* 176 */           remainingNanos = end - System.nanoTime();
/*     */         } 
/*     */       } 
/*     */     } finally {
/* 180 */       if (interrupted) {
/* 181 */         Thread.currentThread().interrupt();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void joinUninterruptibly(Thread toJoin, long timeout, TimeUnit unit) {
/* 193 */     Preconditions.checkNotNull(toJoin);
/* 194 */     boolean interrupted = false;
/*     */     try {
/* 196 */       long remainingNanos = unit.toNanos(timeout);
/* 197 */       long end = System.nanoTime() + remainingNanos;
/*     */       
/*     */       while (true) {
/*     */         try {
/* 201 */           TimeUnit.NANOSECONDS.timedJoin(toJoin, remainingNanos);
/*     */           return;
/* 203 */         } catch (InterruptedException e) {
/* 204 */           interrupted = true;
/* 205 */           remainingNanos = end - System.nanoTime();
/*     */         } 
/*     */       } 
/*     */     } finally {
/* 209 */       if (interrupted) {
/* 210 */         Thread.currentThread().interrupt();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> E takeUninterruptibly(BlockingQueue<E> queue) {
/* 219 */     boolean interrupted = false;
/*     */     
/*     */     while (true) {
/*     */       try {
/* 223 */         return queue.take();
/* 224 */       } catch (InterruptedException e) {
/*     */ 
/*     */       
/*     */       } finally {
/*     */         
/* 229 */         if (interrupted) {
/* 230 */           Thread.currentThread().interrupt();
/*     */         }
/*     */       } 
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
/*     */   public static <E> void putUninterruptibly(BlockingQueue<E> queue, E element) {
/* 245 */     boolean interrupted = false;
/*     */     
/*     */     while (true) {
/*     */       try {
/* 249 */         queue.put(element);
/*     */         return;
/* 251 */       } catch (InterruptedException e) {
/*     */ 
/*     */       
/*     */       } finally {
/*     */         
/* 256 */         if (interrupted) {
/* 257 */           Thread.currentThread().interrupt();
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void sleepUninterruptibly(long sleepFor, TimeUnit unit) {
/* 268 */     boolean interrupted = false;
/*     */     try {
/* 270 */       long remainingNanos = unit.toNanos(sleepFor);
/* 271 */       long end = System.nanoTime() + remainingNanos;
/*     */       
/*     */       while (true) {
/*     */         try {
/* 275 */           TimeUnit.NANOSECONDS.sleep(remainingNanos);
/*     */           return;
/* 277 */         } catch (InterruptedException e) {
/* 278 */           interrupted = true;
/* 279 */           remainingNanos = end - System.nanoTime();
/*     */         } 
/*     */       } 
/*     */     } finally {
/* 283 */       if (interrupted) {
/* 284 */         Thread.currentThread().interrupt();
/*     */       }
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
/*     */   public static boolean tryAcquireUninterruptibly(Semaphore semaphore, long timeout, TimeUnit unit) {
/* 297 */     return tryAcquireUninterruptibly(semaphore, 1, timeout, unit);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean tryAcquireUninterruptibly(Semaphore semaphore, int permits, long timeout, TimeUnit unit) {
/* 308 */     boolean interrupted = false;
/*     */     try {
/* 310 */       long remainingNanos = unit.toNanos(timeout);
/* 311 */       long end = System.nanoTime() + remainingNanos;
/*     */ 
/*     */       
/*     */       while (true) {
/*     */         try {
/* 316 */           return semaphore.tryAcquire(permits, remainingNanos, TimeUnit.NANOSECONDS);
/* 317 */         } catch (InterruptedException e) {
/* 318 */           interrupted = true;
/* 319 */           remainingNanos = end - System.nanoTime();
/*     */         } 
/*     */       } 
/*     */     } finally {
/* 323 */       if (interrupted)
/* 324 */         Thread.currentThread().interrupt(); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\commo\\util\concurrent\Uninterruptibles.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */