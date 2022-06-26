/*     */ package net.minecraft.launcher.ui;
/*     */ 
/*     */ import com.mojang.launcher.OperatingSystem;
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.CardLayout;
/*     */ import java.awt.Component;
/*     */ import java.awt.GridBagLayout;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JProgressBar;
/*     */ import net.mc.main.Main;
/*     */ import net.minecraft.launcher.Launcher;
/*     */ import net.minecraft.launcher.ui.tabs.LauncherTabPanel;
/*     */ import org.apache.commons.lang3.SystemUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LauncherPanel
/*     */   extends JPanel
/*     */ {
/*     */   public static final String CARD_DIRT_BACKGROUND = "loading";
/*     */   public static final String CARD_LOGIN = "login";
/*     */   public static final String CARD_LAUNCHER = "launcher";
/*     */   private final CardLayout cardLayout;
/*     */   private final LauncherTabPanel tabPanel;
/*     */   private final BottomBarPanel bottomBar;
/*     */   private final JProgressBar progressBar;
/*     */   private final Launcher minecraftLauncher;
/*     */   private final JPanel loginPanel;
/*     */   private JLabel warningLabel;
/*     */   
/*     */   public LauncherPanel(Launcher minecraftLauncher) {
/*  37 */     this.minecraftLauncher = minecraftLauncher;
/*  38 */     this.cardLayout = new CardLayout();
/*  39 */     setLayout(this.cardLayout);
/*     */     
/*  41 */     this.progressBar = new JProgressBar();
/*  42 */     this.bottomBar = new BottomBarPanel(minecraftLauncher);
/*  43 */     this.tabPanel = new LauncherTabPanel(minecraftLauncher);
/*  44 */     this.loginPanel = new TexturedPanel("/dirt.png");
/*  45 */     createInterface();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createInterface() {
/*  50 */     add(createLauncherInterface(), "launcher");
/*  51 */     add(createDirtInterface(), "loading");
/*  52 */     add(createLoginInterface(), "login");
/*     */   }
/*     */ 
/*     */   
/*     */   protected JPanel createLauncherInterface() {
/*  57 */     JPanel result = new JPanel(new BorderLayout());
/*     */     
/*  59 */     this.tabPanel.getBlog().setPage("http://servers.teamshiginima.com/");
/*  60 */     this.tabPanel.ggg().setPage(Main.npg);
/*     */     
/*  62 */     boolean javaBootstrap = (getMinecraftLauncher().getBootstrapVersion().intValue() < 100);
/*  63 */     boolean upgradableOS = (OperatingSystem.getCurrentPlatform() == OperatingSystem.WINDOWS);
/*  64 */     if (OperatingSystem.getCurrentPlatform() == OperatingSystem.OSX) {
/*     */       
/*  66 */       String ver = SystemUtils.OS_VERSION;
/*  67 */       if (ver != null && !ver.isEmpty()) {
/*     */         
/*  69 */         String[] split = ver.split("\\.", 3);
/*  70 */         if (split.length >= 2) {
/*     */           
/*     */           try {
/*  73 */             int major = Integer.parseInt(split[0]);
/*  74 */             int minor = Integer.parseInt(split[1]);
/*  75 */             if (major == 10) {
/*  76 */               upgradableOS = (minor >= 8);
/*  77 */             } else if (major > 10) {
/*  78 */               upgradableOS = true;
/*     */             }
/*     */           
/*  81 */           } catch (NumberFormatException numberFormatException) {}
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/*  86 */     JPanel center = new JPanel();
/*  87 */     center.setLayout(new BorderLayout());
/*  88 */     center.add((Component)this.tabPanel, "Center");
/*  89 */     center.add(this.progressBar, "South");
/*     */     
/*  91 */     this.progressBar.setVisible(false);
/*  92 */     this.progressBar.setMinimum(0);
/*  93 */     this.progressBar.setMaximum(100);
/*  94 */     this.progressBar.setStringPainted(true);
/*     */     
/*  96 */     result.add(center, "Center");
/*  97 */     result.add(this.bottomBar, "South");
/*     */     
/*  99 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   protected JPanel createDirtInterface() {
/* 104 */     return new TexturedPanel("/dirt.png");
/*     */   }
/*     */ 
/*     */   
/*     */   protected JPanel createLoginInterface() {
/* 109 */     this.loginPanel.setLayout(new GridBagLayout());
/* 110 */     return this.loginPanel;
/*     */   }
/*     */ 
/*     */   
/*     */   public LauncherTabPanel getTabPanel() {
/* 115 */     return this.tabPanel;
/*     */   }
/*     */ 
/*     */   
/*     */   public BottomBarPanel getBottomBar() {
/* 120 */     return this.bottomBar;
/*     */   }
/*     */ 
/*     */   
/*     */   public JProgressBar getProgressBar() {
/* 125 */     return this.progressBar;
/*     */   }
/*     */ 
/*     */   
/*     */   public Launcher getMinecraftLauncher() {
/* 130 */     return this.minecraftLauncher;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setCard(String card, JPanel additional) {
/* 135 */     if (card.equals("login")) {
/*     */       
/* 137 */       this.loginPanel.removeAll();
/* 138 */       this.loginPanel.add(additional);
/*     */     } 
/* 140 */     this.cardLayout.show(this, card);
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\net\minecraft\launche\\ui\LauncherPanel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */