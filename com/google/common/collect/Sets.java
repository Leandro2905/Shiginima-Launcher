/*      */ package com.google.common.collect;
/*      */ 
/*      */ import com.google.common.annotations.GwtCompatible;
/*      */ import com.google.common.annotations.GwtIncompatible;
/*      */ import com.google.common.base.Preconditions;
/*      */ import com.google.common.base.Predicate;
/*      */ import com.google.common.base.Predicates;
/*      */ import java.io.Serializable;
/*      */ import java.util.AbstractSet;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.Comparator;
/*      */ import java.util.EnumSet;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedHashSet;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.NavigableSet;
/*      */ import java.util.NoSuchElementException;
/*      */ import java.util.Set;
/*      */ import java.util.SortedSet;
/*      */ import java.util.TreeSet;
/*      */ import java.util.concurrent.ConcurrentHashMap;
/*      */ import java.util.concurrent.CopyOnWriteArraySet;
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
/*      */ @GwtCompatible(emulated = true)
/*      */ public final class Sets
/*      */ {
/*      */   static abstract class ImprovedAbstractSet<E>
/*      */     extends AbstractSet<E>
/*      */   {
/*      */     public boolean removeAll(Collection<?> c) {
/*   74 */       return Sets.removeAllImpl(this, c);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean retainAll(Collection<?> c) {
/*   79 */       return super.retainAll((Collection)Preconditions.checkNotNull(c));
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
/*      */   @GwtCompatible(serializable = true)
/*      */   public static <E extends Enum<E>> ImmutableSet<E> immutableEnumSet(E anElement, E... otherElements) {
/*   98 */     return ImmutableEnumSet.asImmutable(EnumSet.of(anElement, otherElements));
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
/*      */   @GwtCompatible(serializable = true)
/*      */   public static <E extends Enum<E>> ImmutableSet<E> immutableEnumSet(Iterable<E> elements) {
/*  116 */     if (elements instanceof ImmutableEnumSet)
/*  117 */       return (ImmutableEnumSet)elements; 
/*  118 */     if (elements instanceof Collection) {
/*  119 */       Collection<E> collection = (Collection<E>)elements;
/*  120 */       if (collection.isEmpty()) {
/*  121 */         return ImmutableSet.of();
/*      */       }
/*  123 */       return ImmutableEnumSet.asImmutable(EnumSet.copyOf(collection));
/*      */     } 
/*      */     
/*  126 */     Iterator<E> itr = elements.iterator();
/*  127 */     if (itr.hasNext()) {
/*  128 */       EnumSet<E> enumSet = EnumSet.of(itr.next());
/*  129 */       Iterators.addAll(enumSet, itr);
/*  130 */       return ImmutableEnumSet.asImmutable(enumSet);
/*      */     } 
/*  132 */     return ImmutableSet.of();
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
/*      */   public static <E extends Enum<E>> EnumSet<E> newEnumSet(Iterable<E> iterable, Class<E> elementType) {
/*  145 */     EnumSet<E> set = EnumSet.noneOf(elementType);
/*  146 */     Iterables.addAll(set, iterable);
/*  147 */     return set;
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
/*      */   public static <E> HashSet<E> newHashSet() {
/*  164 */     return new HashSet<E>();
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
/*      */   public static <E> HashSet<E> newHashSet(E... elements) {
/*  182 */     HashSet<E> set = newHashSetWithExpectedSize(elements.length);
/*  183 */     Collections.addAll(set, elements);
/*  184 */     return set;
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
/*      */   public static <E> HashSet<E> newHashSetWithExpectedSize(int expectedSize) {
/*  201 */     return new HashSet<E>(Maps.capacity(expectedSize));
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
/*      */   public static <E> HashSet<E> newHashSet(Iterable<? extends E> elements) {
/*  218 */     return (elements instanceof Collection) ? new HashSet<E>(Collections2.cast(elements)) : newHashSet(elements.iterator());
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
/*      */   public static <E> HashSet<E> newHashSet(Iterator<? extends E> elements) {
/*  237 */     HashSet<E> set = newHashSet();
/*  238 */     Iterators.addAll(set, elements);
/*  239 */     return set;
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
/*      */   public static <E> Set<E> newConcurrentHashSet() {
/*  254 */     return newSetFromMap(new ConcurrentHashMap<E, Boolean>());
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
/*      */   public static <E> Set<E> newConcurrentHashSet(Iterable<? extends E> elements) {
/*  273 */     Set<E> set = newConcurrentHashSet();
/*  274 */     Iterables.addAll(set, elements);
/*  275 */     return set;
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
/*      */   public static <E> LinkedHashSet<E> newLinkedHashSet() {
/*  289 */     return new LinkedHashSet<E>();
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
/*      */   public static <E> LinkedHashSet<E> newLinkedHashSetWithExpectedSize(int expectedSize) {
/*  308 */     return new LinkedHashSet<E>(Maps.capacity(expectedSize));
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
/*      */   public static <E> LinkedHashSet<E> newLinkedHashSet(Iterable<? extends E> elements) {
/*  324 */     if (elements instanceof Collection) {
/*  325 */       return new LinkedHashSet<E>(Collections2.cast(elements));
/*      */     }
/*  327 */     LinkedHashSet<E> set = newLinkedHashSet();
/*  328 */     Iterables.addAll(set, elements);
/*  329 */     return set;
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
/*      */   public static <E extends Comparable> TreeSet<E> newTreeSet() {
/*  344 */     return new TreeSet<E>();
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
/*      */   public static <E extends Comparable> TreeSet<E> newTreeSet(Iterable<? extends E> elements) {
/*  364 */     TreeSet<E> set = newTreeSet();
/*  365 */     Iterables.addAll(set, elements);
/*  366 */     return set;
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
/*      */   public static <E> TreeSet<E> newTreeSet(Comparator<? super E> comparator) {
/*  381 */     return new TreeSet<E>((Comparator<? super E>)Preconditions.checkNotNull(comparator));
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
/*      */   public static <E> Set<E> newIdentityHashSet() {
/*  395 */     return newSetFromMap(Maps.newIdentityHashMap());
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
/*      */   @GwtIncompatible("CopyOnWriteArraySet")
/*      */   public static <E> CopyOnWriteArraySet<E> newCopyOnWriteArraySet() {
/*  409 */     return new CopyOnWriteArraySet<E>();
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
/*      */   @GwtIncompatible("CopyOnWriteArraySet")
/*      */   public static <E> CopyOnWriteArraySet<E> newCopyOnWriteArraySet(Iterable<? extends E> elements) {
/*  424 */     Collection<? extends E> elementsCollection = (elements instanceof Collection) ? Collections2.<E>cast(elements) : Lists.<E>newArrayList(elements);
/*      */ 
/*      */     
/*  427 */     return new CopyOnWriteArraySet<E>(elementsCollection);
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
/*      */   public static <E extends Enum<E>> EnumSet<E> complementOf(Collection<E> collection) {
/*  447 */     if (collection instanceof EnumSet) {
/*  448 */       return EnumSet.complementOf((EnumSet<E>)collection);
/*      */     }
/*  450 */     Preconditions.checkArgument(!collection.isEmpty(), "collection is empty; use the other version of this method");
/*      */     
/*  452 */     Class<E> type = ((Enum<E>)collection.iterator().next()).getDeclaringClass();
/*  453 */     return makeComplementByHand(collection, type);
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
/*      */   public static <E extends Enum<E>> EnumSet<E> complementOf(Collection<E> collection, Class<E> type) {
/*  470 */     Preconditions.checkNotNull(collection);
/*  471 */     return (collection instanceof EnumSet) ? EnumSet.<E>complementOf((EnumSet<E>)collection) : makeComplementByHand(collection, type);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static <E extends Enum<E>> EnumSet<E> makeComplementByHand(Collection<E> collection, Class<E> type) {
/*  478 */     EnumSet<E> result = EnumSet.allOf(type);
/*  479 */     result.removeAll(collection);
/*  480 */     return result;
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
/*      */   public static <E> Set<E> newSetFromMap(Map<E, Boolean> map) {
/*  515 */     return Platform.newSetFromMap(map);
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
/*      */   public static abstract class SetView<E>
/*      */     extends AbstractSet<E>
/*      */   {
/*      */     private SetView() {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public ImmutableSet<E> immutableCopy() {
/*  541 */       return ImmutableSet.copyOf(this);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public <S extends Set<E>> S copyInto(S set) {
/*  554 */       set.addAll(this);
/*  555 */       return set;
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
/*      */   public static <E> SetView<E> union(final Set<? extends E> set1, final Set<? extends E> set2) {
/*  581 */     Preconditions.checkNotNull(set1, "set1");
/*  582 */     Preconditions.checkNotNull(set2, "set2");
/*      */     
/*  584 */     final Set<? extends E> set2minus1 = difference(set2, set1);
/*      */     
/*  586 */     return new SetView<E>() {
/*      */         public int size() {
/*  588 */           return set1.size() + set2minus1.size();
/*      */         }
/*      */         public boolean isEmpty() {
/*  591 */           return (set1.isEmpty() && set2.isEmpty());
/*      */         }
/*      */         public Iterator<E> iterator() {
/*  594 */           return Iterators.unmodifiableIterator(Iterators.concat(set1.iterator(), set2minus1.iterator()));
/*      */         }
/*      */         
/*      */         public boolean contains(Object object) {
/*  598 */           return (set1.contains(object) || set2.contains(object));
/*      */         }
/*      */         public <S extends Set<E>> S copyInto(S set) {
/*  601 */           set.addAll(set1);
/*  602 */           set.addAll(set2);
/*  603 */           return set;
/*      */         }
/*      */         public ImmutableSet<E> immutableCopy() {
/*  606 */           return (new ImmutableSet.Builder<E>()).addAll(set1).addAll(set2).build();
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <E> SetView<E> intersection(final Set<E> set1, final Set<?> set2) {
/*  640 */     Preconditions.checkNotNull(set1, "set1");
/*  641 */     Preconditions.checkNotNull(set2, "set2");
/*      */     
/*  643 */     final Predicate<Object> inSet2 = Predicates.in(set2);
/*  644 */     return new SetView<E>() {
/*      */         public Iterator<E> iterator() {
/*  646 */           return Iterators.filter(set1.iterator(), inSet2);
/*      */         }
/*      */         public int size() {
/*  649 */           return Iterators.size(iterator());
/*      */         }
/*      */         public boolean isEmpty() {
/*  652 */           return !iterator().hasNext();
/*      */         }
/*      */         public boolean contains(Object object) {
/*  655 */           return (set1.contains(object) && set2.contains(object));
/*      */         }
/*      */         public boolean containsAll(Collection<?> collection) {
/*  658 */           return (set1.containsAll(collection) && set2.containsAll(collection));
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
/*      */   public static <E> SetView<E> difference(final Set<E> set1, final Set<?> set2) {
/*  677 */     Preconditions.checkNotNull(set1, "set1");
/*  678 */     Preconditions.checkNotNull(set2, "set2");
/*      */     
/*  680 */     final Predicate<Object> notInSet2 = Predicates.not(Predicates.in(set2));
/*  681 */     return new SetView<E>() {
/*      */         public Iterator<E> iterator() {
/*  683 */           return Iterators.filter(set1.iterator(), notInSet2);
/*      */         }
/*      */         public int size() {
/*  686 */           return Iterators.size(iterator());
/*      */         }
/*      */         public boolean isEmpty() {
/*  689 */           return set2.containsAll(set1);
/*      */         }
/*      */         public boolean contains(Object element) {
/*  692 */           return (set1.contains(element) && !set2.contains(element));
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
/*      */   public static <E> SetView<E> symmetricDifference(Set<? extends E> set1, Set<? extends E> set2) {
/*  711 */     Preconditions.checkNotNull(set1, "set1");
/*  712 */     Preconditions.checkNotNull(set2, "set2");
/*      */ 
/*      */     
/*  715 */     return difference(union(set1, set2), intersection(set1, set2));
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
/*      */   public static <E> Set<E> filter(Set<E> unfiltered, Predicate<? super E> predicate) {
/*  747 */     if (unfiltered instanceof SortedSet) {
/*  748 */       return filter((SortedSet<E>)unfiltered, predicate);
/*      */     }
/*  750 */     if (unfiltered instanceof FilteredSet) {
/*      */ 
/*      */       
/*  753 */       FilteredSet<E> filtered = (FilteredSet<E>)unfiltered;
/*  754 */       Predicate<E> combinedPredicate = Predicates.and(filtered.predicate, predicate);
/*      */       
/*  756 */       return new FilteredSet<E>((Set<E>)filtered.unfiltered, combinedPredicate);
/*      */     } 
/*      */ 
/*      */     
/*  760 */     return new FilteredSet<E>((Set<E>)Preconditions.checkNotNull(unfiltered), (Predicate<? super E>)Preconditions.checkNotNull(predicate));
/*      */   }
/*      */   
/*      */   private static class FilteredSet<E>
/*      */     extends Collections2.FilteredCollection<E>
/*      */     implements Set<E> {
/*      */     FilteredSet(Set<E> unfiltered, Predicate<? super E> predicate) {
/*  767 */       super(unfiltered, predicate);
/*      */     }
/*      */     
/*      */     public boolean equals(@Nullable Object object) {
/*  771 */       return Sets.equalsImpl(this, object);
/*      */     }
/*      */     
/*      */     public int hashCode() {
/*  775 */       return Sets.hashCodeImpl(this);
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
/*      */   public static <E> SortedSet<E> filter(SortedSet<E> unfiltered, Predicate<? super E> predicate) {
/*  810 */     return Platform.setsFilterSortedSet(unfiltered, predicate);
/*      */   }
/*      */ 
/*      */   
/*      */   static <E> SortedSet<E> filterSortedIgnoreNavigable(SortedSet<E> unfiltered, Predicate<? super E> predicate) {
/*  815 */     if (unfiltered instanceof FilteredSet) {
/*      */ 
/*      */       
/*  818 */       FilteredSet<E> filtered = (FilteredSet<E>)unfiltered;
/*  819 */       Predicate<E> combinedPredicate = Predicates.and(filtered.predicate, predicate);
/*      */       
/*  821 */       return new FilteredSortedSet<E>((SortedSet<E>)filtered.unfiltered, combinedPredicate);
/*      */     } 
/*      */ 
/*      */     
/*  825 */     return new FilteredSortedSet<E>((SortedSet<E>)Preconditions.checkNotNull(unfiltered), (Predicate<? super E>)Preconditions.checkNotNull(predicate));
/*      */   }
/*      */   
/*      */   private static class FilteredSortedSet<E>
/*      */     extends FilteredSet<E>
/*      */     implements SortedSet<E>
/*      */   {
/*      */     FilteredSortedSet(SortedSet<E> unfiltered, Predicate<? super E> predicate) {
/*  833 */       super(unfiltered, predicate);
/*      */     }
/*      */ 
/*      */     
/*      */     public Comparator<? super E> comparator() {
/*  838 */       return ((SortedSet<E>)this.unfiltered).comparator();
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedSet<E> subSet(E fromElement, E toElement) {
/*  843 */       return new FilteredSortedSet(((SortedSet<E>)this.unfiltered).subSet(fromElement, toElement), this.predicate);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public SortedSet<E> headSet(E toElement) {
/*  849 */       return new FilteredSortedSet(((SortedSet<E>)this.unfiltered).headSet(toElement), this.predicate);
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedSet<E> tailSet(E fromElement) {
/*  854 */       return new FilteredSortedSet(((SortedSet<E>)this.unfiltered).tailSet(fromElement), this.predicate);
/*      */     }
/*      */ 
/*      */     
/*      */     public E first() {
/*  859 */       return iterator().next();
/*      */     }
/*      */ 
/*      */     
/*      */     public E last() {
/*  864 */       SortedSet<E> sortedUnfiltered = (SortedSet<E>)this.unfiltered;
/*      */       while (true) {
/*  866 */         E element = sortedUnfiltered.last();
/*  867 */         if (this.predicate.apply(element)) {
/*  868 */           return element;
/*      */         }
/*  870 */         sortedUnfiltered = sortedUnfiltered.headSet(element);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible("NavigableSet")
/*      */   public static <E> NavigableSet<E> filter(NavigableSet<E> unfiltered, Predicate<? super E> predicate) {
/*  908 */     if (unfiltered instanceof FilteredSet) {
/*      */ 
/*      */       
/*  911 */       FilteredSet<E> filtered = (FilteredSet<E>)unfiltered;
/*  912 */       Predicate<E> combinedPredicate = Predicates.and(filtered.predicate, predicate);
/*      */       
/*  914 */       return new FilteredNavigableSet<E>((NavigableSet<E>)filtered.unfiltered, combinedPredicate);
/*      */     } 
/*      */ 
/*      */     
/*  918 */     return new FilteredNavigableSet<E>((NavigableSet<E>)Preconditions.checkNotNull(unfiltered), (Predicate<? super E>)Preconditions.checkNotNull(predicate));
/*      */   }
/*      */   
/*      */   @GwtIncompatible("NavigableSet")
/*      */   private static class FilteredNavigableSet<E>
/*      */     extends FilteredSortedSet<E>
/*      */     implements NavigableSet<E> {
/*      */     FilteredNavigableSet(NavigableSet<E> unfiltered, Predicate<? super E> predicate) {
/*  926 */       super(unfiltered, predicate);
/*      */     }
/*      */     
/*      */     NavigableSet<E> unfiltered() {
/*  930 */       return (NavigableSet<E>)this.unfiltered;
/*      */     }
/*      */ 
/*      */     
/*      */     @Nullable
/*      */     public E lower(E e) {
/*  936 */       return Iterators.getNext(headSet(e, false).descendingIterator(), null);
/*      */     }
/*      */ 
/*      */     
/*      */     @Nullable
/*      */     public E floor(E e) {
/*  942 */       return Iterators.getNext(headSet(e, true).descendingIterator(), null);
/*      */     }
/*      */ 
/*      */     
/*      */     public E ceiling(E e) {
/*  947 */       return Iterables.getFirst(tailSet(e, true), null);
/*      */     }
/*      */ 
/*      */     
/*      */     public E higher(E e) {
/*  952 */       return Iterables.getFirst(tailSet(e, false), null);
/*      */     }
/*      */ 
/*      */     
/*      */     public E pollFirst() {
/*  957 */       return Iterables.removeFirstMatching(unfiltered(), this.predicate);
/*      */     }
/*      */ 
/*      */     
/*      */     public E pollLast() {
/*  962 */       return Iterables.removeFirstMatching(unfiltered().descendingSet(), this.predicate);
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<E> descendingSet() {
/*  967 */       return Sets.filter(unfiltered().descendingSet(), this.predicate);
/*      */     }
/*      */ 
/*      */     
/*      */     public Iterator<E> descendingIterator() {
/*  972 */       return Iterators.filter(unfiltered().descendingIterator(), this.predicate);
/*      */     }
/*      */ 
/*      */     
/*      */     public E last() {
/*  977 */       return descendingIterator().next();
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public NavigableSet<E> subSet(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive) {
/*  983 */       return Sets.filter(unfiltered().subSet(fromElement, fromInclusive, toElement, toInclusive), this.predicate);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public NavigableSet<E> headSet(E toElement, boolean inclusive) {
/*  989 */       return Sets.filter(unfiltered().headSet(toElement, inclusive), this.predicate);
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<E> tailSet(E fromElement, boolean inclusive) {
/*  994 */       return Sets.filter(unfiltered().tailSet(fromElement, inclusive), this.predicate);
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
/*      */   public static <B> Set<List<B>> cartesianProduct(List<? extends Set<? extends B>> sets) {
/* 1055 */     return CartesianSet.create(sets);
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
/*      */   public static <B> Set<List<B>> cartesianProduct(Set<? extends B>... sets) {
/* 1115 */     return cartesianProduct(Arrays.asList(sets));
/*      */   }
/*      */   
/*      */   private static final class CartesianSet<E>
/*      */     extends ForwardingCollection<List<E>> implements Set<List<E>> {
/*      */     private final transient ImmutableList<ImmutableSet<E>> axes;
/*      */     private final transient CartesianList<E> delegate;
/*      */     
/*      */     static <E> Set<List<E>> create(List<? extends Set<? extends E>> sets) {
/* 1124 */       ImmutableList.Builder<ImmutableSet<E>> axesBuilder = new ImmutableList.Builder<ImmutableSet<E>>(sets.size());
/*      */       
/* 1126 */       for (Set<? extends E> set : sets) {
/* 1127 */         ImmutableSet<E> copy = ImmutableSet.copyOf(set);
/* 1128 */         if (copy.isEmpty()) {
/* 1129 */           return ImmutableSet.of();
/*      */         }
/* 1131 */         axesBuilder.add(copy);
/*      */       } 
/* 1133 */       final ImmutableList<ImmutableSet<E>> axes = axesBuilder.build();
/* 1134 */       ImmutableList<List<E>> listAxes = (ImmutableList)new ImmutableList<List<List<E>>>()
/*      */         {
/*      */           public int size()
/*      */           {
/* 1138 */             return axes.size();
/*      */           }
/*      */ 
/*      */           
/*      */           public List<E> get(int index) {
/* 1143 */             return ((ImmutableSet<E>)axes.get(index)).asList();
/*      */           }
/*      */ 
/*      */           
/*      */           boolean isPartialView() {
/* 1148 */             return true;
/*      */           }
/*      */         };
/* 1151 */       return new CartesianSet<E>(axes, new CartesianList<E>(listAxes));
/*      */     }
/*      */ 
/*      */     
/*      */     private CartesianSet(ImmutableList<ImmutableSet<E>> axes, CartesianList<E> delegate) {
/* 1156 */       this.axes = axes;
/* 1157 */       this.delegate = delegate;
/*      */     }
/*      */ 
/*      */     
/*      */     protected Collection<List<E>> delegate() {
/* 1162 */       return this.delegate;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean equals(@Nullable Object object) {
/* 1168 */       if (object instanceof CartesianSet) {
/* 1169 */         CartesianSet<?> that = (CartesianSet)object;
/* 1170 */         return this.axes.equals(that.axes);
/*      */       } 
/* 1172 */       return super.equals(object);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int hashCode() {
/* 1181 */       int adjust = size() - 1;
/* 1182 */       for (int i = 0; i < this.axes.size(); i++) {
/* 1183 */         adjust *= 31;
/* 1184 */         adjust = adjust ^ 0xFFFFFFFF ^ 0xFFFFFFFF;
/*      */       } 
/*      */       
/* 1187 */       int hash = 1;
/* 1188 */       for (Set<E> axis : this.axes) {
/* 1189 */         hash = 31 * hash + size() / axis.size() * axis.hashCode();
/*      */         
/* 1191 */         hash = hash ^ 0xFFFFFFFF ^ 0xFFFFFFFF;
/*      */       } 
/* 1193 */       hash += adjust;
/* 1194 */       return hash ^ 0xFFFFFFFF ^ 0xFFFFFFFF;
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
/*      */   @GwtCompatible(serializable = false)
/*      */   public static <E> Set<Set<E>> powerSet(Set<E> set) {
/* 1229 */     return new PowerSet<E>(set);
/*      */   }
/*      */   
/*      */   private static final class SubSet<E> extends AbstractSet<E> {
/*      */     private final ImmutableMap<E, Integer> inputSet;
/*      */     private final int mask;
/*      */     
/*      */     SubSet(ImmutableMap<E, Integer> inputSet, int mask) {
/* 1237 */       this.inputSet = inputSet;
/* 1238 */       this.mask = mask;
/*      */     }
/*      */ 
/*      */     
/*      */     public Iterator<E> iterator() {
/* 1243 */       return new UnmodifiableIterator<E>() {
/* 1244 */           final ImmutableList<E> elements = Sets.SubSet.this.inputSet.keySet().asList();
/* 1245 */           int remainingSetBits = Sets.SubSet.this.mask;
/*      */ 
/*      */           
/*      */           public boolean hasNext() {
/* 1249 */             return (this.remainingSetBits != 0);
/*      */           }
/*      */ 
/*      */           
/*      */           public E next() {
/* 1254 */             int index = Integer.numberOfTrailingZeros(this.remainingSetBits);
/* 1255 */             if (index == 32) {
/* 1256 */               throw new NoSuchElementException();
/*      */             }
/* 1258 */             this.remainingSetBits &= 1 << index ^ 0xFFFFFFFF;
/* 1259 */             return this.elements.get(index);
/*      */           }
/*      */         };
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/* 1266 */       return Integer.bitCount(this.mask);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean contains(@Nullable Object o) {
/* 1271 */       Integer index = this.inputSet.get(o);
/* 1272 */       return (index != null && (this.mask & 1 << index.intValue()) != 0);
/*      */     }
/*      */   }
/*      */   
/*      */   private static final class PowerSet<E> extends AbstractSet<Set<E>> {
/*      */     final ImmutableMap<E, Integer> inputSet;
/*      */     
/*      */     PowerSet(Set<E> input) {
/* 1280 */       ImmutableMap.Builder<E, Integer> builder = ImmutableMap.builder();
/* 1281 */       int i = 0;
/* 1282 */       for (E e : Preconditions.checkNotNull(input)) {
/* 1283 */         builder.put(e, Integer.valueOf(i++));
/*      */       }
/* 1285 */       this.inputSet = builder.build();
/* 1286 */       Preconditions.checkArgument((this.inputSet.size() <= 30), "Too many elements to create power set: %s > 30", new Object[] { Integer.valueOf(this.inputSet.size()) });
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/* 1291 */       return 1 << this.inputSet.size();
/*      */     }
/*      */     
/*      */     public boolean isEmpty() {
/* 1295 */       return false;
/*      */     }
/*      */     
/*      */     public Iterator<Set<E>> iterator() {
/* 1299 */       return (Iterator)new AbstractIndexedListIterator<Set<Set<E>>>(size()) {
/*      */           protected Set<E> get(int setBits) {
/* 1301 */             return new Sets.SubSet<E>(Sets.PowerSet.this.inputSet, setBits);
/*      */           }
/*      */         };
/*      */     }
/*      */     
/*      */     public boolean contains(@Nullable Object obj) {
/* 1307 */       if (obj instanceof Set) {
/* 1308 */         Set<?> set = (Set)obj;
/* 1309 */         return this.inputSet.keySet().containsAll(set);
/*      */       } 
/* 1311 */       return false;
/*      */     }
/*      */     
/*      */     public boolean equals(@Nullable Object obj) {
/* 1315 */       if (obj instanceof PowerSet) {
/* 1316 */         PowerSet<?> that = (PowerSet)obj;
/* 1317 */         return this.inputSet.equals(that.inputSet);
/*      */       } 
/* 1319 */       return super.equals(obj);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int hashCode() {
/* 1328 */       return this.inputSet.keySet().hashCode() << this.inputSet.size() - 1;
/*      */     }
/*      */     
/*      */     public String toString() {
/* 1332 */       String str = String.valueOf(String.valueOf(this.inputSet)); return (new StringBuilder(10 + str.length())).append("powerSet(").append(str).append(")").toString();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static int hashCodeImpl(Set<?> s) {
/* 1340 */     int hashCode = 0;
/* 1341 */     for (Object o : s) {
/* 1342 */       hashCode += (o != null) ? o.hashCode() : 0;
/*      */       
/* 1344 */       hashCode = hashCode ^ 0xFFFFFFFF ^ 0xFFFFFFFF;
/*      */     } 
/*      */     
/* 1347 */     return hashCode;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static boolean equalsImpl(Set<?> s, @Nullable Object object) {
/* 1354 */     if (s == object) {
/* 1355 */       return true;
/*      */     }
/* 1357 */     if (object instanceof Set) {
/* 1358 */       Set<?> o = (Set)object;
/*      */       
/*      */       try {
/* 1361 */         return (s.size() == o.size() && s.containsAll(o));
/* 1362 */       } catch (NullPointerException ignored) {
/* 1363 */         return false;
/* 1364 */       } catch (ClassCastException ignored) {
/* 1365 */         return false;
/*      */       } 
/*      */     } 
/* 1368 */     return false;
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
/*      */   @GwtIncompatible("NavigableSet")
/*      */   public static <E> NavigableSet<E> unmodifiableNavigableSet(NavigableSet<E> set) {
/* 1390 */     if (set instanceof ImmutableSortedSet || set instanceof UnmodifiableNavigableSet)
/*      */     {
/* 1392 */       return set;
/*      */     }
/* 1394 */     return new UnmodifiableNavigableSet<E>(set);
/*      */   }
/*      */   
/*      */   @GwtIncompatible("NavigableSet")
/*      */   static final class UnmodifiableNavigableSet<E> extends ForwardingSortedSet<E> implements NavigableSet<E>, Serializable { private final NavigableSet<E> delegate;
/*      */     private transient UnmodifiableNavigableSet<E> descendingSet;
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     UnmodifiableNavigableSet(NavigableSet<E> delegate) {
/* 1403 */       this.delegate = (NavigableSet<E>)Preconditions.checkNotNull(delegate);
/*      */     }
/*      */ 
/*      */     
/*      */     protected SortedSet<E> delegate() {
/* 1408 */       return Collections.unmodifiableSortedSet(this.delegate);
/*      */     }
/*      */ 
/*      */     
/*      */     public E lower(E e) {
/* 1413 */       return this.delegate.lower(e);
/*      */     }
/*      */ 
/*      */     
/*      */     public E floor(E e) {
/* 1418 */       return this.delegate.floor(e);
/*      */     }
/*      */ 
/*      */     
/*      */     public E ceiling(E e) {
/* 1423 */       return this.delegate.ceiling(e);
/*      */     }
/*      */ 
/*      */     
/*      */     public E higher(E e) {
/* 1428 */       return this.delegate.higher(e);
/*      */     }
/*      */ 
/*      */     
/*      */     public E pollFirst() {
/* 1433 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public E pollLast() {
/* 1438 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public NavigableSet<E> descendingSet() {
/* 1445 */       UnmodifiableNavigableSet<E> result = this.descendingSet;
/* 1446 */       if (result == null) {
/* 1447 */         result = this.descendingSet = new UnmodifiableNavigableSet(this.delegate.descendingSet());
/*      */         
/* 1449 */         result.descendingSet = this;
/*      */       } 
/* 1451 */       return result;
/*      */     }
/*      */ 
/*      */     
/*      */     public Iterator<E> descendingIterator() {
/* 1456 */       return Iterators.unmodifiableIterator(this.delegate.descendingIterator());
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public NavigableSet<E> subSet(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive) {
/* 1465 */       return Sets.unmodifiableNavigableSet(this.delegate.subSet(fromElement, fromInclusive, toElement, toInclusive));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public NavigableSet<E> headSet(E toElement, boolean inclusive) {
/* 1474 */       return Sets.unmodifiableNavigableSet(this.delegate.headSet(toElement, inclusive));
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<E> tailSet(E fromElement, boolean inclusive) {
/* 1479 */       return Sets.unmodifiableNavigableSet(this.delegate.tailSet(fromElement, inclusive));
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible("NavigableSet")
/*      */   public static <E> NavigableSet<E> synchronizedNavigableSet(NavigableSet<E> navigableSet) {
/* 1532 */     return Synchronized.navigableSet(navigableSet);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static boolean removeAllImpl(Set<?> set, Iterator<?> iterator) {
/* 1539 */     boolean changed = false;
/* 1540 */     while (iterator.hasNext()) {
/* 1541 */       changed |= set.remove(iterator.next());
/*      */     }
/* 1543 */     return changed;
/*      */   }
/*      */   
/*      */   static boolean removeAllImpl(Set<?> set, Collection<?> collection) {
/* 1547 */     Preconditions.checkNotNull(collection);
/* 1548 */     if (collection instanceof Multiset) {
/* 1549 */       collection = ((Multiset)collection).elementSet();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1558 */     if (collection instanceof Set && collection.size() > set.size()) {
/* 1559 */       return Iterators.removeAll(set.iterator(), collection);
/*      */     }
/* 1561 */     return removeAllImpl(set, collection.iterator());
/*      */   }
/*      */   
/*      */   @GwtIncompatible("NavigableSet")
/*      */   static class DescendingSet<E>
/*      */     extends ForwardingNavigableSet<E> {
/*      */     private final NavigableSet<E> forward;
/*      */     
/*      */     DescendingSet(NavigableSet<E> forward) {
/* 1570 */       this.forward = forward;
/*      */     }
/*      */ 
/*      */     
/*      */     protected NavigableSet<E> delegate() {
/* 1575 */       return this.forward;
/*      */     }
/*      */ 
/*      */     
/*      */     public E lower(E e) {
/* 1580 */       return this.forward.higher(e);
/*      */     }
/*      */ 
/*      */     
/*      */     public E floor(E e) {
/* 1585 */       return this.forward.ceiling(e);
/*      */     }
/*      */ 
/*      */     
/*      */     public E ceiling(E e) {
/* 1590 */       return this.forward.floor(e);
/*      */     }
/*      */ 
/*      */     
/*      */     public E higher(E e) {
/* 1595 */       return this.forward.lower(e);
/*      */     }
/*      */ 
/*      */     
/*      */     public E pollFirst() {
/* 1600 */       return this.forward.pollLast();
/*      */     }
/*      */ 
/*      */     
/*      */     public E pollLast() {
/* 1605 */       return this.forward.pollFirst();
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<E> descendingSet() {
/* 1610 */       return this.forward;
/*      */     }
/*      */ 
/*      */     
/*      */     public Iterator<E> descendingIterator() {
/* 1615 */       return this.forward.iterator();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public NavigableSet<E> subSet(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive) {
/* 1624 */       return this.forward.subSet(toElement, toInclusive, fromElement, fromInclusive).descendingSet();
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<E> headSet(E toElement, boolean inclusive) {
/* 1629 */       return this.forward.tailSet(toElement, inclusive).descendingSet();
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<E> tailSet(E fromElement, boolean inclusive) {
/* 1634 */       return this.forward.headSet(fromElement, inclusive).descendingSet();
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public Comparator<? super E> comparator() {
/* 1640 */       Comparator<? super E> forwardComparator = this.forward.comparator();
/* 1641 */       if (forwardComparator == null) {
/* 1642 */         return Ordering.<Comparable>natural().reverse();
/*      */       }
/* 1644 */       return reverse(forwardComparator);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     private static <T> Ordering<T> reverse(Comparator<T> forward) {
/* 1650 */       return Ordering.<T>from(forward).reverse();
/*      */     }
/*      */ 
/*      */     
/*      */     public E first() {
/* 1655 */       return this.forward.last();
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedSet<E> headSet(E toElement) {
/* 1660 */       return standardHeadSet(toElement);
/*      */     }
/*      */ 
/*      */     
/*      */     public E last() {
/* 1665 */       return this.forward.first();
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedSet<E> subSet(E fromElement, E toElement) {
/* 1670 */       return standardSubSet(fromElement, toElement);
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedSet<E> tailSet(E fromElement) {
/* 1675 */       return standardTailSet(fromElement);
/*      */     }
/*      */ 
/*      */     
/*      */     public Iterator<E> iterator() {
/* 1680 */       return this.forward.descendingIterator();
/*      */     }
/*      */ 
/*      */     
/*      */     public Object[] toArray() {
/* 1685 */       return standardToArray();
/*      */     }
/*      */ 
/*      */     
/*      */     public <T> T[] toArray(T[] array) {
/* 1690 */       return (T[])standardToArray((Object[])array);
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/* 1695 */       return standardToString();
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\collect\Sets.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */