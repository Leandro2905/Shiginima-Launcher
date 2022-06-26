/*    */ package org.apache.logging.log4j.core.appender.db.jpa.converter;
/*    */ 
/*    */ import javax.persistence.AttributeConverter;
/*    */ import javax.persistence.Converter;
/*    */ import org.apache.logging.log4j.util.Strings;
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
/*    */ @Converter(autoApply = false)
/*    */ public class StackTraceElementAttributeConverter
/*    */   implements AttributeConverter<StackTraceElement, String>
/*    */ {
/*    */   private static final int UNKNOWN_SOURCE = -1;
/*    */   private static final int NATIVE_METHOD = -2;
/*    */   
/*    */   public String convertToDatabaseColumn(StackTraceElement element) {
/* 36 */     if (element == null) {
/* 37 */       return null;
/*    */     }
/*    */     
/* 40 */     return element.toString();
/*    */   }
/*    */ 
/*    */   
/*    */   public StackTraceElement convertToEntityAttribute(String s) {
/* 45 */     if (Strings.isEmpty(s)) {
/* 46 */       return null;
/*    */     }
/*    */     
/* 49 */     return convertString(s);
/*    */   }
/*    */   
/*    */   static StackTraceElement convertString(String s) {
/* 53 */     int open = s.indexOf("(");
/*    */     
/* 55 */     String classMethod = s.substring(0, open);
/* 56 */     String className = classMethod.substring(0, classMethod.lastIndexOf("."));
/* 57 */     String methodName = classMethod.substring(classMethod.lastIndexOf(".") + 1);
/*    */     
/* 59 */     String parenthesisContents = s.substring(open + 1, s.indexOf(")"));
/*    */     
/* 61 */     String fileName = null;
/* 62 */     int lineNumber = -1;
/* 63 */     if ("Native Method".equals(parenthesisContents)) {
/* 64 */       lineNumber = -2;
/* 65 */     } else if (!"Unknown Source".equals(parenthesisContents)) {
/* 66 */       int colon = parenthesisContents.indexOf(":");
/* 67 */       if (colon > -1) {
/* 68 */         fileName = parenthesisContents.substring(0, colon);
/*    */         try {
/* 70 */           lineNumber = Integer.parseInt(parenthesisContents.substring(colon + 1));
/* 71 */         } catch (NumberFormatException ignore) {}
/*    */       }
/*    */       else {
/*    */         
/* 75 */         fileName = parenthesisContents.substring(0);
/*    */       } 
/*    */     } 
/*    */     
/* 79 */     return new StackTraceElement(className, methodName, fileName, lineNumber);
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\appender\db\jpa\converter\StackTraceElementAttributeConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */