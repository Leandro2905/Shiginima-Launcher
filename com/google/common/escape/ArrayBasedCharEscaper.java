/*     */ package com.google.common.escape;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public abstract class ArrayBasedCharEscaper
/*     */   extends CharEscaper
/*     */ {
/*     */   private final char[][] replacements;
/*     */   private final int replacementsLength;
/*     */   private final char safeMin;
/*     */   private final char safeMax;
/*     */   
/*     */   protected ArrayBasedCharEscaper(Map<Character, String> replacementMap, char safeMin, char safeMax) {
/*  77 */     this(ArrayBasedEscaperMap.create(replacementMap), safeMin, safeMax);
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
/*     */   protected ArrayBasedCharEscaper(ArrayBasedEscaperMap escaperMap, char safeMin, char safeMax) {
/*  98 */     Preconditions.checkNotNull(escaperMap);
/*  99 */     this.replacements = escaperMap.getReplacementArray();
/* 100 */     this.replacementsLength = this.replacements.length;
/* 101 */     if (safeMax < safeMin) {
/*     */ 
/*     */       
/* 104 */       safeMax = Character.MIN_VALUE;
/* 105 */       safeMin = Character.MAX_VALUE;
/*     */     } 
/* 107 */     this.safeMin = safeMin;
/* 108 */     this.safeMax = safeMax;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String escape(String s) {
/* 118 */     Preconditions.checkNotNull(s);
/* 119 */     for (int i = 0; i < s.length(); i++) {
/* 120 */       char c = s.charAt(i);
/* 121 */       if ((c < this.replacementsLength && this.replacements[c] != null) || c > this.safeMax || c < this.safeMin)
/*     */       {
/* 123 */         return escapeSlow(s, i);
/*     */       }
/*     */     } 
/* 126 */     return s;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final char[] escape(char c) {
/* 135 */     if (c < this.replacementsLength) {
/* 136 */       char[] chars = this.replacements[c];
/* 137 */       if (chars != null) {
/* 138 */         return chars;
/*     */       }
/*     */     } 
/* 141 */     if (c >= this.safeMin && c <= this.safeMax) {
/* 142 */       return null;
/*     */     }
/* 144 */     return escapeUnsafe(c);
/*     */   }
/*     */   
/*     */   protected abstract char[] escapeUnsafe(char paramChar);
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\escape\ArrayBasedCharEscaper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */