/*     */ package com.google.common.cache;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.Maps;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import java.util.concurrent.ExecutionException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public abstract class AbstractCache<K, V>
/*     */   implements Cache<K, V>
/*     */ {
/*     */   public V get(K key, Callable<? extends V> valueLoader) throws ExecutionException {
/*  55 */     throw new UnsupportedOperationException();
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
/*     */   public ImmutableMap<K, V> getAllPresent(Iterable<?> keys) {
/*  69 */     Map<K, V> result = Maps.newLinkedHashMap();
/*  70 */     for (Object key : keys) {
/*  71 */       if (!result.containsKey(key)) {
/*     */         
/*  73 */         K castKey = (K)key;
/*  74 */         V value = getIfPresent(key);
/*  75 */         if (value != null) {
/*  76 */           result.put(castKey, value);
/*     */         }
/*     */       } 
/*     */     } 
/*  80 */     return ImmutableMap.copyOf(result);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void put(K key, V value) {
/*  88 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void putAll(Map<? extends K, ? extends V> m) {
/*  96 */     for (Map.Entry<? extends K, ? extends V> entry : m.entrySet()) {
/*  97 */       put(entry.getKey(), entry.getValue());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void cleanUp() {}
/*     */ 
/*     */   
/*     */   public long size() {
/* 106 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public void invalidate(Object key) {
/* 111 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void invalidateAll(Iterable<?> keys) {
/* 119 */     for (Object key : keys) {
/* 120 */       invalidate(key);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void invalidateAll() {
/* 126 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public CacheStats stats() {
/* 131 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public ConcurrentMap<K, V> asMap() {
/* 136 */     throw new UnsupportedOperationException();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */   public static final class SimpleStatsCounter
/*     */     implements StatsCounter
/*     */   {
/* 209 */     private final LongAddable hitCount = LongAddables.create();
/* 210 */     private final LongAddable missCount = LongAddables.create();
/* 211 */     private final LongAddable loadSuccessCount = LongAddables.create();
/* 212 */     private final LongAddable loadExceptionCount = LongAddables.create();
/* 213 */     private final LongAddable totalLoadTime = LongAddables.create();
/* 214 */     private final LongAddable evictionCount = LongAddables.create();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void recordHits(int count) {
/* 226 */       this.hitCount.add(count);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void recordMisses(int count) {
/* 234 */       this.missCount.add(count);
/*     */     }
/*     */ 
/*     */     
/*     */     public void recordLoadSuccess(long loadTime) {
/* 239 */       this.loadSuccessCount.increment();
/* 240 */       this.totalLoadTime.add(loadTime);
/*     */     }
/*     */ 
/*     */     
/*     */     public void recordLoadException(long loadTime) {
/* 245 */       this.loadExceptionCount.increment();
/* 246 */       this.totalLoadTime.add(loadTime);
/*     */     }
/*     */ 
/*     */     
/*     */     public void recordEviction() {
/* 251 */       this.evictionCount.increment();
/*     */     }
/*     */ 
/*     */     
/*     */     public CacheStats snapshot() {
/* 256 */       return new CacheStats(this.hitCount.sum(), this.missCount.sum(), this.loadSuccessCount.sum(), this.loadExceptionCount.sum(), this.totalLoadTime.sum(), this.evictionCount.sum());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void incrementBy(AbstractCache.StatsCounter other) {
/* 269 */       CacheStats otherStats = other.snapshot();
/* 270 */       this.hitCount.add(otherStats.hitCount());
/* 271 */       this.missCount.add(otherStats.missCount());
/* 272 */       this.loadSuccessCount.add(otherStats.loadSuccessCount());
/* 273 */       this.loadExceptionCount.add(otherStats.loadExceptionCount());
/* 274 */       this.totalLoadTime.add(otherStats.totalLoadTime());
/* 275 */       this.evictionCount.add(otherStats.evictionCount());
/*     */     }
/*     */   }
/*     */   
/*     */   @Beta
/*     */   public static interface StatsCounter {
/*     */     void recordHits(int param1Int);
/*     */     
/*     */     void recordMisses(int param1Int);
/*     */     
/*     */     void recordLoadSuccess(long param1Long);
/*     */     
/*     */     void recordLoadException(long param1Long);
/*     */     
/*     */     void recordEviction();
/*     */     
/*     */     CacheStats snapshot();
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\cache\AbstractCache.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */