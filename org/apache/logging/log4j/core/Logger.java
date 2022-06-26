/*     */ package org.apache.logging.log4j.core;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.apache.logging.log4j.Level;
/*     */ import org.apache.logging.log4j.Marker;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.config.LoggerConfig;
/*     */ import org.apache.logging.log4j.core.filter.CompositeFilter;
/*     */ import org.apache.logging.log4j.message.Message;
/*     */ import org.apache.logging.log4j.message.MessageFactory;
/*     */ import org.apache.logging.log4j.message.SimpleMessage;
/*     */ import org.apache.logging.log4j.spi.AbstractLogger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Logger
/*     */   extends AbstractLogger
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected volatile PrivateConfig config;
/*     */   private final LoggerContext context;
/*     */   
/*     */   protected Logger(LoggerContext context, String name, MessageFactory messageFactory) {
/*  59 */     super(name, messageFactory);
/*  60 */     this.context = context;
/*  61 */     this.config = new PrivateConfig(context.getConfiguration(), this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Logger getParent() {
/*  70 */     LoggerConfig lc = this.config.loggerConfig.getName().equals(getName()) ? this.config.loggerConfig.getParent() : this.config.loggerConfig;
/*     */     
/*  72 */     if (lc == null) {
/*  73 */       return null;
/*     */     }
/*  75 */     if (this.context.hasLogger(lc.getName())) {
/*  76 */       return this.context.getLogger(lc.getName(), getMessageFactory());
/*     */     }
/*  78 */     return new Logger(this.context, lc.getName(), getMessageFactory());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LoggerContext getContext() {
/*  86 */     return this.context;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void setLevel(Level level) {
/*  94 */     if (level != null) {
/*  95 */       this.config = new PrivateConfig(this.config, level);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void logMessage(String fqcn, Level level, Marker marker, Message message, Throwable t) {
/* 101 */     Message msg = (message == null) ? (Message)new SimpleMessage("") : message;
/* 102 */     this.config.config.getConfigurationMonitor().checkConfiguration();
/* 103 */     this.config.loggerConfig.log(getName(), fqcn, marker, level, msg, t);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEnabled(Level level, Marker marker, String message, Throwable t) {
/* 108 */     return this.config.filter(level, marker, message, t);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEnabled(Level level, Marker marker, String message) {
/* 113 */     return this.config.filter(level, marker, message);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEnabled(Level level, Marker marker, String message, Object... params) {
/* 118 */     return this.config.filter(level, marker, message, params);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEnabled(Level level, Marker marker, Object message, Throwable t) {
/* 123 */     return this.config.filter(level, marker, message, t);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEnabled(Level level, Marker marker, Message message, Throwable t) {
/* 128 */     return this.config.filter(level, marker, message, t);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addAppender(Appender appender) {
/* 136 */     this.config.config.addLoggerAppender(this, appender);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeAppender(Appender appender) {
/* 144 */     this.config.loggerConfig.removeAppender(appender.getName());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, Appender> getAppenders() {
/* 152 */     return this.config.loggerConfig.getAppenders();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Iterator<Filter> getFilters() {
/* 160 */     Filter filter = this.config.loggerConfig.getFilter();
/* 161 */     if (filter == null)
/* 162 */       return (new ArrayList<Filter>()).iterator(); 
/* 163 */     if (filter instanceof CompositeFilter) {
/* 164 */       return ((CompositeFilter)filter).iterator();
/*     */     }
/* 166 */     List<Filter> filters = new ArrayList<Filter>();
/* 167 */     filters.add(filter);
/* 168 */     return filters.iterator();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Level getLevel() {
/* 179 */     return this.config.level;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int filterCount() {
/* 187 */     Filter filter = this.config.loggerConfig.getFilter();
/* 188 */     if (filter == null)
/* 189 */       return 0; 
/* 190 */     if (filter instanceof CompositeFilter) {
/* 191 */       return ((CompositeFilter)filter).size();
/*     */     }
/* 193 */     return 1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addFilter(Filter filter) {
/* 201 */     this.config.config.addLoggerFilter(this, filter);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isAdditive() {
/* 210 */     return this.config.loggerConfig.isAdditive();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAdditive(boolean additive) {
/* 219 */     this.config.config.setLoggerAdditive(this, additive);
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
/*     */   void updateConfiguration(Configuration config) {
/* 235 */     this.config = new PrivateConfig(config, this);
/*     */   }
/*     */ 
/*     */   
/*     */   protected class PrivateConfig
/*     */   {
/*     */     public final LoggerConfig loggerConfig;
/*     */     
/*     */     public final Configuration config;
/*     */     
/*     */     private final Level level;
/*     */     private final int intLevel;
/*     */     private final Logger logger;
/*     */     
/*     */     public PrivateConfig(Configuration config, Logger logger) {
/* 250 */       this.config = config;
/* 251 */       this.loggerConfig = config.getLoggerConfig(Logger.this.getName());
/* 252 */       this.level = this.loggerConfig.getLevel();
/* 253 */       this.intLevel = this.level.intLevel();
/* 254 */       this.logger = logger;
/*     */     }
/*     */     
/*     */     public PrivateConfig(PrivateConfig pc, Level level) {
/* 258 */       this.config = pc.config;
/* 259 */       this.loggerConfig = pc.loggerConfig;
/* 260 */       this.level = level;
/* 261 */       this.intLevel = this.level.intLevel();
/* 262 */       this.logger = pc.logger;
/*     */     }
/*     */     
/*     */     public PrivateConfig(PrivateConfig pc, LoggerConfig lc) {
/* 266 */       this.config = pc.config;
/* 267 */       this.loggerConfig = lc;
/* 268 */       this.level = lc.getLevel();
/* 269 */       this.intLevel = this.level.intLevel();
/* 270 */       this.logger = pc.logger;
/*     */     }
/*     */ 
/*     */     
/*     */     public void logEvent(LogEvent event) {
/* 275 */       this.config.getConfigurationMonitor().checkConfiguration();
/* 276 */       this.loggerConfig.log(event);
/*     */     }
/*     */     
/*     */     boolean filter(Level level, Marker marker, String msg) {
/* 280 */       this.config.getConfigurationMonitor().checkConfiguration();
/* 281 */       Filter filter = this.config.getFilter();
/* 282 */       if (filter != null) {
/* 283 */         Filter.Result r = filter.filter(this.logger, level, marker, msg, new Object[0]);
/* 284 */         if (r != Filter.Result.NEUTRAL) {
/* 285 */           return (r == Filter.Result.ACCEPT);
/*     */         }
/*     */       } 
/*     */       
/* 289 */       return (this.intLevel >= level.intLevel());
/*     */     }
/*     */     
/*     */     boolean filter(Level level, Marker marker, String msg, Throwable t) {
/* 293 */       this.config.getConfigurationMonitor().checkConfiguration();
/* 294 */       Filter filter = this.config.getFilter();
/* 295 */       if (filter != null) {
/* 296 */         Filter.Result r = filter.filter(this.logger, level, marker, msg, t);
/* 297 */         if (r != Filter.Result.NEUTRAL) {
/* 298 */           return (r == Filter.Result.ACCEPT);
/*     */         }
/*     */       } 
/*     */       
/* 302 */       return (this.intLevel >= level.intLevel());
/*     */     }
/*     */     
/*     */     boolean filter(Level level, Marker marker, String msg, Object... p1) {
/* 306 */       this.config.getConfigurationMonitor().checkConfiguration();
/* 307 */       Filter filter = this.config.getFilter();
/* 308 */       if (filter != null) {
/* 309 */         Filter.Result r = filter.filter(this.logger, level, marker, msg, p1);
/* 310 */         if (r != Filter.Result.NEUTRAL) {
/* 311 */           return (r == Filter.Result.ACCEPT);
/*     */         }
/*     */       } 
/*     */       
/* 315 */       return (this.intLevel >= level.intLevel());
/*     */     }
/*     */     
/*     */     boolean filter(Level level, Marker marker, Object msg, Throwable t) {
/* 319 */       this.config.getConfigurationMonitor().checkConfiguration();
/* 320 */       Filter filter = this.config.getFilter();
/* 321 */       if (filter != null) {
/* 322 */         Filter.Result r = filter.filter(this.logger, level, marker, msg, t);
/* 323 */         if (r != Filter.Result.NEUTRAL) {
/* 324 */           return (r == Filter.Result.ACCEPT);
/*     */         }
/*     */       } 
/*     */       
/* 328 */       return (this.intLevel >= level.intLevel());
/*     */     }
/*     */     
/*     */     boolean filter(Level level, Marker marker, Message msg, Throwable t) {
/* 332 */       this.config.getConfigurationMonitor().checkConfiguration();
/* 333 */       Filter filter = this.config.getFilter();
/* 334 */       if (filter != null) {
/* 335 */         Filter.Result r = filter.filter(this.logger, level, marker, msg, t);
/* 336 */         if (r != Filter.Result.NEUTRAL) {
/* 337 */           return (r == Filter.Result.ACCEPT);
/*     */         }
/*     */       } 
/*     */       
/* 341 */       return (this.intLevel >= level.intLevel());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 351 */     String nameLevel = "" + getName() + ':' + getLevel();
/* 352 */     if (this.context == null) {
/* 353 */       return nameLevel;
/*     */     }
/* 355 */     String contextName = this.context.getName();
/* 356 */     return (contextName == null) ? nameLevel : (nameLevel + " in " + contextName);
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\Logger.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */