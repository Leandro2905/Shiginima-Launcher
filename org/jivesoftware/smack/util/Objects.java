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
/*    */ public class Objects
/*    */ {
/*    */   public static <T> T requireNonNull(T obj, String message) {
/* 22 */     if (obj == null) {
/* 23 */       throw new NullPointerException(message);
/*    */     }
/* 25 */     return obj;
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jivesoftware\smac\\util\Objects.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */