/*     */ package org.apache.logging.log4j.simple;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.text.DateFormat;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Date;
/*     */ import java.util.Map;
/*     */ import org.apache.logging.log4j.Level;
/*     */ import org.apache.logging.log4j.Marker;
/*     */ import org.apache.logging.log4j.ThreadContext;
/*     */ import org.apache.logging.log4j.message.Message;
/*     */ import org.apache.logging.log4j.message.MessageFactory;
/*     */ import org.apache.logging.log4j.spi.AbstractLogger;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SimpleLogger
/*     */   extends AbstractLogger
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   private static final char SPACE = ' ';
/*     */   private DateFormat dateFormatter;
/*     */   private Level level;
/*     */   private final boolean showDateTime;
/*     */   private final boolean showContextMap;
/*     */   private PrintStream stream;
/*     */   private final String logName;
/*     */   
/*     */   public SimpleLogger(String name, Level defaultLevel, boolean showLogName, boolean showShortLogName, boolean showDateTime, boolean showContextMap, String dateTimeFormat, MessageFactory messageFactory, PropertiesUtil props, PrintStream stream) {
/*  66 */     super(name, messageFactory);
/*  67 */     String lvl = props.getStringProperty("org.apache.logging.log4j.simplelog." + name + ".level");
/*  68 */     this.level = Level.toLevel(lvl, defaultLevel);
/*  69 */     if (showShortLogName) {
/*  70 */       int index = name.lastIndexOf(".");
/*  71 */       if (index > 0 && index < name.length()) {
/*  72 */         this.logName = name.substring(index + 1);
/*     */       } else {
/*  74 */         this.logName = name;
/*     */       } 
/*  76 */     } else if (showLogName) {
/*  77 */       this.logName = name;
/*     */     } else {
/*  79 */       this.logName = null;
/*     */     } 
/*  81 */     this.showDateTime = showDateTime;
/*  82 */     this.showContextMap = showContextMap;
/*  83 */     this.stream = stream;
/*     */     
/*  85 */     if (showDateTime) {
/*     */       try {
/*  87 */         this.dateFormatter = new SimpleDateFormat(dateTimeFormat);
/*  88 */       } catch (IllegalArgumentException e) {
/*     */         
/*  90 */         this.dateFormatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss:SSS zzz");
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public Level getLevel() {
/*  97 */     return this.level;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEnabled(Level level, Marker marker, Message msg, Throwable t) {
/* 102 */     return (this.level.intLevel() >= level.intLevel());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEnabled(Level level, Marker marker, Object msg, Throwable t) {
/* 107 */     return (this.level.intLevel() >= level.intLevel());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEnabled(Level level, Marker marker, String msg) {
/* 112 */     return (this.level.intLevel() >= level.intLevel());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEnabled(Level level, Marker marker, String msg, Object... p1) {
/* 117 */     return (this.level.intLevel() >= level.intLevel());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEnabled(Level level, Marker marker, String msg, Throwable t) {
/* 122 */     return (this.level.intLevel() >= level.intLevel());
/*     */   }
/*     */ 
/*     */   
/*     */   public void logMessage(String fqcn, Level level, Marker marker, Message msg, Throwable throwable) {
/*     */     Throwable t;
/* 128 */     StringBuilder sb = new StringBuilder();
/*     */     
/* 130 */     if (this.showDateTime) {
/* 131 */       String dateText; Date now = new Date();
/*     */       
/* 133 */       synchronized (this.dateFormatter) {
/* 134 */         dateText = this.dateFormatter.format(now);
/*     */       } 
/* 136 */       sb.append(dateText);
/* 137 */       sb.append(' ');
/*     */     } 
/*     */     
/* 140 */     sb.append(level.toString());
/* 141 */     sb.append(' ');
/* 142 */     if (this.logName != null && this.logName.length() > 0) {
/* 143 */       sb.append(this.logName);
/* 144 */       sb.append(' ');
/*     */     } 
/* 146 */     sb.append(msg.getFormattedMessage());
/* 147 */     if (this.showContextMap) {
/* 148 */       Map<String, String> mdc = ThreadContext.getContext();
/* 149 */       if (mdc.size() > 0) {
/* 150 */         sb.append(' ');
/* 151 */         sb.append(mdc.toString());
/* 152 */         sb.append(' ');
/*     */       } 
/*     */     } 
/* 155 */     Object[] params = msg.getParameters();
/*     */     
/* 157 */     if (throwable == null && params != null && params[params.length - 1] instanceof Throwable) {
/* 158 */       t = (Throwable)params[params.length - 1];
/*     */     } else {
/* 160 */       t = throwable;
/*     */     } 
/* 162 */     if (t != null) {
/* 163 */       sb.append(' ');
/* 164 */       ByteArrayOutputStream baos = new ByteArrayOutputStream();
/* 165 */       t.printStackTrace(new PrintStream(baos));
/* 166 */       sb.append(baos.toString());
/*     */     } 
/* 168 */     this.stream.println(sb.toString());
/*     */   }
/*     */   
/*     */   public void setLevel(Level level) {
/* 172 */     if (level != null) {
/* 173 */       this.level = level;
/*     */     }
/*     */   }
/*     */   
/*     */   public void setStream(PrintStream stream) {
/* 178 */     this.stream = stream;
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\simple\SimpleLogger.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */