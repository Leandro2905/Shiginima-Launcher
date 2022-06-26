/*     */ package org.apache.commons.io.output;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.SequenceInputStream;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import org.apache.commons.io.input.ClosedInputStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ByteArrayOutputStream
/*     */   extends OutputStream
/*     */ {
/*  57 */   private static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
/*     */ 
/*     */   
/*  60 */   private final List<byte[]> buffers = (List)new ArrayList<byte>();
/*     */ 
/*     */   
/*     */   private int currentBufferIndex;
/*     */ 
/*     */   
/*     */   private int filledBufferSum;
/*     */ 
/*     */   
/*     */   private byte[] currentBuffer;
/*     */   
/*     */   private int count;
/*     */ 
/*     */   
/*     */   public ByteArrayOutputStream() {
/*  75 */     this(1024);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ByteArrayOutputStream(int size) {
/*  86 */     if (size < 0) {
/*  87 */       throw new IllegalArgumentException("Negative initial size: " + size);
/*     */     }
/*     */     
/*  90 */     synchronized (this) {
/*  91 */       needNewBuffer(size);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void needNewBuffer(int newcount) {
/* 102 */     if (this.currentBufferIndex < this.buffers.size() - 1) {
/*     */       
/* 104 */       this.filledBufferSum += this.currentBuffer.length;
/*     */       
/* 106 */       this.currentBufferIndex++;
/* 107 */       this.currentBuffer = this.buffers.get(this.currentBufferIndex);
/*     */     } else {
/*     */       int newBufferSize;
/*     */       
/* 111 */       if (this.currentBuffer == null) {
/* 112 */         newBufferSize = newcount;
/* 113 */         this.filledBufferSum = 0;
/*     */       } else {
/* 115 */         newBufferSize = Math.max(this.currentBuffer.length << 1, newcount - this.filledBufferSum);
/*     */ 
/*     */         
/* 118 */         this.filledBufferSum += this.currentBuffer.length;
/*     */       } 
/*     */       
/* 121 */       this.currentBufferIndex++;
/* 122 */       this.currentBuffer = new byte[newBufferSize];
/* 123 */       this.buffers.add(this.currentBuffer);
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
/*     */   public void write(byte[] b, int off, int len) {
/* 135 */     if (off < 0 || off > b.length || len < 0 || off + len > b.length || off + len < 0)
/*     */     {
/*     */ 
/*     */ 
/*     */       
/* 140 */       throw new IndexOutOfBoundsException(); } 
/* 141 */     if (len == 0) {
/*     */       return;
/*     */     }
/* 144 */     synchronized (this) {
/* 145 */       int newcount = this.count + len;
/* 146 */       int remaining = len;
/* 147 */       int inBufferPos = this.count - this.filledBufferSum;
/* 148 */       while (remaining > 0) {
/* 149 */         int part = Math.min(remaining, this.currentBuffer.length - inBufferPos);
/* 150 */         System.arraycopy(b, off + len - remaining, this.currentBuffer, inBufferPos, part);
/* 151 */         remaining -= part;
/* 152 */         if (remaining > 0) {
/* 153 */           needNewBuffer(newcount);
/* 154 */           inBufferPos = 0;
/*     */         } 
/*     */       } 
/* 157 */       this.count = newcount;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void write(int b) {
/* 167 */     int inBufferPos = this.count - this.filledBufferSum;
/* 168 */     if (inBufferPos == this.currentBuffer.length) {
/* 169 */       needNewBuffer(this.count + 1);
/* 170 */       inBufferPos = 0;
/*     */     } 
/* 172 */     this.currentBuffer[inBufferPos] = (byte)b;
/* 173 */     this.count++;
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
/*     */   public synchronized int write(InputStream in) throws IOException {
/* 188 */     int readCount = 0;
/* 189 */     int inBufferPos = this.count - this.filledBufferSum;
/* 190 */     int n = in.read(this.currentBuffer, inBufferPos, this.currentBuffer.length - inBufferPos);
/* 191 */     while (n != -1) {
/* 192 */       readCount += n;
/* 193 */       inBufferPos += n;
/* 194 */       this.count += n;
/* 195 */       if (inBufferPos == this.currentBuffer.length) {
/* 196 */         needNewBuffer(this.currentBuffer.length);
/* 197 */         inBufferPos = 0;
/*     */       } 
/* 199 */       n = in.read(this.currentBuffer, inBufferPos, this.currentBuffer.length - inBufferPos);
/*     */     } 
/* 201 */     return readCount;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized int size() {
/* 209 */     return this.count;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void reset() {
/* 229 */     this.count = 0;
/* 230 */     this.filledBufferSum = 0;
/* 231 */     this.currentBufferIndex = 0;
/* 232 */     this.currentBuffer = this.buffers.get(this.currentBufferIndex);
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
/*     */   public synchronized void writeTo(OutputStream out) throws IOException {
/* 244 */     int remaining = this.count;
/* 245 */     for (byte[] buf : this.buffers) {
/* 246 */       int c = Math.min(buf.length, remaining);
/* 247 */       out.write(buf, 0, c);
/* 248 */       remaining -= c;
/* 249 */       if (remaining == 0) {
/*     */         break;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static InputStream toBufferedInputStream(InputStream input) throws IOException {
/* 278 */     ByteArrayOutputStream output = new ByteArrayOutputStream();
/* 279 */     output.write(input);
/* 280 */     return output.toBufferedInputStream();
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
/*     */   private InputStream toBufferedInputStream() {
/* 294 */     int remaining = this.count;
/* 295 */     if (remaining == 0) {
/* 296 */       return (InputStream)new ClosedInputStream();
/*     */     }
/* 298 */     List<ByteArrayInputStream> list = new ArrayList<ByteArrayInputStream>(this.buffers.size());
/* 299 */     for (byte[] buf : this.buffers) {
/* 300 */       int c = Math.min(buf.length, remaining);
/* 301 */       list.add(new ByteArrayInputStream(buf, 0, c));
/* 302 */       remaining -= c;
/* 303 */       if (remaining == 0) {
/*     */         break;
/*     */       }
/*     */     } 
/* 307 */     return new SequenceInputStream(Collections.enumeration((Collection)list));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized byte[] toByteArray() {
/* 318 */     int remaining = this.count;
/* 319 */     if (remaining == 0) {
/* 320 */       return EMPTY_BYTE_ARRAY;
/*     */     }
/* 322 */     byte[] newbuf = new byte[remaining];
/* 323 */     int pos = 0;
/* 324 */     for (byte[] buf : this.buffers) {
/* 325 */       int c = Math.min(buf.length, remaining);
/* 326 */       System.arraycopy(buf, 0, newbuf, pos, c);
/* 327 */       pos += c;
/* 328 */       remaining -= c;
/* 329 */       if (remaining == 0) {
/*     */         break;
/*     */       }
/*     */     } 
/* 333 */     return newbuf;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 343 */     return new String(toByteArray());
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
/*     */   public String toString(String enc) throws UnsupportedEncodingException {
/* 356 */     return new String(toByteArray(), enc);
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\commons\io\output\ByteArrayOutputStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */