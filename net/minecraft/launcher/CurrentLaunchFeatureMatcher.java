/*    */ package net.minecraft.launcher;
/*    */ 
/*    */ import net.minecraft.launcher.profile.Profile;
/*    */ import net.minecraft.launcher.updater.CompleteMinecraftVersion;
/*    */ 
/*    */ 
/*    */ public class CurrentLaunchFeatureMatcher
/*    */   implements CompatibilityRule.FeatureMatcher
/*    */ {
/*    */   private final Profile profile;
/*    */   private final CompleteMinecraftVersion version;
/*    */   
/*    */   public CurrentLaunchFeatureMatcher(Profile profile, CompleteMinecraftVersion version) {
/* 14 */     this.profile = profile;
/* 15 */     this.version = version;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean hasFeature(String name, Object value) {
/* 20 */     if (name.equals(""));
/* 21 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\net\minecraft\launcher\CurrentLaunchFeatureMatcher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */