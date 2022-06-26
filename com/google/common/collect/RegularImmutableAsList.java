/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import com.google.common.annotations.GwtIncompatible;
/*    */ import java.util.ListIterator;
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
/*    */ @GwtCompatible(emulated = true)
/*    */ class RegularImmutableAsList<E>
/*    */   extends ImmutableAsList<E>
/*    */ {
/*    */   private final ImmutableCollection<E> delegate;
/*    */   private final ImmutableList<? extends E> delegateList;
/*    */   
/*    */   RegularImmutableAsList(ImmutableCollection<E> delegate, ImmutableList<? extends E> delegateList) {
/* 35 */     this.delegate = delegate;
/* 36 */     this.delegateList = delegateList;
/*    */   }
/*    */   
/*    */   RegularImmutableAsList(ImmutableCollection<E> delegate, Object[] array) {
/* 40 */     this(delegate, ImmutableList.asImmutableList(array));
/*    */   }
/*    */ 
/*    */   
/*    */   ImmutableCollection<E> delegateCollection() {
/* 45 */     return this.delegate;
/*    */   }
/*    */   
/*    */   ImmutableList<? extends E> delegateList() {
/* 49 */     return this.delegateList;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public UnmodifiableListIterator<E> listIterator(int index) {
/* 55 */     return (UnmodifiableListIterator)this.delegateList.listIterator(index);
/*    */   }
/*    */ 
/*    */   
/*    */   @GwtIncompatible("not present in emulated superclass")
/*    */   int copyIntoArray(Object[] dst, int offset) {
/* 61 */     return this.delegateList.copyIntoArray(dst, offset);
/*    */   }
/*    */ 
/*    */   
/*    */   public E get(int index) {
/* 66 */     return this.delegateList.get(index);
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\collect\RegularImmutableAsList.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */