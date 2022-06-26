/*     */ package org.apache.logging.log4j.core.layout;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Calendar;
/*     */ import java.util.GregorianCalendar;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.SortedMap;
/*     */ import java.util.TreeMap;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import org.apache.logging.log4j.Level;
/*     */ import org.apache.logging.log4j.LoggingException;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.appender.TlsSyslogFrame;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginElement;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginFactory;
/*     */ import org.apache.logging.log4j.core.net.Facility;
/*     */ import org.apache.logging.log4j.core.net.Priority;
/*     */ import org.apache.logging.log4j.core.pattern.LogEventPatternConverter;
/*     */ import org.apache.logging.log4j.core.pattern.PatternConverter;
/*     */ import org.apache.logging.log4j.core.pattern.PatternFormatter;
/*     */ import org.apache.logging.log4j.core.pattern.PatternParser;
/*     */ import org.apache.logging.log4j.core.pattern.ThrowablePatternConverter;
/*     */ import org.apache.logging.log4j.core.util.Charsets;
/*     */ import org.apache.logging.log4j.core.util.NetUtils;
/*     */ import org.apache.logging.log4j.core.util.Patterns;
/*     */ import org.apache.logging.log4j.message.Message;
/*     */ import org.apache.logging.log4j.message.StructuredDataId;
/*     */ import org.apache.logging.log4j.message.StructuredDataMessage;
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
/*     */ @Plugin(name = "Rfc5424Layout", category = "Core", elementType = "layout", printObject = true)
/*     */ public final class Rfc5424Layout
/*     */   extends AbstractStringLayout
/*     */ {
/*     */   private static final String LF = "\n";
/*     */   public static final int DEFAULT_ENTERPRISE_NUMBER = 18060;
/*     */   public static final String DEFAULT_ID = "Audit";
/*  78 */   public static final Pattern NEWLINE_PATTERN = Pattern.compile("\\r?\\n");
/*     */ 
/*     */ 
/*     */   
/*  82 */   public static final Pattern PARAM_VALUE_ESCAPE_PATTERN = Pattern.compile("[\\\"\\]\\\\]");
/*     */   
/*     */   public static final String DEFAULT_MDCID = "mdc";
/*     */   
/*     */   private static final int TWO_DIGITS = 10;
/*     */   
/*     */   private static final int THREE_DIGITS = 100;
/*     */   private static final int MILLIS_PER_MINUTE = 60000;
/*     */   private static final int MINUTES_PER_HOUR = 60;
/*     */   private static final String COMPONENT_KEY = "RFC5424-Converter";
/*     */   private final Facility facility;
/*     */   private final String defaultId;
/*     */   private final int enterpriseNumber;
/*     */   private final boolean includeMdc;
/*     */   private final String mdcId;
/*     */   private final StructuredDataId mdcSdId;
/*     */   private final String localHostName;
/*     */   private final String appName;
/*     */   private final String messageId;
/*     */   private final String configName;
/*     */   private final String mdcPrefix;
/*     */   private final String eventPrefix;
/*     */   private final List<String> mdcExcludes;
/*     */   private final List<String> mdcIncludes;
/*     */   private final List<String> mdcRequired;
/*     */   private final ListChecker checker;
/* 108 */   private final ListChecker noopChecker = new NoopChecker();
/*     */   
/*     */   private final boolean includeNewLine;
/*     */   private final String escapeNewLine;
/*     */   private final boolean useTlsMessageFormat;
/* 113 */   private long lastTimestamp = -1L;
/*     */ 
/*     */   
/*     */   private String timestamppStr;
/*     */ 
/*     */   
/*     */   private final List<PatternFormatter> exceptionFormatters;
/*     */   
/*     */   private final Map<String, FieldFormatter> fieldFormatters;
/*     */ 
/*     */   
/*     */   private Rfc5424Layout(Configuration config, Facility facility, String id, int ein, boolean includeMDC, boolean includeNL, String escapeNL, String mdcId, String mdcPrefix, String eventPrefix, String appName, String messageId, String excludes, String includes, String required, Charset charset, String exceptionPattern, boolean useTLSMessageFormat, LoggerFields[] loggerFields) {
/* 125 */     super(charset);
/* 126 */     PatternParser exceptionParser = createPatternParser(config, (Class)ThrowablePatternConverter.class);
/* 127 */     this.exceptionFormatters = (exceptionPattern == null) ? null : exceptionParser.parse(exceptionPattern, false, false);
/* 128 */     this.facility = facility;
/* 129 */     this.defaultId = (id == null) ? "Audit" : id;
/* 130 */     this.enterpriseNumber = ein;
/* 131 */     this.includeMdc = includeMDC;
/* 132 */     this.includeNewLine = includeNL;
/* 133 */     this.escapeNewLine = (escapeNL == null) ? null : Matcher.quoteReplacement(escapeNL);
/* 134 */     this.mdcId = mdcId;
/* 135 */     this.mdcSdId = new StructuredDataId(mdcId, this.enterpriseNumber, null, null);
/* 136 */     this.mdcPrefix = mdcPrefix;
/* 137 */     this.eventPrefix = eventPrefix;
/* 138 */     this.appName = appName;
/* 139 */     this.messageId = messageId;
/* 140 */     this.useTlsMessageFormat = useTLSMessageFormat;
/* 141 */     this.localHostName = NetUtils.getLocalHostname();
/* 142 */     ListChecker c = null;
/* 143 */     if (excludes != null) {
/* 144 */       String[] array = excludes.split(Patterns.COMMA_SEPARATOR);
/* 145 */       if (array.length > 0) {
/* 146 */         c = new ExcludeChecker();
/* 147 */         this.mdcExcludes = new ArrayList<String>(array.length);
/* 148 */         for (String str : array) {
/* 149 */           this.mdcExcludes.add(str.trim());
/*     */         }
/*     */       } else {
/* 152 */         this.mdcExcludes = null;
/*     */       } 
/*     */     } else {
/* 155 */       this.mdcExcludes = null;
/*     */     } 
/* 157 */     if (includes != null) {
/* 158 */       String[] array = includes.split(Patterns.COMMA_SEPARATOR);
/* 159 */       if (array.length > 0) {
/* 160 */         c = new IncludeChecker();
/* 161 */         this.mdcIncludes = new ArrayList<String>(array.length);
/* 162 */         for (String str : array) {
/* 163 */           this.mdcIncludes.add(str.trim());
/*     */         }
/*     */       } else {
/* 166 */         this.mdcIncludes = null;
/*     */       } 
/*     */     } else {
/* 169 */       this.mdcIncludes = null;
/*     */     } 
/* 171 */     if (required != null) {
/* 172 */       String[] array = required.split(Patterns.COMMA_SEPARATOR);
/* 173 */       if (array.length > 0) {
/* 174 */         this.mdcRequired = new ArrayList<String>(array.length);
/* 175 */         for (String str : array) {
/* 176 */           this.mdcRequired.add(str.trim());
/*     */         }
/*     */       } else {
/* 179 */         this.mdcRequired = null;
/*     */       } 
/*     */     } else {
/*     */       
/* 183 */       this.mdcRequired = null;
/*     */     } 
/* 185 */     this.checker = (c != null) ? c : this.noopChecker;
/* 186 */     String name = (config == null) ? null : config.getName();
/* 187 */     this.configName = (name != null && name.length() > 0) ? name : null;
/* 188 */     this.fieldFormatters = createFieldFormatters(loggerFields, config);
/*     */   }
/*     */ 
/*     */   
/*     */   private Map<String, FieldFormatter> createFieldFormatters(LoggerFields[] loggerFields, Configuration config) {
/* 193 */     Map<String, FieldFormatter> sdIdMap = new HashMap<String, FieldFormatter>();
/*     */     
/* 195 */     if (loggerFields != null) {
/* 196 */       for (LoggerFields lField : loggerFields) {
/* 197 */         StructuredDataId key = (lField.getSdId() == null) ? this.mdcSdId : lField.getSdId();
/* 198 */         Map<String, List<PatternFormatter>> sdParams = new HashMap<String, List<PatternFormatter>>();
/* 199 */         Map<String, String> fields = lField.getMap();
/* 200 */         if (!fields.isEmpty()) {
/* 201 */           PatternParser fieldParser = createPatternParser(config, null);
/*     */           
/* 203 */           for (Map.Entry<String, String> entry : fields.entrySet()) {
/* 204 */             List<PatternFormatter> formatters = fieldParser.parse(entry.getValue(), false, false);
/* 205 */             sdParams.put(entry.getKey(), formatters);
/*     */           } 
/* 207 */           FieldFormatter fieldFormatter = new FieldFormatter(sdParams, lField.getDiscardIfAllFieldsAreEmpty());
/*     */           
/* 209 */           sdIdMap.put(key.toString(), fieldFormatter);
/*     */         } 
/*     */       } 
/*     */     }
/* 213 */     return (sdIdMap.size() > 0) ? sdIdMap : null;
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
/*     */   private static PatternParser createPatternParser(Configuration config, Class<? extends PatternConverter> filterClass) {
/* 225 */     if (config == null) {
/* 226 */       return new PatternParser(config, "Converter", LogEventPatternConverter.class, filterClass);
/*     */     }
/* 228 */     PatternParser parser = (PatternParser)config.getComponent("RFC5424-Converter");
/* 229 */     if (parser == null) {
/* 230 */       parser = new PatternParser(config, "Converter", ThrowablePatternConverter.class);
/* 231 */       config.addComponent("RFC5424-Converter", parser);
/* 232 */       parser = (PatternParser)config.getComponent("RFC5424-Converter");
/*     */     } 
/* 234 */     return parser;
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
/*     */   public Map<String, String> getContentFormat() {
/* 246 */     Map<String, String> result = new HashMap<String, String>();
/* 247 */     result.put("structured", "true");
/* 248 */     result.put("formatType", "RFC5424");
/* 249 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toSerializable(LogEvent event) {
/* 260 */     StringBuilder buf = new StringBuilder();
/* 261 */     appendPriority(buf, event.getLevel());
/* 262 */     appendTimestamp(buf, event.getTimeMillis());
/* 263 */     appendSpace(buf);
/* 264 */     appendHostName(buf);
/* 265 */     appendSpace(buf);
/* 266 */     appendAppName(buf);
/* 267 */     appendSpace(buf);
/* 268 */     appendProcessId(buf);
/* 269 */     appendSpace(buf);
/* 270 */     appendMessageId(buf, event.getMessage());
/* 271 */     appendSpace(buf);
/* 272 */     appendStructuredElements(buf, event);
/* 273 */     appendMessage(buf, event);
/* 274 */     if (this.useTlsMessageFormat) {
/* 275 */       return (new TlsSyslogFrame(buf.toString())).toString();
/*     */     }
/* 277 */     return buf.toString();
/*     */   }
/*     */   
/*     */   private void appendPriority(StringBuilder buffer, Level logLevel) {
/* 281 */     buffer.append('<');
/* 282 */     buffer.append(Priority.getPriority(this.facility, logLevel));
/* 283 */     buffer.append(">1 ");
/*     */   }
/*     */   
/*     */   private void appendTimestamp(StringBuilder buffer, long milliseconds) {
/* 287 */     buffer.append(computeTimeStampString(milliseconds));
/*     */   }
/*     */   
/*     */   private void appendSpace(StringBuilder buffer) {
/* 291 */     buffer.append(' ');
/*     */   }
/*     */   
/*     */   private void appendHostName(StringBuilder buffer) {
/* 295 */     buffer.append(this.localHostName);
/*     */   }
/*     */   
/*     */   private void appendAppName(StringBuilder buffer) {
/* 299 */     if (this.appName != null) {
/* 300 */       buffer.append(this.appName);
/* 301 */     } else if (this.configName != null) {
/* 302 */       buffer.append(this.configName);
/*     */     } else {
/* 304 */       buffer.append('-');
/*     */     } 
/*     */   }
/*     */   
/*     */   private void appendProcessId(StringBuilder buffer) {
/* 309 */     buffer.append(getProcId());
/*     */   }
/*     */   
/*     */   private void appendMessageId(StringBuilder buffer, Message message) {
/* 313 */     boolean isStructured = message instanceof StructuredDataMessage;
/* 314 */     String type = isStructured ? ((StructuredDataMessage)message).getType() : null;
/* 315 */     if (type != null) {
/* 316 */       buffer.append(type);
/* 317 */     } else if (this.messageId != null) {
/* 318 */       buffer.append(this.messageId);
/*     */     } else {
/* 320 */       buffer.append('-');
/*     */     } 
/*     */   }
/*     */   
/*     */   private void appendMessage(StringBuilder buffer, LogEvent event) {
/* 325 */     Message message = event.getMessage();
/*     */     
/* 327 */     String text = (message instanceof StructuredDataMessage) ? message.getFormat() : message.getFormattedMessage();
/*     */     
/* 329 */     if (text != null && text.length() > 0) {
/* 330 */       buffer.append(' ').append(escapeNewlines(text, this.escapeNewLine));
/*     */     }
/*     */     
/* 333 */     if (this.exceptionFormatters != null && event.getThrown() != null) {
/* 334 */       StringBuilder exception = new StringBuilder("\n");
/* 335 */       for (PatternFormatter formatter : this.exceptionFormatters) {
/* 336 */         formatter.format(event, exception);
/*     */       }
/* 338 */       buffer.append(escapeNewlines(exception.toString(), this.escapeNewLine));
/*     */     } 
/* 340 */     if (this.includeNewLine) {
/* 341 */       buffer.append("\n");
/*     */     }
/*     */   }
/*     */   
/*     */   private void appendStructuredElements(StringBuilder buffer, LogEvent event) {
/* 346 */     Message message = event.getMessage();
/* 347 */     boolean isStructured = message instanceof StructuredDataMessage;
/*     */     
/* 349 */     if (!isStructured && this.fieldFormatters != null && this.fieldFormatters.isEmpty() && !this.includeMdc) {
/* 350 */       buffer.append('-');
/*     */       
/*     */       return;
/*     */     } 
/* 354 */     Map<String, StructuredDataElement> sdElements = new HashMap<String, StructuredDataElement>();
/* 355 */     Map<String, String> contextMap = event.getContextMap();
/*     */     
/* 357 */     if (this.mdcRequired != null) {
/* 358 */       checkRequired(contextMap);
/*     */     }
/*     */     
/* 361 */     if (this.fieldFormatters != null) {
/* 362 */       for (Map.Entry<String, FieldFormatter> sdElement : this.fieldFormatters.entrySet()) {
/* 363 */         String sdId = sdElement.getKey();
/* 364 */         StructuredDataElement elem = ((FieldFormatter)sdElement.getValue()).format(event);
/* 365 */         sdElements.put(sdId, elem);
/*     */       } 
/*     */     }
/*     */     
/* 369 */     if (this.includeMdc && contextMap.size() > 0) {
/* 370 */       if (sdElements.containsKey(this.mdcSdId.toString())) {
/* 371 */         StructuredDataElement union = sdElements.get(this.mdcSdId.toString());
/* 372 */         union.union(contextMap);
/* 373 */         sdElements.put(this.mdcSdId.toString(), union);
/*     */       } else {
/* 375 */         StructuredDataElement formattedContextMap = new StructuredDataElement(contextMap, false);
/* 376 */         sdElements.put(this.mdcSdId.toString(), formattedContextMap);
/*     */       } 
/*     */     }
/*     */     
/* 380 */     if (isStructured) {
/* 381 */       StructuredDataMessage data = (StructuredDataMessage)message;
/* 382 */       Map<String, String> map = data.getData();
/* 383 */       StructuredDataId id = data.getId();
/* 384 */       String sdId = getId(id);
/*     */       
/* 386 */       if (sdElements.containsKey(sdId)) {
/* 387 */         StructuredDataElement union = sdElements.get(id.toString());
/* 388 */         union.union(map);
/* 389 */         sdElements.put(sdId, union);
/*     */       } else {
/* 391 */         StructuredDataElement formattedData = new StructuredDataElement(map, false);
/* 392 */         sdElements.put(sdId, formattedData);
/*     */       } 
/*     */     } 
/*     */     
/* 396 */     if (sdElements.isEmpty()) {
/* 397 */       buffer.append('-');
/*     */       
/*     */       return;
/*     */     } 
/* 401 */     for (Map.Entry<String, StructuredDataElement> entry : sdElements.entrySet()) {
/* 402 */       formatStructuredElement(entry.getKey(), this.mdcPrefix, entry.getValue(), buffer, this.checker);
/*     */     }
/*     */   }
/*     */   
/*     */   private String escapeNewlines(String text, String escapeNewLine) {
/* 407 */     if (null == escapeNewLine) {
/* 408 */       return text;
/*     */     }
/* 410 */     return NEWLINE_PATTERN.matcher(text).replaceAll(escapeNewLine);
/*     */   }
/*     */   
/*     */   protected String getProcId() {
/* 414 */     return "-";
/*     */   }
/*     */   
/*     */   protected List<String> getMdcExcludes() {
/* 418 */     return this.mdcExcludes;
/*     */   }
/*     */   
/*     */   protected List<String> getMdcIncludes() {
/* 422 */     return this.mdcIncludes;
/*     */   }
/*     */   
/*     */   private String computeTimeStampString(long now) {
/*     */     long last;
/* 427 */     synchronized (this) {
/* 428 */       last = this.lastTimestamp;
/* 429 */       if (now == this.lastTimestamp) {
/* 430 */         return this.timestamppStr;
/*     */       }
/*     */     } 
/*     */     
/* 434 */     StringBuilder buffer = new StringBuilder();
/* 435 */     Calendar cal = new GregorianCalendar();
/* 436 */     cal.setTimeInMillis(now);
/* 437 */     buffer.append(Integer.toString(cal.get(1)));
/* 438 */     buffer.append('-');
/* 439 */     pad(cal.get(2) + 1, 10, buffer);
/* 440 */     buffer.append('-');
/* 441 */     pad(cal.get(5), 10, buffer);
/* 442 */     buffer.append('T');
/* 443 */     pad(cal.get(11), 10, buffer);
/* 444 */     buffer.append(':');
/* 445 */     pad(cal.get(12), 10, buffer);
/* 446 */     buffer.append(':');
/* 447 */     pad(cal.get(13), 10, buffer);
/*     */     
/* 449 */     int millis = cal.get(14);
/* 450 */     if (millis != 0) {
/* 451 */       buffer.append('.');
/* 452 */       pad(millis, 100, buffer);
/*     */     } 
/*     */     
/* 455 */     int tzmin = (cal.get(15) + cal.get(16)) / 60000;
/* 456 */     if (tzmin == 0) {
/* 457 */       buffer.append('Z');
/*     */     } else {
/* 459 */       if (tzmin < 0) {
/* 460 */         tzmin = -tzmin;
/* 461 */         buffer.append('-');
/*     */       } else {
/* 463 */         buffer.append('+');
/*     */       } 
/* 465 */       int tzhour = tzmin / 60;
/* 466 */       tzmin -= tzhour * 60;
/* 467 */       pad(tzhour, 10, buffer);
/* 468 */       buffer.append(':');
/* 469 */       pad(tzmin, 10, buffer);
/*     */     } 
/* 471 */     synchronized (this) {
/* 472 */       if (last == this.lastTimestamp) {
/* 473 */         this.lastTimestamp = now;
/* 474 */         this.timestamppStr = buffer.toString();
/*     */       } 
/*     */     } 
/* 477 */     return buffer.toString();
/*     */   }
/*     */   
/*     */   private void pad(int val, int max, StringBuilder buf) {
/* 481 */     while (max > 1) {
/* 482 */       if (val < max) {
/* 483 */         buf.append('0');
/*     */       }
/* 485 */       max /= 10;
/*     */     } 
/* 487 */     buf.append(Integer.toString(val));
/*     */   }
/*     */ 
/*     */   
/*     */   private void formatStructuredElement(String id, String prefix, StructuredDataElement data, StringBuilder sb, ListChecker checker) {
/* 492 */     if ((id == null && this.defaultId == null) || data.discard()) {
/*     */       return;
/*     */     }
/*     */     
/* 496 */     sb.append('[');
/* 497 */     sb.append(id);
/* 498 */     if (!this.mdcSdId.toString().equals(id)) {
/* 499 */       appendMap(prefix, data.getFields(), sb, this.noopChecker);
/*     */     } else {
/* 501 */       appendMap(prefix, data.getFields(), sb, checker);
/*     */     } 
/* 503 */     sb.append(']');
/*     */   }
/*     */   
/*     */   private String getId(StructuredDataId id) {
/* 507 */     StringBuilder sb = new StringBuilder();
/* 508 */     if (id == null || id.getName() == null) {
/* 509 */       sb.append(this.defaultId);
/*     */     } else {
/* 511 */       sb.append(id.getName());
/*     */     } 
/* 513 */     int ein = (id != null) ? id.getEnterpriseNumber() : this.enterpriseNumber;
/* 514 */     if (ein < 0) {
/* 515 */       ein = this.enterpriseNumber;
/*     */     }
/* 517 */     if (ein >= 0) {
/* 518 */       sb.append('@').append(ein);
/*     */     }
/* 520 */     return sb.toString();
/*     */   }
/*     */   
/*     */   private void checkRequired(Map<String, String> map) {
/* 524 */     for (String key : this.mdcRequired) {
/* 525 */       String value = map.get(key);
/* 526 */       if (value == null) {
/* 527 */         throw new LoggingException("Required key " + key + " is missing from the " + this.mdcId);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void appendMap(String prefix, Map<String, String> map, StringBuilder sb, ListChecker checker) {
/* 534 */     SortedMap<String, String> sorted = new TreeMap<String, String>(map);
/* 535 */     for (Map.Entry<String, String> entry : sorted.entrySet()) {
/* 536 */       if (checker.check(entry.getKey()) && entry.getValue() != null) {
/* 537 */         sb.append(' ');
/* 538 */         if (prefix != null) {
/* 539 */           sb.append(prefix);
/*     */         }
/* 541 */         sb.append(escapeNewlines(escapeSDParams(entry.getKey()), this.escapeNewLine)).append("=\"").append(escapeNewlines(escapeSDParams(entry.getValue()), this.escapeNewLine)).append('"');
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private String escapeSDParams(String value) {
/* 548 */     return PARAM_VALUE_ESCAPE_PATTERN.matcher(value).replaceAll("\\\\$0");
/*     */   }
/*     */ 
/*     */   
/*     */   private static interface ListChecker
/*     */   {
/*     */     boolean check(String param1String);
/*     */   }
/*     */ 
/*     */   
/*     */   private class IncludeChecker
/*     */     implements ListChecker
/*     */   {
/*     */     private IncludeChecker() {}
/*     */     
/*     */     public boolean check(String key) {
/* 564 */       return Rfc5424Layout.this.mdcIncludes.contains(key);
/*     */     }
/*     */   }
/*     */   
/*     */   private class ExcludeChecker
/*     */     implements ListChecker
/*     */   {
/*     */     private ExcludeChecker() {}
/*     */     
/*     */     public boolean check(String key) {
/* 574 */       return !Rfc5424Layout.this.mdcExcludes.contains(key);
/*     */     }
/*     */   }
/*     */   
/*     */   private class NoopChecker
/*     */     implements ListChecker
/*     */   {
/*     */     private NoopChecker() {}
/*     */     
/*     */     public boolean check(String key) {
/* 584 */       return true;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 590 */     StringBuilder sb = new StringBuilder();
/* 591 */     sb.append("facility=").append(this.facility.name());
/* 592 */     sb.append(" appName=").append(this.appName);
/* 593 */     sb.append(" defaultId=").append(this.defaultId);
/* 594 */     sb.append(" enterpriseNumber=").append(this.enterpriseNumber);
/* 595 */     sb.append(" newLine=").append(this.includeNewLine);
/* 596 */     sb.append(" includeMDC=").append(this.includeMdc);
/* 597 */     sb.append(" messageId=").append(this.messageId);
/* 598 */     return sb.toString();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */   public static Rfc5424Layout createLayout(@PluginAttribute(value = "facility", defaultString = "LOCAL0") Facility facility, @PluginAttribute("id") String id, @PluginAttribute(value = "enterpriseNumber", defaultInt = 18060) int enterpriseNumber, @PluginAttribute(value = "includeMDC", defaultBoolean = true) boolean includeMDC, @PluginAttribute(value = "mdcId", defaultString = "mdc") String mdcId, @PluginAttribute("mdcPrefix") String mdcPrefix, @PluginAttribute("eventPrefix") String eventPrefix, @PluginAttribute(value = "newLine", defaultBoolean = false) boolean newLine, @PluginAttribute("newLineEscape") String escapeNL, @PluginAttribute("appName") String appName, @PluginAttribute("messageId") String msgId, @PluginAttribute("mdcExcludes") String excludes, @PluginAttribute("mdcIncludes") String includes, @PluginAttribute("mdcRequired") String required, @PluginAttribute("exceptionPattern") String exceptionPattern, @PluginAttribute(value = "useTlsMessageFormat", defaultBoolean = false) boolean useTlsMessageFormat, @PluginElement("LoggerFields") LoggerFields[] loggerFields, @PluginConfiguration Configuration config) {
/* 645 */     Charset charset = Charsets.UTF_8;
/* 646 */     if (includes != null && excludes != null) {
/* 647 */       LOGGER.error("mdcIncludes and mdcExcludes are mutually exclusive. Includes wil be ignored");
/* 648 */       includes = null;
/*     */     } 
/*     */     
/* 651 */     return new Rfc5424Layout(config, facility, id, enterpriseNumber, includeMDC, newLine, escapeNL, mdcId, mdcPrefix, eventPrefix, appName, msgId, excludes, includes, required, charset, exceptionPattern, useTlsMessageFormat, loggerFields);
/*     */   }
/*     */ 
/*     */   
/*     */   private class FieldFormatter
/*     */   {
/*     */     private final Map<String, List<PatternFormatter>> delegateMap;
/*     */     
/*     */     private final boolean discardIfEmpty;
/*     */     
/*     */     public FieldFormatter(Map<String, List<PatternFormatter>> fieldMap, boolean discardIfEmpty) {
/* 662 */       this.discardIfEmpty = discardIfEmpty;
/* 663 */       this.delegateMap = fieldMap;
/*     */     }
/*     */     
/*     */     public Rfc5424Layout.StructuredDataElement format(LogEvent event) {
/* 667 */       Map<String, String> map = new HashMap<String, String>();
/*     */       
/* 669 */       for (Map.Entry<String, List<PatternFormatter>> entry : this.delegateMap.entrySet()) {
/* 670 */         StringBuilder buffer = new StringBuilder();
/* 671 */         for (PatternFormatter formatter : entry.getValue()) {
/* 672 */           formatter.format(event, buffer);
/*     */         }
/* 674 */         map.put(entry.getKey(), buffer.toString());
/*     */       } 
/* 676 */       return new Rfc5424Layout.StructuredDataElement(map, this.discardIfEmpty);
/*     */     }
/*     */   }
/*     */   
/*     */   private class StructuredDataElement
/*     */   {
/*     */     private final Map<String, String> fields;
/*     */     private final boolean discardIfEmpty;
/*     */     
/*     */     public StructuredDataElement(Map<String, String> fields, boolean discardIfEmpty) {
/* 686 */       this.discardIfEmpty = discardIfEmpty;
/* 687 */       this.fields = fields;
/*     */     }
/*     */     
/*     */     boolean discard() {
/* 691 */       if (!this.discardIfEmpty) {
/* 692 */         return false;
/*     */       }
/* 694 */       boolean foundNotEmptyValue = false;
/* 695 */       for (Map.Entry<String, String> entry : this.fields.entrySet()) {
/* 696 */         if (Strings.isNotEmpty(entry.getValue())) {
/* 697 */           foundNotEmptyValue = true;
/*     */           break;
/*     */         } 
/*     */       } 
/* 701 */       return !foundNotEmptyValue;
/*     */     }
/*     */     
/*     */     void union(Map<String, String> fields) {
/* 705 */       this.fields.putAll(fields);
/*     */     }
/*     */     
/*     */     Map<String, String> getFields() {
/* 709 */       return this.fields;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\layout\Rfc5424Layout.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */