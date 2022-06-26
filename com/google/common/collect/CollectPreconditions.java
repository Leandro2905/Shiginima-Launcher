/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import com.google.common.base.Preconditions;
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
/*    */ final class CollectPreconditions
/*    */ {
/*    */   static void checkEntryNotNull(Object key, Object value) {
/* 30 */     if (key == null) {
/* 31 */       String str = String.valueOf(String.valueOf(value)); throw new NullPointerException((new StringBuilder(24 + str.length())).append("null key in entry: null=").append(str).toString());
/* 32 */     }  if (value == null) {
/* 33 */       String str = String.valueOf(String.valueOf(key)); throw new NullPointerException((new StringBuilder(26 + str.length())).append("null value in entry: ").append(str).append("=null").toString());
/*    */     } 
/*    */   }
/*    */   
/*    */   static int checkNonnegative(int value, String name) {
/* 38 */     if (value < 0) {
/* 39 */       String str = String.valueOf(String.valueOf(name)); int i = value; throw new IllegalArgumentException((new StringBuilder(40 + str.length())).append(str).append(" cannot be negative but was: ").append(i).toString());
/*    */     } 
/* 41 */     return value;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static void checkRemove(boolean canRemove) {
/* 49 */     Preconditions.checkState(canRemove, "no calls to next() since the last call to remove()");
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\collect\CollectPreconditions.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */