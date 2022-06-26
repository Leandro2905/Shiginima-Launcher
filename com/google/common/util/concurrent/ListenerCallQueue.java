/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.Queues;
/*     */ import java.util.Queue;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.annotation.concurrent.GuardedBy;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class ListenerCallQueue<L>
/*     */   implements Runnable
/*     */ {
/*  40 */   private static final Logger logger = Logger.getLogger(ListenerCallQueue.class.getName());
/*     */   private final L listener;
/*     */   private final Executor executor;
/*     */   
/*     */   static abstract class Callback<L> {
/*     */     Callback(String methodCall) {
/*  46 */       this.methodCall = methodCall;
/*     */     }
/*     */ 
/*     */     
/*     */     private final String methodCall;
/*     */     
/*     */     void enqueueOn(Iterable<ListenerCallQueue<L>> queues) {
/*  53 */       for (ListenerCallQueue<L> queue : queues) {
/*  54 */         queue.add(this);
/*     */       }
/*     */     }
/*     */     
/*     */     abstract void call(L param1L);
/*     */   }
/*     */   
/*     */   @GuardedBy("this")
/*  62 */   private final Queue<Callback<L>> waitQueue = Queues.newArrayDeque();
/*     */ 
/*     */   
/*     */   ListenerCallQueue(L listener, Executor executor) {
/*  66 */     this.listener = (L)Preconditions.checkNotNull(listener);
/*  67 */     this.executor = (Executor)Preconditions.checkNotNull(executor);
/*     */   }
/*     */   @GuardedBy("this")
/*     */   private boolean isThreadScheduled;
/*     */   synchronized void add(Callback<L> callback) {
/*  72 */     this.waitQueue.add(callback);
/*     */   }
/*     */ 
/*     */   
/*     */   void execute() {
/*  77 */     boolean scheduleTaskRunner = false;
/*  78 */     synchronized (this) {
/*  79 */       if (!this.isThreadScheduled) {
/*  80 */         this.isThreadScheduled = true;
/*  81 */         scheduleTaskRunner = true;
/*     */       } 
/*     */     } 
/*  84 */     if (scheduleTaskRunner) {
/*     */       try {
/*  86 */         this.executor.execute(this);
/*  87 */       } catch (RuntimeException e) {
/*     */         
/*  89 */         synchronized (this) {
/*  90 */           this.isThreadScheduled = false;
/*     */         } 
/*     */         
/*  93 */         String str1 = String.valueOf(String.valueOf(this.listener)), str2 = String.valueOf(String.valueOf(this.executor)); logger.log(Level.SEVERE, (new StringBuilder(42 + str1.length() + str2.length())).append("Exception while running callbacks for ").append(str1).append(" on ").append(str2).toString(), e);
/*     */ 
/*     */         
/*  96 */         throw e;
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   public void run() {
/* 102 */     boolean stillRunning = true;
/*     */     try {
/*     */       while (true) {
/*     */         Callback<L> nextToRun;
/* 106 */         synchronized (this) {
/* 107 */           Preconditions.checkState(this.isThreadScheduled);
/* 108 */           nextToRun = this.waitQueue.poll();
/* 109 */           if (nextToRun == null) {
/* 110 */             this.isThreadScheduled = false;
/* 111 */             stillRunning = false;
/*     */             
/*     */             break;
/*     */           } 
/*     */         } 
/*     */         
/*     */         try {
/* 118 */           nextToRun.call(this.listener);
/* 119 */         } catch (RuntimeException e) {
/*     */           
/* 121 */           String str1 = String.valueOf(String.valueOf(this.listener)), str2 = String.valueOf(String.valueOf(nextToRun.methodCall)); logger.log(Level.SEVERE, (new StringBuilder(37 + str1.length() + str2.length())).append("Exception while executing callback: ").append(str1).append(".").append(str2).toString(), e);
/*     */         }
/*     */       
/*     */       } 
/*     */     } finally {
/*     */       
/* 127 */       if (stillRunning)
/*     */       {
/*     */ 
/*     */         
/* 131 */         synchronized (this) {
/* 132 */           this.isThreadScheduled = false;
/*     */         } 
/*     */       }
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\commo\\util\concurrent\ListenerCallQueue.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */