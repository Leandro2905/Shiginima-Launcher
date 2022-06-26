/*     */ package org.apache.commons.lang3.concurrent;
/*     */ 
/*     */ import java.util.concurrent.ScheduledExecutorService;
/*     */ import java.util.concurrent.ScheduledFuture;
/*     */ import java.util.concurrent.ScheduledThreadPoolExecutor;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TimedSemaphore
/*     */ {
/*     */   public static final int NO_LIMIT = 0;
/*     */   private static final int THREAD_POOL_SIZE = 1;
/*     */   private final ScheduledExecutorService executorService;
/*     */   private final long period;
/*     */   private final TimeUnit unit;
/*     */   private final boolean ownExecutor;
/*     */   private ScheduledFuture<?> task;
/*     */   private long totalAcquireCount;
/*     */   private long periodCount;
/*     */   private int limit;
/*     */   private int acquireCount;
/*     */   private int lastCallsPerPeriod;
/*     */   private boolean shutdown;
/*     */   
/*     */   public TimedSemaphore(long timePeriod, TimeUnit timeUnit, int limit) {
/* 189 */     this(null, timePeriod, timeUnit, limit);
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
/*     */   public TimedSemaphore(ScheduledExecutorService service, long timePeriod, TimeUnit timeUnit, int limit) {
/* 206 */     Validate.inclusiveBetween(1L, Long.MAX_VALUE, timePeriod, "Time period must be greater than 0!");
/*     */     
/* 208 */     this.period = timePeriod;
/* 209 */     this.unit = timeUnit;
/*     */     
/* 211 */     if (service != null) {
/* 212 */       this.executorService = service;
/* 213 */       this.ownExecutor = false;
/*     */     } else {
/* 215 */       ScheduledThreadPoolExecutor s = new ScheduledThreadPoolExecutor(1);
/*     */       
/* 217 */       s.setContinueExistingPeriodicTasksAfterShutdownPolicy(false);
/* 218 */       s.setExecuteExistingDelayedTasksAfterShutdownPolicy(false);
/* 219 */       this.executorService = s;
/* 220 */       this.ownExecutor = true;
/*     */     } 
/*     */     
/* 223 */     setLimit(limit);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final synchronized int getLimit() {
/* 234 */     return this.limit;
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
/*     */   public final synchronized void setLimit(int limit) {
/* 248 */     this.limit = limit;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void shutdown() {
/* 257 */     if (!this.shutdown) {
/*     */       
/* 259 */       if (this.ownExecutor)
/*     */       {
/*     */         
/* 262 */         getExecutorService().shutdownNow();
/*     */       }
/* 264 */       if (this.task != null) {
/* 265 */         this.task.cancel(false);
/*     */       }
/*     */       
/* 268 */       this.shutdown = true;
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
/*     */   public synchronized boolean isShutdown() {
/* 280 */     return this.shutdown;
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
/*     */   public synchronized void acquire() throws InterruptedException {
/* 295 */     if (isShutdown()) {
/* 296 */       throw new IllegalStateException("TimedSemaphore is shut down!");
/*     */     }
/*     */     
/* 299 */     if (this.task == null) {
/* 300 */       this.task = startTimer();
/*     */     }
/*     */     
/* 303 */     boolean canPass = false;
/*     */     do {
/* 305 */       canPass = (getLimit() <= 0 || this.acquireCount < getLimit());
/* 306 */       if (!canPass) {
/* 307 */         wait();
/*     */       } else {
/* 309 */         this.acquireCount++;
/*     */       } 
/* 311 */     } while (!canPass);
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
/*     */   public synchronized int getLastAcquiresPerPeriod() {
/* 325 */     return this.lastCallsPerPeriod;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized int getAcquireCount() {
/* 335 */     return this.acquireCount;
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
/*     */   public synchronized int getAvailablePermits() {
/* 350 */     return getLimit() - getAcquireCount();
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
/*     */   public synchronized double getAverageCallsPerPeriod() {
/* 363 */     return (this.periodCount == 0L) ? 0.0D : (this.totalAcquireCount / this.periodCount);
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
/*     */   public long getPeriod() {
/* 375 */     return this.period;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TimeUnit getUnit() {
/* 384 */     return this.unit;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ScheduledExecutorService getExecutorService() {
/* 393 */     return this.executorService;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ScheduledFuture<?> startTimer() {
/* 404 */     return getExecutorService().scheduleAtFixedRate(new Runnable()
/*     */         {
/*     */           public void run() {
/* 407 */             TimedSemaphore.this.endOfPeriod();
/*     */           }
/*     */         },  getPeriod(), getPeriod(), getUnit());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   synchronized void endOfPeriod() {
/* 418 */     this.lastCallsPerPeriod = this.acquireCount;
/* 419 */     this.totalAcquireCount += this.acquireCount;
/* 420 */     this.periodCount++;
/* 421 */     this.acquireCount = 0;
/* 422 */     notifyAll();
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\commons\lang3\concurrent\TimedSemaphore.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */