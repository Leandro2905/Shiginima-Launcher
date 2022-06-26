/*     */ package com.google.common.base;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
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
/*     */ @Beta
/*     */ @GwtCompatible(emulated = true)
/*     */ public final class Stopwatch
/*     */ {
/*     */   private final Ticker ticker;
/*     */   private boolean isRunning;
/*     */   private long elapsedNanos;
/*     */   private long startTick;
/*     */   
/*     */   public static Stopwatch createUnstarted() {
/*  89 */     return new Stopwatch();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Stopwatch createUnstarted(Ticker ticker) {
/*  99 */     return new Stopwatch(ticker);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Stopwatch createStarted() {
/* 109 */     return (new Stopwatch()).start();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Stopwatch createStarted(Ticker ticker) {
/* 119 */     return (new Stopwatch(ticker)).start();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   Stopwatch() {
/* 130 */     this(Ticker.systemTicker());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   Stopwatch(Ticker ticker) {
/* 141 */     this.ticker = Preconditions.<Ticker>checkNotNull(ticker, "ticker");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isRunning() {
/* 150 */     return this.isRunning;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Stopwatch start() {
/* 160 */     Preconditions.checkState(!this.isRunning, "This stopwatch is already running.");
/* 161 */     this.isRunning = true;
/* 162 */     this.startTick = this.ticker.read();
/* 163 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Stopwatch stop() {
/* 174 */     long tick = this.ticker.read();
/* 175 */     Preconditions.checkState(this.isRunning, "This stopwatch is already stopped.");
/* 176 */     this.isRunning = false;
/* 177 */     this.elapsedNanos += tick - this.startTick;
/* 178 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Stopwatch reset() {
/* 188 */     this.elapsedNanos = 0L;
/* 189 */     this.isRunning = false;
/* 190 */     return this;
/*     */   }
/*     */   
/*     */   private long elapsedNanos() {
/* 194 */     return this.isRunning ? (this.ticker.read() - this.startTick + this.elapsedNanos) : this.elapsedNanos;
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
/*     */   public long elapsed(TimeUnit desiredUnit) {
/* 208 */     return desiredUnit.convert(elapsedNanos(), TimeUnit.NANOSECONDS);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible("String.format()")
/*     */   public String toString() {
/* 216 */     long nanos = elapsedNanos();
/*     */     
/* 218 */     TimeUnit unit = chooseUnit(nanos);
/* 219 */     double value = nanos / TimeUnit.NANOSECONDS.convert(1L, unit);
/*     */ 
/*     */     
/* 222 */     return String.format("%.4g %s", new Object[] { Double.valueOf(value), abbreviate(unit) });
/*     */   }
/*     */   
/*     */   private static TimeUnit chooseUnit(long nanos) {
/* 226 */     if (TimeUnit.DAYS.convert(nanos, TimeUnit.NANOSECONDS) > 0L) {
/* 227 */       return TimeUnit.DAYS;
/*     */     }
/* 229 */     if (TimeUnit.HOURS.convert(nanos, TimeUnit.NANOSECONDS) > 0L) {
/* 230 */       return TimeUnit.HOURS;
/*     */     }
/* 232 */     if (TimeUnit.MINUTES.convert(nanos, TimeUnit.NANOSECONDS) > 0L) {
/* 233 */       return TimeUnit.MINUTES;
/*     */     }
/* 235 */     if (TimeUnit.SECONDS.convert(nanos, TimeUnit.NANOSECONDS) > 0L) {
/* 236 */       return TimeUnit.SECONDS;
/*     */     }
/* 238 */     if (TimeUnit.MILLISECONDS.convert(nanos, TimeUnit.NANOSECONDS) > 0L) {
/* 239 */       return TimeUnit.MILLISECONDS;
/*     */     }
/* 241 */     if (TimeUnit.MICROSECONDS.convert(nanos, TimeUnit.NANOSECONDS) > 0L) {
/* 242 */       return TimeUnit.MICROSECONDS;
/*     */     }
/* 244 */     return TimeUnit.NANOSECONDS;
/*     */   }
/*     */   
/*     */   private static String abbreviate(TimeUnit unit) {
/* 248 */     switch (unit) {
/*     */       case NANOSECONDS:
/* 250 */         return "ns";
/*     */       case MICROSECONDS:
/* 252 */         return "μs";
/*     */       case MILLISECONDS:
/* 254 */         return "ms";
/*     */       case SECONDS:
/* 256 */         return "s";
/*     */       case MINUTES:
/* 258 */         return "min";
/*     */       case HOURS:
/* 260 */         return "h";
/*     */       case DAYS:
/* 262 */         return "d";
/*     */     } 
/* 264 */     throw new AssertionError();
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\base\Stopwatch.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */