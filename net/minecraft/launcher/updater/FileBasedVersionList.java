/*    */ package net.minecraft.launcher.updater;
/*    */ 
/*    */ import com.mojang.launcher.versions.Version;
/*    */ import java.io.FileNotFoundException;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import net.minecraft.launcher.game.MinecraftReleaseType;
/*    */ import org.apache.commons.io.IOUtils;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class FileBasedVersionList
/*    */   extends VersionList
/*    */ {
/*    */   public String getContent(String path) throws IOException {
/* 18 */     return IOUtils.toString(getFileInputStream(path)).replaceAll("\\r\\n", "\r").replaceAll("\\r", "\n");
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected abstract InputStream getFileInputStream(String paramString) throws FileNotFoundException;
/*    */ 
/*    */   
/*    */   public CompleteMinecraftVersion getCompleteVersion(Version version) throws IOException {
/* 27 */     if (version instanceof com.mojang.launcher.versions.CompleteVersion) {
/* 28 */       return (CompleteMinecraftVersion)version;
/*    */     }
/* 30 */     if (!(version instanceof PartialVersion)) {
/* 31 */       throw new IllegalArgumentException("Version must be a partial");
/*    */     }
/* 33 */     PartialVersion partial = (PartialVersion)version;
/*    */     
/* 35 */     CompleteMinecraftVersion complete = (CompleteMinecraftVersion)this.gson.fromJson(getContent("versions/" + version.getId() + "/" + version.getId() + ".json"), CompleteMinecraftVersion.class);
/* 36 */     MinecraftReleaseType type = (MinecraftReleaseType)version.getType();
/*    */     
/* 38 */     replacePartialWithFull(partial, complete);
/*    */     
/* 40 */     return complete;
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\net\minecraft\launche\\updater\FileBasedVersionList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */