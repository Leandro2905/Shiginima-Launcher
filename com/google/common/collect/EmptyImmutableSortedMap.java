/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
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
/*     */ 
/*     */ 
/*     */ @GwtCompatible(emulated = true)
/*     */ final class EmptyImmutableSortedMap<K, V>
/*     */   extends ImmutableSortedMap<K, V>
/*     */ {
/*     */   private final transient ImmutableSortedSet<K> keySet;
/*     */   
/*     */   EmptyImmutableSortedMap(Comparator<? super K> comparator) {
/*  37 */     this.keySet = ImmutableSortedSet.emptySet(comparator);
/*     */   }
/*     */ 
/*     */   
/*     */   EmptyImmutableSortedMap(Comparator<? super K> comparator, ImmutableSortedMap<K, V> descendingMap) {
/*  42 */     super(descendingMap);
/*  43 */     this.keySet = ImmutableSortedSet.emptySet(comparator);
/*     */   }
/*     */ 
/*     */   
/*     */   public V get(@Nullable Object key) {
/*  48 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public ImmutableSortedSet<K> keySet() {
/*  53 */     return this.keySet;
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/*  58 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/*  63 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public ImmutableCollection<V> values() {
/*  68 */     return ImmutableList.of();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  73 */     return "{}";
/*     */   }
/*     */ 
/*     */   
/*     */   boolean isPartialView() {
/*  78 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public ImmutableSet<Map.Entry<K, V>> entrySet() {
/*  83 */     return ImmutableSet.of();
/*     */   }
/*     */ 
/*     */   
/*     */   ImmutableSet<Map.Entry<K, V>> createEntrySet() {
/*  88 */     throw new AssertionError("should never be called");
/*     */   }
/*     */ 
/*     */   
/*     */   public ImmutableSetMultimap<K, V> asMultimap() {
/*  93 */     return ImmutableSetMultimap.of();
/*     */   }
/*     */ 
/*     */   
/*     */   public ImmutableSortedMap<K, V> headMap(K toKey, boolean inclusive) {
/*  98 */     Preconditions.checkNotNull(toKey);
/*  99 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ImmutableSortedMap<K, V> tailMap(K fromKey, boolean inclusive) {
/* 104 */     Preconditions.checkNotNull(fromKey);
/* 105 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   ImmutableSortedMap<K, V> createDescendingMap() {
/* 110 */     return new EmptyImmutableSortedMap(Ordering.<K>from(comparator()).reverse(), this);
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\collect\EmptyImmutableSortedMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */