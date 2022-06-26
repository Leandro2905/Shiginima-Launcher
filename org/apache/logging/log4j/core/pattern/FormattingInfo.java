/*     */ package org.apache.logging.log4j.core.pattern;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class FormattingInfo
/*     */ {
/*  27 */   private static final char[] SPACES = new char[] { ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ' };
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  32 */   private static final FormattingInfo DEFAULT = new FormattingInfo(false, 0, 2147483647);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final int minLength;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final int maxLength;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final boolean leftAlign;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FormattingInfo(boolean leftAlign, int minLength, int maxLength) {
/*  60 */     this.leftAlign = leftAlign;
/*  61 */     this.minLength = minLength;
/*  62 */     this.maxLength = maxLength;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static FormattingInfo getDefault() {
/*  71 */     return DEFAULT;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isLeftAligned() {
/*  80 */     return this.leftAlign;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMinLength() {
/*  89 */     return this.minLength;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMaxLength() {
/*  98 */     return this.maxLength;
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
/*     */   public void format(int fieldStart, StringBuilder buffer) {
/* 110 */     int rawLength = buffer.length() - fieldStart;
/*     */     
/* 112 */     if (rawLength > this.maxLength) {
/* 113 */       buffer.delete(fieldStart, buffer.length() - this.maxLength);
/* 114 */     } else if (rawLength < this.minLength) {
/* 115 */       if (this.leftAlign) {
/* 116 */         int fieldEnd = buffer.length();
/* 117 */         buffer.setLength(fieldStart + this.minLength);
/*     */         
/* 119 */         for (int i = fieldEnd; i < buffer.length(); i++) {
/* 120 */           buffer.setCharAt(i, ' ');
/*     */         }
/*     */       } else {
/* 123 */         int padLength = this.minLength - rawLength;
/*     */         
/* 125 */         for (; padLength > SPACES.length; padLength -= SPACES.length) {
/* 126 */           buffer.insert(fieldStart, SPACES);
/*     */         }
/*     */         
/* 129 */         buffer.insert(fieldStart, SPACES, 0, padLength);
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
/*     */   public String toString() {
/* 141 */     StringBuilder sb = new StringBuilder();
/* 142 */     sb.append(super.toString());
/* 143 */     sb.append("[leftAlign=");
/* 144 */     sb.append(this.leftAlign);
/* 145 */     sb.append(", maxLength=");
/* 146 */     sb.append(this.maxLength);
/* 147 */     sb.append(", minLength=");
/* 148 */     sb.append(this.minLength);
/* 149 */     sb.append(']');
/* 150 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\pattern\FormattingInfo.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */