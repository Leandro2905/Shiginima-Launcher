/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.MoreObjects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.primitives.Ints;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.ConcurrentModificationException;
/*     */ import java.util.Iterator;
/*     */ import java.util.NavigableSet;
/*     */ import java.util.NoSuchElementException;
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
/*     */ 
/*     */ 
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
/*     */ public final class TreeMultiset<E>
/*     */   extends AbstractSortedMultiset<E>
/*     */   implements Serializable
/*     */ {
/*     */   private final transient Reference<AvlNode<E>> rootReference;
/*     */   private final transient GeneralRange<E> range;
/*     */   private final transient AvlNode<E> header;
/*     */   @GwtIncompatible("not needed in emulated source")
/*     */   private static final long serialVersionUID = 1L;
/*     */   
/*     */   public static <E extends Comparable> TreeMultiset<E> create() {
/*  74 */     return new TreeMultiset<E>(Ordering.natural());
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
/*     */   public static <E> TreeMultiset<E> create(@Nullable Comparator<? super E> comparator) {
/*  91 */     return (comparator == null) ? new TreeMultiset<E>(Ordering.natural()) : new TreeMultiset<E>(comparator);
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
/*     */   public static <E extends Comparable> TreeMultiset<E> create(Iterable<? extends E> elements) {
/* 106 */     TreeMultiset<E> multiset = create();
/* 107 */     Iterables.addAll(multiset, elements);
/* 108 */     return multiset;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   TreeMultiset(Reference<AvlNode<E>> rootReference, GeneralRange<E> range, AvlNode<E> endLink) {
/* 116 */     super(range.comparator());
/* 117 */     this.rootReference = rootReference;
/* 118 */     this.range = range;
/* 119 */     this.header = endLink;
/*     */   }
/*     */   
/*     */   TreeMultiset(Comparator<? super E> comparator) {
/* 123 */     super(comparator);
/* 124 */     this.range = GeneralRange.all(comparator);
/* 125 */     this.header = new AvlNode<E>(null, 1);
/* 126 */     successor(this.header, this.header);
/* 127 */     this.rootReference = new Reference<AvlNode<E>>();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private enum Aggregate
/*     */   {
/* 134 */     SIZE
/*     */     {
/*     */       int nodeAggregate(TreeMultiset.AvlNode<?> node) {
/* 137 */         return node.elemCount;
/*     */       }
/*     */ 
/*     */       
/*     */       long treeAggregate(@Nullable TreeMultiset.AvlNode<?> root) {
/* 142 */         return (root == null) ? 0L : root.totalCount;
/*     */       }
/*     */     },
/* 145 */     DISTINCT
/*     */     {
/*     */       int nodeAggregate(TreeMultiset.AvlNode<?> node) {
/* 148 */         return 1;
/*     */       }
/*     */ 
/*     */       
/*     */       long treeAggregate(@Nullable TreeMultiset.AvlNode<?> root) {
/* 153 */         return (root == null) ? 0L : root.distinctElements;
/*     */       } };
/*     */     
/*     */     abstract int nodeAggregate(TreeMultiset.AvlNode<?> param1AvlNode);
/*     */     
/*     */     abstract long treeAggregate(@Nullable TreeMultiset.AvlNode<?> param1AvlNode);
/*     */   }
/*     */   
/*     */   private long aggregateForEntries(Aggregate aggr) {
/* 162 */     AvlNode<E> root = this.rootReference.get();
/* 163 */     long total = aggr.treeAggregate(root);
/* 164 */     if (this.range.hasLowerBound()) {
/* 165 */       total -= aggregateBelowRange(aggr, root);
/*     */     }
/* 167 */     if (this.range.hasUpperBound()) {
/* 168 */       total -= aggregateAboveRange(aggr, root);
/*     */     }
/* 170 */     return total;
/*     */   }
/*     */   
/*     */   private long aggregateBelowRange(Aggregate aggr, @Nullable AvlNode<E> node) {
/* 174 */     if (node == null) {
/* 175 */       return 0L;
/*     */     }
/* 177 */     int cmp = comparator().compare(this.range.getLowerEndpoint(), node.elem);
/* 178 */     if (cmp < 0)
/* 179 */       return aggregateBelowRange(aggr, node.left); 
/* 180 */     if (cmp == 0) {
/* 181 */       switch (this.range.getLowerBoundType()) {
/*     */         case OPEN:
/* 183 */           return aggr.nodeAggregate(node) + aggr.treeAggregate(node.left);
/*     */         case CLOSED:
/* 185 */           return aggr.treeAggregate(node.left);
/*     */       } 
/* 187 */       throw new AssertionError();
/*     */     } 
/*     */     
/* 190 */     return aggr.treeAggregate(node.left) + aggr.nodeAggregate(node) + aggregateBelowRange(aggr, node.right);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private long aggregateAboveRange(Aggregate aggr, @Nullable AvlNode<E> node) {
/* 196 */     if (node == null) {
/* 197 */       return 0L;
/*     */     }
/* 199 */     int cmp = comparator().compare(this.range.getUpperEndpoint(), node.elem);
/* 200 */     if (cmp > 0)
/* 201 */       return aggregateAboveRange(aggr, node.right); 
/* 202 */     if (cmp == 0) {
/* 203 */       switch (this.range.getUpperBoundType()) {
/*     */         case OPEN:
/* 205 */           return aggr.nodeAggregate(node) + aggr.treeAggregate(node.right);
/*     */         case CLOSED:
/* 207 */           return aggr.treeAggregate(node.right);
/*     */       } 
/* 209 */       throw new AssertionError();
/*     */     } 
/*     */     
/* 212 */     return aggr.treeAggregate(node.right) + aggr.nodeAggregate(node) + aggregateAboveRange(aggr, node.left);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int size() {
/* 219 */     return Ints.saturatedCast(aggregateForEntries(Aggregate.SIZE));
/*     */   }
/*     */ 
/*     */   
/*     */   int distinctElements() {
/* 224 */     return Ints.saturatedCast(aggregateForEntries(Aggregate.DISTINCT));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int count(@Nullable Object element) {
/*     */     try {
/* 231 */       E e = (E)element;
/* 232 */       AvlNode<E> root = this.rootReference.get();
/* 233 */       if (!this.range.contains(e) || root == null) {
/* 234 */         return 0;
/*     */       }
/* 236 */       return root.count(comparator(), e);
/* 237 */     } catch (ClassCastException e) {
/* 238 */       return 0;
/* 239 */     } catch (NullPointerException e) {
/* 240 */       return 0;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int add(@Nullable E element, int occurrences) {
/* 246 */     CollectPreconditions.checkNonnegative(occurrences, "occurrences");
/* 247 */     if (occurrences == 0) {
/* 248 */       return count(element);
/*     */     }
/* 250 */     Preconditions.checkArgument(this.range.contains(element));
/* 251 */     AvlNode<E> root = this.rootReference.get();
/* 252 */     if (root == null) {
/* 253 */       comparator().compare(element, element);
/* 254 */       AvlNode<E> avlNode = new AvlNode<E>(element, occurrences);
/* 255 */       successor(this.header, avlNode, this.header);
/* 256 */       this.rootReference.checkAndSet(root, avlNode);
/* 257 */       return 0;
/*     */     } 
/* 259 */     int[] result = new int[1];
/* 260 */     AvlNode<E> newRoot = root.add(comparator(), element, occurrences, result);
/* 261 */     this.rootReference.checkAndSet(root, newRoot);
/* 262 */     return result[0];
/*     */   }
/*     */   
/*     */   public int remove(@Nullable Object element, int occurrences) {
/*     */     AvlNode<E> newRoot;
/* 267 */     CollectPreconditions.checkNonnegative(occurrences, "occurrences");
/* 268 */     if (occurrences == 0) {
/* 269 */       return count(element);
/*     */     }
/* 271 */     AvlNode<E> root = this.rootReference.get();
/* 272 */     int[] result = new int[1];
/*     */ 
/*     */     
/*     */     try {
/* 276 */       E e = (E)element;
/* 277 */       if (!this.range.contains(e) || root == null) {
/* 278 */         return 0;
/*     */       }
/* 280 */       newRoot = root.remove(comparator(), e, occurrences, result);
/* 281 */     } catch (ClassCastException e) {
/* 282 */       return 0;
/* 283 */     } catch (NullPointerException e) {
/* 284 */       return 0;
/*     */     } 
/* 286 */     this.rootReference.checkAndSet(root, newRoot);
/* 287 */     return result[0];
/*     */   }
/*     */ 
/*     */   
/*     */   public int setCount(@Nullable E element, int count) {
/* 292 */     CollectPreconditions.checkNonnegative(count, "count");
/* 293 */     if (!this.range.contains(element)) {
/* 294 */       Preconditions.checkArgument((count == 0));
/* 295 */       return 0;
/*     */     } 
/*     */     
/* 298 */     AvlNode<E> root = this.rootReference.get();
/* 299 */     if (root == null) {
/* 300 */       if (count > 0) {
/* 301 */         add(element, count);
/*     */       }
/* 303 */       return 0;
/*     */     } 
/* 305 */     int[] result = new int[1];
/* 306 */     AvlNode<E> newRoot = root.setCount(comparator(), element, count, result);
/* 307 */     this.rootReference.checkAndSet(root, newRoot);
/* 308 */     return result[0];
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean setCount(@Nullable E element, int oldCount, int newCount) {
/* 313 */     CollectPreconditions.checkNonnegative(newCount, "newCount");
/* 314 */     CollectPreconditions.checkNonnegative(oldCount, "oldCount");
/* 315 */     Preconditions.checkArgument(this.range.contains(element));
/*     */     
/* 317 */     AvlNode<E> root = this.rootReference.get();
/* 318 */     if (root == null) {
/* 319 */       if (oldCount == 0) {
/* 320 */         if (newCount > 0) {
/* 321 */           add(element, newCount);
/*     */         }
/* 323 */         return true;
/*     */       } 
/* 325 */       return false;
/*     */     } 
/*     */     
/* 328 */     int[] result = new int[1];
/* 329 */     AvlNode<E> newRoot = root.setCount(comparator(), element, oldCount, newCount, result);
/* 330 */     this.rootReference.checkAndSet(root, newRoot);
/* 331 */     return (result[0] == oldCount);
/*     */   }
/*     */   
/*     */   private Multiset.Entry<E> wrapEntry(final AvlNode<E> baseEntry) {
/* 335 */     return new Multisets.AbstractEntry<E>()
/*     */       {
/*     */         public E getElement() {
/* 338 */           return baseEntry.getElement();
/*     */         }
/*     */ 
/*     */         
/*     */         public int getCount() {
/* 343 */           int result = baseEntry.getCount();
/* 344 */           if (result == 0) {
/* 345 */             return TreeMultiset.this.count(getElement());
/*     */           }
/* 347 */           return result;
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private AvlNode<E> firstNode() {
/* 357 */     AvlNode<E> node, root = this.rootReference.get();
/* 358 */     if (root == null) {
/* 359 */       return null;
/*     */     }
/*     */     
/* 362 */     if (this.range.hasLowerBound()) {
/* 363 */       E endpoint = this.range.getLowerEndpoint();
/* 364 */       node = ((AvlNode)this.rootReference.get()).ceiling(comparator(), endpoint);
/* 365 */       if (node == null) {
/* 366 */         return null;
/*     */       }
/* 368 */       if (this.range.getLowerBoundType() == BoundType.OPEN && comparator().compare(endpoint, node.getElement()) == 0)
/*     */       {
/* 370 */         node = node.succ;
/*     */       }
/*     */     } else {
/* 373 */       node = this.header.succ;
/*     */     } 
/* 375 */     return (node == this.header || !this.range.contains(node.getElement())) ? null : node;
/*     */   }
/*     */   @Nullable
/*     */   private AvlNode<E> lastNode() {
/* 379 */     AvlNode<E> node, root = this.rootReference.get();
/* 380 */     if (root == null) {
/* 381 */       return null;
/*     */     }
/*     */     
/* 384 */     if (this.range.hasUpperBound()) {
/* 385 */       E endpoint = this.range.getUpperEndpoint();
/* 386 */       node = ((AvlNode)this.rootReference.get()).floor(comparator(), endpoint);
/* 387 */       if (node == null) {
/* 388 */         return null;
/*     */       }
/* 390 */       if (this.range.getUpperBoundType() == BoundType.OPEN && comparator().compare(endpoint, node.getElement()) == 0)
/*     */       {
/* 392 */         node = node.pred;
/*     */       }
/*     */     } else {
/* 395 */       node = this.header.pred;
/*     */     } 
/* 397 */     return (node == this.header || !this.range.contains(node.getElement())) ? null : node;
/*     */   }
/*     */ 
/*     */   
/*     */   Iterator<Multiset.Entry<E>> entryIterator() {
/* 402 */     return (Iterator)new Iterator<Multiset.Entry<Multiset.Entry<E>>>() {
/* 403 */         TreeMultiset.AvlNode<E> current = TreeMultiset.this.firstNode();
/*     */         
/*     */         Multiset.Entry<E> prevEntry;
/*     */         
/*     */         public boolean hasNext() {
/* 408 */           if (this.current == null)
/* 409 */             return false; 
/* 410 */           if (TreeMultiset.this.range.tooHigh(this.current.getElement())) {
/* 411 */             this.current = null;
/* 412 */             return false;
/*     */           } 
/* 414 */           return true;
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*     */         public Multiset.Entry<E> next() {
/* 420 */           if (!hasNext()) {
/* 421 */             throw new NoSuchElementException();
/*     */           }
/* 423 */           Multiset.Entry<E> result = TreeMultiset.this.wrapEntry(this.current);
/* 424 */           this.prevEntry = result;
/* 425 */           if (this.current.succ == TreeMultiset.this.header) {
/* 426 */             this.current = null;
/*     */           } else {
/* 428 */             this.current = this.current.succ;
/*     */           } 
/* 430 */           return result;
/*     */         }
/*     */ 
/*     */         
/*     */         public void remove() {
/* 435 */           CollectPreconditions.checkRemove((this.prevEntry != null));
/* 436 */           TreeMultiset.this.setCount(this.prevEntry.getElement(), 0);
/* 437 */           this.prevEntry = null;
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   Iterator<Multiset.Entry<E>> descendingEntryIterator() {
/* 444 */     return (Iterator)new Iterator<Multiset.Entry<Multiset.Entry<E>>>() {
/* 445 */         TreeMultiset.AvlNode<E> current = TreeMultiset.this.lastNode();
/* 446 */         Multiset.Entry<E> prevEntry = null;
/*     */ 
/*     */         
/*     */         public boolean hasNext() {
/* 450 */           if (this.current == null)
/* 451 */             return false; 
/* 452 */           if (TreeMultiset.this.range.tooLow(this.current.getElement())) {
/* 453 */             this.current = null;
/* 454 */             return false;
/*     */           } 
/* 456 */           return true;
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*     */         public Multiset.Entry<E> next() {
/* 462 */           if (!hasNext()) {
/* 463 */             throw new NoSuchElementException();
/*     */           }
/* 465 */           Multiset.Entry<E> result = TreeMultiset.this.wrapEntry(this.current);
/* 466 */           this.prevEntry = result;
/* 467 */           if (this.current.pred == TreeMultiset.this.header) {
/* 468 */             this.current = null;
/*     */           } else {
/* 470 */             this.current = this.current.pred;
/*     */           } 
/* 472 */           return result;
/*     */         }
/*     */ 
/*     */         
/*     */         public void remove() {
/* 477 */           CollectPreconditions.checkRemove((this.prevEntry != null));
/* 478 */           TreeMultiset.this.setCount(this.prevEntry.getElement(), 0);
/* 479 */           this.prevEntry = null;
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   public SortedMultiset<E> headMultiset(@Nullable E upperBound, BoundType boundType) {
/* 486 */     return new TreeMultiset(this.rootReference, this.range.intersect(GeneralRange.upTo(comparator(), upperBound, boundType)), this.header);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SortedMultiset<E> tailMultiset(@Nullable E lowerBound, BoundType boundType) {
/* 494 */     return new TreeMultiset(this.rootReference, this.range.intersect(GeneralRange.downTo(comparator(), lowerBound, boundType)), this.header);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static int distinctElements(@Nullable AvlNode<?> node) {
/* 501 */     return (node == null) ? 0 : node.distinctElements;
/*     */   }
/*     */   private static final class Reference<T> { @Nullable
/*     */     private T value;
/*     */     
/*     */     @Nullable
/*     */     public T get() {
/* 508 */       return this.value;
/*     */     }
/*     */     private Reference() {}
/*     */     public void checkAndSet(@Nullable T expected, T newValue) {
/* 512 */       if (this.value != expected) {
/* 513 */         throw new ConcurrentModificationException();
/*     */       }
/* 515 */       this.value = newValue;
/*     */     } }
/*     */ 
/*     */   
/*     */   private static final class AvlNode<E>
/*     */     extends Multisets.AbstractEntry<E>
/*     */   {
/*     */     @Nullable
/*     */     private final E elem;
/*     */     private int elemCount;
/*     */     private int distinctElements;
/*     */     private long totalCount;
/*     */     private int height;
/*     */     private AvlNode<E> left;
/*     */     private AvlNode<E> right;
/*     */     private AvlNode<E> pred;
/*     */     private AvlNode<E> succ;
/*     */     
/*     */     AvlNode(@Nullable E elem, int elemCount) {
/* 534 */       Preconditions.checkArgument((elemCount > 0));
/* 535 */       this.elem = elem;
/* 536 */       this.elemCount = elemCount;
/* 537 */       this.totalCount = elemCount;
/* 538 */       this.distinctElements = 1;
/* 539 */       this.height = 1;
/* 540 */       this.left = null;
/* 541 */       this.right = null;
/*     */     }
/*     */     
/*     */     public int count(Comparator<? super E> comparator, E e) {
/* 545 */       int cmp = comparator.compare(e, this.elem);
/* 546 */       if (cmp < 0)
/* 547 */         return (this.left == null) ? 0 : this.left.count(comparator, e); 
/* 548 */       if (cmp > 0) {
/* 549 */         return (this.right == null) ? 0 : this.right.count(comparator, e);
/*     */       }
/* 551 */       return this.elemCount;
/*     */     }
/*     */ 
/*     */     
/*     */     private AvlNode<E> addRightChild(E e, int count) {
/* 556 */       this.right = new AvlNode(e, count);
/* 557 */       TreeMultiset.successor(this, this.right, this.succ);
/* 558 */       this.height = Math.max(2, this.height);
/* 559 */       this.distinctElements++;
/* 560 */       this.totalCount += count;
/* 561 */       return this;
/*     */     }
/*     */     
/*     */     private AvlNode<E> addLeftChild(E e, int count) {
/* 565 */       this.left = new AvlNode(e, count);
/* 566 */       TreeMultiset.successor(this.pred, this.left, this);
/* 567 */       this.height = Math.max(2, this.height);
/* 568 */       this.distinctElements++;
/* 569 */       this.totalCount += count;
/* 570 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     AvlNode<E> add(Comparator<? super E> comparator, @Nullable E e, int count, int[] result) {
/* 578 */       int cmp = comparator.compare(e, this.elem);
/* 579 */       if (cmp < 0) {
/* 580 */         AvlNode<E> initLeft = this.left;
/* 581 */         if (initLeft == null) {
/* 582 */           result[0] = 0;
/* 583 */           return addLeftChild(e, count);
/*     */         } 
/* 585 */         int initHeight = initLeft.height;
/*     */         
/* 587 */         this.left = initLeft.add(comparator, e, count, result);
/* 588 */         if (result[0] == 0) {
/* 589 */           this.distinctElements++;
/*     */         }
/* 591 */         this.totalCount += count;
/* 592 */         return (this.left.height == initHeight) ? this : rebalance();
/* 593 */       }  if (cmp > 0) {
/* 594 */         AvlNode<E> initRight = this.right;
/* 595 */         if (initRight == null) {
/* 596 */           result[0] = 0;
/* 597 */           return addRightChild(e, count);
/*     */         } 
/* 599 */         int initHeight = initRight.height;
/*     */         
/* 601 */         this.right = initRight.add(comparator, e, count, result);
/* 602 */         if (result[0] == 0) {
/* 603 */           this.distinctElements++;
/*     */         }
/* 605 */         this.totalCount += count;
/* 606 */         return (this.right.height == initHeight) ? this : rebalance();
/*     */       } 
/*     */ 
/*     */       
/* 610 */       result[0] = this.elemCount;
/* 611 */       long resultCount = this.elemCount + count;
/* 612 */       Preconditions.checkArgument((resultCount <= 2147483647L));
/* 613 */       this.elemCount += count;
/* 614 */       this.totalCount += count;
/* 615 */       return this;
/*     */     }
/*     */     
/*     */     AvlNode<E> remove(Comparator<? super E> comparator, @Nullable E e, int count, int[] result) {
/* 619 */       int cmp = comparator.compare(e, this.elem);
/* 620 */       if (cmp < 0) {
/* 621 */         AvlNode<E> initLeft = this.left;
/* 622 */         if (initLeft == null) {
/* 623 */           result[0] = 0;
/* 624 */           return this;
/*     */         } 
/*     */         
/* 627 */         this.left = initLeft.remove(comparator, e, count, result);
/*     */         
/* 629 */         if (result[0] > 0) {
/* 630 */           if (count >= result[0]) {
/* 631 */             this.distinctElements--;
/* 632 */             this.totalCount -= result[0];
/*     */           } else {
/* 634 */             this.totalCount -= count;
/*     */           } 
/*     */         }
/* 637 */         return (result[0] == 0) ? this : rebalance();
/* 638 */       }  if (cmp > 0) {
/* 639 */         AvlNode<E> initRight = this.right;
/* 640 */         if (initRight == null) {
/* 641 */           result[0] = 0;
/* 642 */           return this;
/*     */         } 
/*     */         
/* 645 */         this.right = initRight.remove(comparator, e, count, result);
/*     */         
/* 647 */         if (result[0] > 0) {
/* 648 */           if (count >= result[0]) {
/* 649 */             this.distinctElements--;
/* 650 */             this.totalCount -= result[0];
/*     */           } else {
/* 652 */             this.totalCount -= count;
/*     */           } 
/*     */         }
/* 655 */         return rebalance();
/*     */       } 
/*     */ 
/*     */       
/* 659 */       result[0] = this.elemCount;
/* 660 */       if (count >= this.elemCount) {
/* 661 */         return deleteMe();
/*     */       }
/* 663 */       this.elemCount -= count;
/* 664 */       this.totalCount -= count;
/* 665 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     AvlNode<E> setCount(Comparator<? super E> comparator, @Nullable E e, int count, int[] result) {
/* 670 */       int cmp = comparator.compare(e, this.elem);
/* 671 */       if (cmp < 0) {
/* 672 */         AvlNode<E> initLeft = this.left;
/* 673 */         if (initLeft == null) {
/* 674 */           result[0] = 0;
/* 675 */           return (count > 0) ? addLeftChild(e, count) : this;
/*     */         } 
/*     */         
/* 678 */         this.left = initLeft.setCount(comparator, e, count, result);
/*     */         
/* 680 */         if (count == 0 && result[0] != 0) {
/* 681 */           this.distinctElements--;
/* 682 */         } else if (count > 0 && result[0] == 0) {
/* 683 */           this.distinctElements++;
/*     */         } 
/*     */         
/* 686 */         this.totalCount += (count - result[0]);
/* 687 */         return rebalance();
/* 688 */       }  if (cmp > 0) {
/* 689 */         AvlNode<E> initRight = this.right;
/* 690 */         if (initRight == null) {
/* 691 */           result[0] = 0;
/* 692 */           return (count > 0) ? addRightChild(e, count) : this;
/*     */         } 
/*     */         
/* 695 */         this.right = initRight.setCount(comparator, e, count, result);
/*     */         
/* 697 */         if (count == 0 && result[0] != 0) {
/* 698 */           this.distinctElements--;
/* 699 */         } else if (count > 0 && result[0] == 0) {
/* 700 */           this.distinctElements++;
/*     */         } 
/*     */         
/* 703 */         this.totalCount += (count - result[0]);
/* 704 */         return rebalance();
/*     */       } 
/*     */ 
/*     */       
/* 708 */       result[0] = this.elemCount;
/* 709 */       if (count == 0) {
/* 710 */         return deleteMe();
/*     */       }
/* 712 */       this.totalCount += (count - this.elemCount);
/* 713 */       this.elemCount = count;
/* 714 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     AvlNode<E> setCount(Comparator<? super E> comparator, @Nullable E e, int expectedCount, int newCount, int[] result) {
/* 723 */       int cmp = comparator.compare(e, this.elem);
/* 724 */       if (cmp < 0) {
/* 725 */         AvlNode<E> initLeft = this.left;
/* 726 */         if (initLeft == null) {
/* 727 */           result[0] = 0;
/* 728 */           if (expectedCount == 0 && newCount > 0) {
/* 729 */             return addLeftChild(e, newCount);
/*     */           }
/* 731 */           return this;
/*     */         } 
/*     */         
/* 734 */         this.left = initLeft.setCount(comparator, e, expectedCount, newCount, result);
/*     */         
/* 736 */         if (result[0] == expectedCount) {
/* 737 */           if (newCount == 0 && result[0] != 0) {
/* 738 */             this.distinctElements--;
/* 739 */           } else if (newCount > 0 && result[0] == 0) {
/* 740 */             this.distinctElements++;
/*     */           } 
/* 742 */           this.totalCount += (newCount - result[0]);
/*     */         } 
/* 744 */         return rebalance();
/* 745 */       }  if (cmp > 0) {
/* 746 */         AvlNode<E> initRight = this.right;
/* 747 */         if (initRight == null) {
/* 748 */           result[0] = 0;
/* 749 */           if (expectedCount == 0 && newCount > 0) {
/* 750 */             return addRightChild(e, newCount);
/*     */           }
/* 752 */           return this;
/*     */         } 
/*     */         
/* 755 */         this.right = initRight.setCount(comparator, e, expectedCount, newCount, result);
/*     */         
/* 757 */         if (result[0] == expectedCount) {
/* 758 */           if (newCount == 0 && result[0] != 0) {
/* 759 */             this.distinctElements--;
/* 760 */           } else if (newCount > 0 && result[0] == 0) {
/* 761 */             this.distinctElements++;
/*     */           } 
/* 763 */           this.totalCount += (newCount - result[0]);
/*     */         } 
/* 765 */         return rebalance();
/*     */       } 
/*     */ 
/*     */       
/* 769 */       result[0] = this.elemCount;
/* 770 */       if (expectedCount == this.elemCount) {
/* 771 */         if (newCount == 0) {
/* 772 */           return deleteMe();
/*     */         }
/* 774 */         this.totalCount += (newCount - this.elemCount);
/* 775 */         this.elemCount = newCount;
/*     */       } 
/* 777 */       return this;
/*     */     }
/*     */     
/*     */     private AvlNode<E> deleteMe() {
/* 781 */       int oldElemCount = this.elemCount;
/* 782 */       this.elemCount = 0;
/* 783 */       TreeMultiset.successor(this.pred, this.succ);
/* 784 */       if (this.left == null)
/* 785 */         return this.right; 
/* 786 */       if (this.right == null)
/* 787 */         return this.left; 
/* 788 */       if (this.left.height >= this.right.height) {
/* 789 */         AvlNode<E> avlNode = this.pred;
/*     */         
/* 791 */         avlNode.left = this.left.removeMax(avlNode);
/* 792 */         avlNode.right = this.right;
/* 793 */         this.distinctElements--;
/* 794 */         this.totalCount -= oldElemCount;
/* 795 */         return avlNode.rebalance();
/*     */       } 
/* 797 */       AvlNode<E> newTop = this.succ;
/* 798 */       newTop.right = this.right.removeMin(newTop);
/* 799 */       newTop.left = this.left;
/* 800 */       this.distinctElements--;
/* 801 */       this.totalCount -= oldElemCount;
/* 802 */       return newTop.rebalance();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     private AvlNode<E> removeMin(AvlNode<E> node) {
/* 808 */       if (this.left == null) {
/* 809 */         return this.right;
/*     */       }
/* 811 */       this.left = this.left.removeMin(node);
/* 812 */       this.distinctElements--;
/* 813 */       this.totalCount -= node.elemCount;
/* 814 */       return rebalance();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     private AvlNode<E> removeMax(AvlNode<E> node) {
/* 820 */       if (this.right == null) {
/* 821 */         return this.left;
/*     */       }
/* 823 */       this.right = this.right.removeMax(node);
/* 824 */       this.distinctElements--;
/* 825 */       this.totalCount -= node.elemCount;
/* 826 */       return rebalance();
/*     */     }
/*     */ 
/*     */     
/*     */     private void recomputeMultiset() {
/* 831 */       this.distinctElements = 1 + TreeMultiset.distinctElements(this.left) + TreeMultiset.distinctElements(this.right);
/*     */       
/* 833 */       this.totalCount = this.elemCount + totalCount(this.left) + totalCount(this.right);
/*     */     }
/*     */     
/*     */     private void recomputeHeight() {
/* 837 */       this.height = 1 + Math.max(height(this.left), height(this.right));
/*     */     }
/*     */     
/*     */     private void recompute() {
/* 841 */       recomputeMultiset();
/* 842 */       recomputeHeight();
/*     */     }
/*     */     
/*     */     private AvlNode<E> rebalance() {
/* 846 */       switch (balanceFactor()) {
/*     */         case -2:
/* 848 */           if (this.right.balanceFactor() > 0) {
/* 849 */             this.right = this.right.rotateRight();
/*     */           }
/* 851 */           return rotateLeft();
/*     */         case 2:
/* 853 */           if (this.left.balanceFactor() < 0) {
/* 854 */             this.left = this.left.rotateLeft();
/*     */           }
/* 856 */           return rotateRight();
/*     */       } 
/* 858 */       recomputeHeight();
/* 859 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     private int balanceFactor() {
/* 864 */       return height(this.left) - height(this.right);
/*     */     }
/*     */     
/*     */     private AvlNode<E> rotateLeft() {
/* 868 */       Preconditions.checkState((this.right != null));
/* 869 */       AvlNode<E> newTop = this.right;
/* 870 */       this.right = newTop.left;
/* 871 */       newTop.left = this;
/* 872 */       newTop.totalCount = this.totalCount;
/* 873 */       newTop.distinctElements = this.distinctElements;
/* 874 */       recompute();
/* 875 */       newTop.recomputeHeight();
/* 876 */       return newTop;
/*     */     }
/*     */     
/*     */     private AvlNode<E> rotateRight() {
/* 880 */       Preconditions.checkState((this.left != null));
/* 881 */       AvlNode<E> newTop = this.left;
/* 882 */       this.left = newTop.right;
/* 883 */       newTop.right = this;
/* 884 */       newTop.totalCount = this.totalCount;
/* 885 */       newTop.distinctElements = this.distinctElements;
/* 886 */       recompute();
/* 887 */       newTop.recomputeHeight();
/* 888 */       return newTop;
/*     */     }
/*     */     
/*     */     private static long totalCount(@Nullable AvlNode<?> node) {
/* 892 */       return (node == null) ? 0L : node.totalCount;
/*     */     }
/*     */     
/*     */     private static int height(@Nullable AvlNode<?> node) {
/* 896 */       return (node == null) ? 0 : node.height;
/*     */     }
/*     */     @Nullable
/*     */     private AvlNode<E> ceiling(Comparator<? super E> comparator, E e) {
/* 900 */       int cmp = comparator.compare(e, this.elem);
/* 901 */       if (cmp < 0)
/* 902 */         return (this.left == null) ? this : (AvlNode<E>)MoreObjects.firstNonNull(this.left.ceiling(comparator, e), this); 
/* 903 */       if (cmp == 0) {
/* 904 */         return this;
/*     */       }
/* 906 */       return (this.right == null) ? null : this.right.ceiling(comparator, e);
/*     */     }
/*     */     
/*     */     @Nullable
/*     */     private AvlNode<E> floor(Comparator<? super E> comparator, E e) {
/* 911 */       int cmp = comparator.compare(e, this.elem);
/* 912 */       if (cmp > 0)
/* 913 */         return (this.right == null) ? this : (AvlNode<E>)MoreObjects.firstNonNull(this.right.floor(comparator, e), this); 
/* 914 */       if (cmp == 0) {
/* 915 */         return this;
/*     */       }
/* 917 */       return (this.left == null) ? null : this.left.floor(comparator, e);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public E getElement() {
/* 923 */       return this.elem;
/*     */     }
/*     */ 
/*     */     
/*     */     public int getCount() {
/* 928 */       return this.elemCount;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 933 */       return Multisets.<E>immutableEntry(getElement(), getCount()).toString();
/*     */     }
/*     */   }
/*     */   
/*     */   private static <T> void successor(AvlNode<T> a, AvlNode<T> b) {
/* 938 */     a.succ = b;
/* 939 */     b.pred = a;
/*     */   }
/*     */   
/*     */   private static <T> void successor(AvlNode<T> a, AvlNode<T> b, AvlNode<T> c) {
/* 943 */     successor(a, b);
/* 944 */     successor(b, c);
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
/*     */   @GwtIncompatible("java.io.ObjectOutputStream")
/*     */   private void writeObject(ObjectOutputStream stream) throws IOException {
/* 959 */     stream.defaultWriteObject();
/* 960 */     stream.writeObject(elementSet().comparator());
/* 961 */     Serialization.writeMultiset(this, stream);
/*     */   }
/*     */   
/*     */   @GwtIncompatible("java.io.ObjectInputStream")
/*     */   private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
/* 966 */     stream.defaultReadObject();
/*     */ 
/*     */     
/* 969 */     Comparator<? super E> comparator = (Comparator<? super E>)stream.readObject();
/* 970 */     Serialization.<AbstractSortedMultiset>getFieldSetter(AbstractSortedMultiset.class, "comparator").set(this, comparator);
/* 971 */     Serialization.<TreeMultiset<E>>getFieldSetter((Class)TreeMultiset.class, "range").set(this, GeneralRange.all(comparator));
/*     */ 
/*     */     
/* 974 */     Serialization.<TreeMultiset<E>>getFieldSetter((Class)TreeMultiset.class, "rootReference").set(this, new Reference());
/*     */ 
/*     */     
/* 977 */     AvlNode<E> header = new AvlNode<E>(null, 1);
/* 978 */     Serialization.<TreeMultiset<E>>getFieldSetter((Class)TreeMultiset.class, "header").set(this, header);
/* 979 */     successor(header, header);
/* 980 */     Serialization.populateMultiset(this, stream);
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\collect\TreeMultiset.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */