/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.primitives.Ints;
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
/*     */ public final class ImmutableRangeSet<C extends Comparable>
/*     */   extends AbstractRangeSet<C>
/*     */   implements Serializable
/*     */ {
/*  46 */   private static final ImmutableRangeSet<Comparable<?>> EMPTY = new ImmutableRangeSet(ImmutableList.of());
/*     */ 
/*     */   
/*  49 */   private static final ImmutableRangeSet<Comparable<?>> ALL = new ImmutableRangeSet(ImmutableList.of((Range)Range.all()));
/*     */   
/*     */   private final transient ImmutableList<Range<C>> ranges;
/*     */   
/*     */   private transient ImmutableRangeSet<C> complement;
/*     */ 
/*     */   
/*     */   public static <C extends Comparable> ImmutableRangeSet<C> of() {
/*  57 */     return (ImmutableRangeSet)EMPTY;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <C extends Comparable> ImmutableRangeSet<C> all() {
/*  65 */     return (ImmutableRangeSet)ALL;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <C extends Comparable> ImmutableRangeSet<C> of(Range<C> range) {
/*  73 */     Preconditions.checkNotNull(range);
/*  74 */     if (range.isEmpty())
/*  75 */       return of(); 
/*  76 */     if (range.equals(Range.all())) {
/*  77 */       return all();
/*     */     }
/*  79 */     return new ImmutableRangeSet<C>(ImmutableList.of(range));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <C extends Comparable> ImmutableRangeSet<C> copyOf(RangeSet<C> rangeSet) {
/*  87 */     Preconditions.checkNotNull(rangeSet);
/*  88 */     if (rangeSet.isEmpty())
/*  89 */       return of(); 
/*  90 */     if (rangeSet.encloses((Range)Range.all())) {
/*  91 */       return all();
/*     */     }
/*     */     
/*  94 */     if (rangeSet instanceof ImmutableRangeSet) {
/*  95 */       ImmutableRangeSet<C> immutableRangeSet = (ImmutableRangeSet<C>)rangeSet;
/*  96 */       if (!immutableRangeSet.isPartialView()) {
/*  97 */         return immutableRangeSet;
/*     */       }
/*     */     } 
/* 100 */     return new ImmutableRangeSet<C>(ImmutableList.copyOf(rangeSet.asRanges()));
/*     */   }
/*     */   
/*     */   ImmutableRangeSet(ImmutableList<Range<C>> ranges) {
/* 104 */     this.ranges = ranges;
/*     */   }
/*     */   
/*     */   private ImmutableRangeSet(ImmutableList<Range<C>> ranges, ImmutableRangeSet<C> complement) {
/* 108 */     this.ranges = ranges;
/* 109 */     this.complement = complement;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean encloses(Range<C> otherRange) {
/* 116 */     int index = SortedLists.binarySearch(this.ranges, (Function)Range.lowerBoundFn(), otherRange.lowerBound, Ordering.natural(), SortedLists.KeyPresentBehavior.ANY_PRESENT, SortedLists.KeyAbsentBehavior.NEXT_LOWER);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 122 */     return (index != -1 && ((Range<C>)this.ranges.get(index)).encloses(otherRange));
/*     */   }
/*     */ 
/*     */   
/*     */   public Range<C> rangeContaining(C value) {
/* 127 */     int index = SortedLists.binarySearch(this.ranges, (Function)Range.lowerBoundFn(), Cut.belowValue(value), Ordering.natural(), SortedLists.KeyPresentBehavior.ANY_PRESENT, SortedLists.KeyAbsentBehavior.NEXT_LOWER);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 133 */     if (index != -1) {
/* 134 */       Range<C> range = this.ranges.get(index);
/* 135 */       return range.contains(value) ? range : null;
/*     */     } 
/* 137 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public Range<C> span() {
/* 142 */     if (this.ranges.isEmpty()) {
/* 143 */       throw new NoSuchElementException();
/*     */     }
/* 145 */     return (Range)Range.create(((Range)this.ranges.get(0)).lowerBound, ((Range)this.ranges.get(this.ranges.size() - 1)).upperBound);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 152 */     return this.ranges.isEmpty();
/*     */   }
/*     */ 
/*     */   
/*     */   public void add(Range<C> range) {
/* 157 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public void addAll(RangeSet<C> other) {
/* 162 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public void remove(Range<C> range) {
/* 167 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeAll(RangeSet<C> other) {
/* 172 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public ImmutableSet<Range<C>> asRanges() {
/* 177 */     if (this.ranges.isEmpty()) {
/* 178 */       return ImmutableSet.of();
/*     */     }
/* 180 */     return (ImmutableSet)new RegularImmutableSortedSet<Range<?>>(this.ranges, Range.RANGE_LEX_ORDERING);
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
/*     */   private final class ComplementRanges
/*     */     extends ImmutableList<Range<C>>
/*     */   {
/* 195 */     private final boolean positiveBoundedBelow = ((Range)ImmutableRangeSet.this.ranges.get(0)).hasLowerBound();
/* 196 */     private final boolean positiveBoundedAbove = ((Range)Iterables.<Range>getLast(ImmutableRangeSet.this.ranges)).hasUpperBound();
/*     */     ComplementRanges() {
/* 198 */       int size = ImmutableRangeSet.this.ranges.size() - 1;
/* 199 */       if (this.positiveBoundedBelow) {
/* 200 */         size++;
/*     */       }
/* 202 */       if (this.positiveBoundedAbove) {
/* 203 */         size++;
/*     */       }
/* 205 */       this.size = size;
/*     */     }
/*     */     private final int size;
/*     */     
/*     */     public int size() {
/* 210 */       return this.size;
/*     */     }
/*     */     
/*     */     public Range<C> get(int index) {
/*     */       Cut<C> lowerBound, upperBound;
/* 215 */       Preconditions.checkElementIndex(index, this.size);
/*     */ 
/*     */       
/* 218 */       if (this.positiveBoundedBelow) {
/* 219 */         lowerBound = (index == 0) ? Cut.<C>belowAll() : ((Range)ImmutableRangeSet.this.ranges.get(index - 1)).upperBound;
/*     */       } else {
/* 221 */         lowerBound = ((Range)ImmutableRangeSet.this.ranges.get(index)).upperBound;
/*     */       } 
/*     */ 
/*     */       
/* 225 */       if (this.positiveBoundedAbove && index == this.size - 1) {
/* 226 */         upperBound = Cut.aboveAll();
/*     */       } else {
/* 228 */         upperBound = ((Range)ImmutableRangeSet.this.ranges.get(index + (this.positiveBoundedBelow ? 0 : 1))).lowerBound;
/*     */       } 
/*     */       
/* 231 */       return (Range)Range.create(lowerBound, upperBound);
/*     */     }
/*     */ 
/*     */     
/*     */     boolean isPartialView() {
/* 236 */       return true;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public ImmutableRangeSet<C> complement() {
/* 242 */     ImmutableRangeSet<C> result = this.complement;
/* 243 */     if (result != null)
/* 244 */       return result; 
/* 245 */     if (this.ranges.isEmpty())
/* 246 */       return this.complement = all(); 
/* 247 */     if (this.ranges.size() == 1 && ((Range)this.ranges.get(0)).equals(Range.all())) {
/* 248 */       return this.complement = of();
/*     */     }
/* 250 */     ImmutableList<Range<C>> complementRanges = new ComplementRanges();
/* 251 */     result = this.complement = new ImmutableRangeSet(complementRanges, this);
/*     */     
/* 253 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ImmutableList<Range<C>> intersectRanges(final Range<C> range) {
/*     */     final int fromIndex, toIndex;
/* 261 */     if (this.ranges.isEmpty() || range.isEmpty())
/* 262 */       return ImmutableList.of(); 
/* 263 */     if (range.encloses(span())) {
/* 264 */       return this.ranges;
/*     */     }
/*     */ 
/*     */     
/* 268 */     if (range.hasLowerBound()) {
/* 269 */       fromIndex = SortedLists.binarySearch(this.ranges, (Function)Range.upperBoundFn(), range.lowerBound, SortedLists.KeyPresentBehavior.FIRST_AFTER, SortedLists.KeyAbsentBehavior.NEXT_HIGHER);
/*     */     }
/*     */     else {
/*     */       
/* 273 */       fromIndex = 0;
/*     */     } 
/*     */ 
/*     */     
/* 277 */     if (range.hasUpperBound()) {
/* 278 */       toIndex = SortedLists.binarySearch(this.ranges, (Function)Range.lowerBoundFn(), range.upperBound, SortedLists.KeyPresentBehavior.FIRST_PRESENT, SortedLists.KeyAbsentBehavior.NEXT_HIGHER);
/*     */     }
/*     */     else {
/*     */       
/* 282 */       toIndex = this.ranges.size();
/*     */     } 
/* 284 */     final int length = toIndex - fromIndex;
/* 285 */     if (length == 0) {
/* 286 */       return ImmutableList.of();
/*     */     }
/* 288 */     return new ImmutableList<Range<C>>()
/*     */       {
/*     */         public int size() {
/* 291 */           return length;
/*     */         }
/*     */ 
/*     */         
/*     */         public Range<C> get(int index) {
/* 296 */           Preconditions.checkElementIndex(index, length);
/* 297 */           if (index == 0 || index == length - 1) {
/* 298 */             return ((Range<C>)ImmutableRangeSet.this.ranges.get(index + fromIndex)).intersection(range);
/*     */           }
/* 300 */           return ImmutableRangeSet.this.ranges.get(index + fromIndex);
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*     */         boolean isPartialView() {
/* 306 */           return true;
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableRangeSet<C> subRangeSet(Range<C> range) {
/* 317 */     if (!isEmpty()) {
/* 318 */       Range<C> span = span();
/* 319 */       if (range.encloses(span))
/* 320 */         return this; 
/* 321 */       if (range.isConnected(span)) {
/* 322 */         return new ImmutableRangeSet(intersectRanges(range));
/*     */       }
/*     */     } 
/* 325 */     return of();
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
/*     */   public ImmutableSortedSet<C> asSet(DiscreteDomain<C> domain) {
/* 348 */     Preconditions.checkNotNull(domain);
/* 349 */     if (isEmpty()) {
/* 350 */       return ImmutableSortedSet.of();
/*     */     }
/* 352 */     Range<C> span = span().canonical(domain);
/* 353 */     if (!span.hasLowerBound())
/*     */     {
/*     */       
/* 356 */       throw new IllegalArgumentException("Neither the DiscreteDomain nor this range set are bounded below");
/*     */     }
/* 358 */     if (!span.hasUpperBound()) {
/*     */       try {
/* 360 */         domain.maxValue();
/* 361 */       } catch (NoSuchElementException e) {
/* 362 */         throw new IllegalArgumentException("Neither the DiscreteDomain nor this range set are bounded above");
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 367 */     return new AsSet(domain);
/*     */   }
/*     */   
/*     */   private final class AsSet extends ImmutableSortedSet<C> { private final DiscreteDomain<C> domain;
/*     */     private transient Integer size;
/*     */     
/*     */     AsSet(DiscreteDomain<C> domain) {
/* 374 */       super(Ordering.natural());
/* 375 */       this.domain = domain;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int size() {
/* 383 */       Integer result = this.size;
/* 384 */       if (result == null) {
/* 385 */         long total = 0L;
/* 386 */         for (Range<C> range : (Iterable<Range<C>>)ImmutableRangeSet.this.ranges) {
/* 387 */           total += ContiguousSet.<C>create(range, this.domain).size();
/* 388 */           if (total >= 2147483647L) {
/*     */             break;
/*     */           }
/*     */         } 
/* 392 */         result = this.size = Integer.valueOf(Ints.saturatedCast(total));
/*     */       } 
/* 394 */       return result.intValue();
/*     */     }
/*     */ 
/*     */     
/*     */     public UnmodifiableIterator<C> iterator() {
/* 399 */       return new AbstractIterator<C>() {
/* 400 */           final Iterator<Range<C>> rangeItr = ImmutableRangeSet.this.ranges.iterator();
/* 401 */           Iterator<C> elemItr = Iterators.emptyIterator();
/*     */ 
/*     */           
/*     */           protected C computeNext() {
/* 405 */             while (!this.elemItr.hasNext()) {
/* 406 */               if (this.rangeItr.hasNext()) {
/* 407 */                 this.elemItr = ContiguousSet.<C>create(this.rangeItr.next(), ImmutableRangeSet.AsSet.this.domain).iterator(); continue;
/*     */               } 
/* 409 */               return endOfData();
/*     */             } 
/*     */             
/* 412 */             return this.elemItr.next();
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */     
/*     */     @GwtIncompatible("NavigableSet")
/*     */     public UnmodifiableIterator<C> descendingIterator() {
/* 420 */       return new AbstractIterator<C>() {
/* 421 */           final Iterator<Range<C>> rangeItr = ImmutableRangeSet.this.ranges.reverse().iterator();
/* 422 */           Iterator<C> elemItr = Iterators.emptyIterator();
/*     */ 
/*     */           
/*     */           protected C computeNext() {
/* 426 */             while (!this.elemItr.hasNext()) {
/* 427 */               if (this.rangeItr.hasNext()) {
/* 428 */                 this.elemItr = ContiguousSet.<C>create(this.rangeItr.next(), ImmutableRangeSet.AsSet.this.domain).descendingIterator(); continue;
/*     */               } 
/* 430 */               return endOfData();
/*     */             } 
/*     */             
/* 433 */             return this.elemItr.next();
/*     */           }
/*     */         };
/*     */     }
/*     */     
/*     */     ImmutableSortedSet<C> subSet(Range<C> range) {
/* 439 */       return ImmutableRangeSet.this.subRangeSet(range).asSet(this.domain);
/*     */     }
/*     */ 
/*     */     
/*     */     ImmutableSortedSet<C> headSetImpl(C toElement, boolean inclusive) {
/* 444 */       return subSet((Range)Range.upTo((Comparable<?>)toElement, BoundType.forBoolean(inclusive)));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     ImmutableSortedSet<C> subSetImpl(C fromElement, boolean fromInclusive, C toElement, boolean toInclusive) {
/* 450 */       if (!fromInclusive && !toInclusive && Range.compareOrThrow((Comparable)fromElement, (Comparable)toElement) == 0) {
/* 451 */         return ImmutableSortedSet.of();
/*     */       }
/* 453 */       return subSet((Range)Range.range((Comparable<?>)fromElement, BoundType.forBoolean(fromInclusive), (Comparable<?>)toElement, BoundType.forBoolean(toInclusive)));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     ImmutableSortedSet<C> tailSetImpl(C fromElement, boolean inclusive) {
/* 460 */       return subSet((Range)Range.downTo((Comparable<?>)fromElement, BoundType.forBoolean(inclusive)));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean contains(@Nullable Object o) {
/* 465 */       if (o == null) {
/* 466 */         return false;
/*     */       }
/*     */       
/*     */       try {
/* 470 */         Comparable comparable = (Comparable)o;
/* 471 */         return ImmutableRangeSet.this.contains(comparable);
/* 472 */       } catch (ClassCastException e) {
/* 473 */         return false;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     int indexOf(Object target) {
/* 479 */       if (contains(target)) {
/*     */         
/* 481 */         Comparable comparable = (Comparable)target;
/* 482 */         long total = 0L;
/* 483 */         for (Range<C> range : (Iterable<Range<C>>)ImmutableRangeSet.this.ranges) {
/* 484 */           if (range.contains((C)comparable)) {
/* 485 */             return Ints.saturatedCast(total + ContiguousSet.<C>create(range, this.domain).indexOf(comparable));
/*     */           }
/* 487 */           total += ContiguousSet.<C>create(range, this.domain).size();
/*     */         } 
/*     */         
/* 490 */         throw new AssertionError("impossible");
/*     */       } 
/* 492 */       return -1;
/*     */     }
/*     */ 
/*     */     
/*     */     boolean isPartialView() {
/* 497 */       return ImmutableRangeSet.this.ranges.isPartialView();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 502 */       return ImmutableRangeSet.this.ranges.toString();
/*     */     }
/*     */ 
/*     */     
/*     */     Object writeReplace() {
/* 507 */       return new ImmutableRangeSet.AsSetSerializedForm<C>(ImmutableRangeSet.this.ranges, this.domain);
/*     */     } }
/*     */ 
/*     */   
/*     */   private static class AsSetSerializedForm<C extends Comparable> implements Serializable {
/*     */     private final ImmutableList<Range<C>> ranges;
/*     */     private final DiscreteDomain<C> domain;
/*     */     
/*     */     AsSetSerializedForm(ImmutableList<Range<C>> ranges, DiscreteDomain<C> domain) {
/* 516 */       this.ranges = ranges;
/* 517 */       this.domain = domain;
/*     */     }
/*     */     
/*     */     Object readResolve() {
/* 521 */       return (new ImmutableRangeSet<C>(this.ranges)).asSet(this.domain);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean isPartialView() {
/* 532 */     return this.ranges.isPartialView();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <C extends Comparable<?>> Builder<C> builder() {
/* 539 */     return new Builder<C>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class Builder<C extends Comparable<?>>
/*     */   {
/* 549 */     private final RangeSet<C> rangeSet = TreeRangeSet.create();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder<C> add(Range<C> range) {
/* 560 */       if (range.isEmpty()) {
/* 561 */         String str = String.valueOf(String.valueOf(range)); throw new IllegalArgumentException((new StringBuilder(33 + str.length())).append("range must not be empty, but was ").append(str).toString());
/* 562 */       }  if (!this.rangeSet.complement().encloses(range)) {
/* 563 */         for (Range<C> currentRange : this.rangeSet.asRanges()) {
/* 564 */           Preconditions.checkArgument((!currentRange.isConnected(range) || currentRange.intersection(range).isEmpty()), "Ranges may not overlap, but received %s and %s", new Object[] { currentRange, range });
/*     */         } 
/*     */ 
/*     */         
/* 568 */         throw new AssertionError("should have thrown an IAE above");
/*     */       } 
/* 570 */       this.rangeSet.add(range);
/* 571 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder<C> addAll(RangeSet<C> ranges) {
/* 579 */       for (Range<C> range : ranges.asRanges()) {
/* 580 */         add(range);
/*     */       }
/* 582 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ImmutableRangeSet<C> build() {
/* 589 */       return (ImmutableRangeSet)ImmutableRangeSet.copyOf(this.rangeSet);
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class SerializedForm<C extends Comparable> implements Serializable {
/*     */     private final ImmutableList<Range<C>> ranges;
/*     */     
/*     */     SerializedForm(ImmutableList<Range<C>> ranges) {
/* 597 */       this.ranges = ranges;
/*     */     }
/*     */     
/*     */     Object readResolve() {
/* 601 */       if (this.ranges.isEmpty())
/* 602 */         return ImmutableRangeSet.of(); 
/* 603 */       if (this.ranges.equals(ImmutableList.of(Range.all()))) {
/* 604 */         return ImmutableRangeSet.all();
/*     */       }
/* 606 */       return new ImmutableRangeSet<C>(this.ranges);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   Object writeReplace() {
/* 612 */     return new SerializedForm<C>(this.ranges);
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\collect\ImmutableRangeSet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */