/*     */ package org.apache.commons.io.output;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.Writer;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.CharBuffer;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.CharsetDecoder;
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
/*     */ public class WriterOutputStream
/*     */   extends OutputStream
/*     */ {
/*     */   private static final int DEFAULT_BUFFER_SIZE = 1024;
/*     */   private final Writer writer;
/*     */   private final CharsetDecoder decoder;
/*     */   private final boolean writeImmediately;
/*  85 */   private final ByteBuffer decoderIn = ByteBuffer.allocate(128);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final CharBuffer decoderOut;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public WriterOutputStream(Writer writer, CharsetDecoder decoder) {
/* 104 */     this(writer, decoder, 1024, false);
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
/*     */   public WriterOutputStream(Writer writer, CharsetDecoder decoder, int bufferSize, boolean writeImmediately) {
/* 121 */     this.writer = writer;
/* 122 */     this.decoder = decoder;
/* 123 */     this.writeImmediately = writeImmediately;
/* 124 */     this.decoderOut = CharBuffer.allocate(bufferSize);
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
/*     */   public WriterOutputStream(Writer writer, Charset charset, int bufferSize, boolean writeImmediately) {
/* 140 */     this(writer, charset.newDecoder().onMalformedInput(CodingErrorAction.REPLACE).onUnmappableCharacter(CodingErrorAction.REPLACE).replaceWith("?"), bufferSize, writeImmediately);
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
/*     */   public WriterOutputStream(Writer writer, Charset charset) {
/* 158 */     this(writer, charset, 1024, false);
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
/*     */   public WriterOutputStream(Writer writer, String charsetName, int bufferSize, boolean writeImmediately) {
/* 174 */     this(writer, Charset.forName(charsetName), bufferSize, writeImmediately);
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
/*     */   public WriterOutputStream(Writer writer, String charsetName) {
/* 186 */     this(writer, charsetName, 1024, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public WriterOutputStream(Writer writer) {
/* 197 */     this(writer, Charset.defaultCharset(), 1024, false);
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
/*     */   public void write(byte[] b, int off, int len) throws IOException {
/* 210 */     while (len > 0) {
/* 211 */       int c = Math.min(len, this.decoderIn.remaining());
/* 212 */       this.decoderIn.put(b, off, c);
/* 213 */       processInput(false);
/* 214 */       len -= c;
/* 215 */       off += c;
/*     */     } 
/* 217 */     if (this.writeImmediately) {
/* 218 */       flushOutput();
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
/*     */   public void write(byte[] b) throws IOException {
/* 230 */     write(b, 0, b.length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(int b) throws IOException {
/* 241 */     write(new byte[] { (byte)b }, 0, 1);
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
/* 252 */     flushOutput();
/* 253 */     this.writer.flush();
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
/* 264 */     processInput(true);
/* 265 */     flushOutput();
/* 266 */     this.writer.close();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void processInput(boolean endOfInput) throws IOException {
/*     */     CoderResult coderResult;
/* 277 */     this.decoderIn.flip();
/*     */     
/*     */     while (true) {
/* 280 */       coderResult = this.decoder.decode(this.decoderIn, this.decoderOut, endOfInput);
/* 281 */       if (coderResult.isOverflow())
/* 282 */       { flushOutput(); continue; }  break;
/* 283 */     }  if (coderResult.isUnderflow()) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 292 */       this.decoderIn.compact();
/*     */       return;
/*     */     } 
/*     */     throw new IOException("Unexpected coder result");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void flushOutput() throws IOException {
/* 301 */     if (this.decoderOut.position() > 0) {
/* 302 */       this.writer.write(this.decoderOut.array(), 0, this.decoderOut.position());
/* 303 */       this.decoderOut.rewind();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\commons\io\output\WriterOutputStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */