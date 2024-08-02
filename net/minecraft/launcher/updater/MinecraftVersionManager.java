/*     */ package net.minecraft.launcher.updater;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.gson.Gson;
/*     */ import com.mojang.launcher.OperatingSystem;
/*     */ import com.mojang.launcher.events.RefreshedVersionsListener;
/*     */ import com.mojang.launcher.updater.ExceptionalThreadPoolExecutor;
/*     */ import com.mojang.launcher.updater.VersionFilter;
/*     */ import com.mojang.launcher.updater.VersionManager;
/*     */ import com.mojang.launcher.updater.VersionSyncInfo;
/*     */ import com.mojang.launcher.updater.download.DownloadJob;
/*     */ import com.mojang.launcher.updater.download.Downloadable;
/*     */ import com.mojang.launcher.updater.download.EtagDownloadable;
/*     */ import com.mojang.launcher.updater.download.assets.AssetDownloadable;
/*     */ import com.mojang.launcher.updater.download.assets.AssetIndex;
/*     */ import com.mojang.launcher.versions.CompleteVersion;
/*     */ import com.mojang.launcher.versions.ReleaseType;
/*     */ import com.mojang.launcher.versions.Version;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.Proxy;
/*     */ import java.net.URL;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ThreadPoolExecutor;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import net.minecraft.launcher.game.MinecraftReleaseType;
/*     */ import org.apache.commons.io.FileUtils;
/*     */ import org.apache.commons.io.IOUtils;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MinecraftVersionManager
/*     */   implements VersionManager
/*     */ {
/*  50 */   private static final Logger LOGGER = LogManager.getLogger();
/*     */   private final VersionList localVersionList;
/*     */   private final VersionList remoteVersionList;
/*  53 */   private final ThreadPoolExecutor executorService = (ThreadPoolExecutor)new ExceptionalThreadPoolExecutor(4, 8, 30L, TimeUnit.SECONDS);
/*  54 */   private final List<RefreshedVersionsListener> refreshedVersionsListeners = Collections.synchronizedList(new ArrayList<>());
/*  55 */   private final Object refreshLock = new Object();
/*     */   private boolean isRefreshing;
/*  57 */   private final Gson gson = new Gson();
/*     */ 
/*     */   
/*     */   public MinecraftVersionManager(VersionList localVersionList, VersionList remoteVersionList) {
/*  61 */     this.localVersionList = localVersionList;
/*  62 */     this.remoteVersionList = remoteVersionList;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void refreshVersions() throws IOException {
/*  68 */     synchronized (this.refreshLock) {
/*     */       
/*  70 */       this.isRefreshing = true;
/*     */     } 
/*     */     
/*     */     try {
/*  74 */       LOGGER.info("Refreshing local version list...");
/*  75 */       this.localVersionList.refreshVersions();
/*  76 */       LOGGER.info("Refreshing remote version list...");
/*  77 */       this.remoteVersionList.refreshVersions();
/*     */     }
/*  79 */     catch (IOException ex) {
/*     */       
/*  81 */       synchronized (this.refreshLock) {
/*     */         
/*  83 */         this.isRefreshing = false;
/*     */       } 
/*  85 */       throw ex;
/*     */     } 
/*  87 */     LOGGER.info("Refresh complete.");
/*  88 */     synchronized (this.refreshLock) {
/*     */       
/*  90 */       this.isRefreshing = false;
/*     */     } 
/*  92 */     for (RefreshedVersionsListener listener : Lists.newArrayList(this.refreshedVersionsListeners)) {
/*  93 */       listener.onVersionsRefreshed(this);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public List<VersionSyncInfo> getVersions() {
/*  99 */     return getVersions(null);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<VersionSyncInfo> getVersions(VersionFilter<? extends ReleaseType> filter) {
/* 104 */     synchronized (this.refreshLock) {
/*     */       
/* 106 */       if (this.isRefreshing) {
/* 107 */         return new ArrayList<>();
/*     */       }
/*     */     } 
/* 110 */     List<VersionSyncInfo> result = new ArrayList<>();
/* 111 */     Object<Object, Object> lookup = (Object<Object, Object>)new HashMap<>();
/* 112 */     Map<MinecraftReleaseType, Integer> counts = Maps.newEnumMap(MinecraftReleaseType.class);
/* 113 */     for (MinecraftReleaseType type : MinecraftReleaseType.values()) {
/* 114 */       counts.put(type, Integer.valueOf(0));
/*     */     }
/* 116 */     for (Version version : Lists.newArrayList(this.localVersionList.getVersions())) {
/* 117 */       if (version.getType() != null && version.getUpdatedTime() != null) {
/*     */         
/* 119 */         MinecraftReleaseType type = (MinecraftReleaseType)version.getType();
/* 120 */         if (filter == null || (filter.getTypes().contains(type) && ((Integer)counts.get(type)).intValue() < filter.getMaxCount())) {
/*     */           
/* 122 */           VersionSyncInfo syncInfo = getVersionSyncInfo(version, this.remoteVersionList.getVersion(version.getId()));
/* 123 */           ((Map)lookup).put(version.getId(), syncInfo);
/* 124 */           result.add(syncInfo);
/*     */         } 
/*     */       } 
/*     */     } 
/* 128 */     for (Version version : this.remoteVersionList.getVersions()) {
/* 129 */       if (version.getType() != null && version.getUpdatedTime() != null) {
/*     */         
/* 131 */         MinecraftReleaseType type = (MinecraftReleaseType)version.getType();
/* 132 */         if (!((Map)lookup).containsKey(version.getId()) && (filter == null || (filter
/* 133 */           .getTypes().contains(type) && ((Integer)counts.get(type)).intValue() < filter.getMaxCount()))) {
/*     */           
/* 135 */           VersionSyncInfo syncInfo = getVersionSyncInfo(this.localVersionList.getVersion(version.getId()), version);
/* 136 */           ((Map)lookup).put(version.getId(), syncInfo);
/* 137 */           result.add(syncInfo);
/* 138 */           if (filter != null) {
/* 139 */             counts.put(type, Integer.valueOf(((Integer)counts.get(type)).intValue() + 1));
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/* 144 */     if (result.isEmpty()) {
/* 145 */       for (Version version : this.localVersionList.getVersions()) {
/* 146 */         if (version.getType() != null && version.getUpdatedTime() != null) {
/*     */           
/* 148 */           VersionSyncInfo syncInfo = getVersionSyncInfo(version, this.remoteVersionList.getVersion(version.getId()));
/* 149 */           ((Map)lookup).put(version.getId(), syncInfo);
/* 150 */           result.add(syncInfo);
/*     */         } 
/*     */       } 
/*     */     }
/* 154 */     Collections.sort(result, new Comparator<VersionSyncInfo>()
/*     */         {
/*     */           public int compare(Object aa, Object bb)
/*     */           {
/* 158 */             VersionSyncInfo a = (VersionSyncInfo)aa;
/* 159 */             VersionSyncInfo b = (VersionSyncInfo)bb;
/*     */             
/* 161 */             Version aVer = a.getLatestVersion();
/* 162 */             Version bVer = b.getLatestVersion();
/* 163 */             if (aVer.getReleaseTime() != null && bVer.getReleaseTime() != null) {
/* 164 */               return bVer.getReleaseTime().compareTo(aVer.getReleaseTime());
/*     */             }
/* 166 */             return bVer.getUpdatedTime().compareTo(aVer.getUpdatedTime());
/*     */           }
/*     */         });
/* 169 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public VersionSyncInfo getVersionSyncInfo(Version version) {
/* 174 */     return getVersionSyncInfo(version.getId());
/*     */   }
/*     */ 
/*     */   
/*     */   public VersionSyncInfo getVersionSyncInfo(String name) {
/* 179 */     return getVersionSyncInfo(this.localVersionList.getVersion(name), this.remoteVersionList.getVersion(name));
/*     */   }
/*     */ 
/*     */   
/*     */   public VersionSyncInfo getVersionSyncInfo(Version localVersion, Version remoteVersion) {
/* 184 */     boolean installed = (localVersion != null);
/* 185 */     boolean upToDate = installed;
/* 186 */     CompleteMinecraftVersion resolved = null;
/* 187 */     if (installed && remoteVersion != null) {
/* 188 */       upToDate = !remoteVersion.getUpdatedTime().after(localVersion.getUpdatedTime());
/*     */     }
/* 190 */     if (localVersion instanceof CompleteVersion) {
/*     */ 
/*     */       
/*     */       try {
/* 194 */         resolved = ((CompleteMinecraftVersion)localVersion).resolve(this);
/*     */       }
/* 196 */       catch (IOException ex) {
/*     */         
/* 198 */         LOGGER.error("Couldn't resolve version " + localVersion.getId(), ex);
/* 199 */         resolved = (CompleteMinecraftVersion)localVersion;
/*     */       } 
/* 201 */       upToDate &= this.localVersionList.hasAllFiles(resolved, OperatingSystem.getCurrentPlatform());
/*     */     } 
/* 203 */     return new VersionSyncInfo((Version)resolved, remoteVersion, installed, upToDate);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<VersionSyncInfo> getInstalledVersions() {
/* 208 */     List<VersionSyncInfo> result = new ArrayList<>();
/*     */     
/* 210 */     Collection<Version> versions = Lists.newArrayList(this.localVersionList.getVersions());
/* 211 */     for (Version version : versions) {
/* 212 */       if (version.getType() != null && version.getUpdatedTime() != null) {
/*     */         
/* 214 */         VersionSyncInfo syncInfo = getVersionSyncInfo(version, this.remoteVersionList.getVersion(version.getId()));
/* 215 */         result.add(syncInfo);
/*     */       } 
/*     */     } 
/* 218 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public VersionList getRemoteVersionList() {
/* 223 */     return this.remoteVersionList;
/*     */   }
/*     */ 
/*     */   
/*     */   public VersionList getLocalVersionList() {
/* 228 */     return this.localVersionList;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public CompleteMinecraftVersion getLatestCompleteVersion(VersionSyncInfo syncInfo) throws IOException {
/* 234 */     if (syncInfo.getLatestSource() == VersionSyncInfo.VersionSource.REMOTE) {
/*     */       
/* 236 */       CompleteMinecraftVersion result = null;
/* 237 */       IOException exception = null;
/*     */       
/*     */       try {
/* 240 */         result = this.remoteVersionList.getCompleteVersion(syncInfo.getLatestVersion());
/*     */       }
/* 242 */       catch (IOException e) {
/*     */         
/* 244 */         exception = e;
/*     */         
/*     */         try {
/* 247 */           result = this.localVersionList.getCompleteVersion(syncInfo.getLatestVersion());
/*     */         }
/* 249 */         catch (IOException iOException) {}
/*     */       } 
/* 251 */       if (result != null) {
/* 252 */         return result;
/*     */       }
/* 254 */       throw exception;
/*     */     } 
/* 256 */     return this.localVersionList.getCompleteVersion(syncInfo.getLatestVersion());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public DownloadJob downloadVersion(VersionSyncInfo syncInfo, DownloadJob job) throws IOException {
/* 262 */     if (!(this.localVersionList instanceof LocalVersionList)) {
/* 263 */       throw new IllegalArgumentException("Cannot download if local repo isn't a LocalVersionList");
/*     */     }
/* 265 */     if (!(this.remoteVersionList instanceof RemoteVersionList)) {
/* 266 */       throw new IllegalArgumentException("Cannot download if local repo isn't a RemoteVersionList");
/*     */     }
/* 268 */     CompleteMinecraftVersion version = getLatestCompleteVersion(syncInfo);
/* 269 */     File baseDirectory = ((LocalVersionList)this.localVersionList).getBaseDirectory();
/* 270 */     Proxy proxy = ((RemoteVersionList)this.remoteVersionList).getProxy();
/*     */     
/* 272 */     job.addDownloadables(version.getRequiredDownloadables(OperatingSystem.getCurrentPlatform(), proxy, baseDirectory, false));
/*     */     
/* 274 */     String jarFile = "versions/" + version.getJar() + "/" + version.getJar() + ".jar";
/* 275 */     AbstractDownloadInfo clientInfo = version.getDownloadURL(DownloadType.CLIENT);
/* 276 */     if (clientInfo == null) {
/* 277 */       job.addDownloadables(new Downloadable[] { (Downloadable)new EtagDownloadable(proxy, new URL("https://s3.amazonaws.com/Minecraft.Download/" + jarFile), new File(baseDirectory, jarFile), false) });
/*     */     } else {
/* 279 */       job.addDownloadables(new Downloadable[] { new PreHashedDownloadable(proxy, clientInfo.getUrl(), new File(baseDirectory, jarFile), false, clientInfo.getSha1()) });
/*     */     } 
/* 281 */     return job;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public DownloadJob downloadResources(DownloadJob job, CompleteVersion version) throws IOException {
/* 287 */     File baseDirectory = ((LocalVersionList)this.localVersionList).getBaseDirectory();
/*     */     
/* 289 */     job.addDownloadables(getResourceFiles(((RemoteVersionList)this.remoteVersionList).getProxy(), baseDirectory, (CompleteMinecraftVersion)version));
/*     */     
/* 291 */     return job;
/*     */   }
/*     */ 
/*     */   
/*     */   private Set<Downloadable> getResourceFiles(Proxy proxy, File baseDirectory, CompleteMinecraftVersion version) {
/* 296 */     Set<Downloadable> result = new HashSet<>();
/* 297 */     InputStream inputStream = null;
/* 298 */     File assets = new File(baseDirectory, "assets");
/* 299 */     File objectsFolder = new File(assets, "objects");
/* 300 */     File indexesFolder = new File(assets, "indexes");
/* 301 */     long start = System.nanoTime();
/*     */     
/* 303 */     AssetIndexInfo indexInfo = version.getAssetIndex();
/* 304 */     File indexFile = new File(indexesFolder, indexInfo.getId() + ".json");
/*     */     
/*     */     try {
/* 307 */       URL indexUrl = indexInfo.getUrl();
/* 308 */       inputStream = indexUrl.openConnection(proxy).getInputStream();
/* 309 */       String json = IOUtils.toString(inputStream);
/* 310 */       FileUtils.writeStringToFile(indexFile, json);
/* 311 */       AssetIndex index = (AssetIndex)this.gson.fromJson(json, AssetIndex.class);
/* 312 */       for (Map.Entry<AssetIndex.AssetObject, String> entry : (Iterable<Map.Entry<AssetIndex.AssetObject, String>>)index.getUniqueObjects().entrySet()) {
/*     */         
/* 314 */         AssetIndex.AssetObject object = entry.getKey();
/* 315 */         String filename = object.getHash().substring(0, 2) + "/" + object.getHash();
/* 316 */         File file = new File(objectsFolder, filename);
/* 317 */         if (!file.isFile() || FileUtils.sizeOf(file) != object.getSize()) {
/*     */           
/* 319 */           AssetDownloadable assetDownloadable = new AssetDownloadable(proxy, entry.getValue(), object, "https://resources.download.minecraft.net/", objectsFolder);
/* 320 */           assetDownloadable.setExpectedSize(object.getSize());
/* 321 */           result.add(assetDownloadable);
/*     */         } 
/*     */       } 
/* 324 */       long end = System.nanoTime();
/* 325 */       long delta = end - start;
/* 326 */       LOGGER.debug("Delta time to compare resources: " + (delta / 1000000L) + " ms ");
/*     */     }
/* 328 */     catch (Exception ex) {
/*     */       
/* 330 */       LOGGER.error("Couldn't download resources", ex);
/*     */     }
/*     */     finally {
/*     */       
/* 334 */       IOUtils.closeQuietly(inputStream);
/*     */     } 
/* 336 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public ThreadPoolExecutor getExecutorService() {
/* 341 */     return this.executorService;
/*     */   }
/*     */ 
/*     */   
/*     */   public void addRefreshedVersionsListener(RefreshedVersionsListener listener) {
/* 346 */     this.refreshedVersionsListeners.add(listener);
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeRefreshedVersionsListener(RefreshedVersionsListener listener) {
/* 351 */     this.refreshedVersionsListeners.remove(listener);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public VersionSyncInfo syncVersion(VersionSyncInfo syncInfo) throws IOException {
/* 357 */     CompleteVersion remoteVersion = getRemoteVersionList().getCompleteVersion(syncInfo.getRemoteVersion());
/* 358 */     getLocalVersionList().removeVersion(syncInfo.getLocalVersion());
/* 359 */     getLocalVersionList().addVersion(remoteVersion);
/* 360 */     ((LocalVersionList)getLocalVersionList()).saveVersion(((CompleteMinecraftVersion)remoteVersion).getSavableVersion());
/* 361 */     return getVersionSyncInfo((Version)remoteVersion);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void installVersion(CompleteVersion version) throws IOException {
/* 367 */     if (version instanceof CompleteMinecraftVersion) {
/* 368 */       version = ((CompleteMinecraftVersion)version).getSavableVersion();
/*     */     }
/* 370 */     VersionList localVersionList = getLocalVersionList();
/* 371 */     if (localVersionList.getVersion(version.getId()) != null) {
/* 372 */       localVersionList.removeVersion(version.getId());
/*     */     }
/* 374 */     localVersionList.addVersion(version);
/* 375 */     if (localVersionList instanceof LocalVersionList) {
/* 376 */       ((LocalVersionList)localVersionList).saveVersion(version);
/*     */     }
/* 378 */     LOGGER.info("Installed " + version);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void uninstallVersion(CompleteVersion version) throws IOException {
/* 384 */     VersionList localVersionList = getLocalVersionList();
/* 385 */     if (localVersionList instanceof LocalVersionList) {
/*     */       
/* 387 */       localVersionList.uninstallVersion((Version)version);
/* 388 */       LOGGER.info("Uninstalled " + version);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\net\minecraft\launche\\updater\MinecraftVersionManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
