/*     */ package org.apache.commons.io.comparator;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Comparator;
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
/*     */ public class CompositeFileComparator
/*     */   extends AbstractFileComparator
/*     */   implements Serializable
/*     */ {
/*  47 */   private static final Comparator<?>[] NO_COMPARATORS = (Comparator<?>[])new Comparator[0];
/*     */ 
/*     */ 
/*     */   
/*     */   private final Comparator<File>[] delegates;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CompositeFileComparator(Comparator<File>... delegates) {
/*  57 */     if (delegates == null) {
/*  58 */       this.delegates = (Comparator<File>[])NO_COMPARATORS;
/*     */     } else {
/*  60 */       this.delegates = (Comparator<File>[])new Comparator[delegates.length];
/*  61 */       System.arraycopy(delegates, 0, this.delegates, 0, delegates.length);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CompositeFileComparator(Iterable<Comparator<File>> delegates) {
/*  72 */     if (delegates == null) {
/*  73 */       this.delegates = (Comparator<File>[])NO_COMPARATORS;
/*     */     } else {
/*  75 */       List<Comparator<File>> list = new ArrayList<Comparator<File>>();
/*  76 */       for (Comparator<File> comparator : delegates) {
/*  77 */         list.add(comparator);
/*     */       }
/*  79 */       this.delegates = list.<Comparator<File>>toArray((Comparator<File>[])new Comparator[list.size()]);
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
/*     */   
/*     */   public int compare(File file1, File file2) {
/*  92 */     int result = 0;
/*  93 */     for (Comparator<File> delegate : this.delegates) {
/*  94 */       result = delegate.compare(file1, file2);
/*  95 */       if (result != 0) {
/*     */         break;
/*     */       }
/*     */     } 
/*  99 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 109 */     StringBuilder builder = new StringBuilder();
/* 110 */     builder.append(super.toString());
/* 111 */     builder.append('{');
/* 112 */     for (int i = 0; i < this.delegates.length; i++) {
/* 113 */       if (i > 0) {
/* 114 */         builder.append(',');
/*     */       }
/* 116 */       builder.append(this.delegates[i]);
/*     */     } 
/* 118 */     builder.append('}');
/* 119 */     return builder.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\commons\io\comparator\CompositeFileComparator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */