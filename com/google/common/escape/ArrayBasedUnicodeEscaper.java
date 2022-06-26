/*     */ package com.google.common.escape;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.util.Map;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public abstract class ArrayBasedUnicodeEscaper
/*     */   extends UnicodeEscaper
/*     */ {
/*     */   private final char[][] replacements;
/*     */   private final int replacementsLength;
/*     */   private final int safeMin;
/*     */   private final int safeMax;
/*     */   private final char safeMinChar;
/*     */   private final char safeMaxChar;
/*     */   
/*     */   protected ArrayBasedUnicodeEscaper(Map<Character, String> replacementMap, int safeMin, int safeMax, @Nullable String unsafeReplacement) {
/*  83 */     this(ArrayBasedEscaperMap.create(replacementMap), safeMin, safeMax, unsafeReplacement);
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
/*     */   protected ArrayBasedUnicodeEscaper(ArrayBasedEscaperMap escaperMap, int safeMin, int safeMax, @Nullable String unsafeReplacement) {
/* 107 */     Preconditions.checkNotNull(escaperMap);
/* 108 */     this.replacements = escaperMap.getReplacementArray();
/* 109 */     this.replacementsLength = this.replacements.length;
/* 110 */     if (safeMax < safeMin) {
/*     */ 
/*     */       
/* 113 */       safeMax = -1;
/* 114 */       safeMin = Integer.MAX_VALUE;
/*     */     } 
/* 116 */     this.safeMin = safeMin;
/* 117 */     this.safeMax = safeMax;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 132 */     if (safeMin >= 55296) {
/*     */ 
/*     */       
/* 135 */       this.safeMinChar = Character.MAX_VALUE;
/* 136 */       this.safeMaxChar = Character.MIN_VALUE;
/*     */     }
/*     */     else {
/*     */       
/* 140 */       this.safeMinChar = (char)safeMin;
/* 141 */       this.safeMaxChar = (char)Math.min(safeMax, 55295);
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
/*     */   public final String escape(String s) {
/* 153 */     Preconditions.checkNotNull(s);
/* 154 */     for (int i = 0; i < s.length(); i++) {
/* 155 */       char c = s.charAt(i);
/* 156 */       if ((c < this.replacementsLength && this.replacements[c] != null) || c > this.safeMaxChar || c < this.safeMinChar)
/*     */       {
/* 158 */         return escapeSlow(s, i);
/*     */       }
/*     */     } 
/* 161 */     return s;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected final int nextEscapeIndex(CharSequence csq, int index, int end) {
/* 167 */     while (index < end) {
/* 168 */       char c = csq.charAt(index);
/* 169 */       if ((c < this.replacementsLength && this.replacements[c] != null) || c > this.safeMaxChar || c < this.safeMinChar) {
/*     */         break;
/*     */       }
/*     */       
/* 173 */       index++;
/*     */     } 
/* 175 */     return index;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final char[] escape(int cp) {
/* 185 */     if (cp < this.replacementsLength) {
/* 186 */       char[] chars = this.replacements[cp];
/* 187 */       if (chars != null) {
/* 188 */         return chars;
/*     */       }
/*     */     } 
/* 191 */     if (cp >= this.safeMin && cp <= this.safeMax) {
/* 192 */       return null;
/*     */     }
/* 194 */     return escapeUnsafe(cp);
/*     */   }
/*     */   
/*     */   protected abstract char[] escapeUnsafe(int paramInt);
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\escape\ArrayBasedUnicodeEscaper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */