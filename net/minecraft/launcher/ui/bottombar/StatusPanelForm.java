/*    */ package net.minecraft.launcher.ui.bottombar;
/*    */ 
/*    */ import com.google.gson.Gson;
/*    */ import com.google.gson.GsonBuilder;
/*    */ import com.google.gson.TypeAdapterFactory;
/*    */ import com.mojang.launcher.updater.LowerCaseEnumTypeAdapterFactory;
/*    */ import java.awt.GridBagConstraints;
/*    */ import javax.swing.JLabel;
/*    */ import net.minecraft.launcher.Launcher;
/*    */ import org.apache.logging.log4j.LogManager;
/*    */ import org.apache.logging.log4j.Logger;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class StatusPanelForm
/*    */   extends SidebarGridForm
/*    */ {
/* 21 */   private static final Logger LOGGER = LogManager.getLogger();
/*    */   private static final String SERVER_SESSION = "session.minecraft.net";
/*    */   private static final String SERVER_LOGIN = "login.minecraft.net";
/*    */   private final Launcher minecraftLauncher;
/* 25 */   private final JLabel sessionStatus = new JLabel("???");
/* 26 */   private final JLabel loginStatus = new JLabel("???");
/* 27 */   private final Gson gson = (new GsonBuilder()).registerTypeAdapterFactory((TypeAdapterFactory)new LowerCaseEnumTypeAdapterFactory()).create();
/*    */ 
/*    */   
/*    */   public StatusPanelForm(Launcher minecraftLauncher) {
/* 31 */     this.minecraftLauncher = minecraftLauncher;
/*    */     
/* 33 */     createInterface();
/* 34 */     refreshStatuses();
/*    */   }
/*    */ 
/*    */   
/*    */   protected void populateGrid(GridBagConstraints constraints) {
/* 39 */     add(new JLabel("Multiplayer:", 2), constraints, 0, 0, 0, 1, 17);
/* 40 */     add(this.sessionStatus, constraints, 1, 0, 1, 1);
/*    */     
/* 42 */     add(new JLabel("Login:", 2), constraints, 0, 1, 0, 1, 17);
/* 43 */     add(this.loginStatus, constraints, 1, 1, 1, 1);
/*    */   }
/*    */ 
/*    */   
/*    */   public JLabel getSessionStatus() {
/* 48 */     return this.sessionStatus;
/*    */   }
/*    */ 
/*    */   
/*    */   public JLabel getLoginStatus() {
/* 53 */     return this.loginStatus;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void refreshStatuses() {}
/*    */ 
/*    */   
/*    */   public enum ServerStatus
/*    */   {
/* 63 */     GREEN("Online, no problems detected."), YELLOW("May be experiencing issues."), RED("Offline, experiencing problems.");
/*    */     
/*    */     private final String title;
/*    */ 
/*    */     
/*    */     ServerStatus(String title) {
/* 69 */       this.title = title;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\net\minecraft\launche\\ui\bottombar\StatusPanelForm.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */