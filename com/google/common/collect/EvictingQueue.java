/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.Collection;
/*     */ import java.util.Queue;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ @GwtIncompatible("java.util.ArrayDeque")
/*     */ public final class EvictingQueue<E>
/*     */   extends ForwardingQueue<E>
/*     */   implements Serializable
/*     */ {
/*     */   private final Queue<E> delegate;
/*     */   @VisibleForTesting
/*     */   final int maxSize;
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   private EvictingQueue(int maxSize) {
/*  54 */     Preconditions.checkArgument((maxSize >= 0), "maxSize (%s) must >= 0", new Object[] { Integer.valueOf(maxSize) });
/*  55 */     this.delegate = new ArrayDeque<E>(maxSize);
/*  56 */     this.maxSize = maxSize;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> EvictingQueue<E> create(int maxSize) {
/*  66 */     return new EvictingQueue<E>(maxSize);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int remainingCapacity() {
/*  76 */     return this.maxSize - size();
/*     */   }
/*     */   
/*     */   protected Queue<E> delegate() {
/*  80 */     return this.delegate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean offer(E e) {
/*  90 */     return add(e);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean add(E e) {
/* 100 */     Preconditions.checkNotNull(e);
/* 101 */     if (this.maxSize == 0) {
/* 102 */       return true;
/*     */     }
/* 104 */     if (size() == this.maxSize) {
/* 105 */       this.delegate.remove();
/*     */     }
/* 107 */     this.delegate.add(e);
/* 108 */     return true;
/*     */   }
/*     */   
/*     */   public boolean addAll(Collection<? extends E> collection) {
/* 112 */     return standardAddAll(collection);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean contains(Object object) {
/* 117 */     return delegate().contains(Preconditions.checkNotNull(object));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean remove(Object object) {
/* 122 */     return delegate().remove(Preconditions.checkNotNull(object));
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\collect\EvictingQueue.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */