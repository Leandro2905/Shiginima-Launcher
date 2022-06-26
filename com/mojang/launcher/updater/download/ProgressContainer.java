/*    */ package com.mojang.launcher.updater.download;
/*    */ 
/*    */ 
/*    */ public class ProgressContainer
/*    */ {
/*    */   private long total;
/*    */   private long current;
/*    */   private DownloadJob job;
/*    */   
/*    */   public DownloadJob getJob() {
/* 11 */     return this.job;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setJob(DownloadJob job) {
/* 16 */     this.job = job;
/* 17 */     if (job != null) {
/* 18 */       job.updateProgress();
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public long getTotal() {
/* 24 */     return this.total;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setTotal(long total) {
/* 29 */     this.total = total;
/* 30 */     if (this.job != null) {
/* 31 */       this.job.updateProgress();
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public long getCurrent() {
/* 37 */     return this.current;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setCurrent(long current) {
/* 42 */     this.current = current;
/* 43 */     if (current > this.total) {
/* 44 */       this.total = current;
/*    */     }
/* 46 */     if (this.job != null) {
/* 47 */       this.job.updateProgress();
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void addProgress(long amount) {
/* 53 */     setCurrent(getCurrent() + amount);
/*    */   }
/*    */ 
/*    */   
/*    */   public float getProgress() {
/* 58 */     if (this.total == 0L) {
/* 59 */       return 0.0F;
/*    */     }
/* 61 */     return (float)this.current / (float)this.total;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 66 */     return "ProgressContainer{current=" + this.current + ", total=" + this.total + '}';
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\mojang\launche\\updater\download\ProgressContainer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */