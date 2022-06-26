/*     */ package net.minecraft.launcher.profile;
/*     */ 
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.mojang.launcher.updater.VersionFilter;
/*     */ import com.mojang.launcher.versions.ReleaseType;
/*     */ import com.mojang.launcher.versions.ReleaseTypeFactory;
/*     */ import java.io.File;
/*     */ import java.util.Set;
/*     */ import net.minecraft.launcher.game.MinecraftReleaseType;
/*     */ import net.minecraft.launcher.game.MinecraftReleaseTypeFactory;
/*     */ 
/*     */ public class Profile
/*     */   implements Comparable<Profile> {
/*     */   public static final String DEFAULT_JRE_ARGUMENTS_64BIT = "-Xmx1G -XX:+UseConcMarkSweepGC -XX:+CMSIncrementalMode -XX:-UseAdaptiveSizePolicy -Xmn128M";
/*     */   public static final String DEFAULT_JRE_ARGUMENTS_32BIT = "-Xmx512M -XX:+UseConcMarkSweepGC -XX:+CMSIncrementalMode -XX:-UseAdaptiveSizePolicy -Xmn128M";
/*  17 */   public static final Resolution DEFAULT_RESOLUTION = new Resolution(854, 480);
/*  18 */   public static final LauncherVisibilityRule DEFAULT_LAUNCHER_VISIBILITY = LauncherVisibilityRule.CLOSE_LAUNCHER;
/*  19 */   public static final Set<MinecraftReleaseType> DEFAULT_RELEASE_TYPES = Sets.newHashSet((Object[])new MinecraftReleaseType[] { MinecraftReleaseType.RELEASE });
/*     */   
/*     */   private String name;
/*     */   private File gameDir;
/*     */   private String lastVersionId;
/*     */   private String javaDir;
/*     */   private String javaArgs;
/*     */   private Resolution resolution;
/*     */   private Set<MinecraftReleaseType> allowedReleaseTypes;
/*     */   private String playerUUID;
/*     */   private Boolean useHopperCrashService;
/*     */   private LauncherVisibilityRule launcherVisibilityOnGameClose;
/*     */   
/*     */   public Profile() {}
/*     */   
/*     */   public Profile(Profile copy) {
/*  35 */     this.name = copy.name;
/*  36 */     this.gameDir = copy.gameDir;
/*  37 */     this.playerUUID = copy.playerUUID;
/*  38 */     this.lastVersionId = copy.lastVersionId;
/*  39 */     this.javaDir = copy.javaDir;
/*  40 */     this.javaArgs = copy.javaArgs;
/*  41 */     this.resolution = (copy.resolution == null) ? null : new Resolution(copy.resolution);
/*  42 */     this.allowedReleaseTypes = (copy.allowedReleaseTypes == null) ? null : Sets.newHashSet(copy.allowedReleaseTypes);
/*  43 */     this.useHopperCrashService = copy.useHopperCrashService;
/*  44 */     this.launcherVisibilityOnGameClose = copy.launcherVisibilityOnGameClose;
/*     */   }
/*     */ 
/*     */   
/*     */   public Profile(String name) {
/*  49 */     this.name = name;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getName() {
/*  54 */     return (String)Objects.firstNonNull(this.name, "");
/*     */   }
/*     */ 
/*     */   
/*     */   public void setName(String name) {
/*  59 */     this.name = name;
/*     */   }
/*     */ 
/*     */   
/*     */   public File getGameDir() {
/*  64 */     return this.gameDir;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setGameDir(File gameDir) {
/*  69 */     this.gameDir = gameDir;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setLastVersionId(String lastVersionId) {
/*  74 */     this.lastVersionId = lastVersionId;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setJavaDir(String javaDir) {
/*  79 */     this.javaDir = javaDir;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setJavaArgs(String javaArgs) {
/*  84 */     this.javaArgs = javaArgs;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getLastVersionId() {
/*  89 */     return this.lastVersionId;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getJavaArgs() {
/*  94 */     return this.javaArgs;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getJavaPath() {
/*  99 */     return this.javaDir;
/*     */   }
/*     */ 
/*     */   
/*     */   public Resolution getResolution() {
/* 104 */     return this.resolution;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setResolution(Resolution resolution) {
/* 109 */     this.resolution = resolution;
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public String getPlayerUUID() {
/* 115 */     return this.playerUUID;
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void setPlayerUUID(String playerUUID) {
/* 121 */     this.playerUUID = playerUUID;
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<MinecraftReleaseType> getAllowedReleaseTypes() {
/* 126 */     return this.allowedReleaseTypes;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setAllowedReleaseTypes(Set<MinecraftReleaseType> allowedReleaseTypes) {
/* 131 */     this.allowedReleaseTypes = allowedReleaseTypes;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean getUseHopperCrashService() {
/* 136 */     return (this.useHopperCrashService == null);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setUseHopperCrashService(boolean useHopperCrashService) {
/* 141 */     this.useHopperCrashService = useHopperCrashService ? null : Boolean.valueOf(false);
/*     */   }
/*     */ 
/*     */   
/*     */   public VersionFilter<MinecraftReleaseType> getVersionFilter() {
/* 146 */     VersionFilter<MinecraftReleaseType> filter = (new VersionFilter((ReleaseTypeFactory)MinecraftReleaseTypeFactory.instance())).setMaxCount(2147483647);
/*     */     
/* 148 */     if (this.allowedReleaseTypes == null) {
/* 149 */       filter.onlyForTypes((ReleaseType[])DEFAULT_RELEASE_TYPES.toArray((Object[])new MinecraftReleaseType[DEFAULT_RELEASE_TYPES.size()]));
/*     */     } else {
/* 151 */       filter.onlyForTypes((ReleaseType[])this.allowedReleaseTypes.toArray((Object[])new MinecraftReleaseType[this.allowedReleaseTypes.size()]));
/*     */     } 
/* 153 */     return filter;
/*     */   }
/*     */ 
/*     */   
/*     */   public LauncherVisibilityRule getLauncherVisibilityOnGameClose() {
/* 158 */     return this.launcherVisibilityOnGameClose;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setLauncherVisibilityOnGameClose(LauncherVisibilityRule launcherVisibilityOnGameClose) {
/* 163 */     this.launcherVisibilityOnGameClose = launcherVisibilityOnGameClose;
/*     */   }
/*     */ 
/*     */   
/*     */   public int compareTo(Profile o) {
/* 168 */     if (o == null) {
/* 169 */       return -1;
/*     */     }
/* 171 */     return getName().compareTo(o.getName());
/*     */   }
/*     */ 
/*     */   
/*     */   public static class Resolution
/*     */   {
/*     */     private int width;
/*     */     private int height;
/*     */     
/*     */     public Resolution() {}
/*     */     
/*     */     public Resolution(Resolution resolution) {
/* 183 */       this(resolution.getWidth(), resolution.getHeight());
/*     */     }
/*     */ 
/*     */     
/*     */     public Resolution(int width, int height) {
/* 188 */       this.width = width;
/* 189 */       this.height = height;
/*     */     }
/*     */ 
/*     */     
/*     */     public int getWidth() {
/* 194 */       return this.width;
/*     */     }
/*     */ 
/*     */     
/*     */     public int getHeight() {
/* 199 */       return this.height;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\net\minecraft\launcher\profile\Profile.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */