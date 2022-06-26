/*     */ package org.jivesoftware.smack.util;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MultiMap<K, V>
/*     */ {
/*     */   public static final int DEFAULT_MAP_SIZE = 6;
/*     */   private static final int ENTRY_SET_SIZE = 3;
/*     */   private final Map<K, Set<V>> map;
/*     */   
/*     */   public MultiMap() {
/*  51 */     this(6);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MultiMap(int size) {
/*  60 */     this.map = new LinkedHashMap<>(size);
/*     */   }
/*     */   
/*     */   public int size() {
/*  64 */     int size = 0;
/*  65 */     for (Set<V> set : this.map.values()) {
/*  66 */       size += set.size();
/*     */     }
/*  68 */     return size;
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/*  72 */     return this.map.isEmpty();
/*     */   }
/*     */   
/*     */   public boolean containsKey(Object key) {
/*  76 */     return this.map.containsKey(key);
/*     */   }
/*     */   
/*     */   public boolean containsValue(Object value) {
/*  80 */     for (Set<V> set : this.map.values()) {
/*  81 */       if (set.contains(value)) {
/*  82 */         return true;
/*     */       }
/*     */     } 
/*  85 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public V getFirst(Object key) {
/*  95 */     Set<V> res = getAll(key);
/*  96 */     if (res.isEmpty()) {
/*  97 */       return null;
/*     */     }
/*  99 */     return res.iterator().next();
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
/*     */   public Set<V> getAll(Object key) {
/* 113 */     Set<V> res = this.map.get(key);
/* 114 */     if (res == null) {
/* 115 */       res = Collections.emptySet();
/*     */     }
/* 117 */     return res;
/*     */   }
/*     */   
/*     */   public boolean put(K key, V value) {
/*     */     boolean keyExisted;
/* 122 */     Set<V> set = this.map.get(key);
/* 123 */     if (set == null) {
/* 124 */       set = new LinkedHashSet<>(3);
/* 125 */       set.add(value);
/* 126 */       this.map.put(key, set);
/* 127 */       keyExisted = false;
/*     */     } else {
/* 129 */       set.add(value);
/* 130 */       keyExisted = true;
/*     */     } 
/* 132 */     return keyExisted;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public V remove(Object key) {
/* 142 */     Set<V> res = this.map.remove(key);
/* 143 */     if (res == null) {
/* 144 */       return null;
/*     */     }
/* 146 */     assert !res.isEmpty();
/* 147 */     return res.iterator().next();
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
/*     */   public boolean removeOne(Object key, V value) {
/* 161 */     Set<V> set = this.map.get(key);
/* 162 */     if (set == null) {
/* 163 */       return false;
/*     */     }
/* 165 */     boolean res = set.remove(value);
/* 166 */     if (set.isEmpty())
/*     */     {
/* 168 */       this.map.remove(key);
/*     */     }
/* 170 */     return res;
/*     */   }
/*     */   
/*     */   public void putAll(Map<? extends K, ? extends V> map) {
/* 174 */     for (Map.Entry<? extends K, ? extends V> entry : map.entrySet()) {
/* 175 */       put(entry.getKey(), entry.getValue());
/*     */     }
/*     */   }
/*     */   
/*     */   public void clear() {
/* 180 */     this.map.clear();
/*     */   }
/*     */   
/*     */   public Set<K> keySet() {
/* 184 */     return this.map.keySet();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<V> values() {
/* 193 */     List<V> values = new ArrayList<>(size());
/* 194 */     for (Set<V> set : this.map.values()) {
/* 195 */       values.addAll(set);
/*     */     }
/* 197 */     return values;
/*     */   }
/*     */   
/*     */   public Set<Map.Entry<K, V>> entrySet() {
/* 201 */     Set<Map.Entry<K, V>> entrySet = new LinkedHashSet<>(size());
/* 202 */     for (Map.Entry<K, Set<V>> entries : this.map.entrySet()) {
/* 203 */       K key = entries.getKey();
/* 204 */       for (V value : entries.getValue()) {
/* 205 */         entrySet.add(new SimpleMapEntry<>(key, value));
/*     */       }
/*     */     } 
/* 208 */     return entrySet;
/*     */   }
/*     */   
/*     */   private static class SimpleMapEntry<K, V>
/*     */     implements Map.Entry<K, V> {
/*     */     private final K key;
/*     */     private V value;
/*     */     
/*     */     private SimpleMapEntry(K key, V value) {
/* 217 */       this.key = key;
/* 218 */       this.value = value;
/*     */     }
/*     */ 
/*     */     
/*     */     public K getKey() {
/* 223 */       return this.key;
/*     */     }
/*     */ 
/*     */     
/*     */     public V getValue() {
/* 228 */       return this.value;
/*     */     }
/*     */ 
/*     */     
/*     */     public V setValue(V value) {
/* 233 */       V tmp = this.value;
/* 234 */       this.value = value;
/* 235 */       return tmp;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jivesoftware\smac\\util\MultiMap.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */