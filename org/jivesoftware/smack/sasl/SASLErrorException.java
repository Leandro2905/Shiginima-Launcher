/*    */ package org.jivesoftware.smack.sasl;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import org.jivesoftware.smack.XMPPException;
/*    */ import org.jivesoftware.smack.sasl.packet.SaslStreamElements;
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
/*    */ public class SASLErrorException
/*    */   extends XMPPException
/*    */ {
/*    */   private static final long serialVersionUID = 6247573875760717257L;
/*    */   private final SaslStreamElements.SASLFailure saslFailure;
/*    */   private final String mechanism;
/*    */   private final Map<String, String> texts;
/*    */   
/*    */   public SASLErrorException(String mechanism, SaslStreamElements.SASLFailure saslFailure) {
/* 37 */     this(mechanism, saslFailure, new HashMap<>());
/*    */   }
/*    */   
/*    */   public SASLErrorException(String mechanism, SaslStreamElements.SASLFailure saslFailure, Map<String, String> texts) {
/* 41 */     super("SASLError using " + mechanism + ": " + saslFailure.getSASLErrorString());
/* 42 */     this.mechanism = mechanism;
/* 43 */     this.saslFailure = saslFailure;
/* 44 */     this.texts = texts;
/*    */   }
/*    */   
/*    */   public SaslStreamElements.SASLFailure getSASLFailure() {
/* 48 */     return this.saslFailure;
/*    */   }
/*    */   
/*    */   public String getMechanism() {
/* 52 */     return this.mechanism;
/*    */   }
/*    */   
/*    */   public Map<String, String> getTexts() {
/* 56 */     return this.texts;
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jivesoftware\smack\sasl\SASLErrorException.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */