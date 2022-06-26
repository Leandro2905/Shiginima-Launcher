/*    */ package org.jivesoftware.smack.util.stringencoder;
/*    */ 
/*    */ import org.jivesoftware.smack.util.Objects;
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
/*    */ public class Base64UrlSafeEncoder
/*    */ {
/*    */   private static StringEncoder base64UrlSafeEncoder;
/*    */   
/*    */   public static void setEncoder(StringEncoder encoder) {
/* 26 */     Objects.requireNonNull(encoder, "encoder must no be null");
/* 27 */     base64UrlSafeEncoder = encoder;
/*    */   }
/*    */   
/*    */   public static StringEncoder getStringEncoder() {
/* 31 */     return base64UrlSafeEncoder;
/*    */   }
/*    */   
/*    */   public static final String encode(String string) {
/* 35 */     return base64UrlSafeEncoder.encode(string);
/*    */   }
/*    */   
/*    */   public static final String decode(String string) {
/* 39 */     return base64UrlSafeEncoder.decode(string);
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jivesoftware\smac\\util\stringencoder\Base64UrlSafeEncoder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */