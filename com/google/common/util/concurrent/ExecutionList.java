/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.annotation.Nullable;
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
/*     */ 
/*     */ 
/*     */ public final class ExecutionList
/*     */ {
/*     */   @VisibleForTesting
/*  49 */   static final Logger log = Logger.getLogger(ExecutionList.class.getName());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GuardedBy("this")
/*     */   private RunnableExecutorPair runnables;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GuardedBy("this")
/*     */   private boolean executed;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void add(Runnable runnable, Executor executor) {
/*  85 */     Preconditions.checkNotNull(runnable, "Runnable was null.");
/*  86 */     Preconditions.checkNotNull(executor, "Executor was null.");
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  91 */     synchronized (this) {
/*  92 */       if (!this.executed) {
/*  93 */         this.runnables = new RunnableExecutorPair(runnable, executor, this.runnables);
/*     */ 
/*     */         
/*     */         return;
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 101 */     executeListener(runnable, executor);
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
/*     */   public void execute() {
/*     */     RunnableExecutorPair list;
/* 120 */     synchronized (this) {
/* 121 */       if (this.executed) {
/*     */         return;
/*     */       }
/* 124 */       this.executed = true;
/* 125 */       list = this.runnables;
/* 126 */       this.runnables = null;
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
/* 137 */     RunnableExecutorPair reversedList = null;
/* 138 */     while (list != null) {
/* 139 */       RunnableExecutorPair tmp = list;
/* 140 */       list = list.next;
/* 141 */       tmp.next = reversedList;
/* 142 */       reversedList = tmp;
/*     */     } 
/* 144 */     while (reversedList != null) {
/* 145 */       executeListener(reversedList.runnable, reversedList.executor);
/* 146 */       reversedList = reversedList.next;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void executeListener(Runnable runnable, Executor executor) {
/*     */     try {
/* 156 */       executor.execute(runnable);
/* 157 */     } catch (RuntimeException e) {
/*     */ 
/*     */ 
/*     */       
/* 161 */       String str1 = String.valueOf(String.valueOf(runnable)), str2 = String.valueOf(String.valueOf(executor)); log.log(Level.SEVERE, (new StringBuilder(57 + str1.length() + str2.length())).append("RuntimeException while executing runnable ").append(str1).append(" with executor ").append(str2).toString(), e);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static final class RunnableExecutorPair {
/*     */     final Runnable runnable;
/*     */     final Executor executor;
/*     */     @Nullable
/*     */     RunnableExecutorPair next;
/*     */     
/*     */     RunnableExecutorPair(Runnable runnable, Executor executor, RunnableExecutorPair next) {
/* 172 */       this.runnable = runnable;
/* 173 */       this.executor = executor;
/* 174 */       this.next = next;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\commo\\util\concurrent\ExecutionList.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */