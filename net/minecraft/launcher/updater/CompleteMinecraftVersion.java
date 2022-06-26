/*     */ package net.minecraft.launcher.updater;
/*     */ 
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.launcher.OperatingSystem;
/*     */ import com.mojang.launcher.game.process.GameProcessBuilder;
/*     */ import com.mojang.launcher.updater.VersionSyncInfo;
/*     */ import com.mojang.launcher.updater.download.Downloadable;
/*     */ import com.mojang.launcher.versions.CompleteVersion;
/*     */ import com.mojang.launcher.versions.ReleaseType;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.Proxy;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Date;
/*     */ import java.util.EnumMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import net.minecraft.launcher.CompatibilityRule;
/*     */ import net.minecraft.launcher.CurrentLaunchFeatureMatcher;
/*     */ import net.minecraft.launcher.Launcher;
/*     */ import org.apache.commons.lang3.text.StrSubstitutor;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CompleteMinecraftVersion
/*     */   implements CompleteVersion
/*     */ {
/*  40 */   private static final Logger LOGGER = LogManager.getLogger();
/*     */   private String inheritsFrom;
/*     */   private String id;
/*     */   private Date time;
/*     */   private Date releaseTime;
/*     */   private ReleaseType type;
/*     */   private String minecraftArguments;
/*     */   private List<Library> libraries;
/*     */   private String mainClass;
/*     */   private int minimumLauncherVersion;
/*     */   private String incompatibilityReason;
/*     */   private String assets;
/*     */   private List<CompatibilityRule> compatibilityRules;
/*     */   private String jar;
/*     */   private CompleteMinecraftVersion savableVersion;
/*     */   private transient boolean synced = false;
/*  56 */   private Map<DownloadType, DownloadInfo> downloads = Maps.newEnumMap(DownloadType.class);
/*     */   
/*     */   private AssetIndexInfo assetIndex;
/*     */   
/*     */   private Map<ArgumentType, List<Argument>> arguments;
/*     */   
/*     */   public CompleteMinecraftVersion() {}
/*     */   
/*     */   public CompleteMinecraftVersion(CompleteMinecraftVersion version) {
/*  65 */     this.inheritsFrom = version.inheritsFrom;
/*  66 */     this.id = version.id;
/*  67 */     this.time = version.time;
/*  68 */     this.releaseTime = version.releaseTime;
/*  69 */     this.type = version.type;
/*  70 */     this.minecraftArguments = version.minecraftArguments;
/*  71 */     this.mainClass = version.mainClass;
/*  72 */     this.minimumLauncherVersion = version.minimumLauncherVersion;
/*  73 */     this.incompatibilityReason = version.incompatibilityReason;
/*  74 */     this.assets = version.assets;
/*  75 */     this.jar = version.jar;
/*  76 */     this.downloads = version.downloads;
/*     */     
/*  78 */     if (version.libraries != null) {
/*     */       
/*  80 */       this.libraries = Lists.newArrayList();
/*  81 */       for (Library library : version.getLibraries()) {
/*  82 */         this.libraries.add(new Library(library));
/*     */       }
/*     */     } 
/*  85 */     if (version.arguments != null) {
/*     */       
/*  87 */       this.arguments = Maps.newEnumMap(ArgumentType.class);
/*  88 */       for (Map.Entry<ArgumentType, List<Argument>> entry : version.arguments.entrySet()) {
/*  89 */         this.arguments.put(entry.getKey(), new ArrayList<>(entry.getValue()));
/*     */       }
/*     */     } 
/*  92 */     if (version.compatibilityRules != null) {
/*     */       
/*  94 */       this.compatibilityRules = Lists.newArrayList();
/*  95 */       for (CompatibilityRule compatibilityRule : version.compatibilityRules) {
/*  96 */         this.compatibilityRules.add(new CompatibilityRule(compatibilityRule));
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String getId() {
/* 103 */     return this.id;
/*     */   }
/*     */ 
/*     */   
/*     */   public ReleaseType getType() {
/* 108 */     return this.type;
/*     */   }
/*     */ 
/*     */   
/*     */   public Date getUpdatedTime() {
/* 113 */     return this.time;
/*     */   }
/*     */ 
/*     */   
/*     */   public Date getReleaseTime() {
/* 118 */     return this.releaseTime;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<Library> getLibraries() {
/* 123 */     return this.libraries;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getMainClass() {
/* 128 */     return this.mainClass;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getJar() {
/* 133 */     return (this.jar == null) ? this.id : this.jar;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setType(ReleaseType type) {
/* 138 */     if (type == null) {
/* 139 */       throw new IllegalArgumentException("Release type cannot be null");
/*     */     }
/* 141 */     this.type = type;
/*     */   }
/*     */ 
/*     */   
/*     */   public Collection<Library> getRelevantLibraries(CompatibilityRule.FeatureMatcher featureMatcher) {
/* 146 */     List<Library> result = new ArrayList<>();
/* 147 */     for (Library library : this.libraries) {
/* 148 */       if (library.appliesToCurrentEnvironment(featureMatcher)) {
/* 149 */         result.add(library);
/*     */       }
/*     */     } 
/* 152 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public Collection<File> getClassPath(OperatingSystem os, File base, CompatibilityRule.FeatureMatcher featureMatcher) {
/* 157 */     Collection<Library> libraries = getRelevantLibraries(featureMatcher);
/* 158 */     Collection<File> result = new ArrayList<>();
/* 159 */     for (Library library : libraries) {
/* 160 */       if (library.getNatives() == null) {
/* 161 */         result.add(new File(base, "libraries/" + library.getArtifactPath()));
/*     */       }
/*     */     } 
/* 164 */     result.add(new File(base, "versions/" + getJar() + "/" + getJar() + ".jar"));
/*     */     
/* 166 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<String> getRequiredFiles(OperatingSystem os) {
/* 171 */     Set<String> neededFiles = new HashSet<>();
/* 172 */     for (Library library : getRelevantLibraries(createFeatureMatcher())) {
/* 173 */       if (library.getNatives() != null) {
/*     */         
/* 175 */         String natives = library.getNatives().get(os);
/* 176 */         if (natives != null) {
/* 177 */           neededFiles.add("libraries/" + library.getArtifactPath(natives));
/*     */         }
/*     */         
/*     */         continue;
/*     */       } 
/* 182 */       neededFiles.add("libraries/" + library.getArtifactPath());
/*     */     } 
/*     */     
/* 185 */     return neededFiles;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<Downloadable> getRequiredDownloadables(OperatingSystem os, Proxy proxy, File targetDirectory, boolean ignoreLocalFiles) throws MalformedURLException {
/* 191 */     Set<Downloadable> neededFiles = new HashSet<>();
/* 192 */     for (Library library : getRelevantLibraries(createFeatureMatcher())) {
/*     */       
/* 194 */       String file = null;
/* 195 */       String classifier = null;
/* 196 */       if (library.getNatives() != null) {
/*     */         
/* 198 */         classifier = library.getNatives().get(os);
/* 199 */         if (classifier != null) {
/* 200 */           file = library.getArtifactPath(classifier);
/*     */         }
/*     */       }
/*     */       else {
/*     */         
/* 205 */         file = library.getArtifactPath();
/*     */       } 
/* 207 */       if (file != null) {
/*     */         
/* 209 */         File local = new File(targetDirectory, "libraries/" + file);
/* 210 */         Downloadable download = library.createDownload(proxy, file, local, ignoreLocalFiles, classifier);
/* 211 */         if (download != null) {
/* 212 */           neededFiles.add(download);
/*     */         }
/*     */       } 
/*     */     } 
/* 216 */     return neededFiles;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 221 */     return "CompleteVersion{id='" + this.id + '\'' + ", updatedTime=" + this.time + ", releasedTime=" + this.time + ", type=" + this.type + ", libraries=" + this.libraries + ", mainClass='" + this.mainClass + '\'' + ", jar='" + this.jar + '\'' + ", minimumLauncherVersion=" + this.minimumLauncherVersion + '}';
/*     */   }
/*     */ 
/*     */   
/*     */   public String getMinecraftArguments() {
/* 226 */     return this.minecraftArguments;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMinimumLauncherVersion() {
/* 231 */     return this.minimumLauncherVersion;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean appliesToCurrentEnvironment() {
/* 236 */     if (this.compatibilityRules == null) {
/* 237 */       return true;
/*     */     }
/* 239 */     CompatibilityRule.Action lastAction = CompatibilityRule.Action.DISALLOW;
/* 240 */     for (CompatibilityRule compatibilityRule : this.compatibilityRules) {
/*     */       
/* 242 */       CompatibilityRule.Action action = compatibilityRule.getAppliedAction((CompatibilityRule.FeatureMatcher)new CurrentLaunchFeatureMatcher(Launcher.getCurrentInstance().getProfileManager().getSelectedProfile(), this));
/* 243 */       if (action != null) {
/* 244 */         lastAction = action;
/*     */       }
/*     */     } 
/* 247 */     return (lastAction == CompatibilityRule.Action.ALLOW);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getIncompatibilityReason() {
/* 252 */     return this.incompatibilityReason;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSynced() {
/* 257 */     return this.synced;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setSynced(boolean synced) {
/* 262 */     this.synced = synced;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getInheritsFrom() {
/* 267 */     return this.inheritsFrom;
/*     */   }
/*     */ 
/*     */   
/*     */   public CompleteMinecraftVersion resolve(MinecraftVersionManager versionManager) throws IOException {
/* 272 */     Set<String> s = new HashSet<>();
/* 273 */     return resolve(versionManager, s);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected CompleteMinecraftVersion resolve(MinecraftVersionManager versionManager, Set<String> resolvedSoFar) throws IOException {
/* 279 */     if (this.inheritsFrom == null) {
/* 280 */       return this;
/*     */     }
/* 282 */     if (!resolvedSoFar.add(this.id)) {
/* 283 */       throw new IllegalStateException("Circular dependency detected");
/*     */     }
/* 285 */     VersionSyncInfo parentSync = versionManager.getVersionSyncInfo(this.inheritsFrom);
/* 286 */     CompleteMinecraftVersion parent = versionManager.getLatestCompleteVersion(parentSync).resolve(versionManager, resolvedSoFar);
/* 287 */     CompleteMinecraftVersion result = new CompleteMinecraftVersion(parent);
/* 288 */     if (!parentSync.isInstalled() || !parentSync.isUpToDate() || parentSync.getLatestSource() != VersionSyncInfo.VersionSource.LOCAL) {
/* 289 */       versionManager.installVersion(parent);
/*     */     }
/* 291 */     result.savableVersion = this;
/* 292 */     result.inheritsFrom = null;
/* 293 */     result.id = this.id;
/* 294 */     result.time = this.time;
/* 295 */     result.releaseTime = this.releaseTime;
/* 296 */     result.type = this.type;
/* 297 */     if (this.minecraftArguments != null) {
/* 298 */       result.minecraftArguments = this.minecraftArguments;
/*     */     }
/* 300 */     if (this.mainClass != null) {
/* 301 */       result.mainClass = this.mainClass;
/*     */     }
/* 303 */     if (this.incompatibilityReason != null) {
/* 304 */       result.incompatibilityReason = this.incompatibilityReason;
/*     */     }
/* 306 */     if (this.assets != null) {
/* 307 */       result.assets = this.assets;
/*     */     }
/* 309 */     if (this.jar != null) {
/* 310 */       result.jar = this.jar;
/*     */     }
/* 312 */     if (this.libraries != null) {
/*     */       
/* 314 */       List<Library> newLibraries = Lists.newArrayList();
/* 315 */       for (Library library : this.libraries) {
/* 316 */         newLibraries.add(new Library(library));
/*     */       }
/* 318 */       for (Library library : result.libraries) {
/* 319 */         newLibraries.add(library);
/*     */       }
/* 321 */       result.libraries = newLibraries;
/*     */     } 
/* 323 */     if (this.arguments != null) {
/*     */       
/* 325 */       if (result.arguments == null) {
/* 326 */         result.arguments = new EnumMap<>(ArgumentType.class);
/*     */       }
/* 328 */       for (Map.Entry<ArgumentType, List<Argument>> entry : this.arguments.entrySet()) {
/*     */         
/* 330 */         List<Argument> arguments = result.arguments.get(entry.getKey());
/* 331 */         if (arguments == null) {
/*     */           
/* 333 */           arguments = new ArrayList<>();
/* 334 */           result.arguments.put(entry.getKey(), arguments);
/*     */         } 
/* 336 */         arguments.addAll(entry.getValue());
/*     */       } 
/*     */     } 
/* 339 */     if (this.compatibilityRules != null) {
/* 340 */       for (CompatibilityRule compatibilityRule : this.compatibilityRules) {
/* 341 */         result.compatibilityRules.add(new CompatibilityRule(compatibilityRule));
/*     */       }
/*     */     }
/* 344 */     return result;
/*     */   }
/*     */   
/*     */   public CompleteMinecraftVersion getSavableVersion() {
/* 348 */     return (CompleteMinecraftVersion)Objects.firstNonNull(this.savableVersion, this);
/*     */   }
/*     */ 
/*     */   
/*     */   public AbstractDownloadInfo getDownloadURL(DownloadType type) {
/* 353 */     return this.downloads.get(type);
/*     */   }
/*     */ 
/*     */   
/*     */   public AssetIndexInfo getAssetIndex() {
/* 358 */     if (this.assetIndex == null) {
/* 359 */       this.assetIndex = new AssetIndexInfo((String)Objects.firstNonNull(this.assets, "legacy"));
/*     */     }
/* 361 */     return this.assetIndex;
/*     */   }
/*     */ 
/*     */   
/*     */   public CompatibilityRule.FeatureMatcher createFeatureMatcher() {
/* 366 */     return (CompatibilityRule.FeatureMatcher)new CurrentLaunchFeatureMatcher(Launcher.getCurrentInstance().getProfileManager().getSelectedProfile(), this);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addArguments(ArgumentType type, CompatibilityRule.FeatureMatcher featureMatcher, GameProcessBuilder builder, StrSubstitutor substitutor) {
/* 371 */     if (this.arguments != null) {
/*     */       
/* 373 */       List<Argument> args = this.arguments.get(type);
/* 374 */       if (args != null) {
/* 375 */         for (Argument argument : args) {
/* 376 */           argument.apply(builder, featureMatcher, substitutor);
/*     */         }
/*     */       }
/*     */     }
/* 380 */     else if (this.minecraftArguments != null) {
/*     */       
/* 382 */       if (type == ArgumentType.GAME) {
/*     */         
/* 384 */         for (String arg : this.minecraftArguments.split(" ")) {
/* 385 */           builder.withArguments(new String[] { substitutor.replace(arg) });
/*     */         } 
/* 387 */         if (featureMatcher.hasFeature("is_demo_user", Boolean.valueOf(true))) {
/* 388 */           builder.withArguments(new String[] { "--demo" });
/*     */         }
/* 390 */         if (featureMatcher.hasFeature("has_custom_resolution", Boolean.valueOf(true))) {
/* 391 */           builder.withArguments(new String[] { "--width", substitutor.replace("${resolution_width}"), "--height", substitutor.replace("${resolution_height}") });
/*     */         }
/*     */       }
/* 394 */       else if (type == ArgumentType.JVM) {
/*     */         
/* 396 */         if (OperatingSystem.getCurrentPlatform() == OperatingSystem.WINDOWS) {
/*     */           
/* 398 */           builder.withArguments(new String[] { "-XX:HeapDumpPath=MojangTricksIntelDriversForPerformance_javaw.exe_minecraft.exe.heapdump" });
/* 399 */           if (Launcher.getCurrentInstance().usesWinTenHack()) {
/* 400 */             builder.withArguments(new String[] { "-Dos.name=Windows 10", "-Dos.version=10.0" });
/*     */           }
/*     */         }
/* 403 */         else if (OperatingSystem.getCurrentPlatform() == OperatingSystem.OSX) {
/*     */           
/* 405 */           builder.withArguments(new String[] { substitutor.replace("-Xdock:icon=${asset=icons/minecraft.icns}"), "-Xdock:name=Minecraft" });
/*     */         } 
/* 407 */         builder.withArguments(new String[] { substitutor.replace("-Djava.library.path=${natives_directory}") });
/* 408 */         builder.withArguments(new String[] { substitutor.replace("-Dminecraft.launcher.brand=${launcher_name}") });
/* 409 */         builder.withArguments(new String[] { substitutor.replace("-Dminecraft.launcher.version=${launcher_version}") });
/* 410 */         builder.withArguments(new String[] { substitutor.replace("-Dminecraft.client.jar=${primary_jar}") });
/* 411 */         builder.withArguments(new String[] { "-cp", substitutor.replace("${classpath}") });
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\net\minecraft\launche\\updater\CompleteMinecraftVersion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */