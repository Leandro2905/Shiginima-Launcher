/*     */ package net.minecraft.launcher.ui.popups.profile;
/*     */ 
/*     */ import com.mojang.launcher.OperatingSystem;
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Window;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.awt.event.WindowEvent;
/*     */ import java.io.IOException;
/*     */ import java.util.Map;
/*     */ import javax.swing.Box;
/*     */ import javax.swing.BoxLayout;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JDialog;
/*     */ import javax.swing.JFrame;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.border.EmptyBorder;
/*     */ import net.minecraft.launcher.Language;
/*     */ import net.minecraft.launcher.Launcher;
/*     */ import net.minecraft.launcher.SwingUserInterface;
/*     */ import net.minecraft.launcher.profile.Profile;
/*     */ import net.minecraft.launcher.profile.ProfileManager;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class ProfileEditorPopup
/*     */   extends JPanel
/*     */   implements ActionListener {
/*  30 */   private static final Logger LOGGER = LogManager.getLogger();
/*     */   private final Launcher minecraftLauncher;
/*     */   private final Profile originalProfile;
/*     */   private final Profile profile;
/*  34 */   private final JButton saveButton = new JButton(Language.get("main.pe.button.save"));
/*  35 */   private final JButton cancelButton = new JButton(Language.get("main.pe.button.cancel"));
/*  36 */   private final JButton browseButton = new JButton(Language.get("main.pe.button.opendir"));
/*     */   
/*     */   private final ProfileInfoPanel profileInfoPanel;
/*     */   private final ProfileVersionPanel profileVersionPanel;
/*     */   private final ProfileJavaPanel javaInfoPanel;
/*     */   
/*     */   public ProfileEditorPopup(Launcher minecraftLauncher, Profile profile) {
/*  43 */     super(true);
/*     */     
/*  45 */     this.minecraftLauncher = minecraftLauncher;
/*  46 */     this.originalProfile = profile;
/*  47 */     this.profile = new Profile(profile);
/*  48 */     this.profileInfoPanel = new ProfileInfoPanel(this);
/*  49 */     this.profileVersionPanel = new ProfileVersionPanel(this);
/*  50 */     this.javaInfoPanel = new ProfileJavaPanel(this);
/*     */     
/*  52 */     this.saveButton.addActionListener(this);
/*  53 */     this.cancelButton.addActionListener(this);
/*  54 */     this.browseButton.addActionListener(this);
/*     */     
/*  56 */     setBorder(new EmptyBorder(5, 5, 5, 5));
/*  57 */     setLayout(new BorderLayout(0, 5));
/*  58 */     createInterface();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createInterface() {
/*  63 */     JPanel standardPanels = new JPanel(true);
/*  64 */     standardPanels.setLayout(new BoxLayout(standardPanels, 1));
/*  65 */     standardPanels.add(this.profileInfoPanel);
/*  66 */     standardPanels.add(this.profileVersionPanel);
/*  67 */     standardPanels.add(this.javaInfoPanel);
/*     */     
/*  69 */     add(standardPanels, "Center");
/*     */     
/*  71 */     JPanel buttonPannel = new JPanel();
/*  72 */     buttonPannel.setLayout(new BoxLayout(buttonPannel, 0));
/*  73 */     buttonPannel.add(this.cancelButton);
/*  74 */     buttonPannel.add(Box.createGlue());
/*  75 */     buttonPannel.add(this.browseButton);
/*  76 */     buttonPannel.add(Box.createHorizontalStrut(5));
/*  77 */     buttonPannel.add(this.saveButton);
/*  78 */     add(buttonPannel, "South");
/*     */   }
/*     */ 
/*     */   
/*     */   public void actionPerformed(ActionEvent e) {
/*  83 */     if (e.getSource() == this.saveButton) {
/*     */ 
/*     */       
/*     */       try {
/*  87 */         ProfileManager manager = this.minecraftLauncher.getProfileManager();
/*  88 */         Map<String, Profile> profiles = manager.getProfiles();
/*  89 */         String selected = manager.getSelectedProfile().getName();
/*  90 */         if (!this.originalProfile.getName().equals(this.profile.getName())) {
/*     */           
/*  92 */           profiles.remove(this.originalProfile.getName());
/*  93 */           while (profiles.containsKey(this.profile.getName())) {
/*  94 */             this.profile.setName(this.profile.getName() + "_");
/*     */           }
/*     */         } 
/*  97 */         profiles.put(this.profile.getName(), this.profile);
/*  98 */         if (selected.equals(this.originalProfile.getName())) {
/*  99 */           manager.setSelectedProfile(this.profile.getName());
/*     */         }
/* 101 */         manager.saveProfiles();
/* 102 */         manager.fireRefreshEvent();
/*     */       }
/* 104 */       catch (IOException ex) {
/*     */         
/* 106 */         LOGGER.error("Couldn't save profiles whilst editing " + this.profile.getName(), ex);
/*     */       } 
/* 108 */       closeWindow();
/*     */     }
/* 110 */     else if (e.getSource() == this.browseButton) {
/*     */       
/* 112 */       OperatingSystem.openFolder((this.profile.getGameDir() == null) ? this.minecraftLauncher.getLauncher().getWorkingDirectory() : this.profile.getGameDir());
/*     */     }
/*     */     else {
/*     */       
/* 116 */       closeWindow();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void closeWindow() {
/* 122 */     if (SwingUtilities.isEventDispatchThread()) {
/*     */       
/* 124 */       Window window = (Window)getTopLevelAncestor();
/* 125 */       window.dispatchEvent(new WindowEvent(window, 201));
/*     */     }
/*     */     else {
/*     */       
/* 129 */       SwingUtilities.invokeLater(new Runnable()
/*     */           {
/*     */             public void run()
/*     */             {
/* 133 */               ProfileEditorPopup.this.closeWindow();
/*     */             }
/*     */           });
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Launcher getMinecraftLauncher() {
/* 141 */     return this.minecraftLauncher;
/*     */   }
/*     */ 
/*     */   
/*     */   public Profile getProfile() {
/* 146 */     return this.profile;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void showEditProfileDialog(Launcher minecraftLauncher, Profile profile) {
/* 151 */     JFrame frame = ((SwingUserInterface)minecraftLauncher.getUserInterface()).getFrame();
/* 152 */     JDialog dialog = new JDialog(frame, Language.get("main.pe.title"), true);
/* 153 */     ProfileEditorPopup editor = new ProfileEditorPopup(minecraftLauncher, profile);
/* 154 */     dialog.add(editor);
/* 155 */     dialog.pack();
/* 156 */     dialog.setLocationRelativeTo(frame);
/* 157 */     dialog.setVisible(true);
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\net\minecraft\launche\\ui\popups\profile\ProfileEditorPopup.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */