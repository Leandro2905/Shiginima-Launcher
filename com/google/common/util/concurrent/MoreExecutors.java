/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Supplier;
/*     */ import com.google.common.base.Throwables;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Queues;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.BlockingQueue;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.Delayed;
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.Executors;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.RejectedExecutionException;
/*     */ import java.util.concurrent.ScheduledExecutorService;
/*     */ import java.util.concurrent.ScheduledFuture;
/*     */ import java.util.concurrent.ScheduledThreadPoolExecutor;
/*     */ import java.util.concurrent.ThreadFactory;
/*     */ import java.util.concurrent.ThreadPoolExecutor;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.TimeoutException;
/*     */ import java.util.concurrent.locks.Condition;
/*     */ import java.util.concurrent.locks.Lock;
/*     */ import java.util.concurrent.locks.ReentrantLock;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class MoreExecutors
/*     */ {
/*     */   @Beta
/*     */   public static ExecutorService getExitingExecutorService(ThreadPoolExecutor executor, long terminationTimeout, TimeUnit timeUnit) {
/*  86 */     return (new Application()).getExitingExecutorService(executor, terminationTimeout, timeUnit);
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
/*     */   @Beta
/*     */   public static ScheduledExecutorService getExitingScheduledExecutorService(ScheduledThreadPoolExecutor executor, long terminationTimeout, TimeUnit timeUnit) {
/* 109 */     return (new Application()).getExitingScheduledExecutorService(executor, terminationTimeout, timeUnit);
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
/*     */   @Beta
/*     */   public static void addDelayedShutdownHook(ExecutorService service, long terminationTimeout, TimeUnit timeUnit) {
/* 127 */     (new Application()).addDelayedShutdownHook(service, terminationTimeout, timeUnit);
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
/*     */   @Beta
/*     */   public static ExecutorService getExitingExecutorService(ThreadPoolExecutor executor) {
/* 148 */     return (new Application()).getExitingExecutorService(executor);
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
/*     */   @Beta
/*     */   public static ScheduledExecutorService getExitingScheduledExecutorService(ScheduledThreadPoolExecutor executor) {
/* 169 */     return (new Application()).getExitingScheduledExecutorService(executor);
/*     */   }
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   static class Application
/*     */   {
/*     */     final ExecutorService getExitingExecutorService(ThreadPoolExecutor executor, long terminationTimeout, TimeUnit timeUnit) {
/* 177 */       MoreExecutors.useDaemonThreadFactory(executor);
/* 178 */       ExecutorService service = Executors.unconfigurableExecutorService(executor);
/* 179 */       addDelayedShutdownHook(service, terminationTimeout, timeUnit);
/* 180 */       return service;
/*     */     }
/*     */ 
/*     */     
/*     */     final ScheduledExecutorService getExitingScheduledExecutorService(ScheduledThreadPoolExecutor executor, long terminationTimeout, TimeUnit timeUnit) {
/* 185 */       MoreExecutors.useDaemonThreadFactory(executor);
/* 186 */       ScheduledExecutorService service = Executors.unconfigurableScheduledExecutorService(executor);
/* 187 */       addDelayedShutdownHook(service, terminationTimeout, timeUnit);
/* 188 */       return service;
/*     */     }
/*     */ 
/*     */     
/*     */     final void addDelayedShutdownHook(final ExecutorService service, final long terminationTimeout, final TimeUnit timeUnit) {
/* 193 */       Preconditions.checkNotNull(service);
/* 194 */       Preconditions.checkNotNull(timeUnit);
/* 195 */       String str = String.valueOf(String.valueOf(service)); addShutdownHook(MoreExecutors.newThread((new StringBuilder(24 + str.length())).append("DelayedShutdownHook-for-").append(str).toString(), new Runnable()
/*     */             {
/*     */ 
/*     */ 
/*     */ 
/*     */               
/*     */               public void run()
/*     */               {
/*     */                 try {
/* 204 */                   service.shutdown();
/* 205 */                   service.awaitTermination(terminationTimeout, timeUnit);
/* 206 */                 } catch (InterruptedException ignored) {}
/*     */               }
/*     */             }));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     final ExecutorService getExitingExecutorService(ThreadPoolExecutor executor) {
/* 214 */       return getExitingExecutorService(executor, 120L, TimeUnit.SECONDS);
/*     */     }
/*     */ 
/*     */     
/*     */     final ScheduledExecutorService getExitingScheduledExecutorService(ScheduledThreadPoolExecutor executor) {
/* 219 */       return getExitingScheduledExecutorService(executor, 120L, TimeUnit.SECONDS);
/*     */     }
/*     */     @VisibleForTesting
/*     */     void addShutdownHook(Thread hook) {
/* 223 */       Runtime.getRuntime().addShutdownHook(hook);
/*     */     }
/*     */   }
/*     */   
/*     */   private static void useDaemonThreadFactory(ThreadPoolExecutor executor) {
/* 228 */     executor.setThreadFactory((new ThreadFactoryBuilder()).setDaemon(true).setThreadFactory(executor.getThreadFactory()).build());
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static ListeningExecutorService sameThreadExecutor() {
/* 270 */     return new DirectExecutorService();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class DirectExecutorService
/*     */     extends AbstractListeningExecutorService
/*     */   {
/* 280 */     private final Lock lock = new ReentrantLock();
/*     */ 
/*     */     
/* 283 */     private final Condition termination = this.lock.newCondition();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 292 */     private int runningTasks = 0;
/*     */     
/*     */     private boolean shutdown = false;
/*     */     
/*     */     public void execute(Runnable command) {
/* 297 */       startTask();
/*     */       try {
/* 299 */         command.run();
/*     */       } finally {
/* 301 */         endTask();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isShutdown() {
/* 307 */       this.lock.lock();
/*     */       try {
/* 309 */         return this.shutdown;
/*     */       } finally {
/* 311 */         this.lock.unlock();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void shutdown() {
/* 317 */       this.lock.lock();
/*     */       try {
/* 319 */         this.shutdown = true;
/*     */       } finally {
/* 321 */         this.lock.unlock();
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public List<Runnable> shutdownNow() {
/* 328 */       shutdown();
/* 329 */       return Collections.emptyList();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isTerminated() {
/* 334 */       this.lock.lock();
/*     */       try {
/* 336 */         return (this.shutdown && this.runningTasks == 0);
/*     */       } finally {
/* 338 */         this.lock.unlock();
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
/* 345 */       long nanos = unit.toNanos(timeout);
/* 346 */       this.lock.lock();
/*     */       try {
/*     */         while (true) {
/* 349 */           if (isTerminated())
/* 350 */             return true; 
/* 351 */           if (nanos <= 0L) {
/* 352 */             return false;
/*     */           }
/* 354 */           nanos = this.termination.awaitNanos(nanos);
/*     */         } 
/*     */       } finally {
/*     */         
/* 358 */         this.lock.unlock();
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private void startTask() {
/* 370 */       this.lock.lock();
/*     */       try {
/* 372 */         if (isShutdown()) {
/* 373 */           throw new RejectedExecutionException("Executor already shutdown");
/*     */         }
/* 375 */         this.runningTasks++;
/*     */       } finally {
/* 377 */         this.lock.unlock();
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private void endTask() {
/* 385 */       this.lock.lock();
/*     */       try {
/* 387 */         this.runningTasks--;
/* 388 */         if (isTerminated()) {
/* 389 */           this.termination.signalAll();
/*     */         }
/*     */       } finally {
/* 392 */         this.lock.unlock();
/*     */       } 
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
/*     */ 
/*     */     
/*     */     private DirectExecutorService() {}
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
/*     */   public static ListeningExecutorService newDirectExecutorService() {
/* 430 */     return new DirectExecutorService();
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
/*     */   public static Executor directExecutor() {
/* 450 */     return DirectExecutor.INSTANCE;
/*     */   }
/*     */   
/*     */   private enum DirectExecutor
/*     */     implements Executor {
/* 455 */     INSTANCE;
/*     */     public void execute(Runnable command) {
/* 457 */       command.run();
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
/*     */   public static ListeningExecutorService listeningDecorator(ExecutorService delegate) {
/* 481 */     return (delegate instanceof ListeningExecutorService) ? (ListeningExecutorService)delegate : ((delegate instanceof ScheduledExecutorService) ? new ScheduledListeningDecorator((ScheduledExecutorService)delegate) : new ListeningDecorator(delegate));
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
/*     */   public static ListeningScheduledExecutorService listeningDecorator(ScheduledExecutorService delegate) {
/* 509 */     return (delegate instanceof ListeningScheduledExecutorService) ? (ListeningScheduledExecutorService)delegate : new ScheduledListeningDecorator(delegate);
/*     */   }
/*     */ 
/*     */   
/*     */   private static class ListeningDecorator
/*     */     extends AbstractListeningExecutorService
/*     */   {
/*     */     private final ExecutorService delegate;
/*     */     
/*     */     ListeningDecorator(ExecutorService delegate) {
/* 519 */       this.delegate = (ExecutorService)Preconditions.checkNotNull(delegate);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
/* 525 */       return this.delegate.awaitTermination(timeout, unit);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isShutdown() {
/* 530 */       return this.delegate.isShutdown();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isTerminated() {
/* 535 */       return this.delegate.isTerminated();
/*     */     }
/*     */ 
/*     */     
/*     */     public void shutdown() {
/* 540 */       this.delegate.shutdown();
/*     */     }
/*     */ 
/*     */     
/*     */     public List<Runnable> shutdownNow() {
/* 545 */       return this.delegate.shutdownNow();
/*     */     }
/*     */ 
/*     */     
/*     */     public void execute(Runnable command) {
/* 550 */       this.delegate.execute(command);
/*     */     }
/*     */   }
/*     */   
/*     */   private static class ScheduledListeningDecorator
/*     */     extends ListeningDecorator
/*     */     implements ListeningScheduledExecutorService {
/*     */     final ScheduledExecutorService delegate;
/*     */     
/*     */     ScheduledListeningDecorator(ScheduledExecutorService delegate) {
/* 560 */       super(delegate);
/* 561 */       this.delegate = (ScheduledExecutorService)Preconditions.checkNotNull(delegate);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public ListenableScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
/* 567 */       ListenableFutureTask<Void> task = ListenableFutureTask.create(command, null);
/*     */       
/* 569 */       ScheduledFuture<?> scheduled = this.delegate.schedule(task, delay, unit);
/* 570 */       return new ListenableScheduledTask(task, scheduled);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public <V> ListenableScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit) {
/* 576 */       ListenableFutureTask<V> task = ListenableFutureTask.create(callable);
/* 577 */       ScheduledFuture<?> scheduled = this.delegate.schedule(task, delay, unit);
/* 578 */       return new ListenableScheduledTask<V>(task, scheduled);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public ListenableScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) {
/* 584 */       NeverSuccessfulListenableFutureTask task = new NeverSuccessfulListenableFutureTask(command);
/*     */       
/* 586 */       ScheduledFuture<?> scheduled = this.delegate.scheduleAtFixedRate(task, initialDelay, period, unit);
/*     */       
/* 588 */       return new ListenableScheduledTask(task, scheduled);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public ListenableScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit) {
/* 594 */       NeverSuccessfulListenableFutureTask task = new NeverSuccessfulListenableFutureTask(command);
/*     */       
/* 596 */       ScheduledFuture<?> scheduled = this.delegate.scheduleWithFixedDelay(task, initialDelay, delay, unit);
/*     */       
/* 598 */       return new ListenableScheduledTask(task, scheduled);
/*     */     }
/*     */ 
/*     */     
/*     */     private static final class ListenableScheduledTask<V>
/*     */       extends ForwardingListenableFuture.SimpleForwardingListenableFuture<V>
/*     */       implements ListenableScheduledFuture<V>
/*     */     {
/*     */       private final ScheduledFuture<?> scheduledDelegate;
/*     */ 
/*     */       
/*     */       public ListenableScheduledTask(ListenableFuture<V> listenableDelegate, ScheduledFuture<?> scheduledDelegate) {
/* 610 */         super(listenableDelegate);
/* 611 */         this.scheduledDelegate = scheduledDelegate;
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean cancel(boolean mayInterruptIfRunning) {
/* 616 */         boolean cancelled = super.cancel(mayInterruptIfRunning);
/* 617 */         if (cancelled)
/*     */         {
/* 619 */           this.scheduledDelegate.cancel(mayInterruptIfRunning);
/*     */         }
/*     */ 
/*     */         
/* 623 */         return cancelled;
/*     */       }
/*     */ 
/*     */       
/*     */       public long getDelay(TimeUnit unit) {
/* 628 */         return this.scheduledDelegate.getDelay(unit);
/*     */       }
/*     */ 
/*     */       
/*     */       public int compareTo(Delayed other) {
/* 633 */         return this.scheduledDelegate.compareTo(other);
/*     */       }
/*     */     }
/*     */     
/*     */     private static final class NeverSuccessfulListenableFutureTask
/*     */       extends AbstractFuture<Void>
/*     */       implements Runnable {
/*     */       private final Runnable delegate;
/*     */       
/*     */       public NeverSuccessfulListenableFutureTask(Runnable delegate) {
/* 643 */         this.delegate = (Runnable)Preconditions.checkNotNull(delegate);
/*     */       }
/*     */       
/*     */       public void run() {
/*     */         try {
/* 648 */           this.delegate.run();
/* 649 */         } catch (Throwable t) {
/* 650 */           setException(t);
/* 651 */           throw Throwables.propagate(t);
/*     */         } 
/*     */       }
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
/*     */   static <T> T invokeAnyImpl(ListeningExecutorService executorService, Collection<? extends Callable<T>> tasks, boolean timed, long nanos) throws InterruptedException, ExecutionException, TimeoutException {
/* 675 */     Preconditions.checkNotNull(executorService);
/* 676 */     int ntasks = tasks.size();
/* 677 */     Preconditions.checkArgument((ntasks > 0));
/* 678 */     List<Future<T>> futures = Lists.newArrayListWithCapacity(ntasks);
/* 679 */     BlockingQueue<Future<T>> futureQueue = Queues.newLinkedBlockingQueue();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static <T> ListenableFuture<T> submitAndAddQueueListener(ListeningExecutorService executorService, Callable<T> task, final BlockingQueue<Future<T>> queue) {
/* 748 */     final ListenableFuture<T> future = executorService.submit(task);
/* 749 */     future.addListener(new Runnable() {
/*     */           public void run() {
/* 751 */             queue.add(future);
/*     */           }
/*     */         },  directExecutor());
/* 754 */     return future;
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
/*     */   @Beta
/*     */   public static ThreadFactory platformThreadFactory() {
/* 767 */     if (!isAppEngine()) {
/* 768 */       return Executors.defaultThreadFactory();
/*     */     }
/*     */     try {
/* 771 */       return (ThreadFactory)Class.forName("com.google.appengine.api.ThreadManager").getMethod("currentRequestThreadFactory", new Class[0]).invoke(null, new Object[0]);
/*     */     
/*     */     }
/* 774 */     catch (IllegalAccessException e) {
/* 775 */       throw new RuntimeException("Couldn't invoke ThreadManager.currentRequestThreadFactory", e);
/* 776 */     } catch (ClassNotFoundException e) {
/* 777 */       throw new RuntimeException("Couldn't invoke ThreadManager.currentRequestThreadFactory", e);
/* 778 */     } catch (NoSuchMethodException e) {
/* 779 */       throw new RuntimeException("Couldn't invoke ThreadManager.currentRequestThreadFactory", e);
/* 780 */     } catch (InvocationTargetException e) {
/* 781 */       throw Throwables.propagate(e.getCause());
/*     */     } 
/*     */   }
/*     */   
/*     */   private static boolean isAppEngine() {
/* 786 */     if (System.getProperty("com.google.appengine.runtime.environment") == null) {
/* 787 */       return false;
/*     */     }
/*     */     
/*     */     try {
/* 791 */       return (Class.forName("com.google.apphosting.api.ApiProxy").getMethod("getCurrentEnvironment", new Class[0]).invoke(null, new Object[0]) != null);
/*     */     
/*     */     }
/* 794 */     catch (ClassNotFoundException e) {
/*     */       
/* 796 */       return false;
/* 797 */     } catch (InvocationTargetException e) {
/*     */       
/* 799 */       return false;
/* 800 */     } catch (IllegalAccessException e) {
/*     */       
/* 802 */       return false;
/* 803 */     } catch (NoSuchMethodException e) {
/*     */       
/* 805 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static Thread newThread(String name, Runnable runnable) {
/* 814 */     Preconditions.checkNotNull(name);
/* 815 */     Preconditions.checkNotNull(runnable);
/* 816 */     Thread result = platformThreadFactory().newThread(runnable);
/*     */     try {
/* 818 */       result.setName(name);
/* 819 */     } catch (SecurityException e) {}
/*     */ 
/*     */     
/* 822 */     return result;
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
/*     */   static Executor renamingDecorator(final Executor executor, final Supplier<String> nameSupplier) {
/* 841 */     Preconditions.checkNotNull(executor);
/* 842 */     Preconditions.checkNotNull(nameSupplier);
/* 843 */     if (isAppEngine())
/*     */     {
/* 845 */       return executor;
/*     */     }
/* 847 */     return new Executor() {
/*     */         public void execute(Runnable command) {
/* 849 */           executor.execute(Callables.threadRenaming(command, nameSupplier));
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static ExecutorService renamingDecorator(ExecutorService service, final Supplier<String> nameSupplier) {
/* 868 */     Preconditions.checkNotNull(service);
/* 869 */     Preconditions.checkNotNull(nameSupplier);
/* 870 */     if (isAppEngine())
/*     */     {
/* 872 */       return service;
/*     */     }
/* 874 */     return new WrappingExecutorService(service) {
/*     */         protected <T> Callable<T> wrapTask(Callable<T> callable) {
/* 876 */           return Callables.threadRenaming(callable, nameSupplier);
/*     */         }
/*     */         protected Runnable wrapTask(Runnable command) {
/* 879 */           return Callables.threadRenaming(command, nameSupplier);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static ScheduledExecutorService renamingDecorator(ScheduledExecutorService service, final Supplier<String> nameSupplier) {
/* 898 */     Preconditions.checkNotNull(service);
/* 899 */     Preconditions.checkNotNull(nameSupplier);
/* 900 */     if (isAppEngine())
/*     */     {
/* 902 */       return service;
/*     */     }
/* 904 */     return new WrappingScheduledExecutorService(service) {
/*     */         protected <T> Callable<T> wrapTask(Callable<T> callable) {
/* 906 */           return Callables.threadRenaming(callable, nameSupplier);
/*     */         }
/*     */         protected Runnable wrapTask(Runnable command) {
/* 909 */           return Callables.threadRenaming(command, nameSupplier);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Beta
/*     */   public static boolean shutdownAndAwaitTermination(ExecutorService service, long timeout, TimeUnit unit) {
/* 942 */     Preconditions.checkNotNull(unit);
/*     */     
/* 944 */     service.shutdown();
/*     */     try {
/* 946 */       long halfTimeoutNanos = TimeUnit.NANOSECONDS.convert(timeout, unit) / 2L;
/*     */       
/* 948 */       if (!service.awaitTermination(halfTimeoutNanos, TimeUnit.NANOSECONDS)) {
/*     */         
/* 950 */         service.shutdownNow();
/*     */         
/* 952 */         service.awaitTermination(halfTimeoutNanos, TimeUnit.NANOSECONDS);
/*     */       } 
/* 954 */     } catch (InterruptedException ie) {
/*     */       
/* 956 */       Thread.currentThread().interrupt();
/*     */       
/* 958 */       service.shutdownNow();
/*     */     } 
/* 960 */     return service.isTerminated();
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\commo\\util\concurrent\MoreExecutors.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */