/*     */ package org.apache.logging.log4j.core.appender.rolling;
/*     */ 
/*     */ import java.text.NumberFormat;
/*     */ import java.text.ParseException;
/*     */ import java.util.Locale;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginFactory;
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
/*     */ 
/*     */ @Plugin(name = "SizeBasedTriggeringPolicy", category = "Core", printObject = true)
/*     */ public class SizeBasedTriggeringPolicy
/*     */   implements TriggeringPolicy
/*     */ {
/*  40 */   protected static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*     */ 
/*     */   
/*     */   private static final long KB = 1024L;
/*     */ 
/*     */   
/*     */   private static final long MB = 1048576L;
/*     */ 
/*     */   
/*     */   private static final long GB = 1073741824L;
/*     */ 
/*     */   
/*     */   private static final long MAX_FILE_SIZE = 10485760L;
/*     */ 
/*     */   
/*  55 */   private static final Pattern VALUE_PATTERN = Pattern.compile("([0-9]+([\\.,][0-9]+)?)\\s*(|K|M|G)B?", 2);
/*     */ 
/*     */   
/*     */   private final long maxFileSize;
/*     */ 
/*     */   
/*     */   private RollingFileManager manager;
/*     */ 
/*     */ 
/*     */   
/*     */   protected SizeBasedTriggeringPolicy() {
/*  66 */     this.maxFileSize = 10485760L;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected SizeBasedTriggeringPolicy(long maxFileSize) {
/*  75 */     this.maxFileSize = maxFileSize;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void initialize(RollingFileManager manager) {
/*  84 */     this.manager = manager;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isTriggeringEvent(LogEvent event) {
/*  95 */     boolean triggered = (this.manager.getFileSize() > this.maxFileSize);
/*  96 */     if (triggered) {
/*  97 */       this.manager.getPatternProcessor().updateTime();
/*     */     }
/*  99 */     return triggered;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 104 */     return "SizeBasedTriggeringPolicy(size=" + this.maxFileSize + ')';
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @PluginFactory
/*     */   public static SizeBasedTriggeringPolicy createPolicy(@PluginAttribute("size") String size) {
/* 115 */     long maxSize = (size == null) ? 10485760L : valueOf(size);
/* 116 */     return new SizeBasedTriggeringPolicy(maxSize);
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
/*     */   private static long valueOf(String string) {
/* 128 */     Matcher matcher = VALUE_PATTERN.matcher(string);
/*     */ 
/*     */     
/* 131 */     if (matcher.matches()) {
/*     */       
/*     */       try {
/* 134 */         long value = NumberFormat.getNumberInstance(Locale.getDefault()).parse(matcher.group(1)).longValue();
/*     */ 
/*     */ 
/*     */         
/* 138 */         String units = matcher.group(3);
/*     */         
/* 140 */         if (units.isEmpty())
/* 141 */           return value; 
/* 142 */         if (units.equalsIgnoreCase("K"))
/* 143 */           return value * 1024L; 
/* 144 */         if (units.equalsIgnoreCase("M"))
/* 145 */           return value * 1048576L; 
/* 146 */         if (units.equalsIgnoreCase("G")) {
/* 147 */           return value * 1073741824L;
/*     */         }
/* 149 */         LOGGER.error("Units not recognized: " + string);
/* 150 */         return 10485760L;
/*     */       }
/* 152 */       catch (ParseException e) {
/* 153 */         LOGGER.error("Unable to parse numeric part: " + string, e);
/* 154 */         return 10485760L;
/*     */       } 
/*     */     }
/* 157 */     LOGGER.error("Unable to parse bytes: " + string);
/* 158 */     return 10485760L;
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\appender\rolling\SizeBasedTriggeringPolicy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */