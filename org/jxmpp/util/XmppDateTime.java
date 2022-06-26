/*     */ package org.jxmpp.util;
/*     */ 
/*     */ import java.text.DateFormat;
/*     */ import java.text.ParseException;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Calendar;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import java.util.TimeZone;
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
/*     */ public class XmppDateTime
/*     */ {
/*  34 */   private static final DateFormatType dateFormatter = DateFormatType.XEP_0082_DATE_PROFILE;
/*  35 */   private static final Pattern datePattern = Pattern.compile("^\\d+-\\d+-\\d+$");
/*     */   
/*  37 */   private static final DateFormatType timeFormatter = DateFormatType.XEP_0082_TIME_MILLIS_ZONE_PROFILE;
/*  38 */   private static final Pattern timePattern = Pattern.compile("^(\\d+:){2}\\d+.\\d+(Z|([+-](\\d+:\\d+)))$");
/*  39 */   private static final DateFormatType timeNoZoneFormatter = DateFormatType.XEP_0082_TIME_MILLIS_PROFILE;
/*  40 */   private static final Pattern timeNoZonePattern = Pattern.compile("^(\\d+:){2}\\d+.\\d+$");
/*     */   
/*  42 */   private static final DateFormatType timeNoMillisFormatter = DateFormatType.XEP_0082_TIME_ZONE_PROFILE;
/*  43 */   private static final Pattern timeNoMillisPattern = Pattern.compile("^(\\d+:){2}\\d+(Z|([+-](\\d+:\\d+)))$");
/*  44 */   private static final DateFormatType timeNoMillisNoZoneFormatter = DateFormatType.XEP_0082_TIME_PROFILE;
/*  45 */   private static final Pattern timeNoMillisNoZonePattern = Pattern.compile("^(\\d+:){2}\\d+$");
/*     */   
/*  47 */   private static final DateFormatType dateTimeFormatter = DateFormatType.XEP_0082_DATETIME_MILLIS_PROFILE;
/*  48 */   private static final Pattern dateTimePattern = Pattern.compile("^\\d+(-\\d+){2}+T(\\d+:){2}\\d+.\\d+(Z|([+-](\\d+:\\d+)))?$");
/*     */   
/*  50 */   private static final DateFormatType dateTimeNoMillisFormatter = DateFormatType.XEP_0082_DATETIME_PROFILE;
/*  51 */   private static final Pattern dateTimeNoMillisPattern = Pattern.compile("^\\d+(-\\d+){2}+T(\\d+:){2}\\d+(Z|([+-](\\d+:\\d+)))?$");
/*     */ 
/*     */   
/*  54 */   private static final DateFormat xep0091Formatter = new SimpleDateFormat("yyyyMMdd'T'HH:mm:ss");
/*  55 */   private static final DateFormat xep0091Date6DigitFormatter = new SimpleDateFormat("yyyyMd'T'HH:mm:ss");
/*  56 */   private static final DateFormat xep0091Date7Digit1MonthFormatter = new SimpleDateFormat("yyyyMdd'T'HH:mm:ss");
/*  57 */   private static final DateFormat xep0091Date7Digit2MonthFormatter = new SimpleDateFormat("yyyyMMd'T'HH:mm:ss");
/*  58 */   private static final Pattern xep0091Pattern = Pattern.compile("^\\d+T\\d+:\\d+:\\d+$");
/*     */   
/*     */   public enum DateFormatType
/*     */   {
/*  62 */     XEP_0082_DATE_PROFILE("yyyy-MM-dd"),
/*  63 */     XEP_0082_DATETIME_PROFILE("yyyy-MM-dd'T'HH:mm:ssZ"),
/*  64 */     XEP_0082_DATETIME_MILLIS_PROFILE("yyyy-MM-dd'T'HH:mm:ss.SSSZ"),
/*  65 */     XEP_0082_TIME_PROFILE("hh:mm:ss"),
/*  66 */     XEP_0082_TIME_ZONE_PROFILE("hh:mm:ssZ"),
/*  67 */     XEP_0082_TIME_MILLIS_PROFILE("hh:mm:ss.SSS"),
/*  68 */     XEP_0082_TIME_MILLIS_ZONE_PROFILE("hh:mm:ss.SSSZ"),
/*  69 */     XEP_0091_DATETIME("yyyyMMdd'T'HH:mm:ss");
/*     */ 
/*     */     
/*     */     private final String FORMAT_STRING;
/*     */ 
/*     */     
/*     */     private final DateFormat FORMATTER;
/*     */ 
/*     */     
/*     */     private final boolean CONVERT_TIMEZONE;
/*     */ 
/*     */     
/*     */     private final boolean HANDLE_MILLIS;
/*     */ 
/*     */ 
/*     */     
/*     */     DateFormatType(String dateFormat) {
/*  86 */       this.FORMAT_STRING = dateFormat;
/*  87 */       this.FORMATTER = new SimpleDateFormat(this.FORMAT_STRING);
/*  88 */       this.FORMATTER.setTimeZone(TimeZone.getTimeZone("UTC"));
/*  89 */       this.CONVERT_TIMEZONE = (dateFormat.charAt(dateFormat.length() - 1) == 'Z');
/*  90 */       this.HANDLE_MILLIS = dateFormat.contains("SSS");
/*     */     }
/*     */     
/*     */     public String format(Date date) {
/*     */       String res;
/*  95 */       synchronized (this.FORMATTER) {
/*  96 */         res = this.FORMATTER.format(date);
/*     */       } 
/*  98 */       if (this.CONVERT_TIMEZONE) {
/*  99 */         res = XmppDateTime.convertRfc822TimezoneToXep82(res);
/*     */       }
/* 101 */       return res;
/*     */     }
/*     */     
/*     */     public Date parse(String dateString) throws ParseException {
/* 105 */       if (this.CONVERT_TIMEZONE) {
/* 106 */         dateString = XmppDateTime.convertXep82TimezoneToRfc822(dateString);
/*     */       }
/* 108 */       if (this.HANDLE_MILLIS) {
/* 109 */         dateString = XmppDateTime.handleMilliseconds(dateString);
/*     */       }
/* 111 */       synchronized (this.FORMATTER) {
/* 112 */         return this.FORMATTER.parse(dateString);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/* 117 */   private static final List<PatternCouplings> couplings = new ArrayList<>();
/*     */   
/*     */   static {
/* 120 */     TimeZone utc = TimeZone.getTimeZone("UTC");
/*     */     
/* 122 */     xep0091Formatter.setTimeZone(utc);
/* 123 */     xep0091Date6DigitFormatter.setTimeZone(utc);
/* 124 */     xep0091Date7Digit1MonthFormatter.setTimeZone(utc);
/* 125 */     xep0091Date7Digit1MonthFormatter.setLenient(false);
/* 126 */     xep0091Date7Digit2MonthFormatter.setTimeZone(utc);
/* 127 */     xep0091Date7Digit2MonthFormatter.setLenient(false);
/*     */     
/* 129 */     couplings.add(new PatternCouplings(datePattern, dateFormatter));
/* 130 */     couplings.add(new PatternCouplings(dateTimePattern, dateTimeFormatter));
/* 131 */     couplings.add(new PatternCouplings(dateTimeNoMillisPattern, dateTimeNoMillisFormatter));
/* 132 */     couplings.add(new PatternCouplings(timePattern, timeFormatter));
/* 133 */     couplings.add(new PatternCouplings(timeNoZonePattern, timeNoZoneFormatter));
/* 134 */     couplings.add(new PatternCouplings(timeNoMillisPattern, timeNoMillisFormatter));
/* 135 */     couplings.add(new PatternCouplings(timeNoMillisNoZonePattern, timeNoMillisNoZoneFormatter));
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
/*     */   public static Date parseXEP0082Date(String dateString) throws ParseException {
/* 150 */     for (PatternCouplings coupling : couplings) {
/* 151 */       Matcher matcher = coupling.pattern.matcher(dateString);
/*     */       
/* 153 */       if (matcher.matches()) {
/* 154 */         return coupling.formatter.parse(dateString);
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 162 */     synchronized (dateTimeNoMillisFormatter) {
/* 163 */       return dateTimeNoMillisFormatter.parse(dateString);
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
/*     */   public static Date parseDate(String dateString) throws ParseException {
/* 183 */     Matcher matcher = xep0091Pattern.matcher(dateString);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 189 */     if (matcher.matches()) {
/* 190 */       int length = dateString.split("T")[0].length();
/*     */       
/* 192 */       if (length < 8) {
/* 193 */         Date date = handleDateWithMissingLeadingZeros(dateString, length);
/*     */         
/* 195 */         if (date != null)
/* 196 */           return date; 
/*     */       } else {
/* 198 */         synchronized (xep0091Formatter) {
/* 199 */           return xep0091Formatter.parse(dateString);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 204 */     return parseXEP0082Date(dateString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String formatXEP0082Date(Date date) {
/* 215 */     synchronized (dateTimeFormatter) {
/* 216 */       return dateTimeFormatter.format(date);
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
/*     */   public static String convertXep82TimezoneToRfc822(String dateString) {
/* 229 */     if (dateString.charAt(dateString.length() - 1) == 'Z') {
/* 230 */       return dateString.replace("Z", "+0000");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 236 */     return dateString.replaceAll("([\\+\\-]\\d\\d):(\\d\\d)", "$1$2");
/*     */   }
/*     */ 
/*     */   
/*     */   public static String convertRfc822TimezoneToXep82(String dateString) {
/* 241 */     int length = dateString.length();
/* 242 */     String res = dateString.substring(0, length - 2);
/* 243 */     res = res + ':';
/* 244 */     res = res + dateString.substring(length - 2, length);
/* 245 */     return res;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String asString(TimeZone timeZone) {
/* 255 */     int rawOffset = timeZone.getRawOffset();
/* 256 */     int hours = rawOffset / 3600000;
/* 257 */     int minutes = Math.abs(rawOffset / 60000 - hours * 60);
/* 258 */     return String.format("%+d:%02d", new Object[] { Integer.valueOf(hours), Integer.valueOf(minutes) });
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
/*     */   private static Date handleDateWithMissingLeadingZeros(String stampString, int dateLength) throws ParseException {
/* 274 */     if (dateLength == 6) {
/* 275 */       synchronized (xep0091Date6DigitFormatter) {
/* 276 */         return xep0091Date6DigitFormatter.parse(stampString);
/*     */       } 
/*     */     }
/* 279 */     Calendar now = Calendar.getInstance();
/*     */     
/* 281 */     Calendar oneDigitMonth = parseXEP91Date(stampString, xep0091Date7Digit1MonthFormatter);
/* 282 */     Calendar twoDigitMonth = parseXEP91Date(stampString, xep0091Date7Digit2MonthFormatter);
/*     */     
/* 284 */     List<Calendar> dates = filterDatesBefore(now, new Calendar[] { oneDigitMonth, twoDigitMonth });
/*     */     
/* 286 */     if (!dates.isEmpty()) {
/* 287 */       return determineNearestDate(now, dates).getTime();
/*     */     }
/* 289 */     return null;
/*     */   }
/*     */   
/*     */   private static Calendar parseXEP91Date(String stampString, DateFormat dateFormat) {
/*     */     try {
/* 294 */       synchronized (dateFormat) {
/* 295 */         dateFormat.parse(stampString);
/* 296 */         return dateFormat.getCalendar();
/*     */       } 
/* 298 */     } catch (ParseException e) {
/* 299 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   private static List<Calendar> filterDatesBefore(Calendar now, Calendar... dates) {
/* 304 */     List<Calendar> result = new ArrayList<>();
/*     */     
/* 306 */     for (Calendar calendar : dates) {
/* 307 */       if (calendar != null && calendar.before(now)) {
/* 308 */         result.add(calendar);
/*     */       }
/*     */     } 
/*     */     
/* 312 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 321 */   private static final Pattern SECOND_FRACTION = Pattern.compile(".*\\.(\\d{1,})(Z|((\\+|-)\\d{4}))");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String handleMilliseconds(String dateString) {
/* 334 */     Matcher matcher = SECOND_FRACTION.matcher(dateString);
/* 335 */     if (!matcher.matches())
/*     */     {
/* 337 */       return dateString;
/*     */     }
/*     */     
/* 340 */     int fractionalSecondsDigitCount = matcher.group(1).length();
/* 341 */     if (fractionalSecondsDigitCount == 3)
/*     */     {
/* 343 */       return dateString;
/*     */     }
/*     */ 
/*     */     
/* 347 */     int posDecimal = dateString.indexOf(".");
/* 348 */     StringBuilder sb = new StringBuilder(dateString.length() - fractionalSecondsDigitCount + 3);
/* 349 */     if (fractionalSecondsDigitCount > 3) {
/*     */       
/* 351 */       sb.append(dateString.substring(0, posDecimal + 4));
/*     */     } else {
/*     */       
/* 354 */       sb.append(dateString.substring(0, posDecimal + fractionalSecondsDigitCount + 1));
/*     */       
/* 356 */       for (int i = fractionalSecondsDigitCount; i < 3; i++) {
/* 357 */         sb.append('0');
/*     */       }
/*     */     } 
/*     */     
/* 361 */     sb.append(dateString.substring(posDecimal + fractionalSecondsDigitCount + 1));
/* 362 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   private static Calendar determineNearestDate(final Calendar now, List<Calendar> dates) {
/* 367 */     Collections.sort(dates, new Comparator<Calendar>()
/*     */         {
/*     */           public int compare(Calendar o1, Calendar o2) {
/* 370 */             Long diff1 = new Long(now.getTimeInMillis() - o1.getTimeInMillis());
/* 371 */             Long diff2 = new Long(now.getTimeInMillis() - o2.getTimeInMillis());
/* 372 */             return diff1.compareTo(diff2);
/*     */           }
/*     */         });
/*     */ 
/*     */     
/* 377 */     return dates.get(0);
/*     */   }
/*     */   
/*     */   private static class PatternCouplings {
/*     */     final Pattern pattern;
/*     */     final XmppDateTime.DateFormatType formatter;
/*     */     
/*     */     public PatternCouplings(Pattern datePattern, XmppDateTime.DateFormatType dateFormat) {
/* 385 */       this.pattern = datePattern;
/* 386 */       this.formatter = dateFormat;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jxmp\\util\XmppDateTime.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */