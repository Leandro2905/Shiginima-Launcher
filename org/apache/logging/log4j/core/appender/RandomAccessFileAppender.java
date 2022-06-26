/*     */ package org.apache.logging.log4j.core.appender;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.apache.logging.log4j.core.Filter;
/*     */ import org.apache.logging.log4j.core.Layout;
/*     */ import org.apache.logging.log4j.core.LogEvent;
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
/*     */ @Plugin(name = "RandomAccessFile", category = "Core", elementType = "appender", printObject = true)
/*     */ public final class RandomAccessFileAppender
/*     */   extends AbstractOutputStreamAppender<RandomAccessFileManager>
/*     */ {
/*     */   private final String fileName;
/*     */   private Object advertisement;
/*     */   private final Advertiser advertiser;
/*     */   
/*     */   private RandomAccessFileAppender(String name, Layout<? extends Serializable> layout, Filter filter, RandomAccessFileManager manager, String filename, boolean ignoreExceptions, boolean immediateFlush, Advertiser advertiser) {
/*  50 */     super(name, layout, filter, ignoreExceptions, immediateFlush, manager);
/*  51 */     if (advertiser != null) {
/*  52 */       Map<String, String> configuration = new HashMap<String, String>(layout.getContentFormat());
/*     */       
/*  54 */       configuration.putAll(manager.getContentFormat());
/*  55 */       configuration.put("contentType", layout.getContentType());
/*  56 */       configuration.put("name", name);
/*  57 */       this.advertisement = advertiser.advertise(configuration);
/*     */     } 
/*  59 */     this.fileName = filename;
/*  60 */     this.advertiser = advertiser;
/*     */   }
/*     */ 
/*     */   
/*     */   public void stop() {
/*  65 */     super.stop();
/*  66 */     if (this.advertiser != null) {
/*  67 */       this.advertiser.unadvertise(this.advertisement);
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
/*     */   public void append(LogEvent event) {
/*  85 */     getManager().setEndOfBatch(event.isEndOfBatch());
/*  86 */     super.append(event);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFileName() {
/*  95 */     return this.fileName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getBufferSize() {
/* 103 */     return getManager().getBufferSize();
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
/*     */   public static RandomAccessFileAppender createAppender(@PluginAttribute("fileName") String fileName, @PluginAttribute("append") String append, @PluginAttribute("name") String name, @PluginAttribute("immediateFlush") String immediateFlush, @PluginAttribute("bufferSize") String bufferSizeStr, @PluginAttribute("ignoreExceptions") String ignore, @PluginElement("Layout") Layout<? extends Serializable> layout, @PluginElement("Filter") Filter filter, @PluginAttribute("advertise") String advertise, @PluginAttribute("advertiseURI") String advertiseURI, @PluginConfiguration Configuration config) {
/*     */     PatternLayout patternLayout;
/* 144 */     boolean isAppend = Booleans.parseBoolean(append, true);
/* 145 */     boolean isFlush = Booleans.parseBoolean(immediateFlush, true);
/* 146 */     boolean ignoreExceptions = Booleans.parseBoolean(ignore, true);
/* 147 */     boolean isAdvertise = Boolean.parseBoolean(advertise);
/* 148 */     int bufferSize = Integers.parseInt(bufferSizeStr, 262144);
/*     */     
/* 150 */     if (name == null) {
/* 151 */       LOGGER.error("No name provided for FileAppender");
/* 152 */       return null;
/*     */     } 
/*     */     
/* 155 */     if (fileName == null) {
/* 156 */       LOGGER.error("No filename provided for FileAppender with name " + name);
/*     */       
/* 158 */       return null;
/*     */     } 
/* 160 */     if (layout == null) {
/* 161 */       patternLayout = PatternLayout.createDefaultLayout();
/*     */     }
/* 163 */     RandomAccessFileManager manager = RandomAccessFileManager.getFileManager(fileName, isAppend, isFlush, bufferSize, advertiseURI, (Layout<? extends Serializable>)patternLayout);
/*     */ 
/*     */     
/* 166 */     if (manager == null) {
/* 167 */       return null;
/*     */     }
/*     */     
/* 170 */     return new RandomAccessFileAppender(name, (Layout<? extends Serializable>)patternLayout, filter, manager, fileName, ignoreExceptions, isFlush, isAdvertise ? config.getAdvertiser() : null);
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\appender\RandomAccessFileAppender.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */