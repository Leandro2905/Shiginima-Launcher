/*     */ package org.apache.logging.log4j.core.appender;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.apache.logging.log4j.core.Filter;
/*     */ import org.apache.logging.log4j.core.Layout;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.appender.rolling.DefaultRolloverStrategy;
/*     */ import org.apache.logging.log4j.core.appender.rolling.RollingFileManager;
/*     */ import org.apache.logging.log4j.core.appender.rolling.RolloverStrategy;
/*     */ import org.apache.logging.log4j.core.appender.rolling.TriggeringPolicy;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginElement;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginFactory;
/*     */ import org.apache.logging.log4j.core.layout.PatternLayout;
/*     */ import org.apache.logging.log4j.core.net.Advertiser;
/*     */ import org.apache.logging.log4j.core.util.Booleans;
/*     */ import org.apache.logging.log4j.core.util.Integers;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Plugin(name = "RollingFile", category = "Core", elementType = "appender", printObject = true)
/*     */ public final class RollingFileAppender
/*     */   extends AbstractOutputStreamAppender<RollingFileManager>
/*     */ {
/*     */   private static final int DEFAULT_BUFFER_SIZE = 8192;
/*     */   private final String fileName;
/*     */   private final String filePattern;
/*     */   private Object advertisement;
/*     */   private final Advertiser advertiser;
/*     */   
/*     */   private RollingFileAppender(String name, Layout<? extends Serializable> layout, Filter filter, RollingFileManager manager, String fileName, String filePattern, boolean ignoreExceptions, boolean immediateFlush, Advertiser advertiser) {
/*  58 */     super(name, layout, filter, ignoreExceptions, immediateFlush, manager);
/*  59 */     if (advertiser != null) {
/*  60 */       Map<String, String> configuration = new HashMap<String, String>(layout.getContentFormat());
/*  61 */       configuration.put("contentType", layout.getContentType());
/*  62 */       configuration.put("name", name);
/*  63 */       this.advertisement = advertiser.advertise(configuration);
/*     */     } 
/*  65 */     this.fileName = fileName;
/*  66 */     this.filePattern = filePattern;
/*  67 */     this.advertiser = advertiser;
/*     */   }
/*     */ 
/*     */   
/*     */   public void stop() {
/*  72 */     super.stop();
/*  73 */     if (this.advertiser != null) {
/*  74 */       this.advertiser.unadvertise(this.advertisement);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void append(LogEvent event) {
/*  85 */     getManager().checkRollover(event);
/*  86 */     super.append(event);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFileName() {
/*  94 */     return this.fileName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFilePattern() {
/* 102 */     return this.filePattern;
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
/*     */   @PluginFactory
/*     */   public static RollingFileAppender createAppender(@PluginAttribute("fileName") String fileName, @PluginAttribute("filePattern") String filePattern, @PluginAttribute("append") String append, @PluginAttribute("name") String name, @PluginAttribute("bufferedIO") String bufferedIO, @PluginAttribute("bufferSize") String bufferSizeStr, @PluginAttribute("immediateFlush") String immediateFlush, @PluginElement("Policy") TriggeringPolicy policy, @PluginElement("Strategy") RolloverStrategy strategy, @PluginElement("Layout") Layout<? extends Serializable> layout, @PluginElement("Filter") Filter filter, @PluginAttribute("ignoreExceptions") String ignore, @PluginAttribute("advertise") String advertise, @PluginAttribute("advertiseURI") String advertiseURI, @PluginConfiguration Configuration config) {
/*     */     DefaultRolloverStrategy defaultRolloverStrategy;
/*     */     PatternLayout patternLayout;
/* 144 */     boolean isAppend = Booleans.parseBoolean(append, true);
/* 145 */     boolean ignoreExceptions = Booleans.parseBoolean(ignore, true);
/* 146 */     boolean isBuffered = Booleans.parseBoolean(bufferedIO, true);
/* 147 */     boolean isFlush = Booleans.parseBoolean(immediateFlush, true);
/* 148 */     boolean isAdvertise = Boolean.parseBoolean(advertise);
/* 149 */     int bufferSize = Integers.parseInt(bufferSizeStr, 8192);
/* 150 */     if (!isBuffered && bufferSize > 0) {
/* 151 */       LOGGER.warn("The bufferSize is set to {} but bufferedIO is not true: {}", new Object[] { Integer.valueOf(bufferSize), bufferedIO });
/*     */     }
/* 153 */     if (name == null) {
/* 154 */       LOGGER.error("No name provided for FileAppender");
/* 155 */       return null;
/*     */     } 
/*     */     
/* 158 */     if (fileName == null) {
/* 159 */       LOGGER.error("No filename was provided for FileAppender with name " + name);
/* 160 */       return null;
/*     */     } 
/*     */     
/* 163 */     if (filePattern == null) {
/* 164 */       LOGGER.error("No filename pattern provided for FileAppender with name " + name);
/* 165 */       return null;
/*     */     } 
/*     */     
/* 168 */     if (policy == null) {
/* 169 */       LOGGER.error("A TriggeringPolicy must be provided");
/* 170 */       return null;
/*     */     } 
/*     */     
/* 173 */     if (strategy == null) {
/* 174 */       defaultRolloverStrategy = DefaultRolloverStrategy.createStrategy(null, null, null, String.valueOf(-1), config);
/*     */     }
/*     */ 
/*     */     
/* 178 */     if (layout == null) {
/* 179 */       patternLayout = PatternLayout.createDefaultLayout();
/*     */     }
/*     */     
/* 182 */     RollingFileManager manager = RollingFileManager.getFileManager(fileName, filePattern, isAppend, isBuffered, policy, (RolloverStrategy)defaultRolloverStrategy, advertiseURI, (Layout)patternLayout, bufferSize);
/*     */     
/* 184 */     if (manager == null) {
/* 185 */       return null;
/*     */     }
/*     */     
/* 188 */     return new RollingFileAppender(name, (Layout<? extends Serializable>)patternLayout, filter, manager, fileName, filePattern, ignoreExceptions, isFlush, isAdvertise ? config.getAdvertiser() : null);
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\appender\RollingFileAppender.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */