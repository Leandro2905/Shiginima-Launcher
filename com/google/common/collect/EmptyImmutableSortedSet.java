/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Set;
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
/*     */ @GwtCompatible(serializable = true, emulated = true)
/*     */ class EmptyImmutableSortedSet<E>
/*     */   extends ImmutableSortedSet<E>
/*     */ {
/*     */   EmptyImmutableSortedSet(Comparator<? super E> comparator) {
/*  38 */     super(comparator);
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/*  43 */     return 0;
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/*  47 */     return true;
/*     */   }
/*     */   
/*     */   public boolean contains(@Nullable Object target) {
/*  51 */     return false;
/*     */   }
/*     */   
/*     */   public boolean containsAll(Collection<?> targets) {
/*  55 */     return targets.isEmpty();
/*     */   }
/*     */   
/*     */   public UnmodifiableIterator<E> iterator() {
/*  59 */     return Iterators.emptyIterator();
/*     */   }
/*     */   
/*     */   @GwtIncompatible("NavigableSet")
/*     */   public UnmodifiableIterator<E> descendingIterator() {
/*  64 */     return Iterators.emptyIterator();
/*     */   }
/*     */   
/*     */   boolean isPartialView() {
/*  68 */     return false;
/*     */   }
/*     */   
/*     */   public ImmutableList<E> asList() {
/*  72 */     return ImmutableList.of();
/*     */   }
/*     */ 
/*     */   
/*     */   int copyIntoArray(Object[] dst, int offset) {
/*  77 */     return offset;
/*     */   }
/*     */   
/*     */   public boolean equals(@Nullable Object object) {
/*  81 */     if (object instanceof Set) {
/*  82 */       Set<?> that = (Set)object;
/*  83 */       return that.isEmpty();
/*     */     } 
/*  85 */     return false;
/*     */   }
/*     */   
/*     */   public int hashCode() {
/*  89 */     return 0;
/*     */   }
/*     */   
/*     */   public String toString() {
/*  93 */     return "[]";
/*     */   }
/*     */ 
/*     */   
/*     */   public E first() {
/*  98 */     throw new NoSuchElementException();
/*     */   }
/*     */ 
/*     */   
/*     */   public E last() {
/* 103 */     throw new NoSuchElementException();
/*     */   }
/*     */ 
/*     */   
/*     */   ImmutableSortedSet<E> headSetImpl(E toElement, boolean inclusive) {
/* 108 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   ImmutableSortedSet<E> subSetImpl(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive) {
/* 114 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   ImmutableSortedSet<E> tailSetImpl(E fromElement, boolean inclusive) {
/* 119 */     return this;
/*     */   }
/*     */   
/*     */   int indexOf(@Nullable Object target) {
/* 123 */     return -1;
/*     */   }
/*     */ 
/*     */   
/*     */   ImmutableSortedSet<E> createDescendingSet() {
/* 128 */     return new EmptyImmutableSortedSet(Ordering.<E>from(this.comparator).reverse());
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\collect\EmptyImmutableSortedSet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */