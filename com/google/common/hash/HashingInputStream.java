/*     */ package com.google.common.hash;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.io.FilterInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public final class HashingInputStream
/*     */   extends FilterInputStream
/*     */ {
/*     */   private final Hasher hasher;
/*     */   
/*     */   public HashingInputStream(HashFunction hashFunction, InputStream in) {
/*  42 */     super((InputStream)Preconditions.checkNotNull(in));
/*  43 */     this.hasher = (Hasher)Preconditions.checkNotNull(hashFunction.newHasher());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int read() throws IOException {
/*  52 */     int b = this.in.read();
/*  53 */     if (b != -1) {
/*  54 */       this.hasher.putByte((byte)b);
/*     */     }
/*  56 */     return b;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int read(byte[] bytes, int off, int len) throws IOException {
/*  65 */     int numOfBytesRead = this.in.read(bytes, off, len);
/*  66 */     if (numOfBytesRead != -1) {
/*  67 */       this.hasher.putBytes(bytes, off, numOfBytesRead);
/*     */     }
/*  69 */     return numOfBytesRead;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean markSupported() {
/*  78 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void mark(int readlimit) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void reset() throws IOException {
/*  93 */     throw new IOException("reset not supported");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HashCode hash() {
/* 101 */     return this.hasher.hash();
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\hash\HashingInputStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */