/*      */ package com.google.common.collect;
/*      */ 
/*      */ import com.google.common.annotations.Beta;
/*      */ import com.google.common.annotations.GwtCompatible;
/*      */ import com.google.common.annotations.GwtIncompatible;
/*      */ import com.google.common.annotations.VisibleForTesting;
/*      */ import com.google.common.base.Function;
/*      */ import com.google.common.base.Objects;
/*      */ import com.google.common.base.Preconditions;
/*      */ import com.google.common.math.IntMath;
/*      */ import com.google.common.primitives.Ints;
/*      */ import java.io.Serializable;
/*      */ import java.math.RoundingMode;
/*      */ import java.util.AbstractList;
/*      */ import java.util.AbstractSequentialList;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedList;
/*      */ import java.util.List;
/*      */ import java.util.ListIterator;
/*      */ import java.util.NoSuchElementException;
/*      */ import java.util.RandomAccess;
/*      */ import java.util.concurrent.CopyOnWriteArrayList;
/*      */ import javax.annotation.Nullable;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ @GwtCompatible(emulated = true)
/*      */ public final class Lists
/*      */ {
/*      */   @GwtCompatible(serializable = true)
/*      */   public static <E> ArrayList<E> newArrayList() {
/*   88 */     return new ArrayList<E>();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtCompatible(serializable = true)
/*      */   public static <E> ArrayList<E> newArrayList(E... elements) {
/*  110 */     Preconditions.checkNotNull(elements);
/*      */     
/*  112 */     int capacity = computeArrayListCapacity(elements.length);
/*  113 */     ArrayList<E> list = new ArrayList<E>(capacity);
/*  114 */     Collections.addAll(list, elements);
/*  115 */     return list;
/*      */   }
/*      */   @VisibleForTesting
/*      */   static int computeArrayListCapacity(int arraySize) {
/*  119 */     CollectPreconditions.checkNonnegative(arraySize, "arraySize");
/*      */ 
/*      */     
/*  122 */     return Ints.saturatedCast(5L + arraySize + (arraySize / 10));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtCompatible(serializable = true)
/*      */   public static <E> ArrayList<E> newArrayList(Iterable<? extends E> elements) {
/*  142 */     Preconditions.checkNotNull(elements);
/*      */     
/*  144 */     return (elements instanceof Collection) ? new ArrayList<E>(Collections2.cast(elements)) : newArrayList(elements.iterator());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtCompatible(serializable = true)
/*      */   public static <E> ArrayList<E> newArrayList(Iterator<? extends E> elements) {
/*  159 */     ArrayList<E> list = newArrayList();
/*  160 */     Iterators.addAll(list, elements);
/*  161 */     return list;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtCompatible(serializable = true)
/*      */   public static <E> ArrayList<E> newArrayListWithCapacity(int initialArraySize) {
/*  185 */     CollectPreconditions.checkNonnegative(initialArraySize, "initialArraySize");
/*  186 */     return new ArrayList<E>(initialArraySize);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtCompatible(serializable = true)
/*      */   public static <E> ArrayList<E> newArrayListWithExpectedSize(int estimatedSize) {
/*  208 */     return new ArrayList<E>(computeArrayListCapacity(estimatedSize));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtCompatible(serializable = true)
/*      */   public static <E> LinkedList<E> newLinkedList() {
/*  232 */     return new LinkedList<E>();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtCompatible(serializable = true)
/*      */   public static <E> LinkedList<E> newLinkedList(Iterable<? extends E> elements) {
/*  258 */     LinkedList<E> list = newLinkedList();
/*  259 */     Iterables.addAll(list, elements);
/*  260 */     return list;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible("CopyOnWriteArrayList")
/*      */   public static <E> CopyOnWriteArrayList<E> newCopyOnWriteArrayList() {
/*  274 */     return new CopyOnWriteArrayList<E>();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible("CopyOnWriteArrayList")
/*      */   public static <E> CopyOnWriteArrayList<E> newCopyOnWriteArrayList(Iterable<? extends E> elements) {
/*  289 */     Collection<? extends E> elementsCollection = (elements instanceof Collection) ? Collections2.<E>cast(elements) : newArrayList(elements);
/*      */ 
/*      */     
/*  292 */     return new CopyOnWriteArrayList<E>(elementsCollection);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <E> List<E> asList(@Nullable E first, E[] rest) {
/*  312 */     return new OnePlusArrayList<E>(first, rest);
/*      */   }
/*      */   
/*      */   private static class OnePlusArrayList<E>
/*      */     extends AbstractList<E> implements Serializable, RandomAccess {
/*      */     final E first;
/*      */     final E[] rest;
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     OnePlusArrayList(@Nullable E first, E[] rest) {
/*  322 */       this.first = first;
/*  323 */       this.rest = (E[])Preconditions.checkNotNull(rest);
/*      */     }
/*      */     public int size() {
/*  326 */       return this.rest.length + 1;
/*      */     }
/*      */     
/*      */     public E get(int index) {
/*  330 */       Preconditions.checkElementIndex(index, size());
/*  331 */       return (index == 0) ? this.first : this.rest[index - 1];
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <E> List<E> asList(@Nullable E first, @Nullable E second, E[] rest) {
/*  355 */     return new TwoPlusArrayList<E>(first, second, rest);
/*      */   }
/*      */   
/*      */   private static class TwoPlusArrayList<E>
/*      */     extends AbstractList<E> implements Serializable, RandomAccess {
/*      */     final E first;
/*      */     final E second;
/*      */     final E[] rest;
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     TwoPlusArrayList(@Nullable E first, @Nullable E second, E[] rest) {
/*  366 */       this.first = first;
/*  367 */       this.second = second;
/*  368 */       this.rest = (E[])Preconditions.checkNotNull(rest);
/*      */     }
/*      */     public int size() {
/*  371 */       return this.rest.length + 2;
/*      */     }
/*      */     public E get(int index) {
/*  374 */       switch (index) {
/*      */         case 0:
/*  376 */           return this.first;
/*      */         case 1:
/*  378 */           return this.second;
/*      */       } 
/*      */       
/*  381 */       Preconditions.checkElementIndex(index, size());
/*  382 */       return this.rest[index - 2];
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static <B> List<List<B>> cartesianProduct(List<? extends List<? extends B>> lists) {
/*  445 */     return CartesianList.create(lists);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static <B> List<List<B>> cartesianProduct(List<? extends B>... lists) {
/*  505 */     return cartesianProduct(Arrays.asList(lists));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <F, T> List<T> transform(List<F> fromList, Function<? super F, ? extends T> function) {
/*  543 */     return (fromList instanceof RandomAccess) ? new TransformingRandomAccessList<F, T>(fromList, function) : new TransformingSequentialList<F, T>(fromList, function);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static class TransformingSequentialList<F, T>
/*      */     extends AbstractSequentialList<T>
/*      */     implements Serializable
/*      */   {
/*      */     final List<F> fromList;
/*      */     
/*      */     final Function<? super F, ? extends T> function;
/*      */     
/*      */     private static final long serialVersionUID = 0L;
/*      */ 
/*      */     
/*      */     TransformingSequentialList(List<F> fromList, Function<? super F, ? extends T> function) {
/*  560 */       this.fromList = (List<F>)Preconditions.checkNotNull(fromList);
/*  561 */       this.function = (Function<? super F, ? extends T>)Preconditions.checkNotNull(function);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void clear() {
/*  569 */       this.fromList.clear();
/*      */     }
/*      */     public int size() {
/*  572 */       return this.fromList.size();
/*      */     }
/*      */     public ListIterator<T> listIterator(int index) {
/*  575 */       return new TransformedListIterator<F, T>(this.fromList.listIterator(index))
/*      */         {
/*      */           T transform(F from) {
/*  578 */             return (T)Lists.TransformingSequentialList.this.function.apply(from);
/*      */           }
/*      */         };
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static class TransformingRandomAccessList<F, T>
/*      */     extends AbstractList<T>
/*      */     implements RandomAccess, Serializable
/*      */   {
/*      */     final List<F> fromList;
/*      */ 
/*      */     
/*      */     final Function<? super F, ? extends T> function;
/*      */ 
/*      */     
/*      */     private static final long serialVersionUID = 0L;
/*      */ 
/*      */ 
/*      */     
/*      */     TransformingRandomAccessList(List<F> fromList, Function<? super F, ? extends T> function) {
/*  601 */       this.fromList = (List<F>)Preconditions.checkNotNull(fromList);
/*  602 */       this.function = (Function<? super F, ? extends T>)Preconditions.checkNotNull(function);
/*      */     }
/*      */     public void clear() {
/*  605 */       this.fromList.clear();
/*      */     }
/*      */     public T get(int index) {
/*  608 */       return (T)this.function.apply(this.fromList.get(index));
/*      */     }
/*      */     public Iterator<T> iterator() {
/*  611 */       return listIterator();
/*      */     }
/*      */     public ListIterator<T> listIterator(int index) {
/*  614 */       return new TransformedListIterator<F, T>(this.fromList.listIterator(index))
/*      */         {
/*      */           T transform(F from) {
/*  617 */             return (T)Lists.TransformingRandomAccessList.this.function.apply(from);
/*      */           }
/*      */         };
/*      */     }
/*      */     public boolean isEmpty() {
/*  622 */       return this.fromList.isEmpty();
/*      */     }
/*      */     public T remove(int index) {
/*  625 */       return (T)this.function.apply(this.fromList.remove(index));
/*      */     }
/*      */     public int size() {
/*  628 */       return this.fromList.size();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> List<List<T>> partition(List<T> list, int size) {
/*  652 */     Preconditions.checkNotNull(list);
/*  653 */     Preconditions.checkArgument((size > 0));
/*  654 */     return (list instanceof RandomAccess) ? new RandomAccessPartition<T>(list, size) : new Partition<T>(list, size);
/*      */   }
/*      */   
/*      */   private static class Partition<T>
/*      */     extends AbstractList<List<T>>
/*      */   {
/*      */     final List<T> list;
/*      */     final int size;
/*      */     
/*      */     Partition(List<T> list, int size) {
/*  664 */       this.list = list;
/*  665 */       this.size = size;
/*      */     }
/*      */     
/*      */     public List<T> get(int index) {
/*  669 */       Preconditions.checkElementIndex(index, size());
/*  670 */       int start = index * this.size;
/*  671 */       int end = Math.min(start + this.size, this.list.size());
/*  672 */       return this.list.subList(start, end);
/*      */     }
/*      */     
/*      */     public int size() {
/*  676 */       return IntMath.divide(this.list.size(), this.size, RoundingMode.CEILING);
/*      */     }
/*      */     
/*      */     public boolean isEmpty() {
/*  680 */       return this.list.isEmpty();
/*      */     }
/*      */   }
/*      */   
/*      */   private static class RandomAccessPartition<T>
/*      */     extends Partition<T> implements RandomAccess {
/*      */     RandomAccessPartition(List<T> list, int size) {
/*  687 */       super(list, size);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Beta
/*      */   public static ImmutableList<Character> charactersOf(String string) {
/*  698 */     return new StringAsImmutableList((String)Preconditions.checkNotNull(string));
/*      */   }
/*      */ 
/*      */   
/*      */   private static final class StringAsImmutableList
/*      */     extends ImmutableList<Character>
/*      */   {
/*      */     private final String string;
/*      */     
/*      */     StringAsImmutableList(String string) {
/*  708 */       this.string = string;
/*      */     }
/*      */     
/*      */     public int indexOf(@Nullable Object object) {
/*  712 */       return (object instanceof Character) ? this.string.indexOf(((Character)object).charValue()) : -1;
/*      */     }
/*      */ 
/*      */     
/*      */     public int lastIndexOf(@Nullable Object object) {
/*  717 */       return (object instanceof Character) ? this.string.lastIndexOf(((Character)object).charValue()) : -1;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public ImmutableList<Character> subList(int fromIndex, int toIndex) {
/*  723 */       Preconditions.checkPositionIndexes(fromIndex, toIndex, size());
/*  724 */       return Lists.charactersOf(this.string.substring(fromIndex, toIndex));
/*      */     }
/*      */     
/*      */     boolean isPartialView() {
/*  728 */       return false;
/*      */     }
/*      */     
/*      */     public Character get(int index) {
/*  732 */       Preconditions.checkElementIndex(index, size());
/*  733 */       return Character.valueOf(this.string.charAt(index));
/*      */     }
/*      */     
/*      */     public int size() {
/*  737 */       return this.string.length();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Beta
/*      */   public static List<Character> charactersOf(CharSequence sequence) {
/*  753 */     return new CharSequenceAsList((CharSequence)Preconditions.checkNotNull(sequence));
/*      */   }
/*      */   
/*      */   private static final class CharSequenceAsList
/*      */     extends AbstractList<Character> {
/*      */     private final CharSequence sequence;
/*      */     
/*      */     CharSequenceAsList(CharSequence sequence) {
/*  761 */       this.sequence = sequence;
/*      */     }
/*      */     
/*      */     public Character get(int index) {
/*  765 */       Preconditions.checkElementIndex(index, size());
/*  766 */       return Character.valueOf(this.sequence.charAt(index));
/*      */     }
/*      */     
/*      */     public int size() {
/*  770 */       return this.sequence.length();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> List<T> reverse(List<T> list) {
/*  787 */     if (list instanceof ImmutableList)
/*  788 */       return ((ImmutableList<T>)list).reverse(); 
/*  789 */     if (list instanceof ReverseList)
/*  790 */       return ((ReverseList<T>)list).getForwardList(); 
/*  791 */     if (list instanceof RandomAccess) {
/*  792 */       return new RandomAccessReverseList<T>(list);
/*      */     }
/*  794 */     return new ReverseList<T>(list);
/*      */   }
/*      */   
/*      */   private static class ReverseList<T>
/*      */     extends AbstractList<T> {
/*      */     private final List<T> forwardList;
/*      */     
/*      */     ReverseList(List<T> forwardList) {
/*  802 */       this.forwardList = (List<T>)Preconditions.checkNotNull(forwardList);
/*      */     }
/*      */     
/*      */     List<T> getForwardList() {
/*  806 */       return this.forwardList;
/*      */     }
/*      */     
/*      */     private int reverseIndex(int index) {
/*  810 */       int size = size();
/*  811 */       Preconditions.checkElementIndex(index, size);
/*  812 */       return size - 1 - index;
/*      */     }
/*      */     
/*      */     private int reversePosition(int index) {
/*  816 */       int size = size();
/*  817 */       Preconditions.checkPositionIndex(index, size);
/*  818 */       return size - index;
/*      */     }
/*      */     
/*      */     public void add(int index, @Nullable T element) {
/*  822 */       this.forwardList.add(reversePosition(index), element);
/*      */     }
/*      */     
/*      */     public void clear() {
/*  826 */       this.forwardList.clear();
/*      */     }
/*      */     
/*      */     public T remove(int index) {
/*  830 */       return this.forwardList.remove(reverseIndex(index));
/*      */     }
/*      */     
/*      */     protected void removeRange(int fromIndex, int toIndex) {
/*  834 */       subList(fromIndex, toIndex).clear();
/*      */     }
/*      */     
/*      */     public T set(int index, @Nullable T element) {
/*  838 */       return this.forwardList.set(reverseIndex(index), element);
/*      */     }
/*      */     
/*      */     public T get(int index) {
/*  842 */       return this.forwardList.get(reverseIndex(index));
/*      */     }
/*      */     
/*      */     public int size() {
/*  846 */       return this.forwardList.size();
/*      */     }
/*      */     
/*      */     public List<T> subList(int fromIndex, int toIndex) {
/*  850 */       Preconditions.checkPositionIndexes(fromIndex, toIndex, size());
/*  851 */       return Lists.reverse(this.forwardList.subList(reversePosition(toIndex), reversePosition(fromIndex)));
/*      */     }
/*      */ 
/*      */     
/*      */     public Iterator<T> iterator() {
/*  856 */       return listIterator();
/*      */     }
/*      */     
/*      */     public ListIterator<T> listIterator(int index) {
/*  860 */       int start = reversePosition(index);
/*  861 */       final ListIterator<T> forwardIterator = this.forwardList.listIterator(start);
/*  862 */       return new ListIterator<T>()
/*      */         {
/*      */           boolean canRemoveOrSet;
/*      */           
/*      */           public void add(T e) {
/*  867 */             forwardIterator.add(e);
/*  868 */             forwardIterator.previous();
/*  869 */             this.canRemoveOrSet = false;
/*      */           }
/*      */           
/*      */           public boolean hasNext() {
/*  873 */             return forwardIterator.hasPrevious();
/*      */           }
/*      */           
/*      */           public boolean hasPrevious() {
/*  877 */             return forwardIterator.hasNext();
/*      */           }
/*      */           
/*      */           public T next() {
/*  881 */             if (!hasNext()) {
/*  882 */               throw new NoSuchElementException();
/*      */             }
/*  884 */             this.canRemoveOrSet = true;
/*  885 */             return forwardIterator.previous();
/*      */           }
/*      */           
/*      */           public int nextIndex() {
/*  889 */             return Lists.ReverseList.this.reversePosition(forwardIterator.nextIndex());
/*      */           }
/*      */           
/*      */           public T previous() {
/*  893 */             if (!hasPrevious()) {
/*  894 */               throw new NoSuchElementException();
/*      */             }
/*  896 */             this.canRemoveOrSet = true;
/*  897 */             return forwardIterator.next();
/*      */           }
/*      */           
/*      */           public int previousIndex() {
/*  901 */             return nextIndex() - 1;
/*      */           }
/*      */           
/*      */           public void remove() {
/*  905 */             CollectPreconditions.checkRemove(this.canRemoveOrSet);
/*  906 */             forwardIterator.remove();
/*  907 */             this.canRemoveOrSet = false;
/*      */           }
/*      */           
/*      */           public void set(T e) {
/*  911 */             Preconditions.checkState(this.canRemoveOrSet);
/*  912 */             forwardIterator.set(e);
/*      */           }
/*      */         };
/*      */     }
/*      */   }
/*      */   
/*      */   private static class RandomAccessReverseList<T>
/*      */     extends ReverseList<T> implements RandomAccess {
/*      */     RandomAccessReverseList(List<T> forwardList) {
/*  921 */       super(forwardList);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static int hashCodeImpl(List<?> list) {
/*  930 */     int hashCode = 1;
/*  931 */     for (Object o : list) {
/*  932 */       hashCode = 31 * hashCode + ((o == null) ? 0 : o.hashCode());
/*      */       
/*  934 */       hashCode = hashCode ^ 0xFFFFFFFF ^ 0xFFFFFFFF;
/*      */     } 
/*      */     
/*  937 */     return hashCode;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static boolean equalsImpl(List<?> list, @Nullable Object object) {
/*  944 */     if (object == Preconditions.checkNotNull(list)) {
/*  945 */       return true;
/*      */     }
/*  947 */     if (!(object instanceof List)) {
/*  948 */       return false;
/*      */     }
/*      */     
/*  951 */     List<?> o = (List)object;
/*      */     
/*  953 */     return (list.size() == o.size() && Iterators.elementsEqual(list.iterator(), o.iterator()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static <E> boolean addAllImpl(List<E> list, int index, Iterable<? extends E> elements) {
/*  962 */     boolean changed = false;
/*  963 */     ListIterator<E> listIterator = list.listIterator(index);
/*  964 */     for (E e : elements) {
/*  965 */       listIterator.add(e);
/*  966 */       changed = true;
/*      */     } 
/*  968 */     return changed;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static int indexOfImpl(List<?> list, @Nullable Object element) {
/*  975 */     ListIterator<?> listIterator = list.listIterator();
/*  976 */     while (listIterator.hasNext()) {
/*  977 */       if (Objects.equal(element, listIterator.next())) {
/*  978 */         return listIterator.previousIndex();
/*      */       }
/*      */     } 
/*  981 */     return -1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static int lastIndexOfImpl(List<?> list, @Nullable Object element) {
/*  988 */     ListIterator<?> listIterator = list.listIterator(list.size());
/*  989 */     while (listIterator.hasPrevious()) {
/*  990 */       if (Objects.equal(element, listIterator.previous())) {
/*  991 */         return listIterator.nextIndex();
/*      */       }
/*      */     } 
/*  994 */     return -1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static <E> ListIterator<E> listIteratorImpl(List<E> list, int index) {
/* 1001 */     return (new AbstractListWrapper<E>(list)).listIterator(index);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static <E> List<E> subListImpl(List<E> list, int fromIndex, int toIndex) {
/*      */     List<E> wrapper;
/* 1010 */     if (list instanceof RandomAccess) {
/* 1011 */       wrapper = new RandomAccessListWrapper<E>(list) {
/*      */           public ListIterator<E> listIterator(int index) {
/* 1013 */             return this.backingList.listIterator(index);
/*      */           }
/*      */           
/*      */           private static final long serialVersionUID = 0L;
/*      */         };
/*      */     } else {
/* 1019 */       wrapper = new AbstractListWrapper<E>(list) {
/*      */           public ListIterator<E> listIterator(int index) {
/* 1021 */             return this.backingList.listIterator(index);
/*      */           }
/*      */           
/*      */           private static final long serialVersionUID = 0L;
/*      */         };
/*      */     } 
/* 1027 */     return wrapper.subList(fromIndex, toIndex);
/*      */   }
/*      */   
/*      */   private static class AbstractListWrapper<E> extends AbstractList<E> {
/*      */     final List<E> backingList;
/*      */     
/*      */     AbstractListWrapper(List<E> backingList) {
/* 1034 */       this.backingList = (List<E>)Preconditions.checkNotNull(backingList);
/*      */     }
/*      */     
/*      */     public void add(int index, E element) {
/* 1038 */       this.backingList.add(index, element);
/*      */     }
/*      */     
/*      */     public boolean addAll(int index, Collection<? extends E> c) {
/* 1042 */       return this.backingList.addAll(index, c);
/*      */     }
/*      */     
/*      */     public E get(int index) {
/* 1046 */       return this.backingList.get(index);
/*      */     }
/*      */     
/*      */     public E remove(int index) {
/* 1050 */       return this.backingList.remove(index);
/*      */     }
/*      */     
/*      */     public E set(int index, E element) {
/* 1054 */       return this.backingList.set(index, element);
/*      */     }
/*      */     
/*      */     public boolean contains(Object o) {
/* 1058 */       return this.backingList.contains(o);
/*      */     }
/*      */     
/*      */     public int size() {
/* 1062 */       return this.backingList.size();
/*      */     }
/*      */   }
/*      */   
/*      */   private static class RandomAccessListWrapper<E>
/*      */     extends AbstractListWrapper<E> implements RandomAccess {
/*      */     RandomAccessListWrapper(List<E> backingList) {
/* 1069 */       super(backingList);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static <T> List<T> cast(Iterable<T> iterable) {
/* 1077 */     return (List<T>)iterable;
/*      */   }
/*      */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\collect\Lists.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */