/*     */ package org.jivesoftware.smack.util;
/*     */ 
/*     */ import java.util.AbstractQueue;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.concurrent.BlockingQueue;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.locks.Condition;
/*     */ import java.util.concurrent.locks.ReentrantLock;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ArrayBlockingQueueWithShutdown<E>
/*     */   extends AbstractQueue<E>
/*     */   implements BlockingQueue<E>
/*     */ {
/*     */   private final E[] items;
/*     */   private int takeIndex;
/*     */   private int putIndex;
/*     */   private int count;
/*     */   private final ReentrantLock lock;
/*     */   private final Condition notEmpty;
/*     */   private final Condition notFull;
/*     */   private volatile boolean isShutdown = false;
/*     */   
/*     */   private final int inc(int i) {
/*  57 */     return (++i == this.items.length) ? 0 : i;
/*     */   }
/*     */   
/*     */   private final void insert(E e) {
/*  61 */     this.items[this.putIndex] = e;
/*  62 */     this.putIndex = inc(this.putIndex);
/*  63 */     this.count++;
/*  64 */     this.notEmpty.signal();
/*     */   }
/*     */   
/*     */   private final E extract() {
/*  68 */     E e = this.items[this.takeIndex];
/*  69 */     this.items[this.takeIndex] = null;
/*  70 */     this.takeIndex = inc(this.takeIndex);
/*  71 */     this.count--;
/*  72 */     this.notFull.signal();
/*  73 */     return e;
/*     */   }
/*     */   
/*     */   private final void removeAt(int i) {
/*  77 */     if (i == this.takeIndex) {
/*  78 */       this.items[this.takeIndex] = null;
/*  79 */       this.takeIndex = inc(this.takeIndex);
/*     */     } else {
/*     */       
/*     */       while (true) {
/*  83 */         int nexti = inc(i);
/*  84 */         if (nexti != this.putIndex) {
/*  85 */           this.items[i] = this.items[nexti];
/*  86 */           i = nexti; continue;
/*     */         }  break;
/*     */       } 
/*  89 */       this.items[i] = null;
/*  90 */       this.putIndex = i;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*  95 */     this.count--;
/*  96 */     this.notFull.signal();
/*     */   }
/*     */   
/*     */   private static final void checkNotNull(Object o) {
/* 100 */     if (o == null) {
/* 101 */       throw new NullPointerException();
/*     */     }
/*     */   }
/*     */   
/*     */   private final void checkNotShutdown() throws InterruptedException {
/* 106 */     if (this.isShutdown) {
/* 107 */       throw new InterruptedException();
/*     */     }
/*     */   }
/*     */   
/*     */   private final boolean hasNoElements() {
/* 112 */     return (this.count == 0);
/*     */   }
/*     */   
/*     */   private final boolean hasElements() {
/* 116 */     return !hasNoElements();
/*     */   }
/*     */   
/*     */   private final boolean isFull() {
/* 120 */     return (this.count == this.items.length);
/*     */   }
/*     */   
/*     */   private final boolean isNotFull() {
/* 124 */     return !isFull();
/*     */   }
/*     */   
/*     */   public ArrayBlockingQueueWithShutdown(int capacity) {
/* 128 */     this(capacity, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public ArrayBlockingQueueWithShutdown(int capacity, boolean fair) {
/* 133 */     if (capacity <= 0)
/* 134 */       throw new IllegalArgumentException(); 
/* 135 */     this.items = (E[])new Object[capacity];
/* 136 */     this.lock = new ReentrantLock(fair);
/* 137 */     this.notEmpty = this.lock.newCondition();
/* 138 */     this.notFull = this.lock.newCondition();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void shutdown() {
/* 146 */     this.lock.lock();
/*     */     try {
/* 148 */       this.isShutdown = true;
/* 149 */       this.notEmpty.signalAll();
/* 150 */       this.notFull.signalAll();
/*     */     } finally {
/*     */       
/* 153 */       this.lock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void start() {
/* 162 */     this.lock.lock();
/*     */     try {
/* 164 */       this.isShutdown = false;
/*     */     } finally {
/*     */       
/* 167 */       this.lock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isShutdown() {
/* 177 */     this.lock.lock();
/*     */     try {
/* 179 */       return this.isShutdown;
/*     */     } finally {
/* 181 */       this.lock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public E poll() {
/* 187 */     this.lock.lock();
/*     */     try {
/* 189 */       if (hasNoElements()) {
/* 190 */         return null;
/*     */       }
/* 192 */       E e = extract();
/* 193 */       return e;
/*     */     } finally {
/*     */       
/* 196 */       this.lock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public E peek() {
/* 202 */     this.lock.lock();
/*     */     try {
/* 204 */       return hasNoElements() ? null : this.items[this.takeIndex];
/*     */     } finally {
/*     */       
/* 207 */       this.lock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean offer(E e) {
/* 213 */     checkNotNull(e);
/* 214 */     this.lock.lock();
/*     */     try {
/* 216 */       if (isFull() || this.isShutdown) {
/* 217 */         return false;
/*     */       }
/*     */       
/* 220 */       insert(e);
/* 221 */       return true;
/*     */     }
/*     */     finally {
/*     */       
/* 225 */       this.lock.unlock();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void put(E e) throws InterruptedException {
/* 245 */     checkNotNull(e);
/* 246 */     this.lock.lockInterruptibly();
/*     */     
/*     */     try {
/* 249 */       while (isFull()) {
/*     */         try {
/* 251 */           this.notFull.await();
/* 252 */           checkNotShutdown();
/*     */         }
/* 254 */         catch (InterruptedException ie) {
/* 255 */           this.notFull.signal();
/* 256 */           throw ie;
/*     */         } 
/*     */       } 
/* 259 */       insert(e);
/*     */     } finally {
/*     */       
/* 262 */       this.lock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean offer(E e, long timeout, TimeUnit unit) throws InterruptedException {
/* 268 */     checkNotNull(e);
/* 269 */     long nanos = unit.toNanos(timeout);
/* 270 */     this.lock.lockInterruptibly();
/*     */     try {
/*     */       while (true) {
/* 273 */         if (isNotFull()) {
/* 274 */           insert(e);
/* 275 */           return true;
/*     */         } 
/* 277 */         if (nanos <= 0L) {
/* 278 */           return false;
/*     */         }
/*     */         try {
/* 281 */           nanos = this.notFull.awaitNanos(nanos);
/* 282 */           checkNotShutdown();
/*     */         }
/* 284 */         catch (InterruptedException ie) {
/* 285 */           this.notFull.signal();
/* 286 */           throw ie;
/*     */         } 
/*     */       } 
/*     */     } finally {
/*     */       
/* 291 */       this.lock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public E take() throws InterruptedException {
/* 298 */     this.lock.lockInterruptibly();
/*     */     try {
/* 300 */       checkNotShutdown();
/*     */       try {
/* 302 */         while (hasNoElements()) {
/* 303 */           this.notEmpty.await();
/* 304 */           checkNotShutdown();
/*     */         }
/*     */       
/* 307 */       } catch (InterruptedException ie) {
/* 308 */         this.notEmpty.signal();
/* 309 */         throw ie;
/*     */       } 
/* 311 */       E e = extract();
/* 312 */       return e;
/*     */     } finally {
/*     */       
/* 315 */       this.lock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public E poll(long timeout, TimeUnit unit) throws InterruptedException {
/* 321 */     long nanos = unit.toNanos(timeout);
/* 322 */     this.lock.lockInterruptibly();
/*     */     try {
/* 324 */       checkNotShutdown();
/*     */       while (true) {
/* 326 */         if (hasElements()) {
/* 327 */           E e = extract();
/* 328 */           return e;
/*     */         } 
/* 330 */         if (nanos <= 0L) {
/* 331 */           return null;
/*     */         }
/*     */         try {
/* 334 */           nanos = this.notEmpty.awaitNanos(nanos);
/* 335 */           checkNotShutdown();
/*     */         }
/* 337 */         catch (InterruptedException ie) {
/* 338 */           this.notEmpty.signal();
/* 339 */           throw ie;
/*     */         } 
/*     */       } 
/*     */     } finally {
/*     */       
/* 344 */       this.lock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int remainingCapacity() {
/* 350 */     this.lock.lock();
/*     */     try {
/* 352 */       return this.items.length - this.count;
/*     */     } finally {
/*     */       
/* 355 */       this.lock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int drainTo(Collection<? super E> c) {
/* 361 */     checkNotNull(c);
/* 362 */     if (c == this) {
/* 363 */       throw new IllegalArgumentException();
/*     */     }
/* 365 */     this.lock.lock();
/*     */     try {
/* 367 */       int i = this.takeIndex;
/* 368 */       int n = 0;
/* 369 */       for (; n < this.count; n++) {
/* 370 */         c.add(this.items[i]);
/* 371 */         this.items[i] = null;
/* 372 */         i = inc(i);
/*     */       } 
/* 374 */       if (n > 0) {
/* 375 */         this.count = 0;
/* 376 */         this.putIndex = 0;
/* 377 */         this.takeIndex = 0;
/* 378 */         this.notFull.signalAll();
/*     */       } 
/* 380 */       return n;
/*     */     } finally {
/*     */       
/* 383 */       this.lock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int drainTo(Collection<? super E> c, int maxElements) {
/* 389 */     checkNotNull(c);
/* 390 */     if (c == this) {
/* 391 */       throw new IllegalArgumentException();
/*     */     }
/* 393 */     if (maxElements <= 0) {
/* 394 */       return 0;
/*     */     }
/* 396 */     this.lock.lock();
/*     */     try {
/* 398 */       int i = this.takeIndex;
/* 399 */       int n = 0;
/* 400 */       int max = (maxElements < this.count) ? maxElements : this.count;
/* 401 */       for (; n < max; n++) {
/* 402 */         c.add(this.items[i]);
/* 403 */         this.items[i] = null;
/* 404 */         i = inc(i);
/*     */       } 
/* 406 */       if (n > 0) {
/* 407 */         this.count -= n;
/* 408 */         this.takeIndex = i;
/* 409 */         this.notFull.signalAll();
/*     */       } 
/* 411 */       return n;
/*     */     } finally {
/*     */       
/* 414 */       this.lock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/* 420 */     this.lock.lock();
/*     */     try {
/* 422 */       return this.count;
/*     */     } finally {
/*     */       
/* 425 */       this.lock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<E> iterator() {
/* 431 */     this.lock.lock();
/*     */     try {
/* 433 */       return new Itr();
/*     */     } finally {
/*     */       
/* 436 */       this.lock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private class Itr
/*     */     implements Iterator<E>
/*     */   {
/*     */     private int nextIndex;
/*     */     private E nextItem;
/* 446 */     private int lastRet = -1; Itr() {
/* 447 */       if (ArrayBlockingQueueWithShutdown.this.count == 0) {
/* 448 */         this.nextIndex = -1;
/*     */       } else {
/*     */         
/* 451 */         this.nextIndex = ArrayBlockingQueueWithShutdown.this.takeIndex;
/* 452 */         this.nextItem = (E)ArrayBlockingQueueWithShutdown.this.items[ArrayBlockingQueueWithShutdown.this.takeIndex];
/*     */       } 
/*     */     }
/*     */     
/*     */     public boolean hasNext() {
/* 457 */       return (this.nextIndex >= 0);
/*     */     }
/*     */     
/*     */     private void checkNext() {
/* 461 */       if (this.nextIndex == ArrayBlockingQueueWithShutdown.this.putIndex) {
/* 462 */         this.nextIndex = -1;
/* 463 */         this.nextItem = null;
/*     */       } else {
/*     */         
/* 466 */         this.nextItem = (E)ArrayBlockingQueueWithShutdown.this.items[this.nextIndex];
/* 467 */         if (this.nextItem == null) {
/* 468 */           this.nextIndex = -1;
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/*     */     public E next() {
/* 474 */       ArrayBlockingQueueWithShutdown.this.lock.lock();
/*     */       try {
/* 476 */         if (this.nextIndex < 0) {
/* 477 */           throw new NoSuchElementException();
/*     */         }
/* 479 */         this.lastRet = this.nextIndex;
/* 480 */         E e = this.nextItem;
/* 481 */         this.nextIndex = ArrayBlockingQueueWithShutdown.this.inc(this.nextIndex);
/* 482 */         checkNext();
/* 483 */         return e;
/*     */       } finally {
/*     */         
/* 486 */         ArrayBlockingQueueWithShutdown.this.lock.unlock();
/*     */       } 
/*     */     }
/*     */     
/*     */     public void remove() {
/* 491 */       ArrayBlockingQueueWithShutdown.this.lock.lock();
/*     */       try {
/* 493 */         int i = this.lastRet;
/* 494 */         if (i < 0) {
/* 495 */           throw new IllegalStateException();
/*     */         }
/* 497 */         this.lastRet = -1;
/* 498 */         int ti = ArrayBlockingQueueWithShutdown.this.takeIndex;
/* 499 */         ArrayBlockingQueueWithShutdown.this.removeAt(i);
/* 500 */         this.nextIndex = (i == ti) ? ArrayBlockingQueueWithShutdown.this.takeIndex : i;
/* 501 */         checkNext();
/*     */       } finally {
/*     */         
/* 504 */         ArrayBlockingQueueWithShutdown.this.lock.unlock();
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jivesoftware\smac\\util\ArrayBlockingQueueWithShutdown.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */