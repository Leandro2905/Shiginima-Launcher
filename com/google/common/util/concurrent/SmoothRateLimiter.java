/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import java.util.concurrent.TimeUnit;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ abstract class SmoothRateLimiter
/*     */   extends RateLimiter
/*     */ {
/*     */   double storedPermits;
/*     */   double maxPermits;
/*     */   double stableIntervalMicros;
/*     */   
/*     */   static final class SmoothWarmingUp
/*     */     extends SmoothRateLimiter
/*     */   {
/*     */     private final long warmupPeriodMicros;
/*     */     private double slope;
/*     */     private double halfPermits;
/*     */     
/*     */     SmoothWarmingUp(RateLimiter.SleepingStopwatch stopwatch, long warmupPeriod, TimeUnit timeUnit) {
/* 231 */       super(stopwatch);
/* 232 */       this.warmupPeriodMicros = timeUnit.toMicros(warmupPeriod);
/*     */     }
/*     */ 
/*     */     
/*     */     void doSetRate(double permitsPerSecond, double stableIntervalMicros) {
/* 237 */       double oldMaxPermits = this.maxPermits;
/* 238 */       this.maxPermits = this.warmupPeriodMicros / stableIntervalMicros;
/* 239 */       this.halfPermits = this.maxPermits / 2.0D;
/*     */       
/* 241 */       double coldIntervalMicros = stableIntervalMicros * 3.0D;
/* 242 */       this.slope = (coldIntervalMicros - stableIntervalMicros) / this.halfPermits;
/* 243 */       if (oldMaxPermits == Double.POSITIVE_INFINITY) {
/*     */         
/* 245 */         this.storedPermits = 0.0D;
/*     */       } else {
/* 247 */         this.storedPermits = (oldMaxPermits == 0.0D) ? this.maxPermits : (this.storedPermits * this.maxPermits / oldMaxPermits);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     long storedPermitsToWaitTime(double storedPermits, double permitsToTake) {
/* 255 */       double availablePermitsAboveHalf = storedPermits - this.halfPermits;
/* 256 */       long micros = 0L;
/*     */       
/* 258 */       if (availablePermitsAboveHalf > 0.0D) {
/* 259 */         double permitsAboveHalfToTake = Math.min(availablePermitsAboveHalf, permitsToTake);
/* 260 */         micros = (long)(permitsAboveHalfToTake * (permitsToTime(availablePermitsAboveHalf) + permitsToTime(availablePermitsAboveHalf - permitsAboveHalfToTake)) / 2.0D);
/*     */         
/* 262 */         permitsToTake -= permitsAboveHalfToTake;
/*     */       } 
/*     */       
/* 265 */       micros = (long)(micros + this.stableIntervalMicros * permitsToTake);
/* 266 */       return micros;
/*     */     }
/*     */     
/*     */     private double permitsToTime(double permits) {
/* 270 */       return this.stableIntervalMicros + permits * this.slope;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static final class SmoothBursty
/*     */     extends SmoothRateLimiter
/*     */   {
/*     */     final double maxBurstSeconds;
/*     */ 
/*     */ 
/*     */     
/*     */     SmoothBursty(RateLimiter.SleepingStopwatch stopwatch, double maxBurstSeconds) {
/* 285 */       super(stopwatch);
/* 286 */       this.maxBurstSeconds = maxBurstSeconds;
/*     */     }
/*     */ 
/*     */     
/*     */     void doSetRate(double permitsPerSecond, double stableIntervalMicros) {
/* 291 */       double oldMaxPermits = this.maxPermits;
/* 292 */       this.maxPermits = this.maxBurstSeconds * permitsPerSecond;
/* 293 */       if (oldMaxPermits == Double.POSITIVE_INFINITY) {
/*     */         
/* 295 */         this.storedPermits = this.maxPermits;
/*     */       } else {
/* 297 */         this.storedPermits = (oldMaxPermits == 0.0D) ? 0.0D : (this.storedPermits * this.maxPermits / oldMaxPermits);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     long storedPermitsToWaitTime(double storedPermits, double permitsToTake) {
/* 305 */       return 0L;
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
/* 330 */   private long nextFreeTicketMicros = 0L;
/*     */   
/*     */   private SmoothRateLimiter(RateLimiter.SleepingStopwatch stopwatch) {
/* 333 */     super(stopwatch);
/*     */   }
/*     */ 
/*     */   
/*     */   final void doSetRate(double permitsPerSecond, long nowMicros) {
/* 338 */     resync(nowMicros);
/* 339 */     double stableIntervalMicros = TimeUnit.SECONDS.toMicros(1L) / permitsPerSecond;
/* 340 */     this.stableIntervalMicros = stableIntervalMicros;
/* 341 */     doSetRate(permitsPerSecond, stableIntervalMicros);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final double doGetRate() {
/* 348 */     return TimeUnit.SECONDS.toMicros(1L) / this.stableIntervalMicros;
/*     */   }
/*     */ 
/*     */   
/*     */   final long queryEarliestAvailable(long nowMicros) {
/* 353 */     return this.nextFreeTicketMicros;
/*     */   }
/*     */ 
/*     */   
/*     */   final long reserveEarliestAvailable(int requiredPermits, long nowMicros) {
/* 358 */     resync(nowMicros);
/* 359 */     long returnValue = this.nextFreeTicketMicros;
/* 360 */     double storedPermitsToSpend = Math.min(requiredPermits, this.storedPermits);
/* 361 */     double freshPermits = requiredPermits - storedPermitsToSpend;
/*     */     
/* 363 */     long waitMicros = storedPermitsToWaitTime(this.storedPermits, storedPermitsToSpend) + (long)(freshPermits * this.stableIntervalMicros);
/*     */ 
/*     */     
/* 366 */     this.nextFreeTicketMicros += waitMicros;
/* 367 */     this.storedPermits -= storedPermitsToSpend;
/* 368 */     return returnValue;
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
/*     */   private void resync(long nowMicros) {
/* 383 */     if (nowMicros > this.nextFreeTicketMicros) {
/* 384 */       this.storedPermits = Math.min(this.maxPermits, this.storedPermits + (nowMicros - this.nextFreeTicketMicros) / this.stableIntervalMicros);
/*     */       
/* 386 */       this.nextFreeTicketMicros = nowMicros;
/*     */     } 
/*     */   }
/*     */   
/*     */   abstract void doSetRate(double paramDouble1, double paramDouble2);
/*     */   
/*     */   abstract long storedPermitsToWaitTime(double paramDouble1, double paramDouble2);
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\commo\\util\concurrent\SmoothRateLimiter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */