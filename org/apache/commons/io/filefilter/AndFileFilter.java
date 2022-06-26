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
/*     */ 
/*     */ public class AndFileFilter
/*     */   extends AbstractFileFilter
/*     */   implements ConditionalFileFilter, Serializable
/*     */ {
/*     */   private final List<IOFileFilter> fileFilters;
/*     */   
/*     */   public AndFileFilter() {
/*  50 */     this.fileFilters = new ArrayList<IOFileFilter>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AndFileFilter(List<IOFileFilter> fileFilters) {
/*  61 */     if (fileFilters == null) {
/*  62 */       this.fileFilters = new ArrayList<IOFileFilter>();
/*     */     } else {
/*  64 */       this.fileFilters = new ArrayList<IOFileFilter>(fileFilters);
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
/*     */   public AndFileFilter(IOFileFilter filter1, IOFileFilter filter2) {
/*  76 */     if (filter1 == null || filter2 == null) {
/*  77 */       throw new IllegalArgumentException("The filters must not be null");
/*     */     }
/*  79 */     this.fileFilters = new ArrayList<IOFileFilter>(2);
/*  80 */     addFileFilter(filter1);
/*  81 */     addFileFilter(filter2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addFileFilter(IOFileFilter ioFileFilter) {
/*  88 */     this.fileFilters.add(ioFileFilter);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<IOFileFilter> getFileFilters() {
/*  95 */     return Collections.unmodifiableList(this.fileFilters);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean removeFileFilter(IOFileFilter ioFileFilter) {
/* 102 */     return this.fileFilters.remove(ioFileFilter);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFileFilters(List<IOFileFilter> fileFilters) {
/* 109 */     this.fileFilters.clear();
/* 110 */     this.fileFilters.addAll(fileFilters);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean accept(File file) {
/* 118 */     if (this.fileFilters.isEmpty()) {
/* 119 */       return false;
/*     */     }
/* 121 */     for (IOFileFilter fileFilter : this.fileFilters) {
/* 122 */       if (!fileFilter.accept(file)) {
/* 123 */         return false;
/*     */       }
/*     */     } 
/* 126 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean accept(File file, String name) {
/* 134 */     if (this.fileFilters.isEmpty()) {
/* 135 */       return false;
/*     */     }
/* 137 */     for (IOFileFilter fileFilter : this.fileFilters) {
/* 138 */       if (!fileFilter.accept(file, name)) {
/* 139 */         return false;
/*     */       }
/*     */     } 
/* 142 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 152 */     StringBuilder buffer = new StringBuilder();
/* 153 */     buffer.append(super.toString());
/* 154 */     buffer.append("(");
/* 155 */     if (this.fileFilters != null) {
/* 156 */       for (int i = 0; i < this.fileFilters.size(); i++) {
/* 157 */         if (i > 0) {
/* 158 */           buffer.append(",");
/*     */         }
/* 160 */         Object filter = this.fileFilters.get(i);
/* 161 */         buffer.append((filter == null) ? "null" : filter.toString());
/*     */       } 
/*     */     }
/* 164 */     buffer.append(")");
/* 165 */     return buffer.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\commons\io\filefilter\AndFileFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */