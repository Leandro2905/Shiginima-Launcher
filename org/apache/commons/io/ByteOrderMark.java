/*     */ package org.apache.commons.io;
/*     */ 
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
/*     */ public class ByteOrderMark
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*  36 */   public static final ByteOrderMark UTF_8 = new ByteOrderMark("UTF-8", new int[] { 239, 187, 191 });
/*     */ 
/*     */   
/*  39 */   public static final ByteOrderMark UTF_16BE = new ByteOrderMark("UTF-16BE", new int[] { 254, 255 });
/*     */ 
/*     */   
/*  42 */   public static final ByteOrderMark UTF_16LE = new ByteOrderMark("UTF-16LE", new int[] { 255, 254 });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  48 */   public static final ByteOrderMark UTF_32BE = new ByteOrderMark("UTF-32BE", new int[] { 0, 0, 254, 255 });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  54 */   public static final ByteOrderMark UTF_32LE = new ByteOrderMark("UTF-32LE", new int[] { 255, 254, 0, 0 });
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final String charsetName;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final int[] bytes;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ByteOrderMark(String charsetName, int... bytes) {
/*  70 */     if (charsetName == null || charsetName.length() == 0) {
/*  71 */       throw new IllegalArgumentException("No charsetName specified");
/*     */     }
/*  73 */     if (bytes == null || bytes.length == 0) {
/*  74 */       throw new IllegalArgumentException("No bytes specified");
/*     */     }
/*  76 */     this.charsetName = charsetName;
/*  77 */     this.bytes = new int[bytes.length];
/*  78 */     System.arraycopy(bytes, 0, this.bytes, 0, bytes.length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getCharsetName() {
/*  87 */     return this.charsetName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int length() {
/*  96 */     return this.bytes.length;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int get(int pos) {
/* 106 */     return this.bytes[pos];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] getBytes() {
/* 115 */     byte[] copy = new byte[this.bytes.length];
/* 116 */     for (int i = 0; i < this.bytes.length; i++) {
/* 117 */       copy[i] = (byte)this.bytes[i];
/*     */     }
/* 119 */     return copy;
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
/*     */   public boolean equals(Object obj) {
/* 131 */     if (!(obj instanceof ByteOrderMark)) {
/* 132 */       return false;
/*     */     }
/* 134 */     ByteOrderMark bom = (ByteOrderMark)obj;
/* 135 */     if (this.bytes.length != bom.length()) {
/* 136 */       return false;
/*     */     }
/* 138 */     for (int i = 0; i < this.bytes.length; i++) {
/* 139 */       if (this.bytes[i] != bom.get(i)) {
/* 140 */         return false;
/*     */       }
/*     */     } 
/* 143 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 154 */     int hashCode = getClass().hashCode();
/* 155 */     for (int b : this.bytes) {
/* 156 */       hashCode += b;
/*     */     }
/* 158 */     return hashCode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 168 */     StringBuilder builder = new StringBuilder();
/* 169 */     builder.append(getClass().getSimpleName());
/* 170 */     builder.append('[');
/* 171 */     builder.append(this.charsetName);
/* 172 */     builder.append(": ");
/* 173 */     for (int i = 0; i < this.bytes.length; i++) {
/* 174 */       if (i > 0) {
/* 175 */         builder.append(",");
/*     */       }
/* 177 */       builder.append("0x");
/* 178 */       builder.append(Integer.toHexString(0xFF & this.bytes[i]).toUpperCase());
/*     */     } 
/* 180 */     builder.append(']');
/* 181 */     return builder.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\commons\io\ByteOrderMark.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */