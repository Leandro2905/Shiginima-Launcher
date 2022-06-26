/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.AbstractSequentialList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.ConcurrentModificationException;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
/*     */ import java.util.Map;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public class LinkedListMultimap<K, V>
/*     */   extends AbstractMultimap<K, V>
/*     */   implements ListMultimap<K, V>, Serializable
/*     */ {
/*     */   private transient Node<K, V> head;
/*     */   private transient Node<K, V> tail;
/*     */   private transient Map<K, KeyList<K, V>> keyToKeyList;
/*     */   private transient int size;
/*     */   private transient int modCount;
/*     */   @GwtIncompatible("java serialization not supported")
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   private static final class Node<K, V>
/*     */     extends AbstractMapEntry<K, V>
/*     */   {
/*     */     final K key;
/*     */     V value;
/*     */     Node<K, V> next;
/*     */     Node<K, V> previous;
/*     */     Node<K, V> nextSibling;
/*     */     Node<K, V> previousSibling;
/*     */     
/*     */     Node(@Nullable K key, @Nullable V value) {
/* 120 */       this.key = key;
/* 121 */       this.value = value;
/*     */     }
/*     */ 
/*     */     
/*     */     public K getKey() {
/* 126 */       return this.key;
/*     */     }
/*     */ 
/*     */     
/*     */     public V getValue() {
/* 131 */       return this.value;
/*     */     }
/*     */ 
/*     */     
/*     */     public V setValue(@Nullable V newValue) {
/* 136 */       V result = this.value;
/* 137 */       this.value = newValue;
/* 138 */       return result;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class KeyList<K, V> {
/*     */     LinkedListMultimap.Node<K, V> head;
/*     */     LinkedListMultimap.Node<K, V> tail;
/*     */     int count;
/*     */     
/*     */     KeyList(LinkedListMultimap.Node<K, V> firstNode) {
/* 148 */       this.head = firstNode;
/* 149 */       this.tail = firstNode;
/* 150 */       firstNode.previousSibling = null;
/* 151 */       firstNode.nextSibling = null;
/* 152 */       this.count = 1;
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
/*     */   
/*     */   public static <K, V> LinkedListMultimap<K, V> create() {
/* 173 */     return new LinkedListMultimap<K, V>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> LinkedListMultimap<K, V> create(int expectedKeys) {
/* 184 */     return new LinkedListMultimap<K, V>(expectedKeys);
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
/*     */   public static <K, V> LinkedListMultimap<K, V> create(Multimap<? extends K, ? extends V> multimap) {
/* 196 */     return new LinkedListMultimap<K, V>(multimap);
/*     */   }
/*     */   
/*     */   LinkedListMultimap() {
/* 200 */     this.keyToKeyList = Maps.newHashMap();
/*     */   }
/*     */   
/*     */   private LinkedListMultimap(int expectedKeys) {
/* 204 */     this.keyToKeyList = new HashMap<K, KeyList<K, V>>(expectedKeys);
/*     */   }
/*     */   
/*     */   private LinkedListMultimap(Multimap<? extends K, ? extends V> multimap) {
/* 208 */     this(multimap.keySet().size());
/* 209 */     putAll(multimap);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Node<K, V> addNode(@Nullable K key, @Nullable V value, @Nullable Node<K, V> nextSibling) {
/* 220 */     Node<K, V> node = new Node<K, V>(key, value);
/* 221 */     if (this.head == null) {
/* 222 */       this.head = this.tail = node;
/* 223 */       this.keyToKeyList.put(key, new KeyList<K, V>(node));
/* 224 */       this.modCount++;
/* 225 */     } else if (nextSibling == null) {
/* 226 */       this.tail.next = node;
/* 227 */       node.previous = this.tail;
/* 228 */       this.tail = node;
/* 229 */       KeyList<K, V> keyList = this.keyToKeyList.get(key);
/* 230 */       if (keyList == null) {
/* 231 */         this.keyToKeyList.put(key, keyList = new KeyList<K, V>(node));
/* 232 */         this.modCount++;
/*     */       } else {
/* 234 */         keyList.count++;
/* 235 */         Node<K, V> keyTail = keyList.tail;
/* 236 */         keyTail.nextSibling = node;
/* 237 */         node.previousSibling = keyTail;
/* 238 */         keyList.tail = node;
/*     */       } 
/*     */     } else {
/* 241 */       KeyList<K, V> keyList = this.keyToKeyList.get(key);
/* 242 */       keyList.count++;
/* 243 */       node.previous = nextSibling.previous;
/* 244 */       node.previousSibling = nextSibling.previousSibling;
/* 245 */       node.next = nextSibling;
/* 246 */       node.nextSibling = nextSibling;
/* 247 */       if (nextSibling.previousSibling == null) {
/* 248 */         ((KeyList)this.keyToKeyList.get(key)).head = node;
/*     */       } else {
/* 250 */         nextSibling.previousSibling.nextSibling = node;
/*     */       } 
/* 252 */       if (nextSibling.previous == null) {
/* 253 */         this.head = node;
/*     */       } else {
/* 255 */         nextSibling.previous.next = node;
/*     */       } 
/* 257 */       nextSibling.previous = node;
/* 258 */       nextSibling.previousSibling = node;
/*     */     } 
/* 260 */     this.size++;
/* 261 */     return node;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void removeNode(Node<K, V> node) {
/* 270 */     if (node.previous != null) {
/* 271 */       node.previous.next = node.next;
/*     */     } else {
/* 273 */       this.head = node.next;
/*     */     } 
/* 275 */     if (node.next != null) {
/* 276 */       node.next.previous = node.previous;
/*     */     } else {
/* 278 */       this.tail = node.previous;
/*     */     } 
/* 280 */     if (node.previousSibling == null && node.nextSibling == null) {
/* 281 */       KeyList<K, V> keyList = this.keyToKeyList.remove(node.key);
/* 282 */       keyList.count = 0;
/* 283 */       this.modCount++;
/*     */     } else {
/* 285 */       KeyList<K, V> keyList = this.keyToKeyList.get(node.key);
/* 286 */       keyList.count--;
/*     */       
/* 288 */       if (node.previousSibling == null) {
/* 289 */         keyList.head = node.nextSibling;
/*     */       } else {
/* 291 */         node.previousSibling.nextSibling = node.nextSibling;
/*     */       } 
/*     */       
/* 294 */       if (node.nextSibling == null) {
/* 295 */         keyList.tail = node.previousSibling;
/*     */       } else {
/* 297 */         node.nextSibling.previousSibling = node.previousSibling;
/*     */       } 
/*     */     } 
/* 300 */     this.size--;
/*     */   }
/*     */ 
/*     */   
/*     */   private void removeAllNodes(@Nullable Object key) {
/* 305 */     Iterators.clear(new ValueForKeyIterator(key));
/*     */   }
/*     */ 
/*     */   
/*     */   private static void checkElement(@Nullable Object node) {
/* 310 */     if (node == null)
/* 311 */       throw new NoSuchElementException(); 
/*     */   }
/*     */   
/*     */   private class NodeIterator
/*     */     implements ListIterator<Map.Entry<K, V>>
/*     */   {
/*     */     int nextIndex;
/*     */     LinkedListMultimap.Node<K, V> next;
/*     */     LinkedListMultimap.Node<K, V> current;
/*     */     LinkedListMultimap.Node<K, V> previous;
/* 321 */     int expectedModCount = LinkedListMultimap.this.modCount;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private void checkForConcurrentModification() {
/* 341 */       if (LinkedListMultimap.this.modCount != this.expectedModCount) {
/* 342 */         throw new ConcurrentModificationException();
/*     */       }
/*     */     }
/*     */     
/*     */     public boolean hasNext() {
/* 347 */       checkForConcurrentModification();
/* 348 */       return (this.next != null);
/*     */     }
/*     */     
/*     */     public LinkedListMultimap.Node<K, V> next() {
/* 352 */       checkForConcurrentModification();
/* 353 */       LinkedListMultimap.checkElement(this.next);
/* 354 */       this.previous = this.current = this.next;
/* 355 */       this.next = this.next.next;
/* 356 */       this.nextIndex++;
/* 357 */       return this.current;
/*     */     }
/*     */     
/*     */     public void remove() {
/* 361 */       checkForConcurrentModification();
/* 362 */       CollectPreconditions.checkRemove((this.current != null));
/* 363 */       if (this.current != this.next) {
/* 364 */         this.previous = this.current.previous;
/* 365 */         this.nextIndex--;
/*     */       } else {
/* 367 */         this.next = this.current.next;
/*     */       } 
/* 369 */       LinkedListMultimap.this.removeNode(this.current);
/* 370 */       this.current = null;
/* 371 */       this.expectedModCount = LinkedListMultimap.this.modCount;
/*     */     }
/*     */     
/*     */     public boolean hasPrevious() {
/* 375 */       checkForConcurrentModification();
/* 376 */       return (this.previous != null);
/*     */     }
/*     */     
/*     */     public LinkedListMultimap.Node<K, V> previous() {
/* 380 */       checkForConcurrentModification();
/* 381 */       LinkedListMultimap.checkElement(this.previous);
/* 382 */       this.next = this.current = this.previous;
/* 383 */       this.previous = this.previous.previous;
/* 384 */       this.nextIndex--;
/* 385 */       return this.current;
/*     */     }
/*     */     
/*     */     public int nextIndex() {
/* 389 */       return this.nextIndex;
/*     */     }
/*     */     
/*     */     public int previousIndex() {
/* 393 */       return this.nextIndex - 1;
/*     */     }
/*     */     
/*     */     public void set(Map.Entry<K, V> e) {
/* 397 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public void add(Map.Entry<K, V> e) {
/* 401 */       throw new UnsupportedOperationException();
/*     */     } NodeIterator(int index) { int size = LinkedListMultimap.this.size(); Preconditions.checkPositionIndex(index, size); if (index >= size / 2) { this.previous = LinkedListMultimap.this.tail; this.nextIndex = size; while (index++ < size)
/*     */           previous();  } else { this.next = LinkedListMultimap.this.head; while (index-- > 0)
/* 404 */           next();  }  this.current = null; } void setValue(V value) { Preconditions.checkState((this.current != null));
/* 405 */       this.current.value = value; }
/*     */   
/*     */   }
/*     */   
/*     */   private class DistinctKeyIterator
/*     */     implements Iterator<K> {
/* 411 */     final Set<K> seenKeys = Sets.newHashSetWithExpectedSize(LinkedListMultimap.this.keySet().size());
/* 412 */     LinkedListMultimap.Node<K, V> next = LinkedListMultimap.this.head;
/*     */     LinkedListMultimap.Node<K, V> current;
/* 414 */     int expectedModCount = LinkedListMultimap.this.modCount;
/*     */     
/*     */     private void checkForConcurrentModification() {
/* 417 */       if (LinkedListMultimap.this.modCount != this.expectedModCount) {
/* 418 */         throw new ConcurrentModificationException();
/*     */       }
/*     */     }
/*     */     
/*     */     public boolean hasNext() {
/* 423 */       checkForConcurrentModification();
/* 424 */       return (this.next != null);
/*     */     }
/*     */     
/*     */     public K next() {
/* 428 */       checkForConcurrentModification();
/* 429 */       LinkedListMultimap.checkElement(this.next);
/* 430 */       this.current = this.next;
/* 431 */       this.seenKeys.add(this.current.key);
/*     */       do {
/* 433 */         this.next = this.next.next;
/* 434 */       } while (this.next != null && !this.seenKeys.add(this.next.key));
/* 435 */       return this.current.key;
/*     */     }
/*     */     
/*     */     public void remove() {
/* 439 */       checkForConcurrentModification();
/* 440 */       CollectPreconditions.checkRemove((this.current != null));
/* 441 */       LinkedListMultimap.this.removeAllNodes(this.current.key);
/* 442 */       this.current = null;
/* 443 */       this.expectedModCount = LinkedListMultimap.this.modCount;
/*     */     }
/*     */     
/*     */     private DistinctKeyIterator() {}
/*     */   }
/*     */   
/*     */   private class ValueForKeyIterator implements ListIterator<V> {
/*     */     final Object key;
/*     */     int nextIndex;
/*     */     LinkedListMultimap.Node<K, V> next;
/*     */     LinkedListMultimap.Node<K, V> current;
/*     */     LinkedListMultimap.Node<K, V> previous;
/*     */     
/*     */     ValueForKeyIterator(Object key) {
/* 457 */       this.key = key;
/* 458 */       LinkedListMultimap.KeyList<K, V> keyList = (LinkedListMultimap.KeyList<K, V>)LinkedListMultimap.this.keyToKeyList.get(key);
/* 459 */       this.next = (keyList == null) ? null : keyList.head;
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
/*     */     public ValueForKeyIterator(Object key, int index) {
/* 472 */       LinkedListMultimap.KeyList<K, V> keyList = (LinkedListMultimap.KeyList<K, V>)LinkedListMultimap.this.keyToKeyList.get(key);
/* 473 */       int size = (keyList == null) ? 0 : keyList.count;
/* 474 */       Preconditions.checkPositionIndex(index, size);
/* 475 */       if (index >= size / 2) {
/* 476 */         this.previous = (keyList == null) ? null : keyList.tail;
/* 477 */         this.nextIndex = size;
/* 478 */         while (index++ < size) {
/* 479 */           previous();
/*     */         }
/*     */       } else {
/* 482 */         this.next = (keyList == null) ? null : keyList.head;
/* 483 */         while (index-- > 0) {
/* 484 */           next();
/*     */         }
/*     */       } 
/* 487 */       this.key = key;
/* 488 */       this.current = null;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean hasNext() {
/* 493 */       return (this.next != null);
/*     */     }
/*     */ 
/*     */     
/*     */     public V next() {
/* 498 */       LinkedListMultimap.checkElement(this.next);
/* 499 */       this.previous = this.current = this.next;
/* 500 */       this.next = this.next.nextSibling;
/* 501 */       this.nextIndex++;
/* 502 */       return this.current.value;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean hasPrevious() {
/* 507 */       return (this.previous != null);
/*     */     }
/*     */ 
/*     */     
/*     */     public V previous() {
/* 512 */       LinkedListMultimap.checkElement(this.previous);
/* 513 */       this.next = this.current = this.previous;
/* 514 */       this.previous = this.previous.previousSibling;
/* 515 */       this.nextIndex--;
/* 516 */       return this.current.value;
/*     */     }
/*     */ 
/*     */     
/*     */     public int nextIndex() {
/* 521 */       return this.nextIndex;
/*     */     }
/*     */ 
/*     */     
/*     */     public int previousIndex() {
/* 526 */       return this.nextIndex - 1;
/*     */     }
/*     */ 
/*     */     
/*     */     public void remove() {
/* 531 */       CollectPreconditions.checkRemove((this.current != null));
/* 532 */       if (this.current != this.next) {
/* 533 */         this.previous = this.current.previousSibling;
/* 534 */         this.nextIndex--;
/*     */       } else {
/* 536 */         this.next = this.current.nextSibling;
/*     */       } 
/* 538 */       LinkedListMultimap.this.removeNode(this.current);
/* 539 */       this.current = null;
/*     */     }
/*     */ 
/*     */     
/*     */     public void set(V value) {
/* 544 */       Preconditions.checkState((this.current != null));
/* 545 */       this.current.value = value;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void add(V value) {
/* 551 */       this.previous = LinkedListMultimap.this.addNode((K)this.key, value, this.next);
/* 552 */       this.nextIndex++;
/* 553 */       this.current = null;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int size() {
/* 561 */     return this.size;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 566 */     return (this.head == null);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsKey(@Nullable Object key) {
/* 571 */     return this.keyToKeyList.containsKey(key);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsValue(@Nullable Object value) {
/* 576 */     return values().contains(value);
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
/*     */   public boolean put(@Nullable K key, @Nullable V value) {
/* 590 */     addNode(key, value, null);
/* 591 */     return true;
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
/*     */   public List<V> replaceValues(@Nullable K key, Iterable<? extends V> values) {
/* 608 */     List<V> oldValues = getCopy(key);
/* 609 */     ListIterator<V> keyValues = new ValueForKeyIterator(key);
/* 610 */     Iterator<? extends V> newValues = values.iterator();
/*     */ 
/*     */     
/* 613 */     while (keyValues.hasNext() && newValues.hasNext()) {
/* 614 */       keyValues.next();
/* 615 */       keyValues.set(newValues.next());
/*     */     } 
/*     */ 
/*     */     
/* 619 */     while (keyValues.hasNext()) {
/* 620 */       keyValues.next();
/* 621 */       keyValues.remove();
/*     */     } 
/*     */ 
/*     */     
/* 625 */     while (newValues.hasNext()) {
/* 626 */       keyValues.add(newValues.next());
/*     */     }
/*     */     
/* 629 */     return oldValues;
/*     */   }
/*     */   
/*     */   private List<V> getCopy(@Nullable Object key) {
/* 633 */     return Collections.unmodifiableList(Lists.newArrayList(new ValueForKeyIterator(key)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<V> removeAll(@Nullable Object key) {
/* 644 */     List<V> oldValues = getCopy(key);
/* 645 */     removeAllNodes(key);
/* 646 */     return oldValues;
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 651 */     this.head = null;
/* 652 */     this.tail = null;
/* 653 */     this.keyToKeyList.clear();
/* 654 */     this.size = 0;
/* 655 */     this.modCount++;
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
/*     */   public List<V> get(@Nullable final K key) {
/* 671 */     return new AbstractSequentialList<V>() {
/*     */         public int size() {
/* 673 */           LinkedListMultimap.KeyList<K, V> keyList = (LinkedListMultimap.KeyList<K, V>)LinkedListMultimap.this.keyToKeyList.get(key);
/* 674 */           return (keyList == null) ? 0 : keyList.count;
/*     */         }
/*     */         public ListIterator<V> listIterator(int index) {
/* 677 */           return new LinkedListMultimap.ValueForKeyIterator(key, index);
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   Set<K> createKeySet() {
/* 684 */     return new Sets.ImprovedAbstractSet<K>() {
/*     */         public int size() {
/* 686 */           return LinkedListMultimap.this.keyToKeyList.size();
/*     */         }
/*     */         public Iterator<K> iterator() {
/* 689 */           return new LinkedListMultimap.DistinctKeyIterator();
/*     */         }
/*     */         public boolean contains(Object key) {
/* 692 */           return LinkedListMultimap.this.containsKey(key);
/*     */         }
/*     */         
/*     */         public boolean remove(Object o) {
/* 696 */           return !LinkedListMultimap.this.removeAll(o).isEmpty();
/*     */         }
/*     */       };
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
/*     */   public List<V> values() {
/* 712 */     return (List<V>)super.values();
/*     */   }
/*     */ 
/*     */   
/*     */   List<V> createValues() {
/* 717 */     return new AbstractSequentialList<V>() {
/*     */         public int size() {
/* 719 */           return LinkedListMultimap.this.size;
/*     */         }
/*     */         
/*     */         public ListIterator<V> listIterator(int index) {
/* 723 */           final LinkedListMultimap<K, V>.NodeIterator nodeItr = new LinkedListMultimap.NodeIterator(index);
/* 724 */           return new TransformedListIterator<Map.Entry<K, V>, V>(nodeItr)
/*     */             {
/*     */               V transform(Map.Entry<K, V> entry) {
/* 727 */                 return entry.getValue();
/*     */               }
/*     */ 
/*     */               
/*     */               public void set(V value) {
/* 732 */                 nodeItr.setValue(value);
/*     */               }
/*     */             };
/*     */         }
/*     */       };
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
/*     */   public List<Map.Entry<K, V>> entries() {
/* 759 */     return (List<Map.Entry<K, V>>)super.entries();
/*     */   }
/*     */ 
/*     */   
/*     */   List<Map.Entry<K, V>> createEntries() {
/* 764 */     return new AbstractSequentialList<Map.Entry<K, V>>() {
/*     */         public int size() {
/* 766 */           return LinkedListMultimap.this.size;
/*     */         }
/*     */         
/*     */         public ListIterator<Map.Entry<K, V>> listIterator(int index) {
/* 770 */           return new LinkedListMultimap.NodeIterator(index);
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   Iterator<Map.Entry<K, V>> entryIterator() {
/* 777 */     throw new AssertionError("should never be called");
/*     */   }
/*     */ 
/*     */   
/*     */   Map<K, Collection<V>> createAsMap() {
/* 782 */     return new Multimaps.AsMap<K, V>(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible("java.io.ObjectOutputStream")
/*     */   private void writeObject(ObjectOutputStream stream) throws IOException {
/* 792 */     stream.defaultWriteObject();
/* 793 */     stream.writeInt(size());
/* 794 */     for (Map.Entry<K, V> entry : entries()) {
/* 795 */       stream.writeObject(entry.getKey());
/* 796 */       stream.writeObject(entry.getValue());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @GwtIncompatible("java.io.ObjectInputStream")
/*     */   private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
/* 803 */     stream.defaultReadObject();
/* 804 */     this.keyToKeyList = Maps.newLinkedHashMap();
/* 805 */     int size = stream.readInt();
/* 806 */     for (int i = 0; i < size; i++) {
/*     */       
/* 808 */       K key = (K)stream.readObject();
/*     */       
/* 810 */       V value = (V)stream.readObject();
/* 811 */       put(key, value);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\collect\LinkedListMultimap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */