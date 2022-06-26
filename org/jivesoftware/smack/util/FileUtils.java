/*     */ package org.jivesoftware.smack.util;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.FileReader;
/*     */ import java.io.FileWriter;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.Reader;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URI;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
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
/*  38 */   private static final Logger LOGGER = Logger.getLogger(FileUtils.class.getName());
/*     */   
/*     */   public static InputStream getStreamForUrl(String url, ClassLoader loader) throws MalformedURLException, IOException {
/*  41 */     URI fileUri = URI.create(url);
/*     */     
/*  43 */     if (fileUri.getScheme() == null) {
/*  44 */       throw new MalformedURLException("No protocol found in file URL: " + url);
/*     */     }
/*     */     
/*  47 */     if (fileUri.getScheme().equals("classpath")) {
/*     */       
/*  49 */       List<ClassLoader> classLoaders = getClassLoaders();
/*  50 */       if (loader != null) {
/*  51 */         classLoaders.add(0, loader);
/*     */       }
/*  53 */       for (ClassLoader classLoader : classLoaders) {
/*  54 */         InputStream is = classLoader.getResourceAsStream(fileUri.getSchemeSpecificPart());
/*     */         
/*  56 */         if (is != null) {
/*  57 */           return is;
/*     */         }
/*     */       } 
/*     */     } else {
/*     */       
/*  62 */       return fileUri.toURL().openStream();
/*     */     } 
/*  64 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static List<ClassLoader> getClassLoaders() {
/*  73 */     ClassLoader[] classLoaders = new ClassLoader[2];
/*  74 */     classLoaders[0] = FileUtils.class.getClassLoader();
/*  75 */     classLoaders[1] = Thread.currentThread().getContextClassLoader();
/*     */ 
/*     */     
/*  78 */     List<ClassLoader> loaders = new ArrayList<>(classLoaders.length);
/*  79 */     for (ClassLoader classLoader : classLoaders) {
/*  80 */       if (classLoader != null) {
/*  81 */         loaders.add(classLoader);
/*     */       }
/*     */     } 
/*  84 */     return loaders;
/*     */   }
/*     */   
/*     */   public static boolean addLines(String url, Set<String> set) throws MalformedURLException, IOException {
/*  88 */     InputStream is = getStreamForUrl(url, null);
/*  89 */     if (is == null) return false; 
/*  90 */     BufferedReader br = new BufferedReader(new InputStreamReader(is));
/*     */     String line;
/*  92 */     while ((line = br.readLine()) != null) {
/*  93 */       set.add(line);
/*     */     }
/*  95 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String readFileOrThrow(File file) throws FileNotFoundException, IOException {
/* 106 */     Reader reader = null;
/*     */     try {
/* 108 */       reader = new FileReader(file);
/* 109 */       char[] buf = new char[8192];
/*     */       
/* 111 */       StringBuilder s = new StringBuilder(); int len;
/* 112 */       while ((len = reader.read(buf)) >= 0) {
/* 113 */         s.append(buf, 0, len);
/*     */       }
/* 115 */       return s.toString();
/*     */     } finally {
/*     */       
/* 118 */       if (reader != null) {
/* 119 */         reader.close();
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public static String readFile(File file) {
/*     */     try {
/* 126 */       return readFileOrThrow(file);
/* 127 */     } catch (FileNotFoundException e) {
/* 128 */       LOGGER.log(Level.FINE, "readFile", e);
/* 129 */     } catch (IOException e) {
/* 130 */       LOGGER.log(Level.WARNING, "readFile", e);
/*     */     } 
/* 132 */     return null;
/*     */   }
/*     */   
/*     */   public static void writeFileOrThrow(File file, String content) throws IOException {
/* 136 */     FileWriter writer = new FileWriter(file, false);
/* 137 */     writer.write(content);
/* 138 */     writer.close();
/*     */   }
/*     */   
/*     */   public static boolean writeFile(File file, String content) {
/*     */     try {
/* 143 */       writeFileOrThrow(file, content);
/* 144 */       return true;
/*     */     }
/* 146 */     catch (IOException e) {
/* 147 */       LOGGER.log(Level.WARNING, "writeFile", e);
/* 148 */       return false;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jivesoftware\smac\\util\FileUtils.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */