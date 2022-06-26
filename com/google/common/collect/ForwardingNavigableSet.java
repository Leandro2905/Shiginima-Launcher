/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.NavigableSet;
/*     */ import java.util.Set;
/*     */ import java.util.SortedSet;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class ForwardingNavigableSet<E>
/*     */   extends ForwardingSortedSet<E>
/*     */   implements NavigableSet<E>
/*     */ {
/*     */   public E lower(E e) {
/*  58 */     return delegate().lower(e);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected E standardLower(E e) {
/*  67 */     return Iterators.getNext(headSet(e, false).descendingIterator(), null);
/*     */   }
/*     */ 
/*     */   
/*     */   public E floor(E e) {
/*  72 */     return delegate().floor(e);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected E standardFloor(E e) {
/*  81 */     return Iterators.getNext(headSet(e, true).descendingIterator(), null);
/*     */   }
/*     */ 
/*     */   
/*     */   public E ceiling(E e) {
/*  86 */     return delegate().ceiling(e);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected E standardCeiling(E e) {
/*  95 */     return Iterators.getNext(tailSet(e, true).iterator(), null);
/*     */   }
/*     */ 
/*     */   
/*     */   public E higher(E e) {
/* 100 */     return delegate().higher(e);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected E standardHigher(E e) {
/* 109 */     return Iterators.getNext(tailSet(e, false).iterator(), null);
/*     */   }
/*     */ 
/*     */   
/*     */   public E pollFirst() {
/* 114 */     return delegate().pollFirst();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected E standardPollFirst() {
/* 123 */     return Iterators.pollNext(iterator());
/*     */   }
/*     */ 
/*     */   
/*     */   public E pollLast() {
/* 128 */     return delegate().pollLast();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected E standardPollLast() {
/* 137 */     return Iterators.pollNext(descendingIterator());
/*     */   }
/*     */   
/*     */   protected E standardFirst() {
/* 141 */     return iterator().next();
/*     */   }
/*     */   
/*     */   protected E standardLast() {
/* 145 */     return descendingIterator().next();
/*     */   }
/*     */ 
/*     */   
/*     */   public NavigableSet<E> descendingSet() {
/* 150 */     return delegate().descendingSet();
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
/*     */   @Beta
/*     */   protected class StandardDescendingSet
/*     */     extends Sets.DescendingSet<E>
/*     */   {
/*     */     public StandardDescendingSet() {
/* 166 */       super(ForwardingNavigableSet.this);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<E> descendingIterator() {
/* 172 */     return delegate().descendingIterator();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NavigableSet<E> subSet(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive) {
/* 181 */     return delegate().subSet(fromElement, fromInclusive, toElement, toInclusive);
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
/*     */   @Beta
/*     */   protected NavigableSet<E> standardSubSet(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive) {
/* 195 */     return tailSet(fromElement, fromInclusive).headSet(toElement, toInclusive);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected SortedSet<E> standardSubSet(E fromElement, E toElement) {
/* 206 */     return subSet(fromElement, true, toElement, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public NavigableSet<E> headSet(E toElement, boolean inclusive) {
/* 211 */     return delegate().headSet(toElement, inclusive);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected SortedSet<E> standardHeadSet(E toElement) {
/* 221 */     return headSet(toElement, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public NavigableSet<E> tailSet(E fromElement, boolean inclusive) {
/* 226 */     return delegate().tailSet(fromElement, inclusive);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected SortedSet<E> standardTailSet(E fromElement) {
/* 236 */     return tailSet(fromElement, true);
/*     */   }
/*     */   
/*     */   protected abstract NavigableSet<E> delegate();
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\collect\ForwardingNavigableSet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */