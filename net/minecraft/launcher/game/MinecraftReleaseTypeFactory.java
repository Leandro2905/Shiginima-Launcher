/*    */ package net.minecraft.launcher.game;
/*    */ 
/*    */ import com.google.common.collect.Iterators;
/*    */ import com.mojang.launcher.versions.ReleaseType;
/*    */ import com.mojang.launcher.versions.ReleaseTypeFactory;
/*    */ import java.util.Iterator;
/*    */ 
/*    */ public class MinecraftReleaseTypeFactory
/*    */   implements ReleaseTypeFactory<MinecraftReleaseType> {
/* 10 */   private static final MinecraftReleaseTypeFactory FACTORY = new MinecraftReleaseTypeFactory();
/*    */ 
/*    */   
/*    */   public MinecraftReleaseType getTypeByName(String name) {
/* 14 */     return MinecraftReleaseType.getByName(name);
/*    */   }
/*    */ 
/*    */   
/*    */   public MinecraftReleaseType[] getAllTypes() {
/* 19 */     return MinecraftReleaseType.values();
/*    */   }
/*    */ 
/*    */   
/*    */   public Class<MinecraftReleaseType> getTypeClass() {
/* 24 */     return MinecraftReleaseType.class;
/*    */   }
/*    */ 
/*    */   
/*    */   public Iterator<MinecraftReleaseType> iterator() {
/* 29 */     return (Iterator<MinecraftReleaseType>)Iterators.forArray((Object[])MinecraftReleaseType.values());
/*    */   }
/*    */ 
/*    */   
/*    */   public static MinecraftReleaseTypeFactory instance() {
/* 34 */     return FACTORY;
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\net\minecraft\launcher\game\MinecraftReleaseTypeFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */