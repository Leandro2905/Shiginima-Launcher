/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
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
/*     */ @GwtCompatible
/*     */ public abstract class Ordering<T>
/*     */   implements Comparator<T>
/*     */ {
/*     */   static final int LEFT_IS_GREATER = 1;
/*     */   static final int RIGHT_IS_GREATER = -1;
/*     */   
/*     */   @GwtCompatible(serializable = true)
/*     */   public static <C extends Comparable> Ordering<C> natural() {
/* 106 */     return NaturalOrdering.INSTANCE;
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
/*     */   @GwtCompatible(serializable = true)
/*     */   public static <T> Ordering<T> from(Comparator<T> comparator) {
/* 124 */     return (comparator instanceof Ordering) ? (Ordering<T>)comparator : new ComparatorOrdering<T>(comparator);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   @GwtCompatible(serializable = true)
/*     */   public static <T> Ordering<T> from(Ordering<T> ordering) {
/* 136 */     return (Ordering<T>)Preconditions.checkNotNull(ordering);
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
/*     */   @GwtCompatible(serializable = true)
/*     */   public static <T> Ordering<T> explicit(List<T> valuesInOrder) {
/* 162 */     return new ExplicitOrdering<T>(valuesInOrder);
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
/*     */   @GwtCompatible(serializable = true)
/*     */   public static <T> Ordering<T> explicit(T leastValue, T... remainingValuesInOrder) {
/* 191 */     return explicit(Lists.asList(leastValue, remainingValuesInOrder));
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
/*     */   @GwtCompatible(serializable = true)
/*     */   public static Ordering<Object> allEqual() {
/* 225 */     return AllEqualOrdering.INSTANCE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtCompatible(serializable = true)
/*     */   public static Ordering<Object> usingToString() {
/* 237 */     return UsingToStringOrdering.INSTANCE;
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
/*     */   public static Ordering<Object> arbitrary() {
/* 257 */     return ArbitraryOrderingHolder.ARBITRARY_ORDERING;
/*     */   }
/*     */   
/*     */   private static class ArbitraryOrderingHolder {
/* 261 */     static final Ordering<Object> ARBITRARY_ORDERING = new Ordering.ArbitraryOrdering(); }
/*     */   
/*     */   @VisibleForTesting
/*     */   static class ArbitraryOrdering extends Ordering<Object> {
/* 265 */     private Map<Object, Integer> uids = Platform.tryWeakKeys(new MapMaker()).makeComputingMap(new Function<Object, Integer>()
/*     */         {
/*     */ 
/*     */           
/* 269 */           final AtomicInteger counter = new AtomicInteger(0);
/*     */           
/*     */           public Integer apply(Object from) {
/* 272 */             return Integer.valueOf(this.counter.getAndIncrement());
/*     */           }
/*     */         });
/*     */     
/*     */     public int compare(Object left, Object right) {
/* 277 */       if (left == right)
/* 278 */         return 0; 
/* 279 */       if (left == null)
/* 280 */         return -1; 
/* 281 */       if (right == null) {
/* 282 */         return 1;
/*     */       }
/* 284 */       int leftCode = identityHashCode(left);
/* 285 */       int rightCode = identityHashCode(right);
/* 286 */       if (leftCode != rightCode) {
/* 287 */         return (leftCode < rightCode) ? -1 : 1;
/*     */       }
/*     */ 
/*     */       
/* 291 */       int result = ((Integer)this.uids.get(left)).compareTo(this.uids.get(right));
/* 292 */       if (result == 0) {
/* 293 */         throw new AssertionError();
/*     */       }
/* 295 */       return result;
/*     */     }
/*     */     
/*     */     public String toString() {
/* 299 */       return "Ordering.arbitrary()";
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
/*     */     int identityHashCode(Object object) {
/* 311 */       return System.identityHashCode(object);
/*     */     }
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
/*     */   @GwtCompatible(serializable = true)
/*     */   public <S extends T> Ordering<S> reverse() {
/* 333 */     return new ReverseOrdering<S>(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtCompatible(serializable = true)
/*     */   public <S extends T> Ordering<S> nullsFirst() {
/* 344 */     return new NullsFirstOrdering<S>(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtCompatible(serializable = true)
/*     */   public <S extends T> Ordering<S> nullsLast() {
/* 355 */     return new NullsLastOrdering<S>(this);
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
/*     */   @GwtCompatible(serializable = true)
/*     */   public <F> Ordering<F> onResultOf(Function<F, ? extends T> function) {
/* 369 */     return new ByFunctionOrdering<F, T>(function, this);
/*     */   }
/*     */   
/*     */   <T2 extends T> Ordering<Map.Entry<T2, ?>> onKeys() {
/* 373 */     return onResultOf(Maps.keyFunction());
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
/*     */   @GwtCompatible(serializable = true)
/*     */   public <U extends T> Ordering<U> compound(Comparator<? super U> secondaryComparator) {
/* 390 */     return new CompoundOrdering<U>(this, (Comparator<? super U>)Preconditions.checkNotNull(secondaryComparator));
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
/*     */   @GwtCompatible(serializable = true)
/*     */   public static <T> Ordering<T> compound(Iterable<? extends Comparator<? super T>> comparators) {
/* 411 */     return new CompoundOrdering<T>(comparators);
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
/*     */   @GwtCompatible(serializable = true)
/*     */   public <S extends T> Ordering<Iterable<S>> lexicographical() {
/* 440 */     return new LexicographicalOrdering<S>(this);
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
/*     */   public abstract int compare(@Nullable T paramT1, @Nullable T paramT2);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <E extends T> E min(Iterator<E> iterator) {
/* 463 */     E minSoFar = iterator.next();
/*     */     
/* 465 */     while (iterator.hasNext()) {
/* 466 */       minSoFar = min(minSoFar, iterator.next());
/*     */     }
/*     */     
/* 469 */     return minSoFar;
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
/*     */   public <E extends T> E min(Iterable<E> iterable) {
/* 482 */     return min(iterable.iterator());
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
/*     */   public <E extends T> E min(@Nullable E a, @Nullable E b) {
/* 499 */     return (compare((T)a, (T)b) <= 0) ? a : b;
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
/*     */   public <E extends T> E min(@Nullable E a, @Nullable E b, @Nullable E c, E... rest) {
/* 515 */     E minSoFar = min(min(a, b), c);
/*     */     
/* 517 */     for (E r : rest) {
/* 518 */       minSoFar = min(minSoFar, r);
/*     */     }
/*     */     
/* 521 */     return minSoFar;
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
/*     */   public <E extends T> E max(Iterator<E> iterator) {
/* 539 */     E maxSoFar = iterator.next();
/*     */     
/* 541 */     while (iterator.hasNext()) {
/* 542 */       maxSoFar = max(maxSoFar, iterator.next());
/*     */     }
/*     */     
/* 545 */     return maxSoFar;
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
/*     */   public <E extends T> E max(Iterable<E> iterable) {
/* 558 */     return max(iterable.iterator());
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
/*     */   public <E extends T> E max(@Nullable E a, @Nullable E b) {
/* 575 */     return (compare((T)a, (T)b) >= 0) ? a : b;
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
/*     */   public <E extends T> E max(@Nullable E a, @Nullable E b, @Nullable E c, E... rest) {
/* 591 */     E maxSoFar = max(max(a, b), c);
/*     */     
/* 593 */     for (E r : rest) {
/* 594 */       maxSoFar = max(maxSoFar, r);
/*     */     }
/*     */     
/* 597 */     return maxSoFar;
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
/*     */   public <E extends T> List<E> leastOf(Iterable<E> iterable, int k) {
/* 615 */     if (iterable instanceof Collection) {
/* 616 */       Collection<E> collection = (Collection<E>)iterable;
/* 617 */       if (collection.size() <= 2L * k) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 623 */         E[] array = (E[])collection.toArray();
/* 624 */         Arrays.sort(array, this);
/* 625 */         if (array.length > k) {
/* 626 */           array = ObjectArrays.arraysCopyOf(array, k);
/*     */         }
/* 628 */         return Collections.unmodifiableList(Arrays.asList(array));
/*     */       } 
/*     */     } 
/* 631 */     return leastOf(iterable.iterator(), k);
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
/*     */   public <E extends T> List<E> leastOf(Iterator<E> elements, int k) {
/* 649 */     Preconditions.checkNotNull(elements);
/* 650 */     CollectPreconditions.checkNonnegative(k, "k");
/*     */     
/* 652 */     if (k == 0 || !elements.hasNext())
/* 653 */       return ImmutableList.of(); 
/* 654 */     if (k >= 1073741823) {
/*     */       
/* 656 */       ArrayList<E> list = Lists.newArrayList(elements);
/* 657 */       Collections.sort(list, this);
/* 658 */       if (list.size() > k) {
/* 659 */         list.subList(k, list.size()).clear();
/*     */       }
/* 661 */       list.trimToSize();
/* 662 */       return Collections.unmodifiableList(list);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 679 */     int bufferCap = k * 2;
/*     */     
/* 681 */     E[] buffer = (E[])new Object[bufferCap];
/* 682 */     E threshold = elements.next();
/* 683 */     buffer[0] = threshold;
/* 684 */     int bufferSize = 1;
/*     */ 
/*     */ 
/*     */     
/* 688 */     while (bufferSize < k && elements.hasNext()) {
/* 689 */       E e = elements.next();
/* 690 */       buffer[bufferSize++] = e;
/* 691 */       threshold = max(threshold, e);
/*     */     } 
/*     */     
/* 694 */     while (elements.hasNext()) {
/* 695 */       E e = elements.next();
/* 696 */       if (compare((T)e, (T)threshold) >= 0) {
/*     */         continue;
/*     */       }
/*     */       
/* 700 */       buffer[bufferSize++] = e;
/* 701 */       if (bufferSize == bufferCap) {
/*     */ 
/*     */         
/* 704 */         int left = 0;
/* 705 */         int right = bufferCap - 1;
/*     */         
/* 707 */         int minThresholdPosition = 0;
/*     */ 
/*     */ 
/*     */         
/* 711 */         while (left < right) {
/* 712 */           int pivotIndex = left + right + 1 >>> 1;
/* 713 */           int pivotNewIndex = partition(buffer, left, right, pivotIndex);
/* 714 */           if (pivotNewIndex > k) {
/* 715 */             right = pivotNewIndex - 1; continue;
/* 716 */           }  if (pivotNewIndex < k) {
/* 717 */             left = Math.max(pivotNewIndex, left + 1);
/* 718 */             minThresholdPosition = pivotNewIndex;
/*     */           } 
/*     */         } 
/*     */ 
/*     */         
/* 723 */         bufferSize = k;
/*     */         
/* 725 */         threshold = buffer[minThresholdPosition];
/* 726 */         for (int i = minThresholdPosition + 1; i < bufferSize; i++) {
/* 727 */           threshold = max(threshold, buffer[i]);
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 732 */     Arrays.sort(buffer, 0, bufferSize, this);
/*     */     
/* 734 */     bufferSize = Math.min(bufferSize, k);
/* 735 */     return Collections.unmodifiableList(Arrays.asList(ObjectArrays.arraysCopyOf(buffer, bufferSize)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private <E extends T> int partition(E[] values, int left, int right, int pivotIndex) {
/* 742 */     E pivotValue = values[pivotIndex];
/*     */     
/* 744 */     values[pivotIndex] = values[right];
/* 745 */     values[right] = pivotValue;
/*     */     
/* 747 */     int storeIndex = left;
/* 748 */     for (int i = left; i < right; i++) {
/* 749 */       if (compare((T)values[i], (T)pivotValue) < 0) {
/* 750 */         ObjectArrays.swap((Object[])values, storeIndex, i);
/* 751 */         storeIndex++;
/*     */       } 
/*     */     } 
/* 754 */     ObjectArrays.swap((Object[])values, right, storeIndex);
/* 755 */     return storeIndex;
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
/*     */   public <E extends T> List<E> greatestOf(Iterable<E> iterable, int k) {
/* 775 */     return reverse().leastOf(iterable, k);
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
/*     */   public <E extends T> List<E> greatestOf(Iterator<E> iterator, int k) {
/* 793 */     return reverse().leastOf(iterator, k);
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
/*     */   public <E extends T> List<E> sortedCopy(Iterable<E> elements) {
/* 816 */     E[] array = (E[])Iterables.toArray(elements);
/* 817 */     Arrays.sort(array, this);
/* 818 */     return Lists.newArrayList(Arrays.asList(array));
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
/*     */   public <E extends T> ImmutableList<E> immutableSortedCopy(Iterable<E> elements) {
/* 842 */     E[] array = (E[])Iterables.toArray(elements);
/* 843 */     for (E e : array) {
/* 844 */       Preconditions.checkNotNull(e);
/*     */     }
/* 846 */     Arrays.sort(array, this);
/* 847 */     return ImmutableList.asImmutableList((Object[])array);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isOrdered(Iterable<? extends T> iterable) {
/* 857 */     Iterator<? extends T> it = iterable.iterator();
/* 858 */     if (it.hasNext()) {
/* 859 */       T prev = it.next();
/* 860 */       while (it.hasNext()) {
/* 861 */         T next = it.next();
/* 862 */         if (compare(prev, next) > 0) {
/* 863 */           return false;
/*     */         }
/* 865 */         prev = next;
/*     */       } 
/*     */     } 
/* 868 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isStrictlyOrdered(Iterable<? extends T> iterable) {
/* 878 */     Iterator<? extends T> it = iterable.iterator();
/* 879 */     if (it.hasNext()) {
/* 880 */       T prev = it.next();
/* 881 */       while (it.hasNext()) {
/* 882 */         T next = it.next();
/* 883 */         if (compare(prev, next) >= 0) {
/* 884 */           return false;
/*     */         }
/* 886 */         prev = next;
/*     */       } 
/*     */     } 
/* 889 */     return true;
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
/*     */   public int binarySearch(List<? extends T> sortedList, @Nullable T key) {
/* 901 */     return Collections.binarySearch(sortedList, key, this);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   static class IncomparableValueException
/*     */     extends ClassCastException
/*     */   {
/*     */     final Object value;
/*     */     
/*     */     private static final long serialVersionUID = 0L;
/*     */ 
/*     */     
/*     */     IncomparableValueException(Object value) {
/* 916 */       super((new StringBuilder(22 + str.length())).append("Cannot compare value: ").append(str).toString());
/* 917 */       this.value = value;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\collect\Ordering.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */