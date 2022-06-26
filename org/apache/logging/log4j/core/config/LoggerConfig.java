/*     */ package org.apache.logging.log4j.core.config;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import java.util.concurrent.locks.Condition;
/*     */ import java.util.concurrent.locks.Lock;
/*     */ import java.util.concurrent.locks.ReentrantLock;
/*     */ import org.apache.logging.log4j.Level;
/*     */ import org.apache.logging.log4j.Marker;
/*     */ import org.apache.logging.log4j.core.Appender;
/*     */ import org.apache.logging.log4j.core.Filter;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.async.AsyncLoggerContextSelector;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginElement;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginFactory;
/*     */ import org.apache.logging.log4j.core.filter.AbstractFilterable;
/*     */ import org.apache.logging.log4j.core.impl.DefaultLogEventFactory;
/*     */ import org.apache.logging.log4j.core.impl.LogEventFactory;
/*     */ import org.apache.logging.log4j.core.util.Booleans;
/*     */ import org.apache.logging.log4j.core.util.Loader;
/*     */ import org.apache.logging.log4j.message.Message;
/*     */ import org.apache.logging.log4j.util.PropertiesUtil;
/*     */ import org.apache.logging.log4j.util.Strings;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Plugin(name = "logger", category = "Core", printObject = true)
/*     */ public class LoggerConfig
/*     */   extends AbstractFilterable
/*     */ {
/*     */   private static final int MAX_RETRIES = 3;
/*  65 */   private static LogEventFactory LOG_EVENT_FACTORY = null;
/*     */   
/*  67 */   private List<AppenderRef> appenderRefs = new ArrayList<AppenderRef>();
/*  68 */   private final Map<String, AppenderControl> appenders = new ConcurrentHashMap<String, AppenderControl>();
/*     */   private final String name;
/*     */   private LogEventFactory logEventFactory;
/*     */   private Level level;
/*     */   private boolean additive = true;
/*     */   private boolean includeLocation = true;
/*     */   private LoggerConfig parent;
/*  75 */   private final AtomicInteger counter = new AtomicInteger();
/*  76 */   private final AtomicBoolean shutdown = new AtomicBoolean(false);
/*     */   private final Map<Property, Boolean> properties;
/*     */   private final Configuration config;
/*  79 */   private final Lock shutdownLock = new ReentrantLock();
/*  80 */   private final Condition noLogEvents = this.shutdownLock.newCondition();
/*     */   
/*     */   static {
/*  83 */     String factory = PropertiesUtil.getProperties().getStringProperty("Log4jLogEventFactory");
/*  84 */     if (factory != null) {
/*     */       try {
/*  86 */         Class<?> clazz = Loader.loadClass(factory);
/*  87 */         if (clazz != null && LogEventFactory.class.isAssignableFrom(clazz)) {
/*  88 */           LOG_EVENT_FACTORY = (LogEventFactory)clazz.newInstance();
/*     */         }
/*  90 */       } catch (Exception ex) {
/*  91 */         LOGGER.error("Unable to create LogEventFactory {}", new Object[] { factory, ex });
/*     */       } 
/*     */     }
/*  94 */     if (LOG_EVENT_FACTORY == null) {
/*  95 */       LOG_EVENT_FACTORY = (LogEventFactory)new DefaultLogEventFactory();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LoggerConfig() {
/* 103 */     this.logEventFactory = LOG_EVENT_FACTORY;
/* 104 */     this.level = Level.ERROR;
/* 105 */     this.name = "";
/* 106 */     this.properties = null;
/* 107 */     this.config = null;
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
/*     */   public LoggerConfig(String name, Level level, boolean additive) {
/* 119 */     this.logEventFactory = LOG_EVENT_FACTORY;
/* 120 */     this.name = name;
/* 121 */     this.level = level;
/* 122 */     this.additive = additive;
/* 123 */     this.properties = null;
/* 124 */     this.config = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected LoggerConfig(String name, List<AppenderRef> appenders, Filter filter, Level level, boolean additive, Property[] properties, Configuration config, boolean includeLocation) {
/* 132 */     super(filter);
/* 133 */     this.logEventFactory = LOG_EVENT_FACTORY;
/* 134 */     this.name = name;
/* 135 */     this.appenderRefs = appenders;
/* 136 */     this.level = level;
/* 137 */     this.additive = additive;
/* 138 */     this.includeLocation = includeLocation;
/* 139 */     this.config = config;
/* 140 */     if (properties != null && properties.length > 0) {
/* 141 */       this.properties = new HashMap<Property, Boolean>(properties.length);
/* 142 */       for (Property prop : properties) {
/* 143 */         boolean interpolate = prop.getValue().contains("${");
/* 144 */         this.properties.put(prop, Boolean.valueOf(interpolate));
/*     */       } 
/*     */     } else {
/* 147 */       this.properties = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Filter getFilter() {
/* 153 */     return super.getFilter();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/* 162 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setParent(LoggerConfig parent) {
/* 171 */     this.parent = parent;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LoggerConfig getParent() {
/* 180 */     return this.parent;
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
/*     */   public void addAppender(Appender appender, Level level, Filter filter) {
/* 192 */     this.appenders.put(appender.getName(), new AppenderControl(appender, level, filter));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeAppender(String name) {
/* 202 */     AppenderControl ctl = this.appenders.remove(name);
/* 203 */     if (ctl != null) {
/* 204 */       cleanupFilter(ctl);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, Appender> getAppenders() {
/* 215 */     Map<String, Appender> map = new HashMap<String, Appender>();
/* 216 */     for (Map.Entry<String, AppenderControl> entry : this.appenders.entrySet())
/*     */     {
/* 218 */       map.put(entry.getKey(), ((AppenderControl)entry.getValue()).getAppender());
/*     */     }
/* 220 */     return map;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void clearAppenders() {
/* 227 */     waitForCompletion();
/* 228 */     Collection<AppenderControl> controls = this.appenders.values();
/* 229 */     Iterator<AppenderControl> iterator = controls.iterator();
/* 230 */     while (iterator.hasNext()) {
/* 231 */       AppenderControl ctl = iterator.next();
/* 232 */       iterator.remove();
/* 233 */       cleanupFilter(ctl);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void cleanupFilter(AppenderControl ctl) {
/* 238 */     Filter filter = ctl.getFilter();
/* 239 */     if (filter != null) {
/* 240 */       ctl.removeFilter(filter);
/* 241 */       filter.stop();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<AppenderRef> getAppenderRefs() {
/* 251 */     return this.appenderRefs;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLevel(Level level) {
/* 260 */     this.level = level;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Level getLevel() {
/* 269 */     return (this.level == null) ? this.parent.getLevel() : this.level;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LogEventFactory getLogEventFactory() {
/* 278 */     return this.logEventFactory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLogEventFactory(LogEventFactory logEventFactory) {
/* 288 */     this.logEventFactory = logEventFactory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isAdditive() {
/* 297 */     return this.additive;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAdditive(boolean additive) {
/* 307 */     this.additive = additive;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isIncludeLocation() {
/* 318 */     return this.includeLocation;
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
/*     */   public Map<Property, Boolean> getProperties() {
/* 336 */     return (this.properties == null) ? null : Collections.<Property, Boolean>unmodifiableMap(this.properties);
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
/*     */   public void log(String loggerName, String fqcn, Marker marker, Level level, Message data, Throwable t) {
/* 353 */     List<Property> props = null;
/* 354 */     if (this.properties != null) {
/* 355 */       props = new ArrayList<Property>(this.properties.size());
/*     */       
/* 357 */       for (Map.Entry<Property, Boolean> entry : this.properties.entrySet()) {
/* 358 */         Property prop = entry.getKey();
/* 359 */         String value = ((Boolean)entry.getValue()).booleanValue() ? this.config.getStrSubstitutor().replace(prop.getValue()) : prop.getValue();
/*     */         
/* 361 */         props.add(Property.createProperty(prop.getName(), value));
/*     */       } 
/*     */     } 
/* 364 */     LogEvent event = this.logEventFactory.createEvent(loggerName, marker, fqcn, level, data, props, t);
/* 365 */     log(event);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void waitForCompletion() {
/* 373 */     this.shutdownLock.lock();
/*     */     try {
/* 375 */       if (this.shutdown.compareAndSet(false, true)) {
/* 376 */         int retries = 0;
/* 377 */         while (this.counter.get() > 0) {
/*     */           try {
/* 379 */             this.noLogEvents.await((retries + 1), TimeUnit.SECONDS);
/* 380 */           } catch (InterruptedException ie) {
/* 381 */             if (++retries > 3) {
/*     */               break;
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } finally {
/* 388 */       this.shutdownLock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void log(LogEvent event) {
/* 399 */     this.counter.incrementAndGet();
/*     */     try {
/* 401 */       if (isFiltered(event)) {
/*     */         return;
/*     */       }
/*     */       
/* 405 */       event.setIncludeLocation(isIncludeLocation());
/*     */       
/* 407 */       callAppenders(event);
/*     */       
/* 409 */       if (this.additive && this.parent != null) {
/* 410 */         this.parent.log(event);
/*     */       }
/*     */     } finally {
/* 413 */       if (this.counter.decrementAndGet() == 0) {
/* 414 */         this.shutdownLock.lock();
/*     */         try {
/* 416 */           if (this.shutdown.get()) {
/* 417 */             this.noLogEvents.signalAll();
/*     */           }
/*     */         } finally {
/* 420 */           this.shutdownLock.unlock();
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void callAppenders(LogEvent event) {
/* 427 */     for (AppenderControl control : this.appenders.values()) {
/* 428 */       control.callAppender(event);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 435 */     return Strings.isEmpty(this.name) ? "root" : this.name;
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
/*     */   @PluginFactory
/*     */   public static LoggerConfig createLogger(@PluginAttribute("additivity") String additivity, @PluginAttribute("level") Level level, @PluginAttribute("name") String loggerName, @PluginAttribute("includeLocation") String includeLocation, @PluginElement("AppenderRef") AppenderRef[] refs, @PluginElement("Properties") Property[] properties, @PluginConfiguration Configuration config, @PluginElement("Filter") Filter filter) {
/* 461 */     if (loggerName == null) {
/* 462 */       LOGGER.error("Loggers cannot be configured without a name");
/* 463 */       return null;
/*     */     } 
/*     */     
/* 466 */     List<AppenderRef> appenderRefs = Arrays.asList(refs);
/* 467 */     String name = loggerName.equals("root") ? "" : loggerName;
/* 468 */     boolean additive = Booleans.parseBoolean(additivity, true);
/*     */     
/* 470 */     return new LoggerConfig(name, appenderRefs, filter, level, additive, properties, config, includeLocation(includeLocation));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static boolean includeLocation(String includeLocationConfigValue) {
/* 477 */     if (includeLocationConfigValue == null) {
/* 478 */       boolean sync = !AsyncLoggerContextSelector.class.getName().equals(System.getProperty("Log4jContextSelector"));
/*     */       
/* 480 */       return sync;
/*     */     } 
/* 482 */     return Boolean.parseBoolean(includeLocationConfigValue);
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
/*     */   @Plugin(name = "root", category = "Core", printObject = true)
/*     */   public static class RootLogger
/*     */     extends LoggerConfig
/*     */   {
/*     */     @PluginFactory
/*     */     public static LoggerConfig createLogger(@PluginAttribute("additivity") String additivity, @PluginAttribute("level") Level level, @PluginAttribute("includeLocation") String includeLocation, @PluginElement("AppenderRef") AppenderRef[] refs, @PluginElement("Properties") Property[] properties, @PluginConfiguration Configuration config, @PluginElement("Filter") Filter filter) {
/* 500 */       List<AppenderRef> appenderRefs = Arrays.asList(refs);
/* 501 */       Level actualLevel = (level == null) ? Level.ERROR : level;
/* 502 */       boolean additive = Booleans.parseBoolean(additivity, true);
/*     */       
/* 504 */       return new LoggerConfig("", appenderRefs, filter, actualLevel, additive, properties, config, includeLocation(includeLocation));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\config\LoggerConfig.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */