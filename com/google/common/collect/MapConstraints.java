/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.io.Serializable;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
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
/*     */ @Beta
/*     */ @GwtCompatible
/*     */ public final class MapConstraints
/*     */ {
/*     */   public static MapConstraint<Object, Object> notNull() {
/*  54 */     return NotNullMapConstraint.INSTANCE;
/*     */   }
/*     */   
/*     */   private enum NotNullMapConstraint
/*     */     implements MapConstraint<Object, Object> {
/*  59 */     INSTANCE;
/*     */ 
/*     */     
/*     */     public void checkKeyValue(Object key, Object value) {
/*  63 */       Preconditions.checkNotNull(key);
/*  64 */       Preconditions.checkNotNull(value);
/*     */     }
/*     */     
/*     */     public String toString() {
/*  68 */       return "Not null";
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
/*     */   public static <K, V> Map<K, V> constrainedMap(Map<K, V> map, MapConstraint<? super K, ? super V> constraint) {
/*  86 */     return new ConstrainedMap<K, V>(map, constraint);
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
/*     */   public static <K, V> Multimap<K, V> constrainedMultimap(Multimap<K, V> multimap, MapConstraint<? super K, ? super V> constraint) {
/* 107 */     return new ConstrainedMultimap<K, V>(multimap, constraint);
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
/*     */   public static <K, V> ListMultimap<K, V> constrainedListMultimap(ListMultimap<K, V> multimap, MapConstraint<? super K, ? super V> constraint) {
/* 129 */     return new ConstrainedListMultimap<K, V>(multimap, constraint);
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
/*     */   public static <K, V> SetMultimap<K, V> constrainedSetMultimap(SetMultimap<K, V> multimap, MapConstraint<? super K, ? super V> constraint) {
/* 150 */     return new ConstrainedSetMultimap<K, V>(multimap, constraint);
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
/*     */   public static <K, V> SortedSetMultimap<K, V> constrainedSortedSetMultimap(SortedSetMultimap<K, V> multimap, MapConstraint<? super K, ? super V> constraint) {
/* 171 */     return new ConstrainedSortedSetMultimap<K, V>(multimap, constraint);
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
/*     */   private static <K, V> Map.Entry<K, V> constrainedEntry(final Map.Entry<K, V> entry, final MapConstraint<? super K, ? super V> constraint) {
/* 186 */     Preconditions.checkNotNull(entry);
/* 187 */     Preconditions.checkNotNull(constraint);
/* 188 */     return new ForwardingMapEntry<K, V>() {
/*     */         protected Map.Entry<K, V> delegate() {
/* 190 */           return entry;
/*     */         }
/*     */         public V setValue(V value) {
/* 193 */           constraint.checkKeyValue(getKey(), value);
/* 194 */           return (V)entry.setValue(value);
/*     */         }
/*     */       };
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
/*     */   private static <K, V> Map.Entry<K, Collection<V>> constrainedAsMapEntry(final Map.Entry<K, Collection<V>> entry, final MapConstraint<? super K, ? super V> constraint) {
/* 212 */     Preconditions.checkNotNull(entry);
/* 213 */     Preconditions.checkNotNull(constraint);
/* 214 */     return (Map.Entry)new ForwardingMapEntry<K, Collection<Collection<V>>>() {
/*     */         protected Map.Entry<K, Collection<V>> delegate() {
/* 216 */           return entry;
/*     */         }
/*     */         public Collection<V> getValue() {
/* 219 */           return Constraints.constrainedTypePreservingCollection((Collection<V>)entry.getValue(), new Constraint()
/*     */               {
/*     */                 public V checkElement(V value)
/*     */                 {
/* 223 */                   constraint.checkKeyValue(MapConstraints.null.this.getKey(), value);
/* 224 */                   return value;
/*     */                 }
/*     */               });
/*     */         }
/*     */       };
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
/*     */   private static <K, V> Set<Map.Entry<K, Collection<V>>> constrainedAsMapEntries(Set<Map.Entry<K, Collection<V>>> entries, MapConstraint<? super K, ? super V> constraint) {
/* 246 */     return new ConstrainedAsMapEntries<K, V>(entries, constraint);
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
/*     */   private static <K, V> Collection<Map.Entry<K, V>> constrainedEntries(Collection<Map.Entry<K, V>> entries, MapConstraint<? super K, ? super V> constraint) {
/* 264 */     if (entries instanceof Set) {
/* 265 */       return constrainedEntrySet((Set<Map.Entry<K, V>>)entries, constraint);
/*     */     }
/* 267 */     return new ConstrainedEntries<K, V>(entries, constraint);
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
/*     */   private static <K, V> Set<Map.Entry<K, V>> constrainedEntrySet(Set<Map.Entry<K, V>> entries, MapConstraint<? super K, ? super V> constraint) {
/* 287 */     return new ConstrainedEntrySet<K, V>(entries, constraint);
/*     */   }
/*     */   
/*     */   static class ConstrainedMap<K, V>
/*     */     extends ForwardingMap<K, V>
/*     */   {
/*     */     private final Map<K, V> delegate;
/*     */     final MapConstraint<? super K, ? super V> constraint;
/*     */     private transient Set<Map.Entry<K, V>> entrySet;
/*     */     
/*     */     ConstrainedMap(Map<K, V> delegate, MapConstraint<? super K, ? super V> constraint) {
/* 298 */       this.delegate = (Map<K, V>)Preconditions.checkNotNull(delegate);
/* 299 */       this.constraint = (MapConstraint<? super K, ? super V>)Preconditions.checkNotNull(constraint);
/*     */     }
/*     */     protected Map<K, V> delegate() {
/* 302 */       return this.delegate;
/*     */     }
/*     */     public Set<Map.Entry<K, V>> entrySet() {
/* 305 */       Set<Map.Entry<K, V>> result = this.entrySet;
/* 306 */       if (result == null) {
/* 307 */         this.entrySet = result = MapConstraints.constrainedEntrySet(this.delegate.entrySet(), this.constraint);
/*     */       }
/*     */       
/* 310 */       return result;
/*     */     }
/*     */     public V put(K key, V value) {
/* 313 */       this.constraint.checkKeyValue(key, value);
/* 314 */       return this.delegate.put(key, value);
/*     */     }
/*     */     public void putAll(Map<? extends K, ? extends V> map) {
/* 317 */       this.delegate.putAll(MapConstraints.checkMap(map, this.constraint));
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
/*     */   public static <K, V> BiMap<K, V> constrainedBiMap(BiMap<K, V> map, MapConstraint<? super K, ? super V> constraint) {
/* 334 */     return new ConstrainedBiMap<K, V>(map, null, constraint);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class ConstrainedBiMap<K, V>
/*     */     extends ConstrainedMap<K, V>
/*     */     implements BiMap<K, V>
/*     */   {
/*     */     volatile BiMap<V, K> inverse;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     ConstrainedBiMap(BiMap<K, V> delegate, @Nullable BiMap<V, K> inverse, MapConstraint<? super K, ? super V> constraint) {
/* 356 */       super(delegate, constraint);
/* 357 */       this.inverse = inverse;
/*     */     }
/*     */     
/*     */     protected BiMap<K, V> delegate() {
/* 361 */       return (BiMap<K, V>)super.delegate();
/*     */     }
/*     */ 
/*     */     
/*     */     public V forcePut(K key, V value) {
/* 366 */       this.constraint.checkKeyValue(key, value);
/* 367 */       return delegate().forcePut(key, value);
/*     */     }
/*     */ 
/*     */     
/*     */     public BiMap<V, K> inverse() {
/* 372 */       if (this.inverse == null) {
/* 373 */         this.inverse = new ConstrainedBiMap(delegate().inverse(), this, new MapConstraints.InverseConstraint<K, V>(this.constraint));
/*     */       }
/*     */       
/* 376 */       return this.inverse;
/*     */     }
/*     */     
/*     */     public Set<V> values() {
/* 380 */       return delegate().values();
/*     */     }
/*     */   }
/*     */   
/*     */   private static class InverseConstraint<K, V>
/*     */     implements MapConstraint<K, V> {
/*     */     final MapConstraint<? super V, ? super K> constraint;
/*     */     
/*     */     public InverseConstraint(MapConstraint<? super V, ? super K> constraint) {
/* 389 */       this.constraint = (MapConstraint<? super V, ? super K>)Preconditions.checkNotNull(constraint);
/*     */     }
/*     */     
/*     */     public void checkKeyValue(K key, V value) {
/* 393 */       this.constraint.checkKeyValue(value, key);
/*     */     }
/*     */   }
/*     */   
/*     */   private static class ConstrainedMultimap<K, V>
/*     */     extends ForwardingMultimap<K, V>
/*     */     implements Serializable
/*     */   {
/*     */     final MapConstraint<? super K, ? super V> constraint;
/*     */     final Multimap<K, V> delegate;
/*     */     transient Collection<Map.Entry<K, V>> entries;
/*     */     transient Map<K, Collection<V>> asMap;
/*     */     
/*     */     public ConstrainedMultimap(Multimap<K, V> delegate, MapConstraint<? super K, ? super V> constraint) {
/* 407 */       this.delegate = (Multimap<K, V>)Preconditions.checkNotNull(delegate);
/* 408 */       this.constraint = (MapConstraint<? super K, ? super V>)Preconditions.checkNotNull(constraint);
/*     */     }
/*     */     
/*     */     protected Multimap<K, V> delegate() {
/* 412 */       return this.delegate;
/*     */     }
/*     */     
/*     */     public Map<K, Collection<V>> asMap() {
/* 416 */       Map<K, Collection<V>> result = this.asMap;
/* 417 */       if (result == null) {
/* 418 */         final Map<K, Collection<V>> asMapDelegate = this.delegate.asMap();
/*     */         
/* 420 */         this.asMap = result = (Map)new ForwardingMap<K, Collection<Collection<Collection<Collection<V>>>>>() {
/*     */             Set<Map.Entry<K, Collection<V>>> entrySet;
/*     */             Collection<Collection<V>> values;
/*     */             
/*     */             protected Map<K, Collection<V>> delegate() {
/* 425 */               return asMapDelegate;
/*     */             }
/*     */             
/*     */             public Set<Map.Entry<K, Collection<V>>> entrySet() {
/* 429 */               Set<Map.Entry<K, Collection<V>>> result = this.entrySet;
/* 430 */               if (result == null) {
/* 431 */                 this.entrySet = result = MapConstraints.constrainedAsMapEntries(asMapDelegate.entrySet(), MapConstraints.ConstrainedMultimap.this.constraint);
/*     */               }
/*     */               
/* 434 */               return result;
/*     */             }
/*     */ 
/*     */             
/*     */             public Collection<V> get(Object key) {
/*     */               try {
/* 440 */                 Collection<V> collection = MapConstraints.ConstrainedMultimap.this.get(key);
/* 441 */                 return collection.isEmpty() ? null : collection;
/* 442 */               } catch (ClassCastException e) {
/* 443 */                 return null;
/*     */               } 
/*     */             }
/*     */             
/*     */             public Collection<Collection<V>> values() {
/* 448 */               Collection<Collection<V>> result = this.values;
/* 449 */               if (result == null) {
/* 450 */                 this.values = result = new MapConstraints.ConstrainedAsMapValues<K, V>((Collection)delegate().values(), entrySet());
/*     */               }
/*     */               
/* 453 */               return result;
/*     */             }
/*     */             
/*     */             public boolean containsValue(Object o) {
/* 457 */               return values().contains(o);
/*     */             }
/*     */           };
/*     */       } 
/* 461 */       return result;
/*     */     }
/*     */     
/*     */     public Collection<Map.Entry<K, V>> entries() {
/* 465 */       Collection<Map.Entry<K, V>> result = this.entries;
/* 466 */       if (result == null) {
/* 467 */         this.entries = result = MapConstraints.constrainedEntries(this.delegate.entries(), this.constraint);
/*     */       }
/* 469 */       return result;
/*     */     }
/*     */     
/*     */     public Collection<V> get(final K key) {
/* 473 */       return Constraints.constrainedTypePreservingCollection(this.delegate.get(key), new Constraint<V>()
/*     */           {
/*     */             public V checkElement(V value)
/*     */             {
/* 477 */               MapConstraints.ConstrainedMultimap.this.constraint.checkKeyValue(key, value);
/* 478 */               return value;
/*     */             }
/*     */           });
/*     */     }
/*     */     
/*     */     public boolean put(K key, V value) {
/* 484 */       this.constraint.checkKeyValue(key, value);
/* 485 */       return this.delegate.put(key, value);
/*     */     }
/*     */     
/*     */     public boolean putAll(K key, Iterable<? extends V> values) {
/* 489 */       return this.delegate.putAll(key, MapConstraints.checkValues(key, values, this.constraint));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean putAll(Multimap<? extends K, ? extends V> multimap) {
/* 494 */       boolean changed = false;
/* 495 */       for (Map.Entry<? extends K, ? extends V> entry : multimap.entries()) {
/* 496 */         changed |= put(entry.getKey(), entry.getValue());
/*     */       }
/* 498 */       return changed;
/*     */     }
/*     */ 
/*     */     
/*     */     public Collection<V> replaceValues(K key, Iterable<? extends V> values) {
/* 503 */       return this.delegate.replaceValues(key, MapConstraints.checkValues(key, values, this.constraint));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class ConstrainedAsMapValues<K, V>
/*     */     extends ForwardingCollection<Collection<V>>
/*     */   {
/*     */     final Collection<Collection<V>> delegate;
/*     */ 
/*     */     
/*     */     final Set<Map.Entry<K, Collection<V>>> entrySet;
/*     */ 
/*     */     
/*     */     ConstrainedAsMapValues(Collection<Collection<V>> delegate, Set<Map.Entry<K, Collection<V>>> entrySet) {
/* 519 */       this.delegate = delegate;
/* 520 */       this.entrySet = entrySet;
/*     */     }
/*     */     protected Collection<Collection<V>> delegate() {
/* 523 */       return this.delegate;
/*     */     }
/*     */     
/*     */     public Iterator<Collection<V>> iterator() {
/* 527 */       final Iterator<Map.Entry<K, Collection<V>>> iterator = this.entrySet.iterator();
/* 528 */       return new Iterator<Collection<V>>()
/*     */         {
/*     */           public boolean hasNext() {
/* 531 */             return iterator.hasNext();
/*     */           }
/*     */           
/*     */           public Collection<V> next() {
/* 535 */             return (Collection<V>)((Map.Entry)iterator.next()).getValue();
/*     */           }
/*     */           
/*     */           public void remove() {
/* 539 */             iterator.remove();
/*     */           }
/*     */         };
/*     */     }
/*     */     
/*     */     public Object[] toArray() {
/* 545 */       return standardToArray();
/*     */     }
/*     */     public <T> T[] toArray(T[] array) {
/* 548 */       return (T[])standardToArray((Object[])array);
/*     */     }
/*     */     public boolean contains(Object o) {
/* 551 */       return standardContains(o);
/*     */     }
/*     */     public boolean containsAll(Collection<?> c) {
/* 554 */       return standardContainsAll(c);
/*     */     }
/*     */     public boolean remove(Object o) {
/* 557 */       return standardRemove(o);
/*     */     }
/*     */     public boolean removeAll(Collection<?> c) {
/* 560 */       return standardRemoveAll(c);
/*     */     }
/*     */     public boolean retainAll(Collection<?> c) {
/* 563 */       return standardRetainAll(c);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static class ConstrainedEntries<K, V>
/*     */     extends ForwardingCollection<Map.Entry<K, V>>
/*     */   {
/*     */     final MapConstraint<? super K, ? super V> constraint;
/*     */     final Collection<Map.Entry<K, V>> entries;
/*     */     
/*     */     ConstrainedEntries(Collection<Map.Entry<K, V>> entries, MapConstraint<? super K, ? super V> constraint) {
/* 575 */       this.entries = entries;
/* 576 */       this.constraint = constraint;
/*     */     }
/*     */     protected Collection<Map.Entry<K, V>> delegate() {
/* 579 */       return this.entries;
/*     */     }
/*     */     
/*     */     public Iterator<Map.Entry<K, V>> iterator() {
/* 583 */       final Iterator<Map.Entry<K, V>> iterator = this.entries.iterator();
/* 584 */       return new ForwardingIterator<Map.Entry<K, V>>() {
/*     */           public Map.Entry<K, V> next() {
/* 586 */             return MapConstraints.constrainedEntry(iterator.next(), MapConstraints.ConstrainedEntries.this.constraint);
/*     */           }
/*     */           protected Iterator<Map.Entry<K, V>> delegate() {
/* 589 */             return iterator;
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public Object[] toArray() {
/* 597 */       return standardToArray();
/*     */     }
/*     */     public <T> T[] toArray(T[] array) {
/* 600 */       return (T[])standardToArray((Object[])array);
/*     */     }
/*     */     public boolean contains(Object o) {
/* 603 */       return Maps.containsEntryImpl(delegate(), o);
/*     */     }
/*     */     public boolean containsAll(Collection<?> c) {
/* 606 */       return standardContainsAll(c);
/*     */     }
/*     */     public boolean remove(Object o) {
/* 609 */       return Maps.removeEntryImpl(delegate(), o);
/*     */     }
/*     */     public boolean removeAll(Collection<?> c) {
/* 612 */       return standardRemoveAll(c);
/*     */     }
/*     */     public boolean retainAll(Collection<?> c) {
/* 615 */       return standardRetainAll(c);
/*     */     }
/*     */   }
/*     */   
/*     */   static class ConstrainedEntrySet<K, V>
/*     */     extends ConstrainedEntries<K, V>
/*     */     implements Set<Map.Entry<K, V>>
/*     */   {
/*     */     ConstrainedEntrySet(Set<Map.Entry<K, V>> entries, MapConstraint<? super K, ? super V> constraint) {
/* 624 */       super(entries, constraint);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean equals(@Nullable Object object) {
/* 630 */       return Sets.equalsImpl(this, object);
/*     */     }
/*     */     
/*     */     public int hashCode() {
/* 634 */       return Sets.hashCodeImpl(this);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static class ConstrainedAsMapEntries<K, V>
/*     */     extends ForwardingSet<Map.Entry<K, Collection<V>>>
/*     */   {
/*     */     private final MapConstraint<? super K, ? super V> constraint;
/*     */     private final Set<Map.Entry<K, Collection<V>>> entries;
/*     */     
/*     */     ConstrainedAsMapEntries(Set<Map.Entry<K, Collection<V>>> entries, MapConstraint<? super K, ? super V> constraint) {
/* 646 */       this.entries = entries;
/* 647 */       this.constraint = constraint;
/*     */     }
/*     */     
/*     */     protected Set<Map.Entry<K, Collection<V>>> delegate() {
/* 651 */       return this.entries;
/*     */     }
/*     */     
/*     */     public Iterator<Map.Entry<K, Collection<V>>> iterator() {
/* 655 */       final Iterator<Map.Entry<K, Collection<V>>> iterator = this.entries.iterator();
/* 656 */       return new ForwardingIterator<Map.Entry<K, Collection<V>>>() {
/*     */           public Map.Entry<K, Collection<V>> next() {
/* 658 */             return MapConstraints.constrainedAsMapEntry(iterator.next(), MapConstraints.ConstrainedAsMapEntries.this.constraint);
/*     */           }
/*     */           protected Iterator<Map.Entry<K, Collection<V>>> delegate() {
/* 661 */             return iterator;
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public Object[] toArray() {
/* 669 */       return standardToArray();
/*     */     }
/*     */     
/*     */     public <T> T[] toArray(T[] array) {
/* 673 */       return (T[])standardToArray((Object[])array);
/*     */     }
/*     */     
/*     */     public boolean contains(Object o) {
/* 677 */       return Maps.containsEntryImpl(delegate(), o);
/*     */     }
/*     */     
/*     */     public boolean containsAll(Collection<?> c) {
/* 681 */       return standardContainsAll(c);
/*     */     }
/*     */     
/*     */     public boolean equals(@Nullable Object object) {
/* 685 */       return standardEquals(object);
/*     */     }
/*     */     
/*     */     public int hashCode() {
/* 689 */       return standardHashCode();
/*     */     }
/*     */     
/*     */     public boolean remove(Object o) {
/* 693 */       return Maps.removeEntryImpl(delegate(), o);
/*     */     }
/*     */     
/*     */     public boolean removeAll(Collection<?> c) {
/* 697 */       return standardRemoveAll(c);
/*     */     }
/*     */     
/*     */     public boolean retainAll(Collection<?> c) {
/* 701 */       return standardRetainAll(c);
/*     */     }
/*     */   }
/*     */   
/*     */   private static class ConstrainedListMultimap<K, V>
/*     */     extends ConstrainedMultimap<K, V>
/*     */     implements ListMultimap<K, V> {
/*     */     ConstrainedListMultimap(ListMultimap<K, V> delegate, MapConstraint<? super K, ? super V> constraint) {
/* 709 */       super(delegate, constraint);
/*     */     }
/*     */     public List<V> get(K key) {
/* 712 */       return (List<V>)super.get(key);
/*     */     }
/*     */     public List<V> removeAll(Object key) {
/* 715 */       return (List<V>)super.removeAll(key);
/*     */     }
/*     */     
/*     */     public List<V> replaceValues(K key, Iterable<? extends V> values) {
/* 719 */       return (List<V>)super.replaceValues(key, values);
/*     */     }
/*     */   }
/*     */   
/*     */   private static class ConstrainedSetMultimap<K, V>
/*     */     extends ConstrainedMultimap<K, V>
/*     */     implements SetMultimap<K, V> {
/*     */     ConstrainedSetMultimap(SetMultimap<K, V> delegate, MapConstraint<? super K, ? super V> constraint) {
/* 727 */       super(delegate, constraint);
/*     */     }
/*     */     public Set<V> get(K key) {
/* 730 */       return (Set<V>)super.get(key);
/*     */     }
/*     */     public Set<Map.Entry<K, V>> entries() {
/* 733 */       return (Set<Map.Entry<K, V>>)super.entries();
/*     */     }
/*     */     public Set<V> removeAll(Object key) {
/* 736 */       return (Set<V>)super.removeAll(key);
/*     */     }
/*     */     
/*     */     public Set<V> replaceValues(K key, Iterable<? extends V> values) {
/* 740 */       return (Set<V>)super.replaceValues(key, values);
/*     */     }
/*     */   }
/*     */   
/*     */   private static class ConstrainedSortedSetMultimap<K, V>
/*     */     extends ConstrainedSetMultimap<K, V>
/*     */     implements SortedSetMultimap<K, V> {
/*     */     ConstrainedSortedSetMultimap(SortedSetMultimap<K, V> delegate, MapConstraint<? super K, ? super V> constraint) {
/* 748 */       super(delegate, constraint);
/*     */     }
/*     */     public SortedSet<V> get(K key) {
/* 751 */       return (SortedSet<V>)super.get(key);
/*     */     }
/*     */     public SortedSet<V> removeAll(Object key) {
/* 754 */       return (SortedSet<V>)super.removeAll(key);
/*     */     }
/*     */     
/*     */     public SortedSet<V> replaceValues(K key, Iterable<? extends V> values) {
/* 758 */       return (SortedSet<V>)super.replaceValues(key, values);
/*     */     }
/*     */     
/*     */     public Comparator<? super V> valueComparator() {
/* 762 */       return ((SortedSetMultimap)delegate()).valueComparator();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static <K, V> Collection<V> checkValues(K key, Iterable<? extends V> values, MapConstraint<? super K, ? super V> constraint) {
/* 769 */     Collection<V> copy = Lists.newArrayList(values);
/* 770 */     for (V value : copy) {
/* 771 */       constraint.checkKeyValue(key, value);
/*     */     }
/* 773 */     return copy;
/*     */   }
/*     */ 
/*     */   
/*     */   private static <K, V> Map<K, V> checkMap(Map<? extends K, ? extends V> map, MapConstraint<? super K, ? super V> constraint) {
/* 778 */     Map<K, V> copy = new LinkedHashMap<K, V>(map);
/* 779 */     for (Map.Entry<K, V> entry : copy.entrySet()) {
/* 780 */       constraint.checkKeyValue(entry.getKey(), entry.getValue());
/*     */     }
/* 782 */     return copy;
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\collect\MapConstraints.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */