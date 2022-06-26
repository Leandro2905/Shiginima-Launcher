/*    */ package com.mojang.authlib.yggdrasil.request;
/*    */ 
/*    */ import com.mojang.authlib.GameProfile;
/*    */ import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RefreshRequest
/*    */ {
/*    */   private String clientToken;
/*    */   private String accessToken;
/*    */   private GameProfile selectedProfile;
/*    */   private boolean requestUser = true;
/*    */   
/*    */   public RefreshRequest(YggdrasilUserAuthentication authenticationService) {
/* 16 */     this(authenticationService, null);
/*    */   }
/*    */ 
/*    */   
/*    */   public RefreshRequest(YggdrasilUserAuthentication authenticationService, GameProfile profile) {
/* 21 */     this.clientToken = authenticationService.getAuthenticationService().getClientToken();
/* 22 */     this.accessToken = authenticationService.getAuthenticatedToken();
/* 23 */     this.selectedProfile = profile;
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\mojang\authlib\yggdrasil\request\RefreshRequest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */