/*     */ package org.apache.logging.log4j.core.async;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import org.apache.logging.log4j.Level;
/*     */ import org.apache.logging.log4j.core.Filter;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.config.AppenderRef;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.config.LoggerConfig;
/*     */ import org.apache.logging.log4j.core.config.Property;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginElement;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginFactory;
/*     */ import org.apache.logging.log4j.core.jmx.RingBufferAdmin;
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
/*     */ 
/*     */ @Plugin(name = "asyncLogger", category = "Core", printObject = true)
/*     */ public class AsyncLoggerConfig
/*     */   extends LoggerConfig
/*     */ {
/*     */   private AsyncLoggerConfigHelper helper;
/*     */   
/*     */   public AsyncLoggerConfig() {}
/*     */   
/*     */   public AsyncLoggerConfig(String name, Level level, boolean additive) {
/*  88 */     super(name, level, additive);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected AsyncLoggerConfig(String name, List<AppenderRef> appenders, Filter filter, Level level, boolean additive, Property[] properties, Configuration config, boolean includeLocation) {
/*  96 */     super(name, appenders, filter, level, additive, properties, config, includeLocation);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void callAppenders(LogEvent event) {
/* 107 */     event.getSource();
/* 108 */     event.getThreadName();
/*     */ 
/*     */     
/* 111 */     if (!this.helper.callAppendersFromAnotherThread(event)) {
/* 112 */       super.callAppenders(event);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   void asyncCallAppenders(LogEvent event) {
/* 118 */     super.callAppenders(event);
/*     */   }
/*     */ 
/*     */   
/*     */   public void start() {
/* 123 */     LOGGER.trace("AsyncLoggerConfig[{}] starting...", new Object[] { getName() });
/* 124 */     setStarting();
/* 125 */     if (this.helper == null) {
/* 126 */       this.helper = new AsyncLoggerConfigHelper(this);
/*     */     } else {
/* 128 */       AsyncLoggerConfigHelper.claim();
/*     */     } 
/* 130 */     super.start();
/*     */   }
/*     */ 
/*     */   
/*     */   public void stop() {
/* 135 */     LOGGER.trace("AsyncLoggerConfig[{}] stopping...", new Object[] { getName() });
/* 136 */     setStopping();
/* 137 */     AsyncLoggerConfigHelper.release();
/* 138 */     super.stop();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RingBufferAdmin createRingBufferAdmin(String contextName) {
/* 148 */     return this.helper.createRingBufferAdmin(contextName, getName());
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
/*     */ 
/*     */   
/*     */   @PluginFactory
/*     */   public static LoggerConfig createLogger(@PluginAttribute("additivity") String additivity, @PluginAttribute("level") String levelName, @PluginAttribute("name") String loggerName, @PluginAttribute("includeLocation") String includeLocation, @PluginElement("AppenderRef") AppenderRef[] refs, @PluginElement("Properties") Property[] properties, @PluginConfiguration Configuration config, @PluginElement("Filter") Filter filter) {
/*     */     Level level;
/* 174 */     if (loggerName == null) {
/* 175 */       LOGGER.error("Loggers cannot be configured without a name");
/* 176 */       return null;
/*     */     } 
/*     */     
/* 179 */     List<AppenderRef> appenderRefs = Arrays.asList(refs);
/*     */     
/*     */     try {
/* 182 */       level = Level.toLevel(levelName, Level.ERROR);
/* 183 */     } catch (Exception ex) {
/* 184 */       LOGGER.error("Invalid Log level specified: {}. Defaulting to Error", new Object[] { levelName });
/*     */ 
/*     */       
/* 187 */       level = Level.ERROR;
/*     */     } 
/* 189 */     String name = loggerName.equals("root") ? "" : loggerName;
/* 190 */     boolean additive = Booleans.parseBoolean(additivity, true);
/*     */     
/* 192 */     return new AsyncLoggerConfig(name, appenderRefs, filter, level, additive, properties, config, includeLocation(includeLocation));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected static boolean includeLocation(String includeLocationConfigValue) {
/* 198 */     return Boolean.parseBoolean(includeLocationConfigValue);
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
/*     */   @Plugin(name = "asyncRoot", category = "Core", printObject = true)
/*     */   public static class RootLogger
/*     */     extends LoggerConfig
/*     */   {
/*     */     @PluginFactory
/*     */     public static LoggerConfig createLogger(@PluginAttribute("additivity") String additivity, @PluginAttribute("level") String levelName, @PluginAttribute("includeLocation") String includeLocation, @PluginElement("AppenderRef") AppenderRef[] refs, @PluginElement("Properties") Property[] properties, @PluginConfiguration Configuration config, @PluginElement("Filter") Filter filter) {
/*     */       Level level;
/* 216 */       List<AppenderRef> appenderRefs = Arrays.asList(refs);
/*     */       
/*     */       try {
/* 219 */         level = Level.toLevel(levelName, Level.ERROR);
/* 220 */       } catch (Exception ex) {
/* 221 */         LOGGER.error("Invalid Log level specified: {}. Defaulting to Error", new Object[] { levelName });
/*     */ 
/*     */         
/* 224 */         level = Level.ERROR;
/*     */       } 
/* 226 */       boolean additive = Booleans.parseBoolean(additivity, true);
/*     */       
/* 228 */       return new AsyncLoggerConfig("", appenderRefs, filter, level, additive, properties, config, includeLocation(includeLocation));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\async\AsyncLoggerConfig.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */