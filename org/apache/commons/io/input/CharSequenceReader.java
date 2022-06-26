/*     */ package org.apache.commons.io.input;
/*     */ 
/*     */ import java.io.Reader;
/*     */ import java.io.Serializable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CharSequenceReader
/*     */   extends Reader
/*     */   implements Serializable
/*     */ {
/*     */   private final CharSequence charSequence;
/*     */   private int idx;
/*     */   private int mark;
/*     */   
/*     */   public CharSequenceReader(CharSequence charSequence) {
/*  43 */     this.charSequence = (charSequence != null) ? charSequence : "";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() {
/*  51 */     this.idx = 0;
/*  52 */     this.mark = 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void mark(int readAheadLimit) {
/*  62 */     this.mark = this.idx;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean markSupported() {
/*  72 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int read() {
/*  83 */     if (this.idx >= this.charSequence.length()) {
/*  84 */       return -1;
/*     */     }
/*  86 */     return this.charSequence.charAt(this.idx++);
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
/*     */   public int read(char[] array, int offset, int length) {
/* 101 */     if (this.idx >= this.charSequence.length()) {
/* 102 */       return -1;
/*     */     }
/* 104 */     if (array == null) {
/* 105 */       throw new NullPointerException("Character array is missing");
/*     */     }
/* 107 */     if (length < 0 || offset < 0 || offset + length > array.length) {
/* 108 */       throw new IndexOutOfBoundsException("Array Size=" + array.length + ", offset=" + offset + ", length=" + length);
/*     */     }
/*     */     
/* 111 */     int count = 0;
/* 112 */     for (int i = 0; i < length; i++) {
/* 113 */       int c = read();
/* 114 */       if (c == -1) {
/* 115 */         return count;
/*     */       }
/* 117 */       array[offset + i] = (char)c;
/* 118 */       count++;
/*     */     } 
/* 120 */     return count;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void reset() {
/* 129 */     this.idx = this.mark;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long skip(long n) {
/* 140 */     if (n < 0L) {
/* 141 */       throw new IllegalArgumentException("Number of characters to skip is less than zero: " + n);
/*     */     }
/*     */     
/* 144 */     if (this.idx >= this.charSequence.length()) {
/* 145 */       return -1L;
/*     */     }
/* 147 */     int dest = (int)Math.min(this.charSequence.length(), this.idx + n);
/* 148 */     int count = dest - this.idx;
/* 149 */     this.idx = dest;
/* 150 */     return count;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 161 */     return this.charSequence.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\commons\io\input\CharSequenceReader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */