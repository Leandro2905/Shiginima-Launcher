/*     */ package org.apache.commons.lang3.time;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class StopWatch
/*     */ {
/*     */   private static final long NANO_2_MILLIS = 1000000L;
/*     */   
/*     */   private enum State
/*     */   {
/*  67 */     UNSTARTED {
/*  68 */       boolean isStarted() { return false; }
/*  69 */       boolean isStopped() { return true; } boolean isSuspended() {
/*  70 */         return false;
/*     */       } },
/*  72 */     RUNNING {
/*  73 */       boolean isStarted() { return true; }
/*  74 */       boolean isStopped() { return false; } boolean isSuspended() {
/*  75 */         return false;
/*     */       } },
/*  77 */     STOPPED {
/*  78 */       boolean isStarted() { return false; }
/*  79 */       boolean isStopped() { return true; } boolean isSuspended() {
/*  80 */         return false;
/*     */       } },
/*  82 */     SUSPENDED {
/*  83 */       boolean isStarted() { return true; }
/*  84 */       boolean isStopped() { return false; } boolean isSuspended() {
/*  85 */         return true;
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     abstract boolean isStarted();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     abstract boolean isStopped();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     abstract boolean isSuspended();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private enum SplitState
/*     */   {
/* 126 */     SPLIT,
/* 127 */     UNSPLIT;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 132 */   private State runningState = State.UNSTARTED;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 137 */   private SplitState splitState = SplitState.UNSPLIT;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private long startTime;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private long startTimeMillis;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private long stopTime;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void start() {
/* 178 */     if (this.runningState == State.STOPPED) {
/* 179 */       throw new IllegalStateException("Stopwatch must be reset before being restarted. ");
/*     */     }
/* 181 */     if (this.runningState != State.UNSTARTED) {
/* 182 */       throw new IllegalStateException("Stopwatch already started. ");
/*     */     }
/* 184 */     this.startTime = System.nanoTime();
/* 185 */     this.startTimeMillis = System.currentTimeMillis();
/* 186 */     this.runningState = State.RUNNING;
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
/*     */   public void stop() {
/* 203 */     if (this.runningState != State.RUNNING && this.runningState != State.SUSPENDED) {
/* 204 */       throw new IllegalStateException("Stopwatch is not running. ");
/*     */     }
/* 206 */     if (this.runningState == State.RUNNING) {
/* 207 */       this.stopTime = System.nanoTime();
/*     */     }
/* 209 */     this.runningState = State.STOPPED;
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
/*     */   public void reset() {
/* 222 */     this.runningState = State.UNSTARTED;
/* 223 */     this.splitState = SplitState.UNSPLIT;
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
/*     */   public void split() {
/* 240 */     if (this.runningState != State.RUNNING) {
/* 241 */       throw new IllegalStateException("Stopwatch is not running. ");
/*     */     }
/* 243 */     this.stopTime = System.nanoTime();
/* 244 */     this.splitState = SplitState.SPLIT;
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
/*     */   public void unsplit() {
/* 261 */     if (this.splitState != SplitState.SPLIT) {
/* 262 */       throw new IllegalStateException("Stopwatch has not been split. ");
/*     */     }
/* 264 */     this.splitState = SplitState.UNSPLIT;
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
/*     */   public void suspend() {
/* 281 */     if (this.runningState != State.RUNNING) {
/* 282 */       throw new IllegalStateException("Stopwatch must be running to suspend. ");
/*     */     }
/* 284 */     this.stopTime = System.nanoTime();
/* 285 */     this.runningState = State.SUSPENDED;
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
/*     */   public void resume() {
/* 302 */     if (this.runningState != State.SUSPENDED) {
/* 303 */       throw new IllegalStateException("Stopwatch must be suspended to resume. ");
/*     */     }
/* 305 */     this.startTime += System.nanoTime() - this.stopTime;
/* 306 */     this.runningState = State.RUNNING;
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
/*     */   public long getTime() {
/* 322 */     return getNanoTime() / 1000000L;
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
/*     */   public long getNanoTime() {
/* 338 */     if (this.runningState == State.STOPPED || this.runningState == State.SUSPENDED)
/* 339 */       return this.stopTime - this.startTime; 
/* 340 */     if (this.runningState == State.UNSTARTED)
/* 341 */       return 0L; 
/* 342 */     if (this.runningState == State.RUNNING) {
/* 343 */       return System.nanoTime() - this.startTime;
/*     */     }
/* 345 */     throw new RuntimeException("Illegal running state has occurred.");
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
/*     */   public long getSplitTime() {
/* 364 */     return getSplitNanoTime() / 1000000L;
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
/*     */   public long getSplitNanoTime() {
/* 382 */     if (this.splitState != SplitState.SPLIT) {
/* 383 */       throw new IllegalStateException("Stopwatch must be split to get the split time. ");
/*     */     }
/* 385 */     return this.stopTime - this.startTime;
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
/*     */   public long getStartTime() {
/* 397 */     if (this.runningState == State.UNSTARTED) {
/* 398 */       throw new IllegalStateException("Stopwatch has not been started");
/*     */     }
/*     */     
/* 401 */     return this.startTimeMillis;
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
/*     */   public String toString() {
/* 417 */     return DurationFormatUtils.formatDurationHMS(getTime());
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
/*     */   public String toSplitString() {
/* 433 */     return DurationFormatUtils.formatDurationHMS(getSplitTime());
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
/*     */   public boolean isStarted() {
/* 447 */     return this.runningState.isStarted();
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
/*     */   public boolean isSuspended() {
/* 460 */     return this.runningState.isSuspended();
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
/*     */   public boolean isStopped() {
/* 475 */     return this.runningState.isStopped();
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\commons\lang3\time\StopWatch.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */