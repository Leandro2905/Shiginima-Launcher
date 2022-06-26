/*     */ package org.apache.logging.log4j.core.layout;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.LineNumberReader;
/*     */ import java.io.PrintWriter;
/*     */ import java.io.Serializable;
/*     */ import java.io.StringReader;
/*     */ import java.io.StringWriter;
/*     */ import java.lang.management.ManagementFactory;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.apache.logging.log4j.Level;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginFactory;
/*     */ import org.apache.logging.log4j.core.util.Charsets;
/*     */ import org.apache.logging.log4j.core.util.Constants;
/*     */ import org.apache.logging.log4j.core.util.Transform;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Plugin(name = "HtmlLayout", category = "Core", elementType = "layout", printObject = true)
/*     */ public final class HtmlLayout
/*     */   extends AbstractStringLayout
/*     */ {
/*     */   private static final int BUF_SIZE = 256;
/*     */   private static final String TRACE_PREFIX = "<br />&nbsp;&nbsp;&nbsp;&nbsp;";
/*  55 */   private static final String REGEXP = Constants.LINE_SEPARATOR.equals("\n") ? "\n" : (Constants.LINE_SEPARATOR + "|\n");
/*     */   
/*     */   private static final String DEFAULT_TITLE = "Log4j Log Messages";
/*     */   
/*     */   private static final String DEFAULT_CONTENT_TYPE = "text/html";
/*     */   
/*     */   public static final String DEFAULT_FONT_FAMILY = "arial,sans-serif";
/*     */   
/*  63 */   private final long jvmStartTime = ManagementFactory.getRuntimeMXBean().getStartTime();
/*     */   
/*     */   private final boolean locationInfo;
/*     */   private final String title;
/*     */   private final String contentType;
/*     */   private final String font;
/*     */   private final String fontSize;
/*     */   private final String headerSize;
/*     */   
/*     */   public enum FontSize
/*     */   {
/*  74 */     SMALLER("smaller"), XXSMALL("xx-small"), XSMALL("x-small"), SMALL("small"), MEDIUM("medium"), LARGE("large"),
/*  75 */     XLARGE("x-large"), XXLARGE("xx-large"), LARGER("larger");
/*     */     
/*     */     private final String size;
/*     */     
/*     */     FontSize(String size) {
/*  80 */       this.size = size;
/*     */     }
/*     */     
/*     */     public String getFontSize() {
/*  84 */       return this.size;
/*     */     }
/*     */     
/*     */     public static FontSize getFontSize(String size) {
/*  88 */       for (FontSize fontSize : values()) {
/*  89 */         if (fontSize.size.equals(size)) {
/*  90 */           return fontSize;
/*     */         }
/*     */       } 
/*  93 */       return SMALL;
/*     */     }
/*     */     
/*     */     public FontSize larger() {
/*  97 */       return (ordinal() < XXLARGE.ordinal()) ? values()[ordinal() + 1] : this;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private HtmlLayout(boolean locationInfo, String title, String contentType, Charset charset, String font, String fontSize, String headerSize) {
/* 107 */     super(charset);
/* 108 */     this.locationInfo = locationInfo;
/* 109 */     this.title = title;
/* 110 */     this.contentType = addCharsetToContentType(contentType);
/* 111 */     this.font = font;
/* 112 */     this.fontSize = fontSize;
/* 113 */     this.headerSize = headerSize;
/*     */   }
/*     */   
/*     */   private String addCharsetToContentType(String contentType) {
/* 117 */     if (contentType == null) {
/* 118 */       return "text/html; charset=" + getCharset();
/*     */     }
/* 120 */     return contentType.contains("charset") ? contentType : (contentType + "; charset=" + getCharset());
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
/* 131 */     StringBuilder sbuf = new StringBuilder(256);
/*     */     
/* 133 */     sbuf.append(Constants.LINE_SEPARATOR).append("<tr>").append(Constants.LINE_SEPARATOR);
/*     */     
/* 135 */     sbuf.append("<td>");
/* 136 */     sbuf.append(event.getTimeMillis() - this.jvmStartTime);
/* 137 */     sbuf.append("</td>").append(Constants.LINE_SEPARATOR);
/*     */     
/* 139 */     String escapedThread = Transform.escapeHtmlTags(event.getThreadName());
/* 140 */     sbuf.append("<td title=\"").append(escapedThread).append(" thread\">");
/* 141 */     sbuf.append(escapedThread);
/* 142 */     sbuf.append("</td>").append(Constants.LINE_SEPARATOR);
/*     */     
/* 144 */     sbuf.append("<td title=\"Level\">");
/* 145 */     if (event.getLevel().equals(Level.DEBUG)) {
/* 146 */       sbuf.append("<font color=\"#339933\">");
/* 147 */       sbuf.append(Transform.escapeHtmlTags(String.valueOf(event.getLevel())));
/* 148 */       sbuf.append("</font>");
/* 149 */     } else if (event.getLevel().isMoreSpecificThan(Level.WARN)) {
/* 150 */       sbuf.append("<font color=\"#993300\"><strong>");
/* 151 */       sbuf.append(Transform.escapeHtmlTags(String.valueOf(event.getLevel())));
/* 152 */       sbuf.append("</strong></font>");
/*     */     } else {
/* 154 */       sbuf.append(Transform.escapeHtmlTags(String.valueOf(event.getLevel())));
/*     */     } 
/* 156 */     sbuf.append("</td>").append(Constants.LINE_SEPARATOR);
/*     */     
/* 158 */     String escapedLogger = Transform.escapeHtmlTags(event.getLoggerName());
/* 159 */     if (escapedLogger.isEmpty()) {
/* 160 */       escapedLogger = "root";
/*     */     }
/* 162 */     sbuf.append("<td title=\"").append(escapedLogger).append(" logger\">");
/* 163 */     sbuf.append(escapedLogger);
/* 164 */     sbuf.append("</td>").append(Constants.LINE_SEPARATOR);
/*     */     
/* 166 */     if (this.locationInfo) {
/* 167 */       StackTraceElement element = event.getSource();
/* 168 */       sbuf.append("<td>");
/* 169 */       sbuf.append(Transform.escapeHtmlTags(element.getFileName()));
/* 170 */       sbuf.append(':');
/* 171 */       sbuf.append(element.getLineNumber());
/* 172 */       sbuf.append("</td>").append(Constants.LINE_SEPARATOR);
/*     */     } 
/*     */     
/* 175 */     sbuf.append("<td title=\"Message\">");
/* 176 */     sbuf.append(Transform.escapeHtmlTags(event.getMessage().getFormattedMessage()).replaceAll(REGEXP, "<br />"));
/* 177 */     sbuf.append("</td>").append(Constants.LINE_SEPARATOR);
/* 178 */     sbuf.append("</tr>").append(Constants.LINE_SEPARATOR);
/*     */     
/* 180 */     if (event.getContextStack() != null && !event.getContextStack().isEmpty()) {
/* 181 */       sbuf.append("<tr><td bgcolor=\"#EEEEEE\" style=\"font-size : ").append(this.fontSize);
/* 182 */       sbuf.append(";\" colspan=\"6\" ");
/* 183 */       sbuf.append("title=\"Nested Diagnostic Context\">");
/* 184 */       sbuf.append("NDC: ").append(Transform.escapeHtmlTags(event.getContextStack().toString()));
/* 185 */       sbuf.append("</td></tr>").append(Constants.LINE_SEPARATOR);
/*     */     } 
/*     */     
/* 188 */     if (event.getContextMap() != null && !event.getContextMap().isEmpty()) {
/* 189 */       sbuf.append("<tr><td bgcolor=\"#EEEEEE\" style=\"font-size : ").append(this.fontSize);
/* 190 */       sbuf.append(";\" colspan=\"6\" ");
/* 191 */       sbuf.append("title=\"Mapped Diagnostic Context\">");
/* 192 */       sbuf.append("MDC: ").append(Transform.escapeHtmlTags(event.getContextMap().toString()));
/* 193 */       sbuf.append("</td></tr>").append(Constants.LINE_SEPARATOR);
/*     */     } 
/*     */     
/* 196 */     Throwable throwable = event.getThrown();
/* 197 */     if (throwable != null) {
/* 198 */       sbuf.append("<tr><td bgcolor=\"#993300\" style=\"color:White; font-size : ").append(this.fontSize);
/* 199 */       sbuf.append(";\" colspan=\"6\">");
/* 200 */       appendThrowableAsHtml(throwable, sbuf);
/* 201 */       sbuf.append("</td></tr>").append(Constants.LINE_SEPARATOR);
/*     */     } 
/*     */     
/* 204 */     return sbuf.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, String> getContentFormat() {
/* 214 */     return new HashMap<String, String>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getContentType() {
/* 222 */     return this.contentType;
/*     */   }
/*     */   
/*     */   private void appendThrowableAsHtml(Throwable throwable, StringBuilder sbuf) {
/* 226 */     StringWriter sw = new StringWriter();
/* 227 */     PrintWriter pw = new PrintWriter(sw);
/*     */     try {
/* 229 */       throwable.printStackTrace(pw);
/* 230 */     } catch (RuntimeException ex) {}
/*     */ 
/*     */     
/* 233 */     pw.flush();
/* 234 */     LineNumberReader reader = new LineNumberReader(new StringReader(sw.toString()));
/* 235 */     ArrayList<String> lines = new ArrayList<String>();
/*     */     try {
/* 237 */       String line = reader.readLine();
/* 238 */       while (line != null) {
/* 239 */         lines.add(line);
/* 240 */         line = reader.readLine();
/*     */       } 
/* 242 */     } catch (IOException ex) {
/* 243 */       if (ex instanceof java.io.InterruptedIOException) {
/* 244 */         Thread.currentThread().interrupt();
/*     */       }
/* 246 */       lines.add(ex.toString());
/*     */     } 
/* 248 */     boolean first = true;
/* 249 */     for (String line : lines) {
/* 250 */       if (!first) {
/* 251 */         sbuf.append("<br />&nbsp;&nbsp;&nbsp;&nbsp;");
/*     */       } else {
/* 253 */         first = false;
/*     */       } 
/* 255 */       sbuf.append(Transform.escapeHtmlTags(line));
/* 256 */       sbuf.append(Constants.LINE_SEPARATOR);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] getHeader() {
/* 266 */     StringBuilder sbuf = new StringBuilder();
/* 267 */     sbuf.append("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" ");
/* 268 */     sbuf.append("\"http://www.w3.org/TR/html4/loose.dtd\">");
/* 269 */     sbuf.append(Constants.LINE_SEPARATOR);
/* 270 */     sbuf.append("<html>").append(Constants.LINE_SEPARATOR);
/* 271 */     sbuf.append("<head>").append(Constants.LINE_SEPARATOR);
/* 272 */     sbuf.append("<meta charset=\"").append(getCharset()).append("\"/>").append(Constants.LINE_SEPARATOR);
/* 273 */     sbuf.append("<title>").append(this.title).append("</title>").append(Constants.LINE_SEPARATOR);
/* 274 */     sbuf.append("<style type=\"text/css\">").append(Constants.LINE_SEPARATOR);
/* 275 */     sbuf.append("<!--").append(Constants.LINE_SEPARATOR);
/* 276 */     sbuf.append("body, table {font-family:").append(this.font).append("; font-size: ");
/* 277 */     sbuf.append(this.headerSize).append(";}").append(Constants.LINE_SEPARATOR);
/* 278 */     sbuf.append("th {background: #336699; color: #FFFFFF; text-align: left;}").append(Constants.LINE_SEPARATOR);
/* 279 */     sbuf.append("-->").append(Constants.LINE_SEPARATOR);
/* 280 */     sbuf.append("</style>").append(Constants.LINE_SEPARATOR);
/* 281 */     sbuf.append("</head>").append(Constants.LINE_SEPARATOR);
/* 282 */     sbuf.append("<body bgcolor=\"#FFFFFF\" topmargin=\"6\" leftmargin=\"6\">").append(Constants.LINE_SEPARATOR);
/* 283 */     sbuf.append("<hr size=\"1\" noshade>").append(Constants.LINE_SEPARATOR);
/* 284 */     sbuf.append("Log session start time " + new Date() + "<br>").append(Constants.LINE_SEPARATOR);
/* 285 */     sbuf.append("<br>").append(Constants.LINE_SEPARATOR);
/* 286 */     sbuf.append("<table cellspacing=\"0\" cellpadding=\"4\" border=\"1\" bordercolor=\"#224466\" width=\"100%\">");
/*     */     
/* 288 */     sbuf.append(Constants.LINE_SEPARATOR);
/* 289 */     sbuf.append("<tr>").append(Constants.LINE_SEPARATOR);
/* 290 */     sbuf.append("<th>Time</th>").append(Constants.LINE_SEPARATOR);
/* 291 */     sbuf.append("<th>Thread</th>").append(Constants.LINE_SEPARATOR);
/* 292 */     sbuf.append("<th>Level</th>").append(Constants.LINE_SEPARATOR);
/* 293 */     sbuf.append("<th>Logger</th>").append(Constants.LINE_SEPARATOR);
/* 294 */     if (this.locationInfo) {
/* 295 */       sbuf.append("<th>File:Line</th>").append(Constants.LINE_SEPARATOR);
/*     */     }
/* 297 */     sbuf.append("<th>Message</th>").append(Constants.LINE_SEPARATOR);
/* 298 */     sbuf.append("</tr>").append(Constants.LINE_SEPARATOR);
/* 299 */     return sbuf.toString().getBytes(getCharset());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] getFooter() {
/* 308 */     StringBuilder sbuf = new StringBuilder();
/* 309 */     sbuf.append("</table>").append(Constants.LINE_SEPARATOR);
/* 310 */     sbuf.append("<br>").append(Constants.LINE_SEPARATOR);
/* 311 */     sbuf.append("</body></html>");
/* 312 */     return sbuf.toString().getBytes(getCharset());
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
/*     */   public static HtmlLayout createLayout(@PluginAttribute(value = "locationInfo", defaultBoolean = false) boolean locationInfo, @PluginAttribute(value = "title", defaultString = "Log4j Log Messages") String title, @PluginAttribute("contentType") String contentType, @PluginAttribute(value = "charset", defaultString = "UTF-8") Charset charset, @PluginAttribute("fontSize") String fontSize, @PluginAttribute(value = "fontName", defaultString = "arial,sans-serif") String font) {
/* 333 */     FontSize fs = FontSize.getFontSize(fontSize);
/* 334 */     fontSize = fs.getFontSize();
/* 335 */     String headerSize = fs.larger().getFontSize();
/* 336 */     if (contentType == null) {
/* 337 */       contentType = "text/html; charset=" + charset;
/*     */     }
/* 339 */     return new HtmlLayout(locationInfo, title, contentType, charset, font, fontSize, headerSize);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HtmlLayout createDefaultLayout() {
/* 348 */     return newBuilder().build();
/*     */   }
/*     */   
/*     */   @PluginBuilderFactory
/*     */   public static Builder newBuilder() {
/* 353 */     return new Builder();
/*     */   }
/*     */   
/*     */   public static class Builder
/*     */     implements org.apache.logging.log4j.core.util.Builder<HtmlLayout> {
/*     */     @PluginBuilderAttribute
/*     */     private boolean locationInfo = false;
/*     */     @PluginBuilderAttribute
/* 361 */     private String title = "Log4j Log Messages";
/*     */     
/*     */     @PluginBuilderAttribute
/* 364 */     private String contentType = null;
/*     */     
/*     */     @PluginBuilderAttribute
/* 367 */     private Charset charset = Charsets.UTF_8;
/*     */     
/*     */     @PluginBuilderAttribute
/* 370 */     private HtmlLayout.FontSize fontSize = HtmlLayout.FontSize.SMALL;
/*     */     
/*     */     @PluginBuilderAttribute
/* 373 */     private String fontName = "arial,sans-serif";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder withLocationInfo(boolean locationInfo) {
/* 380 */       this.locationInfo = locationInfo;
/* 381 */       return this;
/*     */     }
/*     */     
/*     */     public Builder withTitle(String title) {
/* 385 */       this.title = title;
/* 386 */       return this;
/*     */     }
/*     */     
/*     */     public Builder withContentType(String contentType) {
/* 390 */       this.contentType = contentType;
/* 391 */       return this;
/*     */     }
/*     */     
/*     */     public Builder withCharset(Charset charset) {
/* 395 */       this.charset = charset;
/* 396 */       return this;
/*     */     }
/*     */     
/*     */     public Builder withFontSize(HtmlLayout.FontSize fontSize) {
/* 400 */       this.fontSize = fontSize;
/* 401 */       return this;
/*     */     }
/*     */     
/*     */     public Builder withFontName(String fontName) {
/* 405 */       this.fontName = fontName;
/* 406 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public HtmlLayout build() {
/* 412 */       if (this.contentType == null) {
/* 413 */         this.contentType = "text/html; charset=" + this.charset;
/*     */       }
/* 415 */       return new HtmlLayout(this.locationInfo, this.title, this.contentType, this.charset, this.fontName, this.fontSize.getFontSize(), this.fontSize.larger().getFontSize());
/*     */     }
/*     */     
/*     */     private Builder() {}
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\layout\HtmlLayout.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */