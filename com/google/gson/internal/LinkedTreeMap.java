/*     */ package com.google.gson.internal;
/*     */ 
/*     */ import java.io.ObjectStreamException;
/*     */ import java.io.Serializable;
/*     */ import java.util.AbstractMap;
/*     */ import java.util.AbstractSet;
/*     */ import java.util.Comparator;
/*     */ import java.util.ConcurrentModificationException;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class LinkedTreeMap<K, V>
/*     */   extends AbstractMap<K, V>
/*     */   implements Serializable
/*     */ {
/*  40 */   private static final Comparator<Comparable> NATURAL_ORDER = new Comparator<Comparable>() {
/*     */       public int compare(Comparable<Comparable> a, Comparable b) {
/*  42 */         return a.compareTo(b);
/*     */       }
/*     */     };
/*     */   
/*     */   Comparator<? super K> comparator;
/*     */   Node<K, V> root;
/*  48 */   int size = 0;
/*  49 */   int modCount = 0;
/*     */ 
/*     */   
/*  52 */   final Node<K, V> header = new Node<K, V>();
/*     */   
/*     */   private EntrySet entrySet;
/*     */   
/*     */   private KeySet keySet;
/*     */ 
/*     */   
/*     */   public LinkedTreeMap() {
/*  60 */     this((Comparator)NATURAL_ORDER);
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
/*     */   public LinkedTreeMap(Comparator<? super K> comparator) {
/*  72 */     this.comparator = (comparator != null) ? comparator : (Comparator)NATURAL_ORDER;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int size() {
/*  78 */     return this.size;
/*     */   }
/*     */   
/*     */   public V get(Object key) {
/*  82 */     Node<K, V> node = findByObject(key);
/*  83 */     return (node != null) ? node.value : null;
/*     */   }
/*     */   
/*     */   public boolean containsKey(Object key) {
/*  87 */     return (findByObject(key) != null);
/*     */   }
/*     */   
/*     */   public V put(K key, V value) {
/*  91 */     if (key == null) {
/*  92 */       throw new NullPointerException("key == null");
/*     */     }
/*  94 */     Node<K, V> created = find(key, true);
/*  95 */     V result = created.value;
/*  96 */     created.value = value;
/*  97 */     return result;
/*     */   }
/*     */   
/*     */   public void clear() {
/* 101 */     this.root = null;
/* 102 */     this.size = 0;
/* 103 */     this.modCount++;
/*     */ 
/*     */     
/* 106 */     Node<K, V> header = this.header;
/* 107 */     header.next = header.prev = header;
/*     */   }
/*     */   
/*     */   public V remove(Object key) {
/* 111 */     Node<K, V> node = removeInternalByKey(key);
/* 112 */     return (node != null) ? node.value : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Node<K, V> find(K key, boolean create) {
/*     */     Node<K, V> created;
/* 122 */     Comparator<? super K> comparator = this.comparator;
/* 123 */     Node<K, V> nearest = this.root;
/* 124 */     int comparison = 0;
/*     */     
/* 126 */     if (nearest != null) {
/*     */ 
/*     */       
/* 129 */       Comparable<Object> comparableKey = (comparator == NATURAL_ORDER) ? (Comparable<Object>)key : null;
/*     */ 
/*     */ 
/*     */       
/*     */       while (true) {
/* 134 */         comparison = (comparableKey != null) ? comparableKey.compareTo(nearest.key) : comparator.compare(key, nearest.key);
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 139 */         if (comparison == 0) {
/* 140 */           return nearest;
/*     */         }
/*     */ 
/*     */         
/* 144 */         Node<K, V> child = (comparison < 0) ? nearest.left : nearest.right;
/* 145 */         if (child == null) {
/*     */           break;
/*     */         }
/*     */         
/* 149 */         nearest = child;
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 154 */     if (!create) {
/* 155 */       return null;
/*     */     }
/*     */ 
/*     */     
/* 159 */     Node<K, V> header = this.header;
/*     */     
/* 161 */     if (nearest == null) {
/*     */       
/* 163 */       if (comparator == NATURAL_ORDER && !(key instanceof Comparable)) {
/* 164 */         throw new ClassCastException(key.getClass().getName() + " is not Comparable");
/*     */       }
/* 166 */       created = new Node<K, V>(nearest, key, header, header.prev);
/* 167 */       this.root = created;
/*     */     } else {
/* 169 */       created = new Node<K, V>(nearest, key, header, header.prev);
/* 170 */       if (comparison < 0) {
/* 171 */         nearest.left = created;
/*     */       } else {
/* 173 */         nearest.right = created;
/*     */       } 
/* 175 */       rebalance(nearest, true);
/*     */     } 
/* 177 */     this.size++;
/* 178 */     this.modCount++;
/*     */     
/* 180 */     return created;
/*     */   }
/*     */ 
/*     */   
/*     */   Node<K, V> findByObject(Object key) {
/*     */     try {
/* 186 */       return (key != null) ? find((K)key, false) : null;
/* 187 */     } catch (ClassCastException e) {
/* 188 */       return null;
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
/*     */   Node<K, V> findByEntry(Map.Entry<?, ?> entry) {
/* 202 */     Node<K, V> mine = findByObject(entry.getKey());
/* 203 */     boolean valuesEqual = (mine != null && equal(mine.value, entry.getValue()));
/* 204 */     return valuesEqual ? mine : null;
/*     */   }
/*     */   
/*     */   private boolean equal(Object a, Object b) {
/* 208 */     return (a == b || (a != null && a.equals(b)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void removeInternal(Node<K, V> node, boolean unlink) {
/* 218 */     if (unlink) {
/* 219 */       node.prev.next = node.next;
/* 220 */       node.next.prev = node.prev;
/*     */     } 
/*     */     
/* 223 */     Node<K, V> left = node.left;
/* 224 */     Node<K, V> right = node.right;
/* 225 */     Node<K, V> originalParent = node.parent;
/* 226 */     if (left != null && right != null) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 237 */       Node<K, V> adjacent = (left.height > right.height) ? left.last() : right.first();
/* 238 */       removeInternal(adjacent, false);
/*     */       
/* 240 */       int leftHeight = 0;
/* 241 */       left = node.left;
/* 242 */       if (left != null) {
/* 243 */         leftHeight = left.height;
/* 244 */         adjacent.left = left;
/* 245 */         left.parent = adjacent;
/* 246 */         node.left = null;
/*     */       } 
/*     */       
/* 249 */       int rightHeight = 0;
/* 250 */       right = node.right;
/* 251 */       if (right != null) {
/* 252 */         rightHeight = right.height;
/* 253 */         adjacent.right = right;
/* 254 */         right.parent = adjacent;
/* 255 */         node.right = null;
/*     */       } 
/*     */       
/* 258 */       adjacent.height = Math.max(leftHeight, rightHeight) + 1;
/* 259 */       replaceInParent(node, adjacent); return;
/*     */     } 
/* 261 */     if (left != null) {
/* 262 */       replaceInParent(node, left);
/* 263 */       node.left = null;
/* 264 */     } else if (right != null) {
/* 265 */       replaceInParent(node, right);
/* 266 */       node.right = null;
/*     */     } else {
/* 268 */       replaceInParent(node, null);
/*     */     } 
/*     */     
/* 271 */     rebalance(originalParent, false);
/* 272 */     this.size--;
/* 273 */     this.modCount++;
/*     */   }
/*     */   
/*     */   Node<K, V> removeInternalByKey(Object key) {
/* 277 */     Node<K, V> node = findByObject(key);
/* 278 */     if (node != null) {
/* 279 */       removeInternal(node, true);
/*     */     }
/* 281 */     return node;
/*     */   }
/*     */   
/*     */   private void replaceInParent(Node<K, V> node, Node<K, V> replacement) {
/* 285 */     Node<K, V> parent = node.parent;
/* 286 */     node.parent = null;
/* 287 */     if (replacement != null) {
/* 288 */       replacement.parent = parent;
/*     */     }
/*     */     
/* 291 */     if (parent != null) {
/* 292 */       if (parent.left == node) {
/* 293 */         parent.left = replacement;
/*     */       } else {
/* 295 */         assert parent.right == node;
/* 296 */         parent.right = replacement;
/*     */       } 
/*     */     } else {
/* 299 */       this.root = replacement;
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
/*     */   private void rebalance(Node<K, V> unbalanced, boolean insert) {
/* 311 */     for (Node<K, V> node = unbalanced; node != null; node = node.parent) {
/* 312 */       Node<K, V> left = node.left;
/* 313 */       Node<K, V> right = node.right;
/* 314 */       int leftHeight = (left != null) ? left.height : 0;
/* 315 */       int rightHeight = (right != null) ? right.height : 0;
/*     */       
/* 317 */       int delta = leftHeight - rightHeight;
/* 318 */       if (delta == -2) {
/* 319 */         Node<K, V> rightLeft = right.left;
/* 320 */         Node<K, V> rightRight = right.right;
/* 321 */         int rightRightHeight = (rightRight != null) ? rightRight.height : 0;
/* 322 */         int rightLeftHeight = (rightLeft != null) ? rightLeft.height : 0;
/*     */         
/* 324 */         int rightDelta = rightLeftHeight - rightRightHeight;
/* 325 */         if (rightDelta == -1 || (rightDelta == 0 && !insert)) {
/* 326 */           rotateLeft(node);
/*     */         } else {
/* 328 */           assert rightDelta == 1;
/* 329 */           rotateRight(right);
/* 330 */           rotateLeft(node);
/*     */         } 
/* 332 */         if (insert) {
/*     */           break;
/*     */         }
/*     */       }
/* 336 */       else if (delta == 2) {
/* 337 */         Node<K, V> leftLeft = left.left;
/* 338 */         Node<K, V> leftRight = left.right;
/* 339 */         int leftRightHeight = (leftRight != null) ? leftRight.height : 0;
/* 340 */         int leftLeftHeight = (leftLeft != null) ? leftLeft.height : 0;
/*     */         
/* 342 */         int leftDelta = leftLeftHeight - leftRightHeight;
/* 343 */         if (leftDelta == 1 || (leftDelta == 0 && !insert)) {
/* 344 */           rotateRight(node);
/*     */         } else {
/* 346 */           assert leftDelta == -1;
/* 347 */           rotateLeft(left);
/* 348 */           rotateRight(node);
/*     */         } 
/* 350 */         if (insert) {
/*     */           break;
/*     */         }
/*     */       }
/* 354 */       else if (delta == 0) {
/* 355 */         node.height = leftHeight + 1;
/* 356 */         if (insert) {
/*     */           break;
/*     */         }
/*     */       } else {
/*     */         
/* 361 */         assert delta == -1 || delta == 1;
/* 362 */         node.height = Math.max(leftHeight, rightHeight) + 1;
/* 363 */         if (!insert) {
/*     */           break;
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void rotateLeft(Node<K, V> root) {
/* 374 */     Node<K, V> left = root.left;
/* 375 */     Node<K, V> pivot = root.right;
/* 376 */     Node<K, V> pivotLeft = pivot.left;
/* 377 */     Node<K, V> pivotRight = pivot.right;
/*     */ 
/*     */     
/* 380 */     root.right = pivotLeft;
/* 381 */     if (pivotLeft != null) {
/* 382 */       pivotLeft.parent = root;
/*     */     }
/*     */     
/* 385 */     replaceInParent(root, pivot);
/*     */ 
/*     */     
/* 388 */     pivot.left = root;
/* 389 */     root.parent = pivot;
/*     */ 
/*     */     
/* 392 */     root.height = Math.max((left != null) ? left.height : 0, (pivotLeft != null) ? pivotLeft.height : 0) + 1;
/*     */     
/* 394 */     pivot.height = Math.max(root.height, (pivotRight != null) ? pivotRight.height : 0) + 1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void rotateRight(Node<K, V> root) {
/* 402 */     Node<K, V> pivot = root.left;
/* 403 */     Node<K, V> right = root.right;
/* 404 */     Node<K, V> pivotLeft = pivot.left;
/* 405 */     Node<K, V> pivotRight = pivot.right;
/*     */ 
/*     */     
/* 408 */     root.left = pivotRight;
/* 409 */     if (pivotRight != null) {
/* 410 */       pivotRight.parent = root;
/*     */     }
/*     */     
/* 413 */     replaceInParent(root, pivot);
/*     */ 
/*     */     
/* 416 */     pivot.right = root;
/* 417 */     root.parent = pivot;
/*     */ 
/*     */     
/* 420 */     root.height = Math.max((right != null) ? right.height : 0, (pivotRight != null) ? pivotRight.height : 0) + 1;
/*     */     
/* 422 */     pivot.height = Math.max(root.height, (pivotLeft != null) ? pivotLeft.height : 0) + 1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<Map.Entry<K, V>> entrySet() {
/* 430 */     EntrySet result = this.entrySet;
/* 431 */     return (result != null) ? result : (this.entrySet = new EntrySet());
/*     */   }
/*     */   
/*     */   public Set<K> keySet() {
/* 435 */     KeySet result = this.keySet;
/* 436 */     return (result != null) ? result : (this.keySet = new KeySet());
/*     */   }
/*     */   
/*     */   static final class Node<K, V>
/*     */     implements Map.Entry<K, V> {
/*     */     Node<K, V> parent;
/*     */     Node<K, V> left;
/*     */     Node<K, V> right;
/*     */     Node<K, V> next;
/*     */     Node<K, V> prev;
/*     */     final K key;
/*     */     V value;
/*     */     int height;
/*     */     
/*     */     Node() {
/* 451 */       this.key = null;
/* 452 */       this.next = this.prev = this;
/*     */     }
/*     */ 
/*     */     
/*     */     Node(Node<K, V> parent, K key, Node<K, V> next, Node<K, V> prev) {
/* 457 */       this.parent = parent;
/* 458 */       this.key = key;
/* 459 */       this.height = 1;
/* 460 */       this.next = next;
/* 461 */       this.prev = prev;
/* 462 */       prev.next = this;
/* 463 */       next.prev = this;
/*     */     }
/*     */     
/*     */     public K getKey() {
/* 467 */       return this.key;
/*     */     }
/*     */     
/*     */     public V getValue() {
/* 471 */       return this.value;
/*     */     }
/*     */     
/*     */     public V setValue(V value) {
/* 475 */       V oldValue = this.value;
/* 476 */       this.value = value;
/* 477 */       return oldValue;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object o) {
/* 482 */       if (o instanceof Map.Entry) {
/* 483 */         Map.Entry other = (Map.Entry)o;
/* 484 */         return (((this.key == null) ? (other.getKey() == null) : this.key.equals(other.getKey())) && ((this.value == null) ? (other.getValue() == null) : this.value.equals(other.getValue())));
/*     */       } 
/*     */       
/* 487 */       return false;
/*     */     }
/*     */     
/*     */     public int hashCode() {
/* 491 */       return ((this.key == null) ? 0 : this.key.hashCode()) ^ ((this.value == null) ? 0 : this.value.hashCode());
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 496 */       return (new StringBuilder()).append(this.key).append("=").append(this.value).toString();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Node<K, V> first() {
/* 503 */       Node<K, V> node = this;
/* 504 */       Node<K, V> child = node.left;
/* 505 */       while (child != null) {
/* 506 */         node = child;
/* 507 */         child = node.left;
/*     */       } 
/* 509 */       return node;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Node<K, V> last() {
/* 516 */       Node<K, V> node = this;
/* 517 */       Node<K, V> child = node.right;
/* 518 */       while (child != null) {
/* 519 */         node = child;
/* 520 */         child = node.right;
/*     */       } 
/* 522 */       return node;
/*     */     }
/*     */   }
/*     */   
/*     */   private abstract class LinkedTreeMapIterator<T> implements Iterator<T> {
/* 527 */     LinkedTreeMap.Node<K, V> next = LinkedTreeMap.this.header.next;
/* 528 */     LinkedTreeMap.Node<K, V> lastReturned = null;
/* 529 */     int expectedModCount = LinkedTreeMap.this.modCount;
/*     */     
/*     */     public final boolean hasNext() {
/* 532 */       return (this.next != LinkedTreeMap.this.header);
/*     */     }
/*     */     
/*     */     final LinkedTreeMap.Node<K, V> nextNode() {
/* 536 */       LinkedTreeMap.Node<K, V> e = this.next;
/* 537 */       if (e == LinkedTreeMap.this.header) {
/* 538 */         throw new NoSuchElementException();
/*     */       }
/* 540 */       if (LinkedTreeMap.this.modCount != this.expectedModCount) {
/* 541 */         throw new ConcurrentModificationException();
/*     */       }
/* 543 */       this.next = e.next;
/* 544 */       return this.lastReturned = e;
/*     */     }
/*     */     private LinkedTreeMapIterator() {}
/*     */     public final void remove() {
/* 548 */       if (this.lastReturned == null) {
/* 549 */         throw new IllegalStateException();
/*     */       }
/* 551 */       LinkedTreeMap.this.removeInternal(this.lastReturned, true);
/* 552 */       this.lastReturned = null;
/* 553 */       this.expectedModCount = LinkedTreeMap.this.modCount;
/*     */     }
/*     */   }
/*     */   
/*     */   class EntrySet extends AbstractSet<Map.Entry<K, V>> {
/*     */     public int size() {
/* 559 */       return LinkedTreeMap.this.size;
/*     */     }
/*     */     
/*     */     public Iterator<Map.Entry<K, V>> iterator() {
/* 563 */       return new LinkedTreeMap<K, V>.LinkedTreeMapIterator<Map.Entry<K, V>>() {
/*     */           public Map.Entry<K, V> next() {
/* 565 */             return nextNode();
/*     */           }
/*     */         };
/*     */     }
/*     */     
/*     */     public boolean contains(Object o) {
/* 571 */       return (o instanceof Map.Entry && LinkedTreeMap.this.findByEntry((Map.Entry<?, ?>)o) != null);
/*     */     }
/*     */     
/*     */     public boolean remove(Object o) {
/* 575 */       if (!(o instanceof Map.Entry)) {
/* 576 */         return false;
/*     */       }
/*     */       
/* 579 */       LinkedTreeMap.Node<K, V> node = LinkedTreeMap.this.findByEntry((Map.Entry<?, ?>)o);
/* 580 */       if (node == null) {
/* 581 */         return false;
/*     */       }
/* 583 */       LinkedTreeMap.this.removeInternal(node, true);
/* 584 */       return true;
/*     */     }
/*     */     
/*     */     public void clear() {
/* 588 */       LinkedTreeMap.this.clear();
/*     */     }
/*     */   }
/*     */   
/*     */   class KeySet extends AbstractSet<K> {
/*     */     public int size() {
/* 594 */       return LinkedTreeMap.this.size;
/*     */     }
/*     */     
/*     */     public Iterator<K> iterator() {
/* 598 */       return new LinkedTreeMap<K, V>.LinkedTreeMapIterator<K>() {
/*     */           public K next() {
/* 600 */             return (nextNode()).key;
/*     */           }
/*     */         };
/*     */     }
/*     */     
/*     */     public boolean contains(Object o) {
/* 606 */       return LinkedTreeMap.this.containsKey(o);
/*     */     }
/*     */     
/*     */     public boolean remove(Object key) {
/* 610 */       return (LinkedTreeMap.this.removeInternalByKey(key) != null);
/*     */     }
/*     */     
/*     */     public void clear() {
/* 614 */       LinkedTreeMap.this.clear();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Object writeReplace() throws ObjectStreamException {
/* 625 */     return new LinkedHashMap<K, V>(this);
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\gson\internal\LinkedTreeMap.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */