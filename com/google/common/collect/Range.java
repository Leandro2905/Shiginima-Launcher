/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Predicate;
/*     */ import java.io.Serializable;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
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
/*     */ @GwtCompatible
/*     */ public final class Range<C extends Comparable>
/*     */   implements Predicate<C>, Serializable
/*     */ {
/* 117 */   private static final Function<Range, Cut> LOWER_BOUND_FN = new Function<Range, Cut>()
/*     */     {
/*     */       public Cut apply(Range range) {
/* 120 */         return range.lowerBound;
/*     */       }
/*     */     };
/*     */ 
/*     */   
/*     */   static <C extends Comparable<?>> Function<Range<C>, Cut<C>> lowerBoundFn() {
/* 126 */     return (Function)LOWER_BOUND_FN;
/*     */   }
/*     */   
/* 129 */   private static final Function<Range, Cut> UPPER_BOUND_FN = new Function<Range, Cut>()
/*     */     {
/*     */       public Cut apply(Range range) {
/* 132 */         return range.upperBound;
/*     */       }
/*     */     };
/*     */ 
/*     */   
/*     */   static <C extends Comparable<?>> Function<Range<C>, Cut<C>> upperBoundFn() {
/* 138 */     return (Function)UPPER_BOUND_FN;
/*     */   }
/*     */   
/* 141 */   static final Ordering<Range<?>> RANGE_LEX_ORDERING = new Ordering<Range<?>>()
/*     */     {
/*     */       public int compare(Range<?> left, Range<?> right) {
/* 144 */         return ComparisonChain.start().compare(left.lowerBound, right.lowerBound).compare(left.upperBound, right.upperBound).result();
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <C extends Comparable<?>> Range<C> create(Cut<C> lowerBound, Cut<C> upperBound) {
/* 153 */     return (Range)new Range<Comparable>(lowerBound, upperBound);
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
/*     */   public static <C extends Comparable<?>> Range<C> open(C lower, C upper) {
/* 165 */     return create((Cut)Cut.aboveValue((Comparable)lower), (Cut)Cut.belowValue((Comparable)upper));
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
/*     */   public static <C extends Comparable<?>> Range<C> closed(C lower, C upper) {
/* 177 */     return create((Cut)Cut.belowValue((Comparable)lower), (Cut)Cut.aboveValue((Comparable)upper));
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
/*     */   public static <C extends Comparable<?>> Range<C> closedOpen(C lower, C upper) {
/* 190 */     return create((Cut)Cut.belowValue((Comparable)lower), (Cut)Cut.belowValue((Comparable)upper));
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
/*     */   public static <C extends Comparable<?>> Range<C> openClosed(C lower, C upper) {
/* 203 */     return create((Cut)Cut.aboveValue((Comparable)lower), (Cut)Cut.aboveValue((Comparable)upper));
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
/*     */   public static <C extends Comparable<?>> Range<C> range(C lower, BoundType lowerType, C upper, BoundType upperType) {
/* 217 */     Preconditions.checkNotNull(lowerType);
/* 218 */     Preconditions.checkNotNull(upperType);
/*     */     
/* 220 */     Cut<C> lowerBound = (lowerType == BoundType.OPEN) ? (Cut)Cut.<Comparable>aboveValue((Comparable)lower) : (Cut)Cut.<Comparable>belowValue((Comparable)lower);
/*     */ 
/*     */     
/* 223 */     Cut<C> upperBound = (upperType == BoundType.OPEN) ? (Cut)Cut.<Comparable>belowValue((Comparable)upper) : (Cut)Cut.<Comparable>aboveValue((Comparable)upper);
/*     */ 
/*     */     
/* 226 */     return create(lowerBound, upperBound);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <C extends Comparable<?>> Range<C> lessThan(C endpoint) {
/* 236 */     return create((Cut)Cut.belowAll(), (Cut)Cut.belowValue((Comparable)endpoint));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <C extends Comparable<?>> Range<C> atMost(C endpoint) {
/* 246 */     return create((Cut)Cut.belowAll(), (Cut)Cut.aboveValue((Comparable)endpoint));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <C extends Comparable<?>> Range<C> upTo(C endpoint, BoundType boundType) {
/* 257 */     switch (boundType) {
/*     */       case OPEN:
/* 259 */         return lessThan(endpoint);
/*     */       case CLOSED:
/* 261 */         return atMost(endpoint);
/*     */     } 
/* 263 */     throw new AssertionError();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <C extends Comparable<?>> Range<C> greaterThan(C endpoint) {
/* 274 */     return create((Cut)Cut.aboveValue((Comparable)endpoint), (Cut)Cut.aboveAll());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <C extends Comparable<?>> Range<C> atLeast(C endpoint) {
/* 284 */     return create((Cut)Cut.belowValue((Comparable)endpoint), (Cut)Cut.aboveAll());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <C extends Comparable<?>> Range<C> downTo(C endpoint, BoundType boundType) {
/* 295 */     switch (boundType) {
/*     */       case OPEN:
/* 297 */         return greaterThan(endpoint);
/*     */       case CLOSED:
/* 299 */         return atLeast(endpoint);
/*     */     } 
/* 301 */     throw new AssertionError();
/*     */   }
/*     */ 
/*     */   
/* 305 */   private static final Range<Comparable> ALL = new Range((Cut)Cut.belowAll(), (Cut)Cut.aboveAll());
/*     */   
/*     */   final Cut<C> lowerBound;
/*     */   
/*     */   final Cut<C> upperBound;
/*     */   
/*     */   private static final long serialVersionUID = 0L;
/*     */ 
/*     */   
/*     */   public static <C extends Comparable<?>> Range<C> all() {
/* 315 */     return (Range)ALL;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <C extends Comparable<?>> Range<C> singleton(C value) {
/* 326 */     return closed(value, value);
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
/*     */   public static <C extends Comparable<?>> Range<C> encloseAll(Iterable<C> values) {
/* 342 */     Preconditions.checkNotNull(values);
/* 343 */     if (values instanceof ContiguousSet) {
/* 344 */       return ((ContiguousSet)values).range();
/*     */     }
/* 346 */     Iterator<C> valueIterator = values.iterator();
/* 347 */     Comparable comparable1 = (Comparable)Preconditions.checkNotNull(valueIterator.next());
/* 348 */     Comparable comparable2 = comparable1;
/* 349 */     while (valueIterator.hasNext()) {
/* 350 */       Comparable comparable = (Comparable)Preconditions.checkNotNull(valueIterator.next());
/* 351 */       comparable1 = (Comparable)Ordering.<Comparable>natural().min(comparable1, comparable);
/* 352 */       comparable2 = (Comparable)Ordering.<Comparable>natural().max(comparable2, comparable);
/*     */     } 
/* 354 */     return closed((C)comparable1, (C)comparable2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Range(Cut<C> lowerBound, Cut<C> upperBound) {
/* 361 */     if (lowerBound.compareTo(upperBound) > 0 || lowerBound == Cut.aboveAll() || upperBound == Cut.belowAll()) {
/*     */       
/* 363 */       String.valueOf(toString(lowerBound, upperBound)); throw new IllegalArgumentException((String.valueOf(toString(lowerBound, upperBound)).length() != 0) ? "Invalid range: ".concat(String.valueOf(toString(lowerBound, upperBound))) : new String("Invalid range: "));
/*     */     } 
/* 365 */     this.lowerBound = (Cut<C>)Preconditions.checkNotNull(lowerBound);
/* 366 */     this.upperBound = (Cut<C>)Preconditions.checkNotNull(upperBound);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasLowerBound() {
/* 373 */     return (this.lowerBound != Cut.belowAll());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public C lowerEndpoint() {
/* 383 */     return this.lowerBound.endpoint();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BoundType lowerBoundType() {
/* 394 */     return this.lowerBound.typeAsLowerBound();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasUpperBound() {
/* 401 */     return (this.upperBound != Cut.aboveAll());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public C upperEndpoint() {
/* 411 */     return this.upperBound.endpoint();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BoundType upperBoundType() {
/* 422 */     return this.upperBound.typeAsUpperBound();
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
/*     */   public boolean isEmpty() {
/* 435 */     return this.lowerBound.equals(this.upperBound);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean contains(C value) {
/* 444 */     Preconditions.checkNotNull(value);
/*     */     
/* 446 */     return (this.lowerBound.isLessThan(value) && !this.upperBound.isLessThan(value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public boolean apply(C input) {
/* 456 */     return contains(input);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean containsAll(Iterable<? extends C> values) {
/* 464 */     if (Iterables.isEmpty(values)) {
/* 465 */       return true;
/*     */     }
/*     */ 
/*     */     
/* 469 */     if (values instanceof SortedSet) {
/* 470 */       SortedSet<? extends C> set = cast(values);
/* 471 */       Comparator<?> comparator = set.comparator();
/* 472 */       if (Ordering.<Comparable>natural().equals(comparator) || comparator == null) {
/* 473 */         return (contains(set.first()) && contains(set.last()));
/*     */       }
/*     */     } 
/*     */     
/* 477 */     for (Comparable comparable : values) {
/* 478 */       if (!contains((C)comparable)) {
/* 479 */         return false;
/*     */       }
/*     */     } 
/* 482 */     return true;
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
/*     */   public boolean encloses(Range<C> other) {
/* 510 */     return (this.lowerBound.compareTo(other.lowerBound) <= 0 && this.upperBound.compareTo(other.upperBound) >= 0);
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
/*     */   public boolean isConnected(Range<C> other) {
/* 539 */     return (this.lowerBound.compareTo(other.upperBound) <= 0 && other.lowerBound.compareTo(this.upperBound) <= 0);
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
/*     */   public Range<C> intersection(Range<C> connectedRange) {
/* 560 */     int lowerCmp = this.lowerBound.compareTo(connectedRange.lowerBound);
/* 561 */     int upperCmp = this.upperBound.compareTo(connectedRange.upperBound);
/* 562 */     if (lowerCmp >= 0 && upperCmp <= 0)
/* 563 */       return this; 
/* 564 */     if (lowerCmp <= 0 && upperCmp >= 0) {
/* 565 */       return connectedRange;
/*     */     }
/* 567 */     Cut<C> newLower = (lowerCmp >= 0) ? this.lowerBound : connectedRange.lowerBound;
/* 568 */     Cut<C> newUpper = (upperCmp <= 0) ? this.upperBound : connectedRange.upperBound;
/* 569 */     return (Range)create(newLower, newUpper);
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
/*     */   public Range<C> span(Range<C> other) {
/* 585 */     int lowerCmp = this.lowerBound.compareTo(other.lowerBound);
/* 586 */     int upperCmp = this.upperBound.compareTo(other.upperBound);
/* 587 */     if (lowerCmp <= 0 && upperCmp >= 0)
/* 588 */       return this; 
/* 589 */     if (lowerCmp >= 0 && upperCmp <= 0) {
/* 590 */       return other;
/*     */     }
/* 592 */     Cut<C> newLower = (lowerCmp <= 0) ? this.lowerBound : other.lowerBound;
/* 593 */     Cut<C> newUpper = (upperCmp >= 0) ? this.upperBound : other.upperBound;
/* 594 */     return (Range)create(newLower, newUpper);
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
/*     */   public Range<C> canonical(DiscreteDomain<C> domain) {
/* 623 */     Preconditions.checkNotNull(domain);
/* 624 */     Cut<C> lower = this.lowerBound.canonical(domain);
/* 625 */     Cut<C> upper = this.upperBound.canonical(domain);
/* 626 */     return (lower == this.lowerBound && upper == this.upperBound) ? this : (Range)create(lower, upper);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(@Nullable Object object) {
/* 637 */     if (object instanceof Range) {
/* 638 */       Range<?> other = (Range)object;
/* 639 */       return (this.lowerBound.equals(other.lowerBound) && this.upperBound.equals(other.upperBound));
/*     */     } 
/*     */     
/* 642 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 647 */     return this.lowerBound.hashCode() * 31 + this.upperBound.hashCode();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 655 */     return toString(this.lowerBound, this.upperBound);
/*     */   }
/*     */   
/*     */   private static String toString(Cut<?> lowerBound, Cut<?> upperBound) {
/* 659 */     StringBuilder sb = new StringBuilder(16);
/* 660 */     lowerBound.describeAsLowerBound(sb);
/* 661 */     sb.append('‥');
/* 662 */     upperBound.describeAsUpperBound(sb);
/* 663 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static <T> SortedSet<T> cast(Iterable<T> iterable) {
/* 670 */     return (SortedSet<T>)iterable;
/*     */   }
/*     */   
/*     */   Object readResolve() {
/* 674 */     if (equals(ALL)) {
/* 675 */       return all();
/*     */     }
/* 677 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static int compareOrThrow(Comparable<Comparable> left, Comparable right) {
/* 683 */     return left.compareTo(right);
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\collect\Range.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */