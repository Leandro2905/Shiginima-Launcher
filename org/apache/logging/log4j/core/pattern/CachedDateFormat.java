/*     */ package org.apache.logging.log4j.core.pattern;
/*     */ 
/*     */ import java.text.DateFormat;
/*     */ import java.text.FieldPosition;
/*     */ import java.text.NumberFormat;
/*     */ import java.text.ParsePosition;
/*     */ import java.util.Date;
/*     */ import java.util.TimeZone;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class CachedDateFormat
/*     */   extends DateFormat
/*     */ {
/*     */   public static final int NO_MILLISECONDS = -2;
/*     */   public static final int UNRECOGNIZED_MILLISECONDS = -1;
/*     */   private static final long serialVersionUID = -1253877934598423628L;
/*     */   private static final String DIGITS = "0123456789";
/*     */   private static final int MAGIC1 = 654;
/*     */   private static final String MAGICSTRING1 = "654";
/*     */   private static final int MAGIC2 = 987;
/*     */   private static final String MAGICSTRING2 = "987";
/*     */   private static final String ZERO_STRING = "000";
/*     */   private static final int BUF_SIZE = 50;
/*     */   private static final int DEFAULT_VALIDITY = 1000;
/*     */   private static final int THREE_DIGITS = 100;
/*     */   private static final int TWO_DIGITS = 10;
/*     */   private static final long SLOTS = 1000L;
/*     */   private final DateFormat formatter;
/*     */   private int millisecondStart;
/*     */   private long slotBegin;
/* 114 */   private final StringBuffer cache = new StringBuffer(50);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final int expiration;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private long previousTime;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 131 */   private final Date tmpDate = new Date(0L);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CachedDateFormat(DateFormat dateFormat, int expiration) {
/* 143 */     if (dateFormat == null) {
/* 144 */       throw new IllegalArgumentException("dateFormat cannot be null");
/*     */     }
/*     */     
/* 147 */     if (expiration < 0) {
/* 148 */       throw new IllegalArgumentException("expiration must be non-negative");
/*     */     }
/*     */     
/* 151 */     this.formatter = dateFormat;
/* 152 */     this.expiration = expiration;
/* 153 */     this.millisecondStart = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 158 */     this.previousTime = Long.MIN_VALUE;
/* 159 */     this.slotBegin = Long.MIN_VALUE;
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
/*     */   public static int findMillisecondStart(long time, String formatted, DateFormat formatter) {
/* 173 */     long slotBegin = time / 1000L * 1000L;
/*     */     
/* 175 */     if (slotBegin > time) {
/* 176 */       slotBegin -= 1000L;
/*     */     }
/*     */     
/* 179 */     int millis = (int)(time - slotBegin);
/*     */     
/* 181 */     int magic = 654;
/* 182 */     String magicString = "654";
/*     */     
/* 184 */     if (millis == 654) {
/* 185 */       magic = 987;
/* 186 */       magicString = "987";
/*     */     } 
/*     */     
/* 189 */     String plusMagic = formatter.format(new Date(slotBegin + magic));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 195 */     if (plusMagic.length() != formatted.length()) {
/* 196 */       return -1;
/*     */     }
/*     */     
/* 199 */     for (int i = 0; i < formatted.length(); i++) {
/* 200 */       if (formatted.charAt(i) != plusMagic.charAt(i)) {
/*     */ 
/*     */         
/* 203 */         StringBuffer formattedMillis = new StringBuffer("ABC");
/* 204 */         millisecondFormat(millis, formattedMillis, 0);
/*     */         
/* 206 */         String plusZero = formatter.format(new Date(slotBegin));
/*     */ 
/*     */ 
/*     */         
/* 210 */         if (plusZero.length() == formatted.length() && magicString.regionMatches(0, plusMagic, i, magicString.length()) && formattedMillis.toString().regionMatches(0, formatted, i, magicString.length()) && "000".regionMatches(0, plusZero, i, "000".length()))
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 218 */           return i;
/*     */         }
/* 220 */         return -1;
/*     */       } 
/*     */     } 
/*     */     
/* 224 */     return -2;
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
/*     */   public StringBuffer format(Date date, StringBuffer sbuf, FieldPosition fieldPosition) {
/* 237 */     format(date.getTime(), sbuf);
/*     */     
/* 239 */     return sbuf;
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
/*     */   public StringBuffer format(long now, StringBuffer buf) {
/* 254 */     if (now == this.previousTime) {
/* 255 */       buf.append(this.cache);
/*     */       
/* 257 */       return buf;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 264 */     if (this.millisecondStart != -1 && now < this.slotBegin + this.expiration && now >= this.slotBegin && now < this.slotBegin + 1000L) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 272 */       if (this.millisecondStart >= 0) {
/* 273 */         millisecondFormat((int)(now - this.slotBegin), this.cache, this.millisecondStart);
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 279 */       this.previousTime = now;
/* 280 */       buf.append(this.cache);
/*     */       
/* 282 */       return buf;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 288 */     this.cache.setLength(0);
/* 289 */     this.tmpDate.setTime(now);
/* 290 */     this.cache.append(this.formatter.format(this.tmpDate));
/* 291 */     buf.append(this.cache);
/* 292 */     this.previousTime = now;
/* 293 */     this.slotBegin = this.previousTime / 1000L * 1000L;
/*     */     
/* 295 */     if (this.slotBegin > this.previousTime) {
/* 296 */       this.slotBegin -= 1000L;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 303 */     if (this.millisecondStart >= 0) {
/* 304 */       this.millisecondStart = findMillisecondStart(now, this.cache.toString(), this.formatter);
/*     */     }
/*     */ 
/*     */     
/* 308 */     return buf;
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
/*     */   private static void millisecondFormat(int millis, StringBuffer buf, int offset) {
/* 321 */     buf.setCharAt(offset, "0123456789".charAt(millis / 100));
/* 322 */     buf.setCharAt(offset + 1, "0123456789".charAt(millis / 10 % 10));
/* 323 */     buf.setCharAt(offset + 2, "0123456789".charAt(millis % 10));
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
/*     */   public void setTimeZone(TimeZone timeZone) {
/* 336 */     this.formatter.setTimeZone(timeZone);
/* 337 */     this.previousTime = Long.MIN_VALUE;
/* 338 */     this.slotBegin = Long.MIN_VALUE;
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
/*     */   public Date parse(String s, ParsePosition pos) {
/* 351 */     return this.formatter.parse(s, pos);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NumberFormat getNumberFormat() {
/* 361 */     return this.formatter.getNumberFormat();
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
/*     */   public static int getMaximumCacheValidity(String pattern) {
/* 378 */     int firstS = pattern.indexOf('S');
/*     */     
/* 380 */     if (firstS >= 0 && firstS != pattern.lastIndexOf("SSS")) {
/* 381 */       return 1;
/*     */     }
/*     */     
/* 384 */     return 1000;
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\pattern\CachedDateFormat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */