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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ColognePhonetic
/*     */   implements StringEncoder
/*     */ {
/* 185 */   private static final char[] AEIJOUY = new char[] { 'A', 'E', 'I', 'J', 'O', 'U', 'Y' };
/* 186 */   private static final char[] SCZ = new char[] { 'S', 'C', 'Z' };
/* 187 */   private static final char[] WFPV = new char[] { 'W', 'F', 'P', 'V' };
/* 188 */   private static final char[] GKQ = new char[] { 'G', 'K', 'Q' };
/* 189 */   private static final char[] CKQ = new char[] { 'C', 'K', 'Q' };
/* 190 */   private static final char[] AHKLOQRUX = new char[] { 'A', 'H', 'K', 'L', 'O', 'Q', 'R', 'U', 'X' };
/* 191 */   private static final char[] SZ = new char[] { 'S', 'Z' };
/* 192 */   private static final char[] AHOUKQX = new char[] { 'A', 'H', 'O', 'U', 'K', 'Q', 'X' };
/* 193 */   private static final char[] TDX = new char[] { 'T', 'D', 'X' };
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private abstract class CologneBuffer
/*     */   {
/*     */     protected final char[] data;
/*     */ 
/*     */ 
/*     */     
/* 204 */     protected int length = 0;
/*     */     
/*     */     public CologneBuffer(char[] data) {
/* 207 */       this.data = data;
/* 208 */       this.length = data.length;
/*     */     }
/*     */     
/*     */     public CologneBuffer(int buffSize) {
/* 212 */       this.data = new char[buffSize];
/* 213 */       this.length = 0;
/*     */     }
/*     */     
/*     */     protected abstract char[] copyData(int param1Int1, int param1Int2);
/*     */     
/*     */     public int length() {
/* 219 */       return this.length;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 224 */       return new String(copyData(0, this.length));
/*     */     }
/*     */   }
/*     */   
/*     */   private class CologneOutputBuffer
/*     */     extends CologneBuffer {
/*     */     public CologneOutputBuffer(int buffSize) {
/* 231 */       super(buffSize);
/*     */     }
/*     */     
/*     */     public void addRight(char chr) {
/* 235 */       this.data[this.length] = chr;
/* 236 */       this.length++;
/*     */     }
/*     */ 
/*     */     
/*     */     protected char[] copyData(int start, int length) {
/* 241 */       char[] newData = new char[length];
/* 242 */       System.arraycopy(this.data, start, newData, 0, length);
/* 243 */       return newData;
/*     */     }
/*     */   }
/*     */   
/*     */   private class CologneInputBuffer
/*     */     extends CologneBuffer {
/*     */     public CologneInputBuffer(char[] data) {
/* 250 */       super(data);
/*     */     }
/*     */     
/*     */     public void addLeft(char ch) {
/* 254 */       this.length++;
/* 255 */       this.data[getNextPos()] = ch;
/*     */     }
/*     */ 
/*     */     
/*     */     protected char[] copyData(int start, int length) {
/* 260 */       char[] newData = new char[length];
/* 261 */       System.arraycopy(this.data, this.data.length - this.length + start, newData, 0, length);
/* 262 */       return newData;
/*     */     }
/*     */     
/*     */     public char getNextChar() {
/* 266 */       return this.data[getNextPos()];
/*     */     }
/*     */     
/*     */     protected int getNextPos() {
/* 270 */       return this.data.length - this.length;
/*     */     }
/*     */     
/*     */     public char removeNext() {
/* 274 */       char ch = getNextChar();
/* 275 */       this.length--;
/* 276 */       return ch;
/*     */     }
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
/* 289 */   private static final char[][] PREPROCESS_MAP = new char[][] { { 'Ä', 'A' }, { 'Ü', 'U' }, { 'Ö', 'O' }, { 'ß', 'S' } };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean arrayContains(char[] arr, char key) {
/* 300 */     for (char element : arr) {
/* 301 */       if (element == key) {
/* 302 */         return true;
/*     */       }
/*     */     } 
/* 305 */     return false;
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
/*     */   public String colognePhonetic(String text) {
/* 320 */     if (text == null) {
/* 321 */       return null;
/*     */     }
/*     */     
/* 324 */     text = preprocess(text);
/*     */     
/* 326 */     CologneOutputBuffer output = new CologneOutputBuffer(text.length() * 2);
/* 327 */     CologneInputBuffer input = new CologneInputBuffer(text.toCharArray());
/*     */ 
/*     */ 
/*     */     
/* 331 */     char lastChar = '-';
/* 332 */     char lastCode = '/';
/*     */ 
/*     */ 
/*     */     
/* 336 */     int rightLength = input.length();
/*     */     
/* 338 */     while (rightLength > 0) {
/* 339 */       char nextChar, code, chr = input.removeNext();
/*     */       
/* 341 */       if ((rightLength = input.length()) > 0) {
/* 342 */         nextChar = input.getNextChar();
/*     */       } else {
/* 344 */         nextChar = '-';
/*     */       } 
/*     */       
/* 347 */       if (arrayContains(AEIJOUY, chr)) {
/* 348 */         code = '0';
/* 349 */       } else if (chr == 'H' || chr < 'A' || chr > 'Z') {
/* 350 */         if (lastCode == '/') {
/*     */           continue;
/*     */         }
/* 353 */         code = '-';
/* 354 */       } else if (chr == 'B' || (chr == 'P' && nextChar != 'H')) {
/* 355 */         code = '1';
/* 356 */       } else if ((chr == 'D' || chr == 'T') && !arrayContains(SCZ, nextChar)) {
/* 357 */         code = '2';
/* 358 */       } else if (arrayContains(WFPV, chr)) {
/* 359 */         code = '3';
/* 360 */       } else if (arrayContains(GKQ, chr)) {
/* 361 */         code = '4';
/* 362 */       } else if (chr == 'X' && !arrayContains(CKQ, lastChar)) {
/* 363 */         code = '4';
/* 364 */         input.addLeft('S');
/* 365 */         rightLength++;
/* 366 */       } else if (chr == 'S' || chr == 'Z') {
/* 367 */         code = '8';
/* 368 */       } else if (chr == 'C') {
/* 369 */         if (lastCode == '/') {
/* 370 */           if (arrayContains(AHKLOQRUX, nextChar)) {
/* 371 */             code = '4';
/*     */           } else {
/* 373 */             code = '8';
/*     */           }
/*     */         
/* 376 */         } else if (arrayContains(SZ, lastChar) || !arrayContains(AHOUKQX, nextChar)) {
/* 377 */           code = '8';
/*     */         } else {
/* 379 */           code = '4';
/*     */         }
/*     */       
/* 382 */       } else if (arrayContains(TDX, chr)) {
/* 383 */         code = '8';
/* 384 */       } else if (chr == 'R') {
/* 385 */         code = '7';
/* 386 */       } else if (chr == 'L') {
/* 387 */         code = '5';
/* 388 */       } else if (chr == 'M' || chr == 'N') {
/* 389 */         code = '6';
/*     */       } else {
/* 391 */         code = chr;
/*     */       } 
/*     */       
/* 394 */       if (code != '-' && ((lastCode != code && (code != '0' || lastCode == '/')) || code < '0' || code > '8')) {
/* 395 */         output.addRight(code);
/*     */       }
/*     */       
/* 398 */       lastChar = chr;
/* 399 */       lastCode = code;
/*     */     } 
/* 401 */     return output.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public Object encode(Object object) throws EncoderException {
/* 406 */     if (!(object instanceof String)) {
/* 407 */       throw new EncoderException("This method's parameter was expected to be of the type " + String.class.getName() + ". But actually it was of the type " + object.getClass().getName() + ".");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 413 */     return encode((String)object);
/*     */   }
/*     */ 
/*     */   
/*     */   public String encode(String text) {
/* 418 */     return colognePhonetic(text);
/*     */   }
/*     */   
/*     */   public boolean isEncodeEqual(String text1, String text2) {
/* 422 */     return colognePhonetic(text1).equals(colognePhonetic(text2));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String preprocess(String text) {
/* 429 */     text = text.toUpperCase(Locale.GERMAN);
/*     */     
/* 431 */     char[] chrs = text.toCharArray();
/*     */     
/* 433 */     for (int index = 0; index < chrs.length; index++) {
/* 434 */       if (chrs[index] > 'Z') {
/* 435 */         for (char[] element : PREPROCESS_MAP) {
/* 436 */           if (chrs[index] == element[0]) {
/* 437 */             chrs[index] = element[1];
/*     */             break;
/*     */           } 
/*     */         } 
/*     */       }
/*     */     } 
/* 443 */     return new String(chrs);
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\commons\codec\language\ColognePhonetic.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */