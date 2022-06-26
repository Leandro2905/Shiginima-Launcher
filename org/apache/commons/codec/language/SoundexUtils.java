/*     */ package org.apache.commons.codec.language;
/*     */ 
/*     */ import java.util.Locale;
/*     */ import org.apache.commons.codec.EncoderException;
/*     */ import org.apache.commons.codec.StringEncoder;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class SoundexUtils
/*     */ {
/*     */   static String clean(String str) {
/*  42 */     if (str == null || str.length() == 0) {
/*  43 */       return str;
/*     */     }
/*  45 */     int len = str.length();
/*  46 */     char[] chars = new char[len];
/*  47 */     int count = 0;
/*  48 */     for (int i = 0; i < len; i++) {
/*  49 */       if (Character.isLetter(str.charAt(i))) {
/*  50 */         chars[count++] = str.charAt(i);
/*     */       }
/*     */     } 
/*  53 */     if (count == len) {
/*  54 */       return str.toUpperCase(Locale.ENGLISH);
/*     */     }
/*  56 */     return (new String(chars, 0, count)).toUpperCase(Locale.ENGLISH);
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
/*     */   static int difference(StringEncoder encoder, String s1, String s2) throws EncoderException {
/*  86 */     return differenceEncoded(encoder.encode(s1), encoder.encode(s2));
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
/*     */   static int differenceEncoded(String es1, String es2) {
/* 111 */     if (es1 == null || es2 == null) {
/* 112 */       return 0;
/*     */     }
/* 114 */     int lengthToMatch = Math.min(es1.length(), es2.length());
/* 115 */     int diff = 0;
/* 116 */     for (int i = 0; i < lengthToMatch; i++) {
/* 117 */       if (es1.charAt(i) == es2.charAt(i)) {
/* 118 */         diff++;
/*     */       }
/*     */     } 
/* 121 */     return diff;
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\commons\codec\language\SoundexUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */