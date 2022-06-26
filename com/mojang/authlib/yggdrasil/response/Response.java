/*    */ package com.mojang.authlib.yggdrasil.response;
/*    */ 
/*    */ 
/*    */ public class Response
/*    */ {
/*    */   private String error;
/*    */   private String errorMessage;
/*    */   private String cause;
/*    */   
/*    */   public String getError() {
/* 11 */     return this.error;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getCause() {
/* 16 */     return this.cause;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getErrorMessage() {
/* 21 */     return this.errorMessage;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void setError(String error) {
/* 26 */     this.error = error;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void setErrorMessage(String errorMessage) {
/* 31 */     this.errorMessage = errorMessage;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void setCause(String cause) {
/* 36 */     this.cause = cause;
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\mojang\authlib\yggdrasil\response\Response.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */