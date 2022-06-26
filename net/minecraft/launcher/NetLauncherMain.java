/*     */ package net.minecraft.launcher;
/*     */ import com.mojang.launcher.OperatingSystem;
/*     */ import java.awt.Component;
/*     */ import java.awt.Dimension;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.Proxy;
/*     */ import java.util.List;
/*     */ import javax.imageio.ImageIO;
/*     */ import javax.swing.JFrame;
/*     */ import joptsimple.ArgumentAcceptingOptionSpec;
/*     */ import joptsimple.NonOptionArgumentSpec;
/*     */ import joptsimple.OptionParser;
/*     */ import joptsimple.OptionSet;
/*     */ import joptsimple.OptionSpec;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class NetLauncherMain {
/*  21 */   private static final Logger LOGGER = LogManager.getLogger();
/*     */ 
/*     */ 
/*     */   
/*     */   public static void main(String[] args) {
/*  26 */     LOGGER.debug("main() called!");
/*  27 */     startLauncher(args);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void startLauncher(String[] args) {
/*  33 */     OptionParser parser = new OptionParser();
/*  34 */     parser.allowsUnrecognizedOptions();
/*  35 */     parser.accepts("winTen");
/*  36 */     ArgumentAcceptingOptionSpec argumentAcceptingOptionSpec1 = parser.accepts("proxyHost").withRequiredArg();
/*  37 */     ArgumentAcceptingOptionSpec argumentAcceptingOptionSpec2 = parser.accepts("proxyPort").withRequiredArg().defaultsTo("8080", (Object[])new String[0]).ofType(Integer.class);
/*  38 */     ArgumentAcceptingOptionSpec argumentAcceptingOptionSpec3 = parser.accepts("workDir").withRequiredArg().ofType(File.class).defaultsTo(getWorkingDirectory(), (Object[])new File[0]);
/*  39 */     NonOptionArgumentSpec nonOptionArgumentSpec = parser.nonOptions();
/*  40 */     OptionSet optionSet = parser.parse(args);
/*  41 */     List<String> leftoverArgs = optionSet.valuesOf((OptionSpec)nonOptionArgumentSpec);
/*     */ 
/*     */     
/*  44 */     String hostName = (String)optionSet.valueOf((OptionSpec)argumentAcceptingOptionSpec1);
/*  45 */     Proxy proxy = Proxy.NO_PROXY;
/*  46 */     if (hostName != null) {
/*     */       
/*     */       try {
/*  49 */         proxy = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress(hostName, ((Integer)optionSet.valueOf((OptionSpec)argumentAcceptingOptionSpec2)).intValue()));
/*     */       }
/*  51 */       catch (Exception exception) {}
/*     */     }
/*  53 */     File workingDirectory = (File)optionSet.valueOf((OptionSpec)argumentAcceptingOptionSpec3);
/*  54 */     workingDirectory.mkdirs();
/*     */     
/*  56 */     LOGGER.debug("About to create JFrame.");
/*     */     
/*  58 */     Proxy finalProxy = proxy;
/*  59 */     JFrame frame = new JFrame();
/*  60 */     frame.setTitle(LauncherConstants.getVersionName() + LauncherConstants.PROPERTIES.getEnvironment().getTitle());
/*  61 */     frame.setPreferredSize(new Dimension(900, 580));
/*     */     
/*     */     try {
/*  64 */       InputStream in = Launcher.class.getResourceAsStream("/favicon.png");
/*  65 */       if (in != null) {
/*  66 */         frame.setIconImage(ImageIO.read(in));
/*     */       }
/*     */     }
/*  69 */     catch (IOException iOException) {}
/*  70 */     frame.pack();
/*  71 */     frame.setLocationRelativeTo((Component)null);
/*  72 */     frame.setVisible(true);
/*     */ 
/*     */     
/*  75 */     if (optionSet.has("winTen")) {
/*     */       
/*  77 */       System.setProperty("os.name", "Windows 10");
/*  78 */       System.setProperty("os.version", "10.0");
/*     */     } 
/*  80 */     LOGGER.debug("Starting up launcher.");
/*  81 */     Launcher launcher = new Launcher(frame, workingDirectory, finalProxy, null, leftoverArgs.<String>toArray(new String[leftoverArgs.size()]), Integer.valueOf(100));
/*  82 */     if (optionSet.has("winTen")) {
/*  83 */       launcher.setWinTenHack();
/*     */     }
/*     */ 
/*     */     
/*  87 */     frame.setLocationRelativeTo((Component)null);
/*     */   }
/*     */ 
/*     */   
/*     */   public static File getWorkingDirectory() {
/*  92 */     String applicationData, folder, userHome = System.getProperty("user.home", ".");
/*     */     
/*  94 */     switch (OperatingSystem.getCurrentPlatform().ordinal())
/*     */     
/*     */     { case 1:
/*  97 */         workingDirectory = new File(userHome, ".minecraft/");
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
/* 111 */         return workingDirectory;case 2: applicationData = System.getenv("APPDATA"); folder = (applicationData != null) ? applicationData : userHome; workingDirectory = new File(folder, ".minecraft/"); return workingDirectory;case 3: workingDirectory = new File(userHome, "Library/Application Support/minecraft"); return workingDirectory; }  File workingDirectory = new File(userHome, "minecraft/"); return workingDirectory;
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\net\minecraft\launcher\NetLauncherMain.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */