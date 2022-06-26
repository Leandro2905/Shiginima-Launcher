/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Supplier;
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.EnumMap;
/*     */ import java.util.EnumSet;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.SortedSet;
/*     */ import java.util.TreeMap;
/*     */ import java.util.TreeSet;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public abstract class MultimapBuilder<K0, V0>
/*     */ {
/*     */   private static final int DEFAULT_EXPECTED_KEYS = 8;
/*     */   
/*     */   private MultimapBuilder() {}
/*     */   
/*     */   public static MultimapBuilderWithKeys<Object> hashKeys() {
/*  85 */     return hashKeys(8);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static MultimapBuilderWithKeys<Object> hashKeys(final int expectedKeys) {
/*  95 */     CollectPreconditions.checkNonnegative(expectedKeys, "expectedKeys");
/*  96 */     return new MultimapBuilderWithKeys()
/*     */       {
/*     */         <K, V> Map<K, Collection<V>> createMap() {
/*  99 */           return new HashMap<K, Collection<V>>(expectedKeys);
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
/*     */   
/*     */   public static MultimapBuilderWithKeys<Object> linkedHashKeys() {
/* 113 */     return linkedHashKeys(8);
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
/*     */   public static MultimapBuilderWithKeys<Object> linkedHashKeys(final int expectedKeys) {
/* 126 */     CollectPreconditions.checkNonnegative(expectedKeys, "expectedKeys");
/* 127 */     return new MultimapBuilderWithKeys()
/*     */       {
/*     */         <K, V> Map<K, Collection<V>> createMap() {
/* 130 */           return new LinkedHashMap<K, Collection<V>>(expectedKeys);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static MultimapBuilderWithKeys<Comparable> treeKeys() {
/* 147 */     return treeKeys(Ordering.natural());
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
/*     */   public static <K0> MultimapBuilderWithKeys<K0> treeKeys(final Comparator<K0> comparator) {
/* 164 */     Preconditions.checkNotNull(comparator);
/* 165 */     return new MultimapBuilderWithKeys<K0>()
/*     */       {
/*     */         <K extends K0, V> Map<K, Collection<V>> createMap() {
/* 168 */           return new TreeMap<K, Collection<V>>(comparator);
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K0 extends Enum<K0>> MultimapBuilderWithKeys<K0> enumKeys(final Class<K0> keyClass) {
/* 178 */     Preconditions.checkNotNull(keyClass);
/* 179 */     return new MultimapBuilderWithKeys<K0>()
/*     */       {
/*     */ 
/*     */         
/*     */         <K extends K0, V> Map<K, Collection<V>> createMap()
/*     */         {
/* 185 */           return (Map)new EnumMap<Enum, Collection<V>>(keyClass);
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   private static final class ArrayListSupplier<V> implements Supplier<List<V>>, Serializable {
/*     */     private final int expectedValuesPerKey;
/*     */     
/*     */     ArrayListSupplier(int expectedValuesPerKey) {
/* 194 */       this.expectedValuesPerKey = CollectPreconditions.checkNonnegative(expectedValuesPerKey, "expectedValuesPerKey");
/*     */     }
/*     */ 
/*     */     
/*     */     public List<V> get() {
/* 199 */       return new ArrayList<V>(this.expectedValuesPerKey);
/*     */     }
/*     */   }
/*     */   
/*     */   private enum LinkedListSupplier implements Supplier<List<Object>> {
/* 204 */     INSTANCE;
/*     */ 
/*     */ 
/*     */     
/*     */     public static <V> Supplier<List<V>> instance() {
/* 209 */       Supplier<List<V>> result = INSTANCE;
/* 210 */       return result;
/*     */     }
/*     */ 
/*     */     
/*     */     public List<Object> get() {
/* 215 */       return new LinkedList();
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class HashSetSupplier<V> implements Supplier<Set<V>>, Serializable {
/*     */     private final int expectedValuesPerKey;
/*     */     
/*     */     HashSetSupplier(int expectedValuesPerKey) {
/* 223 */       this.expectedValuesPerKey = CollectPreconditions.checkNonnegative(expectedValuesPerKey, "expectedValuesPerKey");
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<V> get() {
/* 228 */       return new HashSet<V>(this.expectedValuesPerKey);
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class LinkedHashSetSupplier<V> implements Supplier<Set<V>>, Serializable {
/*     */     private final int expectedValuesPerKey;
/*     */     
/*     */     LinkedHashSetSupplier(int expectedValuesPerKey) {
/* 236 */       this.expectedValuesPerKey = CollectPreconditions.checkNonnegative(expectedValuesPerKey, "expectedValuesPerKey");
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<V> get() {
/* 241 */       return new LinkedHashSet<V>(this.expectedValuesPerKey);
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class TreeSetSupplier<V> implements Supplier<SortedSet<V>>, Serializable {
/*     */     private final Comparator<? super V> comparator;
/*     */     
/*     */     TreeSetSupplier(Comparator<? super V> comparator) {
/* 249 */       this.comparator = (Comparator<? super V>)Preconditions.checkNotNull(comparator);
/*     */     }
/*     */ 
/*     */     
/*     */     public SortedSet<V> get() {
/* 254 */       return new TreeSet<V>(this.comparator);
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class EnumSetSupplier<V extends Enum<V>>
/*     */     implements Supplier<Set<V>>, Serializable {
/*     */     private final Class<V> clazz;
/*     */     
/*     */     EnumSetSupplier(Class<V> clazz) {
/* 263 */       this.clazz = (Class<V>)Preconditions.checkNotNull(clazz);
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<V> get() {
/* 268 */       return EnumSet.noneOf(this.clazz);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static abstract class MultimapBuilderWithKeys<K0>
/*     */   {
/*     */     private static final int DEFAULT_EXPECTED_VALUES_PER_KEY = 2;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     abstract <K extends K0, V> Map<K, Collection<V>> createMap();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public MultimapBuilder.ListMultimapBuilder<K0, Object> arrayListValues() {
/* 290 */       return arrayListValues(2);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public MultimapBuilder.ListMultimapBuilder<K0, Object> arrayListValues(final int expectedValuesPerKey) {
/* 300 */       CollectPreconditions.checkNonnegative(expectedValuesPerKey, "expectedValuesPerKey");
/* 301 */       return new MultimapBuilder.ListMultimapBuilder<K0, Object>()
/*     */         {
/*     */           public <K extends K0, V> ListMultimap<K, V> build() {
/* 304 */             return Multimaps.newListMultimap(MultimapBuilder.MultimapBuilderWithKeys.this.createMap(), new MultimapBuilder.ArrayListSupplier(expectedValuesPerKey));
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public MultimapBuilder.ListMultimapBuilder<K0, Object> linkedListValues() {
/* 315 */       return new MultimapBuilder.ListMultimapBuilder<K0, Object>()
/*     */         {
/*     */           public <K extends K0, V> ListMultimap<K, V> build() {
/* 318 */             return Multimaps.newListMultimap(MultimapBuilder.MultimapBuilderWithKeys.this.createMap(), (Supplier)MultimapBuilder.LinkedListSupplier.instance());
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public MultimapBuilder.SetMultimapBuilder<K0, Object> hashSetValues() {
/* 329 */       return hashSetValues(2);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public MultimapBuilder.SetMultimapBuilder<K0, Object> hashSetValues(final int expectedValuesPerKey) {
/* 339 */       CollectPreconditions.checkNonnegative(expectedValuesPerKey, "expectedValuesPerKey");
/* 340 */       return new MultimapBuilder.SetMultimapBuilder<K0, Object>()
/*     */         {
/*     */           public <K extends K0, V> SetMultimap<K, V> build() {
/* 343 */             return Multimaps.newSetMultimap(MultimapBuilder.MultimapBuilderWithKeys.this.createMap(), new MultimapBuilder.HashSetSupplier(expectedValuesPerKey));
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public MultimapBuilder.SetMultimapBuilder<K0, Object> linkedHashSetValues() {
/* 354 */       return linkedHashSetValues(2);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public MultimapBuilder.SetMultimapBuilder<K0, Object> linkedHashSetValues(final int expectedValuesPerKey) {
/* 364 */       CollectPreconditions.checkNonnegative(expectedValuesPerKey, "expectedValuesPerKey");
/* 365 */       return new MultimapBuilder.SetMultimapBuilder<K0, Object>()
/*     */         {
/*     */           public <K extends K0, V> SetMultimap<K, V> build() {
/* 368 */             return Multimaps.newSetMultimap(MultimapBuilder.MultimapBuilderWithKeys.this.createMap(), new MultimapBuilder.LinkedHashSetSupplier(expectedValuesPerKey));
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public MultimapBuilder.SortedSetMultimapBuilder<K0, Comparable> treeSetValues() {
/* 380 */       return treeSetValues(Ordering.natural());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public <V0> MultimapBuilder.SortedSetMultimapBuilder<K0, V0> treeSetValues(final Comparator<V0> comparator) {
/* 390 */       Preconditions.checkNotNull(comparator, "comparator");
/* 391 */       return new MultimapBuilder.SortedSetMultimapBuilder<K0, V0>()
/*     */         {
/*     */           public <K extends K0, V extends V0> SortedSetMultimap<K, V> build() {
/* 394 */             return Multimaps.newSortedSetMultimap(MultimapBuilder.MultimapBuilderWithKeys.this.createMap(), new MultimapBuilder.TreeSetSupplier(comparator));
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public <V0 extends Enum<V0>> MultimapBuilder.SetMultimapBuilder<K0, V0> enumSetValues(final Class<V0> valueClass) {
/* 406 */       Preconditions.checkNotNull(valueClass, "valueClass");
/* 407 */       return new MultimapBuilder.SetMultimapBuilder<K0, V0>()
/*     */         {
/*     */ 
/*     */           
/*     */           public <K extends K0, V extends V0> SetMultimap<K, V> build()
/*     */           {
/* 413 */             Supplier<Set<V>> factory = (Supplier)new MultimapBuilder.EnumSetSupplier<Enum>(valueClass);
/* 414 */             return Multimaps.newSetMultimap(MultimapBuilder.MultimapBuilderWithKeys.this.createMap(), factory);
/*     */           }
/*     */         };
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
/*     */   public <K extends K0, V extends V0> Multimap<K, V> build(Multimap<? extends K, ? extends V> multimap) {
/* 433 */     Multimap<K, V> result = build();
/* 434 */     result.putAll(multimap);
/* 435 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract <K extends K0, V extends V0> Multimap<K, V> build();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static abstract class ListMultimapBuilder<K0, V0>
/*     */     extends MultimapBuilder<K0, V0>
/*     */   {
/*     */     public <K extends K0, V extends V0> ListMultimap<K, V> build(Multimap<? extends K, ? extends V> multimap) {
/* 450 */       return (ListMultimap<K, V>)super.<K, V>build(multimap);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public abstract <K extends K0, V extends V0> ListMultimap<K, V> build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static abstract class SetMultimapBuilder<K0, V0>
/*     */     extends MultimapBuilder<K0, V0>
/*     */   {
/*     */     public <K extends K0, V extends V0> SetMultimap<K, V> build(Multimap<? extends K, ? extends V> multimap) {
/* 466 */       return (SetMultimap<K, V>)super.<K, V>build(multimap);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public abstract <K extends K0, V extends V0> SetMultimap<K, V> build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static abstract class SortedSetMultimapBuilder<K0, V0>
/*     */     extends SetMultimapBuilder<K0, V0>
/*     */   {
/*     */     public <K extends K0, V extends V0> SortedSetMultimap<K, V> build(Multimap<? extends K, ? extends V> multimap) {
/* 482 */       return (SortedSetMultimap<K, V>)super.<K, V>build(multimap);
/*     */     }
/*     */     
/*     */     public abstract <K extends K0, V extends V0> SortedSetMultimap<K, V> build();
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\collect\MultimapBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */