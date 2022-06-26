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
/*     */ public class PrefixFileFilter
/*     */   extends AbstractFileFilter
/*     */   implements Serializable
/*     */ {
/*     */   private final String[] prefixes;
/*     */   private final IOCase caseSensitivity;
/*     */   
/*     */   public PrefixFileFilter(String prefix) {
/*  59 */     this(prefix, IOCase.SENSITIVE);
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
/*     */   public PrefixFileFilter(String prefix, IOCase caseSensitivity) {
/*  72 */     if (prefix == null) {
/*  73 */       throw new IllegalArgumentException("The prefix must not be null");
/*     */     }
/*  75 */     this.prefixes = new String[] { prefix };
/*  76 */     this.caseSensitivity = (caseSensitivity == null) ? IOCase.SENSITIVE : caseSensitivity;
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
/*     */   public PrefixFileFilter(String[] prefixes) {
/*  89 */     this(prefixes, IOCase.SENSITIVE);
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
/*     */   public PrefixFileFilter(String[] prefixes, IOCase caseSensitivity) {
/* 105 */     if (prefixes == null) {
/* 106 */       throw new IllegalArgumentException("The array of prefixes must not be null");
/*     */     }
/* 108 */     this.prefixes = new String[prefixes.length];
/* 109 */     System.arraycopy(prefixes, 0, this.prefixes, 0, prefixes.length);
/* 110 */     this.caseSensitivity = (caseSensitivity == null) ? IOCase.SENSITIVE : caseSensitivity;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PrefixFileFilter(List<String> prefixes) {
/* 121 */     this(prefixes, IOCase.SENSITIVE);
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
/*     */   public PrefixFileFilter(List<String> prefixes, IOCase caseSensitivity) {
/* 135 */     if (prefixes == null) {
/* 136 */       throw new IllegalArgumentException("The list of prefixes must not be null");
/*     */     }
/* 138 */     this.prefixes = prefixes.<String>toArray(new String[prefixes.size()]);
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
/*     */   public boolean accept(File file) {
/* 150 */     String name = file.getName();
/* 151 */     for (String prefix : this.prefixes) {
/* 152 */       if (this.caseSensitivity.checkStartsWith(name, prefix)) {
/* 153 */         return true;
/*     */       }
/*     */     } 
/* 156 */     return false;
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
/* 168 */     for (String prefix : this.prefixes) {
/* 169 */       if (this.caseSensitivity.checkStartsWith(name, prefix)) {
/* 170 */         return true;
/*     */       }
/*     */     } 
/* 173 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 183 */     StringBuilder buffer = new StringBuilder();
/* 184 */     buffer.append(super.toString());
/* 185 */     buffer.append("(");
/* 186 */     if (this.prefixes != null) {
/* 187 */       for (int i = 0; i < this.prefixes.length; i++) {
/* 188 */         if (i > 0) {
/* 189 */           buffer.append(",");
/*     */         }
/* 191 */         buffer.append(this.prefixes[i]);
/*     */       } 
/*     */     }
/* 194 */     buffer.append(")");
/* 195 */     return buffer.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\commons\io\filefilter\PrefixFileFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */