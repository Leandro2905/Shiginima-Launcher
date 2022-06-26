/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.NavigableMap;
/*     */ import java.util.NavigableSet;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.SortedMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class ForwardingNavigableMap<K, V>
/*     */   extends ForwardingSortedMap<K, V>
/*     */   implements NavigableMap<K, V>
/*     */ {
/*     */   public Map.Entry<K, V> lowerEntry(K key) {
/*  63 */     return delegate().lowerEntry(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Map.Entry<K, V> standardLowerEntry(K key) {
/*  72 */     return headMap(key, false).lastEntry();
/*     */   }
/*     */ 
/*     */   
/*     */   public K lowerKey(K key) {
/*  77 */     return delegate().lowerKey(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected K standardLowerKey(K key) {
/*  86 */     return Maps.keyOrNull(lowerEntry(key));
/*     */   }
/*     */ 
/*     */   
/*     */   public Map.Entry<K, V> floorEntry(K key) {
/*  91 */     return delegate().floorEntry(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Map.Entry<K, V> standardFloorEntry(K key) {
/* 100 */     return headMap(key, true).lastEntry();
/*     */   }
/*     */ 
/*     */   
/*     */   public K floorKey(K key) {
/* 105 */     return delegate().floorKey(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected K standardFloorKey(K key) {
/* 114 */     return Maps.keyOrNull(floorEntry(key));
/*     */   }
/*     */ 
/*     */   
/*     */   public Map.Entry<K, V> ceilingEntry(K key) {
/* 119 */     return delegate().ceilingEntry(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Map.Entry<K, V> standardCeilingEntry(K key) {
/* 128 */     return tailMap(key, true).firstEntry();
/*     */   }
/*     */ 
/*     */   
/*     */   public K ceilingKey(K key) {
/* 133 */     return delegate().ceilingKey(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected K standardCeilingKey(K key) {
/* 142 */     return Maps.keyOrNull(ceilingEntry(key));
/*     */   }
/*     */ 
/*     */   
/*     */   public Map.Entry<K, V> higherEntry(K key) {
/* 147 */     return delegate().higherEntry(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Map.Entry<K, V> standardHigherEntry(K key) {
/* 156 */     return tailMap(key, false).firstEntry();
/*     */   }
/*     */ 
/*     */   
/*     */   public K higherKey(K key) {
/* 161 */     return delegate().higherKey(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected K standardHigherKey(K key) {
/* 170 */     return Maps.keyOrNull(higherEntry(key));
/*     */   }
/*     */ 
/*     */   
/*     */   public Map.Entry<K, V> firstEntry() {
/* 175 */     return delegate().firstEntry();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Map.Entry<K, V> standardFirstEntry() {
/* 184 */     return Iterables.<Map.Entry<K, V>>getFirst(entrySet(), null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected K standardFirstKey() {
/* 193 */     Map.Entry<K, V> entry = firstEntry();
/* 194 */     if (entry == null) {
/* 195 */       throw new NoSuchElementException();
/*     */     }
/* 197 */     return entry.getKey();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Map.Entry<K, V> lastEntry() {
/* 203 */     return delegate().lastEntry();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Map.Entry<K, V> standardLastEntry() {
/* 212 */     return Iterables.<Map.Entry<K, V>>getFirst(descendingMap().entrySet(), null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected K standardLastKey() {
/* 220 */     Map.Entry<K, V> entry = lastEntry();
/* 221 */     if (entry == null) {
/* 222 */       throw new NoSuchElementException();
/*     */     }
/* 224 */     return entry.getKey();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Map.Entry<K, V> pollFirstEntry() {
/* 230 */     return delegate().pollFirstEntry();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Map.Entry<K, V> standardPollFirstEntry() {
/* 239 */     return Iterators.<Map.Entry<K, V>>pollNext(entrySet().iterator());
/*     */   }
/*     */ 
/*     */   
/*     */   public Map.Entry<K, V> pollLastEntry() {
/* 244 */     return delegate().pollLastEntry();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Map.Entry<K, V> standardPollLastEntry() {
/* 253 */     return Iterators.<Map.Entry<K, V>>pollNext(descendingMap().entrySet().iterator());
/*     */   }
/*     */ 
/*     */   
/*     */   public NavigableMap<K, V> descendingMap() {
/* 258 */     return delegate().descendingMap();
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
/*     */   @Beta
/*     */   protected class StandardDescendingMap
/*     */     extends Maps.DescendingMap<K, V>
/*     */   {
/*     */     NavigableMap<K, V> forward() {
/* 280 */       return ForwardingNavigableMap.this;
/*     */     }
/*     */ 
/*     */     
/*     */     protected Iterator<Map.Entry<K, V>> entryIterator() {
/* 285 */       return new Iterator<Map.Entry<K, V>>() {
/* 286 */           private Map.Entry<K, V> toRemove = null;
/* 287 */           private Map.Entry<K, V> nextOrNull = ForwardingNavigableMap.StandardDescendingMap.this.forward().lastEntry();
/*     */ 
/*     */           
/*     */           public boolean hasNext() {
/* 291 */             return (this.nextOrNull != null);
/*     */           }
/*     */ 
/*     */           
/*     */           public Map.Entry<K, V> next() {
/* 296 */             if (!hasNext()) {
/* 297 */               throw new NoSuchElementException();
/*     */             }
/*     */             try {
/* 300 */               return this.nextOrNull;
/*     */             } finally {
/* 302 */               this.toRemove = this.nextOrNull;
/* 303 */               this.nextOrNull = ForwardingNavigableMap.StandardDescendingMap.this.forward().lowerEntry(this.nextOrNull.getKey());
/*     */             } 
/*     */           }
/*     */ 
/*     */           
/*     */           public void remove() {
/* 309 */             CollectPreconditions.checkRemove((this.toRemove != null));
/* 310 */             ForwardingNavigableMap.StandardDescendingMap.this.forward().remove(this.toRemove.getKey());
/* 311 */             this.toRemove = null;
/*     */           }
/*     */         };
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public NavigableSet<K> navigableKeySet() {
/* 319 */     return delegate().navigableKeySet();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Beta
/*     */   protected class StandardNavigableKeySet
/*     */     extends Maps.NavigableKeySet<K, V>
/*     */   {
/*     */     public StandardNavigableKeySet() {
/* 334 */       super(ForwardingNavigableMap.this);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public NavigableSet<K> descendingKeySet() {
/* 340 */     return delegate().descendingKeySet();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Beta
/*     */   protected NavigableSet<K> standardDescendingKeySet() {
/* 352 */     return descendingMap().navigableKeySet();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected SortedMap<K, V> standardSubMap(K fromKey, K toKey) {
/* 363 */     return subMap(fromKey, true, toKey, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public NavigableMap<K, V> subMap(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive) {
/* 368 */     return delegate().subMap(fromKey, fromInclusive, toKey, toInclusive);
/*     */   }
/*     */ 
/*     */   
/*     */   public NavigableMap<K, V> headMap(K toKey, boolean inclusive) {
/* 373 */     return delegate().headMap(toKey, inclusive);
/*     */   }
/*     */ 
/*     */   
/*     */   public NavigableMap<K, V> tailMap(K fromKey, boolean inclusive) {
/* 378 */     return delegate().tailMap(fromKey, inclusive);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected SortedMap<K, V> standardHeadMap(K toKey) {
/* 387 */     return headMap(toKey, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected SortedMap<K, V> standardTailMap(K fromKey) {
/* 396 */     return tailMap(fromKey, true);
/*     */   }
/*     */   
/*     */   protected abstract NavigableMap<K, V> delegate();
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\collect\ForwardingNavigableMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */