/*     */ package org.apache.logging.log4j.core.jmx;
/*     */ 
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.PrintWriter;
/*     */ import java.io.Reader;
/*     */ import java.io.StringWriter;
/*     */ import java.net.URISyntaxException;
/*     */ import java.net.URL;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.atomic.AtomicLong;
/*     */ import javax.management.MBeanNotificationInfo;
/*     */ import javax.management.Notification;
/*     */ import javax.management.NotificationBroadcasterSupport;
/*     */ import javax.management.ObjectName;
/*     */ import org.apache.logging.log4j.core.LoggerContext;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.config.ConfigurationFactory;
/*     */ import org.apache.logging.log4j.core.config.ConfigurationSource;
/*     */ import org.apache.logging.log4j.core.util.Assert;
/*     */ import org.apache.logging.log4j.core.util.Charsets;
/*     */ import org.apache.logging.log4j.core.util.Closer;
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
/*     */ public class LoggerContextAdmin
/*     */   extends NotificationBroadcasterSupport
/*     */   implements LoggerContextAdminMBean, PropertyChangeListener
/*     */ {
/*     */   private static final int PAGE = 4096;
/*     */   private static final int TEXT_BUFFER = 65536;
/*     */   private static final int BUFFER_SIZE = 2048;
/*  60 */   private static final StatusLogger LOGGER = StatusLogger.getLogger();
/*     */   
/*  62 */   private final AtomicLong sequenceNo = new AtomicLong();
/*     */ 
/*     */ 
/*     */   
/*     */   private final ObjectName objectName;
/*     */ 
/*     */   
/*     */   private final LoggerContext loggerContext;
/*     */ 
/*     */ 
/*     */   
/*     */   public LoggerContextAdmin(LoggerContext loggerContext, Executor executor) {
/*  74 */     super(executor, new MBeanNotificationInfo[] { createNotificationInfo() });
/*  75 */     this.loggerContext = (LoggerContext)Assert.requireNonNull(loggerContext, "loggerContext");
/*     */     try {
/*  77 */       String ctxName = Server.escape(loggerContext.getName());
/*  78 */       String name = String.format("org.apache.logging.log4j2:type=%s", new Object[] { ctxName });
/*  79 */       this.objectName = new ObjectName(name);
/*  80 */     } catch (Exception e) {
/*  81 */       throw new IllegalStateException(e);
/*     */     } 
/*  83 */     loggerContext.addPropertyChangeListener(this);
/*     */   }
/*     */   
/*     */   private static MBeanNotificationInfo createNotificationInfo() {
/*  87 */     String[] notifTypes = { "com.apache.logging.log4j.core.jmx.config.reconfigured" };
/*  88 */     String name = Notification.class.getName();
/*  89 */     String description = "Configuration reconfigured";
/*  90 */     return new MBeanNotificationInfo(notifTypes, name, "Configuration reconfigured");
/*     */   }
/*     */ 
/*     */   
/*     */   public String getStatus() {
/*  95 */     return this.loggerContext.getState().toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getName() {
/* 100 */     return this.loggerContext.getName();
/*     */   }
/*     */   
/*     */   private Configuration getConfig() {
/* 104 */     return this.loggerContext.getConfiguration();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getConfigLocationUri() {
/* 109 */     if (this.loggerContext.getConfigLocation() != null) {
/* 110 */       return String.valueOf(this.loggerContext.getConfigLocation());
/*     */     }
/* 112 */     if (getConfigName() != null) {
/* 113 */       return String.valueOf((new File(getConfigName())).toURI());
/*     */     }
/* 115 */     return "";
/*     */   }
/*     */ 
/*     */   
/*     */   public void setConfigLocationUri(String configLocation) throws URISyntaxException, IOException {
/* 120 */     if (configLocation == null || configLocation.isEmpty()) {
/* 121 */       throw new IllegalArgumentException("Missing configuration location");
/*     */     }
/* 123 */     LOGGER.debug("---------");
/* 124 */     LOGGER.debug("Remote request to reconfigure using location " + configLocation);
/* 125 */     File configFile = new File(configLocation);
/* 126 */     ConfigurationSource configSource = null;
/* 127 */     if (configFile.exists()) {
/* 128 */       LOGGER.debug("Opening config file {}", new Object[] { configFile.getAbsolutePath() });
/* 129 */       configSource = new ConfigurationSource(new FileInputStream(configFile), configFile);
/*     */     } else {
/* 131 */       URL configURL = new URL(configLocation);
/* 132 */       LOGGER.debug("Opening config URL {}", new Object[] { configURL });
/* 133 */       configSource = new ConfigurationSource(configURL.openStream(), configURL);
/*     */     } 
/* 135 */     Configuration config = ConfigurationFactory.getInstance().getConfiguration(configSource);
/* 136 */     this.loggerContext.start(config);
/* 137 */     LOGGER.debug("Completed remote request to reconfigure.");
/*     */   }
/*     */ 
/*     */   
/*     */   public void propertyChange(PropertyChangeEvent evt) {
/* 142 */     if (!"config".equals(evt.getPropertyName())) {
/*     */       return;
/*     */     }
/* 145 */     Notification notif = new Notification("com.apache.logging.log4j.core.jmx.config.reconfigured", getObjectName(), nextSeqNo(), now(), null);
/* 146 */     sendNotification(notif);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getConfigText() throws IOException {
/* 151 */     return getConfigText(Charsets.UTF_8.name());
/*     */   }
/*     */ 
/*     */   
/*     */   public String getConfigText(String charsetName) throws IOException {
/*     */     try {
/* 157 */       ConfigurationSource source = this.loggerContext.getConfiguration().getConfigurationSource();
/* 158 */       ConfigurationSource copy = source.resetInputStream();
/* 159 */       Charset charset = Charset.forName(charsetName);
/* 160 */       return readContents(copy.getInputStream(), charset);
/* 161 */     } catch (Exception ex) {
/* 162 */       StringWriter sw = new StringWriter(2048);
/* 163 */       ex.printStackTrace(new PrintWriter(sw));
/* 164 */       return sw.toString();
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
/*     */   private String readContents(InputStream in, Charset charset) throws IOException {
/* 176 */     Reader reader = null;
/*     */     try {
/* 178 */       reader = new InputStreamReader(in, charset);
/* 179 */       StringBuilder result = new StringBuilder(65536);
/* 180 */       char[] buff = new char[4096];
/* 181 */       int count = -1;
/* 182 */       while ((count = reader.read(buff)) >= 0) {
/* 183 */         result.append(buff, 0, count);
/*     */       }
/* 185 */       return result.toString();
/*     */     } finally {
/* 187 */       Closer.closeSilently(in);
/* 188 */       Closer.closeSilently(reader);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void setConfigText(String configText, String charsetName) {
/* 194 */     LOGGER.debug("---------");
/* 195 */     LOGGER.debug("Remote request to reconfigure from config text.");
/*     */     
/*     */     try {
/* 198 */       InputStream in = new ByteArrayInputStream(configText.getBytes(charsetName));
/* 199 */       ConfigurationSource source = new ConfigurationSource(in);
/* 200 */       Configuration updated = ConfigurationFactory.getInstance().getConfiguration(source);
/* 201 */       this.loggerContext.start(updated);
/* 202 */       LOGGER.debug("Completed remote request to reconfigure from config text.");
/* 203 */     } catch (Exception ex) {
/* 204 */       String msg = "Could not reconfigure from config text";
/* 205 */       LOGGER.error("Could not reconfigure from config text", ex);
/* 206 */       throw new IllegalArgumentException("Could not reconfigure from config text", ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String getConfigName() {
/* 212 */     return getConfig().getName();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getConfigClassName() {
/* 217 */     return getConfig().getClass().getName();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getConfigFilter() {
/* 222 */     return String.valueOf(getConfig().getFilter());
/*     */   }
/*     */ 
/*     */   
/*     */   public String getConfigMonitorClassName() {
/* 227 */     return getConfig().getConfigurationMonitor().getClass().getName();
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<String, String> getConfigProperties() {
/* 232 */     return getConfig().getProperties();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ObjectName getObjectName() {
/* 243 */     return this.objectName;
/*     */   }
/*     */   
/*     */   private long nextSeqNo() {
/* 247 */     return this.sequenceNo.getAndIncrement();
/*     */   }
/*     */   
/*     */   private long now() {
/* 251 */     return System.currentTimeMillis();
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\jmx\LoggerContextAdmin.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */