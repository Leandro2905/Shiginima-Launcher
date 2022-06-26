/*    */ package com.mojang.authlib.yggdrasil.request;
/*    */ 
/*    */ import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ValidateRequest
/*    */ {
/*    */   private String clientToken;
/*    */   private String accessToken;
/*    */   
/*    */   public ValidateRequest(YggdrasilUserAuthentication authenticationService) {
/* 13 */     this.clientToken = authenticationService.getAuthenticationService().getClientToken();
/* 14 */     this.accessToken = authenticationService.getAuthenticatedToken();
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\mojang\authlib\yggdrasil\request\ValidateRequest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */