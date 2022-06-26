/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.MoreObjects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.NavigableMap;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Set;
/*     */ import java.util.TreeMap;
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
/*     */ @Beta
/*     */ @GwtIncompatible("uses NavigableMap")
/*     */ public class TreeRangeSet<C extends Comparable<?>>
/*     */   extends AbstractRangeSet<C>
/*     */ {
/*     */   @VisibleForTesting
/*     */   final NavigableMap<Cut<C>, Range<C>> rangesByLowerBound;
/*     */   private transient Set<Range<C>> asRanges;
/*     */   private transient RangeSet<C> complement;
/*     */   
/*     */   public static <C extends Comparable<?>> TreeRangeSet<C> create() {
/*  54 */     return new TreeRangeSet<C>(new TreeMap<Cut<C>, Range<C>>());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <C extends Comparable<?>> TreeRangeSet<C> create(RangeSet<C> rangeSet) {
/*  61 */     TreeRangeSet<C> result = create();
/*  62 */     result.addAll(rangeSet);
/*  63 */     return result;
/*     */   }
/*     */   
/*     */   private TreeRangeSet(NavigableMap<Cut<C>, Range<C>> rangesByLowerCut) {
/*  67 */     this.rangesByLowerBound = rangesByLowerCut;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<Range<C>> asRanges() {
/*  74 */     Set<Range<C>> result = this.asRanges;
/*  75 */     return (result == null) ? (this.asRanges = new AsRanges()) : result;
/*     */   }
/*     */   
/*     */   final class AsRanges
/*     */     extends ForwardingCollection<Range<C>> implements Set<Range<C>> {
/*     */     protected Collection<Range<C>> delegate() {
/*  81 */       return TreeRangeSet.this.rangesByLowerBound.values();
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/*  86 */       return Sets.hashCodeImpl(this);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(@Nullable Object o) {
/*  91 */       return Sets.equalsImpl(this, o);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Range<C> rangeContaining(C value) {
/*  98 */     Preconditions.checkNotNull(value);
/*  99 */     Map.Entry<Cut<C>, Range<C>> floorEntry = this.rangesByLowerBound.floorEntry((Cut)Cut.belowValue((Comparable)value));
/* 100 */     if (floorEntry != null && ((Range)floorEntry.getValue()).contains(value)) {
/* 101 */       return floorEntry.getValue();
/*     */     }
/*     */     
/* 104 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean encloses(Range<C> range) {
/* 110 */     Preconditions.checkNotNull(range);
/* 111 */     Map.Entry<Cut<C>, Range<C>> floorEntry = this.rangesByLowerBound.floorEntry(range.lowerBound);
/* 112 */     return (floorEntry != null && ((Range)floorEntry.getValue()).encloses(range));
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private Range<C> rangeEnclosing(Range<C> range) {
/* 117 */     Preconditions.checkNotNull(range);
/* 118 */     Map.Entry<Cut<C>, Range<C>> floorEntry = this.rangesByLowerBound.floorEntry(range.lowerBound);
/* 119 */     return (floorEntry != null && ((Range)floorEntry.getValue()).encloses(range)) ? floorEntry.getValue() : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Range<C> span() {
/* 126 */     Map.Entry<Cut<C>, Range<C>> firstEntry = this.rangesByLowerBound.firstEntry();
/* 127 */     Map.Entry<Cut<C>, Range<C>> lastEntry = this.rangesByLowerBound.lastEntry();
/* 128 */     if (firstEntry == null) {
/* 129 */       throw new NoSuchElementException();
/*     */     }
/* 131 */     return Range.create(((Range)firstEntry.getValue()).lowerBound, ((Range)lastEntry.getValue()).upperBound);
/*     */   }
/*     */ 
/*     */   
/*     */   public void add(Range<C> rangeToAdd) {
/* 136 */     Preconditions.checkNotNull(rangeToAdd);
/*     */     
/* 138 */     if (rangeToAdd.isEmpty()) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 144 */     Cut<C> lbToAdd = rangeToAdd.lowerBound;
/* 145 */     Cut<C> ubToAdd = rangeToAdd.upperBound;
/*     */     
/* 147 */     Map.Entry<Cut<C>, Range<C>> entryBelowLB = this.rangesByLowerBound.lowerEntry(lbToAdd);
/* 148 */     if (entryBelowLB != null) {
/*     */       
/* 150 */       Range<C> rangeBelowLB = entryBelowLB.getValue();
/* 151 */       if (rangeBelowLB.upperBound.compareTo(lbToAdd) >= 0) {
/*     */         
/* 153 */         if (rangeBelowLB.upperBound.compareTo(ubToAdd) >= 0)
/*     */         {
/* 155 */           ubToAdd = rangeBelowLB.upperBound;
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 161 */         lbToAdd = rangeBelowLB.lowerBound;
/*     */       } 
/*     */     } 
/*     */     
/* 165 */     Map.Entry<Cut<C>, Range<C>> entryBelowUB = this.rangesByLowerBound.floorEntry(ubToAdd);
/* 166 */     if (entryBelowUB != null) {
/*     */       
/* 168 */       Range<C> rangeBelowUB = entryBelowUB.getValue();
/* 169 */       if (rangeBelowUB.upperBound.compareTo(ubToAdd) >= 0)
/*     */       {
/* 171 */         ubToAdd = rangeBelowUB.upperBound;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 176 */     this.rangesByLowerBound.subMap(lbToAdd, ubToAdd).clear();
/*     */     
/* 178 */     replaceRangeWithSameLowerBound(Range.create(lbToAdd, ubToAdd));
/*     */   }
/*     */ 
/*     */   
/*     */   public void remove(Range<C> rangeToRemove) {
/* 183 */     Preconditions.checkNotNull(rangeToRemove);
/*     */     
/* 185 */     if (rangeToRemove.isEmpty()) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 192 */     Map.Entry<Cut<C>, Range<C>> entryBelowLB = this.rangesByLowerBound.lowerEntry(rangeToRemove.lowerBound);
/* 193 */     if (entryBelowLB != null) {
/*     */       
/* 195 */       Range<C> rangeBelowLB = entryBelowLB.getValue();
/* 196 */       if (rangeBelowLB.upperBound.compareTo(rangeToRemove.lowerBound) >= 0) {
/*     */         
/* 198 */         if (rangeToRemove.hasUpperBound() && rangeBelowLB.upperBound.compareTo(rangeToRemove.upperBound) >= 0)
/*     */         {
/*     */           
/* 201 */           replaceRangeWithSameLowerBound(Range.create(rangeToRemove.upperBound, rangeBelowLB.upperBound));
/*     */         }
/*     */         
/* 204 */         replaceRangeWithSameLowerBound(Range.create(rangeBelowLB.lowerBound, rangeToRemove.lowerBound));
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 209 */     Map.Entry<Cut<C>, Range<C>> entryBelowUB = this.rangesByLowerBound.floorEntry(rangeToRemove.upperBound);
/* 210 */     if (entryBelowUB != null) {
/*     */       
/* 212 */       Range<C> rangeBelowUB = entryBelowUB.getValue();
/* 213 */       if (rangeToRemove.hasUpperBound() && rangeBelowUB.upperBound.compareTo(rangeToRemove.upperBound) >= 0)
/*     */       {
/*     */         
/* 216 */         replaceRangeWithSameLowerBound(Range.create(rangeToRemove.upperBound, rangeBelowUB.upperBound));
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 221 */     this.rangesByLowerBound.subMap(rangeToRemove.lowerBound, rangeToRemove.upperBound).clear();
/*     */   }
/*     */   
/*     */   private void replaceRangeWithSameLowerBound(Range<C> range) {
/* 225 */     if (range.isEmpty()) {
/* 226 */       this.rangesByLowerBound.remove(range.lowerBound);
/*     */     } else {
/* 228 */       this.rangesByLowerBound.put(range.lowerBound, range);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RangeSet<C> complement() {
/* 236 */     RangeSet<C> result = this.complement;
/* 237 */     return (result == null) ? (this.complement = new Complement()) : result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   static final class RangesByUpperBound<C extends Comparable<?>>
/*     */     extends AbstractNavigableMap<Cut<C>, Range<C>>
/*     */   {
/*     */     private final NavigableMap<Cut<C>, Range<C>> rangesByLowerBound;
/*     */     
/*     */     private final Range<Cut<C>> upperBoundWindow;
/*     */ 
/*     */     
/*     */     RangesByUpperBound(NavigableMap<Cut<C>, Range<C>> rangesByLowerBound) {
/* 252 */       this.rangesByLowerBound = rangesByLowerBound;
/* 253 */       this.upperBoundWindow = Range.all();
/*     */     }
/*     */ 
/*     */     
/*     */     private RangesByUpperBound(NavigableMap<Cut<C>, Range<C>> rangesByLowerBound, Range<Cut<C>> upperBoundWindow) {
/* 258 */       this.rangesByLowerBound = rangesByLowerBound;
/* 259 */       this.upperBoundWindow = upperBoundWindow;
/*     */     }
/*     */     
/*     */     private NavigableMap<Cut<C>, Range<C>> subMap(Range<Cut<C>> window) {
/* 263 */       if (window.isConnected(this.upperBoundWindow)) {
/* 264 */         return new RangesByUpperBound(this.rangesByLowerBound, window.intersection(this.upperBoundWindow));
/*     */       }
/* 266 */       return ImmutableSortedMap.of();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public NavigableMap<Cut<C>, Range<C>> subMap(Cut<C> fromKey, boolean fromInclusive, Cut<C> toKey, boolean toInclusive) {
/* 273 */       return subMap(Range.range(fromKey, BoundType.forBoolean(fromInclusive), toKey, BoundType.forBoolean(toInclusive)));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public NavigableMap<Cut<C>, Range<C>> headMap(Cut<C> toKey, boolean inclusive) {
/* 280 */       return subMap(Range.upTo(toKey, BoundType.forBoolean(inclusive)));
/*     */     }
/*     */ 
/*     */     
/*     */     public NavigableMap<Cut<C>, Range<C>> tailMap(Cut<C> fromKey, boolean inclusive) {
/* 285 */       return subMap(Range.downTo(fromKey, BoundType.forBoolean(inclusive)));
/*     */     }
/*     */ 
/*     */     
/*     */     public Comparator<? super Cut<C>> comparator() {
/* 290 */       return Ordering.natural();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean containsKey(@Nullable Object key) {
/* 295 */       return (get(key) != null);
/*     */     }
/*     */ 
/*     */     
/*     */     public Range<C> get(@Nullable Object key) {
/* 300 */       if (key instanceof Cut) {
/*     */         
/*     */         try {
/* 303 */           Cut<C> cut = (Cut<C>)key;
/* 304 */           if (!this.upperBoundWindow.contains(cut)) {
/* 305 */             return null;
/*     */           }
/* 307 */           Map.Entry<Cut<C>, Range<C>> candidate = this.rangesByLowerBound.lowerEntry(cut);
/* 308 */           if (candidate != null && ((Range)candidate.getValue()).upperBound.equals(cut)) {
/* 309 */             return candidate.getValue();
/*     */           }
/* 311 */         } catch (ClassCastException e) {
/* 312 */           return null;
/*     */         } 
/*     */       }
/* 315 */       return null;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     Iterator<Map.Entry<Cut<C>, Range<C>>> entryIterator() {
/*     */       final Iterator<Range<C>> backingItr;
/* 325 */       if (!this.upperBoundWindow.hasLowerBound()) {
/* 326 */         backingItr = this.rangesByLowerBound.values().iterator();
/*     */       } else {
/* 328 */         Map.Entry<Cut<C>, Range<C>> lowerEntry = this.rangesByLowerBound.lowerEntry(this.upperBoundWindow.lowerEndpoint());
/*     */         
/* 330 */         if (lowerEntry == null) {
/* 331 */           backingItr = this.rangesByLowerBound.values().iterator();
/* 332 */         } else if (this.upperBoundWindow.lowerBound.isLessThan(((Range)lowerEntry.getValue()).upperBound)) {
/* 333 */           backingItr = this.rangesByLowerBound.tailMap(lowerEntry.getKey(), true).values().iterator();
/*     */         } else {
/* 335 */           backingItr = this.rangesByLowerBound.tailMap(this.upperBoundWindow.lowerEndpoint(), true).values().iterator();
/*     */         } 
/*     */       } 
/*     */       
/* 339 */       return new AbstractIterator<Map.Entry<Cut<C>, Range<C>>>()
/*     */         {
/*     */           protected Map.Entry<Cut<C>, Range<C>> computeNext() {
/* 342 */             if (!backingItr.hasNext()) {
/* 343 */               return endOfData();
/*     */             }
/* 345 */             Range<C> range = backingItr.next();
/* 346 */             if (TreeRangeSet.RangesByUpperBound.this.upperBoundWindow.upperBound.isLessThan(range.upperBound)) {
/* 347 */               return endOfData();
/*     */             }
/* 349 */             return Maps.immutableEntry(range.upperBound, range);
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     Iterator<Map.Entry<Cut<C>, Range<C>>> descendingEntryIterator() {
/*     */       Collection<Range<C>> candidates;
/* 358 */       if (this.upperBoundWindow.hasUpperBound()) {
/* 359 */         candidates = this.rangesByLowerBound.headMap(this.upperBoundWindow.upperEndpoint(), false).descendingMap().values();
/*     */       } else {
/*     */         
/* 362 */         candidates = this.rangesByLowerBound.descendingMap().values();
/*     */       } 
/* 364 */       final PeekingIterator<Range<C>> backingItr = Iterators.peekingIterator(candidates.iterator());
/* 365 */       if (backingItr.hasNext() && this.upperBoundWindow.upperBound.isLessThan(((Range)backingItr.peek()).upperBound))
/*     */       {
/* 367 */         backingItr.next();
/*     */       }
/* 369 */       return new AbstractIterator<Map.Entry<Cut<C>, Range<C>>>()
/*     */         {
/*     */           protected Map.Entry<Cut<C>, Range<C>> computeNext() {
/* 372 */             if (!backingItr.hasNext()) {
/* 373 */               return endOfData();
/*     */             }
/* 375 */             Range<C> range = backingItr.next();
/* 376 */             return TreeRangeSet.RangesByUpperBound.this.upperBoundWindow.lowerBound.isLessThan(range.upperBound) ? Maps.<Cut<C>, Range<C>>immutableEntry(range.upperBound, range) : endOfData();
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int size() {
/* 385 */       if (this.upperBoundWindow.equals(Range.all())) {
/* 386 */         return this.rangesByLowerBound.size();
/*     */       }
/* 388 */       return Iterators.size(entryIterator());
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isEmpty() {
/* 393 */       return this.upperBoundWindow.equals(Range.all()) ? this.rangesByLowerBound.isEmpty() : (!entryIterator().hasNext());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class ComplementRangesByLowerBound<C extends Comparable<?>>
/*     */     extends AbstractNavigableMap<Cut<C>, Range<C>>
/*     */   {
/*     */     private final NavigableMap<Cut<C>, Range<C>> positiveRangesByLowerBound;
/*     */ 
/*     */     
/*     */     private final NavigableMap<Cut<C>, Range<C>> positiveRangesByUpperBound;
/*     */ 
/*     */     
/*     */     private final Range<Cut<C>> complementLowerBoundWindow;
/*     */ 
/*     */     
/*     */     ComplementRangesByLowerBound(NavigableMap<Cut<C>, Range<C>> positiveRangesByLowerBound) {
/* 412 */       this(positiveRangesByLowerBound, Range.all());
/*     */     }
/*     */ 
/*     */     
/*     */     private ComplementRangesByLowerBound(NavigableMap<Cut<C>, Range<C>> positiveRangesByLowerBound, Range<Cut<C>> window) {
/* 417 */       this.positiveRangesByLowerBound = positiveRangesByLowerBound;
/* 418 */       this.positiveRangesByUpperBound = new TreeRangeSet.RangesByUpperBound<C>(positiveRangesByLowerBound);
/* 419 */       this.complementLowerBoundWindow = window;
/*     */     }
/*     */     
/*     */     private NavigableMap<Cut<C>, Range<C>> subMap(Range<Cut<C>> subWindow) {
/* 423 */       if (!this.complementLowerBoundWindow.isConnected(subWindow)) {
/* 424 */         return ImmutableSortedMap.of();
/*     */       }
/* 426 */       subWindow = subWindow.intersection(this.complementLowerBoundWindow);
/* 427 */       return new ComplementRangesByLowerBound(this.positiveRangesByLowerBound, subWindow);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public NavigableMap<Cut<C>, Range<C>> subMap(Cut<C> fromKey, boolean fromInclusive, Cut<C> toKey, boolean toInclusive) {
/* 434 */       return subMap(Range.range(fromKey, BoundType.forBoolean(fromInclusive), toKey, BoundType.forBoolean(toInclusive)));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public NavigableMap<Cut<C>, Range<C>> headMap(Cut<C> toKey, boolean inclusive) {
/* 441 */       return subMap(Range.upTo(toKey, BoundType.forBoolean(inclusive)));
/*     */     }
/*     */ 
/*     */     
/*     */     public NavigableMap<Cut<C>, Range<C>> tailMap(Cut<C> fromKey, boolean inclusive) {
/* 446 */       return subMap(Range.downTo(fromKey, BoundType.forBoolean(inclusive)));
/*     */     }
/*     */ 
/*     */     
/*     */     public Comparator<? super Cut<C>> comparator() {
/* 451 */       return Ordering.natural();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     Iterator<Map.Entry<Cut<C>, Range<C>>> entryIterator() {
/*     */       Collection<Range<C>> positiveRanges;
/*     */       final Cut<C> firstComplementRangeLowerBound;
/* 466 */       if (this.complementLowerBoundWindow.hasLowerBound()) {
/* 467 */         positiveRanges = this.positiveRangesByUpperBound.tailMap(this.complementLowerBoundWindow.lowerEndpoint(), (this.complementLowerBoundWindow.lowerBoundType() == BoundType.CLOSED)).values();
/*     */       }
/*     */       else {
/*     */         
/* 471 */         positiveRanges = this.positiveRangesByUpperBound.values();
/*     */       } 
/* 473 */       final PeekingIterator<Range<C>> positiveItr = Iterators.peekingIterator(positiveRanges.iterator());
/*     */ 
/*     */       
/* 476 */       if (this.complementLowerBoundWindow.contains((Cut)Cut.belowAll()) && (!positiveItr.hasNext() || ((Range)positiveItr.peek()).lowerBound != Cut.belowAll())) {
/*     */         
/* 478 */         firstComplementRangeLowerBound = Cut.belowAll();
/* 479 */       } else if (positiveItr.hasNext()) {
/* 480 */         firstComplementRangeLowerBound = ((Range)positiveItr.next()).upperBound;
/*     */       } else {
/* 482 */         return Iterators.emptyIterator();
/*     */       } 
/* 484 */       return new AbstractIterator<Map.Entry<Cut<C>, Range<C>>>() {
/* 485 */           Cut<C> nextComplementRangeLowerBound = firstComplementRangeLowerBound;
/*     */           
/*     */           protected Map.Entry<Cut<C>, Range<C>> computeNext() {
/*     */             Range<C> negativeRange;
/* 489 */             if (TreeRangeSet.ComplementRangesByLowerBound.this.complementLowerBoundWindow.upperBound.isLessThan(this.nextComplementRangeLowerBound) || this.nextComplementRangeLowerBound == Cut.aboveAll())
/*     */             {
/* 491 */               return endOfData();
/*     */             }
/*     */             
/* 494 */             if (positiveItr.hasNext()) {
/* 495 */               Range<C> positiveRange = positiveItr.next();
/* 496 */               negativeRange = Range.create(this.nextComplementRangeLowerBound, positiveRange.lowerBound);
/* 497 */               this.nextComplementRangeLowerBound = positiveRange.upperBound;
/*     */             } else {
/* 499 */               negativeRange = Range.create(this.nextComplementRangeLowerBound, (Cut)Cut.aboveAll());
/* 500 */               this.nextComplementRangeLowerBound = Cut.aboveAll();
/*     */             } 
/* 502 */             return Maps.immutableEntry(negativeRange.lowerBound, negativeRange);
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     Iterator<Map.Entry<Cut<C>, Range<C>>> descendingEntryIterator() {
/* 517 */       Cut<C> cut, startingPoint = this.complementLowerBoundWindow.hasUpperBound() ? this.complementLowerBoundWindow.upperEndpoint() : (Cut)Cut.<Comparable>aboveAll();
/*     */ 
/*     */       
/* 520 */       boolean inclusive = (this.complementLowerBoundWindow.hasUpperBound() && this.complementLowerBoundWindow.upperBoundType() == BoundType.CLOSED);
/*     */       
/* 522 */       final PeekingIterator<Range<C>> positiveItr = Iterators.peekingIterator(this.positiveRangesByUpperBound.headMap(startingPoint, inclusive).descendingMap().values().iterator());
/*     */ 
/*     */ 
/*     */       
/* 526 */       if (positiveItr.hasNext()) {
/* 527 */         cut = (((Range)positiveItr.peek()).upperBound == Cut.aboveAll()) ? ((Range)positiveItr.next()).lowerBound : this.positiveRangesByLowerBound.higherKey(((Range)positiveItr.peek()).upperBound);
/*     */       } else {
/*     */         
/* 530 */         if (!this.complementLowerBoundWindow.contains((Cut)Cut.belowAll()) || this.positiveRangesByLowerBound.containsKey(Cut.belowAll()))
/*     */         {
/* 532 */           return Iterators.emptyIterator();
/*     */         }
/* 534 */         cut = this.positiveRangesByLowerBound.higherKey((Cut)Cut.belowAll());
/*     */       } 
/* 536 */       final Cut<C> firstComplementRangeUpperBound = (Cut<C>)MoreObjects.firstNonNull(cut, Cut.aboveAll());
/*     */       
/* 538 */       return new AbstractIterator<Map.Entry<Cut<C>, Range<C>>>() {
/* 539 */           Cut<C> nextComplementRangeUpperBound = firstComplementRangeUpperBound;
/*     */ 
/*     */           
/*     */           protected Map.Entry<Cut<C>, Range<C>> computeNext() {
/* 543 */             if (this.nextComplementRangeUpperBound == Cut.belowAll())
/* 544 */               return endOfData(); 
/* 545 */             if (positiveItr.hasNext()) {
/* 546 */               Range<C> positiveRange = positiveItr.next();
/* 547 */               Range<C> negativeRange = Range.create(positiveRange.upperBound, this.nextComplementRangeUpperBound);
/*     */               
/* 549 */               this.nextComplementRangeUpperBound = positiveRange.lowerBound;
/* 550 */               if (TreeRangeSet.ComplementRangesByLowerBound.this.complementLowerBoundWindow.lowerBound.isLessThan(negativeRange.lowerBound)) {
/* 551 */                 return Maps.immutableEntry(negativeRange.lowerBound, negativeRange);
/*     */               }
/* 553 */             } else if (TreeRangeSet.ComplementRangesByLowerBound.this.complementLowerBoundWindow.lowerBound.isLessThan(Cut.belowAll())) {
/* 554 */               Range<C> negativeRange = Range.create((Cut)Cut.belowAll(), this.nextComplementRangeUpperBound);
/*     */               
/* 556 */               this.nextComplementRangeUpperBound = Cut.belowAll();
/* 557 */               return Maps.immutableEntry((Cut)Cut.belowAll(), negativeRange);
/*     */             } 
/* 559 */             return endOfData();
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 566 */       return Iterators.size(entryIterator());
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public Range<C> get(Object key) {
/* 572 */       if (key instanceof Cut) {
/*     */         
/*     */         try {
/* 575 */           Cut<C> cut = (Cut<C>)key;
/*     */           
/* 577 */           Map.Entry<Cut<C>, Range<C>> firstEntry = tailMap(cut, true).firstEntry();
/* 578 */           if (firstEntry != null && ((Cut)firstEntry.getKey()).equals(cut)) {
/* 579 */             return firstEntry.getValue();
/*     */           }
/* 581 */         } catch (ClassCastException e) {
/* 582 */           return null;
/*     */         } 
/*     */       }
/* 585 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean containsKey(Object key) {
/* 590 */       return (get(key) != null);
/*     */     }
/*     */   }
/*     */   
/*     */   private final class Complement extends TreeRangeSet<C> {
/*     */     Complement() {
/* 596 */       super(new TreeRangeSet.ComplementRangesByLowerBound<Comparable<?>>(TreeRangeSet.this.rangesByLowerBound));
/*     */     }
/*     */ 
/*     */     
/*     */     public void add(Range<C> rangeToAdd) {
/* 601 */       TreeRangeSet.this.remove(rangeToAdd);
/*     */     }
/*     */ 
/*     */     
/*     */     public void remove(Range<C> rangeToRemove) {
/* 606 */       TreeRangeSet.this.add(rangeToRemove);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean contains(C value) {
/* 611 */       return !TreeRangeSet.this.contains((Comparable)value);
/*     */     }
/*     */ 
/*     */     
/*     */     public RangeSet<C> complement() {
/* 616 */       return TreeRangeSet.this;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class SubRangeSetRangesByLowerBound<C extends Comparable<?>>
/*     */     extends AbstractNavigableMap<Cut<C>, Range<C>>
/*     */   {
/*     */     private final Range<Cut<C>> lowerBoundWindow;
/*     */ 
/*     */     
/*     */     private final Range<C> restriction;
/*     */ 
/*     */     
/*     */     private final NavigableMap<Cut<C>, Range<C>> rangesByLowerBound;
/*     */ 
/*     */     
/*     */     private final NavigableMap<Cut<C>, Range<C>> rangesByUpperBound;
/*     */ 
/*     */ 
/*     */     
/*     */     private SubRangeSetRangesByLowerBound(Range<Cut<C>> lowerBoundWindow, Range<C> restriction, NavigableMap<Cut<C>, Range<C>> rangesByLowerBound) {
/* 639 */       this.lowerBoundWindow = (Range<Cut<C>>)Preconditions.checkNotNull(lowerBoundWindow);
/* 640 */       this.restriction = (Range<C>)Preconditions.checkNotNull(restriction);
/* 641 */       this.rangesByLowerBound = (NavigableMap<Cut<C>, Range<C>>)Preconditions.checkNotNull(rangesByLowerBound);
/* 642 */       this.rangesByUpperBound = new TreeRangeSet.RangesByUpperBound<C>(rangesByLowerBound);
/*     */     }
/*     */     
/*     */     private NavigableMap<Cut<C>, Range<C>> subMap(Range<Cut<C>> window) {
/* 646 */       if (!window.isConnected(this.lowerBoundWindow)) {
/* 647 */         return ImmutableSortedMap.of();
/*     */       }
/* 649 */       return new SubRangeSetRangesByLowerBound(this.lowerBoundWindow.intersection(window), this.restriction, this.rangesByLowerBound);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public NavigableMap<Cut<C>, Range<C>> subMap(Cut<C> fromKey, boolean fromInclusive, Cut<C> toKey, boolean toInclusive) {
/* 657 */       return subMap(Range.range(fromKey, BoundType.forBoolean(fromInclusive), toKey, BoundType.forBoolean(toInclusive)));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public NavigableMap<Cut<C>, Range<C>> headMap(Cut<C> toKey, boolean inclusive) {
/* 663 */       return subMap(Range.upTo(toKey, BoundType.forBoolean(inclusive)));
/*     */     }
/*     */ 
/*     */     
/*     */     public NavigableMap<Cut<C>, Range<C>> tailMap(Cut<C> fromKey, boolean inclusive) {
/* 668 */       return subMap(Range.downTo(fromKey, BoundType.forBoolean(inclusive)));
/*     */     }
/*     */ 
/*     */     
/*     */     public Comparator<? super Cut<C>> comparator() {
/* 673 */       return Ordering.natural();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean containsKey(@Nullable Object key) {
/* 678 */       return (get(key) != null);
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public Range<C> get(@Nullable Object key) {
/* 684 */       if (key instanceof Cut) {
/*     */         
/*     */         try {
/* 687 */           Cut<C> cut = (Cut<C>)key;
/* 688 */           if (!this.lowerBoundWindow.contains(cut) || cut.compareTo(this.restriction.lowerBound) < 0 || cut.compareTo(this.restriction.upperBound) >= 0)
/*     */           {
/* 690 */             return null; } 
/* 691 */           if (cut.equals(this.restriction.lowerBound)) {
/*     */             
/* 693 */             Range<C> candidate = Maps.<Range<C>>valueOrNull(this.rangesByLowerBound.floorEntry(cut));
/* 694 */             if (candidate != null && candidate.upperBound.compareTo(this.restriction.lowerBound) > 0) {
/* 695 */               return candidate.intersection(this.restriction);
/*     */             }
/*     */           } else {
/* 698 */             Range<C> result = this.rangesByLowerBound.get(cut);
/* 699 */             if (result != null) {
/* 700 */               return result.intersection(this.restriction);
/*     */             }
/*     */           } 
/* 703 */         } catch (ClassCastException e) {
/* 704 */           return null;
/*     */         } 
/*     */       }
/* 707 */       return null;
/*     */     }
/*     */     
/*     */     Iterator<Map.Entry<Cut<C>, Range<C>>> entryIterator() {
/*     */       final Iterator<Range<C>> completeRangeItr;
/* 712 */       if (this.restriction.isEmpty()) {
/* 713 */         return Iterators.emptyIterator();
/*     */       }
/*     */       
/* 716 */       if (this.lowerBoundWindow.upperBound.isLessThan(this.restriction.lowerBound))
/* 717 */         return Iterators.emptyIterator(); 
/* 718 */       if (this.lowerBoundWindow.lowerBound.isLessThan(this.restriction.lowerBound)) {
/*     */         
/* 720 */         completeRangeItr = this.rangesByUpperBound.tailMap(this.restriction.lowerBound, false).values().iterator();
/*     */       }
/*     */       else {
/*     */         
/* 724 */         completeRangeItr = this.rangesByLowerBound.tailMap(this.lowerBoundWindow.lowerBound.endpoint(), (this.lowerBoundWindow.lowerBoundType() == BoundType.CLOSED)).values().iterator();
/*     */       } 
/*     */       
/* 727 */       final Cut<Cut<C>> upperBoundOnLowerBounds = (Cut<Cut<C>>)Ordering.<Comparable>natural().min(this.lowerBoundWindow.upperBound, Cut.belowValue(this.restriction.upperBound));
/*     */       
/* 729 */       return new AbstractIterator<Map.Entry<Cut<C>, Range<C>>>()
/*     */         {
/*     */           protected Map.Entry<Cut<C>, Range<C>> computeNext() {
/* 732 */             if (!completeRangeItr.hasNext()) {
/* 733 */               return endOfData();
/*     */             }
/* 735 */             Range<C> nextRange = completeRangeItr.next();
/* 736 */             if (upperBoundOnLowerBounds.isLessThan(nextRange.lowerBound)) {
/* 737 */               return endOfData();
/*     */             }
/* 739 */             nextRange = nextRange.intersection(TreeRangeSet.SubRangeSetRangesByLowerBound.this.restriction);
/* 740 */             return Maps.immutableEntry(nextRange.lowerBound, nextRange);
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     Iterator<Map.Entry<Cut<C>, Range<C>>> descendingEntryIterator() {
/* 748 */       if (this.restriction.isEmpty()) {
/* 749 */         return Iterators.emptyIterator();
/*     */       }
/* 751 */       Cut<Cut<C>> upperBoundOnLowerBounds = (Cut<Cut<C>>)Ordering.<Comparable>natural().min(this.lowerBoundWindow.upperBound, Cut.belowValue(this.restriction.upperBound));
/*     */       
/* 753 */       final Iterator<Range<C>> completeRangeItr = this.rangesByLowerBound.headMap(upperBoundOnLowerBounds.endpoint(), (upperBoundOnLowerBounds.typeAsUpperBound() == BoundType.CLOSED)).descendingMap().values().iterator();
/*     */ 
/*     */ 
/*     */       
/* 757 */       return new AbstractIterator<Map.Entry<Cut<C>, Range<C>>>()
/*     */         {
/*     */           protected Map.Entry<Cut<C>, Range<C>> computeNext() {
/* 760 */             if (!completeRangeItr.hasNext()) {
/* 761 */               return endOfData();
/*     */             }
/* 763 */             Range<C> nextRange = completeRangeItr.next();
/* 764 */             if (TreeRangeSet.SubRangeSetRangesByLowerBound.this.restriction.lowerBound.compareTo(nextRange.upperBound) >= 0) {
/* 765 */               return endOfData();
/*     */             }
/* 767 */             nextRange = nextRange.intersection(TreeRangeSet.SubRangeSetRangesByLowerBound.this.restriction);
/* 768 */             if (TreeRangeSet.SubRangeSetRangesByLowerBound.this.lowerBoundWindow.contains(nextRange.lowerBound)) {
/* 769 */               return Maps.immutableEntry(nextRange.lowerBound, nextRange);
/*     */             }
/* 771 */             return endOfData();
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int size() {
/* 779 */       return Iterators.size(entryIterator());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public RangeSet<C> subRangeSet(Range<C> view) {
/* 785 */     return view.equals(Range.all()) ? this : new SubRangeSet(view);
/*     */   }
/*     */   
/*     */   private final class SubRangeSet extends TreeRangeSet<C> {
/*     */     private final Range<C> restriction;
/*     */     
/*     */     SubRangeSet(Range<C> restriction) {
/* 792 */       super(new TreeRangeSet.SubRangeSetRangesByLowerBound<Comparable<?>>(Range.all(), restriction, TreeRangeSet.this.rangesByLowerBound, null));
/*     */       
/* 794 */       this.restriction = restriction;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean encloses(Range<C> range) {
/* 799 */       if (!this.restriction.isEmpty() && this.restriction.encloses(range)) {
/* 800 */         Range<C> enclosing = TreeRangeSet.this.rangeEnclosing(range);
/* 801 */         return (enclosing != null && !enclosing.intersection(this.restriction).isEmpty());
/*     */       } 
/* 803 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public Range<C> rangeContaining(C value) {
/* 809 */       if (!this.restriction.contains(value)) {
/* 810 */         return null;
/*     */       }
/* 812 */       Range<C> result = TreeRangeSet.this.rangeContaining(value);
/* 813 */       return (result == null) ? null : result.intersection(this.restriction);
/*     */     }
/*     */ 
/*     */     
/*     */     public void add(Range<C> rangeToAdd) {
/* 818 */       Preconditions.checkArgument(this.restriction.encloses(rangeToAdd), "Cannot add range %s to subRangeSet(%s)", new Object[] { rangeToAdd, this.restriction });
/*     */       
/* 820 */       super.add(rangeToAdd);
/*     */     }
/*     */ 
/*     */     
/*     */     public void remove(Range<C> rangeToRemove) {
/* 825 */       if (rangeToRemove.isConnected(this.restriction)) {
/* 826 */         TreeRangeSet.this.remove(rangeToRemove.intersection(this.restriction));
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean contains(C value) {
/* 832 */       return (this.restriction.contains(value) && TreeRangeSet.this.contains((Comparable)value));
/*     */     }
/*     */ 
/*     */     
/*     */     public void clear() {
/* 837 */       TreeRangeSet.this.remove(this.restriction);
/*     */     }
/*     */ 
/*     */     
/*     */     public RangeSet<C> subRangeSet(Range<C> view) {
/* 842 */       if (view.encloses(this.restriction))
/* 843 */         return this; 
/* 844 */       if (view.isConnected(this.restriction)) {
/* 845 */         return new SubRangeSet(this.restriction.intersection(view));
/*     */       }
/* 847 */       return (RangeSet)ImmutableRangeSet.of();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\collect\TreeRangeSet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */