/*    */ package org.apache.logging.log4j.core.appender.routing;
/*    */ 
/*    */ import org.apache.logging.log4j.Logger;
/*    */ import org.apache.logging.log4j.core.config.plugins.Plugin;
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
/*    */ 
/*    */ @Plugin(name = "Routes", category = "Core", printObject = true)
/*    */ public final class Routes
/*    */ {
/* 32 */   private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*    */   
/*    */   private final String pattern;
/*    */   private final Route[] routes;
/*    */   
/*    */   private Routes(String pattern, Route... routes) {
/* 38 */     this.pattern = pattern;
/* 39 */     this.routes = routes;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getPattern() {
/* 47 */     return this.pattern;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Route[] getRoutes() {
/* 55 */     return this.routes;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 60 */     StringBuilder sb = new StringBuilder("{");
/* 61 */     boolean first = true;
/* 62 */     for (Route route : this.routes) {
/* 63 */       if (!first) {
/* 64 */         sb.append(',');
/*    */       }
/* 66 */       first = false;
/* 67 */       sb.append(route.toString());
/*    */     } 
/* 69 */     sb.append('}');
/* 70 */     return sb.toString();
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
/*    */   @PluginFactory
/*    */   public static Routes createRoutes(@PluginAttribute("pattern") String pattern, @PluginElement("Routes") Route... routes) {
/* 84 */     if (pattern == null) {
/* 85 */       LOGGER.error("A pattern is required");
/* 86 */       return null;
/*    */     } 
/* 88 */     if (routes == null || routes.length == 0) {
/* 89 */       LOGGER.error("No routes configured");
/* 90 */       return null;
/*    */     } 
/* 92 */     return new Routes(pattern, routes);
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\appender\routing\Routes.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */