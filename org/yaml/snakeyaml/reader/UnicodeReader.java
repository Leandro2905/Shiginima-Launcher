/*     */ package org.yaml.snakeyaml.reader;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.PushbackInputStream;
/*     */ import java.io.Reader;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.CharsetDecoder;
/*     */ import java.nio.charset.CodingErrorAction;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class UnicodeReader
/*     */   extends Reader
/*     */ {
/*  54 */   private static final Charset UTF8 = Charset.forName("UTF-8");
/*  55 */   private static final Charset UTF16BE = Charset.forName("UTF-16BE");
/*  56 */   private static final Charset UTF16LE = Charset.forName("UTF-16LE");
/*     */   
/*     */   PushbackInputStream internalIn;
/*  59 */   InputStreamReader internalIn2 = null;
/*     */ 
/*     */ 
/*     */   
/*     */   private static final int BOM_SIZE = 3;
/*     */ 
/*     */ 
/*     */   
/*     */   public UnicodeReader(InputStream in) {
/*  68 */     this.internalIn = new PushbackInputStream(in, 3);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getEncoding() {
/*  76 */     return this.internalIn2.getEncoding();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void init() throws IOException {
/*     */     Charset encoding;
/*     */     int unread;
/*  84 */     if (this.internalIn2 != null) {
/*     */       return;
/*     */     }
/*     */     
/*  88 */     byte[] bom = new byte[3];
/*     */     
/*  90 */     int n = this.internalIn.read(bom, 0, bom.length);
/*     */     
/*  92 */     if (bom[0] == -17 && bom[1] == -69 && bom[2] == -65) {
/*  93 */       encoding = UTF8;
/*  94 */       unread = n - 3;
/*  95 */     } else if (bom[0] == -2 && bom[1] == -1) {
/*  96 */       encoding = UTF16BE;
/*  97 */       unread = n - 2;
/*  98 */     } else if (bom[0] == -1 && bom[1] == -2) {
/*  99 */       encoding = UTF16LE;
/* 100 */       unread = n - 2;
/*     */     } else {
/*     */       
/* 103 */       encoding = UTF8;
/* 104 */       unread = n;
/*     */     } 
/*     */     
/* 107 */     if (unread > 0) {
/* 108 */       this.internalIn.unread(bom, n - unread, unread);
/*     */     }
/*     */     
/* 111 */     CharsetDecoder decoder = encoding.newDecoder().onUnmappableCharacter(CodingErrorAction.REPORT);
/*     */     
/* 113 */     this.internalIn2 = new InputStreamReader(this.internalIn, decoder);
/*     */   }
/*     */   
/*     */   public void close() throws IOException {
/* 117 */     init();
/* 118 */     this.internalIn2.close();
/*     */   }
/*     */   
/*     */   public int read(char[] cbuf, int off, int len) throws IOException {
/* 122 */     init();
/* 123 */     return this.internalIn2.read(cbuf, off, len);
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\yaml\snakeyaml\reader\UnicodeReader.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */