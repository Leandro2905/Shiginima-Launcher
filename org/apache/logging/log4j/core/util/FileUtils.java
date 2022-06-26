/*     */ package org.apache.logging.log4j.core.util;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.net.URL;
/*     */ import java.net.URLDecoder;
/*     */ import java.util.regex.Pattern;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.status.StatusLogger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class FileUtils
/*     */ {
/*     */   private static final String PROTOCOL_FILE = "file";
/*     */   private static final String JBOSS_FILE = "vfsfile";
/*  42 */   private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*  43 */   private static final Pattern WINDOWS_DIRECTORY_SEPARATOR = Pattern.compile("\\\\+");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static File fileFromUri(URI uri) {
/*  56 */     if (uri == null || (uri.getScheme() != null && !"file".equals(uri.getScheme()) && !"vfsfile".equals(uri.getScheme())))
/*     */     {
/*  58 */       return null;
/*     */     }
/*  60 */     if (uri.getScheme() == null) {
/*     */       try {
/*  62 */         uri = (new File(uri.getPath())).toURI();
/*  63 */       } catch (Exception ex) {
/*  64 */         LOGGER.warn("Invalid URI {}", new Object[] { uri });
/*  65 */         return null;
/*     */       } 
/*     */     }
/*     */     try {
/*  69 */       String fileName = uri.toURL().getFile();
/*  70 */       if ((new File(fileName)).exists()) {
/*  71 */         return new File(fileName);
/*     */       }
/*  73 */       return new File(URLDecoder.decode(fileName, "UTF8"));
/*  74 */     } catch (MalformedURLException ex) {
/*  75 */       LOGGER.warn("Invalid URL {}", new Object[] { uri, ex });
/*  76 */     } catch (UnsupportedEncodingException uee) {
/*  77 */       LOGGER.warn("Invalid encoding: UTF8", uee);
/*     */     } 
/*  79 */     return null;
/*     */   }
/*     */   
/*     */   public static boolean isFile(URL url) {
/*  83 */     return (url != null && (url.getProtocol().equals("file") || url.getProtocol().equals("vfsfile")));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void mkdir(File dir, boolean createDirectoryIfNotExisting) throws IOException {
/*  94 */     if (!dir.exists()) {
/*  95 */       if (!createDirectoryIfNotExisting) {
/*  96 */         throw new IOException("The directory " + dir.getAbsolutePath() + " does not exist.");
/*     */       }
/*  98 */       if (!dir.mkdirs()) {
/*  99 */         throw new IOException("Could not create directory " + dir.getAbsolutePath());
/*     */       }
/*     */     } 
/* 102 */     if (!dir.isDirectory()) {
/* 103 */       throw new IOException("File " + dir + " exists and is not a directory. Unable to create directory.");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static URI getCorrectedFilePathUri(String uri) throws URISyntaxException {
/* 116 */     return new URI(WINDOWS_DIRECTORY_SEPARATOR.matcher(uri).replaceAll("/"));
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\cor\\util\FileUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */