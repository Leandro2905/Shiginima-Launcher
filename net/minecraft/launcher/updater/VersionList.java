/*     */ package net.minecraft.launcher.updater;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.gson.Gson;
/*     */ import com.google.gson.GsonBuilder;
/*     */ import com.google.gson.TypeAdapterFactory;
/*     */ import com.mojang.launcher.OperatingSystem;
/*     */ import com.mojang.launcher.updater.DateTypeAdapter;
/*     */ import com.mojang.launcher.updater.LowerCaseEnumTypeAdapterFactory;
/*     */ import com.mojang.launcher.versions.CompleteVersion;
/*     */ import com.mojang.launcher.versions.ReleaseType;
/*     */ import com.mojang.launcher.versions.ReleaseTypeAdapterFactory;
/*     */ import com.mojang.launcher.versions.ReleaseTypeFactory;
/*     */ import com.mojang.launcher.versions.Version;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import net.minecraft.launcher.game.MinecraftReleaseType;
/*     */ import net.minecraft.launcher.game.MinecraftReleaseTypeFactory;
/*     */ 
/*     */ public abstract class VersionList {
/*  27 */   protected final Map<String, Version> versionsByName = new HashMap<>(); protected final Gson gson;
/*  28 */   protected final List<Version> versions = new ArrayList<>();
/*  29 */   protected final Map<MinecraftReleaseType, Version> latestVersions = Maps.newEnumMap(MinecraftReleaseType.class);
/*     */ 
/*     */   
/*     */   public VersionList() {
/*  33 */     GsonBuilder builder = new GsonBuilder();
/*  34 */     builder.registerTypeAdapterFactory((TypeAdapterFactory)new LowerCaseEnumTypeAdapterFactory());
/*  35 */     builder.registerTypeAdapter(Date.class, new DateTypeAdapter());
/*  36 */     builder.registerTypeAdapter(ReleaseType.class, new ReleaseTypeAdapterFactory((ReleaseTypeFactory)MinecraftReleaseTypeFactory.instance()));
/*  37 */     builder.registerTypeAdapter(Argument.class, new Argument.Serializer());
/*  38 */     builder.enableComplexMapKeySerialization();
/*  39 */     builder.setPrettyPrinting();
/*     */     
/*  41 */     this.gson = builder.create();
/*     */   }
/*     */ 
/*     */   
/*     */   public Collection<Version> getVersions() {
/*  46 */     return this.versions;
/*     */   }
/*     */ 
/*     */   
/*     */   public Version getLatestVersion(MinecraftReleaseType type) {
/*  51 */     if (type == null) {
/*  52 */       throw new IllegalArgumentException("Type cannot be null");
/*     */     }
/*  54 */     return this.latestVersions.get(type);
/*     */   }
/*     */ 
/*     */   
/*     */   public Version getVersion(String name) {
/*  59 */     if (name == null || name.length() == 0) {
/*  60 */       throw new IllegalArgumentException("Name cannot be null or empty");
/*     */     }
/*  62 */     return this.versionsByName.get(name);
/*     */   }
/*     */ 
/*     */   
/*     */   public abstract CompleteMinecraftVersion getCompleteVersion(Version paramVersion) throws IOException;
/*     */ 
/*     */   
/*     */   protected void replacePartialWithFull(PartialVersion version, CompleteVersion complete) {
/*  70 */     Collections.replaceAll(this.versions, version, complete);
/*  71 */     this.versionsByName.put(version.getId(), complete);
/*  72 */     if (this.latestVersions.get(version.getType()) == version) {
/*  73 */       this.latestVersions.put(version.getType(), complete);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void clearCache() {
/*  79 */     this.versionsByName.clear();
/*  80 */     this.versions.clear();
/*  81 */     this.latestVersions.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public abstract void refreshVersions() throws IOException;
/*     */ 
/*     */   
/*     */   public CompleteVersion addVersion(CompleteVersion version) {
/*  89 */     if (version.getId() == null) {
/*  90 */       throw new IllegalArgumentException("Cannot add blank version");
/*     */     }
/*  92 */     if (getVersion(version.getId()) != null) {
/*  93 */       throw new IllegalArgumentException("Version '" + version.getId() + "' is already tracked");
/*     */     }
/*  95 */     this.versions.add(version);
/*  96 */     this.versionsByName.put(version.getId(), version);
/*     */     
/*  98 */     return version;
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeVersion(String name) {
/* 103 */     if (name == null || name.length() == 0) {
/* 104 */       throw new IllegalArgumentException("Name cannot be null or empty");
/*     */     }
/* 106 */     Version version = getVersion(name);
/* 107 */     if (version == null) {
/* 108 */       throw new IllegalArgumentException("Unknown version - cannot remove null");
/*     */     }
/* 110 */     removeVersion(version);
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeVersion(Version version) {
/* 115 */     if (version == null) {
/* 116 */       throw new IllegalArgumentException("Cannot remove null version");
/*     */     }
/* 118 */     this.versions.remove(version);
/* 119 */     this.versionsByName.remove(version.getId());
/* 120 */     for (MinecraftReleaseType type : MinecraftReleaseType.values()) {
/* 121 */       if (getLatestVersion(type) == version) {
/* 122 */         this.latestVersions.remove(type);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void setLatestVersion(Version version) {
/* 129 */     if (version == null) {
/* 130 */       throw new IllegalArgumentException("Cannot set latest version to null");
/*     */     }
/* 132 */     this.latestVersions.put((MinecraftReleaseType)version.getType(), version);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setLatestVersion(String name) {
/* 137 */     if (name == null || name.length() == 0) {
/* 138 */       throw new IllegalArgumentException("Name cannot be null or empty");
/*     */     }
/* 140 */     Version version = getVersion(name);
/* 141 */     if (version == null) {
/* 142 */       throw new IllegalArgumentException("Unknown version - cannot set latest version to null");
/*     */     }
/* 144 */     setLatestVersion(version);
/*     */   }
/*     */ 
/*     */   
/*     */   public String serializeVersion(CompleteVersion version) {
/* 149 */     if (version == null) {
/* 150 */       throw new IllegalArgumentException("Cannot serialize null!");
/*     */     }
/* 152 */     return this.gson.toJson(version);
/*     */   }
/*     */ 
/*     */   
/*     */   public abstract boolean hasAllFiles(CompleteMinecraftVersion paramCompleteMinecraftVersion, OperatingSystem paramOperatingSystem);
/*     */   
/*     */   public void uninstallVersion(Version version) {
/* 159 */     removeVersion(version);
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\net\minecraft\launche\\updater\VersionList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */