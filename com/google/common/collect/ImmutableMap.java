/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.io.Serializable;
/*     */ import java.util.Collection;
/*     */ import java.util.EnumMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public abstract class ImmutableMap<K, V>
/*     */   implements Map<K, V>, Serializable
/*     */ {
/*     */   public static <K, V> ImmutableMap<K, V> of() {
/*  70 */     return ImmutableBiMap.of();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableMap<K, V> of(K k1, V v1) {
/*  80 */     return ImmutableBiMap.of(k1, v1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableMap<K, V> of(K k1, V v1, K k2, V v2) {
/*  89 */     return new RegularImmutableMap<K, V>((ImmutableMapEntry.TerminalEntry<?, ?>[])new ImmutableMapEntry.TerminalEntry[] { entryOf(k1, v1), entryOf(k2, v2) });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3) {
/*  99 */     return new RegularImmutableMap<K, V>((ImmutableMapEntry.TerminalEntry<?, ?>[])new ImmutableMapEntry.TerminalEntry[] { entryOf(k1, v1), entryOf(k2, v2), entryOf(k3, v3) });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
/* 110 */     return new RegularImmutableMap<K, V>((ImmutableMapEntry.TerminalEntry<?, ?>[])new ImmutableMapEntry.TerminalEntry[] { entryOf(k1, v1), entryOf(k2, v2), entryOf(k3, v3), entryOf(k4, v4) });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
/* 121 */     return new RegularImmutableMap<K, V>((ImmutableMapEntry.TerminalEntry<?, ?>[])new ImmutableMapEntry.TerminalEntry[] { entryOf(k1, v1), entryOf(k2, v2), entryOf(k3, v3), entryOf(k4, v4), entryOf(k5, v5) });
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
/*     */   static <K, V> ImmutableMapEntry.TerminalEntry<K, V> entryOf(K key, V value) {
/* 135 */     CollectPreconditions.checkEntryNotNull(key, value);
/* 136 */     return new ImmutableMapEntry.TerminalEntry<K, V>(key, value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> Builder<K, V> builder() {
/* 144 */     return new Builder<K, V>();
/*     */   }
/*     */ 
/*     */   
/*     */   static void checkNoConflict(boolean safe, String conflictDescription, Map.Entry<?, ?> entry1, Map.Entry<?, ?> entry2) {
/* 149 */     if (!safe) {
/* 150 */       String str1 = String.valueOf(String.valueOf(conflictDescription)), str2 = String.valueOf(String.valueOf(entry1)), str3 = String.valueOf(String.valueOf(entry2)); throw new IllegalArgumentException((new StringBuilder(34 + str1.length() + str2.length() + str3.length())).append("Multiple entries with same ").append(str1).append(": ").append(str2).append(" and ").append(str3).toString());
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
/*     */   public static class Builder<K, V>
/*     */   {
/*     */     ImmutableMapEntry.TerminalEntry<K, V>[] entries;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     int size;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder() {
/* 184 */       this(4);
/*     */     }
/*     */ 
/*     */     
/*     */     Builder(int initialCapacity) {
/* 189 */       this.entries = (ImmutableMapEntry.TerminalEntry<K, V>[])new ImmutableMapEntry.TerminalEntry[initialCapacity];
/* 190 */       this.size = 0;
/*     */     }
/*     */     
/*     */     private void ensureCapacity(int minCapacity) {
/* 194 */       if (minCapacity > this.entries.length) {
/* 195 */         this.entries = ObjectArrays.<ImmutableMapEntry.TerminalEntry<K, V>>arraysCopyOf(this.entries, ImmutableCollection.Builder.expandedCapacity(this.entries.length, minCapacity));
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder<K, V> put(K key, V value) {
/* 205 */       ensureCapacity(this.size + 1);
/* 206 */       ImmutableMapEntry.TerminalEntry<K, V> entry = ImmutableMap.entryOf(key, value);
/*     */       
/* 208 */       this.entries[this.size++] = entry;
/* 209 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder<K, V> put(Map.Entry<? extends K, ? extends V> entry) {
/* 220 */       return put(entry.getKey(), entry.getValue());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder<K, V> putAll(Map<? extends K, ? extends V> map) {
/* 230 */       ensureCapacity(this.size + map.size());
/* 231 */       for (Map.Entry<? extends K, ? extends V> entry : map.entrySet()) {
/* 232 */         put(entry);
/*     */       }
/* 234 */       return this;
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
/*     */     
/*     */     public ImmutableMap<K, V> build() {
/* 248 */       switch (this.size) {
/*     */         case 0:
/* 250 */           return ImmutableMap.of();
/*     */         case 1:
/* 252 */           return ImmutableMap.of(this.entries[0].getKey(), this.entries[0].getValue());
/*     */       } 
/* 254 */       return new RegularImmutableMap<K, V>(this.size, (ImmutableMapEntry.TerminalEntry<?, ?>[])this.entries);
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
/*     */   public static <K, V> ImmutableMap<K, V> copyOf(Map<? extends K, ? extends V> map) {
/*     */     Map.Entry<K, V> onlyEntry;
/* 273 */     if (map instanceof ImmutableMap && !(map instanceof ImmutableSortedMap)) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 278 */       ImmutableMap<K, V> kvMap = (ImmutableMap)map;
/* 279 */       if (!kvMap.isPartialView()) {
/* 280 */         return kvMap;
/*     */       }
/* 282 */     } else if (map instanceof EnumMap) {
/* 283 */       return copyOfEnumMapUnsafe(map);
/*     */     } 
/* 285 */     Map.Entry[] arrayOfEntry = (Map.Entry[])map.entrySet().toArray((Object[])EMPTY_ENTRY_ARRAY);
/* 286 */     switch (arrayOfEntry.length) {
/*     */       case 0:
/* 288 */         return of();
/*     */       
/*     */       case 1:
/* 291 */         onlyEntry = arrayOfEntry[0];
/* 292 */         return of(onlyEntry.getKey(), onlyEntry.getValue());
/*     */     } 
/* 294 */     return new RegularImmutableMap<K, V>((Map.Entry<?, ?>[])arrayOfEntry);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static <K, V> ImmutableMap<K, V> copyOfEnumMapUnsafe(Map<? extends K, ? extends V> map) {
/* 301 */     return (ImmutableMap)copyOfEnumMap((EnumMap)map);
/*     */   }
/*     */ 
/*     */   
/*     */   private static <K extends Enum<K>, V> ImmutableMap<K, V> copyOfEnumMap(Map<K, ? extends V> original) {
/* 306 */     EnumMap<K, V> copy = new EnumMap<K, V>(original);
/* 307 */     for (Map.Entry<?, ?> entry : copy.entrySet()) {
/* 308 */       CollectPreconditions.checkEntryNotNull(entry.getKey(), entry.getValue());
/*     */     }
/* 310 */     return ImmutableEnumMap.asImmutable(copy);
/*     */   }
/*     */   
/* 313 */   private static final Map.Entry<?, ?>[] EMPTY_ENTRY_ARRAY = (Map.Entry<?, ?>[])new Map.Entry[0];
/*     */   
/*     */   private transient ImmutableSet<Map.Entry<K, V>> entrySet;
/*     */   
/*     */   private transient ImmutableSet<K> keySet;
/*     */   
/*     */   private transient ImmutableCollection<V> values;
/*     */   
/*     */   private transient ImmutableSetMultimap<K, V> multimapView;
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public final V put(K k, V v) {
/* 326 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public final V remove(Object o) {
/* 338 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public final void putAll(Map<? extends K, ? extends V> map) {
/* 350 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public final void clear() {
/* 362 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 367 */     return (size() == 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsKey(@Nullable Object key) {
/* 372 */     return (get(key) != null);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsValue(@Nullable Object value) {
/* 377 */     return values().contains(value);
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
/*     */   public ImmutableSet<Map.Entry<K, V>> entrySet() {
/* 392 */     ImmutableSet<Map.Entry<K, V>> result = this.entrySet;
/* 393 */     return (result == null) ? (this.entrySet = createEntrySet()) : result;
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
/*     */   public ImmutableSet<K> keySet() {
/* 406 */     ImmutableSet<K> result = this.keySet;
/* 407 */     return (result == null) ? (this.keySet = createKeySet()) : result;
/*     */   }
/*     */   
/*     */   ImmutableSet<K> createKeySet() {
/* 411 */     return new ImmutableMapKeySet<K, V>(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableCollection<V> values() {
/* 422 */     ImmutableCollection<V> result = this.values;
/* 423 */     return (result == null) ? (this.values = new ImmutableMapValues<K, V>(this)) : result;
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
/*     */   @Beta
/*     */   public ImmutableSetMultimap<K, V> asMultimap() {
/* 436 */     ImmutableSetMultimap<K, V> result = this.multimapView;
/* 437 */     return (result == null) ? (this.multimapView = createMultimapView()) : result;
/*     */   }
/*     */   
/*     */   private ImmutableSetMultimap<K, V> createMultimapView() {
/* 441 */     ImmutableMap<K, ImmutableSet<V>> map = viewMapValuesAsSingletonSets();
/* 442 */     return new ImmutableSetMultimap<K, V>(map, map.size(), null);
/*     */   }
/*     */   
/*     */   private ImmutableMap<K, ImmutableSet<V>> viewMapValuesAsSingletonSets() {
/* 446 */     return new MapViewOfValuesAsSingletonSets<K, V>(this);
/*     */   }
/*     */   
/*     */   private static final class MapViewOfValuesAsSingletonSets<K, V>
/*     */     extends ImmutableMap<K, ImmutableSet<V>> {
/*     */     private final ImmutableMap<K, V> delegate;
/*     */     
/*     */     MapViewOfValuesAsSingletonSets(ImmutableMap<K, V> delegate) {
/* 454 */       this.delegate = (ImmutableMap<K, V>)Preconditions.checkNotNull(delegate);
/*     */     }
/*     */     
/*     */     public int size() {
/* 458 */       return this.delegate.size();
/*     */     }
/*     */     
/*     */     public boolean containsKey(@Nullable Object key) {
/* 462 */       return this.delegate.containsKey(key);
/*     */     }
/*     */     
/*     */     public ImmutableSet<V> get(@Nullable Object key) {
/* 466 */       V outerValue = this.delegate.get(key);
/* 467 */       return (outerValue == null) ? null : ImmutableSet.<V>of(outerValue);
/*     */     }
/*     */     
/*     */     boolean isPartialView() {
/* 471 */       return false;
/*     */     }
/*     */     
/*     */     ImmutableSet<Map.Entry<K, ImmutableSet<V>>> createEntrySet() {
/* 475 */       return (ImmutableSet)new ImmutableMapEntrySet<K, ImmutableSet<ImmutableSet<V>>>() {
/*     */           ImmutableMap<K, ImmutableSet<V>> map() {
/* 477 */             return ImmutableMap.MapViewOfValuesAsSingletonSets.this;
/*     */           }
/*     */ 
/*     */           
/*     */           public UnmodifiableIterator<Map.Entry<K, ImmutableSet<V>>> iterator() {
/* 482 */             final Iterator<Map.Entry<K, V>> backingIterator = ImmutableMap.MapViewOfValuesAsSingletonSets.this.delegate.entrySet().iterator();
/* 483 */             return new UnmodifiableIterator<Map.Entry<K, ImmutableSet<V>>>() {
/*     */                 public boolean hasNext() {
/* 485 */                   return backingIterator.hasNext();
/*     */                 }
/*     */                 
/*     */                 public Map.Entry<K, ImmutableSet<V>> next() {
/* 489 */                   final Map.Entry<K, V> backingEntry = backingIterator.next();
/* 490 */                   return (Map.Entry)new AbstractMapEntry<K, ImmutableSet<ImmutableSet<V>>>() {
/*     */                       public K getKey() {
/* 492 */                         return (K)backingEntry.getKey();
/*     */                       }
/*     */                       
/*     */                       public ImmutableSet<V> getValue() {
/* 496 */                         return ImmutableSet.of((V)backingEntry.getValue());
/*     */                       }
/*     */                     };
/*     */                 }
/*     */               };
/*     */           }
/*     */         };
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean equals(@Nullable Object object) {
/* 507 */     return Maps.equalsImpl(this, object);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 515 */     return entrySet().hashCode();
/*     */   }
/*     */   
/*     */   public String toString() {
/* 519 */     return Maps.toStringImpl(this);
/*     */   }
/*     */ 
/*     */   
/*     */   static class SerializedForm
/*     */     implements Serializable
/*     */   {
/*     */     private final Object[] keys;
/*     */     private final Object[] values;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     SerializedForm(ImmutableMap<?, ?> map) {
/* 531 */       this.keys = new Object[map.size()];
/* 532 */       this.values = new Object[map.size()];
/* 533 */       int i = 0;
/* 534 */       for (Map.Entry<?, ?> entry : map.entrySet()) {
/* 535 */         this.keys[i] = entry.getKey();
/* 536 */         this.values[i] = entry.getValue();
/* 537 */         i++;
/*     */       } 
/*     */     }
/*     */     Object readResolve() {
/* 541 */       ImmutableMap.Builder<Object, Object> builder = new ImmutableMap.Builder<Object, Object>();
/* 542 */       return createMap(builder);
/*     */     }
/*     */     Object createMap(ImmutableMap.Builder<Object, Object> builder) {
/* 545 */       for (int i = 0; i < this.keys.length; i++) {
/* 546 */         builder.put(this.keys[i], this.values[i]);
/*     */       }
/* 548 */       return builder.build();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   Object writeReplace() {
/* 554 */     return new SerializedForm(this);
/*     */   }
/*     */   
/*     */   public abstract V get(@Nullable Object paramObject);
/*     */   
/*     */   abstract ImmutableSet<Map.Entry<K, V>> createEntrySet();
/*     */   
/*     */   abstract boolean isPartialView();
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\collect\ImmutableMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */