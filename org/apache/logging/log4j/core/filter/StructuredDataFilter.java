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
/*     */ import org.apache.logging.log4j.message.Message;
/*     */ import org.apache.logging.log4j.message.StructuredDataMessage;
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
/*     */ @Plugin(name = "StructuredDataFilter", category = "Core", elementType = "filter", printObject = true)
/*     */ public final class StructuredDataFilter
/*     */   extends MapFilter
/*     */ {
/*     */   private StructuredDataFilter(Map<String, List<String>> map, boolean oper, Filter.Result onMatch, Filter.Result onMismatch) {
/*  44 */     super(map, oper, onMatch, onMismatch);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, Message msg, Throwable t) {
/*  50 */     if (msg instanceof StructuredDataMessage) {
/*  51 */       return filter((StructuredDataMessage)msg);
/*     */     }
/*  53 */     return Filter.Result.NEUTRAL;
/*     */   }
/*     */ 
/*     */   
/*     */   public Filter.Result filter(LogEvent event) {
/*  58 */     Message msg = event.getMessage();
/*  59 */     if (msg instanceof StructuredDataMessage) {
/*  60 */       return filter((StructuredDataMessage)msg);
/*     */     }
/*  62 */     return super.filter(event);
/*     */   }
/*     */   
/*     */   protected Filter.Result filter(StructuredDataMessage message) {
/*  66 */     boolean match = false;
/*  67 */     for (Map.Entry<String, List<String>> entry : getMap().entrySet()) {
/*  68 */       String toMatch = getValue(message, entry.getKey());
/*  69 */       if (toMatch != null) {
/*  70 */         match = ((List)entry.getValue()).contains(toMatch);
/*     */       } else {
/*  72 */         match = false;
/*     */       } 
/*  74 */       if ((!isAnd() && match) || (isAnd() && !match)) {
/*     */         break;
/*     */       }
/*     */     } 
/*  78 */     return match ? this.onMatch : this.onMismatch;
/*     */   }
/*     */   
/*     */   private String getValue(StructuredDataMessage data, String key) {
/*  82 */     if (key.equalsIgnoreCase("id"))
/*  83 */       return data.getId().toString(); 
/*  84 */     if (key.equalsIgnoreCase("id.name"))
/*  85 */       return data.getId().getName(); 
/*  86 */     if (key.equalsIgnoreCase("type"))
/*  87 */       return data.getType(); 
/*  88 */     if (key.equalsIgnoreCase("message")) {
/*  89 */       return data.getFormattedMessage();
/*     */     }
/*  91 */     return (String)data.getData().get(key);
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
/*     */   @PluginFactory
/*     */   public static StructuredDataFilter createFilter(@PluginElement("Pairs") KeyValuePair[] pairs, @PluginAttribute("operator") String oper, @PluginAttribute("onMatch") Filter.Result match, @PluginAttribute("onMismatch") Filter.Result mismatch) {
/* 109 */     if (pairs == null || pairs.length == 0) {
/* 110 */       LOGGER.error("keys and values must be specified for the StructuredDataFilter");
/* 111 */       return null;
/*     */     } 
/* 113 */     Map<String, List<String>> map = new HashMap<String, List<String>>();
/* 114 */     for (KeyValuePair pair : pairs) {
/* 115 */       String key = pair.getKey();
/* 116 */       if (key == null) {
/* 117 */         LOGGER.error("A null key is not valid in MapFilter");
/*     */       } else {
/*     */         
/* 120 */         String value = pair.getValue();
/* 121 */         if (value == null) {
/* 122 */           LOGGER.error("A null value for key " + key + " is not allowed in MapFilter");
/*     */         } else {
/*     */           
/* 125 */           List<String> list = map.get(pair.getKey());
/* 126 */           if (list != null)
/* 127 */           { list.add(value); }
/*     */           else
/* 129 */           { list = new ArrayList<String>();
/* 130 */             list.add(value);
/* 131 */             map.put(pair.getKey(), list); } 
/*     */         } 
/*     */       } 
/* 134 */     }  if (map.isEmpty()) {
/* 135 */       LOGGER.error("StructuredDataFilter is not configured with any valid key value pairs");
/* 136 */       return null;
/*     */     } 
/* 138 */     boolean isAnd = (oper == null || !oper.equalsIgnoreCase("or"));
/* 139 */     return new StructuredDataFilter(map, isAnd, match, mismatch);
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\filter\StructuredDataFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */