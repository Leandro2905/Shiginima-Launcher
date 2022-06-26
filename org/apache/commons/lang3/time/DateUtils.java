/*      */ package org.apache.commons.lang3.time;
/*      */ 
/*      */ import java.text.ParseException;
/*      */ import java.text.ParsePosition;
/*      */ import java.text.SimpleDateFormat;
/*      */ import java.util.Calendar;
/*      */ import java.util.Date;
/*      */ import java.util.Iterator;
/*      */ import java.util.Locale;
/*      */ import java.util.NoSuchElementException;
/*      */ import java.util.concurrent.TimeUnit;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class DateUtils
/*      */ {
/*      */   public static final long MILLIS_PER_SECOND = 1000L;
/*      */   public static final long MILLIS_PER_MINUTE = 60000L;
/*      */   public static final long MILLIS_PER_HOUR = 3600000L;
/*      */   public static final long MILLIS_PER_DAY = 86400000L;
/*      */   public static final int SEMI_MONTH = 1001;
/*   82 */   private static final int[][] fields = new int[][] { { 14 }, { 13 }, { 12 }, { 11, 10 }, { 5, 5, 9 }, { 2, 1001 }, { 1 }, { 0 } };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final int RANGE_WEEK_SUNDAY = 1;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final int RANGE_WEEK_MONDAY = 2;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final int RANGE_WEEK_RELATIVE = 3;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final int RANGE_WEEK_CENTER = 4;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final int RANGE_MONTH_SUNDAY = 5;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final int RANGE_MONTH_MONDAY = 6;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private enum ModifyType
/*      */   {
/*  126 */     TRUNCATE,
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  131 */     ROUND,
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  136 */     CEILING;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isSameDay(Date date1, Date date2) {
/*  166 */     if (date1 == null || date2 == null) {
/*  167 */       throw new IllegalArgumentException("The date must not be null");
/*      */     }
/*  169 */     Calendar cal1 = Calendar.getInstance();
/*  170 */     cal1.setTime(date1);
/*  171 */     Calendar cal2 = Calendar.getInstance();
/*  172 */     cal2.setTime(date2);
/*  173 */     return isSameDay(cal1, cal2);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isSameDay(Calendar cal1, Calendar cal2) {
/*  190 */     if (cal1 == null || cal2 == null) {
/*  191 */       throw new IllegalArgumentException("The date must not be null");
/*      */     }
/*  193 */     return (cal1.get(0) == cal2.get(0) && cal1.get(1) == cal2.get(1) && cal1.get(6) == cal2.get(6));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isSameInstant(Date date1, Date date2) {
/*  211 */     if (date1 == null || date2 == null) {
/*  212 */       throw new IllegalArgumentException("The date must not be null");
/*      */     }
/*  214 */     return (date1.getTime() == date2.getTime());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isSameInstant(Calendar cal1, Calendar cal2) {
/*  229 */     if (cal1 == null || cal2 == null) {
/*  230 */       throw new IllegalArgumentException("The date must not be null");
/*      */     }
/*  232 */     return (cal1.getTime().getTime() == cal2.getTime().getTime());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isSameLocalTime(Calendar cal1, Calendar cal2) {
/*  249 */     if (cal1 == null || cal2 == null) {
/*  250 */       throw new IllegalArgumentException("The date must not be null");
/*      */     }
/*  252 */     return (cal1.get(14) == cal2.get(14) && cal1.get(13) == cal2.get(13) && cal1.get(12) == cal2.get(12) && cal1.get(11) == cal2.get(11) && cal1.get(6) == cal2.get(6) && cal1.get(1) == cal2.get(1) && cal1.get(0) == cal2.get(0) && cal1.getClass() == cal2.getClass());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Date parseDate(String str, String... parsePatterns) throws ParseException {
/*  278 */     return parseDate(str, null, parsePatterns);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Date parseDate(String str, Locale locale, String... parsePatterns) throws ParseException {
/*  301 */     return parseDateWithLeniency(str, locale, parsePatterns, true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Date parseDateStrictly(String str, String... parsePatterns) throws ParseException {
/*  321 */     return parseDateStrictly(str, null, parsePatterns);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Date parseDateStrictly(String str, Locale locale, String... parsePatterns) throws ParseException {
/*  343 */     return parseDateWithLeniency(str, null, parsePatterns, false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static Date parseDateWithLeniency(String str, Locale locale, String[] parsePatterns, boolean lenient) throws ParseException {
/*      */     SimpleDateFormat parser;
/*  365 */     if (str == null || parsePatterns == null) {
/*  366 */       throw new IllegalArgumentException("Date and Patterns must not be null");
/*      */     }
/*      */ 
/*      */     
/*  370 */     if (locale == null) {
/*  371 */       parser = new SimpleDateFormat();
/*      */     } else {
/*  373 */       parser = new SimpleDateFormat("", locale);
/*      */     } 
/*      */     
/*  376 */     parser.setLenient(lenient);
/*  377 */     ParsePosition pos = new ParsePosition(0);
/*  378 */     for (String parsePattern : parsePatterns) {
/*      */       
/*  380 */       String pattern = parsePattern;
/*      */ 
/*      */       
/*  383 */       if (parsePattern.endsWith("ZZ")) {
/*  384 */         pattern = pattern.substring(0, pattern.length() - 1);
/*      */       }
/*      */       
/*  387 */       parser.applyPattern(pattern);
/*  388 */       pos.setIndex(0);
/*      */       
/*  390 */       String str2 = str;
/*      */       
/*  392 */       if (parsePattern.endsWith("ZZ")) {
/*  393 */         str2 = str.replaceAll("([-+][0-9][0-9]):([0-9][0-9])$", "$1$2");
/*      */       }
/*      */       
/*  396 */       Date date = parser.parse(str2, pos);
/*  397 */       if (date != null && pos.getIndex() == str2.length()) {
/*  398 */         return date;
/*      */       }
/*      */     } 
/*  401 */     throw new ParseException("Unable to parse the date: " + str, -1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Date addYears(Date date, int amount) {
/*  415 */     return add(date, 1, amount);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Date addMonths(Date date, int amount) {
/*  429 */     return add(date, 2, amount);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Date addWeeks(Date date, int amount) {
/*  443 */     return add(date, 3, amount);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Date addDays(Date date, int amount) {
/*  457 */     return add(date, 5, amount);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Date addHours(Date date, int amount) {
/*  471 */     return add(date, 11, amount);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Date addMinutes(Date date, int amount) {
/*  485 */     return add(date, 12, amount);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Date addSeconds(Date date, int amount) {
/*  499 */     return add(date, 13, amount);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Date addMilliseconds(Date date, int amount) {
/*  513 */     return add(date, 14, amount);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static Date add(Date date, int calendarField, int amount) {
/*  528 */     if (date == null) {
/*  529 */       throw new IllegalArgumentException("The date must not be null");
/*      */     }
/*  531 */     Calendar c = Calendar.getInstance();
/*  532 */     c.setTime(date);
/*  533 */     c.add(calendarField, amount);
/*  534 */     return c.getTime();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Date setYears(Date date, int amount) {
/*  549 */     return set(date, 1, amount);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Date setMonths(Date date, int amount) {
/*  564 */     return set(date, 2, amount);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Date setDays(Date date, int amount) {
/*  579 */     return set(date, 5, amount);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Date setHours(Date date, int amount) {
/*  595 */     return set(date, 11, amount);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Date setMinutes(Date date, int amount) {
/*  610 */     return set(date, 12, amount);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Date setSeconds(Date date, int amount) {
/*  625 */     return set(date, 13, amount);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Date setMilliseconds(Date date, int amount) {
/*  640 */     return set(date, 14, amount);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static Date set(Date date, int calendarField, int amount) {
/*  657 */     if (date == null) {
/*  658 */       throw new IllegalArgumentException("The date must not be null");
/*      */     }
/*      */     
/*  661 */     Calendar c = Calendar.getInstance();
/*  662 */     c.setLenient(false);
/*  663 */     c.setTime(date);
/*  664 */     c.set(calendarField, amount);
/*  665 */     return c.getTime();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Calendar toCalendar(Date date) {
/*  678 */     Calendar c = Calendar.getInstance();
/*  679 */     c.setTime(date);
/*  680 */     return c;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Date round(Date date, int field) {
/*  711 */     if (date == null) {
/*  712 */       throw new IllegalArgumentException("The date must not be null");
/*      */     }
/*  714 */     Calendar gval = Calendar.getInstance();
/*  715 */     gval.setTime(date);
/*  716 */     modify(gval, field, ModifyType.ROUND);
/*  717 */     return gval.getTime();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Calendar round(Calendar date, int field) {
/*  748 */     if (date == null) {
/*  749 */       throw new IllegalArgumentException("The date must not be null");
/*      */     }
/*  751 */     Calendar rounded = (Calendar)date.clone();
/*  752 */     modify(rounded, field, ModifyType.ROUND);
/*  753 */     return rounded;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Date round(Object date, int field) {
/*  785 */     if (date == null) {
/*  786 */       throw new IllegalArgumentException("The date must not be null");
/*      */     }
/*  788 */     if (date instanceof Date)
/*  789 */       return round((Date)date, field); 
/*  790 */     if (date instanceof Calendar) {
/*  791 */       return round((Calendar)date, field).getTime();
/*      */     }
/*  793 */     throw new ClassCastException("Could not round " + date);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Date truncate(Date date, int field) {
/*  814 */     if (date == null) {
/*  815 */       throw new IllegalArgumentException("The date must not be null");
/*      */     }
/*  817 */     Calendar gval = Calendar.getInstance();
/*  818 */     gval.setTime(date);
/*  819 */     modify(gval, field, ModifyType.TRUNCATE);
/*  820 */     return gval.getTime();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Calendar truncate(Calendar date, int field) {
/*  839 */     if (date == null) {
/*  840 */       throw new IllegalArgumentException("The date must not be null");
/*      */     }
/*  842 */     Calendar truncated = (Calendar)date.clone();
/*  843 */     modify(truncated, field, ModifyType.TRUNCATE);
/*  844 */     return truncated;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Date truncate(Object date, int field) {
/*  864 */     if (date == null) {
/*  865 */       throw new IllegalArgumentException("The date must not be null");
/*      */     }
/*  867 */     if (date instanceof Date)
/*  868 */       return truncate((Date)date, field); 
/*  869 */     if (date instanceof Calendar) {
/*  870 */       return truncate((Calendar)date, field).getTime();
/*      */     }
/*  872 */     throw new ClassCastException("Could not truncate " + date);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Date ceiling(Date date, int field) {
/*  894 */     if (date == null) {
/*  895 */       throw new IllegalArgumentException("The date must not be null");
/*      */     }
/*  897 */     Calendar gval = Calendar.getInstance();
/*  898 */     gval.setTime(date);
/*  899 */     modify(gval, field, ModifyType.CEILING);
/*  900 */     return gval.getTime();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Calendar ceiling(Calendar date, int field) {
/*  920 */     if (date == null) {
/*  921 */       throw new IllegalArgumentException("The date must not be null");
/*      */     }
/*  923 */     Calendar ceiled = (Calendar)date.clone();
/*  924 */     modify(ceiled, field, ModifyType.CEILING);
/*  925 */     return ceiled;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Date ceiling(Object date, int field) {
/*  946 */     if (date == null) {
/*  947 */       throw new IllegalArgumentException("The date must not be null");
/*      */     }
/*  949 */     if (date instanceof Date)
/*  950 */       return ceiling((Date)date, field); 
/*  951 */     if (date instanceof Calendar) {
/*  952 */       return ceiling((Calendar)date, field).getTime();
/*      */     }
/*  954 */     throw new ClassCastException("Could not find ceiling of for type: " + date.getClass());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void modify(Calendar val, int field, ModifyType modType) {
/*  968 */     if (val.get(1) > 280000000) {
/*  969 */       throw new ArithmeticException("Calendar value too large for accurate calculations");
/*      */     }
/*      */     
/*  972 */     if (field == 14) {
/*      */       return;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  982 */     Date date = val.getTime();
/*  983 */     long time = date.getTime();
/*  984 */     boolean done = false;
/*      */ 
/*      */     
/*  987 */     int millisecs = val.get(14);
/*  988 */     if (ModifyType.TRUNCATE == modType || millisecs < 500) {
/*  989 */       time -= millisecs;
/*      */     }
/*  991 */     if (field == 13) {
/*  992 */       done = true;
/*      */     }
/*      */ 
/*      */     
/*  996 */     int seconds = val.get(13);
/*  997 */     if (!done && (ModifyType.TRUNCATE == modType || seconds < 30)) {
/*  998 */       time -= seconds * 1000L;
/*      */     }
/* 1000 */     if (field == 12) {
/* 1001 */       done = true;
/*      */     }
/*      */ 
/*      */     
/* 1005 */     int minutes = val.get(12);
/* 1006 */     if (!done && (ModifyType.TRUNCATE == modType || minutes < 30)) {
/* 1007 */       time -= minutes * 60000L;
/*      */     }
/*      */ 
/*      */     
/* 1011 */     if (date.getTime() != time) {
/* 1012 */       date.setTime(time);
/* 1013 */       val.setTime(date);
/*      */     } 
/*      */ 
/*      */     
/* 1017 */     boolean roundUp = false;
/* 1018 */     for (int[] aField : fields) {
/* 1019 */       for (int element : aField) {
/* 1020 */         if (element == field) {
/*      */           
/* 1022 */           if (modType == ModifyType.CEILING || (modType == ModifyType.ROUND && roundUp)) {
/* 1023 */             if (field == 1001) {
/*      */ 
/*      */ 
/*      */               
/* 1027 */               if (val.get(5) == 1) {
/* 1028 */                 val.add(5, 15);
/*      */               } else {
/* 1030 */                 val.add(5, -15);
/* 1031 */                 val.add(2, 1);
/*      */               }
/*      */             
/* 1034 */             } else if (field == 9) {
/*      */ 
/*      */ 
/*      */               
/* 1038 */               if (val.get(11) == 0) {
/* 1039 */                 val.add(11, 12);
/*      */               } else {
/* 1041 */                 val.add(11, -12);
/* 1042 */                 val.add(5, 1);
/*      */               }
/*      */             
/*      */             }
/*      */             else {
/*      */               
/* 1048 */               val.add(aField[0], 1);
/*      */             } 
/*      */           }
/*      */           
/*      */           return;
/*      */         } 
/*      */       } 
/* 1055 */       int offset = 0;
/* 1056 */       boolean offsetSet = false;
/*      */       
/* 1058 */       switch (field) {
/*      */         case 1001:
/* 1060 */           if (aField[0] == 5) {
/*      */ 
/*      */ 
/*      */             
/* 1064 */             offset = val.get(5) - 1;
/*      */ 
/*      */             
/* 1067 */             if (offset >= 15) {
/* 1068 */               offset -= 15;
/*      */             }
/*      */             
/* 1071 */             roundUp = (offset > 7);
/* 1072 */             offsetSet = true;
/*      */           } 
/*      */           break;
/*      */         case 9:
/* 1076 */           if (aField[0] == 11) {
/*      */ 
/*      */             
/* 1079 */             offset = val.get(11);
/* 1080 */             if (offset >= 12) {
/* 1081 */               offset -= 12;
/*      */             }
/* 1083 */             roundUp = (offset >= 6);
/* 1084 */             offsetSet = true;
/*      */           } 
/*      */           break;
/*      */       } 
/*      */ 
/*      */       
/* 1090 */       if (!offsetSet) {
/* 1091 */         int min = val.getActualMinimum(aField[0]);
/* 1092 */         int max = val.getActualMaximum(aField[0]);
/*      */         
/* 1094 */         offset = val.get(aField[0]) - min;
/*      */         
/* 1096 */         roundUp = (offset > (max - min) / 2);
/*      */       } 
/*      */       
/* 1099 */       if (offset != 0) {
/* 1100 */         val.set(aField[0], val.get(aField[0]) - offset);
/*      */       }
/*      */     } 
/* 1103 */     throw new IllegalArgumentException("The field " + field + " is not supported");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Iterator<Calendar> iterator(Date focus, int rangeStyle) {
/* 1133 */     if (focus == null) {
/* 1134 */       throw new IllegalArgumentException("The date must not be null");
/*      */     }
/* 1136 */     Calendar gval = Calendar.getInstance();
/* 1137 */     gval.setTime(focus);
/* 1138 */     return iterator(gval, rangeStyle);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Iterator<Calendar> iterator(Calendar focus, int rangeStyle) {
/* 1166 */     if (focus == null) {
/* 1167 */       throw new IllegalArgumentException("The date must not be null");
/*      */     }
/* 1169 */     Calendar start = null;
/* 1170 */     Calendar end = null;
/* 1171 */     int startCutoff = 1;
/* 1172 */     int endCutoff = 7;
/* 1173 */     switch (rangeStyle) {
/*      */       
/*      */       case 5:
/*      */       case 6:
/* 1177 */         start = truncate(focus, 2);
/*      */         
/* 1179 */         end = (Calendar)start.clone();
/* 1180 */         end.add(2, 1);
/* 1181 */         end.add(5, -1);
/*      */         
/* 1183 */         if (rangeStyle == 6) {
/* 1184 */           startCutoff = 2;
/* 1185 */           endCutoff = 1;
/*      */         } 
/*      */         break;
/*      */       
/*      */       case 1:
/*      */       case 2:
/*      */       case 3:
/*      */       case 4:
/* 1193 */         start = truncate(focus, 5);
/* 1194 */         end = truncate(focus, 5);
/* 1195 */         switch (rangeStyle) {
/*      */ 
/*      */ 
/*      */           
/*      */           case 2:
/* 1200 */             startCutoff = 2;
/* 1201 */             endCutoff = 1;
/*      */             break;
/*      */           case 3:
/* 1204 */             startCutoff = focus.get(7);
/* 1205 */             endCutoff = startCutoff - 1;
/*      */             break;
/*      */           case 4:
/* 1208 */             startCutoff = focus.get(7) - 3;
/* 1209 */             endCutoff = focus.get(7) + 3;
/*      */             break;
/*      */         } 
/*      */         
/*      */         break;
/*      */       
/*      */       default:
/* 1216 */         throw new IllegalArgumentException("The range style " + rangeStyle + " is not valid.");
/*      */     } 
/* 1218 */     if (startCutoff < 1) {
/* 1219 */       startCutoff += 7;
/*      */     }
/* 1221 */     if (startCutoff > 7) {
/* 1222 */       startCutoff -= 7;
/*      */     }
/* 1224 */     if (endCutoff < 1) {
/* 1225 */       endCutoff += 7;
/*      */     }
/* 1227 */     if (endCutoff > 7) {
/* 1228 */       endCutoff -= 7;
/*      */     }
/* 1230 */     while (start.get(7) != startCutoff) {
/* 1231 */       start.add(5, -1);
/*      */     }
/* 1233 */     while (end.get(7) != endCutoff) {
/* 1234 */       end.add(5, 1);
/*      */     }
/* 1236 */     return new DateIterator(start, end);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Iterator<?> iterator(Object focus, int rangeStyle) {
/* 1256 */     if (focus == null) {
/* 1257 */       throw new IllegalArgumentException("The date must not be null");
/*      */     }
/* 1259 */     if (focus instanceof Date)
/* 1260 */       return iterator((Date)focus, rangeStyle); 
/* 1261 */     if (focus instanceof Calendar) {
/* 1262 */       return iterator((Calendar)focus, rangeStyle);
/*      */     }
/* 1264 */     throw new ClassCastException("Could not iterate based on " + focus);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long getFragmentInMilliseconds(Date date, int fragment) {
/* 1300 */     return getFragment(date, fragment, TimeUnit.MILLISECONDS);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long getFragmentInSeconds(Date date, int fragment) {
/* 1338 */     return getFragment(date, fragment, TimeUnit.SECONDS);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long getFragmentInMinutes(Date date, int fragment) {
/* 1376 */     return getFragment(date, fragment, TimeUnit.MINUTES);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long getFragmentInHours(Date date, int fragment) {
/* 1414 */     return getFragment(date, fragment, TimeUnit.HOURS);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long getFragmentInDays(Date date, int fragment) {
/* 1452 */     return getFragment(date, fragment, TimeUnit.DAYS);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long getFragmentInMilliseconds(Calendar calendar, int fragment) {
/* 1490 */     return getFragment(calendar, fragment, TimeUnit.MILLISECONDS);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long getFragmentInSeconds(Calendar calendar, int fragment) {
/* 1527 */     return getFragment(calendar, fragment, TimeUnit.SECONDS);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long getFragmentInMinutes(Calendar calendar, int fragment) {
/* 1565 */     return getFragment(calendar, fragment, TimeUnit.MINUTES);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long getFragmentInHours(Calendar calendar, int fragment) {
/* 1603 */     return getFragment(calendar, fragment, TimeUnit.HOURS);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long getFragmentInDays(Calendar calendar, int fragment) {
/* 1643 */     return getFragment(calendar, fragment, TimeUnit.DAYS);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static long getFragment(Date date, int fragment, TimeUnit unit) {
/* 1658 */     if (date == null) {
/* 1659 */       throw new IllegalArgumentException("The date must not be null");
/*      */     }
/* 1661 */     Calendar calendar = Calendar.getInstance();
/* 1662 */     calendar.setTime(date);
/* 1663 */     return getFragment(calendar, fragment, unit);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static long getFragment(Calendar calendar, int fragment, TimeUnit unit) {
/* 1678 */     if (calendar == null) {
/* 1679 */       throw new IllegalArgumentException("The date must not be null");
/*      */     }
/*      */     
/* 1682 */     long result = 0L;
/*      */     
/* 1684 */     int offset = (unit == TimeUnit.DAYS) ? 0 : 1;
/*      */ 
/*      */     
/* 1687 */     switch (fragment) {
/*      */       case 1:
/* 1689 */         result += unit.convert((calendar.get(6) - offset), TimeUnit.DAYS);
/*      */         break;
/*      */       case 2:
/* 1692 */         result += unit.convert((calendar.get(5) - offset), TimeUnit.DAYS);
/*      */         break;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1698 */     switch (fragment) {
/*      */ 
/*      */ 
/*      */       
/*      */       case 1:
/*      */       case 2:
/*      */       case 5:
/*      */       case 6:
/* 1706 */         result += unit.convert(calendar.get(11), TimeUnit.HOURS);
/*      */       
/*      */       case 11:
/* 1709 */         result += unit.convert(calendar.get(12), TimeUnit.MINUTES);
/*      */       
/*      */       case 12:
/* 1712 */         result += unit.convert(calendar.get(13), TimeUnit.SECONDS);
/*      */       
/*      */       case 13:
/* 1715 */         result += unit.convert(calendar.get(14), TimeUnit.MILLISECONDS);
/*      */ 
/*      */ 
/*      */       
/*      */       case 14:
/* 1720 */         return result;
/*      */     } 
/*      */     throw new IllegalArgumentException("The fragment " + fragment + " is not supported");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean truncatedEquals(Calendar cal1, Calendar cal2, int field) {
/* 1737 */     return (truncatedCompareTo(cal1, cal2, field) == 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean truncatedEquals(Date date1, Date date2, int field) {
/* 1754 */     return (truncatedCompareTo(date1, date2, field) == 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int truncatedCompareTo(Calendar cal1, Calendar cal2, int field) {
/* 1772 */     Calendar truncatedCal1 = truncate(cal1, field);
/* 1773 */     Calendar truncatedCal2 = truncate(cal2, field);
/* 1774 */     return truncatedCal1.compareTo(truncatedCal2);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int truncatedCompareTo(Date date1, Date date2, int field) {
/* 1792 */     Date truncatedDate1 = truncate(date1, field);
/* 1793 */     Date truncatedDate2 = truncate(date2, field);
/* 1794 */     return truncatedDate1.compareTo(truncatedDate2);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static class DateIterator
/*      */     implements Iterator<Calendar>
/*      */   {
/*      */     private final Calendar endFinal;
/*      */ 
/*      */ 
/*      */     
/*      */     private final Calendar spot;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     DateIterator(Calendar startFinal, Calendar endFinal) {
/* 1814 */       this.endFinal = endFinal;
/* 1815 */       this.spot = startFinal;
/* 1816 */       this.spot.add(5, -1);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean hasNext() {
/* 1826 */       return this.spot.before(this.endFinal);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Calendar next() {
/* 1836 */       if (this.spot.equals(this.endFinal)) {
/* 1837 */         throw new NoSuchElementException();
/*      */       }
/* 1839 */       this.spot.add(5, 1);
/* 1840 */       return (Calendar)this.spot.clone();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void remove() {
/* 1851 */       throw new UnsupportedOperationException();
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\commons\lang3\time\DateUtils.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */