/*     */ package net.minecraft.launcher.updater;
/*     */ 
/*     */ import com.mojang.launcher.OperatingSystem;
/*     */ import com.mojang.launcher.versions.CompleteVersion;
/*     */ import com.mojang.launcher.versions.Version;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.Set;
/*     */ import net.minecraft.launcher.game.MinecraftReleaseType;
/*     */ import org.apache.commons.io.FileUtils;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LocalVersionList
/*     */   extends FileBasedVersionList
/*     */ {
/*  23 */   private static final Logger LOGGER = LogManager.getLogger();
/*     */   
/*     */   private final File baseDirectory;
/*     */   private final File baseVersionsDir;
/*     */   
/*     */   public LocalVersionList(File baseDirectory) {
/*  29 */     if (baseDirectory == null || !baseDirectory.isDirectory()) {
/*  30 */       throw new IllegalArgumentException("Base directory is not a folder!");
/*     */     }
/*  32 */     this.baseDirectory = baseDirectory;
/*  33 */     this.baseVersionsDir = new File(this.baseDirectory, "versions");
/*  34 */     if (!this.baseVersionsDir.isDirectory()) {
/*  35 */       this.baseVersionsDir.mkdirs();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected InputStream getFileInputStream(String path) throws FileNotFoundException {
/*  42 */     return new FileInputStream(new File(this.baseDirectory, path));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void refreshVersions() throws IOException {
/*  48 */     clearCache();
/*     */     
/*  50 */     File[] files = this.baseVersionsDir.listFiles();
/*  51 */     if (files == null) {
/*     */       return;
/*     */     }
/*  54 */     for (File directory : files) {
/*     */       
/*  56 */       String id = directory.getName();
/*  57 */       File jsonFile = new File(directory, id + ".json");
/*  58 */       if (directory.isDirectory() && jsonFile.exists()) {
/*     */         
/*     */         try {
/*  61 */           String path = "versions/" + id + "/" + id + ".json";
/*     */           
/*  63 */           String contents = getContent(path);
/*     */           
/*  65 */           CompleteVersion version = (CompleteVersion)this.gson.fromJson(contents, CompleteMinecraftVersion.class);
/*  66 */           if (version.getType() == null) {
/*     */             
/*  68 */             LOGGER.warn("Ignoring: " + path + "; it has an invalid version specified");
/*     */             return;
/*     */           } 
/*  71 */           if (version.getId().equals(id)) {
/*  72 */             addVersion(version);
/*     */           } else {
/*  74 */             LOGGER.warn("Ignoring: " + path + "; it contains id: '" + version.getId() + "' expected '" + id + "'");
/*     */           }
/*     */         
/*  77 */         } catch (RuntimeException ex) {
/*     */           
/*  79 */           LOGGER.error("Couldn't load local version " + jsonFile.getAbsolutePath(), ex);
/*     */         } 
/*     */       }
/*     */     } 
/*  83 */     for (Version version : getVersions()) {
/*     */       
/*  85 */       MinecraftReleaseType type = (MinecraftReleaseType)version.getType();
/*  86 */       if (getLatestVersion(type) == null || getLatestVersion(type).getUpdatedTime().before(version.getUpdatedTime())) {
/*  87 */         setLatestVersion(version);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void saveVersion(CompleteVersion version) throws IOException {
/*  95 */     String text = serializeVersion(version);
/*  96 */     File target = new File(this.baseVersionsDir, version.getId() + "/" + version.getId() + ".json");
/*  97 */     if (target.getParentFile() != null) {
/*  98 */       target.getParentFile().mkdirs();
/*     */     }
/* 100 */     PrintWriter writer = new PrintWriter(target);
/* 101 */     writer.print(text);
/* 102 */     writer.close();
/*     */   }
/*     */ 
/*     */   
/*     */   public File getBaseDirectory() {
/* 107 */     return this.baseDirectory;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasAllFiles(CompleteMinecraftVersion version, OperatingSystem os) {
/* 112 */     Set<String> files = version.getRequiredFiles(os);
/* 113 */     for (String file : files) {
/* 114 */       if (!(new File(this.baseDirectory, file)).isFile()) {
/* 115 */         return false;
/*     */       }
/*     */     } 
/* 118 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void uninstallVersion(Version version) {
/* 123 */     super.uninstallVersion(version);
/*     */     
/* 125 */     File dir = new File(this.baseVersionsDir, version.getId());
/* 126 */     if (dir.isDirectory())
/* 127 */       FileUtils.deleteQuietly(dir); 
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\net\minecraft\launche\\updater\LocalVersionList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */