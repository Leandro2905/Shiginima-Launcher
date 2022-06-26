/*     */ package org.apache.logging.log4j.core.appender.rewrite;
/*     */ 
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import org.apache.logging.log4j.core.Appender;
/*     */ import org.apache.logging.log4j.core.Filter;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.appender.AbstractAppender;
/*     */ import org.apache.logging.log4j.core.config.AppenderControl;
/*     */ import org.apache.logging.log4j.core.config.AppenderRef;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginElement;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginFactory;
/*     */ import org.apache.logging.log4j.core.util.Booleans;
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
/*     */ @Plugin(name = "Rewrite", category = "Core", elementType = "appender", printObject = true)
/*     */ public final class RewriteAppender
/*     */   extends AbstractAppender
/*     */ {
/*     */   private final Configuration config;
/*  43 */   private final ConcurrentMap<String, AppenderControl> appenders = new ConcurrentHashMap<String, AppenderControl>();
/*     */   
/*     */   private final RewritePolicy rewritePolicy;
/*     */   
/*     */   private final AppenderRef[] appenderRefs;
/*     */   
/*     */   private RewriteAppender(String name, Filter filter, boolean ignoreExceptions, AppenderRef[] appenderRefs, RewritePolicy rewritePolicy, Configuration config) {
/*  50 */     super(name, filter, null, ignoreExceptions);
/*  51 */     this.config = config;
/*  52 */     this.rewritePolicy = rewritePolicy;
/*  53 */     this.appenderRefs = appenderRefs;
/*     */   }
/*     */ 
/*     */   
/*     */   public void start() {
/*  58 */     for (AppenderRef ref : this.appenderRefs) {
/*  59 */       String name = ref.getRef();
/*  60 */       Appender appender = this.config.getAppender(name);
/*  61 */       if (appender != null) {
/*  62 */         Filter filter = (appender instanceof AbstractAppender) ? ((AbstractAppender)appender).getFilter() : null;
/*     */         
/*  64 */         this.appenders.put(name, new AppenderControl(appender, ref.getLevel(), filter));
/*     */       } else {
/*  66 */         LOGGER.error("Appender " + ref + " cannot be located. Reference ignored");
/*     */       } 
/*     */     } 
/*  69 */     super.start();
/*     */   }
/*     */ 
/*     */   
/*     */   public void stop() {
/*  74 */     super.stop();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void append(LogEvent event) {
/*  83 */     if (this.rewritePolicy != null) {
/*  84 */       event = this.rewritePolicy.rewrite(event);
/*     */     }
/*  86 */     for (AppenderControl control : this.appenders.values()) {
/*  87 */       control.callAppender(event);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @PluginFactory
/*     */   public static RewriteAppender createAppender(@PluginAttribute("name") String name, @PluginAttribute("ignoreExceptions") String ignore, @PluginElement("AppenderRef") AppenderRef[] appenderRefs, @PluginConfiguration Configuration config, @PluginElement("RewritePolicy") RewritePolicy rewritePolicy, @PluginElement("Filter") Filter filter) {
/* 111 */     boolean ignoreExceptions = Booleans.parseBoolean(ignore, true);
/* 112 */     if (name == null) {
/* 113 */       LOGGER.error("No name provided for RewriteAppender");
/* 114 */       return null;
/*     */     } 
/* 116 */     if (appenderRefs == null) {
/* 117 */       LOGGER.error("No appender references defined for RewriteAppender");
/* 118 */       return null;
/*     */     } 
/* 120 */     return new RewriteAppender(name, filter, ignoreExceptions, appenderRefs, rewritePolicy, config);
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\appender\rewrite\RewriteAppender.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */