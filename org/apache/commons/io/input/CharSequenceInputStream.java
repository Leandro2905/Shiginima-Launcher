/*     */ package org.apache.commons.io.input;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.CharBuffer;
/*     */ import java.nio.charset.CharacterCodingException;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.CharsetEncoder;
/*     */ import java.nio.charset.CoderResult;
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
/*     */ public class CharSequenceInputStream
/*     */   extends InputStream
/*     */ {
/*     */   private final CharsetEncoder encoder;
/*     */   private final CharBuffer cbuf;
/*     */   private final ByteBuffer bbuf;
/*     */   private int mark;
/*     */   
/*     */   public CharSequenceInputStream(CharSequence s, Charset charset, int bufferSize) {
/*  55 */     this.encoder = charset.newEncoder().onMalformedInput(CodingErrorAction.REPLACE).onUnmappableCharacter(CodingErrorAction.REPLACE);
/*     */ 
/*     */     
/*  58 */     this.bbuf = ByteBuffer.allocate(bufferSize);
/*  59 */     this.bbuf.flip();
/*  60 */     this.cbuf = CharBuffer.wrap(s);
/*  61 */     this.mark = -1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CharSequenceInputStream(CharSequence s, String charset, int bufferSize) {
/*  72 */     this(s, Charset.forName(charset), bufferSize);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CharSequenceInputStream(CharSequence s, Charset charset) {
/*  83 */     this(s, charset, 2048);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CharSequenceInputStream(CharSequence s, String charset) {
/*  94 */     this(s, charset, 2048);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void fillBuffer() throws CharacterCodingException {
/* 104 */     this.bbuf.compact();
/* 105 */     CoderResult result = this.encoder.encode(this.cbuf, this.bbuf, true);
/* 106 */     if (result.isError()) {
/* 107 */       result.throwException();
/*     */     }
/* 109 */     this.bbuf.flip();
/*     */   }
/*     */ 
/*     */   
/*     */   public int read(byte[] b, int off, int len) throws IOException {
/* 114 */     if (b == null) {
/* 115 */       throw new NullPointerException("Byte array is null");
/*     */     }
/* 117 */     if (len < 0 || off + len > b.length) {
/* 118 */       throw new IndexOutOfBoundsException("Array Size=" + b.length + ", offset=" + off + ", length=" + len);
/*     */     }
/*     */     
/* 121 */     if (len == 0) {
/* 122 */       return 0;
/*     */     }
/* 124 */     if (!this.bbuf.hasRemaining() && !this.cbuf.hasRemaining()) {
/* 125 */       return -1;
/*     */     }
/* 127 */     int bytesRead = 0;
/* 128 */     while (len > 0) {
/* 129 */       if (this.bbuf.hasRemaining()) {
/* 130 */         int chunk = Math.min(this.bbuf.remaining(), len);
/* 131 */         this.bbuf.get(b, off, chunk);
/* 132 */         off += chunk;
/* 133 */         len -= chunk;
/* 134 */         bytesRead += chunk; continue;
/*     */       } 
/* 136 */       fillBuffer();
/* 137 */       if (!this.bbuf.hasRemaining() && !this.cbuf.hasRemaining()) {
/*     */         break;
/*     */       }
/*     */     } 
/*     */     
/* 142 */     return (bytesRead == 0 && !this.cbuf.hasRemaining()) ? -1 : bytesRead;
/*     */   }
/*     */ 
/*     */   
/*     */   public int read() throws IOException {
/*     */     while (true) {
/* 148 */       if (this.bbuf.hasRemaining()) {
/* 149 */         return this.bbuf.get() & 0xFF;
/*     */       }
/* 151 */       fillBuffer();
/* 152 */       if (!this.bbuf.hasRemaining() && !this.cbuf.hasRemaining()) {
/* 153 */         return -1;
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int read(byte[] b) throws IOException {
/* 161 */     return read(b, 0, b.length);
/*     */   }
/*     */ 
/*     */   
/*     */   public long skip(long n) throws IOException {
/* 166 */     int skipped = 0;
/* 167 */     while (n > 0L && this.cbuf.hasRemaining()) {
/* 168 */       this.cbuf.get();
/* 169 */       n--;
/* 170 */       skipped++;
/*     */     } 
/* 172 */     return skipped;
/*     */   }
/*     */ 
/*     */   
/*     */   public int available() throws IOException {
/* 177 */     return this.cbuf.remaining();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() throws IOException {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void mark(int readlimit) {
/* 190 */     this.mark = this.cbuf.position();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void reset() throws IOException {
/* 195 */     if (this.mark != -1) {
/* 196 */       this.cbuf.position(this.mark);
/* 197 */       this.mark = -1;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean markSupported() {
/* 203 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\commons\io\input\CharSequenceInputStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */