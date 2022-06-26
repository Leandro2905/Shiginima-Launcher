/*    */ package com.mojang.authlib.yggdrasil;
/*    */ 
/*    */ 
/*    */ public class ProfileIncompleteException
/*    */   extends RuntimeException
/*    */ {
/*    */   public ProfileIncompleteException() {}
/*    */   
/*    */   public ProfileIncompleteException(String message) {
/* 10 */     super(message);
/*    */   }
/*    */ 
/*    */   
/*    */   public ProfileIncompleteException(String message, Throwable cause) {
/* 15 */     super(message, cause);
/*    */   }
/*    */ 
/*    */   
/*    */   public ProfileIncompleteException(Throwable cause) {
/* 20 */     super(cause);
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\mojang\authlib\yggdrasil\ProfileIncompleteException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */