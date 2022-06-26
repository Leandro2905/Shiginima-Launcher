/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.util.NavigableSet;
/*     */ import java.util.NoSuchElementException;
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
/*     */ @Beta
/*     */ @GwtCompatible(emulated = true)
/*     */ public abstract class ContiguousSet<C extends Comparable>
/*     */   extends ImmutableSortedSet<C>
/*     */ {
/*     */   final DiscreteDomain<C> domain;
/*     */   
/*     */   public static <C extends Comparable> ContiguousSet<C> create(Range<C> range, DiscreteDomain<C> domain) {
/*  54 */     Preconditions.checkNotNull(range);
/*  55 */     Preconditions.checkNotNull(domain);
/*  56 */     Range<C> effectiveRange = range;
/*     */     try {
/*  58 */       if (!range.hasLowerBound()) {
/*  59 */         effectiveRange = effectiveRange.intersection((Range)Range.atLeast((Comparable<?>)domain.minValue()));
/*     */       }
/*  61 */       if (!range.hasUpperBound()) {
/*  62 */         effectiveRange = effectiveRange.intersection((Range)Range.atMost((Comparable<?>)domain.maxValue()));
/*     */       }
/*  64 */     } catch (NoSuchElementException e) {
/*  65 */       throw new IllegalArgumentException(e);
/*     */     } 
/*     */ 
/*     */     
/*  69 */     boolean empty = (effectiveRange.isEmpty() || Range.compareOrThrow((Comparable)range.lowerBound.leastValueAbove(domain), (Comparable)range.upperBound.greatestValueBelow(domain)) > 0);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  74 */     return empty ? new EmptyContiguousSet<C>(domain) : new RegularContiguousSet<C>(effectiveRange, domain);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   ContiguousSet(DiscreteDomain<C> domain) {
/*  82 */     super(Ordering.natural());
/*  83 */     this.domain = domain;
/*     */   }
/*     */   
/*     */   public ContiguousSet<C> headSet(C toElement) {
/*  87 */     return headSetImpl((C)Preconditions.checkNotNull(toElement), false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible("NavigableSet")
/*     */   public ContiguousSet<C> headSet(C toElement, boolean inclusive) {
/*  95 */     return headSetImpl((C)Preconditions.checkNotNull(toElement), inclusive);
/*     */   }
/*     */   
/*     */   public ContiguousSet<C> subSet(C fromElement, C toElement) {
/*  99 */     Preconditions.checkNotNull(fromElement);
/* 100 */     Preconditions.checkNotNull(toElement);
/* 101 */     Preconditions.checkArgument((comparator().compare(fromElement, toElement) <= 0));
/* 102 */     return subSetImpl(fromElement, true, toElement, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible("NavigableSet")
/*     */   public ContiguousSet<C> subSet(C fromElement, boolean fromInclusive, C toElement, boolean toInclusive) {
/* 111 */     Preconditions.checkNotNull(fromElement);
/* 112 */     Preconditions.checkNotNull(toElement);
/* 113 */     Preconditions.checkArgument((comparator().compare(fromElement, toElement) <= 0));
/* 114 */     return subSetImpl(fromElement, fromInclusive, toElement, toInclusive);
/*     */   }
/*     */   
/*     */   public ContiguousSet<C> tailSet(C fromElement) {
/* 118 */     return tailSetImpl((C)Preconditions.checkNotNull(fromElement), true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible("NavigableSet")
/*     */   public ContiguousSet<C> tailSet(C fromElement, boolean inclusive) {
/* 126 */     return tailSetImpl((C)Preconditions.checkNotNull(fromElement), inclusive);
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
/*     */   public String toString() {
/* 170 */     return range().toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static <E> ImmutableSortedSet.Builder<E> builder() {
/* 182 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   abstract ContiguousSet<C> headSetImpl(C paramC, boolean paramBoolean);
/*     */   
/*     */   abstract ContiguousSet<C> subSetImpl(C paramC1, boolean paramBoolean1, C paramC2, boolean paramBoolean2);
/*     */   
/*     */   abstract ContiguousSet<C> tailSetImpl(C paramC, boolean paramBoolean);
/*     */   
/*     */   public abstract ContiguousSet<C> intersection(ContiguousSet<C> paramContiguousSet);
/*     */   
/*     */   public abstract Range<C> range();
/*     */   
/*     */   public abstract Range<C> range(BoundType paramBoundType1, BoundType paramBoundType2);
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\collect\ContiguousSet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */