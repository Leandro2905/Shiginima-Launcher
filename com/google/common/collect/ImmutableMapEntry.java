/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtIncompatible;
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
/*    */ @GwtIncompatible("unnecessary")
/*    */ abstract class ImmutableMapEntry<K, V>
/*    */   extends ImmutableEntry<K, V>
/*    */ {
/*    */   ImmutableMapEntry(K key, V value) {
/* 36 */     super(key, value);
/* 37 */     CollectPreconditions.checkEntryNotNull(key, value);
/*    */   }
/*    */   
/*    */   ImmutableMapEntry(ImmutableMapEntry<K, V> contents) {
/* 41 */     super(contents.getKey(), contents.getValue());
/*    */   }
/*    */   
/*    */   @Nullable
/*    */   abstract ImmutableMapEntry<K, V> getNextInKeyBucket();
/*    */   
/*    */   @Nullable
/*    */   abstract ImmutableMapEntry<K, V> getNextInValueBucket();
/*    */   
/*    */   static final class TerminalEntry<K, V>
/*    */     extends ImmutableMapEntry<K, V> {
/*    */     TerminalEntry(ImmutableMapEntry<K, V> contents) {
/* 53 */       super(contents);
/*    */     }
/*    */     
/*    */     TerminalEntry(K key, V value) {
/* 57 */       super(key, value);
/*    */     }
/*    */ 
/*    */     
/*    */     @Nullable
/*    */     ImmutableMapEntry<K, V> getNextInKeyBucket() {
/* 63 */       return null;
/*    */     }
/*    */ 
/*    */     
/*    */     @Nullable
/*    */     ImmutableMapEntry<K, V> getNextInValueBucket() {
/* 69 */       return null;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\collect\ImmutableMapEntry.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */