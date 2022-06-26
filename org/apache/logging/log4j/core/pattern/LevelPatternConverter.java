/*     */ package org.apache.logging.log4j.core.pattern;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import org.apache.logging.log4j.Level;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.util.Patterns;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Plugin(name = "LevelPatternConverter", category = "Converter")
/*     */ @ConverterKeys({"p", "level"})
/*     */ public final class LevelPatternConverter
/*     */   extends LogEventPatternConverter
/*     */ {
/*     */   private static final String OPTION_LENGTH = "length";
/*     */   private static final String OPTION_LOWER = "lowerCase";
/*  40 */   private static final LevelPatternConverter INSTANCE = new LevelPatternConverter(null);
/*     */ 
/*     */   
/*     */   private final Map<Level, String> levelMap;
/*     */ 
/*     */ 
/*     */   
/*     */   private LevelPatternConverter(Map<Level, String> map) {
/*  48 */     super("Level", "level");
/*  49 */     this.levelMap = map;
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
/*     */   public static LevelPatternConverter newInstance(String[] options) {
/*  61 */     if (options == null || options.length == 0) {
/*  62 */       return INSTANCE;
/*     */     }
/*  64 */     Map<Level, String> levelMap = new HashMap<Level, String>();
/*  65 */     int length = Integer.MAX_VALUE;
/*  66 */     boolean lowerCase = false;
/*  67 */     String[] definitions = options[0].split(Patterns.COMMA_SEPARATOR);
/*  68 */     for (String def : definitions) {
/*  69 */       String[] pair = def.split("=");
/*  70 */       if (pair == null || pair.length != 2) {
/*  71 */         LOGGER.error("Invalid option {}", new Object[] { def });
/*     */       } else {
/*     */         
/*  74 */         String key = pair[0].trim();
/*  75 */         String value = pair[1].trim();
/*  76 */         if ("length".equalsIgnoreCase(key)) {
/*  77 */           length = Integer.parseInt(value);
/*  78 */         } else if ("lowerCase".equalsIgnoreCase(key)) {
/*  79 */           lowerCase = Boolean.parseBoolean(value);
/*     */         } else {
/*  81 */           Level level = Level.toLevel(key, null);
/*  82 */           if (level == null) {
/*  83 */             LOGGER.error("Invalid Level {}", new Object[] { key });
/*     */           } else {
/*  85 */             levelMap.put(level, value);
/*     */           } 
/*     */         } 
/*     */       } 
/*  89 */     }  if (levelMap.isEmpty() && length == Integer.MAX_VALUE && !lowerCase) {
/*  90 */       return INSTANCE;
/*     */     }
/*  92 */     for (Level level : Level.values()) {
/*  93 */       if (!levelMap.containsKey(level)) {
/*  94 */         String left = left(level, length);
/*  95 */         levelMap.put(level, lowerCase ? left.toLowerCase(Locale.US) : left);
/*     */       } 
/*     */     } 
/*  98 */     return new LevelPatternConverter(levelMap);
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
/*     */   private static String left(Level level, int length) {
/* 112 */     String string = level.toString();
/* 113 */     if (length >= string.length()) {
/* 114 */       return string;
/*     */     }
/* 116 */     return string.substring(0, length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void format(LogEvent event, StringBuilder output) {
/* 124 */     output.append((this.levelMap == null) ? event.getLevel().toString() : this.levelMap.get(event.getLevel()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getStyleClass(Object e) {
/* 132 */     if (e instanceof LogEvent) {
/* 133 */       return "level " + ((LogEvent)e).getLevel().name().toLowerCase(Locale.ENGLISH);
/*     */     }
/*     */     
/* 136 */     return "level";
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\pattern\LevelPatternConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */