/*     */ package com.mojang.launcher.updater.download.assets;
/*     */ 
/*     */ import com.mojang.launcher.updater.download.Downloadable;
/*     */ import com.mojang.launcher.updater.download.MonitoringInputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.net.HttpURLConnection;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.Proxy;
/*     */ import java.net.URL;
/*     */ import java.util.zip.GZIPInputStream;
/*     */ import org.apache.commons.io.FileUtils;
/*     */ import org.apache.commons.io.IOUtils;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class AssetDownloadable
/*     */   extends Downloadable
/*     */ {
/*  23 */   private static final Logger LOGGER = LogManager.getLogger();
/*     */   private final String name;
/*     */   private final AssetIndex.AssetObject asset;
/*     */   private final String urlBase;
/*     */   private final File destination;
/*  28 */   private Status status = Status.DOWNLOADING;
/*     */ 
/*     */ 
/*     */   
/*     */   public AssetDownloadable(Proxy proxy, String name, AssetIndex.AssetObject asset, String urlBase, File destination) throws MalformedURLException {
/*  33 */     super(proxy, new URL(urlBase + createPathFromHash(asset.getHash())), new File(destination, createPathFromHash(asset.getHash())), false);
/*  34 */     this.name = name;
/*  35 */     this.asset = asset;
/*  36 */     this.urlBase = urlBase;
/*  37 */     this.destination = destination;
/*     */   }
/*     */ 
/*     */   
/*     */   protected static String createPathFromHash(String hash) {
/*  42 */     return hash.substring(0, 2) + "/" + hash;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String download() throws IOException {
/*  48 */     this.status = Status.DOWNLOADING;
/*     */     
/*  50 */     this.numAttempts++;
/*  51 */     File localAsset = getTarget();
/*  52 */     File localCompressed = this.asset.hasCompressedAlternative() ? new File(this.destination, createPathFromHash(this.asset.getCompressedHash())) : null;
/*  53 */     URL remoteAsset = getUrl();
/*  54 */     URL remoteCompressed = this.asset.hasCompressedAlternative() ? new URL(this.urlBase + createPathFromHash(this.asset.getCompressedHash())) : null;
/*     */     
/*  56 */     ensureFileWritable(localAsset);
/*  57 */     if (localCompressed != null) {
/*  58 */       ensureFileWritable(localCompressed);
/*     */     }
/*  60 */     if (localAsset.isFile()) {
/*     */       
/*  62 */       if (FileUtils.sizeOf(localAsset) == this.asset.getSize()) {
/*  63 */         return "Have local file and it's the same size; assuming it's okay!";
/*     */       }
/*  65 */       LOGGER.warn("Had local file but it was the wrong size... had {} but expected {}", new Object[] { Long.valueOf(FileUtils.sizeOf(localAsset)), Long.valueOf(this.asset.getSize()) });
/*  66 */       FileUtils.deleteQuietly(localAsset);
/*  67 */       this.status = Status.DOWNLOADING;
/*     */     } 
/*  69 */     if (localCompressed != null && localCompressed.isFile()) {
/*     */       
/*  71 */       String localCompressedHash = getDigest(localCompressed, "SHA", 40);
/*  72 */       if (localCompressedHash.equalsIgnoreCase(this.asset.getCompressedHash())) {
/*  73 */         return decompressAsset(localAsset, localCompressed);
/*     */       }
/*  75 */       LOGGER.warn("Had local compressed but it was the wrong hash... expected {} but had {}", new Object[] { this.asset.getCompressedHash(), localCompressedHash });
/*  76 */       FileUtils.deleteQuietly(localCompressed);
/*     */     } 
/*  78 */     if (remoteCompressed != null && localCompressed != null) {
/*     */       
/*  80 */       HttpURLConnection httpURLConnection = makeConnection(remoteCompressed);
/*  81 */       int i = httpURLConnection.getResponseCode();
/*  82 */       if (i / 100 == 2) {
/*     */         
/*  84 */         updateExpectedSize(httpURLConnection);
/*     */         
/*  86 */         MonitoringInputStream monitoringInputStream = new MonitoringInputStream(httpURLConnection.getInputStream(), getMonitor());
/*  87 */         FileOutputStream outputStream = new FileOutputStream(localCompressed);
/*  88 */         String hash = copyAndDigest((InputStream)monitoringInputStream, outputStream, "SHA", 40);
/*  89 */         if (hash.equalsIgnoreCase(this.asset.getCompressedHash())) {
/*  90 */           return decompressAsset(localAsset, localCompressed);
/*     */         }
/*  92 */         FileUtils.deleteQuietly(localCompressed);
/*  93 */         throw new RuntimeException(String.format("Hash did not match downloaded compressed asset (Expected %s, downloaded %s)", new Object[] { this.asset.getCompressedHash(), hash }));
/*     */       } 
/*  95 */       throw new RuntimeException("Server responded with " + i);
/*     */     } 
/*  97 */     HttpURLConnection connection = makeConnection(remoteAsset);
/*  98 */     int status = connection.getResponseCode();
/*  99 */     if (status / 100 == 2) {
/*     */       
/* 101 */       updateExpectedSize(connection);
/*     */       
/* 103 */       MonitoringInputStream monitoringInputStream = new MonitoringInputStream(connection.getInputStream(), getMonitor());
/* 104 */       FileOutputStream outputStream = new FileOutputStream(localAsset);
/* 105 */       String hash = copyAndDigest((InputStream)monitoringInputStream, outputStream, "SHA", 40);
/* 106 */       if (hash.equalsIgnoreCase(this.asset.getHash())) {
/* 107 */         return "Downloaded asset and hash matched successfully";
/*     */       }
/* 109 */       FileUtils.deleteQuietly(localAsset);
/* 110 */       throw new RuntimeException(String.format("Hash did not match downloaded asset (Expected %s, downloaded %s)", new Object[] { this.asset.getHash(), hash }));
/*     */     } 
/* 112 */     throw new RuntimeException("Server responded with " + status);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getStatus() {
/* 117 */     return this.status.name + " " + this.name;
/*     */   }
/*     */ 
/*     */   
/*     */   protected String decompressAsset(File localAsset, File localCompressed) throws IOException {
/*     */     String hash;
/* 123 */     this.status = Status.EXTRACTING;
/* 124 */     OutputStream outputStream = FileUtils.openOutputStream(localAsset);
/* 125 */     InputStream inputStream = new GZIPInputStream(FileUtils.openInputStream(localCompressed));
/*     */ 
/*     */     
/*     */     try {
/* 129 */       hash = copyAndDigest(inputStream, outputStream, "SHA", 40);
/*     */     }
/*     */     finally {
/*     */       
/* 133 */       IOUtils.closeQuietly(outputStream);
/* 134 */       IOUtils.closeQuietly(inputStream);
/*     */     } 
/* 136 */     this.status = Status.DOWNLOADING;
/* 137 */     if (hash.equalsIgnoreCase(this.asset.getHash())) {
/* 138 */       return "Had local compressed asset, unpacked successfully and hash matched";
/*     */     }
/* 140 */     FileUtils.deleteQuietly(localAsset);
/* 141 */     throw new RuntimeException("Had local compressed asset but unpacked hash did not match (expected " + this.asset.getHash() + " but had " + hash + ")");
/*     */   }
/*     */   
/*     */   private enum Status
/*     */   {
/* 146 */     DOWNLOADING("Downloading"), EXTRACTING("Extracting");
/*     */     
/*     */     private final String name;
/*     */ 
/*     */     
/*     */     Status(String name) {
/* 152 */       this.name = name;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\mojang\launche\\updater\download\assets\AssetDownloadable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */