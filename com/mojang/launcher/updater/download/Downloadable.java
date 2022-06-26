/*     */ package com.mojang.launcher.updater.download;
/*     */ 
/*     */ import java.io.Closeable;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.math.BigInteger;
/*     */ import java.net.HttpURLConnection;
/*     */ import java.net.Proxy;
/*     */ import java.net.URL;
/*     */ import java.security.DigestInputStream;
/*     */ import java.security.MessageDigest;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public abstract class Downloadable
/*     */ {
/*  21 */   private static final Logger LOGGER = LogManager.getLogger();
/*     */   
/*     */   private final URL url;
/*     */   private final File target;
/*     */   private final boolean forceDownload;
/*     */   private final Proxy proxy;
/*     */   private final ProgressContainer monitor;
/*     */   private long startTime;
/*     */   protected int numAttempts;
/*     */   private long expectedSize;
/*     */   private long endTime;
/*     */   
/*     */   public Downloadable(Proxy proxy, URL remoteFile, File localFile, boolean forceDownload) {
/*  34 */     this.proxy = proxy;
/*  35 */     this.url = remoteFile;
/*  36 */     this.target = localFile;
/*  37 */     this.forceDownload = forceDownload;
/*  38 */     this.monitor = new ProgressContainer();
/*     */   }
/*     */ 
/*     */   
/*     */   public ProgressContainer getMonitor() {
/*  43 */     return this.monitor;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getExpectedSize() {
/*  48 */     return this.expectedSize;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setExpectedSize(long expectedSize) {
/*  53 */     this.expectedSize = expectedSize;
/*     */   }
/*     */ 
/*     */   
/*     */   public static String getDigest(File file, String algorithm, int hashLength) {
/*  58 */     DigestInputStream stream = null;
/*     */     try {
/*     */       int read;
/*  61 */       stream = new DigestInputStream(new FileInputStream(file), MessageDigest.getInstance(algorithm));
/*  62 */       byte[] buffer = new byte[65536];
/*     */ 
/*     */       
/*     */       do {
/*  66 */         read = stream.read(buffer);
/*  67 */       } while (read > 0);
/*     */     }
/*  69 */     catch (Exception ignored) {
/*     */ 
/*     */       
/*  72 */       return null;
/*     */     }
/*     */     finally {
/*     */       
/*  76 */       closeSilently(stream);
/*     */     } 
/*  78 */     return String.format("%1$0" + hashLength + "x", new Object[] { new BigInteger(1, stream.getMessageDigest().digest()) });
/*     */   }
/*     */ 
/*     */   
/*     */   public abstract String download() throws IOException;
/*     */ 
/*     */   
/*     */   protected void updateExpectedSize(HttpURLConnection connection) {
/*  86 */     if (this.expectedSize == 0L) {
/*     */       
/*  88 */       this.monitor.setTotal(connection.getContentLength());
/*  89 */       setExpectedSize(connection.getContentLength());
/*     */     }
/*     */     else {
/*     */       
/*  93 */       this.monitor.setTotal(this.expectedSize);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected HttpURLConnection makeConnection(URL url) throws IOException {
/* 100 */     HttpURLConnection connection = (HttpURLConnection)url.openConnection(this.proxy);
/*     */ 
/*     */     
/* 103 */     connection.setUseCaches(false);
/* 104 */     connection.setDefaultUseCaches(false);
/* 105 */     connection.setRequestProperty("Cache-Control", "no-store,max-age=0,no-cache");
/* 106 */     connection.setRequestProperty("Expires", "0");
/* 107 */     connection.setRequestProperty("Pragma", "no-cache");
/* 108 */     connection.setConnectTimeout(5000);
/* 109 */     connection.setReadTimeout(30000);
/*     */     
/* 111 */     return connection;
/*     */   }
/*     */ 
/*     */   
/*     */   public URL getUrl() {
/* 116 */     return this.url;
/*     */   }
/*     */ 
/*     */   
/*     */   public File getTarget() {
/* 121 */     return this.target;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldIgnoreLocal() {
/* 126 */     return this.forceDownload;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getNumAttempts() {
/* 131 */     return this.numAttempts;
/*     */   }
/*     */ 
/*     */   
/*     */   public Proxy getProxy() {
/* 136 */     return this.proxy;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void closeSilently(Closeable closeable) {
/* 141 */     if (closeable != null) {
/*     */       
/*     */       try {
/* 144 */         closeable.close();
/*     */       }
/* 146 */       catch (IOException iOException) {}
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String copyAndDigest(InputStream inputStream, OutputStream outputStream, String algorithm, int hashLength) throws IOException {
/*     */     MessageDigest digest;
/*     */     try {
/* 156 */       digest = MessageDigest.getInstance(algorithm);
/*     */     }
/* 158 */     catch (NoSuchAlgorithmException e) {
/*     */       
/* 160 */       closeSilently(inputStream);
/* 161 */       closeSilently(outputStream);
/* 162 */       throw new RuntimeException("Missing Digest." + algorithm, e);
/*     */     } 
/* 164 */     byte[] buffer = new byte[65536];
/*     */     
/*     */     try {
/* 167 */       int read = inputStream.read(buffer);
/* 168 */       while (read >= 1)
/*     */       {
/* 170 */         digest.update(buffer, 0, read);
/* 171 */         outputStream.write(buffer, 0, read);
/* 172 */         read = inputStream.read(buffer);
/*     */       }
/*     */     
/*     */     } finally {
/*     */       
/* 177 */       closeSilently(inputStream);
/* 178 */       closeSilently(outputStream);
/*     */     } 
/* 180 */     return String.format("%1$0" + hashLength + "x", new Object[] { new BigInteger(1, digest.digest()) });
/*     */   }
/*     */ 
/*     */   
/*     */   protected void ensureFileWritable(File target) {
/* 185 */     if (target.getParentFile() != null && !target.getParentFile().isDirectory()) {
/*     */       
/* 187 */       LOGGER.info("Making directory " + target.getParentFile());
/* 188 */       if (!target.getParentFile().mkdirs() && 
/* 189 */         !target.getParentFile().isDirectory()) {
/* 190 */         throw new RuntimeException("Could not create directory " + target.getParentFile());
/*     */       }
/*     */     } 
/* 193 */     if (target.isFile() && !target.canWrite()) {
/* 194 */       throw new RuntimeException("Do not have write permissions for " + target + " - aborting!");
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public long getStartTime() {
/* 200 */     return this.startTime;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setStartTime(long startTime) {
/* 205 */     this.startTime = startTime;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getStatus() {
/* 210 */     return "Downloading " + getTarget().getName();
/*     */   }
/*     */ 
/*     */   
/*     */   public long getEndTime() {
/* 215 */     return this.endTime;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setEndTime(long endTime) {
/* 220 */     this.endTime = endTime;
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\mojang\launche\\updater\download\Downloadable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */