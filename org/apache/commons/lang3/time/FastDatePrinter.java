/*      */ package org.apache.commons.lang3.time;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.Serializable;
/*      */ import java.text.DateFormatSymbols;
/*      */ import java.text.FieldPosition;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Calendar;
/*      */ import java.util.Date;
/*      */ import java.util.GregorianCalendar;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.TimeZone;
/*      */ import java.util.concurrent.ConcurrentHashMap;
/*      */ import java.util.concurrent.ConcurrentMap;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class FastDatePrinter
/*      */   implements DatePrinter, Serializable
/*      */ {
/*      */   private static final long serialVersionUID = 1L;
/*      */   public static final int FULL = 0;
/*      */   public static final int LONG = 1;
/*      */   public static final int MEDIUM = 2;
/*      */   public static final int SHORT = 3;
/*      */   private final String mPattern;
/*      */   private final TimeZone mTimeZone;
/*      */   private final Locale mLocale;
/*      */   private transient Rule[] mRules;
/*      */   private transient int mMaxLengthEstimate;
/*      */   
/*      */   protected FastDatePrinter(String pattern, TimeZone timeZone, Locale locale) {
/*  151 */     this.mPattern = pattern;
/*  152 */     this.mTimeZone = timeZone;
/*  153 */     this.mLocale = locale;
/*      */     
/*  155 */     init();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void init() {
/*  162 */     List<Rule> rulesList = parsePattern();
/*  163 */     this.mRules = rulesList.<Rule>toArray(new Rule[rulesList.size()]);
/*      */     
/*  165 */     int len = 0;
/*  166 */     for (int i = this.mRules.length; --i >= 0;) {
/*  167 */       len += this.mRules[i].estimateLength();
/*      */     }
/*      */     
/*  170 */     this.mMaxLengthEstimate = len;
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
/*      */   protected List<Rule> parsePattern() {
/*  182 */     DateFormatSymbols symbols = new DateFormatSymbols(this.mLocale);
/*  183 */     List<Rule> rules = new ArrayList<Rule>();
/*      */     
/*  185 */     String[] ERAs = symbols.getEras();
/*  186 */     String[] months = symbols.getMonths();
/*  187 */     String[] shortMonths = symbols.getShortMonths();
/*  188 */     String[] weekdays = symbols.getWeekdays();
/*  189 */     String[] shortWeekdays = symbols.getShortWeekdays();
/*  190 */     String[] AmPmStrings = symbols.getAmPmStrings();
/*      */     
/*  192 */     int length = this.mPattern.length();
/*  193 */     int[] indexRef = new int[1];
/*      */     
/*  195 */     for (int i = 0; i < length; i++) {
/*  196 */       Rule rule; String sub; indexRef[0] = i;
/*  197 */       String token = parseToken(this.mPattern, indexRef);
/*  198 */       i = indexRef[0];
/*      */       
/*  200 */       int tokenLen = token.length();
/*  201 */       if (tokenLen == 0) {
/*      */         break;
/*      */       }
/*      */ 
/*      */       
/*  206 */       char c = token.charAt(0);
/*      */       
/*  208 */       switch (c) {
/*      */         case 'G':
/*  210 */           rule = new TextField(0, ERAs);
/*      */           break;
/*      */         case 'y':
/*  213 */           if (tokenLen == 2) {
/*  214 */             rule = TwoDigitYearField.INSTANCE; break;
/*      */           } 
/*  216 */           rule = selectNumberRule(1, (tokenLen < 4) ? 4 : tokenLen);
/*      */           break;
/*      */         
/*      */         case 'M':
/*  220 */           if (tokenLen >= 4) {
/*  221 */             rule = new TextField(2, months); break;
/*  222 */           }  if (tokenLen == 3) {
/*  223 */             rule = new TextField(2, shortMonths); break;
/*  224 */           }  if (tokenLen == 2) {
/*  225 */             rule = TwoDigitMonthField.INSTANCE; break;
/*      */           } 
/*  227 */           rule = UnpaddedMonthField.INSTANCE;
/*      */           break;
/*      */         
/*      */         case 'd':
/*  231 */           rule = selectNumberRule(5, tokenLen);
/*      */           break;
/*      */         case 'h':
/*  234 */           rule = new TwelveHourField(selectNumberRule(10, tokenLen));
/*      */           break;
/*      */         case 'H':
/*  237 */           rule = selectNumberRule(11, tokenLen);
/*      */           break;
/*      */         case 'm':
/*  240 */           rule = selectNumberRule(12, tokenLen);
/*      */           break;
/*      */         case 's':
/*  243 */           rule = selectNumberRule(13, tokenLen);
/*      */           break;
/*      */         case 'S':
/*  246 */           rule = selectNumberRule(14, tokenLen);
/*      */           break;
/*      */         case 'E':
/*  249 */           rule = new TextField(7, (tokenLen < 4) ? shortWeekdays : weekdays);
/*      */           break;
/*      */         case 'D':
/*  252 */           rule = selectNumberRule(6, tokenLen);
/*      */           break;
/*      */         case 'F':
/*  255 */           rule = selectNumberRule(8, tokenLen);
/*      */           break;
/*      */         case 'w':
/*  258 */           rule = selectNumberRule(3, tokenLen);
/*      */           break;
/*      */         case 'W':
/*  261 */           rule = selectNumberRule(4, tokenLen);
/*      */           break;
/*      */         case 'a':
/*  264 */           rule = new TextField(9, AmPmStrings);
/*      */           break;
/*      */         case 'k':
/*  267 */           rule = new TwentyFourHourField(selectNumberRule(11, tokenLen));
/*      */           break;
/*      */         case 'K':
/*  270 */           rule = selectNumberRule(10, tokenLen);
/*      */           break;
/*      */         case 'X':
/*  273 */           rule = Iso8601_Rule.getRule(tokenLen);
/*      */           break;
/*      */         case 'z':
/*  276 */           if (tokenLen >= 4) {
/*  277 */             rule = new TimeZoneNameRule(this.mTimeZone, this.mLocale, 1); break;
/*      */           } 
/*  279 */           rule = new TimeZoneNameRule(this.mTimeZone, this.mLocale, 0);
/*      */           break;
/*      */         
/*      */         case 'Z':
/*  283 */           if (tokenLen == 1) {
/*  284 */             rule = TimeZoneNumberRule.INSTANCE_NO_COLON; break;
/*  285 */           }  if (tokenLen == 2) {
/*  286 */             rule = TimeZoneNumberRule.INSTANCE_ISO_8601; break;
/*      */           } 
/*  288 */           rule = TimeZoneNumberRule.INSTANCE_COLON;
/*      */           break;
/*      */         
/*      */         case '\'':
/*  292 */           sub = token.substring(1);
/*  293 */           if (sub.length() == 1) {
/*  294 */             rule = new CharacterLiteral(sub.charAt(0)); break;
/*      */           } 
/*  296 */           rule = new StringLiteral(sub);
/*      */           break;
/*      */         
/*      */         default:
/*  300 */           throw new IllegalArgumentException("Illegal pattern component: " + token);
/*      */       } 
/*      */       
/*  303 */       rules.add(rule);
/*      */     } 
/*      */     
/*  306 */     return rules;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected String parseToken(String pattern, int[] indexRef) {
/*  317 */     StringBuilder buf = new StringBuilder();
/*      */     
/*  319 */     int i = indexRef[0];
/*  320 */     int length = pattern.length();
/*      */     
/*  322 */     char c = pattern.charAt(i);
/*  323 */     if ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z')) {
/*      */ 
/*      */       
/*  326 */       buf.append(c);
/*      */       
/*  328 */       while (i + 1 < length) {
/*  329 */         char peek = pattern.charAt(i + 1);
/*  330 */         if (peek == c) {
/*  331 */           buf.append(c);
/*  332 */           i++;
/*      */         }
/*      */       
/*      */       }
/*      */     
/*      */     } else {
/*      */       
/*  339 */       buf.append('\'');
/*      */       
/*  341 */       boolean inLiteral = false;
/*      */       
/*  343 */       for (; i < length; i++) {
/*  344 */         c = pattern.charAt(i);
/*      */         
/*  346 */         if (c == '\'')
/*  347 */         { if (i + 1 < length && pattern.charAt(i + 1) == '\'') {
/*      */             
/*  349 */             i++;
/*  350 */             buf.append(c);
/*      */           } else {
/*  352 */             inLiteral = !inLiteral;
/*      */           }  }
/*  354 */         else { if (!inLiteral && ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z'))) {
/*      */             
/*  356 */             i--;
/*      */             break;
/*      */           } 
/*  359 */           buf.append(c); }
/*      */       
/*      */       } 
/*      */     } 
/*      */     
/*  364 */     indexRef[0] = i;
/*  365 */     return buf.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected NumberRule selectNumberRule(int field, int padding) {
/*  376 */     switch (padding) {
/*      */       case 1:
/*  378 */         return new UnpaddedNumberField(field);
/*      */       case 2:
/*  380 */         return new TwoDigitNumberField(field);
/*      */     } 
/*  382 */     return new PaddedNumberField(field, padding);
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
/*      */   public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
/*  399 */     if (obj instanceof Date)
/*  400 */       return format((Date)obj, toAppendTo); 
/*  401 */     if (obj instanceof Calendar)
/*  402 */       return format((Calendar)obj, toAppendTo); 
/*  403 */     if (obj instanceof Long) {
/*  404 */       return format(((Long)obj).longValue(), toAppendTo);
/*      */     }
/*  406 */     throw new IllegalArgumentException("Unknown class: " + ((obj == null) ? "<null>" : obj.getClass().getName()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String format(long millis) {
/*  416 */     Calendar c = newCalendar();
/*  417 */     c.setTimeInMillis(millis);
/*  418 */     return applyRulesToString(c);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private String applyRulesToString(Calendar c) {
/*  427 */     return applyRules(c, new StringBuffer(this.mMaxLengthEstimate)).toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private GregorianCalendar newCalendar() {
/*  436 */     return new GregorianCalendar(this.mTimeZone, this.mLocale);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String format(Date date) {
/*  444 */     Calendar c = newCalendar();
/*  445 */     c.setTime(date);
/*  446 */     return applyRulesToString(c);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String format(Calendar calendar) {
/*  454 */     return format(calendar, new StringBuffer(this.mMaxLengthEstimate)).toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StringBuffer format(long millis, StringBuffer buf) {
/*  462 */     return format(new Date(millis), buf);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StringBuffer format(Date date, StringBuffer buf) {
/*  470 */     Calendar c = newCalendar();
/*  471 */     c.setTime(date);
/*  472 */     return applyRules(c, buf);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StringBuffer format(Calendar calendar, StringBuffer buf) {
/*  480 */     return applyRules(calendar, buf);
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
/*      */   protected StringBuffer applyRules(Calendar calendar, StringBuffer buf) {
/*  492 */     for (Rule rule : this.mRules) {
/*  493 */       rule.appendTo(buf, calendar);
/*      */     }
/*  495 */     return buf;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getPattern() {
/*  505 */     return this.mPattern;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public TimeZone getTimeZone() {
/*  513 */     return this.mTimeZone;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Locale getLocale() {
/*  521 */     return this.mLocale;
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
/*      */   public int getMaxLengthEstimate() {
/*  534 */     return this.mMaxLengthEstimate;
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
/*      */   public boolean equals(Object obj) {
/*  547 */     if (!(obj instanceof FastDatePrinter)) {
/*  548 */       return false;
/*      */     }
/*  550 */     FastDatePrinter other = (FastDatePrinter)obj;
/*  551 */     return (this.mPattern.equals(other.mPattern) && this.mTimeZone.equals(other.mTimeZone) && this.mLocale.equals(other.mLocale));
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
/*      */   public int hashCode() {
/*  563 */     return this.mPattern.hashCode() + 13 * (this.mTimeZone.hashCode() + 13 * this.mLocale.hashCode());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String toString() {
/*  573 */     return "FastDatePrinter[" + this.mPattern + "," + this.mLocale + "," + this.mTimeZone.getID() + "]";
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
/*      */   private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
/*  587 */     in.defaultReadObject();
/*  588 */     init();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void appendDigits(StringBuffer buffer, int value) {
/*  598 */     buffer.append((char)(value / 10 + 48));
/*  599 */     buffer.append((char)(value % 10 + 48));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static interface Rule
/*      */   {
/*      */     int estimateLength();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void appendTo(StringBuffer param1StringBuffer, Calendar param1Calendar);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static interface NumberRule
/*      */     extends Rule
/*      */   {
/*      */     void appendTo(StringBuffer param1StringBuffer, int param1Int);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static class CharacterLiteral
/*      */     implements Rule
/*      */   {
/*      */     private final char mValue;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     CharacterLiteral(char value) {
/*  650 */       this.mValue = value;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int estimateLength() {
/*  658 */       return 1;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void appendTo(StringBuffer buffer, Calendar calendar) {
/*  666 */       buffer.append(this.mValue);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static class StringLiteral
/*      */     implements Rule
/*      */   {
/*      */     private final String mValue;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     StringLiteral(String value) {
/*  683 */       this.mValue = value;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int estimateLength() {
/*  691 */       return this.mValue.length();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void appendTo(StringBuffer buffer, Calendar calendar) {
/*  699 */       buffer.append(this.mValue);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static class TextField
/*      */     implements Rule
/*      */   {
/*      */     private final int mField;
/*      */ 
/*      */ 
/*      */     
/*      */     private final String[] mValues;
/*      */ 
/*      */ 
/*      */     
/*      */     TextField(int field, String[] values) {
/*  718 */       this.mField = field;
/*  719 */       this.mValues = values;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int estimateLength() {
/*  727 */       int max = 0;
/*  728 */       for (int i = this.mValues.length; --i >= 0; ) {
/*  729 */         int len = this.mValues[i].length();
/*  730 */         if (len > max) {
/*  731 */           max = len;
/*      */         }
/*      */       } 
/*  734 */       return max;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void appendTo(StringBuffer buffer, Calendar calendar) {
/*  742 */       buffer.append(this.mValues[calendar.get(this.mField)]);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static class UnpaddedNumberField
/*      */     implements NumberRule
/*      */   {
/*      */     private final int mField;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     UnpaddedNumberField(int field) {
/*  758 */       this.mField = field;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int estimateLength() {
/*  766 */       return 4;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void appendTo(StringBuffer buffer, Calendar calendar) {
/*  774 */       appendTo(buffer, calendar.get(this.mField));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public final void appendTo(StringBuffer buffer, int value) {
/*  782 */       if (value < 10) {
/*  783 */         buffer.append((char)(value + 48));
/*  784 */       } else if (value < 100) {
/*  785 */         FastDatePrinter.appendDigits(buffer, value);
/*      */       } else {
/*  787 */         buffer.append(value);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private static class UnpaddedMonthField
/*      */     implements NumberRule
/*      */   {
/*  796 */     static final UnpaddedMonthField INSTANCE = new UnpaddedMonthField();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int estimateLength() {
/*  811 */       return 2;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void appendTo(StringBuffer buffer, Calendar calendar) {
/*  819 */       appendTo(buffer, calendar.get(2) + 1);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public final void appendTo(StringBuffer buffer, int value) {
/*  827 */       if (value < 10) {
/*  828 */         buffer.append((char)(value + 48));
/*      */       } else {
/*  830 */         FastDatePrinter.appendDigits(buffer, value);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static class PaddedNumberField
/*      */     implements NumberRule
/*      */   {
/*      */     private final int mField;
/*      */ 
/*      */     
/*      */     private final int mSize;
/*      */ 
/*      */ 
/*      */     
/*      */     PaddedNumberField(int field, int size) {
/*  849 */       if (size < 3)
/*      */       {
/*  851 */         throw new IllegalArgumentException();
/*      */       }
/*  853 */       this.mField = field;
/*  854 */       this.mSize = size;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int estimateLength() {
/*  862 */       return this.mSize;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void appendTo(StringBuffer buffer, Calendar calendar) {
/*  870 */       appendTo(buffer, calendar.get(this.mField));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public final void appendTo(StringBuffer buffer, int value) {
/*  879 */       for (int digit = 0; digit < this.mSize; digit++) {
/*  880 */         buffer.append('0');
/*      */       }
/*      */       
/*  883 */       int index = buffer.length();
/*  884 */       for (; value > 0; value /= 10) {
/*  885 */         buffer.setCharAt(--index, (char)(48 + value % 10));
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static class TwoDigitNumberField
/*      */     implements NumberRule
/*      */   {
/*      */     private final int mField;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     TwoDigitNumberField(int field) {
/*  902 */       this.mField = field;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int estimateLength() {
/*  910 */       return 2;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void appendTo(StringBuffer buffer, Calendar calendar) {
/*  918 */       appendTo(buffer, calendar.get(this.mField));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public final void appendTo(StringBuffer buffer, int value) {
/*  926 */       if (value < 100) {
/*  927 */         FastDatePrinter.appendDigits(buffer, value);
/*      */       } else {
/*  929 */         buffer.append(value);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private static class TwoDigitYearField
/*      */     implements NumberRule
/*      */   {
/*  938 */     static final TwoDigitYearField INSTANCE = new TwoDigitYearField();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int estimateLength() {
/*  952 */       return 2;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void appendTo(StringBuffer buffer, Calendar calendar) {
/*  960 */       appendTo(buffer, calendar.get(1) % 100);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public final void appendTo(StringBuffer buffer, int value) {
/*  968 */       FastDatePrinter.appendDigits(buffer, value);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private static class TwoDigitMonthField
/*      */     implements NumberRule
/*      */   {
/*  976 */     static final TwoDigitMonthField INSTANCE = new TwoDigitMonthField();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int estimateLength() {
/*  990 */       return 2;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void appendTo(StringBuffer buffer, Calendar calendar) {
/*  998 */       appendTo(buffer, calendar.get(2) + 1);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public final void appendTo(StringBuffer buffer, int value) {
/* 1006 */       FastDatePrinter.appendDigits(buffer, value);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static class TwelveHourField
/*      */     implements NumberRule
/*      */   {
/*      */     private final FastDatePrinter.NumberRule mRule;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     TwelveHourField(FastDatePrinter.NumberRule rule) {
/* 1023 */       this.mRule = rule;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int estimateLength() {
/* 1031 */       return this.mRule.estimateLength();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void appendTo(StringBuffer buffer, Calendar calendar) {
/* 1039 */       int value = calendar.get(10);
/* 1040 */       if (value == 0) {
/* 1041 */         value = calendar.getLeastMaximum(10) + 1;
/*      */       }
/* 1043 */       this.mRule.appendTo(buffer, value);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void appendTo(StringBuffer buffer, int value) {
/* 1051 */       this.mRule.appendTo(buffer, value);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static class TwentyFourHourField
/*      */     implements NumberRule
/*      */   {
/*      */     private final FastDatePrinter.NumberRule mRule;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     TwentyFourHourField(FastDatePrinter.NumberRule rule) {
/* 1068 */       this.mRule = rule;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int estimateLength() {
/* 1076 */       return this.mRule.estimateLength();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void appendTo(StringBuffer buffer, Calendar calendar) {
/* 1084 */       int value = calendar.get(11);
/* 1085 */       if (value == 0) {
/* 1086 */         value = calendar.getMaximum(11) + 1;
/*      */       }
/* 1088 */       this.mRule.appendTo(buffer, value);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void appendTo(StringBuffer buffer, int value) {
/* 1096 */       this.mRule.appendTo(buffer, value);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/* 1102 */   private static final ConcurrentMap<TimeZoneDisplayKey, String> cTimeZoneDisplayCache = new ConcurrentHashMap<TimeZoneDisplayKey, String>(7);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static String getTimeZoneDisplay(TimeZone tz, boolean daylight, int style, Locale locale) {
/* 1114 */     TimeZoneDisplayKey key = new TimeZoneDisplayKey(tz, daylight, style, locale);
/* 1115 */     String value = cTimeZoneDisplayCache.get(key);
/* 1116 */     if (value == null) {
/*      */       
/* 1118 */       value = tz.getDisplayName(daylight, style, locale);
/* 1119 */       String prior = cTimeZoneDisplayCache.putIfAbsent(key, value);
/* 1120 */       if (prior != null) {
/* 1121 */         value = prior;
/*      */       }
/*      */     } 
/* 1124 */     return value;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static class TimeZoneNameRule
/*      */     implements Rule
/*      */   {
/*      */     private final Locale mLocale;
/*      */ 
/*      */     
/*      */     private final int mStyle;
/*      */ 
/*      */     
/*      */     private final String mStandard;
/*      */     
/*      */     private final String mDaylight;
/*      */ 
/*      */     
/*      */     TimeZoneNameRule(TimeZone timeZone, Locale locale, int style) {
/* 1144 */       this.mLocale = locale;
/* 1145 */       this.mStyle = style;
/*      */       
/* 1147 */       this.mStandard = FastDatePrinter.getTimeZoneDisplay(timeZone, false, style, locale);
/* 1148 */       this.mDaylight = FastDatePrinter.getTimeZoneDisplay(timeZone, true, style, locale);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int estimateLength() {
/* 1159 */       return Math.max(this.mStandard.length(), this.mDaylight.length());
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void appendTo(StringBuffer buffer, Calendar calendar) {
/* 1167 */       TimeZone zone = calendar.getTimeZone();
/* 1168 */       if (calendar.get(16) != 0) {
/* 1169 */         buffer.append(FastDatePrinter.getTimeZoneDisplay(zone, true, this.mStyle, this.mLocale));
/*      */       } else {
/* 1171 */         buffer.append(FastDatePrinter.getTimeZoneDisplay(zone, false, this.mStyle, this.mLocale));
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static class TimeZoneNumberRule
/*      */     implements Rule
/*      */   {
/* 1181 */     static final TimeZoneNumberRule INSTANCE_COLON = new TimeZoneNumberRule(true, false);
/* 1182 */     static final TimeZoneNumberRule INSTANCE_NO_COLON = new TimeZoneNumberRule(false, false);
/* 1183 */     static final TimeZoneNumberRule INSTANCE_ISO_8601 = new TimeZoneNumberRule(true, true);
/*      */ 
/*      */ 
/*      */     
/*      */     final boolean mColon;
/*      */ 
/*      */     
/*      */     final boolean mISO8601;
/*      */ 
/*      */ 
/*      */     
/*      */     TimeZoneNumberRule(boolean colon, boolean iso8601) {
/* 1195 */       this.mColon = colon;
/* 1196 */       this.mISO8601 = iso8601;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int estimateLength() {
/* 1204 */       return 5;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void appendTo(StringBuffer buffer, Calendar calendar) {
/* 1212 */       if (this.mISO8601 && calendar.getTimeZone().getID().equals("UTC")) {
/* 1213 */         buffer.append("Z");
/*      */         
/*      */         return;
/*      */       } 
/* 1217 */       int offset = calendar.get(15) + calendar.get(16);
/*      */       
/* 1219 */       if (offset < 0) {
/* 1220 */         buffer.append('-');
/* 1221 */         offset = -offset;
/*      */       } else {
/* 1223 */         buffer.append('+');
/*      */       } 
/*      */       
/* 1226 */       int hours = offset / 3600000;
/* 1227 */       FastDatePrinter.appendDigits(buffer, hours);
/*      */       
/* 1229 */       if (this.mColon) {
/* 1230 */         buffer.append(':');
/*      */       }
/*      */       
/* 1233 */       int minutes = offset / 60000 - 60 * hours;
/* 1234 */       FastDatePrinter.appendDigits(buffer, minutes);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static class Iso8601_Rule
/*      */     implements Rule
/*      */   {
/* 1245 */     static final Iso8601_Rule ISO8601_HOURS = new Iso8601_Rule(3);
/*      */     
/* 1247 */     static final Iso8601_Rule ISO8601_HOURS_MINUTES = new Iso8601_Rule(5);
/*      */     
/* 1249 */     static final Iso8601_Rule ISO8601_HOURS_COLON_MINUTES = new Iso8601_Rule(6);
/*      */ 
/*      */ 
/*      */     
/*      */     final int length;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     static Iso8601_Rule getRule(int tokenLen) {
/* 1259 */       switch (tokenLen) {
/*      */         case 1:
/* 1261 */           return ISO8601_HOURS;
/*      */         case 2:
/* 1263 */           return ISO8601_HOURS_MINUTES;
/*      */         case 3:
/* 1265 */           return ISO8601_HOURS_COLON_MINUTES;
/*      */       } 
/* 1267 */       throw new IllegalArgumentException("invalid number of X");
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     Iso8601_Rule(int length) {
/* 1279 */       this.length = length;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int estimateLength() {
/* 1287 */       return this.length;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void appendTo(StringBuffer buffer, Calendar calendar) {
/* 1295 */       int zoneOffset = calendar.get(15);
/* 1296 */       if (zoneOffset == 0) {
/* 1297 */         buffer.append("Z");
/*      */         
/*      */         return;
/*      */       } 
/* 1301 */       int offset = zoneOffset + calendar.get(16);
/*      */       
/* 1303 */       if (offset < 0) {
/* 1304 */         buffer.append('-');
/* 1305 */         offset = -offset;
/*      */       } else {
/* 1307 */         buffer.append('+');
/*      */       } 
/*      */       
/* 1310 */       int hours = offset / 3600000;
/* 1311 */       FastDatePrinter.appendDigits(buffer, hours);
/*      */       
/* 1313 */       if (this.length < 5) {
/*      */         return;
/*      */       }
/*      */       
/* 1317 */       if (this.length == 6) {
/* 1318 */         buffer.append(':');
/*      */       }
/*      */       
/* 1321 */       int minutes = offset / 60000 - 60 * hours;
/* 1322 */       FastDatePrinter.appendDigits(buffer, minutes);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static class TimeZoneDisplayKey
/*      */   {
/*      */     private final TimeZone mTimeZone;
/*      */ 
/*      */ 
/*      */     
/*      */     private final int mStyle;
/*      */ 
/*      */ 
/*      */     
/*      */     private final Locale mLocale;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     TimeZoneDisplayKey(TimeZone timeZone, boolean daylight, int style, Locale locale) {
/* 1345 */       this.mTimeZone = timeZone;
/* 1346 */       if (daylight) {
/* 1347 */         this.mStyle = style | Integer.MIN_VALUE;
/*      */       } else {
/* 1349 */         this.mStyle = style;
/*      */       } 
/* 1351 */       this.mLocale = locale;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int hashCode() {
/* 1359 */       return (this.mStyle * 31 + this.mLocale.hashCode()) * 31 + this.mTimeZone.hashCode();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean equals(Object obj) {
/* 1367 */       if (this == obj) {
/* 1368 */         return true;
/*      */       }
/* 1370 */       if (obj instanceof TimeZoneDisplayKey) {
/* 1371 */         TimeZoneDisplayKey other = (TimeZoneDisplayKey)obj;
/* 1372 */         return (this.mTimeZone.equals(other.mTimeZone) && this.mStyle == other.mStyle && this.mLocale.equals(other.mLocale));
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/* 1377 */       return false;
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\commons\lang3\time\FastDatePrinter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */