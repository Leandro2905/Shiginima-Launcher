/*    */ package net.minecraft.launcher.updater;
/*    */ 
/*    */ import java.net.MalformedURLException;
/*    */ import java.net.URL;
/*    */ 
/*    */ public class LocalDownloadInfo
/*    */   extends AbstractDownloadInfo
/*    */ {
/*    */   protected String url;
/*    */   protected String sha1;
/*    */   protected int size;
/*    */   
/*    */   public LocalDownloadInfo() {}
/*    */   
/*    */   public LocalDownloadInfo(LocalDownloadInfo other) {
/* 16 */     this.url = other.url;
/* 17 */     this.sha1 = other.sha1;
/* 18 */     this.size = other.size;
/*    */   }
/*    */   
/*    */   public URL getUrl() {
/*    */     try {
/* 23 */       return new URL(this.url);
/* 24 */     } catch (MalformedURLException e) {
/* 25 */       return null;
/*    */     } 
/*    */   }
/*    */   
/*    */   public String getSha1() {
/* 30 */     return this.sha1;
/*    */   }
/*    */   
/*    */   public int getSize() {
/* 34 */     return this.size;
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\net\minecraft\launche\\updater\LocalDownloadInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */