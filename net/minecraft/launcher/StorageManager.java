/*     */ package net.minecraft.launcher;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.FileReader;
/*     */ import java.io.FileWriter;
/*     */ import java.io.IOException;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.net.URLDecoder;
/*     */ import java.net.URLEncoder;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import net.mc.main.Util;
/*     */ import org.yaml.snakeyaml.Yaml;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class StorageManager
/*     */ {
/*     */   private List<String> usernames;
/*     */   private File usernameFile;
/*     */   private File usernameFileOld;
/*  36 */   private String confVersion = "2";
/*     */   
/*  38 */   private String language = "en";
/*  39 */   private String lastused = "";
/*  40 */   private String autoName = "";
/*  41 */   private Boolean autoLogin = Boolean.valueOf(false);
/*  42 */   private Boolean syncLanguage = Boolean.valueOf(false);
/*  43 */   private Boolean useDarkTheme = Boolean.valueOf(true);
/*     */ 
/*     */   
/*     */   public StorageManager() {
/*  47 */     this.usernameFile = new File(Util.getWorkingDirectory(), "shig.inima");
/*     */     
/*  49 */     this.usernameFileOld = new File(Util.getWorkingDirectory(), "usernames.shiginima");
/*     */     
/*  51 */     if (!this.usernameFile.exists()) {
/*     */       
/*     */       try {
/*  54 */         this.usernameFile.createNewFile();
/*  55 */       } catch (IOException ex) {
/*  56 */         Logger.getLogger(StorageManager.class.getName()).log(Level.SEVERE, (String)null, ex);
/*     */       } 
/*     */     }
/*     */     
/*  60 */     if (this.usernameFileOld.exists()) {
/*  61 */       convertFromOld();
/*  62 */       this.usernameFileOld.delete();
/*     */     } 
/*     */     
/*  65 */     this.usernames = new ArrayList<>();
/*     */     
/*  67 */     load();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void convertFromOld() {
/*     */     try {
/*  74 */       BufferedReader br = new BufferedReader(new FileReader(this.usernameFileOld));
/*     */       
/*  76 */       List<String> toput = new ArrayList<>();
/*  77 */       int iter = 0;
/*     */       String line;
/*  79 */       while ((line = br.readLine()) != null) {
/*  80 */         toput.add(line);
/*  81 */         iter++;
/*     */       } 
/*  83 */       this.usernames = toput;
/*     */       
/*  85 */       br.close();
/*  86 */       save();
/*  87 */     } catch (IOException iOException) {}
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<String> getUsernames() {
/*  94 */     List<String> uclean = new ArrayList<>();
/*  95 */     for (String s : this.usernames) {
/*     */       try {
/*  97 */         String uname = URLDecoder.decode(s, "UTF-8");
/*  98 */         uclean.add(uname);
/*  99 */       } catch (UnsupportedEncodingException unsupportedEncodingException) {}
/*     */     } 
/*     */ 
/*     */     
/* 103 */     return uclean;
/*     */   }
/*     */   
/*     */   public boolean removeUsername(String un) throws IOException {
/* 107 */     String username = URLEncoder.encode(un, "UTF-8");
/* 108 */     if (this.usernames.contains(username)) {
/* 109 */       this.usernames.remove(username);
/* 110 */       save();
/* 111 */       return true;
/*     */     } 
/* 113 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean addUsername(String username) throws IOException {
/* 118 */     String uname = URLEncoder.encode(username, "UTF-8");
/* 119 */     if (this.usernames.contains(uname)) {
/* 120 */       return false;
/*     */     }
/* 122 */     this.usernames.add(uname);
/* 123 */     save();
/* 124 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getLastUsed() {
/* 129 */     return this.lastused;
/*     */   }
/*     */   
/*     */   public void setLastUsed(String last) {
/* 133 */     this.lastused = last;
/* 134 */     save();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getLanguage() {
/* 139 */     return this.language;
/*     */   }
/*     */   
/*     */   public void setLanguage(String lang) {
/* 143 */     this.language = lang;
/*     */   }
/*     */   
/*     */   public void setAutoLoginName(String name) {
/* 147 */     this.autoName = name;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getAutoLoginName() {
/* 152 */     return this.autoName;
/*     */   }
/*     */   
/*     */   public Boolean isAutoLogin() {
/* 156 */     return this.autoLogin;
/*     */   }
/*     */   
/*     */   public void setAutoLogin(Boolean hmm) {
/* 160 */     this.autoLogin = hmm;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setSyncLanguage(Boolean bool) {
/* 165 */     this.syncLanguage = bool;
/* 166 */     save();
/*     */   }
/*     */   
/*     */   public Boolean willSyncLanguage() {
/* 170 */     return this.syncLanguage;
/*     */   }
/*     */   
/*     */   public void setDark(Boolean useDark) {
/* 174 */     this.useDarkTheme = useDark;
/*     */   }
/*     */   
/*     */   public Boolean getDark() {
/* 178 */     return this.useDarkTheme;
/*     */   }
/*     */   
/*     */   public void save() {
/* 182 */     Map<String, Object> data = new HashMap<>();
/*     */     
/* 184 */     data.put("username.lastused", this.lastused);
/* 185 */     data.put("username.names", this.usernames);
/* 186 */     data.put("auto.enabled", this.autoLogin);
/* 187 */     data.put("auto.name", this.autoName);
/* 188 */     data.put("language", this.language);
/* 189 */     data.put("confver", this.confVersion);
/* 190 */     data.put("auto.language", this.syncLanguage);
/* 191 */     data.put("theme.dark", this.useDarkTheme);
/*     */     
/* 193 */     System.out.println("SAVED: " + this.language + this.autoName + this.autoLogin);
/*     */     
/* 195 */     Yaml yaml = new Yaml();
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 200 */       FileWriter writer = new FileWriter(this.usernameFile);
/* 201 */       yaml.dump(data, writer);
/* 202 */       writer.close();
/* 203 */     } catch (IOException iOException) {}
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void load() {
/*     */     try {
/* 214 */       BufferedReader br = new BufferedReader(new FileReader(this.usernameFile));
/* 215 */       Yaml yaml = new Yaml();
/* 216 */       Map<String, Object> data = (Map<String, Object>)yaml.load(br);
/* 217 */       br.close();
/*     */       
/* 219 */       String cv = (String)data.get("confver");
/* 220 */       String lang = (String)data.get("language");
/*     */       
/* 222 */       if (cv == null) {
/* 223 */         this.language = "en";
/*     */       } else {
/* 225 */         this.language = lang;
/*     */       } 
/*     */       
/* 228 */       this.usernames = (List<String>)data.get("username.names");
/* 229 */       this.lastused = (String)data.get("username.lastused");
/* 230 */       this.autoLogin = (Boolean)data.get("auto.enabled");
/* 231 */       this.autoName = (String)data.get("auto.name");
/* 232 */       this.syncLanguage = (Boolean)data.get("auto.language");
/*     */ 
/*     */       
/* 235 */       if (this.syncLanguage == null) {
/* 236 */         this.syncLanguage = Boolean.valueOf(false);
/*     */       }
/*     */       
/* 239 */       this.useDarkTheme = (Boolean)data.get("theme.dark");
/*     */       
/* 241 */       if (this.useDarkTheme == null) {
/* 242 */         this.useDarkTheme = Boolean.valueOf(true);
/*     */       }
/*     */       
/* 245 */       System.out.println("LOADED" + this.language + this.autoName + this.autoLogin);
/*     */     }
/* 247 */     catch (Exception exception) {
/*     */     
/*     */     } finally {
/* 250 */       save();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\net\minecraft\launcher\StorageManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */