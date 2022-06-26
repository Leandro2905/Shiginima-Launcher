/*     */ package org.apache.logging.log4j.core.jmx;
/*     */ 
/*     */ import java.lang.management.ManagementFactory;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.Executors;
/*     */ import javax.management.InstanceAlreadyExistsException;
/*     */ import javax.management.MBeanRegistrationException;
/*     */ import javax.management.MBeanServer;
/*     */ import javax.management.NotCompliantMBeanException;
/*     */ import javax.management.ObjectName;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.core.Appender;
/*     */ import org.apache.logging.log4j.core.LoggerContext;
/*     */ import org.apache.logging.log4j.core.appender.AsyncAppender;
/*     */ import org.apache.logging.log4j.core.async.AsyncLogger;
/*     */ import org.apache.logging.log4j.core.async.AsyncLoggerConfig;
/*     */ import org.apache.logging.log4j.core.config.LoggerConfig;
/*     */ import org.apache.logging.log4j.core.impl.Log4jContextFactory;
/*     */ import org.apache.logging.log4j.core.selector.ContextSelector;
/*     */ import org.apache.logging.log4j.spi.LoggerContextFactory;
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
/*     */ public final class Server
/*     */ {
/*     */   public static final String DOMAIN = "org.apache.logging.log4j2";
/*     */   private static final String PROPERTY_DISABLE_JMX = "log4j2.disable.jmx";
/*  59 */   private static final StatusLogger LOGGER = StatusLogger.getLogger();
/*  60 */   static final Executor executor = Executors.newFixedThreadPool(1);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String escape(String name) {
/*  75 */     StringBuilder sb = new StringBuilder(name.length() * 2);
/*  76 */     boolean needsQuotes = false;
/*  77 */     for (int i = 0; i < name.length(); i++) {
/*  78 */       char c = name.charAt(i);
/*  79 */       switch (c) {
/*     */         
/*     */         case '"':
/*     */         case '*':
/*     */         case '?':
/*     */         case '\\':
/*  85 */           sb.append('\\');
/*  86 */           needsQuotes = true;
/*     */ 
/*     */         
/*     */         case ',':
/*     */         case ':':
/*     */         case '=':
/*  92 */           needsQuotes = true;
/*     */ 
/*     */         
/*     */         case '\r':
/*     */           break;
/*     */         
/*     */         case '\n':
/*  99 */           sb.append("\\n");
/* 100 */           needsQuotes = true;
/*     */           break;
/*     */         default:
/* 103 */           sb.append(c); break;
/*     */       } 
/* 105 */     }  if (needsQuotes) {
/* 106 */       sb.insert(0, '"');
/* 107 */       sb.append('"');
/*     */     } 
/* 109 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public static void reregisterMBeansAfterReconfigure() {
/* 114 */     if (Boolean.getBoolean("log4j2.disable.jmx")) {
/* 115 */       LOGGER.debug("JMX disabled for log4j2. Not registering MBeans.");
/*     */       return;
/*     */     } 
/* 118 */     MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
/* 119 */     reregisterMBeansAfterReconfigure(mbs);
/*     */   }
/*     */   
/*     */   public static void reregisterMBeansAfterReconfigure(MBeanServer mbs) {
/* 123 */     if (Boolean.getBoolean("log4j2.disable.jmx")) {
/* 124 */       LOGGER.debug("JMX disabled for log4j2. Not registering MBeans.");
/*     */ 
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/*     */     try {
/* 131 */       ContextSelector selector = getContextSelector();
/* 132 */       if (selector == null) {
/* 133 */         LOGGER.debug("Could not register MBeans: no ContextSelector found.");
/*     */         return;
/*     */       } 
/* 136 */       List<LoggerContext> contexts = selector.getLoggerContexts();
/* 137 */       for (LoggerContext ctx : contexts) {
/*     */ 
/*     */         
/* 140 */         unregisterLoggerContext(ctx.getName(), mbs);
/*     */         
/* 142 */         LoggerContextAdmin mbean = new LoggerContextAdmin(ctx, executor);
/* 143 */         register(mbs, mbean, mbean.getObjectName());
/*     */         
/* 145 */         if (ctx instanceof org.apache.logging.log4j.core.async.AsyncLoggerContext) {
/* 146 */           RingBufferAdmin rbmbean = AsyncLogger.createRingBufferAdmin(ctx.getName());
/* 147 */           register(mbs, rbmbean, rbmbean.getObjectName());
/*     */         } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 155 */         registerStatusLogger(ctx.getName(), mbs, executor);
/* 156 */         registerContextSelector(ctx.getName(), selector, mbs, executor);
/*     */         
/* 158 */         registerLoggerConfigs(ctx, mbs, executor);
/* 159 */         registerAppenders(ctx, mbs, executor);
/*     */       } 
/* 161 */     } catch (Exception ex) {
/* 162 */       LOGGER.error("Could not register mbeans", ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void unregisterMBeans() {
/* 170 */     MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
/* 171 */     unregisterMBeans(mbs);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void unregisterMBeans(MBeanServer mbs) {
/* 180 */     unregisterStatusLogger("*", mbs);
/* 181 */     unregisterContextSelector("*", mbs);
/* 182 */     unregisterContexts(mbs);
/* 183 */     unregisterLoggerConfigs("*", mbs);
/* 184 */     unregisterAsyncLoggerRingBufferAdmins("*", mbs);
/* 185 */     unregisterAsyncLoggerConfigRingBufferAdmins("*", mbs);
/* 186 */     unregisterAppenders("*", mbs);
/* 187 */     unregisterAsyncAppenders("*", mbs);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static ContextSelector getContextSelector() {
/* 198 */     LoggerContextFactory factory = LogManager.getFactory();
/* 199 */     if (factory instanceof Log4jContextFactory) {
/* 200 */       ContextSelector selector = ((Log4jContextFactory)factory).getSelector();
/* 201 */       return selector;
/*     */     } 
/* 203 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void unregisterLoggerContext(String loggerContextName) {
/* 214 */     MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
/* 215 */     unregisterLoggerContext(loggerContextName, mbs);
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
/*     */   public static void unregisterLoggerContext(String contextName, MBeanServer mbs) {
/* 227 */     String pattern = "org.apache.logging.log4j2:type=%s";
/* 228 */     String search = String.format("org.apache.logging.log4j2:type=%s", new Object[] { escape(contextName), "*" });
/* 229 */     unregisterAllMatching(search, mbs);
/*     */ 
/*     */     
/* 232 */     unregisterStatusLogger(contextName, mbs);
/* 233 */     unregisterContextSelector(contextName, mbs);
/* 234 */     unregisterLoggerConfigs(contextName, mbs);
/* 235 */     unregisterAppenders(contextName, mbs);
/* 236 */     unregisterAsyncAppenders(contextName, mbs);
/* 237 */     unregisterAsyncLoggerRingBufferAdmins(contextName, mbs);
/* 238 */     unregisterAsyncLoggerConfigRingBufferAdmins(contextName, mbs);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void registerStatusLogger(String contextName, MBeanServer mbs, Executor executor) throws InstanceAlreadyExistsException, MBeanRegistrationException, NotCompliantMBeanException {
/* 244 */     StatusLoggerAdmin mbean = new StatusLoggerAdmin(contextName, executor);
/* 245 */     register(mbs, mbean, mbean.getObjectName());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void registerContextSelector(String contextName, ContextSelector selector, MBeanServer mbs, Executor executor) throws InstanceAlreadyExistsException, MBeanRegistrationException, NotCompliantMBeanException {
/* 252 */     ContextSelectorAdmin mbean = new ContextSelectorAdmin(contextName, selector);
/* 253 */     register(mbs, mbean, mbean.getObjectName());
/*     */   }
/*     */   
/*     */   private static void unregisterStatusLogger(String contextName, MBeanServer mbs) {
/* 257 */     String pattern = "org.apache.logging.log4j2:type=%s,component=StatusLogger";
/* 258 */     String search = String.format("org.apache.logging.log4j2:type=%s,component=StatusLogger", new Object[] { escape(contextName), "*" });
/* 259 */     unregisterAllMatching(search, mbs);
/*     */   }
/*     */   
/*     */   private static void unregisterContextSelector(String contextName, MBeanServer mbs) {
/* 263 */     String pattern = "org.apache.logging.log4j2:type=%s,component=ContextSelector";
/* 264 */     String search = String.format("org.apache.logging.log4j2:type=%s,component=ContextSelector", new Object[] { escape(contextName), "*" });
/* 265 */     unregisterAllMatching(search, mbs);
/*     */   }
/*     */   
/*     */   private static void unregisterLoggerConfigs(String contextName, MBeanServer mbs) {
/* 269 */     String pattern = "org.apache.logging.log4j2:type=%s,component=Loggers,name=%s";
/* 270 */     String search = String.format("org.apache.logging.log4j2:type=%s,component=Loggers,name=%s", new Object[] { escape(contextName), "*" });
/* 271 */     unregisterAllMatching(search, mbs);
/*     */   }
/*     */   
/*     */   private static void unregisterContexts(MBeanServer mbs) {
/* 275 */     String pattern = "org.apache.logging.log4j2:type=%s";
/* 276 */     String search = String.format("org.apache.logging.log4j2:type=%s", new Object[] { "*" });
/* 277 */     unregisterAllMatching(search, mbs);
/*     */   }
/*     */   
/*     */   private static void unregisterAppenders(String contextName, MBeanServer mbs) {
/* 281 */     String pattern = "org.apache.logging.log4j2:type=%s,component=Appenders,name=%s";
/* 282 */     String search = String.format("org.apache.logging.log4j2:type=%s,component=Appenders,name=%s", new Object[] { escape(contextName), "*" });
/* 283 */     unregisterAllMatching(search, mbs);
/*     */   }
/*     */   
/*     */   private static void unregisterAsyncAppenders(String contextName, MBeanServer mbs) {
/* 287 */     String pattern = "org.apache.logging.log4j2:type=%s,component=AsyncAppenders,name=%s";
/* 288 */     String search = String.format("org.apache.logging.log4j2:type=%s,component=AsyncAppenders,name=%s", new Object[] { escape(contextName), "*" });
/* 289 */     unregisterAllMatching(search, mbs);
/*     */   }
/*     */   
/*     */   private static void unregisterAsyncLoggerRingBufferAdmins(String contextName, MBeanServer mbs) {
/* 293 */     String pattern1 = "org.apache.logging.log4j2:type=%s,component=AsyncLoggerRingBuffer";
/* 294 */     String search1 = String.format("org.apache.logging.log4j2:type=%s,component=AsyncLoggerRingBuffer", new Object[] { escape(contextName) });
/* 295 */     unregisterAllMatching(search1, mbs);
/*     */   }
/*     */   
/*     */   private static void unregisterAsyncLoggerConfigRingBufferAdmins(String contextName, MBeanServer mbs) {
/* 299 */     String pattern2 = "org.apache.logging.log4j2:type=%s,component=Loggers,name=%s,subtype=RingBuffer";
/* 300 */     String search2 = String.format("org.apache.logging.log4j2:type=%s,component=Loggers,name=%s,subtype=RingBuffer", new Object[] { escape(contextName), "*" });
/* 301 */     unregisterAllMatching(search2, mbs);
/*     */   }
/*     */   
/*     */   private static void unregisterAllMatching(String search, MBeanServer mbs) {
/*     */     try {
/* 306 */       ObjectName pattern = new ObjectName(search);
/* 307 */       Set<ObjectName> found = mbs.queryNames(pattern, null);
/* 308 */       for (ObjectName objectName : found) {
/* 309 */         LOGGER.debug("Unregistering MBean {}", new Object[] { objectName });
/* 310 */         mbs.unregisterMBean(objectName);
/*     */       } 
/* 312 */     } catch (Exception ex) {
/* 313 */       LOGGER.error("Could not unregister MBeans for " + search, ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void registerLoggerConfigs(LoggerContext ctx, MBeanServer mbs, Executor executor) throws InstanceAlreadyExistsException, MBeanRegistrationException, NotCompliantMBeanException {
/* 320 */     Map<String, LoggerConfig> map = ctx.getConfiguration().getLoggers();
/* 321 */     for (String name : map.keySet()) {
/* 322 */       LoggerConfig cfg = map.get(name);
/* 323 */       LoggerConfigAdmin mbean = new LoggerConfigAdmin(ctx, cfg);
/* 324 */       register(mbs, mbean, mbean.getObjectName());
/*     */       
/* 326 */       if (cfg instanceof AsyncLoggerConfig) {
/* 327 */         AsyncLoggerConfig async = (AsyncLoggerConfig)cfg;
/* 328 */         RingBufferAdmin rbmbean = async.createRingBufferAdmin(ctx.getName());
/* 329 */         register(mbs, rbmbean, rbmbean.getObjectName());
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void registerAppenders(LoggerContext ctx, MBeanServer mbs, Executor executor) throws InstanceAlreadyExistsException, MBeanRegistrationException, NotCompliantMBeanException {
/* 337 */     Map<String, Appender> map = ctx.getConfiguration().getAppenders();
/* 338 */     for (String name : map.keySet()) {
/* 339 */       Appender appender = map.get(name);
/*     */       
/* 341 */       if (appender instanceof AsyncAppender) {
/* 342 */         AsyncAppender async = (AsyncAppender)appender;
/* 343 */         AsyncAppenderAdmin asyncAppenderAdmin = new AsyncAppenderAdmin(ctx.getName(), async);
/* 344 */         register(mbs, asyncAppenderAdmin, asyncAppenderAdmin.getObjectName()); continue;
/*     */       } 
/* 346 */       AppenderAdmin mbean = new AppenderAdmin(ctx.getName(), appender);
/* 347 */       register(mbs, mbean, mbean.getObjectName());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void register(MBeanServer mbs, Object mbean, ObjectName objectName) throws InstanceAlreadyExistsException, MBeanRegistrationException, NotCompliantMBeanException {
/* 354 */     LOGGER.debug("Registering MBean {}", new Object[] { objectName });
/* 355 */     mbs.registerMBean(mbean, objectName);
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\jmx\Server.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */