/*    */ package org.jivesoftware.smack.util;
/*    */ 
/*    */ import java.security.InvalidKeyException;
/*    */ import java.security.NoSuchAlgorithmException;
/*    */ import javax.crypto.Mac;
/*    */ import javax.crypto.spec.SecretKeySpec;
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
/*    */ public class MAC
/*    */ {
/*    */   public static final String HMACSHA1 = "HmacSHA1";
/*    */   private static Mac HMAC_SHA1;
/*    */   
/*    */   static {
/*    */     try {
/* 33 */       HMAC_SHA1 = Mac.getInstance("HmacSHA1");
/*    */     }
/* 35 */     catch (NoSuchAlgorithmException e) {
/*    */ 
/*    */       
/* 38 */       throw new IllegalStateException(e);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public static synchronized byte[] hmacsha1(SecretKeySpec key, byte[] input) throws InvalidKeyException {
/* 44 */     HMAC_SHA1.init(key);
/* 45 */     return HMAC_SHA1.doFinal(input);
/*    */   }
/*    */   
/*    */   public static byte[] hmacsha1(byte[] keyBytes, byte[] input) throws InvalidKeyException {
/* 49 */     SecretKeySpec key = new SecretKeySpec(keyBytes, "HmacSHA1");
/* 50 */     return hmacsha1(key, input);
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jivesoftware\smac\\util\MAC.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */