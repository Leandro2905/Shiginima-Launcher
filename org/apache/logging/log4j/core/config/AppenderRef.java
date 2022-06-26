/*    */ package org.apache.logging.log4j.core.config;
/*    */ 
/*    */ import org.apache.logging.log4j.Level;
/*    */ import org.apache.logging.log4j.Logger;
/*    */ import org.apache.logging.log4j.core.Filter;
/*    */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*    */ import org.apache.logging.log4j.core.config.plugins.PluginAliases;
/*    */ import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
/*    */ import org.apache.logging.log4j.core.config.plugins.PluginElement;
/*    */ import org.apache.logging.log4j.core.config.plugins.PluginFactory;
/*    */ import org.apache.logging.log4j.status.StatusLogger;
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
/*    */ @Plugin(name = "AppenderRef", category = "Core", printObject = true)
/*    */ @PluginAliases({"appender-ref"})
/*    */ public final class AppenderRef
/*    */ {
/* 35 */   private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*    */   
/*    */   private final String ref;
/*    */   private final Level level;
/*    */   private final Filter filter;
/*    */   
/*    */   private AppenderRef(String ref, Level level, Filter filter) {
/* 42 */     this.ref = ref;
/* 43 */     this.level = level;
/* 44 */     this.filter = filter;
/*    */   }
/*    */   
/*    */   public String getRef() {
/* 48 */     return this.ref;
/*    */   }
/*    */   
/*    */   public Level getLevel() {
/* 52 */     return this.level;
/*    */   }
/*    */   
/*    */   public Filter getFilter() {
/* 56 */     return this.filter;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 61 */     return this.ref;
/*    */   }
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
/*    */   @PluginFactory
/*    */   public static AppenderRef createAppenderRef(@PluginAttribute("ref") String ref, @PluginAttribute("level") Level level, @PluginElement("Filter") Filter filter) {
/* 77 */     if (ref == null) {
/* 78 */       LOGGER.error("Appender references must contain a reference");
/* 79 */       return null;
/*    */     } 
/* 81 */     return new AppenderRef(ref, level, filter);
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\config\AppenderRef.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */