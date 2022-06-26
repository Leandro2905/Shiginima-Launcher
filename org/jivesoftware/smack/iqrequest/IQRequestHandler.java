/*    */ package org.jivesoftware.smack.iqrequest;
/*    */ 
/*    */ import org.jivesoftware.smack.packet.IQ;
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
/*    */ public interface IQRequestHandler
/*    */ {
/*    */   IQ handleIQRequest(IQ paramIQ);
/*    */   
/*    */   Mode getMode();
/*    */   
/*    */   IQ.Type getType();
/*    */   
/*    */   String getElement();
/*    */   
/*    */   String getNamespace();
/*    */   
/*    */   public enum Mode
/*    */   {
/* 35 */     sync,
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 41 */     async;
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jivesoftware\smack\iqrequest\IQRequestHandler.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */