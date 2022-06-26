/*     */ package com.google.common.hash;
/*     */ 
/*     */ import com.google.common.primitives.UnsignedBytes;
/*     */ import java.io.Serializable;
/*     */ import java.nio.ByteBuffer;
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
/*     */ final class Murmur3_32HashFunction
/*     */   extends AbstractStreamingHashFunction
/*     */   implements Serializable
/*     */ {
/*     */   private static final int C1 = -862048943;
/*     */   private static final int C2 = 461845907;
/*     */   private final int seed;
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   Murmur3_32HashFunction(int seed) {
/*  54 */     this.seed = seed;
/*     */   }
/*     */   
/*     */   public int bits() {
/*  58 */     return 32;
/*     */   }
/*     */   
/*     */   public Hasher newHasher() {
/*  62 */     return new Murmur3_32Hasher(this.seed);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  67 */     int i = this.seed; return (new StringBuilder(31)).append("Hashing.murmur3_32(").append(i).append(")").toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(@Nullable Object object) {
/*  72 */     if (object instanceof Murmur3_32HashFunction) {
/*  73 */       Murmur3_32HashFunction other = (Murmur3_32HashFunction)object;
/*  74 */       return (this.seed == other.seed);
/*     */     } 
/*  76 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  81 */     return getClass().hashCode() ^ this.seed;
/*     */   }
/*     */   
/*     */   public HashCode hashInt(int input) {
/*  85 */     int k1 = mixK1(input);
/*  86 */     int h1 = mixH1(this.seed, k1);
/*     */     
/*  88 */     return fmix(h1, 4);
/*     */   }
/*     */   
/*     */   public HashCode hashLong(long input) {
/*  92 */     int low = (int)input;
/*  93 */     int high = (int)(input >>> 32L);
/*     */     
/*  95 */     int k1 = mixK1(low);
/*  96 */     int h1 = mixH1(this.seed, k1);
/*     */     
/*  98 */     k1 = mixK1(high);
/*  99 */     h1 = mixH1(h1, k1);
/*     */     
/* 101 */     return fmix(h1, 8);
/*     */   }
/*     */ 
/*     */   
/*     */   public HashCode hashUnencodedChars(CharSequence input) {
/* 106 */     int h1 = this.seed;
/*     */ 
/*     */     
/* 109 */     for (int i = 1; i < input.length(); i += 2) {
/* 110 */       int k1 = input.charAt(i - 1) | input.charAt(i) << 16;
/* 111 */       k1 = mixK1(k1);
/* 112 */       h1 = mixH1(h1, k1);
/*     */     } 
/*     */ 
/*     */     
/* 116 */     if ((input.length() & 0x1) == 1) {
/* 117 */       int k1 = input.charAt(input.length() - 1);
/* 118 */       k1 = mixK1(k1);
/* 119 */       h1 ^= k1;
/*     */     } 
/*     */     
/* 122 */     return fmix(h1, 2 * input.length());
/*     */   }
/*     */   
/*     */   private static int mixK1(int k1) {
/* 126 */     k1 *= -862048943;
/* 127 */     k1 = Integer.rotateLeft(k1, 15);
/* 128 */     k1 *= 461845907;
/* 129 */     return k1;
/*     */   }
/*     */   
/*     */   private static int mixH1(int h1, int k1) {
/* 133 */     h1 ^= k1;
/* 134 */     h1 = Integer.rotateLeft(h1, 13);
/* 135 */     h1 = h1 * 5 + -430675100;
/* 136 */     return h1;
/*     */   }
/*     */ 
/*     */   
/*     */   private static HashCode fmix(int h1, int length) {
/* 141 */     h1 ^= length;
/* 142 */     h1 ^= h1 >>> 16;
/* 143 */     h1 *= -2048144789;
/* 144 */     h1 ^= h1 >>> 13;
/* 145 */     h1 *= -1028477387;
/* 146 */     h1 ^= h1 >>> 16;
/* 147 */     return HashCode.fromInt(h1);
/*     */   }
/*     */   
/*     */   private static final class Murmur3_32Hasher extends AbstractStreamingHashFunction.AbstractStreamingHasher {
/*     */     private static final int CHUNK_SIZE = 4;
/*     */     private int h1;
/*     */     private int length;
/*     */     
/*     */     Murmur3_32Hasher(int seed) {
/* 156 */       super(4);
/* 157 */       this.h1 = seed;
/* 158 */       this.length = 0;
/*     */     }
/*     */     
/*     */     protected void process(ByteBuffer bb) {
/* 162 */       int k1 = Murmur3_32HashFunction.mixK1(bb.getInt());
/* 163 */       this.h1 = Murmur3_32HashFunction.mixH1(this.h1, k1);
/* 164 */       this.length += 4;
/*     */     }
/*     */     
/*     */     protected void processRemaining(ByteBuffer bb) {
/* 168 */       this.length += bb.remaining();
/* 169 */       int k1 = 0;
/* 170 */       for (int i = 0; bb.hasRemaining(); i += 8) {
/* 171 */         k1 ^= UnsignedBytes.toInt(bb.get()) << i;
/*     */       }
/* 173 */       this.h1 ^= Murmur3_32HashFunction.mixK1(k1);
/*     */     }
/*     */     
/*     */     public HashCode makeHash() {
/* 177 */       return Murmur3_32HashFunction.fmix(this.h1, this.length);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\hash\Murmur3_32HashFunction.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */