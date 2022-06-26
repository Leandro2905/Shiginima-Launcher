/*    */ package org.jivesoftware.smack.util.stringencoder;
/*    */ 
/*    */ import java.io.UnsupportedEncodingException;
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
/*    */ 
/*    */ 
/*    */ public class Base64
/*    */ {
/*    */   private static Encoder base64encoder;
/*    */   
/*    */   public static void setEncoder(Encoder encoder) {
/* 29 */     Objects.requireNonNull(encoder, "encoder must no be null");
/* 30 */     base64encoder = encoder;
/*    */   }
/*    */   
/*    */   public static final String encode(String string) {
/*    */     try {
/* 35 */       return encodeToString(string.getBytes("UTF-8"));
/* 36 */     } catch (UnsupportedEncodingException e) {
/* 37 */       throw new IllegalStateException("UTF-8 not supported", e);
/*    */     } 
/*    */   }
/*    */   
/*    */   public static final String encodeToString(byte[] input) {
/* 42 */     byte[] bytes = encode(input);
/*    */     try {
/* 44 */       return new String(bytes, "US-ASCII");
/* 45 */     } catch (UnsupportedEncodingException e) {
/* 46 */       throw new AssertionError(e);
/*    */     } 
/*    */   }
/*    */   
/*    */   public static final String encodeToString(byte[] input, int offset, int len) {
/* 51 */     byte[] bytes = encode(input, offset, len);
/*    */     try {
/* 53 */       return new String(bytes, "US-ASCII");
/* 54 */     } catch (UnsupportedEncodingException e) {
/* 55 */       throw new AssertionError(e);
/*    */     } 
/*    */   }
/*    */   
/*    */   public static final byte[] encode(byte[] input) {
/* 60 */     return encode(input, 0, input.length);
/*    */   }
/*    */   
/*    */   public static final byte[] encode(byte[] input, int offset, int len) {
/* 64 */     return base64encoder.encode(input, offset, len);
/*    */   }
/*    */   
/*    */   public static final String decodeToString(String string) {
/* 68 */     byte[] bytes = decode(string);
/*    */     try {
/* 70 */       return new String(bytes, "UTF-8");
/* 71 */     } catch (UnsupportedEncodingException e) {
/* 72 */       throw new IllegalStateException("UTF-8 not supported", e);
/*    */     } 
/*    */   }
/*    */   
/*    */   public static final String decodeToString(byte[] input, int offset, int len) {
/* 77 */     byte[] bytes = decode(input, offset, len);
/*    */     try {
/* 79 */       return new String(bytes, "UTF-8");
/* 80 */     } catch (UnsupportedEncodingException e) {
/* 81 */       throw new IllegalStateException("UTF-8 not supported", e);
/*    */     } 
/*    */   }
/*    */   
/*    */   public static final byte[] decode(String string) {
/* 86 */     return base64encoder.decode(string);
/*    */   }
/*    */   
/*    */   public static final byte[] decode(byte[] input) {
/* 90 */     return base64encoder.decode(input, 0, input.length);
/*    */   }
/*    */   
/*    */   public static final byte[] decode(byte[] input, int offset, int len) {
/* 94 */     return base64encoder.decode(input, offset, len);
/*    */   }
/*    */   
/*    */   public static interface Encoder {
/*    */     byte[] decode(String param1String);
/*    */     
/*    */     byte[] decode(byte[] param1ArrayOfbyte, int param1Int1, int param1Int2);
/*    */     
/*    */     String encodeToString(byte[] param1ArrayOfbyte, int param1Int1, int param1Int2);
/*    */     
/*    */     byte[] encode(byte[] param1ArrayOfbyte, int param1Int1, int param1Int2);
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jivesoftware\smac\\util\stringencoder\Base64.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */