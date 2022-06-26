/*     */ package net.minecraft.launcher.ui.popups.profile;
/*     */ 
/*     */ import com.google.common.collect.Sets;
/*     */ import com.mojang.launcher.events.RefreshedVersionsListener;
/*     */ import com.mojang.launcher.updater.VersionManager;
/*     */ import com.mojang.launcher.updater.VersionSyncInfo;
/*     */ import com.mojang.launcher.versions.Version;
/*     */ import java.awt.Component;
/*     */ import java.awt.GridBagConstraints;
/*     */ import java.awt.GridBagLayout;
/*     */ import java.awt.Insets;
/*     */ import java.awt.event.ItemEvent;
/*     */ import java.awt.event.ItemListener;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import javax.swing.BorderFactory;
/*     */ import javax.swing.JCheckBox;
/*     */ import javax.swing.JComboBox;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JList;
/*     */ import javax.swing.JOptionPane;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.plaf.basic.BasicComboBoxRenderer;
/*     */ import net.minecraft.launcher.Language;
/*     */ import net.minecraft.launcher.SwingUserInterface;
/*     */ import net.minecraft.launcher.game.MinecraftReleaseType;
/*     */ import net.minecraft.launcher.profile.Profile;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ProfileVersionPanel
/*     */   extends JPanel
/*     */   implements RefreshedVersionsListener
/*     */ {
/*     */   private final ProfileEditorPopup editor;
/*  38 */   private final JComboBox versionList = new JComboBox();
/*  39 */   private final List<ReleaseTypeCheckBox> customVersionTypes = new ArrayList<>();
/*     */   
/*     */   public ProfileVersionPanel(ProfileEditorPopup editor) {
/*  42 */     this.editor = editor;
/*     */     
/*  44 */     setLayout(new GridBagLayout());
/*  45 */     setBorder(BorderFactory.createTitledBorder(Language.get("main.pe.vertitle")));
/*     */     
/*  47 */     createInterface();
/*  48 */     addEventHandlers();
/*     */     
/*  50 */     List<VersionSyncInfo> versions = editor.getMinecraftLauncher().getLauncher().getVersionManager().getVersions(editor.getProfile().getVersionFilter());
/*  51 */     if (versions.isEmpty()) {
/*  52 */       editor.getMinecraftLauncher().getLauncher().getVersionManager().addRefreshedVersionsListener(this);
/*     */     } else {
/*  54 */       populateVersions(versions);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void createInterface() {
/*  59 */     GridBagConstraints constraints = new GridBagConstraints();
/*  60 */     constraints.insets = new Insets(2, 2, 2, 2);
/*  61 */     constraints.anchor = 17;
/*     */     
/*  63 */     constraints.gridy = 0;
/*  64 */     for (MinecraftReleaseType type : MinecraftReleaseType.values()) {
/*  65 */       if (type.getDescription() != null) {
/*  66 */         ReleaseTypeCheckBox checkbox = new ReleaseTypeCheckBox(type);
/*  67 */         checkbox.setSelected(this.editor.getProfile().getVersionFilter().getTypes().contains(type));
/*  68 */         checkbox.setText(type.getDescription());
/*  69 */         this.customVersionTypes.add(checkbox);
/*     */         
/*  71 */         constraints.fill = 2;
/*  72 */         constraints.weightx = 1.0D;
/*  73 */         constraints.gridwidth = 0;
/*  74 */         add(checkbox, constraints);
/*  75 */         constraints.gridwidth = 1;
/*  76 */         constraints.weightx = 0.0D;
/*  77 */         constraints.fill = 0;
/*     */         
/*  79 */         constraints.gridy++;
/*     */       } 
/*     */     } 
/*  82 */     add(new JLabel(Language.get("main.pe.veruse")), constraints);
/*  83 */     constraints.fill = 2;
/*  84 */     constraints.weightx = 1.0D;
/*  85 */     add(this.versionList, constraints);
/*  86 */     constraints.weightx = 0.0D;
/*  87 */     constraints.fill = 0;
/*     */     
/*  89 */     constraints.gridy++;
/*     */     
/*  91 */     this.versionList.setRenderer(new VersionListRenderer());
/*     */   }
/*     */   
/*     */   protected void addEventHandlers() {
/*  95 */     this.versionList.addItemListener(new ItemListener() {
/*     */           public void itemStateChanged(ItemEvent e) {
/*  97 */             ProfileVersionPanel.this.updateVersionSelection();
/*     */           }
/*     */         });
/* 100 */     for (ReleaseTypeCheckBox type : this.customVersionTypes) {
/* 101 */       type.addItemListener(new ItemListener() {
/*     */             private boolean isUpdating = false;
/*     */             
/*     */             public void itemStateChanged(ItemEvent e) {
/* 105 */               if (this.isUpdating) {
/*     */                 return;
/*     */               }
/* 108 */               if (e.getStateChange() == 1 && type.getType().getPopupWarning() != null) {
/* 109 */                 int result = JOptionPane.showConfirmDialog(((SwingUserInterface)ProfileVersionPanel.this.editor.getMinecraftLauncher().getUserInterface()).getFrame(), type.getType().getPopupWarning() + "\n\nAre you sure you want to continue?");
/*     */                 
/* 111 */                 this.isUpdating = true;
/* 112 */                 if (result == 0) {
/* 113 */                   type.setSelected(true);
/* 114 */                   ProfileVersionPanel.this.updateCustomVersionFilter();
/*     */                 } else {
/* 116 */                   type.setSelected(false);
/*     */                 } 
/* 118 */                 this.isUpdating = false;
/*     */               } else {
/* 120 */                 ProfileVersionPanel.this.updateCustomVersionFilter();
/*     */               } 
/*     */             }
/*     */           });
/*     */     } 
/*     */   }
/*     */   
/*     */   private void updateCustomVersionFilter() {
/* 128 */     Profile profile = this.editor.getProfile();
/* 129 */     Set<MinecraftReleaseType> newTypes = Sets.newHashSet(Profile.DEFAULT_RELEASE_TYPES);
/* 130 */     for (ReleaseTypeCheckBox type : this.customVersionTypes) {
/* 131 */       if (type.isSelected()) {
/* 132 */         newTypes.add(type.getType()); continue;
/*     */       } 
/* 134 */       newTypes.remove(type.getType());
/*     */     } 
/*     */     
/* 137 */     if (newTypes.equals(Profile.DEFAULT_RELEASE_TYPES)) {
/* 138 */       profile.setAllowedReleaseTypes(null);
/*     */     } else {
/* 140 */       profile.setAllowedReleaseTypes(newTypes);
/*     */     } 
/* 142 */     populateVersions(this.editor.getMinecraftLauncher().getLauncher().getVersionManager().getVersions(this.editor.getProfile().getVersionFilter()));
/* 143 */     this.editor.getMinecraftLauncher().getLauncher().getVersionManager().removeRefreshedVersionsListener(this);
/*     */   }
/*     */   
/*     */   private void updateVersionSelection() {
/* 147 */     Object selection = this.versionList.getSelectedItem();
/* 148 */     if (selection instanceof VersionSyncInfo) {
/* 149 */       Version version = ((VersionSyncInfo)selection).getLatestVersion();
/* 150 */       this.editor.getProfile().setLastVersionId(version.getId());
/*     */     } else {
/* 152 */       this.editor.getProfile().setLastVersionId(null);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void populateVersions(List<VersionSyncInfo> versions) {
/* 157 */     String previous = this.editor.getProfile().getLastVersionId();
/* 158 */     VersionSyncInfo selected = null;
/*     */     
/* 160 */     this.versionList.removeAllItems();
/* 161 */     this.versionList.addItem(Language.get("main.pe.verlatest:"));
/* 162 */     for (VersionSyncInfo version : versions) {
/* 163 */       if (version.getLatestVersion().getId().equals(previous)) {
/* 164 */         selected = version;
/*     */       }
/* 166 */       this.versionList.addItem(version);
/*     */     } 
/* 168 */     if (selected == null && !versions.isEmpty()) {
/* 169 */       this.versionList.setSelectedIndex(0);
/*     */     } else {
/* 171 */       this.versionList.setSelectedItem(selected);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void onVersionsRefreshed(final VersionManager manager) {
/* 176 */     SwingUtilities.invokeLater(new Runnable() {
/*     */           public void run() {
/* 178 */             List<VersionSyncInfo> versions = manager.getVersions(ProfileVersionPanel.this.editor.getProfile().getVersionFilter());
/* 179 */             ProfileVersionPanel.this.populateVersions(versions);
/* 180 */             ProfileVersionPanel.this.editor.getMinecraftLauncher().getLauncher().getVersionManager().removeRefreshedVersionsListener(ProfileVersionPanel.this);
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   private static class ReleaseTypeCheckBox
/*     */     extends JCheckBox
/*     */   {
/*     */     private final MinecraftReleaseType type;
/*     */     
/*     */     private ReleaseTypeCheckBox(MinecraftReleaseType type) {
/* 192 */       this.type = type;
/*     */     }
/*     */     
/*     */     public MinecraftReleaseType getType() {
/* 196 */       return this.type;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class VersionListRenderer extends BasicComboBoxRenderer {
/*     */     private VersionListRenderer() {}
/*     */     
/*     */     public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
/* 204 */       if (value instanceof VersionSyncInfo) {
/* 205 */         VersionSyncInfo syncInfo = (VersionSyncInfo)value;
/* 206 */         Version version = syncInfo.getLatestVersion();
/*     */         
/* 208 */         value = String.format("%s %s", new Object[] { version.getType().getName(), version.getId() });
/*     */       } 
/* 210 */       super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
/* 211 */       return this;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\net\minecraft\launche\\ui\popups\profile\ProfileVersionPanel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */