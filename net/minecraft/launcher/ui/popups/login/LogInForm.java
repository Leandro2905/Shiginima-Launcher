/*     */ package net.minecraft.launcher.ui.popups.login;
/*     */ 
/*     */ import com.mojang.authlib.Agent;
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import com.mojang.authlib.UserAuthentication;
/*     */ import com.mojang.authlib.exceptions.AuthenticationException;
/*     */ import com.mojang.authlib.exceptions.InvalidCredentialsException;
/*     */ import com.mojang.authlib.exceptions.UserMigratedException;
/*     */ import com.mojang.util.UUIDTypeAdapter;
/*     */ import java.awt.Font;
/*     */ import java.awt.GridLayout;
/*     */ import java.awt.LayoutManager;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.io.IOException;
/*     */ import java.net.URISyntaxException;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JCheckBox;
/*     */ import javax.swing.JComboBox;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JOptionPane;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JPasswordField;
/*     */ import javax.swing.JTabbedPane;
/*     */ import javax.swing.JTextField;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.UIManager;
/*     */ import net.mc.main.Main;
/*     */ import net.miginfocom.swing.MigLayout;
/*     */ import net.minecraft.launcher.Language;
/*     */ import net.minecraft.launcher.Launcher;
/*     */ import net.minecraft.launcher.profile.AuthenticationDatabase;
/*     */ import org.apache.commons.lang3.ArrayUtils;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
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
/*     */ public class LogInForm
/*     */   extends JPanel
/*     */   implements ActionListener
/*     */ {
/*  58 */   private static final Logger LOGGER = LogManager.getLogger();
/*     */   private final LogInPopup popup;
/*  60 */   private final JTextField usernameField = new JTextField();
/*  61 */   private final JPasswordField passwordField = new JPasswordField();
/*  62 */   private final JComboBox userDropdown = new JComboBox();
/*  63 */   private final JPanel userDropdownPanel = new JPanel();
/*     */   
/*     */   private final UserAuthentication authentication;
/*  66 */   private final JComboBox langDropdown = new JComboBox();
/*     */   
/*  68 */   private final JButton addUser = new JButton(Language.get("login.tab.main.save"));
/*     */   
/*  70 */   private final JButton removeUser = new JButton(Language.get("login.tab.main.delete"));
/*     */   
/*  72 */   private final JButton playButton1 = new JButton(Language.get("login.tab.main.play"));
/*     */   
/*  74 */   private final JCheckBox autologin = new JCheckBox(Language.get("login.tab.options.auto"));
/*     */   
/*  76 */   private final JCheckBox syncLanguage = new JCheckBox(Language.get("login.tab.options.autolang"));
/*     */   
/*  78 */   private final JCheckBox darkTheme = new JCheckBox("Dark Theme");
/*     */   
/*  80 */   private final JButton saveOptions = new JButton(Language.get("button.save"));
/*     */   
/*  82 */   private Map<String, ComboItem> langItems = new HashMap<>();
/*     */   
/*     */   public LogInForm(LogInPopup popup) {
/*  85 */     this.popup = popup;
/*  86 */     this.authentication = popup.getMinecraftLauncher().getProfileManager().getAuthDatabase().getAuthenticationService().createUserAuthentication(Agent.MINECRAFT);
/*     */     
/*  88 */     this.usernameField.addActionListener(this);
/*  89 */     this.passwordField.addActionListener(this);
/*     */     
/*  91 */     this.usernameField.addActionListener(this);
/*  92 */     this.addUser.addActionListener(this);
/*  93 */     this.removeUser.addActionListener(this);
/*  94 */     this.userDropdown.addActionListener(this);
/*  95 */     this.playButton1.addActionListener(this);
/*  96 */     this.autologin.addActionListener(this);
/*  97 */     this.syncLanguage.addActionListener(this);
/*  98 */     this.darkTheme.addActionListener(this);
/*  99 */     this.langDropdown.addActionListener(this);
/* 100 */     this.saveOptions.addActionListener(this);
/*     */ 
/*     */ 
/*     */     
/* 104 */     this.langItems.put("en", new ComboItem("en", (String)Language.langs.get("en")));
/* 105 */     this.langItems.put("cs", new ComboItem("cs", (String)Language.langs.get("cs")));
/* 106 */     this.langItems.put("es", new ComboItem("es", (String)Language.langs.get("es")));
/* 107 */     this.langItems.put("fi", new ComboItem("fi", (String)Language.langs.get("fi")));
/* 108 */     this.langItems.put("kx", new ComboItem("kx", (String)Language.langs.get("kx")));
/* 109 */     this.langItems.put("pl", new ComboItem("pl", (String)Language.langs.get("pl")));
/* 110 */     this.langItems.put("pt", new ComboItem("pt", (String)Language.langs.get("pt")));
/* 111 */     this.langItems.put("tr", new ComboItem("tr", (String)Language.langs.get("tr")));
/* 112 */     this.langItems.put("vi", new ComboItem("vi", (String)Language.langs.get("vi")));
/* 113 */     this.langItems.put("de", new ComboItem("de", (String)Language.langs.get("de")));
/* 114 */     this.langItems.put("it", new ComboItem("it", (String)Language.langs.get("it")));
/* 115 */     this.langItems.put("fr", new ComboItem("fr", (String)Language.langs.get("fr")));
/* 116 */     this.langItems.put("ru", new ComboItem("ru", (String)Language.langs.get("ru")));
/* 117 */     this.langItems.put("hu", new ComboItem("hu", (String)Language.langs.get("hu")));
/* 118 */     this.langItems.put("et", new ComboItem("et", (String)Language.langs.get("et")));
/* 119 */     this.langItems.put("tl", new ComboItem("tl", (String)Language.langs.get("tl")));
/* 120 */     this.langItems.put("hr", new ComboItem("hr", (String)Language.langs.get("hr")));
/* 121 */     this.langItems.put("th", new ComboItem("th", (String)Language.langs.get("th")));
/* 122 */     this.langItems.put("nl", new ComboItem("nl", (String)Language.langs.get("nl")));
/*     */ 
/*     */     
/* 125 */     this.langDropdown.addItem(this.langItems.get("en"));
/* 126 */     this.langDropdown.addItem(this.langItems.get("cs"));
/* 127 */     this.langDropdown.addItem(this.langItems.get("es"));
/* 128 */     this.langDropdown.addItem(this.langItems.get("fi"));
/* 129 */     this.langDropdown.addItem(this.langItems.get("kx"));
/* 130 */     this.langDropdown.addItem(this.langItems.get("pl"));
/* 131 */     this.langDropdown.addItem(this.langItems.get("pt"));
/* 132 */     this.langDropdown.addItem(this.langItems.get("tr"));
/* 133 */     this.langDropdown.addItem(this.langItems.get("vi"));
/* 134 */     this.langDropdown.addItem(this.langItems.get("de"));
/* 135 */     this.langDropdown.addItem(this.langItems.get("it"));
/* 136 */     this.langDropdown.addItem(this.langItems.get("fr"));
/* 137 */     this.langDropdown.addItem(this.langItems.get("ru"));
/* 138 */     this.langDropdown.addItem(this.langItems.get("et"));
/* 139 */     this.langDropdown.addItem(this.langItems.get("hu"));
/* 140 */     this.langDropdown.addItem(this.langItems.get("tl"));
/* 141 */     this.langDropdown.addItem(this.langItems.get("hr"));
/* 142 */     this.langDropdown.addItem(this.langItems.get("th"));
/* 143 */     this.langDropdown.addItem(this.langItems.get("nl"));
/*     */     
/* 145 */     createInterface(Boolean.valueOf(true));
/* 146 */     updateLDD();
/*     */     
/* 148 */     Boolean isAutoLogin = Launcher.getInstance().getUsernameManager().isAutoLogin();
/* 149 */     String autoLoginName = Launcher.getInstance().getUsernameManager().getAutoLoginName();
/*     */     
/* 151 */     this.syncLanguage.setSelected(Launcher.getInstance().getUsernameManager().willSyncLanguage().booleanValue());
/*     */     
/* 153 */     this.darkTheme.setSelected(Launcher.getInstance().getUsernameManager().getDark().booleanValue());
/*     */ 
/*     */ 
/*     */     
/* 157 */     System.out.println(isAutoLogin + " " + autoLoginName);
/*     */   }
/*     */   
/*     */   private void updateLDD() {
/* 161 */     String lang = Language.getLanguage();
/* 162 */     ComboItem item = this.langItems.get(lang);
/*     */     
/* 164 */     this.langDropdown.setSelectedItem(item);
/*     */   }
/*     */ 
/*     */   
/*     */   private void createInterface(Boolean visible) {
/* 169 */     System.out.println("CREATING INTERFACE");
/* 170 */     setLayout(new GridLayout());
/* 171 */     JTabbedPane tabbedPane = new JTabbedPane();
/*     */     
/* 173 */     JComponent panel1 = makeMainPanel();
/* 174 */     tabbedPane.addTab(Language.get("login.tab.main"), panel1);
/*     */     
/* 176 */     JComponent panel2 = makeOptionPanel();
/* 177 */     tabbedPane.addTab(Language.get("login.tab.options"), panel2);
/*     */     
/* 179 */     add(tabbedPane);
/*     */     
/* 181 */     tabbedPane.setVisible(visible.booleanValue());
/*     */   }
/*     */   
/*     */   protected JComponent makeMainPanel() {
/* 185 */     System.out.println("MAKING MAIN PANEL");
/* 186 */     JPanel panel = new JPanel((LayoutManager)new MigLayout("fillx", "[right]rel[grow,fill]", "[]10[]"));
/* 187 */     JLabel usernameLabel = new JLabel(Language.get("login.tab.main.title"));
/* 188 */     usernameLabel.setFont(new Font(UIManager.getDefaults().getFont("TabbedPane.font").getFontName(), 1, 14));
/* 189 */     usernameLabel.setHorizontalAlignment(0);
/*     */     
/* 191 */     panel.add(usernameLabel, "Wrap, grow, span");
/*     */     
/* 193 */     ((JLabel)this.userDropdown.getRenderer()).setHorizontalAlignment(0);
/* 194 */     this.userDropdown.setFont(new Font(UIManager.getDefaults().getFont("TabbedPane.font").getFontName(), 1, 18));
/*     */     
/* 196 */     panel.add(this.userDropdown, "span, grow, wrap");
/*     */     
/* 198 */     panel.add(this.addUser, "split 2, span ,grow");
/*     */     
/* 200 */     panel.add(this.removeUser, "wrap, span, grow");
/*     */     
/* 202 */     this.playButton1.setFont(new Font(UIManager.getDefaults().getFont("TabbedPane.font").getFontName(), 1, 20));
/*     */     
/* 204 */     panel.add(this.playButton1, "span, h 50, grow, wrap");
/*     */     
/* 206 */     updateDropDown();
/* 207 */     this.userDropdown.setSelectedItem(Launcher.getInstance().getUsernameManager().getLastUsed());
/* 208 */     this.popup.getErrorForm().displayError(null, new String[] { "" });
/* 209 */     return panel;
/*     */   }
/*     */   
/*     */   protected JComponent makeOptionPanel() {
/* 213 */     System.out.println("MAKING OPTION PANEL");
/* 214 */     JPanel panel = new JPanel((LayoutManager)new MigLayout("fillx", "[right]rel[grow,fill]", "[]10[]"));
/* 215 */     panel.add(this.autologin, "span, align center, grow, wrap");
/* 216 */     panel.add(this.syncLanguage, "span, align center, grow, wrap");
/* 217 */     panel.add(this.darkTheme, "span, align center, grow, wrap");
/* 218 */     panel.add(new JLabel("Language: "));
/* 219 */     panel.add(this.langDropdown, "span, align center, grow, wrap");
/* 220 */     panel.add(this.saveOptions, "span, align center, grow");
/* 221 */     return panel;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void actionPerformed(ActionEvent e) {
/* 228 */     if (e.getSource() == this.syncLanguage) {
/*     */       
/* 230 */       Boolean selected = Boolean.valueOf(this.syncLanguage.isSelected());
/*     */       
/* 232 */       Launcher.getInstance().getUsernameManager().setSyncLanguage(selected);
/*     */     } 
/*     */ 
/*     */     
/* 236 */     if (e.getSource() == this.darkTheme) {
/*     */       
/* 238 */       Boolean selected = Boolean.valueOf(this.darkTheme.isSelected());
/*     */       
/* 240 */       Launcher.getInstance().getUsernameManager().setDark(selected);
/*     */     } 
/*     */ 
/*     */     
/* 244 */     if (e.getSource() == this.saveOptions) {
/*     */       
/* 246 */       String lang = ((ComboItem)this.langDropdown.getSelectedItem()).getValue();
/* 247 */       Language l = new Language();
/* 248 */       l.load(lang);
/*     */       
/* 250 */       this.popup.getErrorForm().displayError(null, new String[] { Language.get("login.msg.saved") });
/* 251 */       Launcher.getInstance().getUsernameManager().save();
/*     */       
/* 253 */       Object[] options1 = { Language.get("button.restart"), Language.get("button.cancel") };
/* 254 */       JPanel panel = new JPanel();
/*     */       
/* 256 */       panel.add(new JLabel(Language.get("login.tab.options.restart.msg")));
/*     */       
/* 258 */       int result = JOptionPane.showOptionDialog(null, panel, Language.get("login.tab.options.restart"), 1, -1, null, options1, null);
/*     */ 
/*     */       
/* 261 */       if (Launcher.getInstance().getUsernameManager().willSyncLanguage().booleanValue()) {
/* 262 */         Launcher.getInstance().getOptionsManager().setLanguage();
/*     */       }
/*     */       
/* 265 */       if (result == 0) {
/*     */         try {
/* 267 */           Main.restartApplication();
/*     */         }
/* 269 */         catch (IOException ex) {
/* 270 */           Logger.getLogger(LogInForm.class.getName()).log(Level.SEVERE, (String)null, ex);
/* 271 */         } catch (URISyntaxException ex) {
/* 272 */           Logger.getLogger(LogInForm.class.getName()).log(Level.SEVERE, (String)null, ex);
/*     */         } 
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 279 */     if (e.getSource() == this.langDropdown) {
/*     */       
/* 281 */       String lang = ((ComboItem)this.langDropdown.getSelectedItem()).getValue();
/*     */       
/* 283 */       Launcher.getInstance().getUsernameManager().setLanguage(lang);
/*     */       
/* 285 */       System.out.println("POINT5");
/*     */     } 
/*     */ 
/*     */     
/* 289 */     if (e.getSource() == this.autologin) {
/* 290 */       String name = this.userDropdown.getEditor().getItem().toString();
/*     */       
/* 292 */       if (name.equals("")) {
/* 293 */         this.autologin.setSelected(false);
/*     */       
/*     */       }
/* 296 */       else if (this.autologin.isSelected()) {
/*     */         
/* 298 */         this.popup.getErrorForm().displayError(null, new String[] { Language.get("login.msg.auto.1"), Language.get("login.msg.auto.2.1") + name + Language.get("login.msg.auto.2.2"), Language.get("login.msg.auto.3") });
/* 299 */         this.userDropdown.setEnabled(false);
/* 300 */         this.addUser.setEnabled(false);
/* 301 */         this.removeUser.setEnabled(false);
/* 302 */         Launcher.getInstance().getUsernameManager().setAutoLoginName(name);
/* 303 */         Launcher.getInstance().getUsernameManager().setAutoLogin(Boolean.TRUE);
/* 304 */         Launcher.getInstance().getUsernameManager().save();
/* 305 */         System.out.println("POINT4");
/*     */       } else {
/*     */         
/* 308 */         this.popup.getErrorForm().displayError(null, new String[] { "" });
/* 309 */         this.userDropdown.setEnabled(true);
/* 310 */         this.addUser.setEnabled(true);
/* 311 */         this.removeUser.setEnabled(true);
/* 312 */         Launcher.getInstance().getUsernameManager().setAutoLoginName("");
/* 313 */         Launcher.getInstance().getUsernameManager().setAutoLogin(Boolean.FALSE);
/*     */         
/* 315 */         System.out.println("POINT3");
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 322 */     if (e.getSource() == this.playButton1) {
/* 323 */       String name = this.userDropdown.getEditor().getItem().toString();
/*     */       
/* 325 */       System.out.println("USING USERNAME: " + name);
/*     */       
/* 327 */       if (name.equalsIgnoreCase("") || name.isEmpty() || name.length() == 0) {
/* 328 */         this.popup.getErrorForm().displayError(null, new String[] { Language.get("login.msg.blank.1"), Language.get("login.msg.blank.2") });
/*     */       } else {
/* 330 */         this.popup.getErrorForm().displayError(null, new String[] { "" });
/* 331 */         tryLogIn(this.userDropdown.getSelectedItem().toString());
/*     */       } 
/*     */     } 
/*     */     
/* 335 */     if (e.getSource() == this.removeUser) {
/*     */       try {
/* 337 */         this.popup.getErrorForm().displayError(null, new String[] { "" });
/* 338 */         String name = this.userDropdown.getEditor().getItem().toString();
/*     */         
/* 340 */         Launcher.getInstance().getUsernameManager().removeUsername(name);
/*     */         
/* 342 */         this.popup.getErrorForm().displayError(null, new String[] { "" });
/* 343 */         this.autologin.setSelected(false);
/* 344 */         this.userDropdown.setEnabled(true);
/* 345 */         Launcher.getInstance().getUsernameManager().setAutoLoginName("");
/* 346 */         Launcher.getInstance().getUsernameManager().setAutoLogin(Boolean.FALSE);
/*     */         
/* 348 */         System.out.println("POINT2");
/*     */       }
/* 350 */       catch (IOException ex) {
/* 351 */         LOGGER.error("Could not set username", ex);
/*     */       } 
/*     */       
/* 354 */       updateDropDown();
/*     */     } 
/*     */     
/* 357 */     if (e.getSource() == this.addUser) {
/* 358 */       String name = this.userDropdown.getEditor().getItem().toString();
/*     */       
/* 360 */       if ("".equals(name)) {
/* 361 */         this.popup.getErrorForm().displayError(null, new String[] { Language.get("login.msg.blank.1"), Language.get("login.msg.blank.2") });
/*     */       } else {
/*     */         
/* 364 */         this.popup.getErrorForm().displayError(null, new String[] { "" });
/*     */         
/*     */         try {
/* 367 */           Launcher.getInstance().getUsernameManager().addUsername(name);
/* 368 */           updateDropDown();
/* 369 */           this.userDropdown.getEditor().setItem(name);
/* 370 */           this.userDropdown.setSelectedItem(name);
/*     */         }
/* 372 */         catch (IOException ex) {
/* 373 */           LOGGER.error("Could not add user", ex);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean updateDropDown() {
/* 382 */     this.userDropdown.removeAllItems();
/* 383 */     Integer count = Integer.valueOf(0);
/* 384 */     this.userDropdown.addItem("");
/* 385 */     for (String um : Launcher.getInstance().getUsernameManager().getUsernames()) {
/*     */       
/* 387 */       this.userDropdown.addItem(um);
/* 388 */       Integer integer1 = count, integer2 = count = Integer.valueOf(count.intValue() + 1);
/*     */     } 
/*     */ 
/*     */     
/* 392 */     if (count.intValue() != 0) {
/*     */       
/* 394 */       ddpanelset(Boolean.valueOf(true));
/* 395 */       return true;
/*     */     } 
/* 397 */     ddpanelset(Boolean.valueOf(false));
/* 398 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void ddpanelset(Boolean bool) {
/* 406 */     this.userDropdown.setEditable(true);
/*     */     
/* 408 */     this.removeUser.setEnabled(bool.booleanValue());
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void tryLogIn(final String username) {
/* 491 */     System.out.println("OVER HERE 2");
/* 492 */     this.popup.setCanLogIn(false);
/* 493 */     this.authentication.logOut();
/* 494 */     this.authentication.setUsername(username);
/* 495 */     this.authentication.setPassword(String.valueOf(this.passwordField.getPassword()));
/* 496 */     final int passwordLength = (this.passwordField.getPassword()).length;
/*     */     
/* 498 */     this.passwordField.setText("");
/*     */     
/* 500 */     this.popup.getMinecraftLauncher().getLauncher().getVersionManager().getExecutorService().execute(new Runnable() {
/*     */           public void run() {
/*     */             try {
/* 503 */               LogInForm.this.authentication.logIn();
/* 504 */               AuthenticationDatabase authDatabase = LogInForm.this.popup.getMinecraftLauncher().getProfileManager().getAuthDatabase();
/* 505 */               if (LogInForm.this.authentication.getSelectedProfile() == null) {
/* 506 */                 if (ArrayUtils.isNotEmpty((Object[])LogInForm.this.authentication.getAvailableProfiles())) {
/* 507 */                   for (GameProfile profile : LogInForm.this.authentication.getAvailableProfiles()) {
/* 508 */                     LogInForm.this.userDropdown.addItem(profile.getName());
/*     */                   }
/* 510 */                   SwingUtilities.invokeLater(new Runnable() {
/*     */                         public void run() {
/* 512 */                           LogInForm.this.usernameField.setEditable(false);
/* 513 */                           LogInForm.this.passwordField.setEditable(false);
/* 514 */                           LogInForm.this.userDropdownPanel.setVisible(true);
/* 515 */                           LogInForm.this.popup.repack();
/* 516 */                           LogInForm.this.popup.setCanLogIn(true);
/* 517 */                           LogInForm.this.passwordField.setText(StringUtils.repeat('*', passwordLength));
/*     */                         }
/*     */                       });
/*     */                 } else {
/* 521 */                   String uuid = "demo-" + LogInForm.this.authentication.getUserID();
/* 522 */                   authDatabase.register(uuid, LogInForm.this.authentication);
/* 523 */                   LogInForm.this.popup.setLoggedIn(uuid);
/*     */                 } 
/*     */               } else {
/* 526 */                 authDatabase.register(UUIDTypeAdapter.fromUUID(LogInForm.this.authentication.getSelectedProfile().getId()), LogInForm.this.authentication);
/* 527 */                 LogInForm.this.popup.setLoggedIn(UUIDTypeAdapter.fromUUID(LogInForm.this.authentication.getSelectedProfile().getId()));
/*     */               } 
/*     */               
/* 530 */               Launcher.getInstance().getUsernameManager().setLastUsed(username);
/* 531 */               Launcher.getInstance().getUsernameManager().save();
/* 532 */             } catch (UserMigratedException ex) {
/* 533 */               LogInForm.LOGGER.error("Couldn't log in", (Throwable)ex);
/* 534 */               LogInForm.this.popup.getErrorForm().displayError((Throwable)ex, new String[] { "Sorry, but we can't log you in with your username.", "You have migrated your account, please use your email address." });
/* 535 */               LogInForm.this.popup.setCanLogIn(true);
/* 536 */             } catch (InvalidCredentialsException ex) {
/* 537 */               LogInForm.LOGGER.error("Couldn't log in", (Throwable)ex);
/* 538 */               LogInForm.this.popup.getErrorForm().displayError((Throwable)ex, new String[] { "Sorry, but your username or password is incorrect!", "Please try again. If you need help, try the 'Forgot Password' link." });
/* 539 */               LogInForm.this.popup.setCanLogIn(true);
/* 540 */             } catch (AuthenticationException ex) {
/* 541 */               LogInForm.LOGGER.error("Couldn't log in", (Throwable)ex);
/* 542 */               LogInForm.this.popup.getErrorForm().displayError((Throwable)ex, new String[] { "Sorry, but we couldn't connect to our servers.", "Please make sure that you are online and that Minecraft is not blocked." });
/* 543 */               LogInForm.this.popup.setCanLogIn(true);
/*     */             } 
/*     */           }
/*     */         });
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\net\minecraft\launche\\ui\popups\login\LogInForm.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */