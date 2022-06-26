/*     */ package net.minecraft.launcher.ui.bottombar;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.awt.Component;
/*     */ import java.awt.GridBagConstraints;
/*     */ import java.awt.GridBagLayout;
/*     */ import java.awt.GridLayout;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.awt.event.ItemEvent;
/*     */ import java.awt.event.ItemListener;
/*     */ import java.io.IOException;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JComboBox;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JList;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.border.EmptyBorder;
/*     */ import javax.swing.plaf.basic.BasicComboBoxRenderer;
/*     */ import net.minecraft.launcher.Language;
/*     */ import net.minecraft.launcher.Launcher;
/*     */ import net.minecraft.launcher.profile.Profile;
/*     */ import net.minecraft.launcher.profile.ProfileManager;
/*     */ import net.minecraft.launcher.profile.RefreshedProfilesListener;
/*     */ import net.minecraft.launcher.ui.popups.profile.ProfileEditorPopup;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ProfileSelectionPanel
/*     */   extends JPanel
/*     */   implements ActionListener, ItemListener, RefreshedProfilesListener
/*     */ {
/*  38 */   private static final Logger LOGGER = LogManager.getLogger();
/*  39 */   private final JComboBox profileList = new JComboBox();
/*  40 */   private final JButton newProfileButton = new JButton(Language.get("main.button.newprof"));
/*  41 */   private final JButton editProfileButton = new JButton(Language.get("main.button.editprof"));
/*     */   
/*     */   private final Launcher minecraftLauncher;
/*     */   private boolean skipSelectionUpdate;
/*     */   
/*     */   public ProfileSelectionPanel(Launcher minecraftLauncher) {
/*  47 */     this.minecraftLauncher = minecraftLauncher;
/*     */     
/*  49 */     this.profileList.setRenderer(new ProfileListRenderer());
/*  50 */     this.profileList.addItemListener(this);
/*  51 */     this.profileList.addItem("Loading profiles...");
/*     */     
/*  53 */     this.newProfileButton.addActionListener(this);
/*  54 */     this.editProfileButton.addActionListener(this);
/*     */     
/*  56 */     createInterface();
/*     */     
/*  58 */     minecraftLauncher.getProfileManager().addRefreshedProfilesListener(this);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createInterface() {
/*  63 */     setLayout(new GridBagLayout());
/*  64 */     GridBagConstraints constraints = new GridBagConstraints();
/*  65 */     constraints.fill = 2;
/*  66 */     constraints.weightx = 0.0D;
/*     */     
/*  68 */     constraints.gridy = 0;
/*     */     
/*  70 */     add(new JLabel("Profile: "), constraints);
/*  71 */     constraints.gridx = 1;
/*  72 */     add(this.profileList, constraints);
/*  73 */     constraints.gridx = 0;
/*     */     
/*  75 */     constraints.gridy++;
/*     */     
/*  77 */     JPanel buttonPanel = new JPanel(new GridLayout(1, 2));
/*  78 */     buttonPanel.setBorder(new EmptyBorder(2, 0, 0, 0));
/*  79 */     buttonPanel.add(this.newProfileButton);
/*  80 */     buttonPanel.add(this.editProfileButton);
/*     */     
/*  82 */     constraints.gridwidth = 2;
/*  83 */     add(buttonPanel, constraints);
/*  84 */     constraints.gridwidth = 1;
/*     */     
/*  86 */     constraints.gridy++;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onProfilesRefreshed(ProfileManager manager) {
/*  91 */     SwingUtilities.invokeLater(new Runnable()
/*     */         {
/*     */           public void run()
/*     */           {
/*  95 */             ProfileSelectionPanel.this.populateProfiles();
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public void populateProfiles() {
/* 102 */     String previous = this.minecraftLauncher.getProfileManager().getSelectedProfile().getName();
/* 103 */     Profile selected = null;
/* 104 */     List<Profile> profiles = Lists.newArrayList(this.minecraftLauncher.getProfileManager().getProfiles().values());
/* 105 */     this.profileList.removeAllItems();
/*     */     
/* 107 */     Collections.sort(profiles);
/*     */     
/* 109 */     this.skipSelectionUpdate = true;
/* 110 */     for (Profile profile : profiles) {
/*     */       
/* 112 */       if (previous.equals(profile.getName())) {
/* 113 */         selected = profile;
/*     */       }
/* 115 */       this.profileList.addItem(profile);
/*     */     } 
/* 117 */     if (selected == null) {
/*     */       
/* 119 */       if (profiles.isEmpty()) {
/*     */         
/* 121 */         selected = this.minecraftLauncher.getProfileManager().getSelectedProfile();
/* 122 */         this.profileList.addItem(selected);
/*     */       } 
/* 124 */       selected = profiles.iterator().next();
/*     */     } 
/* 126 */     this.profileList.setSelectedItem(selected);
/* 127 */     this.skipSelectionUpdate = false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void itemStateChanged(ItemEvent e) {
/* 132 */     if (e.getStateChange() != 1) {
/*     */       return;
/*     */     }
/* 135 */     if (!this.skipSelectionUpdate && e.getItem() instanceof Profile) {
/*     */       
/* 137 */       Profile profile = (Profile)e.getItem();
/* 138 */       this.minecraftLauncher.getProfileManager().setSelectedProfile(profile.getName());
/*     */       
/*     */       try {
/* 141 */         this.minecraftLauncher.getProfileManager().saveProfiles();
/*     */       }
/* 143 */       catch (IOException e1) {
/*     */         
/* 145 */         LOGGER.error("Couldn't save new selected profile", e1);
/*     */       } 
/* 147 */       this.minecraftLauncher.ensureLoggedIn();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void actionPerformed(ActionEvent e) {
/* 153 */     if (e.getSource() == this.newProfileButton) {
/*     */       
/* 155 */       Profile profile = new Profile(this.minecraftLauncher.getProfileManager().getSelectedProfile());
/* 156 */       profile.setName("Copy of " + profile.getName());
/* 157 */       while (this.minecraftLauncher.getProfileManager().getProfiles().containsKey(profile.getName())) {
/* 158 */         profile.setName(profile.getName() + "_");
/*     */       }
/* 160 */       ProfileEditorPopup.showEditProfileDialog(getMinecraftLauncher(), profile);
/* 161 */       this.minecraftLauncher.getProfileManager().setSelectedProfile(profile.getName());
/*     */     }
/* 163 */     else if (e.getSource() == this.editProfileButton) {
/*     */       
/* 165 */       Profile profile = this.minecraftLauncher.getProfileManager().getSelectedProfile();
/* 166 */       ProfileEditorPopup.showEditProfileDialog(getMinecraftLauncher(), profile);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static class ProfileListRenderer
/*     */     extends BasicComboBoxRenderer {
/*     */     private ProfileListRenderer() {}
/*     */     
/*     */     public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
/* 175 */       if (value instanceof Profile) {
/* 176 */         value = ((Profile)value).getName();
/*     */       }
/* 178 */       super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
/* 179 */       return this;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public Launcher getMinecraftLauncher() {
/* 185 */     return this.minecraftLauncher;
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\net\minecraft\launche\\ui\bottombar\ProfileSelectionPanel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */