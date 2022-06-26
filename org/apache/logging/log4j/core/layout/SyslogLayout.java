/*     */ package org.apache.logging.log4j.core.layout;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.nio.charset.Charset;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginFactory;
/*     */ import org.apache.logging.log4j.core.net.Facility;
/*     */ import org.apache.logging.log4j.core.net.Priority;
/*     */ import org.apache.logging.log4j.core.util.NetUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Plugin(name = "SyslogLayout", category = "Core", elementType = "layout", printObject = true)
/*     */ public final class SyslogLayout
/*     */   extends AbstractStringLayout
/*     */ {
/*  45 */   public static final Pattern NEWLINE_PATTERN = Pattern.compile("\\r?\\n");
/*     */ 
/*     */   
/*     */   private final Facility facility;
/*     */   
/*     */   private final boolean includeNewLine;
/*     */   
/*     */   private final String escapeNewLine;
/*     */   
/*  54 */   private final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd HH:mm:ss ", Locale.ENGLISH);
/*     */ 
/*     */ 
/*     */   
/*  58 */   private final String localHostname = NetUtils.getLocalHostname();
/*     */ 
/*     */ 
/*     */   
/*     */   protected SyslogLayout(Facility facility, boolean includeNL, String escapeNL, Charset charset) {
/*  63 */     super(charset);
/*  64 */     this.facility = facility;
/*  65 */     this.includeNewLine = includeNL;
/*  66 */     this.escapeNewLine = (escapeNL == null) ? null : Matcher.quoteReplacement(escapeNL);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toSerializable(LogEvent event) {
/*  77 */     StringBuilder buf = new StringBuilder();
/*     */     
/*  79 */     buf.append('<');
/*  80 */     buf.append(Priority.getPriority(this.facility, event.getLevel()));
/*  81 */     buf.append('>');
/*  82 */     addDate(event.getTimeMillis(), buf);
/*  83 */     buf.append(' ');
/*  84 */     buf.append(this.localHostname);
/*  85 */     buf.append(' ');
/*     */     
/*  87 */     String message = event.getMessage().getFormattedMessage();
/*  88 */     if (null != this.escapeNewLine) {
/*  89 */       message = NEWLINE_PATTERN.matcher(message).replaceAll(this.escapeNewLine);
/*     */     }
/*  91 */     buf.append(message);
/*     */     
/*  93 */     if (this.includeNewLine) {
/*  94 */       buf.append('\n');
/*     */     }
/*  96 */     return buf.toString();
/*     */   }
/*     */   
/*     */   private synchronized void addDate(long timestamp, StringBuilder buf) {
/* 100 */     int index = buf.length() + 4;
/* 101 */     buf.append(this.dateFormat.format(new Date(timestamp)));
/*     */     
/* 103 */     if (buf.charAt(index) == '0') {
/* 104 */       buf.setCharAt(index, ' ');
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
/*     */   public Map<String, String> getContentFormat() {
/* 119 */     Map<String, String> result = new HashMap<String, String>();
/* 120 */     result.put("structured", "false");
/* 121 */     result.put("formatType", "logfilepatternreceiver");
/* 122 */     result.put("dateFormat", this.dateFormat.toPattern());
/* 123 */     result.put("format", "<LEVEL>TIMESTAMP PROP(HOSTNAME) MESSAGE");
/* 124 */     return result;
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
/*     */   @PluginFactory
/*     */   public static SyslogLayout createLayout(@PluginAttribute(value = "facility", defaultString = "LOCAL0") Facility facility, @PluginAttribute(value = "newLine", defaultBoolean = false) boolean includeNewLine, @PluginAttribute("newLineEscape") String escapeNL, @PluginAttribute(value = "charset", defaultString = "UTF-8") Charset charset) {
/* 141 */     return new SyslogLayout(facility, includeNewLine, escapeNL, charset);
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\layout\SyslogLayout.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */