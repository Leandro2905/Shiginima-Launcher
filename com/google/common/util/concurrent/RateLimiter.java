/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Stopwatch;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import javax.annotation.concurrent.ThreadSafe;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @ThreadSafe
/*     */ @Beta
/*     */ public abstract class RateLimiter
/*     */ {
/*     */   private final SleepingStopwatch stopwatch;
/*     */   private volatile Object mutexDoNotUseDirectly;
/*     */   
/*     */   public static RateLimiter create(double permitsPerSecond) {
/* 129 */     return create(SleepingStopwatch.createFromSystemTimer(), permitsPerSecond);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   static RateLimiter create(SleepingStopwatch stopwatch, double permitsPerSecond) {
/* 138 */     RateLimiter rateLimiter = new SmoothRateLimiter.SmoothBursty(stopwatch, 1.0D);
/* 139 */     rateLimiter.setRate(permitsPerSecond);
/* 140 */     return rateLimiter;
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
/*     */   public static RateLimiter create(double permitsPerSecond, long warmupPeriod, TimeUnit unit) {
/* 168 */     Preconditions.checkArgument((warmupPeriod >= 0L), "warmupPeriod must not be negative: %s", new Object[] { Long.valueOf(warmupPeriod) });
/* 169 */     return create(SleepingStopwatch.createFromSystemTimer(), permitsPerSecond, warmupPeriod, unit);
/*     */   }
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   static RateLimiter create(SleepingStopwatch stopwatch, double permitsPerSecond, long warmupPeriod, TimeUnit unit) {
/* 175 */     RateLimiter rateLimiter = new SmoothRateLimiter.SmoothWarmingUp(stopwatch, warmupPeriod, unit);
/* 176 */     rateLimiter.setRate(permitsPerSecond);
/* 177 */     return rateLimiter;
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
/*     */   private Object mutex() {
/* 190 */     Object mutex = this.mutexDoNotUseDirectly;
/* 191 */     if (mutex == null) {
/* 192 */       synchronized (this) {
/* 193 */         mutex = this.mutexDoNotUseDirectly;
/* 194 */         if (mutex == null) {
/* 195 */           this.mutexDoNotUseDirectly = mutex = new Object();
/*     */         }
/*     */       } 
/*     */     }
/* 199 */     return mutex;
/*     */   }
/*     */   
/*     */   RateLimiter(SleepingStopwatch stopwatch) {
/* 203 */     this.stopwatch = (SleepingStopwatch)Preconditions.checkNotNull(stopwatch);
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
/*     */   public final void setRate(double permitsPerSecond) {
/* 226 */     Preconditions.checkArgument((permitsPerSecond > 0.0D && !Double.isNaN(permitsPerSecond)), "rate must be positive");
/*     */     
/* 228 */     synchronized (mutex()) {
/* 229 */       doSetRate(permitsPerSecond, this.stopwatch.readMicros());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   abstract void doSetRate(double paramDouble, long paramLong);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final double getRate() {
/* 243 */     synchronized (mutex()) {
/* 244 */       return doGetRate();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   abstract double doGetRate();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double acquire() {
/* 260 */     return acquire(1);
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
/*     */   public double acquire(int permits) {
/* 273 */     long microsToWait = reserve(permits);
/* 274 */     this.stopwatch.sleepMicrosUninterruptibly(microsToWait);
/* 275 */     return 1.0D * microsToWait / TimeUnit.SECONDS.toMicros(1L);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final long reserve(int permits) {
/* 285 */     checkPermits(permits);
/* 286 */     synchronized (mutex()) {
/* 287 */       return reserveAndGetWaitLength(permits, this.stopwatch.readMicros());
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
/*     */   public boolean tryAcquire(long timeout, TimeUnit unit) {
/* 305 */     return tryAcquire(1, timeout, unit);
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
/*     */   public boolean tryAcquire(int permits) {
/* 320 */     return tryAcquire(permits, 0L, TimeUnit.MICROSECONDS);
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
/*     */   public boolean tryAcquire() {
/* 334 */     return tryAcquire(1, 0L, TimeUnit.MICROSECONDS);
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
/*     */   public boolean tryAcquire(int permits, long timeout, TimeUnit unit) {
/* 350 */     long microsToWait, timeoutMicros = Math.max(unit.toMicros(timeout), 0L);
/* 351 */     checkPermits(permits);
/*     */     
/* 353 */     synchronized (mutex()) {
/* 354 */       long nowMicros = this.stopwatch.readMicros();
/* 355 */       if (!canAcquire(nowMicros, timeoutMicros)) {
/* 356 */         return false;
/*     */       }
/* 358 */       microsToWait = reserveAndGetWaitLength(permits, nowMicros);
/*     */     } 
/*     */     
/* 361 */     this.stopwatch.sleepMicrosUninterruptibly(microsToWait);
/* 362 */     return true;
/*     */   }
/*     */   
/*     */   private boolean canAcquire(long nowMicros, long timeoutMicros) {
/* 366 */     return (queryEarliestAvailable(nowMicros) - timeoutMicros <= nowMicros);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final long reserveAndGetWaitLength(int permits, long nowMicros) {
/* 375 */     long momentAvailable = reserveEarliestAvailable(permits, nowMicros);
/* 376 */     return Math.max(momentAvailable - nowMicros, 0L);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   abstract long queryEarliestAvailable(long paramLong);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   abstract long reserveEarliestAvailable(int paramInt, long paramLong);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 398 */     return String.format("RateLimiter[stableRate=%3.1fqps]", new Object[] { Double.valueOf(getRate()) });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   static abstract class SleepingStopwatch
/*     */   {
/*     */     abstract long readMicros();
/*     */ 
/*     */     
/*     */     abstract void sleepMicrosUninterruptibly(long param1Long);
/*     */ 
/*     */     
/*     */     static final SleepingStopwatch createFromSystemTimer() {
/* 413 */       return new SleepingStopwatch() {
/* 414 */           final Stopwatch stopwatch = Stopwatch.createStarted();
/*     */ 
/*     */           
/*     */           long readMicros() {
/* 418 */             return this.stopwatch.elapsed(TimeUnit.MICROSECONDS);
/*     */           }
/*     */ 
/*     */           
/*     */           void sleepMicrosUninterruptibly(long micros) {
/* 423 */             if (micros > 0L) {
/* 424 */               Uninterruptibles.sleepUninterruptibly(micros, TimeUnit.MICROSECONDS);
/*     */             }
/*     */           }
/*     */         };
/*     */     }
/*     */   }
/*     */   
/*     */   private static int checkPermits(int permits) {
/* 432 */     Preconditions.checkArgument((permits > 0), "Requested permits (%s) must be positive", new Object[] { Integer.valueOf(permits) });
/* 433 */     return permits;
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\commo\\util\concurrent\RateLimiter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */