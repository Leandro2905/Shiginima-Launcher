/*     */ package org.apache.commons.io.input;
/*     */ 
/*     */ import java.io.EOFException;
/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class NullReader
/*     */   extends Reader
/*     */ {
/*     */   private final long size;
/*     */   private long position;
/*  67 */   private long mark = -1L;
/*     */ 
/*     */   
/*     */   private long readlimit;
/*     */   
/*     */   private boolean eof;
/*     */   
/*     */   private final boolean throwEofException;
/*     */   
/*     */   private final boolean markSupported;
/*     */ 
/*     */   
/*     */   public NullReader(long size) {
/*  80 */     this(size, true, false);
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
/*     */   public NullReader(long size, boolean markSupported, boolean throwEofException) {
/*  95 */     this.size = size;
/*  96 */     this.markSupported = markSupported;
/*  97 */     this.throwEofException = throwEofException;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getPosition() {
/* 106 */     return this.position;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getSize() {
/* 115 */     return this.size;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 126 */     this.eof = false;
/* 127 */     this.position = 0L;
/* 128 */     this.mark = -1L;
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
/*     */   public synchronized void mark(int readlimit) {
/* 140 */     if (!this.markSupported) {
/* 141 */       throw new UnsupportedOperationException("Mark not supported");
/*     */     }
/* 143 */     this.mark = this.position;
/* 144 */     this.readlimit = readlimit;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean markSupported() {
/* 154 */     return this.markSupported;
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
/*     */   public int read() throws IOException {
/* 169 */     if (this.eof) {
/* 170 */       throw new IOException("Read after end of file");
/*     */     }
/* 172 */     if (this.position == this.size) {
/* 173 */       return doEndOfFile();
/*     */     }
/* 175 */     this.position++;
/* 176 */     return processChar();
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
/*     */   public int read(char[] chars) throws IOException {
/* 192 */     return read(chars, 0, chars.length);
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
/*     */   public int read(char[] chars, int offset, int length) throws IOException {
/* 210 */     if (this.eof) {
/* 211 */       throw new IOException("Read after end of file");
/*     */     }
/* 213 */     if (this.position == this.size) {
/* 214 */       return doEndOfFile();
/*     */     }
/* 216 */     this.position += length;
/* 217 */     int returnLength = length;
/* 218 */     if (this.position > this.size) {
/* 219 */       returnLength = length - (int)(this.position - this.size);
/* 220 */       this.position = this.size;
/*     */     } 
/* 222 */     processChars(chars, offset, returnLength);
/* 223 */     return returnLength;
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
/*     */   public synchronized void reset() throws IOException {
/* 236 */     if (!this.markSupported) {
/* 237 */       throw new UnsupportedOperationException("Mark not supported");
/*     */     }
/* 239 */     if (this.mark < 0L) {
/* 240 */       throw new IOException("No position has been marked");
/*     */     }
/* 242 */     if (this.position > this.mark + this.readlimit) {
/* 243 */       throw new IOException("Marked position [" + this.mark + "] is no longer valid - passed the read limit [" + this.readlimit + "]");
/*     */     }
/*     */ 
/*     */     
/* 247 */     this.position = this.mark;
/* 248 */     this.eof = false;
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
/*     */   public long skip(long numberOfChars) throws IOException {
/* 264 */     if (this.eof) {
/* 265 */       throw new IOException("Skip after end of file");
/*     */     }
/* 267 */     if (this.position == this.size) {
/* 268 */       return doEndOfFile();
/*     */     }
/* 270 */     this.position += numberOfChars;
/* 271 */     long returnLength = numberOfChars;
/* 272 */     if (this.position > this.size) {
/* 273 */       returnLength = numberOfChars - this.position - this.size;
/* 274 */       this.position = this.size;
/*     */     } 
/* 276 */     return returnLength;
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
/*     */   protected int processChar() {
/* 288 */     return 0;
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
/*     */   protected void processChars(char[] chars, int offset, int length) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int doEndOfFile() throws EOFException {
/* 314 */     this.eof = true;
/* 315 */     if (this.throwEofException) {
/* 316 */       throw new EOFException();
/*     */     }
/* 318 */     return -1;
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\commons\io\input\NullReader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */