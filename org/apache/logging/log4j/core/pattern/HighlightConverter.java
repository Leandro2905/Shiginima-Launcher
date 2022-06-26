/*     */ package org.apache.logging.log4j.core.pattern;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import org.apache.logging.log4j.Level;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.layout.PatternLayout;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Plugin(name = "highlight", category = "Converter")
/*     */ @ConverterKeys({"highlight"})
/*     */ public final class HighlightConverter
/*     */   extends LogEventPatternConverter
/*     */   implements AnsiConverter
/*     */ {
/*  75 */   private static final Map<Level, String> DEFAULT_STYLES = new HashMap<Level, String>();
/*     */   
/*  77 */   private static final Map<Level, String> LOGBACK_STYLES = new HashMap<Level, String>();
/*     */   
/*     */   private static final String STYLE_KEY = "STYLE";
/*     */   
/*     */   private static final String STYLE_KEY_DEFAULT = "DEFAULT";
/*     */   
/*     */   private static final String STYLE_KEY_LOGBACK = "LOGBACK";
/*     */   
/*  85 */   private static final Map<String, Map<Level, String>> STYLES = new HashMap<String, Map<Level, String>>();
/*     */   private final Map<Level, String> levelStyles;
/*     */   
/*     */   static {
/*  89 */     DEFAULT_STYLES.put(Level.FATAL, AnsiEscape.createSequence(new String[] { "BRIGHT", "RED" }));
/*  90 */     DEFAULT_STYLES.put(Level.ERROR, AnsiEscape.createSequence(new String[] { "BRIGHT", "RED" }));
/*  91 */     DEFAULT_STYLES.put(Level.WARN, AnsiEscape.createSequence(new String[] { "YELLOW" }));
/*  92 */     DEFAULT_STYLES.put(Level.INFO, AnsiEscape.createSequence(new String[] { "GREEN" }));
/*  93 */     DEFAULT_STYLES.put(Level.DEBUG, AnsiEscape.createSequence(new String[] { "CYAN" }));
/*  94 */     DEFAULT_STYLES.put(Level.TRACE, AnsiEscape.createSequence(new String[] { "BLACK" }));
/*     */     
/*  96 */     LOGBACK_STYLES.put(Level.FATAL, AnsiEscape.createSequence(new String[] { "BLINK", "BRIGHT", "RED" }));
/*  97 */     LOGBACK_STYLES.put(Level.ERROR, AnsiEscape.createSequence(new String[] { "BRIGHT", "RED" }));
/*  98 */     LOGBACK_STYLES.put(Level.WARN, AnsiEscape.createSequence(new String[] { "RED" }));
/*  99 */     LOGBACK_STYLES.put(Level.INFO, AnsiEscape.createSequence(new String[] { "BLUE" }));
/* 100 */     LOGBACK_STYLES.put(Level.DEBUG, AnsiEscape.createSequence((String[])null));
/* 101 */     LOGBACK_STYLES.put(Level.TRACE, AnsiEscape.createSequence((String[])null));
/*     */     
/* 103 */     STYLES.put("DEFAULT", DEFAULT_STYLES);
/* 104 */     STYLES.put("LOGBACK", LOGBACK_STYLES);
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
/*     */   private final List<PatternFormatter> patternFormatters;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Map<Level, String> createLevelStyleMap(String[] options) {
/* 132 */     if (options.length < 2) {
/* 133 */       return DEFAULT_STYLES;
/*     */     }
/*     */     
/* 136 */     String string = options[1].replaceAll("noConsoleNoAnsi=(true|false)", "");
/*     */     
/* 138 */     Map<String, String> styles = AnsiEscape.createMap(string, new String[] { "STYLE" });
/* 139 */     Map<Level, String> levelStyles = new HashMap<Level, String>(DEFAULT_STYLES);
/* 140 */     for (Map.Entry<String, String> entry : styles.entrySet()) {
/* 141 */       String key = ((String)entry.getKey()).toUpperCase(Locale.ENGLISH);
/* 142 */       String value = entry.getValue();
/* 143 */       if ("STYLE".equalsIgnoreCase(key)) {
/* 144 */         Map<Level, String> enumMap = STYLES.get(value.toUpperCase(Locale.ENGLISH));
/* 145 */         if (enumMap == null) {
/* 146 */           LOGGER.error("Unknown level style: " + value + ". Use one of " + Arrays.toString(STYLES.keySet().toArray()));
/*     */           continue;
/*     */         } 
/* 149 */         levelStyles.putAll(enumMap);
/*     */         continue;
/*     */       } 
/* 152 */       Level level = Level.toLevel(key);
/* 153 */       if (level == null) {
/* 154 */         LOGGER.error("Unknown level name: " + key + ". Use one of " + Arrays.toString(DEFAULT_STYLES.keySet().toArray()));
/*     */         continue;
/*     */       } 
/* 157 */       levelStyles.put(level, value);
/*     */     } 
/*     */ 
/*     */     
/* 161 */     return levelStyles;
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
/*     */   public static HighlightConverter newInstance(Configuration config, String[] options) {
/* 173 */     if (options.length < 1) {
/* 174 */       LOGGER.error("Incorrect number of options on style. Expected at least 1, received " + options.length);
/* 175 */       return null;
/*     */     } 
/* 177 */     if (options[0] == null) {
/* 178 */       LOGGER.error("No pattern supplied on style");
/* 179 */       return null;
/*     */     } 
/* 181 */     PatternParser parser = PatternLayout.createPatternParser(config);
/* 182 */     List<PatternFormatter> formatters = parser.parse(options[0]);
/* 183 */     return new HighlightConverter(formatters, createLevelStyleMap(options));
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
/*     */   private HighlightConverter(List<PatternFormatter> patternFormatters, Map<Level, String> levelStyles) {
/* 197 */     super("style", "style");
/* 198 */     this.patternFormatters = patternFormatters;
/* 199 */     this.levelStyles = levelStyles;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void format(LogEvent event, StringBuilder toAppendTo) {
/* 207 */     StringBuilder buf = new StringBuilder();
/* 208 */     for (PatternFormatter formatter : this.patternFormatters) {
/* 209 */       formatter.format(event, buf);
/*     */     }
/*     */     
/* 212 */     if (buf.length() > 0) {
/* 213 */       toAppendTo.append(this.levelStyles.get(event.getLevel())).append(buf.toString()).append(AnsiEscape.getDefaultStyle());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean handlesThrowable() {
/* 220 */     for (PatternFormatter formatter : this.patternFormatters) {
/* 221 */       if (formatter.handlesThrowable()) {
/* 222 */         return true;
/*     */       }
/*     */     } 
/* 225 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\pattern\HighlightConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */