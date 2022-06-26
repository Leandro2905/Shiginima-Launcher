/*     */ package org.apache.logging.log4j.core.pattern;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class NameAbbreviator
/*     */ {
/*  30 */   private static final NameAbbreviator DEFAULT = new NOPAbbreviator();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static NameAbbreviator getAbbreviator(String pattern) {
/*  44 */     if (pattern.length() > 0) {
/*     */ 
/*     */       
/*  47 */       String trimmed = pattern.trim();
/*     */       
/*  49 */       if (trimmed.isEmpty()) {
/*  50 */         return DEFAULT;
/*     */       }
/*     */       
/*  53 */       int i = 0;
/*     */       
/*  55 */       while (i < trimmed.length() && trimmed.charAt(i) >= '0' && trimmed.charAt(i) <= '9')
/*     */       {
/*  57 */         i++;
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  63 */       if (i == trimmed.length()) {
/*  64 */         return new MaxElementAbbreviator(Integer.parseInt(trimmed));
/*     */       }
/*     */       
/*  67 */       ArrayList<PatternAbbreviatorFragment> fragments = new ArrayList<PatternAbbreviatorFragment>(5);
/*     */ 
/*     */       
/*  70 */       int pos = 0;
/*     */       
/*  72 */       while (pos < trimmed.length() && pos >= 0) {
/*  73 */         int charCount, ellipsisPos = pos;
/*     */         
/*  75 */         if (trimmed.charAt(pos) == '*') {
/*  76 */           charCount = Integer.MAX_VALUE;
/*  77 */           ellipsisPos++;
/*     */         }
/*  79 */         else if (trimmed.charAt(pos) >= '0' && trimmed.charAt(pos) <= '9') {
/*  80 */           charCount = trimmed.charAt(pos) - 48;
/*  81 */           ellipsisPos++;
/*     */         } else {
/*  83 */           charCount = 0;
/*     */         } 
/*     */ 
/*     */         
/*  87 */         char ellipsis = Character.MIN_VALUE;
/*     */         
/*  89 */         if (ellipsisPos < trimmed.length()) {
/*  90 */           ellipsis = trimmed.charAt(ellipsisPos);
/*     */           
/*  92 */           if (ellipsis == '.') {
/*  93 */             ellipsis = Character.MIN_VALUE;
/*     */           }
/*     */         } 
/*     */         
/*  97 */         fragments.add(new PatternAbbreviatorFragment(charCount, ellipsis));
/*  98 */         pos = trimmed.indexOf('.', pos);
/*     */         
/* 100 */         if (pos == -1) {
/*     */           break;
/*     */         }
/*     */         
/* 104 */         pos++;
/*     */       } 
/*     */       
/* 107 */       return new PatternAbbreviator(fragments);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 113 */     return DEFAULT;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static NameAbbreviator getDefaultAbbreviator() {
/* 122 */     return DEFAULT;
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
/*     */   public abstract String abbreviate(String paramString);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class NOPAbbreviator
/*     */     extends NameAbbreviator
/*     */   {
/*     */     public String abbreviate(String buf) {
/* 148 */       return buf;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class MaxElementAbbreviator
/*     */     extends NameAbbreviator
/*     */   {
/*     */     private final int count;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public MaxElementAbbreviator(int count) {
/* 167 */       this.count = (count < 1) ? 1 : count;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String abbreviate(String buf) {
/* 182 */       int end = buf.length() - 1;
/*     */       
/* 184 */       for (int i = this.count; i > 0; i--) {
/* 185 */         end = buf.lastIndexOf('.', end - 1);
/* 186 */         if (end == -1) {
/* 187 */           return buf;
/*     */         }
/*     */       } 
/*     */       
/* 191 */       return buf.substring(end + 1);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class PatternAbbreviatorFragment
/*     */   {
/*     */     private final int charCount;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final char ellipsis;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public PatternAbbreviatorFragment(int charCount, char ellipsis) {
/* 219 */       this.charCount = charCount;
/* 220 */       this.ellipsis = ellipsis;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int abbreviate(StringBuilder buf, int startPos) {
/* 231 */       int nextDot = buf.toString().indexOf('.', startPos);
/*     */       
/* 233 */       if (nextDot != -1) {
/* 234 */         if (nextDot - startPos > this.charCount) {
/* 235 */           buf.delete(startPos + this.charCount, nextDot);
/* 236 */           nextDot = startPos + this.charCount;
/*     */           
/* 238 */           if (this.ellipsis != '\000') {
/* 239 */             buf.insert(nextDot, this.ellipsis);
/* 240 */             nextDot++;
/*     */           } 
/*     */         } 
/*     */         
/* 244 */         nextDot++;
/*     */       } 
/*     */       
/* 247 */       return nextDot;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class PatternAbbreviator
/*     */     extends NameAbbreviator
/*     */   {
/*     */     private final NameAbbreviator.PatternAbbreviatorFragment[] fragments;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public PatternAbbreviator(List<NameAbbreviator.PatternAbbreviatorFragment> fragments) {
/* 266 */       if (fragments.isEmpty()) {
/* 267 */         throw new IllegalArgumentException("fragments must have at least one element");
/*     */       }
/*     */ 
/*     */       
/* 271 */       this.fragments = new NameAbbreviator.PatternAbbreviatorFragment[fragments.size()];
/* 272 */       fragments.toArray(this.fragments);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String abbreviate(String buf) {
/* 285 */       int pos = 0;
/* 286 */       StringBuilder sb = new StringBuilder(buf);
/*     */       
/* 288 */       for (int i = 0; i < this.fragments.length - 1 && pos < buf.length(); 
/* 289 */         i++) {
/* 290 */         pos = this.fragments[i].abbreviate(sb, pos);
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 296 */       NameAbbreviator.PatternAbbreviatorFragment terminalFragment = this.fragments[this.fragments.length - 1];
/*     */       
/* 298 */       while (pos < buf.length() && pos >= 0) {
/* 299 */         pos = terminalFragment.abbreviate(sb, pos);
/*     */       }
/* 301 */       return sb.toString();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\pattern\NameAbbreviator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */