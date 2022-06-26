/*    */ package com.mojang.launcher.updater;
/*    */ 
/*    */ import com.mojang.launcher.versions.Version;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class VersionSyncInfo
/*    */ {
/*    */   private final Version localVersion;
/*    */   private final Version remoteVersion;
/*    */   private final boolean isInstalled;
/*    */   private final boolean isUpToDate;
/*    */   
/*    */   public VersionSyncInfo(Version localVersion, Version remoteVersion, boolean installed, boolean upToDate) {
/* 15 */     this.localVersion = localVersion;
/* 16 */     this.remoteVersion = remoteVersion;
/* 17 */     this.isInstalled = installed;
/* 18 */     this.isUpToDate = upToDate;
/*    */   }
/*    */ 
/*    */   
/*    */   public Version getLocalVersion() {
/* 23 */     return this.localVersion;
/*    */   }
/*    */ 
/*    */   
/*    */   public Version getRemoteVersion() {
/* 28 */     return this.remoteVersion;
/*    */   }
/*    */ 
/*    */   
/*    */   public Version getLatestVersion() {
/* 33 */     if (getLatestSource() == VersionSource.REMOTE) {
/* 34 */       return this.remoteVersion;
/*    */     }
/* 36 */     return this.localVersion;
/*    */   }
/*    */ 
/*    */   
/*    */   public VersionSource getLatestSource() {
/* 41 */     if (getLocalVersion() == null) {
/* 42 */       return VersionSource.REMOTE;
/*    */     }
/* 44 */     if (getRemoteVersion() == null) {
/* 45 */       return VersionSource.LOCAL;
/*    */     }
/* 47 */     if (getRemoteVersion().getUpdatedTime().after(getLocalVersion().getUpdatedTime())) {
/* 48 */       return VersionSource.REMOTE;
/*    */     }
/* 50 */     return VersionSource.LOCAL;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isInstalled() {
/* 55 */     return this.isInstalled;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isOnRemote() {
/* 60 */     return (this.remoteVersion != null);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isUpToDate() {
/* 65 */     return this.isUpToDate;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 70 */     return "VersionSyncInfo{localVersion=" + this.localVersion + ", remoteVersion=" + this.remoteVersion + ", isInstalled=" + this.isInstalled + ", isUpToDate=" + this.isUpToDate + '}';
/*    */   }
/*    */   
/*    */   public enum VersionSource
/*    */   {
/* 75 */     REMOTE, LOCAL;
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\mojang\launche\\updater\VersionSyncInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */