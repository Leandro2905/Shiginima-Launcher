/*     */ package org.apache.commons.io.filefilter;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileFilter;
/*     */ import java.io.FilenameFilter;
/*     */ import java.io.Serializable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DelegateFileFilter
/*     */   extends AbstractFileFilter
/*     */   implements Serializable
/*     */ {
/*     */   private final FilenameFilter filenameFilter;
/*     */   private final FileFilter fileFilter;
/*     */   
/*     */   public DelegateFileFilter(FilenameFilter filter) {
/*  46 */     if (filter == null) {
/*  47 */       throw new IllegalArgumentException("The FilenameFilter must not be null");
/*     */     }
/*  49 */     this.filenameFilter = filter;
/*  50 */     this.fileFilter = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DelegateFileFilter(FileFilter filter) {
/*  59 */     if (filter == null) {
/*  60 */       throw new IllegalArgumentException("The FileFilter must not be null");
/*     */     }
/*  62 */     this.fileFilter = filter;
/*  63 */     this.filenameFilter = null;
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
/*  74 */     if (this.fileFilter != null) {
/*  75 */       return this.fileFilter.accept(file);
/*     */     }
/*  77 */     return super.accept(file);
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
/*  90 */     if (this.filenameFilter != null) {
/*  91 */       return this.filenameFilter.accept(dir, name);
/*     */     }
/*  93 */     return super.accept(dir, name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 104 */     String delegate = (this.fileFilter != null) ? this.fileFilter.toString() : this.filenameFilter.toString();
/* 105 */     return super.toString() + "(" + delegate + ")";
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\commons\io\filefilter\DelegateFileFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */