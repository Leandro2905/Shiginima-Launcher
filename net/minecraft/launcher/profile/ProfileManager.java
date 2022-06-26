/*     */ package net.minecraft.launcher.profile;
/*     */ 
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.gson.Gson;
/*     */ import com.google.gson.GsonBuilder;
/*     */ import com.google.gson.JsonDeserializationContext;
/*     */ import com.google.gson.JsonDeserializer;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.google.gson.JsonParseException;
/*     */ import com.google.gson.JsonParser;
/*     */ import com.google.gson.JsonSerializationContext;
/*     */ import com.google.gson.JsonSerializer;
/*     */ import com.google.gson.TypeAdapterFactory;
/*     */ import com.google.gson.reflect.TypeToken;
/*     */ import com.mojang.authlib.AuthenticationService;
/*     */ import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
/*     */ import com.mojang.launcher.updater.DateTypeAdapter;
/*     */ import com.mojang.launcher.updater.FileTypeAdapter;
/*     */ import com.mojang.launcher.updater.LowerCaseEnumTypeAdapterFactory;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.launcher.Launcher;
/*     */ import net.minecraft.launcher.LauncherConstants;
/*     */ import org.apache.commons.io.FileUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ProfileManager
/*     */ {
/*     */   public static final String DEFAULT_PROFILE_NAME = "(Default)";
/*     */   private final Launcher launcher;
/*  43 */   private final JsonParser parser = new JsonParser();
/*     */   private final Gson gson;
/*  45 */   private final Map<String, Profile> profiles = new HashMap<>();
/*     */   private final File profileFile;
/*  47 */   private final List<RefreshedProfilesListener> refreshedProfilesListeners = Collections.synchronizedList(new ArrayList<>());
/*  48 */   private final List<UserChangedListener> userChangedListeners = Collections.synchronizedList(new ArrayList<>());
/*     */   
/*     */   private String selectedProfile;
/*     */   private String selectedUser;
/*     */   private AuthenticationDatabase authDatabase;
/*     */   
/*     */   public ProfileManager(Launcher launcher) {
/*  55 */     this.launcher = launcher;
/*  56 */     this.profileFile = new File(launcher.getLauncher().getWorkingDirectory(), "launcher_profiles.json");
/*     */     
/*  58 */     GsonBuilder builder = new GsonBuilder();
/*  59 */     builder.registerTypeAdapterFactory((TypeAdapterFactory)new LowerCaseEnumTypeAdapterFactory());
/*  60 */     builder.registerTypeAdapter(Date.class, new DateTypeAdapter());
/*  61 */     builder.registerTypeAdapter(File.class, new FileTypeAdapter());
/*  62 */     builder.registerTypeAdapter(AuthenticationDatabase.class, new AuthenticationDatabase.Serializer(launcher));
/*  63 */     builder.registerTypeAdapter(RawProfileList.class, new RawProfileList.Serializer(launcher));
/*  64 */     builder.setPrettyPrinting();
/*  65 */     this.gson = builder.create();
/*  66 */     this.authDatabase = new AuthenticationDatabase((AuthenticationService)new YggdrasilAuthenticationService(launcher.getLauncher().getProxy(), launcher.getClientToken().toString()));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void saveProfiles() throws IOException {
/*  72 */     RawProfileList rawProfileList = new RawProfileList(this.profiles, getSelectedProfile().getName(), this.selectedUser, this.launcher.getClientToken(), this.authDatabase);
/*     */     
/*  74 */     FileUtils.writeStringToFile(this.profileFile, this.gson.toJson(rawProfileList));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean loadProfiles() throws IOException {
/*  80 */     this.profiles.clear();
/*  81 */     this.selectedProfile = null;
/*  82 */     this.selectedUser = null;
/*  83 */     if (this.profileFile.isFile()) {
/*     */       
/*  85 */       JsonObject object = this.parser.parse(FileUtils.readFileToString(this.profileFile)).getAsJsonObject();
/*  86 */       if (object.has("launcherVersion")) {
/*     */         
/*  88 */         JsonObject version = object.getAsJsonObject("launcherVersion");
/*  89 */         if (version.has("profilesFormat") && version.getAsJsonPrimitive("profilesFormat").getAsInt() != 1) {
/*     */           
/*  91 */           if (this.launcher.getUserInterface().shouldDowngradeProfiles()) {
/*     */             
/*  93 */             File target = new File(this.profileFile.getParentFile(), "launcher_profiles.old.json");
/*  94 */             if (target.exists()) {
/*  95 */               target.delete();
/*     */             }
/*  97 */             this.profileFile.renameTo(target);
/*  98 */             fireRefreshEvent();
/*  99 */             fireUserChangedEvent();
/* 100 */             return false;
/*     */           } 
/* 102 */           this.launcher.getLauncher().shutdownLauncher();
/* 103 */           System.exit(0);
/* 104 */           return false;
/*     */         } 
/*     */       } 
/* 107 */       if (object.has("clientToken")) {
/* 108 */         this.launcher.setClientToken((UUID)this.gson.fromJson(object.get("clientToken"), UUID.class));
/*     */       }
/* 110 */       RawProfileList rawProfileList = (RawProfileList)this.gson.fromJson((JsonElement)object, RawProfileList.class);
/*     */       
/* 112 */       this.profiles.putAll(rawProfileList.profiles);
/* 113 */       this.selectedProfile = rawProfileList.selectedProfile;
/* 114 */       this.selectedUser = rawProfileList.selectedUser;
/* 115 */       this.authDatabase = rawProfileList.authenticationDatabase;
/*     */       
/* 117 */       fireRefreshEvent();
/* 118 */       fireUserChangedEvent();
/* 119 */       return true;
/*     */     } 
/* 121 */     fireRefreshEvent();
/* 122 */     fireUserChangedEvent();
/* 123 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void fireRefreshEvent() {
/* 128 */     for (RefreshedProfilesListener listener : Lists.newArrayList(this.refreshedProfilesListeners)) {
/* 129 */       listener.onProfilesRefreshed(this);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void fireUserChangedEvent() {
/* 135 */     for (UserChangedListener listener : Lists.newArrayList(this.userChangedListeners)) {
/* 136 */       listener.onUserChanged(this);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public Profile getSelectedProfile() {
/* 142 */     if (this.selectedProfile == null || !this.profiles.containsKey(this.selectedProfile)) {
/* 143 */       if (this.profiles.get("(Default)") != null) {
/*     */         
/* 145 */         this.selectedProfile = "(Default)";
/*     */       }
/* 147 */       else if (this.profiles.size() > 0) {
/*     */         
/* 149 */         this.selectedProfile = ((Profile)this.profiles.values().iterator().next()).getName();
/*     */       }
/*     */       else {
/*     */         
/* 153 */         this.selectedProfile = "(Default)";
/* 154 */         this.profiles.put("(Default)", new Profile(this.selectedProfile));
/*     */       } 
/*     */     }
/* 157 */     return this.profiles.get(this.selectedProfile);
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<String, Profile> getProfiles() {
/* 162 */     return this.profiles;
/*     */   }
/*     */ 
/*     */   
/*     */   public void addRefreshedProfilesListener(RefreshedProfilesListener listener) {
/* 167 */     this.refreshedProfilesListeners.add(listener);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addUserChangedListener(UserChangedListener listener) {
/* 172 */     this.userChangedListeners.add(listener);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setSelectedProfile(String selectedProfile) {
/* 177 */     boolean update = !this.selectedProfile.equals(selectedProfile);
/* 178 */     this.selectedProfile = selectedProfile;
/* 179 */     if (update) {
/* 180 */       fireRefreshEvent();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public String getSelectedUser() {
/* 186 */     return this.selectedUser;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setSelectedUser(String selectedUser) {
/* 191 */     boolean update = !Objects.equal(this.selectedUser, selectedUser);
/* 192 */     if (update) {
/*     */       
/* 194 */       this.selectedUser = selectedUser;
/* 195 */       fireUserChangedEvent();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public AuthenticationDatabase getAuthDatabase() {
/* 201 */     return this.authDatabase;
/*     */   }
/*     */   
/*     */   private static class RawProfileList
/*     */   {
/* 206 */     public Map<String, Profile> profiles = new HashMap<>();
/*     */     public String selectedProfile;
/*     */     public String selectedUser;
/* 209 */     public UUID clientToken = UUID.randomUUID();
/*     */     
/*     */     public AuthenticationDatabase authenticationDatabase;
/*     */     
/*     */     private RawProfileList(Map<String, Profile> profiles, String selectedProfile, String selectedUser, UUID clientToken, AuthenticationDatabase authenticationDatabase) {
/* 214 */       this.profiles = profiles;
/* 215 */       this.selectedProfile = selectedProfile;
/* 216 */       this.selectedUser = selectedUser;
/* 217 */       this.clientToken = clientToken;
/* 218 */       this.authenticationDatabase = authenticationDatabase;
/*     */     }
/*     */ 
/*     */     
/*     */     public static class Serializer
/*     */       implements JsonDeserializer<RawProfileList>, JsonSerializer<RawProfileList>
/*     */     {
/*     */       private final Launcher launcher;
/*     */       
/*     */       public Serializer(Launcher launcher) {
/* 228 */         this.launcher = launcher;
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*     */       public ProfileManager.RawProfileList deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
/* 234 */         JsonObject object = (JsonObject)json;
/* 235 */         Map<String, Profile> profiles = Maps.newHashMap();
/* 236 */         if (object.has("profiles")) {
/* 237 */           profiles = (Map<String, Profile>)context.deserialize(object.get("profiles"), (new TypeToken<Map<String, Profile>>() {  }).getType());
/*     */         }
/* 239 */         String selectedProfile = null;
/* 240 */         if (object.has("selectedProfile")) {
/* 241 */           selectedProfile = object.getAsJsonPrimitive("selectedProfile").getAsString();
/*     */         }
/* 243 */         UUID clientToken = UUID.randomUUID();
/* 244 */         if (object.has("clientToken")) {
/* 245 */           clientToken = (UUID)context.deserialize(object.get("clientToken"), UUID.class);
/*     */         }
/* 247 */         AuthenticationDatabase database = new AuthenticationDatabase((AuthenticationService)new YggdrasilAuthenticationService(this.launcher.getLauncher().getProxy(), this.launcher.getClientToken().toString()));
/* 248 */         if (object.has("authenticationDatabase")) {
/* 249 */           database = (AuthenticationDatabase)context.deserialize(object.get("authenticationDatabase"), AuthenticationDatabase.class);
/*     */         }
/* 251 */         String selectedUser = null;
/* 252 */         if (object.has("selectedUser")) {
/* 253 */           selectedUser = object.getAsJsonPrimitive("selectedUser").getAsString();
/* 254 */         } else if (selectedProfile != null && profiles.containsKey(selectedProfile) && ((Profile)profiles.get(selectedProfile)).getPlayerUUID() != null) {
/* 255 */           selectedUser = ((Profile)profiles.get(selectedProfile)).getPlayerUUID();
/* 256 */         } else if (!database.getknownUUIDs().isEmpty()) {
/* 257 */           selectedUser = database.getknownUUIDs().iterator().next();
/*     */         } 
/* 259 */         for (Profile profile : profiles.values()) {
/* 260 */           profile.setPlayerUUID(null);
/*     */         }
/* 262 */         return new ProfileManager.RawProfileList(profiles, selectedProfile, selectedUser, clientToken, database);
/*     */       }
/*     */       
/*     */       public JsonElement serialize(ProfileManager.RawProfileList src, Type typeOfSrc, JsonSerializationContext context)
/*     */       {
/* 267 */         JsonObject version = new JsonObject();
/* 268 */         version.addProperty("name", LauncherConstants.getVersionName());
/* 269 */         version.addProperty("format", Integer.valueOf(21));
/* 270 */         version.addProperty("profilesFormat", Integer.valueOf(1));
/*     */         
/* 272 */         JsonObject object = new JsonObject();
/* 273 */         object.add("profiles", context.serialize(src.profiles));
/* 274 */         object.add("selectedProfile", context.serialize(src.selectedProfile));
/* 275 */         object.add("clientToken", context.serialize(src.clientToken));
/* 276 */         object.add("authenticationDatabase", context.serialize(src.authenticationDatabase));
/* 277 */         object.add("selectedUser", context.serialize(src.selectedUser));
/* 278 */         object.add("launcherVersion", (JsonElement)version);
/*     */         
/* 280 */         return (JsonElement)object; } } } public static class Serializer implements JsonDeserializer<RawProfileList>, JsonSerializer<RawProfileList> { public JsonElement serialize(ProfileManager.RawProfileList src, Type typeOfSrc, JsonSerializationContext context) { JsonObject version = new JsonObject(); version.addProperty("name", LauncherConstants.getVersionName()); version.addProperty("format", Integer.valueOf(21)); version.addProperty("profilesFormat", Integer.valueOf(1)); JsonObject object = new JsonObject(); object.add("profiles", context.serialize(src.profiles)); object.add("selectedProfile", context.serialize(src.selectedProfile)); object.add("clientToken", context.serialize(src.clientToken)); object.add("authenticationDatabase", context.serialize(src.authenticationDatabase)); object.add("selectedUser", context.serialize(src.selectedUser)); object.add("launcherVersion", (JsonElement)version); return (JsonElement)object; }
/*     */ 
/*     */     
/*     */     private final Launcher launcher;
/*     */     
/*     */     public Serializer(Launcher launcher) {
/*     */       this.launcher = launcher;
/*     */     }
/*     */     
/*     */     public ProfileManager.RawProfileList deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
/*     */       JsonObject object = (JsonObject)json;
/*     */       Map<String, Profile> profiles = Maps.newHashMap();
/*     */       if (object.has("profiles"))
/*     */         profiles = (Map<String, Profile>)context.deserialize(object.get("profiles"), (new TypeToken<Map<String, Profile>>() {
/*     */             
/*     */             }).getType()); 
/*     */       String selectedProfile = null;
/*     */       if (object.has("selectedProfile"))
/*     */         selectedProfile = object.getAsJsonPrimitive("selectedProfile").getAsString(); 
/*     */       UUID clientToken = UUID.randomUUID();
/*     */       if (object.has("clientToken"))
/*     */         clientToken = (UUID)context.deserialize(object.get("clientToken"), UUID.class); 
/*     */       AuthenticationDatabase database = new AuthenticationDatabase((AuthenticationService)new YggdrasilAuthenticationService(this.launcher.getLauncher().getProxy(), this.launcher.getClientToken().toString()));
/*     */       if (object.has("authenticationDatabase"))
/*     */         database = (AuthenticationDatabase)context.deserialize(object.get("authenticationDatabase"), AuthenticationDatabase.class); 
/*     */       String selectedUser = null;
/*     */       if (object.has("selectedUser")) {
/*     */         selectedUser = object.getAsJsonPrimitive("selectedUser").getAsString();
/*     */       } else if (selectedProfile != null && profiles.containsKey(selectedProfile) && ((Profile)profiles.get(selectedProfile)).getPlayerUUID() != null) {
/*     */         selectedUser = ((Profile)profiles.get(selectedProfile)).getPlayerUUID();
/*     */       } else if (!database.getknownUUIDs().isEmpty()) {
/*     */         selectedUser = database.getknownUUIDs().iterator().next();
/*     */       } 
/*     */       for (Profile profile : profiles.values())
/*     */         profile.setPlayerUUID(null); 
/*     */       return new ProfileManager.RawProfileList(profiles, selectedProfile, selectedUser, clientToken, database);
/*     */     } }
/*     */ 
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\net\minecraft\launcher\profile\ProfileManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */