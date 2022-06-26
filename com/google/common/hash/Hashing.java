/*     */ package com.google.common.hash;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Supplier;
/*     */ import java.util.Iterator;
/*     */ import java.util.zip.Adler32;
/*     */ import java.util.zip.CRC32;
/*     */ import java.util.zip.Checksum;
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
/*     */ @Beta
/*     */ public final class Hashing
/*     */ {
/*     */   public static HashFunction goodFastHash(int minimumBits) {
/*  61 */     int bits = checkPositiveAndMakeMultipleOf32(minimumBits);
/*     */     
/*  63 */     if (bits == 32) {
/*  64 */       return Murmur3_32Holder.GOOD_FAST_HASH_FUNCTION_32;
/*     */     }
/*  66 */     if (bits <= 128) {
/*  67 */       return Murmur3_128Holder.GOOD_FAST_HASH_FUNCTION_128;
/*     */     }
/*     */ 
/*     */     
/*  71 */     int hashFunctionsNeeded = (bits + 127) / 128;
/*  72 */     HashFunction[] hashFunctions = new HashFunction[hashFunctionsNeeded];
/*  73 */     hashFunctions[0] = Murmur3_128Holder.GOOD_FAST_HASH_FUNCTION_128;
/*  74 */     int seed = GOOD_FAST_HASH_SEED;
/*  75 */     for (int i = 1; i < hashFunctionsNeeded; i++) {
/*  76 */       seed += 1500450271;
/*  77 */       hashFunctions[i] = murmur3_128(seed);
/*     */     } 
/*  79 */     return new ConcatenatedHashFunction(hashFunctions);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  86 */   private static final int GOOD_FAST_HASH_SEED = (int)System.currentTimeMillis();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HashFunction murmur3_32(int seed) {
/*  97 */     return new Murmur3_32HashFunction(seed);
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
/*     */   public static HashFunction murmur3_32() {
/* 109 */     return Murmur3_32Holder.MURMUR3_32;
/*     */   }
/*     */   
/*     */   private static class Murmur3_32Holder {
/* 113 */     static final HashFunction MURMUR3_32 = new Murmur3_32HashFunction(0);
/*     */ 
/*     */     
/* 116 */     static final HashFunction GOOD_FAST_HASH_FUNCTION_32 = Hashing.murmur3_32(Hashing.GOOD_FAST_HASH_SEED);
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
/*     */   public static HashFunction murmur3_128(int seed) {
/* 128 */     return new Murmur3_128HashFunction(seed);
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
/*     */   public static HashFunction murmur3_128() {
/* 140 */     return Murmur3_128Holder.MURMUR3_128;
/*     */   }
/*     */   
/*     */   private static class Murmur3_128Holder {
/* 144 */     static final HashFunction MURMUR3_128 = new Murmur3_128HashFunction(0);
/*     */ 
/*     */     
/* 147 */     static final HashFunction GOOD_FAST_HASH_FUNCTION_128 = Hashing.murmur3_128(Hashing.GOOD_FAST_HASH_SEED);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HashFunction sipHash24() {
/* 158 */     return SipHash24Holder.SIP_HASH_24;
/*     */   }
/*     */   
/*     */   private static class SipHash24Holder {
/* 162 */     static final HashFunction SIP_HASH_24 = new SipHashFunction(2, 4, 506097522914230528L, 1084818905618843912L);
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
/*     */   public static HashFunction sipHash24(long k0, long k1) {
/* 174 */     return new SipHashFunction(2, 4, k0, k1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HashFunction md5() {
/* 182 */     return Md5Holder.MD5;
/*     */   }
/*     */   
/*     */   private static class Md5Holder {
/* 186 */     static final HashFunction MD5 = new MessageDigestHashFunction("MD5", "Hashing.md5()");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HashFunction sha1() {
/* 194 */     return Sha1Holder.SHA_1;
/*     */   }
/*     */   
/*     */   private static class Sha1Holder {
/* 198 */     static final HashFunction SHA_1 = new MessageDigestHashFunction("SHA-1", "Hashing.sha1()");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HashFunction sha256() {
/* 207 */     return Sha256Holder.SHA_256;
/*     */   }
/*     */   
/*     */   private static class Sha256Holder {
/* 211 */     static final HashFunction SHA_256 = new MessageDigestHashFunction("SHA-256", "Hashing.sha256()");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HashFunction sha512() {
/* 220 */     return Sha512Holder.SHA_512;
/*     */   }
/*     */   
/*     */   private static class Sha512Holder {
/* 224 */     static final HashFunction SHA_512 = new MessageDigestHashFunction("SHA-512", "Hashing.sha512()");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HashFunction crc32c() {
/* 235 */     return Crc32cHolder.CRC_32_C;
/*     */   }
/*     */   
/*     */   private static final class Crc32cHolder {
/* 239 */     static final HashFunction CRC_32_C = new Crc32cHashFunction();
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
/*     */   public static HashFunction crc32() {
/* 252 */     return Crc32Holder.CRC_32;
/*     */   }
/*     */   
/*     */   private static class Crc32Holder {
/* 256 */     static final HashFunction CRC_32 = Hashing.checksumHashFunction(Hashing.ChecksumType.CRC_32, "Hashing.crc32()");
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
/*     */   public static HashFunction adler32() {
/* 270 */     return Adler32Holder.ADLER_32;
/*     */   }
/*     */   
/*     */   private static class Adler32Holder {
/* 274 */     static final HashFunction ADLER_32 = Hashing.checksumHashFunction(Hashing.ChecksumType.ADLER_32, "Hashing.adler32()");
/*     */   }
/*     */ 
/*     */   
/*     */   private static HashFunction checksumHashFunction(ChecksumType type, String toString) {
/* 279 */     return new ChecksumHashFunction(type, type.bits, toString);
/*     */   }
/*     */   
/*     */   enum ChecksumType implements Supplier<Checksum> {
/* 283 */     CRC_32(32)
/*     */     {
/*     */       public Checksum get() {
/* 286 */         return new CRC32();
/*     */       }
/*     */     },
/* 289 */     ADLER_32(32)
/*     */     {
/*     */       public Checksum get() {
/* 292 */         return new Adler32();
/*     */       }
/*     */     };
/*     */     
/*     */     private final int bits;
/*     */     
/*     */     ChecksumType(int bits) {
/* 299 */       this.bits = bits;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public abstract Checksum get();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int consistentHash(HashCode hashCode, int buckets) {
/* 320 */     return consistentHash(hashCode.padToLong(), buckets);
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
/*     */   public static int consistentHash(long input, int buckets) {
/* 337 */     Preconditions.checkArgument((buckets > 0), "buckets must be positive: %s", new Object[] { Integer.valueOf(buckets) });
/* 338 */     LinearCongruentialGenerator generator = new LinearCongruentialGenerator(input);
/* 339 */     int candidate = 0;
/*     */ 
/*     */ 
/*     */     
/*     */     while (true) {
/* 344 */       int next = (int)((candidate + 1) / generator.nextDouble());
/* 345 */       if (next >= 0 && next < buckets) {
/* 346 */         candidate = next; continue;
/*     */       }  break;
/* 348 */     }  return candidate;
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
/*     */   public static HashCode combineOrdered(Iterable<HashCode> hashCodes) {
/* 364 */     Iterator<HashCode> iterator = hashCodes.iterator();
/* 365 */     Preconditions.checkArgument(iterator.hasNext(), "Must be at least 1 hash code to combine.");
/* 366 */     int bits = ((HashCode)iterator.next()).bits();
/* 367 */     byte[] resultBytes = new byte[bits / 8];
/* 368 */     for (HashCode hashCode : hashCodes) {
/* 369 */       byte[] nextBytes = hashCode.asBytes();
/* 370 */       Preconditions.checkArgument((nextBytes.length == resultBytes.length), "All hashcodes must have the same bit length.");
/*     */       
/* 372 */       for (int i = 0; i < nextBytes.length; i++) {
/* 373 */         resultBytes[i] = (byte)(resultBytes[i] * 37 ^ nextBytes[i]);
/*     */       }
/*     */     } 
/* 376 */     return HashCode.fromBytesNoCopy(resultBytes);
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
/*     */   public static HashCode combineUnordered(Iterable<HashCode> hashCodes) {
/* 390 */     Iterator<HashCode> iterator = hashCodes.iterator();
/* 391 */     Preconditions.checkArgument(iterator.hasNext(), "Must be at least 1 hash code to combine.");
/* 392 */     byte[] resultBytes = new byte[((HashCode)iterator.next()).bits() / 8];
/* 393 */     for (HashCode hashCode : hashCodes) {
/* 394 */       byte[] nextBytes = hashCode.asBytes();
/* 395 */       Preconditions.checkArgument((nextBytes.length == resultBytes.length), "All hashcodes must have the same bit length.");
/*     */       
/* 397 */       for (int i = 0; i < nextBytes.length; i++) {
/* 398 */         resultBytes[i] = (byte)(resultBytes[i] + nextBytes[i]);
/*     */       }
/*     */     } 
/* 401 */     return HashCode.fromBytesNoCopy(resultBytes);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static int checkPositiveAndMakeMultipleOf32(int bits) {
/* 408 */     Preconditions.checkArgument((bits > 0), "Number of bits must be positive");
/* 409 */     return bits + 31 & 0xFFFFFFE0;
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   static final class ConcatenatedHashFunction
/*     */     extends AbstractCompositeHashFunction {
/*     */     private final int bits;
/*     */     
/*     */     ConcatenatedHashFunction(HashFunction... functions) {
/* 418 */       super(functions);
/* 419 */       int bitSum = 0;
/* 420 */       for (HashFunction function : functions) {
/* 421 */         bitSum += function.bits();
/*     */       }
/* 423 */       this.bits = bitSum;
/*     */     }
/*     */ 
/*     */     
/*     */     HashCode makeHash(Hasher[] hashers) {
/* 428 */       byte[] bytes = new byte[this.bits / 8];
/* 429 */       int i = 0;
/* 430 */       for (Hasher hasher : hashers) {
/* 431 */         HashCode newHash = hasher.hash();
/* 432 */         i += newHash.writeBytesTo(bytes, i, newHash.bits() / 8);
/*     */       } 
/* 434 */       return HashCode.fromBytesNoCopy(bytes);
/*     */     }
/*     */ 
/*     */     
/*     */     public int bits() {
/* 439 */       return this.bits;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(@Nullable Object object) {
/* 444 */       if (object instanceof ConcatenatedHashFunction) {
/* 445 */         ConcatenatedHashFunction other = (ConcatenatedHashFunction)object;
/* 446 */         if (this.bits != other.bits || this.functions.length != other.functions.length) {
/* 447 */           return false;
/*     */         }
/* 449 */         for (int i = 0; i < this.functions.length; i++) {
/* 450 */           if (!this.functions[i].equals(other.functions[i])) {
/* 451 */             return false;
/*     */           }
/*     */         } 
/* 454 */         return true;
/*     */       } 
/* 456 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 461 */       int hash = this.bits;
/* 462 */       for (HashFunction function : this.functions) {
/* 463 */         hash ^= function.hashCode();
/*     */       }
/* 465 */       return hash;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class LinearCongruentialGenerator
/*     */   {
/*     */     private long state;
/*     */ 
/*     */     
/*     */     public LinearCongruentialGenerator(long seed) {
/* 477 */       this.state = seed;
/*     */     }
/*     */     
/*     */     public double nextDouble() {
/* 481 */       this.state = 2862933555777941757L * this.state + 1L;
/* 482 */       return ((int)(this.state >>> 33L) + 1) / 2.147483648E9D;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\hash\Hashing.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */