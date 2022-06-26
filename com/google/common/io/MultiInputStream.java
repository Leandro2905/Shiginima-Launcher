/*     */ package com.google.common.io;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.Iterator;
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
/*     */ final class MultiInputStream
/*     */   extends InputStream
/*     */ {
/*     */   private Iterator<? extends ByteSource> it;
/*     */   private InputStream in;
/*     */   
/*     */   public MultiInputStream(Iterator<? extends ByteSource> it) throws IOException {
/*  46 */     this.it = (Iterator<? extends ByteSource>)Preconditions.checkNotNull(it);
/*  47 */     advance();
/*     */   }
/*     */   
/*     */   public void close() throws IOException {
/*  51 */     if (this.in != null) {
/*     */       try {
/*  53 */         this.in.close();
/*     */       } finally {
/*  55 */         this.in = null;
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void advance() throws IOException {
/*  64 */     close();
/*  65 */     if (this.it.hasNext()) {
/*  66 */       this.in = ((ByteSource)this.it.next()).openStream();
/*     */     }
/*     */   }
/*     */   
/*     */   public int available() throws IOException {
/*  71 */     if (this.in == null) {
/*  72 */       return 0;
/*     */     }
/*  74 */     return this.in.available();
/*     */   }
/*     */   
/*     */   public boolean markSupported() {
/*  78 */     return false;
/*     */   }
/*     */   
/*     */   public int read() throws IOException {
/*  82 */     if (this.in == null) {
/*  83 */       return -1;
/*     */     }
/*  85 */     int result = this.in.read();
/*  86 */     if (result == -1) {
/*  87 */       advance();
/*  88 */       return read();
/*     */     } 
/*  90 */     return result;
/*     */   }
/*     */   
/*     */   public int read(@Nullable byte[] b, int off, int len) throws IOException {
/*  94 */     if (this.in == null) {
/*  95 */       return -1;
/*     */     }
/*  97 */     int result = this.in.read(b, off, len);
/*  98 */     if (result == -1) {
/*  99 */       advance();
/* 100 */       return read(b, off, len);
/*     */     } 
/* 102 */     return result;
/*     */   }
/*     */   
/*     */   public long skip(long n) throws IOException {
/* 106 */     if (this.in == null || n <= 0L) {
/* 107 */       return 0L;
/*     */     }
/* 109 */     long result = this.in.skip(n);
/* 110 */     if (result != 0L) {
/* 111 */       return result;
/*     */     }
/* 113 */     if (read() == -1) {
/* 114 */       return 0L;
/*     */     }
/* 116 */     return 1L + this.in.skip(n - 1L);
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\io\MultiInputStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */