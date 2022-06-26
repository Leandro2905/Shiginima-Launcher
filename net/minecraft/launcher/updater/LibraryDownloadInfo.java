/*    */ package net.minecraft.launcher.updater;
/*    */ 
/*    */ import java.util.LinkedHashMap;
/*    */ import java.util.Map;
/*    */ 
/*    */ public class LibraryDownloadInfo
/*    */ {
/*    */   private LocalDownloadInfo artifact;
/*    */   private Map<String, DownloadInfo> classifiers;
/*    */   
/*    */   public LibraryDownloadInfo() {}
/*    */   
/*    */   public LibraryDownloadInfo(LibraryDownloadInfo other) {
/* 14 */     this.artifact = other.artifact;
/* 15 */     if (other.classifiers != null) {
/* 16 */       this.classifiers = new LinkedHashMap<>();
/* 17 */       for (Map.Entry<String, DownloadInfo> entry : other.classifiers.entrySet())
/* 18 */         this.classifiers.put(entry.getKey(), new DownloadInfo(entry.getValue())); 
/*    */     } 
/*    */   }
/*    */   
/*    */   public AbstractDownloadInfo getDownloadInfo(String classifier) {
/* 23 */     if (classifier == null)
/* 24 */       return this.artifact; 
/* 25 */     return this.classifiers.get(classifier);
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\net\minecraft\launche\\updater\LibraryDownloadInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */