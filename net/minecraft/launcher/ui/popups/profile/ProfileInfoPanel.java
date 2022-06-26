/*     */ package net.minecraft.launcher.ui.popups.profile;
/*     */ 
/*     */ import java.awt.GridBagConstraints;
/*     */ import java.awt.GridBagLayout;
/*     */ import java.awt.Insets;
/*     */ import java.awt.event.ItemEvent;
/*     */ import java.awt.event.ItemListener;
/*     */ import java.io.File;
/*     */ import javax.swing.BorderFactory;
/*     */ import javax.swing.Box;
/*     */ import javax.swing.BoxLayout;
/*     */ import javax.swing.JCheckBox;
/*     */ import javax.swing.JComboBox;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JTextField;
/*     */ import javax.swing.event.DocumentEvent;
/*     */ import javax.swing.event.DocumentListener;
/*     */ import net.minecraft.launcher.Language;
/*     */ import net.minecraft.launcher.profile.LauncherVisibilityRule;
/*     */ import net.minecraft.launcher.profile.Profile;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ProfileInfoPanel
/*     */   extends JPanel
/*     */ {
/*     */   private final ProfileEditorPopup editor;
/*  30 */   private final JCheckBox gameDirCustom = new JCheckBox(Language.get("main.pe.gamedir"));
/*  31 */   private final JTextField profileName = new JTextField();
/*  32 */   private final JTextField gameDirField = new JTextField();
/*  33 */   private final JCheckBox resolutionCustom = new JCheckBox(Language.get("main.pe.res"));
/*  34 */   private final JTextField resolutionWidth = new JTextField();
/*  35 */   private final JTextField resolutionHeight = new JTextField();
/*     */   
/*  37 */   private final JCheckBox launcherVisibilityCustom = new JCheckBox(Language.get("main.pe.vis"));
/*  38 */   private final JComboBox launcherVisibilityOption = new JComboBox();
/*     */ 
/*     */   
/*     */   public ProfileInfoPanel(ProfileEditorPopup editor) {
/*  42 */     this.editor = editor;
/*     */     
/*  44 */     setLayout(new GridBagLayout());
/*  45 */     setBorder(BorderFactory.createTitledBorder("Profile Info"));
/*     */     
/*  47 */     createInterface();
/*  48 */     fillDefaultValues();
/*  49 */     addEventHandlers();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createInterface() {
/*  54 */     GridBagConstraints constraints = new GridBagConstraints();
/*  55 */     constraints.insets = new Insets(2, 2, 2, 2);
/*  56 */     constraints.anchor = 17;
/*     */     
/*  58 */     constraints.gridy = 0;
/*     */     
/*  60 */     add(new JLabel(Language.get("main.pe.pname")), constraints);
/*  61 */     constraints.fill = 2;
/*  62 */     constraints.weightx = 1.0D;
/*  63 */     add(this.profileName, constraints);
/*  64 */     constraints.weightx = 0.0D;
/*  65 */     constraints.fill = 0;
/*     */     
/*  67 */     constraints.gridy++;
/*     */     
/*  69 */     add(this.gameDirCustom, constraints);
/*  70 */     constraints.fill = 2;
/*  71 */     constraints.weightx = 1.0D;
/*  72 */     add(this.gameDirField, constraints);
/*  73 */     constraints.weightx = 0.0D;
/*  74 */     constraints.fill = 0;
/*     */     
/*  76 */     constraints.gridy++;
/*     */     
/*  78 */     JPanel resolutionPanel = new JPanel();
/*  79 */     resolutionPanel.setLayout(new BoxLayout(resolutionPanel, 0));
/*  80 */     resolutionPanel.add(this.resolutionWidth);
/*  81 */     resolutionPanel.add(Box.createHorizontalStrut(5));
/*  82 */     resolutionPanel.add(new JLabel("x"));
/*  83 */     resolutionPanel.add(Box.createHorizontalStrut(5));
/*  84 */     resolutionPanel.add(this.resolutionHeight);
/*     */     
/*  86 */     add(this.resolutionCustom, constraints);
/*  87 */     constraints.fill = 2;
/*  88 */     constraints.weightx = 1.0D;
/*  89 */     add(resolutionPanel, constraints);
/*     */     
/*  91 */     constraints.gridwidth = 1;
/*  92 */     constraints.weightx = 0.0D;
/*  93 */     constraints.fill = 0;
/*     */     
/*  95 */     constraints.gridy++;
/*     */     
/*  97 */     add(this.launcherVisibilityCustom, constraints);
/*  98 */     constraints.fill = 2;
/*  99 */     constraints.weightx = 1.0D;
/* 100 */     add(this.launcherVisibilityOption, constraints);
/* 101 */     constraints.weightx = 0.0D;
/* 102 */     constraints.fill = 0;
/*     */     
/* 104 */     constraints.gridy++;
/* 105 */     for (LauncherVisibilityRule value : LauncherVisibilityRule.values()) {
/* 106 */       this.launcherVisibilityOption.addItem(value);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void fillDefaultValues() {
/* 112 */     this.profileName.setText(this.editor.getProfile().getName());
/*     */     
/* 114 */     File gameDir = this.editor.getProfile().getGameDir();
/* 115 */     if (gameDir != null) {
/*     */       
/* 117 */       this.gameDirCustom.setSelected(true);
/* 118 */       this.gameDirField.setText(gameDir.getAbsolutePath());
/*     */     }
/*     */     else {
/*     */       
/* 122 */       this.gameDirCustom.setSelected(false);
/* 123 */       this.gameDirField.setText(this.editor.getMinecraftLauncher().getLauncher().getWorkingDirectory().getAbsolutePath());
/*     */     } 
/* 125 */     updateGameDirState();
/*     */     
/* 127 */     Profile.Resolution resolution = this.editor.getProfile().getResolution();
/* 128 */     this.resolutionCustom.setSelected((resolution != null));
/* 129 */     if (resolution == null) {
/* 130 */       resolution = Profile.DEFAULT_RESOLUTION;
/*     */     }
/* 132 */     this.resolutionWidth.setText(String.valueOf(resolution.getWidth()));
/* 133 */     this.resolutionHeight.setText(String.valueOf(resolution.getHeight()));
/* 134 */     updateResolutionState();
/*     */ 
/*     */ 
/*     */     
/* 138 */     LauncherVisibilityRule visibility = this.editor.getProfile().getLauncherVisibilityOnGameClose();
/* 139 */     if (visibility != null) {
/*     */       
/* 141 */       this.launcherVisibilityCustom.setSelected(true);
/* 142 */       this.launcherVisibilityOption.setSelectedItem(visibility);
/*     */     }
/*     */     else {
/*     */       
/* 146 */       this.launcherVisibilityCustom.setSelected(false);
/* 147 */       this.launcherVisibilityOption.setSelectedItem(Profile.DEFAULT_LAUNCHER_VISIBILITY);
/*     */     } 
/* 149 */     updateLauncherVisibilityState();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addEventHandlers() {
/* 154 */     this.profileName.getDocument().addDocumentListener(new DocumentListener()
/*     */         {
/*     */           public void insertUpdate(DocumentEvent e)
/*     */           {
/* 158 */             ProfileInfoPanel.this.updateProfileName();
/*     */           }
/*     */ 
/*     */           
/*     */           public void removeUpdate(DocumentEvent e) {
/* 163 */             ProfileInfoPanel.this.updateProfileName();
/*     */           }
/*     */ 
/*     */           
/*     */           public void changedUpdate(DocumentEvent e) {
/* 168 */             ProfileInfoPanel.this.updateProfileName();
/*     */           }
/*     */         });
/* 171 */     this.gameDirCustom.addItemListener(new ItemListener()
/*     */         {
/*     */           public void itemStateChanged(ItemEvent e)
/*     */           {
/* 175 */             ProfileInfoPanel.this.updateGameDirState();
/*     */           }
/*     */         });
/* 178 */     this.gameDirField.getDocument().addDocumentListener(new DocumentListener()
/*     */         {
/*     */           public void insertUpdate(DocumentEvent e)
/*     */           {
/* 182 */             ProfileInfoPanel.this.updateGameDir();
/*     */           }
/*     */ 
/*     */           
/*     */           public void removeUpdate(DocumentEvent e) {
/* 187 */             ProfileInfoPanel.this.updateGameDir();
/*     */           }
/*     */ 
/*     */           
/*     */           public void changedUpdate(DocumentEvent e) {
/* 192 */             ProfileInfoPanel.this.updateGameDir();
/*     */           }
/*     */         });
/* 195 */     this.resolutionCustom.addItemListener(new ItemListener()
/*     */         {
/*     */           public void itemStateChanged(ItemEvent e)
/*     */           {
/* 199 */             ProfileInfoPanel.this.updateResolutionState();
/*     */           }
/*     */         });
/* 202 */     DocumentListener resolutionListener = new DocumentListener()
/*     */       {
/*     */         public void insertUpdate(DocumentEvent e)
/*     */         {
/* 206 */           ProfileInfoPanel.this.updateResolution();
/*     */         }
/*     */ 
/*     */         
/*     */         public void removeUpdate(DocumentEvent e) {
/* 211 */           ProfileInfoPanel.this.updateResolution();
/*     */         }
/*     */ 
/*     */         
/*     */         public void changedUpdate(DocumentEvent e) {
/* 216 */           ProfileInfoPanel.this.updateResolution();
/*     */         }
/*     */       };
/* 219 */     this.resolutionWidth.getDocument().addDocumentListener(resolutionListener);
/* 220 */     this.resolutionHeight.getDocument().addDocumentListener(resolutionListener);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 229 */     this.launcherVisibilityCustom.addItemListener(new ItemListener()
/*     */         {
/*     */           public void itemStateChanged(ItemEvent e)
/*     */           {
/* 233 */             ProfileInfoPanel.this.updateLauncherVisibilityState();
/*     */           }
/*     */         });
/* 236 */     this.launcherVisibilityOption.addItemListener(new ItemListener()
/*     */         {
/*     */           public void itemStateChanged(ItemEvent e)
/*     */           {
/* 240 */             ProfileInfoPanel.this.updateLauncherVisibilitySelection();
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   private void updateLauncherVisibilityState() {
/* 247 */     Profile profile = this.editor.getProfile();
/* 248 */     if (this.launcherVisibilityCustom.isSelected() && this.launcherVisibilityOption.getSelectedItem() instanceof LauncherVisibilityRule) {
/*     */       
/* 250 */       profile.setLauncherVisibilityOnGameClose((LauncherVisibilityRule)this.launcherVisibilityOption.getSelectedItem());
/* 251 */       this.launcherVisibilityOption.setEnabled(true);
/*     */     }
/*     */     else {
/*     */       
/* 255 */       profile.setLauncherVisibilityOnGameClose(null);
/* 256 */       this.launcherVisibilityOption.setEnabled(false);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void updateLauncherVisibilitySelection() {
/* 262 */     Profile profile = this.editor.getProfile();
/* 263 */     if (this.launcherVisibilityOption.getSelectedItem() instanceof LauncherVisibilityRule) {
/* 264 */       profile.setLauncherVisibilityOnGameClose((LauncherVisibilityRule)this.launcherVisibilityOption.getSelectedItem());
/*     */     }
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
/*     */ 
/*     */   
/*     */   private void updateProfileName() {
/* 280 */     if (this.profileName.getText().length() > 0) {
/* 281 */       this.editor.getProfile().setName(this.profileName.getText());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private void updateGameDirState() {
/* 287 */     if (this.gameDirCustom.isSelected()) {
/*     */       
/* 289 */       this.gameDirField.setEnabled(true);
/* 290 */       this.editor.getProfile().setGameDir(new File(this.gameDirField.getText()));
/*     */     }
/*     */     else {
/*     */       
/* 294 */       this.gameDirField.setEnabled(false);
/* 295 */       this.editor.getProfile().setGameDir(null);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void updateResolutionState() {
/* 301 */     if (this.resolutionCustom.isSelected()) {
/*     */       
/* 303 */       this.resolutionWidth.setEnabled(true);
/* 304 */       this.resolutionHeight.setEnabled(true);
/* 305 */       updateResolution();
/*     */     }
/*     */     else {
/*     */       
/* 309 */       this.resolutionWidth.setEnabled(false);
/* 310 */       this.resolutionHeight.setEnabled(false);
/* 311 */       this.editor.getProfile().setResolution(null);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void updateResolution() {
/*     */     try {
/* 319 */       int width = Integer.parseInt(this.resolutionWidth.getText());
/* 320 */       int height = Integer.parseInt(this.resolutionHeight.getText());
/*     */       
/* 322 */       this.editor.getProfile().setResolution(new Profile.Resolution(width, height));
/*     */     }
/* 324 */     catch (NumberFormatException ignored) {
/*     */       
/* 326 */       this.editor.getProfile().setResolution(null);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void updateGameDir() {
/* 332 */     File file = new File(this.gameDirField.getText());
/* 333 */     this.editor.getProfile().setGameDir(file);
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\net\minecraft\launche\\ui\popups\profile\ProfileInfoPanel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */