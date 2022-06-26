/*     */ package com.google.common.collect;
/*     */ 
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
/*     */ abstract class AbstractRangeSet<C extends Comparable>
/*     */   implements RangeSet<C>
/*     */ {
/*     */   public boolean contains(C value) {
/*  29 */     return (rangeContaining(value) != null);
/*     */   }
/*     */ 
/*     */   
/*     */   public abstract Range<C> rangeContaining(C paramC);
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/*  37 */     return asRanges().isEmpty();
/*     */   }
/*     */ 
/*     */   
/*     */   public void add(Range<C> range) {
/*  42 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public void remove(Range<C> range) {
/*  47 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/*  52 */     remove((Range)Range.all());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean enclosesAll(RangeSet<C> other) {
/*  57 */     for (Range<C> range : other.asRanges()) {
/*  58 */       if (!encloses(range)) {
/*  59 */         return false;
/*     */       }
/*     */     } 
/*  62 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void addAll(RangeSet<C> other) {
/*  67 */     for (Range<C> range : other.asRanges()) {
/*  68 */       add(range);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeAll(RangeSet<C> other) {
/*  74 */     for (Range<C> range : other.asRanges()) {
/*  75 */       remove(range);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public abstract boolean encloses(Range<C> paramRange);
/*     */ 
/*     */   
/*     */   public boolean equals(@Nullable Object obj) {
/*  84 */     if (obj == this)
/*  85 */       return true; 
/*  86 */     if (obj instanceof RangeSet) {
/*  87 */       RangeSet<?> other = (RangeSet)obj;
/*  88 */       return asRanges().equals(other.asRanges());
/*     */     } 
/*  90 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public final int hashCode() {
/*  95 */     return asRanges().hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public final String toString() {
/* 100 */     return asRanges().toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\collect\AbstractRangeSet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */