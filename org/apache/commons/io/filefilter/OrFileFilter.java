/*     */ package org.apache.commons.io.filefilter;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class OrFileFilter
/*     */   extends AbstractFileFilter
/*     */   implements ConditionalFileFilter, Serializable
/*     */ {
/*     */   private final List<IOFileFilter> fileFilters;
/*     */   
/*     */   public OrFileFilter() {
/*  49 */     this.fileFilters = new ArrayList<IOFileFilter>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public OrFileFilter(List<IOFileFilter> fileFilters) {
/*  60 */     if (fileFilters == null) {
/*  61 */       this.fileFilters = new ArrayList<IOFileFilter>();
/*     */     } else {
/*  63 */       this.fileFilters = new ArrayList<IOFileFilter>(fileFilters);
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
/*     */   public OrFileFilter(IOFileFilter filter1, IOFileFilter filter2) {
/*  75 */     if (filter1 == null || filter2 == null) {
/*  76 */       throw new IllegalArgumentException("The filters must not be null");
/*     */     }
/*  78 */     this.fileFilters = new ArrayList<IOFileFilter>(2);
/*  79 */     addFileFilter(filter1);
/*  80 */     addFileFilter(filter2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addFileFilter(IOFileFilter ioFileFilter) {
/*  87 */     this.fileFilters.add(ioFileFilter);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<IOFileFilter> getFileFilters() {
/*  94 */     return Collections.unmodifiableList(this.fileFilters);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean removeFileFilter(IOFileFilter ioFileFilter) {
/* 101 */     return this.fileFilters.remove(ioFileFilter);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFileFilters(List<IOFileFilter> fileFilters) {
/* 108 */     this.fileFilters.clear();
/* 109 */     this.fileFilters.addAll(fileFilters);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean accept(File file) {
/* 117 */     for (IOFileFilter fileFilter : this.fileFilters) {
/* 118 */       if (fileFilter.accept(file)) {
/* 119 */         return true;
/*     */       }
/*     */     } 
/* 122 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean accept(File file, String name) {
/* 130 */     for (IOFileFilter fileFilter : this.fileFilters) {
/* 131 */       if (fileFilter.accept(file, name)) {
/* 132 */         return true;
/*     */       }
/*     */     } 
/* 135 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 145 */     StringBuilder buffer = new StringBuilder();
/* 146 */     buffer.append(super.toString());
/* 147 */     buffer.append("(");
/* 148 */     if (this.fileFilters != null) {
/* 149 */       for (int i = 0; i < this.fileFilters.size(); i++) {
/* 150 */         if (i > 0) {
/* 151 */           buffer.append(",");
/*     */         }
/* 153 */         Object filter = this.fileFilters.get(i);
/* 154 */         buffer.append((filter == null) ? "null" : filter.toString());
/*     */       } 
/*     */     }
/* 157 */     buffer.append(")");
/* 158 */     return buffer.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\commons\io\filefilter\OrFileFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */