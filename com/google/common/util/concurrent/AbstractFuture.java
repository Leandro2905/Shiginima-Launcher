/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.util.concurrent.CancellationException;
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.TimeoutException;
/*     */ import java.util.concurrent.locks.AbstractQueuedSynchronizer;
/*     */ import javax.annotation.Nullable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractFuture<V>
/*     */   implements ListenableFuture<V>
/*     */ {
/*  68 */   private final Sync<V> sync = new Sync<V>();
/*     */ 
/*     */   
/*  71 */   private final ExecutionList executionList = new ExecutionList();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public V get(long timeout, TimeUnit unit) throws InterruptedException, TimeoutException, ExecutionException {
/*  96 */     return this.sync.get(unit.toNanos(timeout));
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
/*     */   public V get() throws InterruptedException, ExecutionException {
/* 116 */     return this.sync.get();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isDone() {
/* 121 */     return this.sync.isDone();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isCancelled() {
/* 126 */     return this.sync.isCancelled();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean cancel(boolean mayInterruptIfRunning) {
/* 131 */     if (!this.sync.cancel(mayInterruptIfRunning)) {
/* 132 */       return false;
/*     */     }
/* 134 */     this.executionList.execute();
/* 135 */     if (mayInterruptIfRunning) {
/* 136 */       interruptTask();
/*     */     }
/* 138 */     return true;
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
/*     */   protected void interruptTask() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final boolean wasInterrupted() {
/* 160 */     return this.sync.wasInterrupted();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addListener(Runnable listener, Executor exec) {
/* 170 */     this.executionList.add(listener, exec);
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
/*     */   protected boolean set(@Nullable V value) {
/* 183 */     boolean result = this.sync.set(value);
/* 184 */     if (result) {
/* 185 */       this.executionList.execute();
/*     */     }
/* 187 */     return result;
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
/*     */   protected boolean setException(Throwable throwable) {
/* 200 */     boolean result = this.sync.setException((Throwable)Preconditions.checkNotNull(throwable));
/* 201 */     if (result) {
/* 202 */       this.executionList.execute();
/*     */     }
/* 204 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static final class Sync<V>
/*     */     extends AbstractQueuedSynchronizer
/*     */   {
/*     */     private static final long serialVersionUID = 0L;
/*     */ 
/*     */ 
/*     */     
/*     */     static final int RUNNING = 0;
/*     */ 
/*     */ 
/*     */     
/*     */     static final int COMPLETING = 1;
/*     */ 
/*     */ 
/*     */     
/*     */     static final int COMPLETED = 2;
/*     */ 
/*     */ 
/*     */     
/*     */     static final int CANCELLED = 4;
/*     */ 
/*     */     
/*     */     static final int INTERRUPTED = 8;
/*     */ 
/*     */     
/*     */     private V value;
/*     */ 
/*     */     
/*     */     private Throwable exception;
/*     */ 
/*     */ 
/*     */     
/*     */     protected int tryAcquireShared(int ignored) {
/* 243 */       if (isDone()) {
/* 244 */         return 1;
/*     */       }
/* 246 */       return -1;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected boolean tryReleaseShared(int finalState) {
/* 255 */       setState(finalState);
/* 256 */       return true;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     V get(long nanos) throws TimeoutException, CancellationException, ExecutionException, InterruptedException {
/* 268 */       if (!tryAcquireSharedNanos(-1, nanos)) {
/* 269 */         throw new TimeoutException("Timeout waiting for task.");
/*     */       }
/*     */       
/* 272 */       return getValue();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     V get() throws CancellationException, ExecutionException, InterruptedException {
/* 285 */       acquireSharedInterruptibly(-1);
/* 286 */       return getValue();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private V getValue() throws CancellationException, ExecutionException {
/* 295 */       int state = getState();
/* 296 */       switch (state) {
/*     */         case 2:
/* 298 */           if (this.exception != null) {
/* 299 */             throw new ExecutionException(this.exception);
/*     */           }
/* 301 */           return this.value;
/*     */ 
/*     */         
/*     */         case 4:
/*     */         case 8:
/* 306 */           throw AbstractFuture.cancellationExceptionWithCause("Task was cancelled.", this.exception);
/*     */       } 
/*     */ 
/*     */       
/* 310 */       int i = state; throw new IllegalStateException((new StringBuilder(49)).append("Error, synchronizer in invalid state: ").append(i).toString());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     boolean isDone() {
/* 320 */       return ((getState() & 0xE) != 0);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     boolean isCancelled() {
/* 327 */       return ((getState() & 0xC) != 0);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     boolean wasInterrupted() {
/* 334 */       return (getState() == 8);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     boolean set(@Nullable V v) {
/* 341 */       return complete(v, (Throwable)null, 2);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     boolean setException(Throwable t) {
/* 348 */       return complete((V)null, t, 2);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     boolean cancel(boolean interrupt) {
/* 355 */       return complete((V)null, (Throwable)null, interrupt ? 8 : 4);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private boolean complete(@Nullable V v, @Nullable Throwable t, int finalState) {
/* 372 */       boolean doCompletion = compareAndSetState(0, 1);
/* 373 */       if (doCompletion) {
/*     */ 
/*     */         
/* 376 */         this.value = v;
/*     */         
/* 378 */         this.exception = ((finalState & 0xC) != 0) ? new CancellationException("Future.cancel() was called.") : t;
/*     */         
/* 380 */         releaseShared(finalState);
/* 381 */       } else if (getState() == 1) {
/*     */ 
/*     */         
/* 384 */         acquireShared(-1);
/*     */       } 
/* 386 */       return doCompletion;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static final CancellationException cancellationExceptionWithCause(@Nullable String message, @Nullable Throwable cause) {
/* 392 */     CancellationException exception = new CancellationException(message);
/* 393 */     exception.initCause(cause);
/* 394 */     return exception;
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\commo\\util\concurrent\AbstractFuture.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */