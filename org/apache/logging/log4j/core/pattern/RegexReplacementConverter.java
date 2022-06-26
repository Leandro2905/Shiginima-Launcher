/*    */ package org.apache.logging.log4j.core.pattern;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.regex.Pattern;
/*    */ import org.apache.logging.log4j.core.LogEvent;
/*    */ import org.apache.logging.log4j.core.config.Configuration;
/*    */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*    */ import org.apache.logging.log4j.core.layout.PatternLayout;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Plugin(name = "replace", category = "Converter")
/*    */ @ConverterKeys({"replace"})
/*    */ public final class RegexReplacementConverter
/*    */   extends LogEventPatternConverter
/*    */ {
/*    */   private final Pattern pattern;
/*    */   private final String substitution;
/*    */   private final List<PatternFormatter> formatters;
/*    */   
/*    */   private RegexReplacementConverter(List<PatternFormatter> formatters, Pattern pattern, String substitution) {
/* 48 */     super("replace", "replace");
/* 49 */     this.pattern = pattern;
/* 50 */     this.substitution = substitution;
/* 51 */     this.formatters = formatters;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static RegexReplacementConverter newInstance(Configuration config, String[] options) {
/* 63 */     if (options.length != 3) {
/* 64 */       LOGGER.error("Incorrect number of options on replace. Expected 3 received " + options.length);
/* 65 */       return null;
/*    */     } 
/* 67 */     if (options[0] == null) {
/* 68 */       LOGGER.error("No pattern supplied on replace");
/* 69 */       return null;
/*    */     } 
/* 71 */     if (options[1] == null) {
/* 72 */       LOGGER.error("No regular expression supplied on replace");
/* 73 */       return null;
/*    */     } 
/* 75 */     if (options[2] == null) {
/* 76 */       LOGGER.error("No substitution supplied on replace");
/* 77 */       return null;
/*    */     } 
/* 79 */     Pattern p = Pattern.compile(options[1]);
/* 80 */     PatternParser parser = PatternLayout.createPatternParser(config);
/* 81 */     List<PatternFormatter> formatters = parser.parse(options[0]);
/* 82 */     return new RegexReplacementConverter(formatters, p, options[2]);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void format(LogEvent event, StringBuilder toAppendTo) {
/* 91 */     StringBuilder buf = new StringBuilder();
/* 92 */     for (PatternFormatter formatter : this.formatters) {
/* 93 */       formatter.format(event, buf);
/*    */     }
/* 95 */     toAppendTo.append(this.pattern.matcher(buf.toString()).replaceAll(this.substitution));
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\pattern\RegexReplacementConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */