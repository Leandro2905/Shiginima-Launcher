/*     */ package org.apache.logging.log4j.core.pattern;
/*     */ 
/*     */ import java.util.List;
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
/*     */ @Plugin(name = "encode", category = "Converter")
/*     */ @ConverterKeys({"enc", "encode"})
/*     */ public final class EncodingPatternConverter
/*     */   extends LogEventPatternConverter
/*     */ {
/*     */   private final List<PatternFormatter> formatters;
/*     */   
/*     */   private EncodingPatternConverter(List<PatternFormatter> formatters) {
/*  41 */     super("encode", "encode");
/*  42 */     this.formatters = formatters;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static EncodingPatternConverter newInstance(Configuration config, String[] options) {
/*  53 */     if (options.length != 1) {
/*  54 */       LOGGER.error("Incorrect number of options on escape. Expected 1, received " + options.length);
/*  55 */       return null;
/*     */     } 
/*  57 */     if (options[0] == null) {
/*  58 */       LOGGER.error("No pattern supplied on escape");
/*  59 */       return null;
/*     */     } 
/*  61 */     PatternParser parser = PatternLayout.createPatternParser(config);
/*  62 */     List<PatternFormatter> formatters = parser.parse(options[0]);
/*  63 */     return new EncodingPatternConverter(formatters);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void format(LogEvent event, StringBuilder toAppendTo) {
/*  71 */     StringBuilder buf = new StringBuilder();
/*  72 */     for (PatternFormatter formatter : this.formatters) {
/*  73 */       formatter.format(event, buf);
/*     */     }
/*  75 */     for (int i = 0; i < buf.length(); i++) {
/*  76 */       char c = buf.charAt(i);
/*  77 */       switch (c) {
/*     */         case '\r':
/*  79 */           toAppendTo.append("\\r");
/*     */           break;
/*     */         case '\n':
/*  82 */           toAppendTo.append("\\n");
/*     */           break;
/*     */         case '&':
/*  85 */           toAppendTo.append("&amp;");
/*     */           break;
/*     */         case '<':
/*  88 */           toAppendTo.append("&lt;");
/*     */           break;
/*     */         case '>':
/*  91 */           toAppendTo.append("&gt;");
/*     */           break;
/*     */         case '"':
/*  94 */           toAppendTo.append("&quot;");
/*     */           break;
/*     */         case '\'':
/*  97 */           toAppendTo.append("&apos;");
/*     */           break;
/*     */         case '/':
/* 100 */           toAppendTo.append("&#x2F;");
/*     */           break;
/*     */         default:
/* 103 */           toAppendTo.append(c);
/*     */           break;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\pattern\EncodingPatternConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */