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
/*     */ import org.apache.logging.log4j.core.appender.rolling.RollingRandomAccessFileManager;
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
/*     */ 
/*     */ @Plugin(name = "RollingRandomAccessFile", category = "Core", elementType = "appender", printObject = true)
/*     */ public final class RollingRandomAccessFileAppender
/*     */   extends AbstractOutputStreamAppender<RollingFileManager>
/*     */ {
/*     */   private final String fileName;
/*     */   private final String filePattern;
/*     */   private Object advertisement;
/*     */   private final Advertiser advertiser;
/*     */   
/*     */   private RollingRandomAccessFileAppender(String name, Layout<? extends Serializable> layout, Filter filter, RollingFileManager manager, String fileName, String filePattern, boolean ignoreExceptions, boolean immediateFlush, int bufferSize, Advertiser advertiser) {
/*  59 */     super(name, layout, filter, ignoreExceptions, immediateFlush, manager);
/*  60 */     if (advertiser != null) {
/*  61 */       Map<String, String> configuration = new HashMap<String, String>(layout.getContentFormat());
/*  62 */       configuration.put("contentType", layout.getContentType());
/*  63 */       configuration.put("name", name);
/*  64 */       this.advertisement = advertiser.advertise(configuration);
/*     */     } 
/*  66 */     this.fileName = fileName;
/*  67 */     this.filePattern = filePattern;
/*  68 */     this.advertiser = advertiser;
/*     */   }
/*     */ 
/*     */   
/*     */   public void stop() {
/*  73 */     super.stop();
/*  74 */     if (this.advertiser != null) {
/*  75 */       this.advertiser.unadvertise(this.advertisement);
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
/*  86 */     RollingRandomAccessFileManager manager = (RollingRandomAccessFileManager)getManager();
/*  87 */     manager.checkRollover(event);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  95 */     manager.setEndOfBatch(event.isEndOfBatch());
/*  96 */     super.append(event);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFileName() {
/* 105 */     return this.fileName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFilePattern() {
/* 114 */     return this.filePattern;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getBufferSize() {
/* 122 */     return ((RollingRandomAccessFileManager)getManager()).getBufferSize();
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
/*     */ 
/*     */ 
/*     */   
/*     */   @PluginFactory
/*     */   public static RollingRandomAccessFileAppender createAppender(@PluginAttribute("fileName") String fileName, @PluginAttribute("filePattern") String filePattern, @PluginAttribute("append") String append, @PluginAttribute("name") String name, @PluginAttribute("immediateFlush") String immediateFlush, @PluginAttribute("bufferSize") String bufferSizeStr, @PluginElement("Policy") TriggeringPolicy policy, @PluginElement("Strategy") RolloverStrategy strategy, @PluginElement("Layout") Layout<? extends Serializable> layout, @PluginElement("Filter") Filter filter, @PluginAttribute("ignoreExceptions") String ignore, @PluginAttribute("advertise") String advertise, @PluginAttribute("advertiseURI") String advertiseURI, @PluginConfiguration Configuration config) {
/*     */     DefaultRolloverStrategy defaultRolloverStrategy;
/*     */     PatternLayout patternLayout;
/* 169 */     boolean isAppend = Booleans.parseBoolean(append, true);
/* 170 */     boolean ignoreExceptions = Booleans.parseBoolean(ignore, true);
/* 171 */     boolean isFlush = Booleans.parseBoolean(immediateFlush, true);
/* 172 */     boolean isAdvertise = Boolean.parseBoolean(advertise);
/* 173 */     int bufferSize = Integers.parseInt(bufferSizeStr, 262144);
/*     */     
/* 175 */     if (name == null) {
/* 176 */       LOGGER.error("No name provided for FileAppender");
/* 177 */       return null;
/*     */     } 
/*     */     
/* 180 */     if (fileName == null) {
/* 181 */       LOGGER.error("No filename was provided for FileAppender with name " + name);
/* 182 */       return null;
/*     */     } 
/*     */     
/* 185 */     if (filePattern == null) {
/* 186 */       LOGGER.error("No filename pattern provided for FileAppender with name " + name);
/* 187 */       return null;
/*     */     } 
/*     */     
/* 190 */     if (policy == null) {
/* 191 */       LOGGER.error("A TriggeringPolicy must be provided");
/* 192 */       return null;
/*     */     } 
/*     */     
/* 195 */     if (strategy == null) {
/* 196 */       defaultRolloverStrategy = DefaultRolloverStrategy.createStrategy(null, null, null, String.valueOf(-1), config);
/*     */     }
/*     */ 
/*     */     
/* 200 */     if (layout == null) {
/* 201 */       patternLayout = PatternLayout.createDefaultLayout();
/*     */     }
/*     */     
/* 204 */     RollingRandomAccessFileManager manager = RollingRandomAccessFileManager.getRollingRandomAccessFileManager(fileName, filePattern, isAppend, isFlush, bufferSize, policy, (RolloverStrategy)defaultRolloverStrategy, advertiseURI, (Layout)patternLayout);
/*     */     
/* 206 */     if (manager == null) {
/* 207 */       return null;
/*     */     }
/*     */     
/* 210 */     return new RollingRandomAccessFileAppender(name, (Layout<? extends Serializable>)patternLayout, filter, (RollingFileManager)manager, fileName, filePattern, ignoreExceptions, isFlush, bufferSize, isAdvertise ? config.getAdvertiser() : null);
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\appender\RollingRandomAccessFileAppender.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */