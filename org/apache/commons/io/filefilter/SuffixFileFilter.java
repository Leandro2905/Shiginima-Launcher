/*     */ package org.apache.commons.io.filefilter;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.Serializable;
/*     */ import java.util.List;
/*     */ import org.apache.commons.io.IOCase;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SuffixFileFilter
/*     */   extends AbstractFileFilter
/*     */   implements Serializable
/*     */ {
/*     */   private final String[] suffixes;
/*     */   private final IOCase caseSensitivity;
/*     */   
/*     */   public SuffixFileFilter(String suffix) {
/*  60 */     this(suffix, IOCase.SENSITIVE);
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
/*     */   public SuffixFileFilter(String suffix, IOCase caseSensitivity) {
/*  73 */     if (suffix == null) {
/*  74 */       throw new IllegalArgumentException("The suffix must not be null");
/*     */     }
/*  76 */     this.suffixes = new String[] { suffix };
/*  77 */     this.caseSensitivity = (caseSensitivity == null) ? IOCase.SENSITIVE : caseSensitivity;
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
/*     */   public SuffixFileFilter(String[] suffixes) {
/*  90 */     this(suffixes, IOCase.SENSITIVE);
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
/*     */   public SuffixFileFilter(String[] suffixes, IOCase caseSensitivity) {
/* 106 */     if (suffixes == null) {
/* 107 */       throw new IllegalArgumentException("The array of suffixes must not be null");
/*     */     }
/* 109 */     this.suffixes = new String[suffixes.length];
/* 110 */     System.arraycopy(suffixes, 0, this.suffixes, 0, suffixes.length);
/* 111 */     this.caseSensitivity = (caseSensitivity == null) ? IOCase.SENSITIVE : caseSensitivity;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SuffixFileFilter(List<String> suffixes) {
/* 122 */     this(suffixes, IOCase.SENSITIVE);
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
/*     */   public SuffixFileFilter(List<String> suffixes, IOCase caseSensitivity) {
/* 136 */     if (suffixes == null) {
/* 137 */       throw new IllegalArgumentException("The list of suffixes must not be null");
/*     */     }
/* 139 */     this.suffixes = suffixes.<String>toArray(new String[suffixes.size()]);
/* 140 */     this.caseSensitivity = (caseSensitivity == null) ? IOCase.SENSITIVE : caseSensitivity;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean accept(File file) {
/* 151 */     String name = file.getName();
/* 152 */     for (String suffix : this.suffixes) {
/* 153 */       if (this.caseSensitivity.checkEndsWith(name, suffix)) {
/* 154 */         return true;
/*     */       }
/*     */     } 
/* 157 */     return false;
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
/*     */   public boolean accept(File file, String name) {
/* 169 */     for (String suffix : this.suffixes) {
/* 170 */       if (this.caseSensitivity.checkEndsWith(name, suffix)) {
/* 171 */         return true;
/*     */       }
/*     */     } 
/* 174 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 184 */     StringBuilder buffer = new StringBuilder();
/* 185 */     buffer.append(super.toString());
/* 186 */     buffer.append("(");
/* 187 */     if (this.suffixes != null) {
/* 188 */       for (int i = 0; i < this.suffixes.length; i++) {
/* 189 */         if (i > 0) {
/* 190 */           buffer.append(",");
/*     */         }
/* 192 */         buffer.append(this.suffixes[i]);
/*     */       } 
/*     */     }
/* 195 */     buffer.append(")");
/* 196 */     return buffer.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\commons\io\filefilter\SuffixFileFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */