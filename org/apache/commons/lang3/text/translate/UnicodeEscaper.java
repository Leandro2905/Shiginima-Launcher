/*     */ package org.apache.commons.lang3.text.translate;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.Writer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class UnicodeEscaper
/*     */   extends CodePointTranslator
/*     */ {
/*     */   private final int below;
/*     */   private final int above;
/*     */   private final boolean between;
/*     */   
/*     */   public UnicodeEscaper() {
/*  38 */     this(0, 2147483647, true);
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
/*     */   protected UnicodeEscaper(int below, int above, boolean between) {
/*  52 */     this.below = below;
/*  53 */     this.above = above;
/*  54 */     this.between = between;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static UnicodeEscaper below(int codepoint) {
/*  64 */     return outsideOf(codepoint, 2147483647);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static UnicodeEscaper above(int codepoint) {
/*  74 */     return outsideOf(0, codepoint);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static UnicodeEscaper outsideOf(int codepointLow, int codepointHigh) {
/*  85 */     return new UnicodeEscaper(codepointLow, codepointHigh, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static UnicodeEscaper between(int codepointLow, int codepointHigh) {
/*  96 */     return new UnicodeEscaper(codepointLow, codepointHigh, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean translate(int codepoint, Writer out) throws IOException {
/* 104 */     if (this.between) {
/* 105 */       if (codepoint < this.below || codepoint > this.above) {
/* 106 */         return false;
/*     */       }
/*     */     }
/* 109 */     else if (codepoint >= this.below && codepoint <= this.above) {
/* 110 */       return false;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 115 */     if (codepoint > 65535) {
/* 116 */       out.write(toUtf16Escape(codepoint));
/*     */     } else {
/* 118 */       out.write("\\u");
/* 119 */       out.write(HEX_DIGITS[codepoint >> 12 & 0xF]);
/* 120 */       out.write(HEX_DIGITS[codepoint >> 8 & 0xF]);
/* 121 */       out.write(HEX_DIGITS[codepoint >> 4 & 0xF]);
/* 122 */       out.write(HEX_DIGITS[codepoint & 0xF]);
/*     */     } 
/* 124 */     return true;
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
/*     */   protected String toUtf16Escape(int codepoint) {
/* 137 */     return "\\u" + hex(codepoint);
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\commons\lang3\text\translate\UnicodeEscaper.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */