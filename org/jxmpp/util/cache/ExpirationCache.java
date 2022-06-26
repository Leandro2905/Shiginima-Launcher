/*     */ package org.jxmpp.util.cache;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ExpirationCache<K, V>
/*     */   implements Cache<K, V>, Map<K, V>
/*     */ {
/*     */   private final LruCache<K, ExpireElement<V>> cache;
/*     */   private long defaultExpirationTime;
/*     */   
/*     */   public ExpirationCache(int maxSize, long defaultExpirationTime) {
/*  31 */     this.cache = new LruCache<>(maxSize);
/*  32 */     setDefaultExpirationTime(defaultExpirationTime);
/*     */   }
/*     */   
/*     */   public void setDefaultExpirationTime(long defaultExpirationTime) {
/*  36 */     if (defaultExpirationTime <= 0L) {
/*  37 */       throw new IllegalArgumentException();
/*     */     }
/*  39 */     this.defaultExpirationTime = defaultExpirationTime;
/*     */   }
/*     */ 
/*     */   
/*     */   public V put(K key, V value) {
/*  44 */     return put(key, value, this.defaultExpirationTime);
/*     */   }
/*     */   
/*     */   public V put(K key, V value, long expirationTime) {
/*  48 */     ExpireElement<V> eOld = this.cache.put(key, new ExpireElement<>(value, expirationTime));
/*  49 */     if (eOld == null) {
/*  50 */       return null;
/*     */     }
/*  52 */     return eOld.element;
/*     */   }
/*     */ 
/*     */   
/*     */   public V get(Object key) {
/*  57 */     ExpireElement<V> v = this.cache.get(key);
/*  58 */     if (v == null) {
/*  59 */       return null;
/*     */     }
/*  61 */     if (v.isExpired()) {
/*  62 */       remove(key);
/*  63 */       return null;
/*     */     } 
/*  65 */     return v.element;
/*     */   }
/*     */   
/*     */   public V remove(Object key) {
/*  69 */     ExpireElement<V> e = this.cache.remove(key);
/*  70 */     if (e == null) {
/*  71 */       return null;
/*     */     }
/*  73 */     return e.element;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMaxCacheSize() {
/*  78 */     return this.cache.getMaxCacheSize();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setMaxCacheSize(int maxCacheSize) {
/*  83 */     this.cache.setMaxCacheSize(maxCacheSize);
/*     */   }
/*     */   
/*     */   private static class ExpireElement<V> {
/*     */     public final V element;
/*     */     public final long expirationTimestamp;
/*     */     
/*     */     public ExpireElement(V element, long expirationTime) {
/*  91 */       this.element = element;
/*  92 */       this.expirationTimestamp = System.currentTimeMillis() + expirationTime;
/*     */     }
/*     */     
/*     */     public boolean isExpired() {
/*  96 */       return (System.currentTimeMillis() > this.expirationTimestamp);
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 101 */       return this.element.hashCode();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object other) {
/* 106 */       if (!(other instanceof ExpireElement))
/* 107 */         return false; 
/* 108 */       ExpireElement<?> otherElement = (ExpireElement)other;
/* 109 */       return this.element.equals(otherElement.element);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/* 115 */     return this.cache.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 120 */     return this.cache.isEmpty();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsKey(Object key) {
/* 125 */     return this.cache.containsKey(key);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsValue(Object value) {
/* 130 */     return this.cache.containsValue(value);
/*     */   }
/*     */ 
/*     */   
/*     */   public void putAll(Map<? extends K, ? extends V> m) {
/* 135 */     for (Map.Entry<? extends K, ? extends V> entry : m.entrySet()) {
/* 136 */       put(entry.getKey(), entry.getValue());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 142 */     this.cache.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<K> keySet() {
/* 147 */     return this.cache.keySet();
/*     */   }
/*     */ 
/*     */   
/*     */   public Collection<V> values() {
/* 152 */     Set<V> res = new HashSet<>();
/* 153 */     for (ExpireElement<V> value : this.cache.values()) {
/* 154 */       res.add(value.element);
/*     */     }
/* 156 */     return res;
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<Map.Entry<K, V>> entrySet() {
/* 161 */     Set<Map.Entry<K, V>> res = new HashSet<>();
/* 162 */     for (Map.Entry<K, ExpireElement<V>> entry : this.cache.entrySet()) {
/* 163 */       res.add(new EntryImpl<>(entry.getKey(), ((ExpireElement)entry.getValue()).element));
/*     */     }
/* 165 */     return res;
/*     */   }
/*     */   
/*     */   private static class EntryImpl<K, V>
/*     */     implements Map.Entry<K, V> {
/*     */     private final K key;
/*     */     private V value;
/*     */     
/*     */     public EntryImpl(K key, V value) {
/* 174 */       this.key = key;
/* 175 */       this.value = value;
/*     */     }
/*     */     
/*     */     public K getKey() {
/* 179 */       return this.key;
/*     */     }
/*     */ 
/*     */     
/*     */     public V getValue() {
/* 184 */       return this.value;
/*     */     }
/*     */ 
/*     */     
/*     */     public V setValue(V value) {
/* 189 */       V oldValue = this.value;
/* 190 */       this.value = value;
/* 191 */       return oldValue;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jxmp\\util\cache\ExpirationCache.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */