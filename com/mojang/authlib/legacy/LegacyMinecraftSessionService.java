/*    */ package com.mojang.authlib.legacy;
/*    */ 
/*    */ import com.mojang.authlib.AuthenticationService;
/*    */ import com.mojang.authlib.GameProfile;
/*    */ import com.mojang.authlib.HttpAuthenticationService;
/*    */ import com.mojang.authlib.exceptions.AuthenticationException;
/*    */ import com.mojang.authlib.exceptions.AuthenticationUnavailableException;
/*    */ import com.mojang.authlib.minecraft.HttpMinecraftSessionService;
/*    */ import com.mojang.authlib.minecraft.MinecraftProfileTexture;
/*    */ import java.io.IOException;
/*    */ import java.net.URL;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ 
/*    */ public class LegacyMinecraftSessionService
/*    */   extends HttpMinecraftSessionService
/*    */ {
/*    */   private static final String BASE_URL = "http://session.minecraft.net/game/";
/* 19 */   private static final URL JOIN_URL = HttpAuthenticationService.constantURL("http://session.minecraft.net/game/joinserver.jsp");
/* 20 */   private static final URL CHECK_URL = HttpAuthenticationService.constantURL("http://session.minecraft.net/game/checkserver.jsp");
/*    */ 
/*    */   
/*    */   protected LegacyMinecraftSessionService(LegacyAuthenticationService authenticationService) {
/* 24 */     super(authenticationService);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void joinServer(GameProfile profile, String authenticationToken, String serverId) throws AuthenticationException {
/* 30 */     Map<String, Object> arguments = new HashMap<>();
/*    */     
/* 32 */     arguments.put("user", profile.getName());
/* 33 */     arguments.put("sessionId", authenticationToken);
/* 34 */     arguments.put("serverId", serverId);
/*    */     
/* 36 */     URL url = HttpAuthenticationService.concatenateURL(JOIN_URL, HttpAuthenticationService.buildQuery(arguments));
/*    */     
/*    */     try {
/* 39 */       String response = getAuthenticationService().performGetRequest(url);
/* 40 */       if (!response.equals("OK")) {
/* 41 */         throw new AuthenticationException(response);
/*    */       }
/*    */     }
/* 44 */     catch (IOException e) {
/*    */       
/* 46 */       throw new AuthenticationUnavailableException(e);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public GameProfile hasJoinedServer(GameProfile user, String serverId) throws AuthenticationUnavailableException {
/* 53 */     Map<String, Object> arguments = new HashMap<>();
/*    */     
/* 55 */     arguments.put("user", user.getName());
/* 56 */     arguments.put("serverId", serverId);
/*    */     
/* 58 */     URL url = HttpAuthenticationService.concatenateURL(CHECK_URL, HttpAuthenticationService.buildQuery(arguments));
/*    */     
/*    */     try {
/* 61 */       String response = getAuthenticationService().performGetRequest(url);
/*    */       
/* 63 */       return response.equals("YES") ? user : null;
/*    */     }
/* 65 */     catch (IOException e) {
/*    */       
/* 67 */       throw new AuthenticationUnavailableException(e);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> getTextures(GameProfile profile, boolean requireSecure) {
/* 73 */     return new HashMap<>();
/*    */   }
/*    */ 
/*    */   
/*    */   public GameProfile fillProfileProperties(GameProfile profile, boolean requireSecure) {
/* 78 */     return profile;
/*    */   }
/*    */ 
/*    */   
/*    */   public LegacyAuthenticationService getAuthenticationService() {
/* 83 */     return (LegacyAuthenticationService)super.getAuthenticationService();
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\mojang\authlib\legacy\LegacyMinecraftSessionService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */