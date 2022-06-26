/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import java.io.Serializable;
/*    */ import java.util.Comparator;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @GwtCompatible(serializable = true)
/*    */ final class CompoundOrdering<T>
/*    */   extends Ordering<T>
/*    */   implements Serializable
/*    */ {
/*    */   final ImmutableList<Comparator<? super T>> comparators;
/*    */   private static final long serialVersionUID = 0L;
/*    */   
/*    */   CompoundOrdering(Comparator<? super T> primary, Comparator<? super T> secondary) {
/* 31 */     this.comparators = ImmutableList.of(primary, secondary);
/*    */   }
/*    */ 
/*    */   
/*    */   CompoundOrdering(Iterable<? extends Comparator<? super T>> comparators) {
/* 36 */     this.comparators = ImmutableList.copyOf(comparators);
/*    */   }
/*    */ 
/*    */   
/*    */   public int compare(T left, T right) {
/* 41 */     int size = this.comparators.size();
/* 42 */     for (int i = 0; i < size; i++) {
/* 43 */       int result = ((Comparator<T>)this.comparators.get(i)).compare(left, right);
/* 44 */       if (result != 0) {
/* 45 */         return result;
/*    */       }
/*    */     } 
/* 48 */     return 0;
/*    */   }
/*    */   
/*    */   public boolean equals(Object object) {
/* 52 */     if (object == this) {
/* 53 */       return true;
/*    */     }
/* 55 */     if (object instanceof CompoundOrdering) {
/* 56 */       CompoundOrdering<?> that = (CompoundOrdering)object;
/* 57 */       return this.comparators.equals(that.comparators);
/*    */     } 
/* 59 */     return false;
/*    */   }
/*    */   
/*    */   public int hashCode() {
/* 63 */     return this.comparators.hashCode();
/*    */   }
/*    */   
/*    */   public String toString() {
/* 67 */     String str = String.valueOf(String.valueOf(this.comparators)); return (new StringBuilder(19 + str.length())).append("Ordering.compound(").append(str).append(")").toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\collect\CompoundOrdering.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */