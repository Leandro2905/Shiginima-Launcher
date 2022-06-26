/*     */ package com.google.common.collect;
/*     */ 
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
/*     */ @GwtCompatible(emulated = true)
/*     */ abstract class DescendingMultiset<E>
/*     */   extends ForwardingMultiset<E>
/*     */   implements SortedMultiset<E>
/*     */ {
/*     */   private transient Comparator<? super E> comparator;
/*     */   private transient NavigableSet<E> elementSet;
/*     */   private transient Set<Multiset.Entry<E>> entrySet;
/*     */   
/*     */   public Comparator<? super E> comparator() {
/*  40 */     Comparator<? super E> result = this.comparator;
/*  41 */     if (result == null) {
/*  42 */       return this.comparator = Ordering.from(forwardMultiset().comparator()).<Object>reverse();
/*     */     }
/*     */     
/*  45 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public NavigableSet<E> elementSet() {
/*  51 */     NavigableSet<E> result = this.elementSet;
/*  52 */     if (result == null) {
/*  53 */       return this.elementSet = new SortedMultisets.NavigableElementSet<E>(this);
/*     */     }
/*  55 */     return result;
/*     */   }
/*     */   
/*     */   public Multiset.Entry<E> pollFirstEntry() {
/*  59 */     return forwardMultiset().pollLastEntry();
/*     */   }
/*     */   
/*     */   public Multiset.Entry<E> pollLastEntry() {
/*  63 */     return forwardMultiset().pollFirstEntry();
/*     */   }
/*     */ 
/*     */   
/*     */   public SortedMultiset<E> headMultiset(E toElement, BoundType boundType) {
/*  68 */     return forwardMultiset().tailMultiset(toElement, boundType).descendingMultiset();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public SortedMultiset<E> subMultiset(E fromElement, BoundType fromBoundType, E toElement, BoundType toBoundType) {
/*  74 */     return forwardMultiset().subMultiset(toElement, toBoundType, fromElement, fromBoundType).descendingMultiset();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public SortedMultiset<E> tailMultiset(E fromElement, BoundType boundType) {
/*  80 */     return forwardMultiset().headMultiset(fromElement, boundType).descendingMultiset();
/*     */   }
/*     */ 
/*     */   
/*     */   protected Multiset<E> delegate() {
/*  85 */     return forwardMultiset();
/*     */   }
/*     */   
/*     */   public SortedMultiset<E> descendingMultiset() {
/*  89 */     return forwardMultiset();
/*     */   }
/*     */   
/*     */   public Multiset.Entry<E> firstEntry() {
/*  93 */     return forwardMultiset().lastEntry();
/*     */   }
/*     */   
/*     */   public Multiset.Entry<E> lastEntry() {
/*  97 */     return forwardMultiset().firstEntry();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<Multiset.Entry<E>> entrySet() {
/* 105 */     Set<Multiset.Entry<E>> result = this.entrySet;
/* 106 */     return (result == null) ? (this.entrySet = createEntrySet()) : result;
/*     */   }
/*     */   
/*     */   Set<Multiset.Entry<E>> createEntrySet() {
/* 110 */     return new Multisets.EntrySet<E>() {
/*     */         Multiset<E> multiset() {
/* 112 */           return DescendingMultiset.this;
/*     */         }
/*     */         
/*     */         public Iterator<Multiset.Entry<E>> iterator() {
/* 116 */           return DescendingMultiset.this.entryIterator();
/*     */         }
/*     */         
/*     */         public int size() {
/* 120 */           return DescendingMultiset.this.forwardMultiset().entrySet().size();
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public Iterator<E> iterator() {
/* 126 */     return Multisets.iteratorImpl(this);
/*     */   }
/*     */   
/*     */   public Object[] toArray() {
/* 130 */     return standardToArray();
/*     */   }
/*     */   
/*     */   public <T> T[] toArray(T[] array) {
/* 134 */     return (T[])standardToArray((Object[])array);
/*     */   }
/*     */   
/*     */   public String toString() {
/* 138 */     return entrySet().toString();
/*     */   }
/*     */   
/*     */   abstract SortedMultiset<E> forwardMultiset();
/*     */   
/*     */   abstract Iterator<Multiset.Entry<E>> entryIterator();
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\collect\DescendingMultiset.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */