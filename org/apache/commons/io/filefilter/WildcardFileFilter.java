/*     */ package org.apache.commons.io.filefilter;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.Serializable;
/*     */ import java.util.List;
/*     */ import org.apache.commons.io.FilenameUtils;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class WildcardFileFilter
/*     */   extends AbstractFileFilter
/*     */   implements Serializable
/*     */ {
/*     */   private final String[] wildcards;
/*     */   private final IOCase caseSensitivity;
/*     */   
/*     */   public WildcardFileFilter(String wildcard) {
/*  65 */     this(wildcard, (IOCase)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public WildcardFileFilter(String wildcard, IOCase caseSensitivity) {
/*  76 */     if (wildcard == null) {
/*  77 */       throw new IllegalArgumentException("The wildcard must not be null");
/*     */     }
/*  79 */     this.wildcards = new String[] { wildcard };
/*  80 */     this.caseSensitivity = (caseSensitivity == null) ? IOCase.SENSITIVE : caseSensitivity;
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
/*     */   public WildcardFileFilter(String[] wildcards) {
/*  93 */     this(wildcards, (IOCase)null);
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
/*     */   public WildcardFileFilter(String[] wildcards, IOCase caseSensitivity) {
/* 107 */     if (wildcards == null) {
/* 108 */       throw new IllegalArgumentException("The wildcard array must not be null");
/*     */     }
/* 110 */     this.wildcards = new String[wildcards.length];
/* 111 */     System.arraycopy(wildcards, 0, this.wildcards, 0, wildcards.length);
/* 112 */     this.caseSensitivity = (caseSensitivity == null) ? IOCase.SENSITIVE : caseSensitivity;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public WildcardFileFilter(List<String> wildcards) {
/* 123 */     this(wildcards, (IOCase)null);
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
/*     */   public WildcardFileFilter(List<String> wildcards, IOCase caseSensitivity) {
/* 135 */     if (wildcards == null) {
/* 136 */       throw new IllegalArgumentException("The wildcard list must not be null");
/*     */     }
/* 138 */     this.wildcards = wildcards.<String>toArray(new String[wildcards.size()]);
/* 139 */     this.caseSensitivity = (caseSensitivity == null) ? IOCase.SENSITIVE : caseSensitivity;
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
/*     */   public boolean accept(File dir, String name) {
/* 152 */     for (String wildcard : this.wildcards) {
/* 153 */       if (FilenameUtils.wildcardMatch(name, wildcard, this.caseSensitivity)) {
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
/*     */   public boolean accept(File file) {
/* 168 */     String name = file.getName();
/* 169 */     for (String wildcard : this.wildcards) {
/* 170 */       if (FilenameUtils.wildcardMatch(name, wildcard, this.caseSensitivity)) {
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
/* 187 */     if (this.wildcards != null) {
/* 188 */       for (int i = 0; i < this.wildcards.length; i++) {
/* 189 */         if (i > 0) {
/* 190 */           buffer.append(",");
/*     */         }
/* 192 */         buffer.append(this.wildcards[i]);
/*     */       } 
/*     */     }
/* 195 */     buffer.append(")");
/* 196 */     return buffer.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\commons\io\filefilter\WildcardFileFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */