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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MatchRatingApproachEncoder
/*     */   implements StringEncoder
/*     */ {
/*     */   private static final String SPACE = " ";
/*     */   private static final String EMPTY = "";
/*     */   private static final int ONE = 1;
/*     */   private static final int TWO = 2;
/*     */   private static final int THREE = 3;
/*     */   private static final int FOUR = 4;
/*     */   private static final int FIVE = 5;
/*     */   private static final int SIX = 6;
/*     */   private static final int SEVEN = 7;
/*     */   private static final int EIGHT = 8;
/*     */   private static final int ELEVEN = 11;
/*     */   private static final int TWELVE = 12;
/*     */   private static final String PLAIN_ASCII = "AaEeIiOoUuAaEeIiOoUuYyAaEeIiOoUuYyAaOoNnAaEeIiOoUuYyAaCcOoUu";
/*     */   private static final String UNICODE = "ÀàÈèÌìÒòÙùÁáÉéÍíÓóÚúÝýÂâÊêÎîÔôÛûŶŷÃãÕõÑñÄäËëÏïÖöÜüŸÿÅåÇçŐőŰű";
/*  66 */   private static final String[] DOUBLE_CONSONANT = new String[] { "BB", "CC", "DD", "FF", "GG", "HH", "JJ", "KK", "LL", "MM", "NN", "PP", "QQ", "RR", "SS", "TT", "VV", "WW", "XX", "YY", "ZZ" };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   String cleanName(String name) {
/*  84 */     String upperName = name.toUpperCase(Locale.ENGLISH);
/*     */     
/*  86 */     String[] charsToTrim = { "\\-", "[&]", "\\'", "\\.", "[\\,]" };
/*  87 */     for (String str : charsToTrim) {
/*  88 */       upperName = upperName.replaceAll(str, "");
/*     */     }
/*     */     
/*  91 */     upperName = removeAccents(upperName);
/*  92 */     upperName = upperName.replaceAll("\\s+", "");
/*     */     
/*  94 */     return upperName;
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
/*     */   public final Object encode(Object pObject) throws EncoderException {
/* 110 */     if (!(pObject instanceof String)) {
/* 111 */       throw new EncoderException("Parameter supplied to Match Rating Approach encoder is not of type java.lang.String");
/*     */     }
/*     */     
/* 114 */     return encode((String)pObject);
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
/*     */   public final String encode(String name) {
/* 127 */     if (name == null || "".equalsIgnoreCase(name) || " ".equalsIgnoreCase(name) || name.length() == 1) {
/* 128 */       return "";
/*     */     }
/*     */ 
/*     */     
/* 132 */     name = cleanName(name);
/*     */ 
/*     */ 
/*     */     
/* 136 */     name = removeVowels(name);
/*     */ 
/*     */     
/* 139 */     name = removeDoubleConsonants(name);
/*     */ 
/*     */     
/* 142 */     name = getFirst3Last3(name);
/*     */     
/* 144 */     return name;
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
/*     */   String getFirst3Last3(String name) {
/* 160 */     int nameLength = name.length();
/*     */     
/* 162 */     if (nameLength > 6) {
/* 163 */       String firstThree = name.substring(0, 3);
/* 164 */       String lastThree = name.substring(nameLength - 3, nameLength);
/* 165 */       return firstThree + lastThree;
/*     */     } 
/* 167 */     return name;
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
/*     */   int getMinRating(int sumLength) {
/* 185 */     int minRating = 0;
/*     */     
/* 187 */     if (sumLength <= 4) {
/* 188 */       minRating = 5;
/* 189 */     } else if (sumLength >= 5 && sumLength <= 7) {
/* 190 */       minRating = 4;
/* 191 */     } else if (sumLength >= 8 && sumLength <= 11) {
/* 192 */       minRating = 3;
/* 193 */     } else if (sumLength == 12) {
/* 194 */       minRating = 2;
/*     */     } else {
/* 196 */       minRating = 1;
/*     */     } 
/*     */     
/* 199 */     return minRating;
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
/*     */   public boolean isEncodeEquals(String name1, String name2) {
/* 214 */     if (name1 == null || "".equalsIgnoreCase(name1) || " ".equalsIgnoreCase(name1))
/* 215 */       return false; 
/* 216 */     if (name2 == null || "".equalsIgnoreCase(name2) || " ".equalsIgnoreCase(name2))
/* 217 */       return false; 
/* 218 */     if (name1.length() == 1 || name2.length() == 1)
/* 219 */       return false; 
/* 220 */     if (name1.equalsIgnoreCase(name2)) {
/* 221 */       return true;
/*     */     }
/*     */ 
/*     */     
/* 225 */     name1 = cleanName(name1);
/* 226 */     name2 = cleanName(name2);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 231 */     name1 = removeVowels(name1);
/* 232 */     name2 = removeVowels(name2);
/*     */ 
/*     */     
/* 235 */     name1 = removeDoubleConsonants(name1);
/* 236 */     name2 = removeDoubleConsonants(name2);
/*     */ 
/*     */     
/* 239 */     name1 = getFirst3Last3(name1);
/* 240 */     name2 = getFirst3Last3(name2);
/*     */ 
/*     */ 
/*     */     
/* 244 */     if (Math.abs(name1.length() - name2.length()) >= 3) {
/* 245 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 250 */     int sumLength = Math.abs(name1.length() + name2.length());
/* 251 */     int minRating = 0;
/* 252 */     minRating = getMinRating(sumLength);
/*     */ 
/*     */ 
/*     */     
/* 256 */     int count = leftToRightThenRightToLeftProcessing(name1, name2);
/*     */ 
/*     */ 
/*     */     
/* 260 */     return (count >= minRating);
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
/*     */   boolean isVowel(String letter) {
/* 277 */     return (letter.equalsIgnoreCase("E") || letter.equalsIgnoreCase("A") || letter.equalsIgnoreCase("O") || letter.equalsIgnoreCase("I") || letter.equalsIgnoreCase("U"));
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
/*     */   int leftToRightThenRightToLeftProcessing(String name1, String name2) {
/* 295 */     char[] name1Char = name1.toCharArray();
/* 296 */     char[] name2Char = name2.toCharArray();
/*     */     
/* 298 */     int name1Size = name1.length() - 1;
/* 299 */     int name2Size = name2.length() - 1;
/*     */     
/* 301 */     String name1LtRStart = "";
/* 302 */     String name1LtREnd = "";
/*     */     
/* 304 */     String name2RtLStart = "";
/* 305 */     String name2RtLEnd = "";
/*     */     
/* 307 */     for (int i = 0; i < name1Char.length && 
/* 308 */       i <= name2Size; i++) {
/*     */ 
/*     */ 
/*     */       
/* 312 */       name1LtRStart = name1.substring(i, i + 1);
/* 313 */       name1LtREnd = name1.substring(name1Size - i, name1Size - i + 1);
/*     */       
/* 315 */       name2RtLStart = name2.substring(i, i + 1);
/* 316 */       name2RtLEnd = name2.substring(name2Size - i, name2Size - i + 1);
/*     */ 
/*     */       
/* 319 */       if (name1LtRStart.equals(name2RtLStart)) {
/* 320 */         name1Char[i] = ' ';
/* 321 */         name2Char[i] = ' ';
/*     */       } 
/*     */ 
/*     */       
/* 325 */       if (name1LtREnd.equals(name2RtLEnd)) {
/* 326 */         name1Char[name1Size - i] = ' ';
/* 327 */         name2Char[name2Size - i] = ' ';
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 332 */     String strA = (new String(name1Char)).replaceAll("\\s+", "");
/* 333 */     String strB = (new String(name2Char)).replaceAll("\\s+", "");
/*     */ 
/*     */     
/* 336 */     if (strA.length() > strB.length()) {
/* 337 */       return Math.abs(6 - strA.length());
/*     */     }
/* 339 */     return Math.abs(6 - strB.length());
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
/*     */   String removeAccents(String accentedWord) {
/* 352 */     if (accentedWord == null) {
/* 353 */       return null;
/*     */     }
/*     */     
/* 356 */     StringBuilder sb = new StringBuilder();
/* 357 */     int n = accentedWord.length();
/*     */     
/* 359 */     for (int i = 0; i < n; i++) {
/* 360 */       char c = accentedWord.charAt(i);
/* 361 */       int pos = "ÀàÈèÌìÒòÙùÁáÉéÍíÓóÚúÝýÂâÊêÎîÔôÛûŶŷÃãÕõÑñÄäËëÏïÖöÜüŸÿÅåÇçŐőŰű".indexOf(c);
/* 362 */       if (pos > -1) {
/* 363 */         sb.append("AaEeIiOoUuAaEeIiOoUuYyAaEeIiOoUuYyAaOoNnAaEeIiOoUuYyAaCcOoUu".charAt(pos));
/*     */       } else {
/* 365 */         sb.append(c);
/*     */       } 
/*     */     } 
/*     */     
/* 369 */     return sb.toString();
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
/*     */   String removeDoubleConsonants(String name) {
/* 385 */     String replacedName = name.toUpperCase();
/* 386 */     for (String dc : DOUBLE_CONSONANT) {
/* 387 */       if (replacedName.contains(dc)) {
/* 388 */         String singleLetter = dc.substring(0, 1);
/* 389 */         replacedName = replacedName.replace(dc, singleLetter);
/*     */       } 
/*     */     } 
/* 392 */     return replacedName;
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
/*     */   String removeVowels(String name) {
/* 409 */     String firstLetter = name.substring(0, 1);
/*     */     
/* 411 */     name = name.replaceAll("A", "");
/* 412 */     name = name.replaceAll("E", "");
/* 413 */     name = name.replaceAll("I", "");
/* 414 */     name = name.replaceAll("O", "");
/* 415 */     name = name.replaceAll("U", "");
/*     */     
/* 417 */     name = name.replaceAll("\\s{2,}\\b", " ");
/*     */ 
/*     */     
/* 420 */     if (isVowel(firstLetter)) {
/* 421 */       return firstLetter + name;
/*     */     }
/* 423 */     return name;
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\commons\codec\language\MatchRatingApproachEncoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */