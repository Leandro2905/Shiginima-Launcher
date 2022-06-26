/*     */ package org.apache.commons.io.input;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TeeInputStream
/*     */   extends ProxyInputStream
/*     */ {
/*     */   private final OutputStream branch;
/*     */   private final boolean closeBranch;
/*     */   
/*     */   public TeeInputStream(InputStream input, OutputStream branch) {
/*  60 */     this(input, branch, false);
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
/*     */   public TeeInputStream(InputStream input, OutputStream branch, boolean closeBranch) {
/*  76 */     super(input);
/*  77 */     this.branch = branch;
/*  78 */     this.closeBranch = closeBranch;
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
/*     */     try {
/*  91 */       super.close();
/*     */     } finally {
/*  93 */       if (this.closeBranch) {
/*  94 */         this.branch.close();
/*     */       }
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
/*     */   public int read() throws IOException {
/* 108 */     int ch = super.read();
/* 109 */     if (ch != -1) {
/* 110 */       this.branch.write(ch);
/*     */     }
/* 112 */     return ch;
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
/*     */   public int read(byte[] bts, int st, int end) throws IOException {
/* 127 */     int n = super.read(bts, st, end);
/* 128 */     if (n != -1) {
/* 129 */       this.branch.write(bts, st, n);
/*     */     }
/* 131 */     return n;
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
/*     */   public int read(byte[] bts) throws IOException {
/* 144 */     int n = super.read(bts);
/* 145 */     if (n != -1) {
/* 146 */       this.branch.write(bts, 0, n);
/*     */     }
/* 148 */     return n;
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\commons\io\input\TeeInputStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */