/*     */ package net.minecraft.launcher;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.google.gson.Gson;
/*     */ import com.mojang.authlib.Agent;
/*     */ import com.mojang.authlib.UserAuthentication;
/*     */ import com.mojang.authlib.exceptions.AuthenticationException;
/*     */ import com.mojang.launcher.OperatingSystem;
/*     */ import com.mojang.launcher.updater.DateTypeAdapter;
/*     */ import com.mojang.launcher.updater.VersionManager;
/*     */ import com.mojang.launcher.updater.VersionSyncInfo;
/*     */ import com.mojang.launcher.updater.download.assets.AssetIndex;
/*     */ import com.mojang.launcher.versions.CompleteVersion;
/*     */ import com.mojang.launcher.versions.ReleaseTypeFactory;
/*     */ import com.mojang.launcher.versions.Version;
/*     */ import java.io.File;
/*     */ import java.io.FileFilter;
/*     */ import java.io.IOException;
/*     */ import java.net.PasswordAuthentication;
/*     */ import java.net.Proxy;
/*     */ import java.net.URL;
/*     */ import java.text.DateFormat;
/*     */ import java.util.Calendar;
/*     */ import java.util.Collection;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Set;
/*     */ import java.util.UUID;
/*     */ import javax.swing.JFrame;
/*     */ import joptsimple.ArgumentAcceptingOptionSpec;
/*     */ import joptsimple.NonOptionArgumentSpec;
/*     */ import joptsimple.OptionException;
/*     */ import joptsimple.OptionParser;
/*     */ import joptsimple.OptionSet;
/*     */ import joptsimple.OptionSpec;
/*     */ import net.minecraft.launcher.game.GameLaunchDispatcher;
/*     */ import net.minecraft.launcher.game.MinecraftReleaseType;
/*     */ import net.minecraft.launcher.game.MinecraftReleaseTypeFactory;
/*     */ import net.minecraft.launcher.profile.Profile;
/*     */ import net.minecraft.launcher.profile.ProfileManager;
/*     */ import net.minecraft.launcher.updater.CompleteMinecraftVersion;
/*     */ import net.minecraft.launcher.updater.Library;
/*     */ import net.minecraft.launcher.updater.LocalVersionList;
/*     */ import net.minecraft.launcher.updater.MinecraftVersionManager;
/*     */ import net.minecraft.launcher.updater.RemoteVersionList;
/*     */ import net.minecraft.launcher.updater.VersionList;
/*     */ import org.apache.commons.io.Charsets;
/*     */ import org.apache.commons.io.FileUtils;
/*     */ import org.apache.commons.io.filefilter.AgeFileFilter;
/*     */ import org.apache.commons.io.filefilter.DirectoryFileFilter;
/*     */ import org.apache.commons.io.filefilter.FileFileFilter;
/*     */ import org.apache.commons.io.filefilter.FileFilterUtils;
/*     */ import org.apache.commons.io.filefilter.IOFileFilter;
/*     */ import org.apache.commons.io.filefilter.PrefixFileFilter;
/*     */ import org.apache.commons.io.filefilter.TrueFileFilter;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.core.LoggerContext;
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
/*     */ public class Launcher
/*     */ {
/*  73 */   private final Gson gson = new Gson();
/*     */   private boolean winTenHack = false;
/*  75 */   private UUID clientToken = UUID.randomUUID();
/*     */   
/*     */   static {
/*  78 */     Thread.currentThread().setContextClassLoader(Launcher.class.getClassLoader());
/*     */   }
/*     */   
/*  81 */   private static final Logger LOGGER = LogManager.getLogger();
/*     */   
/*     */   private static Launcher INSTANCE;
/*     */   private final com.mojang.launcher.Launcher launcher;
/*     */   private final Integer bootstrapVersion;
/*     */   private final MinecraftUserInterface userInterface;
/*     */   private final ProfileManager profileManager;
/*     */   private final GameLaunchDispatcher launchDispatcher;
/*     */   private String requestedUser;
/*     */   private final StorageManager usernameManager;
/*     */   private final OptionsManager optionsManager;
/*     */   
/*     */   public static Launcher getCurrentInstance() {
/*  94 */     return INSTANCE;
/*     */   }
/*     */   
/*     */   public Launcher(JFrame frame, File workingDirectory, Proxy proxy, PasswordAuthentication proxyAuth, String[] args) {
/*  98 */     this(frame, workingDirectory, proxy, proxyAuth, args, Integer.valueOf(0));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Launcher(JFrame frame, File workingDirectory, Proxy proxy, PasswordAuthentication proxyAuth, String[] args, Integer bootstrapVersion) {
/*     */     try {
/* 106 */       LoggerContext context = (LoggerContext)LogManager.getContext(false);
/* 107 */       URL url = Launcher.class.getResource("/log4j2.xml");
/* 108 */       context.setConfigLocation(url.toURI());
/* 109 */     } catch (Exception exception) {}
/*     */ 
/*     */ 
/*     */     
/* 113 */     INSTANCE = this;
/*     */     
/* 115 */     this.usernameManager = new StorageManager();
/* 116 */     this.usernameManager.load();
/* 117 */     this.optionsManager = new OptionsManager();
/*     */     
/* 119 */     setupErrorHandling();
/* 120 */     this.bootstrapVersion = bootstrapVersion;
/* 121 */     this.userInterface = selectUserInterface(frame);
/* 122 */     if (bootstrapVersion.intValue() < 4) {
/* 123 */       this.userInterface.showOutdatedNotice();
/* 124 */       System.exit(0);
/* 125 */       throw new Error("Outdated bootstrap");
/*     */     } 
/* 127 */     LOGGER.info(this.userInterface.getTitle() + " (through bootstrap " + bootstrapVersion + ") started on " + OperatingSystem.getCurrentPlatform().getName() + "...");
/* 128 */     LOGGER.info("Current time is " + DateFormat.getDateTimeInstance(2, 2, Locale.US).format(new Date()));
/* 129 */     if (!OperatingSystem.getCurrentPlatform().isSupported()) {
/* 130 */       LOGGER.fatal("This operating system is unknown or unsupported, we cannot guarantee that the game will launch successfully.");
/*     */     }
/* 132 */     LOGGER.info("System.getProperty('os.name') == '" + System.getProperty("os.name") + "'");
/* 133 */     LOGGER.info("System.getProperty('os.version') == '" + System.getProperty("os.version") + "'");
/* 134 */     LOGGER.info("System.getProperty('os.arch') == '" + System.getProperty("os.arch") + "'");
/* 135 */     LOGGER.info("System.getProperty('java.version') == '" + System.getProperty("java.version") + "'");
/* 136 */     LOGGER.info("System.getProperty('java.vendor') == '" + System.getProperty("java.vendor") + "'");
/* 137 */     LOGGER.info("System.getProperty('sun.arch.data.model') == '" + System.getProperty("sun.arch.data.model") + "'");
/* 138 */     LOGGER.info("proxy == " + proxy);
/*     */     
/* 140 */     this.launchDispatcher = new GameLaunchDispatcher(this, processArgs(args));
/* 141 */     this.launcher = new com.mojang.launcher.Launcher(this.userInterface, workingDirectory, proxy, proxyAuth, (VersionManager)new MinecraftVersionManager((VersionList)new LocalVersionList(workingDirectory), (VersionList)new RemoteVersionList(LauncherConstants.PROPERTIES.getVersionManifest(), proxy)), Agent.MINECRAFT, (ReleaseTypeFactory)MinecraftReleaseTypeFactory.instance(), 21);
/* 142 */     this.profileManager = new ProfileManager(this);
/* 143 */     ((SwingUserInterface)this.userInterface).initializeFrame();
/*     */     
/* 145 */     refreshVersionsAndProfiles();
/*     */   }
/*     */   
/*     */   public File findNativeLauncher() {
/* 149 */     String programData = System.getenv("ProgramData");
/* 150 */     if (programData == null) {
/* 151 */       programData = System.getenv("ALLUSERSPROFILE");
/*     */     }
/* 153 */     if (programData != null) {
/* 154 */       File shortcut = new File(programData, "Microsoft\\Windows\\Start Menu\\Programs\\Minecraft\\Minecraft.lnk");
/* 155 */       if (shortcut.isFile()) {
/* 156 */         return shortcut;
/*     */       }
/*     */     } 
/* 159 */     return null;
/*     */   }
/*     */   
/*     */   public void runNativeLauncher(File executable, String[] args) {
/* 163 */     ProcessBuilder pb = new ProcessBuilder(new String[] { "cmd", "/c", executable.getAbsolutePath() });
/*     */     try {
/* 165 */       pb.start();
/* 166 */     } catch (IOException e) {
/* 167 */       e.printStackTrace();
/*     */     } 
/* 169 */     System.exit(0);
/*     */   }
/*     */   
/*     */   private void setupErrorHandling() {
/* 173 */     Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
/*     */           public void uncaughtException(Thread t, Throwable e) {
/* 175 */             Launcher.LOGGER.fatal("Unhandled exception in thread " + t, e);
/*     */           }
/*     */         });
/*     */   }
/*     */   private String[] processArgs(String[] args) {
/*     */     OptionSet optionSet;
/* 181 */     OptionParser optionParser = new OptionParser();
/* 182 */     optionParser.allowsUnrecognizedOptions();
/*     */     
/* 184 */     ArgumentAcceptingOptionSpec argumentAcceptingOptionSpec = optionParser.accepts("user").withRequiredArg().ofType(String.class);
/* 185 */     NonOptionArgumentSpec nonOptionArgumentSpec = optionParser.nonOptions();
/*     */     
/*     */     try {
/* 188 */       optionSet = optionParser.parse(args);
/* 189 */     } catch (OptionException e) {
/* 190 */       return args;
/*     */     } 
/* 192 */     if (optionSet.has((OptionSpec)argumentAcceptingOptionSpec)) {
/* 193 */       this.requestedUser = (String)optionSet.valueOf((OptionSpec)argumentAcceptingOptionSpec);
/*     */     }
/* 195 */     List<String> remainingOptions = optionSet.valuesOf((OptionSpec)nonOptionArgumentSpec);
/* 196 */     return remainingOptions.<String>toArray(new String[remainingOptions.size()]);
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
/*     */ 
/*     */   
/*     */   public void refreshVersionsAndProfiles() {
/* 241 */     getLauncher().getVersionManager().getExecutorService().submit(new Runnable()
/*     */         {
/*     */           public void run()
/*     */           {
/*     */             try {
/* 246 */               Launcher.this.getLauncher().getVersionManager().refreshVersions();
/* 247 */             } catch (Throwable e) {
/* 248 */               Launcher.LOGGER.error("Unexpected exception refreshing version list", e);
/*     */             } 
/*     */             try {
/* 251 */               Launcher.this.profileManager.loadProfiles();
/* 252 */               Launcher.LOGGER.info("Loaded " + Launcher.this.profileManager.getProfiles().size() + " profile(s); selected '" + Launcher.this.profileManager.getSelectedProfile().getName() + "'");
/* 253 */             } catch (Throwable e) {
/* 254 */               Launcher.LOGGER.error("Unexpected exception refreshing profile list", e);
/*     */             } 
/* 256 */             Launcher.LOGGER.info("Starting ensureloggedin");
/* 257 */             Launcher.this.ensureLoggedIn();
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   private MinecraftUserInterface selectUserInterface(JFrame frame) {
/* 264 */     return new SwingUserInterface(this, frame);
/*     */   }
/*     */   
/*     */   public com.mojang.launcher.Launcher getLauncher() {
/* 268 */     return this.launcher;
/*     */   }
/*     */   
/*     */   public MinecraftUserInterface getUserInterface() {
/* 272 */     return this.userInterface;
/*     */   }
/*     */   
/*     */   public Integer getBootstrapVersion() {
/* 276 */     return this.bootstrapVersion;
/*     */   }
/*     */   
/*     */   public void ensureLoggedIn() {
/* 280 */     UserAuthentication auth = this.profileManager.getAuthDatabase().getByUUID(this.profileManager.getSelectedUser());
/*     */     
/* 282 */     System.out.println("AUTO LOGIN " + this.usernameManager.isAutoLogin());
/*     */     
/* 284 */     if (this.usernameManager.isAutoLogin().booleanValue()) {
/* 285 */       LOGGER.info("UA2");
/* 286 */       auth.setUsername(this.usernameManager.getAutoLoginName());
/* 287 */       if (auth.canLogIn()) {
/*     */         try {
/* 289 */           auth.logIn();
/*     */           try {
/* 291 */             this.profileManager.saveProfiles();
/* 292 */           } catch (IOException e) {
/* 293 */             LOGGER.error("Couldn't save profiles after refreshing auth!", e);
/*     */           } 
/* 295 */           this.profileManager.fireRefreshEvent();
/* 296 */         } catch (AuthenticationException e) {
/* 297 */           LOGGER.error("Exception whilst logging into profile", (Throwable)e);
/* 298 */           getUserInterface().showLoginPrompt();
/*     */         } 
/*     */       } else {
/* 301 */         getUserInterface().showLoginPrompt();
/*     */       } 
/*     */     } else {
/* 304 */       getUserInterface().showLoginPrompt();
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
/*     */   public UUID getClientToken() {
/* 343 */     return this.clientToken;
/*     */   }
/*     */   
/*     */   public void setClientToken(UUID clientToken) {
/* 347 */     this.clientToken = clientToken;
/*     */   }
/*     */ 
/*     */   
/*     */   public void cleanupOrphanedAssets() throws IOException {
/* 352 */     File assetsDir = new File(getLauncher().getWorkingDirectory(), "assets");
/* 353 */     File indexDir = new File(assetsDir, "indexes");
/* 354 */     File objectsDir = new File(assetsDir, "objects");
/* 355 */     Set<String> referencedObjects = Sets.newHashSet();
/* 356 */     if (!objectsDir.isDirectory()) {
/*     */       return;
/*     */     }
/* 359 */     for (VersionSyncInfo syncInfo : getLauncher().getVersionManager().getInstalledVersions()) {
/* 360 */       if (syncInfo.getLocalVersion() instanceof CompleteMinecraftVersion) {
/* 361 */         CompleteMinecraftVersion version = (CompleteMinecraftVersion)syncInfo.getLocalVersion();
/* 362 */         String assetVersion = version.getAssetIndex().getId();
/* 363 */         File indexFile = new File(indexDir, assetVersion + ".json");
/* 364 */         AssetIndex index = (AssetIndex)this.gson.fromJson(FileUtils.readFileToString(indexFile, Charsets.UTF_8), AssetIndex.class);
/* 365 */         for (AssetIndex.AssetObject object : index.getUniqueObjects().keySet()) {
/* 366 */           referencedObjects.add(object.getHash().toLowerCase());
/*     */         }
/*     */       } 
/*     */     } 
/* 370 */     File[] directories = objectsDir.listFiles((FileFilter)DirectoryFileFilter.DIRECTORY);
/* 371 */     if (directories != null) {
/* 372 */       for (File directory : directories) {
/* 373 */         File[] files = directory.listFiles((FileFilter)FileFileFilter.FILE);
/* 374 */         if (files != null) {
/* 375 */           for (File file : files) {
/* 376 */             if (!referencedObjects.contains(file.getName().toLowerCase())) {
/* 377 */               LOGGER.info("Cleaning up orphaned object {}", new Object[] { file.getName() });
/* 378 */               FileUtils.deleteQuietly(file);
/*     */             } 
/*     */           } 
/*     */         }
/*     */       } 
/*     */     }
/* 384 */     deleteEmptyDirectories(objectsDir);
/*     */   }
/*     */ 
/*     */   
/*     */   public void cleanupOrphanedLibraries() throws IOException {
/* 389 */     File librariesDir = new File(getLauncher().getWorkingDirectory(), "libraries");
/* 390 */     Set<File> referencedLibraries = Sets.newHashSet();
/* 391 */     if (!librariesDir.isDirectory()) {
/*     */       return;
/*     */     }
/* 394 */     for (VersionSyncInfo syncInfo : getLauncher().getVersionManager().getInstalledVersions()) {
/* 395 */       if (syncInfo.getLocalVersion() instanceof CompleteMinecraftVersion) {
/* 396 */         CompleteMinecraftVersion version = (CompleteMinecraftVersion)syncInfo.getLocalVersion();
/* 397 */         for (Library library : version.getRelevantLibraries(version.createFeatureMatcher())) {
/* 398 */           String file = null;
/* 399 */           if (library.getNatives() != null) {
/* 400 */             String natives = (String)library.getNatives().get(OperatingSystem.getCurrentPlatform());
/* 401 */             if (natives != null) {
/* 402 */               file = library.getArtifactPath(natives);
/*     */             }
/*     */           } else {
/* 405 */             file = library.getArtifactPath();
/*     */           } 
/* 407 */           if (file != null) {
/* 408 */             referencedLibraries.add(new File(librariesDir, file));
/* 409 */             referencedLibraries.add(new File(librariesDir, file + ".sha"));
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 414 */     Collection<File> libraries = FileUtils.listFiles(librariesDir, TrueFileFilter.TRUE, TrueFileFilter.TRUE);
/* 415 */     if (libraries != null) {
/* 416 */       for (File file : libraries) {
/* 417 */         if (!referencedLibraries.contains(file)) {
/* 418 */           LOGGER.info("Cleaning up orphaned library {}", new Object[] { file });
/* 419 */           FileUtils.deleteQuietly(file);
/*     */         } 
/*     */       } 
/*     */     }
/* 423 */     deleteEmptyDirectories(librariesDir);
/*     */   }
/*     */   
/*     */   public void cleanupOldSkins() {
/* 427 */     File assetsDir = new File(getLauncher().getWorkingDirectory(), "assets");
/* 428 */     File skinsDir = new File(assetsDir, "skins");
/* 429 */     if (!skinsDir.isDirectory()) {
/*     */       return;
/*     */     }
/* 432 */     Collection<File> files = FileUtils.listFiles(skinsDir, (IOFileFilter)new AgeFileFilter(System.currentTimeMillis() - 604800000L), TrueFileFilter.TRUE);
/* 433 */     if (files != null) {
/* 434 */       for (File file : files) {
/* 435 */         LOGGER.info("Cleaning up old skin {}", new Object[] { file.getName() });
/* 436 */         FileUtils.deleteQuietly(file);
/*     */       } 
/*     */     }
/* 439 */     deleteEmptyDirectories(skinsDir);
/*     */   }
/*     */ 
/*     */   
/*     */   public void cleanupOldVirtuals() throws IOException {
/* 444 */     File assetsDir = new File(getLauncher().getWorkingDirectory(), "assets");
/* 445 */     File virtualsDir = new File(assetsDir, "virtual");
/* 446 */     DateTypeAdapter dateAdapter = new DateTypeAdapter();
/* 447 */     Calendar calendar = Calendar.getInstance();
/* 448 */     calendar.add(5, -5);
/* 449 */     Date cutoff = calendar.getTime();
/* 450 */     if (!virtualsDir.isDirectory()) {
/*     */       return;
/*     */     }
/* 453 */     File[] directories = virtualsDir.listFiles((FileFilter)DirectoryFileFilter.DIRECTORY);
/* 454 */     if (directories != null) {
/* 455 */       for (File directory : directories) {
/* 456 */         File lastUsedFile = new File(directory, ".lastused");
/* 457 */         if (lastUsedFile.isFile()) {
/* 458 */           Date lastUsed = dateAdapter.deserializeToDate(FileUtils.readFileToString(lastUsedFile));
/* 459 */           if (cutoff.after(lastUsed)) {
/* 460 */             LOGGER.info("Cleaning up old virtual directory {}", new Object[] { directory });
/* 461 */             FileUtils.deleteQuietly(directory);
/*     */           } 
/*     */         } else {
/* 464 */           LOGGER.info("Cleaning up strange virtual directory {}", new Object[] { directory });
/* 465 */           FileUtils.deleteQuietly(directory);
/*     */         } 
/*     */       } 
/*     */     }
/* 469 */     deleteEmptyDirectories(virtualsDir);
/*     */   }
/*     */   
/*     */   public void cleanupOldNatives() {
/* 473 */     File root = new File(this.launcher.getWorkingDirectory(), "versions/");
/* 474 */     LOGGER.info("Looking for old natives & assets to clean up...");
/* 475 */     AgeFileFilter ageFileFilter = new AgeFileFilter(System.currentTimeMillis() - 3600000L);
/* 476 */     if (!root.isDirectory()) {
/*     */       return;
/*     */     }
/* 479 */     File[] versions = root.listFiles((FileFilter)DirectoryFileFilter.DIRECTORY);
/* 480 */     if (versions != null) {
/* 481 */       for (File version : versions) {
/* 482 */         File[] files = version.listFiles((FileFilter)FileFilterUtils.and(new IOFileFilter[] { (IOFileFilter)new PrefixFileFilter(version.getName() + "-natives-"), (IOFileFilter)ageFileFilter }));
/* 483 */         if (files != null) {
/* 484 */           for (File folder : files) {
/* 485 */             LOGGER.debug("Deleting " + folder);
/*     */             
/* 487 */             FileUtils.deleteQuietly(folder);
/*     */           } 
/*     */         }
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   public void cleanupOrphanedVersions() {
/* 495 */     LOGGER.info("Looking for orphaned versions to clean up...");
/* 496 */     Set<String> referencedVersions = Sets.newHashSet();
/* 497 */     for (Profile profile : getProfileManager().getProfiles().values()) {
/* 498 */       String lastVersionId = profile.getLastVersionId();
/* 499 */       VersionSyncInfo syncInfo = null;
/* 500 */       if (lastVersionId != null) {
/* 501 */         syncInfo = getLauncher().getVersionManager().getVersionSyncInfo(lastVersionId);
/*     */       }
/* 503 */       if (syncInfo == null || syncInfo.getLatestVersion() == null) {
/* 504 */         syncInfo = getLauncher().getVersionManager().getVersions(profile.getVersionFilter()).get(0);
/*     */       }
/* 506 */       if (syncInfo != null) {
/* 507 */         Version version = syncInfo.getLatestVersion();
/* 508 */         referencedVersions.add(version.getId());
/* 509 */         if (version instanceof CompleteMinecraftVersion) {
/* 510 */           CompleteMinecraftVersion completeMinecraftVersion = (CompleteMinecraftVersion)version;
/* 511 */           referencedVersions.add(completeMinecraftVersion.getInheritsFrom());
/* 512 */           referencedVersions.add(completeMinecraftVersion.getJar());
/*     */         } 
/*     */       } 
/*     */     } 
/* 516 */     Calendar calendar = Calendar.getInstance();
/* 517 */     calendar.add(5, -7);
/* 518 */     Date cutoff = calendar.getTime();
/* 519 */     for (VersionSyncInfo versionSyncInfo : getLauncher().getVersionManager().getInstalledVersions()) {
/* 520 */       if (versionSyncInfo.getLocalVersion() instanceof CompleteMinecraftVersion) {
/* 521 */         CompleteVersion version = (CompleteVersion)versionSyncInfo.getLocalVersion();
/* 522 */         if (!referencedVersions.contains(version.getId()) && version.getType() == MinecraftReleaseType.SNAPSHOT) {
/* 523 */           if (versionSyncInfo.isOnRemote()) {
/* 524 */             LOGGER.info("Deleting orphaned version {} because it's a snapshot available on remote", new Object[] { version.getId() });
/*     */             try {
/* 526 */               getLauncher().getVersionManager().uninstallVersion(version);
/* 527 */             } catch (IOException e) {
/* 528 */               LOGGER.warn("Couldn't uninstall version " + version.getId(), e);
/*     */             }  continue;
/* 530 */           }  if (version.getUpdatedTime().before(cutoff)) {
/* 531 */             LOGGER.info("Deleting orphaned version {} because it's an unsupported old snapshot", new Object[] { version.getId() });
/*     */             try {
/* 533 */               getLauncher().getVersionManager().uninstallVersion(version);
/* 534 */             } catch (IOException e) {
/* 535 */               LOGGER.warn("Couldn't uninstall version " + version.getId(), e);
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private static Collection<File> listEmptyDirectories(File directory) {
/* 544 */     List<File> result = Lists.newArrayList();
/* 545 */     File[] files = directory.listFiles();
/* 546 */     if (files != null) {
/* 547 */       for (File file : files) {
/* 548 */         if (file.isDirectory()) {
/* 549 */           File[] subFiles = file.listFiles();
/* 550 */           if (subFiles == null || subFiles.length == 0) {
/* 551 */             result.add(file);
/*     */           } else {
/* 553 */             result.addAll(listEmptyDirectories(file));
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }
/* 558 */     return result;
/*     */   }
/*     */   
/*     */   private static void deleteEmptyDirectories(File directory) {
/*     */     while (true) {
/* 563 */       Collection<File> files = listEmptyDirectories(directory);
/* 564 */       if (files.isEmpty()) {
/*     */         return;
/*     */       }
/* 567 */       for (File file : files) {
/* 568 */         if (FileUtils.deleteQuietly(file)) {
/* 569 */           LOGGER.info("Deleted empty directory {}", new Object[] { file });
/*     */           continue;
/*     */         } 
/*     */         return;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void performCleanups() throws IOException {
/* 579 */     cleanupOrphanedVersions();
/* 580 */     cleanupOrphanedAssets();
/* 581 */     cleanupOldSkins();
/* 582 */     cleanupOldNatives();
/* 583 */     cleanupOldVirtuals();
/*     */   }
/*     */   
/*     */   public ProfileManager getProfileManager() {
/* 587 */     return this.profileManager;
/*     */   }
/*     */   
/*     */   public GameLaunchDispatcher getLaunchDispatcher() {
/* 591 */     return this.launchDispatcher;
/*     */   }
/*     */   
/*     */   public boolean usesWinTenHack() {
/* 595 */     return this.winTenHack;
/*     */   }
/*     */   
/*     */   public void setWinTenHack() {
/* 599 */     this.winTenHack = true;
/*     */   }
/*     */   
/*     */   public static Launcher getInstance() {
/* 603 */     return INSTANCE;
/*     */   }
/*     */   
/*     */   public StorageManager getUsernameManager() {
/* 607 */     return this.usernameManager;
/*     */   }
/*     */   
/*     */   public OptionsManager getOptionsManager() {
/* 611 */     return this.optionsManager;
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\net\minecraft\launcher\Launcher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */