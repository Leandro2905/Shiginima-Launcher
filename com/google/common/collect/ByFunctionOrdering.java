/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import com.google.common.base.Function;
/*    */ import com.google.common.base.Objects;
/*    */ import com.google.common.base.Preconditions;
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
/*    */ @GwtCompatible(serializable = true)
/*    */ final class ByFunctionOrdering<F, T>
/*    */   extends Ordering<F>
/*    */   implements Serializable
/*    */ {
/*    */   final Function<F, ? extends T> function;
/*    */   final Ordering<T> ordering;
/*    */   private static final long serialVersionUID = 0L;
/*    */   
/*    */   ByFunctionOrdering(Function<F, ? extends T> function, Ordering<T> ordering) {
/* 41 */     this.function = (Function<F, ? extends T>)Preconditions.checkNotNull(function);
/* 42 */     this.ordering = (Ordering<T>)Preconditions.checkNotNull(ordering);
/*    */   }
/*    */   
/*    */   public int compare(F left, F right) {
/* 46 */     return this.ordering.compare((T)this.function.apply(left), (T)this.function.apply(right));
/*    */   }
/*    */   
/*    */   public boolean equals(@Nullable Object object) {
/* 50 */     if (object == this) {
/* 51 */       return true;
/*    */     }
/* 53 */     if (object instanceof ByFunctionOrdering) {
/* 54 */       ByFunctionOrdering<?, ?> that = (ByFunctionOrdering<?, ?>)object;
/* 55 */       return (this.function.equals(that.function) && this.ordering.equals(that.ordering));
/*    */     } 
/*    */     
/* 58 */     return false;
/*    */   }
/*    */   
/*    */   public int hashCode() {
/* 62 */     return Objects.hashCode(new Object[] { this.function, this.ordering });
/*    */   }
/*    */   
/*    */   public String toString() {
/* 66 */     String str1 = String.valueOf(String.valueOf(this.ordering)), str2 = String.valueOf(String.valueOf(this.function)); return (new StringBuilder(13 + str1.length() + str2.length())).append(str1).append(".onResultOf(").append(str2).append(")").toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\collect\ByFunctionOrdering.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */