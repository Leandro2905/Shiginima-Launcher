/*     */ package org.apache.commons.io.input;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.Reader;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.CharBuffer;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ReaderInputStream
/*     */   extends InputStream
/*     */ {
/*     */   private static final int DEFAULT_BUFFER_SIZE = 1024;
/*     */   private final Reader reader;
/*     */   private final CharsetEncoder encoder;
/*     */   private final CharBuffer encoderIn;
/*     */   private final ByteBuffer encoderOut;
/*     */   private CoderResult lastCoderResult;
/*     */   private boolean endOfInput;
/*     */   
/*     */   public ReaderInputStream(Reader reader, CharsetEncoder encoder) {
/* 107 */     this(reader, encoder, 1024);
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
/*     */   public ReaderInputStream(Reader reader, CharsetEncoder encoder, int bufferSize) {
/* 119 */     this.reader = reader;
/* 120 */     this.encoder = encoder;
/* 121 */     this.encoderIn = CharBuffer.allocate(bufferSize);
/* 122 */     this.encoderIn.flip();
/* 123 */     this.encoderOut = ByteBuffer.allocate(128);
/* 124 */     this.encoderOut.flip();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ReaderInputStream(Reader reader, Charset charset, int bufferSize) {
/* 135 */     this(reader, charset.newEncoder().onMalformedInput(CodingErrorAction.REPLACE).onUnmappableCharacter(CodingErrorAction.REPLACE), bufferSize);
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
/*     */   public ReaderInputStream(Reader reader, Charset charset) {
/* 150 */     this(reader, charset, 1024);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ReaderInputStream(Reader reader, String charsetName, int bufferSize) {
/* 161 */     this(reader, Charset.forName(charsetName), bufferSize);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ReaderInputStream(Reader reader, String charsetName) {
/* 172 */     this(reader, charsetName, 1024);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ReaderInputStream(Reader reader) {
/* 182 */     this(reader, Charset.defaultCharset());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void fillBuffer() throws IOException {
/* 192 */     if (!this.endOfInput && (this.lastCoderResult == null || this.lastCoderResult.isUnderflow())) {
/* 193 */       this.encoderIn.compact();
/* 194 */       int position = this.encoderIn.position();
/*     */ 
/*     */ 
/*     */       
/* 198 */       int c = this.reader.read(this.encoderIn.array(), position, this.encoderIn.remaining());
/* 199 */       if (c == -1) {
/* 200 */         this.endOfInput = true;
/*     */       } else {
/* 202 */         this.encoderIn.position(position + c);
/*     */       } 
/* 204 */       this.encoderIn.flip();
/*     */     } 
/* 206 */     this.encoderOut.compact();
/* 207 */     this.lastCoderResult = this.encoder.encode(this.encoderIn, this.encoderOut, this.endOfInput);
/* 208 */     this.encoderOut.flip();
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
/*     */   public int read(byte[] b, int off, int len) throws IOException {
/* 223 */     if (b == null) {
/* 224 */       throw new NullPointerException("Byte array must not be null");
/*     */     }
/* 226 */     if (len < 0 || off < 0 || off + len > b.length) {
/* 227 */       throw new IndexOutOfBoundsException("Array Size=" + b.length + ", offset=" + off + ", length=" + len);
/*     */     }
/*     */     
/* 230 */     int read = 0;
/* 231 */     if (len == 0) {
/* 232 */       return 0;
/*     */     }
/* 234 */     while (len > 0) {
/* 235 */       if (this.encoderOut.hasRemaining()) {
/* 236 */         int c = Math.min(this.encoderOut.remaining(), len);
/* 237 */         this.encoderOut.get(b, off, c);
/* 238 */         off += c;
/* 239 */         len -= c;
/* 240 */         read += c; continue;
/*     */       } 
/* 242 */       fillBuffer();
/* 243 */       if (this.endOfInput && !this.encoderOut.hasRemaining()) {
/*     */         break;
/*     */       }
/*     */     } 
/*     */     
/* 248 */     return (read == 0 && this.endOfInput) ? -1 : read;
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
/*     */   public int read(byte[] b) throws IOException {
/* 261 */     return read(b, 0, b.length);
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
/*     */     while (true) {
/* 274 */       if (this.encoderOut.hasRemaining()) {
/* 275 */         return this.encoderOut.get() & 0xFF;
/*     */       }
/* 277 */       fillBuffer();
/* 278 */       if (this.endOfInput && !this.encoderOut.hasRemaining()) {
/* 279 */         return -1;
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
/*     */   public void close() throws IOException {
/* 292 */     this.reader.close();
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\commons\io\input\ReaderInputStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */