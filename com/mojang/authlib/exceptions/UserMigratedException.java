/*    */ package com.mojang.authlib.exceptions;
/*    */ 
/*    */ 
/*    */ public class UserMigratedException
/*    */   extends InvalidCredentialsException
/*    */ {
/*    */   public UserMigratedException() {}
/*    */   
/*    */   public UserMigratedException(String message) {
/* 10 */     super(message);
/*    */   }
/*    */ 
/*    */   
/*    */   public UserMigratedException(String message, Throwable cause) {
/* 15 */     super(message, cause);
/*    */   }
/*    */ 
/*    */   
/*    */   public UserMigratedException(Throwable cause) {
/* 20 */     super(cause);
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\mojang\authlib\exceptions\UserMigratedException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */