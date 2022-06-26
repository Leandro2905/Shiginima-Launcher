/*     */ package org.apache.logging.log4j.core.async;
/*     */ 
/*     */ import com.lmax.disruptor.BlockingWaitStrategy;
/*     */ import com.lmax.disruptor.EventFactory;
/*     */ import com.lmax.disruptor.EventHandler;
/*     */ import com.lmax.disruptor.EventTranslatorTwoArg;
/*     */ import com.lmax.disruptor.ExceptionHandler;
/*     */ import com.lmax.disruptor.RingBuffer;
/*     */ import com.lmax.disruptor.Sequence;
/*     */ import com.lmax.disruptor.SequenceReportingEventHandler;
/*     */ import com.lmax.disruptor.SleepingWaitStrategy;
/*     */ import com.lmax.disruptor.WaitStrategy;
/*     */ import com.lmax.disruptor.YieldingWaitStrategy;
/*     */ import com.lmax.disruptor.dsl.Disruptor;
/*     */ import com.lmax.disruptor.dsl.ProducerType;
/*     */ import com.lmax.disruptor.util.Util;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.Executors;
/*     */ import java.util.concurrent.ThreadFactory;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.jmx.RingBufferAdmin;
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
/*     */ class AsyncLoggerConfigHelper
/*     */ {
/*     */   private static final int MAX_DRAIN_ATTEMPTS_BEFORE_SHUTDOWN = 200;
/*     */   private static final int SLEEP_MILLIS_BETWEEN_DRAIN_ATTEMPTS = 50;
/*     */   private static final int RINGBUFFER_MIN_SIZE = 128;
/*     */   private static final int RINGBUFFER_DEFAULT_SIZE = 262144;
/*  65 */   private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*     */   
/*  67 */   private static ThreadFactory threadFactory = new DaemonThreadFactory("AsyncLoggerConfig-");
/*     */   
/*     */   private static volatile Disruptor<Log4jEventWrapper> disruptor;
/*     */   private static ExecutorService executor;
/*  71 */   private static volatile int count = 0;
/*  72 */   private static ThreadLocal<Boolean> isAppenderThread = new ThreadLocal<Boolean>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  78 */   private static final EventFactory<Log4jEventWrapper> FACTORY = new EventFactory<Log4jEventWrapper>()
/*     */     {
/*     */       public AsyncLoggerConfigHelper.Log4jEventWrapper newInstance() {
/*  81 */         return new AsyncLoggerConfigHelper.Log4jEventWrapper();
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  88 */   private final EventTranslatorTwoArg<Log4jEventWrapper, LogEvent, AsyncLoggerConfig> translator = new EventTranslatorTwoArg<Log4jEventWrapper, LogEvent, AsyncLoggerConfig>()
/*     */     {
/*     */ 
/*     */       
/*     */       public void translateTo(AsyncLoggerConfigHelper.Log4jEventWrapper ringBufferElement, long sequence, LogEvent logEvent, AsyncLoggerConfig loggerConfig)
/*     */       {
/*  94 */         ringBufferElement.event = logEvent;
/*  95 */         ringBufferElement.loggerConfig = loggerConfig;
/*     */       }
/*     */     };
/*     */   
/*     */   private final AsyncLoggerConfig asyncLoggerConfig;
/*     */   
/*     */   public AsyncLoggerConfigHelper(AsyncLoggerConfig asyncLoggerConfig) {
/* 102 */     this.asyncLoggerConfig = asyncLoggerConfig;
/* 103 */     claim();
/*     */   }
/*     */   
/*     */   private static synchronized void initDisruptor() {
/* 107 */     if (disruptor != null) {
/* 108 */       LOGGER.trace("AsyncLoggerConfigHelper not starting new disruptor, using existing object. Ref count is {}.", new Object[] { Integer.valueOf(count) });
/*     */       return;
/*     */     } 
/* 111 */     LOGGER.trace("AsyncLoggerConfigHelper creating new disruptor. Ref count is {}.", new Object[] { Integer.valueOf(count) });
/* 112 */     int ringBufferSize = calculateRingBufferSize();
/* 113 */     WaitStrategy waitStrategy = createWaitStrategy();
/* 114 */     executor = Executors.newSingleThreadExecutor(threadFactory);
/* 115 */     initThreadLocalForExecutorThread();
/* 116 */     disruptor = new Disruptor(FACTORY, ringBufferSize, executor, ProducerType.MULTI, waitStrategy);
/*     */     
/* 118 */     Log4jEventWrapperHandler[] arrayOfLog4jEventWrapperHandler = { new Log4jEventWrapperHandler() };
/*     */     
/* 120 */     ExceptionHandler errorHandler = getExceptionHandler();
/* 121 */     disruptor.handleExceptionsWith(errorHandler);
/* 122 */     disruptor.handleEventsWith((EventHandler[])arrayOfLog4jEventWrapperHandler);
/*     */     
/* 124 */     LOGGER.debug("Starting AsyncLoggerConfig disruptor with ringbuffer size={}, waitStrategy={}, exceptionHandler={}...", new Object[] { Integer.valueOf(disruptor.getRingBuffer().getBufferSize()), waitStrategy.getClass().getSimpleName(), errorHandler });
/*     */ 
/*     */     
/* 127 */     disruptor.start();
/*     */   }
/*     */   
/*     */   private static WaitStrategy createWaitStrategy() {
/* 131 */     String strategy = System.getProperty("AsyncLoggerConfig.WaitStrategy");
/*     */     
/* 133 */     LOGGER.debug("property AsyncLoggerConfig.WaitStrategy={}", new Object[] { strategy });
/* 134 */     if ("Sleep".equals(strategy))
/* 135 */       return (WaitStrategy)new SleepingWaitStrategy(); 
/* 136 */     if ("Yield".equals(strategy))
/* 137 */       return (WaitStrategy)new YieldingWaitStrategy(); 
/* 138 */     if ("Block".equals(strategy)) {
/* 139 */       return (WaitStrategy)new BlockingWaitStrategy();
/*     */     }
/* 141 */     LOGGER.debug("disruptor event handler uses BlockingWaitStrategy");
/* 142 */     return (WaitStrategy)new BlockingWaitStrategy();
/*     */   }
/*     */   
/*     */   private static int calculateRingBufferSize() {
/* 146 */     int ringBufferSize = 262144;
/* 147 */     String userPreferredRBSize = System.getProperty("AsyncLoggerConfig.RingBufferSize", String.valueOf(ringBufferSize));
/*     */ 
/*     */     
/*     */     try {
/* 151 */       int size = Integer.parseInt(userPreferredRBSize);
/* 152 */       if (size < 128) {
/* 153 */         size = 128;
/* 154 */         LOGGER.warn("Invalid RingBufferSize {}, using minimum size {}.", new Object[] { userPreferredRBSize, Integer.valueOf(128) });
/*     */       } 
/*     */ 
/*     */       
/* 158 */       ringBufferSize = size;
/* 159 */     } catch (Exception ex) {
/* 160 */       LOGGER.warn("Invalid RingBufferSize {}, using default size {}.", new Object[] { userPreferredRBSize, Integer.valueOf(ringBufferSize) });
/*     */     } 
/*     */     
/* 163 */     return Util.ceilingNextPowerOfTwo(ringBufferSize);
/*     */   }
/*     */   
/*     */   private static ExceptionHandler getExceptionHandler() {
/* 167 */     String cls = System.getProperty("AsyncLoggerConfig.ExceptionHandler");
/*     */     
/* 169 */     if (cls == null) {
/* 170 */       return null;
/*     */     }
/*     */     
/*     */     try {
/* 174 */       Class<? extends ExceptionHandler> klass = (Class)Class.forName(cls);
/*     */       
/* 176 */       ExceptionHandler result = klass.newInstance();
/* 177 */       return result;
/* 178 */     } catch (Exception ignored) {
/* 179 */       LOGGER.debug("AsyncLoggerConfig.ExceptionHandler not set: error creating " + cls + ": ", ignored);
/*     */ 
/*     */       
/* 182 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class Log4jEventWrapper
/*     */   {
/*     */     private AsyncLoggerConfig loggerConfig;
/*     */     
/*     */     private LogEvent event;
/*     */ 
/*     */     
/*     */     private Log4jEventWrapper() {}
/*     */ 
/*     */     
/*     */     public void clear() {
/* 199 */       this.loggerConfig = null;
/* 200 */       this.event = null;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static class Log4jEventWrapperHandler
/*     */     implements SequenceReportingEventHandler<Log4jEventWrapper>
/*     */   {
/*     */     private static final int NOTIFY_PROGRESS_THRESHOLD = 50;
/*     */     private Sequence sequenceCallback;
/*     */     private int counter;
/*     */     
/*     */     private Log4jEventWrapperHandler() {}
/*     */     
/*     */     public void setSequenceCallback(Sequence sequenceCallback) {
/* 215 */       this.sequenceCallback = sequenceCallback;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void onEvent(AsyncLoggerConfigHelper.Log4jEventWrapper event, long sequence, boolean endOfBatch) throws Exception {
/* 221 */       event.event.setEndOfBatch(endOfBatch);
/* 222 */       event.loggerConfig.asyncCallAppenders(event.event);
/* 223 */       event.clear();
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 228 */       if (++this.counter > 50) {
/* 229 */         this.sequenceCallback.set(sequence);
/* 230 */         this.counter = 0;
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static synchronized void claim() {
/* 242 */     count++;
/* 243 */     initDisruptor();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static synchronized void release() {
/* 252 */     if (--count > 0) {
/* 253 */       LOGGER.trace("AsyncLoggerConfigHelper: not shutting down disruptor: ref count is {}.", new Object[] { Integer.valueOf(count) });
/*     */       return;
/*     */     } 
/* 256 */     Disruptor<Log4jEventWrapper> temp = disruptor;
/* 257 */     if (temp == null) {
/* 258 */       LOGGER.trace("AsyncLoggerConfigHelper: disruptor already shut down: ref count is {}. (Resetting to zero.)", new Object[] { Integer.valueOf(count) });
/*     */       
/* 260 */       count = 0;
/*     */       return;
/*     */     } 
/* 263 */     LOGGER.trace("AsyncLoggerConfigHelper: shutting down disruptor: ref count is {}.", new Object[] { Integer.valueOf(count) });
/*     */ 
/*     */ 
/*     */     
/* 267 */     disruptor = null;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 272 */     for (int i = 0; hasBacklog(temp) && i < 200; i++) {
/*     */       try {
/* 274 */         Thread.sleep(50L);
/* 275 */       } catch (InterruptedException e) {}
/*     */     } 
/*     */     
/* 278 */     temp.shutdown();
/* 279 */     executor.shutdown();
/* 280 */     executor = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean hasBacklog(Disruptor<?> disruptor) {
/* 287 */     RingBuffer<?> ringBuffer = disruptor.getRingBuffer();
/* 288 */     return !ringBuffer.hasAvailableCapacity(ringBuffer.getBufferSize());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void initThreadLocalForExecutorThread() {
/* 297 */     executor.submit(new Runnable()
/*     */         {
/*     */           public void run() {
/* 300 */             AsyncLoggerConfigHelper.isAppenderThread.set(Boolean.TRUE);
/*     */           }
/*     */         });
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
/*     */   public boolean callAppendersFromAnotherThread(LogEvent event) {
/* 319 */     Disruptor<Log4jEventWrapper> temp = disruptor;
/* 320 */     if (temp == null) {
/* 321 */       LOGGER.fatal("Ignoring log event after log4j was shut down");
/* 322 */       return true;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 327 */     if (isAppenderThread.get() == Boolean.TRUE && temp.getRingBuffer().remainingCapacity() == 0L)
/*     */     {
/*     */ 
/*     */       
/* 331 */       return false;
/*     */     }
/*     */     
/*     */     try {
/* 335 */       LogEvent logEvent = event;
/* 336 */       if (event instanceof RingBufferLogEvent) {
/* 337 */         logEvent = ((RingBufferLogEvent)event).createMemento();
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 342 */       disruptor.getRingBuffer().publishEvent(this.translator, logEvent, this.asyncLoggerConfig);
/* 343 */     } catch (NullPointerException npe) {
/* 344 */       LOGGER.fatal("Ignoring log event after log4j was shut down.");
/*     */     } 
/* 346 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RingBufferAdmin createRingBufferAdmin(String contextName, String loggerConfigName) {
/* 357 */     return RingBufferAdmin.forAsyncLoggerConfig(disruptor.getRingBuffer(), contextName, loggerConfigName);
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\async\AsyncLoggerConfigHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */