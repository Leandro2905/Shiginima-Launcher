/*     */ package net.minecraft.launcher;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.URISyntaxException;
/*     */ import java.net.URL;
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.jar.JarEntry;
/*     */ import java.util.jar.JarFile;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
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
/*     */ public class Language
/*     */ {
/*  31 */   private static final Logger LOGGER = LogManager.getLogger();
/*     */   public static Map<String, Object> data;
/*  33 */   public static Map<String, String> langs = new HashMap<>();
/*     */   
/*  35 */   private static String lang = "en";
/*     */ 
/*     */ 
/*     */   
/*     */   public static String get(String id) {
/*  40 */     return (String)data.get(id);
/*     */   }
/*     */   
/*     */   public static Boolean getBoolean(String id) {
/*  44 */     return (Boolean)data.get(id);
/*     */   }
/*     */   
/*     */   public static String getLanguage() {
/*  48 */     return lang;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void loadFileNames() throws URISyntaxException {
/*  55 */     String path = "langs";
/*  56 */     File jarFile = new File(NetLauncherMain.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
/*     */ 
/*     */     
/*  59 */     if (jarFile.isFile()) {
/*     */       
/*     */       try {
/*  62 */         JarFile jar = new JarFile(jarFile);
/*  63 */         Enumeration<JarEntry> entries = jar.entries();
/*  64 */         while (entries.hasMoreElements()) {
/*  65 */           String name = ((JarEntry)entries.nextElement()).getName();
/*  66 */           if (name.startsWith(path + "/")) {
/*     */             
/*  68 */             System.out.println(name);
/*     */             
/*     */             try {
/*  71 */               Yaml yaml = new Yaml();
/*  72 */               Map<String, Object> data2 = (Map<String, Object>)yaml.load(getClass().getResourceAsStream("/" + name));
/*     */               
/*  74 */               langs.put((String)data2.get("langcode"), (String)data2.get("description"));
/*     */               
/*  76 */               LOGGER.info("[Shiginima Launcher Log] SUCCESSFULLY LOADED LANGUAGE: " + (String)data2.get("langcode") + " " + (String)data2.get("description"));
/*  77 */             } catch (Exception ex) {
/*  78 */               System.out.println("Exception " + ex.getLocalizedMessage());
/*     */             } 
/*     */           } 
/*     */         } 
/*  82 */         jar.close();
/*  83 */       } catch (IOException ex) {
/*  84 */         Logger.getLogger(Language.class.getName()).log(Level.SEVERE, (String)null, ex);
/*     */       } 
/*     */     } else {
/*  87 */       URL url = Launcher.class.getResource("/" + path);
/*  88 */       if (url != null) {
/*     */         try {
/*  90 */           File apps = new File(url.toURI());
/*  91 */           for (File app : apps.listFiles()) {
/*     */ 
/*     */ 
/*     */             
/*  95 */             Yaml yaml = new Yaml();
/*  96 */             InputStream ios = new FileInputStream(app);
/*  97 */             System.out.println("Trying to load: " + app);
/*  98 */             Map<String, Object> data2 = (Map<String, Object>)yaml.load(ios);
/*     */             
/* 100 */             langs.put((String)data2.get("langcode"), (String)data2.get("description"));
/*     */             
/* 102 */             System.out.println("LOADED LANGUAGE: " + (String)data2.get("langcode") + " " + (String)data2.get("description"));
/*     */           } 
/* 104 */         } catch (URISyntaxException uRISyntaxException) {
/*     */         
/* 106 */         } catch (FileNotFoundException ex) {
/* 107 */           Logger.getLogger(Language.class.getName()).log(Level.SEVERE, (String)null, ex);
/*     */         } 
/*     */       }
/*     */     } 
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
/*     */   public void load(String lang) {
/* 133 */     Language.lang = lang;
/*     */     try {
/* 135 */       Yaml yaml = new Yaml();
/* 136 */       data = (Map<String, Object>)yaml.load(getClass().getResourceAsStream("/langs/" + lang + ".yml"));
/*     */       
/* 138 */       System.out.println("LOADED LANGUAGES");
/* 139 */     } catch (Exception exception) {}
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\net\minecraft\launcher\Language.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */