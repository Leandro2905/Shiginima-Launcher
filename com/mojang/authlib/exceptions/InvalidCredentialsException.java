/*    */ package com.mojang.authlib.exceptions;
/*    */ 
/*    */ 
/*    */ public class InvalidCredentialsException
/*    */   extends AuthenticationException
/*    */ {
/*    */   public InvalidCredentialsException() {}
/*    */   
/*    */   public InvalidCredentialsException(String message) {
/* 10 */     super(message);
/*    */   }
/*    */ 
/*    */   
/*    */   public InvalidCredentialsException(String message, Throwable cause) {
/* 15 */     super(message, cause);
/*    */   }
/*    */ 
/*    */   
/*    */   public InvalidCredentialsException(Throwable cause) {
/* 20 */     super(cause);
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\mojang\authlib\exceptions\InvalidCredentialsException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */