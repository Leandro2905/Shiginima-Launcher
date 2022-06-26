/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.base.Equivalence;
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.lang.ref.ReferenceQueue;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import java.util.concurrent.atomic.AtomicReferenceArray;
/*     */ import javax.annotation.Nullable;
/*     */ import javax.annotation.concurrent.GuardedBy;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class ComputingConcurrentHashMap<K, V>
/*     */   extends MapMakerInternalMap<K, V>
/*     */ {
/*     */   final Function<? super K, ? extends V> computingFunction;
/*     */   private static final long serialVersionUID = 4L;
/*     */   
/*     */   ComputingConcurrentHashMap(MapMaker builder, Function<? super K, ? extends V> computingFunction) {
/*  51 */     super(builder);
/*  52 */     this.computingFunction = (Function<? super K, ? extends V>)Preconditions.checkNotNull(computingFunction);
/*     */   }
/*     */ 
/*     */   
/*     */   MapMakerInternalMap.Segment<K, V> createSegment(int initialCapacity, int maxSegmentSize) {
/*  57 */     return new ComputingSegment<K, V>(this, initialCapacity, maxSegmentSize);
/*     */   }
/*     */ 
/*     */   
/*     */   ComputingSegment<K, V> segmentFor(int hash) {
/*  62 */     return (ComputingSegment<K, V>)super.segmentFor(hash);
/*     */   }
/*     */   
/*     */   V getOrCompute(K key) throws ExecutionException {
/*  66 */     int hash = hash(Preconditions.checkNotNull(key));
/*  67 */     return segmentFor(hash).getOrCompute(key, hash, this.computingFunction);
/*     */   }
/*     */   
/*     */   static final class ComputingSegment<K, V>
/*     */     extends MapMakerInternalMap.Segment<K, V> {
/*     */     ComputingSegment(MapMakerInternalMap<K, V> map, int initialCapacity, int maxSegmentSize) {
/*  73 */       super(map, initialCapacity, maxSegmentSize);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     V getOrCompute(K key, int hash, Function<? super K, ? extends V> computingFunction) throws ExecutionException {
/*     */       try {
/*     */         while (true) {
/*  81 */           MapMakerInternalMap.ReferenceEntry<K, V> e = getEntry(key, hash);
/*  82 */           if (e != null) {
/*  83 */             V v = getLiveValue(e);
/*  84 */             if (v != null) {
/*  85 */               recordRead(e);
/*  86 */               return v;
/*     */             } 
/*     */           } 
/*     */ 
/*     */ 
/*     */           
/*  92 */           if (e == null || !e.getValueReference().isComputingReference()) {
/*  93 */             boolean createNewEntry = true;
/*  94 */             ComputingConcurrentHashMap.ComputingValueReference<K, V> computingValueReference = null;
/*  95 */             lock();
/*     */             try {
/*  97 */               preWriteCleanup();
/*     */               
/*  99 */               int newCount = this.count - 1;
/* 100 */               AtomicReferenceArray<MapMakerInternalMap.ReferenceEntry<K, V>> table = this.table;
/* 101 */               int index = hash & table.length() - 1;
/* 102 */               MapMakerInternalMap.ReferenceEntry<K, V> first = table.get(index);
/*     */               
/* 104 */               for (e = first; e != null; e = e.getNext()) {
/* 105 */                 K entryKey = e.getKey();
/* 106 */                 if (e.getHash() == hash && entryKey != null && this.map.keyEquivalence.equivalent(key, entryKey)) {
/*     */                   
/* 108 */                   MapMakerInternalMap.ValueReference<K, V> valueReference = e.getValueReference();
/* 109 */                   if (valueReference.isComputingReference()) {
/* 110 */                     createNewEntry = false; break;
/*     */                   } 
/* 112 */                   V v = (V)e.getValueReference().get();
/* 113 */                   if (v == null) {
/* 114 */                     enqueueNotification(entryKey, hash, v, MapMaker.RemovalCause.COLLECTED);
/* 115 */                   } else if (this.map.expires() && this.map.isExpired(e)) {
/*     */ 
/*     */                     
/* 118 */                     enqueueNotification(entryKey, hash, v, MapMaker.RemovalCause.EXPIRED);
/*     */                   } else {
/* 120 */                     recordLockedRead(e);
/* 121 */                     return v;
/*     */                   } 
/*     */ 
/*     */                   
/* 125 */                   this.evictionQueue.remove(e);
/* 126 */                   this.expirationQueue.remove(e);
/* 127 */                   this.count = newCount;
/*     */                   
/*     */                   break;
/*     */                 } 
/*     */               } 
/*     */               
/* 133 */               if (createNewEntry) {
/* 134 */                 computingValueReference = new ComputingConcurrentHashMap.ComputingValueReference<K, V>(computingFunction);
/*     */                 
/* 136 */                 if (e == null) {
/* 137 */                   e = newEntry(key, hash, first);
/* 138 */                   e.setValueReference(computingValueReference);
/* 139 */                   table.set(index, e);
/*     */                 } else {
/* 141 */                   e.setValueReference(computingValueReference);
/*     */                 } 
/*     */               } 
/*     */             } finally {
/* 145 */               unlock();
/* 146 */               postWriteCleanup();
/*     */             } 
/*     */             
/* 149 */             if (createNewEntry)
/*     */             {
/* 151 */               return compute(key, hash, e, computingValueReference);
/*     */             }
/*     */           } 
/*     */ 
/*     */           
/* 156 */           Preconditions.checkState(!Thread.holdsLock(e), "Recursive computation");
/*     */           
/* 158 */           V value = (V)e.getValueReference().waitForValue();
/* 159 */           if (value != null) {
/* 160 */             recordRead(e);
/* 161 */             return value;
/*     */           }
/*     */         
/*     */         } 
/*     */       } finally {
/*     */         
/* 167 */         postReadCleanup();
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     V compute(K key, int hash, MapMakerInternalMap.ReferenceEntry<K, V> e, ComputingConcurrentHashMap.ComputingValueReference<K, V> computingValueReference) throws ExecutionException {
/* 174 */       V value = null;
/* 175 */       long start = System.nanoTime();
/* 176 */       long end = 0L;
/*     */ 
/*     */ 
/*     */       
/*     */       try {
/* 181 */         synchronized (e) {
/* 182 */           value = computingValueReference.compute(key, hash);
/* 183 */           end = System.nanoTime();
/*     */         } 
/* 185 */         if (value != null) {
/*     */           
/* 187 */           V oldValue = put(key, hash, value, true);
/* 188 */           if (oldValue != null)
/*     */           {
/* 190 */             enqueueNotification(key, hash, value, MapMaker.RemovalCause.REPLACED);
/*     */           }
/*     */         } 
/* 193 */         return value;
/*     */       } finally {
/* 195 */         if (end == 0L) {
/* 196 */           end = System.nanoTime();
/*     */         }
/* 198 */         if (value == null) {
/* 199 */           clearValue(key, hash, computingValueReference);
/*     */         }
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static final class ComputationExceptionReference<K, V>
/*     */     implements MapMakerInternalMap.ValueReference<K, V>
/*     */   {
/*     */     final Throwable t;
/*     */     
/*     */     ComputationExceptionReference(Throwable t) {
/* 212 */       this.t = t;
/*     */     }
/*     */ 
/*     */     
/*     */     public V get() {
/* 217 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     public MapMakerInternalMap.ReferenceEntry<K, V> getEntry() {
/* 222 */       return null;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public MapMakerInternalMap.ValueReference<K, V> copyFor(ReferenceQueue<V> queue, V value, MapMakerInternalMap.ReferenceEntry<K, V> entry) {
/* 228 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isComputingReference() {
/* 233 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public V waitForValue() throws ExecutionException {
/* 238 */       throw new ExecutionException(this.t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void clear(MapMakerInternalMap.ValueReference<K, V> newValue) {}
/*     */   }
/*     */ 
/*     */   
/*     */   private static final class ComputedReference<K, V>
/*     */     implements MapMakerInternalMap.ValueReference<K, V>
/*     */   {
/*     */     final V value;
/*     */     
/*     */     ComputedReference(@Nullable V value) {
/* 252 */       this.value = value;
/*     */     }
/*     */ 
/*     */     
/*     */     public V get() {
/* 257 */       return this.value;
/*     */     }
/*     */ 
/*     */     
/*     */     public MapMakerInternalMap.ReferenceEntry<K, V> getEntry() {
/* 262 */       return null;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public MapMakerInternalMap.ValueReference<K, V> copyFor(ReferenceQueue<V> queue, V value, MapMakerInternalMap.ReferenceEntry<K, V> entry) {
/* 268 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isComputingReference() {
/* 273 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public V waitForValue() {
/* 278 */       return get();
/*     */     }
/*     */     
/*     */     public void clear(MapMakerInternalMap.ValueReference<K, V> newValue) {}
/*     */   }
/*     */   
/*     */   private static final class ComputingValueReference<K, V>
/*     */     implements MapMakerInternalMap.ValueReference<K, V> {
/*     */     final Function<? super K, ? extends V> computingFunction;
/*     */     @GuardedBy("ComputingValueReference.this")
/* 288 */     volatile MapMakerInternalMap.ValueReference<K, V> computedReference = MapMakerInternalMap.unset();
/*     */ 
/*     */     
/*     */     public ComputingValueReference(Function<? super K, ? extends V> computingFunction) {
/* 292 */       this.computingFunction = computingFunction;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public V get() {
/* 299 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     public MapMakerInternalMap.ReferenceEntry<K, V> getEntry() {
/* 304 */       return null;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public MapMakerInternalMap.ValueReference<K, V> copyFor(ReferenceQueue<V> queue, @Nullable V value, MapMakerInternalMap.ReferenceEntry<K, V> entry) {
/* 310 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isComputingReference() {
/* 315 */       return true;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public V waitForValue() throws ExecutionException {
/* 323 */       if (this.computedReference == MapMakerInternalMap.UNSET) {
/* 324 */         boolean interrupted = false;
/*     */         try {
/* 326 */           synchronized (this) {
/* 327 */             while (this.computedReference == MapMakerInternalMap.UNSET) {
/*     */               try {
/* 329 */                 wait();
/* 330 */               } catch (InterruptedException ie) {
/* 331 */                 interrupted = true;
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         } finally {
/* 336 */           if (interrupted) {
/* 337 */             Thread.currentThread().interrupt();
/*     */           }
/*     */         } 
/*     */       } 
/* 341 */       return this.computedReference.waitForValue();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void clear(MapMakerInternalMap.ValueReference<K, V> newValue) {
/* 348 */       setValueReference(newValue);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     V compute(K key, int hash) throws ExecutionException {
/*     */       V value;
/*     */       try {
/* 356 */         value = (V)this.computingFunction.apply(key);
/* 357 */       } catch (Throwable t) {
/* 358 */         setValueReference(new ComputingConcurrentHashMap.ComputationExceptionReference<K, V>(t));
/* 359 */         throw new ExecutionException(t);
/*     */       } 
/*     */       
/* 362 */       setValueReference(new ComputingConcurrentHashMap.ComputedReference<K, V>(value));
/* 363 */       return value;
/*     */     }
/*     */     
/*     */     void setValueReference(MapMakerInternalMap.ValueReference<K, V> valueReference) {
/* 367 */       synchronized (this) {
/* 368 */         if (this.computedReference == MapMakerInternalMap.UNSET) {
/* 369 */           this.computedReference = valueReference;
/* 370 */           notifyAll();
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Object writeReplace() {
/* 382 */     return new ComputingSerializationProxy<K, V>(this.keyStrength, this.valueStrength, this.keyEquivalence, this.valueEquivalence, this.expireAfterWriteNanos, this.expireAfterAccessNanos, this.maximumSize, this.concurrencyLevel, this.removalListener, this, this.computingFunction);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static final class ComputingSerializationProxy<K, V>
/*     */     extends MapMakerInternalMap.AbstractSerializationProxy<K, V>
/*     */   {
/*     */     final Function<? super K, ? extends V> computingFunction;
/*     */     
/*     */     private static final long serialVersionUID = 4L;
/*     */ 
/*     */     
/*     */     ComputingSerializationProxy(MapMakerInternalMap.Strength keyStrength, MapMakerInternalMap.Strength valueStrength, Equivalence<Object> keyEquivalence, Equivalence<Object> valueEquivalence, long expireAfterWriteNanos, long expireAfterAccessNanos, int maximumSize, int concurrencyLevel, MapMaker.RemovalListener<? super K, ? super V> removalListener, ConcurrentMap<K, V> delegate, Function<? super K, ? extends V> computingFunction) {
/* 396 */       super(keyStrength, valueStrength, keyEquivalence, valueEquivalence, expireAfterWriteNanos, expireAfterAccessNanos, maximumSize, concurrencyLevel, removalListener, delegate);
/*     */       
/* 398 */       this.computingFunction = computingFunction;
/*     */     }
/*     */     
/*     */     private void writeObject(ObjectOutputStream out) throws IOException {
/* 402 */       out.defaultWriteObject();
/* 403 */       writeMapTo(out);
/*     */     }
/*     */ 
/*     */     
/*     */     private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
/* 408 */       in.defaultReadObject();
/* 409 */       MapMaker mapMaker = readMapMaker(in);
/* 410 */       this.delegate = mapMaker.makeComputingMap(this.computingFunction);
/* 411 */       readEntries(in);
/*     */     }
/*     */     
/*     */     Object readResolve() {
/* 415 */       return this.delegate;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\collect\ComputingConcurrentHashMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */