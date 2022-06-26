/*    */ package com.google.common.hash;
/*    */ 
/*    */ import java.nio.charset.Charset;
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
/*    */ abstract class AbstractHasher
/*    */   implements Hasher
/*    */ {
/*    */   public final Hasher putBoolean(boolean b) {
/* 28 */     return putByte(b ? 1 : 0);
/*    */   }
/*    */   
/*    */   public final Hasher putDouble(double d) {
/* 32 */     return putLong(Double.doubleToRawLongBits(d));
/*    */   }
/*    */   
/*    */   public final Hasher putFloat(float f) {
/* 36 */     return putInt(Float.floatToRawIntBits(f));
/*    */   }
/*    */   
/*    */   public Hasher putUnencodedChars(CharSequence charSequence) {
/* 40 */     for (int i = 0, len = charSequence.length(); i < len; i++) {
/* 41 */       putChar(charSequence.charAt(i));
/*    */     }
/* 43 */     return this;
/*    */   }
/*    */   
/*    */   public Hasher putString(CharSequence charSequence, Charset charset) {
/* 47 */     return putBytes(charSequence.toString().getBytes(charset));
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\hash\AbstractHasher.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */