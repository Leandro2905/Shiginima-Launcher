/*     */ package net.minecraft.launcher.ui.bottombar;
/*     */ 
/*     */ import com.mojang.authlib.UserAuthentication;
/*     */ import com.mojang.launcher.events.RefreshedVersionsListener;
/*     */ import com.mojang.launcher.updater.VersionManager;
/*     */ import com.mojang.launcher.updater.VersionSyncInfo;
/*     */ import java.awt.Desktop;
/*     */ import java.awt.GridBagConstraints;
/*     */ import java.awt.GridBagLayout;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.io.IOException;
/*     */ import java.net.URI;
/*     */ import java.util.List;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.SwingUtilities;
/*     */ import net.mc.main.Main;
/*     */ import net.minecraft.launcher.Language;
/*     */ import net.minecraft.launcher.Launcher;
/*     */ import net.minecraft.launcher.profile.Profile;
/*     */ import net.minecraft.launcher.profile.ProfileManager;
/*     */ import net.minecraft.launcher.profile.RefreshedProfilesListener;
/*     */ import net.minecraft.launcher.profile.UserChangedListener;
/*     */ 
/*     */ 
/*     */ public class PlayerInfoPanel
/*     */   extends JPanel
/*     */   implements RefreshedVersionsListener, RefreshedProfilesListener, UserChangedListener
/*     */ {
/*     */   private final Launcher minecraftLauncher;
/*  33 */   private final JLabel welcomeText = new JLabel("", 0);
/*  34 */   private final JLabel versionText = new JLabel("", 0);
/*  35 */   private final JButton switchUserButton = new JButton(Language.get("main.button.logout"));
/*  36 */   private final JButton supportButton = new JButton(Language.get("main.button.web"));
/*     */ 
/*     */   
/*     */   public PlayerInfoPanel(final Launcher minecraftLauncher) {
/*  40 */     this.minecraftLauncher = minecraftLauncher;
/*     */     
/*  42 */     minecraftLauncher.getProfileManager().addRefreshedProfilesListener(this);
/*  43 */     minecraftLauncher.getProfileManager().addUserChangedListener(this);
/*  44 */     checkState();
/*  45 */     createInterface();
/*     */     
/*  47 */     this.switchUserButton.setEnabled(false);
/*  48 */     this.switchUserButton.addActionListener(new ActionListener()
/*     */         {
/*     */           public void actionPerformed(ActionEvent e)
/*     */           {
/*  52 */             minecraftLauncher.getUserInterface().showLoginPrompt();
/*     */           }
/*     */         });
/*     */     
/*  56 */     this.supportButton.addActionListener(new ActionListener() {
/*     */           public void actionPerformed(ActionEvent e) {
/*  58 */             PlayerInfoPanel.this.openWebPage(Main.cbuttonlink);
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   public void openWebPage(String url) {
/*     */     try {
/*  65 */       Desktop.getDesktop().browse(URI.create(url));
/*  66 */     } catch (IOException e) {
/*  67 */       System.out.println(e.getMessage());
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void createInterface() {
/*  72 */     setLayout(new GridBagLayout());
/*  73 */     GridBagConstraints constraints = new GridBagConstraints();
/*  74 */     constraints.fill = 2;
/*     */     
/*  76 */     constraints.gridy = 0;
/*     */     
/*  78 */     constraints.weightx = 1.0D;
/*  79 */     constraints.gridwidth = 2;
/*  80 */     add(this.welcomeText, constraints);
/*  81 */     constraints.gridwidth = 1;
/*  82 */     constraints.weightx = 0.0D;
/*     */     
/*  84 */     constraints.gridy++;
/*     */     
/*  86 */     constraints.weightx = 1.0D;
/*  87 */     constraints.gridwidth = 2;
/*  88 */     add(this.versionText, constraints);
/*  89 */     constraints.gridwidth = 1;
/*  90 */     constraints.weightx = 0.0D;
/*     */     
/*  92 */     constraints.gridy++;
/*     */     
/*  94 */     constraints.weightx = 0.5D;
/*  95 */     constraints.fill = 0;
/*  96 */     add(this.switchUserButton, constraints);
/*  97 */     add(this.supportButton, constraints);
/*  98 */     constraints.weightx = 0.0D;
/*     */     
/* 100 */     constraints.gridy++;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onProfilesRefreshed(ProfileManager manager) {
/* 106 */     SwingUtilities.invokeLater(new Runnable()
/*     */         {
/*     */           public void run()
/*     */           {
/* 110 */             PlayerInfoPanel.this.checkState();
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public void checkState() {
/* 117 */     ProfileManager profileManager = this.minecraftLauncher.getProfileManager();
/* 118 */     UserAuthentication auth = (profileManager.getSelectedUser() == null) ? null : profileManager.getAuthDatabase().getByUUID(profileManager.getSelectedUser());
/* 119 */     if (auth == null || !auth.isLoggedIn()) {
/* 120 */       this.welcomeText.setText("Welcome, guest! Please log in.");
/* 121 */     } else if (auth.getSelectedProfile() == null) {
/* 122 */       this.welcomeText.setText("<html>Welcome, player!</html>");
/*     */     } else {
/* 124 */       this.welcomeText.setText("<html>Welcome, <b>" + auth.getSelectedProfile().getName() + "</b></html>");
/*     */     } 
/* 126 */     Profile profile = profileManager.getProfiles().isEmpty() ? null : profileManager.getSelectedProfile();
/* 127 */     List<VersionSyncInfo> versions = (profile == null) ? null : this.minecraftLauncher.getLauncher().getVersionManager().getVersions(profile.getVersionFilter());
/* 128 */     VersionSyncInfo version = (profile == null || versions.isEmpty()) ? null : versions.get(0);
/* 129 */     if (profile != null && profile.getLastVersionId() != null) {
/*     */       
/* 131 */       VersionSyncInfo requestedVersion = this.minecraftLauncher.getLauncher().getVersionManager().getVersionSyncInfo(profile.getLastVersionId());
/* 132 */       if (requestedVersion != null && requestedVersion.getLatestVersion() != null) {
/* 133 */         version = requestedVersion;
/*     */       }
/*     */     } 
/* 136 */     if (version == null) {
/* 137 */       this.versionText.setText("Loading versions...");
/* 138 */     } else if (version.isUpToDate()) {
/* 139 */       this.versionText.setText("Ready to play Minecraft " + version.getLatestVersion().getId());
/* 140 */     } else if (version.isInstalled()) {
/* 141 */       this.versionText.setText("Ready to update & play Minecraft " + version.getLatestVersion().getId());
/* 142 */     } else if (version.isOnRemote()) {
/* 143 */       this.versionText.setText("Ready to download & play Minecraft " + version.getLatestVersion().getId());
/*     */     } 
/* 145 */     this.switchUserButton.setEnabled(true);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onVersionsRefreshed(VersionManager manager) {
/* 150 */     SwingUtilities.invokeLater(new Runnable()
/*     */         {
/*     */           public void run()
/*     */           {
/* 154 */             PlayerInfoPanel.this.checkState();
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public Launcher getMinecraftLauncher() {
/* 161 */     return this.minecraftLauncher;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onUserChanged(ProfileManager manager) {
/* 166 */     SwingUtilities.invokeLater(new Runnable()
/*     */         {
/*     */           public void run()
/*     */           {
/* 170 */             PlayerInfoPanel.this.checkState();
/*     */           }
/*     */         });
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\net\minecraft\launche\\ui\bottombar\PlayerInfoPanel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */