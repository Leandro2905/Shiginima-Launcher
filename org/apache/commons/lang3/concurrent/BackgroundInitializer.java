/*     */ package org.apache.commons.lang3.concurrent;
/*     */ 
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.Executors;
/*     */ import java.util.concurrent.Future;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class BackgroundInitializer<T>
/*     */   implements ConcurrentInitializer<T>
/*     */ {
/*     */   private ExecutorService externalExecutor;
/*     */   private ExecutorService executor;
/*     */   private Future<T> future;
/*     */   
/*     */   protected BackgroundInitializer() {
/* 102 */     this(null);
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
/*     */   protected BackgroundInitializer(ExecutorService exec) {
/* 116 */     setExternalExecutor(exec);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final synchronized ExecutorService getExternalExecutor() {
/* 125 */     return this.externalExecutor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized boolean isStarted() {
/* 136 */     return (this.future != null);
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
/*     */   public final synchronized void setExternalExecutor(ExecutorService externalExecutor) {
/* 155 */     if (isStarted()) {
/* 156 */       throw new IllegalStateException("Cannot set ExecutorService after start()!");
/*     */     }
/*     */ 
/*     */     
/* 160 */     this.externalExecutor = externalExecutor;
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
/*     */   public synchronized boolean start() {
/* 175 */     if (!isStarted()) {
/*     */       ExecutorService tempExec;
/*     */ 
/*     */ 
/*     */       
/* 180 */       this.executor = getExternalExecutor();
/* 181 */       if (this.executor == null) {
/* 182 */         this.executor = tempExec = createExecutor();
/*     */       } else {
/* 184 */         tempExec = null;
/*     */       } 
/*     */       
/* 187 */       this.future = this.executor.submit(createTask(tempExec));
/*     */       
/* 189 */       return true;
/*     */     } 
/*     */     
/* 192 */     return false;
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
/*     */   public T get() throws ConcurrentException {
/*     */     try {
/* 212 */       return getFuture().get();
/* 213 */     } catch (ExecutionException execex) {
/* 214 */       ConcurrentUtils.handleCause(execex);
/* 215 */       return null;
/* 216 */     } catch (InterruptedException iex) {
/*     */       
/* 218 */       Thread.currentThread().interrupt();
/* 219 */       throw new ConcurrentException(iex);
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
/*     */   public synchronized Future<T> getFuture() {
/* 232 */     if (this.future == null) {
/* 233 */       throw new IllegalStateException("start() must be called first!");
/*     */     }
/*     */     
/* 236 */     return this.future;
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
/*     */   protected final synchronized ExecutorService getActiveExecutor() {
/* 249 */     return this.executor;
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
/*     */   protected int getTaskCount() {
/* 264 */     return 1;
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
/*     */   protected abstract T initialize() throws Exception;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Callable<T> createTask(ExecutorService execDestroy) {
/* 291 */     return new InitializationTask(execDestroy);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ExecutorService createExecutor() {
/* 301 */     return Executors.newFixedThreadPool(getTaskCount());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private class InitializationTask
/*     */     implements Callable<T>
/*     */   {
/*     */     private final ExecutorService execFinally;
/*     */ 
/*     */ 
/*     */     
/*     */     public InitializationTask(ExecutorService exec) {
/* 315 */       this.execFinally = exec;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public T call() throws Exception {
/*     */       try {
/* 327 */         return (T)BackgroundInitializer.this.initialize();
/*     */       } finally {
/* 329 */         if (this.execFinally != null)
/* 330 */           this.execFinally.shutdown(); 
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\commons\lang3\concurrent\BackgroundInitializer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */