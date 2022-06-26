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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CharSetUtils
/*     */ {
/*     */   public static String squeeze(String str, String... set) {
/*  65 */     if (StringUtils.isEmpty(str) || deepEmpty(set)) {
/*  66 */       return str;
/*     */     }
/*  68 */     CharSet chars = CharSet.getInstance(set);
/*  69 */     StringBuilder buffer = new StringBuilder(str.length());
/*  70 */     char[] chrs = str.toCharArray();
/*  71 */     int sz = chrs.length;
/*  72 */     char lastChar = ' ';
/*  73 */     char ch = ' ';
/*  74 */     for (int i = 0; i < sz; i++) {
/*  75 */       ch = chrs[i];
/*     */       
/*  77 */       if (ch != lastChar || i == 0 || !chars.contains(ch)) {
/*     */ 
/*     */         
/*  80 */         buffer.append(ch);
/*  81 */         lastChar = ch;
/*     */       } 
/*  83 */     }  return buffer.toString();
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
/*     */   public static boolean containsAny(String str, String... set) {
/* 108 */     if (StringUtils.isEmpty(str) || deepEmpty(set)) {
/* 109 */       return false;
/*     */     }
/* 111 */     CharSet chars = CharSet.getInstance(set);
/* 112 */     for (char c : str.toCharArray()) {
/* 113 */       if (chars.contains(c)) {
/* 114 */         return true;
/*     */       }
/*     */     } 
/* 117 */     return false;
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
/*     */   public static int count(String str, String... set) {
/* 141 */     if (StringUtils.isEmpty(str) || deepEmpty(set)) {
/* 142 */       return 0;
/*     */     }
/* 144 */     CharSet chars = CharSet.getInstance(set);
/* 145 */     int count = 0;
/* 146 */     for (char c : str.toCharArray()) {
/* 147 */       if (chars.contains(c)) {
/* 148 */         count++;
/*     */       }
/*     */     } 
/* 151 */     return count;
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
/*     */   public static String keep(String str, String... set) {
/* 176 */     if (str == null) {
/* 177 */       return null;
/*     */     }
/* 179 */     if (str.isEmpty() || deepEmpty(set)) {
/* 180 */       return "";
/*     */     }
/* 182 */     return modify(str, set, true);
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
/*     */   public static String delete(String str, String... set) {
/* 206 */     if (StringUtils.isEmpty(str) || deepEmpty(set)) {
/* 207 */       return str;
/*     */     }
/* 209 */     return modify(str, set, false);
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
/*     */   private static String modify(String str, String[] set, boolean expect) {
/* 222 */     CharSet chars = CharSet.getInstance(set);
/* 223 */     StringBuilder buffer = new StringBuilder(str.length());
/* 224 */     char[] chrs = str.toCharArray();
/* 225 */     int sz = chrs.length;
/* 226 */     for (int i = 0; i < sz; i++) {
/* 227 */       if (chars.contains(chrs[i]) == expect) {
/* 228 */         buffer.append(chrs[i]);
/*     */       }
/*     */     } 
/* 231 */     return buffer.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean deepEmpty(String[] strings) {
/* 242 */     if (strings != null) {
/* 243 */       for (String s : strings) {
/* 244 */         if (StringUtils.isNotEmpty(s)) {
/* 245 */           return false;
/*     */         }
/*     */       } 
/*     */     }
/* 249 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\commons\lang3\CharSetUtils.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */