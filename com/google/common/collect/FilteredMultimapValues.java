/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import com.google.common.base.Objects;
/*    */ import com.google.common.base.Preconditions;
/*    */ import com.google.common.base.Predicate;
/*    */ import com.google.common.base.Predicates;
/*    */ import java.util.AbstractCollection;
/*    */ import java.util.Collection;
/*    */ import java.util.Iterator;
/*    */ import java.util.Map;
/*    */ import javax.annotation.Nullable;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @GwtCompatible
/*    */ final class FilteredMultimapValues<K, V>
/*    */   extends AbstractCollection<V>
/*    */ {
/*    */   private final FilteredMultimap<K, V> multimap;
/*    */   
/*    */   FilteredMultimapValues(FilteredMultimap<K, V> multimap) {
/* 42 */     this.multimap = (FilteredMultimap<K, V>)Preconditions.checkNotNull(multimap);
/*    */   }
/*    */ 
/*    */   
/*    */   public Iterator<V> iterator() {
/* 47 */     return Maps.valueIterator(this.multimap.entries().iterator());
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean contains(@Nullable Object o) {
/* 52 */     return this.multimap.containsValue(o);
/*    */   }
/*    */ 
/*    */   
/*    */   public int size() {
/* 57 */     return this.multimap.size();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean remove(@Nullable Object o) {
/* 62 */     Predicate<? super Map.Entry<K, V>> entryPredicate = this.multimap.entryPredicate();
/* 63 */     Iterator<Map.Entry<K, V>> unfilteredItr = this.multimap.unfiltered().entries().iterator();
/* 64 */     while (unfilteredItr.hasNext()) {
/* 65 */       Map.Entry<K, V> entry = unfilteredItr.next();
/* 66 */       if (entryPredicate.apply(entry) && Objects.equal(entry.getValue(), o)) {
/* 67 */         unfilteredItr.remove();
/* 68 */         return true;
/*    */       } 
/*    */     } 
/* 71 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean removeAll(Collection<?> c) {
/* 76 */     return Iterables.removeIf(this.multimap.unfiltered().entries(), Predicates.and(this.multimap.entryPredicate(), Maps.valuePredicateOnEntries(Predicates.in(c))));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean retainAll(Collection<?> c) {
/* 84 */     return Iterables.removeIf(this.multimap.unfiltered().entries(), Predicates.and(this.multimap.entryPredicate(), Maps.valuePredicateOnEntries(Predicates.not(Predicates.in(c)))));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void clear() {
/* 92 */     this.multimap.clear();
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\collect\FilteredMultimapValues.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */