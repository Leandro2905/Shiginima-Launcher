/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
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
/*     */ @GwtCompatible
/*     */ abstract class RegularImmutableTable<R, C, V>
/*     */   extends ImmutableTable<R, C, V>
/*     */ {
/*     */   final ImmutableSet<Table.Cell<R, C, V>> createCellSet() {
/*  41 */     return isEmpty() ? ImmutableSet.<Table.Cell<R, C, V>>of() : new CellSet();
/*     */   }
/*     */   
/*     */   private final class CellSet
/*     */     extends ImmutableSet<Table.Cell<R, C, V>> {
/*     */     public int size() {
/*  47 */       return RegularImmutableTable.this.size();
/*     */     }
/*     */     private CellSet() {}
/*     */     
/*     */     public UnmodifiableIterator<Table.Cell<R, C, V>> iterator() {
/*  52 */       return asList().iterator();
/*     */     }
/*     */ 
/*     */     
/*     */     ImmutableList<Table.Cell<R, C, V>> createAsList() {
/*  57 */       return new ImmutableAsList<Table.Cell<R, C, V>>()
/*     */         {
/*     */           public Table.Cell<R, C, V> get(int index) {
/*  60 */             return RegularImmutableTable.this.getCell(index);
/*     */           }
/*     */ 
/*     */           
/*     */           ImmutableCollection<Table.Cell<R, C, V>> delegateCollection() {
/*  65 */             return RegularImmutableTable.CellSet.this;
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean contains(@Nullable Object object) {
/*  72 */       if (object instanceof Table.Cell) {
/*  73 */         Table.Cell<?, ?, ?> cell = (Table.Cell<?, ?, ?>)object;
/*  74 */         Object value = RegularImmutableTable.this.get(cell.getRowKey(), cell.getColumnKey());
/*  75 */         return (value != null && value.equals(cell.getValue()));
/*     */       } 
/*  77 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     boolean isPartialView() {
/*  82 */       return false;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final ImmutableCollection<V> createValues() {
/*  90 */     return isEmpty() ? ImmutableList.<V>of() : new Values();
/*     */   }
/*     */   
/*     */   private final class Values extends ImmutableList<V> { private Values() {}
/*     */     
/*     */     public int size() {
/*  96 */       return RegularImmutableTable.this.size();
/*     */     }
/*     */ 
/*     */     
/*     */     public V get(int index) {
/* 101 */       return (V)RegularImmutableTable.this.getValue(index);
/*     */     }
/*     */ 
/*     */     
/*     */     boolean isPartialView() {
/* 106 */       return true;
/*     */     } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <R, C, V> RegularImmutableTable<R, C, V> forCells(List<Table.Cell<R, C, V>> cells, @Nullable final Comparator<? super R> rowComparator, @Nullable final Comparator<? super C> columnComparator) {
/* 114 */     Preconditions.checkNotNull(cells);
/* 115 */     if (rowComparator != null || columnComparator != null) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 123 */       Comparator<Table.Cell<R, C, V>> comparator = new Comparator<Table.Cell<R, C, V>>() {
/*     */           public int compare(Table.Cell<R, C, V> cell1, Table.Cell<R, C, V> cell2) {
/* 125 */             int rowCompare = (rowComparator == null) ? 0 : rowComparator.compare(cell1.getRowKey(), cell2.getRowKey());
/*     */             
/* 127 */             if (rowCompare != 0) {
/* 128 */               return rowCompare;
/*     */             }
/* 130 */             return (columnComparator == null) ? 0 : columnComparator.compare(cell1.getColumnKey(), cell2.getColumnKey());
/*     */           }
/*     */         };
/*     */       
/* 134 */       Collections.sort(cells, comparator);
/*     */     } 
/* 136 */     return forCellsInternal(cells, rowComparator, columnComparator);
/*     */   }
/*     */ 
/*     */   
/*     */   static <R, C, V> RegularImmutableTable<R, C, V> forCells(Iterable<Table.Cell<R, C, V>> cells) {
/* 141 */     return forCellsInternal(cells, (Comparator<? super R>)null, (Comparator<? super C>)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final <R, C, V> RegularImmutableTable<R, C, V> forCellsInternal(Iterable<Table.Cell<R, C, V>> cells, @Nullable Comparator<? super R> rowComparator, @Nullable Comparator<? super C> columnComparator) {
/* 152 */     ImmutableSet.Builder<R> rowSpaceBuilder = ImmutableSet.builder();
/* 153 */     ImmutableSet.Builder<C> columnSpaceBuilder = ImmutableSet.builder();
/* 154 */     ImmutableList<Table.Cell<R, C, V>> cellList = ImmutableList.copyOf(cells);
/* 155 */     for (Table.Cell<R, C, V> cell : cellList) {
/* 156 */       rowSpaceBuilder.add(cell.getRowKey());
/* 157 */       columnSpaceBuilder.add(cell.getColumnKey());
/*     */     } 
/*     */     
/* 160 */     ImmutableSet<R> rowSpace = rowSpaceBuilder.build();
/* 161 */     if (rowComparator != null) {
/* 162 */       List<R> rowList = Lists.newArrayList(rowSpace);
/* 163 */       Collections.sort(rowList, rowComparator);
/* 164 */       rowSpace = ImmutableSet.copyOf(rowList);
/*     */     } 
/* 166 */     ImmutableSet<C> columnSpace = columnSpaceBuilder.build();
/* 167 */     if (columnComparator != null) {
/* 168 */       List<C> columnList = Lists.newArrayList(columnSpace);
/* 169 */       Collections.sort(columnList, columnComparator);
/* 170 */       columnSpace = ImmutableSet.copyOf(columnList);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 175 */     return (cellList.size() > rowSpace.size() * columnSpace.size() / 2L) ? new DenseImmutableTable<R, C, V>(cellList, rowSpace, columnSpace) : new SparseImmutableTable<R, C, V>(cellList, rowSpace, columnSpace);
/*     */   }
/*     */   
/*     */   abstract Table.Cell<R, C, V> getCell(int paramInt);
/*     */   
/*     */   abstract V getValue(int paramInt);
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\collect\RegularImmutableTable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */