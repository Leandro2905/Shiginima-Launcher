/*     */ package org.jxmpp.util.cache;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.atomic.AtomicLong;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LruCache<K, V>
/*     */   extends LinkedHashMap<K, V>
/*     */   implements Cache<K, V>
/*     */ {
/*     */   private static final long serialVersionUID = -4980809402073634607L;
/*     */   private static final int DEFAULT_INITIAL_SIZE = 50;
/*     */   private int maxCacheSize;
/*  57 */   private final AtomicLong cacheHits = new AtomicLong();
/*  58 */   private final AtomicLong cacheMisses = new AtomicLong();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LruCache(int maxSize) {
/*  68 */     super((maxSize < 50) ? maxSize : 50, 0.75F, true);
/*     */     
/*  70 */     if (maxSize == 0) {
/*  71 */       throw new IllegalArgumentException("Max cache size cannot be 0.");
/*     */     }
/*  73 */     this.maxCacheSize = maxSize;
/*     */   }
/*     */ 
/*     */   
/*     */   protected final boolean removeEldestEntry(Map.Entry<K, V> eldest) {
/*  78 */     return (size() > this.maxCacheSize);
/*     */   }
/*     */ 
/*     */   
/*     */   public final synchronized V put(K key, V value) {
/*  83 */     return super.put(key, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public final V get(Object key) {
/*     */     V cacheObject;
/*  89 */     synchronized (this) {
/*  90 */       cacheObject = super.get(key);
/*     */     } 
/*  92 */     if (cacheObject == null) {
/*     */       
/*  94 */       this.cacheMisses.incrementAndGet();
/*  95 */       return null;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 100 */     this.cacheHits.incrementAndGet();
/*     */     
/* 102 */     return cacheObject;
/*     */   }
/*     */ 
/*     */   
/*     */   public final synchronized V remove(Object key) {
/* 107 */     return super.remove(key);
/*     */   }
/*     */ 
/*     */   
/*     */   public final void clear() {
/* 112 */     synchronized (this) {
/* 113 */       super.clear();
/*     */     } 
/* 115 */     this.cacheHits.set(0L);
/* 116 */     this.cacheMisses.set(0L);
/*     */   }
/*     */ 
/*     */   
/*     */   public final synchronized int size() {
/* 121 */     return super.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public final synchronized boolean isEmpty() {
/* 126 */     return super.isEmpty();
/*     */   }
/*     */ 
/*     */   
/*     */   public final synchronized Collection<V> values() {
/* 131 */     return super.values();
/*     */   }
/*     */ 
/*     */   
/*     */   public final synchronized boolean containsKey(Object key) {
/* 136 */     return super.containsKey(key);
/*     */   }
/*     */ 
/*     */   
/*     */   public final synchronized void putAll(Map<? extends K, ? extends V> m) {
/* 141 */     super.putAll(m);
/*     */   }
/*     */ 
/*     */   
/*     */   public final synchronized boolean containsValue(Object value) {
/* 146 */     return super.containsValue(value);
/*     */   }
/*     */ 
/*     */   
/*     */   public final synchronized Set<Map.Entry<K, V>> entrySet() {
/* 151 */     return super.entrySet();
/*     */   }
/*     */ 
/*     */   
/*     */   public final synchronized Set<K> keySet() {
/* 156 */     return super.keySet();
/*     */   }
/*     */   
/*     */   public final long getCacheHits() {
/* 160 */     return this.cacheHits.longValue();
/*     */   }
/*     */   
/*     */   public final long getCacheMisses() {
/* 164 */     return this.cacheMisses.longValue();
/*     */   }
/*     */ 
/*     */   
/*     */   public final int getMaxCacheSize() {
/* 169 */     return this.maxCacheSize;
/*     */   }
/*     */ 
/*     */   
/*     */   public final void setMaxCacheSize(int maxCacheSize) {
/* 174 */     this.maxCacheSize = maxCacheSize;
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jxmp\\util\cache\LruCache.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */