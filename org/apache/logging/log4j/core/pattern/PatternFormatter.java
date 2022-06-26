/*    */ package org.apache.logging.log4j.core.pattern;
/*    */ 
/*    */ import org.apache.logging.log4j.core.LogEvent;
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
/*    */ public class PatternFormatter
/*    */ {
/*    */   private final LogEventPatternConverter converter;
/*    */   private final FormattingInfo field;
/*    */   
/*    */   public PatternFormatter(LogEventPatternConverter converter, FormattingInfo field) {
/* 30 */     this.converter = converter;
/* 31 */     this.field = field;
/*    */   }
/*    */   
/*    */   public void format(LogEvent event, StringBuilder buf) {
/* 35 */     int startField = buf.length();
/* 36 */     this.converter.format(event, buf);
/* 37 */     this.field.format(startField, buf);
/*    */   }
/*    */   
/*    */   public LogEventPatternConverter getConverter() {
/* 41 */     return this.converter;
/*    */   }
/*    */   
/*    */   public FormattingInfo getFormattingInfo() {
/* 45 */     return this.field;
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
/*    */   
/*    */   public boolean handlesThrowable() {
/* 58 */     return this.converter.handlesThrowable();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 68 */     StringBuilder sb = new StringBuilder();
/* 69 */     sb.append(super.toString());
/* 70 */     sb.append("[converter=");
/* 71 */     sb.append(this.converter);
/* 72 */     sb.append(", field=");
/* 73 */     sb.append(this.field);
/* 74 */     sb.append(']');
/* 75 */     return sb.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\pattern\PatternFormatter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */