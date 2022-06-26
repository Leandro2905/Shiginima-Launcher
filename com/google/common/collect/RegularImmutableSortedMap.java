/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.NavigableMap;
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
/*     */ @GwtCompatible(emulated = true)
/*     */ final class RegularImmutableSortedMap<K, V>
/*     */   extends ImmutableSortedMap<K, V>
/*     */ {
/*     */   private final transient RegularImmutableSortedSet<K> keySet;
/*     */   private final transient ImmutableList<V> valueList;
/*     */   
/*     */   RegularImmutableSortedMap(RegularImmutableSortedSet<K> keySet, ImmutableList<V> valueList) {
/*  36 */     this.keySet = keySet;
/*  37 */     this.valueList = valueList;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   RegularImmutableSortedMap(RegularImmutableSortedSet<K> keySet, ImmutableList<V> valueList, ImmutableSortedMap<K, V> descendingMap) {
/*  44 */     super(descendingMap);
/*  45 */     this.keySet = keySet;
/*  46 */     this.valueList = valueList;
/*     */   }
/*     */ 
/*     */   
/*     */   ImmutableSet<Map.Entry<K, V>> createEntrySet() {
/*  51 */     return new EntrySet();
/*     */   }
/*     */   
/*     */   private class EntrySet
/*     */     extends ImmutableMapEntrySet<K, V> {
/*     */     public UnmodifiableIterator<Map.Entry<K, V>> iterator() {
/*  57 */       return asList().iterator();
/*     */     }
/*     */     private EntrySet() {}
/*     */     
/*     */     ImmutableList<Map.Entry<K, V>> createAsList() {
/*  62 */       return new ImmutableAsList<Map.Entry<K, V>>()
/*     */         {
/*  64 */           private final ImmutableList<K> keyList = RegularImmutableSortedMap.this.keySet().asList();
/*     */ 
/*     */           
/*     */           public Map.Entry<K, V> get(int index) {
/*  68 */             return Maps.immutableEntry(this.keyList.get(index), (V)RegularImmutableSortedMap.this.valueList.get(index));
/*     */           }
/*     */ 
/*     */           
/*     */           ImmutableCollection<Map.Entry<K, V>> delegateCollection() {
/*  73 */             return RegularImmutableSortedMap.EntrySet.this;
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */     
/*     */     ImmutableMap<K, V> map() {
/*  80 */       return RegularImmutableSortedMap.this;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public ImmutableSortedSet<K> keySet() {
/*  86 */     return this.keySet;
/*     */   }
/*     */ 
/*     */   
/*     */   public ImmutableCollection<V> values() {
/*  91 */     return this.valueList;
/*     */   }
/*     */ 
/*     */   
/*     */   public V get(@Nullable Object key) {
/*  96 */     int index = this.keySet.indexOf(key);
/*  97 */     return (index == -1) ? null : this.valueList.get(index);
/*     */   }
/*     */   
/*     */   private ImmutableSortedMap<K, V> getSubMap(int fromIndex, int toIndex) {
/* 101 */     if (fromIndex == 0 && toIndex == size())
/* 102 */       return this; 
/* 103 */     if (fromIndex == toIndex) {
/* 104 */       return emptyMap(comparator());
/*     */     }
/* 106 */     return from(this.keySet.getSubSet(fromIndex, toIndex), this.valueList.subList(fromIndex, toIndex));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableSortedMap<K, V> headMap(K toKey, boolean inclusive) {
/* 114 */     return getSubMap(0, this.keySet.headIndex((K)Preconditions.checkNotNull(toKey), inclusive));
/*     */   }
/*     */ 
/*     */   
/*     */   public ImmutableSortedMap<K, V> tailMap(K fromKey, boolean inclusive) {
/* 119 */     return getSubMap(this.keySet.tailIndex((K)Preconditions.checkNotNull(fromKey), inclusive), size());
/*     */   }
/*     */ 
/*     */   
/*     */   ImmutableSortedMap<K, V> createDescendingMap() {
/* 124 */     return new RegularImmutableSortedMap((RegularImmutableSortedSet<K>)this.keySet.descendingSet(), this.valueList.reverse(), this);
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\collect\RegularImmutableSortedMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */