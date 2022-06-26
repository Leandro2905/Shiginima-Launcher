/*     */ package org.apache.logging.log4j.util;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.URL;
/*     */ import java.util.Collection;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Properties;
/*     */ import java.util.concurrent.CopyOnWriteArraySet;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.spi.Provider;
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
/*     */ 
/*     */ public final class ProviderUtil
/*     */ {
/*     */   protected static final String PROVIDER_RESOURCE = "META-INF/log4j-provider.properties";
/*     */   private static final String API_VERSION = "Log4jAPIVersion";
/*  41 */   private static final String[] COMPATIBLE_API_VERSIONS = new String[] { "2.0.0" };
/*     */ 
/*     */ 
/*     */   
/*  45 */   private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*     */   
/*  47 */   private static final Collection<Provider> PROVIDERS = new CopyOnWriteArraySet<Provider>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static {
/*  53 */     ClassLoader cl = findClassLoader();
/*  54 */     Enumeration<URL> enumResources = null;
/*     */     try {
/*  56 */       enumResources = cl.getResources("META-INF/log4j-provider.properties");
/*  57 */     } catch (IOException e) {
/*  58 */       LOGGER.fatal("Unable to locate {}", new Object[] { "META-INF/log4j-provider.properties", e });
/*     */     } 
/*  60 */     loadProviders(enumResources, cl);
/*     */   }
/*     */   
/*     */   protected static void loadProviders(Enumeration<URL> enumResources, ClassLoader cl) {
/*  64 */     if (enumResources != null) {
/*  65 */       while (enumResources.hasMoreElements()) {
/*  66 */         URL url = enumResources.nextElement();
/*  67 */         loadProvider(url, cl);
/*     */       } 
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
/*     */   protected static void loadProvider(URL url, ClassLoader cl) {
/*     */     try {
/*  81 */       Properties props = PropertiesUtil.loadClose(url.openStream(), url);
/*  82 */       if (validVersion(props.getProperty("Log4jAPIVersion"))) {
/*  83 */         PROVIDERS.add(new Provider(props, url, cl));
/*     */       }
/*  85 */     } catch (IOException e) {
/*  86 */       LOGGER.error("Unable to open {}", new Object[] { url, e });
/*     */     } 
/*     */   }
/*     */   
/*     */   public static Iterable<Provider> getProviders() {
/*  91 */     return PROVIDERS;
/*     */   }
/*     */   
/*     */   public static boolean hasProviders() {
/*  95 */     return !PROVIDERS.isEmpty();
/*     */   }
/*     */   
/*     */   public static ClassLoader findClassLoader() {
/*  99 */     return LoaderUtil.getThreadContextClassLoader();
/*     */   }
/*     */   
/*     */   private static boolean validVersion(String version) {
/* 103 */     for (String v : COMPATIBLE_API_VERSIONS) {
/* 104 */       if (version.startsWith(v)) {
/* 105 */         return true;
/*     */       }
/*     */     } 
/* 108 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4\\util\ProviderUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */