/*     */ package com.google.common.escape;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public abstract class UnicodeEscaper
/*     */   extends Escaper
/*     */ {
/*     */   private static final int DEST_PAD = 32;
/*     */   
/*     */   protected abstract char[] escape(int paramInt);
/*     */   
/*     */   protected int nextEscapeIndex(CharSequence csq, int start, int end) {
/* 117 */     int index = start;
/* 118 */     while (index < end) {
/* 119 */       int cp = codePointAt(csq, index, end);
/* 120 */       if (cp < 0 || escape(cp) != null) {
/*     */         break;
/*     */       }
/* 123 */       index += Character.isSupplementaryCodePoint(cp) ? 2 : 1;
/*     */     } 
/* 125 */     return index;
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
/*     */   public String escape(String string) {
/* 153 */     Preconditions.checkNotNull(string);
/* 154 */     int end = string.length();
/* 155 */     int index = nextEscapeIndex(string, 0, end);
/* 156 */     return (index == end) ? string : escapeSlow(string, index);
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
/*     */   protected final String escapeSlow(String s, int index) {
/* 177 */     int end = s.length();
/*     */ 
/*     */     
/* 180 */     char[] dest = Platform.charBufferFromThreadLocal();
/* 181 */     int destIndex = 0;
/* 182 */     int unescapedChunkStart = 0;
/*     */     
/* 184 */     while (index < end) {
/* 185 */       int cp = codePointAt(s, index, end);
/* 186 */       if (cp < 0) {
/* 187 */         throw new IllegalArgumentException("Trailing high surrogate at end of input");
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 193 */       char[] escaped = escape(cp);
/* 194 */       int nextIndex = index + (Character.isSupplementaryCodePoint(cp) ? 2 : 1);
/* 195 */       if (escaped != null) {
/* 196 */         int i = index - unescapedChunkStart;
/*     */ 
/*     */ 
/*     */         
/* 200 */         int sizeNeeded = destIndex + i + escaped.length;
/* 201 */         if (dest.length < sizeNeeded) {
/* 202 */           int destLength = sizeNeeded + end - index + 32;
/* 203 */           dest = growBuffer(dest, destIndex, destLength);
/*     */         } 
/*     */         
/* 206 */         if (i > 0) {
/* 207 */           s.getChars(unescapedChunkStart, index, dest, destIndex);
/* 208 */           destIndex += i;
/*     */         } 
/* 210 */         if (escaped.length > 0) {
/* 211 */           System.arraycopy(escaped, 0, dest, destIndex, escaped.length);
/* 212 */           destIndex += escaped.length;
/*     */         } 
/*     */         
/* 215 */         unescapedChunkStart = nextIndex;
/*     */       } 
/* 217 */       index = nextEscapeIndex(s, nextIndex, end);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 222 */     int charsSkipped = end - unescapedChunkStart;
/* 223 */     if (charsSkipped > 0) {
/* 224 */       int endIndex = destIndex + charsSkipped;
/* 225 */       if (dest.length < endIndex) {
/* 226 */         dest = growBuffer(dest, destIndex, endIndex);
/*     */       }
/* 228 */       s.getChars(unescapedChunkStart, end, dest, destIndex);
/* 229 */       destIndex = endIndex;
/*     */     } 
/* 231 */     return new String(dest, 0, destIndex);
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
/*     */   protected static int codePointAt(CharSequence seq, int index, int end) {
/* 267 */     Preconditions.checkNotNull(seq);
/* 268 */     if (index < end) {
/* 269 */       char c1 = seq.charAt(index++);
/* 270 */       if (c1 < '?' || c1 > '?')
/*     */       {
/*     */         
/* 273 */         return c1; } 
/* 274 */       if (c1 <= '?') {
/*     */         
/* 276 */         if (index == end) {
/* 277 */           return -c1;
/*     */         }
/*     */         
/* 280 */         char c = seq.charAt(index);
/* 281 */         if (Character.isLowSurrogate(c)) {
/* 282 */           return Character.toCodePoint(c1, c);
/*     */         }
/* 284 */         char c4 = c, c5 = c; int j = index; String str1 = String.valueOf(String.valueOf(seq)); throw new IllegalArgumentException((new StringBuilder(89 + str1.length())).append("Expected low surrogate but got char '").append(c4).append("' with value ").append(c5).append(" at index ").append(j).append(" in '").append(str1).append("'").toString());
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 289 */       char c2 = c1, c3 = c1; int i = index - 1; String str = String.valueOf(String.valueOf(seq)); throw new IllegalArgumentException((new StringBuilder(88 + str.length())).append("Unexpected low surrogate character '").append(c2).append("' with value ").append(c3).append(" at index ").append(i).append(" in '").append(str).append("'").toString());
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 295 */     throw new IndexOutOfBoundsException("Index exceeds specified range");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static char[] growBuffer(char[] dest, int index, int size) {
/* 304 */     char[] copy = new char[size];
/* 305 */     if (index > 0) {
/* 306 */       System.arraycopy(dest, 0, copy, 0, index);
/*     */     }
/* 308 */     return copy;
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\escape\UnicodeEscaper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */