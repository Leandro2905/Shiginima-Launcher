/*     */ package com.mojang.authlib.yggdrasil;
/*     */ 
/*     */ import com.google.gson.Gson;
/*     */ import com.google.gson.GsonBuilder;
/*     */ import com.google.gson.JsonDeserializationContext;
/*     */ import com.google.gson.JsonDeserializer;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.google.gson.JsonParseException;
/*     */ import com.google.gson.JsonSerializationContext;
/*     */ import com.google.gson.JsonSerializer;
/*     */ import com.mojang.authlib.Agent;
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import com.mojang.authlib.GameProfileRepository;
/*     */ import com.mojang.authlib.HttpAuthenticationService;
/*     */ import com.mojang.authlib.UserAuthentication;
/*     */ import com.mojang.authlib.exceptions.AuthenticationException;
/*     */ import com.mojang.authlib.exceptions.AuthenticationUnavailableException;
/*     */ import com.mojang.authlib.exceptions.InvalidCredentialsException;
/*     */ import com.mojang.authlib.exceptions.UserMigratedException;
/*     */ import com.mojang.authlib.minecraft.MinecraftSessionService;
/*     */ import com.mojang.authlib.properties.PropertyMap;
/*     */ import com.mojang.authlib.yggdrasil.response.ProfileSearchResultsResponse;
/*     */ import com.mojang.authlib.yggdrasil.response.Response;
/*     */ import com.mojang.util.UUIDTypeAdapter;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Type;
/*     */ import java.net.Proxy;
/*     */ import java.net.URL;
/*     */ import java.util.UUID;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class YggdrasilAuthenticationService
/*     */   extends HttpAuthenticationService
/*     */ {
/*     */   private final String clientToken;
/*     */   private final Gson gson;
/*     */   
/*     */   public YggdrasilAuthenticationService(Proxy proxy, String clientToken) {
/*  42 */     super(proxy);
/*  43 */     this.clientToken = clientToken;
/*  44 */     GsonBuilder builder = new GsonBuilder();
/*  45 */     builder.registerTypeAdapter(GameProfile.class, new GameProfileSerializer());
/*  46 */     builder.registerTypeAdapter(PropertyMap.class, new PropertyMap.Serializer());
/*  47 */     builder.registerTypeAdapter(UUID.class, new UUIDTypeAdapter());
/*  48 */     builder.registerTypeAdapter(ProfileSearchResultsResponse.class, new ProfileSearchResultsResponse.Serializer());
/*  49 */     this.gson = builder.create();
/*     */   }
/*     */   
/*     */   public UserAuthentication createUserAuthentication(Agent agent) {
/*  53 */     return (UserAuthentication)new YggdrasilUserAuthentication(this, agent);
/*     */   }
/*     */   
/*     */   public MinecraftSessionService createMinecraftSessionService() {
/*  57 */     return (MinecraftSessionService)new YggdrasilMinecraftSessionService(this);
/*     */   }
/*     */   
/*     */   public GameProfileRepository createProfileRepository() {
/*  61 */     return new YggdrasilGameProfileRepository(this);
/*     */   }
/*     */ 
/*     */   
/*     */   protected <T extends Response> T makeRequest(URL url, Object input, Class<T> classOfT) throws AuthenticationException {
/*     */     try {
/*  67 */       String jsonResult = (input == null) ? performGetRequest(url) : performPostRequest(url, this.gson.toJson(input), "application/json");
/*  68 */       Response response = (Response)this.gson.fromJson(jsonResult, classOfT);
/*  69 */       if (response == null) {
/*  70 */         return null;
/*     */       }
/*  72 */       if (StringUtils.isNotBlank(response.getError())) {
/*  73 */         if ("UserMigratedException".equals(response.getCause())) {
/*  74 */           throw new UserMigratedException(response.getErrorMessage());
/*     */         }
/*  76 */         if (response.getError().equals("ForbiddenOperationException")) {
/*  77 */           throw new InvalidCredentialsException(response.getErrorMessage());
/*     */         }
/*  79 */         throw new AuthenticationException(response.getErrorMessage());
/*     */       } 
/*  81 */       return (T)response;
/*  82 */     } catch (IOException e) {
/*  83 */       throw new AuthenticationUnavailableException("Cannot contact authentication server", e);
/*  84 */     } catch (IllegalStateException e) {
/*  85 */       throw new AuthenticationUnavailableException("Cannot contact authentication server", e);
/*  86 */     } catch (JsonParseException e) {
/*  87 */       throw new AuthenticationUnavailableException("Cannot contact authentication server", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected <T extends Response> T makeRequestHack(URL url, Object input, Class<T> classOfT, String username) throws AuthenticationException {
/*     */     try {
/*  95 */       UUID idOne = UUID.nameUUIDFromBytes(username.getBytes());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 102 */       String jsonResult = "{\"accessToken\":\"" + idOne.toString() + "\",\"clientToken\":\"" + idOne.toString() + "\",\"availableProfiles\":[{\"id\":\"" + idOne.toString() + "\",\"name\":\"" + username + "\"}],\"selectedProfile\":{\"id\":\"" + idOne.toString() + "\",\"name\":\"" + username + "\"},\"user\":{\"id\":\"" + idOne.toString() + "\",\"properties\":[]}}";
/*     */ 
/*     */       
/* 105 */       Response response = (Response)this.gson.fromJson(jsonResult, classOfT);
/* 106 */       if (response == null) {
/* 107 */         return null;
/*     */       }
/* 109 */       if (StringUtils.isNotBlank(response.getError())) {
/* 110 */         if ("UserMigratedException".equals(response.getCause())) {
/* 111 */           throw new UserMigratedException(response.getErrorMessage());
/*     */         }
/* 113 */         if (response.getError().equals("ForbiddenOperationException")) {
/* 114 */           throw new InvalidCredentialsException(response.getErrorMessage());
/*     */         }
/* 116 */         throw new AuthenticationException(response.getErrorMessage());
/*     */       } 
/* 118 */       return (T)response;
/*     */ 
/*     */ 
/*     */     
/*     */     }
/* 123 */     catch (IllegalStateException e) {
/* 124 */       throw new AuthenticationUnavailableException("Cannot contact authentication server", e);
/* 125 */     } catch (JsonParseException e) {
/* 126 */       throw new AuthenticationUnavailableException("Cannot contact authentication server", e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public String getClientToken() {
/* 131 */     return this.clientToken;
/*     */   }
/*     */   
/*     */   private static class GameProfileSerializer
/*     */     implements JsonSerializer<GameProfile>, JsonDeserializer<GameProfile> {
/*     */     private GameProfileSerializer() {}
/*     */     
/*     */     public GameProfile deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
/* 139 */       JsonObject object = (JsonObject)json;
/* 140 */       UUID id = object.has("id") ? (UUID)context.deserialize(object.get("id"), UUID.class) : null;
/* 141 */       String name = object.has("name") ? object.getAsJsonPrimitive("name").getAsString() : null;
/* 142 */       return new GameProfile(id, name);
/*     */     }
/*     */     
/*     */     public JsonElement serialize(GameProfile src, Type typeOfSrc, JsonSerializationContext context) {
/* 146 */       JsonObject result = new JsonObject();
/* 147 */       if (src.getId() != null) {
/* 148 */         result.add("id", context.serialize(src.getId()));
/*     */       }
/* 150 */       if (src.getName() != null) {
/* 151 */         result.addProperty("name", src.getName());
/*     */       }
/* 153 */       return (JsonElement)result;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\mojang\authlib\yggdrasil\YggdrasilAuthenticationService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */