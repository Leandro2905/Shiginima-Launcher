/*    */ package net.minecraft.launcher.game;
/*    */ 
/*    */ import com.google.common.collect.Maps;
/*    */ import com.mojang.launcher.versions.ReleaseType;
/*    */ import java.util.Map;
/*    */ import net.minecraft.launcher.Language;
/*    */ 
/*    */ public enum MinecraftReleaseType implements ReleaseType {
/*    */   private final String description;
/*    */   private final String name;
/* 11 */   SNAPSHOT("snapshot", Language.get("main.pe.mrt.snap")), RELEASE("release", null), OLD_BETA("old_beta", Language.get("main.pe.mrt.oldbeta")), OLD_ALPHA("old_alpha", Language.get("main.pe.mrt.oldalpha"));
/*    */   static {
/* 13 */     POPUP_DEV_VERSIONS = Language.get("main.pe.mrt.dev.popup");
/* 14 */     POPUP_OLD_VERSIONS = Language.get("main.pe.mrt.old.popup");
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 21 */     LOOKUP = Maps.newHashMap();
/* 22 */     for (MinecraftReleaseType type : values())
/* 23 */       LOOKUP.put(type.getName(), type); 
/*    */   }
/*    */   
/*    */   private static final Map<String, MinecraftReleaseType> LOOKUP;
/*    */   
/*    */   MinecraftReleaseType(String name, String description) {
/* 29 */     this.name = name;
/* 30 */     this.description = description;
/*    */   }
/*    */   private static final String POPUP_OLD_VERSIONS; private static final String POPUP_DEV_VERSIONS;
/*    */   
/*    */   public String getName() {
/* 35 */     return this.name;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getDescription() {
/* 40 */     return this.description;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getPopupWarning() {
/* 45 */     if (this.description == null) {
/* 46 */       return null;
/*    */     }
/* 48 */     if (this == SNAPSHOT) {
/* 49 */       return Language.get("main.pe.mrt.snap.popup");
/*    */     }
/* 51 */     if (this == OLD_BETA) {
/* 52 */       return Language.get("main.pe.mrt.oldbeta.popup");
/*    */     }
/* 54 */     if (this == OLD_ALPHA) {
/* 55 */       return Language.get("main.pe.mrt.oldalpha.popup");
/*    */     }
/* 57 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public static MinecraftReleaseType getByName(String name) {
/* 62 */     return LOOKUP.get(name);
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\net\minecraft\launcher\game\MinecraftReleaseType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */