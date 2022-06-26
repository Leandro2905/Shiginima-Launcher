/*     */ package org.apache.logging.log4j.core.appender;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.apache.logging.log4j.core.Filter;
/*     */ import org.apache.logging.log4j.core.Layout;
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
/*     */ @Plugin(name = "File", category = "Core", elementType = "appender", printObject = true)
/*     */ public final class FileAppender
/*     */   extends AbstractOutputStreamAppender<FileManager>
/*     */ {
/*     */   private static final int DEFAULT_BUFFER_SIZE = 8192;
/*     */   private final String fileName;
/*     */   private final Advertiser advertiser;
/*     */   private Object advertisement;
/*     */   
/*     */   private FileAppender(String name, Layout<? extends Serializable> layout, Filter filter, FileManager manager, String filename, boolean ignoreExceptions, boolean immediateFlush, Advertiser advertiser) {
/*  50 */     super(name, layout, filter, ignoreExceptions, immediateFlush, manager);
/*  51 */     if (advertiser != null) {
/*  52 */       Map<String, String> configuration = new HashMap<String, String>(layout.getContentFormat());
/*  53 */       configuration.putAll(manager.getContentFormat());
/*  54 */       configuration.put("contentType", layout.getContentType());
/*  55 */       configuration.put("name", name);
/*  56 */       this.advertisement = advertiser.advertise(configuration);
/*     */     } 
/*  58 */     this.fileName = filename;
/*  59 */     this.advertiser = advertiser;
/*     */   }
/*     */ 
/*     */   
/*     */   public void stop() {
/*  64 */     super.stop();
/*  65 */     if (this.advertiser != null) {
/*  66 */       this.advertiser.unadvertise(this.advertisement);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFileName() {
/*  75 */     return this.fileName;
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
/*     */   public static FileAppender createAppender(@PluginAttribute("fileName") String fileName, @PluginAttribute("append") String append, @PluginAttribute("locking") String locking, @PluginAttribute("name") String name, @PluginAttribute("immediateFlush") String immediateFlush, @PluginAttribute("ignoreExceptions") String ignore, @PluginAttribute("bufferedIo") String bufferedIo, @PluginAttribute("bufferSize") String bufferSizeStr, @PluginElement("Layout") Layout<? extends Serializable> layout, @PluginElement("Filter") Filter filter, @PluginAttribute("advertise") String advertise, @PluginAttribute("advertiseUri") String advertiseUri, @PluginConfiguration Configuration config) {
/*     */     PatternLayout patternLayout;
/* 116 */     boolean isAppend = Booleans.parseBoolean(append, true);
/* 117 */     boolean isLocking = Boolean.parseBoolean(locking);
/* 118 */     boolean isBuffered = Booleans.parseBoolean(bufferedIo, true);
/* 119 */     boolean isAdvertise = Boolean.parseBoolean(advertise);
/* 120 */     if (isLocking && isBuffered) {
/* 121 */       if (bufferedIo != null) {
/* 122 */         LOGGER.warn("Locking and buffering are mutually exclusive. No buffering will occur for " + fileName);
/*     */       }
/* 124 */       isBuffered = false;
/*     */     } 
/* 126 */     int bufferSize = Integers.parseInt(bufferSizeStr, 8192);
/* 127 */     if (!isBuffered && bufferSize > 0) {
/* 128 */       LOGGER.warn("The bufferSize is set to {} but bufferedIO is not true: {}", new Object[] { Integer.valueOf(bufferSize), bufferedIo });
/*     */     }
/* 130 */     boolean isFlush = Booleans.parseBoolean(immediateFlush, true);
/* 131 */     boolean ignoreExceptions = Booleans.parseBoolean(ignore, true);
/*     */     
/* 133 */     if (name == null) {
/* 134 */       LOGGER.error("No name provided for FileAppender");
/* 135 */       return null;
/*     */     } 
/*     */     
/* 138 */     if (fileName == null) {
/* 139 */       LOGGER.error("No filename provided for FileAppender with name " + name);
/* 140 */       return null;
/*     */     } 
/* 142 */     if (layout == null) {
/* 143 */       patternLayout = PatternLayout.createDefaultLayout();
/*     */     }
/*     */     
/* 146 */     FileManager manager = FileManager.getFileManager(fileName, isAppend, isLocking, isBuffered, advertiseUri, (Layout<? extends Serializable>)patternLayout, bufferSize);
/*     */     
/* 148 */     if (manager == null) {
/* 149 */       return null;
/*     */     }
/*     */     
/* 152 */     return new FileAppender(name, (Layout<? extends Serializable>)patternLayout, filter, manager, fileName, ignoreExceptions, isFlush, isAdvertise ? config.getAdvertiser() : null);
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\appender\FileAppender.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */