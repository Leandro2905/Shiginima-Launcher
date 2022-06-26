/*    */ package org.jivesoftware.smack.initializer;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.util.LinkedList;
/*    */ import java.util.List;
/*    */ import java.util.logging.Level;
/*    */ import java.util.logging.Logger;
/*    */ import org.jivesoftware.smack.SmackInitialization;
/*    */ import org.jivesoftware.smack.provider.ProviderFileLoader;
/*    */ import org.jivesoftware.smack.provider.ProviderLoader;
/*    */ import org.jivesoftware.smack.provider.ProviderManager;
/*    */ import org.jivesoftware.smack.util.FileUtils;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class UrlInitializer
/*    */   implements SmackInitializer
/*    */ {
/* 38 */   private static final Logger LOGGER = Logger.getLogger(UrlInitializer.class.getName());
/*    */ 
/*    */ 
/*    */   
/*    */   public List<Exception> initialize() {
/* 43 */     ClassLoader classLoader = getClass().getClassLoader();
/* 44 */     List<Exception> exceptions = new LinkedList<>();
/* 45 */     String providerUrl = getProvidersUrl();
/* 46 */     if (providerUrl != null) {
/*    */       try {
/* 48 */         InputStream is = FileUtils.getStreamForUrl(providerUrl, classLoader);
/*    */         
/* 50 */         if (is != null) {
/* 51 */           LOGGER.log(Level.FINE, "Loading providers for providerUrl [" + providerUrl + "]");
/*    */           
/* 53 */           ProviderFileLoader pfl = new ProviderFileLoader(is, classLoader);
/* 54 */           ProviderManager.addLoader((ProviderLoader)pfl);
/* 55 */           exceptions.addAll(pfl.getLoadingExceptions());
/*    */         } else {
/*    */           
/* 58 */           LOGGER.log(Level.WARNING, "No input stream created for " + providerUrl);
/* 59 */           exceptions.add(new IOException("No input stream created for " + providerUrl));
/*    */         }
/*    */       
/* 62 */       } catch (Exception e) {
/* 63 */         LOGGER.log(Level.SEVERE, "Error trying to load provider file " + providerUrl, e);
/* 64 */         exceptions.add(e);
/*    */       } 
/*    */     }
/* 67 */     String configUrl = getConfigUrl();
/* 68 */     if (configUrl != null) {
/*    */       try {
/* 70 */         InputStream inputStream = FileUtils.getStreamForUrl(configUrl, classLoader);
/* 71 */         SmackInitialization.processConfigFile(inputStream, exceptions, classLoader);
/*    */       }
/* 73 */       catch (Exception e) {
/* 74 */         exceptions.add(e);
/*    */       } 
/*    */     }
/* 77 */     return exceptions;
/*    */   }
/*    */   
/*    */   protected String getProvidersUrl() {
/* 81 */     return null;
/*    */   }
/*    */   
/*    */   protected String getConfigUrl() {
/* 85 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jivesoftware\smack\initializer\UrlInitializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */