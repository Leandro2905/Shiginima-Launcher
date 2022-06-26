/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.Collection;
/*     */ import java.util.Deque;
/*     */ import java.util.PriorityQueue;
/*     */ import java.util.Queue;
/*     */ import java.util.concurrent.ArrayBlockingQueue;
/*     */ import java.util.concurrent.BlockingQueue;
/*     */ import java.util.concurrent.ConcurrentLinkedQueue;
/*     */ import java.util.concurrent.LinkedBlockingDeque;
/*     */ import java.util.concurrent.LinkedBlockingQueue;
/*     */ import java.util.concurrent.PriorityBlockingQueue;
/*     */ import java.util.concurrent.SynchronousQueue;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Queues
/*     */ {
/*     */   public static <E> ArrayBlockingQueue<E> newArrayBlockingQueue(int capacity) {
/*  51 */     return new ArrayBlockingQueue<E>(capacity);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ArrayDeque<E> newArrayDeque() {
/*  62 */     return new ArrayDeque<E>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ArrayDeque<E> newArrayDeque(Iterable<? extends E> elements) {
/*  72 */     if (elements instanceof Collection) {
/*  73 */       return new ArrayDeque<E>(Collections2.cast(elements));
/*     */     }
/*  75 */     ArrayDeque<E> deque = new ArrayDeque<E>();
/*  76 */     Iterables.addAll(deque, elements);
/*  77 */     return deque;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ConcurrentLinkedQueue<E> newConcurrentLinkedQueue() {
/*  86 */     return new ConcurrentLinkedQueue<E>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ConcurrentLinkedQueue<E> newConcurrentLinkedQueue(Iterable<? extends E> elements) {
/*  95 */     if (elements instanceof Collection) {
/*  96 */       return new ConcurrentLinkedQueue<E>(Collections2.cast(elements));
/*     */     }
/*  98 */     ConcurrentLinkedQueue<E> queue = new ConcurrentLinkedQueue<E>();
/*  99 */     Iterables.addAll(queue, elements);
/* 100 */     return queue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> LinkedBlockingDeque<E> newLinkedBlockingDeque() {
/* 111 */     return new LinkedBlockingDeque<E>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> LinkedBlockingDeque<E> newLinkedBlockingDeque(int capacity) {
/* 121 */     return new LinkedBlockingDeque<E>(capacity);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> LinkedBlockingDeque<E> newLinkedBlockingDeque(Iterable<? extends E> elements) {
/* 132 */     if (elements instanceof Collection) {
/* 133 */       return new LinkedBlockingDeque<E>(Collections2.cast(elements));
/*     */     }
/* 135 */     LinkedBlockingDeque<E> deque = new LinkedBlockingDeque<E>();
/* 136 */     Iterables.addAll(deque, elements);
/* 137 */     return deque;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> LinkedBlockingQueue<E> newLinkedBlockingQueue() {
/* 146 */     return new LinkedBlockingQueue<E>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> LinkedBlockingQueue<E> newLinkedBlockingQueue(int capacity) {
/* 155 */     return new LinkedBlockingQueue<E>(capacity);
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
/*     */   public static <E> LinkedBlockingQueue<E> newLinkedBlockingQueue(Iterable<? extends E> elements) {
/* 167 */     if (elements instanceof Collection) {
/* 168 */       return new LinkedBlockingQueue<E>(Collections2.cast(elements));
/*     */     }
/* 170 */     LinkedBlockingQueue<E> queue = new LinkedBlockingQueue<E>();
/* 171 */     Iterables.addAll(queue, elements);
/* 172 */     return queue;
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
/*     */   public static <E extends Comparable> PriorityBlockingQueue<E> newPriorityBlockingQueue() {
/* 186 */     return new PriorityBlockingQueue<E>();
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
/*     */   public static <E extends Comparable> PriorityBlockingQueue<E> newPriorityBlockingQueue(Iterable<? extends E> elements) {
/* 199 */     if (elements instanceof Collection) {
/* 200 */       return new PriorityBlockingQueue<E>(Collections2.cast(elements));
/*     */     }
/* 202 */     PriorityBlockingQueue<E> queue = new PriorityBlockingQueue<E>();
/* 203 */     Iterables.addAll(queue, elements);
/* 204 */     return queue;
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
/*     */   public static <E extends Comparable> PriorityQueue<E> newPriorityQueue() {
/* 216 */     return new PriorityQueue<E>();
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
/*     */   public static <E extends Comparable> PriorityQueue<E> newPriorityQueue(Iterable<? extends E> elements) {
/* 229 */     if (elements instanceof Collection) {
/* 230 */       return new PriorityQueue<E>(Collections2.cast(elements));
/*     */     }
/* 232 */     PriorityQueue<E> queue = new PriorityQueue<E>();
/* 233 */     Iterables.addAll(queue, elements);
/* 234 */     return queue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> SynchronousQueue<E> newSynchronousQueue() {
/* 243 */     return new SynchronousQueue<E>();
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
/*     */   @Beta
/*     */   public static <E> int drain(BlockingQueue<E> q, Collection<? super E> buffer, int numElements, long timeout, TimeUnit unit) throws InterruptedException {
/* 262 */     Preconditions.checkNotNull(buffer);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 268 */     long deadline = System.nanoTime() + unit.toNanos(timeout);
/* 269 */     int added = 0;
/* 270 */     while (added < numElements) {
/*     */ 
/*     */       
/* 273 */       added += q.drainTo(buffer, numElements - added);
/* 274 */       if (added < numElements) {
/* 275 */         E e = q.poll(deadline - System.nanoTime(), TimeUnit.NANOSECONDS);
/* 276 */         if (e == null) {
/*     */           break;
/*     */         }
/* 279 */         buffer.add(e);
/* 280 */         added++;
/*     */       } 
/*     */     } 
/* 283 */     return added;
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
/*     */   @Beta
/*     */   public static <E> int drainUninterruptibly(BlockingQueue<E> q, Collection<? super E> buffer, int numElements, long timeout, TimeUnit unit) {
/* 302 */     Preconditions.checkNotNull(buffer);
/* 303 */     long deadline = System.nanoTime() + unit.toNanos(timeout);
/* 304 */     int added = 0;
/* 305 */     boolean interrupted = false;
/*     */     try {
/* 307 */       while (added < numElements) {
/*     */ 
/*     */         
/* 310 */         added += q.drainTo(buffer, numElements - added);
/* 311 */         if (added < numElements) {
/*     */           E e;
/*     */           while (true) {
/*     */             try {
/* 315 */               e = q.poll(deadline - System.nanoTime(), TimeUnit.NANOSECONDS);
/*     */               break;
/* 317 */             } catch (InterruptedException ex) {
/* 318 */               interrupted = true;
/*     */             } 
/*     */           } 
/* 321 */           if (e == null) {
/*     */             break;
/*     */           }
/* 324 */           buffer.add(e);
/* 325 */           added++;
/*     */         } 
/*     */       } 
/*     */     } finally {
/* 329 */       if (interrupted) {
/* 330 */         Thread.currentThread().interrupt();
/*     */       }
/*     */     } 
/* 333 */     return added;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> Queue<E> synchronizedQueue(Queue<E> queue) {
/* 364 */     return Synchronized.queue(queue, null);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> Deque<E> synchronizedDeque(Deque<E> deque) {
/* 395 */     return Synchronized.deque(deque, null);
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\collect\Queues.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */