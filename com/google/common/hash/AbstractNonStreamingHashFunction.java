/*     */ package com.google.common.hash;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
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
/*     */ 
/*     */ 
/*     */ abstract class AbstractNonStreamingHashFunction
/*     */   implements HashFunction
/*     */ {
/*     */   public Hasher newHasher() {
/*  35 */     return new BufferingHasher(32);
/*     */   }
/*     */ 
/*     */   
/*     */   public Hasher newHasher(int expectedInputSize) {
/*  40 */     Preconditions.checkArgument((expectedInputSize >= 0));
/*  41 */     return new BufferingHasher(expectedInputSize);
/*     */   }
/*     */   
/*     */   public <T> HashCode hashObject(T instance, Funnel<? super T> funnel) {
/*  45 */     return newHasher().<T>putObject(instance, funnel).hash();
/*     */   }
/*     */   
/*     */   public HashCode hashUnencodedChars(CharSequence input) {
/*  49 */     int len = input.length();
/*  50 */     Hasher hasher = newHasher(len * 2);
/*  51 */     for (int i = 0; i < len; i++) {
/*  52 */       hasher.putChar(input.charAt(i));
/*     */     }
/*  54 */     return hasher.hash();
/*     */   }
/*     */   
/*     */   public HashCode hashString(CharSequence input, Charset charset) {
/*  58 */     return hashBytes(input.toString().getBytes(charset));
/*     */   }
/*     */   
/*     */   public HashCode hashInt(int input) {
/*  62 */     return newHasher(4).putInt(input).hash();
/*     */   }
/*     */   
/*     */   public HashCode hashLong(long input) {
/*  66 */     return newHasher(8).putLong(input).hash();
/*     */   }
/*     */   
/*     */   public HashCode hashBytes(byte[] input) {
/*  70 */     return hashBytes(input, 0, input.length);
/*     */   }
/*     */ 
/*     */   
/*     */   private final class BufferingHasher
/*     */     extends AbstractHasher
/*     */   {
/*     */     final AbstractNonStreamingHashFunction.ExposedByteArrayOutputStream stream;
/*     */     static final int BOTTOM_BYTE = 255;
/*     */     
/*     */     BufferingHasher(int expectedInputSize) {
/*  81 */       this.stream = new AbstractNonStreamingHashFunction.ExposedByteArrayOutputStream(expectedInputSize);
/*     */     }
/*     */ 
/*     */     
/*     */     public Hasher putByte(byte b) {
/*  86 */       this.stream.write(b);
/*  87 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public Hasher putBytes(byte[] bytes) {
/*     */       try {
/*  93 */         this.stream.write(bytes);
/*  94 */       } catch (IOException e) {
/*  95 */         throw new RuntimeException(e);
/*     */       } 
/*  97 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public Hasher putBytes(byte[] bytes, int off, int len) {
/* 102 */       this.stream.write(bytes, off, len);
/* 103 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public Hasher putShort(short s) {
/* 108 */       this.stream.write(s & 0xFF);
/* 109 */       this.stream.write(s >>> 8 & 0xFF);
/* 110 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public Hasher putInt(int i) {
/* 115 */       this.stream.write(i & 0xFF);
/* 116 */       this.stream.write(i >>> 8 & 0xFF);
/* 117 */       this.stream.write(i >>> 16 & 0xFF);
/* 118 */       this.stream.write(i >>> 24 & 0xFF);
/* 119 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public Hasher putLong(long l) {
/* 124 */       for (int i = 0; i < 64; i += 8) {
/* 125 */         this.stream.write((byte)(int)(l >>> i & 0xFFL));
/*     */       }
/* 127 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public Hasher putChar(char c) {
/* 132 */       this.stream.write(c & 0xFF);
/* 133 */       this.stream.write(c >>> 8 & 0xFF);
/* 134 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public <T> Hasher putObject(T instance, Funnel<? super T> funnel) {
/* 139 */       funnel.funnel(instance, this);
/* 140 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public HashCode hash() {
/* 145 */       return AbstractNonStreamingHashFunction.this.hashBytes(this.stream.byteArray(), 0, this.stream.length());
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class ExposedByteArrayOutputStream
/*     */     extends ByteArrayOutputStream {
/*     */     ExposedByteArrayOutputStream(int expectedInputSize) {
/* 152 */       super(expectedInputSize);
/*     */     }
/*     */     byte[] byteArray() {
/* 155 */       return this.buf;
/*     */     }
/*     */     int length() {
/* 158 */       return this.count;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\hash\AbstractNonStreamingHashFunction.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */