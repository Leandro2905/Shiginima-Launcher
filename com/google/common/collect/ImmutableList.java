/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.io.InvalidObjectException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
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
/*     */ public abstract class ImmutableList<E>
/*     */   extends ImmutableCollection<E>
/*     */   implements List<E>, RandomAccess
/*     */ {
/*  66 */   private static final ImmutableList<Object> EMPTY = new RegularImmutableList(ObjectArrays.EMPTY_ARRAY);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableList<E> of() {
/*  77 */     return (ImmutableList)EMPTY;
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
/*     */   public static <E> ImmutableList<E> of(E element) {
/*  89 */     return new SingletonImmutableList<E>(element);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableList<E> of(E e1, E e2) {
/*  98 */     return construct(new Object[] { e1, e2 });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableList<E> of(E e1, E e2, E e3) {
/* 107 */     return construct(new Object[] { e1, e2, e3 });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableList<E> of(E e1, E e2, E e3, E e4) {
/* 116 */     return construct(new Object[] { e1, e2, e3, e4 });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableList<E> of(E e1, E e2, E e3, E e4, E e5) {
/* 125 */     return construct(new Object[] { e1, e2, e3, e4, e5 });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableList<E> of(E e1, E e2, E e3, E e4, E e5, E e6) {
/* 134 */     return construct(new Object[] { e1, e2, e3, e4, e5, e6 });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableList<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E e7) {
/* 144 */     return construct(new Object[] { e1, e2, e3, e4, e5, e6, e7 });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableList<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E e7, E e8) {
/* 154 */     return construct(new Object[] { e1, e2, e3, e4, e5, e6, e7, e8 });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableList<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E e7, E e8, E e9) {
/* 164 */     return construct(new Object[] { e1, e2, e3, e4, e5, e6, e7, e8, e9 });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableList<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E e7, E e8, E e9, E e10) {
/* 174 */     return construct(new Object[] { e1, e2, e3, e4, e5, e6, e7, e8, e9, e10 });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableList<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E e7, E e8, E e9, E e10, E e11) {
/* 184 */     return construct(new Object[] { e1, e2, e3, e4, e5, e6, e7, e8, e9, e10, e11 });
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
/*     */   public static <E> ImmutableList<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E e7, E e8, E e9, E e10, E e11, E e12, E... others) {
/* 199 */     Object[] array = new Object[12 + others.length];
/* 200 */     array[0] = e1;
/* 201 */     array[1] = e2;
/* 202 */     array[2] = e3;
/* 203 */     array[3] = e4;
/* 204 */     array[4] = e5;
/* 205 */     array[5] = e6;
/* 206 */     array[6] = e7;
/* 207 */     array[7] = e8;
/* 208 */     array[8] = e9;
/* 209 */     array[9] = e10;
/* 210 */     array[10] = e11;
/* 211 */     array[11] = e12;
/* 212 */     System.arraycopy(others, 0, array, 12, others.length);
/* 213 */     return construct(array);
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
/*     */   public static <E> ImmutableList<E> copyOf(Iterable<? extends E> elements) {
/* 225 */     Preconditions.checkNotNull(elements);
/* 226 */     return (elements instanceof Collection) ? copyOf((Collection<? extends E>)elements) : copyOf(elements.iterator());
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableList<E> copyOf(Collection<? extends E> elements) {
/* 251 */     if (elements instanceof ImmutableCollection) {
/*     */       
/* 253 */       ImmutableList<E> list = ((ImmutableCollection)elements).asList();
/* 254 */       return list.isPartialView() ? asImmutableList(list.toArray()) : list;
/*     */     } 
/*     */ 
/*     */     
/* 258 */     return construct(elements.toArray());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableList<E> copyOf(Iterator<? extends E> elements) {
/* 268 */     if (!elements.hasNext()) {
/* 269 */       return of();
/*     */     }
/* 271 */     E first = elements.next();
/* 272 */     if (!elements.hasNext()) {
/* 273 */       return of(first);
/*     */     }
/* 275 */     return (new Builder<E>()).add(first).addAll(elements).build();
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
/*     */   public static <E> ImmutableList<E> copyOf(E[] elements) {
/* 289 */     switch (elements.length) {
/*     */       case 0:
/* 291 */         return of();
/*     */       case 1:
/* 293 */         return new SingletonImmutableList<E>(elements[0]);
/*     */     } 
/* 295 */     return new RegularImmutableList<E>(ObjectArrays.checkElementsNotNull((Object[])elements.clone()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static <E> ImmutableList<E> construct(Object... elements) {
/* 303 */     return asImmutableList(ObjectArrays.checkElementsNotNull(elements));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <E> ImmutableList<E> asImmutableList(Object[] elements) {
/* 312 */     return asImmutableList(elements, elements.length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <E> ImmutableList<E> asImmutableList(Object[] elements, int length) {
/*     */     ImmutableList<E> list;
/* 320 */     switch (length) {
/*     */       case 0:
/* 322 */         return of();
/*     */       
/*     */       case 1:
/* 325 */         list = new SingletonImmutableList<E>((E)elements[0]);
/* 326 */         return list;
/*     */     } 
/* 328 */     if (length < elements.length) {
/* 329 */       elements = ObjectArrays.arraysCopyOf(elements, length);
/*     */     }
/* 331 */     return new RegularImmutableList<E>(elements);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UnmodifiableIterator<E> iterator() {
/* 340 */     return listIterator();
/*     */   }
/*     */   
/*     */   public UnmodifiableListIterator<E> listIterator() {
/* 344 */     return listIterator(0);
/*     */   }
/*     */   
/*     */   public UnmodifiableListIterator<E> listIterator(int index) {
/* 348 */     return new AbstractIndexedListIterator<E>(size(), index)
/*     */       {
/*     */         protected E get(int index) {
/* 351 */           return ImmutableList.this.get(index);
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   public int indexOf(@Nullable Object object) {
/* 358 */     return (object == null) ? -1 : Lists.indexOfImpl(this, object);
/*     */   }
/*     */ 
/*     */   
/*     */   public int lastIndexOf(@Nullable Object object) {
/* 363 */     return (object == null) ? -1 : Lists.lastIndexOfImpl(this, object);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean contains(@Nullable Object object) {
/* 368 */     return (indexOf(object) >= 0);
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
/*     */   public ImmutableList<E> subList(int fromIndex, int toIndex) {
/* 381 */     Preconditions.checkPositionIndexes(fromIndex, toIndex, size());
/* 382 */     int length = toIndex - fromIndex;
/* 383 */     switch (length) {
/*     */       case 0:
/* 385 */         return of();
/*     */       case 1:
/* 387 */         return of(get(fromIndex));
/*     */     } 
/* 389 */     return subListUnchecked(fromIndex, toIndex);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   ImmutableList<E> subListUnchecked(int fromIndex, int toIndex) {
/* 399 */     return new SubList(fromIndex, toIndex - fromIndex);
/*     */   }
/*     */   
/*     */   class SubList extends ImmutableList<E> {
/*     */     final transient int offset;
/*     */     final transient int length;
/*     */     
/*     */     SubList(int offset, int length) {
/* 407 */       this.offset = offset;
/* 408 */       this.length = length;
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 413 */       return this.length;
/*     */     }
/*     */ 
/*     */     
/*     */     public E get(int index) {
/* 418 */       Preconditions.checkElementIndex(index, this.length);
/* 419 */       return ImmutableList.this.get(index + this.offset);
/*     */     }
/*     */ 
/*     */     
/*     */     public ImmutableList<E> subList(int fromIndex, int toIndex) {
/* 424 */       Preconditions.checkPositionIndexes(fromIndex, toIndex, this.length);
/* 425 */       return ImmutableList.this.subList(fromIndex + this.offset, toIndex + this.offset);
/*     */     }
/*     */ 
/*     */     
/*     */     boolean isPartialView() {
/* 430 */       return true;
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
/*     */   @Deprecated
/*     */   public final boolean addAll(int index, Collection<? extends E> newElements) {
/* 443 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public final E set(int index, E element) {
/* 455 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public final void add(int index, E element) {
/* 467 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public final E remove(int index) {
/* 479 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final ImmutableList<E> asList() {
/* 488 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   int copyIntoArray(Object[] dst, int offset) {
/* 494 */     int size = size();
/* 495 */     for (int i = 0; i < size; i++) {
/* 496 */       dst[offset + i] = get(i);
/*     */     }
/* 498 */     return offset + size;
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
/*     */   public ImmutableList<E> reverse() {
/* 510 */     return new ReverseImmutableList<E>(this);
/*     */   }
/*     */   
/*     */   private static class ReverseImmutableList<E> extends ImmutableList<E> {
/*     */     private final transient ImmutableList<E> forwardList;
/*     */     
/*     */     ReverseImmutableList(ImmutableList<E> backingList) {
/* 517 */       this.forwardList = backingList;
/*     */     }
/*     */     
/*     */     private int reverseIndex(int index) {
/* 521 */       return size() - 1 - index;
/*     */     }
/*     */     
/*     */     private int reversePosition(int index) {
/* 525 */       return size() - index;
/*     */     }
/*     */     
/*     */     public ImmutableList<E> reverse() {
/* 529 */       return this.forwardList;
/*     */     }
/*     */     
/*     */     public boolean contains(@Nullable Object object) {
/* 533 */       return this.forwardList.contains(object);
/*     */     }
/*     */     
/*     */     public int indexOf(@Nullable Object object) {
/* 537 */       int index = this.forwardList.lastIndexOf(object);
/* 538 */       return (index >= 0) ? reverseIndex(index) : -1;
/*     */     }
/*     */     
/*     */     public int lastIndexOf(@Nullable Object object) {
/* 542 */       int index = this.forwardList.indexOf(object);
/* 543 */       return (index >= 0) ? reverseIndex(index) : -1;
/*     */     }
/*     */     
/*     */     public ImmutableList<E> subList(int fromIndex, int toIndex) {
/* 547 */       Preconditions.checkPositionIndexes(fromIndex, toIndex, size());
/* 548 */       return this.forwardList.subList(reversePosition(toIndex), reversePosition(fromIndex)).reverse();
/*     */     }
/*     */ 
/*     */     
/*     */     public E get(int index) {
/* 553 */       Preconditions.checkElementIndex(index, size());
/* 554 */       return this.forwardList.get(reverseIndex(index));
/*     */     }
/*     */     
/*     */     public int size() {
/* 558 */       return this.forwardList.size();
/*     */     }
/*     */     
/*     */     boolean isPartialView() {
/* 562 */       return this.forwardList.isPartialView();
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean equals(@Nullable Object obj) {
/* 567 */     return Lists.equalsImpl(this, obj);
/*     */   }
/*     */   
/*     */   public int hashCode() {
/* 571 */     int hashCode = 1;
/* 572 */     int n = size();
/* 573 */     for (int i = 0; i < n; i++) {
/* 574 */       hashCode = 31 * hashCode + get(i).hashCode();
/*     */       
/* 576 */       hashCode = hashCode ^ 0xFFFFFFFF ^ 0xFFFFFFFF;
/*     */     } 
/*     */     
/* 579 */     return hashCode;
/*     */   }
/*     */   
/*     */   static class SerializedForm
/*     */     implements Serializable
/*     */   {
/*     */     final Object[] elements;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     SerializedForm(Object[] elements) {
/* 589 */       this.elements = elements;
/*     */     }
/*     */     Object readResolve() {
/* 592 */       return ImmutableList.copyOf(this.elements);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void readObject(ObjectInputStream stream) throws InvalidObjectException {
/* 599 */     throw new InvalidObjectException("Use SerializedForm");
/*     */   }
/*     */   
/*     */   Object writeReplace() {
/* 603 */     return new SerializedForm(toArray());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> Builder<E> builder() {
/* 611 */     return new Builder<E>();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final class Builder<E>
/*     */     extends ImmutableCollection.ArrayBasedBuilder<E>
/*     */   {
/*     */     public Builder() {
/* 636 */       this(4);
/*     */     }
/*     */ 
/*     */     
/*     */     Builder(int capacity) {
/* 641 */       super(capacity);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder<E> add(E element) {
/* 652 */       super.add(element);
/* 653 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder<E> addAll(Iterable<? extends E> elements) {
/* 665 */       super.addAll(elements);
/* 666 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder<E> add(E... elements) {
/* 678 */       super.add(elements);
/* 679 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder<E> addAll(Iterator<? extends E> elements) {
/* 691 */       super.addAll(elements);
/* 692 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ImmutableList<E> build() {
/* 700 */       return ImmutableList.asImmutableList(this.contents, this.size);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\collect\ImmutableList.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */