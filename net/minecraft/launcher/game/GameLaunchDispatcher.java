/*     */ package net.minecraft.launcher.game;
/*     */ 
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.collect.BiMap;
/*     */ import com.google.common.collect.HashBiMap;
/*     */ import com.mojang.authlib.UserAuthentication;
/*     */ import com.mojang.launcher.game.GameInstanceStatus;
/*     */ import com.mojang.launcher.game.runner.GameRunner;
/*     */ import com.mojang.launcher.game.runner.GameRunnerListener;
/*     */ import com.mojang.launcher.updater.VersionSyncInfo;
/*     */ import java.io.File;
/*     */ import java.util.concurrent.locks.ReentrantLock;
/*     */ import net.minecraft.launcher.Language;
/*     */ import net.minecraft.launcher.Launcher;
/*     */ import net.minecraft.launcher.profile.LauncherVisibilityRule;
/*     */ import net.minecraft.launcher.profile.Profile;
/*     */ import net.minecraft.launcher.profile.ProfileManager;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GameLaunchDispatcher
/*     */   implements GameRunnerListener
/*     */ {
/*     */   private final Launcher launcher;
/*     */   public static String[] additionalLaunchArgs;
/*  29 */   private final ReentrantLock lock = new ReentrantLock();
/*  30 */   private final BiMap<UserAuthentication, MinecraftGameRunner> instances = (BiMap<UserAuthentication, MinecraftGameRunner>)HashBiMap.create();
/*     */   
/*     */   private boolean downloadInProgress = false;
/*     */   
/*     */   public GameLaunchDispatcher(Launcher launcher, String[] additionalLaunchArgs) {
/*  35 */     this.launcher = launcher;
/*  36 */     this; GameLaunchDispatcher.additionalLaunchArgs = additionalLaunchArgs;
/*     */   }
/*     */ 
/*     */   
/*     */   public PlayStatus getStatus() {
/*  41 */     ProfileManager profileManager = this.launcher.getProfileManager();
/*  42 */     Profile profile = profileManager.getProfiles().isEmpty() ? null : profileManager.getSelectedProfile();
/*  43 */     UserAuthentication user = (profileManager.getSelectedUser() == null) ? null : profileManager.getAuthDatabase().getByUUID(profileManager.getSelectedUser());
/*  44 */     if (user == null || !user.isLoggedIn() || profile == null || this.launcher.getLauncher().getVersionManager().getVersions(profile.getVersionFilter()).isEmpty()) {
/*  45 */       return PlayStatus.LOADING;
/*     */     }
/*  47 */     this.lock.lock();
/*     */ 
/*     */     
/*     */     try {
/*  51 */       if (this.downloadInProgress) {
/*  52 */         return PlayStatus.DOWNLOADING;
/*     */       }
/*  54 */       if (this.instances.containsKey(user)) {
/*  55 */         return PlayStatus.ALREADY_PLAYING;
/*     */       }
/*     */     }
/*     */     finally {
/*     */       
/*  60 */       this.lock.unlock();
/*     */     } 
/*  62 */     if (user.getSelectedProfile() == null) {
/*  63 */       return PlayStatus.CAN_PLAY_DEMO;
/*     */     }
/*  65 */     if (user.canPlayOnline()) {
/*  66 */       return PlayStatus.CAN_PLAY_ONLINE;
/*     */     }
/*  68 */     return PlayStatus.CAN_PLAY_OFFLINE;
/*     */   }
/*     */ 
/*     */   
/*     */   public GameInstanceStatus getInstanceStatus() {
/*  73 */     ProfileManager profileManager = this.launcher.getProfileManager();
/*  74 */     UserAuthentication user = (profileManager.getSelectedUser() == null) ? null : profileManager.getAuthDatabase().getByUUID(profileManager.getSelectedUser());
/*     */     
/*  76 */     this.lock.lock();
/*     */     
/*     */     try {
/*  79 */       GameRunner gameRunner = (GameRunner)this.instances.get(user);
/*  80 */       if (gameRunner != null) {
/*  81 */         return gameRunner.getStatus();
/*     */       }
/*     */     }
/*     */     finally {
/*     */       
/*  86 */       this.lock.unlock();
/*     */     } 
/*  88 */     return GameInstanceStatus.IDLE;
/*     */   }
/*     */ 
/*     */   
/*     */   public void play() {
/*  93 */     ProfileManager profileManager = this.launcher.getProfileManager();
/*  94 */     final Profile profile = profileManager.getSelectedProfile();
/*  95 */     UserAuthentication user = (profileManager.getSelectedUser() == null) ? null : profileManager.getAuthDatabase().getByUUID(profileManager.getSelectedUser());
/*  96 */     final String lastVersionId = profile.getLastVersionId();
/*  97 */     this; final MinecraftGameRunner gameRunner = new MinecraftGameRunner(this.launcher, additionalLaunchArgs);
/*  98 */     gameRunner.setStatus(GameInstanceStatus.PREPARING);
/*     */     
/* 100 */     this.lock.lock();
/*     */     
/*     */     try {
/* 103 */       if (this.instances.containsKey(user) || this.downloadInProgress) {
/*     */         return;
/*     */       }
/* 106 */       this.instances.put(user, gameRunner);
/* 107 */       this.downloadInProgress = true;
/*     */     }
/*     */     finally {
/*     */       
/* 111 */       this.lock.unlock();
/*     */     } 
/* 113 */     this.launcher.getLauncher().getVersionManager().getExecutorService().execute(new Runnable()
/*     */         {
/*     */           public void run()
/*     */           {
/* 117 */             gameRunner.setVisibility((LauncherVisibilityRule)Objects.firstNonNull(profile.getLauncherVisibilityOnGameClose(), Profile.DEFAULT_LAUNCHER_VISIBILITY));
/*     */             
/* 119 */             VersionSyncInfo syncInfo = null;
/* 120 */             if (lastVersionId != null) {
/* 121 */               syncInfo = GameLaunchDispatcher.this.launcher.getLauncher().getVersionManager().getVersionSyncInfo(lastVersionId);
/*     */             }
/* 123 */             if (syncInfo == null || syncInfo.getLatestVersion() == null) {
/* 124 */               syncInfo = GameLaunchDispatcher.this.launcher.getLauncher().getVersionManager().getVersions(profile.getVersionFilter()).get(0);
/*     */             }
/* 126 */             gameRunner.setStatus(GameInstanceStatus.IDLE);
/* 127 */             gameRunner.addListener(GameLaunchDispatcher.this);
/* 128 */             gameRunner.playGame(syncInfo);
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public void onGameInstanceChangedState(GameRunner runner, GameInstanceStatus status) {
/* 135 */     this.lock.lock();
/*     */     
/*     */     try {
/* 138 */       if (status == GameInstanceStatus.IDLE) {
/* 139 */         this.instances.inverse().remove(runner);
/*     */       }
/* 141 */       this.downloadInProgress = false;
/* 142 */       for (GameRunner instance : this.instances.values()) {
/* 143 */         if (instance.getStatus() != GameInstanceStatus.PLAYING) {
/*     */           
/* 145 */           this.downloadInProgress = true;
/*     */           break;
/*     */         } 
/*     */       } 
/* 149 */       this.launcher.getUserInterface().updatePlayState();
/*     */     }
/*     */     finally {
/*     */       
/* 153 */       this.lock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isRunningInSameFolder() {
/* 159 */     this.lock.lock();
/*     */     try {
/* 161 */       File currentGameDir = (File)Objects.firstNonNull(this.launcher.getProfileManager().getSelectedProfile().getGameDir(), this.launcher.getLauncher().getWorkingDirectory());
/* 162 */       for (MinecraftGameRunner runner : this.instances.values()) {
/* 163 */         Profile profile = runner.getSelectedProfile();
/* 164 */         if (profile != null)
/*     */         {
/* 166 */           File otherGameDir = (File)Objects.firstNonNull(profile.getGameDir(), this.launcher.getLauncher().getWorkingDirectory());
/* 167 */           if (currentGameDir.equals(otherGameDir)) {
/* 168 */             return true;
/*     */           }
/*     */         }
/*     */       
/*     */       }
/*     */     
/*     */     } finally {
/*     */       
/* 176 */       this.lock.unlock();
/*     */     } 
/* 178 */     return false;
/*     */   }
/*     */   
/*     */   public enum PlayStatus
/*     */   {
/* 183 */     LOADING("Loading...", false), CAN_PLAY_DEMO("Play Demo", true), CAN_PLAY_ONLINE(Language.get("main.button.play"), true), CAN_PLAY_OFFLINE(Language.get("main.button.play"), true), ALREADY_PLAYING("Already Playing...", false), DOWNLOADING("Installing...", false);
/*     */     
/* 185 */     private String name = Language.get("main.button.play");
/*     */     private final boolean canPlay;
/*     */     
/*     */     PlayStatus(String name, boolean canPlay) {
/* 189 */       this.name = Language.get("main.button.play");
/* 190 */       this.canPlay = canPlay;
/*     */     }
/*     */     
/*     */     public String getName() {
/* 194 */       return this.name;
/*     */     }
/*     */     
/*     */     public boolean canPlay() {
/* 198 */       return this.canPlay;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\net\minecraft\launcher\game\GameLaunchDispatcher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */