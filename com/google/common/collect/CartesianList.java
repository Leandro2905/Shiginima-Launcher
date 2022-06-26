/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.math.IntMath;
/*     */ import java.util.AbstractList;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
/*     */ import java.util.RandomAccess;
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
/*     */ @GwtCompatible
/*     */ final class CartesianList<E>
/*     */   extends AbstractList<List<E>>
/*     */   implements RandomAccess
/*     */ {
/*     */   private final transient ImmutableList<List<E>> axes;
/*     */   private final transient int[] axesSizeProduct;
/*     */   
/*     */   static <E> List<List<E>> create(List<? extends List<? extends E>> lists) {
/*  41 */     ImmutableList.Builder<List<E>> axesBuilder = new ImmutableList.Builder<List<E>>(lists.size());
/*     */     
/*  43 */     for (List<? extends E> list : lists) {
/*  44 */       List<E> copy = ImmutableList.copyOf(list);
/*  45 */       if (copy.isEmpty()) {
/*  46 */         return ImmutableList.of();
/*     */       }
/*  48 */       axesBuilder.add(copy);
/*     */     } 
/*  50 */     return new CartesianList<E>(axesBuilder.build());
/*     */   }
/*     */   
/*     */   CartesianList(ImmutableList<List<E>> axes) {
/*  54 */     this.axes = axes;
/*  55 */     int[] axesSizeProduct = new int[axes.size() + 1];
/*  56 */     axesSizeProduct[axes.size()] = 1;
/*     */     try {
/*  58 */       for (int i = axes.size() - 1; i >= 0; i--) {
/*  59 */         axesSizeProduct[i] = IntMath.checkedMultiply(axesSizeProduct[i + 1], ((List)axes.get(i)).size());
/*     */       }
/*     */     }
/*  62 */     catch (ArithmeticException e) {
/*  63 */       throw new IllegalArgumentException("Cartesian product too large; must have size at most Integer.MAX_VALUE");
/*     */     } 
/*     */     
/*  66 */     this.axesSizeProduct = axesSizeProduct;
/*     */   }
/*     */   
/*     */   private int getAxisIndexForProductIndex(int index, int axis) {
/*  70 */     return index / this.axesSizeProduct[axis + 1] % ((List)this.axes.get(axis)).size();
/*     */   }
/*     */ 
/*     */   
/*     */   public ImmutableList<E> get(final int index) {
/*  75 */     Preconditions.checkElementIndex(index, size());
/*  76 */     return new ImmutableList<E>()
/*     */       {
/*     */         public int size()
/*     */         {
/*  80 */           return CartesianList.this.axes.size();
/*     */         }
/*     */ 
/*     */         
/*     */         public E get(int axis) {
/*  85 */           Preconditions.checkElementIndex(axis, size());
/*  86 */           int axisIndex = CartesianList.this.getAxisIndexForProductIndex(index, axis);
/*  87 */           return ((List<E>)CartesianList.this.axes.get(axis)).get(axisIndex);
/*     */         }
/*     */ 
/*     */         
/*     */         boolean isPartialView() {
/*  92 */           return true;
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/*  99 */     return this.axesSizeProduct[0];
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean contains(@Nullable Object o) {
/* 104 */     if (!(o instanceof List)) {
/* 105 */       return false;
/*     */     }
/* 107 */     List<?> list = (List)o;
/* 108 */     if (list.size() != this.axes.size()) {
/* 109 */       return false;
/*     */     }
/* 111 */     ListIterator<?> itr = list.listIterator();
/* 112 */     while (itr.hasNext()) {
/* 113 */       int index = itr.nextIndex();
/* 114 */       if (!((List)this.axes.get(index)).contains(itr.next())) {
/* 115 */         return false;
/*     */       }
/*     */     } 
/* 118 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\collect\CartesianList.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */