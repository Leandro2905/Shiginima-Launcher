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
/*    */ public class ByteUtils
/*    */ {
/*    */   public static byte[] concact(byte[] arrayOne, byte[] arrayTwo) {
/* 21 */     int combinedLength = arrayOne.length + arrayTwo.length;
/* 22 */     byte[] res = new byte[combinedLength];
/* 23 */     System.arraycopy(arrayOne, 0, res, 0, arrayOne.length);
/* 24 */     System.arraycopy(arrayTwo, 0, res, arrayOne.length, arrayTwo.length);
/* 25 */     return res;
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jivesoftware\smac\\util\ByteUtils.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */