/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import java.io.Serializable;
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
/*     */ @GwtCompatible(emulated = true)
/*     */ final class EmptyContiguousSet<C extends Comparable>
/*     */   extends ContiguousSet<C>
/*     */ {
/*     */   EmptyContiguousSet(DiscreteDomain<C> domain) {
/*  34 */     super(domain);
/*     */   }
/*     */   
/*     */   public C first() {
/*  38 */     throw new NoSuchElementException();
/*     */   }
/*     */   
/*     */   public C last() {
/*  42 */     throw new NoSuchElementException();
/*     */   }
/*     */   
/*     */   public int size() {
/*  46 */     return 0;
/*     */   }
/*     */   
/*     */   public ContiguousSet<C> intersection(ContiguousSet<C> other) {
/*  50 */     return this;
/*     */   }
/*     */   
/*     */   public Range<C> range() {
/*  54 */     throw new NoSuchElementException();
/*     */   }
/*     */   
/*     */   public Range<C> range(BoundType lowerBoundType, BoundType upperBoundType) {
/*  58 */     throw new NoSuchElementException();
/*     */   }
/*     */   
/*     */   ContiguousSet<C> headSetImpl(C toElement, boolean inclusive) {
/*  62 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   ContiguousSet<C> subSetImpl(C fromElement, boolean fromInclusive, C toElement, boolean toInclusive) {
/*  67 */     return this;
/*     */   }
/*     */   
/*     */   ContiguousSet<C> tailSetImpl(C fromElement, boolean fromInclusive) {
/*  71 */     return this;
/*     */   }
/*     */   
/*     */   @GwtIncompatible("not used by GWT emulation")
/*     */   int indexOf(Object target) {
/*  76 */     return -1;
/*     */   }
/*     */   
/*     */   public UnmodifiableIterator<C> iterator() {
/*  80 */     return Iterators.emptyIterator();
/*     */   }
/*     */   
/*     */   @GwtIncompatible("NavigableSet")
/*     */   public UnmodifiableIterator<C> descendingIterator() {
/*  85 */     return Iterators.emptyIterator();
/*     */   }
/*     */   
/*     */   boolean isPartialView() {
/*  89 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/*  93 */     return true;
/*     */   }
/*     */   
/*     */   public ImmutableList<C> asList() {
/*  97 */     return ImmutableList.of();
/*     */   }
/*     */   
/*     */   public String toString() {
/* 101 */     return "[]";
/*     */   }
/*     */   
/*     */   public boolean equals(@Nullable Object object) {
/* 105 */     if (object instanceof Set) {
/* 106 */       Set<?> that = (Set)object;
/* 107 */       return that.isEmpty();
/*     */     } 
/* 109 */     return false;
/*     */   }
/*     */   
/*     */   public int hashCode() {
/* 113 */     return 0;
/*     */   }
/*     */   
/*     */   @GwtIncompatible("serialization")
/*     */   private static final class SerializedForm<C extends Comparable> implements Serializable {
/*     */     private final DiscreteDomain<C> domain;
/*     */     
/*     */     private SerializedForm(DiscreteDomain<C> domain) {
/* 121 */       this.domain = domain;
/*     */     }
/*     */     private static final long serialVersionUID = 0L;
/*     */     private Object readResolve() {
/* 125 */       return new EmptyContiguousSet<C>(this.domain);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible("serialization")
/*     */   Object writeReplace() {
/* 134 */     return new SerializedForm<Comparable>(this.domain);
/*     */   }
/*     */   
/*     */   @GwtIncompatible("NavigableSet")
/*     */   ImmutableSortedSet<C> createDescendingSet() {
/* 139 */     return new EmptyImmutableSortedSet<C>(Ordering.<Comparable>natural().reverse());
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\collect\EmptyContiguousSet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */