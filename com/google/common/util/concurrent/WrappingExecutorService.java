/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Throwables;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.Executors;
/*     */ import java.util.concurrent.Future;
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
/*     */ abstract class WrappingExecutorService
/*     */   implements ExecutorService
/*     */ {
/*     */   private final ExecutorService delegate;
/*     */   
/*     */   protected WrappingExecutorService(ExecutorService delegate) {
/*  50 */     this.delegate = (ExecutorService)Preconditions.checkNotNull(delegate);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract <T> Callable<T> wrapTask(Callable<T> paramCallable);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Runnable wrapTask(Runnable command) {
/*  65 */     final Callable<Object> wrapped = wrapTask(Executors.callable(command, null));
/*     */     
/*  67 */     return new Runnable() {
/*     */         public void run() {
/*     */           try {
/*  70 */             wrapped.call();
/*  71 */           } catch (Exception e) {
/*  72 */             Throwables.propagate(e);
/*     */           } 
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final <T> ImmutableList<Callable<T>> wrapTasks(Collection<? extends Callable<T>> tasks) {
/*  85 */     ImmutableList.Builder<Callable<T>> builder = ImmutableList.builder();
/*  86 */     for (Callable<T> task : tasks) {
/*  87 */       builder.add(wrapTask(task));
/*     */     }
/*  89 */     return builder.build();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final void execute(Runnable command) {
/*  95 */     this.delegate.execute(wrapTask(command));
/*     */   }
/*     */ 
/*     */   
/*     */   public final <T> Future<T> submit(Callable<T> task) {
/* 100 */     return this.delegate.submit(wrapTask((Callable<T>)Preconditions.checkNotNull(task)));
/*     */   }
/*     */ 
/*     */   
/*     */   public final Future<?> submit(Runnable task) {
/* 105 */     return this.delegate.submit(wrapTask(task));
/*     */   }
/*     */ 
/*     */   
/*     */   public final <T> Future<T> submit(Runnable task, T result) {
/* 110 */     return this.delegate.submit(wrapTask(task), result);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
/* 116 */     return this.delegate.invokeAll((Collection<? extends Callable<T>>)wrapTasks(tasks));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException {
/* 123 */     return this.delegate.invokeAll((Collection<? extends Callable<T>>)wrapTasks(tasks), timeout, unit);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
/* 129 */     return this.delegate.invokeAny((Collection<? extends Callable<T>>)wrapTasks(tasks));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
/* 136 */     return this.delegate.invokeAny((Collection<? extends Callable<T>>)wrapTasks(tasks), timeout, unit);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void shutdown() {
/* 143 */     this.delegate.shutdown();
/*     */   }
/*     */ 
/*     */   
/*     */   public final List<Runnable> shutdownNow() {
/* 148 */     return this.delegate.shutdownNow();
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isShutdown() {
/* 153 */     return this.delegate.isShutdown();
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isTerminated() {
/* 158 */     return this.delegate.isTerminated();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
/* 164 */     return this.delegate.awaitTermination(timeout, unit);
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\commo\\util\concurrent\WrappingExecutorService.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */