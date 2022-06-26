/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.Deque;
/*     */ import java.util.Iterator;
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
/*     */ @Beta
/*     */ @GwtCompatible(emulated = true)
/*     */ public abstract class TreeTraverser<T>
/*     */ {
/*     */   public abstract Iterable<T> children(T paramT);
/*     */   
/*     */   public final FluentIterable<T> preOrderTraversal(final T root) {
/*  70 */     Preconditions.checkNotNull(root);
/*  71 */     return new FluentIterable<T>()
/*     */       {
/*     */         public UnmodifiableIterator<T> iterator() {
/*  74 */           return TreeTraverser.this.preOrderIterator((T)root);
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   UnmodifiableIterator<T> preOrderIterator(T root) {
/*  81 */     return new PreOrderIterator(root);
/*     */   }
/*     */   
/*     */   private final class PreOrderIterator extends UnmodifiableIterator<T> {
/*     */     private final Deque<Iterator<T>> stack;
/*     */     
/*     */     PreOrderIterator(T root) {
/*  88 */       this.stack = new ArrayDeque<Iterator<T>>();
/*  89 */       this.stack.addLast(Iterators.singletonIterator((T)Preconditions.checkNotNull(root)));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean hasNext() {
/*  94 */       return !this.stack.isEmpty();
/*     */     }
/*     */ 
/*     */     
/*     */     public T next() {
/*  99 */       Iterator<T> itr = this.stack.getLast();
/* 100 */       T result = (T)Preconditions.checkNotNull(itr.next());
/* 101 */       if (!itr.hasNext()) {
/* 102 */         this.stack.removeLast();
/*     */       }
/* 104 */       Iterator<T> childItr = TreeTraverser.this.children(result).iterator();
/* 105 */       if (childItr.hasNext()) {
/* 106 */         this.stack.addLast(childItr);
/*     */       }
/* 108 */       return result;
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
/*     */   public final FluentIterable<T> postOrderTraversal(final T root) {
/* 120 */     Preconditions.checkNotNull(root);
/* 121 */     return new FluentIterable<T>()
/*     */       {
/*     */         public UnmodifiableIterator<T> iterator() {
/* 124 */           return TreeTraverser.this.postOrderIterator((T)root);
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   UnmodifiableIterator<T> postOrderIterator(T root) {
/* 131 */     return new PostOrderIterator(root);
/*     */   }
/*     */   
/*     */   private static final class PostOrderNode<T> {
/*     */     final T root;
/*     */     final Iterator<T> childIterator;
/*     */     
/*     */     PostOrderNode(T root, Iterator<T> childIterator) {
/* 139 */       this.root = (T)Preconditions.checkNotNull(root);
/* 140 */       this.childIterator = (Iterator<T>)Preconditions.checkNotNull(childIterator);
/*     */     }
/*     */   }
/*     */   
/*     */   private final class PostOrderIterator extends AbstractIterator<T> {
/*     */     private final ArrayDeque<TreeTraverser.PostOrderNode<T>> stack;
/*     */     
/*     */     PostOrderIterator(T root) {
/* 148 */       this.stack = new ArrayDeque<TreeTraverser.PostOrderNode<T>>();
/* 149 */       this.stack.addLast(expand(root));
/*     */     }
/*     */ 
/*     */     
/*     */     protected T computeNext() {
/* 154 */       while (!this.stack.isEmpty()) {
/* 155 */         TreeTraverser.PostOrderNode<T> top = this.stack.getLast();
/* 156 */         if (top.childIterator.hasNext()) {
/* 157 */           T child = top.childIterator.next();
/* 158 */           this.stack.addLast(expand(child)); continue;
/*     */         } 
/* 160 */         this.stack.removeLast();
/* 161 */         return top.root;
/*     */       } 
/*     */       
/* 164 */       return endOfData();
/*     */     }
/*     */     
/*     */     private TreeTraverser.PostOrderNode<T> expand(T t) {
/* 168 */       return new TreeTraverser.PostOrderNode<T>(t, TreeTraverser.this.children(t).iterator());
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
/*     */   public final FluentIterable<T> breadthFirstTraversal(final T root) {
/* 180 */     Preconditions.checkNotNull(root);
/* 181 */     return new FluentIterable<T>()
/*     */       {
/*     */         public UnmodifiableIterator<T> iterator() {
/* 184 */           return new TreeTraverser.BreadthFirstIterator((T)root);
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   private final class BreadthFirstIterator
/*     */     extends UnmodifiableIterator<T> implements PeekingIterator<T> {
/*     */     private final Queue<T> queue;
/*     */     
/*     */     BreadthFirstIterator(T root) {
/* 194 */       this.queue = new ArrayDeque<T>();
/* 195 */       this.queue.add(root);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean hasNext() {
/* 200 */       return !this.queue.isEmpty();
/*     */     }
/*     */ 
/*     */     
/*     */     public T peek() {
/* 205 */       return this.queue.element();
/*     */     }
/*     */ 
/*     */     
/*     */     public T next() {
/* 210 */       T result = this.queue.remove();
/* 211 */       Iterables.addAll(this.queue, TreeTraverser.this.children(result));
/* 212 */       return result;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\collect\TreeTraverser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */