/*    */ package org.jivesoftware.smack.sasl;
/*    */ 
/*    */ import java.util.logging.Level;
/*    */ import java.util.logging.Logger;
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
/*    */ public enum SASLError
/*    */ {
/*    */   private static final Logger LOGGER;
/* 24 */   aborted,
/* 25 */   account_disabled,
/* 26 */   credentials_expired,
/* 27 */   encryption_required,
/* 28 */   incorrect_encoding,
/* 29 */   invalid_authzid,
/* 30 */   invalid_mechanism,
/* 31 */   malformed_request,
/* 32 */   mechanism_too_weak,
/* 33 */   not_authorized,
/* 34 */   temporary_auth_failure;
/*    */   static {
/* 36 */     LOGGER = Logger.getLogger(SASLError.class.getName());
/*    */   }
/*    */   public String toString() {
/* 39 */     return name().replace('_', '-');
/*    */   }
/*    */   
/*    */   public static SASLError fromString(String string) {
/* 43 */     string = string.replace('-', '_');
/* 44 */     SASLError saslError = null;
/*    */     try {
/* 46 */       saslError = valueOf(string);
/* 47 */     } catch (Exception e) {
/* 48 */       LOGGER.log(Level.WARNING, "Could not transform string '" + string + "' to SASLError", e);
/*    */     } 
/* 50 */     return saslError;
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jivesoftware\smack\sasl\SASLError.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */