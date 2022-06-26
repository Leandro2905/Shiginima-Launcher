/*    */ package org.jivesoftware.smack.util;
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
/*    */ public class NumberUtil
/*    */ {
/*    */   public static void checkIfInUInt32Range(long value) {
/* 27 */     if (value < 0L) {
/* 28 */       throw new IllegalArgumentException("unsigned 32-bit integers can't be negative");
/*    */     }
/* 30 */     if (value > 4294967295L)
/* 31 */       throw new IllegalArgumentException("unsigned 32-bit integers can't be greater then 2^32 - 1"); 
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jivesoftware\smac\\util\NumberUtil.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */