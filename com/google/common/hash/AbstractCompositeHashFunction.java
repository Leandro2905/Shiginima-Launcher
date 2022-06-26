/*     */ package com.google.common.hash;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.nio.charset.Charset;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ abstract class AbstractCompositeHashFunction
/*     */   extends AbstractStreamingHashFunction
/*     */ {
/*     */   final HashFunction[] functions;
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   AbstractCompositeHashFunction(HashFunction... functions) {
/*  34 */     for (HashFunction function : functions) {
/*  35 */       Preconditions.checkNotNull(function);
/*     */     }
/*  37 */     this.functions = functions;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   abstract HashCode makeHash(Hasher[] paramArrayOfHasher);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Hasher newHasher() {
/*  50 */     final Hasher[] hashers = new Hasher[this.functions.length];
/*  51 */     for (int i = 0; i < hashers.length; i++) {
/*  52 */       hashers[i] = this.functions[i].newHasher();
/*     */     }
/*  54 */     return new Hasher() {
/*     */         public Hasher putByte(byte b) {
/*  56 */           for (Hasher hasher : hashers) {
/*  57 */             hasher.putByte(b);
/*     */           }
/*  59 */           return this;
/*     */         }
/*     */         
/*     */         public Hasher putBytes(byte[] bytes) {
/*  63 */           for (Hasher hasher : hashers) {
/*  64 */             hasher.putBytes(bytes);
/*     */           }
/*  66 */           return this;
/*     */         }
/*     */         
/*     */         public Hasher putBytes(byte[] bytes, int off, int len) {
/*  70 */           for (Hasher hasher : hashers) {
/*  71 */             hasher.putBytes(bytes, off, len);
/*     */           }
/*  73 */           return this;
/*     */         }
/*     */         
/*     */         public Hasher putShort(short s) {
/*  77 */           for (Hasher hasher : hashers) {
/*  78 */             hasher.putShort(s);
/*     */           }
/*  80 */           return this;
/*     */         }
/*     */         
/*     */         public Hasher putInt(int i) {
/*  84 */           for (Hasher hasher : hashers) {
/*  85 */             hasher.putInt(i);
/*     */           }
/*  87 */           return this;
/*     */         }
/*     */         
/*     */         public Hasher putLong(long l) {
/*  91 */           for (Hasher hasher : hashers) {
/*  92 */             hasher.putLong(l);
/*     */           }
/*  94 */           return this;
/*     */         }
/*     */         
/*     */         public Hasher putFloat(float f) {
/*  98 */           for (Hasher hasher : hashers) {
/*  99 */             hasher.putFloat(f);
/*     */           }
/* 101 */           return this;
/*     */         }
/*     */         
/*     */         public Hasher putDouble(double d) {
/* 105 */           for (Hasher hasher : hashers) {
/* 106 */             hasher.putDouble(d);
/*     */           }
/* 108 */           return this;
/*     */         }
/*     */         
/*     */         public Hasher putBoolean(boolean b) {
/* 112 */           for (Hasher hasher : hashers) {
/* 113 */             hasher.putBoolean(b);
/*     */           }
/* 115 */           return this;
/*     */         }
/*     */         
/*     */         public Hasher putChar(char c) {
/* 119 */           for (Hasher hasher : hashers) {
/* 120 */             hasher.putChar(c);
/*     */           }
/* 122 */           return this;
/*     */         }
/*     */         
/*     */         public Hasher putUnencodedChars(CharSequence chars) {
/* 126 */           for (Hasher hasher : hashers) {
/* 127 */             hasher.putUnencodedChars(chars);
/*     */           }
/* 129 */           return this;
/*     */         }
/*     */         
/*     */         public Hasher putString(CharSequence chars, Charset charset) {
/* 133 */           for (Hasher hasher : hashers) {
/* 134 */             hasher.putString(chars, charset);
/*     */           }
/* 136 */           return this;
/*     */         }
/*     */         
/*     */         public <T> Hasher putObject(T instance, Funnel<? super T> funnel) {
/* 140 */           for (Hasher hasher : hashers) {
/* 141 */             hasher.putObject(instance, funnel);
/*     */           }
/* 143 */           return this;
/*     */         }
/*     */         
/*     */         public HashCode hash() {
/* 147 */           return AbstractCompositeHashFunction.this.makeHash(hashers);
/*     */         }
/*     */       };
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\hash\AbstractCompositeHashFunction.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */