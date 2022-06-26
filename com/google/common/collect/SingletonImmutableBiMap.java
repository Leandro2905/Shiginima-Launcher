/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
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
/*    */ 
/*    */ 
/*    */ @GwtCompatible(serializable = true, emulated = true)
/*    */ final class SingletonImmutableBiMap<K, V>
/*    */   extends ImmutableBiMap<K, V>
/*    */ {
/*    */   final transient K singleKey;
/*    */   final transient V singleValue;
/*    */   transient ImmutableBiMap<V, K> inverse;
/*    */   
/*    */   SingletonImmutableBiMap(K singleKey, V singleValue) {
/* 39 */     CollectPreconditions.checkEntryNotNull(singleKey, singleValue);
/* 40 */     this.singleKey = singleKey;
/* 41 */     this.singleValue = singleValue;
/*    */   }
/*    */ 
/*    */   
/*    */   private SingletonImmutableBiMap(K singleKey, V singleValue, ImmutableBiMap<V, K> inverse) {
/* 46 */     this.singleKey = singleKey;
/* 47 */     this.singleValue = singleValue;
/* 48 */     this.inverse = inverse;
/*    */   }
/*    */   
/*    */   SingletonImmutableBiMap(Map.Entry<? extends K, ? extends V> entry) {
/* 52 */     this(entry.getKey(), entry.getValue());
/*    */   }
/*    */   
/*    */   public V get(@Nullable Object key) {
/* 56 */     return this.singleKey.equals(key) ? this.singleValue : null;
/*    */   }
/*    */ 
/*    */   
/*    */   public int size() {
/* 61 */     return 1;
/*    */   }
/*    */   
/*    */   public boolean containsKey(@Nullable Object key) {
/* 65 */     return this.singleKey.equals(key);
/*    */   }
/*    */   
/*    */   public boolean containsValue(@Nullable Object value) {
/* 69 */     return this.singleValue.equals(value);
/*    */   }
/*    */   
/*    */   boolean isPartialView() {
/* 73 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   ImmutableSet<Map.Entry<K, V>> createEntrySet() {
/* 78 */     return ImmutableSet.of(Maps.immutableEntry(this.singleKey, this.singleValue));
/*    */   }
/*    */ 
/*    */   
/*    */   ImmutableSet<K> createKeySet() {
/* 83 */     return ImmutableSet.of(this.singleKey);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ImmutableBiMap<V, K> inverse() {
/* 91 */     ImmutableBiMap<V, K> result = this.inverse;
/* 92 */     if (result == null) {
/* 93 */       return this.inverse = new SingletonImmutableBiMap((K)this.singleValue, (V)this.singleKey, this);
/*    */     }
/*    */     
/* 96 */     return result;
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\collect\SingletonImmutableBiMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */