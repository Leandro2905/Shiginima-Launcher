/*    */ package net.minecraft.launcher.updater;
/*    */ 
/*    */ import com.mojang.launcher.updater.download.Downloadable;
/*    */ import com.mojang.launcher.updater.download.MonitoringInputStream;
/*    */ import java.io.File;
/*    */ import java.io.FileOutputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.net.HttpURLConnection;
/*    */ import java.net.Proxy;
/*    */ import java.net.URL;
/*    */ import org.apache.commons.io.FileUtils;
/*    */ 
/*    */ 
/*    */ public class PreHashedDownloadable
/*    */   extends Downloadable
/*    */ {
/*    */   private final String expectedHash;
/*    */   
/*    */   public PreHashedDownloadable(Proxy proxy, URL remoteFile, File localFile, boolean forceDownload, String expectedHash) {
/* 21 */     super(proxy, remoteFile, localFile, forceDownload);
/* 22 */     this.expectedHash = expectedHash;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String download() throws IOException {
/* 28 */     this.numAttempts++;
/* 29 */     ensureFileWritable(getTarget());
/* 30 */     File target = getTarget();
/* 31 */     String localHash = null;
/* 32 */     if (target.isFile()) {
/*    */       
/* 34 */       localHash = getDigest(target, "SHA-1", 40);
/* 35 */       if (this.expectedHash.equalsIgnoreCase(localHash)) {
/* 36 */         return "Local file matches hash, using that";
/*    */       }
/* 38 */       FileUtils.deleteQuietly(target);
/*    */     } 
/*    */     
/*    */     try {
/* 42 */       HttpURLConnection connection = makeConnection(getUrl());
/* 43 */       int status = connection.getResponseCode();
/* 44 */       if (status / 100 == 2) {
/*    */         
/* 46 */         updateExpectedSize(connection);
/*    */         
/* 48 */         MonitoringInputStream monitoringInputStream = new MonitoringInputStream(connection.getInputStream(), getMonitor());
/* 49 */         FileOutputStream outputStream = new FileOutputStream(getTarget());
/* 50 */         String digest = copyAndDigest((InputStream)monitoringInputStream, outputStream, "SHA", 40);
/* 51 */         if (this.expectedHash.equalsIgnoreCase(digest)) {
/* 52 */           return "Downloaded successfully and hash matched";
/*    */         }
/* 54 */         throw new RuntimeException(String.format("Hash did not match downloaded file (Expected %s, downloaded %s)", new Object[] { this.expectedHash, digest }));
/*    */       } 
/* 56 */       if (getTarget().isFile()) {
/* 57 */         return "Couldn't connect to server (responded with " + status + ") but have local file, assuming it's good";
/*    */       }
/* 59 */       throw new RuntimeException("Server responded with " + status);
/*    */     }
/* 61 */     catch (IOException e) {
/*    */       
/* 63 */       if (getTarget().isFile()) {
/* 64 */         return "Couldn't connect to server (" + e.getClass().getSimpleName() + ": '" + e.getMessage() + "') but have local file, assuming it's good";
/*    */       }
/* 66 */       throw e;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\net\minecraft\launche\\updater\PreHashedDownloadable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */