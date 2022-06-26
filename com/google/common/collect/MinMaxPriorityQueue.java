/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.math.IntMath;
/*     */ import java.util.AbstractQueue;
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.ConcurrentModificationException;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Queue;
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
/*     */ public final class MinMaxPriorityQueue<E>
/*     */   extends AbstractQueue<E>
/*     */ {
/*     */   private final Heap minHeap;
/*     */   private final Heap maxHeap;
/*     */   @VisibleForTesting
/*     */   final int maximumSize;
/*     */   private Object[] queue;
/*     */   private int size;
/*     */   private int modCount;
/*     */   private static final int EVEN_POWERS_OF_TWO = 1431655765;
/*     */   private static final int ODD_POWERS_OF_TWO = -1431655766;
/*     */   private static final int DEFAULT_CAPACITY = 11;
/*     */   
/*     */   public static <E extends Comparable<E>> MinMaxPriorityQueue<E> create() {
/*  98 */     return (new Builder(Ordering.natural())).create();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E extends Comparable<E>> MinMaxPriorityQueue<E> create(Iterable<? extends E> initialContents) {
/* 107 */     return (new Builder(Ordering.natural())).create(initialContents);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <B> Builder<B> orderedBy(Comparator<B> comparator) {
/* 116 */     return new Builder<B>(comparator);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Builder<Comparable> expectedSize(int expectedSize) {
/* 125 */     return (new Builder<Comparable>(Ordering.natural())).expectedSize(expectedSize);
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
/*     */   public static Builder<Comparable> maximumSize(int maximumSize) {
/* 137 */     return (new Builder<Comparable>(Ordering.natural())).maximumSize(maximumSize);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Beta
/*     */   public static final class Builder<B>
/*     */   {
/*     */     private static final int UNSET_EXPECTED_SIZE = -1;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final Comparator<B> comparator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 163 */     private int expectedSize = -1;
/* 164 */     private int maximumSize = Integer.MAX_VALUE;
/*     */     
/*     */     private Builder(Comparator<B> comparator) {
/* 167 */       this.comparator = (Comparator<B>)Preconditions.checkNotNull(comparator);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder<B> expectedSize(int expectedSize) {
/* 175 */       Preconditions.checkArgument((expectedSize >= 0));
/* 176 */       this.expectedSize = expectedSize;
/* 177 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder<B> maximumSize(int maximumSize) {
/* 187 */       Preconditions.checkArgument((maximumSize > 0));
/* 188 */       this.maximumSize = maximumSize;
/* 189 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public <T extends B> MinMaxPriorityQueue<T> create() {
/* 197 */       return create(Collections.emptySet());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public <T extends B> MinMaxPriorityQueue<T> create(Iterable<? extends T> initialContents) {
/* 206 */       MinMaxPriorityQueue<T> queue = new MinMaxPriorityQueue<T>(this, MinMaxPriorityQueue.initialQueueSize(this.expectedSize, this.maximumSize, initialContents));
/*     */       
/* 208 */       for (T element : initialContents) {
/* 209 */         queue.offer(element);
/*     */       }
/* 211 */       return queue;
/*     */     }
/*     */ 
/*     */     
/*     */     private <T extends B> Ordering<T> ordering() {
/* 216 */       return Ordering.from(this.comparator);
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
/*     */   private MinMaxPriorityQueue(Builder<? super E> builder, int queueSize) {
/* 228 */     Ordering<E> ordering = builder.ordering();
/* 229 */     this.minHeap = new Heap(ordering);
/* 230 */     this.maxHeap = new Heap(ordering.reverse());
/* 231 */     this.minHeap.otherHeap = this.maxHeap;
/* 232 */     this.maxHeap.otherHeap = this.minHeap;
/*     */     
/* 234 */     this.maximumSize = builder.maximumSize;
/*     */     
/* 236 */     this.queue = new Object[queueSize];
/*     */   }
/*     */   
/*     */   public int size() {
/* 240 */     return this.size;
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
/*     */   public boolean add(E element) {
/* 252 */     offer(element);
/* 253 */     return true;
/*     */   }
/*     */   
/*     */   public boolean addAll(Collection<? extends E> newElements) {
/* 257 */     boolean modified = false;
/* 258 */     for (E element : newElements) {
/* 259 */       offer(element);
/* 260 */       modified = true;
/*     */     } 
/* 262 */     return modified;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean offer(E element) {
/* 272 */     Preconditions.checkNotNull(element);
/* 273 */     this.modCount++;
/* 274 */     int insertIndex = this.size++;
/*     */     
/* 276 */     growIfNeeded();
/*     */ 
/*     */ 
/*     */     
/* 280 */     heapForIndex(insertIndex).bubbleUp(insertIndex, element);
/* 281 */     return (this.size <= this.maximumSize || pollLast() != element);
/*     */   }
/*     */   
/*     */   public E poll() {
/* 285 */     return isEmpty() ? null : removeAndGet(0);
/*     */   }
/*     */ 
/*     */   
/*     */   E elementData(int index) {
/* 290 */     return (E)this.queue[index];
/*     */   }
/*     */   
/*     */   public E peek() {
/* 294 */     return isEmpty() ? null : elementData(0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int getMaxElementIndex() {
/* 301 */     switch (this.size) {
/*     */       case 1:
/* 303 */         return 0;
/*     */       case 2:
/* 305 */         return 1;
/*     */     } 
/*     */ 
/*     */     
/* 309 */     return (this.maxHeap.compareElements(1, 2) <= 0) ? 1 : 2;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public E pollFirst() {
/* 318 */     return poll();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public E removeFirst() {
/* 327 */     return remove();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public E peekFirst() {
/* 335 */     return peek();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public E pollLast() {
/* 343 */     return isEmpty() ? null : removeAndGet(getMaxElementIndex());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public E removeLast() {
/* 352 */     if (isEmpty()) {
/* 353 */       throw new NoSuchElementException();
/*     */     }
/* 355 */     return removeAndGet(getMaxElementIndex());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public E peekLast() {
/* 363 */     return isEmpty() ? null : elementData(getMaxElementIndex());
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
/*     */   @VisibleForTesting
/*     */   MoveDesc<E> removeAt(int index) {
/* 382 */     Preconditions.checkPositionIndex(index, this.size);
/* 383 */     this.modCount++;
/* 384 */     this.size--;
/* 385 */     if (this.size == index) {
/* 386 */       this.queue[this.size] = null;
/* 387 */       return null;
/*     */     } 
/* 389 */     E actualLastElement = elementData(this.size);
/* 390 */     int lastElementAt = heapForIndex(this.size).getCorrectLastElement(actualLastElement);
/*     */     
/* 392 */     E toTrickle = elementData(this.size);
/* 393 */     this.queue[this.size] = null;
/* 394 */     MoveDesc<E> changes = fillHole(index, toTrickle);
/* 395 */     if (lastElementAt < index) {
/*     */       
/* 397 */       if (changes == null)
/*     */       {
/* 399 */         return new MoveDesc<E>(actualLastElement, toTrickle);
/*     */       }
/*     */ 
/*     */       
/* 403 */       return new MoveDesc<E>(actualLastElement, changes.replaced);
/*     */     } 
/*     */ 
/*     */     
/* 407 */     return changes;
/*     */   }
/*     */   
/*     */   private MoveDesc<E> fillHole(int index, E toTrickle) {
/* 411 */     Heap heap = heapForIndex(index);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 419 */     int vacated = heap.fillHoleAt(index);
/*     */     
/* 421 */     int bubbledTo = heap.bubbleUpAlternatingLevels(vacated, toTrickle);
/* 422 */     if (bubbledTo == vacated)
/*     */     {
/*     */ 
/*     */       
/* 426 */       return heap.tryCrossOverAndBubbleUp(index, vacated, toTrickle);
/*     */     }
/* 428 */     return (bubbledTo < index) ? new MoveDesc<E>(toTrickle, elementData(index)) : null;
/*     */   }
/*     */ 
/*     */   
/*     */   static class MoveDesc<E>
/*     */   {
/*     */     final E toTrickle;
/*     */     
/*     */     final E replaced;
/*     */ 
/*     */     
/*     */     MoveDesc(E toTrickle, E replaced) {
/* 440 */       this.toTrickle = toTrickle;
/* 441 */       this.replaced = replaced;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private E removeAndGet(int index) {
/* 449 */     E value = elementData(index);
/* 450 */     removeAt(index);
/* 451 */     return value;
/*     */   }
/*     */   
/*     */   private Heap heapForIndex(int i) {
/* 455 */     return isEvenLevel(i) ? this.minHeap : this.maxHeap;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   static boolean isEvenLevel(int index) {
/* 462 */     int oneBased = index + 1;
/* 463 */     Preconditions.checkState((oneBased > 0), "negative index");
/* 464 */     return ((oneBased & 0x55555555) > (oneBased & 0xAAAAAAAA));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   boolean isIntact() {
/* 474 */     for (int i = 1; i < this.size; i++) {
/* 475 */       if (!heapForIndex(i).verifyIndex(i)) {
/* 476 */         return false;
/*     */       }
/*     */     } 
/* 479 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private class Heap
/*     */   {
/*     */     final Ordering<E> ordering;
/*     */ 
/*     */     
/*     */     Heap otherHeap;
/*     */ 
/*     */     
/*     */     Heap(Ordering<E> ordering) {
/* 493 */       this.ordering = ordering;
/*     */     }
/*     */     
/*     */     int compareElements(int a, int b) {
/* 497 */       return this.ordering.compare(MinMaxPriorityQueue.this.elementData(a), MinMaxPriorityQueue.this.elementData(b));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     MinMaxPriorityQueue.MoveDesc<E> tryCrossOverAndBubbleUp(int removeIndex, int vacated, E toTrickle) {
/*     */       E parent;
/* 507 */       int crossOver = crossOver(vacated, toTrickle);
/* 508 */       if (crossOver == vacated) {
/* 509 */         return null;
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 517 */       if (crossOver < removeIndex) {
/*     */ 
/*     */         
/* 520 */         parent = MinMaxPriorityQueue.this.elementData(removeIndex);
/*     */       } else {
/* 522 */         parent = MinMaxPriorityQueue.this.elementData(getParentIndex(removeIndex));
/*     */       } 
/*     */       
/* 525 */       if (this.otherHeap.bubbleUpAlternatingLevels(crossOver, toTrickle) < removeIndex)
/*     */       {
/* 527 */         return new MinMaxPriorityQueue.MoveDesc<E>(toTrickle, parent);
/*     */       }
/* 529 */       return null;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     void bubbleUp(int index, E x) {
/*     */       Heap heap;
/* 537 */       int crossOver = crossOverUp(index, x);
/*     */ 
/*     */       
/* 540 */       if (crossOver == index) {
/* 541 */         heap = this;
/*     */       } else {
/* 543 */         index = crossOver;
/* 544 */         heap = this.otherHeap;
/*     */       } 
/* 546 */       heap.bubbleUpAlternatingLevels(index, x);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     int bubbleUpAlternatingLevels(int index, E x) {
/* 554 */       while (index > 2) {
/* 555 */         int grandParentIndex = getGrandparentIndex(index);
/* 556 */         E e = MinMaxPriorityQueue.this.elementData(grandParentIndex);
/* 557 */         if (this.ordering.compare(e, x) <= 0) {
/*     */           break;
/*     */         }
/* 560 */         MinMaxPriorityQueue.this.queue[index] = e;
/* 561 */         index = grandParentIndex;
/*     */       } 
/* 563 */       MinMaxPriorityQueue.this.queue[index] = x;
/* 564 */       return index;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     int findMin(int index, int len) {
/* 573 */       if (index >= MinMaxPriorityQueue.this.size) {
/* 574 */         return -1;
/*     */       }
/* 576 */       Preconditions.checkState((index > 0));
/* 577 */       int limit = Math.min(index, MinMaxPriorityQueue.this.size - len) + len;
/* 578 */       int minIndex = index;
/* 579 */       for (int i = index + 1; i < limit; i++) {
/* 580 */         if (compareElements(i, minIndex) < 0) {
/* 581 */           minIndex = i;
/*     */         }
/*     */       } 
/* 584 */       return minIndex;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     int findMinChild(int index) {
/* 591 */       return findMin(getLeftChildIndex(index), 2);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     int findMinGrandChild(int index) {
/* 598 */       int leftChildIndex = getLeftChildIndex(index);
/* 599 */       if (leftChildIndex < 0) {
/* 600 */         return -1;
/*     */       }
/* 602 */       return findMin(getLeftChildIndex(leftChildIndex), 4);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     int crossOverUp(int index, E x) {
/* 611 */       if (index == 0) {
/* 612 */         MinMaxPriorityQueue.this.queue[0] = x;
/* 613 */         return 0;
/*     */       } 
/* 615 */       int parentIndex = getParentIndex(index);
/* 616 */       E parentElement = MinMaxPriorityQueue.this.elementData(parentIndex);
/* 617 */       if (parentIndex != 0) {
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 622 */         int grandparentIndex = getParentIndex(parentIndex);
/* 623 */         int uncleIndex = getRightChildIndex(grandparentIndex);
/* 624 */         if (uncleIndex != parentIndex && getLeftChildIndex(uncleIndex) >= MinMaxPriorityQueue.this.size) {
/*     */           
/* 626 */           E uncleElement = MinMaxPriorityQueue.this.elementData(uncleIndex);
/* 627 */           if (this.ordering.compare(uncleElement, parentElement) < 0) {
/* 628 */             parentIndex = uncleIndex;
/* 629 */             parentElement = uncleElement;
/*     */           } 
/*     */         } 
/*     */       } 
/* 633 */       if (this.ordering.compare(parentElement, x) < 0) {
/* 634 */         MinMaxPriorityQueue.this.queue[index] = parentElement;
/* 635 */         MinMaxPriorityQueue.this.queue[parentIndex] = x;
/* 636 */         return parentIndex;
/*     */       } 
/* 638 */       MinMaxPriorityQueue.this.queue[index] = x;
/* 639 */       return index;
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
/*     */     
/*     */     int getCorrectLastElement(E actualLastElement) {
/* 652 */       int parentIndex = getParentIndex(MinMaxPriorityQueue.this.size);
/* 653 */       if (parentIndex != 0) {
/* 654 */         int grandparentIndex = getParentIndex(parentIndex);
/* 655 */         int uncleIndex = getRightChildIndex(grandparentIndex);
/* 656 */         if (uncleIndex != parentIndex && getLeftChildIndex(uncleIndex) >= MinMaxPriorityQueue.this.size) {
/*     */           
/* 658 */           E uncleElement = MinMaxPriorityQueue.this.elementData(uncleIndex);
/* 659 */           if (this.ordering.compare(uncleElement, actualLastElement) < 0) {
/* 660 */             MinMaxPriorityQueue.this.queue[uncleIndex] = actualLastElement;
/* 661 */             MinMaxPriorityQueue.this.queue[MinMaxPriorityQueue.this.size] = uncleElement;
/* 662 */             return uncleIndex;
/*     */           } 
/*     */         } 
/*     */       } 
/* 666 */       return MinMaxPriorityQueue.this.size;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     int crossOver(int index, E x) {
/* 676 */       int minChildIndex = findMinChild(index);
/*     */ 
/*     */       
/* 679 */       if (minChildIndex > 0 && this.ordering.compare(MinMaxPriorityQueue.this.elementData(minChildIndex), x) < 0) {
/*     */         
/* 681 */         MinMaxPriorityQueue.this.queue[index] = MinMaxPriorityQueue.this.elementData(minChildIndex);
/* 682 */         MinMaxPriorityQueue.this.queue[minChildIndex] = x;
/* 683 */         return minChildIndex;
/*     */       } 
/* 685 */       return crossOverUp(index, x);
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
/*     */     int fillHoleAt(int index) {
/*     */       int minGrandchildIndex;
/* 698 */       while ((minGrandchildIndex = findMinGrandChild(index)) > 0) {
/* 699 */         MinMaxPriorityQueue.this.queue[index] = MinMaxPriorityQueue.this.elementData(minGrandchildIndex);
/* 700 */         index = minGrandchildIndex;
/*     */       } 
/* 702 */       return index;
/*     */     }
/*     */     
/*     */     private boolean verifyIndex(int i) {
/* 706 */       if (getLeftChildIndex(i) < MinMaxPriorityQueue.this.size && compareElements(i, getLeftChildIndex(i)) > 0)
/*     */       {
/* 708 */         return false;
/*     */       }
/* 710 */       if (getRightChildIndex(i) < MinMaxPriorityQueue.this.size && compareElements(i, getRightChildIndex(i)) > 0)
/*     */       {
/* 712 */         return false;
/*     */       }
/* 714 */       if (i > 0 && compareElements(i, getParentIndex(i)) > 0) {
/* 715 */         return false;
/*     */       }
/* 717 */       if (i > 2 && compareElements(getGrandparentIndex(i), i) > 0) {
/* 718 */         return false;
/*     */       }
/* 720 */       return true;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     private int getLeftChildIndex(int i) {
/* 726 */       return i * 2 + 1;
/*     */     }
/*     */     
/*     */     private int getRightChildIndex(int i) {
/* 730 */       return i * 2 + 2;
/*     */     }
/*     */     
/*     */     private int getParentIndex(int i) {
/* 734 */       return (i - 1) / 2;
/*     */     }
/*     */     
/*     */     private int getGrandparentIndex(int i) {
/* 738 */       return getParentIndex(getParentIndex(i));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private class QueueIterator
/*     */     implements Iterator<E>
/*     */   {
/* 749 */     private int cursor = -1;
/* 750 */     private int expectedModCount = MinMaxPriorityQueue.this.modCount;
/*     */     private Queue<E> forgetMeNot;
/*     */     private List<E> skipMe;
/*     */     private E lastFromForgetMeNot;
/*     */     private boolean canRemove;
/*     */     
/*     */     public boolean hasNext() {
/* 757 */       checkModCount();
/* 758 */       return (nextNotInSkipMe(this.cursor + 1) < MinMaxPriorityQueue.this.size() || (this.forgetMeNot != null && !this.forgetMeNot.isEmpty()));
/*     */     }
/*     */ 
/*     */     
/*     */     public E next() {
/* 763 */       checkModCount();
/* 764 */       int tempCursor = nextNotInSkipMe(this.cursor + 1);
/* 765 */       if (tempCursor < MinMaxPriorityQueue.this.size()) {
/* 766 */         this.cursor = tempCursor;
/* 767 */         this.canRemove = true;
/* 768 */         return MinMaxPriorityQueue.this.elementData(this.cursor);
/* 769 */       }  if (this.forgetMeNot != null) {
/* 770 */         this.cursor = MinMaxPriorityQueue.this.size();
/* 771 */         this.lastFromForgetMeNot = this.forgetMeNot.poll();
/* 772 */         if (this.lastFromForgetMeNot != null) {
/* 773 */           this.canRemove = true;
/* 774 */           return this.lastFromForgetMeNot;
/*     */         } 
/*     */       } 
/* 777 */       throw new NoSuchElementException("iterator moved past last element in queue.");
/*     */     }
/*     */ 
/*     */     
/*     */     public void remove() {
/* 782 */       CollectPreconditions.checkRemove(this.canRemove);
/* 783 */       checkModCount();
/* 784 */       this.canRemove = false;
/* 785 */       this.expectedModCount++;
/* 786 */       if (this.cursor < MinMaxPriorityQueue.this.size()) {
/* 787 */         MinMaxPriorityQueue.MoveDesc<E> moved = MinMaxPriorityQueue.this.removeAt(this.cursor);
/* 788 */         if (moved != null) {
/* 789 */           if (this.forgetMeNot == null) {
/* 790 */             this.forgetMeNot = new ArrayDeque<E>();
/* 791 */             this.skipMe = new ArrayList<E>(3);
/*     */           } 
/* 793 */           this.forgetMeNot.add(moved.toTrickle);
/* 794 */           this.skipMe.add(moved.replaced);
/*     */         } 
/* 796 */         this.cursor--;
/*     */       } else {
/* 798 */         Preconditions.checkState(removeExact(this.lastFromForgetMeNot));
/* 799 */         this.lastFromForgetMeNot = null;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     private boolean containsExact(Iterable<E> elements, E target) {
/* 805 */       for (E element : elements) {
/* 806 */         if (element == target) {
/* 807 */           return true;
/*     */         }
/*     */       } 
/* 810 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     boolean removeExact(Object target) {
/* 815 */       for (int i = 0; i < MinMaxPriorityQueue.this.size; i++) {
/* 816 */         if (MinMaxPriorityQueue.this.queue[i] == target) {
/* 817 */           MinMaxPriorityQueue.this.removeAt(i);
/* 818 */           return true;
/*     */         } 
/*     */       } 
/* 821 */       return false;
/*     */     }
/*     */     
/*     */     void checkModCount() {
/* 825 */       if (MinMaxPriorityQueue.this.modCount != this.expectedModCount) {
/* 826 */         throw new ConcurrentModificationException();
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private int nextNotInSkipMe(int c) {
/* 835 */       if (this.skipMe != null) {
/* 836 */         while (c < MinMaxPriorityQueue.this.size() && containsExact(this.skipMe, MinMaxPriorityQueue.this.elementData(c))) {
/* 837 */           c++;
/*     */         }
/*     */       }
/* 840 */       return c;
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
/*     */ 
/*     */     
/*     */     private QueueIterator() {}
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
/*     */   public Iterator<E> iterator() {
/* 867 */     return new QueueIterator();
/*     */   }
/*     */   
/*     */   public void clear() {
/* 871 */     for (int i = 0; i < this.size; i++) {
/* 872 */       this.queue[i] = null;
/*     */     }
/* 874 */     this.size = 0;
/*     */   }
/*     */   
/*     */   public Object[] toArray() {
/* 878 */     Object[] copyTo = new Object[this.size];
/* 879 */     System.arraycopy(this.queue, 0, copyTo, 0, this.size);
/* 880 */     return copyTo;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Comparator<? super E> comparator() {
/* 889 */     return this.minHeap.ordering;
/*     */   }
/*     */   @VisibleForTesting
/*     */   int capacity() {
/* 893 */     return this.queue.length;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   static int initialQueueSize(int configuredExpectedSize, int maximumSize, Iterable<?> initialContents) {
/* 903 */     int result = (configuredExpectedSize == -1) ? 11 : configuredExpectedSize;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 908 */     if (initialContents instanceof Collection) {
/* 909 */       int initialSize = ((Collection)initialContents).size();
/* 910 */       result = Math.max(result, initialSize);
/*     */     } 
/*     */ 
/*     */     
/* 914 */     return capAtMaximumSize(result, maximumSize);
/*     */   }
/*     */   
/*     */   private void growIfNeeded() {
/* 918 */     if (this.size > this.queue.length) {
/* 919 */       int newCapacity = calculateNewCapacity();
/* 920 */       Object[] newQueue = new Object[newCapacity];
/* 921 */       System.arraycopy(this.queue, 0, newQueue, 0, this.queue.length);
/* 922 */       this.queue = newQueue;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private int calculateNewCapacity() {
/* 928 */     int oldCapacity = this.queue.length;
/* 929 */     int newCapacity = (oldCapacity < 64) ? ((oldCapacity + 1) * 2) : IntMath.checkedMultiply(oldCapacity / 2, 3);
/*     */ 
/*     */     
/* 932 */     return capAtMaximumSize(newCapacity, this.maximumSize);
/*     */   }
/*     */ 
/*     */   
/*     */   private static int capAtMaximumSize(int queueSize, int maximumSize) {
/* 937 */     return Math.min(queueSize - 1, maximumSize) + 1;
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\collect\MinMaxPriorityQueue.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */