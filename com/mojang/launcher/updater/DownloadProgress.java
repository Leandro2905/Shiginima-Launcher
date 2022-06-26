/*    */ package com.mojang.launcher.updater;
/*    */ 
/*    */ 
/*    */ public class DownloadProgress
/*    */ {
/*    */   private final long current;
/*    */   private final long total;
/*    */   private final float percent;
/*    */   private final String status;
/*    */   
/*    */   public DownloadProgress(long current, long total, String status) {
/* 12 */     this.current = current;
/* 13 */     this.total = total;
/* 14 */     this.percent = (float)current / (float)total;
/* 15 */     this.status = status;
/*    */   }
/*    */ 
/*    */   
/*    */   public long getCurrent() {
/* 20 */     return this.current;
/*    */   }
/*    */ 
/*    */   
/*    */   public long getTotal() {
/* 25 */     return this.total;
/*    */   }
/*    */ 
/*    */   
/*    */   public float getPercent() {
/* 30 */     return this.percent;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getStatus() {
/* 35 */     return this.status;
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\mojang\launche\\updater\DownloadProgress.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */