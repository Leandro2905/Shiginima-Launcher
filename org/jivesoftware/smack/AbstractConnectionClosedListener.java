/*    */ package org.jivesoftware.smack;
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
/*    */ public abstract class AbstractConnectionClosedListener
/*    */   extends AbstractConnectionListener
/*    */ {
/*    */   public final void connectionClosed() {
/* 26 */     connectionTerminated();
/*    */   }
/*    */ 
/*    */   
/*    */   public final void connectionClosedOnError(Exception e) {
/* 31 */     connectionTerminated();
/*    */   }
/*    */   
/*    */   public abstract void connectionTerminated();
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jivesoftware\smack\AbstractConnectionClosedListener.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */