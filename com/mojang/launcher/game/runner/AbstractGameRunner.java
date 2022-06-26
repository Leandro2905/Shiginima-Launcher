/*     */ package com.mojang.launcher.game.runner;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.launcher.Launcher;
/*     */ import com.mojang.launcher.game.GameInstanceStatus;
/*     */ import com.mojang.launcher.updater.DownloadProgress;
/*     */ import com.mojang.launcher.updater.VersionSyncInfo;
/*     */ import com.mojang.launcher.updater.download.DownloadJob;
/*     */ import com.mojang.launcher.updater.download.DownloadListener;
/*     */ import com.mojang.launcher.updater.download.Downloadable;
/*     */ import com.mojang.launcher.versions.CompleteVersion;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractGameRunner
/*     */   implements GameRunner, DownloadListener
/*     */ {
/*  26 */   protected static final Logger LOGGER = LogManager.getLogger();
/*  27 */   protected final Object lock = new Object();
/*  28 */   private final List<DownloadJob> jobs = new ArrayList<>();
/*     */   protected CompleteVersion version;
/*  30 */   private GameInstanceStatus status = GameInstanceStatus.IDLE;
/*  31 */   private final List<GameRunnerListener> listeners = Lists.newArrayList();
/*     */ 
/*     */   
/*     */   protected void setStatus(GameInstanceStatus status) {
/*  35 */     synchronized (this.lock) {
/*     */       
/*  37 */       this.status = status;
/*  38 */       for (GameRunnerListener listener : Lists.newArrayList(this.listeners)) {
/*  39 */         listener.onGameInstanceChangedState(this, status);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected abstract Launcher getLauncher();
/*     */   
/*     */   public GameInstanceStatus getStatus() {
/*  48 */     return this.status;
/*     */   }
/*     */ 
/*     */   
/*     */   public void playGame(VersionSyncInfo syncInfo) {
/*  53 */     synchronized (this.lock) {
/*     */       
/*  55 */       if (getStatus() != GameInstanceStatus.IDLE) {
/*     */         
/*  57 */         LOGGER.warn("Tried to play game but game is already starting!");
/*     */         return;
/*     */       } 
/*  60 */       setStatus(GameInstanceStatus.PREPARING);
/*     */     } 
/*  62 */     LOGGER.info("Getting syncinfo for selected version");
/*  63 */     if (syncInfo == null) {
/*     */       
/*  65 */       LOGGER.warn("Tried to launch a version without a version being selected...");
/*  66 */       setStatus(GameInstanceStatus.IDLE);
/*     */       return;
/*     */     } 
/*  69 */     synchronized (this.lock) {
/*     */       
/*  71 */       LOGGER.info("Queueing library & version downloads");
/*     */       
/*     */       try {
/*  74 */         this.version = getLauncher().getVersionManager().getLatestCompleteVersion(syncInfo);
/*     */       }
/*  76 */       catch (IOException e) {
/*     */         
/*  78 */         LOGGER.error("Couldn't get complete version info for " + syncInfo.getLatestVersion(), e);
/*  79 */         setStatus(GameInstanceStatus.IDLE);
/*     */         return;
/*     */       } 
/*  82 */       if (syncInfo.getRemoteVersion() != null && syncInfo.getLatestSource() != VersionSyncInfo.VersionSource.REMOTE && !this.version.isSynced()) {
/*     */ 
/*     */         
/*     */         try {
/*  86 */           syncInfo = getLauncher().getVersionManager().syncVersion(syncInfo);
/*  87 */           this.version = getLauncher().getVersionManager().getLatestCompleteVersion(syncInfo);
/*     */         }
/*  89 */         catch (IOException e) {
/*     */           
/*  91 */           LOGGER.error("Couldn't sync local and remote versions", e);
/*     */         } 
/*  93 */         this.version.setSynced(true);
/*     */       } 
/*  95 */       if (!this.version.appliesToCurrentEnvironment()) {
/*     */         
/*  97 */         String reason = this.version.getIncompatibilityReason();
/*  98 */         if (reason == null) {
/*  99 */           reason = "This version is incompatible with your computer. Please try another one by going into Edit Profile and selecting one through the dropdown. Sorry!";
/*     */         }
/* 101 */         LOGGER.error("Version " + this.version.getId() + " is incompatible with current environment: " + reason);
/* 102 */         getLauncher().getUserInterface().gameLaunchFailure(reason);
/* 103 */         setStatus(GameInstanceStatus.IDLE);
/*     */ 
/*     */ 
/*     */         
/*     */         return;
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 112 */       if (!syncInfo.isUpToDate()) {
/*     */         
/*     */         try {
/* 115 */           getLauncher().getVersionManager().installVersion(this.version);
/*     */         }
/* 117 */         catch (IOException e) {
/*     */           
/* 119 */           LOGGER.error("Couldn't save version info to install " + syncInfo.getLatestVersion(), e);
/* 120 */           setStatus(GameInstanceStatus.IDLE);
/*     */           return;
/*     */         } 
/*     */       }
/* 124 */       setStatus(GameInstanceStatus.DOWNLOADING);
/* 125 */       downloadRequiredFiles(syncInfo);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void downloadRequiredFiles(VersionSyncInfo syncInfo) {
/*     */     try {
/* 133 */       DownloadJob librariesJob = new DownloadJob("Version & Libraries", false, this);
/* 134 */       addJob(librariesJob);
/* 135 */       getLauncher().getVersionManager().downloadVersion(syncInfo, librariesJob);
/* 136 */       librariesJob.startDownloading(getLauncher().getDownloaderExecutorService());
/*     */       
/* 138 */       DownloadJob resourceJob = new DownloadJob("Resources", true, this);
/* 139 */       addJob(resourceJob);
/* 140 */       getLauncher().getVersionManager().downloadResources(resourceJob, this.version);
/* 141 */       resourceJob.startDownloading(getLauncher().getDownloaderExecutorService());
/*     */     }
/* 143 */     catch (IOException e) {
/*     */       
/* 145 */       LOGGER.error("Couldn't get version info for " + syncInfo.getLatestVersion(), e);
/* 146 */       setStatus(GameInstanceStatus.IDLE);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void updateProgressBar() {
/* 152 */     synchronized (this.lock) {
/*     */       
/* 154 */       if (hasRemainingJobs()) {
/*     */         
/* 156 */         long total = 0L;
/* 157 */         long current = 0L;
/* 158 */         Downloadable longestRunning = null;
/* 159 */         for (DownloadJob job : this.jobs) {
/* 160 */           for (Downloadable file : job.getAllFiles()) {
/*     */             
/* 162 */             total += file.getMonitor().getTotal();
/* 163 */             current += file.getMonitor().getCurrent();
/* 164 */             if (longestRunning == null || longestRunning.getEndTime() > 0L || (file.getStartTime() < longestRunning.getStartTime() && file.getEndTime() == 0L)) {
/* 165 */               longestRunning = file;
/*     */             }
/*     */           } 
/*     */         } 
/* 169 */         getLauncher().getUserInterface().setDownloadProgress(new DownloadProgress(current, total, (longestRunning == null) ? null : longestRunning.getStatus()));
/*     */       }
/*     */       else {
/*     */         
/* 173 */         this.jobs.clear();
/* 174 */         getLauncher().getUserInterface().hideDownloadProgress();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasRemainingJobs() {
/* 181 */     synchronized (this.lock) {
/*     */       
/* 183 */       for (DownloadJob job : this.jobs) {
/* 184 */         if (!job.isComplete()) {
/* 185 */           return true;
/*     */         }
/*     */       } 
/*     */     } 
/* 189 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void addJob(DownloadJob job) {
/* 194 */     synchronized (this.lock) {
/*     */       
/* 196 */       this.jobs.add(job);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDownloadJobFinished(DownloadJob job) {
/* 202 */     updateProgressBar();
/* 203 */     synchronized (this.lock) {
/*     */       
/* 205 */       if (job.getFailures() > 0) {
/*     */         
/* 207 */         LOGGER.error("Job '" + job.getName() + "' finished with " + job.getFailures() + " failure(s)! (took " + job.getStopWatch().toString() + ")");
/* 208 */         setStatus(GameInstanceStatus.IDLE);
/*     */       }
/*     */       else {
/*     */         
/* 212 */         LOGGER.info("Job '" + job.getName() + "' finished successfully (took " + job.getStopWatch().toString() + ")");
/* 213 */         if (getStatus() != GameInstanceStatus.IDLE && !hasRemainingJobs()) {
/*     */           
/*     */           try {
/* 216 */             setStatus(GameInstanceStatus.LAUNCHING);
/* 217 */             launchGame();
/*     */           }
/* 219 */           catch (Throwable ex) {
/*     */             
/* 221 */             LOGGER.fatal("Fatal error launching game. Report this to http://bugs.mojang.com please!", ex);
/*     */           } 
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected abstract void launchGame() throws IOException;
/*     */ 
/*     */   
/*     */   public void onDownloadJobProgressChanged(DownloadJob job) {
/* 233 */     updateProgressBar();
/*     */   }
/*     */ 
/*     */   
/*     */   public void addListener(GameRunnerListener listener) {
/* 238 */     synchronized (this.lock) {
/*     */       
/* 240 */       this.listeners.add(listener);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\mojang\launcher\game\runner\AbstractGameRunner.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */