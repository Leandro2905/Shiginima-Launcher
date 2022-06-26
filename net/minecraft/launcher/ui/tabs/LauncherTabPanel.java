/*    */ package net.minecraft.launcher.ui.tabs;
/*    */ 
/*    */ import java.awt.Component;
/*    */ import javax.swing.JTabbedPane;
/*    */ import net.minecraft.launcher.Language;
/*    */ import net.minecraft.launcher.Launcher;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class LauncherTabPanel
/*    */   extends JTabbedPane
/*    */ {
/*    */   private final Launcher minecraftLauncher;
/*    */   private final WebsiteTab blog;
/*    */   private final WebsiteTab news;
/*    */   private final ConsoleTab console;
/*    */   
/*    */   public LauncherTabPanel(Launcher minecraftLauncher) {
/* 19 */     super(1);
/* 20 */     this.news = new WebsiteTab(minecraftLauncher);
/* 21 */     this.minecraftLauncher = minecraftLauncher;
/* 22 */     this.blog = new WebsiteTab(minecraftLauncher);
/* 23 */     this.console = new ConsoleTab(minecraftLauncher);
/*    */     
/* 25 */     createInterface();
/*    */   }
/*    */ 
/*    */   
/*    */   protected void createInterface() {
/* 30 */     addTab(Language.get("main.tab.news"), this.news);
/*    */ 
/*    */     
/* 33 */     addTab("Server List", this.blog);
/* 34 */     addTab(Language.get("main.tab.devcons"), this.console);
/* 35 */     addTab(Language.get("main.tab.peditor"), new ProfileListTab(this.minecraftLauncher));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Launcher getMinecraftLauncher() {
/* 42 */     return this.minecraftLauncher;
/*    */   }
/*    */ 
/*    */   
/*    */   public WebsiteTab getBlog() {
/* 47 */     return this.blog;
/*    */   }
/*    */   
/*    */   public WebsiteTab ggg() {
/* 51 */     return this.news;
/*    */   }
/*    */ 
/*    */   
/*    */   public ConsoleTab getConsole() {
/* 56 */     return this.console;
/*    */   }
/*    */ 
/*    */   
/*    */   public void showConsole() {
/* 61 */     setSelectedComponent(this.console);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void removeTab(Component tab) {
/* 67 */     for (int i = 0; i < getTabCount(); i++) {
/* 68 */       if (getTabComponentAt(i) == tab) {
/*    */         
/* 70 */         removeTabAt(i);
/*    */         break;
/*    */       } 
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void removeTab(String name) {
/* 78 */     int index = indexOfTab(name);
/* 79 */     if (index > -1)
/* 80 */       removeTabAt(index); 
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\net\minecraft\launche\\ui\tabs\LauncherTabPanel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */