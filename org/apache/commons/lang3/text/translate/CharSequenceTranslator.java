/*     */ package org.apache.commons.lang3.text.translate;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.StringWriter;
/*     */ import java.io.Writer;
/*     */ import java.util.Locale;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class CharSequenceTranslator
/*     */ {
/*  34 */   static final char[] HEX_DIGITS = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract int translate(CharSequence paramCharSequence, int paramInt, Writer paramWriter) throws IOException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String translate(CharSequence input) {
/*  56 */     if (input == null) {
/*  57 */       return null;
/*     */     }
/*     */     try {
/*  60 */       StringWriter writer = new StringWriter(input.length() * 2);
/*  61 */       translate(input, writer);
/*  62 */       return writer.toString();
/*  63 */     } catch (IOException ioe) {
/*     */       
/*  65 */       throw new RuntimeException(ioe);
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
/*     */   public final void translate(CharSequence input, Writer out) throws IOException {
/*  78 */     if (out == null) {
/*  79 */       throw new IllegalArgumentException("The Writer must not be null");
/*     */     }
/*  81 */     if (input == null) {
/*     */       return;
/*     */     }
/*  84 */     int pos = 0;
/*  85 */     int len = input.length();
/*  86 */     while (pos < len) {
/*  87 */       int consumed = translate(input, pos, out);
/*  88 */       if (consumed == 0) {
/*     */ 
/*     */         
/*  91 */         char c1 = input.charAt(pos);
/*  92 */         out.write(c1);
/*  93 */         pos++;
/*  94 */         if (Character.isHighSurrogate(c1) && pos < len) {
/*  95 */           char c2 = input.charAt(pos);
/*  96 */           if (Character.isLowSurrogate(c2)) {
/*  97 */             out.write(c2);
/*  98 */             pos++;
/*     */           } 
/*     */         } 
/*     */         
/*     */         continue;
/*     */       } 
/*     */       
/* 105 */       for (int pt = 0; pt < consumed; pt++) {
/* 106 */         pos += Character.charCount(Character.codePointAt(input, pos));
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
/*     */   public final CharSequenceTranslator with(CharSequenceTranslator... translators) {
/* 119 */     CharSequenceTranslator[] newArray = new CharSequenceTranslator[translators.length + 1];
/* 120 */     newArray[0] = this;
/* 121 */     System.arraycopy(translators, 0, newArray, 1, translators.length);
/* 122 */     return new AggregateTranslator(newArray);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String hex(int codepoint) {
/* 133 */     return Integer.toHexString(codepoint).toUpperCase(Locale.ENGLISH);
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\commons\lang3\text\translate\CharSequenceTranslator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */