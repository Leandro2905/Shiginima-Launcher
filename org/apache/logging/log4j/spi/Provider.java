/*     */ package org.apache.logging.log4j.spi;
/*     */ 
/*     */ import java.net.URL;
/*     */ import java.util.Properties;
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
/*     */ public class Provider
/*     */ {
/*  31 */   private static final Integer DEFAULT_PRIORITY = Integer.valueOf(-1);
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String FACTORY_PRIORITY = "FactoryPriority";
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String THREAD_CONTEXT_MAP = "ThreadContextMap";
/*     */ 
/*     */   
/*     */   public static final String LOGGER_CONTEXT_FACTORY = "LoggerContextFactory";
/*     */ 
/*     */   
/*  45 */   private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*     */   
/*     */   private final Integer priority;
/*     */   private final String className;
/*     */   private final String threadContextMap;
/*     */   private final URL url;
/*     */   private final ClassLoader classLoader;
/*     */   
/*     */   public Provider(Properties props, URL url, ClassLoader classLoader) {
/*  54 */     this.url = url;
/*  55 */     this.classLoader = classLoader;
/*  56 */     String weight = props.getProperty("FactoryPriority");
/*  57 */     this.priority = (weight == null) ? DEFAULT_PRIORITY : Integer.valueOf(weight);
/*  58 */     this.className = props.getProperty("LoggerContextFactory");
/*  59 */     this.threadContextMap = props.getProperty("ThreadContextMap");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Integer getPriority() {
/*  68 */     return this.priority;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getClassName() {
/*  78 */     return this.className;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Class<? extends LoggerContextFactory> loadLoggerContextFactory() {
/*  88 */     if (this.className == null) {
/*  89 */       return null;
/*     */     }
/*     */     try {
/*  92 */       Class<?> clazz = this.classLoader.loadClass(this.className);
/*  93 */       if (LoggerContextFactory.class.isAssignableFrom(clazz)) {
/*  94 */         return (Class)clazz;
/*     */       }
/*  96 */     } catch (Exception e) {
/*  97 */       LOGGER.error("Unable to create class {} specified in {}", new Object[] { this.className, this.url.toString(), e });
/*     */     } 
/*  99 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getThreadContextMap() {
/* 109 */     return this.threadContextMap;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Class<? extends ThreadContextMap> loadThreadContextMap() {
/* 119 */     if (this.threadContextMap == null) {
/* 120 */       return null;
/*     */     }
/*     */     try {
/* 123 */       Class<?> clazz = this.classLoader.loadClass(this.threadContextMap);
/* 124 */       if (ThreadContextMap.class.isAssignableFrom(clazz)) {
/* 125 */         return (Class)clazz;
/*     */       }
/* 127 */     } catch (Exception e) {
/* 128 */       LOGGER.error("Unable to create class {} specified in {}", new Object[] { this.threadContextMap, this.url.toString(), e });
/*     */     } 
/* 130 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URL getUrl() {
/* 139 */     return this.url;
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\spi\Provider.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */