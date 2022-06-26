/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Predicate;
/*     */ import com.google.common.base.Predicates;
/*     */ import com.google.common.base.Supplier;
/*     */ import java.io.Serializable;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ class StandardTable<R, C, V>
/*     */   extends AbstractTable<R, C, V>
/*     */   implements Serializable
/*     */ {
/*     */   @GwtTransient
/*     */   final Map<R, Map<C, V>> backingMap;
/*     */   @GwtTransient
/*     */   final Supplier<? extends Map<C, V>> factory;
/*     */   private transient Set<C> columnKeySet;
/*     */   private transient Map<R, Map<C, V>> rowMap;
/*     */   private transient ColumnMap columnMap;
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   StandardTable(Map<R, Map<C, V>> backingMap, Supplier<? extends Map<C, V>> factory) {
/*  73 */     this.backingMap = backingMap;
/*  74 */     this.factory = factory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean contains(@Nullable Object rowKey, @Nullable Object columnKey) {
/*  81 */     return (rowKey != null && columnKey != null && super.contains(rowKey, columnKey));
/*     */   }
/*     */   
/*     */   public boolean containsColumn(@Nullable Object columnKey) {
/*  85 */     if (columnKey == null) {
/*  86 */       return false;
/*     */     }
/*  88 */     for (Map<C, V> map : this.backingMap.values()) {
/*  89 */       if (Maps.safeContainsKey(map, columnKey)) {
/*  90 */         return true;
/*     */       }
/*     */     } 
/*  93 */     return false;
/*     */   }
/*     */   
/*     */   public boolean containsRow(@Nullable Object rowKey) {
/*  97 */     return (rowKey != null && Maps.safeContainsKey(this.backingMap, rowKey));
/*     */   }
/*     */   
/*     */   public boolean containsValue(@Nullable Object value) {
/* 101 */     return (value != null && super.containsValue(value));
/*     */   }
/*     */   
/*     */   public V get(@Nullable Object rowKey, @Nullable Object columnKey) {
/* 105 */     return (rowKey == null || columnKey == null) ? null : super.get(rowKey, columnKey);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 111 */     return this.backingMap.isEmpty();
/*     */   }
/*     */   
/*     */   public int size() {
/* 115 */     int size = 0;
/* 116 */     for (Map<C, V> map : this.backingMap.values()) {
/* 117 */       size += map.size();
/*     */     }
/* 119 */     return size;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() {
/* 125 */     this.backingMap.clear();
/*     */   }
/*     */   
/*     */   private Map<C, V> getOrCreate(R rowKey) {
/* 129 */     Map<C, V> map = this.backingMap.get(rowKey);
/* 130 */     if (map == null) {
/* 131 */       map = (Map<C, V>)this.factory.get();
/* 132 */       this.backingMap.put(rowKey, map);
/*     */     } 
/* 134 */     return map;
/*     */   }
/*     */   
/*     */   public V put(R rowKey, C columnKey, V value) {
/* 138 */     Preconditions.checkNotNull(rowKey);
/* 139 */     Preconditions.checkNotNull(columnKey);
/* 140 */     Preconditions.checkNotNull(value);
/* 141 */     return getOrCreate(rowKey).put(columnKey, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public V remove(@Nullable Object rowKey, @Nullable Object columnKey) {
/* 146 */     if (rowKey == null || columnKey == null) {
/* 147 */       return null;
/*     */     }
/* 149 */     Map<C, V> map = Maps.<Map<C, V>>safeGet(this.backingMap, rowKey);
/* 150 */     if (map == null) {
/* 151 */       return null;
/*     */     }
/* 153 */     V value = map.remove(columnKey);
/* 154 */     if (map.isEmpty()) {
/* 155 */       this.backingMap.remove(rowKey);
/*     */     }
/* 157 */     return value;
/*     */   }
/*     */   
/*     */   private Map<R, V> removeColumn(Object column) {
/* 161 */     Map<R, V> output = new LinkedHashMap<R, V>();
/* 162 */     Iterator<Map.Entry<R, Map<C, V>>> iterator = this.backingMap.entrySet().iterator();
/*     */     
/* 164 */     while (iterator.hasNext()) {
/* 165 */       Map.Entry<R, Map<C, V>> entry = iterator.next();
/* 166 */       V value = (V)((Map)entry.getValue()).remove(column);
/* 167 */       if (value != null) {
/* 168 */         output.put(entry.getKey(), value);
/* 169 */         if (((Map)entry.getValue()).isEmpty()) {
/* 170 */           iterator.remove();
/*     */         }
/*     */       } 
/*     */     } 
/* 174 */     return output;
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean containsMapping(Object rowKey, Object columnKey, Object value) {
/* 179 */     return (value != null && value.equals(get(rowKey, columnKey)));
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean removeMapping(Object rowKey, Object columnKey, Object value) {
/* 184 */     if (containsMapping(rowKey, columnKey, value)) {
/* 185 */       remove(rowKey, columnKey);
/* 186 */       return true;
/*     */     } 
/* 188 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   private abstract class TableSet<T>
/*     */     extends Sets.ImprovedAbstractSet<T>
/*     */   {
/*     */     private TableSet() {}
/*     */ 
/*     */     
/*     */     public boolean isEmpty() {
/* 199 */       return StandardTable.this.backingMap.isEmpty();
/*     */     }
/*     */     
/*     */     public void clear() {
/* 203 */       StandardTable.this.backingMap.clear();
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
/*     */   public Set<Table.Cell<R, C, V>> cellSet() {
/* 218 */     return super.cellSet();
/*     */   }
/*     */   
/*     */   Iterator<Table.Cell<R, C, V>> cellIterator() {
/* 222 */     return new CellIterator();
/*     */   }
/*     */   
/*     */   private class CellIterator implements Iterator<Table.Cell<R, C, V>> {
/* 226 */     final Iterator<Map.Entry<R, Map<C, V>>> rowIterator = StandardTable.this.backingMap.entrySet().iterator();
/*     */     
/*     */     Map.Entry<R, Map<C, V>> rowEntry;
/* 229 */     Iterator<Map.Entry<C, V>> columnIterator = Iterators.emptyModifiableIterator();
/*     */ 
/*     */     
/*     */     public boolean hasNext() {
/* 233 */       return (this.rowIterator.hasNext() || this.columnIterator.hasNext());
/*     */     }
/*     */     
/*     */     public Table.Cell<R, C, V> next() {
/* 237 */       if (!this.columnIterator.hasNext()) {
/* 238 */         this.rowEntry = this.rowIterator.next();
/* 239 */         this.columnIterator = ((Map<C, V>)this.rowEntry.getValue()).entrySet().iterator();
/*     */       } 
/* 241 */       Map.Entry<C, V> columnEntry = this.columnIterator.next();
/* 242 */       return Tables.immutableCell(this.rowEntry.getKey(), columnEntry.getKey(), columnEntry.getValue());
/*     */     }
/*     */ 
/*     */     
/*     */     public void remove() {
/* 247 */       this.columnIterator.remove();
/* 248 */       if (((Map)this.rowEntry.getValue()).isEmpty())
/* 249 */         this.rowIterator.remove(); 
/*     */     }
/*     */     
/*     */     private CellIterator() {} }
/*     */   
/*     */   public Map<C, V> row(R rowKey) {
/* 255 */     return new Row(rowKey);
/*     */   }
/*     */   
/*     */   class Row extends Maps.ImprovedAbstractMap<C, V> { final R rowKey;
/*     */     Map<C, V> backingRowMap;
/*     */     
/*     */     Row(R rowKey) {
/* 262 */       this.rowKey = (R)Preconditions.checkNotNull(rowKey);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     Map<C, V> backingRowMap() {
/* 268 */       return (this.backingRowMap == null || (this.backingRowMap.isEmpty() && StandardTable.this.backingMap.containsKey(this.rowKey))) ? (this.backingRowMap = computeBackingRowMap()) : this.backingRowMap;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     Map<C, V> computeBackingRowMap() {
/* 275 */       return (Map<C, V>)StandardTable.this.backingMap.get(this.rowKey);
/*     */     }
/*     */ 
/*     */     
/*     */     void maintainEmptyInvariant() {
/* 280 */       if (backingRowMap() != null && this.backingRowMap.isEmpty()) {
/* 281 */         StandardTable.this.backingMap.remove(this.rowKey);
/* 282 */         this.backingRowMap = null;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean containsKey(Object key) {
/* 288 */       Map<C, V> backingRowMap = backingRowMap();
/* 289 */       return (key != null && backingRowMap != null && Maps.safeContainsKey(backingRowMap, key));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public V get(Object key) {
/* 295 */       Map<C, V> backingRowMap = backingRowMap();
/* 296 */       return (key != null && backingRowMap != null) ? Maps.<V>safeGet(backingRowMap, key) : null;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public V put(C key, V value) {
/* 303 */       Preconditions.checkNotNull(key);
/* 304 */       Preconditions.checkNotNull(value);
/* 305 */       if (this.backingRowMap != null && !this.backingRowMap.isEmpty()) {
/* 306 */         return this.backingRowMap.put(key, value);
/*     */       }
/* 308 */       return StandardTable.this.put(this.rowKey, key, value);
/*     */     }
/*     */ 
/*     */     
/*     */     public V remove(Object key) {
/* 313 */       Map<C, V> backingRowMap = backingRowMap();
/* 314 */       if (backingRowMap == null) {
/* 315 */         return null;
/*     */       }
/* 317 */       V result = Maps.safeRemove(backingRowMap, key);
/* 318 */       maintainEmptyInvariant();
/* 319 */       return result;
/*     */     }
/*     */ 
/*     */     
/*     */     public void clear() {
/* 324 */       Map<C, V> backingRowMap = backingRowMap();
/* 325 */       if (backingRowMap != null) {
/* 326 */         backingRowMap.clear();
/*     */       }
/* 328 */       maintainEmptyInvariant();
/*     */     }
/*     */ 
/*     */     
/*     */     protected Set<Map.Entry<C, V>> createEntrySet() {
/* 333 */       return new RowEntrySet();
/*     */     }
/*     */     
/*     */     private final class RowEntrySet extends Maps.EntrySet<C, V> { private RowEntrySet() {}
/*     */       
/*     */       Map<C, V> map() {
/* 339 */         return StandardTable.Row.this;
/*     */       }
/*     */ 
/*     */       
/*     */       public int size() {
/* 344 */         Map<C, V> map = StandardTable.Row.this.backingRowMap();
/* 345 */         return (map == null) ? 0 : map.size();
/*     */       }
/*     */ 
/*     */       
/*     */       public Iterator<Map.Entry<C, V>> iterator() {
/* 350 */         Map<C, V> map = StandardTable.Row.this.backingRowMap();
/* 351 */         if (map == null) {
/* 352 */           return Iterators.emptyModifiableIterator();
/*     */         }
/* 354 */         final Iterator<Map.Entry<C, V>> iterator = map.entrySet().iterator();
/* 355 */         return new Iterator<Map.Entry<C, V>>() {
/*     */             public boolean hasNext() {
/* 357 */               return iterator.hasNext();
/*     */             }
/*     */             public Map.Entry<C, V> next() {
/* 360 */               final Map.Entry<C, V> entry = iterator.next();
/* 361 */               return new ForwardingMapEntry<C, V>() {
/*     */                   protected Map.Entry<C, V> delegate() {
/* 363 */                     return entry;
/*     */                   }
/*     */                   public V setValue(V value) {
/* 366 */                     return super.setValue((V)Preconditions.checkNotNull(value));
/*     */                   }
/*     */ 
/*     */                   
/*     */                   public boolean equals(Object object) {
/* 371 */                     return standardEquals(object);
/*     */                   }
/*     */                 };
/*     */             }
/*     */ 
/*     */             
/*     */             public void remove() {
/* 378 */               iterator.remove();
/* 379 */               StandardTable.Row.this.maintainEmptyInvariant();
/*     */             }
/*     */           };
/*     */       } }
/*     */      }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<R, V> column(C columnKey) {
/* 393 */     return new Column(columnKey);
/*     */   }
/*     */   
/*     */   private class Column extends Maps.ImprovedAbstractMap<R, V> {
/*     */     final C columnKey;
/*     */     
/*     */     Column(C columnKey) {
/* 400 */       this.columnKey = (C)Preconditions.checkNotNull(columnKey);
/*     */     }
/*     */     
/*     */     public V put(R key, V value) {
/* 404 */       return StandardTable.this.put(key, this.columnKey, value);
/*     */     }
/*     */     
/*     */     public V get(Object key) {
/* 408 */       return (V)StandardTable.this.get(key, this.columnKey);
/*     */     }
/*     */     
/*     */     public boolean containsKey(Object key) {
/* 412 */       return StandardTable.this.contains(key, this.columnKey);
/*     */     }
/*     */     
/*     */     public V remove(Object key) {
/* 416 */       return (V)StandardTable.this.remove(key, this.columnKey);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     boolean removeFromColumnIf(Predicate<? super Map.Entry<R, V>> predicate) {
/* 424 */       boolean changed = false;
/* 425 */       Iterator<Map.Entry<R, Map<C, V>>> iterator = StandardTable.this.backingMap.entrySet().iterator();
/*     */       
/* 427 */       while (iterator.hasNext()) {
/* 428 */         Map.Entry<R, Map<C, V>> entry = iterator.next();
/* 429 */         Map<C, V> map = entry.getValue();
/* 430 */         V value = map.get(this.columnKey);
/* 431 */         if (value != null && predicate.apply(Maps.immutableEntry(entry.getKey(), value))) {
/*     */           
/* 433 */           map.remove(this.columnKey);
/* 434 */           changed = true;
/* 435 */           if (map.isEmpty()) {
/* 436 */             iterator.remove();
/*     */           }
/*     */         } 
/*     */       } 
/* 440 */       return changed;
/*     */     }
/*     */     
/*     */     Set<Map.Entry<R, V>> createEntrySet() {
/* 444 */       return new EntrySet();
/*     */     }
/*     */     private class EntrySet extends Sets.ImprovedAbstractSet<Map.Entry<R, V>> { private EntrySet() {}
/*     */       
/*     */       public Iterator<Map.Entry<R, V>> iterator() {
/* 449 */         return new StandardTable.Column.EntrySetIterator();
/*     */       }
/*     */       
/*     */       public int size() {
/* 453 */         int size = 0;
/* 454 */         for (Map<C, V> map : (Iterable<Map<C, V>>)StandardTable.this.backingMap.values()) {
/* 455 */           if (map.containsKey(StandardTable.Column.this.columnKey)) {
/* 456 */             size++;
/*     */           }
/*     */         } 
/* 459 */         return size;
/*     */       }
/*     */       
/*     */       public boolean isEmpty() {
/* 463 */         return !StandardTable.this.containsColumn(StandardTable.Column.this.columnKey);
/*     */       }
/*     */       
/*     */       public void clear() {
/* 467 */         StandardTable.Column.this.removeFromColumnIf(Predicates.alwaysTrue());
/*     */       }
/*     */       
/*     */       public boolean contains(Object o) {
/* 471 */         if (o instanceof Map.Entry) {
/* 472 */           Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
/* 473 */           return StandardTable.this.containsMapping(entry.getKey(), StandardTable.Column.this.columnKey, entry.getValue());
/*     */         } 
/* 475 */         return false;
/*     */       }
/*     */       
/*     */       public boolean remove(Object obj) {
/* 479 */         if (obj instanceof Map.Entry) {
/* 480 */           Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
/* 481 */           return StandardTable.this.removeMapping(entry.getKey(), StandardTable.Column.this.columnKey, entry.getValue());
/*     */         } 
/* 483 */         return false;
/*     */       }
/*     */       
/*     */       public boolean retainAll(Collection<?> c) {
/* 487 */         return StandardTable.Column.this.removeFromColumnIf(Predicates.not(Predicates.in(c)));
/*     */       } }
/*     */ 
/*     */     
/*     */     private class EntrySetIterator extends AbstractIterator<Map.Entry<R, V>> {
/* 492 */       final Iterator<Map.Entry<R, Map<C, V>>> iterator = StandardTable.this.backingMap.entrySet().iterator();
/*     */       
/*     */       protected Map.Entry<R, V> computeNext() {
/* 495 */         while (this.iterator.hasNext()) {
/* 496 */           final Map.Entry<R, Map<C, V>> entry = this.iterator.next();
/* 497 */           if (((Map)entry.getValue()).containsKey(StandardTable.Column.this.columnKey)) {
/* 498 */             return new AbstractMapEntry<R, V>() {
/*     */                 public R getKey() {
/* 500 */                   return (R)entry.getKey();
/*     */                 }
/*     */                 public V getValue() {
/* 503 */                   return (V)((Map)entry.getValue()).get(StandardTable.Column.this.columnKey);
/*     */                 }
/*     */                 public V setValue(V value) {
/* 506 */                   return ((Map<C, V>)entry.getValue()).put(StandardTable.Column.this.columnKey, (V)Preconditions.checkNotNull(value));
/*     */                 }
/*     */               };
/*     */           }
/*     */         } 
/* 511 */         return endOfData();
/*     */       }
/*     */       private EntrySetIterator() {} }
/*     */     
/*     */     Set<R> createKeySet() {
/* 516 */       return new KeySet();
/*     */     }
/*     */     
/*     */     private class KeySet extends Maps.KeySet<R, V> {
/*     */       KeySet() {
/* 521 */         super(StandardTable.Column.this);
/*     */       }
/*     */       
/*     */       public boolean contains(Object obj) {
/* 525 */         return StandardTable.this.contains(obj, StandardTable.Column.this.columnKey);
/*     */       }
/*     */       
/*     */       public boolean remove(Object obj) {
/* 529 */         return (StandardTable.this.remove(obj, StandardTable.Column.this.columnKey) != null);
/*     */       }
/*     */       
/*     */       public boolean retainAll(Collection<?> c) {
/* 533 */         return StandardTable.Column.this.removeFromColumnIf((Predicate)Maps.keyPredicateOnEntries(Predicates.not(Predicates.in(c))));
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     Collection<V> createValues() {
/* 539 */       return new Values();
/*     */     }
/*     */     
/*     */     private class Values extends Maps.Values<R, V> {
/*     */       Values() {
/* 544 */         super(StandardTable.Column.this);
/*     */       }
/*     */       
/*     */       public boolean remove(Object obj) {
/* 548 */         return (obj != null && StandardTable.Column.this.removeFromColumnIf((Predicate)Maps.valuePredicateOnEntries(Predicates.equalTo(obj))));
/*     */       }
/*     */       
/*     */       public boolean removeAll(Collection<?> c) {
/* 552 */         return StandardTable.Column.this.removeFromColumnIf((Predicate)Maps.valuePredicateOnEntries(Predicates.in(c)));
/*     */       }
/*     */       
/*     */       public boolean retainAll(Collection<?> c) {
/* 556 */         return StandardTable.Column.this.removeFromColumnIf((Predicate)Maps.valuePredicateOnEntries(Predicates.not(Predicates.in(c))));
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public Set<R> rowKeySet() {
/* 562 */     return rowMap().keySet();
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
/*     */   public Set<C> columnKeySet() {
/* 578 */     Set<C> result = this.columnKeySet;
/* 579 */     return (result == null) ? (this.columnKeySet = new ColumnKeySet()) : result;
/*     */   }
/*     */   private class ColumnKeySet extends TableSet<C> { private ColumnKeySet() {}
/*     */     
/*     */     public Iterator<C> iterator() {
/* 584 */       return StandardTable.this.createColumnKeyIterator();
/*     */     }
/*     */     
/*     */     public int size() {
/* 588 */       return Iterators.size(iterator());
/*     */     }
/*     */     
/*     */     public boolean remove(Object obj) {
/* 592 */       if (obj == null) {
/* 593 */         return false;
/*     */       }
/* 595 */       boolean changed = false;
/* 596 */       Iterator<Map<C, V>> iterator = StandardTable.this.backingMap.values().iterator();
/* 597 */       while (iterator.hasNext()) {
/* 598 */         Map<C, V> map = iterator.next();
/* 599 */         if (map.keySet().remove(obj)) {
/* 600 */           changed = true;
/* 601 */           if (map.isEmpty()) {
/* 602 */             iterator.remove();
/*     */           }
/*     */         } 
/*     */       } 
/* 606 */       return changed;
/*     */     }
/*     */     
/*     */     public boolean removeAll(Collection<?> c) {
/* 610 */       Preconditions.checkNotNull(c);
/* 611 */       boolean changed = false;
/* 612 */       Iterator<Map<C, V>> iterator = StandardTable.this.backingMap.values().iterator();
/* 613 */       while (iterator.hasNext()) {
/* 614 */         Map<C, V> map = iterator.next();
/*     */ 
/*     */         
/* 617 */         if (Iterators.removeAll(map.keySet().iterator(), c)) {
/* 618 */           changed = true;
/* 619 */           if (map.isEmpty()) {
/* 620 */             iterator.remove();
/*     */           }
/*     */         } 
/*     */       } 
/* 624 */       return changed;
/*     */     }
/*     */     
/*     */     public boolean retainAll(Collection<?> c) {
/* 628 */       Preconditions.checkNotNull(c);
/* 629 */       boolean changed = false;
/* 630 */       Iterator<Map<C, V>> iterator = StandardTable.this.backingMap.values().iterator();
/* 631 */       while (iterator.hasNext()) {
/* 632 */         Map<C, V> map = iterator.next();
/* 633 */         if (map.keySet().retainAll(c)) {
/* 634 */           changed = true;
/* 635 */           if (map.isEmpty()) {
/* 636 */             iterator.remove();
/*     */           }
/*     */         } 
/*     */       } 
/* 640 */       return changed;
/*     */     }
/*     */     
/*     */     public boolean contains(Object obj) {
/* 644 */       return StandardTable.this.containsColumn(obj);
/*     */     } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Iterator<C> createColumnKeyIterator() {
/* 653 */     return new ColumnKeyIterator();
/*     */   }
/*     */   
/*     */   private class ColumnKeyIterator
/*     */     extends AbstractIterator<C>
/*     */   {
/* 659 */     final Map<C, V> seen = (Map<C, V>)StandardTable.this.factory.get();
/* 660 */     final Iterator<Map<C, V>> mapIterator = StandardTable.this.backingMap.values().iterator();
/* 661 */     Iterator<Map.Entry<C, V>> entryIterator = Iterators.emptyIterator();
/*     */     
/*     */     protected C computeNext() {
/*     */       while (true) {
/* 665 */         while (this.entryIterator.hasNext()) {
/* 666 */           Map.Entry<C, V> entry = this.entryIterator.next();
/* 667 */           if (!this.seen.containsKey(entry.getKey())) {
/* 668 */             this.seen.put(entry.getKey(), entry.getValue());
/* 669 */             return entry.getKey();
/*     */           } 
/* 671 */         }  if (this.mapIterator.hasNext()) {
/* 672 */           this.entryIterator = ((Map<C, V>)this.mapIterator.next()).entrySet().iterator(); continue;
/*     */         }  break;
/* 674 */       }  return endOfData();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private ColumnKeyIterator() {}
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Collection<V> values() {
/* 687 */     return super.values();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<R, Map<C, V>> rowMap() {
/* 693 */     Map<R, Map<C, V>> result = this.rowMap;
/* 694 */     return (result == null) ? (this.rowMap = createRowMap()) : result;
/*     */   }
/*     */   
/*     */   Map<R, Map<C, V>> createRowMap() {
/* 698 */     return new RowMap();
/*     */   }
/*     */   
/*     */   class RowMap extends Maps.ImprovedAbstractMap<R, Map<C, V>> {
/*     */     public boolean containsKey(Object key) {
/* 703 */       return StandardTable.this.containsRow(key);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public Map<C, V> get(Object key) {
/* 709 */       return StandardTable.this.containsRow(key) ? StandardTable.this.row(key) : null;
/*     */     }
/*     */     
/*     */     public Map<C, V> remove(Object key) {
/* 713 */       return (key == null) ? null : (Map<C, V>)StandardTable.this.backingMap.remove(key);
/*     */     }
/*     */     
/*     */     protected Set<Map.Entry<R, Map<C, V>>> createEntrySet() {
/* 717 */       return new EntrySet();
/*     */     }
/*     */     
/*     */     class EntrySet extends StandardTable<R, C, V>.TableSet<Map.Entry<R, Map<C, V>>> {
/*     */       public Iterator<Map.Entry<R, Map<C, V>>> iterator() {
/* 722 */         return Maps.asMapEntryIterator(StandardTable.this.backingMap.keySet(), new Function<R, Map<C, V>>()
/*     */             {
/*     */               public Map<C, V> apply(R rowKey) {
/* 725 */                 return StandardTable.this.row(rowKey);
/*     */               }
/*     */             });
/*     */       }
/*     */       
/*     */       public int size() {
/* 731 */         return StandardTable.this.backingMap.size();
/*     */       }
/*     */       
/*     */       public boolean contains(Object obj) {
/* 735 */         if (obj instanceof Map.Entry) {
/* 736 */           Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
/* 737 */           return (entry.getKey() != null && entry.getValue() instanceof Map && Collections2.safeContains(StandardTable.this.backingMap.entrySet(), entry));
/*     */         } 
/*     */ 
/*     */         
/* 741 */         return false;
/*     */       }
/*     */       
/*     */       public boolean remove(Object obj) {
/* 745 */         if (obj instanceof Map.Entry) {
/* 746 */           Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
/* 747 */           return (entry.getKey() != null && entry.getValue() instanceof Map && StandardTable.this.backingMap.entrySet().remove(entry));
/*     */         } 
/*     */ 
/*     */         
/* 751 */         return false;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<C, Map<R, V>> columnMap() {
/* 759 */     ColumnMap result = this.columnMap;
/* 760 */     return (result == null) ? (this.columnMap = new ColumnMap()) : result;
/*     */   }
/*     */   
/*     */   private class ColumnMap
/*     */     extends Maps.ImprovedAbstractMap<C, Map<R, V>> {
/*     */     private ColumnMap() {}
/*     */     
/*     */     public Map<R, V> get(Object key) {
/* 768 */       return StandardTable.this.containsColumn(key) ? StandardTable.this.column(key) : null;
/*     */     }
/*     */     
/*     */     public boolean containsKey(Object key) {
/* 772 */       return StandardTable.this.containsColumn(key);
/*     */     }
/*     */     
/*     */     public Map<R, V> remove(Object key) {
/* 776 */       return StandardTable.this.containsColumn(key) ? StandardTable.this.removeColumn(key) : null;
/*     */     }
/*     */     
/*     */     public Set<Map.Entry<C, Map<R, V>>> createEntrySet() {
/* 780 */       return new ColumnMapEntrySet();
/*     */     }
/*     */     
/*     */     public Set<C> keySet() {
/* 784 */       return StandardTable.this.columnKeySet();
/*     */     }
/*     */     
/*     */     Collection<Map<R, V>> createValues() {
/* 788 */       return new ColumnMapValues();
/*     */     }
/*     */     
/*     */     class ColumnMapEntrySet extends StandardTable<R, C, V>.TableSet<Map.Entry<C, Map<R, V>>> {
/*     */       public Iterator<Map.Entry<C, Map<R, V>>> iterator() {
/* 793 */         return Maps.asMapEntryIterator(StandardTable.this.columnKeySet(), new Function<C, Map<R, V>>()
/*     */             {
/*     */               public Map<R, V> apply(C columnKey) {
/* 796 */                 return StandardTable.this.column(columnKey);
/*     */               }
/*     */             });
/*     */       }
/*     */       
/*     */       public int size() {
/* 802 */         return StandardTable.this.columnKeySet().size();
/*     */       }
/*     */       
/*     */       public boolean contains(Object obj) {
/* 806 */         if (obj instanceof Map.Entry) {
/* 807 */           Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
/* 808 */           if (StandardTable.this.containsColumn(entry.getKey())) {
/*     */ 
/*     */ 
/*     */             
/* 812 */             C columnKey = (C)entry.getKey();
/* 813 */             return StandardTable.ColumnMap.this.get(columnKey).equals(entry.getValue());
/*     */           } 
/*     */         } 
/* 816 */         return false;
/*     */       }
/*     */       
/*     */       public boolean remove(Object obj) {
/* 820 */         if (contains(obj)) {
/* 821 */           Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
/* 822 */           StandardTable.this.removeColumn(entry.getKey());
/* 823 */           return true;
/*     */         } 
/* 825 */         return false;
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       public boolean removeAll(Collection<?> c) {
/* 835 */         Preconditions.checkNotNull(c);
/* 836 */         return Sets.removeAllImpl(this, c.iterator());
/*     */       }
/*     */       
/*     */       public boolean retainAll(Collection<?> c) {
/* 840 */         Preconditions.checkNotNull(c);
/* 841 */         boolean changed = false;
/* 842 */         for (C columnKey : Lists.newArrayList(StandardTable.this.columnKeySet().iterator())) {
/* 843 */           if (!c.contains(Maps.immutableEntry(columnKey, StandardTable.this.column(columnKey)))) {
/* 844 */             StandardTable.this.removeColumn(columnKey);
/* 845 */             changed = true;
/*     */           } 
/*     */         } 
/* 848 */         return changed;
/*     */       }
/*     */     }
/*     */     
/*     */     private class ColumnMapValues extends Maps.Values<C, Map<R, V>> {
/*     */       ColumnMapValues() {
/* 854 */         super(StandardTable.ColumnMap.this);
/*     */       }
/*     */       
/*     */       public boolean remove(Object obj) {
/* 858 */         for (Map.Entry<C, Map<R, V>> entry : StandardTable.ColumnMap.this.entrySet()) {
/* 859 */           if (((Map)entry.getValue()).equals(obj)) {
/* 860 */             StandardTable.this.removeColumn(entry.getKey());
/* 861 */             return true;
/*     */           } 
/*     */         } 
/* 864 */         return false;
/*     */       }
/*     */       
/*     */       public boolean removeAll(Collection<?> c) {
/* 868 */         Preconditions.checkNotNull(c);
/* 869 */         boolean changed = false;
/* 870 */         for (C columnKey : Lists.newArrayList(StandardTable.this.columnKeySet().iterator())) {
/* 871 */           if (c.contains(StandardTable.this.column(columnKey))) {
/* 872 */             StandardTable.this.removeColumn(columnKey);
/* 873 */             changed = true;
/*     */           } 
/*     */         } 
/* 876 */         return changed;
/*     */       }
/*     */       
/*     */       public boolean retainAll(Collection<?> c) {
/* 880 */         Preconditions.checkNotNull(c);
/* 881 */         boolean changed = false;
/* 882 */         for (C columnKey : Lists.newArrayList(StandardTable.this.columnKeySet().iterator())) {
/* 883 */           if (!c.contains(StandardTable.this.column(columnKey))) {
/* 884 */             StandardTable.this.removeColumn(columnKey);
/* 885 */             changed = true;
/*     */           } 
/*     */         } 
/* 888 */         return changed;
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\collect\StandardTable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */