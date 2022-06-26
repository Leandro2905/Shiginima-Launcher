/*    */ package com.google.common.util.concurrent;
/*    */ 
/*    */ import java.util.concurrent.Callable;
/*    */ import java.util.concurrent.Executor;
/*    */ import java.util.concurrent.FutureTask;
/*    */ import javax.annotation.Nullable;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ListenableFutureTask<V>
/*    */   extends FutureTask<V>
/*    */   implements ListenableFuture<V>
/*    */ {
/* 43 */   private final ExecutionList executionList = new ExecutionList();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static <V> ListenableFutureTask<V> create(Callable<V> callable) {
/* 53 */     return new ListenableFutureTask<V>(callable);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static <V> ListenableFutureTask<V> create(Runnable runnable, @Nullable V result) {
/* 70 */     return new ListenableFutureTask<V>(runnable, result);
/*    */   }
/*    */   
/*    */   ListenableFutureTask(Callable<V> callable) {
/* 74 */     super(callable);
/*    */   }
/*    */   
/*    */   ListenableFutureTask(Runnable runnable, @Nullable V result) {
/* 78 */     super(runnable, result);
/*    */   }
/*    */ 
/*    */   
/*    */   public void addListener(Runnable listener, Executor exec) {
/* 83 */     this.executionList.add(listener, exec);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void done() {
/* 91 */     this.executionList.execute();
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\commo\\util\concurrent\ListenableFutureTask.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */