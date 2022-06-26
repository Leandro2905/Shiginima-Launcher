/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.TimeoutException;
/*     */ import javax.annotation.Nullable;
/*     */ import javax.annotation.concurrent.GuardedBy;
/*     */ import javax.annotation.concurrent.Immutable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public abstract class AbstractService
/*     */   implements Service
/*     */ {
/*  57 */   private static final ListenerCallQueue.Callback<Service.Listener> STARTING_CALLBACK = new ListenerCallQueue.Callback<Service.Listener>("starting()")
/*     */     {
/*     */       void call(Service.Listener listener) {
/*  60 */         listener.starting();
/*     */       }
/*     */     };
/*  63 */   private static final ListenerCallQueue.Callback<Service.Listener> RUNNING_CALLBACK = new ListenerCallQueue.Callback<Service.Listener>("running()")
/*     */     {
/*     */       void call(Service.Listener listener) {
/*  66 */         listener.running();
/*     */       }
/*     */     };
/*  69 */   private static final ListenerCallQueue.Callback<Service.Listener> STOPPING_FROM_STARTING_CALLBACK = stoppingCallback(Service.State.STARTING);
/*     */   
/*  71 */   private static final ListenerCallQueue.Callback<Service.Listener> STOPPING_FROM_RUNNING_CALLBACK = stoppingCallback(Service.State.RUNNING);
/*     */ 
/*     */   
/*  74 */   private static final ListenerCallQueue.Callback<Service.Listener> TERMINATED_FROM_NEW_CALLBACK = terminatedCallback(Service.State.NEW);
/*     */   
/*  76 */   private static final ListenerCallQueue.Callback<Service.Listener> TERMINATED_FROM_RUNNING_CALLBACK = terminatedCallback(Service.State.RUNNING);
/*     */   
/*  78 */   private static final ListenerCallQueue.Callback<Service.Listener> TERMINATED_FROM_STOPPING_CALLBACK = terminatedCallback(Service.State.STOPPING);
/*     */ 
/*     */   
/*     */   private static ListenerCallQueue.Callback<Service.Listener> terminatedCallback(final Service.State from) {
/*  82 */     String str = String.valueOf(String.valueOf(from)); return new ListenerCallQueue.Callback<Service.Listener>((new StringBuilder(21 + str.length())).append("terminated({from = ").append(str).append("})").toString()) {
/*     */         void call(Service.Listener listener) {
/*  84 */           listener.terminated(from);
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   private static ListenerCallQueue.Callback<Service.Listener> stoppingCallback(final Service.State from) {
/*  90 */     String str = String.valueOf(String.valueOf(from)); return new ListenerCallQueue.Callback<Service.Listener>((new StringBuilder(19 + str.length())).append("stopping({from = ").append(str).append("})").toString()) {
/*     */         void call(Service.Listener listener) {
/*  92 */           listener.stopping(from);
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*  97 */   private final Monitor monitor = new Monitor();
/*     */   
/*  99 */   private final Monitor.Guard isStartable = new Monitor.Guard(this.monitor) {
/*     */       public boolean isSatisfied() {
/* 101 */         return (AbstractService.this.state() == Service.State.NEW);
/*     */       }
/*     */     };
/*     */   
/* 105 */   private final Monitor.Guard isStoppable = new Monitor.Guard(this.monitor) {
/*     */       public boolean isSatisfied() {
/* 107 */         return (AbstractService.this.state().compareTo(Service.State.RUNNING) <= 0);
/*     */       }
/*     */     };
/*     */   
/* 111 */   private final Monitor.Guard hasReachedRunning = new Monitor.Guard(this.monitor) {
/*     */       public boolean isSatisfied() {
/* 113 */         return (AbstractService.this.state().compareTo(Service.State.RUNNING) >= 0);
/*     */       }
/*     */     };
/*     */   
/* 117 */   private final Monitor.Guard isStopped = new Monitor.Guard(this.monitor) {
/*     */       public boolean isSatisfied() {
/* 119 */         return AbstractService.this.state().isTerminal();
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */   
/*     */   @GuardedBy("monitor")
/* 126 */   private final List<ListenerCallQueue<Service.Listener>> listeners = Collections.synchronizedList(new ArrayList<ListenerCallQueue<Service.Listener>>());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GuardedBy("monitor")
/* 139 */   private volatile StateSnapshot snapshot = new StateSnapshot(Service.State.NEW);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract void doStart();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract void doStop();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Service startAsync() {
/* 170 */     if (this.monitor.enterIf(this.isStartable)) {
/*     */       try {
/* 172 */         this.snapshot = new StateSnapshot(Service.State.STARTING);
/* 173 */         starting();
/* 174 */         doStart();
/*     */       }
/* 176 */       catch (Throwable startupFailure) {
/* 177 */         notifyFailed(startupFailure);
/*     */       } finally {
/* 179 */         this.monitor.leave();
/* 180 */         executeListeners();
/*     */       } 
/*     */     } else {
/* 183 */       String str = String.valueOf(String.valueOf(this)); throw new IllegalStateException((new StringBuilder(33 + str.length())).append("Service ").append(str).append(" has already been started").toString());
/*     */     } 
/* 185 */     return this;
/*     */   }
/*     */   
/*     */   public final Service stopAsync() {
/* 189 */     if (this.monitor.enterIf(this.isStoppable)) {
/*     */       try {
/* 191 */         String str1, str2; Service.State previous = state();
/* 192 */         switch (previous) {
/*     */           case NEW:
/* 194 */             this.snapshot = new StateSnapshot(Service.State.TERMINATED);
/* 195 */             terminated(Service.State.NEW);
/*     */             break;
/*     */           case STARTING:
/* 198 */             this.snapshot = new StateSnapshot(Service.State.STARTING, true, null);
/* 199 */             stopping(Service.State.STARTING);
/*     */             break;
/*     */           case RUNNING:
/* 202 */             this.snapshot = new StateSnapshot(Service.State.STOPPING);
/* 203 */             stopping(Service.State.RUNNING);
/* 204 */             doStop();
/*     */             break;
/*     */           
/*     */           case STOPPING:
/*     */           case TERMINATED:
/*     */           case FAILED:
/* 210 */             str1 = String.valueOf(String.valueOf(previous)); throw new AssertionError((new StringBuilder(45 + str1.length())).append("isStoppable is incorrectly implemented, saw: ").append(str1).toString());
/*     */           default:
/* 212 */             str2 = String.valueOf(String.valueOf(previous)); throw new AssertionError((new StringBuilder(18 + str2.length())).append("Unexpected state: ").append(str2).toString());
/*     */         } 
/*     */ 
/*     */       
/* 216 */       } catch (Throwable shutdownFailure) {
/* 217 */         notifyFailed(shutdownFailure);
/*     */       } finally {
/* 219 */         this.monitor.leave();
/* 220 */         executeListeners();
/*     */       } 
/*     */     }
/* 223 */     return this;
/*     */   }
/*     */   
/*     */   public final void awaitRunning() {
/* 227 */     this.monitor.enterWhenUninterruptibly(this.hasReachedRunning);
/*     */     try {
/* 229 */       checkCurrentState(Service.State.RUNNING);
/*     */     } finally {
/* 231 */       this.monitor.leave();
/*     */     } 
/*     */   }
/*     */   
/*     */   public final void awaitRunning(long timeout, TimeUnit unit) throws TimeoutException {
/* 236 */     if (this.monitor.enterWhenUninterruptibly(this.hasReachedRunning, timeout, unit)) {
/*     */       try {
/* 238 */         checkCurrentState(Service.State.RUNNING);
/*     */       } finally {
/* 240 */         this.monitor.leave();
/*     */       
/*     */       }
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 247 */       String str1 = String.valueOf(String.valueOf(this)), str2 = String.valueOf(String.valueOf(state())); throw new TimeoutException((new StringBuilder(66 + str1.length() + str2.length())).append("Timed out waiting for ").append(str1).append(" to reach the RUNNING state. ").append("Current state: ").append(str2).toString());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final void awaitTerminated() {
/* 253 */     this.monitor.enterWhenUninterruptibly(this.isStopped);
/*     */     try {
/* 255 */       checkCurrentState(Service.State.TERMINATED);
/*     */     } finally {
/* 257 */       this.monitor.leave();
/*     */     } 
/*     */   }
/*     */   
/*     */   public final void awaitTerminated(long timeout, TimeUnit unit) throws TimeoutException {
/* 262 */     if (this.monitor.enterWhenUninterruptibly(this.isStopped, timeout, unit)) {
/*     */       try {
/* 264 */         checkCurrentState(Service.State.TERMINATED);
/*     */       } finally {
/* 266 */         this.monitor.leave();
/*     */       
/*     */       }
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 273 */       String str1 = String.valueOf(String.valueOf(this)), str2 = String.valueOf(String.valueOf(state())); throw new TimeoutException((new StringBuilder(65 + str1.length() + str2.length())).append("Timed out waiting for ").append(str1).append(" to reach a terminal state. ").append("Current state: ").append(str2).toString());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @GuardedBy("monitor")
/*     */   private void checkCurrentState(Service.State expected) {
/* 281 */     Service.State actual = state();
/* 282 */     if (actual != expected) {
/* 283 */       if (actual == Service.State.FAILED) {
/*     */         
/* 285 */         String str = String.valueOf(String.valueOf(expected)); throw new IllegalStateException((new StringBuilder(55 + str.length())).append("Expected the service to be ").append(str).append(", but the service has FAILED").toString(), failureCause());
/*     */       } 
/*     */       
/* 288 */       String str1 = String.valueOf(String.valueOf(expected)), str2 = String.valueOf(String.valueOf(actual)); throw new IllegalStateException((new StringBuilder(37 + str1.length() + str2.length())).append("Expected the service to be ").append(str1).append(", but was ").append(str2).toString());
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
/*     */   protected final void notifyStarted() {
/* 300 */     this.monitor.enter();
/*     */ 
/*     */     
/*     */     try {
/* 304 */       if (this.snapshot.state != Service.State.STARTING) {
/* 305 */         String str = String.valueOf(String.valueOf(this.snapshot.state)); IllegalStateException failure = new IllegalStateException((new StringBuilder(43 + str.length())).append("Cannot notifyStarted() when the service is ").append(str).toString());
/*     */         
/* 307 */         notifyFailed(failure);
/* 308 */         throw failure;
/*     */       } 
/*     */       
/* 311 */       if (this.snapshot.shutdownWhenStartupFinishes) {
/* 312 */         this.snapshot = new StateSnapshot(Service.State.STOPPING);
/*     */ 
/*     */         
/* 315 */         doStop();
/*     */       } else {
/* 317 */         this.snapshot = new StateSnapshot(Service.State.RUNNING);
/* 318 */         running();
/*     */       } 
/*     */     } finally {
/* 321 */       this.monitor.leave();
/* 322 */       executeListeners();
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
/*     */   protected final void notifyStopped() {
/* 334 */     this.monitor.enter();
/*     */ 
/*     */     
/*     */     try {
/* 338 */       Service.State previous = this.snapshot.state;
/* 339 */       if (previous != Service.State.STOPPING && previous != Service.State.RUNNING) {
/* 340 */         String str = String.valueOf(String.valueOf(previous)); IllegalStateException failure = new IllegalStateException((new StringBuilder(43 + str.length())).append("Cannot notifyStopped() when the service is ").append(str).toString());
/*     */         
/* 342 */         notifyFailed(failure);
/* 343 */         throw failure;
/*     */       } 
/* 345 */       this.snapshot = new StateSnapshot(Service.State.TERMINATED);
/* 346 */       terminated(previous);
/*     */     } finally {
/* 348 */       this.monitor.leave();
/* 349 */       executeListeners();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void notifyFailed(Throwable cause) {
/* 359 */     Preconditions.checkNotNull(cause);
/*     */     
/* 361 */     this.monitor.enter(); try {
/*     */       String str1, str2;
/* 363 */       Service.State previous = state();
/* 364 */       switch (previous) {
/*     */         case NEW:
/*     */         case TERMINATED:
/* 367 */           str1 = String.valueOf(String.valueOf(previous)); throw new IllegalStateException((new StringBuilder(22 + str1.length())).append("Failed while in state:").append(str1).toString(), cause);
/*     */         case STARTING:
/*     */         case RUNNING:
/*     */         case STOPPING:
/* 371 */           this.snapshot = new StateSnapshot(Service.State.FAILED, false, cause);
/* 372 */           failed(previous, cause);
/*     */           break;
/*     */         
/*     */         case FAILED:
/*     */           break;
/*     */         default:
/* 378 */           str2 = String.valueOf(String.valueOf(previous)); throw new AssertionError((new StringBuilder(18 + str2.length())).append("Unexpected state: ").append(str2).toString());
/*     */       } 
/*     */     } finally {
/* 381 */       this.monitor.leave();
/* 382 */       executeListeners();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isRunning() {
/* 388 */     return (state() == Service.State.RUNNING);
/*     */   }
/*     */ 
/*     */   
/*     */   public final Service.State state() {
/* 393 */     return this.snapshot.externalState();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Throwable failureCause() {
/* 401 */     return this.snapshot.failureCause();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void addListener(Service.Listener listener, Executor executor) {
/* 409 */     Preconditions.checkNotNull(listener, "listener");
/* 410 */     Preconditions.checkNotNull(executor, "executor");
/* 411 */     this.monitor.enter();
/*     */     try {
/* 413 */       if (!state().isTerminal()) {
/* 414 */         this.listeners.add(new ListenerCallQueue<Service.Listener>(listener, executor));
/*     */       }
/*     */     } finally {
/* 417 */       this.monitor.leave();
/*     */     } 
/*     */   }
/*     */   
/*     */   public String toString() {
/* 422 */     String str1 = String.valueOf(String.valueOf(getClass().getSimpleName())), str2 = String.valueOf(String.valueOf(state())); return (new StringBuilder(3 + str1.length() + str2.length())).append(str1).append(" [").append(str2).append("]").toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void executeListeners() {
/* 430 */     if (!this.monitor.isOccupiedByCurrentThread())
/*     */     {
/* 432 */       for (int i = 0; i < this.listeners.size(); i++) {
/* 433 */         ((ListenerCallQueue)this.listeners.get(i)).execute();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   @GuardedBy("monitor")
/*     */   private void starting() {
/* 440 */     STARTING_CALLBACK.enqueueOn(this.listeners);
/*     */   }
/*     */   
/*     */   @GuardedBy("monitor")
/*     */   private void running() {
/* 445 */     RUNNING_CALLBACK.enqueueOn(this.listeners);
/*     */   }
/*     */   
/*     */   @GuardedBy("monitor")
/*     */   private void stopping(Service.State from) {
/* 450 */     if (from == Service.State.STARTING) {
/* 451 */       STOPPING_FROM_STARTING_CALLBACK.enqueueOn(this.listeners);
/* 452 */     } else if (from == Service.State.RUNNING) {
/* 453 */       STOPPING_FROM_RUNNING_CALLBACK.enqueueOn(this.listeners);
/*     */     } else {
/* 455 */       throw new AssertionError();
/*     */     } 
/*     */   }
/*     */   
/*     */   @GuardedBy("monitor")
/*     */   private void terminated(Service.State from) {
/* 461 */     switch (from) {
/*     */       case NEW:
/* 463 */         TERMINATED_FROM_NEW_CALLBACK.enqueueOn(this.listeners);
/*     */         return;
/*     */       case RUNNING:
/* 466 */         TERMINATED_FROM_RUNNING_CALLBACK.enqueueOn(this.listeners);
/*     */         return;
/*     */       case STOPPING:
/* 469 */         TERMINATED_FROM_STOPPING_CALLBACK.enqueueOn(this.listeners);
/*     */         return;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 475 */     throw new AssertionError();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @GuardedBy("monitor")
/*     */   private void failed(final Service.State from, final Throwable cause) {
/* 482 */     String str1 = String.valueOf(String.valueOf(from)), str2 = String.valueOf(String.valueOf(cause)); (new ListenerCallQueue.Callback<Service.Listener>((new StringBuilder(27 + str1.length() + str2.length())).append("failed({from = ").append(str1).append(", cause = ").append(str2).append("})").toString()) {
/*     */         void call(Service.Listener listener) {
/* 484 */           listener.failed(from, cause);
/*     */         }
/*     */       }).enqueueOn(this.listeners);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Immutable
/*     */   private static final class StateSnapshot
/*     */   {
/*     */     final Service.State state;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     final boolean shutdownWhenStartupFinishes;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     final Throwable failure;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     StateSnapshot(Service.State internalState) {
/* 516 */       this(internalState, false, null);
/*     */     }
/*     */ 
/*     */     
/*     */     StateSnapshot(Service.State internalState, boolean shutdownWhenStartupFinishes, @Nullable Throwable failure) {
/* 521 */       Preconditions.checkArgument((!shutdownWhenStartupFinishes || internalState == Service.State.STARTING), "shudownWhenStartupFinishes can only be set if state is STARTING. Got %s instead.", new Object[] { internalState });
/*     */ 
/*     */       
/* 524 */       Preconditions.checkArgument(((((failure != null) ? 1 : 0) ^ ((internalState == Service.State.FAILED) ? 1 : 0)) == 0), "A failure cause should be set if and only if the state is failed.  Got %s and %s instead.", new Object[] { internalState, failure });
/*     */ 
/*     */       
/* 527 */       this.state = internalState;
/* 528 */       this.shutdownWhenStartupFinishes = shutdownWhenStartupFinishes;
/* 529 */       this.failure = failure;
/*     */     }
/*     */ 
/*     */     
/*     */     Service.State externalState() {
/* 534 */       if (this.shutdownWhenStartupFinishes && this.state == Service.State.STARTING) {
/* 535 */         return Service.State.STOPPING;
/*     */       }
/* 537 */       return this.state;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     Throwable failureCause() {
/* 543 */       Preconditions.checkState((this.state == Service.State.FAILED), "failureCause() is only valid if the service has failed, service is %s", new Object[] { this.state });
/*     */       
/* 545 */       return this.failure;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\commo\\util\concurrent\AbstractService.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */