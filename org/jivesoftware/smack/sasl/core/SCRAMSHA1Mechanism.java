/*     */ package org.jivesoftware.smack.sasl.core;
/*     */ 
/*     */ import java.security.InvalidKeyException;
/*     */ import java.security.SecureRandom;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import javax.security.auth.callback.CallbackHandler;
/*     */ import org.jivesoftware.smack.SmackException;
/*     */ import org.jivesoftware.smack.sasl.SASLMechanism;
/*     */ import org.jivesoftware.smack.util.ByteUtils;
/*     */ import org.jivesoftware.smack.util.MAC;
/*     */ import org.jivesoftware.smack.util.SHA1;
/*     */ import org.jivesoftware.smack.util.stringencoder.Base64;
/*     */ import org.jxmpp.util.cache.Cache;
/*     */ import org.jxmpp.util.cache.LruCache;
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
/*     */ public class SCRAMSHA1Mechanism
/*     */   extends SASLMechanism
/*     */ {
/*     */   public static final String NAME = "SCRAM-SHA-1";
/*     */   private static final int RANDOM_ASCII_BYTE_COUNT = 32;
/*     */   private static final String DEFAULT_GS2_HEADER = "n,,";
/*  42 */   private static final byte[] CLIENT_KEY_BYTES = toBytes("Client Key");
/*  43 */   private static final byte[] SERVER_KEY_BYTES = toBytes("Server Key");
/*  44 */   private static final byte[] ONE = new byte[] { 0, 0, 0, 1 };
/*     */   
/*  46 */   private static final SecureRandom RANDOM = new SecureRandom();
/*     */   
/*  48 */   private static final Cache<String, Keys> CACHE = (Cache<String, Keys>)new LruCache(10);
/*     */   
/*     */   private enum State {
/*  51 */     INITIAL,
/*  52 */     AUTH_TEXT_SENT,
/*  53 */     RESPONSE_SENT,
/*  54 */     VALID_SERVER_RESPONSE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  60 */   private State state = State.INITIAL;
/*     */ 
/*     */   
/*     */   private String clientRandomAscii;
/*     */ 
/*     */   
/*     */   private String clientFirstMessageBare;
/*     */   
/*     */   private byte[] serverSignature;
/*     */ 
/*     */   
/*     */   protected void authenticateInternal(CallbackHandler cbh) throws SmackException {
/*  72 */     throw new UnsupportedOperationException("CallbackHandler not (yet) supported");
/*     */   }
/*     */ 
/*     */   
/*     */   protected byte[] getAuthenticationText() throws SmackException {
/*  77 */     this.clientRandomAscii = getRandomAscii();
/*  78 */     String saslPrepedAuthcId = saslPrep(this.authenticationId);
/*  79 */     this.clientFirstMessageBare = "n=" + escape(saslPrepedAuthcId) + ",r=" + this.clientRandomAscii;
/*  80 */     String clientFirstMessage = "n,," + this.clientFirstMessageBare;
/*  81 */     this.state = State.AUTH_TEXT_SENT;
/*  82 */     return toBytes(clientFirstMessage);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getName() {
/*  87 */     return "SCRAM-SHA-1";
/*     */   }
/*     */ 
/*     */   
/*     */   public int getPriority() {
/*  92 */     return 110;
/*     */   }
/*     */ 
/*     */   
/*     */   public SCRAMSHA1Mechanism newInstance() {
/*  97 */     return new SCRAMSHA1Mechanism();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void checkIfSuccessfulOrThrow() throws SmackException {
/* 103 */     if (this.state != State.VALID_SERVER_RESPONSE)
/* 104 */       throw new SmackException("SCRAM-SHA1 is missing valid server response");  } protected byte[] evaluateChallenge(byte[] challenge) throws SmackException { String serverFirstMessage; Map<Character, String> attributes; String rvalue, receivedClientRandomAscii; int iterations; String iterationsString, salt, clientFinalMessageWithoutProof; byte[] authMessage;
/*     */     String cacheKey;
/*     */     byte[] serverKey, clientKey;
/*     */     Keys keys;
/*     */     byte[] storedKey, clientSignature, clientProof;
/*     */     int i;
/* 110 */     String clientFinalMessage, clientCalculatedServerFinalMessage, challengeString = new String(challenge);
/* 111 */     switch (this.state) {
/*     */       case AUTH_TEXT_SENT:
/* 113 */         serverFirstMessage = challengeString;
/* 114 */         attributes = parseAttributes(challengeString);
/*     */ 
/*     */         
/* 117 */         rvalue = attributes.get(Character.valueOf('r'));
/* 118 */         if (rvalue == null) {
/* 119 */           throw new SmackException("Server random ASCII is null");
/*     */         }
/* 121 */         if (rvalue.length() <= this.clientRandomAscii.length()) {
/* 122 */           throw new SmackException("Server random ASCII is shorter then client random ASCII");
/*     */         }
/* 124 */         receivedClientRandomAscii = rvalue.substring(0, this.clientRandomAscii.length());
/* 125 */         if (!receivedClientRandomAscii.equals(this.clientRandomAscii)) {
/* 126 */           throw new SmackException("Received client random ASCII does not match client random ASCII");
/*     */         }
/*     */ 
/*     */ 
/*     */         
/* 131 */         iterationsString = attributes.get(Character.valueOf('i'));
/* 132 */         if (iterationsString == null) {
/* 133 */           throw new SmackException("Iterations attribute not set");
/*     */         }
/*     */         try {
/* 136 */           iterations = Integer.parseInt(iterationsString);
/*     */         }
/* 138 */         catch (NumberFormatException e) {
/* 139 */           throw new SmackException("Exception parsing iterations", e);
/*     */         } 
/*     */ 
/*     */         
/* 143 */         salt = attributes.get(Character.valueOf('s'));
/* 144 */         if (salt == null) {
/* 145 */           throw new SmackException("SALT not send");
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 151 */         clientFinalMessageWithoutProof = "c=" + Base64.encode("n,,") + ",r=" + rvalue;
/*     */ 
/*     */ 
/*     */         
/* 155 */         authMessage = toBytes(this.clientFirstMessageBare + ',' + serverFirstMessage + ',' + clientFinalMessageWithoutProof);
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 160 */         cacheKey = this.password + ',' + salt;
/*     */         
/* 162 */         keys = (Keys)CACHE.get(cacheKey);
/* 163 */         if (keys == null) {
/*     */           
/* 165 */           byte[] saltedPassword = hi(saslPrep(this.password), Base64.decode(salt), iterations);
/*     */ 
/*     */           
/* 168 */           serverKey = hmac(saltedPassword, SERVER_KEY_BYTES);
/*     */ 
/*     */           
/* 171 */           clientKey = hmac(saltedPassword, CLIENT_KEY_BYTES);
/*     */           
/* 173 */           keys = new Keys(clientKey, serverKey);
/* 174 */           CACHE.put(cacheKey, keys);
/*     */         } else {
/*     */           
/* 177 */           serverKey = keys.serverKey;
/* 178 */           clientKey = keys.clientKey;
/*     */         } 
/*     */ 
/*     */         
/* 182 */         this.serverSignature = hmac(serverKey, authMessage);
/*     */ 
/*     */         
/* 185 */         storedKey = SHA1.bytes(clientKey);
/*     */ 
/*     */         
/* 188 */         clientSignature = hmac(storedKey, authMessage);
/*     */ 
/*     */         
/* 191 */         clientProof = new byte[clientKey.length];
/* 192 */         for (i = 0; i < clientProof.length; i++) {
/* 193 */           clientProof[i] = (byte)(clientKey[i] ^ clientSignature[i]);
/*     */         }
/*     */         
/* 196 */         clientFinalMessage = clientFinalMessageWithoutProof + ",p=" + Base64.encodeToString(clientProof);
/* 197 */         this.state = State.RESPONSE_SENT;
/* 198 */         return toBytes(clientFinalMessage);
/*     */       case RESPONSE_SENT:
/* 200 */         clientCalculatedServerFinalMessage = "v=" + Base64.encodeToString(this.serverSignature);
/* 201 */         if (!clientCalculatedServerFinalMessage.equals(challengeString)) {
/* 202 */           throw new SmackException("Server final message does not match calculated one");
/*     */         }
/* 204 */         this.state = State.VALID_SERVER_RESPONSE;
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 209 */         return null;
/*     */     } 
/*     */     throw new SmackException("Invalid state"); }
/*     */    private static Map<Character, String> parseAttributes(String string) throws SmackException {
/* 213 */     if (string.length() == 0) {
/* 214 */       return Collections.emptyMap();
/*     */     }
/*     */     
/* 217 */     String[] keyValuePairs = string.split(",");
/* 218 */     Map<Character, String> res = new HashMap<>(keyValuePairs.length, 1.0F);
/* 219 */     for (String keyValuePair : keyValuePairs) {
/* 220 */       if (keyValuePair.length() < 3) {
/* 221 */         throw new SmackException("Invalid Key-Value pair: " + keyValuePair);
/*     */       }
/* 223 */       char key = keyValuePair.charAt(0);
/* 224 */       if (keyValuePair.charAt(1) != '=') {
/* 225 */         throw new SmackException("Invalid Key-Value pair: " + keyValuePair);
/*     */       }
/* 227 */       String value = keyValuePair.substring(2);
/* 228 */       res.put(Character.valueOf(key), value);
/*     */     } 
/*     */     
/* 231 */     return res;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   String getRandomAscii() {
/* 242 */     int count = 0;
/* 243 */     char[] randomAscii = new char[32];
/* 244 */     while (count < 32) {
/* 245 */       int r = RANDOM.nextInt(128);
/* 246 */       char c = (char)r;
/*     */       
/* 248 */       if (!isPrintableNonCommaAsciiChar(c)) {
/*     */         continue;
/*     */       }
/* 251 */       randomAscii[count++] = c;
/*     */     } 
/* 253 */     return new String(randomAscii);
/*     */   }
/*     */   
/*     */   private static boolean isPrintableNonCommaAsciiChar(char c) {
/* 257 */     if (c == ',') {
/* 258 */       return false;
/*     */     }
/* 260 */     return (c >= ' ' && c < '');
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
/*     */   private static String escape(String string) {
/* 274 */     StringBuilder sb = new StringBuilder((int)(string.length() * 1.1D));
/* 275 */     for (int i = 0; i < string.length(); i++) {
/* 276 */       char c = string.charAt(i);
/* 277 */       switch (c) {
/*     */         case ',':
/* 279 */           sb.append("=2C");
/*     */           break;
/*     */         case '=':
/* 282 */           sb.append("=3D");
/*     */           break;
/*     */         default:
/* 285 */           sb.append(c);
/*     */           break;
/*     */       } 
/*     */     } 
/* 289 */     return sb.toString();
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
/*     */   private static byte[] hmac(byte[] key, byte[] str) throws SmackException {
/*     */     try {
/* 302 */       return MAC.hmacsha1(key, str);
/*     */     }
/* 304 */     catch (InvalidKeyException e) {
/* 305 */       throw new SmackException("SCRAM-SHA-1 HMAC-SHA1 Exception", e);
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
/*     */   private static byte[] hi(String str, byte[] salt, int iterations) throws SmackException {
/* 323 */     byte[] key = str.getBytes();
/*     */     
/* 325 */     byte[] u = hmac(key, ByteUtils.concact(salt, ONE));
/* 326 */     byte[] res = (byte[])u.clone();
/* 327 */     for (int i = 1; i < iterations; i++) {
/* 328 */       u = hmac(key, u);
/* 329 */       for (int j = 0; j < u.length; j++) {
/* 330 */         res[j] = (byte)(res[j] ^ u[j]);
/*     */       }
/*     */     } 
/* 333 */     return res;
/*     */   }
/*     */   
/*     */   private static class Keys {
/*     */     private final byte[] clientKey;
/*     */     private final byte[] serverKey;
/*     */     
/*     */     public Keys(byte[] clientKey, byte[] serverKey) {
/* 341 */       this.clientKey = clientKey;
/* 342 */       this.serverKey = serverKey;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jivesoftware\smack\sasl\core\SCRAMSHA1Mechanism.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */