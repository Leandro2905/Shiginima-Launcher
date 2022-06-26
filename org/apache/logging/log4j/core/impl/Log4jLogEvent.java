/*     */ package org.apache.logging.log4j.core.impl;
/*     */ 
/*     */ import java.io.InvalidObjectException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.apache.logging.log4j.Level;
/*     */ import org.apache.logging.log4j.Marker;
/*     */ import org.apache.logging.log4j.ThreadContext;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.config.Property;
/*     */ import org.apache.logging.log4j.core.util.Clock;
/*     */ import org.apache.logging.log4j.core.util.ClockFactory;
/*     */ import org.apache.logging.log4j.message.LoggerNameAwareMessage;
/*     */ import org.apache.logging.log4j.message.Message;
/*     */ import org.apache.logging.log4j.message.TimestampMessage;
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
/*     */ public class Log4jLogEvent
/*     */   implements LogEvent
/*     */ {
/*     */   private static final long serialVersionUID = -1351367343806656055L;
/*  45 */   private static final Clock clock = ClockFactory.getClock();
/*     */   private final String loggerFqcn;
/*     */   private final Marker marker;
/*     */   private final Level level;
/*     */   private final String loggerName;
/*     */   private final Message message;
/*     */   private final long timeMillis;
/*     */   private final transient Throwable thrown;
/*     */   private ThrowableProxy thrownProxy;
/*     */   private final Map<String, String> contextMap;
/*     */   private final ThreadContext.ContextStack contextStack;
/*  56 */   private String threadName = null;
/*     */   private StackTraceElement source;
/*     */   private boolean includeLocation;
/*     */   private boolean endOfBatch = false;
/*     */   
/*     */   public Log4jLogEvent() {
/*  62 */     this(clock.currentTimeMillis());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Log4jLogEvent(long timestamp) {
/*  69 */     this("", null, "", null, null, (Throwable)null, null, null, null, null, timestamp);
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
/*     */   public Log4jLogEvent(String loggerName, Marker marker, String loggerFQCN, Level level, Message message, Throwable t) {
/*  83 */     this(loggerName, marker, loggerFQCN, level, message, null, t);
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
/*     */   public Log4jLogEvent(String loggerName, Marker marker, String loggerFQCN, Level level, Message message, List<Property> properties, Throwable t) {
/*  98 */     this(loggerName, marker, loggerFQCN, level, message, t, createMap(properties), (ThreadContext.getDepth() == 0) ? null : ThreadContext.cloneStack(), null, null, (message instanceof TimestampMessage) ? ((TimestampMessage)message).getTimestamp() : clock.currentTimeMillis());
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Log4jLogEvent(String loggerName, Marker marker, String loggerFQCN, Level level, Message message, Throwable t, Map<String, String> mdc, ThreadContext.ContextStack ndc, String threadName, StackTraceElement location, long timestamp) {
/* 126 */     this(loggerName, marker, loggerFQCN, level, message, t, null, mdc, ndc, threadName, location, timestamp);
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
/*     */ 
/*     */ 
/*     */   
/*     */   public static Log4jLogEvent createEvent(String loggerName, Marker marker, String loggerFQCN, Level level, Message message, Throwable thrown, ThrowableProxy thrownProxy, Map<String, String> mdc, ThreadContext.ContextStack ndc, String threadName, StackTraceElement location, long timestamp) {
/* 151 */     Log4jLogEvent result = new Log4jLogEvent(loggerName, marker, loggerFQCN, level, message, thrown, thrownProxy, mdc, ndc, threadName, location, timestamp);
/*     */     
/* 153 */     return result;
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
/*     */   private Log4jLogEvent(String loggerName, Marker marker, String loggerFQCN, Level level, Message message, Throwable thrown, ThrowableProxy thrownProxy, Map<String, String> contextMap, ThreadContext.ContextStack contextStack, String threadName, StackTraceElement source, long timestamp) {
/* 175 */     this.loggerName = loggerName;
/* 176 */     this.marker = marker;
/* 177 */     this.loggerFqcn = loggerFQCN;
/* 178 */     this.level = (level == null) ? Level.OFF : level;
/* 179 */     this.message = message;
/* 180 */     this.thrown = thrown;
/* 181 */     this.thrownProxy = thrownProxy;
/* 182 */     this.contextMap = (contextMap == null) ? ThreadContext.EMPTY_MAP : contextMap;
/* 183 */     this.contextStack = (contextStack == null) ? (ThreadContext.ContextStack)ThreadContext.EMPTY_STACK : contextStack;
/* 184 */     this.timeMillis = (message instanceof TimestampMessage) ? ((TimestampMessage)message).getTimestamp() : timestamp;
/* 185 */     this.threadName = threadName;
/* 186 */     this.source = source;
/* 187 */     if (message != null && message instanceof LoggerNameAwareMessage) {
/* 188 */       ((LoggerNameAwareMessage)message).setLoggerName(loggerName);
/*     */     }
/*     */   }
/*     */   
/*     */   private static Map<String, String> createMap(List<Property> properties) {
/* 193 */     Map<String, String> contextMap = ThreadContext.getImmutableContext();
/* 194 */     if (contextMap == null && (properties == null || properties.isEmpty())) {
/* 195 */       return null;
/*     */     }
/* 197 */     if (properties == null || properties.isEmpty()) {
/* 198 */       return contextMap;
/*     */     }
/* 200 */     Map<String, String> map = new HashMap<String, String>(contextMap);
/*     */     
/* 202 */     for (Property prop : properties) {
/* 203 */       if (!map.containsKey(prop.getName())) {
/* 204 */         map.put(prop.getName(), prop.getValue());
/*     */       }
/*     */     } 
/* 207 */     return Collections.unmodifiableMap(map);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Level getLevel() {
/* 216 */     return this.level;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getLoggerName() {
/* 225 */     return this.loggerName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Message getMessage() {
/* 234 */     return this.message;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getThreadName() {
/* 243 */     if (this.threadName == null) {
/* 244 */       this.threadName = Thread.currentThread().getName();
/*     */     }
/* 246 */     return this.threadName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getTimeMillis() {
/* 255 */     return this.timeMillis;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Throwable getThrown() {
/* 264 */     return this.thrown;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ThrowableProxy getThrownProxy() {
/* 273 */     if (this.thrownProxy == null && this.thrown != null) {
/* 274 */       this.thrownProxy = new ThrowableProxy(this.thrown);
/*     */     }
/* 276 */     return this.thrownProxy;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Marker getMarker() {
/* 286 */     return this.marker;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getLoggerFqcn() {
/* 295 */     return this.loggerFqcn;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, String> getContextMap() {
/* 304 */     return this.contextMap;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ThreadContext.ContextStack getContextStack() {
/* 313 */     return this.contextStack;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StackTraceElement getSource() {
/* 323 */     if (this.source != null) {
/* 324 */       return this.source;
/*     */     }
/* 326 */     if (this.loggerFqcn == null || !this.includeLocation) {
/* 327 */       return null;
/*     */     }
/* 329 */     this.source = calcLocation(this.loggerFqcn);
/* 330 */     return this.source;
/*     */   }
/*     */   
/*     */   public static StackTraceElement calcLocation(String fqcnOfLogger) {
/* 334 */     if (fqcnOfLogger == null) {
/* 335 */       return null;
/*     */     }
/* 337 */     StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
/* 338 */     StackTraceElement last = null;
/* 339 */     for (int i = stackTrace.length - 1; i > 0; i--) {
/* 340 */       String className = stackTrace[i].getClassName();
/* 341 */       if (fqcnOfLogger.equals(className)) {
/* 342 */         return last;
/*     */       }
/* 344 */       last = stackTrace[i];
/*     */     } 
/* 346 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isIncludeLocation() {
/* 351 */     return this.includeLocation;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setIncludeLocation(boolean includeLocation) {
/* 356 */     this.includeLocation = includeLocation;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEndOfBatch() {
/* 361 */     return this.endOfBatch;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setEndOfBatch(boolean endOfBatch) {
/* 366 */     this.endOfBatch = endOfBatch;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object writeReplace() {
/* 374 */     getThrownProxy();
/* 375 */     return new LogEventProxy(this, this.includeLocation);
/*     */   }
/*     */ 
/*     */   
/*     */   public static Serializable serialize(Log4jLogEvent event, boolean includeLocation) {
/* 380 */     event.getThrownProxy();
/* 381 */     return new LogEventProxy(event, includeLocation);
/*     */   }
/*     */   
/*     */   public static boolean canDeserialize(Serializable event) {
/* 385 */     return event instanceof LogEventProxy;
/*     */   }
/*     */   
/*     */   public static Log4jLogEvent deserialize(Serializable event) {
/* 389 */     if (event == null) {
/* 390 */       throw new NullPointerException("Event cannot be null");
/*     */     }
/* 392 */     if (event instanceof LogEventProxy) {
/* 393 */       LogEventProxy proxy = (LogEventProxy)event;
/* 394 */       Log4jLogEvent result = new Log4jLogEvent(proxy.loggerName, proxy.marker, proxy.loggerFQCN, proxy.level, proxy.message, proxy.thrown, proxy.thrownProxy, proxy.contextMap, proxy.contextStack, proxy.threadName, proxy.source, proxy.timeMillis);
/*     */ 
/*     */ 
/*     */       
/* 398 */       result.setEndOfBatch(proxy.isEndOfBatch);
/* 399 */       result.setIncludeLocation(proxy.isLocationRequired);
/* 400 */       return result;
/*     */     } 
/* 402 */     throw new IllegalArgumentException("Event is not a serialized LogEvent: " + event.toString());
/*     */   }
/*     */   
/*     */   private void readObject(ObjectInputStream stream) throws InvalidObjectException {
/* 406 */     throw new InvalidObjectException("Proxy required");
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 411 */     StringBuilder sb = new StringBuilder();
/* 412 */     String n = this.loggerName.isEmpty() ? "root" : this.loggerName;
/* 413 */     sb.append("Logger=").append(n);
/* 414 */     sb.append(" Level=").append(this.level.name());
/* 415 */     sb.append(" Message=").append(this.message.getFormattedMessage());
/* 416 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 421 */     if (this == o) {
/* 422 */       return true;
/*     */     }
/* 424 */     if (o == null || getClass() != o.getClass()) {
/* 425 */       return false;
/*     */     }
/*     */     
/* 428 */     Log4jLogEvent that = (Log4jLogEvent)o;
/*     */     
/* 430 */     if (this.endOfBatch != that.endOfBatch) {
/* 431 */       return false;
/*     */     }
/* 433 */     if (this.includeLocation != that.includeLocation) {
/* 434 */       return false;
/*     */     }
/* 436 */     if (this.timeMillis != that.timeMillis) {
/* 437 */       return false;
/*     */     }
/* 439 */     if ((this.loggerFqcn != null) ? !this.loggerFqcn.equals(that.loggerFqcn) : (that.loggerFqcn != null)) {
/* 440 */       return false;
/*     */     }
/* 442 */     if ((this.level != null) ? !this.level.equals(that.level) : (that.level != null)) {
/* 443 */       return false;
/*     */     }
/* 445 */     if ((this.source != null) ? !this.source.equals(that.source) : (that.source != null)) {
/* 446 */       return false;
/*     */     }
/* 448 */     if ((this.marker != null) ? !this.marker.equals(that.marker) : (that.marker != null)) {
/* 449 */       return false;
/*     */     }
/* 451 */     if ((this.contextMap != null) ? !this.contextMap.equals(that.contextMap) : (that.contextMap != null)) {
/* 452 */       return false;
/*     */     }
/* 454 */     if (!this.message.equals(that.message)) {
/* 455 */       return false;
/*     */     }
/* 457 */     if (!this.loggerName.equals(that.loggerName)) {
/* 458 */       return false;
/*     */     }
/* 460 */     if ((this.contextStack != null) ? !this.contextStack.equals(that.contextStack) : (that.contextStack != null)) {
/* 461 */       return false;
/*     */     }
/* 463 */     if ((this.threadName != null) ? !this.threadName.equals(that.threadName) : (that.threadName != null)) {
/* 464 */       return false;
/*     */     }
/* 466 */     if ((this.thrown != null) ? !this.thrown.equals(that.thrown) : (that.thrown != null)) {
/* 467 */       return false;
/*     */     }
/* 469 */     if ((this.thrownProxy != null) ? !this.thrownProxy.equals(that.thrownProxy) : (that.thrownProxy != null)) {
/* 470 */       return false;
/*     */     }
/*     */     
/* 473 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 478 */     int result = (this.loggerFqcn != null) ? this.loggerFqcn.hashCode() : 0;
/* 479 */     result = 31 * result + ((this.marker != null) ? this.marker.hashCode() : 0);
/* 480 */     result = 31 * result + ((this.level != null) ? this.level.hashCode() : 0);
/* 481 */     result = 31 * result + this.loggerName.hashCode();
/* 482 */     result = 31 * result + this.message.hashCode();
/* 483 */     result = 31 * result + (int)(this.timeMillis ^ this.timeMillis >>> 32L);
/* 484 */     result = 31 * result + ((this.thrown != null) ? this.thrown.hashCode() : 0);
/* 485 */     result = 31 * result + ((this.thrownProxy != null) ? this.thrownProxy.hashCode() : 0);
/* 486 */     result = 31 * result + ((this.contextMap != null) ? this.contextMap.hashCode() : 0);
/* 487 */     result = 31 * result + ((this.contextStack != null) ? this.contextStack.hashCode() : 0);
/* 488 */     result = 31 * result + ((this.threadName != null) ? this.threadName.hashCode() : 0);
/* 489 */     result = 31 * result + ((this.source != null) ? this.source.hashCode() : 0);
/* 490 */     result = 31 * result + (this.includeLocation ? 1 : 0);
/* 491 */     result = 31 * result + (this.endOfBatch ? 1 : 0);
/* 492 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   private static class LogEventProxy
/*     */     implements Serializable
/*     */   {
/*     */     private static final long serialVersionUID = -7139032940312647146L;
/*     */     
/*     */     private final String loggerFQCN;
/*     */     private final Marker marker;
/*     */     private final Level level;
/*     */     private final String loggerName;
/*     */     private final Message message;
/*     */     private final long timeMillis;
/*     */     private final transient Throwable thrown;
/*     */     private final ThrowableProxy thrownProxy;
/*     */     private final Map<String, String> contextMap;
/*     */     private final ThreadContext.ContextStack contextStack;
/*     */     private final String threadName;
/*     */     private final StackTraceElement source;
/*     */     private final boolean isLocationRequired;
/*     */     private final boolean isEndOfBatch;
/*     */     
/*     */     public LogEventProxy(Log4jLogEvent event, boolean includeLocation) {
/* 517 */       this.loggerFQCN = event.loggerFqcn;
/* 518 */       this.marker = event.marker;
/* 519 */       this.level = event.level;
/* 520 */       this.loggerName = event.loggerName;
/* 521 */       this.message = event.message;
/* 522 */       this.timeMillis = event.timeMillis;
/* 523 */       this.thrown = event.thrown;
/* 524 */       this.thrownProxy = event.thrownProxy;
/* 525 */       this.contextMap = event.contextMap;
/* 526 */       this.contextStack = event.contextStack;
/* 527 */       this.source = includeLocation ? event.getSource() : null;
/* 528 */       this.threadName = event.getThreadName();
/* 529 */       this.isLocationRequired = includeLocation;
/* 530 */       this.isEndOfBatch = event.endOfBatch;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected Object readResolve() {
/* 538 */       Log4jLogEvent result = new Log4jLogEvent(this.loggerName, this.marker, this.loggerFQCN, this.level, this.message, this.thrown, this.thrownProxy, this.contextMap, this.contextStack, this.threadName, this.source, this.timeMillis);
/*     */       
/* 540 */       result.setEndOfBatch(this.isEndOfBatch);
/* 541 */       result.setIncludeLocation(this.isLocationRequired);
/* 542 */       return result;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\impl\Log4jLogEvent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */