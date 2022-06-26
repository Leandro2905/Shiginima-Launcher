/*    */ package net.minecraft.launcher.ui;
/*    */ 
/*    */ import java.awt.Component;
/*    */ import java.awt.GridBagConstraints;
/*    */ import java.awt.GridBagLayout;
/*    */ import java.awt.GridLayout;
/*    */ import javax.swing.JPanel;
/*    */ import javax.swing.border.EmptyBorder;
/*    */ import net.minecraft.launcher.Launcher;
/*    */ import net.minecraft.launcher.ui.bottombar.PlayButtonPanel;
/*    */ import net.minecraft.launcher.ui.bottombar.PlayerInfoPanel;
/*    */ import net.minecraft.launcher.ui.bottombar.ProfileSelectionPanel;
/*    */ 
/*    */ public class BottomBarPanel
/*    */   extends JPanel
/*    */ {
/*    */   private final Launcher minecraftLauncher;
/*    */   private final ProfileSelectionPanel profileSelectionPanel;
/*    */   private final PlayerInfoPanel playerInfoPanel;
/*    */   private final PlayButtonPanel playButtonPanel;
/*    */   
/*    */   public BottomBarPanel(Launcher minecraftLauncher) {
/* 23 */     this.minecraftLauncher = minecraftLauncher;
/*    */     
/* 25 */     int border = 4;
/* 26 */     setBorder(new EmptyBorder(border, border, border, border));
/*    */     
/* 28 */     this.profileSelectionPanel = new ProfileSelectionPanel(minecraftLauncher);
/* 29 */     this.playerInfoPanel = new PlayerInfoPanel(minecraftLauncher);
/* 30 */     this.playButtonPanel = new PlayButtonPanel(minecraftLauncher);
/*    */     
/* 32 */     createInterface();
/*    */   }
/*    */ 
/*    */   
/*    */   protected void createInterface() {
/* 37 */     setLayout(new GridLayout(1, 3));
/*    */     
/* 39 */     add(wrapSidePanel((JPanel)this.profileSelectionPanel, 17));
/* 40 */     add((Component)this.playButtonPanel);
/* 41 */     add(wrapSidePanel((JPanel)this.playerInfoPanel, 13));
/*    */   }
/*    */ 
/*    */   
/*    */   protected JPanel wrapSidePanel(JPanel target, int side) {
/* 46 */     JPanel wrapper = new JPanel(new GridBagLayout());
/* 47 */     GridBagConstraints constraints = new GridBagConstraints();
/* 48 */     constraints.anchor = side;
/* 49 */     constraints.weightx = 1.0D;
/* 50 */     constraints.weighty = 1.0D;
/*    */     
/* 52 */     wrapper.add(target, constraints);
/*    */     
/* 54 */     return wrapper;
/*    */   }
/*    */ 
/*    */   
/*    */   public Launcher getMinecraftLauncher() {
/* 59 */     return this.minecraftLauncher;
/*    */   }
/*    */ 
/*    */   
/*    */   public ProfileSelectionPanel getProfileSelectionPanel() {
/* 64 */     return this.profileSelectionPanel;
/*    */   }
/*    */ 
/*    */   
/*    */   public PlayerInfoPanel getPlayerInfoPanel() {
/* 69 */     return this.playerInfoPanel;
/*    */   }
/*    */ 
/*    */   
/*    */   public PlayButtonPanel getPlayButtonPanel() {
/* 74 */     return this.playButtonPanel;
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\net\minecraft\launche\\ui\BottomBarPanel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */