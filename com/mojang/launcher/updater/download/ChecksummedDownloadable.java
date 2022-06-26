/*     */ package com.mojang.launcher.updater.download;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.HttpURLConnection;
/*     */ import java.net.Proxy;
/*     */ import java.net.URL;
/*     */ import org.apache.commons.io.Charsets;
/*     */ import org.apache.commons.io.FileUtils;
/*     */ import org.apache.commons.io.IOUtils;
/*     */ 
/*     */ 
/*     */ public class ChecksummedDownloadable
/*     */   extends Downloadable
/*     */ {
/*     */   private String checksum;
/*     */   
/*     */   public ChecksummedDownloadable(Proxy proxy, URL remoteFile, File localFile, boolean forceDownload) {
/*  21 */     super(proxy, remoteFile, localFile, forceDownload);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String download() throws IOException {
/*  27 */     this.numAttempts++;
/*  28 */     ensureFileWritable(getTarget());
/*     */     
/*  30 */     File target = getTarget();
/*  31 */     File checksumFile = new File(target.getAbsolutePath() + ".sha");
/*  32 */     String localHash = null;
/*  33 */     if (target.isFile()) {
/*  34 */       localHash = getDigest(target, "SHA-1", 40);
/*     */     }
/*  36 */     if (target.isFile() && checksumFile.isFile()) {
/*     */       
/*  38 */       this.checksum = readFile(checksumFile, "");
/*  39 */       if (this.checksum.length() == 0 || this.checksum.trim().equalsIgnoreCase(localHash)) {
/*  40 */         return "Local file matches local checksum, using that";
/*     */       }
/*  42 */       this.checksum = null;
/*  43 */       FileUtils.deleteQuietly(checksumFile);
/*     */     } 
/*  45 */     if (this.checksum == null) {
/*     */       
/*     */       try {
/*  48 */         HttpURLConnection connection = makeConnection(new URL(getUrl().toString() + ".sha1"));
/*  49 */         int status = connection.getResponseCode();
/*  50 */         if (status / 100 == 2) {
/*     */           
/*  52 */           InputStream inputStream = connection.getInputStream();
/*     */           
/*     */           try {
/*  55 */             this.checksum = IOUtils.toString(inputStream, Charsets.UTF_8);
/*  56 */             FileUtils.writeStringToFile(checksumFile, this.checksum);
/*     */           }
/*  58 */           catch (IOException e) {
/*     */             
/*  60 */             this.checksum = "";
/*     */           }
/*     */           finally {
/*     */             
/*  64 */             IOUtils.closeQuietly(inputStream);
/*     */           }
/*     */         
/*  67 */         } else if (checksumFile.isFile()) {
/*     */           
/*  69 */           this.checksum = readFile(checksumFile, "");
/*     */         }
/*     */         else {
/*     */           
/*  73 */           this.checksum = "";
/*     */         }
/*     */       
/*  76 */       } catch (IOException e) {
/*     */         
/*  78 */         if (target.isFile()) {
/*  79 */           this.checksum = readFile(checksumFile, "");
/*     */         } else {
/*  81 */           throw e;
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/*     */     try {
/*  87 */       HttpURLConnection connection = makeConnection(getUrl());
/*  88 */       int status = connection.getResponseCode();
/*  89 */       if (status / 100 == 2) {
/*     */         
/*  91 */         updateExpectedSize(connection);
/*     */         
/*  93 */         InputStream inputStream = new MonitoringInputStream(connection.getInputStream(), getMonitor());
/*  94 */         FileOutputStream outputStream = new FileOutputStream(getTarget());
/*  95 */         String digest = copyAndDigest(inputStream, outputStream, "SHA", 40);
/*  96 */         if (this.checksum == null || this.checksum.length() == 0) {
/*  97 */           return "Didn't have checksum so assuming our copy is good";
/*     */         }
/*  99 */         if (this.checksum.trim().equalsIgnoreCase(digest)) {
/* 100 */           return "Downloaded successfully and checksum matched";
/*     */         }
/* 102 */         throw new RuntimeException(String.format("Checksum did not match downloaded file (Checksum was %s, downloaded %s)", new Object[] { this.checksum, digest }));
/*     */       } 
/* 104 */       if (getTarget().isFile()) {
/* 105 */         return "Couldn't connect to server (responded with " + status + ") but have local file, assuming it's good";
/*     */       }
/* 107 */       throw new RuntimeException("Server responded with " + status);
/*     */     }
/* 109 */     catch (IOException e) {
/*     */       
/* 111 */       if (getTarget().isFile() && (this.checksum == null || this.checksum.length() == 0)) {
/* 112 */         return "Couldn't connect to server (" + e.getClass().getSimpleName() + ": '" + e.getMessage() + "') but have local file, assuming it's good";
/*     */       }
/* 114 */       throw e;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private String readFile(File file, String def) {
/*     */     try {
/* 122 */       return FileUtils.readFileToString(file);
/*     */     }
/* 124 */     catch (Throwable throwable) {
/* 125 */       return def;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\mojang\launche\\updater\download\ChecksummedDownloadable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */