/*     */ package org.apache.logging.log4j.core.async;
/*     */ 
/*     */ import com.lmax.disruptor.BlockingWaitStrategy;
/*     */ import com.lmax.disruptor.EventHandler;
/*     */ import com.lmax.disruptor.ExceptionHandler;
/*     */ import com.lmax.disruptor.RingBuffer;
/*     */ import com.lmax.disruptor.SleepingWaitStrategy;
/*     */ import com.lmax.disruptor.WaitStrategy;
/*     */ import com.lmax.disruptor.YieldingWaitStrategy;
/*     */ import com.lmax.disruptor.dsl.Disruptor;
/*     */ import com.lmax.disruptor.dsl.ProducerType;
/*     */ import com.lmax.disruptor.util.Util;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.Executors;
/*     */ import org.apache.logging.log4j.Level;
/*     */ import org.apache.logging.log4j.Marker;
/*     */ import org.apache.logging.log4j.ThreadContext;
/*     */ import org.apache.logging.log4j.core.Logger;
/*     */ import org.apache.logging.log4j.core.LoggerContext;
/*     */ import org.apache.logging.log4j.core.config.Property;
/*     */ import org.apache.logging.log4j.core.impl.Log4jLogEvent;
/*     */ import org.apache.logging.log4j.core.jmx.RingBufferAdmin;
/*     */ import org.apache.logging.log4j.core.util.Clock;
/*     */ import org.apache.logging.log4j.core.util.ClockFactory;
/*     */ import org.apache.logging.log4j.core.util.Loader;
/*     */ import org.apache.logging.log4j.message.Message;
/*     */ import org.apache.logging.log4j.message.MessageFactory;
/*     */ import org.apache.logging.log4j.message.TimestampMessage;
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
/*     */ public class AsyncLogger
/*     */   extends Logger
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   private static final int SLEEP_MILLIS_BETWEEN_DRAIN_ATTEMPTS = 50;
/*     */   private static final int MAX_DRAIN_ATTEMPTS_BEFORE_SHUTDOWN = 200;
/*     */   private static final int RINGBUFFER_MIN_SIZE = 128;
/*     */   private static final int RINGBUFFER_DEFAULT_SIZE = 262144;
/*  83 */   private static final StatusLogger LOGGER = StatusLogger.getLogger();
/*  84 */   private static final ThreadNameStrategy THREAD_NAME_STRATEGY = ThreadNameStrategy.create(); private static volatile Disruptor<RingBufferLogEvent> disruptor;
/*  85 */   private static final ThreadLocal<Info> threadlocalInfo = new ThreadLocal<Info>();
/*     */   
/*     */   enum ThreadNameStrategy {
/*  88 */     CACHED
/*     */     {
/*     */       public String getThreadName(AsyncLogger.Info info) {
/*  91 */         return info.cachedThreadName;
/*     */       }
/*     */     },
/*  94 */     UNCACHED
/*     */     {
/*     */       public String getThreadName(AsyncLogger.Info info) {
/*  97 */         return Thread.currentThread().getName();
/*     */       }
/*     */     };
/*     */ 
/*     */     
/*     */     static ThreadNameStrategy create() {
/* 103 */       String name = System.getProperty("AsyncLogger.ThreadNameStrategy", CACHED.name());
/*     */       try {
/* 105 */         return valueOf(name);
/* 106 */       } catch (Exception ex) {
/* 107 */         AsyncLogger.LOGGER.debug("Using AsyncLogger.ThreadNameStrategy.CACHED: '{}' not valid: {}", new Object[] { name, ex.toString() });
/* 108 */         return CACHED;
/*     */       } 
/*     */     }
/*     */     
/*     */     abstract String getThreadName(AsyncLogger.Info param1Info); }
/* 113 */   private static final Clock clock = ClockFactory.getClock();
/*     */   
/* 115 */   private static final ExecutorService executor = Executors.newSingleThreadExecutor(new DaemonThreadFactory("AsyncLogger-"));
/*     */ 
/*     */   
/*     */   static {
/* 119 */     initInfoForExecutorThread();
/* 120 */     LOGGER.debug("AsyncLogger.ThreadNameStrategy={}", new Object[] { THREAD_NAME_STRATEGY });
/* 121 */     int ringBufferSize = calculateRingBufferSize();
/*     */     
/* 123 */     WaitStrategy waitStrategy = createWaitStrategy();
/* 124 */     disruptor = new Disruptor(RingBufferLogEvent.FACTORY, ringBufferSize, executor, ProducerType.MULTI, waitStrategy);
/*     */     
/* 126 */     disruptor.handleExceptionsWith(getExceptionHandler());
/* 127 */     disruptor.handleEventsWith(new EventHandler[] { (EventHandler)new RingBufferLogEventHandler() });
/*     */     
/* 129 */     LOGGER.debug("Starting AsyncLogger disruptor with ringbuffer size {}...", new Object[] { Integer.valueOf(disruptor.getRingBuffer().getBufferSize()) });
/*     */     
/* 131 */     disruptor.start();
/*     */   }
/*     */   
/*     */   private static int calculateRingBufferSize() {
/* 135 */     int ringBufferSize = 262144;
/* 136 */     String userPreferredRBSize = System.getProperty("AsyncLogger.RingBufferSize", String.valueOf(ringBufferSize));
/*     */     
/*     */     try {
/* 139 */       int size = Integer.parseInt(userPreferredRBSize);
/* 140 */       if (size < 128) {
/* 141 */         size = 128;
/* 142 */         LOGGER.warn("Invalid RingBufferSize {}, using minimum size {}.", new Object[] { userPreferredRBSize, Integer.valueOf(128) });
/*     */       } 
/*     */       
/* 145 */       ringBufferSize = size;
/* 146 */     } catch (Exception ex) {
/* 147 */       LOGGER.warn("Invalid RingBufferSize {}, using default size {}.", new Object[] { userPreferredRBSize, Integer.valueOf(ringBufferSize) });
/*     */     } 
/* 149 */     return Util.ceilingNextPowerOfTwo(ringBufferSize);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void initInfoForExecutorThread() {
/* 160 */     executor.submit(new Runnable()
/*     */         {
/*     */           public void run() {
/* 163 */             boolean isAppenderThread = true;
/* 164 */             AsyncLogger.Info info = new AsyncLogger.Info(new RingBufferLogEventTranslator(), Thread.currentThread().getName(), true);
/*     */             
/* 166 */             AsyncLogger.threadlocalInfo.set(info);
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   private static WaitStrategy createWaitStrategy() {
/* 172 */     String strategy = System.getProperty("AsyncLogger.WaitStrategy");
/* 173 */     LOGGER.debug("property AsyncLogger.WaitStrategy={}", new Object[] { strategy });
/* 174 */     if ("Sleep".equals(strategy))
/* 175 */       return (WaitStrategy)new SleepingWaitStrategy(); 
/* 176 */     if ("Yield".equals(strategy))
/* 177 */       return (WaitStrategy)new YieldingWaitStrategy(); 
/* 178 */     if ("Block".equals(strategy)) {
/* 179 */       return (WaitStrategy)new BlockingWaitStrategy();
/*     */     }
/* 181 */     LOGGER.debug("disruptor event handler uses BlockingWaitStrategy");
/* 182 */     return (WaitStrategy)new BlockingWaitStrategy();
/*     */   }
/*     */   
/*     */   private static ExceptionHandler getExceptionHandler() {
/* 186 */     String cls = System.getProperty("AsyncLogger.ExceptionHandler");
/* 187 */     if (cls == null) {
/* 188 */       LOGGER.debug("No AsyncLogger.ExceptionHandler specified");
/* 189 */       return null;
/*     */     } 
/*     */     try {
/* 192 */       ExceptionHandler result = (ExceptionHandler)Loader.newCheckedInstanceOf(cls, ExceptionHandler.class);
/* 193 */       LOGGER.debug("AsyncLogger.ExceptionHandler={}", new Object[] { result });
/* 194 */       return result;
/* 195 */     } catch (Exception ignored) {
/* 196 */       LOGGER.debug("AsyncLogger.ExceptionHandler not set: error creating " + cls + ": ", ignored);
/* 197 */       return null;
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
/*     */   public AsyncLogger(LoggerContext context, String name, MessageFactory messageFactory) {
/* 210 */     super(context, name, messageFactory);
/*     */   }
/*     */ 
/*     */   
/*     */   static class Info
/*     */   {
/*     */     private final RingBufferLogEventTranslator translator;
/*     */     private final String cachedThreadName;
/*     */     private final boolean isAppenderThread;
/*     */     
/*     */     public Info(RingBufferLogEventTranslator translator, String threadName, boolean appenderThread) {
/* 221 */       this.translator = translator;
/* 222 */       this.cachedThreadName = threadName;
/* 223 */       this.isAppenderThread = appenderThread;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void logMessage(String fqcn, Level level, Marker marker, Message message, Throwable thrown) {
/* 229 */     Info info = threadlocalInfo.get();
/* 230 */     if (info == null) {
/* 231 */       info = new Info(new RingBufferLogEventTranslator(), Thread.currentThread().getName(), false);
/* 232 */       threadlocalInfo.set(info);
/*     */     } 
/*     */     
/* 235 */     Disruptor<RingBufferLogEvent> temp = disruptor;
/* 236 */     if (temp == null) {
/* 237 */       LOGGER.fatal("Ignoring log event after log4j was shut down");
/*     */ 
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 243 */     if (info.isAppenderThread && temp.getRingBuffer().remainingCapacity() == 0L) {
/*     */       
/* 245 */       this.config.loggerConfig.log(getName(), fqcn, marker, level, message, thrown);
/*     */       return;
/*     */     } 
/* 248 */     boolean includeLocation = this.config.loggerConfig.isIncludeLocation();
/* 249 */     info.translator.setValues(this, getName(), marker, fqcn, level, message, thrown, ThreadContext.getImmutableContext(), ThreadContext.getImmutableStack(), THREAD_NAME_STRATEGY.getThreadName(info), includeLocation ? location(fqcn) : null, (message instanceof TimestampMessage) ? ((TimestampMessage)message).getTimestamp() : clock.currentTimeMillis());
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 283 */       disruptor.publishEvent(info.translator);
/* 284 */     } catch (NullPointerException npe) {
/* 285 */       LOGGER.fatal("Ignoring log event after log4j was shut down.");
/*     */     } 
/*     */   }
/*     */   
/*     */   private static StackTraceElement location(String fqcnOfLogger) {
/* 290 */     return Log4jLogEvent.calcLocation(fqcnOfLogger);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void actualAsyncLog(RingBufferLogEvent event) {
/* 300 */     Map<Property, Boolean> properties = this.config.loggerConfig.getProperties();
/* 301 */     event.mergePropertiesIntoContextMap(properties, this.config.config.getStrSubstitutor());
/* 302 */     this.config.logEvent(event);
/*     */   }
/*     */   
/*     */   public static void stop() {
/* 306 */     Disruptor<RingBufferLogEvent> temp = disruptor;
/*     */ 
/*     */ 
/*     */     
/* 310 */     disruptor = null;
/* 311 */     if (temp == null) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 318 */     for (int i = 0; hasBacklog(temp) && i < 200; i++) {
/*     */       try {
/* 320 */         Thread.sleep(50L);
/* 321 */       } catch (InterruptedException e) {}
/*     */     } 
/*     */     
/* 324 */     temp.shutdown();
/* 325 */     executor.shutdown();
/* 326 */     threadlocalInfo.remove();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean hasBacklog(Disruptor<?> disruptor) {
/* 333 */     RingBuffer<?> ringBuffer = disruptor.getRingBuffer();
/* 334 */     return !ringBuffer.hasAvailableCapacity(ringBuffer.getBufferSize());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static RingBufferAdmin createRingBufferAdmin(String contextName) {
/* 344 */     return RingBufferAdmin.forAsyncLogger(disruptor.getRingBuffer(), contextName);
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\async\AsyncLogger.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */