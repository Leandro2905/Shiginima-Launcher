/*    */ package com.google.common.base;
/*    */ 
/*    */ import com.google.common.annotations.Beta;
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import java.io.Serializable;
/*    */ import javax.annotation.Nullable;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Beta
/*    */ @GwtCompatible
/*    */ final class FunctionalEquivalence<F, T>
/*    */   extends Equivalence<F>
/*    */   implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 0L;
/*    */   private final Function<F, ? extends T> function;
/*    */   private final Equivalence<T> resultEquivalence;
/*    */   
/*    */   FunctionalEquivalence(Function<F, ? extends T> function, Equivalence<T> resultEquivalence) {
/* 46 */     this.function = Preconditions.<Function<F, ? extends T>>checkNotNull(function);
/* 47 */     this.resultEquivalence = Preconditions.<Equivalence<T>>checkNotNull(resultEquivalence);
/*    */   }
/*    */   
/*    */   protected boolean doEquivalent(F a, F b) {
/* 51 */     return this.resultEquivalence.equivalent(this.function.apply(a), this.function.apply(b));
/*    */   }
/*    */   
/*    */   protected int doHash(F a) {
/* 55 */     return this.resultEquivalence.hash(this.function.apply(a));
/*    */   }
/*    */   
/*    */   public boolean equals(@Nullable Object obj) {
/* 59 */     if (obj == this) {
/* 60 */       return true;
/*    */     }
/* 62 */     if (obj instanceof FunctionalEquivalence) {
/* 63 */       FunctionalEquivalence<?, ?> that = (FunctionalEquivalence<?, ?>)obj;
/* 64 */       return (this.function.equals(that.function) && this.resultEquivalence.equals(that.resultEquivalence));
/*    */     } 
/*    */     
/* 67 */     return false;
/*    */   }
/*    */   
/*    */   public int hashCode() {
/* 71 */     return Objects.hashCode(new Object[] { this.function, this.resultEquivalence });
/*    */   }
/*    */   
/*    */   public String toString() {
/* 75 */     String str1 = String.valueOf(String.valueOf(this.resultEquivalence)), str2 = String.valueOf(String.valueOf(this.function)); return (new StringBuilder(13 + str1.length() + str2.length())).append(str1).append(".onResultOf(").append(str2).append(")").toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\base\FunctionalEquivalence.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */