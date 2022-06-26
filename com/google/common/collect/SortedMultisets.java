/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.NavigableSet;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.SortedSet;
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
/*     */ 
/*     */ @GwtCompatible(emulated = true)
/*     */ final class SortedMultisets
/*     */ {
/*     */   static class ElementSet<E>
/*     */     extends Multisets.ElementSet<E>
/*     */     implements SortedSet<E>
/*     */   {
/*     */     private final SortedMultiset<E> multiset;
/*     */     
/*     */     ElementSet(SortedMultiset<E> multiset) {
/*  53 */       this.multiset = multiset;
/*     */     }
/*     */     
/*     */     final SortedMultiset<E> multiset() {
/*  57 */       return this.multiset;
/*     */     }
/*     */     
/*     */     public Comparator<? super E> comparator() {
/*  61 */       return multiset().comparator();
/*     */     }
/*     */     
/*     */     public SortedSet<E> subSet(E fromElement, E toElement) {
/*  65 */       return multiset().subMultiset(fromElement, BoundType.CLOSED, toElement, BoundType.OPEN).elementSet();
/*     */     }
/*     */     
/*     */     public SortedSet<E> headSet(E toElement) {
/*  69 */       return multiset().headMultiset(toElement, BoundType.OPEN).elementSet();
/*     */     }
/*     */     
/*     */     public SortedSet<E> tailSet(E fromElement) {
/*  73 */       return multiset().tailMultiset(fromElement, BoundType.CLOSED).elementSet();
/*     */     }
/*     */     
/*     */     public E first() {
/*  77 */       return SortedMultisets.getElementOrThrow(multiset().firstEntry());
/*     */     }
/*     */     
/*     */     public E last() {
/*  81 */       return SortedMultisets.getElementOrThrow(multiset().lastEntry());
/*     */     }
/*     */   }
/*     */   
/*     */   @GwtIncompatible("Navigable")
/*     */   static class NavigableElementSet<E>
/*     */     extends ElementSet<E>
/*     */     implements NavigableSet<E>
/*     */   {
/*     */     NavigableElementSet(SortedMultiset<E> multiset) {
/*  91 */       super(multiset);
/*     */     }
/*     */ 
/*     */     
/*     */     public E lower(E e) {
/*  96 */       return SortedMultisets.getElementOrNull(multiset().headMultiset(e, BoundType.OPEN).lastEntry());
/*     */     }
/*     */ 
/*     */     
/*     */     public E floor(E e) {
/* 101 */       return SortedMultisets.getElementOrNull(multiset().headMultiset(e, BoundType.CLOSED).lastEntry());
/*     */     }
/*     */ 
/*     */     
/*     */     public E ceiling(E e) {
/* 106 */       return SortedMultisets.getElementOrNull(multiset().tailMultiset(e, BoundType.CLOSED).firstEntry());
/*     */     }
/*     */ 
/*     */     
/*     */     public E higher(E e) {
/* 111 */       return SortedMultisets.getElementOrNull(multiset().tailMultiset(e, BoundType.OPEN).firstEntry());
/*     */     }
/*     */ 
/*     */     
/*     */     public NavigableSet<E> descendingSet() {
/* 116 */       return new NavigableElementSet(multiset().descendingMultiset());
/*     */     }
/*     */ 
/*     */     
/*     */     public Iterator<E> descendingIterator() {
/* 121 */       return descendingSet().iterator();
/*     */     }
/*     */ 
/*     */     
/*     */     public E pollFirst() {
/* 126 */       return SortedMultisets.getElementOrNull(multiset().pollFirstEntry());
/*     */     }
/*     */ 
/*     */     
/*     */     public E pollLast() {
/* 131 */       return SortedMultisets.getElementOrNull(multiset().pollLastEntry());
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public NavigableSet<E> subSet(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive) {
/* 137 */       return new NavigableElementSet(multiset().subMultiset(fromElement, BoundType.forBoolean(fromInclusive), toElement, BoundType.forBoolean(toInclusive)));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public NavigableSet<E> headSet(E toElement, boolean inclusive) {
/* 144 */       return new NavigableElementSet(multiset().headMultiset(toElement, BoundType.forBoolean(inclusive)));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public NavigableSet<E> tailSet(E fromElement, boolean inclusive) {
/* 150 */       return new NavigableElementSet(multiset().tailMultiset(fromElement, BoundType.forBoolean(inclusive)));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static <E> E getElementOrThrow(Multiset.Entry<E> entry) {
/* 156 */     if (entry == null) {
/* 157 */       throw new NoSuchElementException();
/*     */     }
/* 159 */     return entry.getElement();
/*     */   }
/*     */   
/*     */   private static <E> E getElementOrNull(@Nullable Multiset.Entry<E> entry) {
/* 163 */     return (entry == null) ? null : entry.getElement();
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\collect\SortedMultisets.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */