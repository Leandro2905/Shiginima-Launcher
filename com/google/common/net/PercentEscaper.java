/*     */ package com.google.common.net;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.escape.UnicodeEscaper;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Beta
/*     */ @GwtCompatible
/*     */ public final class PercentEscaper
/*     */   extends UnicodeEscaper
/*     */ {
/*  62 */   private static final char[] PLUS_SIGN = new char[] { '+' };
/*     */ 
/*     */   
/*  65 */   private static final char[] UPPER_HEX_DIGITS = "0123456789ABCDEF".toCharArray();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final boolean plusForSpace;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final boolean[] safeOctets;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PercentEscaper(String safeChars, boolean plusForSpace) {
/*  98 */     Preconditions.checkNotNull(safeChars);
/*     */     
/* 100 */     if (safeChars.matches(".*[0-9A-Za-z].*")) {
/* 101 */       throw new IllegalArgumentException("Alphanumeric characters are always 'safe' and should not be explicitly specified");
/*     */     }
/*     */ 
/*     */     
/* 105 */     safeChars = String.valueOf(safeChars).concat("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789");
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 110 */     if (plusForSpace && safeChars.contains(" ")) {
/* 111 */       throw new IllegalArgumentException("plusForSpace cannot be specified when space is a 'safe' character");
/*     */     }
/*     */     
/* 114 */     this.plusForSpace = plusForSpace;
/* 115 */     this.safeOctets = createSafeOctets(safeChars);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean[] createSafeOctets(String safeChars) {
/* 124 */     int maxChar = -1;
/* 125 */     char[] safeCharArray = safeChars.toCharArray();
/* 126 */     for (char c : safeCharArray) {
/* 127 */       maxChar = Math.max(c, maxChar);
/*     */     }
/* 129 */     boolean[] octets = new boolean[maxChar + 1];
/* 130 */     for (char c : safeCharArray) {
/* 131 */       octets[c] = true;
/*     */     }
/* 133 */     return octets;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int nextEscapeIndex(CharSequence csq, int index, int end) {
/* 143 */     Preconditions.checkNotNull(csq);
/* 144 */     for (; index < end; index++) {
/* 145 */       char c = csq.charAt(index);
/* 146 */       if (c >= this.safeOctets.length || !this.safeOctets[c]) {
/*     */         break;
/*     */       }
/*     */     } 
/* 150 */     return index;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String escape(String s) {
/* 160 */     Preconditions.checkNotNull(s);
/* 161 */     int slen = s.length();
/* 162 */     for (int index = 0; index < slen; index++) {
/* 163 */       char c = s.charAt(index);
/* 164 */       if (c >= this.safeOctets.length || !this.safeOctets[c]) {
/* 165 */         return escapeSlow(s, index);
/*     */       }
/*     */     } 
/* 168 */     return s;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected char[] escape(int cp) {
/* 178 */     if (cp < this.safeOctets.length && this.safeOctets[cp])
/* 179 */       return null; 
/* 180 */     if (cp == 32 && this.plusForSpace)
/* 181 */       return PLUS_SIGN; 
/* 182 */     if (cp <= 127) {
/*     */ 
/*     */       
/* 185 */       char[] dest = new char[3];
/* 186 */       dest[0] = '%';
/* 187 */       dest[2] = UPPER_HEX_DIGITS[cp & 0xF];
/* 188 */       dest[1] = UPPER_HEX_DIGITS[cp >>> 4];
/* 189 */       return dest;
/* 190 */     }  if (cp <= 2047) {
/*     */ 
/*     */       
/* 193 */       char[] dest = new char[6];
/* 194 */       dest[0] = '%';
/* 195 */       dest[3] = '%';
/* 196 */       dest[5] = UPPER_HEX_DIGITS[cp & 0xF];
/* 197 */       cp >>>= 4;
/* 198 */       dest[4] = UPPER_HEX_DIGITS[0x8 | cp & 0x3];
/* 199 */       cp >>>= 2;
/* 200 */       dest[2] = UPPER_HEX_DIGITS[cp & 0xF];
/* 201 */       cp >>>= 4;
/* 202 */       dest[1] = UPPER_HEX_DIGITS[0xC | cp];
/* 203 */       return dest;
/* 204 */     }  if (cp <= 65535) {
/*     */ 
/*     */       
/* 207 */       char[] dest = new char[9];
/* 208 */       dest[0] = '%';
/* 209 */       dest[1] = 'E';
/* 210 */       dest[3] = '%';
/* 211 */       dest[6] = '%';
/* 212 */       dest[8] = UPPER_HEX_DIGITS[cp & 0xF];
/* 213 */       cp >>>= 4;
/* 214 */       dest[7] = UPPER_HEX_DIGITS[0x8 | cp & 0x3];
/* 215 */       cp >>>= 2;
/* 216 */       dest[5] = UPPER_HEX_DIGITS[cp & 0xF];
/* 217 */       cp >>>= 4;
/* 218 */       dest[4] = UPPER_HEX_DIGITS[0x8 | cp & 0x3];
/* 219 */       cp >>>= 2;
/* 220 */       dest[2] = UPPER_HEX_DIGITS[cp];
/* 221 */       return dest;
/* 222 */     }  if (cp <= 1114111) {
/* 223 */       char[] dest = new char[12];
/*     */ 
/*     */       
/* 226 */       dest[0] = '%';
/* 227 */       dest[1] = 'F';
/* 228 */       dest[3] = '%';
/* 229 */       dest[6] = '%';
/* 230 */       dest[9] = '%';
/* 231 */       dest[11] = UPPER_HEX_DIGITS[cp & 0xF];
/* 232 */       cp >>>= 4;
/* 233 */       dest[10] = UPPER_HEX_DIGITS[0x8 | cp & 0x3];
/* 234 */       cp >>>= 2;
/* 235 */       dest[8] = UPPER_HEX_DIGITS[cp & 0xF];
/* 236 */       cp >>>= 4;
/* 237 */       dest[7] = UPPER_HEX_DIGITS[0x8 | cp & 0x3];
/* 238 */       cp >>>= 2;
/* 239 */       dest[5] = UPPER_HEX_DIGITS[cp & 0xF];
/* 240 */       cp >>>= 4;
/* 241 */       dest[4] = UPPER_HEX_DIGITS[0x8 | cp & 0x3];
/* 242 */       cp >>>= 2;
/* 243 */       dest[2] = UPPER_HEX_DIGITS[cp & 0x7];
/* 244 */       return dest;
/*     */     } 
/*     */     
/* 247 */     int i = cp; throw new IllegalArgumentException((new StringBuilder(43)).append("Invalid unicode character value ").append(i).toString());
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\net\PercentEscaper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */