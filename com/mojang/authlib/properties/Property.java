/*    */ package com.mojang.authlib.properties;
/*    */ 
/*    */ import java.security.InvalidKeyException;
/*    */ import java.security.NoSuchAlgorithmException;
/*    */ import java.security.PublicKey;
/*    */ import java.security.Signature;
/*    */ import java.security.SignatureException;
/*    */ import org.apache.commons.codec.binary.Base64;
/*    */ 
/*    */ 
/*    */ public class Property
/*    */ {
/*    */   private final String name;
/*    */   private final String value;
/*    */   private final String signature;
/*    */   
/*    */   public Property(String value, String name) {
/* 18 */     this(value, name, null);
/*    */   }
/*    */ 
/*    */   
/*    */   public Property(String name, String value, String signature) {
/* 23 */     this.name = name;
/* 24 */     this.value = value;
/* 25 */     this.signature = signature;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getName() {
/* 30 */     return this.name;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getValue() {
/* 35 */     return this.value;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getSignature() {
/* 40 */     return this.signature;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean hasSignature() {
/* 45 */     return (this.signature != null);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isSignatureValid(PublicKey publicKey) {
/*    */     try {
/* 52 */       Signature signature = Signature.getInstance("SHA1withRSA");
/* 53 */       signature.initVerify(publicKey);
/* 54 */       signature.update(this.value.getBytes());
/* 55 */       return signature.verify(Base64.decodeBase64(this.signature));
/*    */     }
/* 57 */     catch (NoSuchAlgorithmException e) {
/*    */       
/* 59 */       e.printStackTrace();
/*    */     }
/* 61 */     catch (InvalidKeyException e) {
/*    */       
/* 63 */       e.printStackTrace();
/*    */     }
/* 65 */     catch (SignatureException e) {
/*    */       
/* 67 */       e.printStackTrace();
/*    */     } 
/* 69 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\mojang\authlib\properties\Property.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */