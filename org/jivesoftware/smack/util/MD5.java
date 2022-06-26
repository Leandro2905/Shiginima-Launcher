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
/*    */ public class MD5
/*    */ {
/*    */   private static MessageDigest MD5_DIGEST;
/*    */   
/*    */   static {
/*    */     try {
/* 31 */       MD5_DIGEST = MessageDigest.getInstance("MD5");
/*    */     }
/* 33 */     catch (NoSuchAlgorithmException e) {
/*    */ 
/*    */       
/* 36 */       throw new IllegalStateException(e);
/*    */     } 
/*    */   }
/*    */   
/*    */   public static synchronized byte[] bytes(byte[] bytes) {
/* 41 */     return MD5_DIGEST.digest(bytes);
/*    */   }
/*    */   
/*    */   public static byte[] bytes(String string) {
/* 45 */     return bytes(StringUtils.toBytes(string));
/*    */   }
/*    */   
/*    */   public static String hex(byte[] bytes) {
/* 49 */     return StringUtils.encodeHex(bytes(bytes));
/*    */   }
/*    */   
/*    */   public static String hex(String string) {
/* 53 */     return hex(StringUtils.toBytes(string));
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jivesoftware\smac\\util\MD5.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */