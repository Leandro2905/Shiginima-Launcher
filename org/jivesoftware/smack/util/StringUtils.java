/*     */ package org.jivesoftware.smack.util;
/*     */ 
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.util.Collection;
/*     */ import java.util.Random;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class StringUtils
/*     */ {
/*     */   public static final String MD5 = "MD5";
/*     */   public static final String SHA1 = "SHA-1";
/*     */   public static final String UTF8 = "UTF-8";
/*     */   public static final String USASCII = "US-ASCII";
/*     */   public static final String QUOTE_ENCODE = "&quot;";
/*     */   public static final String APOS_ENCODE = "&apos;";
/*     */   public static final String AMP_ENCODE = "&amp;";
/*     */   public static final String LT_ENCODE = "&lt;";
/*     */   public static final String GT_ENCODE = "&gt;";
/*  40 */   public static final char[] HEX_CHARS = "0123456789abcdef".toCharArray();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static CharSequence escapeForXML(String string) {
/*  50 */     if (string == null) {
/*  51 */       return null;
/*     */     }
/*  53 */     char[] input = string.toCharArray();
/*  54 */     int len = input.length;
/*  55 */     StringBuilder out = new StringBuilder((int)(len * 1.3D));
/*     */ 
/*     */     
/*  58 */     int last = 0;
/*  59 */     int i = 0;
/*  60 */     while (i < len) {
/*  61 */       CharSequence toAppend = null;
/*  62 */       char ch = input[i];
/*  63 */       switch (ch) {
/*     */         case '<':
/*  65 */           toAppend = "&lt;";
/*     */           break;
/*     */         case '>':
/*  68 */           toAppend = "&gt;";
/*     */           break;
/*     */         case '&':
/*  71 */           toAppend = "&amp;";
/*     */           break;
/*     */         case '"':
/*  74 */           toAppend = "&quot;";
/*     */           break;
/*     */         case '\'':
/*  77 */           toAppend = "&apos;";
/*     */           break;
/*     */       } 
/*     */ 
/*     */       
/*  82 */       if (toAppend != null) {
/*  83 */         if (i > last) {
/*  84 */           out.append(input, last, i - last);
/*     */         }
/*  86 */         out.append(toAppend);
/*  87 */         last = ++i; continue;
/*     */       } 
/*  89 */       i++;
/*     */     } 
/*     */     
/*  92 */     if (last == 0) {
/*  93 */       return string;
/*     */     }
/*  95 */     if (i > last) {
/*  96 */       out.append(input, last, i - last);
/*     */     }
/*  98 */     return out;
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
/*     */   @Deprecated
/*     */   public static synchronized String hash(String data) {
/* 120 */     return SHA1.hex(data);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String encodeHex(byte[] bytes) {
/* 130 */     char[] hexChars = new char[bytes.length * 2];
/* 131 */     for (int j = 0; j < bytes.length; j++) {
/* 132 */       int v = bytes[j] & 0xFF;
/* 133 */       hexChars[j * 2] = HEX_CHARS[v >>> 4];
/* 134 */       hexChars[j * 2 + 1] = HEX_CHARS[v & 0xF];
/*     */     } 
/* 136 */     return new String(hexChars);
/*     */   }
/*     */   
/*     */   public static byte[] toBytes(String string) {
/*     */     try {
/* 141 */       return string.getBytes("UTF-8");
/*     */     }
/* 143 */     catch (UnsupportedEncodingException e) {
/* 144 */       throw new IllegalStateException("UTF-8 encoding not supported by platform", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 153 */   private static Random randGen = new Random();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 161 */   private static char[] numbersAndLetters = "0123456789abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String randomString(int length) {
/* 178 */     if (length < 1) {
/* 179 */       return null;
/*     */     }
/*     */     
/* 182 */     char[] randBuffer = new char[length];
/* 183 */     for (int i = 0; i < randBuffer.length; i++) {
/* 184 */       randBuffer[i] = numbersAndLetters[randGen.nextInt(numbersAndLetters.length)];
/*     */     }
/* 186 */     return new String(randBuffer);
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
/*     */   public static boolean isNotEmpty(CharSequence cs) {
/* 201 */     return !isNullOrEmpty(cs);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isNullOrEmpty(CharSequence cs) {
/* 211 */     return (cs == null || isEmpty(cs));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isEmpty(CharSequence cs) {
/* 221 */     return (cs.length() == 0);
/*     */   }
/*     */   
/*     */   public static String collectionToString(Collection<String> collection) {
/* 225 */     StringBuilder sb = new StringBuilder();
/* 226 */     for (String s : collection) {
/* 227 */       sb.append(s);
/* 228 */       sb.append(" ");
/*     */     } 
/* 230 */     String res = sb.toString();
/*     */     
/* 232 */     res = res.substring(0, res.length() - 1);
/* 233 */     return res;
/*     */   }
/*     */   
/*     */   public static String returnIfNotEmptyTrimmed(String string) {
/* 237 */     if (string == null)
/* 238 */       return null; 
/* 239 */     String trimmedString = string.trim();
/* 240 */     if (trimmedString.length() > 0) {
/* 241 */       return trimmedString;
/*     */     }
/* 243 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean nullSafeCharSequenceEquals(CharSequence csOne, CharSequence csTwo) {
/* 248 */     return (nullSafeCharSequenceComperator(csOne, csTwo) == 0);
/*     */   }
/*     */   
/*     */   public static int nullSafeCharSequenceComperator(CharSequence csOne, CharSequence csTwo) {
/* 252 */     if ((((csOne == null) ? 1 : 0) ^ ((csTwo == null) ? 1 : 0)) != 0) {
/* 253 */       return (csOne == null) ? -1 : 1;
/*     */     }
/* 255 */     if (csOne == null && csTwo == null) {
/* 256 */       return 0;
/*     */     }
/* 258 */     return csOne.toString().compareTo(csTwo.toString());
/*     */   }
/*     */   
/*     */   public static <CS extends CharSequence> CS requireNotNullOrEmpty(CS cs, String message) {
/* 262 */     if (isNullOrEmpty((CharSequence)cs)) {
/* 263 */       throw new IllegalArgumentException(message);
/*     */     }
/* 265 */     return cs;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String maybeToString(CharSequence cs) {
/* 275 */     if (cs == null) {
/* 276 */       return null;
/*     */     }
/* 278 */     return cs.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jivesoftware\smac\\util\StringUtils.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */