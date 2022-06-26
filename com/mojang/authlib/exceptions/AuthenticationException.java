/*    */ package com.mojang.authlib.exceptions;
/*    */ 
/*    */ 
/*    */ public class AuthenticationException
/*    */   extends Exception
/*    */ {
/*    */   public AuthenticationException() {}
/*    */   
/*    */   public AuthenticationException(String message) {
/* 10 */     super(message);
/*    */   }
/*    */ 
/*    */   
/*    */   public AuthenticationException(String message, Throwable cause) {
/* 15 */     super(message, cause);
/*    */   }
/*    */ 
/*    */   
/*    */   public AuthenticationException(Throwable cause) {
/* 20 */     super(cause);
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\mojang\authlib\exceptions\AuthenticationException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */