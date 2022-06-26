/*    */ package com.google.gson.internal;
/*    */ 
/*    */ import java.io.ObjectStreamException;
/*    */ import java.math.BigDecimal;
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
/*    */ public final class LazilyParsedNumber
/*    */   extends Number
/*    */ {
/*    */   private final String value;
/*    */   
/*    */   public LazilyParsedNumber(String value) {
/* 30 */     this.value = value;
/*    */   }
/*    */ 
/*    */   
/*    */   public int intValue() {
/*    */     try {
/* 36 */       return Integer.parseInt(this.value);
/* 37 */     } catch (NumberFormatException e) {
/*    */       try {
/* 39 */         return (int)Long.parseLong(this.value);
/* 40 */       } catch (NumberFormatException nfe) {
/* 41 */         return (new BigDecimal(this.value)).intValue();
/*    */       } 
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public long longValue() {
/*    */     try {
/* 49 */       return Long.parseLong(this.value);
/* 50 */     } catch (NumberFormatException e) {
/* 51 */       return (new BigDecimal(this.value)).longValue();
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public float floatValue() {
/* 57 */     return Float.parseFloat(this.value);
/*    */   }
/*    */ 
/*    */   
/*    */   public double doubleValue() {
/* 62 */     return Double.parseDouble(this.value);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 67 */     return this.value;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private Object writeReplace() throws ObjectStreamException {
/* 76 */     return new BigDecimal(this.value);
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\gson\internal\LazilyParsedNumber.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */