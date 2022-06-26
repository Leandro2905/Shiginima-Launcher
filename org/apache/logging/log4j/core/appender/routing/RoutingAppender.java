/*     */ package org.apache.logging.log4j.core.appender.routing;
/*     */ 
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import org.apache.logging.log4j.core.Appender;
/*     */ import org.apache.logging.log4j.core.Filter;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.appender.AbstractAppender;
/*     */ import org.apache.logging.log4j.core.appender.rewrite.RewritePolicy;
/*     */ import org.apache.logging.log4j.core.config.AppenderControl;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.config.Node;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Plugin(name = "Routing", category = "Core", elementType = "appender", printObject = true)
/*     */ public final class RoutingAppender
/*     */   extends AbstractAppender
/*     */ {
/*     */   private static final String DEFAULT_KEY = "ROUTING_APPENDER_DEFAULT";
/*     */   private final Routes routes;
/*     */   private final Route defaultRoute;
/*     */   private final Configuration config;
/*  52 */   private final ConcurrentMap<String, AppenderControl> appenders = new ConcurrentHashMap<String, AppenderControl>();
/*     */   
/*     */   private final RewritePolicy rewritePolicy;
/*     */ 
/*     */   
/*     */   private RoutingAppender(String name, Filter filter, boolean ignoreExceptions, Routes routes, RewritePolicy rewritePolicy, Configuration config) {
/*  58 */     super(name, filter, null, ignoreExceptions);
/*  59 */     this.routes = routes;
/*  60 */     this.config = config;
/*  61 */     this.rewritePolicy = rewritePolicy;
/*  62 */     Route defRoute = null;
/*  63 */     for (Route route : routes.getRoutes()) {
/*  64 */       if (route.getKey() == null) {
/*  65 */         if (defRoute == null) {
/*  66 */           defRoute = route;
/*     */         } else {
/*  68 */           error("Multiple default routes. Route " + route.toString() + " will be ignored");
/*     */         } 
/*     */       }
/*     */     } 
/*  72 */     this.defaultRoute = defRoute;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void start() {
/*  78 */     for (Route route : this.routes.getRoutes()) {
/*  79 */       if (route.getAppenderRef() != null) {
/*  80 */         Appender appender = this.config.getAppender(route.getAppenderRef());
/*  81 */         if (appender != null) {
/*  82 */           String key = (route == this.defaultRoute) ? "ROUTING_APPENDER_DEFAULT" : route.getKey();
/*  83 */           this.appenders.put(key, new AppenderControl(appender, null, null));
/*     */         } else {
/*  85 */           LOGGER.error("Appender " + route.getAppenderRef() + " cannot be located. Route ignored");
/*     */         } 
/*     */       } 
/*     */     } 
/*  89 */     super.start();
/*     */   }
/*     */ 
/*     */   
/*     */   public void stop() {
/*  94 */     super.stop();
/*  95 */     Map<String, Appender> map = this.config.getAppenders();
/*  96 */     for (Map.Entry<String, AppenderControl> entry : this.appenders.entrySet()) {
/*  97 */       String name = ((AppenderControl)entry.getValue()).getAppender().getName();
/*  98 */       if (!map.containsKey(name)) {
/*  99 */         ((AppenderControl)entry.getValue()).getAppender().stop();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void append(LogEvent event) {
/* 106 */     if (this.rewritePolicy != null) {
/* 107 */       event = this.rewritePolicy.rewrite(event);
/*     */     }
/* 109 */     String key = this.config.getStrSubstitutor().replace(event, this.routes.getPattern());
/* 110 */     AppenderControl control = getControl(key, event);
/* 111 */     if (control != null) {
/* 112 */       control.callAppender(event);
/*     */     }
/*     */   }
/*     */   
/*     */   private synchronized AppenderControl getControl(String key, LogEvent event) {
/* 117 */     AppenderControl control = this.appenders.get(key);
/* 118 */     if (control != null) {
/* 119 */       return control;
/*     */     }
/* 121 */     Route route = null;
/* 122 */     for (Route r : this.routes.getRoutes()) {
/* 123 */       if (r.getAppenderRef() == null && key.equals(r.getKey())) {
/* 124 */         route = r;
/*     */         break;
/*     */       } 
/*     */     } 
/* 128 */     if (route == null) {
/* 129 */       route = this.defaultRoute;
/* 130 */       control = this.appenders.get("ROUTING_APPENDER_DEFAULT");
/* 131 */       if (control != null) {
/* 132 */         return control;
/*     */       }
/*     */     } 
/* 135 */     if (route != null) {
/* 136 */       Appender app = createAppender(route, event);
/* 137 */       if (app == null) {
/* 138 */         return null;
/*     */       }
/* 140 */       control = new AppenderControl(app, null, null);
/* 141 */       this.appenders.put(key, control);
/*     */     } 
/*     */     
/* 144 */     return control;
/*     */   }
/*     */   
/*     */   private Appender createAppender(Route route, LogEvent event) {
/* 148 */     Node routeNode = route.getNode();
/* 149 */     for (Node node : routeNode.getChildren()) {
/* 150 */       if (node.getType().getElementName().equals("appender")) {
/* 151 */         Node appNode = new Node(node);
/* 152 */         this.config.createConfiguration(appNode, event);
/* 153 */         if (appNode.getObject() instanceof Appender) {
/* 154 */           Appender app = (Appender)appNode.getObject();
/* 155 */           app.start();
/* 156 */           return app;
/*     */         } 
/* 158 */         LOGGER.error("Unable to create Appender of type " + node.getName());
/* 159 */         return null;
/*     */       } 
/*     */     } 
/* 162 */     LOGGER.error("No Appender was configured for route " + route.getKey());
/* 163 */     return null;
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
/*     */   public static RoutingAppender createAppender(@PluginAttribute("name") String name, @PluginAttribute("ignoreExceptions") String ignore, @PluginElement("Routes") Routes routes, @PluginConfiguration Configuration config, @PluginElement("RewritePolicy") RewritePolicy rewritePolicy, @PluginElement("Filter") Filter filter) {
/* 186 */     boolean ignoreExceptions = Booleans.parseBoolean(ignore, true);
/* 187 */     if (name == null) {
/* 188 */       LOGGER.error("No name provided for RoutingAppender");
/* 189 */       return null;
/*     */     } 
/* 191 */     if (routes == null) {
/* 192 */       LOGGER.error("No routes defined for RoutingAppender");
/* 193 */       return null;
/*     */     } 
/* 195 */     return new RoutingAppender(name, filter, ignoreExceptions, routes, rewritePolicy, config);
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\appender\routing\RoutingAppender.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */