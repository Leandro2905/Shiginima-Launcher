/*     */ package com.mojang.authlib;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.net.HttpURLConnection;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.Proxy;
/*     */ import java.net.URL;
/*     */ import java.net.URLEncoder;
/*     */ import java.util.Map;
/*     */ import org.apache.commons.io.Charsets;
/*     */ import org.apache.commons.io.IOUtils;
/*     */ import org.apache.commons.lang3.Validate;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ 
/*     */ public abstract class HttpAuthenticationService
/*     */   extends BaseAuthenticationService
/*     */ {
/*  23 */   private static final Logger LOGGER = LogManager.getLogger();
/*     */   
/*     */   private final Proxy proxy;
/*     */   
/*     */   protected HttpAuthenticationService(Proxy proxy) {
/*  28 */     Validate.notNull(proxy);
/*  29 */     this.proxy = proxy;
/*     */   }
/*     */ 
/*     */   
/*     */   public Proxy getProxy() {
/*  34 */     return this.proxy;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected HttpURLConnection createUrlConnection(URL url) throws IOException {
/*  40 */     Validate.notNull(url);
/*  41 */     LOGGER.debug("Opening connection to " + url);
/*  42 */     HttpURLConnection connection = (HttpURLConnection)url.openConnection(this.proxy);
/*  43 */     connection.setConnectTimeout(15000);
/*  44 */     connection.setReadTimeout(15000);
/*  45 */     connection.setUseCaches(false);
/*  46 */     return connection;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String performPostRequest(URL url, String post, String contentType) throws IOException {
/*  52 */     Validate.notNull(url);
/*  53 */     Validate.notNull(post);
/*  54 */     Validate.notNull(contentType);
/*  55 */     HttpURLConnection connection = createUrlConnection(url);
/*  56 */     byte[] postAsBytes = post.getBytes(Charsets.UTF_8);
/*     */     
/*  58 */     connection.setRequestProperty("Content-Type", contentType + "; charset=utf-8");
/*  59 */     connection.setRequestProperty("Content-Length", "" + postAsBytes.length);
/*  60 */     connection.setDoOutput(true);
/*     */     
/*  62 */     LOGGER.debug("Writing POST data to " + url + ": " + post);
/*     */     
/*  64 */     OutputStream outputStream = null;
/*     */     
/*     */     try {
/*  67 */       outputStream = connection.getOutputStream();
/*  68 */       IOUtils.write(postAsBytes, outputStream);
/*     */     }
/*     */     finally {
/*     */       
/*  72 */       IOUtils.closeQuietly(outputStream);
/*     */     } 
/*  74 */     LOGGER.debug("Reading data from " + url);
/*     */     
/*  76 */     InputStream inputStream = null;
/*     */     
/*     */     try {
/*  79 */       inputStream = connection.getInputStream();
/*  80 */       String result = IOUtils.toString(inputStream, Charsets.UTF_8);
/*  81 */       LOGGER.debug("Successful read, server response was " + connection.getResponseCode());
/*  82 */       LOGGER.debug("Response: " + result);
/*  83 */       return result;
/*     */     }
/*  85 */     catch (IOException e) {
/*     */       
/*  87 */       IOUtils.closeQuietly(inputStream);
/*  88 */       inputStream = connection.getErrorStream();
/*  89 */       if (inputStream != null) {
/*     */         
/*  91 */         LOGGER.debug("Reading error page from " + url);
/*  92 */         String result = IOUtils.toString(inputStream, Charsets.UTF_8);
/*  93 */         LOGGER.debug("Successful read, server response was " + connection.getResponseCode());
/*  94 */         LOGGER.debug("Response: " + result);
/*  95 */         return result;
/*     */       } 
/*  97 */       LOGGER.debug("Request failed", e);
/*  98 */       throw e;
/*     */     }
/*     */     finally {
/*     */       
/* 102 */       IOUtils.closeQuietly(inputStream);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String performGetRequest(URL url) throws IOException {
/* 109 */     Validate.notNull(url);
/* 110 */     HttpURLConnection connection = createUrlConnection(url);
/*     */     
/* 112 */     LOGGER.debug("Reading data from " + url);
/*     */     
/* 114 */     InputStream inputStream = null;
/*     */     
/*     */     try {
/* 117 */       inputStream = connection.getInputStream();
/* 118 */       String result = IOUtils.toString(inputStream, Charsets.UTF_8);
/* 119 */       LOGGER.debug("Successful read, server response was " + connection.getResponseCode());
/* 120 */       LOGGER.debug("Response: " + result);
/* 121 */       return result;
/*     */     }
/* 123 */     catch (IOException e) {
/*     */       
/* 125 */       IOUtils.closeQuietly(inputStream);
/* 126 */       inputStream = connection.getErrorStream();
/* 127 */       if (inputStream != null) {
/*     */         
/* 129 */         LOGGER.debug("Reading error page from " + url);
/* 130 */         String result = IOUtils.toString(inputStream, Charsets.UTF_8);
/* 131 */         LOGGER.debug("Successful read, server response was " + connection.getResponseCode());
/* 132 */         LOGGER.debug("Response: " + result);
/* 133 */         return result;
/*     */       } 
/* 135 */       LOGGER.debug("Request failed", e);
/* 136 */       throw e;
/*     */     }
/*     */     finally {
/*     */       
/* 140 */       IOUtils.closeQuietly(inputStream);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static URL constantURL(String url) {
/*     */     try {
/* 148 */       return new URL(url);
/*     */     }
/* 150 */     catch (MalformedURLException ex) {
/*     */       
/* 152 */       throw new Error("Couldn't create constant for " + url, ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static String buildQuery(Map<String, Object> query) {
/* 158 */     if (query == null) {
/* 159 */       return "";
/*     */     }
/* 161 */     StringBuilder builder = new StringBuilder();
/* 162 */     for (Map.Entry<String, Object> entry : query.entrySet()) {
/*     */       
/* 164 */       if (builder.length() > 0) {
/* 165 */         builder.append('&');
/*     */       }
/*     */       
/*     */       try {
/* 169 */         builder.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
/*     */       }
/* 171 */       catch (UnsupportedEncodingException e) {
/*     */         
/* 173 */         LOGGER.error("Unexpected exception building query", e);
/*     */       } 
/* 175 */       if (entry.getValue() != null) {
/*     */         
/* 177 */         builder.append('=');
/*     */         
/*     */         try {
/* 180 */           builder.append(URLEncoder.encode(entry.getValue().toString(), "UTF-8"));
/*     */         }
/* 182 */         catch (UnsupportedEncodingException e) {
/*     */           
/* 184 */           LOGGER.error("Unexpected exception building query", e);
/*     */         } 
/*     */       } 
/*     */     } 
/* 188 */     return builder.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static URL concatenateURL(URL url, String query) {
/*     */     try {
/* 195 */       if (url.getQuery() != null && url.getQuery().length() > 0) {
/* 196 */         return new URL(url.getProtocol(), url.getHost(), url.getPort(), url.getFile() + "&" + query);
/*     */       }
/* 198 */       return new URL(url.getProtocol(), url.getHost(), url.getPort(), url.getFile() + "?" + query);
/*     */     }
/* 200 */     catch (MalformedURLException ex) {
/*     */       
/* 202 */       throw new IllegalArgumentException("Could not concatenate given URL with GET arguments!", ex);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\mojang\authlib\HttpAuthenticationService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */