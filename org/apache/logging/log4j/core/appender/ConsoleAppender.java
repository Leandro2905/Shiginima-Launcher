/*     */ package org.apache.logging.log4j.core.appender;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.io.Serializable;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.nio.charset.Charset;
/*     */ import org.apache.logging.log4j.core.Filter;
/*     */ import org.apache.logging.log4j.core.Layout;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginElement;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginFactory;
/*     */ import org.apache.logging.log4j.core.layout.PatternLayout;
/*     */ import org.apache.logging.log4j.core.util.Booleans;
/*     */ import org.apache.logging.log4j.core.util.Loader;
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
/*     */ @Plugin(name = "Console", category = "Core", elementType = "appender", printObject = true)
/*     */ public final class ConsoleAppender
/*     */   extends AbstractOutputStreamAppender<OutputStreamManager>
/*     */ {
/*     */   private static final String JANSI_CLASS = "org.fusesource.jansi.WindowsAnsiOutputStream";
/*  52 */   private static ConsoleManagerFactory factory = new ConsoleManagerFactory();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public enum Target
/*     */   {
/*  59 */     SYSTEM_OUT,
/*     */     
/*  61 */     SYSTEM_ERR;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private ConsoleAppender(String name, Layout<? extends Serializable> layout, Filter filter, OutputStreamManager manager, boolean ignoreExceptions) {
/*  67 */     super(name, layout, filter, ignoreExceptions, true, manager);
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
/*     */   @PluginFactory
/*     */   public static ConsoleAppender createAppender(@PluginElement("Layout") Layout<? extends Serializable> layout, @PluginElement("Filter") Filter filter, @PluginAttribute(value = "target", defaultString = "SYSTEM_OUT") String targetStr, @PluginAttribute("name") String name, @PluginAttribute(value = "follow", defaultBoolean = false) String follow, @PluginAttribute(value = "ignoreExceptions", defaultBoolean = true) String ignore) {
/*     */     PatternLayout patternLayout;
/*  89 */     if (name == null) {
/*  90 */       LOGGER.error("No name provided for ConsoleAppender");
/*  91 */       return null;
/*     */     } 
/*  93 */     if (layout == null) {
/*  94 */       patternLayout = PatternLayout.createDefaultLayout();
/*     */     }
/*  96 */     boolean isFollow = Boolean.parseBoolean(follow);
/*  97 */     boolean ignoreExceptions = Booleans.parseBoolean(ignore, true);
/*  98 */     Target target = (targetStr == null) ? Target.SYSTEM_OUT : Target.valueOf(targetStr);
/*  99 */     return new ConsoleAppender(name, (Layout<? extends Serializable>)patternLayout, filter, getManager(isFollow, target, (Layout<? extends Serializable>)patternLayout), ignoreExceptions);
/*     */   }
/*     */   
/*     */   private static OutputStreamManager getManager(boolean follow, Target target, Layout<? extends Serializable> layout) {
/* 103 */     String type = target.name();
/* 104 */     OutputStream os = getOutputStream(follow, target);
/* 105 */     return OutputStreamManager.getManager(target.name() + '.' + follow, new FactoryData(os, type, layout), factory);
/*     */   }
/*     */   
/*     */   private static OutputStream getOutputStream(boolean follow, Target target) {
/* 109 */     String enc = Charset.defaultCharset().name();
/* 110 */     PrintStream printStream = null;
/*     */     try {
/* 112 */       printStream = (target == Target.SYSTEM_OUT) ? (follow ? new PrintStream(new SystemOutStream(), true, enc) : System.out) : (follow ? new PrintStream(new SystemErrStream(), true, enc) : System.err);
/*     */     
/*     */     }
/* 115 */     catch (UnsupportedEncodingException ex) {
/* 116 */       throw new IllegalStateException("Unsupported default encoding " + enc, ex);
/*     */     } 
/* 118 */     PropertiesUtil propsUtil = PropertiesUtil.getProperties();
/* 119 */     if (!propsUtil.getStringProperty("os.name").startsWith("Windows") || propsUtil.getBooleanProperty("log4j.skipJansi"))
/*     */     {
/* 121 */       return printStream;
/*     */     }
/*     */     try {
/* 124 */       ClassLoader loader = Loader.getClassLoader();
/*     */       
/* 126 */       Class<?> clazz = loader.loadClass("org.fusesource.jansi.WindowsAnsiOutputStream");
/* 127 */       Constructor<?> constructor = clazz.getConstructor(new Class[] { OutputStream.class });
/* 128 */       return (OutputStream)constructor.newInstance(new Object[] { printStream });
/* 129 */     } catch (ClassNotFoundException cnfe) {
/* 130 */       LOGGER.debug("Jansi is not installed, cannot find {}", new Object[] { "org.fusesource.jansi.WindowsAnsiOutputStream" });
/* 131 */     } catch (NoSuchMethodException nsme) {
/* 132 */       LOGGER.warn("{} is missing the proper constructor", new Object[] { "org.fusesource.jansi.WindowsAnsiOutputStream" });
/* 133 */     } catch (Exception ex) {
/* 134 */       LOGGER.warn("Unable to instantiate {}", new Object[] { "org.fusesource.jansi.WindowsAnsiOutputStream" });
/*     */     } 
/* 136 */     return printStream;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class SystemErrStream
/*     */     extends OutputStream
/*     */   {
/*     */     public void close() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void flush() {
/* 153 */       System.err.flush();
/*     */     }
/*     */ 
/*     */     
/*     */     public void write(byte[] b) throws IOException {
/* 158 */       System.err.write(b);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void write(byte[] b, int off, int len) throws IOException {
/* 164 */       System.err.write(b, off, len);
/*     */     }
/*     */ 
/*     */     
/*     */     public void write(int b) {
/* 169 */       System.err.write(b);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class SystemOutStream
/*     */     extends OutputStream
/*     */   {
/*     */     public void close() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void flush() {
/* 187 */       System.out.flush();
/*     */     }
/*     */ 
/*     */     
/*     */     public void write(byte[] b) throws IOException {
/* 192 */       System.out.write(b);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void write(byte[] b, int off, int len) throws IOException {
/* 198 */       System.out.write(b, off, len);
/*     */     }
/*     */ 
/*     */     
/*     */     public void write(int b) throws IOException {
/* 203 */       System.out.write(b);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class FactoryData
/*     */   {
/*     */     private final OutputStream os;
/*     */ 
/*     */     
/*     */     private final String type;
/*     */ 
/*     */     
/*     */     private final Layout<? extends Serializable> layout;
/*     */ 
/*     */ 
/*     */     
/*     */     public FactoryData(OutputStream os, String type, Layout<? extends Serializable> layout) {
/* 222 */       this.os = os;
/* 223 */       this.type = type;
/* 224 */       this.layout = layout;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class ConsoleManagerFactory
/*     */     implements ManagerFactory<OutputStreamManager, FactoryData>
/*     */   {
/*     */     private ConsoleManagerFactory() {}
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public OutputStreamManager createManager(String name, ConsoleAppender.FactoryData data) {
/* 241 */       return new OutputStreamManager(data.os, data.type, data.layout);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\appender\ConsoleAppender.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */