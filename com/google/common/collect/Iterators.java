/*      */ package com.google.common.collect;
/*      */ 
/*      */ import com.google.common.annotations.Beta;
/*      */ import com.google.common.annotations.GwtCompatible;
/*      */ import com.google.common.annotations.GwtIncompatible;
/*      */ import com.google.common.base.Function;
/*      */ import com.google.common.base.Objects;
/*      */ import com.google.common.base.Optional;
/*      */ import com.google.common.base.Preconditions;
/*      */ import com.google.common.base.Predicate;
/*      */ import com.google.common.base.Predicates;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.Comparator;
/*      */ import java.util.Enumeration;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.ListIterator;
/*      */ import java.util.NoSuchElementException;
/*      */ import java.util.PriorityQueue;
/*      */ import java.util.Queue;
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
/*      */ @GwtCompatible(emulated = true)
/*      */ public final class Iterators
/*      */ {
/*   72 */   static final UnmodifiableListIterator<Object> EMPTY_LIST_ITERATOR = new UnmodifiableListIterator()
/*      */     {
/*      */       public boolean hasNext()
/*      */       {
/*   76 */         return false;
/*      */       }
/*      */       
/*      */       public Object next() {
/*   80 */         throw new NoSuchElementException();
/*      */       }
/*      */       
/*      */       public boolean hasPrevious() {
/*   84 */         return false;
/*      */       }
/*      */       
/*      */       public Object previous() {
/*   88 */         throw new NoSuchElementException();
/*      */       }
/*      */       
/*      */       public int nextIndex() {
/*   92 */         return 0;
/*      */       }
/*      */       
/*      */       public int previousIndex() {
/*   96 */         return -1;
/*      */       }
/*      */     };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static <T> UnmodifiableIterator<T> emptyIterator() {
/*  112 */     return emptyListIterator();
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
/*      */   static <T> UnmodifiableListIterator<T> emptyListIterator() {
/*  124 */     return (UnmodifiableListIterator)EMPTY_LIST_ITERATOR;
/*      */   }
/*      */   
/*  127 */   private static final Iterator<Object> EMPTY_MODIFIABLE_ITERATOR = new Iterator()
/*      */     {
/*      */       public boolean hasNext() {
/*  130 */         return false;
/*      */       }
/*      */       
/*      */       public Object next() {
/*  134 */         throw new NoSuchElementException();
/*      */       }
/*      */       
/*      */       public void remove() {
/*  138 */         CollectPreconditions.checkRemove(false);
/*      */       }
/*      */     };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static <T> Iterator<T> emptyModifiableIterator() {
/*  151 */     return (Iterator)EMPTY_MODIFIABLE_ITERATOR;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> UnmodifiableIterator<T> unmodifiableIterator(final Iterator<T> iterator) {
/*  157 */     Preconditions.checkNotNull(iterator);
/*  158 */     if (iterator instanceof UnmodifiableIterator) {
/*  159 */       return (UnmodifiableIterator<T>)iterator;
/*      */     }
/*  161 */     return new UnmodifiableIterator<T>()
/*      */       {
/*      */         public boolean hasNext() {
/*  164 */           return iterator.hasNext();
/*      */         }
/*      */         
/*      */         public T next() {
/*  168 */           return iterator.next();
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static <T> UnmodifiableIterator<T> unmodifiableIterator(UnmodifiableIterator<T> iterator) {
/*  181 */     return (UnmodifiableIterator<T>)Preconditions.checkNotNull(iterator);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int size(Iterator<?> iterator) {
/*  190 */     int count = 0;
/*  191 */     while (iterator.hasNext()) {
/*  192 */       iterator.next();
/*  193 */       count++;
/*      */     } 
/*  195 */     return count;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean contains(Iterator<?> iterator, @Nullable Object element) {
/*  202 */     return any(iterator, Predicates.equalTo(element));
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
/*      */   public static boolean removeAll(Iterator<?> removeFrom, Collection<?> elementsToRemove) {
/*  216 */     return removeIf(removeFrom, Predicates.in(elementsToRemove));
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
/*      */   public static <T> boolean removeIf(Iterator<T> removeFrom, Predicate<? super T> predicate) {
/*  232 */     Preconditions.checkNotNull(predicate);
/*  233 */     boolean modified = false;
/*  234 */     while (removeFrom.hasNext()) {
/*  235 */       if (predicate.apply(removeFrom.next())) {
/*  236 */         removeFrom.remove();
/*  237 */         modified = true;
/*      */       } 
/*      */     } 
/*  240 */     return modified;
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
/*      */   public static boolean retainAll(Iterator<?> removeFrom, Collection<?> elementsToRetain) {
/*  254 */     return removeIf(removeFrom, Predicates.not(Predicates.in(elementsToRetain)));
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
/*      */   public static boolean elementsEqual(Iterator<?> iterator1, Iterator<?> iterator2) {
/*  269 */     while (iterator1.hasNext()) {
/*  270 */       if (!iterator2.hasNext()) {
/*  271 */         return false;
/*      */       }
/*  273 */       Object o1 = iterator1.next();
/*  274 */       Object o2 = iterator2.next();
/*  275 */       if (!Objects.equal(o1, o2)) {
/*  276 */         return false;
/*      */       }
/*      */     } 
/*  279 */     return !iterator2.hasNext();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String toString(Iterator<?> iterator) {
/*  288 */     return Collections2.STANDARD_JOINER.appendTo((new StringBuilder()).append('['), iterator).append(']').toString();
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
/*      */   public static <T> T getOnlyElement(Iterator<T> iterator) {
/*  302 */     T first = iterator.next();
/*  303 */     if (!iterator.hasNext()) {
/*  304 */       return first;
/*      */     }
/*      */     
/*  307 */     StringBuilder sb = new StringBuilder();
/*  308 */     String str = String.valueOf(String.valueOf(first)); sb.append((new StringBuilder(31 + str.length())).append("expected one element but was: <").append(str).toString());
/*  309 */     for (int i = 0; i < 4 && iterator.hasNext(); i++) {
/*  310 */       String str1 = String.valueOf(String.valueOf(iterator.next())); sb.append((new StringBuilder(2 + str1.length())).append(", ").append(str1).toString());
/*      */     } 
/*  312 */     if (iterator.hasNext()) {
/*  313 */       sb.append(", ...");
/*      */     }
/*  315 */     sb.append('>');
/*      */     
/*  317 */     throw new IllegalArgumentException(sb.toString());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public static <T> T getOnlyElement(Iterator<? extends T> iterator, @Nullable T defaultValue) {
/*  329 */     return iterator.hasNext() ? getOnlyElement((Iterator)iterator) : defaultValue;
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
/*      */   @GwtIncompatible("Array.newInstance(Class, int)")
/*      */   public static <T> T[] toArray(Iterator<? extends T> iterator, Class<T> type) {
/*  344 */     List<T> list = Lists.newArrayList(iterator);
/*  345 */     return Iterables.toArray(list, type);
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
/*      */   public static <T> boolean addAll(Collection<T> addTo, Iterator<? extends T> iterator) {
/*  358 */     Preconditions.checkNotNull(addTo);
/*  359 */     Preconditions.checkNotNull(iterator);
/*  360 */     boolean wasModified = false;
/*  361 */     while (iterator.hasNext()) {
/*  362 */       wasModified |= addTo.add(iterator.next());
/*      */     }
/*  364 */     return wasModified;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int frequency(Iterator<?> iterator, @Nullable Object element) {
/*  375 */     return size(filter(iterator, Predicates.equalTo(element)));
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
/*      */   public static <T> Iterator<T> cycle(final Iterable<T> iterable) {
/*  393 */     Preconditions.checkNotNull(iterable);
/*  394 */     return new Iterator<T>() {
/*  395 */         Iterator<T> iterator = Iterators.emptyIterator();
/*      */         
/*      */         Iterator<T> removeFrom;
/*      */         
/*      */         public boolean hasNext() {
/*  400 */           if (!this.iterator.hasNext()) {
/*  401 */             this.iterator = iterable.iterator();
/*      */           }
/*  403 */           return this.iterator.hasNext();
/*      */         }
/*      */         
/*      */         public T next() {
/*  407 */           if (!hasNext()) {
/*  408 */             throw new NoSuchElementException();
/*      */           }
/*  410 */           this.removeFrom = this.iterator;
/*  411 */           return this.iterator.next();
/*      */         }
/*      */         
/*      */         public void remove() {
/*  415 */           CollectPreconditions.checkRemove((this.removeFrom != null));
/*  416 */           this.removeFrom.remove();
/*  417 */           this.removeFrom = null;
/*      */         }
/*      */       };
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
/*      */   public static <T> Iterator<T> cycle(T... elements) {
/*  436 */     return cycle(Lists.newArrayList(elements));
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
/*      */   public static <T> Iterator<T> concat(Iterator<? extends T> a, Iterator<? extends T> b) {
/*  454 */     return concat(ImmutableList.<Iterator<? extends T>>of(a, b).iterator());
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
/*      */   public static <T> Iterator<T> concat(Iterator<? extends T> a, Iterator<? extends T> b, Iterator<? extends T> c) {
/*  473 */     return concat(ImmutableList.<Iterator<? extends T>>of(a, b, c).iterator());
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
/*      */   public static <T> Iterator<T> concat(Iterator<? extends T> a, Iterator<? extends T> b, Iterator<? extends T> c, Iterator<? extends T> d) {
/*  493 */     return concat(ImmutableList.<Iterator<? extends T>>of(a, b, c, d).iterator());
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
/*      */   public static <T> Iterator<T> concat(Iterator<? extends T>... inputs) {
/*  512 */     return concat(ImmutableList.<Iterator<? extends T>>copyOf(inputs).iterator());
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
/*      */   public static <T> Iterator<T> concat(final Iterator<? extends Iterator<? extends T>> inputs) {
/*  531 */     Preconditions.checkNotNull(inputs);
/*  532 */     return new Iterator<T>() {
/*  533 */         Iterator<? extends T> current = Iterators.emptyIterator();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         Iterator<? extends T> removeFrom;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         public boolean hasNext() {
/*      */           boolean currentHasNext;
/*  547 */           while (!(currentHasNext = ((Iterator)Preconditions.checkNotNull(this.current)).hasNext()) && inputs.hasNext()) {
/*  548 */             this.current = inputs.next();
/*      */           }
/*  550 */           return currentHasNext;
/*      */         }
/*      */         
/*      */         public T next() {
/*  554 */           if (!hasNext()) {
/*  555 */             throw new NoSuchElementException();
/*      */           }
/*  557 */           this.removeFrom = this.current;
/*  558 */           return this.current.next();
/*      */         }
/*      */         
/*      */         public void remove() {
/*  562 */           CollectPreconditions.checkRemove((this.removeFrom != null));
/*  563 */           this.removeFrom.remove();
/*  564 */           this.removeFrom = null;
/*      */         }
/*      */       };
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
/*      */   public static <T> UnmodifiableIterator<List<T>> partition(Iterator<T> iterator, int size) {
/*  586 */     return partitionImpl(iterator, size, false);
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
/*      */   public static <T> UnmodifiableIterator<List<T>> paddedPartition(Iterator<T> iterator, int size) {
/*  607 */     return partitionImpl(iterator, size, true);
/*      */   }
/*      */ 
/*      */   
/*      */   private static <T> UnmodifiableIterator<List<T>> partitionImpl(final Iterator<T> iterator, final int size, final boolean pad) {
/*  612 */     Preconditions.checkNotNull(iterator);
/*  613 */     Preconditions.checkArgument((size > 0));
/*  614 */     return new UnmodifiableIterator<List<T>>()
/*      */       {
/*      */         public boolean hasNext() {
/*  617 */           return iterator.hasNext();
/*      */         }
/*      */         
/*      */         public List<T> next() {
/*  621 */           if (!hasNext()) {
/*  622 */             throw new NoSuchElementException();
/*      */           }
/*  624 */           Object[] array = new Object[size];
/*  625 */           int count = 0;
/*  626 */           for (; count < size && iterator.hasNext(); count++) {
/*  627 */             array[count] = iterator.next();
/*      */           }
/*  629 */           for (int i = count; i < size; i++) {
/*  630 */             array[i] = null;
/*      */           }
/*      */ 
/*      */           
/*  634 */           List<T> list = Collections.unmodifiableList(Arrays.asList((T[])array));
/*      */           
/*  636 */           return (pad || count == size) ? list : list.subList(0, count);
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> UnmodifiableIterator<T> filter(final Iterator<T> unfiltered, final Predicate<? super T> predicate) {
/*  646 */     Preconditions.checkNotNull(unfiltered);
/*  647 */     Preconditions.checkNotNull(predicate);
/*  648 */     return new AbstractIterator<T>() {
/*      */         protected T computeNext() {
/*  650 */           while (unfiltered.hasNext()) {
/*  651 */             T element = unfiltered.next();
/*  652 */             if (predicate.apply(element)) {
/*  653 */               return element;
/*      */             }
/*      */           } 
/*  656 */           return endOfData();
/*      */         }
/*      */       };
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
/*      */   @GwtIncompatible("Class.isInstance")
/*      */   public static <T> UnmodifiableIterator<T> filter(Iterator<?> unfiltered, Class<T> type) {
/*  675 */     return filter((Iterator)unfiltered, Predicates.instanceOf(type));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> boolean any(Iterator<T> iterator, Predicate<? super T> predicate) {
/*  684 */     return (indexOf(iterator, predicate) != -1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> boolean all(Iterator<T> iterator, Predicate<? super T> predicate) {
/*  694 */     Preconditions.checkNotNull(predicate);
/*  695 */     while (iterator.hasNext()) {
/*  696 */       T element = iterator.next();
/*  697 */       if (!predicate.apply(element)) {
/*  698 */         return false;
/*      */       }
/*      */     } 
/*  701 */     return true;
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
/*      */   public static <T> T find(Iterator<T> iterator, Predicate<? super T> predicate) {
/*  717 */     return filter(iterator, predicate).next();
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
/*      */   @Nullable
/*      */   public static <T> T find(Iterator<? extends T> iterator, Predicate<? super T> predicate, @Nullable T defaultValue) {
/*  733 */     return getNext(filter(iterator, predicate), defaultValue);
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
/*      */   public static <T> Optional<T> tryFind(Iterator<T> iterator, Predicate<? super T> predicate) {
/*  751 */     UnmodifiableIterator<T> filteredIterator = filter(iterator, predicate);
/*  752 */     return filteredIterator.hasNext() ? Optional.of(filteredIterator.next()) : Optional.absent();
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
/*      */   public static <T> int indexOf(Iterator<T> iterator, Predicate<? super T> predicate) {
/*  775 */     Preconditions.checkNotNull(predicate, "predicate");
/*  776 */     for (int i = 0; iterator.hasNext(); i++) {
/*  777 */       T current = iterator.next();
/*  778 */       if (predicate.apply(current)) {
/*  779 */         return i;
/*      */       }
/*      */     } 
/*  782 */     return -1;
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
/*      */   public static <F, T> Iterator<T> transform(Iterator<F> fromIterator, final Function<? super F, ? extends T> function) {
/*  795 */     Preconditions.checkNotNull(function);
/*  796 */     return new TransformedIterator<F, T>(fromIterator)
/*      */       {
/*      */         T transform(F from) {
/*  799 */           return (T)function.apply(from);
/*      */         }
/*      */       };
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
/*      */   public static <T> T get(Iterator<T> iterator, int position) {
/*  815 */     checkNonnegative(position);
/*  816 */     int skipped = advance(iterator, position);
/*  817 */     if (!iterator.hasNext()) {
/*  818 */       int i = position, j = skipped; throw new IndexOutOfBoundsException((new StringBuilder(91)).append("position (").append(i).append(") must be less than the number of elements that remained (").append(j).append(")").toString());
/*      */     } 
/*      */ 
/*      */     
/*  822 */     return iterator.next();
/*      */   }
/*      */   
/*      */   static void checkNonnegative(int position) {
/*  826 */     if (position < 0) {
/*  827 */       int i = position; throw new IndexOutOfBoundsException((new StringBuilder(43)).append("position (").append(i).append(") must not be negative").toString());
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
/*      */   @Nullable
/*      */   public static <T> T get(Iterator<? extends T> iterator, int position, @Nullable T defaultValue) {
/*  849 */     checkNonnegative(position);
/*  850 */     advance(iterator, position);
/*  851 */     return getNext(iterator, defaultValue);
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
/*      */   @Nullable
/*      */   public static <T> T getNext(Iterator<? extends T> iterator, @Nullable T defaultValue) {
/*  865 */     return iterator.hasNext() ? iterator.next() : defaultValue;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> T getLast(Iterator<T> iterator) {
/*      */     while (true) {
/*  876 */       T current = iterator.next();
/*  877 */       if (!iterator.hasNext()) {
/*  878 */         return current;
/*      */       }
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
/*      */   @Nullable
/*      */   public static <T> T getLast(Iterator<? extends T> iterator, @Nullable T defaultValue) {
/*  893 */     return iterator.hasNext() ? getLast((Iterator)iterator) : defaultValue;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int advance(Iterator<?> iterator, int numberToAdvance) {
/*  904 */     Preconditions.checkNotNull(iterator);
/*  905 */     Preconditions.checkArgument((numberToAdvance >= 0), "numberToAdvance must be nonnegative");
/*      */     
/*      */     int i;
/*  908 */     for (i = 0; i < numberToAdvance && iterator.hasNext(); i++) {
/*  909 */       iterator.next();
/*      */     }
/*  911 */     return i;
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
/*      */   public static <T> Iterator<T> limit(final Iterator<T> iterator, final int limitSize) {
/*  928 */     Preconditions.checkNotNull(iterator);
/*  929 */     Preconditions.checkArgument((limitSize >= 0), "limit is negative");
/*  930 */     return new Iterator<T>()
/*      */       {
/*      */         private int count;
/*      */         
/*      */         public boolean hasNext() {
/*  935 */           return (this.count < limitSize && iterator.hasNext());
/*      */         }
/*      */ 
/*      */         
/*      */         public T next() {
/*  940 */           if (!hasNext()) {
/*  941 */             throw new NoSuchElementException();
/*      */           }
/*  943 */           this.count++;
/*  944 */           return iterator.next();
/*      */         }
/*      */ 
/*      */         
/*      */         public void remove() {
/*  949 */           iterator.remove();
/*      */         }
/*      */       };
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
/*      */   public static <T> Iterator<T> consumingIterator(final Iterator<T> iterator) {
/*  968 */     Preconditions.checkNotNull(iterator);
/*  969 */     return new UnmodifiableIterator<T>()
/*      */       {
/*      */         public boolean hasNext() {
/*  972 */           return iterator.hasNext();
/*      */         }
/*      */ 
/*      */         
/*      */         public T next() {
/*  977 */           T next = iterator.next();
/*  978 */           iterator.remove();
/*  979 */           return next;
/*      */         }
/*      */ 
/*      */         
/*      */         public String toString() {
/*  984 */           return "Iterators.consumingIterator(...)";
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   static <T> T pollNext(Iterator<T> iterator) {
/*  995 */     if (iterator.hasNext()) {
/*  996 */       T result = iterator.next();
/*  997 */       iterator.remove();
/*  998 */       return result;
/*      */     } 
/* 1000 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static void clear(Iterator<?> iterator) {
/* 1010 */     Preconditions.checkNotNull(iterator);
/* 1011 */     while (iterator.hasNext()) {
/* 1012 */       iterator.next();
/* 1013 */       iterator.remove();
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
/*      */   public static <T> UnmodifiableIterator<T> forArray(T... array) {
/* 1031 */     return forArray(array, 0, array.length, 0);
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
/*      */   static <T> UnmodifiableListIterator<T> forArray(final T[] array, final int offset, int length, int index) {
/* 1043 */     Preconditions.checkArgument((length >= 0));
/* 1044 */     int end = offset + length;
/*      */ 
/*      */     
/* 1047 */     Preconditions.checkPositionIndexes(offset, end, array.length);
/* 1048 */     Preconditions.checkPositionIndex(index, length);
/* 1049 */     if (length == 0) {
/* 1050 */       return emptyListIterator();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1058 */     return new AbstractIndexedListIterator<T>(length, index) {
/*      */         protected T get(int index) {
/* 1060 */           return (T)array[offset + index];
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> UnmodifiableIterator<T> singletonIterator(@Nullable final T value) {
/* 1073 */     return new UnmodifiableIterator<T>() {
/*      */         boolean done;
/*      */         
/*      */         public boolean hasNext() {
/* 1077 */           return !this.done;
/*      */         }
/*      */         
/*      */         public T next() {
/* 1081 */           if (this.done) {
/* 1082 */             throw new NoSuchElementException();
/*      */           }
/* 1084 */           this.done = true;
/* 1085 */           return (T)value;
/*      */         }
/*      */       };
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
/*      */   public static <T> UnmodifiableIterator<T> forEnumeration(final Enumeration<T> enumeration) {
/* 1100 */     Preconditions.checkNotNull(enumeration);
/* 1101 */     return new UnmodifiableIterator<T>()
/*      */       {
/*      */         public boolean hasNext() {
/* 1104 */           return enumeration.hasMoreElements();
/*      */         }
/*      */         
/*      */         public T next() {
/* 1108 */           return enumeration.nextElement();
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> Enumeration<T> asEnumeration(final Iterator<T> iterator) {
/* 1121 */     Preconditions.checkNotNull(iterator);
/* 1122 */     return new Enumeration<T>()
/*      */       {
/*      */         public boolean hasMoreElements() {
/* 1125 */           return iterator.hasNext();
/*      */         }
/*      */         
/*      */         public T nextElement() {
/* 1129 */           return iterator.next();
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */   
/*      */   private static class PeekingImpl<E>
/*      */     implements PeekingIterator<E>
/*      */   {
/*      */     private final Iterator<? extends E> iterator;
/*      */     
/*      */     private boolean hasPeeked;
/*      */     private E peekedElement;
/*      */     
/*      */     public PeekingImpl(Iterator<? extends E> iterator) {
/* 1144 */       this.iterator = (Iterator<? extends E>)Preconditions.checkNotNull(iterator);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean hasNext() {
/* 1149 */       return (this.hasPeeked || this.iterator.hasNext());
/*      */     }
/*      */ 
/*      */     
/*      */     public E next() {
/* 1154 */       if (!this.hasPeeked) {
/* 1155 */         return this.iterator.next();
/*      */       }
/* 1157 */       E result = this.peekedElement;
/* 1158 */       this.hasPeeked = false;
/* 1159 */       this.peekedElement = null;
/* 1160 */       return result;
/*      */     }
/*      */ 
/*      */     
/*      */     public void remove() {
/* 1165 */       Preconditions.checkState(!this.hasPeeked, "Can't remove after you've peeked at next");
/* 1166 */       this.iterator.remove();
/*      */     }
/*      */ 
/*      */     
/*      */     public E peek() {
/* 1171 */       if (!this.hasPeeked) {
/* 1172 */         this.peekedElement = this.iterator.next();
/* 1173 */         this.hasPeeked = true;
/*      */       } 
/* 1175 */       return this.peekedElement;
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
/*      */   public static <T> PeekingIterator<T> peekingIterator(Iterator<? extends T> iterator) {
/* 1219 */     if (iterator instanceof PeekingImpl) {
/*      */ 
/*      */ 
/*      */       
/* 1223 */       PeekingImpl<T> peeking = (PeekingImpl)iterator;
/* 1224 */       return peeking;
/*      */     } 
/* 1226 */     return new PeekingImpl<T>(iterator);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static <T> PeekingIterator<T> peekingIterator(PeekingIterator<T> iterator) {
/* 1237 */     return (PeekingIterator<T>)Preconditions.checkNotNull(iterator);
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
/*      */   @Beta
/*      */   public static <T> UnmodifiableIterator<T> mergeSorted(Iterable<? extends Iterator<? extends T>> iterators, Comparator<? super T> comparator) {
/* 1257 */     Preconditions.checkNotNull(iterators, "iterators");
/* 1258 */     Preconditions.checkNotNull(comparator, "comparator");
/*      */     
/* 1260 */     return new MergingIterator<T>(iterators, comparator);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static class MergingIterator<T>
/*      */     extends UnmodifiableIterator<T>
/*      */   {
/*      */     final Queue<PeekingIterator<T>> queue;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public MergingIterator(Iterable<? extends Iterator<? extends T>> iterators, final Comparator<? super T> itemComparator) {
/* 1279 */       Comparator<PeekingIterator<T>> heapComparator = (Comparator)new Comparator<PeekingIterator<PeekingIterator<T>>>()
/*      */         {
/*      */           public int compare(PeekingIterator<T> o1, PeekingIterator<T> o2)
/*      */           {
/* 1283 */             return itemComparator.compare(o1.peek(), o2.peek());
/*      */           }
/*      */         };
/*      */       
/* 1287 */       this.queue = new PriorityQueue<PeekingIterator<T>>(2, heapComparator);
/*      */       
/* 1289 */       for (Iterator<? extends T> iterator : iterators) {
/* 1290 */         if (iterator.hasNext()) {
/* 1291 */           this.queue.add(Iterators.peekingIterator(iterator));
/*      */         }
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean hasNext() {
/* 1298 */       return !this.queue.isEmpty();
/*      */     }
/*      */ 
/*      */     
/*      */     public T next() {
/* 1303 */       PeekingIterator<T> nextIter = this.queue.remove();
/* 1304 */       T next = nextIter.next();
/* 1305 */       if (nextIter.hasNext()) {
/* 1306 */         this.queue.add(nextIter);
/*      */       }
/* 1308 */       return next;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static <T> ListIterator<T> cast(Iterator<T> iterator) {
/* 1316 */     return (ListIterator<T>)iterator;
/*      */   }
/*      */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\collect\Iterators.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */