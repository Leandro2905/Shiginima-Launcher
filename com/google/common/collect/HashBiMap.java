/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.AbstractMap;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.ConcurrentModificationException;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.NoSuchElementException;
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
/*     */ @GwtCompatible(emulated = true)
/*     */ public final class HashBiMap<K, V>
/*     */   extends AbstractMap<K, V>
/*     */   implements BiMap<K, V>, Serializable
/*     */ {
/*     */   private static final double LOAD_FACTOR = 1.0D;
/*     */   private transient BiEntry<K, V>[] hashTableKToV;
/*     */   private transient BiEntry<K, V>[] hashTableVToK;
/*     */   private transient int size;
/*     */   private transient int mask;
/*     */   private transient int modCount;
/*     */   private transient BiMap<V, K> inverse;
/*     */   @GwtIncompatible("Not needed in emulated source")
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   public static <K, V> HashBiMap<K, V> create() {
/*  58 */     return create(16);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> HashBiMap<K, V> create(int expectedSize) {
/*  68 */     return new HashBiMap<K, V>(expectedSize);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> HashBiMap<K, V> create(Map<? extends K, ? extends V> map) {
/*  76 */     HashBiMap<K, V> bimap = create(map.size());
/*  77 */     bimap.putAll(map);
/*  78 */     return bimap;
/*     */   }
/*     */   
/*     */   private static final class BiEntry<K, V>
/*     */     extends ImmutableEntry<K, V>
/*     */   {
/*     */     final int keyHash;
/*     */     final int valueHash;
/*     */     @Nullable
/*     */     BiEntry<K, V> nextInKToVBucket;
/*     */     @Nullable
/*     */     BiEntry<K, V> nextInVToKBucket;
/*     */     
/*     */     BiEntry(K key, int keyHash, V value, int valueHash) {
/*  92 */       super(key, value);
/*  93 */       this.keyHash = keyHash;
/*  94 */       this.valueHash = valueHash;
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
/*     */   private HashBiMap(int expectedSize) {
/* 107 */     init(expectedSize);
/*     */   }
/*     */   
/*     */   private void init(int expectedSize) {
/* 111 */     CollectPreconditions.checkNonnegative(expectedSize, "expectedSize");
/* 112 */     int tableSize = Hashing.closedTableSize(expectedSize, 1.0D);
/* 113 */     this.hashTableKToV = createTable(tableSize);
/* 114 */     this.hashTableVToK = createTable(tableSize);
/* 115 */     this.mask = tableSize - 1;
/* 116 */     this.modCount = 0;
/* 117 */     this.size = 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void delete(BiEntry<K, V> entry) {
/* 125 */     int keyBucket = entry.keyHash & this.mask;
/* 126 */     BiEntry<K, V> prevBucketEntry = null;
/* 127 */     BiEntry<K, V> bucketEntry = this.hashTableKToV[keyBucket];
/* 128 */     for (;; bucketEntry = bucketEntry.nextInKToVBucket) {
/* 129 */       if (bucketEntry == entry) {
/* 130 */         if (prevBucketEntry == null) {
/* 131 */           this.hashTableKToV[keyBucket] = entry.nextInKToVBucket; break;
/*     */         } 
/* 133 */         prevBucketEntry.nextInKToVBucket = entry.nextInKToVBucket;
/*     */         
/*     */         break;
/*     */       } 
/* 137 */       prevBucketEntry = bucketEntry;
/*     */     } 
/*     */     
/* 140 */     int valueBucket = entry.valueHash & this.mask;
/* 141 */     prevBucketEntry = null;
/* 142 */     BiEntry<K, V> biEntry1 = this.hashTableVToK[valueBucket];
/* 143 */     for (;; biEntry1 = biEntry1.nextInVToKBucket) {
/* 144 */       if (biEntry1 == entry) {
/* 145 */         if (prevBucketEntry == null) {
/* 146 */           this.hashTableVToK[valueBucket] = entry.nextInVToKBucket; break;
/*     */         } 
/* 148 */         prevBucketEntry.nextInVToKBucket = entry.nextInVToKBucket;
/*     */         
/*     */         break;
/*     */       } 
/* 152 */       prevBucketEntry = biEntry1;
/*     */     } 
/*     */     
/* 155 */     this.size--;
/* 156 */     this.modCount++;
/*     */   }
/*     */   
/*     */   private void insert(BiEntry<K, V> entry) {
/* 160 */     int keyBucket = entry.keyHash & this.mask;
/* 161 */     entry.nextInKToVBucket = this.hashTableKToV[keyBucket];
/* 162 */     this.hashTableKToV[keyBucket] = entry;
/*     */     
/* 164 */     int valueBucket = entry.valueHash & this.mask;
/* 165 */     entry.nextInVToKBucket = this.hashTableVToK[valueBucket];
/* 166 */     this.hashTableVToK[valueBucket] = entry;
/*     */     
/* 168 */     this.size++;
/* 169 */     this.modCount++;
/*     */   }
/*     */   
/*     */   private static int hash(@Nullable Object o) {
/* 173 */     return Hashing.smear((o == null) ? 0 : o.hashCode());
/*     */   }
/*     */   
/*     */   private BiEntry<K, V> seekByKey(@Nullable Object key, int keyHash) {
/* 177 */     for (BiEntry<K, V> entry = this.hashTableKToV[keyHash & this.mask]; entry != null; 
/* 178 */       entry = entry.nextInKToVBucket) {
/* 179 */       if (keyHash == entry.keyHash && Objects.equal(key, entry.key)) {
/* 180 */         return entry;
/*     */       }
/*     */     } 
/* 183 */     return null;
/*     */   }
/*     */   
/*     */   private BiEntry<K, V> seekByValue(@Nullable Object value, int valueHash) {
/* 187 */     for (BiEntry<K, V> entry = this.hashTableVToK[valueHash & this.mask]; entry != null; 
/* 188 */       entry = entry.nextInVToKBucket) {
/* 189 */       if (valueHash == entry.valueHash && Objects.equal(value, entry.value)) {
/* 190 */         return entry;
/*     */       }
/*     */     } 
/* 193 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsKey(@Nullable Object key) {
/* 198 */     return (seekByKey(key, hash(key)) != null);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsValue(@Nullable Object value) {
/* 203 */     return (seekByValue(value, hash(value)) != null);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public V get(@Nullable Object key) {
/* 209 */     BiEntry<K, V> entry = seekByKey(key, hash(key));
/* 210 */     return (entry == null) ? null : entry.value;
/*     */   }
/*     */ 
/*     */   
/*     */   public V put(@Nullable K key, @Nullable V value) {
/* 215 */     return put(key, value, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public V forcePut(@Nullable K key, @Nullable V value) {
/* 220 */     return put(key, value, true);
/*     */   }
/*     */   
/*     */   private V put(@Nullable K key, @Nullable V value, boolean force) {
/* 224 */     int keyHash = hash(key);
/* 225 */     int valueHash = hash(value);
/*     */     
/* 227 */     BiEntry<K, V> oldEntryForKey = seekByKey(key, keyHash);
/* 228 */     if (oldEntryForKey != null && valueHash == oldEntryForKey.valueHash && Objects.equal(value, oldEntryForKey.value))
/*     */     {
/* 230 */       return value;
/*     */     }
/*     */     
/* 233 */     BiEntry<K, V> oldEntryForValue = seekByValue(value, valueHash);
/* 234 */     if (oldEntryForValue != null) {
/* 235 */       if (force) {
/* 236 */         delete(oldEntryForValue);
/*     */       } else {
/* 238 */         String str = String.valueOf(String.valueOf(value)); throw new IllegalArgumentException((new StringBuilder(23 + str.length())).append("value already present: ").append(str).toString());
/*     */       } 
/*     */     }
/*     */     
/* 242 */     if (oldEntryForKey != null) {
/* 243 */       delete(oldEntryForKey);
/*     */     }
/* 245 */     BiEntry<K, V> newEntry = new BiEntry<K, V>(key, keyHash, value, valueHash);
/* 246 */     insert(newEntry);
/* 247 */     rehashIfNecessary();
/* 248 */     return (oldEntryForKey == null) ? null : oldEntryForKey.value;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private K putInverse(@Nullable V value, @Nullable K key, boolean force) {
/* 253 */     int valueHash = hash(value);
/* 254 */     int keyHash = hash(key);
/*     */     
/* 256 */     BiEntry<K, V> oldEntryForValue = seekByValue(value, valueHash);
/* 257 */     if (oldEntryForValue != null && keyHash == oldEntryForValue.keyHash && Objects.equal(key, oldEntryForValue.key))
/*     */     {
/* 259 */       return key;
/*     */     }
/*     */     
/* 262 */     BiEntry<K, V> oldEntryForKey = seekByKey(key, keyHash);
/* 263 */     if (oldEntryForKey != null) {
/* 264 */       if (force) {
/* 265 */         delete(oldEntryForKey);
/*     */       } else {
/* 267 */         String str = String.valueOf(String.valueOf(key)); throw new IllegalArgumentException((new StringBuilder(23 + str.length())).append("value already present: ").append(str).toString());
/*     */       } 
/*     */     }
/*     */     
/* 271 */     if (oldEntryForValue != null) {
/* 272 */       delete(oldEntryForValue);
/*     */     }
/* 274 */     BiEntry<K, V> newEntry = new BiEntry<K, V>(key, keyHash, value, valueHash);
/* 275 */     insert(newEntry);
/* 276 */     rehashIfNecessary();
/* 277 */     return (oldEntryForValue == null) ? null : oldEntryForValue.key;
/*     */   }
/*     */   
/*     */   private void rehashIfNecessary() {
/* 281 */     BiEntry<K, V>[] oldKToV = this.hashTableKToV;
/* 282 */     if (Hashing.needsResizing(this.size, oldKToV.length, 1.0D)) {
/* 283 */       int newTableSize = oldKToV.length * 2;
/*     */       
/* 285 */       this.hashTableKToV = createTable(newTableSize);
/* 286 */       this.hashTableVToK = createTable(newTableSize);
/* 287 */       this.mask = newTableSize - 1;
/* 288 */       this.size = 0;
/*     */       
/* 290 */       for (int bucket = 0; bucket < oldKToV.length; bucket++) {
/* 291 */         BiEntry<K, V> entry = oldKToV[bucket];
/* 292 */         while (entry != null) {
/* 293 */           BiEntry<K, V> nextEntry = entry.nextInKToVBucket;
/* 294 */           insert(entry);
/* 295 */           entry = nextEntry;
/*     */         } 
/*     */       } 
/* 298 */       this.modCount++;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private BiEntry<K, V>[] createTable(int length) {
/* 304 */     return (BiEntry<K, V>[])new BiEntry[length];
/*     */   }
/*     */ 
/*     */   
/*     */   public V remove(@Nullable Object key) {
/* 309 */     BiEntry<K, V> entry = seekByKey(key, hash(key));
/* 310 */     if (entry == null) {
/* 311 */       return null;
/*     */     }
/* 313 */     delete(entry);
/* 314 */     return entry.value;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() {
/* 320 */     this.size = 0;
/* 321 */     Arrays.fill((Object[])this.hashTableKToV, (Object)null);
/* 322 */     Arrays.fill((Object[])this.hashTableVToK, (Object)null);
/* 323 */     this.modCount++;
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/* 328 */     return this.size;
/*     */   }
/*     */   
/*     */   abstract class Itr<T> implements Iterator<T> {
/* 332 */     int nextBucket = 0;
/* 333 */     HashBiMap.BiEntry<K, V> next = null;
/* 334 */     HashBiMap.BiEntry<K, V> toRemove = null;
/* 335 */     int expectedModCount = HashBiMap.this.modCount;
/*     */     
/*     */     private void checkForConcurrentModification() {
/* 338 */       if (HashBiMap.this.modCount != this.expectedModCount) {
/* 339 */         throw new ConcurrentModificationException();
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean hasNext() {
/* 345 */       checkForConcurrentModification();
/* 346 */       if (this.next != null) {
/* 347 */         return true;
/*     */       }
/* 349 */       while (this.nextBucket < HashBiMap.this.hashTableKToV.length) {
/* 350 */         if (HashBiMap.this.hashTableKToV[this.nextBucket] != null) {
/* 351 */           this.next = HashBiMap.this.hashTableKToV[this.nextBucket++];
/* 352 */           return true;
/*     */         } 
/* 354 */         this.nextBucket++;
/*     */       } 
/* 356 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public T next() {
/* 361 */       checkForConcurrentModification();
/* 362 */       if (!hasNext()) {
/* 363 */         throw new NoSuchElementException();
/*     */       }
/*     */       
/* 366 */       HashBiMap.BiEntry<K, V> entry = this.next;
/* 367 */       this.next = entry.nextInKToVBucket;
/* 368 */       this.toRemove = entry;
/* 369 */       return output(entry);
/*     */     }
/*     */ 
/*     */     
/*     */     public void remove() {
/* 374 */       checkForConcurrentModification();
/* 375 */       CollectPreconditions.checkRemove((this.toRemove != null));
/* 376 */       HashBiMap.this.delete(this.toRemove);
/* 377 */       this.expectedModCount = HashBiMap.this.modCount;
/* 378 */       this.toRemove = null;
/*     */     }
/*     */ 
/*     */     
/*     */     abstract T output(HashBiMap.BiEntry<K, V> param1BiEntry);
/*     */   }
/*     */   
/*     */   public Set<K> keySet() {
/* 386 */     return new KeySet();
/*     */   }
/*     */   
/*     */   private final class KeySet extends Maps.KeySet<K, V> {
/*     */     KeySet() {
/* 391 */       super(HashBiMap.this);
/*     */     }
/*     */ 
/*     */     
/*     */     public Iterator<K> iterator() {
/* 396 */       return new HashBiMap<K, V>.Itr<K>()
/*     */         {
/*     */           K output(HashBiMap.BiEntry<K, V> entry) {
/* 399 */             return entry.key;
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean remove(@Nullable Object o) {
/* 406 */       HashBiMap.BiEntry<K, V> entry = HashBiMap.this.seekByKey(o, HashBiMap.hash(o));
/* 407 */       if (entry == null) {
/* 408 */         return false;
/*     */       }
/* 410 */       HashBiMap.this.delete(entry);
/* 411 */       return true;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<V> values() {
/* 418 */     return inverse().keySet();
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<Map.Entry<K, V>> entrySet() {
/* 423 */     return new EntrySet();
/*     */   }
/*     */   
/*     */   private final class EntrySet extends Maps.EntrySet<K, V> { private EntrySet() {}
/*     */     
/*     */     Map<K, V> map() {
/* 429 */       return HashBiMap.this;
/*     */     }
/*     */ 
/*     */     
/*     */     public Iterator<Map.Entry<K, V>> iterator() {
/* 434 */       return new HashBiMap<K, V>.Itr<Map.Entry<K, V>>()
/*     */         {
/*     */           Map.Entry<K, V> output(HashBiMap.BiEntry<K, V> entry) {
/* 437 */             return new MapEntry(entry);
/*     */           }
/*     */ 
/*     */           
/*     */           class MapEntry
/*     */             extends AbstractMapEntry<K, V>
/*     */           {
/*     */             HashBiMap.BiEntry<K, V> delegate;
/*     */ 
/*     */             
/*     */             public K getKey() {
/* 448 */               return this.delegate.key;
/*     */             }
/*     */             
/*     */             public V getValue() {
/* 452 */               return this.delegate.value;
/*     */             }
/*     */             
/*     */             public V setValue(V value) {
/* 456 */               V oldValue = this.delegate.value;
/* 457 */               int valueHash = HashBiMap.hash(value);
/* 458 */               if (valueHash == this.delegate.valueHash && Objects.equal(value, oldValue)) {
/* 459 */                 return value;
/*     */               }
/* 461 */               Preconditions.checkArgument((HashBiMap.this.seekByValue(value, valueHash) == null), "value already present: %s", new Object[] { value });
/*     */               
/* 463 */               HashBiMap.this.delete(this.delegate);
/* 464 */               HashBiMap.BiEntry<K, V> newEntry = new HashBiMap.BiEntry<K, V>(this.delegate.key, this.delegate.keyHash, value, valueHash);
/*     */               
/* 466 */               HashBiMap.this.insert(newEntry);
/* 467 */               HashBiMap.EntrySet.null.this.expectedModCount = HashBiMap.this.modCount;
/* 468 */               if (HashBiMap.EntrySet.null.this.toRemove == this.delegate) {
/* 469 */                 HashBiMap.EntrySet.null.this.toRemove = newEntry;
/*     */               }
/* 471 */               this.delegate = newEntry;
/* 472 */               return oldValue;
/*     */             }
/*     */           }
/*     */         };
/*     */     } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BiMap<V, K> inverse() {
/* 483 */     return (this.inverse == null) ? (this.inverse = new Inverse()) : this.inverse;
/*     */   }
/*     */   
/*     */   private final class Inverse extends AbstractMap<V, K> implements BiMap<V, K>, Serializable {
/*     */     BiMap<K, V> forward() {
/* 488 */       return HashBiMap.this;
/*     */     }
/*     */     private Inverse() {}
/*     */     
/*     */     public int size() {
/* 493 */       return HashBiMap.this.size;
/*     */     }
/*     */ 
/*     */     
/*     */     public void clear() {
/* 498 */       forward().clear();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean containsKey(@Nullable Object value) {
/* 503 */       return forward().containsValue(value);
/*     */     }
/*     */ 
/*     */     
/*     */     public K get(@Nullable Object value) {
/* 508 */       HashBiMap.BiEntry<K, V> entry = HashBiMap.this.seekByValue(value, HashBiMap.hash(value));
/* 509 */       return (entry == null) ? null : entry.key;
/*     */     }
/*     */ 
/*     */     
/*     */     public K put(@Nullable V value, @Nullable K key) {
/* 514 */       return HashBiMap.this.putInverse(value, key, false);
/*     */     }
/*     */ 
/*     */     
/*     */     public K forcePut(@Nullable V value, @Nullable K key) {
/* 519 */       return HashBiMap.this.putInverse(value, key, true);
/*     */     }
/*     */ 
/*     */     
/*     */     public K remove(@Nullable Object value) {
/* 524 */       HashBiMap.BiEntry<K, V> entry = HashBiMap.this.seekByValue(value, HashBiMap.hash(value));
/* 525 */       if (entry == null) {
/* 526 */         return null;
/*     */       }
/* 528 */       HashBiMap.this.delete(entry);
/* 529 */       return entry.key;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public BiMap<K, V> inverse() {
/* 535 */       return forward();
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<V> keySet() {
/* 540 */       return new InverseKeySet();
/*     */     }
/*     */     
/*     */     private final class InverseKeySet extends Maps.KeySet<V, K> {
/*     */       InverseKeySet() {
/* 545 */         super(HashBiMap.Inverse.this);
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean remove(@Nullable Object o) {
/* 550 */         HashBiMap.BiEntry<K, V> entry = HashBiMap.this.seekByValue(o, HashBiMap.hash(o));
/* 551 */         if (entry == null) {
/* 552 */           return false;
/*     */         }
/* 554 */         HashBiMap.this.delete(entry);
/* 555 */         return true;
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*     */       public Iterator<V> iterator() {
/* 561 */         return new HashBiMap<K, V>.Itr<V>() {
/*     */             V output(HashBiMap.BiEntry<K, V> entry) {
/* 563 */               return entry.value;
/*     */             }
/*     */           };
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<K> values() {
/* 571 */       return forward().keySet();
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<Map.Entry<V, K>> entrySet() {
/* 576 */       return new Maps.EntrySet<K, V>()
/*     */         {
/*     */           Map<V, K> map()
/*     */           {
/* 580 */             return HashBiMap.Inverse.this;
/*     */           }
/*     */ 
/*     */           
/*     */           public Iterator<Map.Entry<V, K>> iterator() {
/* 585 */             return new HashBiMap<K, V>.Itr<Map.Entry<V, K>>()
/*     */               {
/*     */                 Map.Entry<V, K> output(HashBiMap.BiEntry<K, V> entry) {
/* 588 */                   return new InverseEntry(entry);
/*     */                 }
/*     */ 
/*     */ 
/*     */                 
/*     */                 class InverseEntry
/*     */                   extends AbstractMapEntry<V, K>
/*     */                 {
/*     */                   HashBiMap.BiEntry<K, V> delegate;
/*     */ 
/*     */                   
/*     */                   public V getKey() {
/* 600 */                     return this.delegate.value;
/*     */                   }
/*     */ 
/*     */                   
/*     */                   public K getValue() {
/* 605 */                     return this.delegate.key;
/*     */                   }
/*     */ 
/*     */                   
/*     */                   public K setValue(K key) {
/* 610 */                     K oldKey = this.delegate.key;
/* 611 */                     int keyHash = HashBiMap.hash(key);
/* 612 */                     if (keyHash == this.delegate.keyHash && Objects.equal(key, oldKey)) {
/* 613 */                       return key;
/*     */                     }
/* 615 */                     Preconditions.checkArgument((HashBiMap.this.seekByKey(key, keyHash) == null), "value already present: %s", new Object[] { key });
/* 616 */                     HashBiMap.this.delete(this.delegate);
/* 617 */                     HashBiMap.BiEntry<K, V> newEntry = new HashBiMap.BiEntry<K, V>(key, keyHash, this.delegate.value, this.delegate.valueHash);
/*     */                     
/* 619 */                     HashBiMap.this.insert(newEntry);
/* 620 */                     HashBiMap.Inverse.null.null.this.expectedModCount = HashBiMap.this.modCount;
/*     */ 
/*     */                     
/* 623 */                     return oldKey;
/*     */                   }
/*     */                 }
/*     */               };
/*     */           }
/*     */         };
/*     */     }
/*     */     
/*     */     Object writeReplace() {
/* 632 */       return new HashBiMap.InverseSerializedForm<Object, Object>(HashBiMap.this);
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class InverseSerializedForm<K, V> implements Serializable {
/*     */     private final HashBiMap<K, V> bimap;
/*     */     
/*     */     InverseSerializedForm(HashBiMap<K, V> bimap) {
/* 640 */       this.bimap = bimap;
/*     */     }
/*     */     
/*     */     Object readResolve() {
/* 644 */       return this.bimap.inverse();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible("java.io.ObjectOutputStream")
/*     */   private void writeObject(ObjectOutputStream stream) throws IOException {
/* 653 */     stream.defaultWriteObject();
/* 654 */     Serialization.writeMap(this, stream);
/*     */   }
/*     */   
/*     */   @GwtIncompatible("java.io.ObjectInputStream")
/*     */   private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
/* 659 */     stream.defaultReadObject();
/* 660 */     int size = Serialization.readCount(stream);
/* 661 */     init(size);
/* 662 */     Serialization.populateMap(this, stream, size);
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\collect\HashBiMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */