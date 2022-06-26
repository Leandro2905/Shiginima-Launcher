/*     */ package com.google.common.hash;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.ByteOrder;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ abstract class AbstractByteHasher
/*     */   extends AbstractHasher
/*     */ {
/*  38 */   private final ByteBuffer scratch = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void update(byte[] b) {
/*  49 */     update(b, 0, b.length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void update(byte[] b, int off, int len) {
/*  56 */     for (int i = off; i < off + len; i++) {
/*  57 */       update(b[i]);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public Hasher putByte(byte b) {
/*  63 */     update(b);
/*  64 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Hasher putBytes(byte[] bytes) {
/*  69 */     Preconditions.checkNotNull(bytes);
/*  70 */     update(bytes);
/*  71 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Hasher putBytes(byte[] bytes, int off, int len) {
/*  76 */     Preconditions.checkPositionIndexes(off, off + len, bytes.length);
/*  77 */     update(bytes, off, len);
/*  78 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Hasher update(int bytes) {
/*     */     try {
/*  86 */       update(this.scratch.array(), 0, bytes);
/*     */     } finally {
/*  88 */       this.scratch.clear();
/*     */     } 
/*  90 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Hasher putShort(short s) {
/*  95 */     this.scratch.putShort(s);
/*  96 */     return update(2);
/*     */   }
/*     */ 
/*     */   
/*     */   public Hasher putInt(int i) {
/* 101 */     this.scratch.putInt(i);
/* 102 */     return update(4);
/*     */   }
/*     */ 
/*     */   
/*     */   public Hasher putLong(long l) {
/* 107 */     this.scratch.putLong(l);
/* 108 */     return update(8);
/*     */   }
/*     */ 
/*     */   
/*     */   public Hasher putChar(char c) {
/* 113 */     this.scratch.putChar(c);
/* 114 */     return update(2);
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> Hasher putObject(T instance, Funnel<? super T> funnel) {
/* 119 */     funnel.funnel(instance, this);
/* 120 */     return this;
/*     */   }
/*     */   
/*     */   protected abstract void update(byte paramByte);
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\hash\AbstractByteHasher.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */