/*    */ package org.apache.logging.log4j.core.pattern;
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
/*    */ public abstract class NamePatternConverter
/*    */   extends LogEventPatternConverter
/*    */ {
/*    */   private final NameAbbreviator abbreviator;
/*    */   
/*    */   protected NamePatternConverter(String name, String style, String[] options) {
/* 37 */     super(name, style);
/*    */     
/* 39 */     if (options != null && options.length > 0) {
/* 40 */       this.abbreviator = NameAbbreviator.getAbbreviator(options[0]);
/*    */     } else {
/* 42 */       this.abbreviator = NameAbbreviator.getDefaultAbbreviator();
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected final String abbreviate(String buf) {
/* 53 */     return this.abbreviator.abbreviate(buf);
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\pattern\NamePatternConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */