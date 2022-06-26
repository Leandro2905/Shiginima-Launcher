/*     */ package org.apache.logging.log4j.core.filter;
/*     */ 
/*     */ import java.text.ParseException;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Calendar;
/*     */ import java.util.TimeZone;
/*     */ import org.apache.logging.log4j.core.Filter;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Plugin(name = "TimeFilter", category = "Core", elementType = "filter", printObject = true)
/*     */ public final class TimeFilter
/*     */   extends AbstractFilter
/*     */ {
/*     */   private static final long HOUR_MS = 3600000L;
/*     */   private static final long MINUTE_MS = 60000L;
/*     */   private static final long SECOND_MS = 1000L;
/*     */   private final long start;
/*     */   private final long end;
/*     */   private final TimeZone timezone;
/*     */   
/*     */   private TimeFilter(long start, long end, TimeZone tz, Filter.Result onMatch, Filter.Result onMismatch) {
/*  65 */     super(onMatch, onMismatch);
/*  66 */     this.start = start;
/*  67 */     this.end = end;
/*  68 */     this.timezone = tz;
/*     */   }
/*     */ 
/*     */   
/*     */   public Filter.Result filter(LogEvent event) {
/*  73 */     Calendar calendar = Calendar.getInstance(this.timezone);
/*  74 */     calendar.setTimeInMillis(event.getTimeMillis());
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  79 */     long apparentOffset = calendar.get(11) * 3600000L + calendar.get(12) * 60000L + calendar.get(13) * 1000L + calendar.get(14);
/*     */ 
/*     */ 
/*     */     
/*  83 */     return (apparentOffset >= this.start && apparentOffset < this.end) ? this.onMatch : this.onMismatch;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  88 */     StringBuilder sb = new StringBuilder();
/*  89 */     sb.append("start=").append(this.start);
/*  90 */     sb.append(", end=").append(this.end);
/*  91 */     sb.append(", timezone=").append(this.timezone.toString());
/*  92 */     return sb.toString();
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
/*     */   @PluginFactory
/*     */   public static TimeFilter createFilter(@PluginAttribute("start") String start, @PluginAttribute("end") String end, @PluginAttribute("timezone") String tz, @PluginAttribute("onMatch") Filter.Result match, @PluginAttribute("onMismatch") Filter.Result mismatch) {
/* 111 */     SimpleDateFormat stf = new SimpleDateFormat("HH:mm:ss");
/* 112 */     long s = 0L;
/* 113 */     if (start != null) {
/* 114 */       stf.setTimeZone(TimeZone.getTimeZone("UTC"));
/*     */       try {
/* 116 */         s = stf.parse(start).getTime();
/* 117 */       } catch (ParseException ex) {
/* 118 */         LOGGER.warn("Error parsing start value " + start, ex);
/*     */       } 
/*     */     } 
/* 121 */     long e = Long.MAX_VALUE;
/* 122 */     if (end != null) {
/* 123 */       stf.setTimeZone(TimeZone.getTimeZone("UTC"));
/*     */       try {
/* 125 */         e = stf.parse(end).getTime();
/* 126 */       } catch (ParseException ex) {
/* 127 */         LOGGER.warn("Error parsing start value " + end, ex);
/*     */       } 
/*     */     } 
/* 130 */     TimeZone timezone = (tz == null) ? TimeZone.getDefault() : TimeZone.getTimeZone(tz);
/* 131 */     Filter.Result onMatch = (match == null) ? Filter.Result.NEUTRAL : match;
/* 132 */     Filter.Result onMismatch = (mismatch == null) ? Filter.Result.DENY : mismatch;
/* 133 */     return new TimeFilter(s, e, timezone, onMatch, onMismatch);
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\filter\TimeFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */