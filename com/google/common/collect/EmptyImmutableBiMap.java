/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
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
/*    */ @GwtCompatible(emulated = true)
/*    */ final class EmptyImmutableBiMap
/*    */   extends ImmutableBiMap<Object, Object>
/*    */ {
/* 31 */   static final EmptyImmutableBiMap INSTANCE = new EmptyImmutableBiMap();
/*    */ 
/*    */ 
/*    */   
/*    */   public ImmutableBiMap<Object, Object> inverse() {
/* 36 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public int size() {
/* 41 */     return 0;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isEmpty() {
/* 46 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public Object get(@Nullable Object key) {
/* 51 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public ImmutableSet<Map.Entry<Object, Object>> entrySet() {
/* 56 */     return ImmutableSet.of();
/*    */   }
/*    */ 
/*    */   
/*    */   ImmutableSet<Map.Entry<Object, Object>> createEntrySet() {
/* 61 */     throw new AssertionError("should never be called");
/*    */   }
/*    */ 
/*    */   
/*    */   public ImmutableSetMultimap<Object, Object> asMultimap() {
/* 66 */     return ImmutableSetMultimap.of();
/*    */   }
/*    */ 
/*    */   
/*    */   public ImmutableSet<Object> keySet() {
/* 71 */     return ImmutableSet.of();
/*    */   }
/*    */ 
/*    */   
/*    */   boolean isPartialView() {
/* 76 */     return false;
/*    */   }
/*    */   
/*    */   Object readResolve() {
/* 80 */     return INSTANCE;
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\collect\EmptyImmutableBiMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */