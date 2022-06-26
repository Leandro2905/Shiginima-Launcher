/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
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
/*     */ 
/*     */ @GwtCompatible(serializable = true, emulated = true)
/*     */ final class RegularImmutableMap<K, V>
/*     */   extends ImmutableMap<K, V>
/*     */ {
/*     */   private final transient ImmutableMapEntry<K, V>[] entries;
/*     */   private final transient ImmutableMapEntry<K, V>[] table;
/*     */   private final transient int mask;
/*     */   private static final double MAX_LOAD_FACTOR = 1.2D;
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   RegularImmutableMap(ImmutableMapEntry.TerminalEntry<?, ?>... theEntries) {
/*  44 */     this(theEntries.length, theEntries);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   RegularImmutableMap(int size, ImmutableMapEntry.TerminalEntry<?, ?>[] theEntries) {
/*  54 */     this.entries = createEntryArray(size);
/*  55 */     int tableSize = Hashing.closedTableSize(size, 1.2D);
/*  56 */     this.table = createEntryArray(tableSize);
/*  57 */     this.mask = tableSize - 1;
/*  58 */     for (int entryIndex = 0; entryIndex < size; entryIndex++) {
/*     */       
/*  60 */       ImmutableMapEntry.TerminalEntry<?, ?> terminalEntry = theEntries[entryIndex];
/*  61 */       K key = (K)terminalEntry.getKey();
/*  62 */       int tableIndex = Hashing.smear(key.hashCode()) & this.mask;
/*  63 */       ImmutableMapEntry<K, V> existing = this.table[tableIndex];
/*     */       
/*  65 */       ImmutableMapEntry<K, V> newEntry = (existing == null) ? (ImmutableMapEntry)terminalEntry : new NonTerminalMapEntry<K, V>((ImmutableMapEntry)terminalEntry, existing);
/*     */ 
/*     */       
/*  68 */       this.table[tableIndex] = newEntry;
/*  69 */       this.entries[entryIndex] = newEntry;
/*  70 */       checkNoConflictInBucket(key, newEntry, existing);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   RegularImmutableMap(Map.Entry<?, ?>[] theEntries) {
/*  78 */     int size = theEntries.length;
/*  79 */     this.entries = createEntryArray(size);
/*  80 */     int tableSize = Hashing.closedTableSize(size, 1.2D);
/*  81 */     this.table = createEntryArray(tableSize);
/*  82 */     this.mask = tableSize - 1;
/*  83 */     for (int entryIndex = 0; entryIndex < size; entryIndex++) {
/*     */       
/*  85 */       Map.Entry<?, ?> entry = theEntries[entryIndex];
/*  86 */       K key = (K)entry.getKey();
/*  87 */       V value = (V)entry.getValue();
/*  88 */       CollectPreconditions.checkEntryNotNull(key, value);
/*  89 */       int tableIndex = Hashing.smear(key.hashCode()) & this.mask;
/*  90 */       ImmutableMapEntry<K, V> existing = this.table[tableIndex];
/*     */       
/*  92 */       ImmutableMapEntry<K, V> newEntry = (existing == null) ? new ImmutableMapEntry.TerminalEntry<K, V>(key, value) : new NonTerminalMapEntry<K, V>(key, value, existing);
/*     */ 
/*     */       
/*  95 */       this.table[tableIndex] = newEntry;
/*  96 */       this.entries[entryIndex] = newEntry;
/*  97 */       checkNoConflictInBucket(key, newEntry, existing);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void checkNoConflictInBucket(K key, ImmutableMapEntry<K, V> entry, ImmutableMapEntry<K, V> bucketHead) {
/* 103 */     for (; bucketHead != null; bucketHead = bucketHead.getNextInKeyBucket())
/* 104 */       checkNoConflict(!key.equals(bucketHead.getKey()), "key", entry, bucketHead); 
/*     */   }
/*     */   
/*     */   private static final class NonTerminalMapEntry<K, V>
/*     */     extends ImmutableMapEntry<K, V> {
/*     */     private final ImmutableMapEntry<K, V> nextInKeyBucket;
/*     */     
/*     */     NonTerminalMapEntry(K key, V value, ImmutableMapEntry<K, V> nextInKeyBucket) {
/* 112 */       super(key, value);
/* 113 */       this.nextInKeyBucket = nextInKeyBucket;
/*     */     }
/*     */     
/*     */     NonTerminalMapEntry(ImmutableMapEntry<K, V> contents, ImmutableMapEntry<K, V> nextInKeyBucket) {
/* 117 */       super(contents);
/* 118 */       this.nextInKeyBucket = nextInKeyBucket;
/*     */     }
/*     */ 
/*     */     
/*     */     ImmutableMapEntry<K, V> getNextInKeyBucket() {
/* 123 */       return this.nextInKeyBucket;
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     ImmutableMapEntry<K, V> getNextInValueBucket() {
/* 129 */       return null;
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
/*     */   private ImmutableMapEntry<K, V>[] createEntryArray(int size) {
/* 148 */     return (ImmutableMapEntry<K, V>[])new ImmutableMapEntry[size];
/*     */   }
/*     */   
/*     */   public V get(@Nullable Object key) {
/* 152 */     if (key == null) {
/* 153 */       return null;
/*     */     }
/* 155 */     int index = Hashing.smear(key.hashCode()) & this.mask;
/* 156 */     ImmutableMapEntry<K, V> entry = this.table[index];
/* 157 */     for (; entry != null; 
/* 158 */       entry = entry.getNextInKeyBucket()) {
/* 159 */       K candidateKey = entry.getKey();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 167 */       if (key.equals(candidateKey)) {
/* 168 */         return entry.getValue();
/*     */       }
/*     */     } 
/* 171 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/* 176 */     return this.entries.length;
/*     */   }
/*     */   
/*     */   boolean isPartialView() {
/* 180 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   ImmutableSet<Map.Entry<K, V>> createEntrySet() {
/* 185 */     return new EntrySet();
/*     */   }
/*     */   
/*     */   private class EntrySet
/*     */     extends ImmutableMapEntrySet<K, V> {
/*     */     ImmutableMap<K, V> map() {
/* 191 */       return RegularImmutableMap.this;
/*     */     }
/*     */     private EntrySet() {}
/*     */     
/*     */     public UnmodifiableIterator<Map.Entry<K, V>> iterator() {
/* 196 */       return asList().iterator();
/*     */     }
/*     */ 
/*     */     
/*     */     ImmutableList<Map.Entry<K, V>> createAsList() {
/* 201 */       return new RegularImmutableAsList<Map.Entry<K, V>>(this, (Object[])RegularImmutableMap.this.entries);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\collect\RegularImmutableMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */