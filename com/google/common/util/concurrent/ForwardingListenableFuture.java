/*    */ package com.google.common.util.concurrent;
/*    */ 
/*    */ import com.google.common.base.Preconditions;
/*    */ import java.util.concurrent.Executor;
/*    */ import java.util.concurrent.Future;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class ForwardingListenableFuture<V>
/*    */   extends ForwardingFuture<V>
/*    */   implements ListenableFuture<V>
/*    */ {
/*    */   public void addListener(Runnable listener, Executor exec) {
/* 47 */     delegate().addListener(listener, exec);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected abstract ListenableFuture<V> delegate();
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static abstract class SimpleForwardingListenableFuture<V>
/*    */     extends ForwardingListenableFuture<V>
/*    */   {
/*    */     private final ListenableFuture<V> delegate;
/*    */ 
/*    */ 
/*    */     
/*    */     protected SimpleForwardingListenableFuture(ListenableFuture<V> delegate) {
/* 66 */       this.delegate = (ListenableFuture<V>)Preconditions.checkNotNull(delegate);
/*    */     }
/*    */ 
/*    */     
/*    */     protected final ListenableFuture<V> delegate() {
/* 71 */       return this.delegate;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\commo\\util\concurrent\ForwardingListenableFuture.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */