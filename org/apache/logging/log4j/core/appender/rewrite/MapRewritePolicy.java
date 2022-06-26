/*     */ package org.apache.logging.log4j.core.appender.rewrite;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginElement;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginFactory;
/*     */ import org.apache.logging.log4j.core.impl.Log4jLogEvent;
/*     */ import org.apache.logging.log4j.core.util.KeyValuePair;
/*     */ import org.apache.logging.log4j.message.MapMessage;
/*     */ import org.apache.logging.log4j.message.Message;
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
/*     */ @Plugin(name = "MapRewritePolicy", category = "Core", elementType = "rewritePolicy", printObject = true)
/*     */ public final class MapRewritePolicy
/*     */   implements RewritePolicy
/*     */ {
/*  42 */   protected static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*     */   
/*     */   private final Map<String, String> map;
/*     */   
/*     */   private final Mode mode;
/*     */   
/*     */   private MapRewritePolicy(Map<String, String> map, Mode mode) {
/*  49 */     this.map = map;
/*  50 */     this.mode = mode;
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
/*  61 */     Message msg = source.getMessage();
/*  62 */     if (msg == null || !(msg instanceof MapMessage)) {
/*  63 */       return source;
/*     */     }
/*     */     
/*  66 */     Map<String, String> newMap = new HashMap<String, String>(((MapMessage)msg).getData());
/*  67 */     switch (this.mode) {
/*     */       case Add:
/*  69 */         newMap.putAll(this.map);
/*     */         break;
/*     */       
/*     */       default:
/*  73 */         for (Map.Entry<String, String> entry : this.map.entrySet()) {
/*  74 */           if (newMap.containsKey(entry.getKey())) {
/*  75 */             newMap.put(entry.getKey(), entry.getValue());
/*     */           }
/*     */         } 
/*     */         break;
/*     */     } 
/*  80 */     MapMessage message = ((MapMessage)msg).newInstance(newMap);
/*  81 */     if (source instanceof Log4jLogEvent) {
/*  82 */       Log4jLogEvent event = (Log4jLogEvent)source;
/*  83 */       return (LogEvent)Log4jLogEvent.createEvent(event.getLoggerName(), event.getMarker(), event.getLoggerFqcn(), event.getLevel(), (Message)message, event.getThrown(), event.getThrownProxy(), event.getContextMap(), event.getContextStack(), event.getThreadName(), event.getSource(), event.getTimeMillis());
/*     */     } 
/*     */ 
/*     */     
/*  87 */     return (LogEvent)new Log4jLogEvent(source.getLoggerName(), source.getMarker(), source.getLoggerFqcn(), source.getLevel(), (Message)message, source.getThrown(), source.getContextMap(), source.getContextStack(), source.getThreadName(), source.getSource(), source.getTimeMillis());
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
/*     */   public enum Mode
/*     */   {
/* 100 */     Add,
/*     */ 
/*     */ 
/*     */     
/* 104 */     Update;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 109 */     StringBuilder sb = new StringBuilder();
/* 110 */     sb.append("mode=").append(this.mode);
/* 111 */     sb.append(" {");
/* 112 */     boolean first = true;
/* 113 */     for (Map.Entry<String, String> entry : this.map.entrySet()) {
/* 114 */       if (!first) {
/* 115 */         sb.append(", ");
/*     */       }
/* 117 */       sb.append(entry.getKey()).append('=').append(entry.getValue());
/* 118 */       first = false;
/*     */     } 
/* 120 */     sb.append('}');
/* 121 */     return sb.toString();
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
/*     */   @PluginFactory
/*     */   public static MapRewritePolicy createPolicy(@PluginAttribute("mode") String mode, @PluginElement("KeyValuePair") KeyValuePair[] pairs) {
/*     */     Mode op;
/* 135 */     if (mode == null) {
/* 136 */       op = Mode.Add;
/*     */     } else {
/* 138 */       op = Mode.valueOf(mode);
/* 139 */       if (op == null) {
/* 140 */         LOGGER.error("Undefined mode " + mode);
/* 141 */         return null;
/*     */       } 
/*     */     } 
/* 144 */     if (pairs == null || pairs.length == 0) {
/* 145 */       LOGGER.error("keys and values must be specified for the MapRewritePolicy");
/* 146 */       return null;
/*     */     } 
/* 148 */     Map<String, String> map = new HashMap<String, String>();
/* 149 */     for (KeyValuePair pair : pairs) {
/* 150 */       String key = pair.getKey();
/* 151 */       if (key == null) {
/* 152 */         LOGGER.error("A null key is not valid in MapRewritePolicy");
/*     */       } else {
/*     */         
/* 155 */         String value = pair.getValue();
/* 156 */         if (value == null)
/* 157 */         { LOGGER.error("A null value for key " + key + " is not allowed in MapRewritePolicy"); }
/*     */         else
/*     */         
/* 160 */         { map.put(pair.getKey(), pair.getValue()); } 
/*     */       } 
/* 162 */     }  if (map.isEmpty()) {
/* 163 */       LOGGER.error("MapRewritePolicy is not configured with any valid key value pairs");
/* 164 */       return null;
/*     */     } 
/* 166 */     return new MapRewritePolicy(map, op);
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\appender\rewrite\MapRewritePolicy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */