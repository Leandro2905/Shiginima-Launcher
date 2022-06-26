/*     */ package com.google.common.hash;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.primitives.Ints;
/*     */ import com.google.common.primitives.UnsignedInts;
/*     */ import java.io.Serializable;
/*     */ import java.security.MessageDigest;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public abstract class HashCode
/*     */ {
/*     */   public abstract int bits();
/*     */   
/*     */   public abstract int asInt();
/*     */   
/*     */   public abstract long asLong();
/*     */   
/*     */   public abstract long padToLong();
/*     */   
/*     */   public abstract byte[] asBytes();
/*     */   
/*     */   public int writeBytesTo(byte[] dest, int offset, int maxLength) {
/*  90 */     maxLength = Ints.min(new int[] { maxLength, bits() / 8 });
/*  91 */     Preconditions.checkPositionIndexes(offset, offset + maxLength, dest.length);
/*  92 */     writeBytesToImpl(dest, offset, maxLength);
/*  93 */     return maxLength;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   abstract void writeBytesToImpl(byte[] paramArrayOfbyte, int paramInt1, int paramInt2);
/*     */ 
/*     */ 
/*     */   
/*     */   byte[] getBytesInternal() {
/* 104 */     return asBytes();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   abstract boolean equalsSameBits(HashCode paramHashCode);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HashCode fromInt(int hash) {
/* 120 */     return new IntHashCode(hash);
/*     */   }
/*     */   
/*     */   private static final class IntHashCode extends HashCode implements Serializable {
/*     */     final int hash;
/*     */     
/*     */     IntHashCode(int hash) {
/* 127 */       this.hash = hash;
/*     */     }
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     public int bits() {
/* 132 */       return 32;
/*     */     }
/*     */ 
/*     */     
/*     */     public byte[] asBytes() {
/* 137 */       return new byte[] { (byte)this.hash, (byte)(this.hash >> 8), (byte)(this.hash >> 16), (byte)(this.hash >> 24) };
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int asInt() {
/* 146 */       return this.hash;
/*     */     }
/*     */ 
/*     */     
/*     */     public long asLong() {
/* 151 */       throw new IllegalStateException("this HashCode only has 32 bits; cannot create a long");
/*     */     }
/*     */ 
/*     */     
/*     */     public long padToLong() {
/* 156 */       return UnsignedInts.toLong(this.hash);
/*     */     }
/*     */ 
/*     */     
/*     */     void writeBytesToImpl(byte[] dest, int offset, int maxLength) {
/* 161 */       for (int i = 0; i < maxLength; i++) {
/* 162 */         dest[offset + i] = (byte)(this.hash >> i * 8);
/*     */       }
/*     */     }
/*     */     
/*     */     boolean equalsSameBits(HashCode that) {
/* 167 */       return (this.hash == that.asInt());
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
/*     */   public static HashCode fromLong(long hash) {
/* 180 */     return new LongHashCode(hash);
/*     */   }
/*     */   
/*     */   private static final class LongHashCode extends HashCode implements Serializable { final long hash;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     LongHashCode(long hash) {
/* 187 */       this.hash = hash;
/*     */     }
/*     */ 
/*     */     
/*     */     public int bits() {
/* 192 */       return 64;
/*     */     }
/*     */ 
/*     */     
/*     */     public byte[] asBytes() {
/* 197 */       return new byte[] { (byte)(int)this.hash, (byte)(int)(this.hash >> 8L), (byte)(int)(this.hash >> 16L), (byte)(int)(this.hash >> 24L), (byte)(int)(this.hash >> 32L), (byte)(int)(this.hash >> 40L), (byte)(int)(this.hash >> 48L), (byte)(int)(this.hash >> 56L) };
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
/*     */     public int asInt() {
/* 210 */       return (int)this.hash;
/*     */     }
/*     */ 
/*     */     
/*     */     public long asLong() {
/* 215 */       return this.hash;
/*     */     }
/*     */ 
/*     */     
/*     */     public long padToLong() {
/* 220 */       return this.hash;
/*     */     }
/*     */ 
/*     */     
/*     */     void writeBytesToImpl(byte[] dest, int offset, int maxLength) {
/* 225 */       for (int i = 0; i < maxLength; i++) {
/* 226 */         dest[offset + i] = (byte)(int)(this.hash >> i * 8);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     boolean equalsSameBits(HashCode that) {
/* 232 */       return (this.hash == that.asLong());
/*     */     } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HashCode fromBytes(byte[] bytes) {
/* 245 */     Preconditions.checkArgument((bytes.length >= 1), "A HashCode must contain at least 1 byte.");
/* 246 */     return fromBytesNoCopy((byte[])bytes.clone());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static HashCode fromBytesNoCopy(byte[] bytes) {
/* 254 */     return new BytesHashCode(bytes);
/*     */   }
/*     */   
/*     */   private static final class BytesHashCode extends HashCode implements Serializable { final byte[] bytes;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     BytesHashCode(byte[] bytes) {
/* 261 */       this.bytes = (byte[])Preconditions.checkNotNull(bytes);
/*     */     }
/*     */ 
/*     */     
/*     */     public int bits() {
/* 266 */       return this.bytes.length * 8;
/*     */     }
/*     */ 
/*     */     
/*     */     public byte[] asBytes() {
/* 271 */       return (byte[])this.bytes.clone();
/*     */     }
/*     */ 
/*     */     
/*     */     public int asInt() {
/* 276 */       Preconditions.checkState((this.bytes.length >= 4), "HashCode#asInt() requires >= 4 bytes (it only has %s bytes).", new Object[] { Integer.valueOf(this.bytes.length) });
/*     */       
/* 278 */       return this.bytes[0] & 0xFF | (this.bytes[1] & 0xFF) << 8 | (this.bytes[2] & 0xFF) << 16 | (this.bytes[3] & 0xFF) << 24;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public long asLong() {
/* 286 */       Preconditions.checkState((this.bytes.length >= 8), "HashCode#asLong() requires >= 8 bytes (it only has %s bytes).", new Object[] { Integer.valueOf(this.bytes.length) });
/*     */       
/* 288 */       return padToLong();
/*     */     }
/*     */ 
/*     */     
/*     */     public long padToLong() {
/* 293 */       long retVal = (this.bytes[0] & 0xFF);
/* 294 */       for (int i = 1; i < Math.min(this.bytes.length, 8); i++) {
/* 295 */         retVal |= (this.bytes[i] & 0xFFL) << i * 8;
/*     */       }
/* 297 */       return retVal;
/*     */     }
/*     */ 
/*     */     
/*     */     void writeBytesToImpl(byte[] dest, int offset, int maxLength) {
/* 302 */       System.arraycopy(this.bytes, 0, dest, offset, maxLength);
/*     */     }
/*     */ 
/*     */     
/*     */     byte[] getBytesInternal() {
/* 307 */       return this.bytes;
/*     */     }
/*     */ 
/*     */     
/*     */     boolean equalsSameBits(HashCode that) {
/* 312 */       return MessageDigest.isEqual(this.bytes, that.getBytesInternal());
/*     */     } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HashCode fromString(String string) {
/* 329 */     Preconditions.checkArgument((string.length() >= 2), "input string (%s) must have at least 2 characters", new Object[] { string });
/*     */     
/* 331 */     Preconditions.checkArgument((string.length() % 2 == 0), "input string (%s) must have an even number of characters", new Object[] { string });
/*     */ 
/*     */     
/* 334 */     byte[] bytes = new byte[string.length() / 2];
/* 335 */     for (int i = 0; i < string.length(); i += 2) {
/* 336 */       int ch1 = decode(string.charAt(i)) << 4;
/* 337 */       int ch2 = decode(string.charAt(i + 1));
/* 338 */       bytes[i / 2] = (byte)(ch1 + ch2);
/*     */     } 
/* 340 */     return fromBytesNoCopy(bytes);
/*     */   }
/*     */   
/*     */   private static int decode(char ch) {
/* 344 */     if (ch >= '0' && ch <= '9') {
/* 345 */       return ch - 48;
/*     */     }
/* 347 */     if (ch >= 'a' && ch <= 'f') {
/* 348 */       return ch - 97 + 10;
/*     */     }
/* 350 */     char c = ch; throw new IllegalArgumentException((new StringBuilder(32)).append("Illegal hexadecimal character: ").append(c).toString());
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean equals(@Nullable Object object) {
/* 355 */     if (object instanceof HashCode) {
/* 356 */       HashCode that = (HashCode)object;
/* 357 */       return (bits() == that.bits() && equalsSameBits(that));
/*     */     } 
/* 359 */     return false;
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
/*     */   public final int hashCode() {
/* 371 */     if (bits() >= 32) {
/* 372 */       return asInt();
/*     */     }
/*     */     
/* 375 */     byte[] bytes = asBytes();
/* 376 */     int val = bytes[0] & 0xFF;
/* 377 */     for (int i = 1; i < bytes.length; i++) {
/* 378 */       val |= (bytes[i] & 0xFF) << i * 8;
/*     */     }
/* 380 */     return val;
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
/*     */   public final String toString() {
/* 396 */     byte[] bytes = asBytes();
/* 397 */     StringBuilder sb = new StringBuilder(2 * bytes.length);
/* 398 */     for (byte b : bytes) {
/* 399 */       sb.append(hexDigits[b >> 4 & 0xF]).append(hexDigits[b & 0xF]);
/*     */     }
/* 401 */     return sb.toString();
/*     */   }
/*     */   
/* 404 */   private static final char[] hexDigits = "0123456789abcdef".toCharArray();
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\hash\HashCode.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */