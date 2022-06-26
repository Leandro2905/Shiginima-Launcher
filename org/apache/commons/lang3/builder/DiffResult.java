/*     */ package org.apache.commons.lang3.builder;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DiffResult
/*     */   implements Iterable<Diff<?>>
/*     */ {
/*     */   public static final String OBJECTS_SAME_STRING = "";
/*     */   private static final String DIFFERS_STRING = "differs from";
/*     */   private final List<Diff<?>> diffs;
/*     */   private final Object lhs;
/*     */   private final Object rhs;
/*     */   private final ToStringStyle style;
/*     */   
/*     */   DiffResult(Object lhs, Object rhs, List<Diff<?>> diffs, ToStringStyle style) {
/*  75 */     if (lhs == null) {
/*  76 */       throw new IllegalArgumentException("Left hand object cannot be null");
/*     */     }
/*     */     
/*  79 */     if (rhs == null) {
/*  80 */       throw new IllegalArgumentException("Right hand object cannot be null");
/*     */     }
/*     */     
/*  83 */     if (diffs == null) {
/*  84 */       throw new IllegalArgumentException("List of differences cannot be null");
/*     */     }
/*     */ 
/*     */     
/*  88 */     this.diffs = diffs;
/*  89 */     this.lhs = lhs;
/*  90 */     this.rhs = rhs;
/*     */     
/*  92 */     if (style == null) {
/*  93 */       this.style = ToStringStyle.DEFAULT_STYLE;
/*     */     } else {
/*  95 */       this.style = style;
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
/*     */   public List<Diff<?>> getDiffs() {
/* 108 */     return Collections.unmodifiableList(this.diffs);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getNumberOfDiffs() {
/* 119 */     return this.diffs.size();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ToStringStyle getToStringStyle() {
/* 130 */     return this.style;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 166 */     return toString(this.style);
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
/*     */   public String toString(ToStringStyle style) {
/* 181 */     if (this.diffs.size() == 0) {
/* 182 */       return "";
/*     */     }
/*     */     
/* 185 */     ToStringBuilder lhsBuilder = new ToStringBuilder(this.lhs, style);
/* 186 */     ToStringBuilder rhsBuilder = new ToStringBuilder(this.rhs, style);
/*     */     
/* 188 */     for (Diff<?> diff : this.diffs) {
/* 189 */       lhsBuilder.append(diff.getFieldName(), diff.getLeft());
/* 190 */       rhsBuilder.append(diff.getFieldName(), diff.getRight());
/*     */     } 
/*     */     
/* 193 */     return String.format("%s %s %s", new Object[] { lhsBuilder.build(), "differs from", rhsBuilder.build() });
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
/*     */   public Iterator<Diff<?>> iterator() {
/* 206 */     return this.diffs.iterator();
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\commons\lang3\builder\DiffResult.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */