/*    */ package com.mojang.authlib.yggdrasil.response;
/*    */ 
/*    */ import com.mojang.authlib.GameProfile;
/*    */ 
/*    */ 
/*    */ public class AuthenticationResponse
/*    */   extends Response
/*    */ {
/*    */   private String accessToken;
/*    */   private String clientToken;
/*    */   private GameProfile selectedProfile;
/*    */   private GameProfile[] availableProfiles;
/*    */   private User user;
/*    */   
/*    */   public String getAccessToken() {
/* 16 */     return this.accessToken;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getClientToken() {
/* 21 */     return this.clientToken;
/*    */   }
/*    */ 
/*    */   
/*    */   public GameProfile[] getAvailableProfiles() {
/* 26 */     return this.availableProfiles;
/*    */   }
/*    */ 
/*    */   
/*    */   public GameProfile getSelectedProfile() {
/* 31 */     return this.selectedProfile;
/*    */   }
/*    */ 
/*    */   
/*    */   public User getUser() {
/* 36 */     return this.user;
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\mojang\authlib\yggdrasil\response\AuthenticationResponse.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */