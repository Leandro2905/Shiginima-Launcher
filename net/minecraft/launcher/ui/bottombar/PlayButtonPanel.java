/*     */ package net.minecraft.launcher.ui.bottombar;
/*     */ 
/*     */ import com.mojang.launcher.OperatingSystem;
/*     */ import com.mojang.launcher.events.RefreshedVersionsListener;
/*     */ import com.mojang.launcher.game.GameInstanceStatus;
/*     */ import com.mojang.launcher.updater.VersionManager;
/*     */ import java.awt.Cursor;
/*     */ import java.awt.Font;
/*     */ import java.awt.GridBagConstraints;
/*     */ import java.awt.GridBagLayout;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.awt.event.MouseAdapter;
/*     */ import java.awt.event.MouseEvent;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JOptionPane;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.SwingUtilities;
/*     */ import net.minecraft.launcher.Language;
/*     */ import net.minecraft.launcher.Launcher;
/*     */ import net.minecraft.launcher.LauncherConstants;
/*     */ import net.minecraft.launcher.SwingUserInterface;
/*     */ import net.minecraft.launcher.game.GameLaunchDispatcher;
/*     */ import net.minecraft.launcher.profile.ProfileManager;
/*     */ import net.minecraft.launcher.profile.RefreshedProfilesListener;
/*     */ import net.minecraft.launcher.profile.UserChangedListener;
/*     */ 
/*     */ 
/*     */ public class PlayButtonPanel
/*     */   extends JPanel
/*     */   implements RefreshedVersionsListener, RefreshedProfilesListener, UserChangedListener
/*     */ {
/*     */   private final Launcher minecraftLauncher;
/*  35 */   private final JButton playButton = new JButton("Play");
/*  36 */   private final JLabel demoHelpLink = new JLabel("(Why can I only play demo?)");
/*     */ 
/*     */   
/*     */   public PlayButtonPanel(Launcher minecraftLauncher) {
/*  40 */     this.minecraftLauncher = minecraftLauncher;
/*     */     
/*  42 */     minecraftLauncher.getProfileManager().addRefreshedProfilesListener(this);
/*  43 */     minecraftLauncher.getProfileManager().addUserChangedListener(this);
/*  44 */     checkState();
/*  45 */     createInterface();
/*     */     
/*  47 */     this.playButton.addActionListener(new ActionListener()
/*     */         {
/*     */           public void actionPerformed(ActionEvent e)
/*     */           {
/*  51 */             GameLaunchDispatcher dispatcher = PlayButtonPanel.this.getMinecraftLauncher().getLaunchDispatcher();
/*  52 */             if (dispatcher.isRunningInSameFolder()) {
/*     */               
/*  54 */               int result = JOptionPane.showConfirmDialog(((SwingUserInterface)PlayButtonPanel.this.getMinecraftLauncher().getUserInterface()).getFrame(), "You already have an instance of Minecraft running. If you launch another one in the same folder, they may clash and corrupt your saves.\nThis could cause many issues, in singleplayer or otherwise. We will not be responsible for anything that goes wrong.\nDo you want to start another instance of Minecraft, despite this?\nYou may solve this issue by launching the game in a different folder (see the \"Edit Profile\" button)", "Duplicate instance warning", 0);
/*  55 */               if (result == 0) {
/*  56 */                 dispatcher.play();
/*     */               }
/*     */             }
/*     */             else {
/*     */               
/*  61 */               dispatcher.play();
/*     */             } 
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createInterface() {
/*  69 */     setLayout(new GridBagLayout());
/*  70 */     GridBagConstraints constraints = new GridBagConstraints();
/*  71 */     constraints.fill = 1;
/*  72 */     constraints.weightx = 1.0D;
/*  73 */     constraints.weighty = 1.0D;
/*     */     
/*  75 */     constraints.gridy = 0;
/*  76 */     constraints.gridx = 0;
/*  77 */     add(this.playButton, constraints);
/*     */     
/*  79 */     constraints.gridy++;
/*  80 */     constraints.weighty = 0.0D;
/*  81 */     constraints.anchor = 10;
/*  82 */     Font smalltextFont = this.demoHelpLink.getFont().deriveFont(this.demoHelpLink.getFont().getSize() - 2.0F);
/*  83 */     this.demoHelpLink.setCursor(new Cursor(12));
/*  84 */     this.demoHelpLink.setFont(smalltextFont);
/*  85 */     this.demoHelpLink.setHorizontalAlignment(0);
/*  86 */     this.demoHelpLink.addMouseListener(new MouseAdapter()
/*     */         {
/*     */           public void mouseClicked(MouseEvent e)
/*     */           {
/*  90 */             OperatingSystem.openLink(LauncherConstants.URL_DEMO_HELP);
/*     */           }
/*     */         });
/*  93 */     add(this.demoHelpLink, constraints);
/*     */     
/*  95 */     this.playButton.setFont(this.playButton.getFont().deriveFont(1, (this.playButton.getFont().getSize() + 2)));
/*     */   }
/*     */ 
/*     */   
/*     */   public void onProfilesRefreshed(ProfileManager manager) {
/* 100 */     checkState();
/*     */   }
/*     */ 
/*     */   
/*     */   public void checkState() {
/* 105 */     GameLaunchDispatcher.PlayStatus status = this.minecraftLauncher.getLaunchDispatcher().getStatus();
/* 106 */     this.playButton.setText(Language.get("main.button.play"));
/* 107 */     this.playButton.setEnabled(status.canPlay());
/* 108 */     this.demoHelpLink.setVisible((status == GameLaunchDispatcher.PlayStatus.CAN_PLAY_DEMO));
/* 109 */     if (status == GameLaunchDispatcher.PlayStatus.DOWNLOADING) {
/*     */       
/* 111 */       GameInstanceStatus instanceStatus = this.minecraftLauncher.getLaunchDispatcher().getInstanceStatus();
/* 112 */       if (instanceStatus != GameInstanceStatus.IDLE) {
/* 113 */         this.playButton.setText(instanceStatus.getName());
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onVersionsRefreshed(VersionManager manager) {
/* 120 */     SwingUtilities.invokeLater(new Runnable()
/*     */         {
/*     */           public void run()
/*     */           {
/* 124 */             PlayButtonPanel.this.checkState();
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public Launcher getMinecraftLauncher() {
/* 131 */     return this.minecraftLauncher;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onUserChanged(ProfileManager manager) {
/* 136 */     SwingUtilities.invokeLater(new Runnable()
/*     */         {
/*     */           public void run()
/*     */           {
/* 140 */             PlayButtonPanel.this.checkState();
/*     */           }
/*     */         });
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\net\minecraft\launche\\ui\bottombar\PlayButtonPanel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */