/*     */ package org.apache.logging.log4j.core.filter;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.apache.logging.log4j.Level;
/*     */ import org.apache.logging.log4j.Marker;
/*     */ import org.apache.logging.log4j.ThreadContext;
/*     */ import org.apache.logging.log4j.core.Filter;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.Logger;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginElement;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginFactory;
/*     */ import org.apache.logging.log4j.core.util.KeyValuePair;
/*     */ import org.apache.logging.log4j.message.Message;
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
/*     */ @Plugin(name = "DynamicThresholdFilter", category = "Core", elementType = "filter", printObject = true)
/*     */ public final class DynamicThresholdFilter
/*     */   extends AbstractFilter
/*     */ {
/*  39 */   private Map<String, Level> levelMap = new HashMap<String, Level>();
/*  40 */   private Level defaultThreshold = Level.ERROR;
/*     */   
/*     */   private final String key;
/*     */   
/*     */   private DynamicThresholdFilter(String key, Map<String, Level> pairs, Level defaultLevel, Filter.Result onMatch, Filter.Result onMismatch) {
/*  45 */     super(onMatch, onMismatch);
/*  46 */     if (key == null) {
/*  47 */       throw new NullPointerException("key cannot be null");
/*     */     }
/*  49 */     this.key = key;
/*  50 */     this.levelMap = pairs;
/*  51 */     this.defaultThreshold = defaultLevel;
/*     */   }
/*     */   
/*     */   public String getKey() {
/*  55 */     return this.key;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object... params) {
/*  61 */     return filter(level);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, Object msg, Throwable t) {
/*  67 */     return filter(level);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, Message msg, Throwable t) {
/*  73 */     return filter(level);
/*     */   }
/*     */ 
/*     */   
/*     */   public Filter.Result filter(LogEvent event) {
/*  78 */     return filter(event.getLevel());
/*     */   }
/*     */   
/*     */   private Filter.Result filter(Level level) {
/*  82 */     Object value = ThreadContext.get(this.key);
/*  83 */     if (value != null) {
/*  84 */       Level ctxLevel = this.levelMap.get(value);
/*  85 */       if (ctxLevel == null) {
/*  86 */         ctxLevel = this.defaultThreshold;
/*     */       }
/*  88 */       return level.isMoreSpecificThan(ctxLevel) ? this.onMatch : this.onMismatch;
/*     */     } 
/*  90 */     return Filter.Result.NEUTRAL;
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<String, Level> getLevelMap() {
/*  95 */     return this.levelMap;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 100 */     StringBuilder sb = new StringBuilder();
/* 101 */     sb.append("key=").append(this.key);
/* 102 */     sb.append(", default=").append(this.defaultThreshold);
/* 103 */     if (this.levelMap.size() > 0) {
/* 104 */       sb.append('{');
/* 105 */       boolean first = true;
/* 106 */       for (Map.Entry<String, Level> entry : this.levelMap.entrySet()) {
/* 107 */         if (!first) {
/* 108 */           sb.append(", ");
/* 109 */           first = false;
/*     */         } 
/* 111 */         sb.append(entry.getKey()).append('=').append(entry.getValue());
/*     */       } 
/* 113 */       sb.append('}');
/*     */     } 
/* 115 */     return sb.toString();
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
/*     */   @PluginFactory
/*     */   public static DynamicThresholdFilter createFilter(@PluginAttribute("key") String key, @PluginElement("Pairs") KeyValuePair[] pairs, @PluginAttribute("defaultThreshold") Level defaultThreshold, @PluginAttribute("onMatch") Filter.Result onMatch, @PluginAttribute("onMismatch") Filter.Result onMismatch) {
/* 134 */     Map<String, Level> map = new HashMap<String, Level>();
/* 135 */     for (KeyValuePair pair : pairs) {
/* 136 */       map.put(pair.getKey(), Level.toLevel(pair.getValue()));
/*     */     }
/* 138 */     Level level = (defaultThreshold == null) ? Level.ERROR : defaultThreshold;
/* 139 */     return new DynamicThresholdFilter(key, map, level, onMatch, onMismatch);
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\filter\DynamicThresholdFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */