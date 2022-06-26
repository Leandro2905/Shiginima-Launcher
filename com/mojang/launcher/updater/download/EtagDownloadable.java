/*    */ package com.mojang.launcher.updater.download;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.io.FileOutputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.net.HttpURLConnection;
/*    */ import java.net.Proxy;
/*    */ import java.net.URL;
/*    */ 
/*    */ 
/*    */ public class EtagDownloadable
/*    */   extends Downloadable
/*    */ {
/*    */   public EtagDownloadable(Proxy proxy, URL remoteFile, File localFile, boolean forceDownload) {
/* 16 */     super(proxy, remoteFile, localFile, forceDownload);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String download() throws IOException {
/* 22 */     this.numAttempts++;
/* 23 */     ensureFileWritable(getTarget());
/*    */     
/*    */     try {
/* 26 */       HttpURLConnection connection = makeConnection(getUrl());
/* 27 */       int status = connection.getResponseCode();
/* 28 */       if (status == 304) {
/* 29 */         return "Used own copy as it matched etag";
/*    */       }
/* 31 */       if (status / 100 == 2) {
/*    */         
/* 33 */         updateExpectedSize(connection);
/*    */         
/* 35 */         InputStream inputStream = new MonitoringInputStream(connection.getInputStream(), getMonitor());
/* 36 */         FileOutputStream outputStream = new FileOutputStream(getTarget());
/* 37 */         String md5 = copyAndDigest(inputStream, outputStream, "MD5", 32);
/* 38 */         String etag = getEtag(connection.getHeaderField("ETag"));
/* 39 */         if (etag.contains("-")) {
/* 40 */           return "Didn't have etag so assuming our copy is good";
/*    */         }
/* 42 */         if (etag.equalsIgnoreCase(md5)) {
/* 43 */           return "Downloaded successfully and etag matched";
/*    */         }
/* 45 */         throw new RuntimeException(String.format("E-tag did not match downloaded MD5 (ETag was %s, downloaded %s)", new Object[] { etag, md5 }));
/*    */       } 
/* 47 */       if (getTarget().isFile()) {
/* 48 */         return "Couldn't connect to server (responded with " + status + ") but have local file, assuming it's good";
/*    */       }
/* 50 */       throw new RuntimeException("Server responded with " + status);
/*    */     }
/* 52 */     catch (IOException e) {
/*    */       
/* 54 */       if (getTarget().isFile()) {
/* 55 */         return "Couldn't connect to server (" + e.getClass().getSimpleName() + ": '" + e.getMessage() + "') but have local file, assuming it's good";
/*    */       }
/* 57 */       throw e;
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected HttpURLConnection makeConnection(URL url) throws IOException {
/* 64 */     HttpURLConnection connection = super.makeConnection(url);
/* 65 */     if (!shouldIgnoreLocal() && getTarget().isFile()) {
/* 66 */       connection.setRequestProperty("If-None-Match", getDigest(getTarget(), "MD5", 32));
/*    */     }
/* 68 */     return connection;
/*    */   }
/*    */ 
/*    */   
/*    */   public static String getEtag(String etag) {
/* 73 */     if (etag == null) {
/* 74 */       etag = "-";
/* 75 */     } else if (etag.startsWith("\"") && etag.endsWith("\"")) {
/* 76 */       etag = etag.substring(1, etag.length() - 1);
/*    */     } 
/* 78 */     return etag;
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\mojang\launche\\updater\download\EtagDownloadable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */