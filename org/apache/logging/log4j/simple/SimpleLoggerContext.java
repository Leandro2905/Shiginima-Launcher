/*     */ package org.apache.logging.log4j.simple;
/*     */ 
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.util.Properties;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import org.apache.logging.log4j.Level;
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
/*     */ public class SimpleLoggerContext
/*     */   implements LoggerContext
/*     */ {
/*     */   protected static final String DEFAULT_DATE_TIME_FORMAT = "yyyy/MM/dd HH:mm:ss:SSS zzz";
/*     */   protected static final String SYSTEM_PREFIX = "org.apache.logging.log4j.simplelog.";
/*     */   private final Properties simpleLogProps;
/*     */   private final PropertiesUtil props;
/*     */   private final boolean showLogName;
/*     */   private final boolean showShortName;
/*     */   private final boolean showDateTime;
/*     */   private final boolean showContextMap;
/*     */   private final String dateTimeFormat;
/*     */   private final Level defaultLevel;
/*     */   private final PrintStream stream;
/*     */   private final ConcurrentMap<String, ExtendedLogger> loggers;
/*     */   
/*     */   public SimpleLoggerContext() {
/*     */     PrintStream printStream;
/*  45 */     this.simpleLogProps = new Properties();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  67 */     this.loggers = new ConcurrentHashMap<String, ExtendedLogger>();
/*     */ 
/*     */     
/*  70 */     this.props = new PropertiesUtil("log4j2.simplelog.properties");
/*     */     
/*  72 */     this.showContextMap = this.props.getBooleanProperty("org.apache.logging.log4j.simplelog.showContextMap", false);
/*  73 */     this.showLogName = this.props.getBooleanProperty("org.apache.logging.log4j.simplelog.showlogname", false);
/*  74 */     this.showShortName = this.props.getBooleanProperty("org.apache.logging.log4j.simplelog.showShortLogname", true);
/*  75 */     this.showDateTime = this.props.getBooleanProperty("org.apache.logging.log4j.simplelog.showdatetime", false);
/*  76 */     String lvl = this.props.getStringProperty("org.apache.logging.log4j.simplelog.level");
/*  77 */     this.defaultLevel = Level.toLevel(lvl, Level.ERROR);
/*     */     
/*  79 */     this.dateTimeFormat = this.showDateTime ? this.props.getStringProperty("org.apache.logging.log4j.simplelog.dateTimeFormat", "yyyy/MM/dd HH:mm:ss:SSS zzz") : null;
/*     */ 
/*     */     
/*  82 */     String fileName = this.props.getStringProperty("org.apache.logging.log4j.simplelog.logFile", "system.err");
/*     */     
/*  84 */     if ("system.err".equalsIgnoreCase(fileName)) {
/*  85 */       printStream = System.err;
/*  86 */     } else if ("system.out".equalsIgnoreCase(fileName)) {
/*  87 */       printStream = System.out;
/*     */     } else {
/*     */       try {
/*  90 */         FileOutputStream os = new FileOutputStream(fileName);
/*  91 */         printStream = new PrintStream(os);
/*  92 */       } catch (FileNotFoundException fnfe) {
/*  93 */         printStream = System.err;
/*     */       } 
/*     */     } 
/*  96 */     this.stream = printStream;
/*     */   }
/*     */ 
/*     */   
/*     */   public ExtendedLogger getLogger(String name) {
/* 101 */     return getLogger(name, null);
/*     */   }
/*     */ 
/*     */   
/*     */   public ExtendedLogger getLogger(String name, MessageFactory messageFactory) {
/* 106 */     if (this.loggers.containsKey(name)) {
/* 107 */       ExtendedLogger logger = this.loggers.get(name);
/* 108 */       AbstractLogger.checkMessageFactory(logger, messageFactory);
/* 109 */       return logger;
/*     */     } 
/*     */     
/* 112 */     this.loggers.putIfAbsent(name, new SimpleLogger(name, this.defaultLevel, this.showLogName, this.showShortName, this.showDateTime, this.showContextMap, this.dateTimeFormat, messageFactory, this.props, this.stream));
/*     */     
/* 114 */     return this.loggers.get(name);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasLogger(String name) {
/* 119 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object getExternalContext() {
/* 124 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\simple\SimpleLoggerContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */