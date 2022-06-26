/*    */ package org.apache.logging.log4j.core.pattern;
/*    */ 
/*    */ import org.apache.logging.log4j.core.LogEvent;
/*    */ import org.apache.logging.log4j.core.config.plugins.Plugin;
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
/*    */ @Plugin(name = "ClassNamePatternConverter", category = "Converter")
/*    */ @ConverterKeys({"C", "class"})
/*    */ public final class ClassNamePatternConverter
/*    */   extends NamePatternConverter
/*    */ {
/*    */   private static final String NA = "?";
/*    */   
/*    */   private ClassNamePatternConverter(String[] options) {
/* 39 */     super("Class Name", "class name", options);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static ClassNamePatternConverter newInstance(String[] options) {
/* 49 */     return new ClassNamePatternConverter(options);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void format(LogEvent event, StringBuilder toAppendTo) {
/* 60 */     StackTraceElement element = event.getSource();
/* 61 */     if (element == null) {
/* 62 */       toAppendTo.append("?");
/*    */     } else {
/* 64 */       toAppendTo.append(abbreviate(element.getClassName()));
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\pattern\ClassNamePatternConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */