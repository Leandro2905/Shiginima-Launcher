/*    */ package org.apache.logging.log4j.core.pattern;
/*    */ 
/*    */ import org.apache.logging.log4j.core.LogEvent;
/*    */ import org.apache.logging.log4j.core.config.Configuration;
/*    */ import org.apache.logging.log4j.core.util.OptionConverter;
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
/*    */ public final class LiteralPatternConverter
/*    */   extends LogEventPatternConverter
/*    */   implements ArrayPatternConverter
/*    */ {
/*    */   private final String literal;
/*    */   private final Configuration config;
/*    */   private final boolean substitute;
/*    */   
/*    */   public LiteralPatternConverter(Configuration config, String literal) {
/* 44 */     super("Literal", "literal");
/* 45 */     this.literal = OptionConverter.convertSpecialChars(literal);
/* 46 */     this.config = config;
/* 47 */     this.substitute = (config != null && literal.contains("${"));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void format(LogEvent event, StringBuilder toAppendTo) {
/* 55 */     toAppendTo.append(this.substitute ? this.config.getStrSubstitutor().replace(event, this.literal) : this.literal);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void format(Object obj, StringBuilder output) {
/* 63 */     output.append(this.substitute ? this.config.getStrSubstitutor().replace(this.literal) : this.literal);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void format(StringBuilder output, Object... objects) {
/* 71 */     output.append(this.substitute ? this.config.getStrSubstitutor().replace(this.literal) : this.literal);
/*    */   }
/*    */   
/*    */   public String getLiteral() {
/* 75 */     return this.literal;
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\pattern\LiteralPatternConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */