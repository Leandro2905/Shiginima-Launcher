/*     */ package org.apache.logging.log4j.core.impl;
/*     */ 
/*     */ import java.net.URI;
/*     */ import org.apache.logging.log4j.core.LifeCycle;
/*     */ import org.apache.logging.log4j.core.LoggerContext;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.config.ConfigurationFactory;
/*     */ import org.apache.logging.log4j.core.config.ConfigurationSource;
/*     */ import org.apache.logging.log4j.core.selector.ClassLoaderContextSelector;
/*     */ import org.apache.logging.log4j.core.selector.ContextSelector;
/*     */ import org.apache.logging.log4j.core.util.Loader;
/*     */ import org.apache.logging.log4j.spi.LoggerContext;
/*     */ import org.apache.logging.log4j.spi.LoggerContextFactory;
/*     */ import org.apache.logging.log4j.status.StatusLogger;
/*     */ import org.apache.logging.log4j.util.PropertiesUtil;
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
/*     */ public class Log4jContextFactory
/*     */   implements LoggerContextFactory
/*     */ {
/*  39 */   private static final StatusLogger LOGGER = StatusLogger.getLogger();
/*     */ 
/*     */   
/*     */   private ContextSelector selector;
/*     */ 
/*     */ 
/*     */   
/*     */   public Log4jContextFactory() {
/*  47 */     String sel = PropertiesUtil.getProperties().getStringProperty("Log4jContextSelector");
/*  48 */     if (sel != null) {
/*     */       try {
/*  50 */         this.selector = (ContextSelector)Loader.newCheckedInstanceOf(sel, ContextSelector.class);
/*  51 */       } catch (Exception ex) {
/*  52 */         LOGGER.error("Unable to create context {}", new Object[] { sel, ex });
/*     */       } 
/*     */     }
/*  55 */     if (this.selector == null) {
/*  56 */       this.selector = (ContextSelector)new ClassLoaderContextSelector();
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
/*     */ 
/*     */ 
/*     */   
/*     */   public LoggerContext getContext(String fqcn, ClassLoader loader, Object externalContext, boolean currentContext) {
/*  72 */     LoggerContext ctx = this.selector.getContext(fqcn, loader, currentContext);
/*  73 */     ctx.setExternalContext(externalContext);
/*  74 */     if (ctx.getState() == LifeCycle.State.INITIALIZED) {
/*  75 */       ctx.start();
/*     */     }
/*  77 */     return ctx;
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
/*     */   public LoggerContext getContext(String fqcn, ClassLoader loader, Object externalContext, boolean currentContext, ConfigurationSource source) {
/*  92 */     LoggerContext ctx = this.selector.getContext(fqcn, loader, currentContext, null);
/*  93 */     if (externalContext != null && ctx.getExternalContext() == null) {
/*  94 */       ctx.setExternalContext(externalContext);
/*     */     }
/*  96 */     if (ctx.getState() == LifeCycle.State.INITIALIZED) {
/*  97 */       if (source != null) {
/*  98 */         ContextAnchor.THREAD_CONTEXT.set(ctx);
/*  99 */         Configuration config = ConfigurationFactory.getInstance().getConfiguration(source);
/* 100 */         LOGGER.debug("Starting LoggerContext[name={}] from configuration {}", new Object[] { ctx.getName(), source });
/* 101 */         ctx.start(config);
/* 102 */         ContextAnchor.THREAD_CONTEXT.remove();
/*     */       } else {
/* 104 */         ctx.start();
/*     */       } 
/*     */     }
/* 107 */     return ctx;
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
/*     */   public LoggerContext getContext(String fqcn, ClassLoader loader, Object externalContext, boolean currentContext, URI configLocation, String name) {
/* 123 */     LoggerContext ctx = this.selector.getContext(fqcn, loader, currentContext, configLocation);
/* 124 */     if (externalContext != null && ctx.getExternalContext() == null) {
/* 125 */       ctx.setExternalContext(externalContext);
/*     */     }
/* 127 */     if (ctx.getState() == LifeCycle.State.INITIALIZED) {
/* 128 */       if (configLocation != null || name != null) {
/* 129 */         ContextAnchor.THREAD_CONTEXT.set(ctx);
/* 130 */         Configuration config = ConfigurationFactory.getInstance().getConfiguration(name, configLocation);
/* 131 */         LOGGER.debug("Starting LoggerContext[name={}] from configuration at {}", new Object[] { ctx.getName(), configLocation });
/* 132 */         ctx.start(config);
/* 133 */         ContextAnchor.THREAD_CONTEXT.remove();
/*     */       } else {
/* 135 */         ctx.start();
/*     */       } 
/*     */     }
/* 138 */     return ctx;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ContextSelector getSelector() {
/* 146 */     return this.selector;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeContext(LoggerContext context) {
/* 156 */     if (context instanceof LoggerContext)
/* 157 */       this.selector.removeContext((LoggerContext)context); 
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\impl\Log4jContextFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */