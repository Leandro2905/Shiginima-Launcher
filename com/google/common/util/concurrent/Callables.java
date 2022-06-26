/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Supplier;
/*     */ import java.util.concurrent.Callable;
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
/*     */ public final class Callables
/*     */ {
/*     */   public static <T> Callable<T> returning(@Nullable final T value) {
/*  41 */     return new Callable<T>() {
/*     */         public T call() {
/*  43 */           return (T)value;
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
/*     */   static <T> Callable<T> threadRenaming(final Callable<T> callable, final Supplier<String> nameSupplier) {
/*  59 */     Preconditions.checkNotNull(nameSupplier);
/*  60 */     Preconditions.checkNotNull(callable);
/*  61 */     return new Callable<T>() {
/*     */         public T call() throws Exception {
/*  63 */           Thread currentThread = Thread.currentThread();
/*  64 */           String oldName = currentThread.getName();
/*  65 */           boolean restoreName = Callables.trySetName((String)nameSupplier.get(), currentThread);
/*     */           try {
/*  67 */             return (T)callable.call();
/*     */           } finally {
/*  69 */             if (restoreName) {
/*  70 */               Callables.trySetName(oldName, currentThread);
/*     */             }
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
/*     */ 
/*     */ 
/*     */   
/*     */   static Runnable threadRenaming(final Runnable task, final Supplier<String> nameSupplier) {
/*  87 */     Preconditions.checkNotNull(nameSupplier);
/*  88 */     Preconditions.checkNotNull(task);
/*  89 */     return new Runnable() {
/*     */         public void run() {
/*  91 */           Thread currentThread = Thread.currentThread();
/*  92 */           String oldName = currentThread.getName();
/*  93 */           boolean restoreName = Callables.trySetName((String)nameSupplier.get(), currentThread);
/*     */           try {
/*  95 */             task.run();
/*     */           } finally {
/*  97 */             if (restoreName) {
/*  98 */               Callables.trySetName(oldName, currentThread);
/*     */             }
/*     */           } 
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean trySetName(String threadName, Thread currentThread) {
/*     */     try {
/* 111 */       currentThread.setName(threadName);
/* 112 */       return true;
/* 113 */     } catch (SecurityException e) {
/* 114 */       return false;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\commo\\util\concurrent\Callables.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */