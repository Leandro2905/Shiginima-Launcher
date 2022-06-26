/*     */ package net.minecraft.launcher.ui.popups.login;
/*     */ 
/*     */ import java.awt.Window;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import javax.imageio.ImageIO;
/*     */ import javax.swing.Box;
/*     */ import javax.swing.BoxLayout;
/*     */ import javax.swing.ImageIcon;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JProgressBar;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.border.EmptyBorder;
/*     */ import net.minecraft.launcher.Launcher;
/*     */ 
/*     */ public class LogInPopup
/*     */   extends JPanel
/*     */   implements ActionListener
/*     */ {
/*     */   private final Launcher launcher;
/*     */   private final Callback callback;
/*     */   private final AuthErrorForm errorForm;
/*     */   private final LogInForm logInForm;
/*  29 */   private final JButton loginButton = new JButton("Log In");
/*  30 */   private final JProgressBar progressBar = new JProgressBar();
/*     */   
/*     */   public LogInPopup(Launcher launcher, Callback callback) {
/*  33 */     super(true);
/*  34 */     this.launcher = launcher;
/*  35 */     this.callback = callback;
/*  36 */     this.errorForm = new AuthErrorForm(this);
/*     */     
/*  38 */     this.logInForm = new LogInForm(this);
/*     */     
/*  40 */     createInterface();
/*     */     
/*  42 */     this.loginButton.addActionListener(this);
/*     */   }
/*     */   
/*     */   protected void createInterface() {
/*  46 */     setLayout(new BoxLayout(this, 1));
/*  47 */     setBorder(new EmptyBorder(5, 15, 5, 15));
/*     */     try {
/*  49 */       InputStream stream = LogInPopup.class.getResourceAsStream("/Minecraft_Logo.png");
/*  50 */       if (stream != null) {
/*  51 */         BufferedImage image = ImageIO.read(stream);
/*  52 */         JLabel label = new JLabel(new ImageIcon(image));
/*  53 */         JPanel imagePanel = new JPanel();
/*  54 */         imagePanel.add(label);
/*  55 */         add(imagePanel);
/*  56 */         add(Box.createVerticalStrut(10));
/*     */       } 
/*  58 */     } catch (IOException e) {
/*  59 */       e.printStackTrace();
/*     */     } 
/*     */     
/*  62 */     add(this.errorForm);
/*  63 */     add(this.logInForm);
/*     */     
/*  65 */     add(Box.createVerticalStrut(15));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  71 */     this.progressBar.setIndeterminate(true);
/*  72 */     this.progressBar.setVisible(false);
/*  73 */     add(this.progressBar);
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
/*     */   public void actionPerformed(ActionEvent e) {}
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
/*     */   public static void showLoginPrompt(Launcher launcher, final Callback callback) {
/*  97 */     System.out.println("SLP3");
/*  98 */     SwingUtilities.invokeLater(new Runnable() {
/*     */           public void run() {
/* 100 */             LogInPopup popup = new LogInPopup(Launcher.getInstance(), callback);
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public Launcher getLauncher() {
/* 107 */     return this.launcher;
/*     */   }
/*     */   
/*     */   public void setCanLogIn(final boolean enabled) {
/* 111 */     if (SwingUtilities.isEventDispatchThread()) {
/* 112 */       this.loginButton.setEnabled(enabled);
/* 113 */       this.progressBar.setIndeterminate(false);
/* 114 */       this.progressBar.setIndeterminate(true);
/*     */       
/* 116 */       this.progressBar.setVisible(!enabled);
/* 117 */       repack();
/*     */     } else {
/* 119 */       SwingUtilities.invokeLater(new Runnable() {
/*     */             public void run() {
/* 121 */               LogInPopup.this.setCanLogIn(enabled);
/*     */             }
/*     */           });
/*     */     } 
/*     */   }
/*     */   
/*     */   public LogInForm getLogInForm() {
/* 128 */     return this.logInForm;
/*     */   }
/*     */   
/*     */   public AuthErrorForm getErrorForm() {
/* 132 */     return this.errorForm;
/*     */   }
/*     */   
/*     */   public void setLoggedIn(String uuid) {
/* 136 */     this.callback.onLogIn(uuid);
/*     */   }
/*     */   
/*     */   public void repack() {
/* 140 */     Window window = SwingUtilities.windowForComponent(this);
/* 141 */     if (window != null) {
/* 142 */       window.pack();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Launcher getMinecraftLauncher() {
/* 153 */     return this.launcher;
/*     */   }
/*     */   
/*     */   public static interface Callback {
/*     */     void onLogIn(String param1String);
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\net\minecraft\launche\\ui\popups\login\LogInPopup.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */