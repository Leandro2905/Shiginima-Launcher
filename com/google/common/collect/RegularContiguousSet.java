/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.io.Serializable;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
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
/*     */ @GwtCompatible(emulated = true)
/*     */ final class RegularContiguousSet<C extends Comparable>
/*     */   extends ContiguousSet<C>
/*     */ {
/*     */   private final Range<C> range;
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   RegularContiguousSet(Range<C> range, DiscreteDomain<C> domain) {
/*  40 */     super(domain);
/*  41 */     this.range = range;
/*     */   }
/*     */   
/*     */   private ContiguousSet<C> intersectionInCurrentDomain(Range<C> other) {
/*  45 */     return this.range.isConnected(other) ? ContiguousSet.<C>create(this.range.intersection(other), this.domain) : new EmptyContiguousSet<C>(this.domain);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   ContiguousSet<C> headSetImpl(C toElement, boolean inclusive) {
/*  51 */     return intersectionInCurrentDomain((Range)Range.upTo((Comparable<?>)toElement, BoundType.forBoolean(inclusive)));
/*     */   }
/*     */ 
/*     */   
/*     */   ContiguousSet<C> subSetImpl(C fromElement, boolean fromInclusive, C toElement, boolean toInclusive) {
/*  56 */     if (fromElement.compareTo(toElement) == 0 && !fromInclusive && !toInclusive)
/*     */     {
/*  58 */       return new EmptyContiguousSet<C>(this.domain);
/*     */     }
/*  60 */     return intersectionInCurrentDomain((Range)Range.range((Comparable<?>)fromElement, BoundType.forBoolean(fromInclusive), (Comparable<?>)toElement, BoundType.forBoolean(toInclusive)));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   ContiguousSet<C> tailSetImpl(C fromElement, boolean inclusive) {
/*  66 */     return intersectionInCurrentDomain((Range)Range.downTo((Comparable<?>)fromElement, BoundType.forBoolean(inclusive)));
/*     */   }
/*     */   
/*     */   @GwtIncompatible("not used by GWT emulation")
/*     */   int indexOf(Object target) {
/*  71 */     return contains(target) ? (int)this.domain.distance(first(), (C)target) : -1;
/*     */   }
/*     */   
/*     */   public UnmodifiableIterator<C> iterator() {
/*  75 */     return new AbstractSequentialIterator<C>((Comparable)first()) {
/*  76 */         final C last = RegularContiguousSet.this.last();
/*     */ 
/*     */         
/*     */         protected C computeNext(C previous) {
/*  80 */           return RegularContiguousSet.equalsOrThrow((Comparable<?>)previous, (Comparable<?>)this.last) ? null : RegularContiguousSet.this.domain.next(previous);
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   @GwtIncompatible("NavigableSet")
/*     */   public UnmodifiableIterator<C> descendingIterator() {
/*  87 */     return new AbstractSequentialIterator<C>((Comparable)last()) {
/*  88 */         final C first = RegularContiguousSet.this.first();
/*     */ 
/*     */         
/*     */         protected C computeNext(C previous) {
/*  92 */           return RegularContiguousSet.equalsOrThrow((Comparable<?>)previous, (Comparable<?>)this.first) ? null : RegularContiguousSet.this.domain.previous(previous);
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   private static boolean equalsOrThrow(Comparable<?> left, @Nullable Comparable<?> right) {
/*  98 */     return (right != null && Range.compareOrThrow(left, right) == 0);
/*     */   }
/*     */   
/*     */   boolean isPartialView() {
/* 102 */     return false;
/*     */   }
/*     */   
/*     */   public C first() {
/* 106 */     return this.range.lowerBound.leastValueAbove(this.domain);
/*     */   }
/*     */   
/*     */   public C last() {
/* 110 */     return this.range.upperBound.greatestValueBelow(this.domain);
/*     */   }
/*     */   
/*     */   public int size() {
/* 114 */     long distance = this.domain.distance(first(), last());
/* 115 */     return (distance >= 2147483647L) ? Integer.MAX_VALUE : ((int)distance + 1);
/*     */   }
/*     */   
/*     */   public boolean contains(@Nullable Object object) {
/* 119 */     if (object == null) {
/* 120 */       return false;
/*     */     }
/*     */     try {
/* 123 */       return this.range.contains((C)object);
/* 124 */     } catch (ClassCastException e) {
/* 125 */       return false;
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean containsAll(Collection<?> targets) {
/* 130 */     return Collections2.containsAllImpl(this, targets);
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/* 134 */     return false;
/*     */   }
/*     */   
/*     */   public ContiguousSet<C> intersection(ContiguousSet<C> other) {
/* 138 */     Preconditions.checkNotNull(other);
/* 139 */     Preconditions.checkArgument(this.domain.equals(other.domain));
/* 140 */     if (other.isEmpty()) {
/* 141 */       return other;
/*     */     }
/* 143 */     Comparable<Comparable> comparable1 = (Comparable)Ordering.<Comparable>natural().max(first(), other.first());
/* 144 */     Comparable<Comparable> comparable2 = (Comparable)Ordering.<Comparable>natural().min(last(), other.last());
/* 145 */     return (comparable1.compareTo(comparable2) < 0) ? ContiguousSet.<C>create((Range)Range.closed(comparable1, comparable2), this.domain) : new EmptyContiguousSet<C>(this.domain);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Range<C> range() {
/* 152 */     return range(BoundType.CLOSED, BoundType.CLOSED);
/*     */   }
/*     */   
/*     */   public Range<C> range(BoundType lowerBoundType, BoundType upperBoundType) {
/* 156 */     return (Range)Range.create(this.range.lowerBound.withLowerBoundType(lowerBoundType, this.domain), this.range.upperBound.withUpperBoundType(upperBoundType, this.domain));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(@Nullable Object object) {
/* 161 */     if (object == this)
/* 162 */       return true; 
/* 163 */     if (object instanceof RegularContiguousSet) {
/* 164 */       RegularContiguousSet<?> that = (RegularContiguousSet)object;
/* 165 */       if (this.domain.equals(that.domain)) {
/* 166 */         return (first().equals(that.first()) && last().equals(that.last()));
/*     */       }
/*     */     } 
/*     */     
/* 170 */     return super.equals(object);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 175 */     return Sets.hashCodeImpl(this);
/*     */   }
/*     */   
/*     */   @GwtIncompatible("serialization")
/*     */   private static final class SerializedForm<C extends Comparable> implements Serializable {
/*     */     final Range<C> range;
/*     */     final DiscreteDomain<C> domain;
/*     */     
/*     */     private SerializedForm(Range<C> range, DiscreteDomain<C> domain) {
/* 184 */       this.range = range;
/* 185 */       this.domain = domain;
/*     */     }
/*     */     
/*     */     private Object readResolve() {
/* 189 */       return new RegularContiguousSet<C>(this.range, this.domain);
/*     */     }
/*     */   }
/*     */   
/*     */   @GwtIncompatible("serialization")
/*     */   Object writeReplace() {
/* 195 */     return new SerializedForm<Comparable>(this.range, this.domain);
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\collect\RegularContiguousSet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */