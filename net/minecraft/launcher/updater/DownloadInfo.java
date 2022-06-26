/*    */ package net.minecraft.launcher.updater;
/*    */ 
/*    */ import java.net.URL;
/*    */ 
/*    */ public class DownloadInfo
/*    */   extends AbstractDownloadInfo
/*    */ {
/*    */   protected URL url;
/*    */   protected String sha1;
/*    */   protected int size;
/*    */   
/*    */   public DownloadInfo() {}
/*    */   
/*    */   public DownloadInfo(DownloadInfo other) {
/* 15 */     this.url = other.url;
/* 16 */     this.sha1 = other.sha1;
/* 17 */     this.size = other.size;
/*    */   }
/*    */ 
/*    */   
/*    */   public URL getUrl() {
/* 22 */     return this.url;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getSha1() {
/* 27 */     return this.sha1;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getSize() {
/* 32 */     return this.size;
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\net\minecraft\launche\\updater\DownloadInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */