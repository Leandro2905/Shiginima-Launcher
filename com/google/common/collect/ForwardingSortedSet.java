/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Set;
/*     */ import java.util.SortedSet;
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
/*     */ public abstract class ForwardingSortedSet<E>
/*     */   extends ForwardingSet<E>
/*     */   implements SortedSet<E>
/*     */ {
/*     */   public Comparator<? super E> comparator() {
/*  67 */     return delegate().comparator();
/*     */   }
/*     */ 
/*     */   
/*     */   public E first() {
/*  72 */     return delegate().first();
/*     */   }
/*     */ 
/*     */   
/*     */   public SortedSet<E> headSet(E toElement) {
/*  77 */     return delegate().headSet(toElement);
/*     */   }
/*     */ 
/*     */   
/*     */   public E last() {
/*  82 */     return delegate().last();
/*     */   }
/*     */ 
/*     */   
/*     */   public SortedSet<E> subSet(E fromElement, E toElement) {
/*  87 */     return delegate().subSet(fromElement, toElement);
/*     */   }
/*     */ 
/*     */   
/*     */   public SortedSet<E> tailSet(E fromElement) {
/*  92 */     return delegate().tailSet(fromElement);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private int unsafeCompare(Object o1, Object o2) {
/*  98 */     Comparator<? super E> comparator = comparator();
/*  99 */     return (comparator == null) ? ((Comparable<Object>)o1).compareTo(o2) : comparator.compare((E)o1, (E)o2);
/*     */   }
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
/*     */   @Beta
/*     */   protected boolean standardContains(@Nullable Object object) {
/*     */     try {
/* 115 */       SortedSet<Object> self = (SortedSet)this;
/* 116 */       Object ceiling = self.tailSet(object).first();
/* 117 */       return (unsafeCompare(ceiling, object) == 0);
/* 118 */     } catch (ClassCastException e) {
/* 119 */       return false;
/* 120 */     } catch (NoSuchElementException e) {
/* 121 */       return false;
/* 122 */     } catch (NullPointerException e) {
/* 123 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Beta
/*     */   protected boolean standardRemove(@Nullable Object object) {
/*     */     try {
/* 138 */       SortedSet<Object> self = (SortedSet)this;
/* 139 */       Iterator<Object> iterator = self.tailSet(object).iterator();
/* 140 */       if (iterator.hasNext()) {
/* 141 */         Object ceiling = iterator.next();
/* 142 */         if (unsafeCompare(ceiling, object) == 0) {
/* 143 */           iterator.remove();
/* 144 */           return true;
/*     */         } 
/*     */       } 
/* 147 */     } catch (ClassCastException e) {
/* 148 */       return false;
/* 149 */     } catch (NullPointerException e) {
/* 150 */       return false;
/*     */     } 
/* 152 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Beta
/*     */   protected SortedSet<E> standardSubSet(E fromElement, E toElement) {
/* 164 */     return tailSet(fromElement).headSet(toElement);
/*     */   }
/*     */   
/*     */   protected abstract SortedSet<E> delegate();
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\collect\ForwardingSortedSet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */