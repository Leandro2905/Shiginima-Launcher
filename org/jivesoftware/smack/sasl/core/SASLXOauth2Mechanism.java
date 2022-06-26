/*    */ package org.jivesoftware.smack.sasl.core;
/*    */ 
/*    */ import javax.security.auth.callback.CallbackHandler;
/*    */ import org.jivesoftware.smack.SmackException;
/*    */ import org.jivesoftware.smack.sasl.SASLMechanism;
/*    */ import org.jivesoftware.smack.util.stringencoder.Base64;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SASLXOauth2Mechanism
/*    */   extends SASLMechanism
/*    */ {
/*    */   public static final String NAME = "X-OAUTH2";
/*    */   
/*    */   protected void authenticateInternal(CallbackHandler cbh) throws SmackException {
/* 70 */     throw new UnsupportedOperationException("CallbackHandler not (yet) supported");
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected byte[] getAuthenticationText() throws SmackException {
/* 76 */     return Base64.encode(toBytes(Character.MIN_VALUE + this.authenticationId + Character.MIN_VALUE + this.password));
/*    */   }
/*    */ 
/*    */   
/*    */   public String getName() {
/* 81 */     return "X-OAUTH2";
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public int getPriority() {
/* 87 */     return 410;
/*    */   }
/*    */ 
/*    */   
/*    */   public SASLXOauth2Mechanism newInstance() {
/* 92 */     return new SASLXOauth2Mechanism();
/*    */   }
/*    */   
/*    */   public void checkIfSuccessfulOrThrow() throws SmackException {}
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jivesoftware\smack\sasl\core\SASLXOauth2Mechanism.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */