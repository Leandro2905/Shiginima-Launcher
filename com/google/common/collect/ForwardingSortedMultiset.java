/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.NavigableSet;
/*     */ import java.util.Set;
/*     */ import java.util.SortedSet;
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
/*     */ @Beta
/*     */ @GwtCompatible(emulated = true)
/*     */ public abstract class ForwardingSortedMultiset<E>
/*     */   extends ForwardingMultiset<E>
/*     */   implements SortedMultiset<E>
/*     */ {
/*     */   public NavigableSet<E> elementSet() {
/*  54 */     return (NavigableSet<E>)super.elementSet();
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
/*     */   protected class StandardElementSet
/*     */     extends SortedMultisets.NavigableElementSet<E>
/*     */   {
/*     */     public StandardElementSet() {
/*  71 */       super(ForwardingSortedMultiset.this);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public Comparator<? super E> comparator() {
/*  77 */     return delegate().comparator();
/*     */   }
/*     */ 
/*     */   
/*     */   public SortedMultiset<E> descendingMultiset() {
/*  82 */     return delegate().descendingMultiset();
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
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract class StandardDescendingMultiset
/*     */     extends DescendingMultiset<E>
/*     */   {
/*     */     SortedMultiset<E> forwardMultiset() {
/* 102 */       return ForwardingSortedMultiset.this;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public Multiset.Entry<E> firstEntry() {
/* 108 */     return delegate().firstEntry();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Multiset.Entry<E> standardFirstEntry() {
/* 118 */     Iterator<Multiset.Entry<E>> entryIterator = entrySet().iterator();
/* 119 */     if (!entryIterator.hasNext()) {
/* 120 */       return null;
/*     */     }
/* 122 */     Multiset.Entry<E> entry = entryIterator.next();
/* 123 */     return Multisets.immutableEntry(entry.getElement(), entry.getCount());
/*     */   }
/*     */ 
/*     */   
/*     */   public Multiset.Entry<E> lastEntry() {
/* 128 */     return delegate().lastEntry();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Multiset.Entry<E> standardLastEntry() {
/* 139 */     Iterator<Multiset.Entry<E>> entryIterator = descendingMultiset().entrySet().iterator();
/*     */ 
/*     */     
/* 142 */     if (!entryIterator.hasNext()) {
/* 143 */       return null;
/*     */     }
/* 145 */     Multiset.Entry<E> entry = entryIterator.next();
/* 146 */     return Multisets.immutableEntry(entry.getElement(), entry.getCount());
/*     */   }
/*     */ 
/*     */   
/*     */   public Multiset.Entry<E> pollFirstEntry() {
/* 151 */     return delegate().pollFirstEntry();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Multiset.Entry<E> standardPollFirstEntry() {
/* 161 */     Iterator<Multiset.Entry<E>> entryIterator = entrySet().iterator();
/* 162 */     if (!entryIterator.hasNext()) {
/* 163 */       return null;
/*     */     }
/* 165 */     Multiset.Entry<E> entry = entryIterator.next();
/* 166 */     entry = Multisets.immutableEntry(entry.getElement(), entry.getCount());
/* 167 */     entryIterator.remove();
/* 168 */     return entry;
/*     */   }
/*     */ 
/*     */   
/*     */   public Multiset.Entry<E> pollLastEntry() {
/* 173 */     return delegate().pollLastEntry();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Multiset.Entry<E> standardPollLastEntry() {
/* 184 */     Iterator<Multiset.Entry<E>> entryIterator = descendingMultiset().entrySet().iterator();
/*     */ 
/*     */     
/* 187 */     if (!entryIterator.hasNext()) {
/* 188 */       return null;
/*     */     }
/* 190 */     Multiset.Entry<E> entry = entryIterator.next();
/* 191 */     entry = Multisets.immutableEntry(entry.getElement(), entry.getCount());
/* 192 */     entryIterator.remove();
/* 193 */     return entry;
/*     */   }
/*     */ 
/*     */   
/*     */   public SortedMultiset<E> headMultiset(E upperBound, BoundType boundType) {
/* 198 */     return delegate().headMultiset(upperBound, boundType);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public SortedMultiset<E> subMultiset(E lowerBound, BoundType lowerBoundType, E upperBound, BoundType upperBoundType) {
/* 204 */     return delegate().subMultiset(lowerBound, lowerBoundType, upperBound, upperBoundType);
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
/*     */   protected SortedMultiset<E> standardSubMultiset(E lowerBound, BoundType lowerBoundType, E upperBound, BoundType upperBoundType) {
/* 217 */     return tailMultiset(lowerBound, lowerBoundType).headMultiset(upperBound, upperBoundType);
/*     */   }
/*     */ 
/*     */   
/*     */   public SortedMultiset<E> tailMultiset(E lowerBound, BoundType boundType) {
/* 222 */     return delegate().tailMultiset(lowerBound, boundType);
/*     */   }
/*     */   
/*     */   protected abstract SortedMultiset<E> delegate();
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\collect\ForwardingSortedMultiset.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */