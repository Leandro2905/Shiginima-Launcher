/*    */ package com.mojang.launcher.updater.download;
/*    */ 
/*    */ import java.io.FilterInputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ 
/*    */ 
/*    */ public class MonitoringInputStream
/*    */   extends FilterInputStream
/*    */ {
/*    */   private final ProgressContainer monitor;
/*    */   
/*    */   public MonitoringInputStream(InputStream in, ProgressContainer monitor) {
/* 14 */     super(in);
/* 15 */     this.monitor = monitor;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public int read() throws IOException {
/* 21 */     int result = this.in.read();
/* 22 */     if (result >= 0) {
/* 23 */       this.monitor.addProgress(1L);
/*    */     }
/* 25 */     return result;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public int read(byte[] buffer) throws IOException {
/* 31 */     int size = this.in.read(buffer);
/* 32 */     if (size >= 0) {
/* 33 */       this.monitor.addProgress(size);
/*    */     }
/* 35 */     return size;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public int read(byte[] buffer, int off, int len) throws IOException {
/* 41 */     int size = this.in.read(buffer, off, len);
/* 42 */     if (size > 0) {
/* 43 */       this.monitor.addProgress(size);
/*    */     }
/* 45 */     return size;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public long skip(long size) throws IOException {
/* 51 */     long skipped = super.skip(size);
/* 52 */     if (skipped > 0L) {
/* 53 */       this.monitor.addProgress(skipped);
/*    */     }
/* 55 */     return skipped;
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\mojang\launche\\updater\download\MonitoringInputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */