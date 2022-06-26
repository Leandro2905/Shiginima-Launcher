/*    */ package net.minecraft.launcher.profile;
/*    */ 
/*    */ public enum LauncherVisibilityRule
/*    */ {
/*  5 */   HIDE_LAUNCHER("Hide launcher and re-open when game closes"), CLOSE_LAUNCHER("Close launcher when game starts"), DO_NOTHING("Keep the launcher open");
/*    */   
/*    */   private final String name;
/*    */ 
/*    */   
/*    */   LauncherVisibilityRule(String name) {
/* 11 */     this.name = name;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getName() {
/* 16 */     return this.name;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 21 */     return this.name;
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\net\minecraft\launcher\profile\LauncherVisibilityRule.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */