/*     */ package com.mojang.authlib.legacy;
/*     */ 
/*     */ import com.mojang.authlib.AuthenticationService;
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import com.mojang.authlib.HttpAuthenticationService;
/*     */ import com.mojang.authlib.HttpUserAuthentication;
/*     */ import com.mojang.authlib.UserType;
/*     */ import com.mojang.authlib.exceptions.AuthenticationException;
/*     */ import com.mojang.authlib.exceptions.InvalidCredentialsException;
/*     */ import com.mojang.util.UUIDTypeAdapter;
/*     */ import java.io.IOException;
/*     */ import java.net.URL;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ 
/*     */ public class LegacyUserAuthentication
/*     */   extends HttpUserAuthentication {
/*  19 */   private static final URL AUTHENTICATION_URL = HttpAuthenticationService.constantURL("https://login.minecraft.net");
/*     */   
/*     */   private static final int AUTHENTICATION_VERSION = 14;
/*     */   private static final int RESPONSE_PART_PROFILE_NAME = 2;
/*     */   private static final int RESPONSE_PART_SESSION_TOKEN = 3;
/*     */   private static final int RESPONSE_PART_PROFILE_ID = 4;
/*     */   private String sessionToken;
/*     */   
/*     */   protected LegacyUserAuthentication(LegacyAuthenticationService authenticationService) {
/*  28 */     super(authenticationService);
/*     */   }
/*     */ 
/*     */   
/*     */   public void logIn() throws AuthenticationException {
/*     */     String response;
/*  34 */     if (StringUtils.isBlank(getUsername())) {
/*  35 */       throw new InvalidCredentialsException("Invalid username");
/*     */     }
/*  37 */     if (StringUtils.isBlank(getPassword())) {
/*  38 */       throw new InvalidCredentialsException("Invalid password");
/*     */     }
/*  40 */     Map<String, Object> args = new HashMap<>();
/*  41 */     args.put("user", getUsername());
/*  42 */     args.put("password", getPassword());
/*  43 */     args.put("version", Integer.valueOf(14));
/*     */ 
/*     */     
/*     */     try {
/*  47 */       response = getAuthenticationService().performPostRequest(AUTHENTICATION_URL, HttpAuthenticationService.buildQuery(args), "application/x-www-form-urlencoded").trim();
/*     */     }
/*  49 */     catch (IOException e) {
/*     */       
/*  51 */       throw new AuthenticationException("Authentication server is not responding", e);
/*     */     } 
/*  53 */     String[] split = response.split(":");
/*  54 */     if (split.length == 5) {
/*     */       
/*  56 */       String profileId = split[4];
/*  57 */       String profileName = split[2];
/*  58 */       String sessionToken = split[3];
/*  59 */       if (StringUtils.isBlank(profileId) || StringUtils.isBlank(profileName) || StringUtils.isBlank(sessionToken)) {
/*  60 */         throw new AuthenticationException("Unknown response from authentication server: " + response);
/*     */       }
/*  62 */       setSelectedProfile(new GameProfile(UUIDTypeAdapter.fromString(profileId), profileName));
/*  63 */       this.sessionToken = sessionToken;
/*  64 */       setUserType(UserType.LEGACY);
/*     */     }
/*     */     else {
/*     */       
/*  68 */       throw new InvalidCredentialsException(response);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void logOut() {
/*  74 */     super.logOut();
/*  75 */     this.sessionToken = null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canPlayOnline() {
/*  80 */     return (isLoggedIn() && getSelectedProfile() != null && getAuthenticatedToken() != null);
/*     */   }
/*     */ 
/*     */   
/*     */   public GameProfile[] getAvailableProfiles() {
/*  85 */     if (getSelectedProfile() != null) {
/*  86 */       return new GameProfile[] { getSelectedProfile() };
/*     */     }
/*  88 */     return new GameProfile[0];
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void selectGameProfile(GameProfile profile) throws AuthenticationException {
/*  94 */     throw new UnsupportedOperationException("Game profiles cannot be changed in the legacy authentication service");
/*     */   }
/*     */ 
/*     */   
/*     */   public String getAuthenticatedToken() {
/*  99 */     return this.sessionToken;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getUserID() {
/* 104 */     return getUsername();
/*     */   }
/*     */ 
/*     */   
/*     */   public LegacyAuthenticationService getAuthenticationService() {
/* 109 */     return (LegacyAuthenticationService)super.getAuthenticationService();
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\mojang\authlib\legacy\LegacyUserAuthentication.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */