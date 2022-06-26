/*    */ package org.jivesoftware.smack.sasl;
/*    */ 
/*    */ import javax.security.auth.callback.CallbackHandler;
/*    */ import org.jivesoftware.smack.SmackException;
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
/*    */ public class SASLAnonymous
/*    */   extends SASLMechanism
/*    */ {
/*    */   public static final String NAME = "ANONYMOUS";
/*    */   
/*    */   public String getName() {
/* 33 */     return "ANONYMOUS";
/*    */   }
/*    */ 
/*    */   
/*    */   public int getPriority() {
/* 38 */     return 500;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void authenticateInternal(CallbackHandler cbh) throws SmackException {}
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected byte[] getAuthenticationText() throws SmackException {
/* 50 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public SASLAnonymous newInstance() {
/* 55 */     return new SASLAnonymous();
/*    */   }
/*    */   
/*    */   public void checkIfSuccessfulOrThrow() throws SmackException {}
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jivesoftware\smack\sasl\SASLAnonymous.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */