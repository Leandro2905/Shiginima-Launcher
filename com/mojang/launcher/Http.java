/*    */ package com.mojang.launcher;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.io.UnsupportedEncodingException;
/*    */ import java.net.HttpURLConnection;
/*    */ import java.net.Proxy;
/*    */ import java.net.URL;
/*    */ import java.net.URLEncoder;
/*    */ import java.util.Map;
/*    */ import org.apache.commons.io.IOUtils;
/*    */ import org.apache.logging.log4j.LogManager;
/*    */ import org.apache.logging.log4j.Logger;
/*    */ 
/*    */ 
/*    */ public class Http
/*    */ {
/* 18 */   private static final Logger LOGGER = LogManager.getLogger();
/*    */ 
/*    */   
/*    */   public static String buildQuery(Map<String, Object> query) {
/* 22 */     StringBuilder builder = new StringBuilder();
/* 23 */     for (Map.Entry<String, Object> entry : query.entrySet()) {
/*    */       
/* 25 */       if (builder.length() > 0) {
/* 26 */         builder.append('&');
/*    */       }
/*    */       
/*    */       try {
/* 30 */         builder.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
/*    */       }
/* 32 */       catch (UnsupportedEncodingException e) {
/*    */         
/* 34 */         LOGGER.error("Unexpected exception building query", e);
/*    */       } 
/* 36 */       if (entry.getValue() != null) {
/*    */         
/* 38 */         builder.append('=');
/*    */         
/*    */         try {
/* 41 */           builder.append(URLEncoder.encode(entry.getValue().toString(), "UTF-8"));
/*    */         }
/* 43 */         catch (UnsupportedEncodingException e) {
/*    */           
/* 45 */           LOGGER.error("Unexpected exception building query", e);
/*    */         } 
/*    */       } 
/*    */     } 
/* 49 */     return builder.toString();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static String performGet(URL url, Proxy proxy) throws IOException {
/* 55 */     HttpURLConnection connection = (HttpURLConnection)url.openConnection(proxy);
/* 56 */     connection.setConnectTimeout(15000);
/* 57 */     connection.setReadTimeout(60000);
/* 58 */     connection.setRequestMethod("GET");
/*    */     
/* 60 */     InputStream inputStream = connection.getInputStream();
/*    */     
/*    */     try {
/* 63 */       return IOUtils.toString(inputStream);
/*    */     }
/*    */     finally {
/*    */       
/* 67 */       IOUtils.closeQuietly(inputStream);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\mojang\launcher\Http.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */