/*    */ package com.mojang.authlib.yggdrasil.request;
/*    */ 
/*    */ import com.mojang.authlib.Agent;
/*    */ import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AuthenticationRequest
/*    */ {
/*    */   private Agent agent;
/*    */   private String username;
/*    */   private String password;
/*    */   private String clientToken;
/*    */   private boolean requestUser = true;
/*    */   
/*    */   public AuthenticationRequest(YggdrasilUserAuthentication authenticationService, String username, String password) {
/* 17 */     this.agent = authenticationService.getAgent();
/* 18 */     this.username = username;
/* 19 */     this.clientToken = authenticationService.getAuthenticationService().getClientToken();
/* 20 */     this.password = password;
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\mojang\authlib\yggdrasil\request\AuthenticationRequest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */