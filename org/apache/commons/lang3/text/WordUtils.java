/*     */ package org.apache.commons.lang3.text;
/*     */ 
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.apache.commons.lang3.SystemUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class WordUtils
/*     */ {
/*     */   public static String wrap(String str, int wrapLength) {
/*  97 */     return wrap(str, wrapLength, null, false);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String wrap(String str, int wrapLength, String newLineStr, boolean wrapLongWords) {
/* 173 */     if (str == null) {
/* 174 */       return null;
/*     */     }
/* 176 */     if (newLineStr == null) {
/* 177 */       newLineStr = SystemUtils.LINE_SEPARATOR;
/*     */     }
/* 179 */     if (wrapLength < 1) {
/* 180 */       wrapLength = 1;
/*     */     }
/* 182 */     int inputLineLength = str.length();
/* 183 */     int offset = 0;
/* 184 */     StringBuilder wrappedLine = new StringBuilder(inputLineLength + 32);
/*     */     
/* 186 */     while (offset < inputLineLength) {
/* 187 */       if (str.charAt(offset) == ' ') {
/* 188 */         offset++;
/*     */         
/*     */         continue;
/*     */       } 
/* 192 */       if (inputLineLength - offset <= wrapLength) {
/*     */         break;
/*     */       }
/* 195 */       int spaceToWrapAt = str.lastIndexOf(' ', wrapLength + offset);
/*     */       
/* 197 */       if (spaceToWrapAt >= offset) {
/*     */         
/* 199 */         wrappedLine.append(str.substring(offset, spaceToWrapAt));
/* 200 */         wrappedLine.append(newLineStr);
/* 201 */         offset = spaceToWrapAt + 1;
/*     */         
/*     */         continue;
/*     */       } 
/* 205 */       if (wrapLongWords) {
/*     */         
/* 207 */         wrappedLine.append(str.substring(offset, wrapLength + offset));
/* 208 */         wrappedLine.append(newLineStr);
/* 209 */         offset += wrapLength;
/*     */         continue;
/*     */       } 
/* 212 */       spaceToWrapAt = str.indexOf(' ', wrapLength + offset);
/* 213 */       if (spaceToWrapAt >= 0) {
/* 214 */         wrappedLine.append(str.substring(offset, spaceToWrapAt));
/* 215 */         wrappedLine.append(newLineStr);
/* 216 */         offset = spaceToWrapAt + 1; continue;
/*     */       } 
/* 218 */       wrappedLine.append(str.substring(offset));
/* 219 */       offset = inputLineLength;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 226 */     wrappedLine.append(str.substring(offset));
/*     */     
/* 228 */     return wrappedLine.toString();
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
/*     */   public static String capitalize(String str) {
/* 256 */     return capitalize(str, null);
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
/*     */   public static String capitalize(String str, char... delimiters) {
/* 289 */     int delimLen = (delimiters == null) ? -1 : delimiters.length;
/* 290 */     if (StringUtils.isEmpty(str) || delimLen == 0) {
/* 291 */       return str;
/*     */     }
/* 293 */     char[] buffer = str.toCharArray();
/* 294 */     boolean capitalizeNext = true;
/* 295 */     for (int i = 0; i < buffer.length; i++) {
/* 296 */       char ch = buffer[i];
/* 297 */       if (isDelimiter(ch, delimiters)) {
/* 298 */         capitalizeNext = true;
/* 299 */       } else if (capitalizeNext) {
/* 300 */         buffer[i] = Character.toTitleCase(ch);
/* 301 */         capitalizeNext = false;
/*     */       } 
/*     */     } 
/* 304 */     return new String(buffer);
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
/*     */   public static String capitalizeFully(String str) {
/* 328 */     return capitalizeFully(str, null);
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
/*     */   public static String capitalizeFully(String str, char... delimiters) {
/* 358 */     int delimLen = (delimiters == null) ? -1 : delimiters.length;
/* 359 */     if (StringUtils.isEmpty(str) || delimLen == 0) {
/* 360 */       return str;
/*     */     }
/* 362 */     str = str.toLowerCase();
/* 363 */     return capitalize(str, delimiters);
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
/*     */   public static String uncapitalize(String str) {
/* 385 */     return uncapitalize(str, null);
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
/*     */   public static String uncapitalize(String str, char... delimiters) {
/* 414 */     int delimLen = (delimiters == null) ? -1 : delimiters.length;
/* 415 */     if (StringUtils.isEmpty(str) || delimLen == 0) {
/* 416 */       return str;
/*     */     }
/* 418 */     char[] buffer = str.toCharArray();
/* 419 */     boolean uncapitalizeNext = true;
/* 420 */     for (int i = 0; i < buffer.length; i++) {
/* 421 */       char ch = buffer[i];
/* 422 */       if (isDelimiter(ch, delimiters)) {
/* 423 */         uncapitalizeNext = true;
/* 424 */       } else if (uncapitalizeNext) {
/* 425 */         buffer[i] = Character.toLowerCase(ch);
/* 426 */         uncapitalizeNext = false;
/*     */       } 
/*     */     } 
/* 429 */     return new String(buffer);
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
/*     */   public static String swapCase(String str) {
/* 456 */     if (StringUtils.isEmpty(str)) {
/* 457 */       return str;
/*     */     }
/* 459 */     char[] buffer = str.toCharArray();
/*     */     
/* 461 */     boolean whitespace = true;
/*     */     
/* 463 */     for (int i = 0; i < buffer.length; i++) {
/* 464 */       char ch = buffer[i];
/* 465 */       if (Character.isUpperCase(ch)) {
/* 466 */         buffer[i] = Character.toLowerCase(ch);
/* 467 */         whitespace = false;
/* 468 */       } else if (Character.isTitleCase(ch)) {
/* 469 */         buffer[i] = Character.toLowerCase(ch);
/* 470 */         whitespace = false;
/* 471 */       } else if (Character.isLowerCase(ch)) {
/* 472 */         if (whitespace) {
/* 473 */           buffer[i] = Character.toTitleCase(ch);
/* 474 */           whitespace = false;
/*     */         } else {
/* 476 */           buffer[i] = Character.toUpperCase(ch);
/*     */         } 
/*     */       } else {
/* 479 */         whitespace = Character.isWhitespace(ch);
/*     */       } 
/*     */     } 
/* 482 */     return new String(buffer);
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
/*     */   public static String initials(String str) {
/* 509 */     return initials(str, null);
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
/*     */   public static String initials(String str, char... delimiters) {
/* 540 */     if (StringUtils.isEmpty(str)) {
/* 541 */       return str;
/*     */     }
/* 543 */     if (delimiters != null && delimiters.length == 0) {
/* 544 */       return "";
/*     */     }
/* 546 */     int strLen = str.length();
/* 547 */     char[] buf = new char[strLen / 2 + 1];
/* 548 */     int count = 0;
/* 549 */     boolean lastWasGap = true;
/* 550 */     for (int i = 0; i < strLen; i++) {
/* 551 */       char ch = str.charAt(i);
/*     */       
/* 553 */       if (isDelimiter(ch, delimiters)) {
/* 554 */         lastWasGap = true;
/* 555 */       } else if (lastWasGap) {
/* 556 */         buf[count++] = ch;
/* 557 */         lastWasGap = false;
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 562 */     return new String(buf, 0, count);
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
/*     */   private static boolean isDelimiter(char ch, char[] delimiters) {
/* 574 */     if (delimiters == null) {
/* 575 */       return Character.isWhitespace(ch);
/*     */     }
/* 577 */     for (char delimiter : delimiters) {
/* 578 */       if (ch == delimiter) {
/* 579 */         return true;
/*     */       }
/*     */     } 
/* 582 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\commons\lang3\text\WordUtils.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */