/*     */ package org.apache.commons.io.filefilter;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.RandomAccessFile;
/*     */ import java.io.Serializable;
/*     */ import java.util.Arrays;
/*     */ import org.apache.commons.io.IOUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MagicNumberFileFilter
/*     */   extends AbstractFileFilter
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -547733176983104172L;
/*     */   private final byte[] magicNumbers;
/*     */   private final long byteOffset;
/*     */   
/*     */   public MagicNumberFileFilter(byte[] magicNumber) {
/* 112 */     this(magicNumber, 0L);
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
/*     */   public MagicNumberFileFilter(String magicNumber) {
/* 137 */     this(magicNumber, 0L);
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
/*     */   public MagicNumberFileFilter(String magicNumber, long offset) {
/* 161 */     if (magicNumber == null) {
/* 162 */       throw new IllegalArgumentException("The magic number cannot be null");
/*     */     }
/* 164 */     if (magicNumber.length() == 0) {
/* 165 */       throw new IllegalArgumentException("The magic number must contain at least one byte");
/*     */     }
/* 167 */     if (offset < 0L) {
/* 168 */       throw new IllegalArgumentException("The offset cannot be negative");
/*     */     }
/*     */     
/* 171 */     this.magicNumbers = magicNumber.getBytes();
/* 172 */     this.byteOffset = offset;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MagicNumberFileFilter(byte[] magicNumber, long offset) {
/* 206 */     if (magicNumber == null) {
/* 207 */       throw new IllegalArgumentException("The magic number cannot be null");
/*     */     }
/* 209 */     if (magicNumber.length == 0) {
/* 210 */       throw new IllegalArgumentException("The magic number must contain at least one byte");
/*     */     }
/* 212 */     if (offset < 0L) {
/* 213 */       throw new IllegalArgumentException("The offset cannot be negative");
/*     */     }
/*     */     
/* 216 */     this.magicNumbers = new byte[magicNumber.length];
/* 217 */     System.arraycopy(magicNumber, 0, this.magicNumbers, 0, magicNumber.length);
/* 218 */     this.byteOffset = offset;
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
/*     */   public boolean accept(File file) {
/* 239 */     if (file != null && file.isFile() && file.canRead()) {
/* 240 */       RandomAccessFile randomAccessFile = null;
/*     */       try {
/* 242 */         byte[] fileBytes = new byte[this.magicNumbers.length];
/* 243 */         randomAccessFile = new RandomAccessFile(file, "r");
/* 244 */         randomAccessFile.seek(this.byteOffset);
/* 245 */         int read = randomAccessFile.read(fileBytes);
/* 246 */         if (read != this.magicNumbers.length) {
/* 247 */           return false;
/*     */         }
/* 249 */         return Arrays.equals(this.magicNumbers, fileBytes);
/* 250 */       } catch (IOException ioe) {
/*     */       
/*     */       } finally {
/* 253 */         IOUtils.closeQuietly(randomAccessFile);
/*     */       } 
/*     */     } 
/*     */     
/* 257 */     return false;
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
/* 268 */     StringBuilder builder = new StringBuilder(super.toString());
/* 269 */     builder.append("(");
/* 270 */     builder.append(new String(this.magicNumbers));
/* 271 */     builder.append(",");
/* 272 */     builder.append(this.byteOffset);
/* 273 */     builder.append(")");
/* 274 */     return builder.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\commons\io\filefilter\MagicNumberFileFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */