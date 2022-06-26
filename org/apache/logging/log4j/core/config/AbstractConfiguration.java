/*     */ package org.apache.logging.log4j.core.config;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import java.util.concurrent.CopyOnWriteArrayList;
/*     */ import org.apache.logging.log4j.Level;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.core.Appender;
/*     */ import org.apache.logging.log4j.core.Filter;
/*     */ import org.apache.logging.log4j.core.Layout;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.Logger;
/*     */ import org.apache.logging.log4j.core.appender.ConsoleAppender;
/*     */ import org.apache.logging.log4j.core.config.plugins.util.PluginBuilder;
/*     */ import org.apache.logging.log4j.core.config.plugins.util.PluginManager;
/*     */ import org.apache.logging.log4j.core.config.plugins.util.PluginType;
/*     */ import org.apache.logging.log4j.core.filter.AbstractFilterable;
/*     */ import org.apache.logging.log4j.core.impl.Log4jContextFactory;
/*     */ import org.apache.logging.log4j.core.layout.PatternLayout;
/*     */ import org.apache.logging.log4j.core.lookup.Interpolator;
/*     */ import org.apache.logging.log4j.core.lookup.MapLookup;
/*     */ import org.apache.logging.log4j.core.lookup.StrLookup;
/*     */ import org.apache.logging.log4j.core.lookup.StrSubstitutor;
/*     */ import org.apache.logging.log4j.core.net.Advertiser;
/*     */ import org.apache.logging.log4j.core.selector.ContextSelector;
/*     */ import org.apache.logging.log4j.core.util.Assert;
/*     */ import org.apache.logging.log4j.core.util.Loader;
/*     */ import org.apache.logging.log4j.core.util.NameUtil;
/*     */ import org.apache.logging.log4j.spi.LoggerContextFactory;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractConfiguration
/*     */   extends AbstractFilterable
/*     */   implements Configuration
/*     */ {
/*     */   private static final int BUF_SIZE = 16384;
/*     */   protected Node rootNode;
/*  77 */   protected final List<ConfigurationListener> listeners = new CopyOnWriteArrayList<ConfigurationListener>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  82 */   protected ConfigurationMonitor monitor = new DefaultConfigurationMonitor();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  87 */   private Advertiser advertiser = new DefaultAdvertiser();
/*  88 */   private Node advertiserNode = null;
/*     */   
/*     */   private Object advertisement;
/*     */   
/*     */   protected boolean isShutdownHookEnabled = true;
/*     */   
/*     */   private String name;
/*     */   
/*  96 */   private ConcurrentMap<String, Appender> appenders = new ConcurrentHashMap<String, Appender>();
/*  97 */   private ConcurrentMap<String, LoggerConfig> loggers = new ConcurrentHashMap<String, LoggerConfig>();
/*  98 */   private final ConcurrentMap<String, String> properties = new ConcurrentHashMap<String, String>();
/*  99 */   private final StrLookup tempLookup = (StrLookup)new Interpolator(this.properties);
/* 100 */   private final StrSubstitutor subst = new StrSubstitutor(this.tempLookup);
/* 101 */   private LoggerConfig root = new LoggerConfig();
/* 102 */   private final ConcurrentMap<String, Object> componentMap = new ConcurrentHashMap<String, Object>();
/*     */   
/*     */   protected PluginManager pluginManager;
/*     */   
/*     */   private final ConfigurationSource configurationSource;
/*     */ 
/*     */   
/*     */   protected AbstractConfiguration(ConfigurationSource configurationSource) {
/* 110 */     this.configurationSource = (ConfigurationSource)Assert.requireNonNull(configurationSource, "configurationSource is null");
/* 111 */     this.componentMap.put("ContextProperties", this.properties);
/* 112 */     this.pluginManager = new PluginManager("Core");
/* 113 */     this.rootNode = new Node();
/*     */   }
/*     */ 
/*     */   
/*     */   public ConfigurationSource getConfigurationSource() {
/* 118 */     return this.configurationSource;
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<String, String> getProperties() {
/* 123 */     return this.properties;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void start() {
/* 131 */     LOGGER.debug("Starting configuration {}", new Object[] { this });
/* 132 */     setStarting();
/* 133 */     this.pluginManager.collectPlugins();
/* 134 */     PluginManager levelPlugins = new PluginManager("Level");
/* 135 */     levelPlugins.collectPlugins();
/* 136 */     Map<String, PluginType<?>> plugins = levelPlugins.getPlugins();
/* 137 */     if (plugins != null) {
/* 138 */       for (PluginType<?> type : plugins.values()) {
/*     */         
/*     */         try {
/* 141 */           Loader.initializeClass(type.getPluginClass().getName(), type.getPluginClass().getClassLoader());
/* 142 */         } catch (Exception e) {
/* 143 */           LOGGER.error("Unable to initialize {} due to {}", new Object[] { type.getPluginClass().getName(), e.getClass().getSimpleName(), e });
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/* 148 */     setup();
/* 149 */     setupAdvertisement();
/* 150 */     doConfigure();
/* 151 */     Set<LoggerConfig> alreadyStarted = new HashSet<LoggerConfig>();
/* 152 */     for (LoggerConfig logger : this.loggers.values()) {
/* 153 */       logger.start();
/* 154 */       alreadyStarted.add(logger);
/*     */     } 
/* 156 */     for (Appender appender : this.appenders.values()) {
/* 157 */       appender.start();
/*     */     }
/* 159 */     if (!alreadyStarted.contains(this.root)) {
/* 160 */       this.root.start();
/*     */     }
/* 162 */     super.start();
/* 163 */     LOGGER.debug("Started configuration {} OK.", new Object[] { this });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void stop() {
/* 171 */     setStopping();
/* 172 */     LOGGER.trace("Stopping {}...", new Object[] { this });
/*     */ 
/*     */     
/* 175 */     LoggerContextFactory factory = LogManager.getFactory();
/* 176 */     if (factory instanceof Log4jContextFactory) {
/* 177 */       ContextSelector selector = ((Log4jContextFactory)factory).getSelector();
/* 178 */       if (selector instanceof org.apache.logging.log4j.core.async.AsyncLoggerContextSelector);
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
/* 189 */     Set<LoggerConfig> alreadyStopped = new HashSet<LoggerConfig>();
/* 190 */     int asyncLoggerConfigCount = 0;
/* 191 */     for (LoggerConfig logger : this.loggers.values()) {
/* 192 */       if (logger instanceof org.apache.logging.log4j.core.async.AsyncLoggerConfig) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 198 */         logger.stop();
/* 199 */         asyncLoggerConfigCount++;
/* 200 */         alreadyStopped.add(logger);
/*     */       } 
/*     */     } 
/* 203 */     if (this.root instanceof org.apache.logging.log4j.core.async.AsyncLoggerConfig) {
/* 204 */       this.root.stop();
/* 205 */       asyncLoggerConfigCount++;
/* 206 */       alreadyStopped.add(this.root);
/*     */     } 
/* 208 */     LOGGER.trace("AbstractConfiguration stopped {} AsyncLoggerConfigs.", new Object[] { Integer.valueOf(asyncLoggerConfigCount) });
/*     */ 
/*     */     
/* 211 */     Appender[] array = (Appender[])this.appenders.values().toArray((Object[])new Appender[this.appenders.size()]);
/*     */ 
/*     */     
/* 214 */     int asyncAppenderCount = 0;
/* 215 */     for (int i = array.length - 1; i >= 0; i--) {
/* 216 */       if (array[i] instanceof org.apache.logging.log4j.core.appender.AsyncAppender) {
/* 217 */         array[i].stop();
/* 218 */         asyncAppenderCount++;
/*     */       } 
/*     */     } 
/* 221 */     LOGGER.trace("AbstractConfiguration stopped {} AsyncAppenders.", new Object[] { Integer.valueOf(asyncAppenderCount) });
/*     */     
/* 223 */     int appenderCount = 0;
/* 224 */     for (int j = array.length - 1; j >= 0; j--) {
/* 225 */       if (array[j].isStarted()) {
/* 226 */         array[j].stop();
/* 227 */         appenderCount++;
/*     */       } 
/*     */     } 
/* 230 */     LOGGER.trace("AbstractConfiguration stopped {} Appenders.", new Object[] { Integer.valueOf(appenderCount) });
/*     */     
/* 232 */     int loggerCount = 0;
/* 233 */     for (LoggerConfig logger : this.loggers.values()) {
/*     */       
/* 235 */       logger.clearAppenders();
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 240 */       if (alreadyStopped.contains(logger)) {
/*     */         continue;
/*     */       }
/* 243 */       logger.stop();
/* 244 */       loggerCount++;
/*     */     } 
/* 246 */     LOGGER.trace("AbstractConfiguration stopped {} Loggers.", new Object[] { Integer.valueOf(loggerCount) });
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 251 */     if (!alreadyStopped.contains(this.root)) {
/* 252 */       this.root.stop();
/*     */     }
/* 254 */     super.stop();
/* 255 */     if (this.advertiser != null && this.advertisement != null) {
/* 256 */       this.advertiser.unadvertise(this.advertisement);
/*     */     }
/* 258 */     LOGGER.debug("Stopped {} OK", new Object[] { this });
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isShutdownHookEnabled() {
/* 263 */     return this.isShutdownHookEnabled;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void setup() {}
/*     */   
/*     */   protected Level getDefaultStatus() {
/* 270 */     String statusLevel = PropertiesUtil.getProperties().getStringProperty("Log4jDefaultStatusLevel", Level.ERROR.name());
/*     */     
/*     */     try {
/* 273 */       return Level.toLevel(statusLevel);
/* 274 */     } catch (Exception ex) {
/* 275 */       return Level.ERROR;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createAdvertiser(String advertiserString, ConfigurationSource configSource, byte[] buffer, String contentType) {
/* 281 */     if (advertiserString != null) {
/* 282 */       Node node = new Node(null, advertiserString, null);
/* 283 */       Map<String, String> attributes = node.getAttributes();
/* 284 */       attributes.put("content", new String(buffer));
/* 285 */       attributes.put("contentType", contentType);
/* 286 */       attributes.put("name", "configuration");
/* 287 */       if (configSource.getLocation() != null) {
/* 288 */         attributes.put("location", configSource.getLocation());
/*     */       }
/* 290 */       this.advertiserNode = node;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void setupAdvertisement() {
/* 295 */     if (this.advertiserNode != null) {
/*     */       
/* 297 */       String name = this.advertiserNode.getName();
/*     */       
/* 299 */       PluginType<Advertiser> type = this.pluginManager.getPluginType(name);
/* 300 */       if (type != null) {
/*     */         
/* 302 */         Class<Advertiser> clazz = type.getPluginClass();
/*     */         try {
/* 304 */           this.advertiser = clazz.newInstance();
/* 305 */           this.advertisement = this.advertiser.advertise(this.advertiserNode.getAttributes());
/* 306 */         } catch (InstantiationException e) {
/* 307 */           LOGGER.error("InstantiationException attempting to instantiate advertiser: {}", new Object[] { name, e });
/* 308 */         } catch (IllegalAccessException e) {
/* 309 */           LOGGER.error("IllegalAccessException attempting to instantiate advertiser: {}", new Object[] { name, e });
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T getComponent(String name) {
/* 318 */     return (T)this.componentMap.get(name);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addComponent(String name, Object obj) {
/* 323 */     this.componentMap.putIfAbsent(name, obj);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doConfigure() {
/* 328 */     if (this.rootNode.hasChildren() && ((Node)this.rootNode.getChildren().get(0)).getName().equalsIgnoreCase("Properties")) {
/* 329 */       Node first = this.rootNode.getChildren().get(0);
/* 330 */       createConfiguration(first, (LogEvent)null);
/* 331 */       if (first.getObject() != null) {
/* 332 */         this.subst.setVariableResolver((StrLookup)first.getObject());
/*     */       }
/*     */     } else {
/* 335 */       Map<String, String> map = (Map<String, String>)this.componentMap.get("ContextProperties");
/* 336 */       MapLookup mapLookup = (map == null) ? null : new MapLookup(map);
/* 337 */       this.subst.setVariableResolver((StrLookup)new Interpolator((StrLookup)mapLookup));
/*     */     } 
/*     */     
/* 340 */     boolean setLoggers = false;
/* 341 */     boolean setRoot = false;
/* 342 */     for (Node child : this.rootNode.getChildren()) {
/* 343 */       if (child.getName().equalsIgnoreCase("Properties")) {
/* 344 */         if (this.tempLookup == this.subst.getVariableResolver()) {
/* 345 */           LOGGER.error("Properties declaration must be the first element in the configuration");
/*     */         }
/*     */         continue;
/*     */       } 
/* 349 */       createConfiguration(child, (LogEvent)null);
/* 350 */       if (child.getObject() == null) {
/*     */         continue;
/*     */       }
/* 353 */       if (child.getName().equalsIgnoreCase("Appenders")) {
/* 354 */         this.appenders = (ConcurrentMap<String, Appender>)child.getObject(); continue;
/* 355 */       }  if (child.getObject() instanceof Filter) {
/* 356 */         addFilter((Filter)child.getObject()); continue;
/* 357 */       }  if (child.getName().equalsIgnoreCase("Loggers")) {
/* 358 */         Loggers l = (Loggers)child.getObject();
/* 359 */         this.loggers = l.getMap();
/* 360 */         setLoggers = true;
/* 361 */         if (l.getRoot() != null) {
/* 362 */           this.root = l.getRoot();
/* 363 */           setRoot = true;
/*     */         }  continue;
/*     */       } 
/* 366 */       LOGGER.error("Unknown object \"{}\" of type {} is ignored.", new Object[] { child.getName(), child.getObject().getClass().getName() });
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 371 */     if (!setLoggers) {
/* 372 */       LOGGER.warn("No Loggers were configured, using default. Is the Loggers element missing?");
/* 373 */       setToDefault(); return;
/*     */     } 
/* 375 */     if (!setRoot) {
/* 376 */       LOGGER.warn("No Root logger was configured, creating default ERROR-level Root logger with Console appender");
/* 377 */       setToDefault();
/*     */     } 
/*     */ 
/*     */     
/* 381 */     for (Map.Entry<String, LoggerConfig> entry : this.loggers.entrySet()) {
/* 382 */       LoggerConfig l = entry.getValue();
/* 383 */       for (AppenderRef ref : l.getAppenderRefs()) {
/* 384 */         Appender app = this.appenders.get(ref.getRef());
/* 385 */         if (app != null) {
/* 386 */           l.addAppender(app, ref.getLevel(), ref.getFilter()); continue;
/*     */         } 
/* 388 */         LOGGER.error("Unable to locate appender {} for logger {}", new Object[] { ref.getRef(), l.getName() });
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 394 */     setParents();
/*     */   }
/*     */ 
/*     */   
/*     */   private void setToDefault() {
/* 399 */     setName("Default");
/* 400 */     PatternLayout patternLayout = PatternLayout.newBuilder().withPattern("%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n").withConfiguration(this).build();
/*     */ 
/*     */ 
/*     */     
/* 404 */     ConsoleAppender consoleAppender = ConsoleAppender.createAppender((Layout)patternLayout, null, "SYSTEM_OUT", "Console", "false", "true");
/*     */     
/* 406 */     consoleAppender.start();
/* 407 */     addAppender((Appender)consoleAppender);
/* 408 */     LoggerConfig root = getRootLogger();
/* 409 */     root.addAppender((Appender)consoleAppender, (Level)null, (Filter)null);
/*     */     
/* 411 */     String levelName = PropertiesUtil.getProperties().getStringProperty("org.apache.logging.log4j.level");
/* 412 */     Level level = (levelName != null && Level.getLevel(levelName) != null) ? Level.getLevel(levelName) : Level.ERROR;
/*     */     
/* 414 */     root.setLevel(level);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setName(String name) {
/* 422 */     this.name = name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/* 431 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addListener(ConfigurationListener listener) {
/* 440 */     this.listeners.add(listener);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeListener(ConfigurationListener listener) {
/* 449 */     this.listeners.remove(listener);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Appender getAppender(String name) {
/* 459 */     return this.appenders.get(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, Appender> getAppenders() {
/* 468 */     return this.appenders;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addAppender(Appender appender) {
/* 477 */     this.appenders.putIfAbsent(appender.getName(), appender);
/*     */   }
/*     */ 
/*     */   
/*     */   public StrSubstitutor getStrSubstitutor() {
/* 482 */     return this.subst;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setConfigurationMonitor(ConfigurationMonitor monitor) {
/* 487 */     this.monitor = monitor;
/*     */   }
/*     */ 
/*     */   
/*     */   public ConfigurationMonitor getConfigurationMonitor() {
/* 492 */     return this.monitor;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setAdvertiser(Advertiser advertiser) {
/* 497 */     this.advertiser = advertiser;
/*     */   }
/*     */ 
/*     */   
/*     */   public Advertiser getAdvertiser() {
/* 502 */     return this.advertiser;
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
/*     */   public synchronized void addLoggerAppender(Logger logger, Appender appender) {
/* 517 */     String name = logger.getName();
/* 518 */     this.appenders.putIfAbsent(appender.getName(), appender);
/* 519 */     LoggerConfig lc = getLoggerConfig(name);
/* 520 */     if (lc.getName().equals(name)) {
/* 521 */       lc.addAppender(appender, (Level)null, (Filter)null);
/*     */     } else {
/* 523 */       LoggerConfig nlc = new LoggerConfig(name, lc.getLevel(), lc.isAdditive());
/* 524 */       nlc.addAppender(appender, (Level)null, (Filter)null);
/* 525 */       nlc.setParent(lc);
/* 526 */       this.loggers.putIfAbsent(name, nlc);
/* 527 */       setParents();
/* 528 */       logger.getContext().updateLoggers();
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
/*     */   public synchronized void addLoggerFilter(Logger logger, Filter filter) {
/* 542 */     String name = logger.getName();
/* 543 */     LoggerConfig lc = getLoggerConfig(name);
/* 544 */     if (lc.getName().equals(name)) {
/*     */       
/* 546 */       lc.addFilter(filter);
/*     */     } else {
/* 548 */       LoggerConfig nlc = new LoggerConfig(name, lc.getLevel(), lc.isAdditive());
/* 549 */       nlc.addFilter(filter);
/* 550 */       nlc.setParent(lc);
/* 551 */       this.loggers.putIfAbsent(name, nlc);
/* 552 */       setParents();
/* 553 */       logger.getContext().updateLoggers();
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
/*     */   public synchronized void setLoggerAdditive(Logger logger, boolean additive) {
/* 568 */     String name = logger.getName();
/* 569 */     LoggerConfig lc = getLoggerConfig(name);
/* 570 */     if (lc.getName().equals(name)) {
/* 571 */       lc.setAdditive(additive);
/*     */     } else {
/* 573 */       LoggerConfig nlc = new LoggerConfig(name, lc.getLevel(), additive);
/* 574 */       nlc.setParent(lc);
/* 575 */       this.loggers.putIfAbsent(name, nlc);
/* 576 */       setParents();
/* 577 */       logger.getContext().updateLoggers();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void removeAppender(String name) {
/* 588 */     for (LoggerConfig logger : this.loggers.values()) {
/* 589 */       logger.removeAppender(name);
/*     */     }
/* 591 */     Appender app = this.appenders.remove(name);
/*     */     
/* 593 */     if (app != null) {
/* 594 */       app.stop();
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
/*     */   public LoggerConfig getLoggerConfig(String name) {
/* 606 */     if (this.loggers.containsKey(name)) {
/* 607 */       return this.loggers.get(name);
/*     */     }
/* 609 */     String substr = name;
/* 610 */     while ((substr = NameUtil.getSubName(substr)) != null) {
/* 611 */       if (this.loggers.containsKey(substr)) {
/* 612 */         return this.loggers.get(substr);
/*     */       }
/*     */     } 
/* 615 */     return this.root;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LoggerConfig getRootLogger() {
/* 623 */     return this.root;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, LoggerConfig> getLoggers() {
/* 632 */     return Collections.unmodifiableMap(this.loggers);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LoggerConfig getLogger(String name) {
/* 641 */     return this.loggers.get(name);
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
/*     */   public synchronized void addLogger(String name, LoggerConfig loggerConfig) {
/* 653 */     this.loggers.putIfAbsent(name, loggerConfig);
/* 654 */     setParents();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void removeLogger(String name) {
/* 664 */     this.loggers.remove(name);
/* 665 */     setParents();
/*     */   }
/*     */ 
/*     */   
/*     */   public void createConfiguration(Node node, LogEvent event) {
/* 670 */     PluginType<?> type = node.getType();
/* 671 */     if (type != null && type.isDeferChildren()) {
/* 672 */       node.setObject(createPluginObject(type, node, event));
/*     */     } else {
/* 674 */       for (Node child : node.getChildren()) {
/* 675 */         createConfiguration(child, event);
/*     */       }
/*     */       
/* 678 */       if (type == null) {
/* 679 */         if (node.getParent() != null) {
/* 680 */           LOGGER.error("Unable to locate plugin for {}", new Object[] { node.getName() });
/*     */         }
/*     */       } else {
/* 683 */         node.setObject(createPluginObject(type, node, event));
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private <T> Object createPluginObject(PluginType<T> type, Node node, LogEvent event) {
/* 726 */     Class<T> clazz = type.getPluginClass();
/*     */     
/* 728 */     if (Map.class.isAssignableFrom(clazz)) {
/*     */       try {
/* 730 */         return createPluginMap(node, clazz);
/* 731 */       } catch (Exception e) {
/* 732 */         LOGGER.warn("Unable to create Map for {} of class {}", new Object[] { type.getElementName(), clazz, e });
/*     */       } 
/*     */     }
/*     */     
/* 736 */     if (Collection.class.isAssignableFrom(clazz)) {
/*     */       try {
/* 738 */         return createPluginCollection(node, clazz);
/* 739 */       } catch (Exception e) {
/* 740 */         LOGGER.warn("Unable to create List for {} of class {}", new Object[] { type.getElementName(), clazz, e });
/*     */       } 
/*     */     }
/*     */     
/* 744 */     return (new PluginBuilder(type)).withConfiguration(this).withConfigurationNode(node).forLogEvent(event).build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static <T> Object createPluginMap(Node node, Class<T> clazz) throws InstantiationException, IllegalAccessException {
/* 753 */     Map<String, Object> map = (Map<String, Object>)clazz.newInstance();
/* 754 */     for (Node child : node.getChildren()) {
/* 755 */       map.put(child.getName(), child.getObject());
/*     */     }
/* 757 */     return map;
/*     */   }
/*     */ 
/*     */   
/*     */   private static <T> Object createPluginCollection(Node node, Class<T> clazz) throws InstantiationException, IllegalAccessException {
/* 762 */     Collection<Object> list = (Collection<Object>)clazz.newInstance();
/* 763 */     for (Node child : node.getChildren()) {
/* 764 */       list.add(child.getObject());
/*     */     }
/* 766 */     return list;
/*     */   }
/*     */   
/*     */   private void setParents() {
/* 770 */     for (Map.Entry<String, LoggerConfig> entry : this.loggers.entrySet()) {
/* 771 */       LoggerConfig logger = entry.getValue();
/* 772 */       String name = entry.getKey();
/* 773 */       if (!name.isEmpty()) {
/* 774 */         int i = name.lastIndexOf('.');
/* 775 */         if (i > 0) {
/* 776 */           name = name.substring(0, i);
/* 777 */           LoggerConfig parent = getLoggerConfig(name);
/* 778 */           if (parent == null) {
/* 779 */             parent = this.root;
/*     */           }
/* 781 */           logger.setParent(parent); continue;
/*     */         } 
/* 783 */         logger.setParent(this.root);
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
/*     */ 
/*     */ 
/*     */   
/*     */   protected static byte[] toByteArray(InputStream is) throws IOException {
/* 798 */     ByteArrayOutputStream buffer = new ByteArrayOutputStream();
/*     */ 
/*     */     
/* 801 */     byte[] data = new byte[16384];
/*     */     int nRead;
/* 803 */     while ((nRead = is.read(data, 0, data.length)) != -1) {
/* 804 */       buffer.write(data, 0, nRead);
/*     */     }
/*     */     
/* 807 */     return buffer.toByteArray();
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\config\AbstractConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */