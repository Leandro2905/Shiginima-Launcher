/*    */ package org.jivesoftware.smack.parsing;
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
/*    */ public class ExceptionThrowingCallback
/*    */   extends ParsingExceptionCallback
/*    */ {
/*    */   public void handleUnparsablePacket(UnparsablePacket packetData) throws Exception {
/* 33 */     throw packetData.getParsingException();
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jivesoftware\smack\parsing\ExceptionThrowingCallback.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */