/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.base.Joiner;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Predicate;
/*     */ import com.google.common.base.Predicates;
/*     */ import com.google.common.math.IntMath;
/*     */ import com.google.common.math.LongMath;
/*     */ import java.util.AbstractCollection;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
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
/*     */ @GwtCompatible
/*     */ public final class Collections2
/*     */ {
/*     */   public static <E> Collection<E> filter(Collection<E> unfiltered, Predicate<? super E> predicate) {
/*  91 */     if (unfiltered instanceof FilteredCollection)
/*     */     {
/*     */       
/*  94 */       return ((FilteredCollection<E>)unfiltered).createCombined(predicate);
/*     */     }
/*     */     
/*  97 */     return new FilteredCollection<E>((Collection<E>)Preconditions.checkNotNull(unfiltered), (Predicate<? super E>)Preconditions.checkNotNull(predicate));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static boolean safeContains(Collection<?> collection, @Nullable Object object) {
/* 108 */     Preconditions.checkNotNull(collection);
/*     */     try {
/* 110 */       return collection.contains(object);
/* 111 */     } catch (ClassCastException e) {
/* 112 */       return false;
/* 113 */     } catch (NullPointerException e) {
/* 114 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static boolean safeRemove(Collection<?> collection, @Nullable Object object) {
/* 124 */     Preconditions.checkNotNull(collection);
/*     */     try {
/* 126 */       return collection.remove(object);
/* 127 */     } catch (ClassCastException e) {
/* 128 */       return false;
/* 129 */     } catch (NullPointerException e) {
/* 130 */       return false;
/*     */     } 
/*     */   }
/*     */   
/*     */   static class FilteredCollection<E>
/*     */     extends AbstractCollection<E> {
/*     */     final Collection<E> unfiltered;
/*     */     final Predicate<? super E> predicate;
/*     */     
/*     */     FilteredCollection(Collection<E> unfiltered, Predicate<? super E> predicate) {
/* 140 */       this.unfiltered = unfiltered;
/* 141 */       this.predicate = predicate;
/*     */     }
/*     */     
/*     */     FilteredCollection<E> createCombined(Predicate<? super E> newPredicate) {
/* 145 */       return new FilteredCollection(this.unfiltered, Predicates.and(this.predicate, newPredicate));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean add(E element) {
/* 152 */       Preconditions.checkArgument(this.predicate.apply(element));
/* 153 */       return this.unfiltered.add(element);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean addAll(Collection<? extends E> collection) {
/* 158 */       for (E element : collection) {
/* 159 */         Preconditions.checkArgument(this.predicate.apply(element));
/*     */       }
/* 161 */       return this.unfiltered.addAll(collection);
/*     */     }
/*     */ 
/*     */     
/*     */     public void clear() {
/* 166 */       Iterables.removeIf(this.unfiltered, this.predicate);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean contains(@Nullable Object element) {
/* 171 */       if (Collections2.safeContains(this.unfiltered, element)) {
/*     */         
/* 173 */         E e = (E)element;
/* 174 */         return this.predicate.apply(e);
/*     */       } 
/* 176 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean containsAll(Collection<?> collection) {
/* 181 */       return Collections2.containsAllImpl(this, collection);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isEmpty() {
/* 186 */       return !Iterables.any(this.unfiltered, this.predicate);
/*     */     }
/*     */ 
/*     */     
/*     */     public Iterator<E> iterator() {
/* 191 */       return Iterators.filter(this.unfiltered.iterator(), this.predicate);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean remove(Object element) {
/* 196 */       return (contains(element) && this.unfiltered.remove(element));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean removeAll(Collection<?> collection) {
/* 201 */       return Iterables.removeIf(this.unfiltered, Predicates.and(this.predicate, Predicates.in(collection)));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean retainAll(Collection<?> collection) {
/* 206 */       return Iterables.removeIf(this.unfiltered, Predicates.and(this.predicate, Predicates.not(Predicates.in(collection))));
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 211 */       return Iterators.size(iterator());
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public Object[] toArray() {
/* 217 */       return Lists.<E>newArrayList(iterator()).toArray();
/*     */     }
/*     */ 
/*     */     
/*     */     public <T> T[] toArray(T[] array) {
/* 222 */       return (T[])Lists.<E>newArrayList(iterator()).toArray((Object[])array);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <F, T> Collection<T> transform(Collection<F> fromCollection, Function<? super F, T> function) {
/* 247 */     return new TransformedCollection<F, T>(fromCollection, function);
/*     */   }
/*     */   
/*     */   static class TransformedCollection<F, T>
/*     */     extends AbstractCollection<T> {
/*     */     final Collection<F> fromCollection;
/*     */     final Function<? super F, ? extends T> function;
/*     */     
/*     */     TransformedCollection(Collection<F> fromCollection, Function<? super F, ? extends T> function) {
/* 256 */       this.fromCollection = (Collection<F>)Preconditions.checkNotNull(fromCollection);
/* 257 */       this.function = (Function<? super F, ? extends T>)Preconditions.checkNotNull(function);
/*     */     }
/*     */     
/*     */     public void clear() {
/* 261 */       this.fromCollection.clear();
/*     */     }
/*     */     
/*     */     public boolean isEmpty() {
/* 265 */       return this.fromCollection.isEmpty();
/*     */     }
/*     */     
/*     */     public Iterator<T> iterator() {
/* 269 */       return Iterators.transform(this.fromCollection.iterator(), this.function);
/*     */     }
/*     */     
/*     */     public int size() {
/* 273 */       return this.fromCollection.size();
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
/*     */   static boolean containsAllImpl(Collection<?> self, Collection<?> c) {
/* 290 */     return Iterables.all(c, Predicates.in(self));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static String toStringImpl(final Collection<?> collection) {
/* 297 */     StringBuilder sb = newStringBuilderForCollection(collection.size()).append('[');
/*     */     
/* 299 */     STANDARD_JOINER.appendTo(sb, Iterables.transform(collection, new Function<Object, Object>()
/*     */           {
/*     */             public Object apply(Object input) {
/* 302 */               return (input == collection) ? "(this Collection)" : input;
/*     */             }
/*     */           }));
/* 305 */     return sb.append(']').toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static StringBuilder newStringBuilderForCollection(int size) {
/* 312 */     CollectPreconditions.checkNonnegative(size, "size");
/* 313 */     return new StringBuilder((int)Math.min(size * 8L, 1073741824L));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <T> Collection<T> cast(Iterable<T> iterable) {
/* 320 */     return (Collection<T>)iterable;
/*     */   }
/*     */   
/* 323 */   static final Joiner STANDARD_JOINER = Joiner.on(", ").useForNull("null");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */   public static <E extends Comparable<? super E>> Collection<List<E>> orderedPermutations(Iterable<E> elements) {
/* 354 */     return orderedPermutations(elements, Ordering.natural());
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Beta
/*     */   public static <E> Collection<List<E>> orderedPermutations(Iterable<E> elements, Comparator<? super E> comparator) {
/* 406 */     return new OrderedPermutationCollection<E>(elements, comparator);
/*     */   }
/*     */   
/*     */   private static final class OrderedPermutationCollection<E>
/*     */     extends AbstractCollection<List<E>>
/*     */   {
/*     */     final ImmutableList<E> inputList;
/*     */     final Comparator<? super E> comparator;
/*     */     final int size;
/*     */     
/*     */     OrderedPermutationCollection(Iterable<E> input, Comparator<? super E> comparator) {
/* 417 */       this.inputList = Ordering.<E>from(comparator).immutableSortedCopy(input);
/* 418 */       this.comparator = comparator;
/* 419 */       this.size = calculateSize(this.inputList, comparator);
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
/*     */     private static <E> int calculateSize(List<E> sortedInputList, Comparator<? super E> comparator) {
/* 433 */       long permutations = 1L;
/* 434 */       int n = 1;
/* 435 */       int r = 1;
/* 436 */       while (n < sortedInputList.size()) {
/* 437 */         int comparison = comparator.compare(sortedInputList.get(n - 1), sortedInputList.get(n));
/*     */         
/* 439 */         if (comparison < 0) {
/*     */           
/* 441 */           permutations *= LongMath.binomial(n, r);
/* 442 */           r = 0;
/* 443 */           if (!Collections2.isPositiveInt(permutations)) {
/* 444 */             return Integer.MAX_VALUE;
/*     */           }
/*     */         } 
/* 447 */         n++;
/* 448 */         r++;
/*     */       } 
/* 450 */       permutations *= LongMath.binomial(n, r);
/* 451 */       if (!Collections2.isPositiveInt(permutations)) {
/* 452 */         return Integer.MAX_VALUE;
/*     */       }
/* 454 */       return (int)permutations;
/*     */     }
/*     */     
/*     */     public int size() {
/* 458 */       return this.size;
/*     */     }
/*     */     
/*     */     public boolean isEmpty() {
/* 462 */       return false;
/*     */     }
/*     */     
/*     */     public Iterator<List<E>> iterator() {
/* 466 */       return new Collections2.OrderedPermutationIterator<E>(this.inputList, this.comparator);
/*     */     }
/*     */     
/*     */     public boolean contains(@Nullable Object obj) {
/* 470 */       if (obj instanceof List) {
/* 471 */         List<?> list = (List)obj;
/* 472 */         return Collections2.isPermutation(this.inputList, list);
/*     */       } 
/* 474 */       return false;
/*     */     }
/*     */     
/*     */     public String toString() {
/* 478 */       String str = String.valueOf(String.valueOf(this.inputList)); return (new StringBuilder(30 + str.length())).append("orderedPermutationCollection(").append(str).append(")").toString();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static final class OrderedPermutationIterator<E>
/*     */     extends AbstractIterator<List<E>>
/*     */   {
/*     */     List<E> nextPermutation;
/*     */     final Comparator<? super E> comparator;
/*     */     
/*     */     OrderedPermutationIterator(List<E> list, Comparator<? super E> comparator) {
/* 490 */       this.nextPermutation = Lists.newArrayList(list);
/* 491 */       this.comparator = comparator;
/*     */     }
/*     */     
/*     */     protected List<E> computeNext() {
/* 495 */       if (this.nextPermutation == null) {
/* 496 */         return endOfData();
/*     */       }
/* 498 */       ImmutableList<E> next = ImmutableList.copyOf(this.nextPermutation);
/* 499 */       calculateNextPermutation();
/* 500 */       return next;
/*     */     }
/*     */     
/*     */     void calculateNextPermutation() {
/* 504 */       int j = findNextJ();
/* 505 */       if (j == -1) {
/* 506 */         this.nextPermutation = null;
/*     */         
/*     */         return;
/*     */       } 
/* 510 */       int l = findNextL(j);
/* 511 */       Collections.swap(this.nextPermutation, j, l);
/* 512 */       int n = this.nextPermutation.size();
/* 513 */       Collections.reverse(this.nextPermutation.subList(j + 1, n));
/*     */     }
/*     */     
/*     */     int findNextJ() {
/* 517 */       for (int k = this.nextPermutation.size() - 2; k >= 0; k--) {
/* 518 */         if (this.comparator.compare(this.nextPermutation.get(k), this.nextPermutation.get(k + 1)) < 0)
/*     */         {
/* 520 */           return k;
/*     */         }
/*     */       } 
/* 523 */       return -1;
/*     */     }
/*     */     
/*     */     int findNextL(int j) {
/* 527 */       E ak = this.nextPermutation.get(j);
/* 528 */       for (int l = this.nextPermutation.size() - 1; l > j; l--) {
/* 529 */         if (this.comparator.compare(ak, this.nextPermutation.get(l)) < 0) {
/* 530 */           return l;
/*     */         }
/*     */       } 
/* 533 */       throw new AssertionError("this statement should be unreachable");
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Beta
/*     */   public static <E> Collection<List<E>> permutations(Collection<E> elements) {
/* 559 */     return new PermutationCollection<E>(ImmutableList.copyOf(elements));
/*     */   }
/*     */   
/*     */   private static final class PermutationCollection<E>
/*     */     extends AbstractCollection<List<E>> {
/*     */     final ImmutableList<E> inputList;
/*     */     
/*     */     PermutationCollection(ImmutableList<E> input) {
/* 567 */       this.inputList = input;
/*     */     }
/*     */     
/*     */     public int size() {
/* 571 */       return IntMath.factorial(this.inputList.size());
/*     */     }
/*     */     
/*     */     public boolean isEmpty() {
/* 575 */       return false;
/*     */     }
/*     */     
/*     */     public Iterator<List<E>> iterator() {
/* 579 */       return new Collections2.PermutationIterator<E>(this.inputList);
/*     */     }
/*     */     
/*     */     public boolean contains(@Nullable Object obj) {
/* 583 */       if (obj instanceof List) {
/* 584 */         List<?> list = (List)obj;
/* 585 */         return Collections2.isPermutation(this.inputList, list);
/*     */       } 
/* 587 */       return false;
/*     */     }
/*     */     
/*     */     public String toString() {
/* 591 */       String str = String.valueOf(String.valueOf(this.inputList)); return (new StringBuilder(14 + str.length())).append("permutations(").append(str).append(")").toString();
/*     */     }
/*     */   }
/*     */   
/*     */   private static class PermutationIterator<E>
/*     */     extends AbstractIterator<List<E>> {
/*     */     final List<E> list;
/*     */     final int[] c;
/*     */     final int[] o;
/*     */     int j;
/*     */     
/*     */     PermutationIterator(List<E> list) {
/* 603 */       this.list = new ArrayList<E>(list);
/* 604 */       int n = list.size();
/* 605 */       this.c = new int[n];
/* 606 */       this.o = new int[n];
/* 607 */       Arrays.fill(this.c, 0);
/* 608 */       Arrays.fill(this.o, 1);
/* 609 */       this.j = Integer.MAX_VALUE;
/*     */     }
/*     */     
/*     */     protected List<E> computeNext() {
/* 613 */       if (this.j <= 0) {
/* 614 */         return endOfData();
/*     */       }
/* 616 */       ImmutableList<E> next = ImmutableList.copyOf(this.list);
/* 617 */       calculateNextPermutation();
/* 618 */       return next;
/*     */     }
/*     */     
/*     */     void calculateNextPermutation() {
/* 622 */       this.j = this.list.size() - 1;
/* 623 */       int s = 0;
/*     */ 
/*     */ 
/*     */       
/* 627 */       if (this.j == -1) {
/*     */         return;
/*     */       }
/*     */       
/*     */       while (true) {
/* 632 */         int q = this.c[this.j] + this.o[this.j];
/* 633 */         if (q < 0) {
/* 634 */           switchDirection();
/*     */           continue;
/*     */         } 
/* 637 */         if (q == this.j + 1) {
/* 638 */           if (this.j == 0) {
/*     */             break;
/*     */           }
/* 641 */           s++;
/* 642 */           switchDirection();
/*     */           
/*     */           continue;
/*     */         } 
/* 646 */         Collections.swap(this.list, this.j - this.c[this.j] + s, this.j - q + s);
/* 647 */         this.c[this.j] = q;
/*     */         break;
/*     */       } 
/*     */     }
/*     */     
/*     */     void switchDirection() {
/* 653 */       this.o[this.j] = -this.o[this.j];
/* 654 */       this.j--;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean isPermutation(List<?> first, List<?> second) {
/* 663 */     if (first.size() != second.size()) {
/* 664 */       return false;
/*     */     }
/* 666 */     Multiset<?> firstMultiset = HashMultiset.create(first);
/* 667 */     Multiset<?> secondMultiset = HashMultiset.create(second);
/* 668 */     return firstMultiset.equals(secondMultiset);
/*     */   }
/*     */   
/*     */   private static boolean isPositiveInt(long n) {
/* 672 */     return (n >= 0L && n <= 2147483647L);
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\collect\Collections2.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */