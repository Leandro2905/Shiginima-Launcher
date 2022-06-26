/*     */ package org.apache.logging.log4j.core.pattern;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import org.apache.logging.log4j.core.util.Patterns;
/*     */ import org.apache.logging.log4j.util.EnglishEnums;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public enum AnsiEscape
/*     */ {
/*  42 */   CSI("\033["),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  47 */   SUFFIX("m"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  52 */   SEPARATOR(";"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  57 */   NORMAL("0"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  62 */   BRIGHT("1"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  67 */   DIM("2"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  72 */   UNDERLINE("3"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  77 */   BLINK("5"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  82 */   REVERSE("7"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  87 */   HIDDEN("8"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  92 */   BLACK("30"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  97 */   FG_BLACK("30"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 102 */   RED("31"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 107 */   FG_RED("31"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 112 */   GREEN("32"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 117 */   FG_GREEN("32"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 122 */   YELLOW("33"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 127 */   FG_YELLOW("33"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 132 */   BLUE("34"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 137 */   FG_BLUE("34"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 142 */   MAGENTA("35"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 147 */   FG_MAGENTA("35"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 152 */   CYAN("36"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 157 */   FG_CYAN("36"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 162 */   WHITE("37"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 167 */   FG_WHITE("37"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 172 */   DEFAULT("39"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 177 */   FG_DEFAULT("39"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 182 */   BG_BLACK("40"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 187 */   BG_RED("41"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 192 */   BG_GREEN("42"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 197 */   BG_YELLOW("43"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 202 */   BG_BLUE("44"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 207 */   BG_MAGENTA("45"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 212 */   BG_CYAN("46"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 217 */   BG_WHITE("47");
/*     */   
/*     */   private final String code;
/*     */   
/*     */   AnsiEscape(String code) {
/* 222 */     this.code = code;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getDefaultStyle() {
/* 231 */     return CSI.getCode() + SUFFIX.getCode();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getCode() {
/* 240 */     return this.code;
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
/*     */   public static Map<String, String> createMap(String values, String[] dontEscapeKeys) {
/* 264 */     return createMap(values.split(Patterns.COMMA_SEPARATOR), dontEscapeKeys);
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
/*     */   public static Map<String, String> createMap(String[] values, String[] dontEscapeKeys) {
/* 290 */     String[] sortedIgnoreKeys = (dontEscapeKeys != null) ? (String[])dontEscapeKeys.clone() : new String[0];
/* 291 */     Arrays.sort((Object[])sortedIgnoreKeys);
/* 292 */     Map<String, String> map = new HashMap<String, String>();
/* 293 */     for (String string : values) {
/* 294 */       String[] keyValue = string.split(Patterns.toWhitespaceSeparator("="));
/* 295 */       if (keyValue.length > 1) {
/* 296 */         String key = keyValue[0].toUpperCase(Locale.ENGLISH);
/* 297 */         String value = keyValue[1];
/* 298 */         boolean escape = (Arrays.binarySearch((Object[])sortedIgnoreKeys, key) < 0);
/* 299 */         map.put(key, escape ? createSequence(value.split("\\s")) : value);
/*     */       } 
/*     */     } 
/* 302 */     return map;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String createSequence(String... names) {
/* 313 */     if (names == null) {
/* 314 */       return getDefaultStyle();
/*     */     }
/* 316 */     StringBuilder sb = new StringBuilder(CSI.getCode());
/* 317 */     boolean first = true;
/* 318 */     for (String name : names) {
/*     */       try {
/* 320 */         AnsiEscape escape = (AnsiEscape)EnglishEnums.valueOf(AnsiEscape.class, name.trim());
/* 321 */         if (!first) {
/* 322 */           sb.append(SEPARATOR.getCode());
/*     */         }
/* 324 */         first = false;
/* 325 */         sb.append(escape.getCode());
/* 326 */       } catch (Exception ex) {}
/*     */     } 
/*     */ 
/*     */     
/* 330 */     sb.append(SUFFIX.getCode());
/* 331 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\pattern\AnsiEscape.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */