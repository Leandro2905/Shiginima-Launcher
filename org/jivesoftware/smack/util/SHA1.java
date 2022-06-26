/*    */ package org.jivesoftware.smack.util;
/*    */ 
/*    */ import java.security.MessageDigest;
/*    */ import java.security.NoSuchAlgorithmException;
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
/*    */ public class SHA1
/*    */ {
/*    */   private static MessageDigest SHA1_DIGEST;
/*    */   
/*    */   static {
/*    */     try {
/* 31 */       SHA1_DIGEST = MessageDigest.getInstance("SHA-1");
/*    */     }
/* 33 */     catch (NoSuchAlgorithmException e) {
/*    */ 
/*    */       
/* 36 */       throw new IllegalStateException(e);
/*    */     } 
/*    */   }
/*    */   
/*    */   public static synchronized byte[] bytes(byte[] bytes) {
/* 41 */     SHA1_DIGEST.update(bytes);
/* 42 */     return SHA1_DIGEST.digest();
/*    */   }
/*    */   
/*    */   public static byte[] bytes(String string) {
/* 46 */     return bytes(StringUtils.toBytes(string));
/*    */   }
/*    */   
/*    */   public static String hex(byte[] bytes) {
/* 50 */     return StringUtils.encodeHex(bytes(bytes));
/*    */   }
/*    */   
/*    */   public static String hex(String string) {
/* 54 */     return hex(StringUtils.toBytes(string));
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jivesoftware\smac\\util\SHA1.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */