/*     */ package com.google.common.collect;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.Deque;
/*     */ import java.util.Iterator;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class ForwardingDeque<E>
/*     */   extends ForwardingQueue<E>
/*     */   implements Deque<E>
/*     */ {
/*     */   public void addFirst(E e) {
/*  47 */     delegate().addFirst(e);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addLast(E e) {
/*  52 */     delegate().addLast(e);
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<E> descendingIterator() {
/*  57 */     return delegate().descendingIterator();
/*     */   }
/*     */ 
/*     */   
/*     */   public E getFirst() {
/*  62 */     return delegate().getFirst();
/*     */   }
/*     */ 
/*     */   
/*     */   public E getLast() {
/*  67 */     return delegate().getLast();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean offerFirst(E e) {
/*  72 */     return delegate().offerFirst(e);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean offerLast(E e) {
/*  77 */     return delegate().offerLast(e);
/*     */   }
/*     */ 
/*     */   
/*     */   public E peekFirst() {
/*  82 */     return delegate().peekFirst();
/*     */   }
/*     */ 
/*     */   
/*     */   public E peekLast() {
/*  87 */     return delegate().peekLast();
/*     */   }
/*     */ 
/*     */   
/*     */   public E pollFirst() {
/*  92 */     return delegate().pollFirst();
/*     */   }
/*     */ 
/*     */   
/*     */   public E pollLast() {
/*  97 */     return delegate().pollLast();
/*     */   }
/*     */ 
/*     */   
/*     */   public E pop() {
/* 102 */     return delegate().pop();
/*     */   }
/*     */ 
/*     */   
/*     */   public void push(E e) {
/* 107 */     delegate().push(e);
/*     */   }
/*     */ 
/*     */   
/*     */   public E removeFirst() {
/* 112 */     return delegate().removeFirst();
/*     */   }
/*     */ 
/*     */   
/*     */   public E removeLast() {
/* 117 */     return delegate().removeLast();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean removeFirstOccurrence(Object o) {
/* 122 */     return delegate().removeFirstOccurrence(o);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean removeLastOccurrence(Object o) {
/* 127 */     return delegate().removeLastOccurrence(o);
/*     */   }
/*     */   
/*     */   protected abstract Deque<E> delegate();
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\collect\ForwardingDeque.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */