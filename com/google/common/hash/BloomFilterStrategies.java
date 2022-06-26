/*     */ package com.google.common.hash;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.math.LongMath;
/*     */ import com.google.common.primitives.Ints;
/*     */ import com.google.common.primitives.Longs;
/*     */ import java.math.RoundingMode;
/*     */ import java.util.Arrays;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ enum BloomFilterStrategies
/*     */   implements BloomFilter.Strategy
/*     */ {
/*  44 */   MURMUR128_MITZ_32
/*     */   {
/*     */     public <T> boolean put(T object, Funnel<? super T> funnel, int numHashFunctions, BitArray bits) {
/*  47 */       long bitSize = bits.bitSize();
/*  48 */       long hash64 = Hashing.murmur3_128().<T>hashObject(object, funnel).asLong();
/*  49 */       int hash1 = (int)hash64;
/*  50 */       int hash2 = (int)(hash64 >>> 32L);
/*     */       
/*  52 */       boolean bitsChanged = false;
/*  53 */       for (int i = 1; i <= numHashFunctions; i++) {
/*  54 */         int combinedHash = hash1 + i * hash2;
/*     */         
/*  56 */         if (combinedHash < 0) {
/*  57 */           combinedHash ^= 0xFFFFFFFF;
/*     */         }
/*  59 */         bitsChanged |= bits.set(combinedHash % bitSize);
/*     */       } 
/*  61 */       return bitsChanged;
/*     */     }
/*     */ 
/*     */     
/*     */     public <T> boolean mightContain(T object, Funnel<? super T> funnel, int numHashFunctions, BitArray bits) {
/*  66 */       long bitSize = bits.bitSize();
/*  67 */       long hash64 = Hashing.murmur3_128().<T>hashObject(object, funnel).asLong();
/*  68 */       int hash1 = (int)hash64;
/*  69 */       int hash2 = (int)(hash64 >>> 32L);
/*     */       
/*  71 */       for (int i = 1; i <= numHashFunctions; i++) {
/*  72 */         int combinedHash = hash1 + i * hash2;
/*     */         
/*  74 */         if (combinedHash < 0) {
/*  75 */           combinedHash ^= 0xFFFFFFFF;
/*     */         }
/*  77 */         if (!bits.get(combinedHash % bitSize)) {
/*  78 */           return false;
/*     */         }
/*     */       } 
/*  81 */       return true;
/*     */     }
/*     */   },
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  90 */   MURMUR128_MITZ_64
/*     */   {
/*     */     public <T> boolean put(T object, Funnel<? super T> funnel, int numHashFunctions, BitArray bits)
/*     */     {
/*  94 */       long bitSize = bits.bitSize();
/*  95 */       byte[] bytes = Hashing.murmur3_128().<T>hashObject(object, funnel).getBytesInternal();
/*  96 */       long hash1 = lowerEight(bytes);
/*  97 */       long hash2 = upperEight(bytes);
/*     */       
/*  99 */       boolean bitsChanged = false;
/* 100 */       long combinedHash = hash1;
/* 101 */       for (int i = 0; i < numHashFunctions; i++) {
/*     */         
/* 103 */         bitsChanged |= bits.set((combinedHash & Long.MAX_VALUE) % bitSize);
/* 104 */         combinedHash += hash2;
/*     */       } 
/* 106 */       return bitsChanged;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public <T> boolean mightContain(T object, Funnel<? super T> funnel, int numHashFunctions, BitArray bits) {
/* 112 */       long bitSize = bits.bitSize();
/* 113 */       byte[] bytes = Hashing.murmur3_128().<T>hashObject(object, funnel).getBytesInternal();
/* 114 */       long hash1 = lowerEight(bytes);
/* 115 */       long hash2 = upperEight(bytes);
/*     */       
/* 117 */       long combinedHash = hash1;
/* 118 */       for (int i = 0; i < numHashFunctions; i++) {
/*     */         
/* 120 */         if (!bits.get((combinedHash & Long.MAX_VALUE) % bitSize)) {
/* 121 */           return false;
/*     */         }
/* 123 */         combinedHash += hash2;
/*     */       } 
/* 125 */       return true;
/*     */     }
/*     */     
/*     */     private long lowerEight(byte[] bytes) {
/* 129 */       return Longs.fromBytes(bytes[7], bytes[6], bytes[5], bytes[4], bytes[3], bytes[2], bytes[1], bytes[0]);
/*     */     }
/*     */ 
/*     */     
/*     */     private long upperEight(byte[] bytes) {
/* 134 */       return Longs.fromBytes(bytes[15], bytes[14], bytes[13], bytes[12], bytes[11], bytes[10], bytes[9], bytes[8]);
/*     */     }
/*     */   };
/*     */ 
/*     */   
/*     */   static final class BitArray
/*     */   {
/*     */     final long[] data;
/*     */     long bitCount;
/*     */     
/*     */     BitArray(long bits) {
/* 145 */       this(new long[Ints.checkedCast(LongMath.divide(bits, 64L, RoundingMode.CEILING))]);
/*     */     }
/*     */ 
/*     */     
/*     */     BitArray(long[] data) {
/* 150 */       Preconditions.checkArgument((data.length > 0), "data length is zero!");
/* 151 */       this.data = data;
/* 152 */       long bitCount = 0L;
/* 153 */       for (long value : data) {
/* 154 */         bitCount += Long.bitCount(value);
/*     */       }
/* 156 */       this.bitCount = bitCount;
/*     */     }
/*     */ 
/*     */     
/*     */     boolean set(long index) {
/* 161 */       if (!get(index)) {
/* 162 */         this.data[(int)(index >>> 6L)] = this.data[(int)(index >>> 6L)] | 1L << (int)index;
/* 163 */         this.bitCount++;
/* 164 */         return true;
/*     */       } 
/* 166 */       return false;
/*     */     }
/*     */     
/*     */     boolean get(long index) {
/* 170 */       return ((this.data[(int)(index >>> 6L)] & 1L << (int)index) != 0L);
/*     */     }
/*     */ 
/*     */     
/*     */     long bitSize() {
/* 175 */       return this.data.length * 64L;
/*     */     }
/*     */ 
/*     */     
/*     */     long bitCount() {
/* 180 */       return this.bitCount;
/*     */     }
/*     */     
/*     */     BitArray copy() {
/* 184 */       return new BitArray((long[])this.data.clone());
/*     */     }
/*     */ 
/*     */     
/*     */     void putAll(BitArray array) {
/* 189 */       Preconditions.checkArgument((this.data.length == array.data.length), "BitArrays must be of equal length (%s != %s)", new Object[] { Integer.valueOf(this.data.length), Integer.valueOf(array.data.length) });
/*     */       
/* 191 */       this.bitCount = 0L;
/* 192 */       for (int i = 0; i < this.data.length; i++) {
/* 193 */         this.data[i] = this.data[i] | array.data[i];
/* 194 */         this.bitCount += Long.bitCount(this.data[i]);
/*     */       } 
/*     */     }
/*     */     
/*     */     public boolean equals(Object o) {
/* 199 */       if (o instanceof BitArray) {
/* 200 */         BitArray bitArray = (BitArray)o;
/* 201 */         return Arrays.equals(this.data, bitArray.data);
/*     */       } 
/* 203 */       return false;
/*     */     }
/*     */     
/*     */     public int hashCode() {
/* 207 */       return Arrays.hashCode(this.data);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\hash\BloomFilterStrategies.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */