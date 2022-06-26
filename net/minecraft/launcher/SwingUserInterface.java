/*     */ package net.minecraft.launcher;
/*     */ 
/*     */ import com.google.common.util.concurrent.Futures;
/*     */ import com.google.common.util.concurrent.SettableFuture;
/*     */ import com.mojang.authlib.UserAuthentication;
/*     */ import com.mojang.launcher.OperatingSystem;
/*     */ import com.mojang.launcher.events.GameOutputLogProcessor;
/*     */ import com.mojang.launcher.updater.DownloadProgress;
/*     */ import com.mojang.launcher.versions.CompleteVersion;
/*     */ import java.awt.Component;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.event.WindowAdapter;
/*     */ import java.awt.event.WindowEvent;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.util.Timer;
/*     */ import java.util.TimerTask;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import javax.imageio.ImageIO;
/*     */ import javax.swing.BorderFactory;
/*     */ import javax.swing.JFrame;
/*     */ import javax.swing.JOptionPane;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.UIManager;
/*     */ import net.mc.main.DarkTheme;
/*     */ import net.minecraft.launcher.game.MinecraftGameRunner;
/*     */ import net.minecraft.launcher.profile.Profile;
/*     */ import net.minecraft.launcher.profile.ProfileManager;
/*     */ import net.minecraft.launcher.ui.LauncherPanel;
/*     */ import net.minecraft.launcher.ui.popups.login.LogInPopup;
/*     */ import net.minecraft.launcher.ui.tabs.GameOutputTab;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
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
/*     */ public class SwingUserInterface
/*     */   implements MinecraftUserInterface
/*     */ {
/*  53 */   private static final Logger LOGGER = LogManager.getLogger();
/*     */   
/*     */   private static final long MAX_SHUTDOWN_TIME = 10000L;
/*     */   private final Launcher minecraftLauncher;
/*     */   private LauncherPanel launcherPanel;
/*     */   private final JFrame frame;
/*     */   
/*     */   public SwingUserInterface(Launcher minecraftLauncher, JFrame frame) {
/*  61 */     this.minecraftLauncher = minecraftLauncher;
/*  62 */     this.frame = frame;
/*     */     
/*  64 */     setLookAndFeel();
/*     */   }
/*     */ 
/*     */   
/*     */   private static void setLookAndFeel() {
/*  69 */     JFrame frame = new JFrame();
/*     */     
/*     */     try {
/*  72 */       UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
/*     */     }
/*  74 */     catch (Throwable ignored) {
/*     */ 
/*     */       
/*     */       try {
/*  78 */         LOGGER.error("Your java failed to provide normal look and feel, trying the old fallback now");
/*  79 */         UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
/*     */       }
/*  81 */       catch (Throwable t) {
/*     */         
/*  83 */         LOGGER.error("Unexpected exception setting look and feel", t);
/*     */       } 
/*     */     } 
/*  86 */     JPanel panel = new JPanel();
/*  87 */     panel.setBorder(BorderFactory.createTitledBorder("test"));
/*  88 */     frame.add(panel);
/*     */     
/*     */     try {
/*  91 */       frame.pack();
/*     */     }
/*  93 */     catch (Throwable ignored) {
/*     */       
/*  95 */       LOGGER.error("Custom (broken) theme detected, falling back onto x-platform theme");
/*     */       
/*     */       try {
/*  98 */         UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
/*     */       }
/* 100 */       catch (Throwable ex) {
/*     */         
/* 102 */         LOGGER.error("Unexpected exception setting look and feel", ex);
/*     */       } 
/*     */     } 
/* 105 */     frame.dispose();
/*     */   }
/*     */ 
/*     */   
/*     */   public void showLoginPrompt(final Launcher minecraftLauncher, final LogInPopup.Callback callback) {
/* 110 */     SwingUtilities.invokeLater(new Runnable()
/*     */         {
/*     */           public void run()
/*     */           {
/* 114 */             LogInPopup popup = new LogInPopup(minecraftLauncher, callback);
/* 115 */             SwingUserInterface.this.launcherPanel.setCard("login", (JPanel)popup);
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void initializeFrame() {
/* 123 */     new DarkTheme(Launcher.getInstance().getUsernameManager().getDark());
/*     */     
/* 125 */     this.frame.getContentPane().removeAll();
/* 126 */     this.frame.setTitle(LauncherConstants.getVersionName() + LauncherConstants.PROPERTIES.getEnvironment().getTitle());
/* 127 */     this.frame.setPreferredSize(new Dimension(900, 580));
/* 128 */     this.frame.setDefaultCloseOperation(2);
/*     */     
/* 130 */     this.frame.addWindowListener(new WindowAdapter()
/*     */         {
/*     */           public void windowClosing(WindowEvent e)
/*     */           {
/* 134 */             SwingUserInterface.LOGGER.info("Window closed, shutting down.");
/* 135 */             SwingUserInterface.this.frame.setVisible(false);
/* 136 */             SwingUserInterface.this.frame.dispose();
/* 137 */             SwingUserInterface.LOGGER.info("Halting executors");
/* 138 */             SwingUserInterface.this.minecraftLauncher.getLauncher().getVersionManager().getExecutorService().shutdown();
/* 139 */             SwingUserInterface.LOGGER.info("Awaiting termination.");
/*     */             
/*     */             try {
/* 142 */               SwingUserInterface.this.minecraftLauncher.getLauncher().getVersionManager().getExecutorService().awaitTermination(10L, TimeUnit.SECONDS);
/*     */             }
/* 144 */             catch (InterruptedException e1) {
/*     */               
/* 146 */               SwingUserInterface.LOGGER.info("Termination took too long.");
/*     */             } 
/* 148 */             SwingUserInterface.LOGGER.info("Goodbye.");
/* 149 */             SwingUserInterface.this.forcefullyShutdown();
/*     */           }
/*     */         });
/*     */     
/*     */     try {
/* 154 */       InputStream in = Launcher.class.getResourceAsStream("/favicon.png");
/* 155 */       if (in != null) {
/* 156 */         this.frame.setIconImage(ImageIO.read(in));
/*     */       }
/*     */     }
/* 159 */     catch (IOException iOException) {}
/* 160 */     this.launcherPanel = new LauncherPanel(this.minecraftLauncher);
/*     */     
/* 162 */     SwingUtilities.invokeLater(new Runnable()
/*     */         {
/*     */           public void run()
/*     */           {
/* 166 */             SwingUserInterface.this.frame.add((Component)SwingUserInterface.this.launcherPanel);
/* 167 */             SwingUserInterface.this.frame.pack();
/* 168 */             SwingUserInterface.this.frame.setVisible(true);
/* 169 */             SwingUserInterface.this.frame.setAlwaysOnTop(true);
/* 170 */             SwingUserInterface.this.frame.setAlwaysOnTop(false);
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void forcefullyShutdown() {
/*     */     try {
/* 179 */       Timer timer = new Timer();
/* 180 */       timer.schedule(new TimerTask()
/*     */           {
/*     */             public void run()
/*     */             {
/* 184 */               Runtime.getRuntime().halt(0);
/*     */             }
/*     */           },  10000L);
/*     */ 
/*     */ 
/*     */       
/* 190 */       System.exit(0);
/*     */     }
/* 192 */     catch (Throwable ignored) {
/*     */       
/* 194 */       Runtime.getRuntime().halt(0);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void showOutdatedNotice() {
/* 200 */     String error = "Sorry, but your launcher is outdated! Please redownload it at https://mojang.com/2013/06/minecraft-1-6-pre-release/";
/*     */     
/* 202 */     this.frame.getContentPane().removeAll();
/*     */     
/* 204 */     int result = JOptionPane.showOptionDialog(this.frame, error, "Outdated launcher", 0, 0, null, (Object[])LauncherConstants.BOOTSTRAP_OUT_OF_DATE_BUTTONS, LauncherConstants.BOOTSTRAP_OUT_OF_DATE_BUTTONS[0]);
/* 205 */     if (result == 0) {
/*     */       
/*     */       try {
/* 208 */         OperatingSystem.openLink(new URI("https://mojang.com/2013/06/minecraft-1-6-pre-release/"));
/*     */       }
/* 210 */       catch (URISyntaxException e) {
/*     */         
/* 212 */         LOGGER.error("Couldn't open bootstrap download link. Please visit https://mojang.com/2013/06/minecraft-1-6-pre-release/ manually.", e);
/*     */       } 
/*     */     }
/* 215 */     this.minecraftLauncher.getLauncher().shutdownLauncher();
/*     */   }
/*     */ 
/*     */   
/*     */   public void showLoginPrompt() {
/* 220 */     final ProfileManager profileManager = this.minecraftLauncher.getProfileManager();
/*     */     
/*     */     try {
/* 223 */       profileManager.saveProfiles();
/*     */     }
/* 225 */     catch (IOException e) {
/*     */       
/* 227 */       LOGGER.error("Couldn't save profiles before logging in!", e);
/*     */     } 
/* 229 */     final Profile selectedProfile = profileManager.getSelectedProfile();
/* 230 */     showLoginPrompt(this.minecraftLauncher, new LogInPopup.Callback()
/*     */         {
/*     */           public void onLogIn(String uuid)
/*     */           {
/* 234 */             UserAuthentication auth = profileManager.getAuthDatabase().getByUUID(uuid);
/* 235 */             profileManager.setSelectedUser(uuid);
/* 236 */             if (selectedProfile.getName().equals("(Default)") && auth.getSelectedProfile() != null) {
/*     */               
/* 238 */               String playerName = auth.getSelectedProfile().getName();
/* 239 */               String profileName = auth.getSelectedProfile().getName();
/* 240 */               int count = 1;
/* 241 */               while (profileManager.getProfiles().containsKey(profileName)) {
/* 242 */                 profileName = playerName + " " + ++count;
/*     */               }
/* 244 */               Profile newProfile = new Profile(selectedProfile);
/* 245 */               newProfile.setName(profileName);
/* 246 */               profileManager.getProfiles().put(profileName, newProfile);
/* 247 */               profileManager.getProfiles().remove("(Default)");
/* 248 */               profileManager.setSelectedProfile(profileName);
/*     */             } 
/*     */             
/*     */             try {
/* 252 */               profileManager.saveProfiles();
/*     */             }
/* 254 */             catch (IOException e) {
/*     */               
/* 256 */               SwingUserInterface.LOGGER.error("Couldn't save profiles after logging in!", e);
/*     */             } 
/* 258 */             if (uuid == null) {
/* 259 */               SwingUserInterface.this.minecraftLauncher.getLauncher().shutdownLauncher();
/*     */             } else {
/* 261 */               profileManager.fireRefreshEvent();
/*     */             } 
/* 263 */             SwingUserInterface.this.launcherPanel.setCard("launcher", null);
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public void setVisible(final boolean visible) {
/* 270 */     SwingUtilities.invokeLater(new Runnable()
/*     */         {
/*     */           public void run()
/*     */           {
/* 274 */             SwingUserInterface.this.frame.setVisible(visible);
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public void shutdownLauncher() {
/* 281 */     if (SwingUtilities.isEventDispatchThread()) {
/*     */       
/* 283 */       LOGGER.info("Requesting window close");
/* 284 */       this.frame.dispatchEvent(new WindowEvent(this.frame, 201));
/*     */     }
/*     */     else {
/*     */       
/* 288 */       SwingUtilities.invokeLater(new Runnable()
/*     */           {
/*     */             public void run()
/*     */             {
/* 292 */               SwingUserInterface.this.shutdownLauncher();
/*     */             }
/*     */           });
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void setDownloadProgress(final DownloadProgress downloadProgress) {
/* 300 */     SwingUtilities.invokeLater(new Runnable()
/*     */         {
/*     */           public void run()
/*     */           {
/* 304 */             SwingUserInterface.this.launcherPanel.getProgressBar().setVisible(true);
/* 305 */             SwingUserInterface.this.launcherPanel.getProgressBar().setValue((int)(downloadProgress.getPercent() * 100.0F));
/* 306 */             SwingUserInterface.this.launcherPanel.getProgressBar().setString(downloadProgress.getStatus());
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public void hideDownloadProgress() {
/* 313 */     SwingUtilities.invokeLater(new Runnable()
/*     */         {
/*     */           public void run()
/*     */           {
/* 317 */             SwingUserInterface.this.launcherPanel.getProgressBar().setVisible(false);
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void gameLaunchFailure(final String reason) {
/* 325 */     SwingUtilities.invokeLater(new Runnable()
/*     */         {
/*     */           public void run()
/*     */           {
/* 329 */             JOptionPane.showMessageDialog(SwingUserInterface.this.frame, reason, "Cannot play game", 0);
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public void updatePlayState() {
/* 336 */     SwingUtilities.invokeLater(new Runnable()
/*     */         {
/*     */           public void run()
/*     */           {
/* 340 */             SwingUserInterface.this.launcherPanel.getBottomBar().getPlayButtonPanel().checkState();
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public GameOutputLogProcessor showGameOutputTab(final MinecraftGameRunner gameRunner) {
/* 347 */     final SettableFuture<GameOutputLogProcessor> future = SettableFuture.create();
/*     */     
/* 349 */     SwingUtilities.invokeLater(new Runnable()
/*     */         {
/*     */           public void run()
/*     */           {
/* 353 */             GameOutputTab tab = new GameOutputTab(SwingUserInterface.this.minecraftLauncher);
/* 354 */             future.set(tab);
/* 355 */             UserAuthentication auth = gameRunner.getAuth();
/* 356 */             String name = (auth.getSelectedProfile() == null) ? "Demo" : auth.getSelectedProfile().getName();
/* 357 */             SwingUserInterface.this.launcherPanel.getTabPanel().removeTab("Game Output (" + name + ")");
/* 358 */             SwingUserInterface.this.launcherPanel.getTabPanel().addTab("Game Output (" + name + ")", (Component)tab);
/* 359 */             SwingUserInterface.this.launcherPanel.getTabPanel().setSelectedComponent((Component)tab);
/*     */           }
/*     */         });
/* 362 */     return (GameOutputLogProcessor)Futures.getUnchecked((Future)future);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldDowngradeProfiles() {
/* 367 */     int result = JOptionPane.showOptionDialog(this.frame, "It looks like you've used a newer launcher than this one! If you go back to using this one, we will need to reset your configuration.", "Outdated launcher", 0, 0, null, (Object[])LauncherConstants.LAUNCHER_OUT_OF_DATE_BUTTONS, LauncherConstants.LAUNCHER_OUT_OF_DATE_BUTTONS[0]);
/*     */     
/* 369 */     return (result == 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getTitle() {
/* 374 */     return "Minecraft Launcher " + LauncherConstants.getVersionName();
/*     */   }
/*     */ 
/*     */   
/*     */   public JFrame getFrame() {
/* 379 */     return this.frame;
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateSideState() {
/* 384 */     throw new UnsupportedOperationException("Not supported yet.");
/*     */   }
/*     */ 
/*     */   
/*     */   public void showCrashReport(CompleteVersion paramCompleteVersion, File paramFile, String paramString) {
/* 389 */     throw new UnsupportedOperationException("Not supported yet.");
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\net\minecraft\launcher\SwingUserInterface.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */