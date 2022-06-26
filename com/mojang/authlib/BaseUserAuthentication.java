/*     */ package com.mojang.authlib;
/*     */ 
/*     */ import com.google.common.collect.Multimap;
/*     */ import com.mojang.authlib.properties.Property;
/*     */ import com.mojang.authlib.properties.PropertyMap;
/*     */ import com.mojang.util.UUIDTypeAdapter;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.apache.commons.lang3.Validate;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public abstract class BaseUserAuthentication
/*     */   implements UserAuthentication {
/*  18 */   private static final Logger LOGGER = LogManager.getLogger();
/*     */   protected static final String STORAGE_KEY_PROFILE_NAME = "displayName";
/*     */   protected static final String STORAGE_KEY_PROFILE_ID = "uuid";
/*     */   protected static final String STORAGE_KEY_PROFILE_PROPERTIES = "profileProperties";
/*     */   protected static final String STORAGE_KEY_USER_NAME = "username";
/*     */   protected static final String STORAGE_KEY_USER_ID = "userid";
/*     */   protected static final String STORAGE_KEY_USER_PROPERTIES = "userProperties";
/*     */   private final AuthenticationService authenticationService;
/*  26 */   private final PropertyMap userProperties = new PropertyMap();
/*     */   
/*     */   private String userid;
/*     */   private String username;
/*     */   private String password;
/*     */   private GameProfile selectedProfile;
/*     */   private UserType userType;
/*     */   
/*     */   protected BaseUserAuthentication(AuthenticationService authenticationService) {
/*  35 */     Validate.notNull(authenticationService);
/*  36 */     this.authenticationService = authenticationService;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canLogIn() {
/*  41 */     return (!canPlayOnline() && StringUtils.isNotBlank(getUsername()) && StringUtils.isNotBlank(getPassword()));
/*     */   }
/*     */ 
/*     */   
/*     */   public void logOut() {
/*  46 */     this.password = null;
/*  47 */     this.userid = null;
/*  48 */     setSelectedProfile(null);
/*  49 */     getModifiableUserProperties().clear();
/*  50 */     setUserType(null);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isLoggedIn() {
/*  55 */     return (getSelectedProfile() != null);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setUsername(String username) {
/*  60 */     if (isLoggedIn() && canPlayOnline()) {
/*  61 */       throw new IllegalStateException("Cannot change username whilst logged in & online");
/*     */     }
/*  63 */     this.username = username;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPassword(String password) {
/*  68 */     if (isLoggedIn() && canPlayOnline() && StringUtils.isNotBlank(password)) {
/*  69 */       throw new IllegalStateException("Cannot set password whilst logged in & online");
/*     */     }
/*  71 */     this.password = password;
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getUsername() {
/*  76 */     return this.username;
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getPassword() {
/*  81 */     return this.password;
/*     */   }
/*     */ 
/*     */   
/*     */   public void loadFromStorage(Map<String, Object> credentials) {
/*  86 */     logOut();
/*     */     
/*  88 */     setUsername(String.valueOf(credentials.get("username")));
/*  89 */     if (credentials.containsKey("userid")) {
/*  90 */       this.userid = String.valueOf(credentials.get("userid"));
/*     */     } else {
/*  92 */       this.userid = this.username;
/*     */     } 
/*  94 */     if (credentials.containsKey("userProperties")) {
/*     */       
/*     */       try {
/*  97 */         List<Map<String, String>> list = (List<Map<String, String>>)credentials.get("userProperties");
/*  98 */         for (Map<String, String> propertyMap : list)
/*     */         {
/* 100 */           String name = propertyMap.get("name");
/* 101 */           String value = propertyMap.get("value");
/* 102 */           String signature = propertyMap.get("signature");
/* 103 */           if (signature == null) {
/* 104 */             getModifiableUserProperties().put(name, new Property(name, value)); continue;
/*     */           } 
/* 106 */           getModifiableUserProperties().put(name, new Property(name, value, signature));
/*     */         }
/*     */       
/*     */       }
/* 110 */       catch (Throwable t) {
/*     */         
/* 112 */         LOGGER.warn("Couldn't deserialize user properties", t);
/*     */       } 
/*     */     }
/* 115 */     if (credentials.containsKey("displayName") && credentials.containsKey("uuid")) {
/*     */       
/* 117 */       GameProfile profile = new GameProfile(UUIDTypeAdapter.fromString(String.valueOf(credentials.get("uuid"))), String.valueOf(credentials.get("displayName")));
/* 118 */       if (credentials.containsKey("profileProperties")) {
/*     */         
/*     */         try {
/* 121 */           List<Map<String, String>> list = (List<Map<String, String>>)credentials.get("profileProperties");
/* 122 */           for (Map<String, String> propertyMap : list)
/*     */           {
/* 124 */             String name = propertyMap.get("name");
/* 125 */             String value = propertyMap.get("value");
/* 126 */             String signature = propertyMap.get("signature");
/* 127 */             if (signature == null) {
/* 128 */               profile.getProperties().put(name, new Property(name, value)); continue;
/*     */             } 
/* 130 */             profile.getProperties().put(name, new Property(name, value, signature));
/*     */           }
/*     */         
/*     */         }
/* 134 */         catch (Throwable t) {
/*     */           
/* 136 */           LOGGER.warn("Couldn't deserialize profile properties", t);
/*     */         } 
/*     */       }
/* 139 */       setSelectedProfile(profile);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<String, Object> saveForStorage() {
/* 145 */     Map<String, Object> result = new HashMap<>();
/* 146 */     if (getUsername() != null) {
/* 147 */       result.put("username", getUsername());
/*     */     }
/* 149 */     if (getUserID() != null) {
/* 150 */       result.put("userid", getUserID());
/* 151 */     } else if (getUsername() != null) {
/* 152 */       result.put("username", getUsername());
/*     */     } 
/* 154 */     if (!getUserProperties().isEmpty()) {
/*     */       
/* 156 */       List<Map<String, String>> properties = new ArrayList<>();
/* 157 */       for (Property userProperty : getUserProperties().values()) {
/*     */         
/* 159 */         Map<String, String> property = new HashMap<>();
/* 160 */         property.put("name", userProperty.getName());
/* 161 */         property.put("value", userProperty.getValue());
/* 162 */         property.put("signature", userProperty.getSignature());
/* 163 */         properties.add(property);
/*     */       } 
/* 165 */       result.put("userProperties", properties);
/*     */     } 
/* 167 */     GameProfile selectedProfile = getSelectedProfile();
/* 168 */     if (selectedProfile != null) {
/*     */       
/* 170 */       result.put("displayName", selectedProfile.getName());
/* 171 */       result.put("uuid", selectedProfile.getId());
/*     */       
/* 173 */       List<Map<String, String>> properties = new ArrayList<>();
/* 174 */       for (Property profileProperty : selectedProfile.getProperties().values()) {
/*     */         
/* 176 */         Map<String, String> property = new HashMap<>();
/* 177 */         property.put("name", profileProperty.getName());
/* 178 */         property.put("value", profileProperty.getValue());
/* 179 */         property.put("signature", profileProperty.getSignature());
/* 180 */         properties.add(property);
/*     */       } 
/* 182 */       if (!properties.isEmpty()) {
/* 183 */         result.put("profileProperties", properties);
/*     */       }
/*     */     } 
/* 186 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void setSelectedProfile(GameProfile selectedProfile) {
/* 191 */     this.selectedProfile = selectedProfile;
/*     */   }
/*     */ 
/*     */   
/*     */   public GameProfile getSelectedProfile() {
/* 196 */     return this.selectedProfile;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 201 */     StringBuilder result = new StringBuilder();
/*     */     
/* 203 */     result.append(getClass().getSimpleName());
/* 204 */     result.append("{");
/* 205 */     if (isLoggedIn()) {
/*     */       
/* 207 */       result.append("Logged in as ");
/* 208 */       result.append(getUsername());
/* 209 */       if (getSelectedProfile() != null) {
/*     */         
/* 211 */         result.append(" / ");
/* 212 */         result.append(getSelectedProfile());
/* 213 */         result.append(" - ");
/* 214 */         if (canPlayOnline()) {
/* 215 */           result.append("Online");
/*     */         } else {
/* 217 */           result.append("Offline");
/*     */         }
/*     */       
/*     */       } 
/*     */     } else {
/*     */       
/* 223 */       result.append("Not logged in");
/*     */     } 
/* 225 */     result.append("}");
/*     */     
/* 227 */     return result.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public AuthenticationService getAuthenticationService() {
/* 232 */     return this.authenticationService;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getUserID() {
/* 237 */     return this.userid;
/*     */   }
/*     */ 
/*     */   
/*     */   public PropertyMap getUserProperties() {
/* 242 */     if (isLoggedIn()) {
/*     */       
/* 244 */       PropertyMap result = new PropertyMap();
/* 245 */       result.putAll((Multimap)getModifiableUserProperties());
/* 246 */       return result;
/*     */     } 
/* 248 */     return new PropertyMap();
/*     */   }
/*     */ 
/*     */   
/*     */   protected PropertyMap getModifiableUserProperties() {
/* 253 */     return this.userProperties;
/*     */   }
/*     */ 
/*     */   
/*     */   public UserType getUserType() {
/* 258 */     if (isLoggedIn()) {
/* 259 */       return (this.userType == null) ? UserType.LEGACY : this.userType;
/*     */     }
/* 261 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void setUserType(UserType userType) {
/* 266 */     this.userType = userType;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void setUserid(String userid) {
/* 271 */     this.userid = userid;
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\mojang\authlib\BaseUserAuthentication.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */