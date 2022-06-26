/*     */ package org.apache.commons.io.input;
/*     */ 
/*     */ import java.io.EOFException;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class NullInputStream
/*     */   extends InputStream
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
/*     */   public NullInputStream(long size) {
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
/*     */   public NullInputStream(long size, boolean markSupported, boolean throwEofException) {
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
/*     */   public int available() {
/* 125 */     long avail = this.size - this.position;
/* 126 */     if (avail <= 0L)
/* 127 */       return 0; 
/* 128 */     if (avail > 2147483647L) {
/* 129 */       return Integer.MAX_VALUE;
/*     */     }
/* 131 */     return (int)avail;
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
/* 143 */     this.eof = false;
/* 144 */     this.position = 0L;
/* 145 */     this.mark = -1L;
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
/* 157 */     if (!this.markSupported) {
/* 158 */       throw new UnsupportedOperationException("Mark not supported");
/*     */     }
/* 160 */     this.mark = this.position;
/* 161 */     this.readlimit = readlimit;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean markSupported() {
/* 171 */     return this.markSupported;
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
/* 186 */     if (this.eof) {
/* 187 */       throw new IOException("Read after end of file");
/*     */     }
/* 189 */     if (this.position == this.size) {
/* 190 */       return doEndOfFile();
/*     */     }
/* 192 */     this.position++;
/* 193 */     return processByte();
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
/*     */   public int read(byte[] bytes) throws IOException {
/* 209 */     return read(bytes, 0, bytes.length);
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
/*     */   public int read(byte[] bytes, int offset, int length) throws IOException {
/* 227 */     if (this.eof) {
/* 228 */       throw new IOException("Read after end of file");
/*     */     }
/* 230 */     if (this.position == this.size) {
/* 231 */       return doEndOfFile();
/*     */     }
/* 233 */     this.position += length;
/* 234 */     int returnLength = length;
/* 235 */     if (this.position > this.size) {
/* 236 */       returnLength = length - (int)(this.position - this.size);
/* 237 */       this.position = this.size;
/*     */     } 
/* 239 */     processBytes(bytes, offset, returnLength);
/* 240 */     return returnLength;
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
/* 253 */     if (!this.markSupported) {
/* 254 */       throw new UnsupportedOperationException("Mark not supported");
/*     */     }
/* 256 */     if (this.mark < 0L) {
/* 257 */       throw new IOException("No position has been marked");
/*     */     }
/* 259 */     if (this.position > this.mark + this.readlimit) {
/* 260 */       throw new IOException("Marked position [" + this.mark + "] is no longer valid - passed the read limit [" + this.readlimit + "]");
/*     */     }
/*     */ 
/*     */     
/* 264 */     this.position = this.mark;
/* 265 */     this.eof = false;
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
/*     */   public long skip(long numberOfBytes) throws IOException {
/* 281 */     if (this.eof) {
/* 282 */       throw new IOException("Skip after end of file");
/*     */     }
/* 284 */     if (this.position == this.size) {
/* 285 */       return doEndOfFile();
/*     */     }
/* 287 */     this.position += numberOfBytes;
/* 288 */     long returnLength = numberOfBytes;
/* 289 */     if (this.position > this.size) {
/* 290 */       returnLength = numberOfBytes - this.position - this.size;
/* 291 */       this.position = this.size;
/*     */     } 
/* 293 */     return returnLength;
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
/*     */   protected int processByte() {
/* 305 */     return 0;
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
/*     */   protected void processBytes(byte[] bytes, int offset, int length) {}
/*     */ 
/*     */ 
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
/* 331 */     this.eof = true;
/* 332 */     if (this.throwEofException) {
/* 333 */       throw new EOFException();
/*     */     }
/* 335 */     return -1;
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\commons\io\input\NullInputStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */