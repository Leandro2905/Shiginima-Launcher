/*     */ package org.apache.logging.log4j.core.async;
/*     */ 
/*     */ import com.lmax.disruptor.EventFactory;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.apache.logging.log4j.Level;
/*     */ import org.apache.logging.log4j.Marker;
/*     */ import org.apache.logging.log4j.ThreadContext;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.config.Property;
/*     */ import org.apache.logging.log4j.core.impl.Log4jLogEvent;
/*     */ import org.apache.logging.log4j.core.impl.ThrowableProxy;
/*     */ import org.apache.logging.log4j.core.lookup.StrSubstitutor;
/*     */ import org.apache.logging.log4j.message.Message;
/*     */ import org.apache.logging.log4j.message.SimpleMessage;
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
/*     */ public class RingBufferLogEvent
/*     */   implements LogEvent
/*     */ {
/*     */   private static final long serialVersionUID = 8462119088943934758L;
/*     */   
/*     */   private static class Factory
/*     */     implements EventFactory<RingBufferLogEvent>
/*     */   {
/*     */     private Factory() {}
/*     */     
/*     */     public RingBufferLogEvent newInstance() {
/*  51 */       return new RingBufferLogEvent();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*  56 */   public static final Factory FACTORY = new Factory();
/*     */   
/*     */   private transient AsyncLogger asyncLogger;
/*     */   
/*     */   private String loggerName;
/*     */   
/*     */   private Marker marker;
/*     */   
/*     */   private String fqcn;
/*     */   private Level level;
/*     */   private Message message;
/*     */   private transient Throwable thrown;
/*     */   private ThrowableProxy thrownProxy;
/*     */   private Map<String, String> contextMap;
/*     */   private ThreadContext.ContextStack contextStack;
/*     */   private String threadName;
/*     */   private StackTraceElement location;
/*     */   private long currentTimeMillis;
/*     */   private boolean endOfBatch;
/*     */   private boolean includeLocation;
/*     */   
/*     */   public void setValues(AsyncLogger asyncLogger, String loggerName, Marker marker, String fqcn, Level level, Message data, Throwable throwable, Map<String, String> map, ThreadContext.ContextStack contextStack, String threadName, StackTraceElement location, long currentTimeMillis) {
/*  78 */     this.asyncLogger = asyncLogger;
/*  79 */     this.loggerName = loggerName;
/*  80 */     this.marker = marker;
/*  81 */     this.fqcn = fqcn;
/*  82 */     this.level = level;
/*  83 */     this.message = data;
/*  84 */     this.thrown = throwable;
/*  85 */     this.thrownProxy = null;
/*  86 */     this.contextMap = map;
/*  87 */     this.contextStack = contextStack;
/*  88 */     this.threadName = threadName;
/*  89 */     this.location = location;
/*  90 */     this.currentTimeMillis = currentTimeMillis;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute(boolean endOfBatch) {
/*  99 */     this.endOfBatch = endOfBatch;
/* 100 */     this.asyncLogger.actualAsyncLog(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEndOfBatch() {
/* 110 */     return this.endOfBatch;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setEndOfBatch(boolean endOfBatch) {
/* 115 */     this.endOfBatch = endOfBatch;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isIncludeLocation() {
/* 120 */     return this.includeLocation;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setIncludeLocation(boolean includeLocation) {
/* 125 */     this.includeLocation = includeLocation;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getLoggerName() {
/* 130 */     return this.loggerName;
/*     */   }
/*     */ 
/*     */   
/*     */   public Marker getMarker() {
/* 135 */     return this.marker;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getLoggerFqcn() {
/* 140 */     return this.fqcn;
/*     */   }
/*     */ 
/*     */   
/*     */   public Level getLevel() {
/* 145 */     if (this.level == null) {
/* 146 */       this.level = Level.OFF;
/*     */     }
/* 148 */     return this.level;
/*     */   }
/*     */ 
/*     */   
/*     */   public Message getMessage() {
/* 153 */     if (this.message == null) {
/* 154 */       this.message = (Message)new SimpleMessage("");
/*     */     }
/* 156 */     return this.message;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Throwable getThrown() {
/* 162 */     if (this.thrown == null && 
/* 163 */       this.thrownProxy != null) {
/* 164 */       this.thrown = this.thrownProxy.getThrowable();
/*     */     }
/*     */     
/* 167 */     return this.thrown;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ThrowableProxy getThrownProxy() {
/* 173 */     if (this.thrownProxy == null && 
/* 174 */       this.thrown != null) {
/* 175 */       this.thrownProxy = new ThrowableProxy(this.thrown);
/*     */     }
/*     */     
/* 178 */     return this.thrownProxy;
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<String, String> getContextMap() {
/* 183 */     return this.contextMap;
/*     */   }
/*     */ 
/*     */   
/*     */   public ThreadContext.ContextStack getContextStack() {
/* 188 */     return this.contextStack;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getThreadName() {
/* 193 */     return this.threadName;
/*     */   }
/*     */ 
/*     */   
/*     */   public StackTraceElement getSource() {
/* 198 */     return this.location;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getTimeMillis() {
/* 203 */     return this.currentTimeMillis;
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
/*     */   public void mergePropertiesIntoContextMap(Map<Property, Boolean> properties, StrSubstitutor strSubstitutor) {
/* 215 */     if (properties == null) {
/*     */       return;
/*     */     }
/*     */     
/* 219 */     Map<String, String> map = (this.contextMap == null) ? new HashMap<String, String>() : new HashMap<String, String>(this.contextMap);
/*     */ 
/*     */     
/* 222 */     for (Map.Entry<Property, Boolean> entry : properties.entrySet()) {
/* 223 */       Property prop = entry.getKey();
/* 224 */       if (map.containsKey(prop.getName())) {
/*     */         continue;
/*     */       }
/* 227 */       String value = ((Boolean)entry.getValue()).booleanValue() ? strSubstitutor.replace(prop.getValue()) : prop.getValue();
/*     */       
/* 229 */       map.put(prop.getName(), value);
/*     */     } 
/* 231 */     this.contextMap = map;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() {
/* 238 */     setValues(null, null, null, null, null, null, null, null, null, null, null, 0L);
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
/*     */   private void writeObject(ObjectOutputStream out) throws IOException {
/* 254 */     getThrownProxy();
/* 255 */     out.defaultWriteObject();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LogEvent createMemento() {
/* 266 */     return (LogEvent)new Log4jLogEvent(this.loggerName, this.marker, this.fqcn, this.level, this.message, this.thrown, this.contextMap, this.contextStack, this.threadName, this.location, this.currentTimeMillis);
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\async\RingBufferLogEvent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */