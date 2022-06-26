/*     */ package org.apache.logging.log4j.core.appender.rolling;
/*     */ 
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Calendar;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.impl.Log4jLogEvent;
/*     */ import org.apache.logging.log4j.core.lookup.StrSubstitutor;
/*     */ import org.apache.logging.log4j.core.pattern.ArrayPatternConverter;
/*     */ import org.apache.logging.log4j.core.pattern.DatePatternConverter;
/*     */ import org.apache.logging.log4j.core.pattern.FormattingInfo;
/*     */ import org.apache.logging.log4j.core.pattern.PatternConverter;
/*     */ import org.apache.logging.log4j.core.pattern.PatternParser;
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
/*     */ public class PatternProcessor
/*     */ {
/*  41 */   protected static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*     */   
/*     */   private static final String KEY = "FileConverter";
/*     */   private static final char YEAR_CHAR = 'y';
/*     */   private static final char MONTH_CHAR = 'M';
/*  46 */   private static final char[] WEEK_CHARS = new char[] { 'w', 'W' };
/*  47 */   private static final char[] DAY_CHARS = new char[] { 'D', 'd', 'F', 'E' };
/*  48 */   private static final char[] HOUR_CHARS = new char[] { 'H', 'K', 'h', 'k' };
/*     */   
/*     */   private static final char MINUTE_CHAR = 'm';
/*     */   
/*     */   private static final char SECOND_CHAR = 's';
/*     */   private static final char MILLIS_CHAR = 'S';
/*     */   private final ArrayPatternConverter[] patternConverters;
/*     */   private final FormattingInfo[] patternFields;
/*  56 */   private long prevFileTime = 0L;
/*  57 */   private long nextFileTime = 0L;
/*     */   
/*  59 */   private RolloverFrequency frequency = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PatternProcessor(String pattern) {
/*  66 */     PatternParser parser = createPatternParser();
/*  67 */     List<PatternConverter> converters = new ArrayList<PatternConverter>();
/*  68 */     List<FormattingInfo> fields = new ArrayList<FormattingInfo>();
/*  69 */     parser.parse(pattern, converters, fields, false);
/*  70 */     FormattingInfo[] infoArray = new FormattingInfo[fields.size()];
/*  71 */     this.patternFields = fields.<FormattingInfo>toArray(infoArray);
/*  72 */     ArrayPatternConverter[] converterArray = new ArrayPatternConverter[converters.size()];
/*  73 */     this.patternConverters = converters.<ArrayPatternConverter>toArray(converterArray);
/*     */     
/*  75 */     for (ArrayPatternConverter converter : this.patternConverters) {
/*  76 */       if (converter instanceof DatePatternConverter) {
/*  77 */         DatePatternConverter dateConverter = (DatePatternConverter)converter;
/*  78 */         this.frequency = calculateFrequency(dateConverter.getPattern());
/*     */       } 
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
/*     */   public long getNextTime(long current, int increment, boolean modulus) {
/*  91 */     this.prevFileTime = this.nextFileTime;
/*     */ 
/*     */     
/*  94 */     if (this.frequency == null) {
/*  95 */       throw new IllegalStateException("Pattern does not contain a date");
/*     */     }
/*  97 */     Calendar currentCal = Calendar.getInstance();
/*  98 */     currentCal.setTimeInMillis(current);
/*  99 */     Calendar cal = Calendar.getInstance();
/* 100 */     cal.set(currentCal.get(1), 0, 1, 0, 0, 0);
/* 101 */     cal.set(14, 0);
/* 102 */     if (this.frequency == RolloverFrequency.ANNUALLY) {
/* 103 */       increment(cal, 1, increment, modulus);
/* 104 */       long l = cal.getTimeInMillis();
/* 105 */       cal.add(1, -1);
/* 106 */       this.nextFileTime = cal.getTimeInMillis();
/* 107 */       return debugGetNextTime(l);
/*     */     } 
/* 109 */     cal.set(2, currentCal.get(2));
/* 110 */     if (this.frequency == RolloverFrequency.MONTHLY) {
/* 111 */       increment(cal, 2, increment, modulus);
/* 112 */       long l = cal.getTimeInMillis();
/* 113 */       cal.add(2, -1);
/* 114 */       this.nextFileTime = cal.getTimeInMillis();
/* 115 */       return debugGetNextTime(l);
/*     */     } 
/* 117 */     if (this.frequency == RolloverFrequency.WEEKLY) {
/* 118 */       cal.set(3, currentCal.get(3));
/* 119 */       increment(cal, 3, increment, modulus);
/* 120 */       cal.set(7, currentCal.getFirstDayOfWeek());
/* 121 */       long l = cal.getTimeInMillis();
/* 122 */       cal.add(3, -1);
/* 123 */       this.nextFileTime = cal.getTimeInMillis();
/* 124 */       return debugGetNextTime(l);
/*     */     } 
/* 126 */     cal.set(6, currentCal.get(6));
/* 127 */     if (this.frequency == RolloverFrequency.DAILY) {
/* 128 */       increment(cal, 6, increment, modulus);
/* 129 */       long l = cal.getTimeInMillis();
/* 130 */       cal.add(6, -1);
/* 131 */       this.nextFileTime = cal.getTimeInMillis();
/* 132 */       return debugGetNextTime(l);
/*     */     } 
/* 134 */     cal.set(11, currentCal.get(11));
/* 135 */     if (this.frequency == RolloverFrequency.HOURLY) {
/* 136 */       increment(cal, 11, increment, modulus);
/* 137 */       long l = cal.getTimeInMillis();
/* 138 */       cal.add(11, -1);
/* 139 */       this.nextFileTime = cal.getTimeInMillis();
/* 140 */       return debugGetNextTime(l);
/*     */     } 
/* 142 */     cal.set(12, currentCal.get(12));
/* 143 */     if (this.frequency == RolloverFrequency.EVERY_MINUTE) {
/* 144 */       increment(cal, 12, increment, modulus);
/* 145 */       long l = cal.getTimeInMillis();
/* 146 */       cal.add(12, -1);
/* 147 */       this.nextFileTime = cal.getTimeInMillis();
/* 148 */       return debugGetNextTime(l);
/*     */     } 
/* 150 */     cal.set(13, currentCal.get(13));
/* 151 */     if (this.frequency == RolloverFrequency.EVERY_SECOND) {
/* 152 */       increment(cal, 13, increment, modulus);
/* 153 */       long l = cal.getTimeInMillis();
/* 154 */       cal.add(13, -1);
/* 155 */       this.nextFileTime = cal.getTimeInMillis();
/* 156 */       return debugGetNextTime(l);
/*     */     } 
/* 158 */     cal.set(14, currentCal.get(14));
/* 159 */     increment(cal, 14, increment, modulus);
/* 160 */     long nextTime = cal.getTimeInMillis();
/* 161 */     cal.add(14, -1);
/* 162 */     this.nextFileTime = cal.getTimeInMillis();
/* 163 */     return debugGetNextTime(nextTime);
/*     */   }
/*     */   
/*     */   public void updateTime() {
/* 167 */     this.prevFileTime = this.nextFileTime;
/*     */   }
/*     */   
/*     */   private long debugGetNextTime(long nextTime) {
/* 171 */     if (LOGGER.isTraceEnabled()) {
/* 172 */       LOGGER.trace("PatternProcessor.getNextTime returning {}, nextFileTime={}, prevFileTime={}, current={}, freq={}", new Object[] { format(nextTime), format(this.nextFileTime), format(this.prevFileTime), format(System.currentTimeMillis()), this.frequency });
/*     */     }
/*     */     
/* 175 */     return nextTime;
/*     */   }
/*     */   
/*     */   private String format(long time) {
/* 179 */     return (new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss.SSS")).format(new Date(time));
/*     */   }
/*     */   
/*     */   private void increment(Calendar cal, int type, int increment, boolean modulate) {
/* 183 */     int interval = modulate ? (increment - cal.get(type) % increment) : increment;
/* 184 */     cal.add(type, interval);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void formatFileName(StringBuilder buf, Object obj) {
/* 193 */     long time = (this.prevFileTime == 0L) ? System.currentTimeMillis() : this.prevFileTime;
/* 194 */     formatFileName(buf, new Object[] { new Date(time), obj });
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
/*     */   public final void formatFileName(StrSubstitutor subst, StringBuilder buf, Object obj) {
/* 206 */     long time = (this.prevFileTime == 0L) ? System.currentTimeMillis() : this.prevFileTime;
/* 207 */     formatFileName(buf, new Object[] { new Date(time), obj });
/* 208 */     Log4jLogEvent log4jLogEvent = new Log4jLogEvent(time);
/* 209 */     String fileName = subst.replace((LogEvent)log4jLogEvent, buf);
/* 210 */     buf.setLength(0);
/* 211 */     buf.append(fileName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void formatFileName(StringBuilder buf, Object... objects) {
/* 220 */     for (int i = 0; i < this.patternConverters.length; i++) {
/* 221 */       int fieldStart = buf.length();
/* 222 */       this.patternConverters[i].format(buf, objects);
/*     */       
/* 224 */       if (this.patternFields[i] != null) {
/* 225 */         this.patternFields[i].format(fieldStart, buf);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private RolloverFrequency calculateFrequency(String pattern) {
/* 231 */     if (patternContains(pattern, 'S')) {
/* 232 */       return RolloverFrequency.EVERY_MILLISECOND;
/*     */     }
/* 234 */     if (patternContains(pattern, 's')) {
/* 235 */       return RolloverFrequency.EVERY_SECOND;
/*     */     }
/* 237 */     if (patternContains(pattern, 'm')) {
/* 238 */       return RolloverFrequency.EVERY_MINUTE;
/*     */     }
/* 240 */     if (patternContains(pattern, HOUR_CHARS)) {
/* 241 */       return RolloverFrequency.HOURLY;
/*     */     }
/* 243 */     if (patternContains(pattern, DAY_CHARS)) {
/* 244 */       return RolloverFrequency.DAILY;
/*     */     }
/* 246 */     if (patternContains(pattern, WEEK_CHARS)) {
/* 247 */       return RolloverFrequency.WEEKLY;
/*     */     }
/* 249 */     if (patternContains(pattern, 'M')) {
/* 250 */       return RolloverFrequency.MONTHLY;
/*     */     }
/* 252 */     if (patternContains(pattern, 'y')) {
/* 253 */       return RolloverFrequency.ANNUALLY;
/*     */     }
/* 255 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   private PatternParser createPatternParser() {
/* 260 */     return new PatternParser(null, "FileConverter", null);
/*     */   }
/*     */   
/*     */   private boolean patternContains(String pattern, char... chars) {
/* 264 */     for (char character : chars) {
/* 265 */       if (patternContains(pattern, character)) {
/* 266 */         return true;
/*     */       }
/*     */     } 
/* 269 */     return false;
/*     */   }
/*     */   
/*     */   private boolean patternContains(String pattern, char character) {
/* 273 */     return (pattern.indexOf(character) >= 0);
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\appender\rolling\PatternProcessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */