/*     */ package org.apache.logging.log4j.core.pattern;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.config.plugins.util.PluginManager;
/*     */ import org.apache.logging.log4j.core.config.plugins.util.PluginType;
/*     */ import org.apache.logging.log4j.status.StatusLogger;
/*     */ import org.apache.logging.log4j.util.Strings;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class PatternParser
/*     */ {
/*     */   static final String NO_CONSOLE_NO_ANSI = "noConsoleNoAnsi";
/*     */   private static final char ESCAPE_CHAR = '%';
/*     */   
/*     */   private enum ParserState
/*     */   {
/*  56 */     LITERAL_STATE,
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  61 */     CONVERTER_STATE,
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  66 */     DOT_STATE,
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  71 */     MIN_STATE,
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  76 */     MAX_STATE;
/*     */   }
/*     */   
/*  79 */   private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*     */ 
/*     */   
/*     */   private static final int BUF_SIZE = 32;
/*     */ 
/*     */   
/*     */   private static final int DECIMAL = 10;
/*     */ 
/*     */   
/*     */   private final Configuration config;
/*     */ 
/*     */   
/*     */   private final Map<String, Class<PatternConverter>> converterRules;
/*     */ 
/*     */ 
/*     */   
/*     */   public PatternParser(String converterKey) {
/*  96 */     this(null, converterKey, null, null);
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
/*     */   public PatternParser(Configuration config, String converterKey, Class<?> expected) {
/* 110 */     this(config, converterKey, expected, null);
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
/*     */   public PatternParser(Configuration config, String converterKey, Class<?> expectedClass, Class<?> filterClass) {
/* 127 */     this.config = config;
/* 128 */     PluginManager manager = new PluginManager(converterKey);
/* 129 */     manager.collectPlugins();
/* 130 */     Map<String, PluginType<?>> plugins = manager.getPlugins();
/* 131 */     Map<String, Class<PatternConverter>> converters = new HashMap<String, Class<PatternConverter>>();
/*     */     
/* 133 */     for (PluginType<?> type : plugins.values()) {
/*     */       
/*     */       try {
/* 136 */         Class<PatternConverter> clazz = type.getPluginClass();
/* 137 */         if (filterClass != null && !filterClass.isAssignableFrom(clazz)) {
/*     */           continue;
/*     */         }
/* 140 */         ConverterKeys keys = clazz.<ConverterKeys>getAnnotation(ConverterKeys.class);
/* 141 */         if (keys != null) {
/* 142 */           for (String key : keys.value()) {
/* 143 */             converters.put(key, clazz);
/*     */           }
/*     */         }
/* 146 */       } catch (Exception ex) {
/* 147 */         LOGGER.error("Error processing plugin " + type.getElementName(), ex);
/*     */       } 
/*     */     } 
/* 150 */     this.converterRules = converters;
/*     */   }
/*     */   
/*     */   public List<PatternFormatter> parse(String pattern) {
/* 154 */     return parse(pattern, false, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<PatternFormatter> parse(String pattern, boolean alwaysWriteExceptions, boolean noConsoleNoAnsi) {
/* 159 */     List<PatternFormatter> list = new ArrayList<PatternFormatter>();
/* 160 */     List<PatternConverter> converters = new ArrayList<PatternConverter>();
/* 161 */     List<FormattingInfo> fields = new ArrayList<FormattingInfo>();
/*     */     
/* 163 */     parse(pattern, converters, fields, noConsoleNoAnsi);
/*     */     
/* 165 */     Iterator<FormattingInfo> fieldIter = fields.iterator();
/* 166 */     boolean handlesThrowable = false;
/*     */     
/* 168 */     for (PatternConverter converter : converters) {
/*     */       LogEventPatternConverter pc; FormattingInfo field;
/* 170 */       if (converter instanceof LogEventPatternConverter) {
/* 171 */         pc = (LogEventPatternConverter)converter;
/* 172 */         handlesThrowable |= pc.handlesThrowable();
/*     */       } else {
/* 174 */         pc = new LiteralPatternConverter(this.config, "");
/*     */       } 
/*     */ 
/*     */       
/* 178 */       if (fieldIter.hasNext()) {
/* 179 */         field = fieldIter.next();
/*     */       } else {
/* 181 */         field = FormattingInfo.getDefault();
/*     */       } 
/* 183 */       list.add(new PatternFormatter(pc, field));
/*     */     } 
/* 185 */     if (alwaysWriteExceptions && !handlesThrowable) {
/* 186 */       LogEventPatternConverter pc = ExtendedThrowablePatternConverter.newInstance(null);
/* 187 */       list.add(new PatternFormatter(pc, FormattingInfo.getDefault()));
/*     */     } 
/* 189 */     return list;
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
/*     */   private static int extractConverter(char lastChar, String pattern, int i, StringBuilder convBuf, StringBuilder currentLiteral) {
/* 215 */     convBuf.setLength(0);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 222 */     if (!Character.isUnicodeIdentifierStart(lastChar)) {
/* 223 */       return i;
/*     */     }
/*     */     
/* 226 */     convBuf.append(lastChar);
/*     */     
/* 228 */     while (i < pattern.length() && Character.isUnicodeIdentifierPart(pattern.charAt(i))) {
/* 229 */       convBuf.append(pattern.charAt(i));
/* 230 */       currentLiteral.append(pattern.charAt(i));
/* 231 */       i++;
/*     */     } 
/*     */     
/* 234 */     return i;
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
/*     */   private static int extractOptions(String pattern, int i, List<String> options) {
/* 249 */     while (i < pattern.length() && pattern.charAt(i) == '{') {
/* 250 */       int end, begin = i++;
/*     */       
/* 252 */       int depth = 0;
/*     */       do {
/* 254 */         end = pattern.indexOf('}', i);
/* 255 */         if (end == -1)
/* 256 */           continue;  int next = pattern.indexOf("{", i);
/* 257 */         if (next != -1 && next < end) {
/* 258 */           i = end + 1;
/* 259 */           depth++;
/* 260 */         } else if (depth > 0) {
/* 261 */           depth--;
/*     */         }
/*     */       
/* 264 */       } while (depth > 0);
/*     */       
/* 266 */       if (end == -1) {
/*     */         break;
/*     */       }
/*     */       
/* 270 */       String r = pattern.substring(begin + 1, end);
/* 271 */       options.add(r);
/* 272 */       i = end + 1;
/*     */     } 
/*     */     
/* 275 */     return i;
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
/*     */   public void parse(String pattern, List<PatternConverter> patternConverters, List<FormattingInfo> formattingInfos, boolean noConsoleNoAnsi) {
/* 292 */     if (pattern == null) {
/* 293 */       throw new NullPointerException("pattern");
/*     */     }
/*     */     
/* 296 */     StringBuilder currentLiteral = new StringBuilder(32);
/*     */     
/* 298 */     int patternLength = pattern.length();
/* 299 */     ParserState state = ParserState.LITERAL_STATE;
/*     */     
/* 301 */     int i = 0;
/* 302 */     FormattingInfo formattingInfo = FormattingInfo.getDefault();
/*     */     
/* 304 */     while (i < patternLength) {
/* 305 */       char c = pattern.charAt(i++);
/*     */       
/* 307 */       switch (state) {
/*     */ 
/*     */         
/*     */         case LITERAL_STATE:
/* 311 */           if (i == patternLength) {
/* 312 */             currentLiteral.append(c);
/*     */             
/*     */             continue;
/*     */           } 
/*     */           
/* 317 */           if (c == '%') {
/*     */             
/* 319 */             switch (pattern.charAt(i)) {
/*     */               case '%':
/* 321 */                 currentLiteral.append(c);
/* 322 */                 i++;
/*     */                 continue;
/*     */             } 
/*     */ 
/*     */ 
/*     */             
/* 328 */             if (currentLiteral.length() != 0) {
/* 329 */               patternConverters.add(new LiteralPatternConverter(this.config, currentLiteral.toString()));
/* 330 */               formattingInfos.add(FormattingInfo.getDefault());
/*     */             } 
/*     */             
/* 333 */             currentLiteral.setLength(0);
/* 334 */             currentLiteral.append(c);
/* 335 */             state = ParserState.CONVERTER_STATE;
/* 336 */             formattingInfo = FormattingInfo.getDefault();
/*     */             continue;
/*     */           } 
/* 339 */           currentLiteral.append(c);
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         case CONVERTER_STATE:
/* 345 */           currentLiteral.append(c);
/*     */           
/* 347 */           switch (c) {
/*     */             case '-':
/* 349 */               formattingInfo = new FormattingInfo(true, formattingInfo.getMinLength(), formattingInfo.getMaxLength());
/*     */               continue;
/*     */ 
/*     */             
/*     */             case '.':
/* 354 */               state = ParserState.DOT_STATE;
/*     */               continue;
/*     */           } 
/*     */ 
/*     */           
/* 359 */           if (c >= '0' && c <= '9') {
/* 360 */             formattingInfo = new FormattingInfo(formattingInfo.isLeftAligned(), c - 48, formattingInfo.getMaxLength());
/*     */             
/* 362 */             state = ParserState.MIN_STATE; continue;
/*     */           } 
/* 364 */           i = finalizeConverter(c, pattern, i, currentLiteral, formattingInfo, this.converterRules, patternConverters, formattingInfos, noConsoleNoAnsi);
/*     */ 
/*     */ 
/*     */           
/* 368 */           state = ParserState.LITERAL_STATE;
/* 369 */           formattingInfo = FormattingInfo.getDefault();
/* 370 */           currentLiteral.setLength(0);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         case MIN_STATE:
/* 377 */           currentLiteral.append(c);
/*     */           
/* 379 */           if (c >= '0' && c <= '9') {
/*     */             
/* 381 */             formattingInfo = new FormattingInfo(formattingInfo.isLeftAligned(), formattingInfo.getMinLength() * 10 + c - 48, formattingInfo.getMaxLength()); continue;
/*     */           } 
/* 383 */           if (c == '.') {
/* 384 */             state = ParserState.DOT_STATE; continue;
/*     */           } 
/* 386 */           i = finalizeConverter(c, pattern, i, currentLiteral, formattingInfo, this.converterRules, patternConverters, formattingInfos, noConsoleNoAnsi);
/*     */           
/* 388 */           state = ParserState.LITERAL_STATE;
/* 389 */           formattingInfo = FormattingInfo.getDefault();
/* 390 */           currentLiteral.setLength(0);
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         case DOT_STATE:
/* 396 */           currentLiteral.append(c);
/*     */           
/* 398 */           if (c >= '0' && c <= '9') {
/* 399 */             formattingInfo = new FormattingInfo(formattingInfo.isLeftAligned(), formattingInfo.getMinLength(), c - 48);
/*     */             
/* 401 */             state = ParserState.MAX_STATE; continue;
/*     */           } 
/* 403 */           LOGGER.error("Error occurred in position " + i + ".\n Was expecting digit, instead got char \"" + c + "\".");
/*     */ 
/*     */           
/* 406 */           state = ParserState.LITERAL_STATE;
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         case MAX_STATE:
/* 412 */           currentLiteral.append(c);
/*     */           
/* 414 */           if (c >= '0' && c <= '9') {
/*     */             
/* 416 */             formattingInfo = new FormattingInfo(formattingInfo.isLeftAligned(), formattingInfo.getMinLength(), formattingInfo.getMaxLength() * 10 + c - 48);
/*     */             continue;
/*     */           } 
/* 419 */           i = finalizeConverter(c, pattern, i, currentLiteral, formattingInfo, this.converterRules, patternConverters, formattingInfos, noConsoleNoAnsi);
/*     */           
/* 421 */           state = ParserState.LITERAL_STATE;
/* 422 */           formattingInfo = FormattingInfo.getDefault();
/* 423 */           currentLiteral.setLength(0);
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     } 
/* 431 */     if (currentLiteral.length() != 0) {
/* 432 */       patternConverters.add(new LiteralPatternConverter(this.config, currentLiteral.toString()));
/* 433 */       formattingInfos.add(FormattingInfo.getDefault());
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
/*     */   
/*     */   private PatternConverter createConverter(String converterId, StringBuilder currentLiteral, Map<String, Class<PatternConverter>> rules, List<String> options, boolean noConsoleNoAnsi) {
/* 454 */     String converterName = converterId;
/* 455 */     Class<PatternConverter> converterClass = null;
/*     */     
/* 457 */     if (rules == null) {
/* 458 */       LOGGER.error("Null rules for [" + converterId + ']');
/* 459 */       return null;
/*     */     } 
/* 461 */     for (int i = converterId.length(); i > 0 && converterClass == null; i--) {
/* 462 */       converterName = converterName.substring(0, i);
/* 463 */       converterClass = rules.get(converterName);
/*     */     } 
/*     */     
/* 466 */     if (converterClass == null) {
/* 467 */       LOGGER.error("Unrecognized format specifier [" + converterId + ']');
/* 468 */       return null;
/*     */     } 
/*     */     
/* 471 */     if (AnsiConverter.class.isAssignableFrom(converterClass)) {
/* 472 */       options.add("noConsoleNoAnsi=" + noConsoleNoAnsi);
/*     */     }
/*     */ 
/*     */     
/* 476 */     Method[] methods = converterClass.getDeclaredMethods();
/* 477 */     Method newInstanceMethod = null;
/* 478 */     for (Method method : methods) {
/* 479 */       if (Modifier.isStatic(method.getModifiers()) && method.getDeclaringClass().equals(converterClass) && method.getName().equals("newInstance"))
/*     */       {
/* 481 */         if (newInstanceMethod == null) {
/* 482 */           newInstanceMethod = method;
/* 483 */         } else if (method.getReturnType().equals(newInstanceMethod.getReturnType())) {
/* 484 */           LOGGER.error("Class " + converterClass + " cannot contain multiple static newInstance methods");
/* 485 */           return null;
/*     */         } 
/*     */       }
/*     */     } 
/* 489 */     if (newInstanceMethod == null) {
/* 490 */       LOGGER.error("Class " + converterClass + " does not contain a static newInstance method");
/* 491 */       return null;
/*     */     } 
/*     */     
/* 494 */     Class<?>[] parmTypes = newInstanceMethod.getParameterTypes();
/* 495 */     Object[] parms = (parmTypes.length > 0) ? new Object[parmTypes.length] : null;
/*     */     
/* 497 */     if (parms != null) {
/* 498 */       int j = 0;
/* 499 */       boolean errors = false;
/* 500 */       for (Class<?> clazz : parmTypes) {
/* 501 */         if (clazz.isArray() && clazz.getName().equals("[Ljava.lang.String;")) {
/* 502 */           String[] optionsArray = options.<String>toArray(new String[options.size()]);
/* 503 */           parms[j] = optionsArray;
/* 504 */         } else if (clazz.isAssignableFrom(Configuration.class)) {
/* 505 */           parms[j] = this.config;
/*     */         } else {
/* 507 */           LOGGER.error("Unknown parameter type " + clazz.getName() + " for static newInstance method of " + converterClass.getName());
/*     */           
/* 509 */           errors = true;
/*     */         } 
/* 511 */         j++;
/*     */       } 
/* 513 */       if (errors) {
/* 514 */         return null;
/*     */       }
/*     */     } 
/*     */     
/*     */     try {
/* 519 */       Object newObj = newInstanceMethod.invoke(null, parms);
/*     */       
/* 521 */       if (newObj instanceof PatternConverter) {
/* 522 */         currentLiteral.delete(0, currentLiteral.length() - converterId.length() - converterName.length());
/*     */         
/* 524 */         return (PatternConverter)newObj;
/*     */       } 
/* 526 */       LOGGER.warn("Class " + converterClass.getName() + " does not extend PatternConverter.");
/* 527 */     } catch (Exception ex) {
/* 528 */       LOGGER.error("Error creating converter for " + converterId, ex);
/*     */     } 
/*     */     
/* 531 */     return null;
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
/*     */   
/*     */   private int finalizeConverter(char c, String pattern, int i, StringBuilder currentLiteral, FormattingInfo formattingInfo, Map<String, Class<PatternConverter>> rules, List<PatternConverter> patternConverters, List<FormattingInfo> formattingInfos, boolean noConsoleNoAnsi) {
/* 561 */     StringBuilder convBuf = new StringBuilder();
/* 562 */     i = extractConverter(c, pattern, i, convBuf, currentLiteral);
/*     */     
/* 564 */     String converterId = convBuf.toString();
/*     */     
/* 566 */     List<String> options = new ArrayList<String>();
/* 567 */     i = extractOptions(pattern, i, options);
/*     */     
/* 569 */     PatternConverter pc = createConverter(converterId, currentLiteral, rules, options, noConsoleNoAnsi);
/*     */     
/* 571 */     if (pc == null) {
/*     */       StringBuilder msg;
/*     */       
/* 574 */       if (Strings.isEmpty(converterId)) {
/* 575 */         msg = new StringBuilder("Empty conversion specifier starting at position ");
/*     */       } else {
/* 577 */         msg = new StringBuilder("Unrecognized conversion specifier [");
/* 578 */         msg.append(converterId);
/* 579 */         msg.append("] starting at position ");
/*     */       } 
/*     */       
/* 582 */       msg.append(Integer.toString(i));
/* 583 */       msg.append(" in conversion pattern.");
/*     */       
/* 585 */       LOGGER.error(msg.toString());
/*     */       
/* 587 */       patternConverters.add(new LiteralPatternConverter(this.config, currentLiteral.toString()));
/* 588 */       formattingInfos.add(FormattingInfo.getDefault());
/*     */     } else {
/* 590 */       patternConverters.add(pc);
/* 591 */       formattingInfos.add(formattingInfo);
/*     */       
/* 593 */       if (currentLiteral.length() > 0) {
/* 594 */         patternConverters.add(new LiteralPatternConverter(this.config, currentLiteral.toString()));
/* 595 */         formattingInfos.add(FormattingInfo.getDefault());
/*     */       } 
/*     */     } 
/*     */     
/* 599 */     currentLiteral.setLength(0);
/*     */     
/* 601 */     return i;
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\pattern\PatternParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */