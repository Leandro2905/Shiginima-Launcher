/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import java.util.Collection;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @GwtCompatible
/*    */ public abstract class ForwardingSetMultimap<K, V>
/*    */   extends ForwardingMultimap<K, V>
/*    */   implements SetMultimap<K, V>
/*    */ {
/*    */   public Set<Map.Entry<K, V>> entries() {
/* 42 */     return delegate().entries();
/*    */   }
/*    */   
/*    */   public Set<V> get(@Nullable K key) {
/* 46 */     return delegate().get(key);
/*    */   }
/*    */   
/*    */   public Set<V> removeAll(@Nullable Object key) {
/* 50 */     return delegate().removeAll(key);
/*    */   }
/*    */   
/*    */   public Set<V> replaceValues(K key, Iterable<? extends V> values) {
/* 54 */     return delegate().replaceValues(key, values);
/*    */   }
/*    */   
/*    */   protected abstract SetMultimap<K, V> delegate();
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\collect\ForwardingSetMultimap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */