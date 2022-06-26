/*     */ package org.apache.logging.log4j.core.appender.routing;
/*     */ 
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.core.config.Node;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginFactory;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginNode;
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
/*     */ @Plugin(name = "Route", category = "Core", printObject = true, deferChildren = true)
/*     */ public final class Route
/*     */ {
/*  32 */   private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*     */   
/*     */   private final Node node;
/*     */   private final String appenderRef;
/*     */   private final String key;
/*     */   
/*     */   private Route(Node node, String appenderRef, String key) {
/*  39 */     this.node = node;
/*  40 */     this.appenderRef = appenderRef;
/*  41 */     this.key = key;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Node getNode() {
/*  49 */     return this.node;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getAppenderRef() {
/*  57 */     return this.appenderRef;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getKey() {
/*  65 */     return this.key;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  70 */     StringBuilder sb = new StringBuilder("Route(");
/*  71 */     sb.append("type=");
/*  72 */     if (this.appenderRef != null) {
/*  73 */       sb.append("static Reference=").append(this.appenderRef);
/*  74 */     } else if (this.node != null) {
/*  75 */       sb.append("dynamic - type=").append(this.node.getName());
/*     */     } else {
/*  77 */       sb.append("invalid Route");
/*     */     } 
/*  79 */     if (this.key != null) {
/*  80 */       sb.append(" key='").append(this.key).append('\'');
/*     */     } else {
/*  82 */       sb.append(" default");
/*     */     } 
/*  84 */     sb.append(')');
/*  85 */     return sb.toString();
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
/*     */   @PluginFactory
/*     */   public static Route createRoute(@PluginAttribute("ref") String appenderRef, @PluginAttribute("key") String key, @PluginNode Node node) {
/* 100 */     if (node != null && node.hasChildren()) {
/* 101 */       if (appenderRef != null) {
/* 102 */         LOGGER.error("A route cannot be configured with an appender reference and an appender definition");
/* 103 */         return null;
/*     */       }
/*     */     
/* 106 */     } else if (appenderRef == null) {
/* 107 */       LOGGER.error("A route must specify an appender reference or an appender definition");
/* 108 */       return null;
/*     */     } 
/*     */     
/* 111 */     return new Route(node, appenderRef, key);
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\appender\routing\Route.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */