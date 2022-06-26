/*     */ package org.apache.commons.io.input;
/*     */ 
/*     */ import java.io.Closeable;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.RandomAccessFile;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.CharsetEncoder;
/*     */ import org.apache.commons.io.Charsets;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ReversedLinesFileReader
/*     */   implements Closeable
/*     */ {
/*     */   private final int blockSize;
/*     */   private final Charset encoding;
/*     */   private final RandomAccessFile randomAccessFile;
/*     */   private final long totalByteLength;
/*     */   private final long totalBlockCount;
/*     */   private final byte[][] newLineSequences;
/*     */   private final int avoidNewlineSplitBufferSize;
/*     */   private final int byteDecrement;
/*     */   private FilePart currentFilePart;
/*     */   private boolean trailingNewlineOfFileSkipped = false;
/*     */   
/*     */   public ReversedLinesFileReader(File file) throws IOException {
/*  63 */     this(file, 4096, Charset.defaultCharset().toString());
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
/*     */   public ReversedLinesFileReader(File file, int blockSize, Charset encoding) throws IOException {
/*  80 */     this.blockSize = blockSize;
/*  81 */     this.encoding = encoding;
/*     */     
/*  83 */     this.randomAccessFile = new RandomAccessFile(file, "r");
/*  84 */     this.totalByteLength = this.randomAccessFile.length();
/*  85 */     int lastBlockLength = (int)(this.totalByteLength % blockSize);
/*  86 */     if (lastBlockLength > 0) {
/*  87 */       this.totalBlockCount = this.totalByteLength / blockSize + 1L;
/*     */     } else {
/*  89 */       this.totalBlockCount = this.totalByteLength / blockSize;
/*  90 */       if (this.totalByteLength > 0L) {
/*  91 */         lastBlockLength = blockSize;
/*     */       }
/*     */     } 
/*  94 */     this.currentFilePart = new FilePart(this.totalBlockCount, lastBlockLength, null);
/*     */ 
/*     */     
/*  97 */     Charset charset = Charsets.toCharset(encoding);
/*  98 */     CharsetEncoder charsetEncoder = charset.newEncoder();
/*  99 */     float maxBytesPerChar = charsetEncoder.maxBytesPerChar();
/* 100 */     if (maxBytesPerChar == 1.0F)
/*     */     
/* 102 */     { this.byteDecrement = 1; }
/* 103 */     else if (charset == Charset.forName("UTF-8"))
/*     */     
/*     */     { 
/* 106 */       this.byteDecrement = 1; }
/* 107 */     else if (charset == Charset.forName("Shift_JIS"))
/*     */     
/*     */     { 
/* 110 */       this.byteDecrement = 1; }
/* 111 */     else if (charset == Charset.forName("UTF-16BE") || charset == Charset.forName("UTF-16LE"))
/*     */     
/*     */     { 
/* 114 */       this.byteDecrement = 2; }
/* 115 */     else { if (charset == Charset.forName("UTF-16")) {
/* 116 */         throw new UnsupportedEncodingException("For UTF-16, you need to specify the byte order (use UTF-16BE or UTF-16LE)");
/*     */       }
/*     */       
/* 119 */       throw new UnsupportedEncodingException("Encoding " + encoding + " is not supported yet (feel free to submit a patch)"); }
/*     */ 
/*     */ 
/*     */     
/* 123 */     this.newLineSequences = new byte[][] { "\r\n".getBytes(encoding), "\n".getBytes(encoding), "\r".getBytes(encoding) };
/*     */     
/* 125 */     this.avoidNewlineSplitBufferSize = (this.newLineSequences[0]).length;
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
/*     */   public ReversedLinesFileReader(File file, int blockSize, String encoding) throws IOException {
/* 144 */     this(file, blockSize, Charsets.toCharset(encoding));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String readLine() throws IOException {
/* 155 */     String line = this.currentFilePart.readLine();
/* 156 */     while (line == null) {
/* 157 */       this.currentFilePart = this.currentFilePart.rollOver();
/* 158 */       if (this.currentFilePart != null) {
/* 159 */         line = this.currentFilePart.readLine();
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 167 */     if ("".equals(line) && !this.trailingNewlineOfFileSkipped) {
/* 168 */       this.trailingNewlineOfFileSkipped = true;
/* 169 */       line = readLine();
/*     */     } 
/*     */     
/* 172 */     return line;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 181 */     this.randomAccessFile.close();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private class FilePart
/*     */   {
/*     */     private final long no;
/*     */ 
/*     */     
/*     */     private final byte[] data;
/*     */ 
/*     */     
/*     */     private byte[] leftOver;
/*     */ 
/*     */     
/*     */     private int currentLastBytePos;
/*     */ 
/*     */     
/*     */     private FilePart(long no, int length, byte[] leftOverOfLastFilePart) throws IOException {
/* 201 */       this.no = no;
/* 202 */       int dataLength = length + ((leftOverOfLastFilePart != null) ? leftOverOfLastFilePart.length : 0);
/* 203 */       this.data = new byte[dataLength];
/* 204 */       long off = (no - 1L) * ReversedLinesFileReader.this.blockSize;
/*     */ 
/*     */       
/* 207 */       if (no > 0L) {
/* 208 */         ReversedLinesFileReader.this.randomAccessFile.seek(off);
/* 209 */         int countRead = ReversedLinesFileReader.this.randomAccessFile.read(this.data, 0, length);
/* 210 */         if (countRead != length) {
/* 211 */           throw new IllegalStateException("Count of requested bytes and actually read bytes don't match");
/*     */         }
/*     */       } 
/*     */       
/* 215 */       if (leftOverOfLastFilePart != null) {
/* 216 */         System.arraycopy(leftOverOfLastFilePart, 0, this.data, length, leftOverOfLastFilePart.length);
/*     */       }
/* 218 */       this.currentLastBytePos = this.data.length - 1;
/* 219 */       this.leftOver = null;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private FilePart rollOver() throws IOException {
/* 230 */       if (this.currentLastBytePos > -1) {
/* 231 */         throw new IllegalStateException("Current currentLastCharPos unexpectedly positive... last readLine() should have returned something! currentLastCharPos=" + this.currentLastBytePos);
/*     */       }
/*     */ 
/*     */       
/* 235 */       if (this.no > 1L) {
/* 236 */         return new FilePart(this.no - 1L, ReversedLinesFileReader.this.blockSize, this.leftOver);
/*     */       }
/*     */       
/* 239 */       if (this.leftOver != null) {
/* 240 */         throw new IllegalStateException("Unexpected leftover of the last block: leftOverOfThisFilePart=" + new String(this.leftOver, ReversedLinesFileReader.this.encoding));
/*     */       }
/*     */       
/* 243 */       return null;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private String readLine() throws IOException {
/* 255 */       String line = null;
/*     */ 
/*     */       
/* 258 */       boolean isLastFilePart = (this.no == 1L);
/*     */       
/* 260 */       int i = this.currentLastBytePos;
/* 261 */       while (i > -1) {
/*     */         
/* 263 */         if (!isLastFilePart && i < ReversedLinesFileReader.this.avoidNewlineSplitBufferSize) {
/*     */ 
/*     */           
/* 266 */           createLeftOver();
/*     */           
/*     */           break;
/*     */         } 
/*     */         int newLineMatchByteCount;
/* 271 */         if ((newLineMatchByteCount = getNewLineMatchByteCount(this.data, i)) > 0) {
/* 272 */           int lineStart = i + 1;
/* 273 */           int lineLengthBytes = this.currentLastBytePos - lineStart + 1;
/*     */           
/* 275 */           if (lineLengthBytes < 0) {
/* 276 */             throw new IllegalStateException("Unexpected negative line length=" + lineLengthBytes);
/*     */           }
/* 278 */           byte[] lineData = new byte[lineLengthBytes];
/* 279 */           System.arraycopy(this.data, lineStart, lineData, 0, lineLengthBytes);
/*     */           
/* 281 */           line = new String(lineData, ReversedLinesFileReader.this.encoding);
/*     */           
/* 283 */           this.currentLastBytePos = i - newLineMatchByteCount;
/*     */           
/*     */           break;
/*     */         } 
/*     */         
/* 288 */         i -= ReversedLinesFileReader.this.byteDecrement;
/*     */ 
/*     */         
/* 291 */         if (i < 0) {
/* 292 */           createLeftOver();
/*     */           
/*     */           break;
/*     */         } 
/*     */       } 
/*     */       
/* 298 */       if (isLastFilePart && this.leftOver != null) {
/*     */         
/* 300 */         line = new String(this.leftOver, ReversedLinesFileReader.this.encoding);
/* 301 */         this.leftOver = null;
/*     */       } 
/*     */       
/* 304 */       return line;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private void createLeftOver() {
/* 311 */       int lineLengthBytes = this.currentLastBytePos + 1;
/* 312 */       if (lineLengthBytes > 0) {
/*     */         
/* 314 */         this.leftOver = new byte[lineLengthBytes];
/* 315 */         System.arraycopy(this.data, 0, this.leftOver, 0, lineLengthBytes);
/*     */       } else {
/* 317 */         this.leftOver = null;
/*     */       } 
/* 319 */       this.currentLastBytePos = -1;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private int getNewLineMatchByteCount(byte[] data, int i) {
/* 330 */       for (byte[] newLineSequence : ReversedLinesFileReader.this.newLineSequences) {
/* 331 */         int k; boolean match = true;
/* 332 */         for (int j = newLineSequence.length - 1; j >= 0; j--) {
/* 333 */           int m = i + j - newLineSequence.length - 1;
/* 334 */           k = match & ((m >= 0 && data[m] == newLineSequence[j]) ? 1 : 0);
/*     */         } 
/* 336 */         if (k != 0) {
/* 337 */           return newLineSequence.length;
/*     */         }
/*     */       } 
/* 340 */       return 0;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\commons\io\input\ReversedLinesFileReader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */