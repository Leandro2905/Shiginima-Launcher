/*     */ package com.mojang.authlib.yggdrasil;
/*     */ 
/*     */ import com.google.common.cache.CacheBuilder;
/*     */ import com.google.common.cache.CacheLoader;
/*     */ import com.google.common.cache.LoadingCache;
/*     */ import com.google.common.collect.Iterables;
/*     */ import com.google.common.collect.Multimap;
/*     */ import com.google.gson.Gson;
/*     */ import com.google.gson.GsonBuilder;
/*     */ import com.google.gson.JsonParseException;
/*     */ import com.mojang.authlib.AuthenticationService;
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import com.mojang.authlib.HttpAuthenticationService;
/*     */ import com.mojang.authlib.exceptions.AuthenticationException;
/*     */ import com.mojang.authlib.exceptions.AuthenticationUnavailableException;
/*     */ import com.mojang.authlib.minecraft.HttpMinecraftSessionService;
/*     */ import com.mojang.authlib.minecraft.InsecureTextureException;
/*     */ import com.mojang.authlib.minecraft.MinecraftProfileTexture;
/*     */ import com.mojang.authlib.properties.Property;
/*     */ import com.mojang.authlib.yggdrasil.request.JoinMinecraftServerRequest;
/*     */ import com.mojang.authlib.yggdrasil.response.HasJoinedMinecraftServerResponse;
/*     */ import com.mojang.authlib.yggdrasil.response.MinecraftProfilePropertiesResponse;
/*     */ import com.mojang.authlib.yggdrasil.response.MinecraftTexturesPayload;
/*     */ import com.mojang.authlib.yggdrasil.response.Response;
/*     */ import com.mojang.util.UUIDTypeAdapter;
/*     */ import java.net.URL;
/*     */ import java.security.KeyFactory;
/*     */ import java.security.PublicKey;
/*     */ import java.security.spec.X509EncodedKeySpec;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.UUID;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.apache.commons.codec.Charsets;
/*     */ import org.apache.commons.codec.binary.Base64;
/*     */ import org.apache.commons.io.IOUtils;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class YggdrasilMinecraftSessionService
/*     */   extends HttpMinecraftSessionService
/*     */ {
/*  43 */   private static final Logger LOGGER = LogManager.getLogger();
/*     */   private static final String BASE_URL = "https://sessionserver.mojang.com/session/minecraft/";
/*  45 */   private static final URL JOIN_URL = HttpAuthenticationService.constantURL("https://sessionserver.mojang.com/session/minecraft/join");
/*  46 */   private static final URL CHECK_URL = HttpAuthenticationService.constantURL("https://sessionserver.mojang.com/session/minecraft/hasJoined");
/*     */   private final PublicKey publicKey;
/*  48 */   private final Gson gson = (new GsonBuilder()).registerTypeAdapter(UUID.class, new UUIDTypeAdapter()).create();
/*  49 */   private final LoadingCache<GameProfile, GameProfile> insecureProfiles = CacheBuilder.newBuilder().expireAfterWrite(6L, TimeUnit.HOURS).build(new CacheLoader()
/*     */       {
/*     */         
/*     */         public GameProfile load(Object key) throws Exception
/*     */         {
/*  54 */           GameProfile k = (GameProfile)key;
/*  55 */           return YggdrasilMinecraftSessionService.this.fillGameProfile(k, false);
/*     */         }
/*     */       });
/*     */ 
/*     */   
/*     */   protected YggdrasilMinecraftSessionService(YggdrasilAuthenticationService authenticationService) {
/*  61 */     super(authenticationService);
/*     */     
/*     */     try {
/*  64 */       X509EncodedKeySpec spec = new X509EncodedKeySpec(IOUtils.toByteArray(YggdrasilMinecraftSessionService.class.getResourceAsStream("/yggdrasil_session_pubkey.der")));
/*  65 */       KeyFactory keyFactory = KeyFactory.getInstance("RSA");
/*  66 */       this.publicKey = keyFactory.generatePublic(spec);
/*     */     }
/*  68 */     catch (Exception e) {
/*     */       
/*  70 */       throw new Error("Missing/invalid yggdrasil public key!");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void joinServer(GameProfile profile, String authenticationToken, String serverId) throws AuthenticationException {
/*  77 */     JoinMinecraftServerRequest request = new JoinMinecraftServerRequest();
/*  78 */     request.accessToken = authenticationToken;
/*  79 */     request.selectedProfile = profile.getId();
/*  80 */     request.serverId = serverId;
/*     */     
/*  82 */     getAuthenticationService().makeRequest(JOIN_URL, request, Response.class);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public GameProfile hasJoinedServer(GameProfile user, String serverId) throws AuthenticationUnavailableException {
/*  88 */     Map<String, Object> arguments = new HashMap<>();
/*     */     
/*  90 */     arguments.put("username", user.getName());
/*  91 */     arguments.put("serverId", serverId);
/*     */     
/*  93 */     URL url = HttpAuthenticationService.concatenateURL(CHECK_URL, HttpAuthenticationService.buildQuery(arguments));
/*     */     
/*     */     try {
/*  96 */       HasJoinedMinecraftServerResponse response = getAuthenticationService().<HasJoinedMinecraftServerResponse>makeRequest(url, null, HasJoinedMinecraftServerResponse.class);
/*  97 */       if (response != null && response.getId() != null) {
/*     */         
/*  99 */         GameProfile result = new GameProfile(response.getId(), user.getName());
/* 100 */         if (response.getProperties() != null) {
/* 101 */           result.getProperties().putAll((Multimap)response.getProperties());
/*     */         }
/* 103 */         return result;
/*     */       } 
/* 105 */       return null;
/*     */     }
/* 107 */     catch (AuthenticationUnavailableException e) {
/*     */       
/* 109 */       throw e;
/*     */     }
/* 111 */     catch (AuthenticationException authenticationException) {
/* 112 */       return null;
/*     */     } 
/*     */   }
/*     */   public Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> getTextures(GameProfile profile, boolean requireSecure) {
/*     */     MinecraftTexturesPayload result;
/* 117 */     Property textureProperty = (Property)Iterables.getFirst(profile.getProperties().get("textures"), null);
/* 118 */     if (textureProperty == null) {
/* 119 */       return new HashMap<>();
/*     */     }
/* 121 */     if (requireSecure) {
/*     */       
/* 123 */       if (!textureProperty.hasSignature()) {
/*     */         
/* 125 */         LOGGER.error("Signature is missing from textures payload");
/* 126 */         throw new InsecureTextureException("Signature is missing from textures payload");
/*     */       } 
/* 128 */       if (!textureProperty.isSignatureValid(this.publicKey)) {
/*     */         
/* 130 */         LOGGER.error("Textures payload has been tampered with (signature invalid)");
/* 131 */         throw new InsecureTextureException("Textures payload has been tampered with (signature invalid)");
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/*     */     try {
/* 137 */       String json = new String(Base64.decodeBase64(textureProperty.getValue()), Charsets.UTF_8);
/* 138 */       result = (MinecraftTexturesPayload)this.gson.fromJson(json, MinecraftTexturesPayload.class);
/*     */     }
/* 140 */     catch (JsonParseException e) {
/*     */       
/* 142 */       LOGGER.error("Could not decode textures payload", (Throwable)e);
/* 143 */       return new HashMap<>();
/*     */     } 
/* 145 */     return (result.getTextures() == null) ? new HashMap<>() : result.getTextures();
/*     */   }
/*     */ 
/*     */   
/*     */   public GameProfile fillProfileProperties(GameProfile profile, boolean requireSecure) {
/* 150 */     if (profile.getId() == null) {
/* 151 */       return profile;
/*     */     }
/* 153 */     if (!requireSecure) {
/* 154 */       return (GameProfile)this.insecureProfiles.getUnchecked(profile);
/*     */     }
/* 156 */     return fillGameProfile(profile, true);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected GameProfile fillGameProfile(GameProfile profile, boolean requireSecure) {
/*     */     try {
/* 163 */       URL url = HttpAuthenticationService.constantURL("https://sessionserver.mojang.com/session/minecraft/profile/" + UUIDTypeAdapter.fromUUID(profile.getId()));
/* 164 */       url = HttpAuthenticationService.concatenateURL(url, "unsigned=" + (!requireSecure ? 1 : 0));
/* 165 */       MinecraftProfilePropertiesResponse response = getAuthenticationService().<MinecraftProfilePropertiesResponse>makeRequest(url, null, MinecraftProfilePropertiesResponse.class);
/* 166 */       if (response == null) {
/*     */         
/* 168 */         LOGGER.debug("Couldn't fetch profile properties for " + profile + " as the profile does not exist");
/* 169 */         return profile;
/*     */       } 
/* 171 */       GameProfile result = new GameProfile(response.getId(), response.getName());
/* 172 */       result.getProperties().putAll((Multimap)response.getProperties());
/* 173 */       profile.getProperties().putAll((Multimap)response.getProperties());
/* 174 */       LOGGER.debug("Successfully fetched profile properties for " + profile);
/* 175 */       return result;
/*     */     }
/* 177 */     catch (AuthenticationException e) {
/*     */       
/* 179 */       LOGGER.warn("Couldn't look up profile properties for " + profile, (Throwable)e);
/*     */       
/* 181 */       return profile;
/*     */     } 
/*     */   }
/*     */   
/*     */   public YggdrasilAuthenticationService getAuthenticationService() {
/* 186 */     return (YggdrasilAuthenticationService)super.getAuthenticationService();
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\mojang\authlib\yggdrasil\YggdrasilMinecraftSessionService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */