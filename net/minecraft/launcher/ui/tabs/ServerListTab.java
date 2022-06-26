/*     */ package net.minecraft.launcher.ui.tabs;
/*     */ 
/*     */ import com.mojang.launcher.OperatingSystem;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.awt.event.MouseAdapter;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import javax.swing.JMenuItem;
/*     */ import javax.swing.JOptionPane;
/*     */ import javax.swing.JPopupMenu;
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.JTable;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.event.PopupMenuEvent;
/*     */ import javax.swing.event.PopupMenuListener;
/*     */ import javax.swing.table.AbstractTableModel;
/*     */ import net.minecraft.launcher.Language;
/*     */ import net.minecraft.launcher.Launcher;
/*     */ import net.minecraft.launcher.LauncherConstants;
/*     */ import net.minecraft.launcher.SwingUserInterface;
/*     */ import net.minecraft.launcher.profile.AuthenticationDatabase;
/*     */ import net.minecraft.launcher.profile.Profile;
/*     */ import net.minecraft.launcher.profile.ProfileManager;
/*     */ import net.minecraft.launcher.profile.RefreshedProfilesListener;
/*     */ import net.minecraft.launcher.ui.popups.profile.ProfileEditorPopup;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class ServerListTab
/*     */   extends JScrollPane
/*     */   implements RefreshedProfilesListener {
/*  37 */   private static final Logger LOGGER = LogManager.getLogger();
/*     */   private static final int COLUMN_NAME = 0;
/*     */   private static final int COLUMN_VERSION = 1;
/*     */   private static final int NUM_COLUMNS = 2;
/*     */   private final Launcher minecraftLauncher;
/*  42 */   private final ProfileTableModel dataModel = new ProfileTableModel();
/*  43 */   private final JTable table = new JTable(this.dataModel);
/*  44 */   private final JPopupMenu popupMenu = new JPopupMenu();
/*  45 */   private final JMenuItem addProfileButton = new JMenuItem(Language.get("main.tab.pe.add"));
/*  46 */   private final JMenuItem copyProfileButton = new JMenuItem(Language.get("main.tab.pe.copy"));
/*  47 */   private final JMenuItem deleteProfileButton = new JMenuItem(Language.get("main.tab.pe.delete"));
/*  48 */   private final JMenuItem browseGameFolder = new JMenuItem(Language.get("main.pe.button.opendir"));
/*     */ 
/*     */   
/*     */   public ServerListTab(Launcher minecraftLauncher) {
/*  52 */     this.minecraftLauncher = minecraftLauncher;
/*     */     
/*  54 */     setViewportView(this.table);
/*  55 */     createInterface();
/*     */     
/*  57 */     minecraftLauncher.getProfileManager().addRefreshedProfilesListener(this);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createInterface() {
/*  62 */     this.popupMenu.add(this.addProfileButton);
/*  63 */     this.popupMenu.add(this.copyProfileButton);
/*  64 */     this.popupMenu.add(this.deleteProfileButton);
/*  65 */     this.popupMenu.add(this.browseGameFolder);
/*     */     
/*  67 */     this.table.setFillsViewportHeight(true);
/*  68 */     this.table.setSelectionMode(0);
/*     */     
/*  70 */     this.popupMenu.addPopupMenuListener(new PopupMenuListener()
/*     */         {
/*     */           public void popupMenuWillBecomeVisible(PopupMenuEvent e)
/*     */           {
/*  74 */             int[] selection = ServerListTab.this.table.getSelectedRows();
/*  75 */             boolean hasSelection = (selection != null && selection.length > 0);
/*     */             
/*  77 */             ServerListTab.this.copyProfileButton.setEnabled(hasSelection);
/*  78 */             ServerListTab.this.deleteProfileButton.setEnabled(hasSelection);
/*  79 */             ServerListTab.this.browseGameFolder.setEnabled(hasSelection);
/*     */           }
/*     */           
/*     */           public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {}
/*     */           
/*     */           public void popupMenuCanceled(PopupMenuEvent e) {}
/*     */         });
/*  86 */     this.addProfileButton.addActionListener(new ActionListener()
/*     */         {
/*     */           public void actionPerformed(ActionEvent e)
/*     */           {
/*  90 */             Profile profile = new Profile();
/*  91 */             profile.setName("New Profile");
/*  92 */             while (ServerListTab.this.minecraftLauncher.getProfileManager().getProfiles().containsKey(profile.getName())) {
/*  93 */               profile.setName(profile.getName() + "_");
/*     */             }
/*  95 */             ProfileEditorPopup.showEditProfileDialog(ServerListTab.this.getMinecraftLauncher(), profile);
/*     */           }
/*     */         });
/*  98 */     this.copyProfileButton.addActionListener(new ActionListener()
/*     */         {
/*     */           public void actionPerformed(ActionEvent e)
/*     */           {
/* 102 */             int selection = ServerListTab.this.table.getSelectedRow();
/* 103 */             if (selection < 0 || selection >= ServerListTab.this.table.getRowCount()) {
/*     */               return;
/*     */             }
/* 106 */             Profile current = ServerListTab.this.dataModel.profiles.get(selection);
/* 107 */             Profile copy = new Profile(current);
/* 108 */             copy.setName("Copy of " + current.getName());
/* 109 */             while (ServerListTab.this.minecraftLauncher.getProfileManager().getProfiles().containsKey(copy.getName())) {
/* 110 */               copy.setName(copy.getName() + "_");
/*     */             }
/* 112 */             ProfileEditorPopup.showEditProfileDialog(ServerListTab.this.getMinecraftLauncher(), copy);
/*     */           }
/*     */         });
/* 115 */     this.browseGameFolder.addActionListener(new ActionListener()
/*     */         {
/*     */           public void actionPerformed(ActionEvent e)
/*     */           {
/* 119 */             int selection = ServerListTab.this.table.getSelectedRow();
/* 120 */             if (selection < 0 || selection >= ServerListTab.this.table.getRowCount()) {
/*     */               return;
/*     */             }
/* 123 */             Profile profile = ServerListTab.this.dataModel.profiles.get(selection);
/* 124 */             OperatingSystem.openFolder((profile.getGameDir() == null) ? ServerListTab.this.minecraftLauncher.getLauncher().getWorkingDirectory() : profile.getGameDir());
/*     */           }
/*     */         });
/* 127 */     this.deleteProfileButton.addActionListener(new ActionListener()
/*     */         {
/*     */           public void actionPerformed(ActionEvent e)
/*     */           {
/* 131 */             int selection = ServerListTab.this.table.getSelectedRow();
/* 132 */             if (selection < 0 || selection >= ServerListTab.this.table.getRowCount()) {
/*     */               return;
/*     */             }
/* 135 */             Profile current = ServerListTab.this.dataModel.profiles.get(selection);
/*     */             
/* 137 */             int result = JOptionPane.showOptionDialog(((SwingUserInterface)ServerListTab.this.minecraftLauncher.getUserInterface()).getFrame(), "Are you sure you want to delete this profile?", "Profile Confirmation", 0, 2, null, (Object[])LauncherConstants.CONFIRM_PROFILE_DELETION_OPTIONS, LauncherConstants.CONFIRM_PROFILE_DELETION_OPTIONS[0]);
/* 138 */             if (result == 0) {
/*     */               
/* 140 */               ServerListTab.this.minecraftLauncher.getProfileManager().getProfiles().remove(current.getName());
/*     */               
/*     */               try {
/* 143 */                 ServerListTab.this.minecraftLauncher.getProfileManager().saveProfiles();
/* 144 */                 ServerListTab.this.minecraftLauncher.getProfileManager().fireRefreshEvent();
/*     */               }
/* 146 */               catch (IOException ex) {
/*     */                 
/* 148 */                 ServerListTab.LOGGER.error("Couldn't save profiles whilst deleting '" + current.getName() + "'", ex);
/*     */               } 
/*     */             } 
/*     */           }
/*     */         });
/* 153 */     this.table.addMouseListener(new MouseAdapter()
/*     */         {
/*     */           public void mouseClicked(MouseEvent e)
/*     */           {
/* 157 */             if (e.getClickCount() == 2) {
/*     */               
/* 159 */               int row = ServerListTab.this.table.getSelectedRow();
/* 160 */               if (row >= 0 && row < ServerListTab.this.dataModel.profiles.size()) {
/* 161 */                 ProfileEditorPopup.showEditProfileDialog(ServerListTab.this.getMinecraftLauncher(), ServerListTab.this.dataModel.profiles.get(row));
/*     */               }
/*     */             } 
/*     */           }
/*     */ 
/*     */           
/*     */           public void mouseReleased(MouseEvent e) {
/* 168 */             if (e.isPopupTrigger() && e.getComponent() instanceof JTable) {
/*     */               
/* 170 */               int r = ServerListTab.this.table.rowAtPoint(e.getPoint());
/* 171 */               if (r >= 0 && r < ServerListTab.this.table.getRowCount()) {
/* 172 */                 ServerListTab.this.table.setRowSelectionInterval(r, r);
/*     */               } else {
/* 174 */                 ServerListTab.this.table.clearSelection();
/*     */               } 
/* 176 */               ServerListTab.this.popupMenu.show(e.getComponent(), e.getX(), e.getY());
/*     */             } 
/*     */           }
/*     */ 
/*     */           
/*     */           public void mousePressed(MouseEvent e) {
/* 182 */             if (e.isPopupTrigger() && e.getComponent() instanceof JTable) {
/*     */               
/* 184 */               int r = ServerListTab.this.table.rowAtPoint(e.getPoint());
/* 185 */               if (r >= 0 && r < ServerListTab.this.table.getRowCount()) {
/* 186 */                 ServerListTab.this.table.setRowSelectionInterval(r, r);
/*     */               } else {
/* 188 */                 ServerListTab.this.table.clearSelection();
/*     */               } 
/* 190 */               ServerListTab.this.popupMenu.show(e.getComponent(), e.getX(), e.getY());
/*     */             } 
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public Launcher getMinecraftLauncher() {
/* 198 */     return this.minecraftLauncher;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onProfilesRefreshed(final ProfileManager manager) {
/* 203 */     SwingUtilities.invokeLater(new Runnable()
/*     */         {
/*     */           public void run()
/*     */           {
/* 207 */             ServerListTab.this.dataModel.setProfiles(manager.getProfiles().values());
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   private class ProfileTableModel
/*     */     extends AbstractTableModel
/*     */   {
/* 215 */     private final List<Profile> profiles = new ArrayList<>();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int getRowCount() {
/* 221 */       return this.profiles.size();
/*     */     }
/*     */ 
/*     */     
/*     */     public int getColumnCount() {
/* 226 */       return 2;
/*     */     }
/*     */ 
/*     */     
/*     */     public Class<?> getColumnClass(int columnIndex) {
/* 231 */       return String.class;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getColumnName(int column) {
/* 236 */       switch (column) {
/*     */         
/*     */         case 1:
/* 239 */           return "Version";
/*     */         case 0:
/* 241 */           return "Version name";
/*     */       } 
/* 243 */       return super.getColumnName(column);
/*     */     }
/*     */ 
/*     */     
/*     */     public Object getValueAt(int rowIndex, int columnIndex) {
/* 248 */       Profile profile = this.profiles.get(rowIndex);
/* 249 */       AuthenticationDatabase authDatabase = ServerListTab.this.minecraftLauncher.getProfileManager().getAuthDatabase();
/* 250 */       switch (columnIndex) {
/*     */         
/*     */         case 0:
/* 253 */           return profile.getName();
/*     */         case 1:
/* 255 */           if (profile.getLastVersionId() == null) {
/* 256 */             return "(Latest version)";
/*     */           }
/* 258 */           return profile.getLastVersionId();
/*     */       } 
/* 260 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     public void setProfiles(Collection<Profile> profiles) {
/* 265 */       this.profiles.clear();
/* 266 */       this.profiles.addAll(profiles);
/* 267 */       Collections.sort(this.profiles);
/* 268 */       fireTableDataChanged();
/*     */     }
/*     */     
/*     */     private ProfileTableModel() {}
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\net\minecraft\launche\\ui\tabs\ServerListTab.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */