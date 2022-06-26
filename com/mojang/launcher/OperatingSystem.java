/*     */ package com.mojang.launcher;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.net.URI;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public enum OperatingSystem
/*     */ {
/*     */   private final String[] aliases;
/*  12 */   LINUX("linux", new String[] { "linux", "unix" }), WINDOWS("windows", new String[] { "win" }), OSX("osx", new String[] { "mac" }), UNKNOWN("unknown", new String[0]);
/*     */   static {
/*  14 */     LOGGER = LogManager.getLogger();
/*     */   }
/*     */   private final String name;
/*     */   private static final Logger LOGGER;
/*     */   
/*     */   OperatingSystem(String name, String... aliases) {
/*  20 */     this.name = name;
/*  21 */     this.aliases = (aliases == null) ? new String[0] : aliases;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getName() {
/*  26 */     return this.name;
/*     */   }
/*     */ 
/*     */   
/*     */   public String[] getAliases() {
/*  31 */     return this.aliases;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSupported() {
/*  36 */     return (this != UNKNOWN);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getJavaDir() {
/*  41 */     String separator = System.getProperty("file.separator");
/*  42 */     String path = System.getProperty("java.home") + separator + "bin" + separator;
/*  43 */     if (getCurrentPlatform() == WINDOWS && (new File(path + "javaw.exe"))
/*  44 */       .isFile()) {
/*  45 */       return path + "javaw.exe";
/*     */     }
/*  47 */     return path + "java";
/*     */   }
/*     */ 
/*     */   
/*     */   public static OperatingSystem getCurrentPlatform() {
/*  52 */     String osName = System.getProperty("os.name").toLowerCase();
/*  53 */     for (OperatingSystem os : values()) {
/*  54 */       for (String alias : os.getAliases()) {
/*  55 */         if (osName.contains(alias)) {
/*  56 */           return os;
/*     */         }
/*     */       } 
/*     */     } 
/*  60 */     return UNKNOWN;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void openLink(URI link) {
/*     */     try {
/*  67 */       Class<?> desktopClass = Class.forName("java.awt.Desktop");
/*  68 */       Object o = desktopClass.getMethod("getDesktop", new Class[0]).invoke(null, new Object[0]);
/*  69 */       desktopClass.getMethod("browse", new Class[] { URI.class }).invoke(o, new Object[] { link });
/*     */     }
/*  71 */     catch (Throwable e) {
/*     */       
/*  73 */       if (getCurrentPlatform() == OSX) {
/*     */         
/*     */         try {
/*  76 */           Runtime.getRuntime().exec(new String[] { "/usr/bin/open", link
/*  77 */                 .toString() });
/*     */         }
/*  79 */         catch (IOException e1) {
/*     */           
/*  81 */           LOGGER.error("Failed to open link " + link.toString(), e1);
/*     */         } 
/*     */       } else {
/*  84 */         LOGGER.error("Failed to open link " + link.toString(), e);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void openFolder(File path) {
/*  91 */     String absolutePath = path.getAbsolutePath();
/*  92 */     OperatingSystem os = getCurrentPlatform();
/*  93 */     if (os == OSX) {
/*     */ 
/*     */       
/*     */       try {
/*  97 */         Runtime.getRuntime().exec(new String[] { "/usr/bin/open", absolutePath });
/*     */ 
/*     */ 
/*     */         
/*     */         return;
/* 102 */       } catch (IOException e) {
/*     */         
/* 104 */         LOGGER.error("Couldn't open " + path + " through /usr/bin/open", e);
/*     */       }
/*     */     
/* 107 */     } else if (os == WINDOWS) {
/*     */       
/* 109 */       String cmd = String.format("cmd.exe /C start \"Open file\" \"%s\"", new Object[] { absolutePath });
/*     */       
/*     */       try {
/* 112 */         Runtime.getRuntime().exec(cmd);
/*     */         
/*     */         return;
/* 115 */       } catch (IOException e) {
/*     */         
/* 117 */         LOGGER.error("Couldn't open " + path + " through cmd.exe", e);
/*     */       } 
/*     */     } 
/*     */     
/*     */     try {
/* 122 */       Class<?> desktopClass = Class.forName("java.awt.Desktop");
/* 123 */       Object desktop = desktopClass.getMethod("getDesktop", new Class[0]).invoke(null, new Object[0]);
/* 124 */       desktopClass.getMethod("browse", new Class[] { URI.class }).invoke(desktop, new Object[] { path.toURI() });
/*     */     }
/* 126 */     catch (Throwable e) {
/*     */       
/* 128 */       LOGGER.error("Couldn't open " + path + " through Desktop.browse()", e);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\mojang\launcher\OperatingSystem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */