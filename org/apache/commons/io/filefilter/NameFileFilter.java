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
/*     */ public class NameFileFilter
/*     */   extends AbstractFileFilter
/*     */   implements Serializable
/*     */ {
/*     */   private final String[] names;
/*     */   private final IOCase caseSensitivity;
/*     */   
/*     */   public NameFileFilter(String name) {
/*  58 */     this(name, (IOCase)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NameFileFilter(String name, IOCase caseSensitivity) {
/*  69 */     if (name == null) {
/*  70 */       throw new IllegalArgumentException("The wildcard must not be null");
/*     */     }
/*  72 */     this.names = new String[] { name };
/*  73 */     this.caseSensitivity = (caseSensitivity == null) ? IOCase.SENSITIVE : caseSensitivity;
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
/*     */   public NameFileFilter(String[] names) {
/*  86 */     this(names, (IOCase)null);
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
/*     */   public NameFileFilter(String[] names, IOCase caseSensitivity) {
/* 100 */     if (names == null) {
/* 101 */       throw new IllegalArgumentException("The array of names must not be null");
/*     */     }
/* 103 */     this.names = new String[names.length];
/* 104 */     System.arraycopy(names, 0, this.names, 0, names.length);
/* 105 */     this.caseSensitivity = (caseSensitivity == null) ? IOCase.SENSITIVE : caseSensitivity;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NameFileFilter(List<String> names) {
/* 116 */     this(names, (IOCase)null);
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
/*     */   public NameFileFilter(List<String> names, IOCase caseSensitivity) {
/* 128 */     if (names == null) {
/* 129 */       throw new IllegalArgumentException("The list of names must not be null");
/*     */     }
/* 131 */     this.names = names.<String>toArray(new String[names.size()]);
/* 132 */     this.caseSensitivity = (caseSensitivity == null) ? IOCase.SENSITIVE : caseSensitivity;
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
/*     */   public boolean accept(File file) {
/* 144 */     String name = file.getName();
/* 145 */     for (String name2 : this.names) {
/* 146 */       if (this.caseSensitivity.checkEquals(name, name2)) {
/* 147 */         return true;
/*     */       }
/*     */     } 
/* 150 */     return false;
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
/*     */   public boolean accept(File dir, String name) {
/* 162 */     for (String name2 : this.names) {
/* 163 */       if (this.caseSensitivity.checkEquals(name, name2)) {
/* 164 */         return true;
/*     */       }
/*     */     } 
/* 167 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 177 */     StringBuilder buffer = new StringBuilder();
/* 178 */     buffer.append(super.toString());
/* 179 */     buffer.append("(");
/* 180 */     if (this.names != null) {
/* 181 */       for (int i = 0; i < this.names.length; i++) {
/* 182 */         if (i > 0) {
/* 183 */           buffer.append(",");
/*     */         }
/* 185 */         buffer.append(this.names[i]);
/*     */       } 
/*     */     }
/* 188 */     buffer.append(")");
/* 189 */     return buffer.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\commons\io\filefilter\NameFileFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */