/*     */ package com.google.common.io;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.base.MoreObjects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.Lists;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.net.URL;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.List;
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
/*     */ @Beta
/*     */ public final class Resources
/*     */ {
/*     */   public static ByteSource asByteSource(URL url) {
/*  56 */     return new UrlByteSource(url);
/*     */   }
/*     */ 
/*     */   
/*     */   private static final class UrlByteSource
/*     */     extends ByteSource
/*     */   {
/*     */     private final URL url;
/*     */ 
/*     */     
/*     */     private UrlByteSource(URL url) {
/*  67 */       this.url = (URL)Preconditions.checkNotNull(url);
/*     */     }
/*     */ 
/*     */     
/*     */     public InputStream openStream() throws IOException {
/*  72 */       return this.url.openStream();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/*  77 */       String str = String.valueOf(String.valueOf(this.url)); return (new StringBuilder(24 + str.length())).append("Resources.asByteSource(").append(str).append(")").toString();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static CharSource asCharSource(URL url, Charset charset) {
/*  88 */     return asByteSource(url).asCharSource(charset);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] toByteArray(URL url) throws IOException {
/*  99 */     return asByteSource(url).read();
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
/*     */ 
/*     */   
/*     */   public static String toString(URL url, Charset charset) throws IOException {
/* 113 */     return asCharSource(url, charset).read();
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> T readLines(URL url, Charset charset, LineProcessor<T> callback) throws IOException {
/* 129 */     return asCharSource(url, charset).readLines(callback);
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
/*     */   public static List<String> readLines(URL url, Charset charset) throws IOException {
/* 151 */     return readLines(url, charset, new LineProcessor<List<String>>() {
/* 152 */           final List<String> result = Lists.newArrayList();
/*     */ 
/*     */           
/*     */           public boolean processLine(String line) {
/* 156 */             this.result.add(line);
/* 157 */             return true;
/*     */           }
/*     */ 
/*     */           
/*     */           public List<String> getResult() {
/* 162 */             return this.result;
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void copy(URL from, OutputStream to) throws IOException {
/* 175 */     asByteSource(from).copyTo(to);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static URL getResource(String resourceName) {
/* 193 */     ClassLoader loader = (ClassLoader)MoreObjects.firstNonNull(Thread.currentThread().getContextClassLoader(), Resources.class.getClassLoader());
/*     */ 
/*     */     
/* 196 */     URL url = loader.getResource(resourceName);
/* 197 */     Preconditions.checkArgument((url != null), "resource %s not found.", new Object[] { resourceName });
/* 198 */     return url;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static URL getResource(Class<?> contextClass, String resourceName) {
/* 208 */     URL url = contextClass.getResource(resourceName);
/* 209 */     Preconditions.checkArgument((url != null), "resource %s relative to %s not found.", new Object[] { resourceName, contextClass.getName() });
/*     */     
/* 211 */     return url;
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\io\Resources.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */