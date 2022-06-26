/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.util.ListIterator;
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
/*     */ @GwtCompatible(serializable = true, emulated = true)
/*     */ class RegularImmutableList<E>
/*     */   extends ImmutableList<E>
/*     */ {
/*     */   private final transient int offset;
/*     */   private final transient int size;
/*     */   private final transient Object[] array;
/*     */   
/*     */   RegularImmutableList(Object[] array, int offset, int size) {
/*  37 */     this.offset = offset;
/*  38 */     this.size = size;
/*  39 */     this.array = array;
/*     */   }
/*     */   
/*     */   RegularImmutableList(Object[] array) {
/*  43 */     this(array, 0, array.length);
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/*  48 */     return this.size;
/*     */   }
/*     */   
/*     */   boolean isPartialView() {
/*  52 */     return (this.size != this.array.length);
/*     */   }
/*     */ 
/*     */   
/*     */   int copyIntoArray(Object[] dst, int dstOff) {
/*  57 */     System.arraycopy(this.array, this.offset, dst, dstOff, this.size);
/*  58 */     return dstOff + this.size;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public E get(int index) {
/*  65 */     Preconditions.checkElementIndex(index, this.size);
/*  66 */     return (E)this.array[index + this.offset];
/*     */   }
/*     */ 
/*     */   
/*     */   public int indexOf(@Nullable Object object) {
/*  71 */     if (object == null) {
/*  72 */       return -1;
/*     */     }
/*  74 */     for (int i = 0; i < this.size; i++) {
/*  75 */       if (this.array[this.offset + i].equals(object)) {
/*  76 */         return i;
/*     */       }
/*     */     } 
/*  79 */     return -1;
/*     */   }
/*     */ 
/*     */   
/*     */   public int lastIndexOf(@Nullable Object object) {
/*  84 */     if (object == null) {
/*  85 */       return -1;
/*     */     }
/*  87 */     for (int i = this.size - 1; i >= 0; i--) {
/*  88 */       if (this.array[this.offset + i].equals(object)) {
/*  89 */         return i;
/*     */       }
/*     */     } 
/*  92 */     return -1;
/*     */   }
/*     */ 
/*     */   
/*     */   ImmutableList<E> subListUnchecked(int fromIndex, int toIndex) {
/*  97 */     return new RegularImmutableList(this.array, this.offset + fromIndex, toIndex - fromIndex);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UnmodifiableListIterator<E> listIterator(int index) {
/* 106 */     return Iterators.forArray((E[])this.array, this.offset, this.size, index);
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\collect\RegularImmutableList.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */