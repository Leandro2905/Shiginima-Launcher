/*     */ package org.apache.logging.log4j.core.filter;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ @Plugin(name = "ThreadContextMapFilter", category = "Core", elementType = "filter", printObject = true)
/*     */ public class ThreadContextMapFilter
/*     */   extends MapFilter
/*     */ {
/*     */   private final String key;
/*     */   private final String value;
/*     */   private final boolean useMap;
/*     */   
/*     */   public ThreadContextMapFilter(Map<String, List<String>> pairs, boolean oper, Filter.Result onMatch, Filter.Result onMismatch) {
/*  50 */     super(pairs, oper, onMatch, onMismatch);
/*  51 */     if (pairs.size() == 1) {
/*  52 */       Iterator<Map.Entry<String, List<String>>> iter = pairs.entrySet().iterator();
/*  53 */       Map.Entry<String, List<String>> entry = iter.next();
/*  54 */       if (((List)entry.getValue()).size() == 1) {
/*  55 */         this.key = entry.getKey();
/*  56 */         this.value = ((List<String>)entry.getValue()).get(0);
/*  57 */         this.useMap = false;
/*     */       } else {
/*  59 */         this.key = null;
/*  60 */         this.value = null;
/*  61 */         this.useMap = true;
/*     */       } 
/*     */     } else {
/*  64 */       this.key = null;
/*  65 */       this.value = null;
/*  66 */       this.useMap = true;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object... params) {
/*  73 */     return filter();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, Object msg, Throwable t) {
/*  79 */     return filter();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, Message msg, Throwable t) {
/*  85 */     return filter();
/*     */   }
/*     */   
/*     */   private Filter.Result filter() {
/*  89 */     boolean match = false;
/*  90 */     if (this.useMap) {
/*  91 */       for (Map.Entry<String, List<String>> entry : getMap().entrySet()) {
/*  92 */         String toMatch = ThreadContext.get(entry.getKey());
/*  93 */         if (toMatch != null) {
/*  94 */           match = ((List)entry.getValue()).contains(toMatch);
/*     */         } else {
/*  96 */           match = false;
/*     */         } 
/*  98 */         if ((!isAnd() && match) || (isAnd() && !match)) {
/*     */           break;
/*     */         }
/*     */       } 
/*     */     } else {
/* 103 */       match = this.value.equals(ThreadContext.get(this.key));
/*     */     } 
/* 105 */     return match ? this.onMatch : this.onMismatch;
/*     */   }
/*     */ 
/*     */   
/*     */   public Filter.Result filter(LogEvent event) {
/* 110 */     return filter(event.getContextMap()) ? this.onMatch : this.onMismatch;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @PluginFactory
/*     */   public static ThreadContextMapFilter createFilter(@PluginElement("Pairs") KeyValuePair[] pairs, @PluginAttribute("operator") String oper, @PluginAttribute("onMatch") Filter.Result match, @PluginAttribute("onMismatch") Filter.Result mismatch) {
/* 119 */     if (pairs == null || pairs.length == 0) {
/* 120 */       LOGGER.error("key and value pairs must be specified for the ThreadContextMapFilter");
/* 121 */       return null;
/*     */     } 
/* 123 */     Map<String, List<String>> map = new HashMap<String, List<String>>();
/* 124 */     for (KeyValuePair pair : pairs) {
/* 125 */       String key = pair.getKey();
/* 126 */       if (key == null) {
/* 127 */         LOGGER.error("A null key is not valid in MapFilter");
/*     */       } else {
/*     */         
/* 130 */         String value = pair.getValue();
/* 131 */         if (value == null) {
/* 132 */           LOGGER.error("A null value for key " + key + " is not allowed in MapFilter");
/*     */         } else {
/*     */           
/* 135 */           List<String> list = map.get(pair.getKey());
/* 136 */           if (list != null)
/* 137 */           { list.add(value); }
/*     */           else
/* 139 */           { list = new ArrayList<String>();
/* 140 */             list.add(value);
/* 141 */             map.put(pair.getKey(), list); } 
/*     */         } 
/*     */       } 
/* 144 */     }  if (map.isEmpty()) {
/* 145 */       LOGGER.error("ThreadContextMapFilter is not configured with any valid key value pairs");
/* 146 */       return null;
/*     */     } 
/* 148 */     boolean isAnd = (oper == null || !oper.equalsIgnoreCase("or"));
/* 149 */     return new ThreadContextMapFilter(map, isAnd, match, mismatch);
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\filter\ThreadContextMapFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */