/*     */ package org.jivesoftware.smack.sasl;
/*     */ 
/*     */ import javax.security.auth.callback.CallbackHandler;
/*     */ import org.jivesoftware.smack.SmackException;
/*     */ import org.jivesoftware.smack.XMPPConnection;
/*     */ import org.jivesoftware.smack.packet.PlainStreamElement;
/*     */ import org.jivesoftware.smack.sasl.packet.SaslStreamElements;
/*     */ import org.jivesoftware.smack.util.StringTransformer;
/*     */ import org.jivesoftware.smack.util.StringUtils;
/*     */ import org.jivesoftware.smack.util.stringencoder.Base64;
/*     */ import org.jxmpp.jid.DomainBareJid;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class SASLMechanism
/*     */   implements Comparable<SASLMechanism>
/*     */ {
/*     */   public static final String CRAMMD5 = "CRAM-MD5";
/*     */   public static final String DIGESTMD5 = "DIGEST-MD5";
/*     */   public static final String EXTERNAL = "EXTERNAL";
/*     */   public static final String GSSAPI = "GSSAPI";
/*     */   public static final String PLAIN = "PLAIN";
/*     */   private static StringTransformer saslPrepTransformer;
/*     */   protected XMPPConnection connection;
/*     */   protected String authenticationId;
/*     */   protected DomainBareJid serviceName;
/*     */   protected String password;
/*     */   protected String host;
/*     */   
/*     */   public static void setSaslPrepTransformer(StringTransformer stringTransformer) {
/*  89 */     saslPrepTransformer = stringTransformer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void authenticate(String username, String host, DomainBareJid serviceName, String password) throws SmackException, SmackException.NotConnectedException, InterruptedException {
/* 166 */     this.authenticationId = username;
/* 167 */     this.host = host;
/* 168 */     this.serviceName = serviceName;
/* 169 */     this.password = password;
/* 170 */     authenticateInternal();
/* 171 */     authenticate();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void authenticateInternal() throws SmackException {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void authenticate(String host, DomainBareJid serviceName, CallbackHandler cbh) throws SmackException, SmackException.NotConnectedException, InterruptedException {
/* 190 */     this.host = host;
/* 191 */     this.serviceName = serviceName;
/* 192 */     authenticateInternal(cbh);
/* 193 */     authenticate();
/*     */   }
/*     */ 
/*     */   
/*     */   private final void authenticate() throws SmackException, SmackException.NotConnectedException, InterruptedException {
/*     */     String authenticationText;
/* 199 */     byte[] authenticationBytes = getAuthenticationText();
/*     */     
/* 201 */     if (authenticationBytes != null) {
/* 202 */       authenticationText = Base64.encodeToString(authenticationBytes);
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 207 */       authenticationText = "=";
/*     */     } 
/*     */     
/* 210 */     this.connection.send((PlainStreamElement)new SaslStreamElements.AuthMechanism(getName(), authenticationText));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void challengeReceived(String challengeString, boolean finalChallenge) throws SmackException, SmackException.NotConnectedException, InterruptedException {
/*     */     SaslStreamElements.Response responseStanza;
/* 233 */     byte[] challenge = Base64.decode(challengeString);
/* 234 */     byte[] response = evaluateChallenge(challenge);
/* 235 */     if (finalChallenge) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 240 */     if (response == null) {
/* 241 */       responseStanza = new SaslStreamElements.Response();
/*     */     } else {
/*     */       
/* 244 */       responseStanza = new SaslStreamElements.Response(Base64.encodeToString(response));
/*     */     } 
/*     */ 
/*     */     
/* 248 */     this.connection.send((PlainStreamElement)responseStanza);
/*     */   }
/*     */   
/*     */   protected byte[] evaluateChallenge(byte[] challenge) throws SmackException {
/* 252 */     return null;
/*     */   }
/*     */   
/*     */   public final int compareTo(SASLMechanism other) {
/* 256 */     return getPriority() - other.getPriority();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SASLMechanism instanceForAuthentication(XMPPConnection connection) {
/* 271 */     SASLMechanism saslMechansim = newInstance();
/* 272 */     saslMechansim.connection = connection;
/* 273 */     return saslMechansim;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected static byte[] toBytes(String string) {
/* 279 */     return StringUtils.toBytes(string);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static String saslPrep(String string) {
/* 290 */     StringTransformer stringTransformer = saslPrepTransformer;
/* 291 */     if (stringTransformer != null) {
/* 292 */       return stringTransformer.transform(string);
/*     */     }
/* 294 */     return string;
/*     */   }
/*     */   
/*     */   protected abstract void authenticateInternal(CallbackHandler paramCallbackHandler) throws SmackException;
/*     */   
/*     */   protected abstract byte[] getAuthenticationText() throws SmackException;
/*     */   
/*     */   public abstract String getName();
/*     */   
/*     */   public abstract int getPriority();
/*     */   
/*     */   public abstract void checkIfSuccessfulOrThrow() throws SmackException;
/*     */   
/*     */   protected abstract SASLMechanism newInstance();
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jivesoftware\smack\sasl\SASLMechanism.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */