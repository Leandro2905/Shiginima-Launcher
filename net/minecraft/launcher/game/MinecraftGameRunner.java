/*     */ package net.minecraft.launcher.game;
/*     */ 
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.base.Predicate;
/*     */ import com.google.gson.Gson;
/*     */ import com.google.gson.GsonBuilder;
/*     */ import com.mojang.authlib.UserAuthentication;
/*     */ import com.mojang.authlib.UserType;
/*     */ import com.mojang.authlib.properties.PropertyMap;
/*     */ import com.mojang.launcher.Launcher;
/*     */ import com.mojang.launcher.LegacyPropertyMapSerializer;
/*     */ import com.mojang.launcher.OperatingSystem;
/*     */ import com.mojang.launcher.game.GameInstanceStatus;
/*     */ import com.mojang.launcher.game.process.GameProcess;
/*     */ import com.mojang.launcher.game.process.GameProcessBuilder;
/*     */ import com.mojang.launcher.game.process.GameProcessFactory;
/*     */ import com.mojang.launcher.game.process.GameProcessRunnable;
/*     */ import com.mojang.launcher.game.process.direct.DirectGameProcessFactory;
/*     */ import com.mojang.launcher.game.runner.AbstractGameRunner;
/*     */ import com.mojang.launcher.updater.DateTypeAdapter;
/*     */ import com.mojang.launcher.updater.VersionSyncInfo;
/*     */ import com.mojang.launcher.updater.download.Downloadable;
/*     */ import com.mojang.launcher.updater.download.assets.AssetIndex;
/*     */ import com.mojang.launcher.versions.CompleteVersion;
/*     */ import com.mojang.launcher.versions.ExtractRules;
/*     */ import com.mojang.util.UUIDTypeAdapter;
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.PasswordAuthentication;
/*     */ import java.net.Proxy;
/*     */ import java.util.Collection;
/*     */ import java.util.Date;
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.TreeSet;
/*     */ import java.util.UUID;
/*     */ import java.util.zip.ZipEntry;
/*     */ import java.util.zip.ZipFile;
/*     */ import net.minecraft.launcher.CompatibilityRule;
/*     */ import net.minecraft.launcher.CurrentLaunchFeatureMatcher;
/*     */ import net.minecraft.launcher.Launcher;
/*     */ import net.minecraft.launcher.LauncherConstants;
/*     */ import net.minecraft.launcher.profile.LauncherVisibilityRule;
/*     */ import net.minecraft.launcher.profile.Profile;
/*     */ import net.minecraft.launcher.updater.ArgumentType;
/*     */ import net.minecraft.launcher.updater.CompleteMinecraftVersion;
/*     */ import net.minecraft.launcher.updater.Library;
/*     */ import org.apache.commons.io.Charsets;
/*     */ import org.apache.commons.io.FileUtils;
/*     */ import org.apache.commons.io.filefilter.FileFilterUtils;
/*     */ import org.apache.commons.io.filefilter.IOFileFilter;
/*     */ import org.apache.commons.io.filefilter.TrueFileFilter;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.apache.commons.lang3.text.StrSubstitutor;
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
/*     */ public class MinecraftGameRunner
/*     */   extends AbstractGameRunner
/*     */   implements GameProcessRunnable
/*     */ {
/*     */   private static final String CRASH_IDENTIFIER_MAGIC = "#@!@#";
/*  82 */   private final Gson gson = new Gson();
/*  83 */   private final DateTypeAdapter dateAdapter = new DateTypeAdapter();
/*     */   private final Launcher minecraftLauncher;
/*     */   private final String[] additionalLaunchArgs;
/*  86 */   private final GameProcessFactory processFactory = (GameProcessFactory)new DirectGameProcessFactory();
/*     */   private File nativeDir;
/*  88 */   private LauncherVisibilityRule visibilityRule = LauncherVisibilityRule.CLOSE_LAUNCHER;
/*     */   private UserAuthentication auth;
/*     */   private Profile selectedProfile;
/*     */   
/*     */   public MinecraftGameRunner(Launcher minecraftLauncher, String[] additionalLaunchArgs) {
/*  93 */     this.minecraftLauncher = minecraftLauncher;
/*  94 */     this.additionalLaunchArgs = additionalLaunchArgs;
/*     */   }
/*     */   
/*     */   protected void setStatus(GameInstanceStatus status) {
/*  98 */     synchronized (this.lock) {
/*  99 */       if (this.nativeDir != null && status == GameInstanceStatus.IDLE) {
/* 100 */         LOGGER.info("Deleting " + this.nativeDir);
/* 101 */         if (!this.nativeDir.isDirectory() || FileUtils.deleteQuietly(this.nativeDir)) {
/* 102 */           this.nativeDir = null;
/*     */         } else {
/* 104 */           LOGGER.warn("Couldn't delete " + this.nativeDir + " - scheduling for deletion upon exit");
/*     */           try {
/* 106 */             FileUtils.forceDeleteOnExit(this.nativeDir);
/* 107 */           } catch (Throwable throwable) {}
/*     */         } 
/*     */       } 
/*     */       
/* 111 */       super.setStatus(status);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected Launcher getLauncher() {
/* 116 */     return this.minecraftLauncher.getLauncher();
/*     */   }
/*     */   
/*     */   protected void downloadRequiredFiles(VersionSyncInfo syncInfo) {
/* 120 */     migrateOldAssets();
/* 121 */     super.downloadRequiredFiles(syncInfo);
/*     */   }
/*     */   
/*     */   protected void launchGame() throws IOException {
/*     */     File assetsDir;
/* 126 */     LOGGER.info("Launching game");
/* 127 */     this.selectedProfile = this.minecraftLauncher.getProfileManager().getSelectedProfile();
/* 128 */     this.auth = this.minecraftLauncher.getProfileManager().getAuthDatabase().getByUUID(this.minecraftLauncher.getProfileManager().getSelectedUser());
/* 129 */     if (getVersion() == null) {
/* 130 */       LOGGER.error("Aborting launch; version is null?");
/*     */       return;
/*     */     } 
/* 133 */     this.nativeDir = new File(getLauncher().getWorkingDirectory(), "versions/" + getVersion().getId() + "/" + getVersion().getId() + "-natives-" + System.nanoTime());
/* 134 */     if (!this.nativeDir.isDirectory()) {
/* 135 */       this.nativeDir.mkdirs();
/*     */     }
/* 137 */     LOGGER.info("Unpacking natives to " + this.nativeDir);
/*     */     try {
/* 139 */       unpackNatives(this.nativeDir);
/* 140 */     } catch (IOException e) {
/* 141 */       LOGGER.error("Couldn't unpack natives 1!", e);
/*     */       
/*     */       return;
/*     */     } 
/*     */     try {
/* 146 */       assetsDir = reconstructAssets();
/* 147 */     } catch (IOException e) {
/* 148 */       LOGGER.error("Couldn't unpack natives 2!", e);
/*     */       return;
/*     */     } 
/* 151 */     File gameDirectory = (this.selectedProfile.getGameDir() == null) ? getLauncher().getWorkingDirectory() : this.selectedProfile.getGameDir();
/* 152 */     LOGGER.info("Launching in " + gameDirectory);
/* 153 */     if (!gameDirectory.exists()) {
/* 154 */       if (!gameDirectory.mkdirs()) {
/* 155 */         LOGGER.error("Aborting launch; couldn't create game directory");
/*     */       }
/* 157 */     } else if (!gameDirectory.isDirectory()) {
/* 158 */       LOGGER.error("Aborting launch; game directory is not actually a directory");
/*     */       return;
/*     */     } 
/* 161 */     File serverResourcePacksDir = new File(gameDirectory, "server-resource-packs");
/* 162 */     if (!serverResourcePacksDir.exists()) {
/* 163 */       serverResourcePacksDir.mkdirs();
/*     */     }
/* 165 */     GameProcessBuilder processBuilder = new GameProcessBuilder((String)Objects.firstNonNull(this.selectedProfile.getJavaPath(), OperatingSystem.getCurrentPlatform().getJavaDir()));
/* 166 */     processBuilder.withSysOutFilter(new Predicate()
/*     */         {
/*     */           public boolean apply(Object input) {
/* 169 */             return ((String)input).contains("#@!@#");
/*     */           }
/*     */         });
/* 172 */     processBuilder.directory(gameDirectory);
/* 173 */     processBuilder.withLogProcessor(this.minecraftLauncher.getUserInterface().showGameOutputTab(this));
/*     */     
/* 175 */     String profileArgs = this.selectedProfile.getJavaArgs();
/* 176 */     if (profileArgs != null) {
/* 177 */       processBuilder.withArguments(profileArgs.split(" "));
/*     */     } else {
/* 179 */       boolean is32Bit = "32".equals(System.getProperty("sun.arch.data.model"));
/* 180 */       String defaultArgument = is32Bit ? "-Xmx512M -XX:+UseConcMarkSweepGC -XX:+CMSIncrementalMode -XX:-UseAdaptiveSizePolicy -Xmn128M" : "-Xmx1G -XX:+UseConcMarkSweepGC -XX:+CMSIncrementalMode -XX:-UseAdaptiveSizePolicy -Xmn128M";
/* 181 */       processBuilder.withArguments(defaultArgument.split(" "));
/*     */     } 
/* 183 */     CompatibilityRule.FeatureMatcher featureMatcher = createFeatureMatcher();
/* 184 */     StrSubstitutor argumentsSubstitutor = createArgumentsSubstitutor(getVersion(), this.selectedProfile, gameDirectory, assetsDir, this.auth);
/*     */     
/* 186 */     getVersion().addArguments(ArgumentType.JVM, featureMatcher, processBuilder, argumentsSubstitutor);
/* 187 */     processBuilder.withArguments(new String[] { getVersion().getMainClass() });
/*     */     
/* 189 */     LOGGER.info("Half command: " + StringUtils.join(processBuilder.getFullCommands(), " "));
/*     */     
/* 191 */     getVersion().addArguments(ArgumentType.GAME, featureMatcher, processBuilder, argumentsSubstitutor);
/*     */     
/* 193 */     Proxy proxy = getLauncher().getProxy();
/* 194 */     PasswordAuthentication proxyAuth = getLauncher().getProxyAuth();
/* 195 */     if (!proxy.equals(Proxy.NO_PROXY)) {
/* 196 */       InetSocketAddress address = (InetSocketAddress)proxy.address();
/* 197 */       processBuilder.withArguments(new String[] { "--proxyHost", address.getHostName() });
/* 198 */       processBuilder.withArguments(new String[] { "--proxyPort", Integer.toString(address.getPort()) });
/* 199 */       if (proxyAuth != null) {
/* 200 */         processBuilder.withArguments(new String[] { "--proxyUser", proxyAuth.getUserName() });
/* 201 */         processBuilder.withArguments(new String[] { "--proxyPass", new String(proxyAuth.getPassword()) });
/*     */       } 
/*     */     } 
/* 204 */     processBuilder.withArguments(this.additionalLaunchArgs);
/*     */     try {
/* 206 */       LOGGER.debug("Running " + StringUtils.join(processBuilder.getFullCommands(), " "));
/* 207 */       GameProcess process = this.processFactory.startGame(processBuilder);
/* 208 */       process.setExitRunnable(this);
/*     */       
/* 210 */       setStatus(GameInstanceStatus.PLAYING);
/* 211 */       if (this.visibilityRule != LauncherVisibilityRule.DO_NOTHING) {
/* 212 */         this.minecraftLauncher.getUserInterface().setVisible(false);
/*     */       }
/* 214 */     } catch (IOException e) {
/* 215 */       LOGGER.error("Couldn't launch game", e);
/* 216 */       setStatus(GameInstanceStatus.IDLE);
/*     */       return;
/*     */     } 
/* 219 */     this.minecraftLauncher.performCleanups();
/*     */   }
/*     */   
/*     */   protected CompleteMinecraftVersion getVersion() {
/* 223 */     return (CompleteMinecraftVersion)this.version;
/*     */   }
/*     */ 
/*     */   
/*     */   private AssetIndex getAssetIndex() throws IOException {
/* 228 */     String assetVersion = getVersion().getAssetIndex().getId();
/* 229 */     File indexFile = new File(new File(getAssetsDir(), "indexes"), assetVersion + ".json");
/* 230 */     return (AssetIndex)this.gson.fromJson(FileUtils.readFileToString(indexFile, Charsets.UTF_8), AssetIndex.class);
/*     */   }
/*     */   
/*     */   private File getAssetsDir() {
/* 234 */     return new File(getLauncher().getWorkingDirectory(), "assets");
/*     */   }
/*     */ 
/*     */   
/*     */   private File reconstructAssets() throws IOException {
/* 239 */     File assetsDir = getAssetsDir();
/* 240 */     File indexDir = new File(assetsDir, "indexes");
/* 241 */     File objectDir = new File(assetsDir, "objects");
/* 242 */     String assetVersion = getVersion().getAssetIndex().getId();
/* 243 */     File indexFile = new File(indexDir, assetVersion + ".json");
/* 244 */     File virtualRoot = new File(new File(assetsDir, "virtual"), assetVersion);
/* 245 */     if (!indexFile.isFile()) {
/* 246 */       LOGGER.warn("No assets index file " + virtualRoot + "; can't reconstruct assets");
/* 247 */       return virtualRoot;
/*     */     } 
/* 249 */     AssetIndex index = (AssetIndex)this.gson.fromJson(FileUtils.readFileToString(indexFile, Charsets.UTF_8), AssetIndex.class);
/* 250 */     if (index.isVirtual()) {
/* 251 */       LOGGER.info("Reconstructing virtual assets folder at " + virtualRoot);
/* 252 */       for (Map.Entry<String, AssetIndex.AssetObject> entry : (Iterable<Map.Entry<String, AssetIndex.AssetObject>>)index.getFileMap().entrySet()) {
/* 253 */         File target = new File(virtualRoot, entry.getKey());
/* 254 */         File original = new File(new File(objectDir, ((AssetIndex.AssetObject)entry.getValue()).getHash().substring(0, 2)), ((AssetIndex.AssetObject)entry.getValue()).getHash());
/* 255 */         if (!target.isFile()) {
/* 256 */           FileUtils.copyFile(original, target, false);
/*     */         }
/*     */       } 
/* 259 */       FileUtils.writeStringToFile(new File(virtualRoot, ".lastused"), this.dateAdapter.serializeToString(new Date()));
/*     */     } 
/* 261 */     return virtualRoot;
/*     */   }
/*     */   
/*     */   public StrSubstitutor createArgumentsSubstitutor(CompleteMinecraftVersion version, Profile selectedProfile, File gameDirectory, File assetsDirectory, UserAuthentication authentication) {
/* 265 */     Map<String, String> map = new HashMap<>();
/*     */     
/* 267 */     map.put("auth_access_token", authentication.getAuthenticatedToken());
/* 268 */     map.put("user_properties", (new GsonBuilder()).registerTypeAdapter(PropertyMap.class, new LegacyPropertyMapSerializer()).create().toJson(authentication.getUserProperties()));
/* 269 */     map.put("user_property_map", (new GsonBuilder()).registerTypeAdapter(PropertyMap.class, new PropertyMap.Serializer()).create().toJson(authentication.getUserProperties()));
/* 270 */     if (authentication.isLoggedIn() && authentication.canPlayOnline()) {
/* 271 */       if (authentication instanceof com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication) {
/* 272 */         map.put("auth_session", String.format("token:%s:%s", new Object[] { authentication.getAuthenticatedToken(), UUIDTypeAdapter.fromUUID(authentication.getSelectedProfile().getId()) }));
/*     */       } else {
/* 274 */         map.put("auth_session", authentication.getAuthenticatedToken());
/*     */       } 
/*     */     } else {
/* 277 */       map.put("auth_session", "-");
/*     */     } 
/* 279 */     if (authentication.getSelectedProfile() != null) {
/* 280 */       map.put("auth_player_name", authentication.getSelectedProfile().getName());
/* 281 */       map.put("auth_uuid", UUIDTypeAdapter.fromUUID(authentication.getSelectedProfile().getId()));
/* 282 */       map.put("user_type", authentication.getUserType().getName());
/*     */     } else {
/* 284 */       map.put("auth_player_name", "Player");
/* 285 */       map.put("auth_uuid", (new UUID(0L, 0L)).toString());
/* 286 */       map.put("user_type", UserType.LEGACY.getName());
/*     */     } 
/* 288 */     map.put("profile_name", selectedProfile.getName());
/* 289 */     map.put("version_name", version.getId());
/*     */     
/* 291 */     map.put("game_directory", gameDirectory.getAbsolutePath());
/* 292 */     map.put("game_assets", assetsDirectory.getAbsolutePath());
/*     */     
/* 294 */     map.put("assets_root", getAssetsDir().getAbsolutePath());
/* 295 */     map.put("assets_index_name", getVersion().getAssetIndex().getId());
/*     */     
/* 297 */     map.put("version_type", getVersion().getType().getName());
/* 298 */     if (selectedProfile.getResolution() != null) {
/* 299 */       map.put("resolution_width", String.valueOf(selectedProfile.getResolution().getWidth()));
/* 300 */       map.put("resolution_height", String.valueOf(selectedProfile.getResolution().getHeight()));
/*     */     } else {
/* 302 */       map.put("resolution_width", "");
/* 303 */       map.put("resolution_height", "");
/*     */     } 
/* 305 */     map.put("language", "en-us");
/*     */     try {
/* 307 */       AssetIndex assetIndex = getAssetIndex();
/* 308 */       for (Map.Entry<String, AssetIndex.AssetObject> entry : (Iterable<Map.Entry<String, AssetIndex.AssetObject>>)assetIndex.getFileMap().entrySet()) {
/* 309 */         String hash = ((AssetIndex.AssetObject)entry.getValue()).getHash();
/* 310 */         String path = (new File(new File(getAssetsDir(), "objects"), hash.substring(0, 2) + "/" + hash)).getAbsolutePath();
/* 311 */         map.put("asset=" + (String)entry.getKey(), path);
/*     */       } 
/* 313 */     } catch (IOException iOException) {}
/*     */     
/* 315 */     map.put("launcher_name", "java-minecraft-launcher");
/* 316 */     map.put("launcher_version", LauncherConstants.getVersionName());
/* 317 */     map.put("natives_directory", this.nativeDir.getAbsolutePath());
/* 318 */     map.put("classpath", constructClassPath(getVersion()));
/* 319 */     map.put("classpath_separator", System.getProperty("path.separator"));
/* 320 */     map.put("primary_jar", (new File(getLauncher().getWorkingDirectory(), "versions/" + getVersion().getJar() + "/" + getVersion().getJar() + ".jar")).getAbsolutePath());
/*     */     
/* 322 */     return new StrSubstitutor(map);
/*     */   }
/*     */   
/*     */   private void migrateOldAssets() {
/* 326 */     File sourceDir = getAssetsDir();
/* 327 */     File objectsDir = new File(sourceDir, "objects");
/* 328 */     if (!sourceDir.isDirectory()) {
/*     */       return;
/*     */     }
/* 331 */     IOFileFilter migratableFilter = FileFilterUtils.notFileFilter(FileFilterUtils.or(new IOFileFilter[] { FileFilterUtils.nameFileFilter("indexes"), FileFilterUtils.nameFileFilter("objects"), FileFilterUtils.nameFileFilter("virtual"), FileFilterUtils.nameFileFilter("skins") }));
/* 332 */     for (File file : new TreeSet(FileUtils.listFiles(sourceDir, TrueFileFilter.TRUE, migratableFilter))) {
/* 333 */       String hash = Downloadable.getDigest(file, "SHA-1", 40);
/* 334 */       File destinationFile = new File(objectsDir, hash.substring(0, 2) + "/" + hash);
/* 335 */       if (!destinationFile.exists()) {
/* 336 */         LOGGER.info("Migrated old asset {} into {}", new Object[] { file, destinationFile });
/*     */         try {
/* 338 */           FileUtils.copyFile(file, destinationFile);
/* 339 */         } catch (IOException e) {
/* 340 */           LOGGER.error("Couldn't migrate old asset", e);
/*     */         } 
/*     */       } 
/* 343 */       FileUtils.deleteQuietly(file);
/*     */     } 
/* 345 */     File[] assets = sourceDir.listFiles();
/* 346 */     if (assets != null) {
/* 347 */       for (File file : assets) {
/* 348 */         if (!file.getName().equals("indexes") && !file.getName().equals("objects") && !file.getName().equals("virtual") && !file.getName().equals("skins")) {
/* 349 */           LOGGER.info("Cleaning up old assets directory {} after migration", new Object[] { file });
/* 350 */           FileUtils.deleteQuietly(file);
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private void unpackNatives(File targetDir) throws IOException {
/* 358 */     OperatingSystem os = OperatingSystem.getCurrentPlatform();
/* 359 */     Collection<Library> libraries = getVersion().getRelevantLibraries(createFeatureMatcher());
/* 360 */     for (Library library : libraries) {
/* 361 */       Map<OperatingSystem, String> nativesPerOs = library.getNatives();
/* 362 */       if (nativesPerOs != null && nativesPerOs.get(os) != null) {
/*     */         
/* 364 */         File file = new File(getLauncher().getWorkingDirectory(), "libraries/" + library.getArtifactPath(nativesPerOs.get(os)));
/* 365 */         ZipFile zip = new ZipFile(file);
/* 366 */         ExtractRules extractRules = library.getExtractRules();
/*     */         try {
/* 368 */           Enumeration<? extends ZipEntry> entries = zip.entries();
/* 369 */           while (entries.hasMoreElements()) {
/* 370 */             ZipEntry entry = entries.nextElement();
/* 371 */             if (extractRules == null || extractRules.shouldExtract(entry.getName())) {
/* 372 */               File targetFile = new File(targetDir, entry.getName());
/* 373 */               if (targetFile.getParentFile() != null) {
/* 374 */                 targetFile.getParentFile().mkdirs();
/*     */               }
/* 376 */               if (!entry.isDirectory()) {
/* 377 */                 BufferedInputStream inputStream = new BufferedInputStream(zip.getInputStream(entry));
/*     */                 
/* 379 */                 byte[] buffer = new byte[2048];
/* 380 */                 FileOutputStream outputStream = new FileOutputStream(targetFile);
/* 381 */                 BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
/*     */                 try {
/*     */                   int length;
/* 384 */                   while ((length = inputStream.read(buffer, 0, buffer.length)) != -1) {
/* 385 */                     bufferedOutputStream.write(buffer, 0, length);
/*     */                   }
/*     */                 } finally {
/* 388 */                   Downloadable.closeSilently(bufferedOutputStream);
/* 389 */                   Downloadable.closeSilently(outputStream);
/* 390 */                   Downloadable.closeSilently(inputStream);
/*     */                 } 
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         } finally {
/* 396 */           zip.close();
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private CompatibilityRule.FeatureMatcher createFeatureMatcher() {
/* 403 */     return (CompatibilityRule.FeatureMatcher)new CurrentLaunchFeatureMatcher(this.selectedProfile, getVersion());
/*     */   }
/*     */   
/*     */   private String constructClassPath(CompleteMinecraftVersion version) {
/* 407 */     StringBuilder result = new StringBuilder();
/* 408 */     Collection<File> classPath = version.getClassPath(OperatingSystem.getCurrentPlatform(), getLauncher().getWorkingDirectory(), createFeatureMatcher());
/* 409 */     String separator = System.getProperty("path.separator");
/* 410 */     for (File file : classPath) {
/* 411 */       if (!file.isFile()) {
/* 412 */         throw new RuntimeException("Classpath file not found: " + file);
/*     */       }
/* 414 */       if (result.length() > 0) {
/* 415 */         result.append(separator);
/*     */       }
/* 417 */       result.append(file.getAbsolutePath());
/*     */     } 
/* 419 */     return result.toString();
/*     */   }
/*     */   
/*     */   public void onGameProcessEnded(GameProcess process) {
/* 423 */     int exitCode = process.getExitCode();
/* 424 */     if (exitCode == 0) {
/* 425 */       LOGGER.info("Game ended with no troubles detected (exit code " + exitCode + ")");
/* 426 */       if (this.visibilityRule == LauncherVisibilityRule.CLOSE_LAUNCHER) {
/* 427 */         LOGGER.info("Following visibility rule and exiting launcher as the game has ended");
/* 428 */         getLauncher().shutdownLauncher();
/* 429 */       } else if (this.visibilityRule == LauncherVisibilityRule.HIDE_LAUNCHER) {
/* 430 */         LOGGER.info("Following visibility rule and showing launcher as the game has ended");
/* 431 */         this.minecraftLauncher.getUserInterface().setVisible(true);
/*     */       } 
/*     */     } else {
/* 434 */       LOGGER.error("Game ended with bad state (exit code " + exitCode + ")");
/* 435 */       LOGGER.info("Ignoring visibility rule and showing launcher due to a game crash");
/* 436 */       this.minecraftLauncher.getUserInterface().setVisible(true);
/*     */       
/* 438 */       String errorText = null;
/* 439 */       Collection<String> sysOutLines = process.getSysOutLines();
/* 440 */       String[] sysOut = sysOutLines.<String>toArray(new String[sysOutLines.size()]);
/* 441 */       for (int i = sysOut.length - 1; i >= 0; i--) {
/* 442 */         String line = sysOut[i];
/* 443 */         int pos = line.lastIndexOf("#@!@#");
/* 444 */         if (pos >= 0 && pos < line.length() - "#@!@#".length() - 1) {
/* 445 */           errorText = line.substring(pos + "#@!@#".length()).trim();
/*     */           break;
/*     */         } 
/*     */       } 
/* 449 */       if (errorText != null) {
/* 450 */         File file = new File(errorText);
/* 451 */         if (file.isFile()) {
/* 452 */           LOGGER.info("Crash report detected, opening: " + errorText);
/* 453 */           InputStream inputStream = null;
/*     */           try {
/* 455 */             inputStream = new FileInputStream(file);
/* 456 */             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
/* 457 */             StringBuilder result = new StringBuilder();
/*     */             String line;
/* 459 */             while ((line = reader.readLine()) != null) {
/* 460 */               if (result.length() > 0) {
/* 461 */                 result.append("\n");
/*     */               }
/* 463 */               result.append(line);
/*     */             } 
/* 465 */             reader.close();
/*     */             
/* 467 */             this.minecraftLauncher.getUserInterface().showCrashReport((CompleteVersion)getVersion(), file, result.toString());
/* 468 */           } catch (IOException e) {
/* 469 */             LOGGER.error("Couldn't open crash report", e);
/*     */           } finally {
/* 471 */             Downloadable.closeSilently(inputStream);
/*     */           } 
/*     */         } else {
/* 474 */           LOGGER.error("Crash report detected, but unknown format: " + errorText);
/*     */         } 
/*     */       } 
/*     */     } 
/* 478 */     setStatus(GameInstanceStatus.IDLE);
/*     */   }
/*     */   
/*     */   public void setVisibility(LauncherVisibilityRule visibility) {
/* 482 */     this.visibilityRule = visibility;
/*     */   }
/*     */   
/*     */   public UserAuthentication getAuth() {
/* 486 */     return this.auth;
/*     */   }
/*     */   
/*     */   public Profile getSelectedProfile() {
/* 490 */     return this.selectedProfile;
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\net\minecraft\launcher\game\MinecraftGameRunner.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */