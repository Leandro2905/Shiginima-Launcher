/*     */ package org.apache.commons.io.input;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.Arrays;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import org.apache.commons.io.ByteOrderMark;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BOMInputStream
/*     */   extends ProxyInputStream
/*     */ {
/*     */   private final boolean include;
/*     */   private final List<ByteOrderMark> boms;
/*     */   private ByteOrderMark byteOrderMark;
/*     */   private int[] firstBytes;
/*     */   private int fbLength;
/*     */   private int fbIndex;
/*     */   private int markFbIndex;
/*     */   private boolean markedAtStart;
/*     */   
/*     */   public BOMInputStream(InputStream delegate) {
/* 107 */     this(delegate, false, new ByteOrderMark[] { ByteOrderMark.UTF_8 });
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
/*     */   public BOMInputStream(InputStream delegate, boolean include) {
/* 119 */     this(delegate, include, new ByteOrderMark[] { ByteOrderMark.UTF_8 });
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
/*     */   public BOMInputStream(InputStream delegate, ByteOrderMark... boms) {
/* 131 */     this(delegate, false, boms);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 137 */   private static final Comparator<ByteOrderMark> ByteOrderMarkLengthComparator = new Comparator<ByteOrderMark>()
/*     */     {
/*     */       public int compare(ByteOrderMark bom1, ByteOrderMark bom2) {
/* 140 */         int len1 = bom1.length();
/* 141 */         int len2 = bom2.length();
/* 142 */         if (len1 > len2) {
/* 143 */           return -1;
/*     */         }
/* 145 */         if (len2 > len1) {
/* 146 */           return 1;
/*     */         }
/* 148 */         return 0;
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BOMInputStream(InputStream delegate, boolean include, ByteOrderMark... boms) {
/* 163 */     super(delegate);
/* 164 */     if (boms == null || boms.length == 0) {
/* 165 */       throw new IllegalArgumentException("No BOMs specified");
/*     */     }
/* 167 */     this.include = include;
/*     */     
/* 169 */     Arrays.sort(boms, ByteOrderMarkLengthComparator);
/* 170 */     this.boms = Arrays.asList(boms);
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
/*     */   public boolean hasBOM() throws IOException {
/* 182 */     return (getBOM() != null);
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
/*     */   public boolean hasBOM(ByteOrderMark bom) throws IOException {
/* 197 */     if (!this.boms.contains(bom)) {
/* 198 */       throw new IllegalArgumentException("Stream not configure to detect " + bom);
/*     */     }
/* 200 */     return (this.byteOrderMark != null && getBOM().equals(bom));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ByteOrderMark getBOM() throws IOException {
/* 211 */     if (this.firstBytes == null) {
/* 212 */       this.fbLength = 0;
/*     */       
/* 214 */       int maxBomSize = ((ByteOrderMark)this.boms.get(0)).length();
/* 215 */       this.firstBytes = new int[maxBomSize];
/*     */       
/* 217 */       for (int i = 0; i < this.firstBytes.length; i++) {
/* 218 */         this.firstBytes[i] = this.in.read();
/* 219 */         this.fbLength++;
/* 220 */         if (this.firstBytes[i] < 0) {
/*     */           break;
/*     */         }
/*     */       } 
/*     */       
/* 225 */       this.byteOrderMark = find();
/* 226 */       if (this.byteOrderMark != null && 
/* 227 */         !this.include) {
/* 228 */         if (this.byteOrderMark.length() < this.firstBytes.length) {
/* 229 */           this.fbIndex = this.byteOrderMark.length();
/*     */         } else {
/* 231 */           this.fbLength = 0;
/*     */         } 
/*     */       }
/*     */     } 
/*     */     
/* 236 */     return this.byteOrderMark;
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
/*     */   public String getBOMCharsetName() throws IOException {
/* 248 */     getBOM();
/* 249 */     return (this.byteOrderMark == null) ? null : this.byteOrderMark.getCharsetName();
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
/*     */   private int readFirstBytes() throws IOException {
/* 262 */     getBOM();
/* 263 */     return (this.fbIndex < this.fbLength) ? this.firstBytes[this.fbIndex++] : -1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ByteOrderMark find() {
/* 272 */     for (ByteOrderMark bom : this.boms) {
/* 273 */       if (matches(bom)) {
/* 274 */         return bom;
/*     */       }
/*     */     } 
/* 277 */     return null;
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
/*     */   private boolean matches(ByteOrderMark bom) {
/* 292 */     for (int i = 0; i < bom.length(); i++) {
/* 293 */       if (bom.get(i) != this.firstBytes[i]) {
/* 294 */         return false;
/*     */       }
/*     */     } 
/* 297 */     return true;
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
/*     */   public int read() throws IOException {
/* 313 */     int b = readFirstBytes();
/* 314 */     return (b >= 0) ? b : this.in.read();
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
/*     */   public int read(byte[] buf, int off, int len) throws IOException {
/* 332 */     int firstCount = 0;
/* 333 */     int b = 0;
/* 334 */     while (len > 0 && b >= 0) {
/* 335 */       b = readFirstBytes();
/* 336 */       if (b >= 0) {
/* 337 */         buf[off++] = (byte)(b & 0xFF);
/* 338 */         len--;
/* 339 */         firstCount++;
/*     */       } 
/*     */     } 
/* 342 */     int secondCount = this.in.read(buf, off, len);
/* 343 */     return (secondCount < 0) ? ((firstCount > 0) ? firstCount : -1) : (firstCount + secondCount);
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
/*     */   public int read(byte[] buf) throws IOException {
/* 357 */     return read(buf, 0, buf.length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void mark(int readlimit) {
/* 368 */     this.markFbIndex = this.fbIndex;
/* 369 */     this.markedAtStart = (this.firstBytes == null);
/* 370 */     this.in.mark(readlimit);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void reset() throws IOException {
/* 381 */     this.fbIndex = this.markFbIndex;
/* 382 */     if (this.markedAtStart) {
/* 383 */       this.firstBytes = null;
/*     */     }
/*     */     
/* 386 */     this.in.reset();
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
/*     */   public long skip(long n) throws IOException {
/* 400 */     while (n > 0L && readFirstBytes() >= 0) {
/* 401 */       n--;
/*     */     }
/* 403 */     return this.in.skip(n);
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\commons\io\input\BOMInputStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */