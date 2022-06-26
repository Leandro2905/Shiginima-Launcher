/*     */ package org.apache.commons.lang3;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CharSequenceUtils
/*     */ {
/*     */   private static final int NOT_FOUND = -1;
/*     */   
/*     */   public static CharSequence subSequence(CharSequence cs, int start) {
/*  58 */     return (cs == null) ? null : cs.subSequence(start, cs.length());
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
/*     */   static int indexOf(CharSequence cs, int searchChar, int start) {
/*  72 */     if (cs instanceof String) {
/*  73 */       return ((String)cs).indexOf(searchChar, start);
/*     */     }
/*  75 */     int sz = cs.length();
/*  76 */     if (start < 0) {
/*  77 */       start = 0;
/*     */     }
/*  79 */     for (int i = start; i < sz; i++) {
/*  80 */       if (cs.charAt(i) == searchChar) {
/*  81 */         return i;
/*     */       }
/*     */     } 
/*  84 */     return -1;
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
/*     */   static int indexOf(CharSequence cs, CharSequence searchChar, int start) {
/*  96 */     return cs.toString().indexOf(searchChar.toString(), start);
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
/*     */   static int lastIndexOf(CharSequence cs, int searchChar, int start) {
/* 118 */     if (cs instanceof String) {
/* 119 */       return ((String)cs).lastIndexOf(searchChar, start);
/*     */     }
/* 121 */     int sz = cs.length();
/* 122 */     if (start < 0) {
/* 123 */       return -1;
/*     */     }
/* 125 */     if (start >= sz) {
/* 126 */       start = sz - 1;
/*     */     }
/* 128 */     for (int i = start; i >= 0; i--) {
/* 129 */       if (cs.charAt(i) == searchChar) {
/* 130 */         return i;
/*     */       }
/*     */     } 
/* 133 */     return -1;
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
/*     */   static int lastIndexOf(CharSequence cs, CharSequence searchChar, int start) {
/* 145 */     return cs.toString().lastIndexOf(searchChar.toString(), start);
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
/*     */   static char[] toCharArray(CharSequence cs) {
/* 164 */     if (cs instanceof String) {
/* 165 */       return ((String)cs).toCharArray();
/*     */     }
/* 167 */     int sz = cs.length();
/* 168 */     char[] array = new char[cs.length()];
/* 169 */     for (int i = 0; i < sz; i++) {
/* 170 */       array[i] = cs.charAt(i);
/*     */     }
/* 172 */     return array;
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
/*     */   static boolean regionMatches(CharSequence cs, boolean ignoreCase, int thisStart, CharSequence substring, int start, int length) {
/* 188 */     if (cs instanceof String && substring instanceof String) {
/* 189 */       return ((String)cs).regionMatches(ignoreCase, thisStart, (String)substring, start, length);
/*     */     }
/* 191 */     int index1 = thisStart;
/* 192 */     int index2 = start;
/* 193 */     int tmpLen = length;
/*     */     
/* 195 */     while (tmpLen-- > 0) {
/* 196 */       char c1 = cs.charAt(index1++);
/* 197 */       char c2 = substring.charAt(index2++);
/*     */       
/* 199 */       if (c1 == c2) {
/*     */         continue;
/*     */       }
/*     */       
/* 203 */       if (!ignoreCase) {
/* 204 */         return false;
/*     */       }
/*     */ 
/*     */       
/* 208 */       if (Character.toUpperCase(c1) != Character.toUpperCase(c2) && Character.toLowerCase(c1) != Character.toLowerCase(c2))
/*     */       {
/* 210 */         return false;
/*     */       }
/*     */     } 
/*     */     
/* 214 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\commons\lang3\CharSequenceUtils.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */