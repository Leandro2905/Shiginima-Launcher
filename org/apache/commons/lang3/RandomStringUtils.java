/*     */ package org.apache.commons.lang3;
/*     */ 
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RandomStringUtils
/*     */ {
/*  43 */   private static final Random RANDOM = new Random();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String random(int count) {
/*  69 */     return random(count, false, false);
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
/*     */   public static String randomAscii(int count) {
/*  83 */     return random(count, 32, 127, false, false);
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
/*     */   public static String randomAlphabetic(int count) {
/*  97 */     return random(count, true, false);
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
/*     */   public static String randomAlphanumeric(int count) {
/* 111 */     return random(count, true, true);
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
/*     */   public static String randomNumeric(int count) {
/* 125 */     return random(count, false, true);
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
/*     */   public static String random(int count, boolean letters, boolean numbers) {
/* 143 */     return random(count, 0, 0, letters, numbers);
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
/*     */   public static String random(int count, int start, int end, boolean letters, boolean numbers) {
/* 163 */     return random(count, start, end, letters, numbers, null, RANDOM);
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
/*     */   public static String random(int count, int start, int end, boolean letters, boolean numbers, char... chars) {
/* 187 */     return random(count, start, end, letters, numbers, chars, RANDOM);
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
/*     */   public static String random(int count, int start, int end, boolean letters, boolean numbers, char[] chars, Random random) {
/* 225 */     if (count == 0)
/* 226 */       return ""; 
/* 227 */     if (count < 0) {
/* 228 */       throw new IllegalArgumentException("Requested random string length " + count + " is less than 0.");
/*     */     }
/* 230 */     if (chars != null && chars.length == 0) {
/* 231 */       throw new IllegalArgumentException("The chars array must not be empty");
/*     */     }
/*     */     
/* 234 */     if (start == 0 && end == 0) {
/* 235 */       if (chars != null) {
/* 236 */         end = chars.length;
/*     */       }
/* 238 */       else if (!letters && !numbers) {
/* 239 */         end = Integer.MAX_VALUE;
/*     */       } else {
/* 241 */         end = 123;
/* 242 */         start = 32;
/*     */       }
/*     */     
/*     */     }
/* 246 */     else if (end <= start) {
/* 247 */       throw new IllegalArgumentException("Parameter end (" + end + ") must be greater than start (" + start + ")");
/*     */     } 
/*     */ 
/*     */     
/* 251 */     char[] buffer = new char[count];
/* 252 */     int gap = end - start;
/*     */     
/* 254 */     while (count-- != 0) {
/*     */       char ch;
/* 256 */       if (chars == null) {
/* 257 */         ch = (char)(random.nextInt(gap) + start);
/*     */       } else {
/* 259 */         ch = chars[random.nextInt(gap) + start];
/*     */       } 
/* 261 */       if ((letters && Character.isLetter(ch)) || (numbers && Character.isDigit(ch)) || (!letters && !numbers)) {
/*     */ 
/*     */         
/* 264 */         if (ch >= '?' && ch <= '?') {
/* 265 */           if (count == 0) {
/* 266 */             count++;
/*     */             continue;
/*     */           } 
/* 269 */           buffer[count] = ch;
/* 270 */           count--;
/* 271 */           buffer[count] = (char)(55296 + random.nextInt(128)); continue;
/*     */         } 
/* 273 */         if (ch >= '?' && ch <= '?') {
/* 274 */           if (count == 0) {
/* 275 */             count++;
/*     */             continue;
/*     */           } 
/* 278 */           buffer[count] = (char)(56320 + random.nextInt(128));
/* 279 */           count--;
/* 280 */           buffer[count] = ch; continue;
/*     */         } 
/* 282 */         if (ch >= '?' && ch <= '?') {
/*     */           
/* 284 */           count++; continue;
/*     */         } 
/* 286 */         buffer[count] = ch;
/*     */         continue;
/*     */       } 
/* 289 */       count++;
/*     */     } 
/*     */     
/* 292 */     return new String(buffer);
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
/*     */   public static String random(int count, String chars) {
/* 310 */     if (chars == null) {
/* 311 */       return random(count, 0, 0, false, false, null, RANDOM);
/*     */     }
/* 313 */     return random(count, chars.toCharArray());
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
/*     */   public static String random(int count, char... chars) {
/* 329 */     if (chars == null) {
/* 330 */       return random(count, 0, 0, false, false, null, RANDOM);
/*     */     }
/* 332 */     return random(count, 0, chars.length, false, false, chars, RANDOM);
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\commons\lang3\RandomStringUtils.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */