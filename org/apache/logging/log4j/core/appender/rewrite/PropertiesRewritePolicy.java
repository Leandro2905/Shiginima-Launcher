/*     */ package org.apache.logging.log4j.core.appender.rewrite;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.config.Property;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginElement;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginFactory;
/*     */ import org.apache.logging.log4j.core.impl.Log4jLogEvent;
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
/*     */ @Plugin(name = "PropertiesRewritePolicy", category = "Core", elementType = "rewritePolicy", printObject = true)
/*     */ public final class PropertiesRewritePolicy
/*     */   implements RewritePolicy
/*     */ {
/*  43 */   protected static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*     */   
/*     */   private final Map<Property, Boolean> properties;
/*     */   
/*     */   private final Configuration config;
/*     */   
/*     */   private PropertiesRewritePolicy(Configuration config, List<Property> props) {
/*  50 */     this.config = config;
/*  51 */     this.properties = new HashMap<Property, Boolean>(props.size());
/*  52 */     for (Property property : props) {
/*  53 */       Boolean interpolate = Boolean.valueOf(property.getValue().contains("${"));
/*  54 */       this.properties.put(property, interpolate);
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
/*     */   public LogEvent rewrite(LogEvent source) {
/*  66 */     Map<String, String> props = new HashMap<String, String>(source.getContextMap());
/*  67 */     for (Map.Entry<Property, Boolean> entry : this.properties.entrySet()) {
/*  68 */       Property prop = entry.getKey();
/*  69 */       props.put(prop.getName(), ((Boolean)entry.getValue()).booleanValue() ? this.config.getStrSubstitutor().replace(prop.getValue()) : prop.getValue());
/*     */     } 
/*     */ 
/*     */     
/*  73 */     return (LogEvent)new Log4jLogEvent(source.getLoggerName(), source.getMarker(), source.getLoggerFqcn(), source.getLevel(), source.getMessage(), source.getThrown(), props, source.getContextStack(), source.getThreadName(), source.getSource(), source.getTimeMillis());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/*  80 */     StringBuilder sb = new StringBuilder();
/*  81 */     sb.append(" {");
/*  82 */     boolean first = true;
/*  83 */     for (Map.Entry<Property, Boolean> entry : this.properties.entrySet()) {
/*  84 */       if (!first) {
/*  85 */         sb.append(", ");
/*     */       }
/*  87 */       Property prop = entry.getKey();
/*  88 */       sb.append(prop.getName()).append('=').append(prop.getValue());
/*  89 */       first = false;
/*     */     } 
/*  91 */     sb.append('}');
/*  92 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @PluginFactory
/*     */   public static PropertiesRewritePolicy createPolicy(@PluginConfiguration Configuration config, @PluginElement("Properties") Property[] props) {
/* 104 */     if (props == null || props.length == 0) {
/* 105 */       LOGGER.error("Properties must be specified for the PropertiesRewritePolicy");
/* 106 */       return null;
/*     */     } 
/* 108 */     List<Property> properties = Arrays.asList(props);
/* 109 */     return new PropertiesRewritePolicy(config, properties);
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\appender\rewrite\PropertiesRewritePolicy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */