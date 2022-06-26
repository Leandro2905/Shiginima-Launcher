/*     */ package org.apache.commons.codec.binary;
/*     */ 
/*     */ import java.io.FilterOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BaseNCodecOutputStream
/*     */   extends FilterOutputStream
/*     */ {
/*     */   private final boolean doEncode;
/*     */   private final BaseNCodec baseNCodec;
/*  40 */   private final byte[] singleByte = new byte[1];
/*     */   
/*  42 */   private final BaseNCodec.Context context = new BaseNCodec.Context();
/*     */ 
/*     */   
/*     */   public BaseNCodecOutputStream(OutputStream out, BaseNCodec basedCodec, boolean doEncode) {
/*  46 */     super(out);
/*  47 */     this.baseNCodec = basedCodec;
/*  48 */     this.doEncode = doEncode;
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
/*     */   public void write(int i) throws IOException {
/*  61 */     this.singleByte[0] = (byte)i;
/*  62 */     write(this.singleByte, 0, 1);
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
/*     */   public void write(byte[] b, int offset, int len) throws IOException {
/*  85 */     if (b == null)
/*  86 */       throw new NullPointerException(); 
/*  87 */     if (offset < 0 || len < 0)
/*  88 */       throw new IndexOutOfBoundsException(); 
/*  89 */     if (offset > b.length || offset + len > b.length)
/*  90 */       throw new IndexOutOfBoundsException(); 
/*  91 */     if (len > 0) {
/*  92 */       if (this.doEncode) {
/*  93 */         this.baseNCodec.encode(b, offset, len, this.context);
/*     */       } else {
/*  95 */         this.baseNCodec.decode(b, offset, len, this.context);
/*     */       } 
/*  97 */       flush(false);
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
/*     */   
/*     */   private void flush(boolean propagate) throws IOException {
/* 111 */     int avail = this.baseNCodec.available(this.context);
/* 112 */     if (avail > 0) {
/* 113 */       byte[] buf = new byte[avail];
/* 114 */       int c = this.baseNCodec.readResults(buf, 0, avail, this.context);
/* 115 */       if (c > 0) {
/* 116 */         this.out.write(buf, 0, c);
/*     */       }
/*     */     } 
/* 119 */     if (propagate) {
/* 120 */       this.out.flush();
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
/*     */   public void flush() throws IOException {
/* 132 */     flush(true);
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
/*     */   public void close() throws IOException {
/* 144 */     if (this.doEncode) {
/* 145 */       this.baseNCodec.encode(this.singleByte, 0, -1, this.context);
/*     */     } else {
/* 147 */       this.baseNCodec.decode(this.singleByte, 0, -1, this.context);
/*     */     } 
/* 149 */     flush();
/* 150 */     this.out.close();
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\commons\codec\binary\BaseNCodecOutputStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */