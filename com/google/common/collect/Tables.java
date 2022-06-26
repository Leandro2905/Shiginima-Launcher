/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Supplier;
/*     */ import java.io.Serializable;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.SortedMap;
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
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public final class Tables
/*     */ {
/*     */   public static <R, C, V> Table.Cell<R, C, V> immutableCell(@Nullable R rowKey, @Nullable C columnKey, @Nullable V value) {
/*  67 */     return new ImmutableCell<R, C, V>(rowKey, columnKey, value);
/*     */   }
/*     */   
/*     */   static final class ImmutableCell<R, C, V>
/*     */     extends AbstractCell<R, C, V> implements Serializable {
/*     */     private final R rowKey;
/*     */     private final C columnKey;
/*     */     private final V value;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     ImmutableCell(@Nullable R rowKey, @Nullable C columnKey, @Nullable V value) {
/*  78 */       this.rowKey = rowKey;
/*  79 */       this.columnKey = columnKey;
/*  80 */       this.value = value;
/*     */     }
/*     */ 
/*     */     
/*     */     public R getRowKey() {
/*  85 */       return this.rowKey;
/*     */     }
/*     */     
/*     */     public C getColumnKey() {
/*  89 */       return this.columnKey;
/*     */     }
/*     */     
/*     */     public V getValue() {
/*  93 */       return this.value;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static abstract class AbstractCell<R, C, V>
/*     */     implements Table.Cell<R, C, V>
/*     */   {
/*     */     public boolean equals(Object obj) {
/* 104 */       if (obj == this) {
/* 105 */         return true;
/*     */       }
/* 107 */       if (obj instanceof Table.Cell) {
/* 108 */         Table.Cell<?, ?, ?> other = (Table.Cell<?, ?, ?>)obj;
/* 109 */         return (Objects.equal(getRowKey(), other.getRowKey()) && Objects.equal(getColumnKey(), other.getColumnKey()) && Objects.equal(getValue(), other.getValue()));
/*     */       } 
/*     */ 
/*     */       
/* 113 */       return false;
/*     */     }
/*     */     
/*     */     public int hashCode() {
/* 117 */       return Objects.hashCode(new Object[] { getRowKey(), getColumnKey(), getValue() });
/*     */     }
/*     */     
/*     */     public String toString() {
/* 121 */       String str1 = String.valueOf(String.valueOf(getRowKey())), str2 = String.valueOf(String.valueOf(getColumnKey())), str3 = String.valueOf(String.valueOf(getValue())); return (new StringBuilder(4 + str1.length() + str2.length() + str3.length())).append("(").append(str1).append(",").append(str2).append(")=").append(str3).toString();
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
/*     */   public static <R, C, V> Table<C, R, V> transpose(Table<R, C, V> table) {
/* 140 */     return (table instanceof TransposeTable) ? ((TransposeTable)table).original : new TransposeTable<C, R, V>(table);
/*     */   }
/*     */   
/*     */   private static class TransposeTable<C, R, V>
/*     */     extends AbstractTable<C, R, V>
/*     */   {
/*     */     final Table<R, C, V> original;
/*     */     
/*     */     TransposeTable(Table<R, C, V> original) {
/* 149 */       this.original = (Table<R, C, V>)Preconditions.checkNotNull(original);
/*     */     }
/*     */ 
/*     */     
/*     */     public void clear() {
/* 154 */       this.original.clear();
/*     */     }
/*     */ 
/*     */     
/*     */     public Map<C, V> column(R columnKey) {
/* 159 */       return this.original.row(columnKey);
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<R> columnKeySet() {
/* 164 */       return this.original.rowKeySet();
/*     */     }
/*     */ 
/*     */     
/*     */     public Map<R, Map<C, V>> columnMap() {
/* 169 */       return this.original.rowMap();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean contains(@Nullable Object rowKey, @Nullable Object columnKey) {
/* 175 */       return this.original.contains(columnKey, rowKey);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean containsColumn(@Nullable Object columnKey) {
/* 180 */       return this.original.containsRow(columnKey);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean containsRow(@Nullable Object rowKey) {
/* 185 */       return this.original.containsColumn(rowKey);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean containsValue(@Nullable Object value) {
/* 190 */       return this.original.containsValue(value);
/*     */     }
/*     */ 
/*     */     
/*     */     public V get(@Nullable Object rowKey, @Nullable Object columnKey) {
/* 195 */       return this.original.get(columnKey, rowKey);
/*     */     }
/*     */ 
/*     */     
/*     */     public V put(C rowKey, R columnKey, V value) {
/* 200 */       return this.original.put(columnKey, rowKey, value);
/*     */     }
/*     */ 
/*     */     
/*     */     public void putAll(Table<? extends C, ? extends R, ? extends V> table) {
/* 205 */       this.original.putAll(Tables.transpose(table));
/*     */     }
/*     */ 
/*     */     
/*     */     public V remove(@Nullable Object rowKey, @Nullable Object columnKey) {
/* 210 */       return this.original.remove(columnKey, rowKey);
/*     */     }
/*     */ 
/*     */     
/*     */     public Map<R, V> row(C rowKey) {
/* 215 */       return this.original.column(rowKey);
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<C> rowKeySet() {
/* 220 */       return this.original.columnKeySet();
/*     */     }
/*     */ 
/*     */     
/*     */     public Map<C, Map<R, V>> rowMap() {
/* 225 */       return this.original.columnMap();
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 230 */       return this.original.size();
/*     */     }
/*     */ 
/*     */     
/*     */     public Collection<V> values() {
/* 235 */       return this.original.values();
/*     */     }
/*     */ 
/*     */     
/* 239 */     private static final Function<Table.Cell<?, ?, ?>, Table.Cell<?, ?, ?>> TRANSPOSE_CELL = new Function<Table.Cell<?, ?, ?>, Table.Cell<?, ?, ?>>()
/*     */       {
/*     */         public Table.Cell<?, ?, ?> apply(Table.Cell<?, ?, ?> cell)
/*     */         {
/* 243 */           return Tables.immutableCell(cell.getColumnKey(), cell.getRowKey(), cell.getValue());
/*     */         }
/*     */       };
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     Iterator<Table.Cell<C, R, V>> cellIterator() {
/* 251 */       return (Iterator)Iterators.transform(this.original.cellSet().iterator(), TRANSPOSE_CELL);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */   public static <R, C, V> Table<R, C, V> newCustomTable(Map<R, Map<C, V>> backingMap, Supplier<? extends Map<C, V>> factory) {
/* 299 */     Preconditions.checkArgument(backingMap.isEmpty());
/* 300 */     Preconditions.checkNotNull(factory);
/*     */     
/* 302 */     return new StandardTable<R, C, V>(backingMap, factory);
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
/*     */   @Beta
/*     */   public static <R, C, V1, V2> Table<R, C, V2> transformValues(Table<R, C, V1> fromTable, Function<? super V1, V2> function) {
/* 334 */     return new TransformedTable<R, C, V1, V2>(fromTable, function);
/*     */   }
/*     */   
/*     */   private static class TransformedTable<R, C, V1, V2>
/*     */     extends AbstractTable<R, C, V2>
/*     */   {
/*     */     final Table<R, C, V1> fromTable;
/*     */     final Function<? super V1, V2> function;
/*     */     
/*     */     TransformedTable(Table<R, C, V1> fromTable, Function<? super V1, V2> function) {
/* 344 */       this.fromTable = (Table<R, C, V1>)Preconditions.checkNotNull(fromTable);
/* 345 */       this.function = (Function<? super V1, V2>)Preconditions.checkNotNull(function);
/*     */     }
/*     */     
/*     */     public boolean contains(Object rowKey, Object columnKey) {
/* 349 */       return this.fromTable.contains(rowKey, columnKey);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public V2 get(Object rowKey, Object columnKey) {
/* 355 */       return contains(rowKey, columnKey) ? (V2)this.function.apply(this.fromTable.get(rowKey, columnKey)) : null;
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 360 */       return this.fromTable.size();
/*     */     }
/*     */     
/*     */     public void clear() {
/* 364 */       this.fromTable.clear();
/*     */     }
/*     */     
/*     */     public V2 put(R rowKey, C columnKey, V2 value) {
/* 368 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     
/*     */     public void putAll(Table<? extends R, ? extends C, ? extends V2> table) {
/* 373 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public V2 remove(Object rowKey, Object columnKey) {
/* 377 */       return contains(rowKey, columnKey) ? (V2)this.function.apply(this.fromTable.remove(rowKey, columnKey)) : null;
/*     */     }
/*     */ 
/*     */     
/*     */     public Map<C, V2> row(R rowKey) {
/* 382 */       return Maps.transformValues(this.fromTable.row(rowKey), this.function);
/*     */     }
/*     */     
/*     */     public Map<R, V2> column(C columnKey) {
/* 386 */       return Maps.transformValues(this.fromTable.column(columnKey), this.function);
/*     */     }
/*     */     
/*     */     Function<Table.Cell<R, C, V1>, Table.Cell<R, C, V2>> cellFunction() {
/* 390 */       return new Function<Table.Cell<R, C, V1>, Table.Cell<R, C, V2>>() {
/*     */           public Table.Cell<R, C, V2> apply(Table.Cell<R, C, V1> cell) {
/* 392 */             return Tables.immutableCell(cell.getRowKey(), cell.getColumnKey(), (V2)Tables.TransformedTable.this.function.apply(cell.getValue()));
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     Iterator<Table.Cell<R, C, V2>> cellIterator() {
/* 401 */       return Iterators.transform(this.fromTable.cellSet().iterator(), cellFunction());
/*     */     }
/*     */     
/*     */     public Set<R> rowKeySet() {
/* 405 */       return this.fromTable.rowKeySet();
/*     */     }
/*     */     
/*     */     public Set<C> columnKeySet() {
/* 409 */       return this.fromTable.columnKeySet();
/*     */     }
/*     */ 
/*     */     
/*     */     Collection<V2> createValues() {
/* 414 */       return Collections2.transform(this.fromTable.values(), this.function);
/*     */     }
/*     */     
/*     */     public Map<R, Map<C, V2>> rowMap() {
/* 418 */       Function<Map<C, V1>, Map<C, V2>> rowFunction = new Function<Map<C, V1>, Map<C, V2>>()
/*     */         {
/*     */           public Map<C, V2> apply(Map<C, V1> row) {
/* 421 */             return Maps.transformValues(row, Tables.TransformedTable.this.function);
/*     */           }
/*     */         };
/* 424 */       return Maps.transformValues(this.fromTable.rowMap(), rowFunction);
/*     */     }
/*     */     
/*     */     public Map<C, Map<R, V2>> columnMap() {
/* 428 */       Function<Map<R, V1>, Map<R, V2>> columnFunction = new Function<Map<R, V1>, Map<R, V2>>()
/*     */         {
/*     */           public Map<R, V2> apply(Map<R, V1> column) {
/* 431 */             return Maps.transformValues(column, Tables.TransformedTable.this.function);
/*     */           }
/*     */         };
/* 434 */       return Maps.transformValues(this.fromTable.columnMap(), columnFunction);
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
/*     */   public static <R, C, V> Table<R, C, V> unmodifiableTable(Table<? extends R, ? extends C, ? extends V> table) {
/* 455 */     return new UnmodifiableTable<R, C, V>(table);
/*     */   }
/*     */   
/*     */   private static class UnmodifiableTable<R, C, V> extends ForwardingTable<R, C, V> implements Serializable {
/*     */     final Table<? extends R, ? extends C, ? extends V> delegate;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     UnmodifiableTable(Table<? extends R, ? extends C, ? extends V> delegate) {
/* 463 */       this.delegate = (Table<? extends R, ? extends C, ? extends V>)Preconditions.checkNotNull(delegate);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected Table<R, C, V> delegate() {
/* 469 */       return (Table)this.delegate;
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<Table.Cell<R, C, V>> cellSet() {
/* 474 */       return Collections.unmodifiableSet(super.cellSet());
/*     */     }
/*     */ 
/*     */     
/*     */     public void clear() {
/* 479 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     
/*     */     public Map<R, V> column(@Nullable C columnKey) {
/* 484 */       return Collections.unmodifiableMap(super.column(columnKey));
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<C> columnKeySet() {
/* 489 */       return Collections.unmodifiableSet(super.columnKeySet());
/*     */     }
/*     */ 
/*     */     
/*     */     public Map<C, Map<R, V>> columnMap() {
/* 494 */       Function<Map<R, V>, Map<R, V>> wrapper = Tables.unmodifiableWrapper();
/* 495 */       return Collections.unmodifiableMap(Maps.transformValues(super.columnMap(), wrapper));
/*     */     }
/*     */ 
/*     */     
/*     */     public V put(@Nullable R rowKey, @Nullable C columnKey, @Nullable V value) {
/* 500 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     
/*     */     public void putAll(Table<? extends R, ? extends C, ? extends V> table) {
/* 505 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     
/*     */     public V remove(@Nullable Object rowKey, @Nullable Object columnKey) {
/* 510 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     
/*     */     public Map<C, V> row(@Nullable R rowKey) {
/* 515 */       return Collections.unmodifiableMap(super.row(rowKey));
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<R> rowKeySet() {
/* 520 */       return Collections.unmodifiableSet(super.rowKeySet());
/*     */     }
/*     */ 
/*     */     
/*     */     public Map<R, Map<C, V>> rowMap() {
/* 525 */       Function<Map<C, V>, Map<C, V>> wrapper = Tables.unmodifiableWrapper();
/* 526 */       return Collections.unmodifiableMap(Maps.transformValues(super.rowMap(), wrapper));
/*     */     }
/*     */ 
/*     */     
/*     */     public Collection<V> values() {
/* 531 */       return Collections.unmodifiableCollection(super.values());
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
/*     */   public static <R, C, V> RowSortedTable<R, C, V> unmodifiableRowSortedTable(RowSortedTable<R, ? extends C, ? extends V> table) {
/* 557 */     return new UnmodifiableRowSortedMap<R, C, V>(table);
/*     */   }
/*     */   
/*     */   static final class UnmodifiableRowSortedMap<R, C, V> extends UnmodifiableTable<R, C, V> implements RowSortedTable<R, C, V> {
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     public UnmodifiableRowSortedMap(RowSortedTable<R, ? extends C, ? extends V> delegate) {
/* 564 */       super(delegate);
/*     */     }
/*     */ 
/*     */     
/*     */     protected RowSortedTable<R, C, V> delegate() {
/* 569 */       return (RowSortedTable<R, C, V>)super.delegate();
/*     */     }
/*     */ 
/*     */     
/*     */     public SortedMap<R, Map<C, V>> rowMap() {
/* 574 */       Function<Map<C, V>, Map<C, V>> wrapper = Tables.unmodifiableWrapper();
/* 575 */       return Collections.unmodifiableSortedMap(Maps.transformValues(delegate().rowMap(), wrapper));
/*     */     }
/*     */ 
/*     */     
/*     */     public SortedSet<R> rowKeySet() {
/* 580 */       return Collections.unmodifiableSortedSet(delegate().rowKeySet());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static <K, V> Function<Map<K, V>, Map<K, V>> unmodifiableWrapper() {
/* 588 */     return (Function)UNMODIFIABLE_WRAPPER;
/*     */   }
/*     */   
/* 591 */   private static final Function<? extends Map<?, ?>, ? extends Map<?, ?>> UNMODIFIABLE_WRAPPER = new Function<Map<Object, Object>, Map<Object, Object>>()
/*     */     {
/*     */       public Map<Object, Object> apply(Map<Object, Object> input)
/*     */       {
/* 595 */         return Collections.unmodifiableMap(input);
/*     */       }
/*     */     };
/*     */   
/*     */   static boolean equalsImpl(Table<?, ?, ?> table, @Nullable Object obj) {
/* 600 */     if (obj == table)
/* 601 */       return true; 
/* 602 */     if (obj instanceof Table) {
/* 603 */       Table<?, ?, ?> that = (Table<?, ?, ?>)obj;
/* 604 */       return table.cellSet().equals(that.cellSet());
/*     */     } 
/* 606 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\collect\Tables.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */