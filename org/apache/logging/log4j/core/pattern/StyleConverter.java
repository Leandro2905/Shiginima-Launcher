/*     */ package org.apache.logging.log4j.core.pattern;
/*     */ 
/*     */ import java.util.List;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.layout.PatternLayout;
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
/*     */ 
/*     */ @Plugin(name = "style", category = "Converter")
/*     */ @ConverterKeys({"style"})
/*     */ public final class StyleConverter
/*     */   extends LogEventPatternConverter
/*     */   implements AnsiConverter
/*     */ {
/*     */   private final List<PatternFormatter> patternFormatters;
/*     */   private final boolean noAnsi;
/*     */   private final String style;
/*     */   private final String defaultStyle;
/*     */   
/*     */   public static StyleConverter newInstance(Configuration config, String[] options) {
/*  45 */     if (options.length < 1) {
/*  46 */       LOGGER.error("Incorrect number of options on style. Expected at least 1, received " + options.length);
/*  47 */       return null;
/*     */     } 
/*  49 */     if (options[0] == null) {
/*  50 */       LOGGER.error("No pattern supplied on style");
/*  51 */       return null;
/*     */     } 
/*  53 */     if (options[1] == null) {
/*  54 */       LOGGER.error("No style attributes provided");
/*  55 */       return null;
/*     */     } 
/*  57 */     PatternParser parser = PatternLayout.createPatternParser(config);
/*  58 */     List<PatternFormatter> formatters = parser.parse(options[0]);
/*  59 */     String style = AnsiEscape.createSequence(options[1].split(Patterns.COMMA_SEPARATOR));
/*  60 */     boolean noConsoleNoAnsi = (options.length > 2 && "noConsoleNoAnsi=true".equals(options[2]));
/*     */     
/*  62 */     boolean hideAnsi = (noConsoleNoAnsi && System.console() == null);
/*  63 */     return new StyleConverter(formatters, style, hideAnsi);
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
/*     */   private StyleConverter(List<PatternFormatter> patternFormatters, String style, boolean noAnsi) {
/*  85 */     super("style", "style");
/*  86 */     this.patternFormatters = patternFormatters;
/*  87 */     this.style = style;
/*  88 */     this.defaultStyle = AnsiEscape.getDefaultStyle();
/*  89 */     this.noAnsi = noAnsi;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void format(LogEvent event, StringBuilder toAppendTo) {
/*  97 */     StringBuilder buf = new StringBuilder();
/*  98 */     for (PatternFormatter formatter : this.patternFormatters) {
/*  99 */       formatter.format(event, buf);
/*     */     }
/*     */     
/* 102 */     if (buf.length() > 0) {
/* 103 */       if (this.noAnsi) {
/*     */         
/* 105 */         toAppendTo.append(buf.toString());
/*     */       } else {
/* 107 */         toAppendTo.append(this.style).append(buf.toString()).append(this.defaultStyle);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean handlesThrowable() {
/* 114 */     for (PatternFormatter formatter : this.patternFormatters) {
/* 115 */       if (formatter.handlesThrowable()) {
/* 116 */         return true;
/*     */       }
/*     */     } 
/* 119 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 129 */     StringBuilder sb = new StringBuilder();
/* 130 */     sb.append(super.toString());
/* 131 */     sb.append("[style=");
/* 132 */     sb.append(this.style);
/* 133 */     sb.append(", defaultStyle=");
/* 134 */     sb.append(this.defaultStyle);
/* 135 */     sb.append(", patternFormatters=");
/* 136 */     sb.append(this.patternFormatters);
/* 137 */     sb.append(", noAnsi=");
/* 138 */     sb.append(this.noAnsi);
/* 139 */     sb.append(']');
/* 140 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\pattern\StyleConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */