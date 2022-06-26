/*     */ package org.apache.commons.codec.language;
/*     */ 
/*     */ import java.util.regex.Pattern;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Nysiis
/*     */   implements StringEncoder
/*     */ {
/*  72 */   private static final char[] CHARS_A = new char[] { 'A' };
/*  73 */   private static final char[] CHARS_AF = new char[] { 'A', 'F' };
/*  74 */   private static final char[] CHARS_C = new char[] { 'C' };
/*  75 */   private static final char[] CHARS_FF = new char[] { 'F', 'F' };
/*  76 */   private static final char[] CHARS_G = new char[] { 'G' };
/*  77 */   private static final char[] CHARS_N = new char[] { 'N' };
/*  78 */   private static final char[] CHARS_NN = new char[] { 'N', 'N' };
/*  79 */   private static final char[] CHARS_S = new char[] { 'S' };
/*  80 */   private static final char[] CHARS_SSS = new char[] { 'S', 'S', 'S' };
/*     */   
/*  82 */   private static final Pattern PAT_MAC = Pattern.compile("^MAC");
/*  83 */   private static final Pattern PAT_KN = Pattern.compile("^KN");
/*  84 */   private static final Pattern PAT_K = Pattern.compile("^K");
/*  85 */   private static final Pattern PAT_PH_PF = Pattern.compile("^(PH|PF)");
/*  86 */   private static final Pattern PAT_SCH = Pattern.compile("^SCH");
/*  87 */   private static final Pattern PAT_EE_IE = Pattern.compile("(EE|IE)$");
/*  88 */   private static final Pattern PAT_DT_ETC = Pattern.compile("(DT|RT|RD|NT|ND)$");
/*     */ 
/*     */   
/*     */   private static final char SPACE = ' ';
/*     */ 
/*     */   
/*     */   private static final int TRUE_LENGTH = 6;
/*     */ 
/*     */   
/*     */   private final boolean strict;
/*     */ 
/*     */   
/*     */   private static boolean isVowel(char c) {
/* 101 */     return (c == 'A' || c == 'E' || c == 'I' || c == 'O' || c == 'U');
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
/*     */   private static char[] transcodeRemaining(char prev, char curr, char next, char aNext) {
/* 120 */     if (curr == 'E' && next == 'V') {
/* 121 */       return CHARS_AF;
/*     */     }
/*     */ 
/*     */     
/* 125 */     if (isVowel(curr)) {
/* 126 */       return CHARS_A;
/*     */     }
/*     */ 
/*     */     
/* 130 */     if (curr == 'Q')
/* 131 */       return CHARS_G; 
/* 132 */     if (curr == 'Z')
/* 133 */       return CHARS_S; 
/* 134 */     if (curr == 'M') {
/* 135 */       return CHARS_N;
/*     */     }
/*     */ 
/*     */     
/* 139 */     if (curr == 'K') {
/* 140 */       if (next == 'N') {
/* 141 */         return CHARS_NN;
/*     */       }
/* 143 */       return CHARS_C;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 148 */     if (curr == 'S' && next == 'C' && aNext == 'H') {
/* 149 */       return CHARS_SSS;
/*     */     }
/*     */ 
/*     */     
/* 153 */     if (curr == 'P' && next == 'H') {
/* 154 */       return CHARS_FF;
/*     */     }
/*     */ 
/*     */     
/* 158 */     if (curr == 'H' && (!isVowel(prev) || !isVowel(next))) {
/* 159 */       return new char[] { prev };
/*     */     }
/*     */ 
/*     */     
/* 163 */     if (curr == 'W' && isVowel(prev)) {
/* 164 */       return new char[] { prev };
/*     */     }
/*     */     
/* 167 */     return new char[] { curr };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Nysiis() {
/* 178 */     this(true);
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
/*     */   public Nysiis(boolean strict) {
/* 193 */     this.strict = strict;
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
/*     */   public Object encode(Object obj) throws EncoderException {
/* 211 */     if (!(obj instanceof String)) {
/* 212 */       throw new EncoderException("Parameter supplied to Nysiis encode is not of type java.lang.String");
/*     */     }
/* 214 */     return nysiis((String)obj);
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
/*     */   public String encode(String str) {
/* 228 */     return nysiis(str);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isStrict() {
/* 237 */     return this.strict;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String nysiis(String str) {
/* 248 */     if (str == null) {
/* 249 */       return null;
/*     */     }
/*     */ 
/*     */     
/* 253 */     str = SoundexUtils.clean(str);
/*     */     
/* 255 */     if (str.length() == 0) {
/* 256 */       return str;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 261 */     str = PAT_MAC.matcher(str).replaceFirst("MCC");
/* 262 */     str = PAT_KN.matcher(str).replaceFirst("NN");
/* 263 */     str = PAT_K.matcher(str).replaceFirst("C");
/* 264 */     str = PAT_PH_PF.matcher(str).replaceFirst("FF");
/* 265 */     str = PAT_SCH.matcher(str).replaceFirst("SSS");
/*     */ 
/*     */ 
/*     */     
/* 269 */     str = PAT_EE_IE.matcher(str).replaceFirst("Y");
/* 270 */     str = PAT_DT_ETC.matcher(str).replaceFirst("D");
/*     */ 
/*     */     
/* 273 */     StringBuilder key = new StringBuilder(str.length());
/* 274 */     key.append(str.charAt(0));
/*     */ 
/*     */     
/* 277 */     char[] chars = str.toCharArray();
/* 278 */     int len = chars.length;
/*     */     
/* 280 */     for (int i = 1; i < len; i++) {
/* 281 */       char next = (i < len - 1) ? chars[i + 1] : ' ';
/* 282 */       char aNext = (i < len - 2) ? chars[i + 2] : ' ';
/* 283 */       char[] transcoded = transcodeRemaining(chars[i - 1], chars[i], next, aNext);
/* 284 */       System.arraycopy(transcoded, 0, chars, i, transcoded.length);
/*     */ 
/*     */       
/* 287 */       if (chars[i] != chars[i - 1]) {
/* 288 */         key.append(chars[i]);
/*     */       }
/*     */     } 
/*     */     
/* 292 */     if (key.length() > 1) {
/* 293 */       char lastChar = key.charAt(key.length() - 1);
/*     */ 
/*     */       
/* 296 */       if (lastChar == 'S') {
/* 297 */         key.deleteCharAt(key.length() - 1);
/* 298 */         lastChar = key.charAt(key.length() - 1);
/*     */       } 
/*     */       
/* 301 */       if (key.length() > 2) {
/* 302 */         char last2Char = key.charAt(key.length() - 2);
/*     */         
/* 304 */         if (last2Char == 'A' && lastChar == 'Y') {
/* 305 */           key.deleteCharAt(key.length() - 2);
/*     */         }
/*     */       } 
/*     */ 
/*     */       
/* 310 */       if (lastChar == 'A') {
/* 311 */         key.deleteCharAt(key.length() - 1);
/*     */       }
/*     */     } 
/*     */     
/* 315 */     String string = key.toString();
/* 316 */     return isStrict() ? string.substring(0, Math.min(6, string.length())) : string;
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\commons\codec\language\Nysiis.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */