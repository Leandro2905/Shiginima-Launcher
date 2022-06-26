/*    */ package org.apache.logging.log4j.core.appender;
/*    */ 
/*    */ import org.apache.logging.log4j.Logger;
/*    */ import org.apache.logging.log4j.core.config.AppenderRef;
/*    */ import org.apache.logging.log4j.core.config.plugins.Plugin;
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
/*    */ 
/*    */ @Plugin(name = "failovers", category = "Core")
/*    */ public final class FailoversPlugin
/*    */ {
/* 32 */   private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
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
/*    */   @PluginFactory
/*    */   public static String[] createFailovers(@PluginElement("AppenderRef") AppenderRef... refs) {
/* 48 */     if (refs == null) {
/* 49 */       LOGGER.error("failovers must contain an appender reference");
/* 50 */       return null;
/*    */     } 
/* 52 */     String[] arr = new String[refs.length];
/* 53 */     for (int i = 0; i < refs.length; i++) {
/* 54 */       arr[i] = refs[i].getRef();
/*    */     }
/* 56 */     return arr;
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\appender\FailoversPlugin.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */