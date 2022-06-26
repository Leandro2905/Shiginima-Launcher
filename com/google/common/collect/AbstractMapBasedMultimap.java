/*      */ package com.google.common.collect;
/*      */ 
/*      */ import com.google.common.annotations.GwtCompatible;
/*      */ import com.google.common.annotations.GwtIncompatible;
/*      */ import com.google.common.base.Preconditions;
/*      */ import java.io.Serializable;
/*      */ import java.util.AbstractCollection;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.Comparator;
/*      */ import java.util.ConcurrentModificationException;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.ListIterator;
/*      */ import java.util.Map;
/*      */ import java.util.NavigableMap;
/*      */ import java.util.NavigableSet;
/*      */ import java.util.RandomAccess;
/*      */ import java.util.Set;
/*      */ import java.util.SortedMap;
/*      */ import java.util.SortedSet;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */ abstract class AbstractMapBasedMultimap<K, V>
/*      */   extends AbstractMultimap<K, V>
/*      */   implements Serializable
/*      */ {
/*      */   private transient Map<K, Collection<V>> map;
/*      */   private transient int totalSize;
/*      */   private static final long serialVersionUID = 2447537837011683357L;
/*      */   
/*      */   protected AbstractMapBasedMultimap(Map<K, Collection<V>> map) {
/*  123 */     Preconditions.checkArgument(map.isEmpty());
/*  124 */     this.map = map;
/*      */   }
/*      */ 
/*      */   
/*      */   final void setMap(Map<K, Collection<V>> map) {
/*  129 */     this.map = map;
/*  130 */     this.totalSize = 0;
/*  131 */     for (Collection<V> values : map.values()) {
/*  132 */       Preconditions.checkArgument(!values.isEmpty());
/*  133 */       this.totalSize += values.size();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   Collection<V> createUnmodifiableEmptyCollection() {
/*  143 */     return unmodifiableCollectionSubclass(createCollection());
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
/*      */   Collection<V> createCollection(@Nullable K key) {
/*  169 */     return createCollection();
/*      */   }
/*      */   
/*      */   Map<K, Collection<V>> backingMap() {
/*  173 */     return this.map;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int size() {
/*  180 */     return this.totalSize;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean containsKey(@Nullable Object key) {
/*  185 */     return this.map.containsKey(key);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean put(@Nullable K key, @Nullable V value) {
/*  192 */     Collection<V> collection = this.map.get(key);
/*  193 */     if (collection == null) {
/*  194 */       collection = createCollection(key);
/*  195 */       if (collection.add(value)) {
/*  196 */         this.totalSize++;
/*  197 */         this.map.put(key, collection);
/*  198 */         return true;
/*      */       } 
/*  200 */       throw new AssertionError("New Collection violated the Collection spec");
/*      */     } 
/*  202 */     if (collection.add(value)) {
/*  203 */       this.totalSize++;
/*  204 */       return true;
/*      */     } 
/*  206 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private Collection<V> getOrCreateCollection(@Nullable K key) {
/*  211 */     Collection<V> collection = this.map.get(key);
/*  212 */     if (collection == null) {
/*  213 */       collection = createCollection(key);
/*  214 */       this.map.put(key, collection);
/*      */     } 
/*  216 */     return collection;
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
/*      */   public Collection<V> replaceValues(@Nullable K key, Iterable<? extends V> values) {
/*  228 */     Iterator<? extends V> iterator = values.iterator();
/*  229 */     if (!iterator.hasNext()) {
/*  230 */       return removeAll(key);
/*      */     }
/*      */ 
/*      */     
/*  234 */     Collection<V> collection = getOrCreateCollection(key);
/*  235 */     Collection<V> oldValues = createCollection();
/*  236 */     oldValues.addAll(collection);
/*      */     
/*  238 */     this.totalSize -= collection.size();
/*  239 */     collection.clear();
/*      */     
/*  241 */     while (iterator.hasNext()) {
/*  242 */       if (collection.add(iterator.next())) {
/*  243 */         this.totalSize++;
/*      */       }
/*      */     } 
/*      */     
/*  247 */     return unmodifiableCollectionSubclass(oldValues);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Collection<V> removeAll(@Nullable Object key) {
/*  257 */     Collection<V> collection = this.map.remove(key);
/*      */     
/*  259 */     if (collection == null) {
/*  260 */       return createUnmodifiableEmptyCollection();
/*      */     }
/*      */     
/*  263 */     Collection<V> output = createCollection();
/*  264 */     output.addAll(collection);
/*  265 */     this.totalSize -= collection.size();
/*  266 */     collection.clear();
/*      */     
/*  268 */     return unmodifiableCollectionSubclass(output);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   Collection<V> unmodifiableCollectionSubclass(Collection<V> collection) {
/*  274 */     if (collection instanceof SortedSet)
/*  275 */       return Collections.unmodifiableSortedSet((SortedSet<V>)collection); 
/*  276 */     if (collection instanceof Set)
/*  277 */       return Collections.unmodifiableSet((Set<? extends V>)collection); 
/*  278 */     if (collection instanceof List) {
/*  279 */       return Collections.unmodifiableList((List<? extends V>)collection);
/*      */     }
/*  281 */     return Collections.unmodifiableCollection(collection);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void clear() {
/*  288 */     for (Collection<V> collection : this.map.values()) {
/*  289 */       collection.clear();
/*      */     }
/*  291 */     this.map.clear();
/*  292 */     this.totalSize = 0;
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
/*      */   public Collection<V> get(@Nullable K key) {
/*  304 */     Collection<V> collection = this.map.get(key);
/*  305 */     if (collection == null) {
/*  306 */       collection = createCollection(key);
/*      */     }
/*  308 */     return wrapCollection(key, collection);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   Collection<V> wrapCollection(@Nullable K key, Collection<V> collection) {
/*  319 */     if (collection instanceof SortedSet)
/*  320 */       return new WrappedSortedSet(key, (SortedSet<V>)collection, null); 
/*  321 */     if (collection instanceof Set)
/*  322 */       return new WrappedSet(key, (Set<V>)collection); 
/*  323 */     if (collection instanceof List) {
/*  324 */       return wrapList(key, (List<V>)collection, null);
/*      */     }
/*  326 */     return new WrappedCollection(key, collection, null);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private List<V> wrapList(@Nullable K key, List<V> list, @Nullable WrappedCollection ancestor) {
/*  332 */     return (list instanceof RandomAccess) ? new RandomAccessWrappedList(key, list, ancestor) : new WrappedList(key, list, ancestor);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private class WrappedCollection
/*      */     extends AbstractCollection<V>
/*      */   {
/*      */     final K key;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     Collection<V> delegate;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     final WrappedCollection ancestor;
/*      */ 
/*      */ 
/*      */     
/*      */     final Collection<V> ancestorDelegate;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     WrappedCollection(K key, @Nullable Collection<V> delegate, WrappedCollection ancestor) {
/*  362 */       this.key = key;
/*  363 */       this.delegate = delegate;
/*  364 */       this.ancestor = ancestor;
/*  365 */       this.ancestorDelegate = (ancestor == null) ? null : ancestor.getDelegate();
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
/*      */     void refreshIfEmpty() {
/*  377 */       if (this.ancestor != null) {
/*  378 */         this.ancestor.refreshIfEmpty();
/*  379 */         if (this.ancestor.getDelegate() != this.ancestorDelegate) {
/*  380 */           throw new ConcurrentModificationException();
/*      */         }
/*  382 */       } else if (this.delegate.isEmpty()) {
/*  383 */         Collection<V> newDelegate = (Collection<V>)AbstractMapBasedMultimap.this.map.get(this.key);
/*  384 */         if (newDelegate != null) {
/*  385 */           this.delegate = newDelegate;
/*      */         }
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void removeIfEmpty() {
/*  395 */       if (this.ancestor != null) {
/*  396 */         this.ancestor.removeIfEmpty();
/*  397 */       } else if (this.delegate.isEmpty()) {
/*  398 */         AbstractMapBasedMultimap.this.map.remove(this.key);
/*      */       } 
/*      */     }
/*      */     
/*      */     K getKey() {
/*  403 */       return this.key;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void addToMap() {
/*  414 */       if (this.ancestor != null) {
/*  415 */         this.ancestor.addToMap();
/*      */       } else {
/*  417 */         AbstractMapBasedMultimap.this.map.put(this.key, this.delegate);
/*      */       } 
/*      */     }
/*      */     
/*      */     public int size() {
/*  422 */       refreshIfEmpty();
/*  423 */       return this.delegate.size();
/*      */     }
/*      */     
/*      */     public boolean equals(@Nullable Object object) {
/*  427 */       if (object == this) {
/*  428 */         return true;
/*      */       }
/*  430 */       refreshIfEmpty();
/*  431 */       return this.delegate.equals(object);
/*      */     }
/*      */     
/*      */     public int hashCode() {
/*  435 */       refreshIfEmpty();
/*  436 */       return this.delegate.hashCode();
/*      */     }
/*      */     
/*      */     public String toString() {
/*  440 */       refreshIfEmpty();
/*  441 */       return this.delegate.toString();
/*      */     }
/*      */     
/*      */     Collection<V> getDelegate() {
/*  445 */       return this.delegate;
/*      */     }
/*      */     
/*      */     public Iterator<V> iterator() {
/*  449 */       refreshIfEmpty();
/*  450 */       return new WrappedIterator();
/*      */     }
/*      */     
/*      */     class WrappedIterator
/*      */       implements Iterator<V> {
/*      */       final Iterator<V> delegateIterator;
/*  456 */       final Collection<V> originalDelegate = AbstractMapBasedMultimap.WrappedCollection.this.delegate;
/*      */       
/*      */       WrappedIterator() {
/*  459 */         this.delegateIterator = AbstractMapBasedMultimap.this.iteratorOrListIterator(AbstractMapBasedMultimap.WrappedCollection.this.delegate);
/*      */       }
/*      */       
/*      */       WrappedIterator(Iterator<V> delegateIterator) {
/*  463 */         this.delegateIterator = delegateIterator;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       void validateIterator() {
/*  471 */         AbstractMapBasedMultimap.WrappedCollection.this.refreshIfEmpty();
/*  472 */         if (AbstractMapBasedMultimap.WrappedCollection.this.delegate != this.originalDelegate) {
/*  473 */           throw new ConcurrentModificationException();
/*      */         }
/*      */       }
/*      */ 
/*      */       
/*      */       public boolean hasNext() {
/*  479 */         validateIterator();
/*  480 */         return this.delegateIterator.hasNext();
/*      */       }
/*      */ 
/*      */       
/*      */       public V next() {
/*  485 */         validateIterator();
/*  486 */         return this.delegateIterator.next();
/*      */       }
/*      */ 
/*      */       
/*      */       public void remove() {
/*  491 */         this.delegateIterator.remove();
/*  492 */         AbstractMapBasedMultimap.this.totalSize--;
/*  493 */         AbstractMapBasedMultimap.WrappedCollection.this.removeIfEmpty();
/*      */       }
/*      */       
/*      */       Iterator<V> getDelegateIterator() {
/*  497 */         validateIterator();
/*  498 */         return this.delegateIterator;
/*      */       }
/*      */     }
/*      */     
/*      */     public boolean add(V value) {
/*  503 */       refreshIfEmpty();
/*  504 */       boolean wasEmpty = this.delegate.isEmpty();
/*  505 */       boolean changed = this.delegate.add(value);
/*  506 */       if (changed) {
/*  507 */         AbstractMapBasedMultimap.this.totalSize++;
/*  508 */         if (wasEmpty) {
/*  509 */           addToMap();
/*      */         }
/*      */       } 
/*  512 */       return changed;
/*      */     }
/*      */     
/*      */     WrappedCollection getAncestor() {
/*  516 */       return this.ancestor;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean addAll(Collection<? extends V> collection) {
/*  522 */       if (collection.isEmpty()) {
/*  523 */         return false;
/*      */       }
/*  525 */       int oldSize = size();
/*  526 */       boolean changed = this.delegate.addAll(collection);
/*  527 */       if (changed) {
/*  528 */         int newSize = this.delegate.size();
/*  529 */         AbstractMapBasedMultimap.this.totalSize += newSize - oldSize;
/*  530 */         if (oldSize == 0) {
/*  531 */           addToMap();
/*      */         }
/*      */       } 
/*  534 */       return changed;
/*      */     }
/*      */     
/*      */     public boolean contains(Object o) {
/*  538 */       refreshIfEmpty();
/*  539 */       return this.delegate.contains(o);
/*      */     }
/*      */     
/*      */     public boolean containsAll(Collection<?> c) {
/*  543 */       refreshIfEmpty();
/*  544 */       return this.delegate.containsAll(c);
/*      */     }
/*      */     
/*      */     public void clear() {
/*  548 */       int oldSize = size();
/*  549 */       if (oldSize == 0) {
/*      */         return;
/*      */       }
/*  552 */       this.delegate.clear();
/*  553 */       AbstractMapBasedMultimap.this.totalSize -= oldSize;
/*  554 */       removeIfEmpty();
/*      */     }
/*      */     
/*      */     public boolean remove(Object o) {
/*  558 */       refreshIfEmpty();
/*  559 */       boolean changed = this.delegate.remove(o);
/*  560 */       if (changed) {
/*  561 */         AbstractMapBasedMultimap.this.totalSize--;
/*  562 */         removeIfEmpty();
/*      */       } 
/*  564 */       return changed;
/*      */     }
/*      */     
/*      */     public boolean removeAll(Collection<?> c) {
/*  568 */       if (c.isEmpty()) {
/*  569 */         return false;
/*      */       }
/*  571 */       int oldSize = size();
/*  572 */       boolean changed = this.delegate.removeAll(c);
/*  573 */       if (changed) {
/*  574 */         int newSize = this.delegate.size();
/*  575 */         AbstractMapBasedMultimap.this.totalSize += newSize - oldSize;
/*  576 */         removeIfEmpty();
/*      */       } 
/*  578 */       return changed;
/*      */     }
/*      */     
/*      */     public boolean retainAll(Collection<?> c) {
/*  582 */       Preconditions.checkNotNull(c);
/*  583 */       int oldSize = size();
/*  584 */       boolean changed = this.delegate.retainAll(c);
/*  585 */       if (changed) {
/*  586 */         int newSize = this.delegate.size();
/*  587 */         AbstractMapBasedMultimap.this.totalSize += newSize - oldSize;
/*  588 */         removeIfEmpty();
/*      */       } 
/*  590 */       return changed;
/*      */     }
/*      */   }
/*      */   
/*      */   private Iterator<V> iteratorOrListIterator(Collection<V> collection) {
/*  595 */     return (collection instanceof List) ? ((List<V>)collection).listIterator() : collection.iterator();
/*      */   }
/*      */   
/*      */   private class WrappedSet
/*      */     extends WrappedCollection
/*      */     implements Set<V>
/*      */   {
/*      */     WrappedSet(K key, Set<V> delegate) {
/*  603 */       super(key, delegate, null);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean removeAll(Collection<?> c) {
/*  608 */       if (c.isEmpty()) {
/*  609 */         return false;
/*      */       }
/*  611 */       int oldSize = size();
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  616 */       boolean changed = Sets.removeAllImpl((Set)this.delegate, c);
/*  617 */       if (changed) {
/*  618 */         int newSize = this.delegate.size();
/*  619 */         AbstractMapBasedMultimap.this.totalSize += newSize - oldSize;
/*  620 */         removeIfEmpty();
/*      */       } 
/*  622 */       return changed;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private class WrappedSortedSet
/*      */     extends WrappedCollection
/*      */     implements SortedSet<V>
/*      */   {
/*      */     WrappedSortedSet(K key, @Nullable SortedSet<V> delegate, AbstractMapBasedMultimap<K, V>.WrappedCollection ancestor) {
/*  633 */       super(key, delegate, ancestor);
/*      */     }
/*      */     
/*      */     SortedSet<V> getSortedSetDelegate() {
/*  637 */       return (SortedSet<V>)getDelegate();
/*      */     }
/*      */ 
/*      */     
/*      */     public Comparator<? super V> comparator() {
/*  642 */       return getSortedSetDelegate().comparator();
/*      */     }
/*      */ 
/*      */     
/*      */     public V first() {
/*  647 */       refreshIfEmpty();
/*  648 */       return getSortedSetDelegate().first();
/*      */     }
/*      */ 
/*      */     
/*      */     public V last() {
/*  653 */       refreshIfEmpty();
/*  654 */       return getSortedSetDelegate().last();
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedSet<V> headSet(V toElement) {
/*  659 */       refreshIfEmpty();
/*  660 */       return new WrappedSortedSet(getKey(), getSortedSetDelegate().headSet(toElement), (getAncestor() == null) ? this : getAncestor());
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public SortedSet<V> subSet(V fromElement, V toElement) {
/*  667 */       refreshIfEmpty();
/*  668 */       return new WrappedSortedSet(getKey(), getSortedSetDelegate().subSet(fromElement, toElement), (getAncestor() == null) ? this : getAncestor());
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public SortedSet<V> tailSet(V fromElement) {
/*  675 */       refreshIfEmpty();
/*  676 */       return new WrappedSortedSet(getKey(), getSortedSetDelegate().tailSet(fromElement), (getAncestor() == null) ? this : getAncestor());
/*      */     }
/*      */   }
/*      */   
/*      */   @GwtIncompatible("NavigableSet")
/*      */   class WrappedNavigableSet
/*      */     extends WrappedSortedSet
/*      */     implements NavigableSet<V>
/*      */   {
/*      */     WrappedNavigableSet(K key, @Nullable NavigableSet<V> delegate, AbstractMapBasedMultimap<K, V>.WrappedCollection ancestor) {
/*  686 */       super(key, delegate, ancestor);
/*      */     }
/*      */ 
/*      */     
/*      */     NavigableSet<V> getSortedSetDelegate() {
/*  691 */       return (NavigableSet<V>)super.getSortedSetDelegate();
/*      */     }
/*      */ 
/*      */     
/*      */     public V lower(V v) {
/*  696 */       return getSortedSetDelegate().lower(v);
/*      */     }
/*      */ 
/*      */     
/*      */     public V floor(V v) {
/*  701 */       return getSortedSetDelegate().floor(v);
/*      */     }
/*      */ 
/*      */     
/*      */     public V ceiling(V v) {
/*  706 */       return getSortedSetDelegate().ceiling(v);
/*      */     }
/*      */ 
/*      */     
/*      */     public V higher(V v) {
/*  711 */       return getSortedSetDelegate().higher(v);
/*      */     }
/*      */ 
/*      */     
/*      */     public V pollFirst() {
/*  716 */       return Iterators.pollNext(iterator());
/*      */     }
/*      */ 
/*      */     
/*      */     public V pollLast() {
/*  721 */       return Iterators.pollNext(descendingIterator());
/*      */     }
/*      */     
/*      */     private NavigableSet<V> wrap(NavigableSet<V> wrapped) {
/*  725 */       return new WrappedNavigableSet(this.key, wrapped, (getAncestor() == null) ? this : getAncestor());
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public NavigableSet<V> descendingSet() {
/*  731 */       return wrap(getSortedSetDelegate().descendingSet());
/*      */     }
/*      */ 
/*      */     
/*      */     public Iterator<V> descendingIterator() {
/*  736 */       return new AbstractMapBasedMultimap.WrappedCollection.WrappedIterator(this, getSortedSetDelegate().descendingIterator());
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public NavigableSet<V> subSet(V fromElement, boolean fromInclusive, V toElement, boolean toInclusive) {
/*  742 */       return wrap(getSortedSetDelegate().subSet(fromElement, fromInclusive, toElement, toInclusive));
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public NavigableSet<V> headSet(V toElement, boolean inclusive) {
/*  748 */       return wrap(getSortedSetDelegate().headSet(toElement, inclusive));
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<V> tailSet(V fromElement, boolean inclusive) {
/*  753 */       return wrap(getSortedSetDelegate().tailSet(fromElement, inclusive));
/*      */     }
/*      */   }
/*      */   
/*      */   private class WrappedList
/*      */     extends WrappedCollection
/*      */     implements List<V> {
/*      */     WrappedList(K key, @Nullable List<V> delegate, AbstractMapBasedMultimap<K, V>.WrappedCollection ancestor) {
/*  761 */       super(key, delegate, ancestor);
/*      */     }
/*      */     
/*      */     List<V> getListDelegate() {
/*  765 */       return (List<V>)getDelegate();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean addAll(int index, Collection<? extends V> c) {
/*  770 */       if (c.isEmpty()) {
/*  771 */         return false;
/*      */       }
/*  773 */       int oldSize = size();
/*  774 */       boolean changed = getListDelegate().addAll(index, c);
/*  775 */       if (changed) {
/*  776 */         int newSize = getDelegate().size();
/*  777 */         AbstractMapBasedMultimap.this.totalSize += newSize - oldSize;
/*  778 */         if (oldSize == 0) {
/*  779 */           addToMap();
/*      */         }
/*      */       } 
/*  782 */       return changed;
/*      */     }
/*      */ 
/*      */     
/*      */     public V get(int index) {
/*  787 */       refreshIfEmpty();
/*  788 */       return getListDelegate().get(index);
/*      */     }
/*      */ 
/*      */     
/*      */     public V set(int index, V element) {
/*  793 */       refreshIfEmpty();
/*  794 */       return getListDelegate().set(index, element);
/*      */     }
/*      */ 
/*      */     
/*      */     public void add(int index, V element) {
/*  799 */       refreshIfEmpty();
/*  800 */       boolean wasEmpty = getDelegate().isEmpty();
/*  801 */       getListDelegate().add(index, element);
/*  802 */       AbstractMapBasedMultimap.this.totalSize++;
/*  803 */       if (wasEmpty) {
/*  804 */         addToMap();
/*      */       }
/*      */     }
/*      */ 
/*      */     
/*      */     public V remove(int index) {
/*  810 */       refreshIfEmpty();
/*  811 */       V value = getListDelegate().remove(index);
/*  812 */       AbstractMapBasedMultimap.this.totalSize--;
/*  813 */       removeIfEmpty();
/*  814 */       return value;
/*      */     }
/*      */ 
/*      */     
/*      */     public int indexOf(Object o) {
/*  819 */       refreshIfEmpty();
/*  820 */       return getListDelegate().indexOf(o);
/*      */     }
/*      */ 
/*      */     
/*      */     public int lastIndexOf(Object o) {
/*  825 */       refreshIfEmpty();
/*  826 */       return getListDelegate().lastIndexOf(o);
/*      */     }
/*      */ 
/*      */     
/*      */     public ListIterator<V> listIterator() {
/*  831 */       refreshIfEmpty();
/*  832 */       return new WrappedListIterator();
/*      */     }
/*      */ 
/*      */     
/*      */     public ListIterator<V> listIterator(int index) {
/*  837 */       refreshIfEmpty();
/*  838 */       return new WrappedListIterator(index);
/*      */     }
/*      */ 
/*      */     
/*      */     public List<V> subList(int fromIndex, int toIndex) {
/*  843 */       refreshIfEmpty();
/*  844 */       return AbstractMapBasedMultimap.this.wrapList(getKey(), getListDelegate().subList(fromIndex, toIndex), (getAncestor() == null) ? this : getAncestor());
/*      */     }
/*      */ 
/*      */     
/*      */     private class WrappedListIterator
/*      */       extends AbstractMapBasedMultimap<K, V>.WrappedCollection.WrappedIterator
/*      */       implements ListIterator<V>
/*      */     {
/*      */       WrappedListIterator() {}
/*      */       
/*      */       public WrappedListIterator(int index) {
/*  855 */         super(AbstractMapBasedMultimap.WrappedList.this.getListDelegate().listIterator(index));
/*      */       }
/*      */       
/*      */       private ListIterator<V> getDelegateListIterator() {
/*  859 */         return (ListIterator<V>)getDelegateIterator();
/*      */       }
/*      */ 
/*      */       
/*      */       public boolean hasPrevious() {
/*  864 */         return getDelegateListIterator().hasPrevious();
/*      */       }
/*      */ 
/*      */       
/*      */       public V previous() {
/*  869 */         return getDelegateListIterator().previous();
/*      */       }
/*      */ 
/*      */       
/*      */       public int nextIndex() {
/*  874 */         return getDelegateListIterator().nextIndex();
/*      */       }
/*      */ 
/*      */       
/*      */       public int previousIndex() {
/*  879 */         return getDelegateListIterator().previousIndex();
/*      */       }
/*      */ 
/*      */       
/*      */       public void set(V value) {
/*  884 */         getDelegateListIterator().set(value);
/*      */       }
/*      */ 
/*      */       
/*      */       public void add(V value) {
/*  889 */         boolean wasEmpty = AbstractMapBasedMultimap.WrappedList.this.isEmpty();
/*  890 */         getDelegateListIterator().add(value);
/*  891 */         AbstractMapBasedMultimap.this.totalSize++;
/*  892 */         if (wasEmpty) {
/*  893 */           AbstractMapBasedMultimap.WrappedList.this.addToMap();
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private class RandomAccessWrappedList
/*      */     extends WrappedList
/*      */     implements RandomAccess
/*      */   {
/*      */     RandomAccessWrappedList(K key, @Nullable List<V> delegate, AbstractMapBasedMultimap<K, V>.WrappedCollection ancestor) {
/*  907 */       super(key, delegate, ancestor);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   Set<K> createKeySet() {
/*  915 */     return (this.map instanceof SortedMap) ? new SortedKeySet((SortedMap<K, Collection<V>>)this.map) : new KeySet(this.map);
/*      */   }
/*      */   
/*      */   private class KeySet
/*      */     extends Maps.KeySet<K, Collection<V>> {
/*      */     KeySet(Map<K, Collection<V>> subMap) {
/*  921 */       super(subMap);
/*      */     }
/*      */     
/*      */     public Iterator<K> iterator() {
/*  925 */       final Iterator<Map.Entry<K, Collection<V>>> entryIterator = map().entrySet().iterator();
/*      */       
/*  927 */       return new Iterator<K>()
/*      */         {
/*      */           Map.Entry<K, Collection<V>> entry;
/*      */           
/*      */           public boolean hasNext() {
/*  932 */             return entryIterator.hasNext();
/*      */           }
/*      */           
/*      */           public K next() {
/*  936 */             this.entry = entryIterator.next();
/*  937 */             return this.entry.getKey();
/*      */           }
/*      */           
/*      */           public void remove() {
/*  941 */             CollectPreconditions.checkRemove((this.entry != null));
/*  942 */             Collection<V> collection = this.entry.getValue();
/*  943 */             entryIterator.remove();
/*  944 */             AbstractMapBasedMultimap.this.totalSize -= collection.size();
/*  945 */             collection.clear();
/*      */           }
/*      */         };
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean remove(Object key) {
/*  953 */       int count = 0;
/*  954 */       Collection<V> collection = map().remove(key);
/*  955 */       if (collection != null) {
/*  956 */         count = collection.size();
/*  957 */         collection.clear();
/*  958 */         AbstractMapBasedMultimap.this.totalSize -= count;
/*      */       } 
/*  960 */       return (count > 0);
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/*  965 */       Iterators.clear(iterator());
/*      */     }
/*      */     
/*      */     public boolean containsAll(Collection<?> c) {
/*  969 */       return map().keySet().containsAll(c);
/*      */     }
/*      */     
/*      */     public boolean equals(@Nullable Object object) {
/*  973 */       return (this == object || map().keySet().equals(object));
/*      */     }
/*      */     
/*      */     public int hashCode() {
/*  977 */       return map().keySet().hashCode();
/*      */     }
/*      */   }
/*      */   
/*      */   private class SortedKeySet
/*      */     extends KeySet implements SortedSet<K> {
/*      */     SortedKeySet(SortedMap<K, Collection<V>> subMap) {
/*  984 */       super(subMap);
/*      */     }
/*      */     
/*      */     SortedMap<K, Collection<V>> sortedMap() {
/*  988 */       return (SortedMap<K, Collection<V>>)map();
/*      */     }
/*      */ 
/*      */     
/*      */     public Comparator<? super K> comparator() {
/*  993 */       return sortedMap().comparator();
/*      */     }
/*      */ 
/*      */     
/*      */     public K first() {
/*  998 */       return sortedMap().firstKey();
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedSet<K> headSet(K toElement) {
/* 1003 */       return new SortedKeySet(sortedMap().headMap(toElement));
/*      */     }
/*      */ 
/*      */     
/*      */     public K last() {
/* 1008 */       return sortedMap().lastKey();
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedSet<K> subSet(K fromElement, K toElement) {
/* 1013 */       return new SortedKeySet(sortedMap().subMap(fromElement, toElement));
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedSet<K> tailSet(K fromElement) {
/* 1018 */       return new SortedKeySet(sortedMap().tailMap(fromElement));
/*      */     }
/*      */   }
/*      */   
/*      */   @GwtIncompatible("NavigableSet")
/*      */   class NavigableKeySet extends SortedKeySet implements NavigableSet<K> {
/*      */     NavigableKeySet(NavigableMap<K, Collection<V>> subMap) {
/* 1025 */       super(subMap);
/*      */     }
/*      */ 
/*      */     
/*      */     NavigableMap<K, Collection<V>> sortedMap() {
/* 1030 */       return (NavigableMap<K, Collection<V>>)super.sortedMap();
/*      */     }
/*      */ 
/*      */     
/*      */     public K lower(K k) {
/* 1035 */       return sortedMap().lowerKey(k);
/*      */     }
/*      */ 
/*      */     
/*      */     public K floor(K k) {
/* 1040 */       return sortedMap().floorKey(k);
/*      */     }
/*      */ 
/*      */     
/*      */     public K ceiling(K k) {
/* 1045 */       return sortedMap().ceilingKey(k);
/*      */     }
/*      */ 
/*      */     
/*      */     public K higher(K k) {
/* 1050 */       return sortedMap().higherKey(k);
/*      */     }
/*      */ 
/*      */     
/*      */     public K pollFirst() {
/* 1055 */       return Iterators.pollNext(iterator());
/*      */     }
/*      */ 
/*      */     
/*      */     public K pollLast() {
/* 1060 */       return Iterators.pollNext(descendingIterator());
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<K> descendingSet() {
/* 1065 */       return new NavigableKeySet(sortedMap().descendingMap());
/*      */     }
/*      */ 
/*      */     
/*      */     public Iterator<K> descendingIterator() {
/* 1070 */       return descendingSet().iterator();
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<K> headSet(K toElement) {
/* 1075 */       return headSet(toElement, false);
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<K> headSet(K toElement, boolean inclusive) {
/* 1080 */       return new NavigableKeySet(sortedMap().headMap(toElement, inclusive));
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<K> subSet(K fromElement, K toElement) {
/* 1085 */       return subSet(fromElement, true, toElement, false);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public NavigableSet<K> subSet(K fromElement, boolean fromInclusive, K toElement, boolean toInclusive) {
/* 1091 */       return new NavigableKeySet(sortedMap().subMap(fromElement, fromInclusive, toElement, toInclusive));
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public NavigableSet<K> tailSet(K fromElement) {
/* 1097 */       return tailSet(fromElement, true);
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<K> tailSet(K fromElement, boolean inclusive) {
/* 1102 */       return new NavigableKeySet(sortedMap().tailMap(fromElement, inclusive));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int removeValuesForKey(Object key) {
/* 1111 */     Collection<V> collection = Maps.<Collection<V>>safeRemove(this.map, key);
/*      */     
/* 1113 */     int count = 0;
/* 1114 */     if (collection != null) {
/* 1115 */       count = collection.size();
/* 1116 */       collection.clear();
/* 1117 */       this.totalSize -= count;
/*      */     } 
/* 1119 */     return count;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private abstract class Itr<T>
/*      */     implements Iterator<T>
/*      */   {
/* 1129 */     final Iterator<Map.Entry<K, Collection<V>>> keyIterator = AbstractMapBasedMultimap.this.map.entrySet().iterator();
/* 1130 */     K key = null;
/* 1131 */     Collection<V> collection = null;
/* 1132 */     Iterator<V> valueIterator = Iterators.emptyModifiableIterator();
/*      */ 
/*      */     
/*      */     abstract T output(K param1K, V param1V);
/*      */ 
/*      */     
/*      */     public boolean hasNext() {
/* 1139 */       return (this.keyIterator.hasNext() || this.valueIterator.hasNext());
/*      */     }
/*      */ 
/*      */     
/*      */     public T next() {
/* 1144 */       if (!this.valueIterator.hasNext()) {
/* 1145 */         Map.Entry<K, Collection<V>> mapEntry = this.keyIterator.next();
/* 1146 */         this.key = mapEntry.getKey();
/* 1147 */         this.collection = mapEntry.getValue();
/* 1148 */         this.valueIterator = this.collection.iterator();
/*      */       } 
/* 1150 */       return output(this.key, this.valueIterator.next());
/*      */     }
/*      */ 
/*      */     
/*      */     public void remove() {
/* 1155 */       this.valueIterator.remove();
/* 1156 */       if (this.collection.isEmpty()) {
/* 1157 */         this.keyIterator.remove();
/*      */       }
/* 1159 */       AbstractMapBasedMultimap.this.totalSize--;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Collection<V> values() {
/* 1170 */     return super.values();
/*      */   }
/*      */ 
/*      */   
/*      */   Iterator<V> valueIterator() {
/* 1175 */     return new Itr<V>()
/*      */       {
/*      */         V output(K key, V value) {
/* 1178 */           return value;
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
/*      */   public Collection<Map.Entry<K, V>> entries() {
/* 1201 */     return super.entries();
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
/*      */   Iterator<Map.Entry<K, V>> entryIterator() {
/* 1214 */     return new Itr<Map.Entry<K, V>>()
/*      */       {
/*      */         Map.Entry<K, V> output(K key, V value) {
/* 1217 */           return Maps.immutableEntry(key, value);
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   Map<K, Collection<V>> createAsMap() {
/* 1226 */     return (this.map instanceof SortedMap) ? new SortedAsMap((SortedMap<K, Collection<V>>)this.map) : new AsMap(this.map);
/*      */   }
/*      */ 
/*      */   
/*      */   abstract Collection<V> createCollection();
/*      */   
/*      */   private class AsMap
/*      */     extends Maps.ImprovedAbstractMap<K, Collection<V>>
/*      */   {
/*      */     final transient Map<K, Collection<V>> submap;
/*      */     
/*      */     AsMap(Map<K, Collection<V>> submap) {
/* 1238 */       this.submap = submap;
/*      */     }
/*      */ 
/*      */     
/*      */     protected Set<Map.Entry<K, Collection<V>>> createEntrySet() {
/* 1243 */       return new AsMapEntries();
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean containsKey(Object key) {
/* 1249 */       return Maps.safeContainsKey(this.submap, key);
/*      */     }
/*      */     
/*      */     public Collection<V> get(Object key) {
/* 1253 */       Collection<V> collection = Maps.<Collection<V>>safeGet(this.submap, key);
/* 1254 */       if (collection == null) {
/* 1255 */         return null;
/*      */       }
/*      */       
/* 1258 */       K k = (K)key;
/* 1259 */       return AbstractMapBasedMultimap.this.wrapCollection(k, collection);
/*      */     }
/*      */     
/*      */     public Set<K> keySet() {
/* 1263 */       return AbstractMapBasedMultimap.this.keySet();
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/* 1268 */       return this.submap.size();
/*      */     }
/*      */     
/*      */     public Collection<V> remove(Object key) {
/* 1272 */       Collection<V> collection = this.submap.remove(key);
/* 1273 */       if (collection == null) {
/* 1274 */         return null;
/*      */       }
/*      */       
/* 1277 */       Collection<V> output = AbstractMapBasedMultimap.this.createCollection();
/* 1278 */       output.addAll(collection);
/* 1279 */       AbstractMapBasedMultimap.this.totalSize -= collection.size();
/* 1280 */       collection.clear();
/* 1281 */       return output;
/*      */     }
/*      */     
/*      */     public boolean equals(@Nullable Object object) {
/* 1285 */       return (this == object || this.submap.equals(object));
/*      */     }
/*      */     
/*      */     public int hashCode() {
/* 1289 */       return this.submap.hashCode();
/*      */     }
/*      */     
/*      */     public String toString() {
/* 1293 */       return this.submap.toString();
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/* 1298 */       if (this.submap == AbstractMapBasedMultimap.this.map) {
/* 1299 */         AbstractMapBasedMultimap.this.clear();
/*      */       } else {
/* 1301 */         Iterators.clear(new AsMapIterator());
/*      */       } 
/*      */     }
/*      */     
/*      */     Map.Entry<K, Collection<V>> wrapEntry(Map.Entry<K, Collection<V>> entry) {
/* 1306 */       K key = entry.getKey();
/* 1307 */       return Maps.immutableEntry(key, AbstractMapBasedMultimap.this.wrapCollection(key, entry.getValue()));
/*      */     }
/*      */     
/*      */     class AsMapEntries
/*      */       extends Maps.EntrySet<K, Collection<V>> {
/*      */       Map<K, Collection<V>> map() {
/* 1313 */         return AbstractMapBasedMultimap.AsMap.this;
/*      */       }
/*      */       
/*      */       public Iterator<Map.Entry<K, Collection<V>>> iterator() {
/* 1317 */         return new AbstractMapBasedMultimap.AsMap.AsMapIterator();
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*      */       public boolean contains(Object o) {
/* 1323 */         return Collections2.safeContains(AbstractMapBasedMultimap.AsMap.this.submap.entrySet(), o);
/*      */       }
/*      */       
/*      */       public boolean remove(Object o) {
/* 1327 */         if (!contains(o)) {
/* 1328 */           return false;
/*      */         }
/* 1330 */         Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
/* 1331 */         AbstractMapBasedMultimap.this.removeValuesForKey(entry.getKey());
/* 1332 */         return true;
/*      */       }
/*      */     }
/*      */     
/*      */     class AsMapIterator
/*      */       implements Iterator<Map.Entry<K, Collection<V>>> {
/* 1338 */       final Iterator<Map.Entry<K, Collection<V>>> delegateIterator = AbstractMapBasedMultimap.AsMap.this.submap.entrySet().iterator();
/*      */       
/*      */       Collection<V> collection;
/*      */ 
/*      */       
/*      */       public boolean hasNext() {
/* 1344 */         return this.delegateIterator.hasNext();
/*      */       }
/*      */ 
/*      */       
/*      */       public Map.Entry<K, Collection<V>> next() {
/* 1349 */         Map.Entry<K, Collection<V>> entry = this.delegateIterator.next();
/* 1350 */         this.collection = entry.getValue();
/* 1351 */         return AbstractMapBasedMultimap.AsMap.this.wrapEntry(entry);
/*      */       }
/*      */ 
/*      */       
/*      */       public void remove() {
/* 1356 */         this.delegateIterator.remove();
/* 1357 */         AbstractMapBasedMultimap.this.totalSize -= this.collection.size();
/* 1358 */         this.collection.clear();
/*      */       } }
/*      */   }
/*      */   
/*      */   private class SortedAsMap extends AsMap implements SortedMap<K, Collection<V>> {
/*      */     SortedSet<K> sortedKeySet;
/*      */     
/*      */     SortedAsMap(SortedMap<K, Collection<V>> submap) {
/* 1366 */       super(submap);
/*      */     }
/*      */     
/*      */     SortedMap<K, Collection<V>> sortedMap() {
/* 1370 */       return (SortedMap<K, Collection<V>>)this.submap;
/*      */     }
/*      */ 
/*      */     
/*      */     public Comparator<? super K> comparator() {
/* 1375 */       return sortedMap().comparator();
/*      */     }
/*      */ 
/*      */     
/*      */     public K firstKey() {
/* 1380 */       return sortedMap().firstKey();
/*      */     }
/*      */ 
/*      */     
/*      */     public K lastKey() {
/* 1385 */       return sortedMap().lastKey();
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedMap<K, Collection<V>> headMap(K toKey) {
/* 1390 */       return new SortedAsMap(sortedMap().headMap(toKey));
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedMap<K, Collection<V>> subMap(K fromKey, K toKey) {
/* 1395 */       return new SortedAsMap(sortedMap().subMap(fromKey, toKey));
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedMap<K, Collection<V>> tailMap(K fromKey) {
/* 1400 */       return new SortedAsMap(sortedMap().tailMap(fromKey));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public SortedSet<K> keySet() {
/* 1408 */       SortedSet<K> result = this.sortedKeySet;
/* 1409 */       return (result == null) ? (this.sortedKeySet = createKeySet()) : result;
/*      */     }
/*      */ 
/*      */     
/*      */     SortedSet<K> createKeySet() {
/* 1414 */       return new AbstractMapBasedMultimap.SortedKeySet(sortedMap());
/*      */     }
/*      */   }
/*      */   
/*      */   @GwtIncompatible("NavigableAsMap")
/*      */   class NavigableAsMap
/*      */     extends SortedAsMap implements NavigableMap<K, Collection<V>> {
/*      */     NavigableAsMap(NavigableMap<K, Collection<V>> submap) {
/* 1422 */       super(submap);
/*      */     }
/*      */ 
/*      */     
/*      */     NavigableMap<K, Collection<V>> sortedMap() {
/* 1427 */       return (NavigableMap<K, Collection<V>>)super.sortedMap();
/*      */     }
/*      */ 
/*      */     
/*      */     public Map.Entry<K, Collection<V>> lowerEntry(K key) {
/* 1432 */       Map.Entry<K, Collection<V>> entry = sortedMap().lowerEntry(key);
/* 1433 */       return (entry == null) ? null : wrapEntry(entry);
/*      */     }
/*      */ 
/*      */     
/*      */     public K lowerKey(K key) {
/* 1438 */       return sortedMap().lowerKey(key);
/*      */     }
/*      */ 
/*      */     
/*      */     public Map.Entry<K, Collection<V>> floorEntry(K key) {
/* 1443 */       Map.Entry<K, Collection<V>> entry = sortedMap().floorEntry(key);
/* 1444 */       return (entry == null) ? null : wrapEntry(entry);
/*      */     }
/*      */ 
/*      */     
/*      */     public K floorKey(K key) {
/* 1449 */       return sortedMap().floorKey(key);
/*      */     }
/*      */ 
/*      */     
/*      */     public Map.Entry<K, Collection<V>> ceilingEntry(K key) {
/* 1454 */       Map.Entry<K, Collection<V>> entry = sortedMap().ceilingEntry(key);
/* 1455 */       return (entry == null) ? null : wrapEntry(entry);
/*      */     }
/*      */ 
/*      */     
/*      */     public K ceilingKey(K key) {
/* 1460 */       return sortedMap().ceilingKey(key);
/*      */     }
/*      */ 
/*      */     
/*      */     public Map.Entry<K, Collection<V>> higherEntry(K key) {
/* 1465 */       Map.Entry<K, Collection<V>> entry = sortedMap().higherEntry(key);
/* 1466 */       return (entry == null) ? null : wrapEntry(entry);
/*      */     }
/*      */ 
/*      */     
/*      */     public K higherKey(K key) {
/* 1471 */       return sortedMap().higherKey(key);
/*      */     }
/*      */ 
/*      */     
/*      */     public Map.Entry<K, Collection<V>> firstEntry() {
/* 1476 */       Map.Entry<K, Collection<V>> entry = sortedMap().firstEntry();
/* 1477 */       return (entry == null) ? null : wrapEntry(entry);
/*      */     }
/*      */ 
/*      */     
/*      */     public Map.Entry<K, Collection<V>> lastEntry() {
/* 1482 */       Map.Entry<K, Collection<V>> entry = sortedMap().lastEntry();
/* 1483 */       return (entry == null) ? null : wrapEntry(entry);
/*      */     }
/*      */ 
/*      */     
/*      */     public Map.Entry<K, Collection<V>> pollFirstEntry() {
/* 1488 */       return pollAsMapEntry(entrySet().iterator());
/*      */     }
/*      */ 
/*      */     
/*      */     public Map.Entry<K, Collection<V>> pollLastEntry() {
/* 1493 */       return pollAsMapEntry(descendingMap().entrySet().iterator());
/*      */     }
/*      */     
/*      */     Map.Entry<K, Collection<V>> pollAsMapEntry(Iterator<Map.Entry<K, Collection<V>>> entryIterator) {
/* 1497 */       if (!entryIterator.hasNext()) {
/* 1498 */         return null;
/*      */       }
/* 1500 */       Map.Entry<K, Collection<V>> entry = entryIterator.next();
/* 1501 */       Collection<V> output = AbstractMapBasedMultimap.this.createCollection();
/* 1502 */       output.addAll(entry.getValue());
/* 1503 */       entryIterator.remove();
/* 1504 */       return Maps.immutableEntry(entry.getKey(), AbstractMapBasedMultimap.this.unmodifiableCollectionSubclass(output));
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableMap<K, Collection<V>> descendingMap() {
/* 1509 */       return new NavigableAsMap(sortedMap().descendingMap());
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<K> keySet() {
/* 1514 */       return (NavigableSet<K>)super.keySet();
/*      */     }
/*      */ 
/*      */     
/*      */     NavigableSet<K> createKeySet() {
/* 1519 */       return new AbstractMapBasedMultimap.NavigableKeySet(sortedMap());
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<K> navigableKeySet() {
/* 1524 */       return keySet();
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<K> descendingKeySet() {
/* 1529 */       return descendingMap().navigableKeySet();
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableMap<K, Collection<V>> subMap(K fromKey, K toKey) {
/* 1534 */       return subMap(fromKey, true, toKey, false);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public NavigableMap<K, Collection<V>> subMap(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive) {
/* 1540 */       return new NavigableAsMap(sortedMap().subMap(fromKey, fromInclusive, toKey, toInclusive));
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableMap<K, Collection<V>> headMap(K toKey) {
/* 1545 */       return headMap(toKey, false);
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableMap<K, Collection<V>> headMap(K toKey, boolean inclusive) {
/* 1550 */       return new NavigableAsMap(sortedMap().headMap(toKey, inclusive));
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableMap<K, Collection<V>> tailMap(K fromKey) {
/* 1555 */       return tailMap(fromKey, true);
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableMap<K, Collection<V>> tailMap(K fromKey, boolean inclusive) {
/* 1560 */       return new NavigableAsMap(sortedMap().tailMap(fromKey, inclusive));
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\collect\AbstractMapBasedMultimap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */