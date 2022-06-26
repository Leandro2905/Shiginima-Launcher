/*      */ package com.google.common.util.concurrent;
/*      */ 
/*      */ import com.google.common.annotations.Beta;
/*      */ import com.google.common.base.Function;
/*      */ import com.google.common.base.Optional;
/*      */ import com.google.common.base.Preconditions;
/*      */ import com.google.common.collect.ImmutableCollection;
/*      */ import com.google.common.collect.ImmutableList;
/*      */ import com.google.common.collect.Lists;
/*      */ import com.google.common.collect.Ordering;
/*      */ import com.google.common.collect.Queues;
/*      */ import com.google.common.collect.Sets;
/*      */ import java.lang.reflect.Constructor;
/*      */ import java.lang.reflect.InvocationTargetException;
/*      */ import java.lang.reflect.UndeclaredThrowableException;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collections;
/*      */ import java.util.List;
/*      */ import java.util.Set;
/*      */ import java.util.concurrent.Callable;
/*      */ import java.util.concurrent.CancellationException;
/*      */ import java.util.concurrent.ConcurrentLinkedQueue;
/*      */ import java.util.concurrent.ExecutionException;
/*      */ import java.util.concurrent.Executor;
/*      */ import java.util.concurrent.Future;
/*      */ import java.util.concurrent.RejectedExecutionException;
/*      */ import java.util.concurrent.TimeUnit;
/*      */ import java.util.concurrent.TimeoutException;
/*      */ import java.util.concurrent.atomic.AtomicBoolean;
/*      */ import java.util.concurrent.atomic.AtomicInteger;
/*      */ import java.util.logging.Level;
/*      */ import java.util.logging.Logger;
/*      */ import javax.annotation.Nullable;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ @Beta
/*      */ public final class Futures
/*      */ {
/*      */   public static <V, X extends Exception> CheckedFuture<V, X> makeChecked(ListenableFuture<V> future, Function<? super Exception, X> mapper) {
/*   92 */     return new MappingCheckedFuture<V, X>((ListenableFuture<V>)Preconditions.checkNotNull(future), mapper);
/*      */   }
/*      */   
/*      */   private static abstract class ImmediateFuture<V> implements ListenableFuture<V> {
/*      */     private ImmediateFuture() {}
/*      */     
/*   98 */     private static final Logger log = Logger.getLogger(ImmediateFuture.class.getName());
/*      */ 
/*      */ 
/*      */     
/*      */     public void addListener(Runnable listener, Executor executor) {
/*  103 */       Preconditions.checkNotNull(listener, "Runnable was null.");
/*  104 */       Preconditions.checkNotNull(executor, "Executor was null.");
/*      */       try {
/*  106 */         executor.execute(listener);
/*  107 */       } catch (RuntimeException e) {
/*      */ 
/*      */         
/*  110 */         String str1 = String.valueOf(String.valueOf(listener)), str2 = String.valueOf(String.valueOf(executor)); log.log(Level.SEVERE, (new StringBuilder(57 + str1.length() + str2.length())).append("RuntimeException while executing runnable ").append(str1).append(" with executor ").append(str2).toString(), e);
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean cancel(boolean mayInterruptIfRunning) {
/*  117 */       return false;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public V get(long timeout, TimeUnit unit) throws ExecutionException {
/*  125 */       Preconditions.checkNotNull(unit);
/*  126 */       return get();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isCancelled() {
/*  131 */       return false;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isDone() {
/*  136 */       return true;
/*      */     }
/*      */     
/*      */     public abstract V get() throws ExecutionException;
/*      */   }
/*      */   
/*      */   private static class ImmediateSuccessfulFuture<V>
/*      */     extends ImmediateFuture<V> {
/*      */     ImmediateSuccessfulFuture(@Nullable V value) {
/*  145 */       this.value = value;
/*      */     }
/*      */     @Nullable
/*      */     private final V value;
/*      */     public V get() {
/*  150 */       return this.value;
/*      */     }
/*      */   }
/*      */   
/*      */   private static class ImmediateSuccessfulCheckedFuture<V, X extends Exception>
/*      */     extends ImmediateFuture<V> implements CheckedFuture<V, X> {
/*      */     @Nullable
/*      */     private final V value;
/*      */     
/*      */     ImmediateSuccessfulCheckedFuture(@Nullable V value) {
/*  160 */       this.value = value;
/*      */     }
/*      */ 
/*      */     
/*      */     public V get() {
/*  165 */       return this.value;
/*      */     }
/*      */ 
/*      */     
/*      */     public V checkedGet() {
/*  170 */       return this.value;
/*      */     }
/*      */ 
/*      */     
/*      */     public V checkedGet(long timeout, TimeUnit unit) {
/*  175 */       Preconditions.checkNotNull(unit);
/*  176 */       return this.value;
/*      */     }
/*      */   }
/*      */   
/*      */   private static class ImmediateFailedFuture<V>
/*      */     extends ImmediateFuture<V> {
/*      */     private final Throwable thrown;
/*      */     
/*      */     ImmediateFailedFuture(Throwable thrown) {
/*  185 */       this.thrown = thrown;
/*      */     }
/*      */ 
/*      */     
/*      */     public V get() throws ExecutionException {
/*  190 */       throw new ExecutionException(this.thrown);
/*      */     }
/*      */   }
/*      */   
/*      */   private static class ImmediateCancelledFuture<V>
/*      */     extends ImmediateFuture<V> {
/*      */     private final CancellationException thrown;
/*      */     
/*      */     ImmediateCancelledFuture() {
/*  199 */       this.thrown = new CancellationException("Immediate cancelled future.");
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isCancelled() {
/*  204 */       return true;
/*      */     }
/*      */ 
/*      */     
/*      */     public V get() {
/*  209 */       throw AbstractFuture.cancellationExceptionWithCause("Task was cancelled.", this.thrown);
/*      */     }
/*      */   }
/*      */   
/*      */   private static class ImmediateFailedCheckedFuture<V, X extends Exception>
/*      */     extends ImmediateFuture<V>
/*      */     implements CheckedFuture<V, X>
/*      */   {
/*      */     private final X thrown;
/*      */     
/*      */     ImmediateFailedCheckedFuture(X thrown) {
/*  220 */       this.thrown = thrown;
/*      */     }
/*      */ 
/*      */     
/*      */     public V get() throws ExecutionException {
/*  225 */       throw new ExecutionException(this.thrown);
/*      */     }
/*      */ 
/*      */     
/*      */     public V checkedGet() throws X {
/*  230 */       throw this.thrown;
/*      */     }
/*      */ 
/*      */     
/*      */     public V checkedGet(long timeout, TimeUnit unit) throws X {
/*  235 */       Preconditions.checkNotNull(unit);
/*  236 */       throw this.thrown;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <V> ListenableFuture<V> immediateFuture(@Nullable V value) {
/*  247 */     return new ImmediateSuccessfulFuture<V>(value);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <V, X extends Exception> CheckedFuture<V, X> immediateCheckedFuture(@Nullable V value) {
/*  260 */     return new ImmediateSuccessfulCheckedFuture<V, X>(value);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <V> ListenableFuture<V> immediateFailedFuture(Throwable throwable) {
/*  274 */     Preconditions.checkNotNull(throwable);
/*  275 */     return new ImmediateFailedFuture<V>(throwable);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <V> ListenableFuture<V> immediateCancelledFuture() {
/*  285 */     return new ImmediateCancelledFuture<V>();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <V, X extends Exception> CheckedFuture<V, X> immediateFailedCheckedFuture(X exception) {
/*  300 */     Preconditions.checkNotNull(exception);
/*  301 */     return new ImmediateFailedCheckedFuture<V, X>(exception);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <V> ListenableFuture<V> withFallback(ListenableFuture<? extends V> input, FutureFallback<? extends V> fallback) {
/*  379 */     return withFallback(input, fallback, MoreExecutors.directExecutor());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <V> ListenableFuture<V> withFallback(ListenableFuture<? extends V> input, FutureFallback<? extends V> fallback, Executor executor) {
/*  443 */     Preconditions.checkNotNull(fallback);
/*  444 */     return new FallbackFuture<V>(input, fallback, executor);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static class FallbackFuture<V>
/*      */     extends AbstractFuture<V>
/*      */   {
/*      */     private volatile ListenableFuture<? extends V> running;
/*      */ 
/*      */ 
/*      */     
/*      */     FallbackFuture(ListenableFuture<? extends V> input, final FutureFallback<? extends V> fallback, Executor executor) {
/*  458 */       this.running = input;
/*  459 */       Futures.addCallback(this.running, new FutureCallback<V>()
/*      */           {
/*      */             public void onSuccess(V value) {
/*  462 */               Futures.FallbackFuture.this.set(value);
/*      */             }
/*      */ 
/*      */             
/*      */             public void onFailure(Throwable t) {
/*  467 */               if (Futures.FallbackFuture.this.isCancelled()) {
/*      */                 return;
/*      */               }
/*      */               try {
/*  471 */                 Futures.FallbackFuture.this.running = fallback.create(t);
/*  472 */                 if (Futures.FallbackFuture.this.isCancelled()) {
/*  473 */                   Futures.FallbackFuture.this.running.cancel(Futures.FallbackFuture.this.wasInterrupted());
/*      */                   return;
/*      */                 } 
/*  476 */                 Futures.addCallback(Futures.FallbackFuture.this.running, new FutureCallback<V>()
/*      */                     {
/*      */                       public void onSuccess(V value) {
/*  479 */                         Futures.FallbackFuture.this.set(value);
/*      */                       }
/*      */ 
/*      */                       
/*      */                       public void onFailure(Throwable t) {
/*  484 */                         if (Futures.FallbackFuture.this.running.isCancelled()) {
/*  485 */                           Futures.FallbackFuture.this.cancel(false);
/*      */                         } else {
/*  487 */                           Futures.FallbackFuture.this.setException(t);
/*      */                         } 
/*      */                       }
/*      */                     },  MoreExecutors.directExecutor());
/*  491 */               } catch (Throwable e) {
/*  492 */                 Futures.FallbackFuture.this.setException(e);
/*      */               } 
/*      */             }
/*      */           }executor);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean cancel(boolean mayInterruptIfRunning) {
/*  500 */       if (super.cancel(mayInterruptIfRunning)) {
/*  501 */         this.running.cancel(mayInterruptIfRunning);
/*  502 */         return true;
/*      */       } 
/*  504 */       return false;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <I, O> ListenableFuture<O> transform(ListenableFuture<I> input, AsyncFunction<? super I, ? extends O> function) {
/*  565 */     ChainingListenableFuture<I, O> output = new ChainingListenableFuture<I, O>(function, input);
/*      */     
/*  567 */     input.addListener(output, MoreExecutors.directExecutor());
/*  568 */     return output;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <I, O> ListenableFuture<O> transform(ListenableFuture<I> input, AsyncFunction<? super I, ? extends O> function, Executor executor) {
/*  613 */     Preconditions.checkNotNull(executor);
/*  614 */     ChainingListenableFuture<I, O> output = new ChainingListenableFuture<I, O>(function, input);
/*      */     
/*  616 */     input.addListener(rejectionPropagatingRunnable(output, output, executor), MoreExecutors.directExecutor());
/*  617 */     return output;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static Runnable rejectionPropagatingRunnable(final AbstractFuture<?> outputFuture, final Runnable delegateTask, final Executor delegateExecutor) {
/*  628 */     return new Runnable() {
/*      */         public void run() {
/*  630 */           final AtomicBoolean thrownFromDelegate = new AtomicBoolean(true);
/*      */           try {
/*  632 */             delegateExecutor.execute(new Runnable() {
/*      */                   public void run() {
/*  634 */                     thrownFromDelegate.set(false);
/*  635 */                     delegateTask.run();
/*      */                   }
/*      */                 });
/*  638 */           } catch (RejectedExecutionException e) {
/*  639 */             if (thrownFromDelegate.get())
/*      */             {
/*  641 */               outputFuture.setException(e);
/*      */             }
/*      */           } 
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <I, O> ListenableFuture<O> transform(ListenableFuture<I> input, Function<? super I, ? extends O> function) {
/*  705 */     Preconditions.checkNotNull(function);
/*  706 */     ChainingListenableFuture<I, O> output = new ChainingListenableFuture<I, O>(asAsyncFunction(function), input);
/*      */     
/*  708 */     input.addListener(output, MoreExecutors.directExecutor());
/*  709 */     return output;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <I, O> ListenableFuture<O> transform(ListenableFuture<I> input, Function<? super I, ? extends O> function, Executor executor) {
/*  751 */     Preconditions.checkNotNull(function);
/*  752 */     return transform(input, asAsyncFunction(function), executor);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static <I, O> AsyncFunction<I, O> asAsyncFunction(final Function<? super I, ? extends O> function) {
/*  758 */     return new AsyncFunction<I, O>() {
/*      */         public ListenableFuture<O> apply(I input) {
/*  760 */           O output = (O)function.apply(input);
/*  761 */           return Futures.immediateFuture(output);
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <I, O> Future<O> lazyTransform(final Future<I> input, final Function<? super I, ? extends O> function) {
/*  791 */     Preconditions.checkNotNull(input);
/*  792 */     Preconditions.checkNotNull(function);
/*  793 */     return new Future<O>()
/*      */       {
/*      */         public boolean cancel(boolean mayInterruptIfRunning)
/*      */         {
/*  797 */           return input.cancel(mayInterruptIfRunning);
/*      */         }
/*      */ 
/*      */         
/*      */         public boolean isCancelled() {
/*  802 */           return input.isCancelled();
/*      */         }
/*      */ 
/*      */         
/*      */         public boolean isDone() {
/*  807 */           return input.isDone();
/*      */         }
/*      */ 
/*      */         
/*      */         public O get() throws InterruptedException, ExecutionException {
/*  812 */           return applyTransformation(input.get());
/*      */         }
/*      */ 
/*      */ 
/*      */         
/*      */         public O get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
/*  818 */           return applyTransformation(input.get(timeout, unit));
/*      */         }
/*      */         
/*      */         private O applyTransformation(I input) throws ExecutionException {
/*      */           try {
/*  823 */             return (O)function.apply(input);
/*  824 */           } catch (Throwable t) {
/*  825 */             throw new ExecutionException(t);
/*      */           } 
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static class ChainingListenableFuture<I, O>
/*      */     extends AbstractFuture<O>
/*      */     implements Runnable
/*      */   {
/*      */     private AsyncFunction<? super I, ? extends O> function;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private ListenableFuture<? extends I> inputFuture;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private volatile ListenableFuture<? extends O> outputFuture;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private ChainingListenableFuture(AsyncFunction<? super I, ? extends O> function, ListenableFuture<? extends I> inputFuture) {
/*  861 */       this.function = (AsyncFunction<? super I, ? extends O>)Preconditions.checkNotNull(function);
/*  862 */       this.inputFuture = (ListenableFuture<? extends I>)Preconditions.checkNotNull(inputFuture);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean cancel(boolean mayInterruptIfRunning) {
/*  871 */       if (super.cancel(mayInterruptIfRunning)) {
/*      */ 
/*      */         
/*  874 */         cancel(this.inputFuture, mayInterruptIfRunning);
/*  875 */         cancel(this.outputFuture, mayInterruptIfRunning);
/*  876 */         return true;
/*      */       } 
/*  878 */       return false;
/*      */     }
/*      */ 
/*      */     
/*      */     private void cancel(@Nullable Future<?> future, boolean mayInterruptIfRunning) {
/*  883 */       if (future != null) {
/*  884 */         future.cancel(mayInterruptIfRunning);
/*      */       }
/*      */     }
/*      */ 
/*      */     
/*      */     public void run() {
/*      */       try {
/*      */         I sourceResult;
/*      */         try {
/*  893 */           sourceResult = Uninterruptibles.getUninterruptibly((Future)this.inputFuture);
/*  894 */         } catch (CancellationException e) {
/*      */ 
/*      */ 
/*      */           
/*  898 */           cancel(false);
/*      */           return;
/*  900 */         } catch (ExecutionException e) {
/*      */           
/*  902 */           setException(e.getCause());
/*      */           
/*      */           return;
/*      */         } 
/*  906 */         final ListenableFuture<? extends O> outputFuture = this.outputFuture = (ListenableFuture<? extends O>)Preconditions.checkNotNull(this.function.apply(sourceResult), "AsyncFunction may not return null.");
/*      */ 
/*      */         
/*  909 */         if (isCancelled()) {
/*  910 */           outputFuture.cancel(wasInterrupted());
/*  911 */           this.outputFuture = null;
/*      */           return;
/*      */         } 
/*  914 */         outputFuture.addListener(new Runnable()
/*      */             {
/*      */               public void run() {
/*      */                 try {
/*  918 */                   Futures.ChainingListenableFuture.this.set(Uninterruptibles.getUninterruptibly(outputFuture));
/*  919 */                 } catch (CancellationException e) {
/*      */ 
/*      */ 
/*      */                   
/*  923 */                   Futures.ChainingListenableFuture.this.cancel(false);
/*      */                   return;
/*  925 */                 } catch (ExecutionException e) {
/*      */                   
/*  927 */                   Futures.ChainingListenableFuture.this.setException(e.getCause());
/*      */                 } finally {
/*      */                   
/*  930 */                   Futures.ChainingListenableFuture.this.outputFuture = null;
/*      */                 } 
/*      */               }
/*      */             },  MoreExecutors.directExecutor());
/*  934 */       } catch (UndeclaredThrowableException e) {
/*      */         
/*  936 */         setException(e.getCause());
/*  937 */       } catch (Throwable t) {
/*      */ 
/*      */         
/*  940 */         setException(t);
/*      */       } finally {
/*      */         
/*  943 */         this.function = null;
/*  944 */         this.inputFuture = null;
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <V> ListenableFuture<V> dereference(ListenableFuture<? extends ListenableFuture<? extends V>> nested) {
/*  973 */     return transform(nested, (AsyncFunction)DEREFERENCER);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  979 */   private static final AsyncFunction<ListenableFuture<Object>, Object> DEREFERENCER = new AsyncFunction<ListenableFuture<Object>, Object>()
/*      */     {
/*      */       public ListenableFuture<Object> apply(ListenableFuture<Object> input) {
/*  982 */         return input;
/*      */       }
/*      */     };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Beta
/*      */   public static <V> ListenableFuture<List<V>> allAsList(ListenableFuture<? extends V>... futures) {
/* 1005 */     return listFuture(ImmutableList.copyOf((Object[])futures), true, MoreExecutors.directExecutor());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Beta
/*      */   public static <V> ListenableFuture<List<V>> allAsList(Iterable<? extends ListenableFuture<? extends V>> futures) {
/* 1027 */     return listFuture(ImmutableList.copyOf(futures), true, MoreExecutors.directExecutor());
/*      */   }
/*      */   
/*      */   private static final class WrappedCombiner<T> implements Callable<T> {
/*      */     final Callable<T> delegate;
/*      */     Futures.CombinerFuture<T> outputFuture;
/*      */     
/*      */     WrappedCombiner(Callable<T> delegate) {
/* 1035 */       this.delegate = (Callable<T>)Preconditions.checkNotNull(delegate);
/*      */     }
/*      */     
/*      */     public T call() throws Exception {
/*      */       try {
/* 1040 */         return this.delegate.call();
/* 1041 */       } catch (ExecutionException e) {
/* 1042 */         this.outputFuture.setException(e.getCause());
/* 1043 */       } catch (CancellationException e) {
/* 1044 */         this.outputFuture.cancel(false);
/*      */       } 
/*      */ 
/*      */       
/* 1048 */       return null;
/*      */     }
/*      */   }
/*      */   
/*      */   private static final class CombinerFuture<V> extends ListenableFutureTask<V> {
/*      */     ImmutableList<ListenableFuture<?>> futures;
/*      */     
/*      */     CombinerFuture(Callable<V> callable, ImmutableList<ListenableFuture<?>> futures) {
/* 1056 */       super(callable);
/* 1057 */       this.futures = futures;
/*      */     }
/*      */     
/*      */     public boolean cancel(boolean mayInterruptIfRunning) {
/* 1061 */       ImmutableList<ListenableFuture<?>> futures = this.futures;
/* 1062 */       if (super.cancel(mayInterruptIfRunning)) {
/* 1063 */         for (ListenableFuture<?> future : futures) {
/* 1064 */           future.cancel(mayInterruptIfRunning);
/*      */         }
/* 1066 */         return true;
/*      */       } 
/* 1068 */       return false;
/*      */     }
/*      */     
/*      */     protected void done() {
/* 1072 */       super.done();
/* 1073 */       this.futures = null;
/*      */     }
/*      */     
/*      */     protected void setException(Throwable t) {
/* 1077 */       super.setException(t);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <V> ListenableFuture<V> nonCancellationPropagating(ListenableFuture<V> future) {
/* 1091 */     return new NonCancellationPropagatingFuture<V>(future);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static class NonCancellationPropagatingFuture<V>
/*      */     extends AbstractFuture<V>
/*      */   {
/*      */     NonCancellationPropagatingFuture(final ListenableFuture<V> delegate) {
/* 1100 */       Preconditions.checkNotNull(delegate);
/* 1101 */       Futures.addCallback(delegate, new FutureCallback<V>()
/*      */           {
/*      */             public void onSuccess(V result) {
/* 1104 */               Futures.NonCancellationPropagatingFuture.this.set(result);
/*      */             }
/*      */ 
/*      */             
/*      */             public void onFailure(Throwable t) {
/* 1109 */               if (delegate.isCancelled()) {
/* 1110 */                 Futures.NonCancellationPropagatingFuture.this.cancel(false);
/*      */               } else {
/* 1112 */                 Futures.NonCancellationPropagatingFuture.this.setException(t);
/*      */               } 
/*      */             }
/*      */           },  MoreExecutors.directExecutor());
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Beta
/*      */   public static <V> ListenableFuture<List<V>> successfulAsList(ListenableFuture<? extends V>... futures) {
/* 1137 */     return listFuture(ImmutableList.copyOf((Object[])futures), false, MoreExecutors.directExecutor());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Beta
/*      */   public static <V> ListenableFuture<List<V>> successfulAsList(Iterable<? extends ListenableFuture<? extends V>> futures) {
/* 1158 */     return listFuture(ImmutableList.copyOf(futures), false, MoreExecutors.directExecutor());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Beta
/*      */   public static <T> ImmutableList<ListenableFuture<T>> inCompletionOrder(Iterable<? extends ListenableFuture<? extends T>> futures) {
/* 1179 */     final ConcurrentLinkedQueue<AsyncSettableFuture<T>> delegates = Queues.newConcurrentLinkedQueue();
/*      */     
/* 1181 */     ImmutableList.Builder<ListenableFuture<T>> listBuilder = ImmutableList.builder();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1192 */     SerializingExecutor executor = new SerializingExecutor(MoreExecutors.directExecutor());
/* 1193 */     for (ListenableFuture<? extends T> future : futures) {
/* 1194 */       AsyncSettableFuture<T> delegate = AsyncSettableFuture.create();
/*      */       
/* 1196 */       delegates.add(delegate);
/* 1197 */       future.addListener(new Runnable() {
/*      */             public void run() {
/* 1199 */               ((AsyncSettableFuture)delegates.remove()).setFuture(future);
/*      */             }
/*      */           },  executor);
/* 1202 */       listBuilder.add(delegate);
/*      */     } 
/* 1204 */     return listBuilder.build();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <V> void addCallback(ListenableFuture<V> future, FutureCallback<? super V> callback) {
/* 1258 */     addCallback(future, callback, MoreExecutors.directExecutor());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <V> void addCallback(final ListenableFuture<V> future, final FutureCallback<? super V> callback, Executor executor) {
/* 1300 */     Preconditions.checkNotNull(callback);
/* 1301 */     Runnable callbackListener = new Runnable()
/*      */       {
/*      */         public void run()
/*      */         {
/*      */           V value;
/*      */           
/*      */           try {
/* 1308 */             value = Uninterruptibles.getUninterruptibly(future);
/* 1309 */           } catch (ExecutionException e) {
/* 1310 */             callback.onFailure(e.getCause());
/*      */             return;
/* 1312 */           } catch (RuntimeException e) {
/* 1313 */             callback.onFailure(e);
/*      */             return;
/* 1315 */           } catch (Error e) {
/* 1316 */             callback.onFailure(e);
/*      */             return;
/*      */           } 
/* 1319 */           callback.onSuccess(value);
/*      */         }
/*      */       };
/* 1322 */     future.addListener(callbackListener, executor);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <V, X extends Exception> V get(Future<V> future, Class<X> exceptionClass) throws X {
/* 1374 */     Preconditions.checkNotNull(future);
/* 1375 */     Preconditions.checkArgument(!RuntimeException.class.isAssignableFrom(exceptionClass), "Futures.get exception type (%s) must not be a RuntimeException", new Object[] { exceptionClass });
/*      */ 
/*      */     
/*      */     try {
/* 1379 */       return future.get();
/* 1380 */     } catch (InterruptedException e) {
/* 1381 */       Thread.currentThread().interrupt();
/* 1382 */       throw newWithCause(exceptionClass, e);
/* 1383 */     } catch (ExecutionException e) {
/* 1384 */       wrapAndThrowExceptionOrError(e.getCause(), exceptionClass);
/* 1385 */       throw (X)new AssertionError();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <V, X extends Exception> V get(Future<V> future, long timeout, TimeUnit unit, Class<X> exceptionClass) throws X {
/* 1440 */     Preconditions.checkNotNull(future);
/* 1441 */     Preconditions.checkNotNull(unit);
/* 1442 */     Preconditions.checkArgument(!RuntimeException.class.isAssignableFrom(exceptionClass), "Futures.get exception type (%s) must not be a RuntimeException", new Object[] { exceptionClass });
/*      */ 
/*      */     
/*      */     try {
/* 1446 */       return future.get(timeout, unit);
/* 1447 */     } catch (InterruptedException e) {
/* 1448 */       Thread.currentThread().interrupt();
/* 1449 */       throw newWithCause(exceptionClass, e);
/* 1450 */     } catch (TimeoutException e) {
/* 1451 */       throw newWithCause(exceptionClass, e);
/* 1452 */     } catch (ExecutionException e) {
/* 1453 */       wrapAndThrowExceptionOrError(e.getCause(), exceptionClass);
/* 1454 */       throw (X)new AssertionError();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private static <X extends Exception> void wrapAndThrowExceptionOrError(Throwable cause, Class<X> exceptionClass) throws X {
/* 1460 */     if (cause instanceof Error) {
/* 1461 */       throw (X)new ExecutionError((Error)cause);
/*      */     }
/* 1463 */     if (cause instanceof RuntimeException) {
/* 1464 */       throw (X)new UncheckedExecutionException(cause);
/*      */     }
/* 1466 */     throw newWithCause(exceptionClass, cause);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <V> V getUnchecked(Future<V> future) {
/* 1507 */     Preconditions.checkNotNull(future);
/*      */     try {
/* 1509 */       return Uninterruptibles.getUninterruptibly(future);
/* 1510 */     } catch (ExecutionException e) {
/* 1511 */       wrapAndThrowUnchecked(e.getCause());
/* 1512 */       throw new AssertionError();
/*      */     } 
/*      */   }
/*      */   
/*      */   private static void wrapAndThrowUnchecked(Throwable cause) {
/* 1517 */     if (cause instanceof Error) {
/* 1518 */       throw new ExecutionError((Error)cause);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1525 */     throw new UncheckedExecutionException(cause);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static <X extends Exception> X newWithCause(Class<X> exceptionClass, Throwable cause) {
/* 1549 */     List<Constructor<X>> constructors = (List)Arrays.asList(exceptionClass.getConstructors());
/*      */     
/* 1551 */     for (Constructor<X> constructor : preferringStrings(constructors)) {
/* 1552 */       Exception exception = newFromConstructor(constructor, cause);
/* 1553 */       if (exception != null) {
/* 1554 */         if (exception.getCause() == null) {
/* 1555 */           exception.initCause(cause);
/*      */         }
/* 1557 */         return (X)exception;
/*      */       } 
/*      */     } 
/* 1560 */     String str = String.valueOf(String.valueOf(exceptionClass)); throw new IllegalArgumentException((new StringBuilder(82 + str.length())).append("No appropriate constructor for exception of type ").append(str).append(" in response to chained exception").toString(), cause);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static <X extends Exception> List<Constructor<X>> preferringStrings(List<Constructor<X>> constructors) {
/* 1567 */     return WITH_STRING_PARAM_FIRST.sortedCopy(constructors);
/*      */   }
/*      */   
/* 1570 */   private static final Ordering<Constructor<?>> WITH_STRING_PARAM_FIRST = Ordering.natural().onResultOf(new Function<Constructor<?>, Boolean>()
/*      */       {
/*      */         public Boolean apply(Constructor<?> input) {
/* 1573 */           return Boolean.valueOf(Arrays.<Class<?>>asList(input.getParameterTypes()).contains(String.class));
/*      */         }
/*      */       }).reverse();
/*      */   
/*      */   @Nullable
/*      */   private static <X> X newFromConstructor(Constructor<X> constructor, Throwable cause) {
/* 1579 */     Class<?>[] paramTypes = constructor.getParameterTypes();
/* 1580 */     Object[] params = new Object[paramTypes.length];
/* 1581 */     for (int i = 0; i < paramTypes.length; i++) {
/* 1582 */       Class<?> paramType = paramTypes[i];
/* 1583 */       if (paramType.equals(String.class)) {
/* 1584 */         params[i] = cause.toString();
/* 1585 */       } else if (paramType.equals(Throwable.class)) {
/* 1586 */         params[i] = cause;
/*      */       } else {
/* 1588 */         return null;
/*      */       } 
/*      */     } 
/*      */     try {
/* 1592 */       return constructor.newInstance(params);
/* 1593 */     } catch (IllegalArgumentException e) {
/* 1594 */       return null;
/* 1595 */     } catch (InstantiationException e) {
/* 1596 */       return null;
/* 1597 */     } catch (IllegalAccessException e) {
/* 1598 */       return null;
/* 1599 */     } catch (InvocationTargetException e) {
/* 1600 */       return null;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static class CombinedFuture<V, C>
/*      */     extends AbstractFuture<C>
/*      */   {
/* 1609 */     private static final Logger logger = Logger.getLogger(CombinedFuture.class.getName());
/*      */     
/*      */     ImmutableCollection<? extends ListenableFuture<? extends V>> futures;
/*      */     
/*      */     final boolean allMustSucceed;
/*      */     final AtomicInteger remaining;
/*      */     Futures.FutureCombiner<V, C> combiner;
/*      */     List<Optional<V>> values;
/* 1617 */     final Object seenExceptionsLock = new Object();
/*      */ 
/*      */     
/*      */     Set<Throwable> seenExceptions;
/*      */ 
/*      */     
/*      */     CombinedFuture(ImmutableCollection<? extends ListenableFuture<? extends V>> futures, boolean allMustSucceed, Executor listenerExecutor, Futures.FutureCombiner<V, C> combiner) {
/* 1624 */       this.futures = futures;
/* 1625 */       this.allMustSucceed = allMustSucceed;
/* 1626 */       this.remaining = new AtomicInteger(futures.size());
/* 1627 */       this.combiner = combiner;
/* 1628 */       this.values = Lists.newArrayListWithCapacity(futures.size());
/* 1629 */       init(listenerExecutor);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected void init(Executor listenerExecutor) {
/* 1637 */       addListener(new Runnable()
/*      */           {
/*      */             public void run()
/*      */             {
/* 1641 */               if (Futures.CombinedFuture.this.isCancelled()) {
/* 1642 */                 for (ListenableFuture<?> future : (Iterable<ListenableFuture<?>>)Futures.CombinedFuture.this.futures) {
/* 1643 */                   future.cancel(Futures.CombinedFuture.this.wasInterrupted());
/*      */                 }
/*      */               }
/*      */ 
/*      */               
/* 1648 */               Futures.CombinedFuture.this.futures = null;
/*      */ 
/*      */ 
/*      */               
/* 1652 */               Futures.CombinedFuture.this.values = null;
/*      */ 
/*      */               
/* 1655 */               Futures.CombinedFuture.this.combiner = null;
/*      */             }
/*      */           },  MoreExecutors.directExecutor());
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1662 */       if (this.futures.isEmpty()) {
/* 1663 */         set(this.combiner.combine((List<Optional<V>>)ImmutableList.of()));
/*      */         
/*      */         return;
/*      */       } 
/*      */       int i;
/* 1668 */       for (i = 0; i < this.futures.size(); i++) {
/* 1669 */         this.values.add(null);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1680 */       i = 0;
/* 1681 */       for (ListenableFuture<? extends V> listenable : this.futures) {
/* 1682 */         final int index = i++;
/* 1683 */         listenable.addListener(new Runnable()
/*      */             {
/*      */               public void run() {
/* 1686 */                 Futures.CombinedFuture.this.setOneValue(index, listenable);
/*      */               }
/*      */             }listenerExecutor);
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private void setExceptionAndMaybeLog(Throwable throwable) {
/* 1699 */       boolean visibleFromOutputFuture = false;
/* 1700 */       boolean firstTimeSeeingThisException = true;
/* 1701 */       if (this.allMustSucceed) {
/*      */ 
/*      */         
/* 1704 */         visibleFromOutputFuture = setException(throwable);
/*      */         
/* 1706 */         synchronized (this.seenExceptionsLock) {
/* 1707 */           if (this.seenExceptions == null) {
/* 1708 */             this.seenExceptions = Sets.newHashSet();
/*      */           }
/* 1710 */           firstTimeSeeingThisException = this.seenExceptions.add(throwable);
/*      */         } 
/*      */       } 
/*      */       
/* 1714 */       if (throwable instanceof Error || (this.allMustSucceed && !visibleFromOutputFuture && firstTimeSeeingThisException))
/*      */       {
/* 1716 */         logger.log(Level.SEVERE, "input future failed.", throwable);
/*      */       }
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private void setOneValue(int index, Future<? extends V> future) {
/* 1724 */       List<Optional<V>> localValues = this.values;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1732 */       if (isDone() || localValues == null)
/*      */       {
/*      */ 
/*      */ 
/*      */         
/* 1737 */         Preconditions.checkState((this.allMustSucceed || isCancelled()), "Future was done before all dependencies completed");
/*      */       }
/*      */ 
/*      */       
/*      */       try {
/* 1742 */         Preconditions.checkState(future.isDone(), "Tried to set value from future which is not done");
/*      */         
/* 1744 */         V returnValue = Uninterruptibles.getUninterruptibly((Future)future);
/* 1745 */         if (localValues != null) {
/* 1746 */           localValues.set(index, Optional.fromNullable(returnValue));
/*      */         }
/* 1748 */       } catch (CancellationException e) {
/* 1749 */         if (this.allMustSucceed)
/*      */         {
/*      */           
/* 1752 */           cancel(false);
/*      */         }
/* 1754 */       } catch (ExecutionException e) {
/* 1755 */         setExceptionAndMaybeLog(e.getCause());
/* 1756 */       } catch (Throwable t) {
/* 1757 */         setExceptionAndMaybeLog(t);
/*      */       } finally {
/* 1759 */         int newRemaining = this.remaining.decrementAndGet();
/* 1760 */         Preconditions.checkState((newRemaining >= 0), "Less than 0 remaining futures");
/* 1761 */         if (newRemaining == 0) {
/* 1762 */           Futures.FutureCombiner<V, C> localCombiner = this.combiner;
/* 1763 */           if (localCombiner != null && localValues != null) {
/* 1764 */             set(localCombiner.combine(localValues));
/*      */           } else {
/* 1766 */             Preconditions.checkState(isDone());
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static <V> ListenableFuture<List<V>> listFuture(ImmutableList<ListenableFuture<? extends V>> futures, boolean allMustSucceed, Executor listenerExecutor) {
/* 1777 */     return new CombinedFuture<V, List<V>>((ImmutableCollection<? extends ListenableFuture<? extends V>>)futures, allMustSucceed, listenerExecutor, new FutureCombiner<V, List<V>>()
/*      */         {
/*      */           
/*      */           public List<V> combine(List<Optional<V>> values)
/*      */           {
/* 1782 */             List<V> result = Lists.newArrayList();
/* 1783 */             for (Optional<V> element : values) {
/* 1784 */               result.add((element != null) ? (V)element.orNull() : null);
/*      */             }
/* 1786 */             return Collections.unmodifiableList(result);
/*      */           }
/*      */         });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static class MappingCheckedFuture<V, X extends Exception>
/*      */     extends AbstractCheckedFuture<V, X>
/*      */   {
/*      */     final Function<? super Exception, X> mapper;
/*      */ 
/*      */ 
/*      */     
/*      */     MappingCheckedFuture(ListenableFuture<V> delegate, Function<? super Exception, X> mapper) {
/* 1802 */       super(delegate);
/*      */       
/* 1804 */       this.mapper = (Function<? super Exception, X>)Preconditions.checkNotNull(mapper);
/*      */     }
/*      */ 
/*      */     
/*      */     protected X mapException(Exception e) {
/* 1809 */       return (X)this.mapper.apply(e);
/*      */     }
/*      */   }
/*      */   
/*      */   private static interface FutureCombiner<V, C> {
/*      */     C combine(List<Optional<V>> param1List);
/*      */   }
/*      */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\commo\\util\concurrent\Futures.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */