/*    */ package com.mojang.launcher.updater;
/*    */ 
/*    */ import java.util.concurrent.Callable;
/*    */ import java.util.concurrent.CancellationException;
/*    */ import java.util.concurrent.ExecutionException;
/*    */ import java.util.concurrent.Future;
/*    */ import java.util.concurrent.FutureTask;
/*    */ import java.util.concurrent.LinkedBlockingQueue;
/*    */ import java.util.concurrent.RunnableFuture;
/*    */ import java.util.concurrent.ThreadPoolExecutor;
/*    */ import java.util.concurrent.TimeUnit;
/*    */ import org.apache.logging.log4j.LogManager;
/*    */ import org.apache.logging.log4j.Logger;
/*    */ 
/*    */ 
/*    */ public class ExceptionalThreadPoolExecutor
/*    */   extends ThreadPoolExecutor
/*    */ {
/* 19 */   private static final Logger LOGGER = LogManager.getLogger();
/*    */   
/*    */   public ExceptionalThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit) {
/* 22 */     super(corePoolSize, maximumPoolSize, keepAliveTime, unit, new LinkedBlockingQueue<>());
/*    */   }
/*    */ 
/*    */   
/*    */   protected void afterExecute(Runnable r, Throwable t) {
/* 27 */     super.afterExecute(r, t);
/* 28 */     if (t == null && r instanceof Future) {
/*    */       
/*    */       try {
/* 31 */         Future future = (Future)r;
/* 32 */         if (future.isDone()) {
/* 33 */           future.get();
/*    */         }
/* 35 */       } catch (CancellationException ce) {
/*    */         
/* 37 */         t = ce;
/*    */       }
/* 39 */       catch (ExecutionException ee) {
/*    */         
/* 41 */         t = ee.getCause();
/*    */       }
/* 43 */       catch (InterruptedException ie) {
/*    */         
/* 45 */         Thread.currentThread().interrupt();
/*    */       } 
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected RunnableFuture newTaskFor(Runnable runnable, Object value) {
/* 53 */     return new ExceptionalFutureTask(runnable, value);
/*    */   }
/*    */ 
/*    */   
/*    */   protected RunnableFuture newTaskFor(Callable callable) {
/* 58 */     return new ExceptionalFutureTask(callable);
/*    */   }
/*    */   
/*    */   public class ExceptionalFutureTask extends FutureTask {
/*    */     final ExceptionalThreadPoolExecutor this$0;
/*    */     
/*    */     public ExceptionalFutureTask(Callable<V> callable) {
/* 65 */       super(callable);
/* 66 */       ExceptionalThreadPoolExecutor.this = ExceptionalThreadPoolExecutor.this;
/*    */     }
/*    */ 
/*    */ 
/*    */     
/*    */     public ExceptionalFutureTask(Runnable runnable, Object result) {
/* 72 */       super(runnable, (V)result);
/* 73 */       ExceptionalThreadPoolExecutor.this = ExceptionalThreadPoolExecutor.this;
/*    */     }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     protected void done() {
/*    */       try {
/* 81 */         get();
/*    */       }
/* 83 */       catch (Throwable t) {
/*    */         
/* 85 */         ExceptionalThreadPoolExecutor.LOGGER.error("Unhandled exception in executor " + this, t);
/*    */       } 
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\mojang\launche\\updater\ExceptionalThreadPoolExecutor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */