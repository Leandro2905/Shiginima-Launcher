/*     */ package net.minecraft.launcher.ui.popups.profile;
/*     */ 
/*     */ import com.mojang.launcher.OperatingSystem;
/*     */ import java.awt.GridBagConstraints;
/*     */ import java.awt.GridBagLayout;
/*     */ import java.awt.Insets;
/*     */ import java.awt.event.ItemEvent;
/*     */ import java.awt.event.ItemListener;
/*     */ import javax.swing.BorderFactory;
/*     */ import javax.swing.JCheckBox;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JTextField;
/*     */ import javax.swing.event.DocumentEvent;
/*     */ import javax.swing.event.DocumentListener;
/*     */ import net.minecraft.launcher.Language;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ProfileJavaPanel
/*     */   extends JPanel
/*     */ {
/*     */   private final ProfileEditorPopup editor;
/*  23 */   private final JCheckBox javaPathCustom = new JCheckBox(Language.get("main.pe.exec"));
/*  24 */   private final JTextField javaPathField = new JTextField();
/*  25 */   private final JCheckBox javaArgsCustom = new JCheckBox(Language.get("main.pe.jvmarg"));
/*  26 */   private final JTextField javaArgsField = new JTextField();
/*     */   
/*     */   public ProfileJavaPanel(ProfileEditorPopup editor) {
/*  29 */     this.editor = editor;
/*     */     
/*  31 */     setLayout(new GridBagLayout());
/*  32 */     setBorder(BorderFactory.createTitledBorder(Language.get("main.pe.jset")));
/*     */     
/*  34 */     createInterface();
/*  35 */     fillDefaultValues();
/*  36 */     addEventHandlers();
/*     */   }
/*     */   
/*     */   protected void createInterface() {
/*  40 */     GridBagConstraints constraints = new GridBagConstraints();
/*  41 */     constraints.insets = new Insets(2, 2, 2, 2);
/*  42 */     constraints.anchor = 17;
/*     */     
/*  44 */     constraints.gridy = 0;
/*     */     
/*  46 */     add(this.javaPathCustom, constraints);
/*  47 */     constraints.fill = 2;
/*  48 */     constraints.weightx = 1.0D;
/*  49 */     add(this.javaPathField, constraints);
/*  50 */     constraints.weightx = 0.0D;
/*  51 */     constraints.fill = 0;
/*     */     
/*  53 */     constraints.gridy++;
/*     */     
/*  55 */     add(this.javaArgsCustom, constraints);
/*  56 */     constraints.fill = 2;
/*  57 */     constraints.weightx = 1.0D;
/*  58 */     add(this.javaArgsField, constraints);
/*  59 */     constraints.weightx = 0.0D;
/*  60 */     constraints.fill = 0;
/*     */     
/*  62 */     constraints.gridy++;
/*     */   }
/*     */   
/*     */   protected void fillDefaultValues() {
/*  66 */     String javaPath = this.editor.getProfile().getJavaPath();
/*  67 */     if (javaPath != null) {
/*  68 */       this.javaPathCustom.setSelected(true);
/*  69 */       this.javaPathField.setText(javaPath);
/*     */     } else {
/*  71 */       this.javaPathCustom.setSelected(false);
/*  72 */       this.javaPathField.setText(OperatingSystem.getCurrentPlatform().getJavaDir());
/*     */     } 
/*  74 */     updateJavaPathState();
/*     */     
/*  76 */     String args = this.editor.getProfile().getJavaArgs();
/*  77 */     if (args != null) {
/*  78 */       this.javaArgsCustom.setSelected(true);
/*  79 */       this.javaArgsField.setText(args);
/*     */     } else {
/*  81 */       this.javaArgsCustom.setSelected(false);
/*  82 */       this.javaArgsField.setText("-Xmx1G -XX:+UseConcMarkSweepGC -XX:+CMSIncrementalMode -XX:-UseAdaptiveSizePolicy -Xmn128M");
/*     */     } 
/*  84 */     updateJavaArgsState();
/*     */   }
/*     */   
/*     */   protected void addEventHandlers() {
/*  88 */     this.javaPathCustom.addItemListener(new ItemListener() {
/*     */           public void itemStateChanged(ItemEvent e) {
/*  90 */             ProfileJavaPanel.this.updateJavaPathState();
/*     */           }
/*     */         });
/*  93 */     this.javaPathField.getDocument().addDocumentListener(new DocumentListener() {
/*     */           public void insertUpdate(DocumentEvent e) {
/*  95 */             ProfileJavaPanel.this.updateJavaPath();
/*     */           }
/*     */           
/*     */           public void removeUpdate(DocumentEvent e) {
/*  99 */             ProfileJavaPanel.this.updateJavaPath();
/*     */           }
/*     */           
/*     */           public void changedUpdate(DocumentEvent e) {
/* 103 */             ProfileJavaPanel.this.updateJavaPath();
/*     */           }
/*     */         });
/* 106 */     this.javaArgsCustom.addItemListener(new ItemListener() {
/*     */           public void itemStateChanged(ItemEvent e) {
/* 108 */             ProfileJavaPanel.this.updateJavaArgsState();
/*     */           }
/*     */         });
/* 111 */     this.javaArgsField.getDocument().addDocumentListener(new DocumentListener() {
/*     */           public void insertUpdate(DocumentEvent e) {
/* 113 */             ProfileJavaPanel.this.updateJavaArgs();
/*     */           }
/*     */           
/*     */           public void removeUpdate(DocumentEvent e) {
/* 117 */             ProfileJavaPanel.this.updateJavaArgs();
/*     */           }
/*     */           
/*     */           public void changedUpdate(DocumentEvent e) {
/* 121 */             ProfileJavaPanel.this.updateJavaArgs();
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   private void updateJavaPath() {
/* 127 */     if (this.javaPathCustom.isSelected()) {
/* 128 */       this.editor.getProfile().setJavaDir(this.javaPathField.getText());
/*     */     } else {
/* 130 */       this.editor.getProfile().setJavaDir(null);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void updateJavaPathState() {
/* 135 */     if (this.javaPathCustom.isSelected()) {
/* 136 */       this.javaPathField.setEnabled(true);
/* 137 */       this.editor.getProfile().setJavaDir(this.javaPathField.getText());
/*     */     } else {
/* 139 */       this.javaPathField.setEnabled(false);
/* 140 */       this.editor.getProfile().setJavaDir(null);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void updateJavaArgs() {
/* 145 */     if (this.javaArgsCustom.isSelected()) {
/* 146 */       this.editor.getProfile().setJavaArgs(this.javaArgsField.getText());
/*     */     } else {
/* 148 */       this.editor.getProfile().setJavaArgs(null);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void updateJavaArgsState() {
/* 153 */     if (this.javaArgsCustom.isSelected()) {
/* 154 */       this.javaArgsField.setEnabled(true);
/* 155 */       this.editor.getProfile().setJavaArgs(this.javaArgsField.getText());
/*     */     } else {
/* 157 */       this.javaArgsField.setEnabled(false);
/* 158 */       this.editor.getProfile().setJavaArgs(null);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\net\minecraft\launche\\ui\popups\profile\ProfileJavaPanel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */