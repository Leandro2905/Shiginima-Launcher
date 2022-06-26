/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import java.io.IOException;
/*     */ import java.io.InvalidObjectException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
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
/*     */ @GwtCompatible(serializable = true, emulated = true)
/*     */ public class ImmutableListMultimap<K, V>
/*     */   extends ImmutableMultimap<K, V>
/*     */   implements ListMultimap<K, V>
/*     */ {
/*     */   private transient ImmutableListMultimap<V, K> inverse;
/*     */   @GwtIncompatible("Not needed in emulated source")
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   public static <K, V> ImmutableListMultimap<K, V> of() {
/*  64 */     return EmptyImmutableListMultimap.INSTANCE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableListMultimap<K, V> of(K k1, V v1) {
/*  71 */     Builder<K, V> builder = builder();
/*     */     
/*  73 */     builder.put(k1, v1);
/*  74 */     return builder.build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableListMultimap<K, V> of(K k1, V v1, K k2, V v2) {
/*  81 */     Builder<K, V> builder = builder();
/*     */     
/*  83 */     builder.put(k1, v1);
/*  84 */     builder.put(k2, v2);
/*  85 */     return builder.build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableListMultimap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3) {
/*  93 */     Builder<K, V> builder = builder();
/*     */     
/*  95 */     builder.put(k1, v1);
/*  96 */     builder.put(k2, v2);
/*  97 */     builder.put(k3, v3);
/*  98 */     return builder.build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableListMultimap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
/* 106 */     Builder<K, V> builder = builder();
/*     */     
/* 108 */     builder.put(k1, v1);
/* 109 */     builder.put(k2, v2);
/* 110 */     builder.put(k3, v3);
/* 111 */     builder.put(k4, v4);
/* 112 */     return builder.build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableListMultimap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
/* 120 */     Builder<K, V> builder = builder();
/*     */     
/* 122 */     builder.put(k1, v1);
/* 123 */     builder.put(k2, v2);
/* 124 */     builder.put(k3, v3);
/* 125 */     builder.put(k4, v4);
/* 126 */     builder.put(k5, v5);
/* 127 */     return builder.build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> Builder<K, V> builder() {
/* 137 */     return new Builder<K, V>();
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
/*     */   public static final class Builder<K, V>
/*     */     extends ImmutableMultimap.Builder<K, V>
/*     */   {
/*     */     public Builder<K, V> put(K key, V value) {
/* 167 */       super.put(key, value);
/* 168 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder<K, V> put(Map.Entry<? extends K, ? extends V> entry) {
/* 178 */       super.put(entry);
/* 179 */       return this;
/*     */     }
/*     */     
/*     */     public Builder<K, V> putAll(K key, Iterable<? extends V> values) {
/* 183 */       super.putAll(key, values);
/* 184 */       return this;
/*     */     }
/*     */     
/*     */     public Builder<K, V> putAll(K key, V... values) {
/* 188 */       super.putAll(key, values);
/* 189 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public Builder<K, V> putAll(Multimap<? extends K, ? extends V> multimap) {
/* 194 */       super.putAll(multimap);
/* 195 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder<K, V> orderKeysBy(Comparator<? super K> keyComparator) {
/* 205 */       super.orderKeysBy(keyComparator);
/* 206 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder<K, V> orderValuesBy(Comparator<? super V> valueComparator) {
/* 216 */       super.orderValuesBy(valueComparator);
/* 217 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ImmutableListMultimap<K, V> build() {
/* 224 */       return (ImmutableListMultimap<K, V>)super.build();
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
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableListMultimap<K, V> copyOf(Multimap<? extends K, ? extends V> multimap) {
/* 242 */     if (multimap.isEmpty()) {
/* 243 */       return of();
/*     */     }
/*     */ 
/*     */     
/* 247 */     if (multimap instanceof ImmutableListMultimap) {
/*     */       
/* 249 */       ImmutableListMultimap<K, V> kvMultimap = (ImmutableListMultimap)multimap;
/*     */       
/* 251 */       if (!kvMultimap.isPartialView()) {
/* 252 */         return kvMultimap;
/*     */       }
/*     */     } 
/*     */     
/* 256 */     ImmutableMap.Builder<K, ImmutableList<V>> builder = ImmutableMap.builder();
/* 257 */     int size = 0;
/*     */ 
/*     */     
/* 260 */     for (Map.Entry<? extends K, ? extends Collection<? extends V>> entry : (Iterable<Map.Entry<? extends K, ? extends Collection<? extends V>>>)multimap.asMap().entrySet()) {
/* 261 */       ImmutableList<V> list = ImmutableList.copyOf(entry.getValue());
/* 262 */       if (!list.isEmpty()) {
/* 263 */         builder.put(entry.getKey(), list);
/* 264 */         size += list.size();
/*     */       } 
/*     */     } 
/*     */     
/* 268 */     return new ImmutableListMultimap<K, V>(builder.build(), size);
/*     */   }
/*     */   
/*     */   ImmutableListMultimap(ImmutableMap<K, ImmutableList<V>> map, int size) {
/* 272 */     super((ImmutableMap)map, size);
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
/*     */   public ImmutableList<V> get(@Nullable K key) {
/* 285 */     ImmutableList<V> list = (ImmutableList<V>)this.map.get(key);
/* 286 */     return (list == null) ? ImmutableList.<V>of() : list;
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
/*     */   public ImmutableListMultimap<V, K> inverse() {
/* 303 */     ImmutableListMultimap<V, K> result = this.inverse;
/* 304 */     return (result == null) ? (this.inverse = invert()) : result;
/*     */   }
/*     */   
/*     */   private ImmutableListMultimap<V, K> invert() {
/* 308 */     Builder<V, K> builder = builder();
/* 309 */     for (Map.Entry<K, V> entry : entries()) {
/* 310 */       builder.put(entry.getValue(), entry.getKey());
/*     */     }
/* 312 */     ImmutableListMultimap<V, K> invertedMultimap = builder.build();
/* 313 */     invertedMultimap.inverse = this;
/* 314 */     return invertedMultimap;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public ImmutableList<V> removeAll(Object key) {
/* 324 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public ImmutableList<V> replaceValues(K key, Iterable<? extends V> values) {
/* 335 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible("java.io.ObjectOutputStream")
/*     */   private void writeObject(ObjectOutputStream stream) throws IOException {
/* 344 */     stream.defaultWriteObject();
/* 345 */     Serialization.writeMultimap(this, stream);
/*     */   }
/*     */   
/*     */   @GwtIncompatible("java.io.ObjectInputStream")
/*     */   private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
/*     */     ImmutableMap<Object, ImmutableList<Object>> tmpMap;
/* 351 */     stream.defaultReadObject();
/* 352 */     int keyCount = stream.readInt();
/* 353 */     if (keyCount < 0) {
/* 354 */       int j = keyCount; throw new InvalidObjectException((new StringBuilder(29)).append("Invalid key count ").append(j).toString());
/*     */     } 
/* 356 */     ImmutableMap.Builder<Object, ImmutableList<Object>> builder = ImmutableMap.builder();
/*     */     
/* 358 */     int tmpSize = 0;
/*     */     
/* 360 */     for (int i = 0; i < keyCount; i++) {
/* 361 */       Object key = stream.readObject();
/* 362 */       int valueCount = stream.readInt();
/* 363 */       if (valueCount <= 0) {
/* 364 */         int k = valueCount; throw new InvalidObjectException((new StringBuilder(31)).append("Invalid value count ").append(k).toString());
/*     */       } 
/*     */       
/* 367 */       Object[] array = new Object[valueCount];
/* 368 */       for (int j = 0; j < valueCount; j++) {
/* 369 */         array[j] = stream.readObject();
/*     */       }
/* 371 */       builder.put(key, ImmutableList.copyOf(array));
/* 372 */       tmpSize += valueCount;
/*     */     } 
/*     */ 
/*     */     
/*     */     try {
/* 377 */       tmpMap = builder.build();
/* 378 */     } catch (IllegalArgumentException e) {
/* 379 */       throw (InvalidObjectException)(new InvalidObjectException(e.getMessage())).initCause(e);
/*     */     } 
/*     */ 
/*     */     
/* 383 */     ImmutableMultimap.FieldSettersHolder.MAP_FIELD_SETTER.set(this, tmpMap);
/* 384 */     ImmutableMultimap.FieldSettersHolder.SIZE_FIELD_SETTER.set(this, tmpSize);
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\collect\ImmutableListMultimap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */