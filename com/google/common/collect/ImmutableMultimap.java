/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.io.Serializable;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
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
/*     */ @GwtCompatible(emulated = true)
/*     */ public abstract class ImmutableMultimap<K, V>
/*     */   extends AbstractMultimap<K, V>
/*     */   implements Serializable
/*     */ {
/*     */   final transient ImmutableMap<K, ? extends ImmutableCollection<V>> map;
/*     */   final transient int size;
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   public static <K, V> ImmutableMultimap<K, V> of() {
/*  70 */     return ImmutableListMultimap.of();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableMultimap<K, V> of(K k1, V v1) {
/*  77 */     return ImmutableListMultimap.of(k1, v1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableMultimap<K, V> of(K k1, V v1, K k2, V v2) {
/*  84 */     return ImmutableListMultimap.of(k1, v1, k2, v2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableMultimap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3) {
/*  92 */     return ImmutableListMultimap.of(k1, v1, k2, v2, k3, v3);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableMultimap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
/* 100 */     return ImmutableListMultimap.of(k1, v1, k2, v2, k3, v3, k4, v4);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableMultimap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
/* 108 */     return ImmutableListMultimap.of(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> Builder<K, V> builder() {
/* 118 */     return new Builder<K, V>();
/*     */   }
/*     */ 
/*     */   
/*     */   private static class BuilderMultimap<K, V>
/*     */     extends AbstractMapBasedMultimap<K, V>
/*     */   {
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     BuilderMultimap() {
/* 128 */       super(new LinkedHashMap<K, Collection<V>>());
/*     */     }
/*     */     Collection<V> createCollection() {
/* 131 */       return Lists.newArrayList();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class Builder<K, V>
/*     */   {
/* 155 */     Multimap<K, V> builderMultimap = new ImmutableMultimap.BuilderMultimap<K, V>();
/*     */ 
/*     */ 
/*     */     
/*     */     Comparator<? super K> keyComparator;
/*     */ 
/*     */ 
/*     */     
/*     */     Comparator<? super V> valueComparator;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder<K, V> put(K key, V value) {
/* 169 */       CollectPreconditions.checkEntryNotNull(key, value);
/* 170 */       this.builderMultimap.put(key, value);
/* 171 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder<K, V> put(Map.Entry<? extends K, ? extends V> entry) {
/* 180 */       return put(entry.getKey(), entry.getValue());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder<K, V> putAll(K key, Iterable<? extends V> values) {
/* 191 */       if (key == null) {
/* 192 */         String.valueOf(Iterables.toString(values)); throw new NullPointerException((String.valueOf(Iterables.toString(values)).length() != 0) ? "null key in entry: null=".concat(String.valueOf(Iterables.toString(values))) : new String("null key in entry: null="));
/*     */       } 
/*     */       
/* 195 */       Collection<V> valueList = this.builderMultimap.get(key);
/* 196 */       for (V value : values) {
/* 197 */         CollectPreconditions.checkEntryNotNull(key, value);
/* 198 */         valueList.add(value);
/*     */       } 
/* 200 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder<K, V> putAll(K key, V... values) {
/* 210 */       return putAll(key, Arrays.asList(values));
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
/*     */     public Builder<K, V> putAll(Multimap<? extends K, ? extends V> multimap) {
/* 224 */       for (Map.Entry<? extends K, ? extends Collection<? extends V>> entry : (Iterable<Map.Entry<? extends K, ? extends Collection<? extends V>>>)multimap.asMap().entrySet()) {
/* 225 */         putAll(entry.getKey(), entry.getValue());
/*     */       }
/* 227 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder<K, V> orderKeysBy(Comparator<? super K> keyComparator) {
/* 236 */       this.keyComparator = (Comparator<? super K>)Preconditions.checkNotNull(keyComparator);
/* 237 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder<K, V> orderValuesBy(Comparator<? super V> valueComparator) {
/* 246 */       this.valueComparator = (Comparator<? super V>)Preconditions.checkNotNull(valueComparator);
/* 247 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ImmutableMultimap<K, V> build() {
/* 254 */       if (this.valueComparator != null) {
/* 255 */         for (Collection<V> values : (Iterable<Collection<V>>)this.builderMultimap.asMap().values()) {
/* 256 */           List<V> list = (List<V>)values;
/* 257 */           Collections.sort(list, this.valueComparator);
/*     */         } 
/*     */       }
/* 260 */       if (this.keyComparator != null) {
/* 261 */         Multimap<K, V> sortedCopy = new ImmutableMultimap.BuilderMultimap<K, V>();
/* 262 */         List<Map.Entry<K, Collection<V>>> entries = Lists.newArrayList(this.builderMultimap.asMap().entrySet());
/*     */         
/* 264 */         Collections.sort(entries, Ordering.<K>from(this.keyComparator).onKeys());
/*     */ 
/*     */         
/* 267 */         for (Map.Entry<K, Collection<V>> entry : entries) {
/* 268 */           sortedCopy.putAll(entry.getKey(), entry.getValue());
/*     */         }
/* 270 */         this.builderMultimap = sortedCopy;
/*     */       } 
/* 272 */       return ImmutableMultimap.copyOf(this.builderMultimap);
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
/*     */   public static <K, V> ImmutableMultimap<K, V> copyOf(Multimap<? extends K, ? extends V> multimap) {
/* 290 */     if (multimap instanceof ImmutableMultimap) {
/*     */       
/* 292 */       ImmutableMultimap<K, V> kvMultimap = (ImmutableMultimap)multimap;
/*     */       
/* 294 */       if (!kvMultimap.isPartialView()) {
/* 295 */         return kvMultimap;
/*     */       }
/*     */     } 
/* 298 */     return ImmutableListMultimap.copyOf(multimap);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible("java serialization is not supported")
/*     */   static class FieldSettersHolder
/*     */   {
/* 310 */     static final Serialization.FieldSetter<ImmutableMultimap> MAP_FIELD_SETTER = Serialization.getFieldSetter(ImmutableMultimap.class, "map");
/*     */ 
/*     */     
/* 313 */     static final Serialization.FieldSetter<ImmutableMultimap> SIZE_FIELD_SETTER = Serialization.getFieldSetter(ImmutableMultimap.class, "size");
/*     */ 
/*     */     
/* 316 */     static final Serialization.FieldSetter<ImmutableSetMultimap> EMPTY_SET_FIELD_SETTER = Serialization.getFieldSetter(ImmutableSetMultimap.class, "emptySet");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   ImmutableMultimap(ImmutableMap<K, ? extends ImmutableCollection<V>> map, int size) {
/* 322 */     this.map = map;
/* 323 */     this.size = size;
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
/*     */   @Deprecated
/*     */   public ImmutableCollection<V> removeAll(Object key) {
/* 337 */     throw new UnsupportedOperationException();
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
/*     */   @Deprecated
/*     */   public ImmutableCollection<V> replaceValues(K key, Iterable<? extends V> values) {
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
/*     */   public void clear() {
/* 362 */     throw new UnsupportedOperationException();
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
/*     */   @Deprecated
/*     */   public boolean put(K key, V value) {
/* 392 */     throw new UnsupportedOperationException();
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
/*     */   public boolean putAll(K key, Iterable<? extends V> values) {
/* 404 */     throw new UnsupportedOperationException();
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
/*     */   public boolean putAll(Multimap<? extends K, ? extends V> multimap) {
/* 416 */     throw new UnsupportedOperationException();
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
/*     */   public boolean remove(Object key, Object value) {
/* 428 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean isPartialView() {
/* 438 */     return this.map.isPartialView();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean containsKey(@Nullable Object key) {
/* 445 */     return this.map.containsKey(key);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsValue(@Nullable Object value) {
/* 450 */     return (value != null && super.containsValue(value));
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/* 455 */     return this.size;
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
/*     */   public ImmutableSet<K> keySet() {
/* 467 */     return this.map.keySet();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableMap<K, Collection<V>> asMap() {
/* 477 */     return (ImmutableMap)this.map;
/*     */   }
/*     */ 
/*     */   
/*     */   Map<K, Collection<V>> createAsMap() {
/* 482 */     throw new AssertionError("should never be called");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableCollection<Map.Entry<K, V>> entries() {
/* 492 */     return (ImmutableCollection<Map.Entry<K, V>>)super.entries();
/*     */   }
/*     */ 
/*     */   
/*     */   ImmutableCollection<Map.Entry<K, V>> createEntries() {
/* 497 */     return new EntryCollection<K, V>(this);
/*     */   }
/*     */   
/*     */   private static class EntryCollection<K, V> extends ImmutableCollection<Map.Entry<K, V>> {
/*     */     final ImmutableMultimap<K, V> multimap;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     EntryCollection(ImmutableMultimap<K, V> multimap) {
/* 505 */       this.multimap = multimap;
/*     */     }
/*     */     
/*     */     public UnmodifiableIterator<Map.Entry<K, V>> iterator() {
/* 509 */       return this.multimap.entryIterator();
/*     */     }
/*     */     
/*     */     boolean isPartialView() {
/* 513 */       return this.multimap.isPartialView();
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 518 */       return this.multimap.size();
/*     */     }
/*     */     
/*     */     public boolean contains(Object object) {
/* 522 */       if (object instanceof Map.Entry) {
/* 523 */         Map.Entry<?, ?> entry = (Map.Entry<?, ?>)object;
/* 524 */         return this.multimap.containsEntry(entry.getKey(), entry.getValue());
/*     */       } 
/* 526 */       return false;
/*     */     }
/*     */   }
/*     */   
/*     */   private abstract class Itr<T>
/*     */     extends UnmodifiableIterator<T>
/*     */   {
/* 533 */     final Iterator<Map.Entry<K, Collection<V>>> mapIterator = ImmutableMultimap.this.asMap().entrySet().iterator();
/* 534 */     K key = null;
/* 535 */     Iterator<V> valueIterator = Iterators.emptyIterator();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean hasNext() {
/* 541 */       return (this.mapIterator.hasNext() || this.valueIterator.hasNext());
/*     */     }
/*     */ 
/*     */     
/*     */     public T next() {
/* 546 */       if (!this.valueIterator.hasNext()) {
/* 547 */         Map.Entry<K, Collection<V>> mapEntry = this.mapIterator.next();
/* 548 */         this.key = mapEntry.getKey();
/* 549 */         this.valueIterator = ((Collection<V>)mapEntry.getValue()).iterator();
/*     */       } 
/* 551 */       return output(this.key, this.valueIterator.next());
/*     */     }
/*     */     private Itr() {}
/*     */     abstract T output(K param1K, V param1V); }
/*     */   
/*     */   UnmodifiableIterator<Map.Entry<K, V>> entryIterator() {
/* 557 */     return new Itr<Map.Entry<K, V>>()
/*     */       {
/*     */         Map.Entry<K, V> output(K key, V value) {
/* 560 */           return Maps.immutableEntry(key, value);
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableMultiset<K> keys() {
/* 573 */     return (ImmutableMultiset<K>)super.keys();
/*     */   }
/*     */ 
/*     */   
/*     */   ImmutableMultiset<K> createKeys() {
/* 578 */     return new Keys();
/*     */   }
/*     */   
/*     */   class Keys
/*     */     extends ImmutableMultiset<K>
/*     */   {
/*     */     public boolean contains(@Nullable Object object) {
/* 585 */       return ImmutableMultimap.this.containsKey(object);
/*     */     }
/*     */ 
/*     */     
/*     */     public int count(@Nullable Object element) {
/* 590 */       Collection<V> values = (Collection<V>)ImmutableMultimap.this.map.get(element);
/* 591 */       return (values == null) ? 0 : values.size();
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<K> elementSet() {
/* 596 */       return ImmutableMultimap.this.keySet();
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 601 */       return ImmutableMultimap.this.size();
/*     */     }
/*     */ 
/*     */     
/*     */     Multiset.Entry<K> getEntry(int index) {
/* 606 */       Map.Entry<K, ? extends Collection<V>> entry = ImmutableMultimap.this.map.entrySet().asList().get(index);
/* 607 */       return Multisets.immutableEntry(entry.getKey(), ((Collection)entry.getValue()).size());
/*     */     }
/*     */ 
/*     */     
/*     */     boolean isPartialView() {
/* 612 */       return true;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableCollection<V> values() {
/* 623 */     return (ImmutableCollection<V>)super.values();
/*     */   }
/*     */ 
/*     */   
/*     */   ImmutableCollection<V> createValues() {
/* 628 */     return new Values<K, V>(this);
/*     */   }
/*     */ 
/*     */   
/*     */   UnmodifiableIterator<V> valueIterator() {
/* 633 */     return new Itr<V>()
/*     */       {
/*     */         V output(K key, V value) {
/* 636 */           return value;
/*     */         }
/*     */       };
/*     */   }
/*     */   public abstract ImmutableCollection<V> get(K paramK);
/*     */   public abstract ImmutableMultimap<V, K> inverse();
/*     */   private static final class Values<K, V> extends ImmutableCollection<V> { private final transient ImmutableMultimap<K, V> multimap;
/*     */     
/*     */     Values(ImmutableMultimap<K, V> multimap) {
/* 645 */       this.multimap = multimap;
/*     */     }
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     public boolean contains(@Nullable Object object) {
/* 650 */       return this.multimap.containsValue(object);
/*     */     }
/*     */     
/*     */     public UnmodifiableIterator<V> iterator() {
/* 654 */       return this.multimap.valueIterator();
/*     */     }
/*     */ 
/*     */     
/*     */     @GwtIncompatible("not present in emulated superclass")
/*     */     int copyIntoArray(Object[] dst, int offset) {
/* 660 */       for (ImmutableCollection<V> valueCollection : this.multimap.map.values()) {
/* 661 */         offset = valueCollection.copyIntoArray(dst, offset);
/*     */       }
/* 663 */       return offset;
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 668 */       return this.multimap.size();
/*     */     }
/*     */     
/*     */     boolean isPartialView() {
/* 672 */       return true;
/*     */     } }
/*     */ 
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\collect\ImmutableMultimap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */