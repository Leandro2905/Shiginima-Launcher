/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import javax.annotation.Nullable;
/*     */ import javax.annotation.concurrent.Immutable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ @Immutable
/*     */ final class DenseImmutableTable<R, C, V>
/*     */   extends RegularImmutableTable<R, C, V>
/*     */ {
/*     */   private final ImmutableMap<R, Integer> rowKeyToIndex;
/*     */   private final ImmutableMap<C, Integer> columnKeyToIndex;
/*     */   private final ImmutableMap<R, Map<C, V>> rowMap;
/*     */   private final ImmutableMap<C, Map<R, V>> columnMap;
/*     */   private final int[] rowCounts;
/*     */   private final int[] columnCounts;
/*     */   private final V[][] values;
/*     */   private final int[] iterationOrderRow;
/*     */   private final int[] iterationOrderColumn;
/*     */   
/*     */   private static <E> ImmutableMap<E, Integer> makeIndex(ImmutableSet<E> set) {
/*  44 */     ImmutableMap.Builder<E, Integer> indexBuilder = ImmutableMap.builder();
/*  45 */     int i = 0;
/*  46 */     for (E key : set) {
/*  47 */       indexBuilder.put(key, Integer.valueOf(i));
/*  48 */       i++;
/*     */     } 
/*  50 */     return indexBuilder.build();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   DenseImmutableTable(ImmutableList<Table.Cell<R, C, V>> cellList, ImmutableSet<R> rowSpace, ImmutableSet<C> columnSpace) {
/*  56 */     V[][] array = (V[][])new Object[rowSpace.size()][columnSpace.size()];
/*  57 */     this.values = array;
/*  58 */     this.rowKeyToIndex = makeIndex(rowSpace);
/*  59 */     this.columnKeyToIndex = makeIndex(columnSpace);
/*  60 */     this.rowCounts = new int[this.rowKeyToIndex.size()];
/*  61 */     this.columnCounts = new int[this.columnKeyToIndex.size()];
/*  62 */     int[] iterationOrderRow = new int[cellList.size()];
/*  63 */     int[] iterationOrderColumn = new int[cellList.size()];
/*  64 */     for (int i = 0; i < cellList.size(); i++) {
/*  65 */       Table.Cell<R, C, V> cell = cellList.get(i);
/*  66 */       R rowKey = cell.getRowKey();
/*  67 */       C columnKey = cell.getColumnKey();
/*  68 */       int rowIndex = ((Integer)this.rowKeyToIndex.get(rowKey)).intValue();
/*  69 */       int columnIndex = ((Integer)this.columnKeyToIndex.get(columnKey)).intValue();
/*  70 */       V existingValue = this.values[rowIndex][columnIndex];
/*  71 */       Preconditions.checkArgument((existingValue == null), "duplicate key: (%s, %s)", new Object[] { rowKey, columnKey });
/*  72 */       this.values[rowIndex][columnIndex] = cell.getValue();
/*  73 */       this.rowCounts[rowIndex] = this.rowCounts[rowIndex] + 1;
/*  74 */       this.columnCounts[columnIndex] = this.columnCounts[columnIndex] + 1;
/*  75 */       iterationOrderRow[i] = rowIndex;
/*  76 */       iterationOrderColumn[i] = columnIndex;
/*     */     } 
/*  78 */     this.iterationOrderRow = iterationOrderRow;
/*  79 */     this.iterationOrderColumn = iterationOrderColumn;
/*  80 */     this.rowMap = new RowMap();
/*  81 */     this.columnMap = new ColumnMap();
/*     */   }
/*     */ 
/*     */   
/*     */   private static abstract class ImmutableArrayMap<K, V>
/*     */     extends ImmutableMap<K, V>
/*     */   {
/*     */     private final int size;
/*     */     
/*     */     ImmutableArrayMap(int size) {
/*  91 */       this.size = size;
/*     */     }
/*     */ 
/*     */     
/*     */     abstract ImmutableMap<K, Integer> keyToIndex();
/*     */     
/*     */     private boolean isFull() {
/*  98 */       return (this.size == keyToIndex().size());
/*     */     }
/*     */     
/*     */     K getKey(int index) {
/* 102 */       return keyToIndex().keySet().asList().get(index);
/*     */     }
/*     */     
/*     */     @Nullable
/*     */     abstract V getValue(int param1Int);
/*     */     
/*     */     ImmutableSet<K> createKeySet() {
/* 109 */       return isFull() ? keyToIndex().keySet() : super.createKeySet();
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 114 */       return this.size;
/*     */     }
/*     */ 
/*     */     
/*     */     public V get(@Nullable Object key) {
/* 119 */       Integer keyIndex = keyToIndex().get(key);
/* 120 */       return (keyIndex == null) ? null : getValue(keyIndex.intValue());
/*     */     }
/*     */ 
/*     */     
/*     */     ImmutableSet<Map.Entry<K, V>> createEntrySet() {
/* 125 */       return new ImmutableMapEntrySet<K, V>() {
/*     */           ImmutableMap<K, V> map() {
/* 127 */             return DenseImmutableTable.ImmutableArrayMap.this;
/*     */           }
/*     */ 
/*     */           
/*     */           public UnmodifiableIterator<Map.Entry<K, V>> iterator() {
/* 132 */             return new AbstractIterator<Map.Entry<K, V>>() {
/* 133 */                 private int index = -1;
/* 134 */                 private final int maxIndex = DenseImmutableTable.ImmutableArrayMap.this.keyToIndex().size();
/*     */ 
/*     */                 
/*     */                 protected Map.Entry<K, V> computeNext() {
/* 138 */                   this.index++; for (; this.index < this.maxIndex; this.index++) {
/* 139 */                     V value = (V)DenseImmutableTable.ImmutableArrayMap.this.getValue(this.index);
/* 140 */                     if (value != null) {
/* 141 */                       return Maps.immutableEntry((K)DenseImmutableTable.ImmutableArrayMap.this.getKey(this.index), value);
/*     */                     }
/*     */                   } 
/* 144 */                   return endOfData();
/*     */                 }
/*     */               };
/*     */           }
/*     */         };
/*     */     }
/*     */   }
/*     */   
/*     */   private final class Row extends ImmutableArrayMap<C, V> {
/*     */     private final int rowIndex;
/*     */     
/*     */     Row(int rowIndex) {
/* 156 */       super(DenseImmutableTable.this.rowCounts[rowIndex]);
/* 157 */       this.rowIndex = rowIndex;
/*     */     }
/*     */ 
/*     */     
/*     */     ImmutableMap<C, Integer> keyToIndex() {
/* 162 */       return DenseImmutableTable.this.columnKeyToIndex;
/*     */     }
/*     */ 
/*     */     
/*     */     V getValue(int keyIndex) {
/* 167 */       return (V)DenseImmutableTable.this.values[this.rowIndex][keyIndex];
/*     */     }
/*     */ 
/*     */     
/*     */     boolean isPartialView() {
/* 172 */       return true;
/*     */     }
/*     */   }
/*     */   
/*     */   private final class Column extends ImmutableArrayMap<R, V> {
/*     */     private final int columnIndex;
/*     */     
/*     */     Column(int columnIndex) {
/* 180 */       super(DenseImmutableTable.this.columnCounts[columnIndex]);
/* 181 */       this.columnIndex = columnIndex;
/*     */     }
/*     */ 
/*     */     
/*     */     ImmutableMap<R, Integer> keyToIndex() {
/* 186 */       return DenseImmutableTable.this.rowKeyToIndex;
/*     */     }
/*     */ 
/*     */     
/*     */     V getValue(int keyIndex) {
/* 191 */       return (V)DenseImmutableTable.this.values[keyIndex][this.columnIndex];
/*     */     }
/*     */ 
/*     */     
/*     */     boolean isPartialView() {
/* 196 */       return true;
/*     */     }
/*     */   }
/*     */   
/*     */   private final class RowMap extends ImmutableArrayMap<R, Map<C, V>> {
/*     */     private RowMap() {
/* 202 */       super(DenseImmutableTable.this.rowCounts.length);
/*     */     }
/*     */ 
/*     */     
/*     */     ImmutableMap<R, Integer> keyToIndex() {
/* 207 */       return DenseImmutableTable.this.rowKeyToIndex;
/*     */     }
/*     */ 
/*     */     
/*     */     Map<C, V> getValue(int keyIndex) {
/* 212 */       return new DenseImmutableTable.Row(keyIndex);
/*     */     }
/*     */ 
/*     */     
/*     */     boolean isPartialView() {
/* 217 */       return false;
/*     */     }
/*     */   }
/*     */   
/*     */   private final class ColumnMap extends ImmutableArrayMap<C, Map<R, V>> {
/*     */     private ColumnMap() {
/* 223 */       super(DenseImmutableTable.this.columnCounts.length);
/*     */     }
/*     */ 
/*     */     
/*     */     ImmutableMap<C, Integer> keyToIndex() {
/* 228 */       return DenseImmutableTable.this.columnKeyToIndex;
/*     */     }
/*     */ 
/*     */     
/*     */     Map<R, V> getValue(int keyIndex) {
/* 233 */       return new DenseImmutableTable.Column(keyIndex);
/*     */     }
/*     */ 
/*     */     
/*     */     boolean isPartialView() {
/* 238 */       return false;
/*     */     }
/*     */   }
/*     */   
/*     */   public ImmutableMap<C, Map<R, V>> columnMap() {
/* 243 */     return this.columnMap;
/*     */   }
/*     */ 
/*     */   
/*     */   public ImmutableMap<R, Map<C, V>> rowMap() {
/* 248 */     return this.rowMap;
/*     */   }
/*     */ 
/*     */   
/*     */   public V get(@Nullable Object rowKey, @Nullable Object columnKey) {
/* 253 */     Integer rowIndex = this.rowKeyToIndex.get(rowKey);
/* 254 */     Integer columnIndex = this.columnKeyToIndex.get(columnKey);
/* 255 */     return (rowIndex == null || columnIndex == null) ? null : this.values[rowIndex.intValue()][columnIndex.intValue()];
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int size() {
/* 261 */     return this.iterationOrderRow.length;
/*     */   }
/*     */ 
/*     */   
/*     */   Table.Cell<R, C, V> getCell(int index) {
/* 266 */     int rowIndex = this.iterationOrderRow[index];
/* 267 */     int columnIndex = this.iterationOrderColumn[index];
/* 268 */     R rowKey = rowKeySet().asList().get(rowIndex);
/* 269 */     C columnKey = columnKeySet().asList().get(columnIndex);
/* 270 */     V value = this.values[rowIndex][columnIndex];
/* 271 */     return cellOf(rowKey, columnKey, value);
/*     */   }
/*     */ 
/*     */   
/*     */   V getValue(int index) {
/* 276 */     return this.values[this.iterationOrderRow[index]][this.iterationOrderColumn[index]];
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\collect\DenseImmutableTable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */