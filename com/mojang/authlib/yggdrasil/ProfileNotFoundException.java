/*    */ package com.mojang.authlib.yggdrasil;
/*    */ 
/*    */ 
/*    */ public class ProfileNotFoundException
/*    */   extends RuntimeException
/*    */ {
/*    */   public ProfileNotFoundException() {}
/*    */   
/*    */   public ProfileNotFoundException(String message) {
/* 10 */     super(message);
/*    */   }
/*    */ 
/*    */   
/*    */   public ProfileNotFoundException(String message, Throwable cause) {
/* 15 */     super(message, cause);
/*    */   }
/*    */ 
/*    */   
/*    */   public ProfileNotFoundException(Throwable cause) {
/* 20 */     super(cause);
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\mojang\authlib\yggdrasil\ProfileNotFoundException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */