/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.util.ArrayDeque;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class SerializingExecutor
/*     */   implements Executor
/*     */ {
/*  47 */   private static final Logger log = Logger.getLogger(SerializingExecutor.class.getName());
/*     */ 
/*     */   
/*     */   private final Executor executor;
/*     */ 
/*     */   
/*     */   @GuardedBy("internalLock")
/*  54 */   private final Queue<Runnable> waitQueue = new ArrayDeque<Runnable>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GuardedBy("internalLock")
/*     */   private boolean isThreadScheduled = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  69 */   private final TaskRunner taskRunner = new TaskRunner();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final Object internalLock;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SerializingExecutor(Executor executor) {
/*  81 */     this.internalLock = new Object() {
/*     */         public String toString() {
/*  83 */           String.valueOf(super.toString()); return (String.valueOf(super.toString()).length() != 0) ? "SerializingExecutor lock: ".concat(String.valueOf(super.toString())) : new String("SerializingExecutor lock: ");
/*     */         }
/*     */       };
/*     */     Preconditions.checkNotNull(executor, "'executor' must not be null.");
/*     */     this.executor = executor;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute(Runnable r) {
/*  93 */     Preconditions.checkNotNull(r, "'r' must not be null.");
/*  94 */     boolean scheduleTaskRunner = false;
/*  95 */     synchronized (this.internalLock) {
/*  96 */       this.waitQueue.add(r);
/*     */       
/*  98 */       if (!this.isThreadScheduled) {
/*  99 */         this.isThreadScheduled = true;
/* 100 */         scheduleTaskRunner = true;
/*     */       } 
/*     */     } 
/* 103 */     if (scheduleTaskRunner) {
/* 104 */       boolean threw = true;
/*     */       try {
/* 106 */         this.executor.execute(this.taskRunner);
/* 107 */         threw = false;
/*     */       } finally {
/* 109 */         if (threw) {
/* 110 */           synchronized (this.internalLock) {
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 115 */             this.isThreadScheduled = false;
/*     */           } 
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private class TaskRunner
/*     */     implements Runnable
/*     */   {
/*     */     private TaskRunner() {}
/*     */ 
/*     */ 
/*     */     
/*     */     public void run() {
/* 132 */       boolean stillRunning = true; try {
/*     */         while (true) {
/*     */           Runnable nextToRun;
/* 135 */           Preconditions.checkState(SerializingExecutor.this.isThreadScheduled);
/*     */           
/* 137 */           synchronized (SerializingExecutor.this.internalLock) {
/* 138 */             nextToRun = SerializingExecutor.this.waitQueue.poll();
/* 139 */             if (nextToRun == null) {
/* 140 */               SerializingExecutor.this.isThreadScheduled = false;
/* 141 */               stillRunning = false;
/*     */               
/*     */               break;
/*     */             } 
/*     */           } 
/*     */           
/*     */           try {
/* 148 */             nextToRun.run();
/* 149 */           } catch (RuntimeException e) {
/*     */             
/* 151 */             String str = String.valueOf(String.valueOf(nextToRun)); SerializingExecutor.log.log(Level.SEVERE, (new StringBuilder(35 + str.length())).append("Exception while executing runnable ").append(str).toString(), e);
/*     */           } 
/*     */         } 
/*     */       } finally {
/*     */         
/* 156 */         if (stillRunning)
/*     */         {
/*     */ 
/*     */           
/* 160 */           synchronized (SerializingExecutor.this.internalLock) {
/* 161 */             SerializingExecutor.this.isThreadScheduled = false;
/*     */           } 
/*     */         }
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\commo\\util\concurrent\SerializingExecutor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */