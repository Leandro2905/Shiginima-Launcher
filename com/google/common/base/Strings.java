/*     */ package com.google.common.base;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import javax.annotation.Nullable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtCompatible
/*     */ public final class Strings
/*     */ {
/*     */   public static String nullToEmpty(@Nullable String string) {
/*  47 */     return (string == null) ? "" : string;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public static String emptyToNull(@Nullable String string) {
/*  59 */     return isNullOrEmpty(string) ? null : string;
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
/*     */   public static boolean isNullOrEmpty(@Nullable String string) {
/*  76 */     return (string == null || string.length() == 0);
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
/*     */   public static String padStart(String string, int minLength, char padChar) {
/*  99 */     Preconditions.checkNotNull(string);
/* 100 */     if (string.length() >= minLength) {
/* 101 */       return string;
/*     */     }
/* 103 */     StringBuilder sb = new StringBuilder(minLength);
/* 104 */     for (int i = string.length(); i < minLength; i++) {
/* 105 */       sb.append(padChar);
/*     */     }
/* 107 */     sb.append(string);
/* 108 */     return sb.toString();
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
/*     */   public static String padEnd(String string, int minLength, char padChar) {
/* 131 */     Preconditions.checkNotNull(string);
/* 132 */     if (string.length() >= minLength) {
/* 133 */       return string;
/*     */     }
/* 135 */     StringBuilder sb = new StringBuilder(minLength);
/* 136 */     sb.append(string);
/* 137 */     for (int i = string.length(); i < minLength; i++) {
/* 138 */       sb.append(padChar);
/*     */     }
/* 140 */     return sb.toString();
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
/*     */   public static String repeat(String string, int count) {
/* 155 */     Preconditions.checkNotNull(string);
/*     */     
/* 157 */     if (count <= 1) {
/* 158 */       Preconditions.checkArgument((count >= 0), "invalid count: %s", new Object[] { Integer.valueOf(count) });
/* 159 */       return (count == 0) ? "" : string;
/*     */     } 
/*     */ 
/*     */     
/* 163 */     int len = string.length();
/* 164 */     long longSize = len * count;
/* 165 */     int size = (int)longSize;
/* 166 */     if (size != longSize) {
/* 167 */       long l = longSize; throw new ArrayIndexOutOfBoundsException((new StringBuilder(51)).append("Required array size too large: ").append(l).toString());
/*     */     } 
/*     */ 
/*     */     
/* 171 */     char[] array = new char[size];
/* 172 */     string.getChars(0, len, array, 0);
/*     */     int n;
/* 174 */     for (n = len; n < size - n; n <<= 1) {
/* 175 */       System.arraycopy(array, 0, array, n, n);
/*     */     }
/* 177 */     System.arraycopy(array, 0, array, n, size - n);
/* 178 */     return new String(array);
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
/*     */   public static String commonPrefix(CharSequence a, CharSequence b) {
/* 190 */     Preconditions.checkNotNull(a);
/* 191 */     Preconditions.checkNotNull(b);
/*     */     
/* 193 */     int maxPrefixLength = Math.min(a.length(), b.length());
/* 194 */     int p = 0;
/* 195 */     while (p < maxPrefixLength && a.charAt(p) == b.charAt(p)) {
/* 196 */       p++;
/*     */     }
/* 198 */     if (validSurrogatePairAt(a, p - 1) || validSurrogatePairAt(b, p - 1)) {
/* 199 */       p--;
/*     */     }
/* 201 */     return a.subSequence(0, p).toString();
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
/*     */   public static String commonSuffix(CharSequence a, CharSequence b) {
/* 213 */     Preconditions.checkNotNull(a);
/* 214 */     Preconditions.checkNotNull(b);
/*     */     
/* 216 */     int maxSuffixLength = Math.min(a.length(), b.length());
/* 217 */     int s = 0;
/*     */     
/* 219 */     while (s < maxSuffixLength && a.charAt(a.length() - s - 1) == b.charAt(b.length() - s - 1)) {
/* 220 */       s++;
/*     */     }
/* 222 */     if (validSurrogatePairAt(a, a.length() - s - 1) || validSurrogatePairAt(b, b.length() - s - 1))
/*     */     {
/* 224 */       s--;
/*     */     }
/* 226 */     return a.subSequence(a.length() - s, a.length()).toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   static boolean validSurrogatePairAt(CharSequence string, int index) {
/* 235 */     return (index >= 0 && index <= string.length() - 2 && Character.isHighSurrogate(string.charAt(index)) && Character.isLowSurrogate(string.charAt(index + 1)));
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\base\Strings.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */