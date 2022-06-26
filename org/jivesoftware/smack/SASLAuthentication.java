/*     */ package org.jivesoftware.smack;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.logging.Logger;
/*     */ import javax.security.auth.callback.CallbackHandler;
/*     */ import org.jivesoftware.smack.packet.Mechanisms;
/*     */ import org.jivesoftware.smack.sasl.SASLAnonymous;
/*     */ import org.jivesoftware.smack.sasl.SASLErrorException;
/*     */ import org.jivesoftware.smack.sasl.SASLMechanism;
/*     */ import org.jivesoftware.smack.sasl.packet.SaslStreamElements;
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
/*     */ public class SASLAuthentication
/*     */ {
/*  61 */   private static final Logger LOGGER = Logger.getLogger(SASLAuthentication.class.getName());
/*     */   
/*  63 */   private static final List<SASLMechanism> REGISTERED_MECHANISMS = new ArrayList<>();
/*     */   
/*  65 */   private static final Set<String> BLACKLISTED_MECHANISMS = new HashSet<>();
/*     */ 
/*     */   
/*     */   private final AbstractXMPPConnection connection;
/*     */ 
/*     */ 
/*     */   
/*     */   public static void registerSASLMechanism(SASLMechanism mechanism) {
/*  73 */     synchronized (REGISTERED_MECHANISMS) {
/*  74 */       REGISTERED_MECHANISMS.add(mechanism);
/*  75 */       Collections.sort(REGISTERED_MECHANISMS);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Map<String, String> getRegisterdSASLMechanisms() {
/*  85 */     Map<String, String> answer = new HashMap<>();
/*  86 */     synchronized (REGISTERED_MECHANISMS) {
/*  87 */       for (SASLMechanism mechanism : REGISTERED_MECHANISMS) {
/*  88 */         answer.put(mechanism.getClass().getName(), mechanism.getName());
/*     */       }
/*     */     } 
/*  91 */     return answer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean unregisterSASLMechanism(String clazz) {
/* 102 */     synchronized (REGISTERED_MECHANISMS) {
/* 103 */       Iterator<SASLMechanism> it = REGISTERED_MECHANISMS.iterator();
/* 104 */       while (it.hasNext()) {
/* 105 */         SASLMechanism mechanism = it.next();
/* 106 */         if (mechanism.getClass().getName().equals(clazz)) {
/* 107 */           it.remove();
/* 108 */           return true;
/*     */         } 
/*     */       } 
/*     */     } 
/* 112 */     return false;
/*     */   }
/*     */   
/*     */   public static boolean blacklistSASLMechanism(String mechansim) {
/* 116 */     synchronized (BLACKLISTED_MECHANISMS) {
/* 117 */       return BLACKLISTED_MECHANISMS.add(mechansim);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static boolean unBlacklistSASLMechanism(String mechanism) {
/* 122 */     synchronized (BLACKLISTED_MECHANISMS) {
/* 123 */       return BLACKLISTED_MECHANISMS.remove(mechanism);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static Set<String> getBlacklistedSASLMechanisms() {
/* 128 */     synchronized (BLACKLISTED_MECHANISMS) {
/* 129 */       return new HashSet<>(BLACKLISTED_MECHANISMS);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 134 */   private SASLMechanism currentMechanism = null;
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean authenticationSuccessful;
/*     */ 
/*     */ 
/*     */   
/*     */   private Exception saslException;
/*     */ 
/*     */ 
/*     */   
/*     */   SASLAuthentication(AbstractXMPPConnection connection) {
/* 147 */     this.connection = connection;
/* 148 */     init();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasAnonymousAuthentication() {
/* 157 */     return serverMechanisms().contains("ANONYMOUS");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasNonAnonymousAuthentication() {
/* 166 */     return (!serverMechanisms().isEmpty() && (serverMechanisms().size() != 1 || !hasAnonymousAuthentication()));
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
/*     */   public void authenticate(String resource, CallbackHandler cbh) throws IOException, XMPPException.XMPPErrorException, SASLErrorException, SmackException, InterruptedException {
/* 187 */     SASLMechanism selectedMechanism = selectMechanism();
/* 188 */     if (selectedMechanism != null) {
/* 189 */       this.currentMechanism = selectedMechanism;
/* 190 */       synchronized (this) {
/* 191 */         this.currentMechanism.authenticate(this.connection.getHost(), this.connection.getServiceName(), cbh);
/*     */         
/* 193 */         wait(this.connection.getPacketReplyTimeout());
/*     */       } 
/*     */       
/* 196 */       maybeThrowException();
/*     */       
/* 198 */       if (!this.authenticationSuccessful) {
/* 199 */         throw SmackException.NoResponseException.newWith(this.connection);
/*     */       }
/*     */     } else {
/*     */       
/* 203 */       throw new SmackException("SASL Authentication failed. No known authentication mechanisims.");
/*     */     } 
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
/*     */   public void authenticate(String username, String password, String resource) throws XMPPException.XMPPErrorException, SASLErrorException, IOException, SmackException, InterruptedException {
/* 228 */     SASLMechanism selectedMechanism = selectMechanism();
/* 229 */     if (selectedMechanism != null) {
/* 230 */       this.currentMechanism = selectedMechanism;
/*     */       
/* 232 */       synchronized (this) {
/* 233 */         this.currentMechanism.authenticate(username, this.connection.getHost(), this.connection.getServiceName(), password);
/*     */ 
/*     */         
/* 236 */         wait(this.connection.getPacketReplyTimeout());
/*     */       } 
/*     */       
/* 239 */       maybeThrowException();
/*     */       
/* 241 */       if (!this.authenticationSuccessful) {
/* 242 */         throw SmackException.NoResponseException.newWith(this.connection);
/*     */       }
/*     */     } else {
/*     */       
/* 246 */       throw new SmackException("SASL Authentication failed. No known authentication mechanisims.");
/*     */     } 
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
/*     */   public void authenticateAnonymously() throws SASLErrorException, SmackException, XMPPException.XMPPErrorException, InterruptedException {
/* 266 */     this.currentMechanism = (new SASLAnonymous()).instanceForAuthentication(this.connection);
/*     */ 
/*     */     
/* 269 */     synchronized (this) {
/* 270 */       this.currentMechanism.authenticate(null, null, null, "");
/* 271 */       wait(this.connection.getPacketReplyTimeout());
/*     */     } 
/*     */     
/* 274 */     maybeThrowException();
/*     */     
/* 276 */     if (!this.authenticationSuccessful) {
/* 277 */       throw SmackException.NoResponseException.newWith(this.connection);
/*     */     }
/*     */   }
/*     */   
/*     */   private void maybeThrowException() throws SmackException, SASLErrorException {
/* 282 */     if (this.saslException != null) {
/* 283 */       if (this.saslException instanceof SmackException)
/* 284 */         throw (SmackException)this.saslException; 
/* 285 */       if (this.saslException instanceof SASLErrorException) {
/* 286 */         throw (SASLErrorException)this.saslException;
/*     */       }
/* 288 */       throw new IllegalStateException("Unexpected exception type", this.saslException);
/*     */     } 
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
/*     */   public void challengeReceived(String challenge) throws SmackException, InterruptedException {
/* 302 */     challengeReceived(challenge, false);
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
/*     */   public void challengeReceived(String challenge, boolean finalChallenge) throws SmackException, InterruptedException {
/*     */     try {
/* 318 */       this.currentMechanism.challengeReceived(challenge, finalChallenge);
/* 319 */     } catch (InterruptedException|SmackException e) {
/* 320 */       authenticationFailed(e);
/* 321 */       throw e;
/*     */     } 
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
/*     */   public void authenticated(SaslStreamElements.Success success) throws SmackException, InterruptedException {
/* 337 */     if (success.getData() != null) {
/* 338 */       challengeReceived(success.getData(), true);
/*     */     }
/* 340 */     this.currentMechanism.checkIfSuccessfulOrThrow();
/* 341 */     this.authenticationSuccessful = true;
/*     */     
/* 343 */     synchronized (this) {
/* 344 */       notify();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void authenticationFailed(SaslStreamElements.SASLFailure saslFailure) {
/* 356 */     authenticationFailed((Exception)new SASLErrorException(this.currentMechanism.getName(), saslFailure));
/*     */   }
/*     */   
/*     */   public void authenticationFailed(Exception exception) {
/* 360 */     this.saslException = exception;
/*     */     
/* 362 */     synchronized (this) {
/* 363 */       notify();
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean authenticationSuccessful() {
/* 368 */     return this.authenticationSuccessful;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void init() {
/* 377 */     this.authenticationSuccessful = false;
/* 378 */     this.saslException = null;
/*     */   }
/*     */ 
/*     */   
/*     */   private SASLMechanism selectMechanism() {
/* 383 */     SASLMechanism selectedMechanism = null;
/* 384 */     Iterator<SASLMechanism> it = REGISTERED_MECHANISMS.iterator();
/*     */     
/* 386 */     while (it.hasNext()) {
/* 387 */       SASLMechanism mechanism = it.next();
/* 388 */       String mechanismName = mechanism.getName();
/* 389 */       synchronized (BLACKLISTED_MECHANISMS) {
/* 390 */         if (BLACKLISTED_MECHANISMS.contains(mechanismName)) {
/*     */           continue;
/*     */         }
/*     */       } 
/* 394 */       if (serverMechanisms().contains(mechanismName)) {
/*     */         
/* 396 */         selectedMechanism = mechanism.instanceForAuthentication(this.connection);
/*     */         break;
/*     */       } 
/*     */     } 
/* 400 */     return selectedMechanism;
/*     */   }
/*     */   
/*     */   private List<String> serverMechanisms() {
/* 404 */     Mechanisms mechanisms = this.connection.<Mechanisms>getFeature("mechanisms", "urn:ietf:params:xml:ns:xmpp-sasl");
/* 405 */     if (mechanisms == null) {
/* 406 */       LOGGER.warning("Server did not report any SASL mechanisms");
/* 407 */       return Collections.emptyList();
/*     */     } 
/* 409 */     return mechanisms.getMechanisms();
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jivesoftware\smack\SASLAuthentication.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */