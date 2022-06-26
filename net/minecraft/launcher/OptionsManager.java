/*     */ package net.minecraft.launcher;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.FileWriter;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.util.LinkedHashMap;
/*     */ import net.mc.main.Util;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class OptionsManager
/*     */ {
/*  26 */   private static final Logger LOGGER = LogManager.getLogger();
/*     */   
/*  28 */   LinkedHashMap<String, String> options = new LinkedHashMap<>();
/*  29 */   File serverData = new File(Util.getWorkingDirectory(), "options.txt");
/*  30 */   Boolean fileExists = Boolean.valueOf(false);
/*     */   
/*     */   public OptionsManager() {
/*  33 */     loadOptions();
/*  34 */     this.serverData = new File(Util.getWorkingDirectory(), "options.txt");
/*     */   }
/*     */   
/*     */   public Boolean exists() {
/*  38 */     return this.fileExists;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setOption(String key, String value) {
/*  43 */     if (this.fileExists.booleanValue()) {
/*  44 */       this.options.put(key, value);
/*  45 */       saveOptions();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void setLanguage() {
/*  51 */     if (this.fileExists.booleanValue() && 
/*  52 */       Language.getBoolean("minecraft.language.exists").equals(Boolean.valueOf(true))) {
/*  53 */       setOption("lang", Language.get("minecraft.language.code"));
/*  54 */       LOGGER.info("[ShiginimaLauncher] Successfully set the minecraft language to " + Language.get("minecraft.language.code"));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getOption(String key) {
/*  61 */     return this.options.get(key);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void saveOptions() {
/*     */     try {
/*  68 */       BufferedWriter bw = new BufferedWriter(new FileWriter(this.serverData));
/*  69 */       for (String p : this.options.keySet()) {
/*  70 */         bw.write(p + ":" + (String)this.options.get(p));
/*  71 */         bw.newLine();
/*     */       } 
/*  73 */       bw.flush();
/*  74 */       bw.close();
/*  75 */       LOGGER.info("[ShiginimaLauncher] Saved options.txt");
/*  76 */     } catch (IOException e) {
/*  77 */       LOGGER.info("[ShiginimaLauncher] options.txt not found.");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void loadOptions() {
/*  83 */     FileInputStream fstream = null;
/*     */     
/*     */     try {
/*  86 */       fstream = new FileInputStream(this.serverData);
/*  87 */       BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
/*     */       
/*     */       String strLine;
/*  90 */       while ((strLine = br.readLine()) != null) {
/*     */ 
/*     */         
/*  93 */         String[] breakParts = strLine.split(":", 2);
/*     */         
/*  95 */         String key = breakParts[0];
/*     */         
/*  97 */         String value = breakParts[1];
/*     */         
/*  99 */         this.options.put(key, value);
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 104 */       br.close();
/* 105 */     } catch (FileNotFoundException ex) {
/* 106 */       LOGGER.info("[ShiginimaLauncher] options.txt not found. Run minecraft at least once to generate the file!.");
/* 107 */     } catch (IOException ex) {
/* 108 */       LOGGER.info("[ShiginimaLauncher] options.txt not found. Run minecraft at least once to generate the file! .");
/*     */     } finally {
/*     */       try {
/* 111 */         if (fstream != null) {
/* 112 */           fstream.close();
/* 113 */           this.fileExists = Boolean.valueOf(true);
/*     */         } 
/* 115 */       } catch (IOException ex) {
/* 116 */         LOGGER.info("[ShiginimaLauncher] options.txt not found. Run minecraft at least once to generate the file!  .");
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\net\minecraft\launcher\OptionsManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */