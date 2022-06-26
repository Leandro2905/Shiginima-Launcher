/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.util.Iterator;
/*     */ import java.util.Set;
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
/*     */ @GwtCompatible(serializable = true, emulated = true)
/*     */ final class SingletonImmutableSet<E>
/*     */   extends ImmutableSet<E>
/*     */ {
/*     */   final transient E element;
/*     */   private transient int cachedHashCode;
/*     */   
/*     */   SingletonImmutableSet(E element) {
/*  47 */     this.element = (E)Preconditions.checkNotNull(element);
/*     */   }
/*     */ 
/*     */   
/*     */   SingletonImmutableSet(E element, int hashCode) {
/*  52 */     this.element = element;
/*  53 */     this.cachedHashCode = hashCode;
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/*  58 */     return 1;
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/*  62 */     return false;
/*     */   }
/*     */   
/*     */   public boolean contains(Object target) {
/*  66 */     return this.element.equals(target);
/*     */   }
/*     */   
/*     */   public UnmodifiableIterator<E> iterator() {
/*  70 */     return Iterators.singletonIterator(this.element);
/*     */   }
/*     */   
/*     */   boolean isPartialView() {
/*  74 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   int copyIntoArray(Object[] dst, int offset) {
/*  79 */     dst[offset] = this.element;
/*  80 */     return offset + 1;
/*     */   }
/*     */   
/*     */   public boolean equals(@Nullable Object object) {
/*  84 */     if (object == this) {
/*  85 */       return true;
/*     */     }
/*  87 */     if (object instanceof Set) {
/*  88 */       Set<?> that = (Set)object;
/*  89 */       return (that.size() == 1 && this.element.equals(that.iterator().next()));
/*     */     } 
/*  91 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public final int hashCode() {
/*  96 */     int code = this.cachedHashCode;
/*  97 */     if (code == 0) {
/*  98 */       this.cachedHashCode = code = this.element.hashCode();
/*     */     }
/* 100 */     return code;
/*     */   }
/*     */   
/*     */   boolean isHashCodeFast() {
/* 104 */     return (this.cachedHashCode != 0);
/*     */   }
/*     */   
/*     */   public String toString() {
/* 108 */     String elementToString = this.element.toString();
/* 109 */     return (new StringBuilder(elementToString.length() + 2)).append('[').append(elementToString).append(']').toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\collect\SingletonImmutableSet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */