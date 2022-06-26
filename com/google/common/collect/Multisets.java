/*      */ package com.google.common.collect;
/*      */ 
/*      */ import com.google.common.annotations.Beta;
/*      */ import com.google.common.annotations.GwtCompatible;
/*      */ import com.google.common.base.Objects;
/*      */ import com.google.common.base.Preconditions;
/*      */ import com.google.common.base.Predicate;
/*      */ import com.google.common.base.Predicates;
/*      */ import com.google.common.primitives.Ints;
/*      */ import java.io.Serializable;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.NoSuchElementException;
/*      */ import java.util.Set;
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
/*      */ @GwtCompatible
/*      */ public final class Multisets
/*      */ {
/*      */   public static <E> Multiset<E> unmodifiableMultiset(Multiset<? extends E> multiset) {
/*   74 */     if (multiset instanceof UnmodifiableMultiset || multiset instanceof ImmutableMultiset)
/*      */     {
/*      */ 
/*      */       
/*   78 */       return (Multiset)multiset;
/*      */     }
/*      */     
/*   81 */     return new UnmodifiableMultiset<E>((Multiset<? extends E>)Preconditions.checkNotNull(multiset));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static <E> Multiset<E> unmodifiableMultiset(ImmutableMultiset<E> multiset) {
/*   92 */     return (Multiset<E>)Preconditions.checkNotNull(multiset);
/*      */   }
/*      */   
/*      */   static class UnmodifiableMultiset<E> extends ForwardingMultiset<E> implements Serializable {
/*      */     final Multiset<? extends E> delegate;
/*      */     transient Set<E> elementSet;
/*      */     
/*      */     UnmodifiableMultiset(Multiset<? extends E> delegate) {
/*  100 */       this.delegate = delegate;
/*      */     }
/*      */     transient Set<Multiset.Entry<E>> entrySet;
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     protected Multiset<E> delegate() {
/*  106 */       return (Multiset)this.delegate;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     Set<E> createElementSet() {
/*  112 */       return Collections.unmodifiableSet(this.delegate.elementSet());
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<E> elementSet() {
/*  117 */       Set<E> es = this.elementSet;
/*  118 */       return (es == null) ? (this.elementSet = createElementSet()) : es;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Set<Multiset.Entry<E>> entrySet() {
/*  125 */       Set<Multiset.Entry<E>> es = this.entrySet;
/*  126 */       return (es == null) ? (this.entrySet = Collections.unmodifiableSet(this.delegate.entrySet())) : es;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Iterator<E> iterator() {
/*  136 */       return Iterators.unmodifiableIterator((Iterator)this.delegate.iterator());
/*      */     }
/*      */     
/*      */     public boolean add(E element) {
/*  140 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     public int add(E element, int occurences) {
/*  144 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     public boolean addAll(Collection<? extends E> elementsToAdd) {
/*  148 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     public boolean remove(Object element) {
/*  152 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     public int remove(Object element, int occurrences) {
/*  156 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     public boolean removeAll(Collection<?> elementsToRemove) {
/*  160 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     public boolean retainAll(Collection<?> elementsToRetain) {
/*  164 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     public void clear() {
/*  168 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     public int setCount(E element, int count) {
/*  172 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     public boolean setCount(E element, int oldCount, int newCount) {
/*  176 */       throw new UnsupportedOperationException();
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
/*      */   @Beta
/*      */   public static <E> SortedMultiset<E> unmodifiableSortedMultiset(SortedMultiset<E> sortedMultiset) {
/*  200 */     return new UnmodifiableSortedMultiset<E>((SortedMultiset<E>)Preconditions.checkNotNull(sortedMultiset));
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
/*      */   public static <E> Multiset.Entry<E> immutableEntry(@Nullable E e, int n) {
/*  212 */     return new ImmutableEntry<E>(e, n);
/*      */   }
/*      */   
/*      */   static final class ImmutableEntry<E> extends AbstractEntry<E> implements Serializable { @Nullable
/*      */     final E element;
/*      */     final int count;
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     ImmutableEntry(@Nullable E element, int count) {
/*  221 */       this.element = element;
/*  222 */       this.count = count;
/*  223 */       CollectPreconditions.checkNonnegative(count, "count");
/*      */     }
/*      */     
/*      */     @Nullable
/*      */     public E getElement() {
/*  228 */       return this.element;
/*      */     }
/*      */ 
/*      */     
/*      */     public int getCount() {
/*  233 */       return this.count;
/*      */     } }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public static <E> Multiset<E> filter(Multiset<E> unfiltered, Predicate<? super E> predicate) {
/*  267 */     if (unfiltered instanceof FilteredMultiset) {
/*      */ 
/*      */       
/*  270 */       FilteredMultiset<E> filtered = (FilteredMultiset<E>)unfiltered;
/*  271 */       Predicate<E> combinedPredicate = Predicates.and(filtered.predicate, predicate);
/*      */       
/*  273 */       return new FilteredMultiset<E>(filtered.unfiltered, combinedPredicate);
/*      */     } 
/*  275 */     return new FilteredMultiset<E>(unfiltered, predicate);
/*      */   }
/*      */   
/*      */   private static final class FilteredMultiset<E> extends AbstractMultiset<E> {
/*      */     final Multiset<E> unfiltered;
/*      */     final Predicate<? super E> predicate;
/*      */     
/*      */     FilteredMultiset(Multiset<E> unfiltered, Predicate<? super E> predicate) {
/*  283 */       this.unfiltered = (Multiset<E>)Preconditions.checkNotNull(unfiltered);
/*  284 */       this.predicate = (Predicate<? super E>)Preconditions.checkNotNull(predicate);
/*      */     }
/*      */ 
/*      */     
/*      */     public UnmodifiableIterator<E> iterator() {
/*  289 */       return Iterators.filter(this.unfiltered.iterator(), this.predicate);
/*      */     }
/*      */ 
/*      */     
/*      */     Set<E> createElementSet() {
/*  294 */       return Sets.filter(this.unfiltered.elementSet(), this.predicate);
/*      */     }
/*      */ 
/*      */     
/*      */     Set<Multiset.Entry<E>> createEntrySet() {
/*  299 */       return Sets.filter(this.unfiltered.entrySet(), new Predicate<Multiset.Entry<E>>()
/*      */           {
/*      */             public boolean apply(Multiset.Entry<E> entry) {
/*  302 */               return Multisets.FilteredMultiset.this.predicate.apply(entry.getElement());
/*      */             }
/*      */           });
/*      */     }
/*      */ 
/*      */     
/*      */     Iterator<Multiset.Entry<E>> entryIterator() {
/*  309 */       throw new AssertionError("should never be called");
/*      */     }
/*      */ 
/*      */     
/*      */     int distinctElements() {
/*  314 */       return elementSet().size();
/*      */     }
/*      */ 
/*      */     
/*      */     public int count(@Nullable Object element) {
/*  319 */       int count = this.unfiltered.count(element);
/*  320 */       if (count > 0) {
/*      */         
/*  322 */         E e = (E)element;
/*  323 */         return this.predicate.apply(e) ? count : 0;
/*      */       } 
/*  325 */       return 0;
/*      */     }
/*      */ 
/*      */     
/*      */     public int add(@Nullable E element, int occurrences) {
/*  330 */       Preconditions.checkArgument(this.predicate.apply(element), "Element %s does not match predicate %s", new Object[] { element, this.predicate });
/*      */       
/*  332 */       return this.unfiltered.add(element, occurrences);
/*      */     }
/*      */ 
/*      */     
/*      */     public int remove(@Nullable Object element, int occurrences) {
/*  337 */       CollectPreconditions.checkNonnegative(occurrences, "occurrences");
/*  338 */       if (occurrences == 0) {
/*  339 */         return count(element);
/*      */       }
/*  341 */       return contains(element) ? this.unfiltered.remove(element, occurrences) : 0;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void clear() {
/*  347 */       elementSet().clear();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static int inferDistinctElements(Iterable<?> elements) {
/*  358 */     if (elements instanceof Multiset) {
/*  359 */       return ((Multiset)elements).elementSet().size();
/*      */     }
/*  361 */     return 11;
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
/*      */   @Beta
/*      */   public static <E> Multiset<E> union(final Multiset<? extends E> multiset1, final Multiset<? extends E> multiset2) {
/*  382 */     Preconditions.checkNotNull(multiset1);
/*  383 */     Preconditions.checkNotNull(multiset2);
/*      */     
/*  385 */     return new AbstractMultiset<E>()
/*      */       {
/*      */         public boolean contains(@Nullable Object element) {
/*  388 */           return (multiset1.contains(element) || multiset2.contains(element));
/*      */         }
/*      */ 
/*      */         
/*      */         public boolean isEmpty() {
/*  393 */           return (multiset1.isEmpty() && multiset2.isEmpty());
/*      */         }
/*      */ 
/*      */         
/*      */         public int count(Object element) {
/*  398 */           return Math.max(multiset1.count(element), multiset2.count(element));
/*      */         }
/*      */ 
/*      */         
/*      */         Set<E> createElementSet() {
/*  403 */           return Sets.union(multiset1.elementSet(), multiset2.elementSet());
/*      */         }
/*      */ 
/*      */         
/*      */         Iterator<Multiset.Entry<E>> entryIterator() {
/*  408 */           final Iterator<? extends Multiset.Entry<? extends E>> iterator1 = multiset1.entrySet().iterator();
/*      */           
/*  410 */           final Iterator<? extends Multiset.Entry<? extends E>> iterator2 = multiset2.entrySet().iterator();
/*      */ 
/*      */           
/*  413 */           return new AbstractIterator()
/*      */             {
/*      */               protected Multiset.Entry<E> computeNext() {
/*  416 */                 if (iterator1.hasNext()) {
/*  417 */                   Multiset.Entry<? extends E> entry1 = iterator1.next();
/*  418 */                   E element = entry1.getElement();
/*  419 */                   int count = Math.max(entry1.getCount(), multiset2.count(element));
/*  420 */                   return Multisets.immutableEntry(element, count);
/*      */                 } 
/*  422 */                 while (iterator2.hasNext()) {
/*  423 */                   Multiset.Entry<? extends E> entry2 = iterator2.next();
/*  424 */                   E element = entry2.getElement();
/*  425 */                   if (!multiset1.contains(element)) {
/*  426 */                     return Multisets.immutableEntry(element, entry2.getCount());
/*      */                   }
/*      */                 } 
/*  429 */                 return endOfData();
/*      */               }
/*      */             };
/*      */         }
/*      */ 
/*      */         
/*      */         int distinctElements() {
/*  436 */           return elementSet().size();
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
/*      */   public static <E> Multiset<E> intersection(final Multiset<E> multiset1, final Multiset<?> multiset2) {
/*  457 */     Preconditions.checkNotNull(multiset1);
/*  458 */     Preconditions.checkNotNull(multiset2);
/*      */     
/*  460 */     return new AbstractMultiset<E>()
/*      */       {
/*      */         public int count(Object element) {
/*  463 */           int count1 = multiset1.count(element);
/*  464 */           return (count1 == 0) ? 0 : Math.min(count1, multiset2.count(element));
/*      */         }
/*      */ 
/*      */         
/*      */         Set<E> createElementSet() {
/*  469 */           return Sets.intersection(multiset1.elementSet(), multiset2.elementSet());
/*      */         }
/*      */ 
/*      */ 
/*      */         
/*      */         Iterator<Multiset.Entry<E>> entryIterator() {
/*  475 */           final Iterator<Multiset.Entry<E>> iterator1 = multiset1.entrySet().iterator();
/*      */           
/*  477 */           return new AbstractIterator()
/*      */             {
/*      */               protected Multiset.Entry<E> computeNext() {
/*  480 */                 while (iterator1.hasNext()) {
/*  481 */                   Multiset.Entry<E> entry1 = iterator1.next();
/*  482 */                   E element = entry1.getElement();
/*  483 */                   int count = Math.min(entry1.getCount(), multiset2.count(element));
/*  484 */                   if (count > 0) {
/*  485 */                     return Multisets.immutableEntry(element, count);
/*      */                   }
/*      */                 } 
/*  488 */                 return endOfData();
/*      */               }
/*      */             };
/*      */         }
/*      */ 
/*      */         
/*      */         int distinctElements() {
/*  495 */           return elementSet().size();
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
/*      */   @Beta
/*      */   public static <E> Multiset<E> sum(final Multiset<? extends E> multiset1, final Multiset<? extends E> multiset2) {
/*  518 */     Preconditions.checkNotNull(multiset1);
/*  519 */     Preconditions.checkNotNull(multiset2);
/*      */ 
/*      */     
/*  522 */     return new AbstractMultiset<E>()
/*      */       {
/*      */         public boolean contains(@Nullable Object element) {
/*  525 */           return (multiset1.contains(element) || multiset2.contains(element));
/*      */         }
/*      */ 
/*      */         
/*      */         public boolean isEmpty() {
/*  530 */           return (multiset1.isEmpty() && multiset2.isEmpty());
/*      */         }
/*      */ 
/*      */         
/*      */         public int size() {
/*  535 */           return multiset1.size() + multiset2.size();
/*      */         }
/*      */ 
/*      */         
/*      */         public int count(Object element) {
/*  540 */           return multiset1.count(element) + multiset2.count(element);
/*      */         }
/*      */ 
/*      */         
/*      */         Set<E> createElementSet() {
/*  545 */           return Sets.union(multiset1.elementSet(), multiset2.elementSet());
/*      */         }
/*      */ 
/*      */         
/*      */         Iterator<Multiset.Entry<E>> entryIterator() {
/*  550 */           final Iterator<? extends Multiset.Entry<? extends E>> iterator1 = multiset1.entrySet().iterator();
/*      */           
/*  552 */           final Iterator<? extends Multiset.Entry<? extends E>> iterator2 = multiset2.entrySet().iterator();
/*      */           
/*  554 */           return new AbstractIterator()
/*      */             {
/*      */               protected Multiset.Entry<E> computeNext() {
/*  557 */                 if (iterator1.hasNext()) {
/*  558 */                   Multiset.Entry<? extends E> entry1 = iterator1.next();
/*  559 */                   E element = entry1.getElement();
/*  560 */                   int count = entry1.getCount() + multiset2.count(element);
/*  561 */                   return Multisets.immutableEntry(element, count);
/*      */                 } 
/*  563 */                 while (iterator2.hasNext()) {
/*  564 */                   Multiset.Entry<? extends E> entry2 = iterator2.next();
/*  565 */                   E element = entry2.getElement();
/*  566 */                   if (!multiset1.contains(element)) {
/*  567 */                     return Multisets.immutableEntry(element, entry2.getCount());
/*      */                   }
/*      */                 } 
/*  570 */                 return endOfData();
/*      */               }
/*      */             };
/*      */         }
/*      */ 
/*      */         
/*      */         int distinctElements() {
/*  577 */           return elementSet().size();
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
/*      */   @Beta
/*      */   public static <E> Multiset<E> difference(final Multiset<E> multiset1, final Multiset<?> multiset2) {
/*  600 */     Preconditions.checkNotNull(multiset1);
/*  601 */     Preconditions.checkNotNull(multiset2);
/*      */ 
/*      */     
/*  604 */     return new AbstractMultiset<E>()
/*      */       {
/*      */         public int count(@Nullable Object element) {
/*  607 */           int count1 = multiset1.count(element);
/*  608 */           return (count1 == 0) ? 0 : Math.max(0, count1 - multiset2.count(element));
/*      */         }
/*      */ 
/*      */ 
/*      */         
/*      */         Iterator<Multiset.Entry<E>> entryIterator() {
/*  614 */           final Iterator<Multiset.Entry<E>> iterator1 = multiset1.entrySet().iterator();
/*  615 */           return new AbstractIterator()
/*      */             {
/*      */               protected Multiset.Entry<E> computeNext() {
/*  618 */                 while (iterator1.hasNext()) {
/*  619 */                   Multiset.Entry<E> entry1 = iterator1.next();
/*  620 */                   E element = entry1.getElement();
/*  621 */                   int count = entry1.getCount() - multiset2.count(element);
/*  622 */                   if (count > 0) {
/*  623 */                     return Multisets.immutableEntry(element, count);
/*      */                   }
/*      */                 } 
/*  626 */                 return endOfData();
/*      */               }
/*      */             };
/*      */         }
/*      */ 
/*      */         
/*      */         int distinctElements() {
/*  633 */           return Iterators.size(entryIterator());
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
/*      */   public static boolean containsOccurrences(Multiset<?> superMultiset, Multiset<?> subMultiset) {
/*  646 */     Preconditions.checkNotNull(superMultiset);
/*  647 */     Preconditions.checkNotNull(subMultiset);
/*  648 */     for (Multiset.Entry<?> entry : subMultiset.entrySet()) {
/*  649 */       int superCount = superMultiset.count(entry.getElement());
/*  650 */       if (superCount < entry.getCount()) {
/*  651 */         return false;
/*      */       }
/*      */     } 
/*  654 */     return true;
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
/*      */   public static boolean retainOccurrences(Multiset<?> multisetToModify, Multiset<?> multisetToRetain) {
/*  678 */     return retainOccurrencesImpl(multisetToModify, multisetToRetain);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static <E> boolean retainOccurrencesImpl(Multiset<E> multisetToModify, Multiset<?> occurrencesToRetain) {
/*  686 */     Preconditions.checkNotNull(multisetToModify);
/*  687 */     Preconditions.checkNotNull(occurrencesToRetain);
/*      */     
/*  689 */     Iterator<Multiset.Entry<E>> entryIterator = multisetToModify.entrySet().iterator();
/*  690 */     boolean changed = false;
/*  691 */     while (entryIterator.hasNext()) {
/*  692 */       Multiset.Entry<E> entry = entryIterator.next();
/*  693 */       int retainCount = occurrencesToRetain.count(entry.getElement());
/*  694 */       if (retainCount == 0) {
/*  695 */         entryIterator.remove();
/*  696 */         changed = true; continue;
/*  697 */       }  if (retainCount < entry.getCount()) {
/*  698 */         multisetToModify.setCount(entry.getElement(), retainCount);
/*  699 */         changed = true;
/*      */       } 
/*      */     } 
/*  702 */     return changed;
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
/*      */   public static boolean removeOccurrences(Multiset<?> multisetToModify, Iterable<?> occurrencesToRemove) {
/*  731 */     if (occurrencesToRemove instanceof Multiset) {
/*  732 */       return removeOccurrencesImpl(multisetToModify, (Multiset)occurrencesToRemove);
/*      */     }
/*      */     
/*  735 */     return removeOccurrencesImpl(multisetToModify, occurrencesToRemove);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean removeOccurrencesImpl(Multiset<?> multisetToModify, Iterable<?> occurrencesToRemove) {
/*  741 */     Preconditions.checkNotNull(multisetToModify);
/*  742 */     Preconditions.checkNotNull(occurrencesToRemove);
/*  743 */     boolean changed = false;
/*  744 */     for (Object o : occurrencesToRemove) {
/*  745 */       changed |= multisetToModify.remove(o);
/*      */     }
/*  747 */     return changed;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static <E> boolean removeOccurrencesImpl(Multiset<E> multisetToModify, Multiset<?> occurrencesToRemove) {
/*  756 */     Preconditions.checkNotNull(multisetToModify);
/*  757 */     Preconditions.checkNotNull(occurrencesToRemove);
/*      */     
/*  759 */     boolean changed = false;
/*  760 */     Iterator<Multiset.Entry<E>> entryIterator = multisetToModify.entrySet().iterator();
/*  761 */     while (entryIterator.hasNext()) {
/*  762 */       Multiset.Entry<E> entry = entryIterator.next();
/*  763 */       int removeCount = occurrencesToRemove.count(entry.getElement());
/*  764 */       if (removeCount >= entry.getCount()) {
/*  765 */         entryIterator.remove();
/*  766 */         changed = true; continue;
/*  767 */       }  if (removeCount > 0) {
/*  768 */         multisetToModify.remove(entry.getElement(), removeCount);
/*  769 */         changed = true;
/*      */       } 
/*      */     } 
/*  772 */     return changed;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static abstract class AbstractEntry<E>
/*      */     implements Multiset.Entry<E>
/*      */   {
/*      */     public boolean equals(@Nullable Object object) {
/*  785 */       if (object instanceof Multiset.Entry) {
/*  786 */         Multiset.Entry<?> that = (Multiset.Entry)object;
/*  787 */         return (getCount() == that.getCount() && Objects.equal(getElement(), that.getElement()));
/*      */       } 
/*      */       
/*  790 */       return false;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int hashCode() {
/*  798 */       E e = getElement();
/*  799 */       return ((e == null) ? 0 : e.hashCode()) ^ getCount();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String toString() {
/*  810 */       String text = String.valueOf(getElement());
/*  811 */       int n = getCount();
/*  812 */       String str1 = String.valueOf(String.valueOf(text)); int i = n; return (n == 1) ? text : (new StringBuilder(14 + str1.length())).append(str1).append(" x ").append(i).toString();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static boolean equalsImpl(Multiset<?> multiset, @Nullable Object object) {
/*  820 */     if (object == multiset) {
/*  821 */       return true;
/*      */     }
/*  823 */     if (object instanceof Multiset) {
/*  824 */       Multiset<?> that = (Multiset)object;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  831 */       if (multiset.size() != that.size() || multiset.entrySet().size() != that.entrySet().size())
/*      */       {
/*  833 */         return false;
/*      */       }
/*  835 */       for (Multiset.Entry<?> entry : that.entrySet()) {
/*  836 */         if (multiset.count(entry.getElement()) != entry.getCount()) {
/*  837 */           return false;
/*      */         }
/*      */       } 
/*  840 */       return true;
/*      */     } 
/*  842 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static <E> boolean addAllImpl(Multiset<E> self, Collection<? extends E> elements) {
/*  850 */     if (elements.isEmpty()) {
/*  851 */       return false;
/*      */     }
/*  853 */     if (elements instanceof Multiset) {
/*  854 */       Multiset<? extends E> that = cast(elements);
/*  855 */       for (Multiset.Entry<? extends E> entry : that.entrySet()) {
/*  856 */         self.add(entry.getElement(), entry.getCount());
/*      */       }
/*      */     } else {
/*  859 */       Iterators.addAll(self, elements.iterator());
/*      */     } 
/*  861 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static boolean removeAllImpl(Multiset<?> self, Collection<?> elementsToRemove) {
/*  869 */     Collection<?> collection = (elementsToRemove instanceof Multiset) ? ((Multiset)elementsToRemove).elementSet() : elementsToRemove;
/*      */ 
/*      */     
/*  872 */     return self.elementSet().removeAll(collection);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static boolean retainAllImpl(Multiset<?> self, Collection<?> elementsToRetain) {
/*  880 */     Preconditions.checkNotNull(elementsToRetain);
/*  881 */     Collection<?> collection = (elementsToRetain instanceof Multiset) ? ((Multiset)elementsToRetain).elementSet() : elementsToRetain;
/*      */ 
/*      */     
/*  884 */     return self.elementSet().retainAll(collection);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static <E> int setCountImpl(Multiset<E> self, E element, int count) {
/*  891 */     CollectPreconditions.checkNonnegative(count, "count");
/*      */     
/*  893 */     int oldCount = self.count(element);
/*      */     
/*  895 */     int delta = count - oldCount;
/*  896 */     if (delta > 0) {
/*  897 */       self.add(element, delta);
/*  898 */     } else if (delta < 0) {
/*  899 */       self.remove(element, -delta);
/*      */     } 
/*      */     
/*  902 */     return oldCount;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static <E> boolean setCountImpl(Multiset<E> self, E element, int oldCount, int newCount) {
/*  910 */     CollectPreconditions.checkNonnegative(oldCount, "oldCount");
/*  911 */     CollectPreconditions.checkNonnegative(newCount, "newCount");
/*      */     
/*  913 */     if (self.count(element) == oldCount) {
/*  914 */       self.setCount(element, newCount);
/*  915 */       return true;
/*      */     } 
/*  917 */     return false;
/*      */   }
/*      */   
/*      */   static abstract class ElementSet<E>
/*      */     extends Sets.ImprovedAbstractSet<E> {
/*      */     abstract Multiset<E> multiset();
/*      */     
/*      */     public void clear() {
/*  925 */       multiset().clear();
/*      */     }
/*      */     
/*      */     public boolean contains(Object o) {
/*  929 */       return multiset().contains(o);
/*      */     }
/*      */     
/*      */     public boolean containsAll(Collection<?> c) {
/*  933 */       return multiset().containsAll(c);
/*      */     }
/*      */     
/*      */     public boolean isEmpty() {
/*  937 */       return multiset().isEmpty();
/*      */     }
/*      */     
/*      */     public Iterator<E> iterator() {
/*  941 */       return new TransformedIterator<Multiset.Entry<E>, E>(multiset().entrySet().iterator())
/*      */         {
/*      */           E transform(Multiset.Entry<E> entry) {
/*  944 */             return entry.getElement();
/*      */           }
/*      */         };
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean remove(Object o) {
/*  951 */       int count = multiset().count(o);
/*  952 */       if (count > 0) {
/*  953 */         multiset().remove(o, count);
/*  954 */         return true;
/*      */       } 
/*  956 */       return false;
/*      */     }
/*      */     
/*      */     public int size() {
/*  960 */       return multiset().entrySet().size();
/*      */     }
/*      */   }
/*      */   
/*      */   static abstract class EntrySet<E> extends Sets.ImprovedAbstractSet<Multiset.Entry<E>> {
/*      */     abstract Multiset<E> multiset();
/*      */     
/*      */     public boolean contains(@Nullable Object o) {
/*  968 */       if (o instanceof Multiset.Entry) {
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  973 */         Multiset.Entry<?> entry = (Multiset.Entry)o;
/*  974 */         if (entry.getCount() <= 0) {
/*  975 */           return false;
/*      */         }
/*  977 */         int count = multiset().count(entry.getElement());
/*  978 */         return (count == entry.getCount());
/*      */       } 
/*      */       
/*  981 */       return false;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean remove(Object object) {
/*  987 */       if (object instanceof Multiset.Entry) {
/*  988 */         Multiset.Entry<?> entry = (Multiset.Entry)object;
/*  989 */         Object element = entry.getElement();
/*  990 */         int entryCount = entry.getCount();
/*  991 */         if (entryCount != 0) {
/*      */ 
/*      */           
/*  994 */           Multiset<Object> multiset = (Multiset)multiset();
/*  995 */           return multiset.setCount(element, entryCount, 0);
/*      */         } 
/*      */       } 
/*  998 */       return false;
/*      */     }
/*      */     
/*      */     public void clear() {
/* 1002 */       multiset().clear();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static <E> Iterator<E> iteratorImpl(Multiset<E> multiset) {
/* 1010 */     return new MultisetIteratorImpl<E>(multiset, multiset.entrySet().iterator());
/*      */   }
/*      */ 
/*      */   
/*      */   static final class MultisetIteratorImpl<E>
/*      */     implements Iterator<E>
/*      */   {
/*      */     private final Multiset<E> multiset;
/*      */     
/*      */     private final Iterator<Multiset.Entry<E>> entryIterator;
/*      */     private Multiset.Entry<E> currentEntry;
/*      */     private int laterCount;
/*      */     private int totalCount;
/*      */     private boolean canRemove;
/*      */     
/*      */     MultisetIteratorImpl(Multiset<E> multiset, Iterator<Multiset.Entry<E>> entryIterator) {
/* 1026 */       this.multiset = multiset;
/* 1027 */       this.entryIterator = entryIterator;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean hasNext() {
/* 1032 */       return (this.laterCount > 0 || this.entryIterator.hasNext());
/*      */     }
/*      */ 
/*      */     
/*      */     public E next() {
/* 1037 */       if (!hasNext()) {
/* 1038 */         throw new NoSuchElementException();
/*      */       }
/* 1040 */       if (this.laterCount == 0) {
/* 1041 */         this.currentEntry = this.entryIterator.next();
/* 1042 */         this.totalCount = this.laterCount = this.currentEntry.getCount();
/*      */       } 
/* 1044 */       this.laterCount--;
/* 1045 */       this.canRemove = true;
/* 1046 */       return this.currentEntry.getElement();
/*      */     }
/*      */ 
/*      */     
/*      */     public void remove() {
/* 1051 */       CollectPreconditions.checkRemove(this.canRemove);
/* 1052 */       if (this.totalCount == 1) {
/* 1053 */         this.entryIterator.remove();
/*      */       } else {
/* 1055 */         this.multiset.remove(this.currentEntry.getElement());
/*      */       } 
/* 1057 */       this.totalCount--;
/* 1058 */       this.canRemove = false;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static int sizeImpl(Multiset<?> multiset) {
/* 1066 */     long size = 0L;
/* 1067 */     for (Multiset.Entry<?> entry : multiset.entrySet()) {
/* 1068 */       size += entry.getCount();
/*      */     }
/* 1070 */     return Ints.saturatedCast(size);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static <T> Multiset<T> cast(Iterable<T> iterable) {
/* 1077 */     return (Multiset<T>)iterable;
/*      */   }
/*      */   
/* 1080 */   private static final Ordering<Multiset.Entry<?>> DECREASING_COUNT_ORDERING = new Ordering<Multiset.Entry<?>>()
/*      */     {
/*      */       public int compare(Multiset.Entry<?> entry1, Multiset.Entry<?> entry2) {
/* 1083 */         return Ints.compare(entry2.getCount(), entry1.getCount());
/*      */       }
/*      */     };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Beta
/*      */   public static <E> ImmutableMultiset<E> copyHighestCountFirst(Multiset<E> multiset) {
/* 1095 */     List<Multiset.Entry<E>> sortedEntries = DECREASING_COUNT_ORDERING.immutableSortedCopy(multiset.entrySet());
/*      */     
/* 1097 */     return ImmutableMultiset.copyFromEntries(sortedEntries);
/*      */   }
/*      */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\collect\Multisets.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */