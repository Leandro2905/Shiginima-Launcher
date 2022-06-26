/*    */ package org.apache.logging.log4j.core.util;
/*    */ 
/*    */ import java.security.MessageDigest;
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
/*    */ public final class NameUtil
/*    */ {
/*    */   private static final int MASK = 255;
/*    */   
/*    */   public static String getSubName(String name) {
/* 34 */     if (name.isEmpty()) {
/* 35 */       return null;
/*    */     }
/* 37 */     int i = name.lastIndexOf('.');
/* 38 */     return (i > 0) ? name.substring(0, i) : "";
/*    */   }
/*    */   
/*    */   public static String md5(String string) {
/*    */     try {
/* 43 */       MessageDigest digest = MessageDigest.getInstance("MD5");
/* 44 */       digest.update(string.getBytes());
/* 45 */       byte[] bytes = digest.digest();
/* 46 */       StringBuilder md5 = new StringBuilder();
/* 47 */       for (byte b : bytes) {
/* 48 */         String hex = Integer.toHexString(0xFF & b);
/* 49 */         if (hex.length() == 1) {
/* 50 */           md5.append('0');
/*    */         }
/* 52 */         md5.append(hex);
/*    */       } 
/* 54 */       return md5.toString();
/* 55 */     } catch (Exception ex) {
/* 56 */       return string;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\cor\\util\NameUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */