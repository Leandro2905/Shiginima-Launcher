/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import java.util.Collection;
/*    */ import java.util.Iterator;
/*    */ import java.util.Set;
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
/*    */ @GwtCompatible(serializable = true, emulated = true)
/*    */ final class EmptyImmutableSet
/*    */   extends ImmutableSet<Object>
/*    */ {
/* 33 */   static final EmptyImmutableSet INSTANCE = new EmptyImmutableSet();
/*    */   
/*    */   private static final long serialVersionUID = 0L;
/*    */ 
/*    */   
/*    */   public int size() {
/* 39 */     return 0;
/*    */   }
/*    */   
/*    */   public boolean isEmpty() {
/* 43 */     return true;
/*    */   }
/*    */   
/*    */   public boolean contains(@Nullable Object target) {
/* 47 */     return false;
/*    */   }
/*    */   
/*    */   public boolean containsAll(Collection<?> targets) {
/* 51 */     return targets.isEmpty();
/*    */   }
/*    */   
/*    */   public UnmodifiableIterator<Object> iterator() {
/* 55 */     return Iterators.emptyIterator();
/*    */   }
/*    */   
/*    */   boolean isPartialView() {
/* 59 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   int copyIntoArray(Object[] dst, int offset) {
/* 64 */     return offset;
/*    */   }
/*    */ 
/*    */   
/*    */   public ImmutableList<Object> asList() {
/* 69 */     return ImmutableList.of();
/*    */   }
/*    */   
/*    */   public boolean equals(@Nullable Object object) {
/* 73 */     if (object instanceof Set) {
/* 74 */       Set<?> that = (Set)object;
/* 75 */       return that.isEmpty();
/*    */     } 
/* 77 */     return false;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/* 81 */     return 0;
/*    */   }
/*    */   
/*    */   boolean isHashCodeFast() {
/* 85 */     return true;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 89 */     return "[]";
/*    */   }
/*    */   
/*    */   Object readResolve() {
/* 93 */     return INSTANCE;
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\collect\EmptyImmutableSet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */