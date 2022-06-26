/*    */ package com.mojang.launcher;
/*    */ 
/*    */ import com.mojang.authlib.Agent;
/*    */ import com.mojang.launcher.updater.ExceptionalThreadPoolExecutor;
/*    */ import com.mojang.launcher.updater.VersionManager;
/*    */ import com.mojang.launcher.versions.ReleaseTypeFactory;
/*    */ import java.io.File;
/*    */ import java.net.PasswordAuthentication;
/*    */ import java.net.Proxy;
/*    */ import java.util.concurrent.ThreadPoolExecutor;
/*    */ import java.util.concurrent.TimeUnit;
/*    */ import org.apache.logging.log4j.LogManager;
/*    */ import org.apache.logging.log4j.Logger;
/*    */ 
/*    */ public class Launcher
/*    */ {
/*    */   private final int launcherFormatVersion;
/*    */   private final ReleaseTypeFactory releaseTypeFactory;
/*    */   private final Agent agent;
/* 20 */   private final ThreadPoolExecutor downloaderExecutorService = (ThreadPoolExecutor)new ExceptionalThreadPoolExecutor(16, 16, 30L, TimeUnit.SECONDS);
/*    */   
/*    */   private final PasswordAuthentication proxyAuth;
/*    */   private final Proxy proxy;
/*    */   private final UserInterface ui;
/*    */   private final File workingDirectory;
/*    */   private final VersionManager versionManager;
/*    */   
/*    */   static {
/* 29 */     Thread.currentThread().setContextClassLoader(Launcher.class.getClassLoader());
/*    */   }
/*    */   
/* 32 */   private static final Logger LOGGER = LogManager.getLogger();
/*    */ 
/*    */   
/*    */   public Launcher(UserInterface ui, File workingDirectory, Proxy proxy, PasswordAuthentication proxyAuth, VersionManager versionManager, Agent agent, ReleaseTypeFactory releaseTypeFactory, int launcherFormatVersion) {
/* 36 */     this.ui = ui;
/* 37 */     this.proxy = proxy;
/* 38 */     this.proxyAuth = proxyAuth;
/* 39 */     this.workingDirectory = workingDirectory;
/* 40 */     this.agent = agent;
/* 41 */     this.versionManager = versionManager;
/* 42 */     this.releaseTypeFactory = releaseTypeFactory;
/* 43 */     this.launcherFormatVersion = launcherFormatVersion;
/*    */     
/* 45 */     this.downloaderExecutorService.allowCoreThreadTimeOut(true);
/*    */   }
/*    */ 
/*    */   
/*    */   public ReleaseTypeFactory getReleaseTypeFactory() {
/* 50 */     return this.releaseTypeFactory;
/*    */   }
/*    */ 
/*    */   
/*    */   public VersionManager getVersionManager() {
/* 55 */     return this.versionManager;
/*    */   }
/*    */ 
/*    */   
/*    */   public File getWorkingDirectory() {
/* 60 */     return this.workingDirectory;
/*    */   }
/*    */ 
/*    */   
/*    */   public UserInterface getUserInterface() {
/* 65 */     return this.ui;
/*    */   }
/*    */ 
/*    */   
/*    */   public Proxy getProxy() {
/* 70 */     return this.proxy;
/*    */   }
/*    */ 
/*    */   
/*    */   public PasswordAuthentication getProxyAuth() {
/* 75 */     return this.proxyAuth;
/*    */   }
/*    */ 
/*    */   
/*    */   public ThreadPoolExecutor getDownloaderExecutorService() {
/* 80 */     return this.downloaderExecutorService;
/*    */   }
/*    */ 
/*    */   
/*    */   public void shutdownLauncher() {
/* 85 */     getUserInterface().shutdownLauncher();
/*    */   }
/*    */ 
/*    */   
/*    */   public Agent getAgent() {
/* 90 */     return this.agent;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getLauncherFormatVersion() {
/* 95 */     return this.launcherFormatVersion;
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\mojang\launcher\Launcher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */