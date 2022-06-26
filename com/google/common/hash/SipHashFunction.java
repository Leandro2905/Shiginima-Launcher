/*     */ package com.google.common.hash;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
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
/*     */ final class SipHashFunction
/*     */   extends AbstractStreamingHashFunction
/*     */   implements Serializable
/*     */ {
/*     */   private final int c;
/*     */   private final int d;
/*     */   private final long k0;
/*     */   private final long k1;
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   SipHashFunction(int c, int d, long k0, long k1) {
/*  53 */     Preconditions.checkArgument((c > 0), "The number of SipRound iterations (c=%s) during Compression must be positive.", new Object[] { Integer.valueOf(c) });
/*     */     
/*  55 */     Preconditions.checkArgument((d > 0), "The number of SipRound iterations (d=%s) during Finalization must be positive.", new Object[] { Integer.valueOf(d) });
/*     */     
/*  57 */     this.c = c;
/*  58 */     this.d = d;
/*  59 */     this.k0 = k0;
/*  60 */     this.k1 = k1;
/*     */   }
/*     */   
/*     */   public int bits() {
/*  64 */     return 64;
/*     */   }
/*     */   
/*     */   public Hasher newHasher() {
/*  68 */     return new SipHasher(this.c, this.d, this.k0, this.k1);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/*  74 */     int i = this.c, j = this.d; long l1 = this.k0, l2 = this.k1; return (new StringBuilder(81)).append("Hashing.sipHash").append(i).append(j).append("(").append(l1).append(", ").append(l2).append(")").toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(@Nullable Object object) {
/*  79 */     if (object instanceof SipHashFunction) {
/*  80 */       SipHashFunction other = (SipHashFunction)object;
/*  81 */       return (this.c == other.c && this.d == other.d && this.k0 == other.k0 && this.k1 == other.k1);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*  86 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  91 */     return (int)((getClass().hashCode() ^ this.c ^ this.d) ^ this.k0 ^ this.k1);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class SipHasher
/*     */     extends AbstractStreamingHashFunction.AbstractStreamingHasher
/*     */   {
/*     */     private static final int CHUNK_SIZE = 8;
/*     */ 
/*     */     
/*     */     private final int c;
/*     */     
/*     */     private final int d;
/*     */     
/* 106 */     private long v0 = 8317987319222330741L;
/* 107 */     private long v1 = 7237128888997146477L;
/* 108 */     private long v2 = 7816392313619706465L;
/* 109 */     private long v3 = 8387220255154660723L;
/*     */ 
/*     */     
/* 112 */     private long b = 0L;
/*     */ 
/*     */ 
/*     */     
/* 116 */     private long finalM = 0L;
/*     */     
/*     */     SipHasher(int c, int d, long k0, long k1) {
/* 119 */       super(8);
/* 120 */       this.c = c;
/* 121 */       this.d = d;
/* 122 */       this.v0 ^= k0;
/* 123 */       this.v1 ^= k1;
/* 124 */       this.v2 ^= k0;
/* 125 */       this.v3 ^= k1;
/*     */     }
/*     */     
/*     */     protected void process(ByteBuffer buffer) {
/* 129 */       this.b += 8L;
/* 130 */       processM(buffer.getLong());
/*     */     }
/*     */     
/*     */     protected void processRemaining(ByteBuffer buffer) {
/* 134 */       this.b += buffer.remaining();
/* 135 */       for (int i = 0; buffer.hasRemaining(); i += 8) {
/* 136 */         this.finalM ^= (buffer.get() & 0xFFL) << i;
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public HashCode makeHash() {
/* 142 */       this.finalM ^= this.b << 56L;
/* 143 */       processM(this.finalM);
/*     */ 
/*     */       
/* 146 */       this.v2 ^= 0xFFL;
/* 147 */       sipRound(this.d);
/* 148 */       return HashCode.fromLong(this.v0 ^ this.v1 ^ this.v2 ^ this.v3);
/*     */     }
/*     */     
/*     */     private void processM(long m) {
/* 152 */       this.v3 ^= m;
/* 153 */       sipRound(this.c);
/* 154 */       this.v0 ^= m;
/*     */     }
/*     */     
/*     */     private void sipRound(int iterations) {
/* 158 */       for (int i = 0; i < iterations; i++) {
/* 159 */         this.v0 += this.v1;
/* 160 */         this.v2 += this.v3;
/* 161 */         this.v1 = Long.rotateLeft(this.v1, 13);
/* 162 */         this.v3 = Long.rotateLeft(this.v3, 16);
/* 163 */         this.v1 ^= this.v0;
/* 164 */         this.v3 ^= this.v2;
/* 165 */         this.v0 = Long.rotateLeft(this.v0, 32);
/* 166 */         this.v2 += this.v1;
/* 167 */         this.v0 += this.v3;
/* 168 */         this.v1 = Long.rotateLeft(this.v1, 17);
/* 169 */         this.v3 = Long.rotateLeft(this.v3, 21);
/* 170 */         this.v1 ^= this.v2;
/* 171 */         this.v3 ^= this.v0;
/* 172 */         this.v2 = Long.rotateLeft(this.v2, 32);
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\hash\SipHashFunction.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */