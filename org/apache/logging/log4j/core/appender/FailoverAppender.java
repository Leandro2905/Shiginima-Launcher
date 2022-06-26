/*     */ package org.apache.logging.log4j.core.appender;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.apache.logging.log4j.LoggingException;
/*     */ import org.apache.logging.log4j.core.Appender;
/*     */ import org.apache.logging.log4j.core.Filter;
/*     */ import org.apache.logging.log4j.core.Layout;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.config.AppenderControl;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginElement;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginFactory;
/*     */ import org.apache.logging.log4j.core.util.Booleans;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Plugin(name = "Failover", category = "Core", elementType = "appender", printObject = true)
/*     */ public final class FailoverAppender
/*     */   extends AbstractAppender
/*     */ {
/*     */   private static final int DEFAULT_INTERVAL_SECONDS = 60;
/*     */   private final String primaryRef;
/*     */   private final String[] failovers;
/*     */   private final Configuration config;
/*     */   private AppenderControl primary;
/*  55 */   private final List<AppenderControl> failoverAppenders = new ArrayList<AppenderControl>();
/*     */   
/*     */   private final long intervalMillis;
/*     */   
/*  59 */   private volatile long nextCheckMillis = 0L;
/*     */ 
/*     */   
/*     */   private FailoverAppender(String name, Filter filter, String primary, String[] failovers, int intervalMillis, Configuration config, boolean ignoreExceptions) {
/*  63 */     super(name, filter, (Layout<? extends Serializable>)null, ignoreExceptions);
/*  64 */     this.primaryRef = primary;
/*  65 */     this.failovers = failovers;
/*  66 */     this.config = config;
/*  67 */     this.intervalMillis = intervalMillis;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void start() {
/*  73 */     Map<String, Appender> map = this.config.getAppenders();
/*  74 */     int errors = 0;
/*  75 */     if (map.containsKey(this.primaryRef)) {
/*  76 */       this.primary = new AppenderControl(map.get(this.primaryRef), null, null);
/*     */     } else {
/*  78 */       LOGGER.error("Unable to locate primary Appender " + this.primaryRef);
/*  79 */       errors++;
/*     */     } 
/*  81 */     for (String name : this.failovers) {
/*  82 */       if (map.containsKey(name)) {
/*  83 */         this.failoverAppenders.add(new AppenderControl(map.get(name), null, null));
/*     */       } else {
/*  85 */         LOGGER.error("Failover appender " + name + " is not configured");
/*     */       } 
/*     */     } 
/*  88 */     if (this.failoverAppenders.isEmpty()) {
/*  89 */       LOGGER.error("No failover appenders are available");
/*  90 */       errors++;
/*     */     } 
/*  92 */     if (errors == 0) {
/*  93 */       super.start();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void append(LogEvent event) {
/* 103 */     if (!isStarted()) {
/* 104 */       error("FailoverAppender " + getName() + " did not start successfully");
/*     */       return;
/*     */     } 
/* 107 */     long localCheckMillis = this.nextCheckMillis;
/* 108 */     if (localCheckMillis == 0L || System.currentTimeMillis() > localCheckMillis) {
/* 109 */       callAppender(event);
/*     */     } else {
/* 111 */       failover(event, (Exception)null);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void callAppender(LogEvent event) {
/*     */     try {
/* 117 */       this.primary.callAppender(event);
/* 118 */       this.nextCheckMillis = 0L;
/* 119 */     } catch (Exception ex) {
/* 120 */       this.nextCheckMillis = System.currentTimeMillis() + this.intervalMillis;
/* 121 */       failover(event, ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void failover(LogEvent event, Exception ex) {
/* 126 */     LoggingException loggingException = (ex != null) ? ((ex instanceof LoggingException) ? (LoggingException)ex : new LoggingException(ex)) : null;
/*     */     
/* 128 */     boolean written = false;
/* 129 */     Exception failoverException = null;
/* 130 */     for (AppenderControl control : this.failoverAppenders) {
/*     */       try {
/* 132 */         control.callAppender(event);
/* 133 */         written = true;
/*     */         break;
/* 135 */       } catch (Exception fex) {
/* 136 */         if (failoverException == null) {
/* 137 */           failoverException = fex;
/*     */         }
/*     */       } 
/*     */     } 
/* 141 */     if (!written && !ignoreExceptions()) {
/* 142 */       if (loggingException != null) {
/* 143 */         throw loggingException;
/*     */       }
/* 145 */       throw new LoggingException("Unable to write to failover appenders", failoverException);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 152 */     StringBuilder sb = new StringBuilder(getName());
/* 153 */     sb.append(" primary=").append(this.primary).append(", failover={");
/* 154 */     boolean first = true;
/* 155 */     for (String str : this.failovers) {
/* 156 */       if (!first) {
/* 157 */         sb.append(", ");
/*     */       }
/* 159 */       sb.append(str);
/* 160 */       first = false;
/*     */     } 
/* 162 */     sb.append('}');
/* 163 */     return sb.toString();
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
/*     */   @PluginFactory
/*     */   public static FailoverAppender createAppender(@PluginAttribute("name") String name, @PluginAttribute("primary") String primary, @PluginElement("Failovers") String[] failovers, @PluginAttribute("retryInterval") String retryIntervalString, @PluginConfiguration Configuration config, @PluginElement("Filter") Filter filter, @PluginAttribute("ignoreExceptions") String ignore) {
/*     */     int retryIntervalMillis;
/* 187 */     if (name == null) {
/* 188 */       LOGGER.error("A name for the Appender must be specified");
/* 189 */       return null;
/*     */     } 
/* 191 */     if (primary == null) {
/* 192 */       LOGGER.error("A primary Appender must be specified");
/* 193 */       return null;
/*     */     } 
/* 195 */     if (failovers == null || failovers.length == 0) {
/* 196 */       LOGGER.error("At least one failover Appender must be specified");
/* 197 */       return null;
/*     */     } 
/*     */     
/* 200 */     int seconds = parseInt(retryIntervalString, 60);
/*     */     
/* 202 */     if (seconds >= 0) {
/* 203 */       retryIntervalMillis = seconds * 1000;
/*     */     } else {
/* 205 */       LOGGER.warn("Interval " + retryIntervalString + " is less than zero. Using default");
/* 206 */       retryIntervalMillis = 60000;
/*     */     } 
/*     */     
/* 209 */     boolean ignoreExceptions = Booleans.parseBoolean(ignore, true);
/*     */     
/* 211 */     return new FailoverAppender(name, filter, primary, failovers, retryIntervalMillis, config, ignoreExceptions);
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\appender\FailoverAppender.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */