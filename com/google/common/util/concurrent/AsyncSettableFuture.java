/*    */ package com.google.common.util.concurrent;
/*    */ 
/*    */ import com.google.common.base.Preconditions;
/*    */ import java.util.concurrent.Future;
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
/*    */ final class AsyncSettableFuture<V>
/*    */   extends ForwardingListenableFuture<V>
/*    */ {
/*    */   public static <V> AsyncSettableFuture<V> create() {
/* 41 */     return new AsyncSettableFuture<V>();
/*    */   }
/*    */   
/* 44 */   private final NestedFuture<V> nested = new NestedFuture<V>();
/* 45 */   private final ListenableFuture<V> dereferenced = Futures.dereference(this.nested);
/*    */ 
/*    */ 
/*    */   
/*    */   protected ListenableFuture<V> delegate() {
/* 50 */     return this.dereferenced;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean setFuture(ListenableFuture<? extends V> future) {
/* 58 */     return this.nested.setFuture((ListenableFuture<? extends V>)Preconditions.checkNotNull(future));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean setValue(@Nullable V value) {
/* 67 */     return setFuture(Futures.immediateFuture(value));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean setException(Throwable exception) {
/* 76 */     return setFuture(Futures.immediateFailedFuture(exception));
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
/*    */   public boolean isSet() {
/* 88 */     return this.nested.isDone();
/*    */   }
/*    */   
/*    */   private static final class NestedFuture<V> extends AbstractFuture<ListenableFuture<? extends V>> {
/*    */     boolean setFuture(ListenableFuture<? extends V> value) {
/* 93 */       boolean result = set(value);
/* 94 */       if (isCancelled()) {
/* 95 */         value.cancel(wasInterrupted());
/*    */       }
/* 97 */       return result;
/*    */     }
/*    */     
/*    */     private NestedFuture() {}
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\commo\\util\concurrent\AsyncSettableFuture.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */