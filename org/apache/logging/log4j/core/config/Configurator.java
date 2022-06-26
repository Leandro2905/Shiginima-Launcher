/*     */ package org.apache.logging.log4j.core.config;
/*     */ 
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.core.LoggerContext;
/*     */ import org.apache.logging.log4j.core.impl.Log4jContextFactory;
/*     */ import org.apache.logging.log4j.core.util.FileUtils;
/*     */ import org.apache.logging.log4j.spi.LoggerContextFactory;
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
/*     */ public final class Configurator
/*     */ {
/*  36 */   private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*     */   
/*  38 */   private static final String FQCN = Configurator.class.getName();
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
/*     */   public static LoggerContext initialize(String name, ClassLoader loader, String configLocation) {
/*  52 */     return initialize(name, loader, configLocation, (Object)null);
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
/*     */   public static LoggerContext initialize(String name, ClassLoader loader, String configLocation, Object externalContext) {
/*     */     try {
/*  68 */       URI uri = (configLocation == null) ? null : FileUtils.getCorrectedFilePathUri(configLocation);
/*  69 */       return initialize(name, loader, uri, externalContext);
/*  70 */     } catch (URISyntaxException ex) {
/*  71 */       LOGGER.error("There was a problem parsing the configuration location [{}].", new Object[] { configLocation, ex });
/*     */       
/*  73 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static LoggerContext initialize(String name, String configLocation) {
/*  83 */     return initialize(name, (ClassLoader)null, configLocation);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static LoggerContext initialize(String name, ClassLoader loader, URI configLocation) {
/*  94 */     return initialize(name, loader, configLocation, (Object)null);
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
/*     */   public static LoggerContext initialize(String name, ClassLoader loader, URI configLocation, Object externalContext) {
/*     */     try {
/* 109 */       Log4jContextFactory factory = getFactory();
/* 110 */       return (factory == null) ? null : factory.getContext(FQCN, loader, externalContext, false, configLocation, name);
/*     */     }
/* 112 */     catch (Exception ex) {
/* 113 */       LOGGER.error("There was a problem initializing the LoggerContext [{}] using configuration at [{}].", new Object[] { name, configLocation, ex });
/*     */ 
/*     */       
/* 116 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static LoggerContext initialize(ClassLoader loader, ConfigurationSource source) {
/* 127 */     return initialize(loader, source, (Object)null);
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
/*     */   public static LoggerContext initialize(ClassLoader loader, ConfigurationSource source, Object externalContext) {
/*     */     try {
/* 144 */       Log4jContextFactory factory = getFactory();
/* 145 */       return (factory == null) ? null : factory.getContext(FQCN, loader, externalContext, false, source);
/*     */     }
/* 147 */     catch (Exception ex) {
/* 148 */       LOGGER.error("There was a problem obtaining a LoggerContext using the configuration source [{}]", new Object[] { source, ex });
/*     */       
/* 150 */       return null;
/*     */     } 
/*     */   }
/*     */   private static Log4jContextFactory getFactory() {
/* 154 */     LoggerContextFactory factory = LogManager.getFactory();
/* 155 */     if (factory instanceof Log4jContextFactory)
/* 156 */       return (Log4jContextFactory)factory; 
/* 157 */     if (factory != null) {
/* 158 */       LOGGER.error("LogManager returned an instance of {} which does not implement {}. Unable to initialize Log4j.", new Object[] { factory.getClass().getName(), Log4jContextFactory.class.getName() });
/*     */       
/* 160 */       return null;
/*     */     } 
/* 162 */     LOGGER.error("LogManager did not return a LoggerContextFactory. This indicates something has gone terribly wrong!");
/* 163 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void shutdown(LoggerContext ctx) {
/* 172 */     if (ctx != null)
/* 173 */       ctx.stop(); 
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\config\Configurator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */