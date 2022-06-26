/*     */ package org.apache.commons.lang3.text;
/*     */ 
/*     */ import java.text.Format;
/*     */ import java.text.MessageFormat;
/*     */ import java.text.ParsePosition;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import org.apache.commons.lang3.ObjectUtils;
/*     */ import org.apache.commons.lang3.Validate;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ExtendedMessageFormat
/*     */   extends MessageFormat
/*     */ {
/*     */   private static final long serialVersionUID = -2362048321261811743L;
/*     */   private static final int HASH_SEED = 31;
/*     */   private static final String DUMMY_PATTERN = "";
/*     */   private static final char START_FMT = ',';
/*     */   private static final char END_FE = '}';
/*     */   private static final char START_FE = '{';
/*     */   private static final char QUOTE = '\'';
/*     */   private String toPattern;
/*     */   private final Map<String, ? extends FormatFactory> registry;
/*     */   
/*     */   public ExtendedMessageFormat(String pattern) {
/*  89 */     this(pattern, Locale.getDefault());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ExtendedMessageFormat(String pattern, Locale locale) {
/* 100 */     this(pattern, locale, (Map<String, ? extends FormatFactory>)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ExtendedMessageFormat(String pattern, Map<String, ? extends FormatFactory> registry) {
/* 111 */     this(pattern, Locale.getDefault(), registry);
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
/*     */   public ExtendedMessageFormat(String pattern, Locale locale, Map<String, ? extends FormatFactory> registry) {
/* 123 */     super("");
/* 124 */     setLocale(locale);
/* 125 */     this.registry = registry;
/* 126 */     applyPattern(pattern);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toPattern() {
/* 134 */     return this.toPattern;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void applyPattern(String pattern) {
/* 144 */     if (this.registry == null) {
/* 145 */       super.applyPattern(pattern);
/* 146 */       this.toPattern = super.toPattern();
/*     */       return;
/*     */     } 
/* 149 */     ArrayList<Format> foundFormats = new ArrayList<Format>();
/* 150 */     ArrayList<String> foundDescriptions = new ArrayList<String>();
/* 151 */     StringBuilder stripCustom = new StringBuilder(pattern.length());
/*     */     
/* 153 */     ParsePosition pos = new ParsePosition(0);
/* 154 */     char[] c = pattern.toCharArray();
/* 155 */     int fmtCount = 0;
/* 156 */     while (pos.getIndex() < pattern.length()) {
/* 157 */       int start, index; Format format; String formatDescription; switch (c[pos.getIndex()]) {
/*     */         case '\'':
/* 159 */           appendQuotedString(pattern, pos, stripCustom);
/*     */           continue;
/*     */         case '{':
/* 162 */           fmtCount++;
/* 163 */           seekNonWs(pattern, pos);
/* 164 */           start = pos.getIndex();
/* 165 */           index = readArgumentIndex(pattern, next(pos));
/* 166 */           stripCustom.append('{').append(index);
/* 167 */           seekNonWs(pattern, pos);
/* 168 */           format = null;
/* 169 */           formatDescription = null;
/* 170 */           if (c[pos.getIndex()] == ',') {
/* 171 */             formatDescription = parseFormatDescription(pattern, next(pos));
/*     */             
/* 173 */             format = getFormat(formatDescription);
/* 174 */             if (format == null) {
/* 175 */               stripCustom.append(',').append(formatDescription);
/*     */             }
/*     */           } 
/* 178 */           foundFormats.add(format);
/* 179 */           foundDescriptions.add((format == null) ? null : formatDescription);
/* 180 */           Validate.isTrue((foundFormats.size() == fmtCount));
/* 181 */           Validate.isTrue((foundDescriptions.size() == fmtCount));
/* 182 */           if (c[pos.getIndex()] != '}') {
/* 183 */             throw new IllegalArgumentException("Unreadable format element at position " + start);
/*     */           }
/*     */           break;
/*     */       } 
/*     */       
/* 188 */       stripCustom.append(c[pos.getIndex()]);
/* 189 */       next(pos);
/*     */     } 
/*     */     
/* 192 */     super.applyPattern(stripCustom.toString());
/* 193 */     this.toPattern = insertFormats(super.toPattern(), foundDescriptions);
/* 194 */     if (containsElements(foundFormats)) {
/* 195 */       Format[] origFormats = getFormats();
/*     */ 
/*     */       
/* 198 */       int i = 0;
/* 199 */       for (Iterator<Format> it = foundFormats.iterator(); it.hasNext(); i++) {
/* 200 */         Format f = it.next();
/* 201 */         if (f != null) {
/* 202 */           origFormats[i] = f;
/*     */         }
/*     */       } 
/* 205 */       super.setFormats(origFormats);
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
/*     */   public void setFormat(int formatElementIndex, Format newFormat) {
/* 218 */     throw new UnsupportedOperationException();
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
/*     */   public void setFormatByArgumentIndex(int argumentIndex, Format newFormat) {
/* 230 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFormats(Format[] newFormats) {
/* 241 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFormatsByArgumentIndex(Format[] newFormats) {
/* 252 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 263 */     if (obj == this) {
/* 264 */       return true;
/*     */     }
/* 266 */     if (obj == null) {
/* 267 */       return false;
/*     */     }
/* 269 */     if (!super.equals(obj)) {
/* 270 */       return false;
/*     */     }
/* 272 */     if (ObjectUtils.notEqual(getClass(), obj.getClass())) {
/* 273 */       return false;
/*     */     }
/* 275 */     ExtendedMessageFormat rhs = (ExtendedMessageFormat)obj;
/* 276 */     if (ObjectUtils.notEqual(this.toPattern, rhs.toPattern)) {
/* 277 */       return false;
/*     */     }
/* 279 */     if (ObjectUtils.notEqual(this.registry, rhs.registry)) {
/* 280 */       return false;
/*     */     }
/* 282 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 291 */     int result = super.hashCode();
/* 292 */     result = 31 * result + ObjectUtils.hashCode(this.registry);
/* 293 */     result = 31 * result + ObjectUtils.hashCode(this.toPattern);
/* 294 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Format getFormat(String desc) {
/* 304 */     if (this.registry != null) {
/* 305 */       String name = desc;
/* 306 */       String args = null;
/* 307 */       int i = desc.indexOf(',');
/* 308 */       if (i > 0) {
/* 309 */         name = desc.substring(0, i).trim();
/* 310 */         args = desc.substring(i + 1).trim();
/*     */       } 
/* 312 */       FormatFactory factory = this.registry.get(name);
/* 313 */       if (factory != null) {
/* 314 */         return factory.getFormat(name, args, getLocale());
/*     */       }
/*     */     } 
/* 317 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int readArgumentIndex(String pattern, ParsePosition pos) {
/* 328 */     int start = pos.getIndex();
/* 329 */     seekNonWs(pattern, pos);
/* 330 */     StringBuilder result = new StringBuilder();
/* 331 */     boolean error = false;
/* 332 */     for (; !error && pos.getIndex() < pattern.length(); next(pos)) {
/* 333 */       char c = pattern.charAt(pos.getIndex());
/* 334 */       if (Character.isWhitespace(c)) {
/* 335 */         seekNonWs(pattern, pos);
/* 336 */         c = pattern.charAt(pos.getIndex());
/* 337 */         if (c != ',' && c != '}') {
/* 338 */           error = true;
/*     */           continue;
/*     */         } 
/*     */       } 
/* 342 */       if ((c == ',' || c == '}') && result.length() > 0) {
/*     */         try {
/* 344 */           return Integer.parseInt(result.toString());
/* 345 */         } catch (NumberFormatException e) {}
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 350 */       error = !Character.isDigit(c);
/* 351 */       result.append(c); continue;
/*     */     } 
/* 353 */     if (error) {
/* 354 */       throw new IllegalArgumentException("Invalid format argument index at position " + start + ": " + pattern.substring(start, pos.getIndex()));
/*     */     }
/*     */ 
/*     */     
/* 358 */     throw new IllegalArgumentException("Unterminated format element at position " + start);
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
/*     */   private String parseFormatDescription(String pattern, ParsePosition pos) {
/* 370 */     int start = pos.getIndex();
/* 371 */     seekNonWs(pattern, pos);
/* 372 */     int text = pos.getIndex();
/* 373 */     int depth = 1;
/* 374 */     for (; pos.getIndex() < pattern.length(); next(pos)) {
/* 375 */       switch (pattern.charAt(pos.getIndex())) {
/*     */         case '{':
/* 377 */           depth++;
/*     */           break;
/*     */         case '}':
/* 380 */           depth--;
/* 381 */           if (depth == 0) {
/* 382 */             return pattern.substring(text, pos.getIndex());
/*     */           }
/*     */           break;
/*     */         case '\'':
/* 386 */           getQuotedString(pattern, pos);
/*     */           break;
/*     */       } 
/*     */ 
/*     */     
/*     */     } 
/* 392 */     throw new IllegalArgumentException("Unterminated format element at position " + start);
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
/*     */   private String insertFormats(String pattern, ArrayList<String> customPatterns) {
/* 404 */     if (!containsElements(customPatterns)) {
/* 405 */       return pattern;
/*     */     }
/* 407 */     StringBuilder sb = new StringBuilder(pattern.length() * 2);
/* 408 */     ParsePosition pos = new ParsePosition(0);
/* 409 */     int fe = -1;
/* 410 */     int depth = 0;
/* 411 */     while (pos.getIndex() < pattern.length()) {
/* 412 */       char c = pattern.charAt(pos.getIndex());
/* 413 */       switch (c) {
/*     */         case '\'':
/* 415 */           appendQuotedString(pattern, pos, sb);
/*     */           continue;
/*     */         case '{':
/* 418 */           depth++;
/* 419 */           sb.append('{').append(readArgumentIndex(pattern, next(pos)));
/*     */           
/* 421 */           if (depth == 1) {
/* 422 */             fe++;
/* 423 */             String customPattern = customPatterns.get(fe);
/* 424 */             if (customPattern != null) {
/* 425 */               sb.append(',').append(customPattern);
/*     */             }
/*     */           } 
/*     */           continue;
/*     */         case '}':
/* 430 */           depth--;
/*     */           break;
/*     */       } 
/* 433 */       sb.append(c);
/* 434 */       next(pos);
/*     */     } 
/*     */     
/* 437 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void seekNonWs(String pattern, ParsePosition pos) {
/* 447 */     int len = 0;
/* 448 */     char[] buffer = pattern.toCharArray();
/*     */     do {
/* 450 */       len = StrMatcher.splitMatcher().isMatch(buffer, pos.getIndex());
/* 451 */       pos.setIndex(pos.getIndex() + len);
/* 452 */     } while (len > 0 && pos.getIndex() < pattern.length());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ParsePosition next(ParsePosition pos) {
/* 462 */     pos.setIndex(pos.getIndex() + 1);
/* 463 */     return pos;
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
/*     */   private StringBuilder appendQuotedString(String pattern, ParsePosition pos, StringBuilder appendTo) {
/* 478 */     assert pattern.toCharArray()[pos.getIndex()] == '\'' : "Quoted string must start with quote character";
/*     */ 
/*     */     
/* 481 */     if (appendTo != null) {
/* 482 */       appendTo.append('\'');
/*     */     }
/* 484 */     next(pos);
/*     */     
/* 486 */     int start = pos.getIndex();
/* 487 */     char[] c = pattern.toCharArray();
/* 488 */     int lastHold = start;
/* 489 */     for (int i = pos.getIndex(); i < pattern.length(); i++) {
/* 490 */       switch (c[pos.getIndex()]) {
/*     */         case '\'':
/* 492 */           next(pos);
/* 493 */           return (appendTo == null) ? null : appendTo.append(c, lastHold, pos.getIndex() - lastHold);
/*     */       } 
/*     */       
/* 496 */       next(pos);
/*     */     } 
/*     */     
/* 499 */     throw new IllegalArgumentException("Unterminated quoted string at position " + start);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void getQuotedString(String pattern, ParsePosition pos) {
/* 510 */     appendQuotedString(pattern, pos, (StringBuilder)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean containsElements(Collection<?> coll) {
/* 519 */     if (coll == null || coll.isEmpty()) {
/* 520 */       return false;
/*     */     }
/* 522 */     for (Object name : coll) {
/* 523 */       if (name != null) {
/* 524 */         return true;
/*     */       }
/*     */     } 
/* 527 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\commons\lang3\text\ExtendedMessageFormat.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */