/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Objects;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
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
/*     */ @GwtCompatible
/*     */ public abstract class ForwardingCollection<E>
/*     */   extends ForwardingObject
/*     */   implements Collection<E>
/*     */ {
/*     */   public Iterator<E> iterator() {
/*  59 */     return delegate().iterator();
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/*  64 */     return delegate().size();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean removeAll(Collection<?> collection) {
/*  69 */     return delegate().removeAll(collection);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/*  74 */     return delegate().isEmpty();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean contains(Object object) {
/*  79 */     return delegate().contains(object);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean add(E element) {
/*  84 */     return delegate().add(element);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean remove(Object object) {
/*  89 */     return delegate().remove(object);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsAll(Collection<?> collection) {
/*  94 */     return delegate().containsAll(collection);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean addAll(Collection<? extends E> collection) {
/*  99 */     return delegate().addAll(collection);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean retainAll(Collection<?> collection) {
/* 104 */     return delegate().retainAll(collection);
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 109 */     delegate().clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public Object[] toArray() {
/* 114 */     return delegate().toArray();
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> T[] toArray(T[] array) {
/* 119 */     return delegate().toArray(array);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean standardContains(@Nullable Object object) {
/* 130 */     return Iterators.contains(iterator(), object);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean standardContainsAll(Collection<?> collection) {
/* 141 */     return Collections2.containsAllImpl(this, collection);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean standardAddAll(Collection<? extends E> collection) {
/* 152 */     return Iterators.addAll(this, collection.iterator());
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
/*     */   protected boolean standardRemove(@Nullable Object object) {
/* 164 */     Iterator<E> iterator = iterator();
/* 165 */     while (iterator.hasNext()) {
/* 166 */       if (Objects.equal(iterator.next(), object)) {
/* 167 */         iterator.remove();
/* 168 */         return true;
/*     */       } 
/*     */     } 
/* 171 */     return false;
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
/*     */   protected boolean standardRemoveAll(Collection<?> collection) {
/* 183 */     return Iterators.removeAll(iterator(), collection);
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
/*     */   protected boolean standardRetainAll(Collection<?> collection) {
/* 195 */     return Iterators.retainAll(iterator(), collection);
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
/*     */   protected void standardClear() {
/* 207 */     Iterators.clear(iterator());
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
/*     */   protected boolean standardIsEmpty() {
/* 219 */     return !iterator().hasNext();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String standardToString() {
/* 230 */     return Collections2.toStringImpl(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object[] standardToArray() {
/* 241 */     Object[] newArray = new Object[size()];
/* 242 */     return toArray(newArray);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected <T> T[] standardToArray(T[] array) {
/* 253 */     return ObjectArrays.toArrayImpl(this, array);
/*     */   }
/*     */   
/*     */   protected abstract Collection<E> delegate();
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\collect\ForwardingCollection.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */