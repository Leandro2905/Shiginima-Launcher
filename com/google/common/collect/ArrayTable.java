/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.Array;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ @GwtCompatible(emulated = true)
/*     */ public final class ArrayTable<R, C, V>
/*     */   extends AbstractTable<R, C, V>
/*     */   implements Serializable
/*     */ {
/*     */   private final ImmutableList<R> rowList;
/*     */   private final ImmutableList<C> columnList;
/*     */   private final ImmutableMap<R, Integer> rowKeyToIndex;
/*     */   private final ImmutableMap<C, Integer> columnKeyToIndex;
/*     */   private final V[][] array;
/*     */   private transient ColumnMap columnMap;
/*     */   private transient RowMap rowMap;
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   public static <R, C, V> ArrayTable<R, C, V> create(Iterable<? extends R> rowKeys, Iterable<? extends C> columnKeys) {
/*  99 */     return new ArrayTable<R, C, V>(rowKeys, columnKeys);
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
/*     */   public static <R, C, V> ArrayTable<R, C, V> create(Table<R, C, V> table) {
/* 131 */     return (table instanceof ArrayTable) ? new ArrayTable<R, C, V>((ArrayTable<R, C, V>)table) : new ArrayTable<R, C, V>(table);
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
/*     */   private ArrayTable(Iterable<? extends R> rowKeys, Iterable<? extends C> columnKeys) {
/* 146 */     this.rowList = ImmutableList.copyOf(rowKeys);
/* 147 */     this.columnList = ImmutableList.copyOf(columnKeys);
/* 148 */     Preconditions.checkArgument(!this.rowList.isEmpty());
/* 149 */     Preconditions.checkArgument(!this.columnList.isEmpty());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 156 */     this.rowKeyToIndex = index(this.rowList);
/* 157 */     this.columnKeyToIndex = index(this.columnList);
/*     */ 
/*     */     
/* 160 */     V[][] tmpArray = (V[][])new Object[this.rowList.size()][this.columnList.size()];
/*     */     
/* 162 */     this.array = tmpArray;
/*     */     
/* 164 */     eraseAll();
/*     */   }
/*     */   
/*     */   private static <E> ImmutableMap<E, Integer> index(List<E> list) {
/* 168 */     ImmutableMap.Builder<E, Integer> columnBuilder = ImmutableMap.builder();
/* 169 */     for (int i = 0; i < list.size(); i++) {
/* 170 */       columnBuilder.put(list.get(i), Integer.valueOf(i));
/*     */     }
/* 172 */     return columnBuilder.build();
/*     */   }
/*     */   
/*     */   private ArrayTable(Table<R, C, V> table) {
/* 176 */     this(table.rowKeySet(), table.columnKeySet());
/* 177 */     putAll(table);
/*     */   }
/*     */   
/*     */   private ArrayTable(ArrayTable<R, C, V> table) {
/* 181 */     this.rowList = table.rowList;
/* 182 */     this.columnList = table.columnList;
/* 183 */     this.rowKeyToIndex = table.rowKeyToIndex;
/* 184 */     this.columnKeyToIndex = table.columnKeyToIndex;
/*     */     
/* 186 */     V[][] copy = (V[][])new Object[this.rowList.size()][this.columnList.size()];
/* 187 */     this.array = copy;
/*     */     
/* 189 */     eraseAll();
/* 190 */     for (int i = 0; i < this.rowList.size(); i++)
/* 191 */       System.arraycopy(table.array[i], 0, copy[i], 0, (table.array[i]).length); 
/*     */   }
/*     */   
/*     */   private static abstract class ArrayMap<K, V>
/*     */     extends Maps.ImprovedAbstractMap<K, V> {
/*     */     private final ImmutableMap<K, Integer> keyIndex;
/*     */     
/*     */     private ArrayMap(ImmutableMap<K, Integer> keyIndex) {
/* 199 */       this.keyIndex = keyIndex;
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<K> keySet() {
/* 204 */       return this.keyIndex.keySet();
/*     */     }
/*     */     
/*     */     K getKey(int index) {
/* 208 */       return this.keyIndex.keySet().asList().get(index);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int size() {
/* 219 */       return this.keyIndex.size();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isEmpty() {
/* 224 */       return this.keyIndex.isEmpty();
/*     */     }
/*     */ 
/*     */     
/*     */     protected Set<Map.Entry<K, V>> createEntrySet() {
/* 229 */       return new Maps.EntrySet<K, V>()
/*     */         {
/*     */           Map<K, V> map() {
/* 232 */             return ArrayTable.ArrayMap.this;
/*     */           }
/*     */ 
/*     */           
/*     */           public Iterator<Map.Entry<K, V>> iterator() {
/* 237 */             return new AbstractIndexedListIterator<Map.Entry<K, V>>(size())
/*     */               {
/*     */                 protected Map.Entry<K, V> get(final int index) {
/* 240 */                   return new AbstractMapEntry<K, V>()
/*     */                     {
/*     */                       public K getKey() {
/* 243 */                         return (K)ArrayTable.ArrayMap.this.getKey(index);
/*     */                       }
/*     */ 
/*     */                       
/*     */                       public V getValue() {
/* 248 */                         return (V)ArrayTable.ArrayMap.this.getValue(index);
/*     */                       }
/*     */ 
/*     */                       
/*     */                       public V setValue(V value) {
/* 253 */                         return (V)ArrayTable.ArrayMap.this.setValue(index, value);
/*     */                       }
/*     */                     };
/*     */                 }
/*     */               };
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean containsKey(@Nullable Object key) {
/* 266 */       return this.keyIndex.containsKey(key);
/*     */     }
/*     */ 
/*     */     
/*     */     public V get(@Nullable Object key) {
/* 271 */       Integer index = this.keyIndex.get(key);
/* 272 */       if (index == null) {
/* 273 */         return null;
/*     */       }
/* 275 */       return getValue(index.intValue());
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public V put(K key, V value) {
/* 281 */       Integer index = this.keyIndex.get(key);
/* 282 */       if (index == null) {
/* 283 */         String str1 = String.valueOf(String.valueOf(getKeyRole())), str2 = String.valueOf(String.valueOf(key)), str3 = String.valueOf(String.valueOf(this.keyIndex.keySet())); throw new IllegalArgumentException((new StringBuilder(9 + str1.length() + str2.length() + str3.length())).append(str1).append(" ").append(str2).append(" not in ").append(str3).toString());
/*     */       } 
/*     */       
/* 286 */       return setValue(index.intValue(), value);
/*     */     }
/*     */ 
/*     */     
/*     */     public V remove(Object key) {
/* 291 */       throw new UnsupportedOperationException();
/*     */     } abstract String getKeyRole();
/*     */     @Nullable
/*     */     abstract V getValue(int param1Int);
/*     */     public void clear() {
/* 296 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     abstract V setValue(int param1Int, V param1V);
/*     */   }
/*     */   
/*     */   public ImmutableList<R> rowKeyList() {
/* 305 */     return this.rowList;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableList<C> columnKeyList() {
/* 313 */     return this.columnList;
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
/*     */   public V at(int rowIndex, int columnIndex) {
/* 332 */     Preconditions.checkElementIndex(rowIndex, this.rowList.size());
/* 333 */     Preconditions.checkElementIndex(columnIndex, this.columnList.size());
/* 334 */     return this.array[rowIndex][columnIndex];
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
/*     */   public V set(int rowIndex, int columnIndex, @Nullable V value) {
/* 354 */     Preconditions.checkElementIndex(rowIndex, this.rowList.size());
/* 355 */     Preconditions.checkElementIndex(columnIndex, this.columnList.size());
/* 356 */     V oldValue = this.array[rowIndex][columnIndex];
/* 357 */     this.array[rowIndex][columnIndex] = value;
/* 358 */     return oldValue;
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
/*     */   @GwtIncompatible("reflection")
/*     */   public V[][] toArray(Class<V> valueClass) {
/* 375 */     V[][] copy = (V[][])Array.newInstance(valueClass, new int[] { this.rowList.size(), this.columnList.size() });
/*     */     
/* 377 */     for (int i = 0; i < this.rowList.size(); i++) {
/* 378 */       System.arraycopy(this.array[i], 0, copy[i], 0, (this.array[i]).length);
/*     */     }
/* 380 */     return copy;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void clear() {
/* 391 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void eraseAll() {
/* 399 */     for (V[] row : this.array) {
/* 400 */       Arrays.fill((Object[])row, (Object)null);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean contains(@Nullable Object rowKey, @Nullable Object columnKey) {
/* 410 */     return (containsRow(rowKey) && containsColumn(columnKey));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean containsColumn(@Nullable Object columnKey) {
/* 419 */     return this.columnKeyToIndex.containsKey(columnKey);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean containsRow(@Nullable Object rowKey) {
/* 428 */     return this.rowKeyToIndex.containsKey(rowKey);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsValue(@Nullable Object value) {
/* 433 */     for (V[] row : this.array) {
/* 434 */       for (V element : row) {
/* 435 */         if (Objects.equal(value, element)) {
/* 436 */           return true;
/*     */         }
/*     */       } 
/*     */     } 
/* 440 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public V get(@Nullable Object rowKey, @Nullable Object columnKey) {
/* 445 */     Integer rowIndex = this.rowKeyToIndex.get(rowKey);
/* 446 */     Integer columnIndex = this.columnKeyToIndex.get(columnKey);
/* 447 */     return (rowIndex == null || columnIndex == null) ? null : at(rowIndex.intValue(), columnIndex.intValue());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 456 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public V put(R rowKey, C columnKey, @Nullable V value) {
/* 467 */     Preconditions.checkNotNull(rowKey);
/* 468 */     Preconditions.checkNotNull(columnKey);
/* 469 */     Integer rowIndex = this.rowKeyToIndex.get(rowKey);
/* 470 */     Preconditions.checkArgument((rowIndex != null), "Row %s not in %s", new Object[] { rowKey, this.rowList });
/* 471 */     Integer columnIndex = this.columnKeyToIndex.get(columnKey);
/* 472 */     Preconditions.checkArgument((columnIndex != null), "Column %s not in %s", new Object[] { columnKey, this.columnList });
/*     */     
/* 474 */     return set(rowIndex.intValue(), columnIndex.intValue(), value);
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
/*     */   public void putAll(Table<? extends R, ? extends C, ? extends V> table) {
/* 495 */     super.putAll(table);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public V remove(Object rowKey, Object columnKey) {
/* 506 */     throw new UnsupportedOperationException();
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
/*     */   public V erase(@Nullable Object rowKey, @Nullable Object columnKey) {
/* 523 */     Integer rowIndex = this.rowKeyToIndex.get(rowKey);
/* 524 */     Integer columnIndex = this.columnKeyToIndex.get(columnKey);
/* 525 */     if (rowIndex == null || columnIndex == null) {
/* 526 */       return null;
/*     */     }
/* 528 */     return set(rowIndex.intValue(), columnIndex.intValue(), null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int size() {
/* 535 */     return this.rowList.size() * this.columnList.size();
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
/*     */   public Set<Table.Cell<R, C, V>> cellSet() {
/* 553 */     return super.cellSet();
/*     */   }
/*     */ 
/*     */   
/*     */   Iterator<Table.Cell<R, C, V>> cellIterator() {
/* 558 */     return new AbstractIndexedListIterator<Table.Cell<R, C, V>>(size()) {
/*     */         protected Table.Cell<R, C, V> get(final int index) {
/* 560 */           return new Tables.AbstractCell<R, C, V>() {
/* 561 */               final int rowIndex = index / ArrayTable.this.columnList.size();
/* 562 */               final int columnIndex = index % ArrayTable.this.columnList.size();
/*     */               
/*     */               public R getRowKey() {
/* 565 */                 return (R)ArrayTable.this.rowList.get(this.rowIndex);
/*     */               }
/*     */               
/*     */               public C getColumnKey() {
/* 569 */                 return (C)ArrayTable.this.columnList.get(this.columnIndex);
/*     */               }
/*     */               
/*     */               public V getValue() {
/* 573 */                 return (V)ArrayTable.this.at(this.rowIndex, this.columnIndex);
/*     */               }
/*     */             };
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
/*     */   public Map<R, V> column(C columnKey) {
/* 594 */     Preconditions.checkNotNull(columnKey);
/* 595 */     Integer columnIndex = this.columnKeyToIndex.get(columnKey);
/* 596 */     return (columnIndex == null) ? ImmutableMap.<R, V>of() : new Column(columnIndex.intValue());
/*     */   }
/*     */   
/*     */   private class Column
/*     */     extends ArrayMap<R, V> {
/*     */     final int columnIndex;
/*     */     
/*     */     Column(int columnIndex) {
/* 604 */       super(ArrayTable.this.rowKeyToIndex);
/* 605 */       this.columnIndex = columnIndex;
/*     */     }
/*     */ 
/*     */     
/*     */     String getKeyRole() {
/* 610 */       return "Row";
/*     */     }
/*     */ 
/*     */     
/*     */     V getValue(int index) {
/* 615 */       return (V)ArrayTable.this.at(index, this.columnIndex);
/*     */     }
/*     */ 
/*     */     
/*     */     V setValue(int index, V newValue) {
/* 620 */       return (V)ArrayTable.this.set(index, this.columnIndex, newValue);
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
/*     */   public ImmutableSet<C> columnKeySet() {
/* 632 */     return this.columnKeyToIndex.keySet();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<C, Map<R, V>> columnMap() {
/* 639 */     ColumnMap map = this.columnMap;
/* 640 */     return (map == null) ? (this.columnMap = new ColumnMap()) : map;
/*     */   }
/*     */   
/*     */   private class ColumnMap extends ArrayMap<C, Map<R, V>> {
/*     */     private ColumnMap() {
/* 645 */       super(ArrayTable.this.columnKeyToIndex);
/*     */     }
/*     */ 
/*     */     
/*     */     String getKeyRole() {
/* 650 */       return "Column";
/*     */     }
/*     */ 
/*     */     
/*     */     Map<R, V> getValue(int index) {
/* 655 */       return new ArrayTable.Column(index);
/*     */     }
/*     */ 
/*     */     
/*     */     Map<R, V> setValue(int index, Map<R, V> newValue) {
/* 660 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     
/*     */     public Map<R, V> put(C key, Map<R, V> value) {
/* 665 */       throw new UnsupportedOperationException();
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
/*     */   public Map<C, V> row(R rowKey) {
/* 684 */     Preconditions.checkNotNull(rowKey);
/* 685 */     Integer rowIndex = this.rowKeyToIndex.get(rowKey);
/* 686 */     return (rowIndex == null) ? ImmutableMap.<C, V>of() : new Row(rowIndex.intValue());
/*     */   }
/*     */   
/*     */   private class Row extends ArrayMap<C, V> {
/*     */     final int rowIndex;
/*     */     
/*     */     Row(int rowIndex) {
/* 693 */       super(ArrayTable.this.columnKeyToIndex);
/* 694 */       this.rowIndex = rowIndex;
/*     */     }
/*     */ 
/*     */     
/*     */     String getKeyRole() {
/* 699 */       return "Column";
/*     */     }
/*     */ 
/*     */     
/*     */     V getValue(int index) {
/* 704 */       return (V)ArrayTable.this.at(this.rowIndex, index);
/*     */     }
/*     */ 
/*     */     
/*     */     V setValue(int index, V newValue) {
/* 709 */       return (V)ArrayTable.this.set(this.rowIndex, index, newValue);
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
/*     */   public ImmutableSet<R> rowKeySet() {
/* 721 */     return this.rowKeyToIndex.keySet();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<R, Map<C, V>> rowMap() {
/* 728 */     RowMap map = this.rowMap;
/* 729 */     return (map == null) ? (this.rowMap = new RowMap()) : map;
/*     */   }
/*     */   
/*     */   private class RowMap extends ArrayMap<R, Map<C, V>> {
/*     */     private RowMap() {
/* 734 */       super(ArrayTable.this.rowKeyToIndex);
/*     */     }
/*     */ 
/*     */     
/*     */     String getKeyRole() {
/* 739 */       return "Row";
/*     */     }
/*     */ 
/*     */     
/*     */     Map<C, V> getValue(int index) {
/* 744 */       return new ArrayTable.Row(index);
/*     */     }
/*     */ 
/*     */     
/*     */     Map<C, V> setValue(int index, Map<C, V> newValue) {
/* 749 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     
/*     */     public Map<C, V> put(R key, Map<C, V> value) {
/* 754 */       throw new UnsupportedOperationException();
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
/*     */   public Collection<V> values() {
/* 769 */     return super.values();
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\collect\ArrayTable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */