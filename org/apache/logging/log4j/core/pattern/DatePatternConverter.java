/*     */ package org.apache.logging.log4j.core.pattern;
/*     */ 
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Date;
/*     */ import java.util.TimeZone;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ 
/*     */ 
/*     */ @Plugin(name = "DatePatternConverter", category = "Converter")
/*     */ @ConverterKeys({"d", "date"})
/*     */ public final class DatePatternConverter
/*     */   extends LogEventPatternConverter
/*     */   implements ArrayPatternConverter
/*     */ {
/*     */   private static final String ABSOLUTE_FORMAT = "ABSOLUTE";
/*     */   private static final String ABSOLUTE_TIME_PATTERN = "HH:mm:ss,SSS";
/*     */   private static final String COMPACT_FORMAT = "COMPACT";
/*     */   private static final String COMPACT_PATTERN = "yyyyMMddHHmmssSSS";
/*     */   private static final String DATE_AND_TIME_FORMAT = "DATE";
/*     */   private static final String DATE_AND_TIME_PATTERN = "dd MMM yyyy HH:mm:ss,SSS";
/*     */   private static final String DEFAULT_FORMAT = "DEFAULT";
/*     */   static final String DEFAULT_PATTERN = "yyyy-MM-dd HH:mm:ss,SSS";
/*     */   private static final String ISO8601_BASIC_FORMAT = "ISO8601_BASIC";
/*     */   private static final String ISO8601_BASIC_PATTERN = "yyyyMMdd'T'HHmmss,SSS";
/*     */   static final String ISO8601_FORMAT = "ISO8601";
/*     */   static final String ISO8601_PATTERN = "yyyy-MM-dd'T'HH:mm:ss,SSS";
/*     */   private static final String UNIX_FORMAT = "UNIX";
/*     */   private static final String UNIX_MILLIS_FORMAT = "UNIX_MILLIS";
/*     */   private String cachedDateString;
/*     */   private final Formatter formatter;
/*     */   private long lastTimestamp;
/*     */   
/*     */   private static abstract class Formatter
/*     */   {
/*     */     public String toPattern() {
/*  37 */       return null;
/*     */     }
/*     */     
/*     */     private Formatter() {}
/*     */     
/*     */     abstract String format(long param1Long); }
/*     */   
/*     */   private static class PatternFormatter extends Formatter { PatternFormatter(SimpleDateFormat simpleDateFormat) {
/*  45 */       this.simpleDateFormat = simpleDateFormat;
/*     */     }
/*     */     private final SimpleDateFormat simpleDateFormat;
/*     */     
/*     */     String format(long time) {
/*  50 */       return this.simpleDateFormat.format(Long.valueOf(time));
/*     */     }
/*     */ 
/*     */     
/*     */     public String toPattern() {
/*  55 */       return this.simpleDateFormat.toPattern();
/*     */     } }
/*     */ 
/*     */   
/*     */   private static class UnixFormatter extends Formatter {
/*     */     private UnixFormatter() {}
/*     */     
/*     */     String format(long time) {
/*  63 */       return Long.toString(time / 1000L);
/*     */     }
/*     */   }
/*     */   
/*     */   private static class UnixMillisFormatter
/*     */     extends Formatter {
/*     */     private UnixMillisFormatter() {}
/*     */     
/*     */     String format(long time) {
/*  72 */       return Long.toString(time);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static DatePatternConverter newInstance(String[] options) {
/* 158 */     return new DatePatternConverter(options);
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
/*     */   private DatePatternConverter(String[] options) {
/* 177 */     super("Date", "date");
/*     */ 
/*     */     
/* 180 */     String patternOption = (options != null && options.length > 0) ? options[0] : null;
/*     */     
/* 182 */     String pattern = null;
/* 183 */     Formatter tempFormatter = null;
/*     */     
/* 185 */     if (patternOption == null || patternOption.equalsIgnoreCase("DEFAULT")) {
/* 186 */       pattern = "yyyy-MM-dd HH:mm:ss,SSS";
/* 187 */     } else if (patternOption.equalsIgnoreCase("ISO8601")) {
/* 188 */       pattern = "yyyy-MM-dd'T'HH:mm:ss,SSS";
/* 189 */     } else if (patternOption.equalsIgnoreCase("ISO8601_BASIC")) {
/* 190 */       pattern = "yyyyMMdd'T'HHmmss,SSS";
/* 191 */     } else if (patternOption.equalsIgnoreCase("ABSOLUTE")) {
/* 192 */       pattern = "HH:mm:ss,SSS";
/* 193 */     } else if (patternOption.equalsIgnoreCase("DATE")) {
/* 194 */       pattern = "dd MMM yyyy HH:mm:ss,SSS";
/* 195 */     } else if (patternOption.equalsIgnoreCase("COMPACT")) {
/* 196 */       pattern = "yyyyMMddHHmmssSSS";
/* 197 */     } else if (patternOption.equalsIgnoreCase("UNIX")) {
/* 198 */       tempFormatter = new UnixFormatter();
/* 199 */     } else if (patternOption.equalsIgnoreCase("UNIX_MILLIS")) {
/* 200 */       tempFormatter = new UnixMillisFormatter();
/*     */     } else {
/* 202 */       pattern = patternOption;
/*     */     } 
/*     */     
/* 205 */     if (pattern != null) {
/*     */       SimpleDateFormat simpleDateFormat;
/*     */       
/*     */       try {
/* 209 */         simpleDateFormat = new SimpleDateFormat(pattern);
/* 210 */       } catch (IllegalArgumentException e) {
/* 211 */         LOGGER.warn("Could not instantiate SimpleDateFormat with pattern " + patternOption, e);
/*     */ 
/*     */         
/* 214 */         simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");
/*     */       } 
/*     */ 
/*     */       
/* 218 */       if (options != null && options.length > 1) {
/* 219 */         TimeZone tz = TimeZone.getTimeZone(options[1]);
/* 220 */         simpleDateFormat.setTimeZone(tz);
/*     */       } 
/* 222 */       tempFormatter = new PatternFormatter(simpleDateFormat);
/*     */     } 
/* 224 */     this.formatter = tempFormatter;
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
/*     */   public void format(Date date, StringBuilder toAppendTo) {
/* 236 */     synchronized (this) {
/* 237 */       toAppendTo.append(this.formatter.format(date.getTime()));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void format(LogEvent event, StringBuilder output) {
/* 246 */     long timestamp = event.getTimeMillis();
/*     */     
/* 248 */     synchronized (this) {
/* 249 */       if (timestamp != this.lastTimestamp) {
/* 250 */         this.lastTimestamp = timestamp;
/* 251 */         this.cachedDateString = this.formatter.format(timestamp);
/*     */       } 
/*     */     } 
/* 254 */     output.append(this.cachedDateString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void format(Object obj, StringBuilder output) {
/* 262 */     if (obj instanceof Date) {
/* 263 */       format((Date)obj, output);
/*     */     }
/* 265 */     super.format(obj, output);
/*     */   }
/*     */ 
/*     */   
/*     */   public void format(StringBuilder toAppendTo, Object... objects) {
/* 270 */     for (Object obj : objects) {
/* 271 */       if (obj instanceof Date) {
/* 272 */         format(obj, toAppendTo);
/*     */         break;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPattern() {
/* 284 */     return this.formatter.toPattern();
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\pattern\DatePatternConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */