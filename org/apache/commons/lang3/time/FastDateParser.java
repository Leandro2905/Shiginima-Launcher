/*     */ package org.apache.commons.lang3.time;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.Serializable;
/*     */ import java.text.DateFormatSymbols;
/*     */ import java.text.ParseException;
/*     */ import java.text.ParsePosition;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Calendar;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.SortedMap;
/*     */ import java.util.TimeZone;
/*     */ import java.util.TreeMap;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FastDateParser
/*     */   implements DateParser, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 2L;
/*  80 */   static final Locale JAPANESE_IMPERIAL = new Locale("ja", "JP", "JP");
/*     */ 
/*     */   
/*     */   private final String pattern;
/*     */ 
/*     */   
/*     */   private final TimeZone timeZone;
/*     */ 
/*     */   
/*     */   private final Locale locale;
/*     */ 
/*     */   
/*     */   private final int century;
/*     */ 
/*     */   
/*     */   private final int startYear;
/*     */ 
/*     */   
/*     */   private transient Pattern parsePattern;
/*     */ 
/*     */   
/*     */   private transient Strategy[] strategies;
/*     */   
/*     */   private transient String currentFormatField;
/*     */   
/*     */   private transient Strategy nextStrategy;
/*     */ 
/*     */   
/*     */   protected FastDateParser(String pattern, TimeZone timeZone, Locale locale) {
/* 109 */     this(pattern, timeZone, locale, null);
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
/*     */   protected FastDateParser(String pattern, TimeZone timeZone, Locale locale, Date centuryStart) {
/*     */     int centuryStartYear;
/* 124 */     this.pattern = pattern;
/* 125 */     this.timeZone = timeZone;
/* 126 */     this.locale = locale;
/*     */     
/* 128 */     Calendar definingCalendar = Calendar.getInstance(timeZone, locale);
/*     */     
/* 130 */     if (centuryStart != null) {
/* 131 */       definingCalendar.setTime(centuryStart);
/* 132 */       centuryStartYear = definingCalendar.get(1);
/*     */     }
/* 134 */     else if (locale.equals(JAPANESE_IMPERIAL)) {
/* 135 */       centuryStartYear = 0;
/*     */     }
/*     */     else {
/*     */       
/* 139 */       definingCalendar.setTime(new Date());
/* 140 */       centuryStartYear = definingCalendar.get(1) - 80;
/*     */     } 
/* 142 */     this.century = centuryStartYear / 100 * 100;
/* 143 */     this.startYear = centuryStartYear - this.century;
/*     */     
/* 145 */     init(definingCalendar);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void init(Calendar definingCalendar) {
/* 156 */     StringBuilder regex = new StringBuilder();
/* 157 */     List<Strategy> collector = new ArrayList<Strategy>();
/*     */     
/* 159 */     Matcher patternMatcher = formatPattern.matcher(this.pattern);
/* 160 */     if (!patternMatcher.lookingAt()) {
/* 161 */       throw new IllegalArgumentException("Illegal pattern character '" + this.pattern.charAt(patternMatcher.regionStart()) + "'");
/*     */     }
/*     */ 
/*     */     
/* 165 */     this.currentFormatField = patternMatcher.group();
/* 166 */     Strategy currentStrategy = getStrategy(this.currentFormatField, definingCalendar);
/*     */     while (true) {
/* 168 */       patternMatcher.region(patternMatcher.end(), patternMatcher.regionEnd());
/* 169 */       if (!patternMatcher.lookingAt()) {
/* 170 */         this.nextStrategy = null;
/*     */         break;
/*     */       } 
/* 173 */       String nextFormatField = patternMatcher.group();
/* 174 */       this.nextStrategy = getStrategy(nextFormatField, definingCalendar);
/* 175 */       if (currentStrategy.addRegex(this, regex)) {
/* 176 */         collector.add(currentStrategy);
/*     */       }
/* 178 */       this.currentFormatField = nextFormatField;
/* 179 */       currentStrategy = this.nextStrategy;
/*     */     } 
/* 181 */     if (patternMatcher.regionStart() != patternMatcher.regionEnd()) {
/* 182 */       throw new IllegalArgumentException("Failed to parse \"" + this.pattern + "\" ; gave up at index " + patternMatcher.regionStart());
/*     */     }
/* 184 */     if (currentStrategy.addRegex(this, regex)) {
/* 185 */       collector.add(currentStrategy);
/*     */     }
/* 187 */     this.currentFormatField = null;
/* 188 */     this.strategies = collector.<Strategy>toArray(new Strategy[collector.size()]);
/* 189 */     this.parsePattern = Pattern.compile(regex.toString());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPattern() {
/* 199 */     return this.pattern;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TimeZone getTimeZone() {
/* 207 */     return this.timeZone;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Locale getLocale() {
/* 215 */     return this.locale;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Pattern getParsePattern() {
/* 224 */     return this.parsePattern;
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
/*     */   public boolean equals(Object obj) {
/* 237 */     if (!(obj instanceof FastDateParser)) {
/* 238 */       return false;
/*     */     }
/* 240 */     FastDateParser other = (FastDateParser)obj;
/* 241 */     return (this.pattern.equals(other.pattern) && this.timeZone.equals(other.timeZone) && this.locale.equals(other.locale));
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
/*     */   public int hashCode() {
/* 253 */     return this.pattern.hashCode() + 13 * (this.timeZone.hashCode() + 13 * this.locale.hashCode());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 263 */     return "FastDateParser[" + this.pattern + "," + this.locale + "," + this.timeZone.getID() + "]";
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
/*     */   private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
/* 277 */     in.defaultReadObject();
/*     */     
/* 279 */     Calendar definingCalendar = Calendar.getInstance(this.timeZone, this.locale);
/* 280 */     init(definingCalendar);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object parseObject(String source) throws ParseException {
/* 288 */     return parse(source);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Date parse(String source) throws ParseException {
/* 296 */     Date date = parse(source, new ParsePosition(0));
/* 297 */     if (date == null) {
/*     */       
/* 299 */       if (this.locale.equals(JAPANESE_IMPERIAL)) {
/* 300 */         throw new ParseException("(The " + this.locale + " locale does not support dates before 1868 AD)\n" + "Unparseable date: \"" + source + "\" does not match " + this.parsePattern.pattern(), 0);
/*     */       }
/*     */ 
/*     */       
/* 304 */       throw new ParseException("Unparseable date: \"" + source + "\" does not match " + this.parsePattern.pattern(), 0);
/*     */     } 
/* 306 */     return date;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object parseObject(String source, ParsePosition pos) {
/* 314 */     return parse(source, pos);
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
/*     */   public Date parse(String source, ParsePosition pos) {
/* 331 */     int offset = pos.getIndex();
/* 332 */     Matcher matcher = this.parsePattern.matcher(source.substring(offset));
/* 333 */     if (!matcher.lookingAt()) {
/* 334 */       return null;
/*     */     }
/*     */     
/* 337 */     Calendar cal = Calendar.getInstance(this.timeZone, this.locale);
/* 338 */     cal.clear();
/*     */     
/* 340 */     for (int i = 0; i < this.strategies.length; ) {
/* 341 */       Strategy strategy = this.strategies[i++];
/* 342 */       strategy.setCalendar(this, cal, matcher.group(i));
/*     */     } 
/* 344 */     pos.setIndex(offset + matcher.end());
/* 345 */     return cal.getTime();
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
/*     */   private static StringBuilder escapeRegex(StringBuilder regex, String value, boolean unquote) {
/* 359 */     regex.append("\\Q");
/* 360 */     for (int i = 0; i < value.length(); i++) {
/* 361 */       char c = value.charAt(i);
/* 362 */       switch (c) {
/*     */         case '\'':
/* 364 */           if (unquote) {
/* 365 */             if (++i == value.length()) {
/* 366 */               return regex;
/*     */             }
/* 368 */             c = value.charAt(i);
/*     */           } 
/*     */           break;
/*     */         case '\\':
/* 372 */           if (++i == value.length()) {
/*     */             break;
/*     */           }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 382 */           regex.append(c);
/* 383 */           c = value.charAt(i);
/* 384 */           if (c == 'E') {
/* 385 */             regex.append("E\\\\E\\");
/* 386 */             c = 'Q';
/*     */           } 
/*     */           break;
/*     */       } 
/*     */ 
/*     */       
/* 392 */       regex.append(c);
/*     */     } 
/* 394 */     regex.append("\\E");
/* 395 */     return regex;
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
/*     */   private static Map<String, Integer> getDisplayNames(int field, Calendar definingCalendar, Locale locale) {
/* 407 */     return definingCalendar.getDisplayNames(field, 0, locale);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int adjustYear(int twoDigitYear) {
/* 416 */     int trial = this.century + twoDigitYear;
/* 417 */     return (twoDigitYear >= this.startYear) ? trial : (trial + 100);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean isNextNumber() {
/* 425 */     return (this.nextStrategy != null && this.nextStrategy.isNumber());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int getFieldWidth() {
/* 433 */     return this.currentFormatField.length();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static abstract class Strategy
/*     */   {
/*     */     private Strategy() {}
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     boolean isNumber() {
/* 448 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     void setCalendar(FastDateParser parser, Calendar cal, String value) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     abstract boolean addRegex(FastDateParser param1FastDateParser, StringBuilder param1StringBuilder);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 479 */   private static final Pattern formatPattern = Pattern.compile("D+|E+|F+|G+|H+|K+|M+|S+|W+|X+|Z+|a+|d+|h+|k+|m+|s+|w+|y+|z+|''|'[^']++(''[^']*+)*+'|[^'A-Za-z]++");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Strategy getStrategy(String formatField, Calendar definingCalendar) {
/* 489 */     switch (formatField.charAt(0)) {
/*     */       case '\'':
/* 491 */         if (formatField.length() > 2) {
/* 492 */           return new CopyQuotedStrategy(formatField.substring(1, formatField.length() - 1));
/*     */         }
/*     */       
/*     */       default:
/* 496 */         return new CopyQuotedStrategy(formatField);
/*     */       case 'D':
/* 498 */         return DAY_OF_YEAR_STRATEGY;
/*     */       case 'E':
/* 500 */         return getLocaleSpecificStrategy(7, definingCalendar);
/*     */       case 'F':
/* 502 */         return DAY_OF_WEEK_IN_MONTH_STRATEGY;
/*     */       case 'G':
/* 504 */         return getLocaleSpecificStrategy(0, definingCalendar);
/*     */       case 'H':
/* 506 */         return HOUR_OF_DAY_STRATEGY;
/*     */       case 'K':
/* 508 */         return HOUR_STRATEGY;
/*     */       case 'M':
/* 510 */         return (formatField.length() >= 3) ? getLocaleSpecificStrategy(2, definingCalendar) : NUMBER_MONTH_STRATEGY;
/*     */       case 'S':
/* 512 */         return MILLISECOND_STRATEGY;
/*     */       case 'W':
/* 514 */         return WEEK_OF_MONTH_STRATEGY;
/*     */       case 'a':
/* 516 */         return getLocaleSpecificStrategy(9, definingCalendar);
/*     */       case 'd':
/* 518 */         return DAY_OF_MONTH_STRATEGY;
/*     */       case 'h':
/* 520 */         return HOUR12_STRATEGY;
/*     */       case 'k':
/* 522 */         return HOUR24_OF_DAY_STRATEGY;
/*     */       case 'm':
/* 524 */         return MINUTE_STRATEGY;
/*     */       case 's':
/* 526 */         return SECOND_STRATEGY;
/*     */       case 'w':
/* 528 */         return WEEK_OF_YEAR_STRATEGY;
/*     */       case 'y':
/* 530 */         return (formatField.length() > 2) ? LITERAL_YEAR_STRATEGY : ABBREVIATED_YEAR_STRATEGY;
/*     */       case 'X':
/* 532 */         return ISO8601TimeZoneStrategy.getStrategy(formatField.length());
/*     */       case 'Z':
/* 534 */         if (formatField.equals("ZZ"))
/* 535 */           return ISO_8601_STRATEGY;  break;
/*     */       case 'z':
/*     */         break;
/*     */     } 
/* 539 */     return getLocaleSpecificStrategy(15, definingCalendar);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 544 */   private static final ConcurrentMap<Locale, Strategy>[] caches = (ConcurrentMap<Locale, Strategy>[])new ConcurrentMap[17];
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static ConcurrentMap<Locale, Strategy> getCache(int field) {
/* 552 */     synchronized (caches) {
/* 553 */       if (caches[field] == null) {
/* 554 */         caches[field] = new ConcurrentHashMap<Locale, Strategy>(3);
/*     */       }
/* 556 */       return caches[field];
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Strategy getLocaleSpecificStrategy(int field, Calendar definingCalendar) {
/* 567 */     ConcurrentMap<Locale, Strategy> cache = getCache(field);
/* 568 */     Strategy strategy = cache.get(this.locale);
/* 569 */     if (strategy == null) {
/* 570 */       strategy = (field == 15) ? new TimeZoneStrategy(this.locale) : new CaseInsensitiveTextStrategy(field, definingCalendar, this.locale);
/*     */ 
/*     */       
/* 573 */       Strategy inCache = cache.putIfAbsent(this.locale, strategy);
/* 574 */       if (inCache != null) {
/* 575 */         return inCache;
/*     */       }
/*     */     } 
/* 578 */     return strategy;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class CopyQuotedStrategy
/*     */     extends Strategy
/*     */   {
/*     */     private final String formatField;
/*     */ 
/*     */ 
/*     */     
/*     */     CopyQuotedStrategy(String formatField) {
/* 592 */       this.formatField = formatField;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     boolean isNumber() {
/* 600 */       char c = this.formatField.charAt(0);
/* 601 */       if (c == '\'') {
/* 602 */         c = this.formatField.charAt(1);
/*     */       }
/* 604 */       return Character.isDigit(c);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     boolean addRegex(FastDateParser parser, StringBuilder regex) {
/* 612 */       FastDateParser.escapeRegex(regex, this.formatField, true);
/* 613 */       return false;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class CaseInsensitiveTextStrategy
/*     */     extends Strategy
/*     */   {
/*     */     private final int field;
/*     */ 
/*     */     
/*     */     private final Locale locale;
/*     */ 
/*     */     
/*     */     private final Map<String, Integer> lKeyValues;
/*     */ 
/*     */     
/*     */     CaseInsensitiveTextStrategy(int field, Calendar definingCalendar, Locale locale) {
/* 632 */       this.field = field;
/* 633 */       this.locale = locale;
/* 634 */       Map<String, Integer> keyValues = FastDateParser.getDisplayNames(field, definingCalendar, locale);
/* 635 */       this.lKeyValues = new HashMap<String, Integer>();
/*     */       
/* 637 */       for (Map.Entry<String, Integer> entry : keyValues.entrySet()) {
/* 638 */         this.lKeyValues.put(((String)entry.getKey()).toLowerCase(locale), entry.getValue());
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     boolean addRegex(FastDateParser parser, StringBuilder regex) {
/* 647 */       regex.append("((?iu)");
/* 648 */       for (String textKeyValue : this.lKeyValues.keySet()) {
/* 649 */         FastDateParser.escapeRegex(regex, textKeyValue, false).append('|');
/*     */       }
/* 651 */       regex.setCharAt(regex.length() - 1, ')');
/* 652 */       return true;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     void setCalendar(FastDateParser parser, Calendar cal, String value) {
/* 660 */       Integer iVal = this.lKeyValues.get(value.toLowerCase(this.locale));
/* 661 */       if (iVal == null) {
/* 662 */         StringBuilder sb = new StringBuilder(value);
/* 663 */         sb.append(" not in (");
/* 664 */         for (String textKeyValue : this.lKeyValues.keySet()) {
/* 665 */           sb.append(textKeyValue).append(' ');
/*     */         }
/* 667 */         sb.setCharAt(sb.length() - 1, ')');
/* 668 */         throw new IllegalArgumentException(sb.toString());
/*     */       } 
/* 670 */       cal.set(this.field, iVal.intValue());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class NumberStrategy
/*     */     extends Strategy
/*     */   {
/*     */     private final int field;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     NumberStrategy(int field) {
/* 686 */       this.field = field;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     boolean isNumber() {
/* 694 */       return true;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     boolean addRegex(FastDateParser parser, StringBuilder regex) {
/* 703 */       if (parser.isNextNumber()) {
/* 704 */         regex.append("(\\p{Nd}{").append(parser.getFieldWidth()).append("}+)");
/*     */       } else {
/*     */         
/* 707 */         regex.append("(\\p{Nd}++)");
/*     */       } 
/* 709 */       return true;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     void setCalendar(FastDateParser parser, Calendar cal, String value) {
/* 717 */       cal.set(this.field, modify(Integer.parseInt(value)));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     int modify(int iValue) {
/* 726 */       return iValue;
/*     */     }
/*     */   }
/*     */   
/* 730 */   private static final Strategy ABBREVIATED_YEAR_STRATEGY = new NumberStrategy(1)
/*     */     {
/*     */ 
/*     */       
/*     */       void setCalendar(FastDateParser parser, Calendar cal, String value)
/*     */       {
/* 736 */         int iValue = Integer.parseInt(value);
/* 737 */         if (iValue < 100) {
/* 738 */           iValue = parser.adjustYear(iValue);
/*     */         }
/* 740 */         cal.set(1, iValue);
/*     */       }
/*     */     };
/*     */ 
/*     */   
/*     */   private static class TimeZoneStrategy
/*     */     extends Strategy
/*     */   {
/*     */     private final String validTimeZoneChars;
/*     */     
/* 750 */     private final SortedMap<String, TimeZone> tzNames = new TreeMap<String, TimeZone>(String.CASE_INSENSITIVE_ORDER);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private static final int ID = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private static final int LONG_STD = 1;
/*     */ 
/*     */ 
/*     */     
/*     */     private static final int SHORT_STD = 2;
/*     */ 
/*     */ 
/*     */     
/*     */     private static final int LONG_DST = 3;
/*     */ 
/*     */ 
/*     */     
/*     */     private static final int SHORT_DST = 4;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     TimeZoneStrategy(Locale locale) {
/* 778 */       String[][] zones = DateFormatSymbols.getInstance(locale).getZoneStrings();
/* 779 */       for (String[] zone : zones) {
/* 780 */         if (!zone[0].startsWith("GMT")) {
/*     */ 
/*     */           
/* 783 */           TimeZone tz = TimeZone.getTimeZone(zone[0]);
/* 784 */           if (!this.tzNames.containsKey(zone[1])) {
/* 785 */             this.tzNames.put(zone[1], tz);
/*     */           }
/* 787 */           if (!this.tzNames.containsKey(zone[2])) {
/* 788 */             this.tzNames.put(zone[2], tz);
/*     */           }
/* 790 */           if (tz.useDaylightTime()) {
/* 791 */             if (!this.tzNames.containsKey(zone[3])) {
/* 792 */               this.tzNames.put(zone[3], tz);
/*     */             }
/* 794 */             if (!this.tzNames.containsKey(zone[4])) {
/* 795 */               this.tzNames.put(zone[4], tz);
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/* 800 */       StringBuilder sb = new StringBuilder();
/* 801 */       sb.append("(GMT[+-]\\d{1,2}:\\d{2}").append('|');
/* 802 */       sb.append("[+-]\\d{4}").append('|');
/* 803 */       for (String id : this.tzNames.keySet()) {
/* 804 */         FastDateParser.escapeRegex(sb, id, false).append('|');
/*     */       }
/* 806 */       sb.setCharAt(sb.length() - 1, ')');
/* 807 */       this.validTimeZoneChars = sb.toString();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     boolean addRegex(FastDateParser parser, StringBuilder regex) {
/* 815 */       regex.append(this.validTimeZoneChars);
/* 816 */       return true;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     void setCalendar(FastDateParser parser, Calendar cal, String value) {
/*     */       TimeZone tz;
/* 825 */       if (value.charAt(0) == '+' || value.charAt(0) == '-') {
/* 826 */         tz = TimeZone.getTimeZone("GMT" + value);
/*     */       }
/* 828 */       else if (value.startsWith("GMT")) {
/* 829 */         tz = TimeZone.getTimeZone(value);
/*     */       } else {
/*     */         
/* 832 */         tz = this.tzNames.get(value);
/* 833 */         if (tz == null) {
/* 834 */           throw new IllegalArgumentException(value + " is not a supported timezone name");
/*     */         }
/*     */       } 
/* 837 */       cal.setTimeZone(tz);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class ISO8601TimeZoneStrategy
/*     */     extends Strategy
/*     */   {
/*     */     private final String pattern;
/*     */ 
/*     */     
/*     */     ISO8601TimeZoneStrategy(String pattern) {
/* 850 */       this.pattern = pattern;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     boolean addRegex(FastDateParser parser, StringBuilder regex) {
/* 858 */       regex.append(this.pattern);
/* 859 */       return true;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     void setCalendar(FastDateParser parser, Calendar cal, String value) {
/* 867 */       if (value.equals("Z")) {
/* 868 */         cal.setTimeZone(TimeZone.getTimeZone("UTC"));
/*     */       } else {
/* 870 */         cal.setTimeZone(TimeZone.getTimeZone("GMT" + value));
/*     */       } 
/*     */     }
/*     */     
/* 874 */     private static final FastDateParser.Strategy ISO_8601_1_STRATEGY = new ISO8601TimeZoneStrategy("(Z|(?:[+-]\\d{2}))");
/* 875 */     private static final FastDateParser.Strategy ISO_8601_2_STRATEGY = new ISO8601TimeZoneStrategy("(Z|(?:[+-]\\d{2}\\d{2}))");
/* 876 */     private static final FastDateParser.Strategy ISO_8601_3_STRATEGY = new ISO8601TimeZoneStrategy("(Z|(?:[+-]\\d{2}(?::)\\d{2}))");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     static FastDateParser.Strategy getStrategy(int tokenLen) {
/* 886 */       switch (tokenLen) {
/*     */         case 1:
/* 888 */           return ISO_8601_1_STRATEGY;
/*     */         case 2:
/* 890 */           return ISO_8601_2_STRATEGY;
/*     */         case 3:
/* 892 */           return ISO_8601_3_STRATEGY;
/*     */       } 
/* 894 */       throw new IllegalArgumentException("invalid number of X");
/*     */     }
/*     */   }
/*     */ 
/*     */   
/* 899 */   private static final Strategy NUMBER_MONTH_STRATEGY = new NumberStrategy(2)
/*     */     {
/*     */       int modify(int iValue) {
/* 902 */         return iValue - 1;
/*     */       }
/*     */     };
/* 905 */   private static final Strategy LITERAL_YEAR_STRATEGY = new NumberStrategy(1);
/* 906 */   private static final Strategy WEEK_OF_YEAR_STRATEGY = new NumberStrategy(3);
/* 907 */   private static final Strategy WEEK_OF_MONTH_STRATEGY = new NumberStrategy(4);
/* 908 */   private static final Strategy DAY_OF_YEAR_STRATEGY = new NumberStrategy(6);
/* 909 */   private static final Strategy DAY_OF_MONTH_STRATEGY = new NumberStrategy(5);
/* 910 */   private static final Strategy DAY_OF_WEEK_IN_MONTH_STRATEGY = new NumberStrategy(8);
/* 911 */   private static final Strategy HOUR_OF_DAY_STRATEGY = new NumberStrategy(11);
/* 912 */   private static final Strategy HOUR24_OF_DAY_STRATEGY = new NumberStrategy(11)
/*     */     {
/*     */       int modify(int iValue) {
/* 915 */         return (iValue == 24) ? 0 : iValue;
/*     */       }
/*     */     };
/* 918 */   private static final Strategy HOUR12_STRATEGY = new NumberStrategy(10)
/*     */     {
/*     */       int modify(int iValue) {
/* 921 */         return (iValue == 12) ? 0 : iValue;
/*     */       }
/*     */     };
/* 924 */   private static final Strategy HOUR_STRATEGY = new NumberStrategy(10);
/* 925 */   private static final Strategy MINUTE_STRATEGY = new NumberStrategy(12);
/* 926 */   private static final Strategy SECOND_STRATEGY = new NumberStrategy(13);
/* 927 */   private static final Strategy MILLISECOND_STRATEGY = new NumberStrategy(14);
/* 928 */   private static final Strategy ISO_8601_STRATEGY = new ISO8601TimeZoneStrategy("(Z|(?:[+-]\\d{2}(?::?\\d{2})?))");
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\commons\lang3\time\FastDateParser.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */