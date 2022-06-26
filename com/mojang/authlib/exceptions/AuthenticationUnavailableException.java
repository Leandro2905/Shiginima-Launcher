/*    */ package com.mojang.authlib.exceptions;
/*    */ 
/*    */ 
/*    */ public class AuthenticationUnavailableException
/*    */   extends AuthenticationException
/*    */ {
/*    */   public AuthenticationUnavailableException() {}
/*    */   
/*    */   public AuthenticationUnavailableException(String message) {
/* 10 */     super(message);
/*    */   }
/*    */ 
/*    */   
/*    */   public AuthenticationUnavailableException(String message, Throwable cause) {
/* 15 */     super(message, cause);
/*    */   }
/*    */ 
/*    */   
/*    */   public AuthenticationUnavailableException(Throwable cause) {
/* 20 */     super(cause);
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\mojang\authlib\exceptions\AuthenticationUnavailableException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */