/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.primitives.Booleans;
/*     */ import com.google.common.primitives.Ints;
/*     */ import com.google.common.primitives.Longs;
/*     */ import java.util.Comparator;
/*     */ import javax.annotation.Nullable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtCompatible
/*     */ public abstract class ComparisonChain
/*     */ {
/*     */   private ComparisonChain() {}
/*     */   
/*     */   public static ComparisonChain start() {
/*  69 */     return ACTIVE;
/*     */   }
/*     */   
/*  72 */   private static final ComparisonChain ACTIVE = new ComparisonChain()
/*     */     {
/*     */       public ComparisonChain compare(Comparable<Comparable> left, Comparable right)
/*     */       {
/*  76 */         return classify(left.compareTo(right));
/*     */       }
/*     */       
/*     */       public <T> ComparisonChain compare(@Nullable T left, @Nullable T right, Comparator<T> comparator) {
/*  80 */         return classify(comparator.compare(left, right));
/*     */       }
/*     */       public ComparisonChain compare(int left, int right) {
/*  83 */         return classify(Ints.compare(left, right));
/*     */       }
/*     */       public ComparisonChain compare(long left, long right) {
/*  86 */         return classify(Longs.compare(left, right));
/*     */       }
/*     */       public ComparisonChain compare(float left, float right) {
/*  89 */         return classify(Float.compare(left, right));
/*     */       }
/*     */       public ComparisonChain compare(double left, double right) {
/*  92 */         return classify(Double.compare(left, right));
/*     */       }
/*     */       public ComparisonChain compareTrueFirst(boolean left, boolean right) {
/*  95 */         return classify(Booleans.compare(right, left));
/*     */       }
/*     */       public ComparisonChain compareFalseFirst(boolean left, boolean right) {
/*  98 */         return classify(Booleans.compare(left, right));
/*     */       }
/*     */       ComparisonChain classify(int result) {
/* 101 */         return (result < 0) ? ComparisonChain.LESS : ((result > 0) ? ComparisonChain.GREATER : ComparisonChain.ACTIVE);
/*     */       }
/*     */       public int result() {
/* 104 */         return 0;
/*     */       }
/*     */     };
/*     */   
/* 108 */   private static final ComparisonChain LESS = new InactiveComparisonChain(-1);
/*     */   
/* 110 */   private static final ComparisonChain GREATER = new InactiveComparisonChain(1); public abstract ComparisonChain compare(Comparable<?> paramComparable1, Comparable<?> paramComparable2); public abstract <T> ComparisonChain compare(@Nullable T paramT1, @Nullable T paramT2, Comparator<T> paramComparator); public abstract ComparisonChain compare(int paramInt1, int paramInt2); public abstract ComparisonChain compare(long paramLong1, long paramLong2); public abstract ComparisonChain compare(float paramFloat1, float paramFloat2);
/*     */   public abstract ComparisonChain compare(double paramDouble1, double paramDouble2);
/*     */   public abstract ComparisonChain compareTrueFirst(boolean paramBoolean1, boolean paramBoolean2);
/*     */   public abstract ComparisonChain compareFalseFirst(boolean paramBoolean1, boolean paramBoolean2);
/*     */   public abstract int result();
/*     */   private static final class InactiveComparisonChain extends ComparisonChain { InactiveComparisonChain(int result) {
/* 116 */       this.result = result;
/*     */     }
/*     */     final int result;
/*     */     public ComparisonChain compare(@Nullable Comparable left, @Nullable Comparable right) {
/* 120 */       return this;
/*     */     }
/*     */     
/*     */     public <T> ComparisonChain compare(@Nullable T left, @Nullable T right, @Nullable Comparator<T> comparator) {
/* 124 */       return this;
/*     */     }
/*     */     public ComparisonChain compare(int left, int right) {
/* 127 */       return this;
/*     */     }
/*     */     public ComparisonChain compare(long left, long right) {
/* 130 */       return this;
/*     */     }
/*     */     public ComparisonChain compare(float left, float right) {
/* 133 */       return this;
/*     */     }
/*     */     public ComparisonChain compare(double left, double right) {
/* 136 */       return this;
/*     */     }
/*     */     public ComparisonChain compareTrueFirst(boolean left, boolean right) {
/* 139 */       return this;
/*     */     }
/*     */     public ComparisonChain compareFalseFirst(boolean left, boolean right) {
/* 142 */       return this;
/*     */     }
/*     */     public int result() {
/* 145 */       return this.result;
/*     */     } }
/*     */ 
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\collect\ComparisonChain.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */