/*     */ package com.mojang.authlib.yggdrasil;
/*     */ 
/*     */ import com.google.common.collect.Multimap;
/*     */ import com.mojang.authlib.Agent;
/*     */ import com.mojang.authlib.AuthenticationService;
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import com.mojang.authlib.HttpAuthenticationService;
/*     */ import com.mojang.authlib.HttpUserAuthentication;
/*     */ import com.mojang.authlib.UserType;
/*     */ import com.mojang.authlib.exceptions.AuthenticationException;
/*     */ import com.mojang.authlib.exceptions.InvalidCredentialsException;
/*     */ import com.mojang.authlib.yggdrasil.request.AuthenticationRequest;
/*     */ import com.mojang.authlib.yggdrasil.request.RefreshRequest;
/*     */ import com.mojang.authlib.yggdrasil.request.ValidateRequest;
/*     */ import com.mojang.authlib.yggdrasil.response.AuthenticationResponse;
/*     */ import com.mojang.authlib.yggdrasil.response.RefreshResponse;
/*     */ import com.mojang.authlib.yggdrasil.response.Response;
/*     */ import com.mojang.authlib.yggdrasil.response.User;
/*     */ import java.net.URL;
/*     */ import java.util.Arrays;
/*     */ import java.util.Map;
/*     */ import org.apache.commons.lang3.ArrayUtils;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class YggdrasilUserAuthentication
/*     */   extends HttpUserAuthentication {
/*  29 */   private static final Logger LOGGER = LogManager.getLogger();
/*     */   private static final String BASE_URL = "https://authserver.mojang.com/";
/*  31 */   private static final URL ROUTE_AUTHENTICATE = HttpAuthenticationService.constantURL("https://authserver.mojang.com/authenticate");
/*  32 */   private static final URL ROUTE_REFRESH = HttpAuthenticationService.constantURL("https://authserver.mojang.com/refresh");
/*  33 */   private static final URL ROUTE_VALIDATE = HttpAuthenticationService.constantURL("https://authserver.mojang.com/validate");
/*  34 */   private static final URL ROUTE_INVALIDATE = HttpAuthenticationService.constantURL("https://authserver.mojang.com/invalidate");
/*  35 */   private static final URL ROUTE_SIGNOUT = HttpAuthenticationService.constantURL("https://authserver.mojang.com/signout");
/*     */   
/*     */   private static final String STORAGE_KEY_ACCESS_TOKEN = "accessToken";
/*     */   private final Agent agent;
/*     */   private GameProfile[] profiles;
/*     */   private String accessToken;
/*     */   private boolean isOnline;
/*     */   
/*     */   public YggdrasilUserAuthentication(YggdrasilAuthenticationService authenticationService, Agent agent) {
/*  44 */     super(authenticationService);
/*  45 */     this.agent = agent;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canLogIn() {
/*  50 */     return (!canPlayOnline() && StringUtils.isNotBlank(getUsername()) && (StringUtils.isNotBlank(getPassword()) || StringUtils.isNotBlank(getAuthenticatedToken())));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void logIn() throws AuthenticationException {
/*  62 */     logInWithPassword();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void logInWithPassword() throws AuthenticationException {
/*  71 */     if (StringUtils.isBlank(getUsername())) {
/*  72 */       throw new InvalidCredentialsException("Invalid username");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  77 */     LOGGER.info("Logging in with username & password");
/*     */     
/*  79 */     AuthenticationRequest request = new AuthenticationRequest(this, getUsername(), getPassword());
/*  80 */     AuthenticationResponse response = getAuthenticationService().<AuthenticationResponse>makeRequestHack(ROUTE_AUTHENTICATE, request, AuthenticationResponse.class, getUsername());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  87 */     if (response.getSelectedProfile() != null) {
/*  88 */       setUserType(response.getSelectedProfile().isLegacy() ? UserType.LEGACY : UserType.MOJANG);
/*  89 */     } else if (ArrayUtils.isNotEmpty((Object[])response.getAvailableProfiles())) {
/*  90 */       setUserType(response.getAvailableProfiles()[0].isLegacy() ? UserType.LEGACY : UserType.MOJANG);
/*     */     } 
/*     */     
/*  93 */     User user = response.getUser();
/*     */ 
/*     */     
/*  96 */     setUserid(getUsername());
/*     */     
/*  98 */     this.isOnline = true;
/*  99 */     this.accessToken = response.getAccessToken();
/* 100 */     this.profiles = response.getAvailableProfiles();
/* 101 */     setSelectedProfile(response.getSelectedProfile());
/* 102 */     getModifiableUserProperties().clear();
/*     */     
/* 104 */     updateUserProperties(user);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void updateUserProperties(User user) {
/* 109 */     if (user == null) {
/*     */       return;
/*     */     }
/* 112 */     if (user.getProperties() != null) {
/* 113 */       getModifiableUserProperties().putAll((Multimap)user.getProperties());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void logInWithToken() throws AuthenticationException {
/* 120 */     if (StringUtils.isBlank(getUserID())) {
/* 121 */       if (StringUtils.isBlank(getUsername())) {
/* 122 */         setUserid(getUsername());
/*     */       } else {
/* 124 */         throw new InvalidCredentialsException("Invalid uuid & username");
/*     */       } 
/*     */     }
/* 127 */     if (StringUtils.isBlank(getAuthenticatedToken())) {
/* 128 */       throw new InvalidCredentialsException("Invalid access token");
/*     */     }
/* 130 */     LOGGER.info("Logging in with access token");
/* 131 */     if (checkTokenValidity()) {
/*     */       
/* 133 */       LOGGER.debug("Skipping refresh call as we're safely logged in.");
/* 134 */       this.isOnline = true;
/*     */       return;
/*     */     } 
/* 137 */     RefreshRequest request = new RefreshRequest(this);
/* 138 */     RefreshResponse response = getAuthenticationService().<RefreshResponse>makeRequest(ROUTE_REFRESH, request, RefreshResponse.class);
/* 139 */     if (!response.getClientToken().equals(getAuthenticationService().getClientToken())) {
/* 140 */       throw new AuthenticationException("Server requested we change our client token. Don't know how to handle this!");
/*     */     }
/* 142 */     if (response.getSelectedProfile() != null) {
/* 143 */       setUserType(response.getSelectedProfile().isLegacy() ? UserType.LEGACY : UserType.MOJANG);
/* 144 */     } else if (ArrayUtils.isNotEmpty((Object[])response.getAvailableProfiles())) {
/* 145 */       setUserType(response.getAvailableProfiles()[0].isLegacy() ? UserType.LEGACY : UserType.MOJANG);
/*     */     } 
/* 147 */     if (response.getUser() != null && response.getUser().getId() != null) {
/* 148 */       setUserid(response.getUser().getId());
/*     */     } else {
/* 150 */       setUserid(getUsername());
/*     */     } 
/* 152 */     this.isOnline = true;
/* 153 */     this.accessToken = response.getAccessToken();
/* 154 */     this.profiles = response.getAvailableProfiles();
/* 155 */     setSelectedProfile(response.getSelectedProfile());
/* 156 */     getModifiableUserProperties().clear();
/*     */     
/* 158 */     updateUserProperties(response.getUser());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean checkTokenValidity() throws AuthenticationException {
/* 164 */     ValidateRequest request = new ValidateRequest(this);
/*     */     
/*     */     try {
/* 167 */       getAuthenticationService().makeRequest(ROUTE_VALIDATE, request, Response.class);
/* 168 */       return true;
/*     */     }
/* 170 */     catch (AuthenticationException authenticationException) {
/* 171 */       return false;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void logOut() {
/* 176 */     super.logOut();
/*     */     
/* 178 */     this.accessToken = null;
/* 179 */     this.profiles = null;
/* 180 */     this.isOnline = false;
/*     */   }
/*     */ 
/*     */   
/*     */   public GameProfile[] getAvailableProfiles() {
/* 185 */     return this.profiles;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isLoggedIn() {
/* 190 */     return StringUtils.isNotBlank(this.accessToken);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canPlayOnline() {
/* 195 */     return (isLoggedIn() && getSelectedProfile() != null && this.isOnline);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void selectGameProfile(GameProfile profile) throws AuthenticationException {
/* 201 */     if (!isLoggedIn()) {
/* 202 */       throw new AuthenticationException("Cannot change game profile whilst not logged in");
/*     */     }
/* 204 */     if (getSelectedProfile() != null) {
/* 205 */       throw new AuthenticationException("Cannot change game profile. You must log out and back in.");
/*     */     }
/* 207 */     if (profile == null || !ArrayUtils.contains((Object[])this.profiles, profile)) {
/* 208 */       throw new IllegalArgumentException("Invalid profile '" + profile + "'");
/*     */     }
/* 210 */     RefreshRequest request = new RefreshRequest(this, profile);
/* 211 */     RefreshResponse response = getAuthenticationService().<RefreshResponse>makeRequest(ROUTE_REFRESH, request, RefreshResponse.class);
/* 212 */     if (!response.getClientToken().equals(getAuthenticationService().getClientToken())) {
/* 213 */       throw new AuthenticationException("Server requested we change our client token. Don't know how to handle this!");
/*     */     }
/* 215 */     this.isOnline = true;
/* 216 */     this.accessToken = response.getAccessToken();
/* 217 */     setSelectedProfile(response.getSelectedProfile());
/*     */   }
/*     */ 
/*     */   
/*     */   public void loadFromStorage(Map<String, Object> credentials) {
/* 222 */     super.loadFromStorage(credentials);
/*     */     
/* 224 */     this.accessToken = String.valueOf(credentials.get("accessToken"));
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<String, Object> saveForStorage() {
/* 229 */     Map<String, Object> result = super.saveForStorage();
/* 230 */     if (StringUtils.isNotBlank(getAuthenticatedToken())) {
/* 231 */       result.put("accessToken", getAuthenticatedToken());
/*     */     }
/* 233 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public String getSessionToken() {
/* 239 */     if (isLoggedIn() && getSelectedProfile() != null && canPlayOnline()) {
/* 240 */       return String.format("token:%s:%s", new Object[] { getAuthenticatedToken(), getSelectedProfile().getId() });
/*     */     }
/* 242 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getAuthenticatedToken() {
/* 247 */     return this.accessToken;
/*     */   }
/*     */ 
/*     */   
/*     */   public Agent getAgent() {
/* 252 */     return this.agent;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 257 */     return "YggdrasilAuthenticationService{agent=" + this.agent + ", profiles=" + Arrays.toString((Object[])this.profiles) + ", selectedProfile=" + getSelectedProfile() + ", username='" + getUsername() + '\'' + ", isLoggedIn=" + isLoggedIn() + ", userType=" + getUserType() + ", canPlayOnline=" + canPlayOnline() + ", accessToken='" + this.accessToken + '\'' + ", clientToken='" + getAuthenticationService().getClientToken() + '\'' + '}';
/*     */   }
/*     */ 
/*     */   
/*     */   public YggdrasilAuthenticationService getAuthenticationService() {
/* 262 */     return (YggdrasilAuthenticationService)super.getAuthenticationService();
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\mojang\authlib\yggdrasil\YggdrasilUserAuthentication.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */