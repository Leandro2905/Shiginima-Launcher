/*    */ package org.apache.logging.log4j.core.pattern;
/*    */ 
/*    */ import java.util.Date;
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
/*    */ @Plugin(name = "IntegerPatternConverter", category = "FileConverter")
/*    */ @ConverterKeys({"i", "index"})
/*    */ public final class IntegerPatternConverter
/*    */   extends AbstractPatternConverter
/*    */   implements ArrayPatternConverter
/*    */ {
/* 34 */   private static final IntegerPatternConverter INSTANCE = new IntegerPatternConverter();
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private IntegerPatternConverter() {
/* 40 */     super("Integer", "integer");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static IntegerPatternConverter newInstance(String[] options) {
/* 51 */     return INSTANCE;
/*    */   }
/*    */ 
/*    */   
/*    */   public void format(StringBuilder toAppendTo, Object... objects) {
/* 56 */     for (Object obj : objects) {
/* 57 */       if (obj instanceof Integer) {
/* 58 */         format(obj, toAppendTo);
/*    */         break;
/*    */       } 
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void format(Object obj, StringBuilder toAppendTo) {
/* 69 */     if (obj instanceof Integer) {
/* 70 */       toAppendTo.append(obj.toString());
/* 71 */     } else if (obj instanceof Date) {
/* 72 */       toAppendTo.append(Long.toString(((Date)obj).getTime()));
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\pattern\IntegerPatternConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */