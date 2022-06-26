/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import com.google.common.base.Preconditions;
/*    */ import java.io.Serializable;
/*    */ import java.util.Iterator;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ @GwtCompatible(serializable = true)
/*    */ final class ReverseNaturalOrdering
/*    */   extends Ordering<Comparable>
/*    */   implements Serializable
/*    */ {
/* 31 */   static final ReverseNaturalOrdering INSTANCE = new ReverseNaturalOrdering();
/*    */   
/*    */   public int compare(Comparable left, Comparable<Comparable> right) {
/* 34 */     Preconditions.checkNotNull(left);
/* 35 */     if (left == right) {
/* 36 */       return 0;
/*    */     }
/*    */     
/* 39 */     return right.compareTo(left);
/*    */   }
/*    */   private static final long serialVersionUID = 0L;
/*    */   public <S extends Comparable> Ordering<S> reverse() {
/* 43 */     return Ordering.natural();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public <E extends Comparable> E min(E a, E b) {
/* 49 */     return (E)NaturalOrdering.INSTANCE.max(a, b);
/*    */   }
/*    */   
/*    */   public <E extends Comparable> E min(E a, E b, E c, E... rest) {
/* 53 */     return (E)NaturalOrdering.INSTANCE.max(a, b, c, (Object[])rest);
/*    */   }
/*    */   
/*    */   public <E extends Comparable> E min(Iterator<E> iterator) {
/* 57 */     return (E)NaturalOrdering.INSTANCE.max(iterator);
/*    */   }
/*    */   
/*    */   public <E extends Comparable> E min(Iterable<E> iterable) {
/* 61 */     return (E)NaturalOrdering.INSTANCE.max(iterable);
/*    */   }
/*    */   
/*    */   public <E extends Comparable> E max(E a, E b) {
/* 65 */     return (E)NaturalOrdering.INSTANCE.min(a, b);
/*    */   }
/*    */   
/*    */   public <E extends Comparable> E max(E a, E b, E c, E... rest) {
/* 69 */     return (E)NaturalOrdering.INSTANCE.min(a, b, c, (Object[])rest);
/*    */   }
/*    */   
/*    */   public <E extends Comparable> E max(Iterator<E> iterator) {
/* 73 */     return (E)NaturalOrdering.INSTANCE.min(iterator);
/*    */   }
/*    */   
/*    */   public <E extends Comparable> E max(Iterable<E> iterable) {
/* 77 */     return (E)NaturalOrdering.INSTANCE.min(iterable);
/*    */   }
/*    */ 
/*    */   
/*    */   private Object readResolve() {
/* 82 */     return INSTANCE;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 86 */     return "Ordering.natural().reverse()";
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\collect\ReverseNaturalOrdering.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */