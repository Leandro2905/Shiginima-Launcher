/*     */ package org.apache.logging.log4j.core.filter;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.apache.logging.log4j.Level;
/*     */ import org.apache.logging.log4j.Marker;
/*     */ import org.apache.logging.log4j.core.Filter;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.Logger;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginElement;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginFactory;
/*     */ import org.apache.logging.log4j.core.util.KeyValuePair;
/*     */ import org.apache.logging.log4j.message.MapMessage;
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
/*     */ 
/*     */ 
/*     */ @Plugin(name = "MapFilter", category = "Core", elementType = "filter", printObject = true)
/*     */ public class MapFilter
/*     */   extends AbstractFilter
/*     */ {
/*     */   private final Map<String, List<String>> map;
/*     */   private final boolean isAnd;
/*     */   
/*     */   protected MapFilter(Map<String, List<String>> map, boolean oper, Filter.Result onMatch, Filter.Result onMismatch) {
/*  47 */     super(onMatch, onMismatch);
/*  48 */     if (map == null) {
/*  49 */       throw new NullPointerException("key cannot be null");
/*     */     }
/*  51 */     this.isAnd = oper;
/*  52 */     this.map = map;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, Message msg, Throwable t) {
/*  58 */     if (msg instanceof MapMessage) {
/*  59 */       return filter(((MapMessage)msg).getData()) ? this.onMatch : this.onMismatch;
/*     */     }
/*  61 */     return Filter.Result.NEUTRAL;
/*     */   }
/*     */ 
/*     */   
/*     */   public Filter.Result filter(LogEvent event) {
/*  66 */     Message msg = event.getMessage();
/*  67 */     if (msg instanceof MapMessage) {
/*  68 */       return filter(((MapMessage)msg).getData()) ? this.onMatch : this.onMismatch;
/*     */     }
/*  70 */     return Filter.Result.NEUTRAL;
/*     */   }
/*     */   
/*     */   protected boolean filter(Map<String, String> data) {
/*  74 */     boolean match = false;
/*  75 */     for (Map.Entry<String, List<String>> entry : this.map.entrySet()) {
/*  76 */       String toMatch = data.get(entry.getKey());
/*  77 */       if (toMatch != null) {
/*  78 */         match = ((List)entry.getValue()).contains(toMatch);
/*     */       } else {
/*  80 */         match = false;
/*     */       } 
/*  82 */       if ((!this.isAnd && match) || (this.isAnd && !match)) {
/*     */         break;
/*     */       }
/*     */     } 
/*  86 */     return match;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  91 */     StringBuilder sb = new StringBuilder();
/*  92 */     sb.append("isAnd=").append(this.isAnd);
/*  93 */     if (this.map.size() > 0) {
/*  94 */       sb.append(", {");
/*  95 */       boolean first = true;
/*  96 */       for (Map.Entry<String, List<String>> entry : this.map.entrySet()) {
/*  97 */         if (!first) {
/*  98 */           sb.append(", ");
/*     */         }
/* 100 */         first = false;
/* 101 */         List<String> list = entry.getValue();
/* 102 */         String value = (list.size() > 1) ? list.get(0) : list.toString();
/* 103 */         sb.append(entry.getKey()).append('=').append(value);
/*     */       } 
/* 105 */       sb.append('}');
/*     */     } 
/* 107 */     return sb.toString();
/*     */   }
/*     */   
/*     */   protected boolean isAnd() {
/* 111 */     return this.isAnd;
/*     */   }
/*     */   
/*     */   protected Map<String, List<String>> getMap() {
/* 115 */     return this.map;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @PluginFactory
/*     */   public static MapFilter createFilter(@PluginElement("Pairs") KeyValuePair[] pairs, @PluginAttribute("operator") String oper, @PluginAttribute("onMatch") Filter.Result match, @PluginAttribute("onMismatch") Filter.Result mismatch) {
/* 124 */     if (pairs == null || pairs.length == 0) {
/* 125 */       LOGGER.error("keys and values must be specified for the MapFilter");
/* 126 */       return null;
/*     */     } 
/* 128 */     Map<String, List<String>> map = new HashMap<String, List<String>>();
/* 129 */     for (KeyValuePair pair : pairs) {
/* 130 */       String key = pair.getKey();
/* 131 */       if (key == null) {
/* 132 */         LOGGER.error("A null key is not valid in MapFilter");
/*     */       } else {
/*     */         
/* 135 */         String value = pair.getValue();
/* 136 */         if (value == null) {
/* 137 */           LOGGER.error("A null value for key " + key + " is not allowed in MapFilter");
/*     */         } else {
/*     */           
/* 140 */           List<String> list = map.get(pair.getKey());
/* 141 */           if (list != null)
/* 142 */           { list.add(value); }
/*     */           else
/* 144 */           { list = new ArrayList<String>();
/* 145 */             list.add(value);
/* 146 */             map.put(pair.getKey(), list); } 
/*     */         } 
/*     */       } 
/* 149 */     }  if (map.isEmpty()) {
/* 150 */       LOGGER.error("MapFilter is not configured with any valid key value pairs");
/* 151 */       return null;
/*     */     } 
/* 153 */     boolean isAnd = (oper == null || !oper.equalsIgnoreCase("or"));
/* 154 */     return new MapFilter(map, isAnd, match, mismatch);
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\filter\MapFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */