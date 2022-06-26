/*     */ package org.apache.commons.io.output;
/*     */ 
/*     */ import java.io.FilterWriter;
/*     */ import java.io.IOException;
/*     */ import java.io.Writer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ProxyWriter
/*     */   extends FilterWriter
/*     */ {
/*     */   public ProxyWriter(Writer proxy) {
/*  41 */     super(proxy);
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
/*     */   public Writer append(char c) throws IOException {
/*     */     try {
/*  55 */       beforeWrite(1);
/*  56 */       this.out.append(c);
/*  57 */       afterWrite(1);
/*  58 */     } catch (IOException e) {
/*  59 */       handleIOException(e);
/*     */     } 
/*  61 */     return this;
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
/*     */   public Writer append(CharSequence csq, int start, int end) throws IOException {
/*     */     try {
/*  76 */       beforeWrite(end - start);
/*  77 */       this.out.append(csq, start, end);
/*  78 */       afterWrite(end - start);
/*  79 */     } catch (IOException e) {
/*  80 */       handleIOException(e);
/*     */     } 
/*  82 */     return this;
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
/*     */   public Writer append(CharSequence csq) throws IOException {
/*     */     try {
/*  95 */       int len = 0;
/*  96 */       if (csq != null) {
/*  97 */         len = csq.length();
/*     */       }
/*     */       
/* 100 */       beforeWrite(len);
/* 101 */       this.out.append(csq);
/* 102 */       afterWrite(len);
/* 103 */     } catch (IOException e) {
/* 104 */       handleIOException(e);
/*     */     } 
/* 106 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(int idx) throws IOException {
/*     */     try {
/* 117 */       beforeWrite(1);
/* 118 */       this.out.write(idx);
/* 119 */       afterWrite(1);
/* 120 */     } catch (IOException e) {
/* 121 */       handleIOException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(char[] chr) throws IOException {
/*     */     try {
/* 133 */       int len = 0;
/* 134 */       if (chr != null) {
/* 135 */         len = chr.length;
/*     */       }
/*     */       
/* 138 */       beforeWrite(len);
/* 139 */       this.out.write(chr);
/* 140 */       afterWrite(len);
/* 141 */     } catch (IOException e) {
/* 142 */       handleIOException(e);
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
/*     */   public void write(char[] chr, int st, int len) throws IOException {
/*     */     try {
/* 156 */       beforeWrite(len);
/* 157 */       this.out.write(chr, st, len);
/* 158 */       afterWrite(len);
/* 159 */     } catch (IOException e) {
/* 160 */       handleIOException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(String str) throws IOException {
/*     */     try {
/* 172 */       int len = 0;
/* 173 */       if (str != null) {
/* 174 */         len = str.length();
/*     */       }
/*     */       
/* 177 */       beforeWrite(len);
/* 178 */       this.out.write(str);
/* 179 */       afterWrite(len);
/* 180 */     } catch (IOException e) {
/* 181 */       handleIOException(e);
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
/*     */   public void write(String str, int st, int len) throws IOException {
/*     */     try {
/* 195 */       beforeWrite(len);
/* 196 */       this.out.write(str, st, len);
/* 197 */       afterWrite(len);
/* 198 */     } catch (IOException e) {
/* 199 */       handleIOException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void flush() throws IOException {
/*     */     try {
/* 210 */       this.out.flush();
/* 211 */     } catch (IOException e) {
/* 212 */       handleIOException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/*     */     try {
/* 223 */       this.out.close();
/* 224 */     } catch (IOException e) {
/* 225 */       handleIOException(e);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void beforeWrite(int n) throws IOException {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void afterWrite(int n) throws IOException {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void handleIOException(IOException e) throws IOException {
/* 272 */     throw e;
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\commons\io\output\ProxyWriter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */