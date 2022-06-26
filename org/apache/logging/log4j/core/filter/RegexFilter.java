/*     */ package org.apache.logging.log4j.core.filter;
/*     */ 
/*     */ import java.lang.reflect.Field;
/*     */ import java.util.Arrays;
/*     */ import java.util.Comparator;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import org.apache.logging.log4j.Level;
/*     */ import org.apache.logging.log4j.Marker;
/*     */ import org.apache.logging.log4j.core.Filter;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.Logger;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginElement;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginFactory;
/*     */ import org.apache.logging.log4j.message.Message;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Plugin(name = "RegexFilter", category = "Core", elementType = "filter", printObject = true)
/*     */ public final class RegexFilter
/*     */   extends AbstractFilter
/*     */ {
/*     */   private static final int DEFAULT_PATTERN_FLAGS = 0;
/*     */   private final Pattern pattern;
/*     */   private final boolean useRawMessage;
/*     */   
/*     */   private RegexFilter(boolean raw, Pattern pattern, Filter.Result onMatch, Filter.Result onMismatch) {
/*  50 */     super(onMatch, onMismatch);
/*  51 */     this.pattern = pattern;
/*  52 */     this.useRawMessage = raw;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object... params) {
/*  58 */     return filter(msg);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, Object msg, Throwable t) {
/*  64 */     if (msg == null) {
/*  65 */       return this.onMismatch;
/*     */     }
/*  67 */     return filter(msg.toString());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, Message msg, Throwable t) {
/*  73 */     if (msg == null) {
/*  74 */       return this.onMismatch;
/*     */     }
/*  76 */     String text = this.useRawMessage ? msg.getFormat() : msg.getFormattedMessage();
/*  77 */     return filter(text);
/*     */   }
/*     */ 
/*     */   
/*     */   public Filter.Result filter(LogEvent event) {
/*  82 */     String text = this.useRawMessage ? event.getMessage().getFormat() : event.getMessage().getFormattedMessage();
/*  83 */     return filter(text);
/*     */   }
/*     */   
/*     */   private Filter.Result filter(String msg) {
/*  87 */     if (msg == null) {
/*  88 */       return this.onMismatch;
/*     */     }
/*  90 */     Matcher m = this.pattern.matcher(msg);
/*  91 */     return m.matches() ? this.onMatch : this.onMismatch;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  96 */     StringBuilder sb = new StringBuilder();
/*  97 */     sb.append("useRaw=").append(this.useRawMessage);
/*  98 */     sb.append(", pattern=").append(this.pattern.toString());
/*  99 */     return sb.toString();
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
/*     */   @PluginFactory
/*     */   public static RegexFilter createFilter(@PluginAttribute("regex") String regex, @PluginElement("PatternFlags") String[] patternFlags, @PluginAttribute("useRawMsg") Boolean useRawMsg, @PluginAttribute("onMatch") Filter.Result match, @PluginAttribute("onMismatch") Filter.Result mismatch) throws IllegalArgumentException, IllegalAccessException {
/* 129 */     if (regex == null) {
/* 130 */       LOGGER.error("A regular expression must be provided for RegexFilter");
/* 131 */       return null;
/*     */     } 
/* 133 */     return new RegexFilter(useRawMsg.booleanValue(), Pattern.compile(regex, toPatternFlags(patternFlags)), match, mismatch);
/*     */   }
/*     */ 
/*     */   
/*     */   private static int toPatternFlags(String[] patternFlags) throws IllegalArgumentException, IllegalAccessException {
/* 138 */     if (patternFlags == null || patternFlags.length == 0) {
/* 139 */       return 0;
/*     */     }
/* 141 */     Field[] fields = Pattern.class.getDeclaredFields();
/* 142 */     Comparator<Field> comparator = new Comparator<Field>()
/*     */       {
/*     */         public int compare(Field f1, Field f2)
/*     */         {
/* 146 */           return f1.getName().compareTo(f2.getName());
/*     */         }
/*     */       };
/* 149 */     Arrays.sort(fields, comparator);
/* 150 */     String[] fieldNames = new String[fields.length];
/* 151 */     for (int i = 0; i < fields.length; i++) {
/* 152 */       fieldNames[i] = fields[i].getName();
/*     */     }
/* 154 */     int flags = 0;
/* 155 */     for (String test : patternFlags) {
/* 156 */       int index = Arrays.binarySearch((Object[])fieldNames, test);
/* 157 */       if (index >= 0) {
/* 158 */         Field field = fields[index];
/* 159 */         flags |= field.getInt(Pattern.class);
/*     */       } 
/*     */     } 
/* 162 */     return flags;
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\filter\RegexFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */