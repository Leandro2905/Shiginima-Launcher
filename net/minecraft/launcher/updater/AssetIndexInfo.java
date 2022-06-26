/*    */ package net.minecraft.launcher.updater;
/*    */ 
/*    */ import net.minecraft.launcher.LauncherConstants;
/*    */ 
/*    */ 
/*    */ public class AssetIndexInfo
/*    */   extends DownloadInfo
/*    */ {
/*    */   protected long totalSize;
/*    */   protected String id;
/*    */   protected boolean known = true;
/*    */   
/*    */   public AssetIndexInfo() {}
/*    */   
/*    */   public AssetIndexInfo(String id) {
/* 16 */     this.id = id;
/* 17 */     this.url = LauncherConstants.constantURL("https://s3.amazonaws.com/Minecraft.Download/indexes/" + id + ".json");
/* 18 */     this.known = false;
/*    */   }
/*    */ 
/*    */   
/*    */   public long getTotalSize() {
/* 23 */     return this.totalSize;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getId() {
/* 28 */     return this.id;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean sizeAndHashKnown() {
/* 33 */     return this.known;
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\net\minecraft\launche\\updater\AssetIndexInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */