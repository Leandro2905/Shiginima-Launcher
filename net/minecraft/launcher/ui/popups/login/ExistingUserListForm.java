/*     */ package net.minecraft.launcher.ui.popups.login;
/*     */ 
/*     */ import com.google.common.base.Objects;
/*     */ import com.mojang.authlib.UserAuthentication;
/*     */ import com.mojang.authlib.exceptions.AuthenticationException;
/*     */ import com.mojang.util.UUIDTypeAdapter;
/*     */ import java.awt.Font;
/*     */ import java.awt.GridBagConstraints;
/*     */ import java.awt.GridBagLayout;
/*     */ import java.awt.Insets;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.io.IOException;
/*     */ import javax.swing.Box;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JComboBox;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JPopupMenu;
/*     */ import javax.swing.SwingUtilities;
/*     */ import net.minecraft.launcher.profile.AuthenticationDatabase;
/*     */ import net.minecraft.launcher.profile.ProfileManager;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ExistingUserListForm
/*     */   extends JPanel
/*     */   implements ActionListener
/*     */ {
/*  35 */   private static final Logger LOGGER = LogManager.getLogger();
/*     */   private final LogInPopup popup;
/*  37 */   private final JComboBox userDropdown = new JComboBox();
/*     */   private final AuthenticationDatabase authDatabase;
/*  39 */   private final JButton playButton = new JButton("Play");
/*  40 */   private final JButton logOutButton = new JButton("Log Out");
/*     */   
/*     */   private final ProfileManager profileManager;
/*     */   
/*     */   public ExistingUserListForm(LogInPopup popup) {
/*  45 */     this.popup = popup;
/*  46 */     this.profileManager = popup.getMinecraftLauncher().getProfileManager();
/*  47 */     this.authDatabase = popup.getMinecraftLauncher().getProfileManager().getAuthDatabase();
/*     */     
/*  49 */     fillUsers();
/*  50 */     createInterface();
/*     */     
/*  52 */     this.playButton.addActionListener(this);
/*  53 */     this.logOutButton.addActionListener(this);
/*     */   }
/*     */ 
/*     */   
/*     */   private void fillUsers() {
/*  58 */     for (String user : this.authDatabase.getKnownNames()) {
/*     */       
/*  60 */       this.userDropdown.addItem(user);
/*  61 */       if (this.profileManager.getSelectedUser() != null && Objects.equal(this.authDatabase.getByUUID(this.profileManager.getSelectedUser()), this.authDatabase.getByName(user))) {
/*  62 */         this.userDropdown.setSelectedItem(user);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createInterface() {
/*  69 */     setLayout(new GridBagLayout());
/*  70 */     GridBagConstraints constraints = new GridBagConstraints();
/*  71 */     constraints.fill = 2;
/*  72 */     constraints.gridx = 0;
/*  73 */     constraints.gridy = -1;
/*  74 */     constraints.gridwidth = 2;
/*  75 */     constraints.weightx = 1.0D;
/*     */     
/*  77 */     add(Box.createGlue());
/*     */     
/*  79 */     String currentUser = this.authDatabase.getKnownNames().size() + " different users";
/*  80 */     String thisOrThese = (this.authDatabase.getKnownNames().size() == 1) ? "this account" : "one of these accounts";
/*  81 */     add(new JLabel("You're already logged in as " + currentUser + "."), constraints);
/*  82 */     add(new JLabel("You may use " + thisOrThese + " and skip authentication."), constraints);
/*     */     
/*  84 */     add(Box.createVerticalStrut(5), constraints);
/*     */     
/*  86 */     JLabel usernameLabel = new JLabel("Existing User:");
/*  87 */     Font labelFont = usernameLabel.getFont().deriveFont(1);
/*     */     
/*  89 */     usernameLabel.setFont(labelFont);
/*  90 */     add(usernameLabel, constraints);
/*     */     
/*  92 */     constraints.gridwidth = 1;
/*  93 */     add(this.userDropdown, constraints);
/*     */     
/*  95 */     constraints.gridx = 1;
/*  96 */     constraints.gridy = 5;
/*  97 */     constraints.weightx = 0.0D;
/*  98 */     constraints.insets = new Insets(0, 5, 0, 0);
/*  99 */     add(this.playButton, constraints);
/* 100 */     constraints.gridx = 2;
/* 101 */     add(this.logOutButton, constraints);
/* 102 */     constraints.insets = new Insets(0, 0, 0, 0);
/* 103 */     constraints.weightx = 1.0D;
/* 104 */     constraints.gridx = 0;
/* 105 */     constraints.gridy = -1;
/*     */     
/* 107 */     constraints.gridwidth = 2;
/*     */     
/* 109 */     add(Box.createVerticalStrut(5), constraints);
/* 110 */     add(new JLabel("Alternatively, log in with a new account below:"), constraints);
/* 111 */     add(new JPopupMenu.Separator(), constraints);
/*     */   }
/*     */   public void actionPerformed(ActionEvent e) {
/*     */     final String uuid;
/*     */     final UserAuthentication auth;
/* 116 */     final Object selected = this.userDropdown.getSelectedItem();
/*     */ 
/*     */     
/* 119 */     if (selected != null && selected instanceof String) {
/*     */       
/* 121 */       auth = this.authDatabase.getByName((String)selected);
/* 122 */       if (auth.getSelectedProfile() == null) {
/* 123 */         uuid = "demo-" + auth.getUserID();
/*     */       } else {
/* 125 */         uuid = UUIDTypeAdapter.fromUUID(auth.getSelectedProfile().getId());
/*     */       }
/*     */     
/*     */     } else {
/*     */       
/* 130 */       auth = null;
/* 131 */       uuid = null;
/*     */     } 
/* 133 */     if (e.getSource() == this.playButton) {
/*     */       
/* 135 */       this.popup.setCanLogIn(false);
/*     */       
/* 137 */       this.popup.getMinecraftLauncher().getLauncher().getVersionManager().getExecutorService().execute(new Runnable()
/*     */           {
/*     */             public void run()
/*     */             {
/* 141 */               if (auth != null && uuid != null) {
/*     */                 
/*     */                 try {
/* 144 */                   if (!auth.canPlayOnline()) {
/* 145 */                     auth.logIn();
/*     */                   }
/* 147 */                   ExistingUserListForm.this.popup.setLoggedIn(uuid);
/*     */                 }
/* 149 */                 catch (AuthenticationException ex) {
/*     */                   
/* 151 */                   ExistingUserListForm.this.popup.getErrorForm().displayError((Throwable)ex, new String[] { "We couldn't log you back in as " + this.val$selected + ".", "Please try to log in again." });
/*     */                   
/* 153 */                   ExistingUserListForm.this.removeUser((String)selected, uuid);
/*     */                   
/* 155 */                   ExistingUserListForm.this.popup.setCanLogIn(true);
/*     */                 } 
/*     */               } else {
/* 158 */                 ExistingUserListForm.this.popup.setCanLogIn(true);
/*     */               }
/*     */             
/*     */             }
/*     */           });
/* 163 */     } else if (e.getSource() == this.logOutButton) {
/*     */       
/* 165 */       removeUser((String)selected, uuid);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void removeUser(final String name, final String uuid) {
/* 171 */     if (!SwingUtilities.isEventDispatchThread()) {
/*     */       
/* 173 */       SwingUtilities.invokeLater(new Runnable()
/*     */           {
/*     */             public void run()
/*     */             {
/* 177 */               ExistingUserListForm.this.removeUser(name, uuid);
/*     */             }
/*     */           });
/*     */     }
/*     */     else {
/*     */       
/* 183 */       this.userDropdown.removeItem(name);
/* 184 */       this.authDatabase.removeUUID(uuid);
/*     */       
/*     */       try {
/* 187 */         this.profileManager.saveProfiles();
/*     */       }
/* 189 */       catch (IOException e) {
/*     */         
/* 191 */         LOGGER.error("Couldn't save profiles whilst removing " + name + " / " + uuid + " from database", e);
/*     */       } 
/* 193 */       if (this.userDropdown.getItemCount() == 0)
/* 194 */         this.popup.remove(this); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\net\minecraft\launche\\ui\popups\login\ExistingUserListForm.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */