/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import java.io.Serializable;
/*     */ import java.util.Iterator;
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
/*     */ @GwtCompatible(serializable = true, emulated = true)
/*     */ class RegularImmutableBiMap<K, V>
/*     */   extends ImmutableBiMap<K, V>
/*     */ {
/*     */   static final double MAX_LOAD_FACTOR = 1.2D;
/*     */   private final transient ImmutableMapEntry<K, V>[] keyTable;
/*     */   private final transient ImmutableMapEntry<K, V>[] valueTable;
/*     */   private final transient ImmutableMapEntry<K, V>[] entries;
/*     */   private final transient int mask;
/*     */   private final transient int hashCode;
/*     */   private transient ImmutableBiMap<V, K> inverse;
/*     */   
/*     */   RegularImmutableBiMap(ImmutableMapEntry.TerminalEntry<?, ?>... entriesToAdd) {
/*  46 */     this(entriesToAdd.length, entriesToAdd);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   RegularImmutableBiMap(int n, ImmutableMapEntry.TerminalEntry<?, ?>[] entriesToAdd) {
/*  56 */     int tableSize = Hashing.closedTableSize(n, 1.2D);
/*  57 */     this.mask = tableSize - 1;
/*  58 */     ImmutableMapEntry[] arrayOfImmutableMapEntry1 = (ImmutableMapEntry[])createEntryArray(tableSize);
/*  59 */     ImmutableMapEntry[] arrayOfImmutableMapEntry2 = (ImmutableMapEntry[])createEntryArray(tableSize);
/*  60 */     ImmutableMapEntry[] arrayOfImmutableMapEntry3 = (ImmutableMapEntry[])createEntryArray(n);
/*  61 */     int hashCode = 0;
/*     */     
/*  63 */     for (int i = 0; i < n; i++) {
/*     */       
/*  65 */       ImmutableMapEntry.TerminalEntry<?, ?> terminalEntry = entriesToAdd[i];
/*  66 */       K key = (K)terminalEntry.getKey();
/*  67 */       V value = (V)terminalEntry.getValue();
/*     */       
/*  69 */       int keyHash = key.hashCode();
/*  70 */       int valueHash = value.hashCode();
/*  71 */       int keyBucket = Hashing.smear(keyHash) & this.mask;
/*  72 */       int valueBucket = Hashing.smear(valueHash) & this.mask;
/*     */       
/*  74 */       ImmutableMapEntry<K, V> nextInKeyBucket = arrayOfImmutableMapEntry1[keyBucket];
/*  75 */       for (ImmutableMapEntry<K, V> keyEntry = nextInKeyBucket; keyEntry != null; 
/*  76 */         keyEntry = keyEntry.getNextInKeyBucket()) {
/*  77 */         checkNoConflict(!key.equals(keyEntry.getKey()), "key", terminalEntry, keyEntry);
/*     */       }
/*  79 */       ImmutableMapEntry<K, V> nextInValueBucket = arrayOfImmutableMapEntry2[valueBucket];
/*  80 */       for (ImmutableMapEntry<K, V> valueEntry = nextInValueBucket; valueEntry != null; 
/*  81 */         valueEntry = valueEntry.getNextInValueBucket()) {
/*  82 */         checkNoConflict(!value.equals(valueEntry.getValue()), "value", terminalEntry, valueEntry);
/*     */       }
/*  84 */       ImmutableMapEntry<K, V> newEntry = (nextInKeyBucket == null && nextInValueBucket == null) ? (ImmutableMapEntry)terminalEntry : new NonTerminalBiMapEntry<K, V>((ImmutableMapEntry)terminalEntry, nextInKeyBucket, nextInValueBucket);
/*     */ 
/*     */ 
/*     */       
/*  88 */       arrayOfImmutableMapEntry1[keyBucket] = newEntry;
/*  89 */       arrayOfImmutableMapEntry2[valueBucket] = newEntry;
/*  90 */       arrayOfImmutableMapEntry3[i] = newEntry;
/*  91 */       hashCode += keyHash ^ valueHash;
/*     */     } 
/*     */     
/*  94 */     this.keyTable = (ImmutableMapEntry<K, V>[])arrayOfImmutableMapEntry1;
/*  95 */     this.valueTable = (ImmutableMapEntry<K, V>[])arrayOfImmutableMapEntry2;
/*  96 */     this.entries = (ImmutableMapEntry<K, V>[])arrayOfImmutableMapEntry3;
/*  97 */     this.hashCode = hashCode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   RegularImmutableBiMap(Map.Entry<?, ?>[] entriesToAdd) {
/* 104 */     int n = entriesToAdd.length;
/* 105 */     int tableSize = Hashing.closedTableSize(n, 1.2D);
/* 106 */     this.mask = tableSize - 1;
/* 107 */     ImmutableMapEntry[] arrayOfImmutableMapEntry1 = (ImmutableMapEntry[])createEntryArray(tableSize);
/* 108 */     ImmutableMapEntry[] arrayOfImmutableMapEntry2 = (ImmutableMapEntry[])createEntryArray(tableSize);
/* 109 */     ImmutableMapEntry[] arrayOfImmutableMapEntry3 = (ImmutableMapEntry[])createEntryArray(n);
/* 110 */     int hashCode = 0;
/*     */     
/* 112 */     for (int i = 0; i < n; i++) {
/*     */       
/* 114 */       Map.Entry<?, ?> entry = entriesToAdd[i];
/* 115 */       K key = (K)entry.getKey();
/* 116 */       V value = (V)entry.getValue();
/* 117 */       CollectPreconditions.checkEntryNotNull(key, value);
/* 118 */       int keyHash = key.hashCode();
/* 119 */       int valueHash = value.hashCode();
/* 120 */       int keyBucket = Hashing.smear(keyHash) & this.mask;
/* 121 */       int valueBucket = Hashing.smear(valueHash) & this.mask;
/*     */       
/* 123 */       ImmutableMapEntry<K, V> nextInKeyBucket = arrayOfImmutableMapEntry1[keyBucket];
/* 124 */       for (ImmutableMapEntry<K, V> keyEntry = nextInKeyBucket; keyEntry != null; 
/* 125 */         keyEntry = keyEntry.getNextInKeyBucket()) {
/* 126 */         checkNoConflict(!key.equals(keyEntry.getKey()), "key", entry, keyEntry);
/*     */       }
/* 128 */       ImmutableMapEntry<K, V> nextInValueBucket = arrayOfImmutableMapEntry2[valueBucket];
/* 129 */       for (ImmutableMapEntry<K, V> valueEntry = nextInValueBucket; valueEntry != null; 
/* 130 */         valueEntry = valueEntry.getNextInValueBucket()) {
/* 131 */         checkNoConflict(!value.equals(valueEntry.getValue()), "value", entry, valueEntry);
/*     */       }
/* 133 */       ImmutableMapEntry<K, V> newEntry = (nextInKeyBucket == null && nextInValueBucket == null) ? new ImmutableMapEntry.TerminalEntry<K, V>(key, value) : new NonTerminalBiMapEntry<K, V>(key, value, nextInKeyBucket, nextInValueBucket);
/*     */ 
/*     */ 
/*     */       
/* 137 */       arrayOfImmutableMapEntry1[keyBucket] = newEntry;
/* 138 */       arrayOfImmutableMapEntry2[valueBucket] = newEntry;
/* 139 */       arrayOfImmutableMapEntry3[i] = newEntry;
/* 140 */       hashCode += keyHash ^ valueHash;
/*     */     } 
/*     */     
/* 143 */     this.keyTable = (ImmutableMapEntry<K, V>[])arrayOfImmutableMapEntry1;
/* 144 */     this.valueTable = (ImmutableMapEntry<K, V>[])arrayOfImmutableMapEntry2;
/* 145 */     this.entries = (ImmutableMapEntry<K, V>[])arrayOfImmutableMapEntry3;
/* 146 */     this.hashCode = hashCode;
/*     */   }
/*     */   
/*     */   private static final class NonTerminalBiMapEntry<K, V>
/*     */     extends ImmutableMapEntry<K, V> {
/*     */     @Nullable
/*     */     private final ImmutableMapEntry<K, V> nextInKeyBucket;
/*     */     
/*     */     NonTerminalBiMapEntry(K key, V value, @Nullable ImmutableMapEntry<K, V> nextInKeyBucket, @Nullable ImmutableMapEntry<K, V> nextInValueBucket) {
/* 155 */       super(key, value);
/* 156 */       this.nextInKeyBucket = nextInKeyBucket;
/* 157 */       this.nextInValueBucket = nextInValueBucket;
/*     */     }
/*     */     @Nullable
/*     */     private final ImmutableMapEntry<K, V> nextInValueBucket;
/*     */     
/*     */     NonTerminalBiMapEntry(ImmutableMapEntry<K, V> contents, @Nullable ImmutableMapEntry<K, V> nextInKeyBucket, @Nullable ImmutableMapEntry<K, V> nextInValueBucket) {
/* 163 */       super(contents);
/* 164 */       this.nextInKeyBucket = nextInKeyBucket;
/* 165 */       this.nextInValueBucket = nextInValueBucket;
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     ImmutableMapEntry<K, V> getNextInKeyBucket() {
/* 171 */       return this.nextInKeyBucket;
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     ImmutableMapEntry<K, V> getNextInValueBucket() {
/* 177 */       return this.nextInValueBucket;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static <K, V> ImmutableMapEntry<K, V>[] createEntryArray(int length) {
/* 183 */     return (ImmutableMapEntry<K, V>[])new ImmutableMapEntry[length];
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public V get(@Nullable Object key) {
/* 189 */     if (key == null) {
/* 190 */       return null;
/*     */     }
/* 192 */     int bucket = Hashing.smear(key.hashCode()) & this.mask;
/* 193 */     for (ImmutableMapEntry<K, V> entry = this.keyTable[bucket]; entry != null; 
/* 194 */       entry = entry.getNextInKeyBucket()) {
/* 195 */       if (key.equals(entry.getKey())) {
/* 196 */         return entry.getValue();
/*     */       }
/*     */     } 
/* 199 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   ImmutableSet<Map.Entry<K, V>> createEntrySet() {
/* 204 */     return new ImmutableMapEntrySet<K, V>()
/*     */       {
/*     */         ImmutableMap<K, V> map() {
/* 207 */           return RegularImmutableBiMap.this;
/*     */         }
/*     */ 
/*     */         
/*     */         public UnmodifiableIterator<Map.Entry<K, V>> iterator() {
/* 212 */           return asList().iterator();
/*     */         }
/*     */ 
/*     */         
/*     */         ImmutableList<Map.Entry<K, V>> createAsList() {
/* 217 */           return new RegularImmutableAsList<Map.Entry<K, V>>(this, (Object[])RegularImmutableBiMap.this.entries);
/*     */         }
/*     */ 
/*     */         
/*     */         boolean isHashCodeFast() {
/* 222 */           return true;
/*     */         }
/*     */ 
/*     */         
/*     */         public int hashCode() {
/* 227 */           return RegularImmutableBiMap.this.hashCode;
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   boolean isPartialView() {
/* 234 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/* 239 */     return this.entries.length;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableBiMap<V, K> inverse() {
/* 246 */     ImmutableBiMap<V, K> result = this.inverse;
/* 247 */     return (result == null) ? (this.inverse = new Inverse()) : result;
/*     */   }
/*     */   
/*     */   private final class Inverse extends ImmutableBiMap<V, K> {
/*     */     private Inverse() {}
/*     */     
/*     */     public int size() {
/* 254 */       return inverse().size();
/*     */     }
/*     */ 
/*     */     
/*     */     public ImmutableBiMap<K, V> inverse() {
/* 259 */       return RegularImmutableBiMap.this;
/*     */     }
/*     */ 
/*     */     
/*     */     public K get(@Nullable Object value) {
/* 264 */       if (value == null) {
/* 265 */         return null;
/*     */       }
/* 267 */       int bucket = Hashing.smear(value.hashCode()) & RegularImmutableBiMap.this.mask;
/* 268 */       for (ImmutableMapEntry<K, V> entry = RegularImmutableBiMap.this.valueTable[bucket]; entry != null; 
/* 269 */         entry = entry.getNextInValueBucket()) {
/* 270 */         if (value.equals(entry.getValue())) {
/* 271 */           return entry.getKey();
/*     */         }
/*     */       } 
/* 274 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     ImmutableSet<Map.Entry<V, K>> createEntrySet() {
/* 279 */       return new InverseEntrySet();
/*     */     }
/*     */     
/*     */     final class InverseEntrySet
/*     */       extends ImmutableMapEntrySet<V, K> {
/*     */       ImmutableMap<V, K> map() {
/* 285 */         return RegularImmutableBiMap.Inverse.this;
/*     */       }
/*     */ 
/*     */       
/*     */       boolean isHashCodeFast() {
/* 290 */         return true;
/*     */       }
/*     */ 
/*     */       
/*     */       public int hashCode() {
/* 295 */         return RegularImmutableBiMap.this.hashCode;
/*     */       }
/*     */ 
/*     */       
/*     */       public UnmodifiableIterator<Map.Entry<V, K>> iterator() {
/* 300 */         return asList().iterator();
/*     */       }
/*     */ 
/*     */       
/*     */       ImmutableList<Map.Entry<V, K>> createAsList() {
/* 305 */         return new ImmutableAsList<Map.Entry<V, K>>()
/*     */           {
/*     */             public Map.Entry<V, K> get(int index) {
/* 308 */               Map.Entry<K, V> entry = RegularImmutableBiMap.this.entries[index];
/* 309 */               return Maps.immutableEntry(entry.getValue(), entry.getKey());
/*     */             }
/*     */ 
/*     */             
/*     */             ImmutableCollection<Map.Entry<V, K>> delegateCollection() {
/* 314 */               return RegularImmutableBiMap.Inverse.InverseEntrySet.this;
/*     */             }
/*     */           };
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     boolean isPartialView() {
/* 322 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     Object writeReplace() {
/* 327 */       return new RegularImmutableBiMap.InverseSerializedForm<Object, Object>(RegularImmutableBiMap.this);
/*     */     } }
/*     */   
/*     */   private static class InverseSerializedForm<K, V> implements Serializable {
/*     */     private final ImmutableBiMap<K, V> forward;
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/*     */     InverseSerializedForm(ImmutableBiMap<K, V> forward) {
/* 335 */       this.forward = forward;
/*     */     }
/*     */     
/*     */     Object readResolve() {
/* 339 */       return this.forward.inverse();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\collect\RegularImmutableBiMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */