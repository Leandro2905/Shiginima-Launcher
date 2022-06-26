/*     */ package net.minecraft.launcher.profile;
/*     */ 
/*     */ import com.google.gson.JsonDeserializationContext;
/*     */ import com.google.gson.JsonDeserializer;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.google.gson.JsonParseException;
/*     */ import com.google.gson.JsonSerializationContext;
/*     */ import com.google.gson.JsonSerializer;
/*     */ import com.mojang.authlib.AuthenticationService;
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import com.mojang.authlib.UserAuthentication;
/*     */ import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import net.minecraft.launcher.Launcher;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AuthenticationDatabase
/*     */ {
/*     */   public static final String DEMO_UUID_PREFIX = "demo-";
/*     */   private final Map<String, UserAuthentication> authById;
/*     */   private final AuthenticationService authenticationService;
/*     */   
/*     */   public AuthenticationDatabase(AuthenticationService authenticationService) {
/*  34 */     this(new HashMap<>(), authenticationService);
/*     */   }
/*     */ 
/*     */   
/*     */   public AuthenticationDatabase(Map<String, UserAuthentication> authById, AuthenticationService authenticationService) {
/*  39 */     this.authById = authById;
/*  40 */     this.authenticationService = authenticationService;
/*     */   }
/*     */ 
/*     */   
/*     */   public UserAuthentication getByName(String name) {
/*  45 */     if (name == null) {
/*  46 */       return null;
/*     */     }
/*  48 */     for (Map.Entry<String, UserAuthentication> entry : this.authById.entrySet()) {
/*     */       
/*  50 */       GameProfile profile = ((UserAuthentication)entry.getValue()).getSelectedProfile();
/*  51 */       if (profile != null && profile.getName().equals(name)) {
/*  52 */         return entry.getValue();
/*     */       }
/*  54 */       if (profile == null && getUserFromDemoUUID(entry.getKey()).equals(name)) {
/*  55 */         return entry.getValue();
/*     */       }
/*     */     } 
/*  58 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public UserAuthentication getByUUID(String uuid) {
/*  63 */     return this.authById.get(uuid);
/*     */   }
/*     */ 
/*     */   
/*     */   public Collection<String> getKnownNames() {
/*  68 */     List<String> names = new ArrayList<>();
/*  69 */     for (Map.Entry<String, UserAuthentication> entry : this.authById.entrySet()) {
/*     */       
/*  71 */       GameProfile profile = ((UserAuthentication)entry.getValue()).getSelectedProfile();
/*  72 */       if (profile != null) {
/*  73 */         names.add(profile.getName()); continue;
/*     */       } 
/*  75 */       names.add(getUserFromDemoUUID(entry.getKey()));
/*     */     } 
/*     */     
/*  78 */     return names;
/*     */   }
/*     */ 
/*     */   
/*     */   public void register(String uuid, UserAuthentication authentication) {
/*  83 */     this.authById.put(uuid, authentication);
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<String> getknownUUIDs() {
/*  88 */     return this.authById.keySet();
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeUUID(String uuid) {
/*  93 */     this.authById.remove(uuid);
/*     */   }
/*     */ 
/*     */   
/*     */   public AuthenticationService getAuthenticationService() {
/*  98 */     return this.authenticationService;
/*     */   }
/*     */ 
/*     */   
/*     */   public static class Serializer
/*     */     implements JsonDeserializer<AuthenticationDatabase>, JsonSerializer<AuthenticationDatabase>
/*     */   {
/*     */     private final Launcher launcher;
/*     */     
/*     */     public Serializer(Launcher launcher) {
/* 108 */       this.launcher = launcher;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public AuthenticationDatabase deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
/* 114 */       Map<String, UserAuthentication> services = new HashMap<>();
/* 115 */       Map<String, Map<String, Object>> credentials = deserializeCredentials((JsonObject)json, context);
/* 116 */       YggdrasilAuthenticationService authService = new YggdrasilAuthenticationService(this.launcher.getLauncher().getProxy(), this.launcher.getClientToken().toString());
/* 117 */       for (Map.Entry<String, Map<String, Object>> entry : credentials.entrySet()) {
/*     */         
/* 119 */         UserAuthentication auth = authService.createUserAuthentication(this.launcher.getLauncher().getAgent());
/* 120 */         auth.loadFromStorage(entry.getValue());
/* 121 */         services.put(entry.getKey(), auth);
/*     */       } 
/* 123 */       return new AuthenticationDatabase(services, (AuthenticationService)authService);
/*     */     }
/*     */ 
/*     */     
/*     */     protected Map<String, Map<String, Object>> deserializeCredentials(JsonObject json, JsonDeserializationContext context) {
/* 128 */       Map<String, Map<String, Object>> result = new LinkedHashMap<>();
/* 129 */       for (Map.Entry<String, JsonElement> authEntry : (Iterable<Map.Entry<String, JsonElement>>)json.entrySet()) {
/*     */         
/* 131 */         Map<String, Object> credentials = new LinkedHashMap<>();
/* 132 */         for (Map.Entry<String, JsonElement> credentialsEntry : (Iterable<Map.Entry<String, JsonElement>>)((JsonObject)authEntry.getValue()).entrySet()) {
/* 133 */           credentials.put(credentialsEntry.getKey(), deserializeCredential(credentialsEntry.getValue()));
/*     */         }
/* 135 */         result.put(authEntry.getKey(), credentials);
/*     */       } 
/* 137 */       return result;
/*     */     }
/*     */ 
/*     */     
/*     */     private Object deserializeCredential(JsonElement element) {
/* 142 */       if (element instanceof JsonObject) {
/*     */         
/* 144 */         Map<String, Object> result = new LinkedHashMap<>();
/* 145 */         for (Map.Entry<String, JsonElement> entry : (Iterable<Map.Entry<String, JsonElement>>)((JsonObject)element).entrySet()) {
/* 146 */           result.put(entry.getKey(), deserializeCredential(entry.getValue()));
/*     */         }
/* 148 */         return result;
/*     */       } 
/* 150 */       if (element instanceof com.google.gson.JsonArray) {
/*     */         
/* 152 */         List<Object> result = new ArrayList();
/* 153 */         for (JsonElement entry : element) {
/* 154 */           result.add(deserializeCredential(entry));
/*     */         }
/* 156 */         return result;
/*     */       } 
/* 158 */       return element.getAsString();
/*     */     }
/*     */ 
/*     */     
/*     */     public JsonElement serialize(AuthenticationDatabase src, Type typeOfSrc, JsonSerializationContext context) {
/* 163 */       Map<String, UserAuthentication> services = src.authById;
/* 164 */       Map<String, Map<String, Object>> credentials = new HashMap<>();
/* 165 */       for (Map.Entry<String, UserAuthentication> entry : services.entrySet()) {
/* 166 */         credentials.put(entry.getKey(), ((UserAuthentication)entry.getValue()).saveForStorage());
/*     */       }
/* 168 */       return context.serialize(credentials);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static String getUserFromDemoUUID(String uuid) {
/* 174 */     if (uuid.startsWith("demo-") && uuid.length() > "demo-".length()) {
/* 175 */       return "Demo User " + uuid.substring("demo-".length());
/*     */     }
/* 177 */     return "Demo User";
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\net\minecraft\launcher\profile\AuthenticationDatabase.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */