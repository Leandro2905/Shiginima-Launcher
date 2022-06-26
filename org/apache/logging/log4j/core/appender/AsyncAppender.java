/*     */ package org.apache.logging.log4j.core.appender;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ArrayBlockingQueue;
/*     */ import java.util.concurrent.BlockingQueue;
/*     */ import java.util.concurrent.atomic.AtomicLong;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.core.Appender;
/*     */ import org.apache.logging.log4j.core.Filter;
/*     */ import org.apache.logging.log4j.core.Layout;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.async.RingBufferLogEvent;
/*     */ import org.apache.logging.log4j.core.config.AppenderControl;
/*     */ import org.apache.logging.log4j.core.config.AppenderRef;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.config.ConfigurationException;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginAliases;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginElement;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginFactory;
/*     */ import org.apache.logging.log4j.core.impl.Log4jLogEvent;
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
/*     */ @Plugin(name = "Async", category = "Core", elementType = "appender", printObject = true)
/*     */ public final class AsyncAppender
/*     */   extends AbstractAppender
/*     */ {
/*     */   private static final int DEFAULT_QUEUE_SIZE = 128;
/*     */   private static final String SHUTDOWN = "Shutdown";
/*     */   private final BlockingQueue<Serializable> queue;
/*     */   private final int queueSize;
/*     */   private final boolean blocking;
/*     */   private final Configuration config;
/*     */   private final AppenderRef[] appenderRefs;
/*     */   private final String errorRef;
/*     */   private final boolean includeLocation;
/*     */   private AppenderControl errorAppender;
/*     */   private AsyncThread thread;
/*  64 */   private static final AtomicLong threadSequence = new AtomicLong(1L);
/*  65 */   private static ThreadLocal<Boolean> isAppenderThread = new ThreadLocal<Boolean>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private AsyncAppender(String name, Filter filter, AppenderRef[] appenderRefs, String errorRef, int queueSize, boolean blocking, boolean ignoreExceptions, Configuration config, boolean includeLocation) {
/*  72 */     super(name, filter, (Layout<? extends Serializable>)null, ignoreExceptions);
/*  73 */     this.queue = new ArrayBlockingQueue<Serializable>(queueSize);
/*  74 */     this.queueSize = queueSize;
/*  75 */     this.blocking = blocking;
/*  76 */     this.config = config;
/*  77 */     this.appenderRefs = appenderRefs;
/*  78 */     this.errorRef = errorRef;
/*  79 */     this.includeLocation = includeLocation;
/*     */   }
/*     */ 
/*     */   
/*     */   public void start() {
/*  84 */     Map<String, Appender> map = this.config.getAppenders();
/*  85 */     List<AppenderControl> appenders = new ArrayList<AppenderControl>();
/*  86 */     for (AppenderRef appenderRef : this.appenderRefs) {
/*  87 */       if (map.containsKey(appenderRef.getRef())) {
/*  88 */         appenders.add(new AppenderControl(map.get(appenderRef.getRef()), appenderRef.getLevel(), appenderRef.getFilter()));
/*     */       } else {
/*     */         
/*  91 */         LOGGER.error("No appender named {} was configured", new Object[] { appenderRef });
/*     */       } 
/*     */     } 
/*  94 */     if (this.errorRef != null) {
/*  95 */       if (map.containsKey(this.errorRef)) {
/*  96 */         this.errorAppender = new AppenderControl(map.get(this.errorRef), null, null);
/*     */       } else {
/*  98 */         LOGGER.error("Unable to set up error Appender. No appender named {} was configured", new Object[] { this.errorRef });
/*     */       } 
/*     */     }
/* 101 */     if (appenders.size() > 0) {
/* 102 */       this.thread = new AsyncThread(appenders, this.queue);
/* 103 */       this.thread.setName("AsyncAppender-" + getName());
/* 104 */     } else if (this.errorRef == null) {
/* 105 */       throw new ConfigurationException("No appenders are available for AsyncAppender " + getName());
/*     */     } 
/*     */     
/* 108 */     this.thread.start();
/* 109 */     super.start();
/*     */   }
/*     */ 
/*     */   
/*     */   public void stop() {
/* 114 */     super.stop();
/* 115 */     LOGGER.trace("AsyncAppender stopping. Queue still has {} events.", new Object[] { Integer.valueOf(this.queue.size()) });
/* 116 */     this.thread.shutdown();
/*     */     try {
/* 118 */       this.thread.join();
/* 119 */     } catch (InterruptedException ex) {
/* 120 */       LOGGER.warn("Interrupted while stopping AsyncAppender {}", new Object[] { getName() });
/*     */     } 
/* 122 */     LOGGER.trace("AsyncAppender stopped. Queue has {} events.", new Object[] { Integer.valueOf(this.queue.size()) });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void append(LogEvent logEvent) {
/* 132 */     if (!isStarted()) {
/* 133 */       throw new IllegalStateException("AsyncAppender " + getName() + " is not active");
/*     */     }
/* 135 */     if (!(logEvent instanceof Log4jLogEvent)) {
/* 136 */       if (!(logEvent instanceof RingBufferLogEvent)) {
/*     */         return;
/*     */       }
/* 139 */       logEvent = ((RingBufferLogEvent)logEvent).createMemento();
/*     */     } 
/* 141 */     Log4jLogEvent coreEvent = (Log4jLogEvent)logEvent;
/* 142 */     boolean appendSuccessful = false;
/* 143 */     if (this.blocking) {
/* 144 */       if (isAppenderThread.get() == Boolean.TRUE && this.queue.remainingCapacity() == 0) {
/*     */ 
/*     */         
/* 147 */         coreEvent.setEndOfBatch(false);
/* 148 */         appendSuccessful = this.thread.callAppenders(coreEvent);
/*     */       } else {
/*     */         
/*     */         try {
/* 152 */           this.queue.put(Log4jLogEvent.serialize(coreEvent, this.includeLocation));
/* 153 */           appendSuccessful = true;
/* 154 */         } catch (InterruptedException e) {
/* 155 */           LOGGER.warn("Interrupted while waiting for a free slot in the AsyncAppender LogEvent-queue {}", new Object[] { getName() });
/*     */         } 
/*     */       } 
/*     */     } else {
/*     */       
/* 160 */       appendSuccessful = this.queue.offer(Log4jLogEvent.serialize(coreEvent, this.includeLocation));
/* 161 */       if (!appendSuccessful) {
/* 162 */         error("Appender " + getName() + " is unable to write primary appenders. queue is full");
/*     */       }
/*     */     } 
/* 165 */     if (!appendSuccessful && this.errorAppender != null) {
/* 166 */       this.errorAppender.callAppender((LogEvent)coreEvent);
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
/*     */   public static AsyncAppender createAppender(@PluginElement("AppenderRef") AppenderRef[] appenderRefs, @PluginAttribute("errorRef") @PluginAliases({"error-ref"}) String errorRef, @PluginAttribute(value = "blocking", defaultBoolean = true) boolean blocking, @PluginAttribute(value = "bufferSize", defaultInt = 128) int size, @PluginAttribute("name") String name, @PluginAttribute(value = "includeLocation", defaultBoolean = false) boolean includeLocation, @PluginElement("Filter") Filter filter, @PluginConfiguration Configuration config, @PluginAttribute(value = "ignoreExceptions", defaultBoolean = true) boolean ignoreExceptions) {
/* 194 */     if (name == null) {
/* 195 */       LOGGER.error("No name provided for AsyncAppender");
/* 196 */       return null;
/*     */     } 
/* 198 */     if (appenderRefs == null) {
/* 199 */       LOGGER.error("No appender references provided to AsyncAppender {}", new Object[] { name });
/*     */     }
/*     */     
/* 202 */     return new AsyncAppender(name, filter, appenderRefs, errorRef, size, blocking, ignoreExceptions, config, includeLocation);
/*     */   }
/*     */ 
/*     */   
/*     */   private class AsyncThread
/*     */     extends Thread
/*     */   {
/*     */     private volatile boolean shutdown = false;
/*     */     
/*     */     private final List<AppenderControl> appenders;
/*     */     
/*     */     private final BlockingQueue<Serializable> queue;
/*     */     
/*     */     public AsyncThread(List<AppenderControl> appenders, BlockingQueue<Serializable> queue) {
/* 216 */       this.appenders = appenders;
/* 217 */       this.queue = queue;
/* 218 */       setDaemon(true);
/* 219 */       setName("AsyncAppenderThread" + AsyncAppender.threadSequence.getAndIncrement());
/*     */     }
/*     */ 
/*     */     
/*     */     public void run() {
/* 224 */       AsyncAppender.isAppenderThread.set(Boolean.TRUE);
/* 225 */       while (!this.shutdown) {
/*     */         Serializable s;
/*     */         try {
/* 228 */           s = this.queue.take();
/* 229 */           if (s != null && s instanceof String && "Shutdown".equals(s.toString())) {
/* 230 */             this.shutdown = true;
/*     */             continue;
/*     */           } 
/* 233 */         } catch (InterruptedException ex) {
/*     */           continue;
/*     */         } 
/*     */         
/* 237 */         Log4jLogEvent event = Log4jLogEvent.deserialize(s);
/* 238 */         event.setEndOfBatch(this.queue.isEmpty());
/* 239 */         boolean success = callAppenders(event);
/* 240 */         if (!success && AsyncAppender.this.errorAppender != null) {
/*     */           try {
/* 242 */             AsyncAppender.this.errorAppender.callAppender((LogEvent)event);
/* 243 */           } catch (Exception ex) {}
/*     */         }
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 249 */       AsyncAppender.LOGGER.trace("AsyncAppender.AsyncThread shutting down. Processing remaining {} queue events.", new Object[] { Integer.valueOf(this.queue.size()) });
/*     */       
/* 251 */       int count = 0;
/* 252 */       int ignored = 0;
/* 253 */       while (!this.queue.isEmpty()) {
/*     */         try {
/* 255 */           Serializable s = this.queue.take();
/* 256 */           if (Log4jLogEvent.canDeserialize(s)) {
/* 257 */             Log4jLogEvent event = Log4jLogEvent.deserialize(s);
/* 258 */             event.setEndOfBatch(this.queue.isEmpty());
/* 259 */             callAppenders(event);
/* 260 */             count++; continue;
/*     */           } 
/* 262 */           ignored++;
/* 263 */           AsyncAppender.LOGGER.trace("Ignoring event of class {}", new Object[] { s.getClass().getName() });
/*     */         }
/* 265 */         catch (InterruptedException ex) {}
/*     */       } 
/*     */ 
/*     */       
/* 269 */       AsyncAppender.LOGGER.trace("AsyncAppender.AsyncThread stopped. Queue has {} events remaining. Processed {} and ignored {} events since shutdown started.", new Object[] { Integer.valueOf(this.queue.size()), Integer.valueOf(count), Integer.valueOf(ignored) });
/*     */     }
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
/*     */     boolean callAppenders(Log4jLogEvent event) {
/* 284 */       boolean success = false;
/* 285 */       for (AppenderControl control : this.appenders) {
/*     */         try {
/* 287 */           control.callAppender((LogEvent)event);
/* 288 */           success = true;
/* 289 */         } catch (Exception ex) {}
/*     */       } 
/*     */ 
/*     */       
/* 293 */       return success;
/*     */     }
/*     */     
/*     */     public void shutdown() {
/* 297 */       this.shutdown = true;
/* 298 */       if (this.queue.isEmpty()) {
/* 299 */         this.queue.offer("Shutdown");
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getAppenderRefStrings() {
/* 310 */     String[] result = new String[this.appenderRefs.length];
/* 311 */     for (int i = 0; i < result.length; i++) {
/* 312 */       result[i] = this.appenderRefs[i].getRef();
/*     */     }
/* 314 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isIncludeLocation() {
/* 324 */     return this.includeLocation;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isBlocking() {
/* 333 */     return this.blocking;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getErrorRef() {
/* 341 */     return this.errorRef;
/*     */   }
/*     */   
/*     */   public int getQueueCapacity() {
/* 345 */     return this.queueSize;
/*     */   }
/*     */   
/*     */   public int getQueueRemainingCapacity() {
/* 349 */     return this.queue.remainingCapacity();
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\appender\AsyncAppender.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */