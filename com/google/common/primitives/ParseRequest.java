/*    */ package com.google.common.primitives;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
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
/*    */ @GwtCompatible
/*    */ final class ParseRequest
/*    */ {
/*    */   final String rawValue;
/*    */   final int radix;
/*    */   
/*    */   private ParseRequest(String rawValue, int radix) {
/* 28 */     this.rawValue = rawValue;
/* 29 */     this.radix = radix;
/*    */   } static ParseRequest fromString(String stringValue) {
/*    */     String rawValue;
/*    */     int radix;
/* 33 */     if (stringValue.length() == 0) {
/* 34 */       throw new NumberFormatException("empty string");
/*    */     }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 40 */     char firstChar = stringValue.charAt(0);
/* 41 */     if (stringValue.startsWith("0x") || stringValue.startsWith("0X")) {
/* 42 */       rawValue = stringValue.substring(2);
/* 43 */       radix = 16;
/* 44 */     } else if (firstChar == '#') {
/* 45 */       rawValue = stringValue.substring(1);
/* 46 */       radix = 16;
/* 47 */     } else if (firstChar == '0' && stringValue.length() > 1) {
/* 48 */       rawValue = stringValue.substring(1);
/* 49 */       radix = 8;
/*    */     } else {
/* 51 */       rawValue = stringValue;
/* 52 */       radix = 10;
/*    */     } 
/*    */     
/* 55 */     return new ParseRequest(rawValue, radix);
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\primitives\ParseRequest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */