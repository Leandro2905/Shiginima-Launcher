/*     */ package org.apache.logging.log4j.core;
/*     */ 
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.io.File;
/*     */ import java.lang.ref.Reference;
/*     */ import java.lang.ref.SoftReference;
/*     */ import java.net.URI;
/*     */ import java.util.Collection;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import java.util.concurrent.CopyOnWriteArrayList;
/*     */ import java.util.concurrent.locks.Lock;
/*     */ import java.util.concurrent.locks.ReentrantLock;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Marker;
/*     */ import org.apache.logging.log4j.MarkerManager;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.config.ConfigurationFactory;
/*     */ import org.apache.logging.log4j.core.config.ConfigurationListener;
/*     */ import org.apache.logging.log4j.core.config.DefaultConfiguration;
/*     */ import org.apache.logging.log4j.core.config.NullConfiguration;
/*     */ import org.apache.logging.log4j.core.config.Reconfigurable;
/*     */ import org.apache.logging.log4j.core.jmx.Server;
/*     */ import org.apache.logging.log4j.core.util.Assert;
/*     */ import org.apache.logging.log4j.core.util.NetUtils;
/*     */ import org.apache.logging.log4j.message.MessageFactory;
/*     */ import org.apache.logging.log4j.spi.AbstractLogger;
/*     */ import org.apache.logging.log4j.spi.ExtendedLogger;
/*     */ import org.apache.logging.log4j.spi.LoggerContext;
/*     */ import org.apache.logging.log4j.util.PropertiesUtil;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LoggerContext
/*     */   extends AbstractLifeCycle
/*     */   implements LoggerContext, ConfigurationListener
/*     */ {
/*  58 */   private static final boolean SHUTDOWN_HOOK_ENABLED = PropertiesUtil.getProperties().getBooleanProperty("log4j.shutdownHookEnabled", true);
/*     */   
/*     */   public static final String PROPERTY_CONFIG = "config";
/*     */   
/*  62 */   private static final Marker SHUTDOWN_HOOK = MarkerManager.getMarker("SHUTDOWN HOOK");
/*  63 */   private static final Configuration NULL_CONFIGURATION = (Configuration)new NullConfiguration();
/*     */   
/*  65 */   private final ConcurrentMap<String, Logger> loggers = new ConcurrentHashMap<String, Logger>();
/*  66 */   private final CopyOnWriteArrayList<PropertyChangeListener> propertyChangeListeners = new CopyOnWriteArrayList<PropertyChangeListener>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  72 */   private volatile Configuration config = (Configuration)new DefaultConfiguration();
/*     */ 
/*     */   
/*     */   private Object externalContext;
/*     */ 
/*     */   
/*     */   private final String name;
/*     */   
/*     */   private URI configLocation;
/*     */   
/*     */   private Reference<Thread> shutdownThread;
/*     */   
/*  84 */   private final Lock configLock = new ReentrantLock();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LoggerContext(String name) {
/*  91 */     this(name, (Object)null, (URI)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LoggerContext(String name, Object externalContext) {
/* 100 */     this(name, externalContext, (URI)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LoggerContext(String name, Object externalContext, URI configLocn) {
/* 110 */     this.name = name;
/* 111 */     this.externalContext = externalContext;
/* 112 */     this.configLocation = configLocn;
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
/*     */   public LoggerContext(String name, Object externalContext, String configLocn) {
/* 124 */     this.name = name;
/* 125 */     this.externalContext = externalContext;
/* 126 */     if (configLocn != null) {
/*     */       URI uRI;
/*     */       try {
/* 129 */         uRI = (new File(configLocn)).toURI();
/* 130 */       } catch (Exception ex) {
/* 131 */         uRI = null;
/*     */       } 
/* 133 */       this.configLocation = uRI;
/*     */     } else {
/* 135 */       this.configLocation = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void start() {
/* 141 */     LOGGER.debug("Starting LoggerContext[name={}, {}]...", new Object[] { getName(), this });
/* 142 */     if (this.configLock.tryLock()) {
/*     */       try {
/* 144 */         if (isInitialized() || isStopped()) {
/* 145 */           setStarting();
/* 146 */           reconfigure();
/* 147 */           setUpShutdownHook();
/* 148 */           setStarted();
/*     */         } 
/*     */       } finally {
/* 151 */         this.configLock.unlock();
/*     */       } 
/*     */     }
/* 154 */     LOGGER.debug("LoggerContext[name={}, {}] started OK.", new Object[] { getName(), this });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void start(Configuration config) {
/* 162 */     LOGGER.debug("Starting LoggerContext[name={}, {}] with configuration {}...", new Object[] { getName(), this, config });
/* 163 */     if (this.configLock.tryLock()) {
/*     */       try {
/* 165 */         if (isInitialized() || isStopped()) {
/* 166 */           setUpShutdownHook();
/* 167 */           setStarted();
/*     */         } 
/*     */       } finally {
/* 170 */         this.configLock.unlock();
/*     */       } 
/*     */     }
/* 173 */     setConfiguration(config);
/* 174 */     LOGGER.debug("LoggerContext[name={}, {}] started OK with configuration {}.", new Object[] { getName(), this, config });
/*     */   }
/*     */   
/*     */   private void setUpShutdownHook() {
/* 178 */     if (this.config.isShutdownHookEnabled() && SHUTDOWN_HOOK_ENABLED) {
/* 179 */       LOGGER.debug(SHUTDOWN_HOOK, "Shutdown hook enabled. Registering a new one.");
/* 180 */       this.shutdownThread = new SoftReference<Thread>(new Thread(new ShutdownThread(this), "log4j-shutdown"));
/*     */ 
/*     */       
/* 183 */       addShutdownHook();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void addShutdownHook() {
/* 188 */     Thread hook = getShutdownThread();
/* 189 */     if (hook != null) {
/*     */       try {
/* 191 */         Runtime.getRuntime().addShutdownHook(hook);
/* 192 */       } catch (IllegalStateException ise) {
/* 193 */         LOGGER.warn(SHUTDOWN_HOOK, "Unable to register shutdown hook due to JVM state");
/* 194 */       } catch (SecurityException se) {
/* 195 */         LOGGER.warn(SHUTDOWN_HOOK, "Unable to register shutdown hook due to security restrictions");
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private Thread getShutdownThread() {
/* 201 */     return (this.shutdownThread == null) ? null : this.shutdownThread.get();
/*     */   }
/*     */ 
/*     */   
/*     */   public void stop() {
/* 206 */     LOGGER.debug("Stopping LoggerContext[name={}, {}]...", new Object[] { getName(), this });
/* 207 */     this.configLock.lock();
/*     */     try {
/* 209 */       if (isStopped()) {
/*     */         return;
/*     */       }
/* 212 */       setStopping();
/* 213 */       tearDownShutdownHook();
/* 214 */       Configuration prev = this.config;
/* 215 */       this.config = NULL_CONFIGURATION;
/* 216 */       updateLoggers();
/* 217 */       prev.stop();
/* 218 */       this.externalContext = null;
/* 219 */       LogManager.getFactory().removeContext(this);
/* 220 */       setStopped();
/*     */     } finally {
/* 222 */       this.configLock.unlock();
/*     */ 
/*     */       
/* 225 */       Server.unregisterLoggerContext(getName());
/*     */     } 
/* 227 */     LOGGER.debug("Stopped LoggerContext[name={}, {}]...", new Object[] { getName(), this });
/*     */   }
/*     */   
/*     */   private void tearDownShutdownHook() {
/* 231 */     if (this.shutdownThread != null) {
/* 232 */       LOGGER.debug(SHUTDOWN_HOOK, "Enqueue shutdown hook for garbage collection.");
/* 233 */       this.shutdownThread.enqueue();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/* 243 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setExternalContext(Object context) {
/* 251 */     this.externalContext = context;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getExternalContext() {
/* 260 */     return this.externalContext;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Logger getLogger(String name) {
/* 270 */     return getLogger(name, (MessageFactory)null);
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
/*     */   public Collection<Logger> getLoggers() {
/* 283 */     return this.loggers.values();
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
/*     */   public Logger getLogger(String name, MessageFactory messageFactory) {
/* 296 */     Logger logger = this.loggers.get(name);
/* 297 */     if (logger != null) {
/* 298 */       AbstractLogger.checkMessageFactory((ExtendedLogger)logger, messageFactory);
/* 299 */       return logger;
/*     */     } 
/*     */     
/* 302 */     logger = newInstance(this, name, messageFactory);
/* 303 */     Logger prev = this.loggers.putIfAbsent(name, logger);
/* 304 */     return (prev == null) ? logger : prev;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasLogger(String name) {
/* 314 */     return this.loggers.containsKey(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Configuration getConfiguration() {
/* 324 */     return this.config;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addFilter(Filter filter) {
/* 333 */     this.config.addFilter(filter);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeFilter(Filter filter) {
/* 341 */     this.config.removeFilter(filter);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private synchronized Configuration setConfiguration(Configuration config) {
/* 350 */     if (config == null) {
/* 351 */       throw new NullPointerException("No Configuration was provided");
/*     */     }
/* 353 */     Configuration prev = this.config;
/* 354 */     config.addListener(this);
/* 355 */     ConcurrentMap<String, String> map = (ConcurrentMap<String, String>)config.getComponent("ContextProperties");
/*     */     
/*     */     try {
/* 358 */       map.putIfAbsent("hostName", NetUtils.getLocalHostname());
/* 359 */     } catch (Exception ex) {
/* 360 */       LOGGER.debug("Ignoring {}, setting hostName to 'unknown'", new Object[] { ex.toString() });
/* 361 */       map.putIfAbsent("hostName", "unknown");
/*     */     } 
/* 363 */     map.putIfAbsent("contextName", this.name);
/* 364 */     config.start();
/* 365 */     this.config = config;
/* 366 */     updateLoggers();
/* 367 */     if (prev != null) {
/* 368 */       prev.removeListener(this);
/* 369 */       prev.stop();
/*     */     } 
/*     */     
/* 372 */     firePropertyChangeEvent(new PropertyChangeEvent(this, "config", prev, config));
/*     */     
/*     */     try {
/* 375 */       Server.reregisterMBeansAfterReconfigure();
/* 376 */     } catch (Throwable t) {
/* 377 */       LOGGER.error("Could not reconfigure JMX", t);
/*     */     } 
/* 379 */     return prev;
/*     */   }
/*     */   
/*     */   private void firePropertyChangeEvent(PropertyChangeEvent event) {
/* 383 */     for (PropertyChangeListener listener : this.propertyChangeListeners) {
/* 384 */       listener.propertyChange(event);
/*     */     }
/*     */   }
/*     */   
/*     */   public void addPropertyChangeListener(PropertyChangeListener listener) {
/* 389 */     this.propertyChangeListeners.add(Assert.requireNonNull(listener, "listener"));
/*     */   }
/*     */   
/*     */   public void removePropertyChangeListener(PropertyChangeListener listener) {
/* 393 */     this.propertyChangeListeners.remove(listener);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized URI getConfigLocation() {
/* 404 */     return this.configLocation;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void setConfigLocation(URI configLocation) {
/* 412 */     this.configLocation = configLocation;
/* 413 */     reconfigure();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void reconfigure() {
/* 420 */     LOGGER.debug("Reconfiguration started for context[name={}] at {} ({})", new Object[] { this.name, this.configLocation, this });
/* 421 */     Configuration instance = ConfigurationFactory.getInstance().getConfiguration(this.name, this.configLocation);
/* 422 */     setConfiguration(instance);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 428 */     LOGGER.debug("Reconfiguration complete for context[name={}] at {} ({})", new Object[] { this.name, this.configLocation, this });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateLoggers() {
/* 435 */     updateLoggers(this.config);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateLoggers(Configuration config) {
/* 443 */     for (Logger logger : this.loggers.values()) {
/* 444 */       logger.updateConfiguration(config);
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
/*     */   public synchronized void onChange(Reconfigurable reconfigurable) {
/* 456 */     LOGGER.debug("Reconfiguration started for context {} ({})", new Object[] { this.name, this });
/* 457 */     Configuration config = reconfigurable.reconfigure();
/* 458 */     if (config != null) {
/* 459 */       setConfiguration(config);
/* 460 */       LOGGER.debug("Reconfiguration completed for {} ({})", new Object[] { this.name, this });
/*     */     } else {
/* 462 */       LOGGER.debug("Reconfiguration failed for {} ({})", new Object[] { this.name, this });
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected Logger newInstance(LoggerContext ctx, String name, MessageFactory messageFactory) {
/* 468 */     return new Logger(ctx, name, messageFactory);
/*     */   }
/*     */   
/*     */   private static class ShutdownThread
/*     */     implements Runnable {
/*     */     private final LoggerContext context;
/*     */     
/*     */     public ShutdownThread(LoggerContext context) {
/* 476 */       this.context = context;
/*     */     }
/*     */ 
/*     */     
/*     */     public void run() {
/* 481 */       AbstractLifeCycle.LOGGER.debug("ShutdownThread stopping LoggerContext[name={}, {}]...", new Object[] { this.context.getName(), this.context });
/* 482 */       this.context.stop();
/* 483 */       AbstractLifeCycle.LOGGER.debug("ShutdownThread stopped LoggerContext[name={}, {}].", new Object[] { this.context.getName(), this.context });
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\LoggerContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */