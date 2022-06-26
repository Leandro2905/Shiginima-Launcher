/*     */ package org.apache.commons.codec.binary;
/*     */ 
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
/*     */ 
/*     */ 
/*     */ public class BaseNCodecInputStream
/*     */   extends FilterInputStream
/*     */ {
/*     */   private final BaseNCodec baseNCodec;
/*     */   private final boolean doEncode;
/*  40 */   private final byte[] singleByte = new byte[1];
/*     */   
/*  42 */   private final BaseNCodec.Context context = new BaseNCodec.Context();
/*     */   
/*     */   protected BaseNCodecInputStream(InputStream in, BaseNCodec baseNCodec, boolean doEncode) {
/*  45 */     super(in);
/*  46 */     this.doEncode = doEncode;
/*  47 */     this.baseNCodec = baseNCodec;
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
/*     */   public int available() throws IOException {
/*  64 */     return this.context.eof ? 0 : 1;
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
/*     */   public synchronized void mark(int readLimit) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean markSupported() {
/*  85 */     return false;
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
/*     */   public int read() throws IOException {
/*  97 */     int r = read(this.singleByte, 0, 1);
/*  98 */     while (r == 0) {
/*  99 */       r = read(this.singleByte, 0, 1);
/*     */     }
/* 101 */     if (r > 0) {
/* 102 */       byte b = this.singleByte[0];
/* 103 */       return (b < 0) ? (256 + b) : b;
/*     */     } 
/* 105 */     return -1;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int read(byte[] b, int offset, int len) throws IOException {
/* 129 */     if (b == null)
/* 130 */       throw new NullPointerException(); 
/* 131 */     if (offset < 0 || len < 0)
/* 132 */       throw new IndexOutOfBoundsException(); 
/* 133 */     if (offset > b.length || offset + len > b.length)
/* 134 */       throw new IndexOutOfBoundsException(); 
/* 135 */     if (len == 0) {
/* 136 */       return 0;
/*     */     }
/* 138 */     int readLen = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 155 */     while (readLen == 0) {
/* 156 */       if (!this.baseNCodec.hasData(this.context)) {
/* 157 */         byte[] buf = new byte[this.doEncode ? 4096 : 8192];
/* 158 */         int c = this.in.read(buf);
/* 159 */         if (this.doEncode) {
/* 160 */           this.baseNCodec.encode(buf, 0, c, this.context);
/*     */         } else {
/* 162 */           this.baseNCodec.decode(buf, 0, c, this.context);
/*     */         } 
/*     */       } 
/* 165 */       readLen = this.baseNCodec.readResults(b, offset, len, this.context);
/*     */     } 
/* 167 */     return readLen;
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
/*     */   public synchronized void reset() throws IOException {
/* 181 */     throw new IOException("mark/reset not supported");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long skip(long n) throws IOException {
/* 192 */     if (n < 0L) {
/* 193 */       throw new IllegalArgumentException("Negative skip length: " + n);
/*     */     }
/*     */ 
/*     */     
/* 197 */     byte[] b = new byte[512];
/* 198 */     long todo = n;
/*     */     
/* 200 */     while (todo > 0L) {
/* 201 */       int len = (int)Math.min(b.length, todo);
/* 202 */       len = read(b, 0, len);
/* 203 */       if (len == -1) {
/*     */         break;
/*     */       }
/* 206 */       todo -= len;
/*     */     } 
/*     */     
/* 209 */     return n - todo;
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\commons\codec\binary\BaseNCodecInputStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */