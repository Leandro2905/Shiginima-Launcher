/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Supplier;
/*     */ import java.io.Serializable;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Set;
/*     */ import java.util.SortedMap;
/*     */ import java.util.SortedSet;
/*     */ import java.util.TreeMap;
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
/*     */ @GwtCompatible(serializable = true)
/*     */ @Beta
/*     */ public class TreeBasedTable<R, C, V>
/*     */   extends StandardRowSortedTable<R, C, V>
/*     */ {
/*     */   private final Comparator<? super C> columnComparator;
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   private static class Factory<C, V>
/*     */     implements Supplier<TreeMap<C, V>>, Serializable
/*     */   {
/*     */     final Comparator<? super C> comparator;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     Factory(Comparator<? super C> comparator) {
/*  86 */       this.comparator = comparator;
/*     */     }
/*     */     
/*     */     public TreeMap<C, V> get() {
/*  90 */       return new TreeMap<C, V>(this.comparator);
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
/*     */   public static <R extends Comparable, C extends Comparable, V> TreeBasedTable<R, C, V> create() {
/* 106 */     return new TreeBasedTable<R, C, V>(Ordering.natural(), Ordering.natural());
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
/*     */   public static <R, C, V> TreeBasedTable<R, C, V> create(Comparator<? super R> rowComparator, Comparator<? super C> columnComparator) {
/* 120 */     Preconditions.checkNotNull(rowComparator);
/* 121 */     Preconditions.checkNotNull(columnComparator);
/* 122 */     return new TreeBasedTable<R, C, V>(rowComparator, columnComparator);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <R, C, V> TreeBasedTable<R, C, V> create(TreeBasedTable<R, C, ? extends V> table) {
/* 131 */     TreeBasedTable<R, C, V> result = new TreeBasedTable<R, C, V>(table.rowComparator(), table.columnComparator());
/*     */ 
/*     */     
/* 134 */     result.putAll(table);
/* 135 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   TreeBasedTable(Comparator<? super R> rowComparator, Comparator<? super C> columnComparator) {
/* 140 */     super(new TreeMap<R, Map<C, V>>(rowComparator), (Supplier)new Factory<C, Object>(columnComparator));
/*     */     
/* 142 */     this.columnComparator = columnComparator;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Comparator<? super R> rowComparator() {
/* 152 */     return rowKeySet().comparator();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Comparator<? super C> columnComparator() {
/* 160 */     return this.columnComparator;
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
/*     */   public SortedMap<C, V> row(R rowKey) {
/* 177 */     return new TreeRow(rowKey);
/*     */   }
/*     */   
/*     */   private class TreeRow extends StandardTable<R, C, V>.Row implements SortedMap<C, V> {
/*     */     @Nullable
/*     */     final C lowerBound;
/*     */     
/*     */     TreeRow(R rowKey) {
/* 185 */       this(rowKey, null, null);
/*     */     } @Nullable
/*     */     final C upperBound; transient SortedMap<C, V> wholeRow;
/*     */     TreeRow(@Nullable R rowKey, @Nullable C lowerBound, C upperBound) {
/* 189 */       super(rowKey);
/* 190 */       this.lowerBound = lowerBound;
/* 191 */       this.upperBound = upperBound;
/* 192 */       Preconditions.checkArgument((lowerBound == null || upperBound == null || compare(lowerBound, upperBound) <= 0));
/*     */     }
/*     */ 
/*     */     
/*     */     public SortedSet<C> keySet() {
/* 197 */       return new Maps.SortedKeySet<C, Object>(this);
/*     */     }
/*     */     
/*     */     public Comparator<? super C> comparator() {
/* 201 */       return TreeBasedTable.this.columnComparator();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     int compare(Object a, Object b) {
/* 207 */       Comparator<Object> cmp = (Comparator)comparator();
/* 208 */       return cmp.compare(a, b);
/*     */     }
/*     */     
/*     */     boolean rangeContains(@Nullable Object o) {
/* 212 */       return (o != null && (this.lowerBound == null || compare(this.lowerBound, o) <= 0) && (this.upperBound == null || compare(this.upperBound, o) > 0));
/*     */     }
/*     */ 
/*     */     
/*     */     public SortedMap<C, V> subMap(C fromKey, C toKey) {
/* 217 */       Preconditions.checkArgument((rangeContains(Preconditions.checkNotNull(fromKey)) && rangeContains(Preconditions.checkNotNull(toKey))));
/*     */       
/* 219 */       return new TreeRow(this.rowKey, fromKey, toKey);
/*     */     }
/*     */     
/*     */     public SortedMap<C, V> headMap(C toKey) {
/* 223 */       Preconditions.checkArgument(rangeContains(Preconditions.checkNotNull(toKey)));
/* 224 */       return new TreeRow(this.rowKey, this.lowerBound, toKey);
/*     */     }
/*     */     
/*     */     public SortedMap<C, V> tailMap(C fromKey) {
/* 228 */       Preconditions.checkArgument(rangeContains(Preconditions.checkNotNull(fromKey)));
/* 229 */       return new TreeRow(this.rowKey, fromKey, this.upperBound);
/*     */     }
/*     */     
/*     */     public C firstKey() {
/* 233 */       SortedMap<C, V> backing = backingRowMap();
/* 234 */       if (backing == null) {
/* 235 */         throw new NoSuchElementException();
/*     */       }
/* 237 */       return backingRowMap().firstKey();
/*     */     }
/*     */     
/*     */     public C lastKey() {
/* 241 */       SortedMap<C, V> backing = backingRowMap();
/* 242 */       if (backing == null) {
/* 243 */         throw new NoSuchElementException();
/*     */       }
/* 245 */       return backingRowMap().lastKey();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     SortedMap<C, V> wholeRow() {
/* 255 */       if (this.wholeRow == null || (this.wholeRow.isEmpty() && TreeBasedTable.this.backingMap.containsKey(this.rowKey)))
/*     */       {
/* 257 */         this.wholeRow = (SortedMap<C, V>)TreeBasedTable.this.backingMap.get(this.rowKey);
/*     */       }
/* 259 */       return this.wholeRow;
/*     */     }
/*     */ 
/*     */     
/*     */     SortedMap<C, V> backingRowMap() {
/* 264 */       return (SortedMap<C, V>)super.backingRowMap();
/*     */     }
/*     */ 
/*     */     
/*     */     SortedMap<C, V> computeBackingRowMap() {
/* 269 */       SortedMap<C, V> map = wholeRow();
/* 270 */       if (map != null) {
/* 271 */         if (this.lowerBound != null) {
/* 272 */           map = map.tailMap(this.lowerBound);
/*     */         }
/* 274 */         if (this.upperBound != null) {
/* 275 */           map = map.headMap(this.upperBound);
/*     */         }
/* 277 */         return map;
/*     */       } 
/* 279 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     void maintainEmptyInvariant() {
/* 284 */       if (wholeRow() != null && this.wholeRow.isEmpty()) {
/* 285 */         TreeBasedTable.this.backingMap.remove(this.rowKey);
/* 286 */         this.wholeRow = null;
/* 287 */         this.backingRowMap = null;
/*     */       } 
/*     */     }
/*     */     
/*     */     public boolean containsKey(Object key) {
/* 292 */       return (rangeContains(key) && super.containsKey(key));
/*     */     }
/*     */     
/*     */     public V put(C key, V value) {
/* 296 */       Preconditions.checkArgument(rangeContains(Preconditions.checkNotNull(key)));
/* 297 */       return super.put(key, value);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public SortedSet<R> rowKeySet() {
/* 304 */     return super.rowKeySet();
/*     */   }
/*     */   
/*     */   public SortedMap<R, Map<C, V>> rowMap() {
/* 308 */     return super.rowMap();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Iterator<C> createColumnKeyIterator() {
/* 317 */     final Comparator<? super C> comparator = columnComparator();
/*     */     
/* 319 */     final Iterator<C> merged = Iterators.mergeSorted(Iterables.transform(this.backingMap.values(), new Function<Map<C, V>, Iterator<C>>()
/*     */           {
/*     */             
/*     */             public Iterator<C> apply(Map<C, V> input)
/*     */             {
/* 324 */               return input.keySet().iterator();
/*     */             }
/*     */           }), comparator);
/*     */     
/* 328 */     return new AbstractIterator<C>()
/*     */       {
/*     */         C lastValue;
/*     */         
/*     */         protected C computeNext() {
/* 333 */           while (merged.hasNext()) {
/* 334 */             C next = merged.next();
/* 335 */             boolean duplicate = (this.lastValue != null && comparator.compare(next, this.lastValue) == 0);
/*     */ 
/*     */ 
/*     */             
/* 339 */             if (!duplicate) {
/* 340 */               this.lastValue = next;
/* 341 */               return this.lastValue;
/*     */             } 
/*     */           } 
/*     */           
/* 345 */           this.lastValue = null;
/* 346 */           return endOfData();
/*     */         }
/*     */       };
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\collect\TreeBasedTable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */