/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import com.google.common.annotations.GwtIncompatible;
/*    */ import java.io.Serializable;
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
/*    */ 
/*    */ 
/*    */ @GwtCompatible(emulated = true)
/*    */ abstract class ImmutableMapEntrySet<K, V>
/*    */   extends ImmutableSet<Map.Entry<K, V>>
/*    */ {
/*    */   abstract ImmutableMap<K, V> map();
/*    */   
/*    */   public int size() {
/* 41 */     return map().size();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean contains(@Nullable Object object) {
/* 46 */     if (object instanceof Map.Entry) {
/* 47 */       Map.Entry<?, ?> entry = (Map.Entry<?, ?>)object;
/* 48 */       V value = map().get(entry.getKey());
/* 49 */       return (value != null && value.equals(entry.getValue()));
/*    */     } 
/* 51 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   boolean isPartialView() {
/* 56 */     return map().isPartialView();
/*    */   }
/*    */ 
/*    */   
/*    */   @GwtIncompatible("serialization")
/*    */   Object writeReplace() {
/* 62 */     return new EntrySetSerializedForm<K, V>(map());
/*    */   }
/*    */   
/*    */   @GwtIncompatible("serialization")
/*    */   private static class EntrySetSerializedForm<K, V> implements Serializable { final ImmutableMap<K, V> map;
/*    */     
/*    */     EntrySetSerializedForm(ImmutableMap<K, V> map) {
/* 69 */       this.map = map;
/*    */     } private static final long serialVersionUID = 0L;
/*    */     Object readResolve() {
/* 72 */       return this.map.entrySet();
/*    */     } }
/*    */ 
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\collect\ImmutableMapEntrySet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */