/*    */ package org.apache.logging.log4j.core.appender.db.jpa.converter;
/*    */ 
/*    */ import javax.persistence.AttributeConverter;
/*    */ import javax.persistence.Converter;
/*    */ import org.apache.logging.log4j.ThreadContext;
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
/*    */ @Converter(autoApply = false)
/*    */ public class ContextStackAttributeConverter
/*    */   implements AttributeConverter<ThreadContext.ContextStack, String>
/*    */ {
/*    */   public String convertToDatabaseColumn(ThreadContext.ContextStack contextStack) {
/* 36 */     if (contextStack == null) {
/* 37 */       return null;
/*    */     }
/*    */     
/* 40 */     StringBuilder builder = new StringBuilder();
/* 41 */     for (String value : contextStack.asList()) {
/* 42 */       if (builder.length() > 0) {
/* 43 */         builder.append('\n');
/*    */       }
/* 45 */       builder.append(value);
/*    */     } 
/* 47 */     return builder.toString();
/*    */   }
/*    */ 
/*    */   
/*    */   public ThreadContext.ContextStack convertToEntityAttribute(String s) {
/* 52 */     throw new UnsupportedOperationException("Log events can only be persisted, not extracted.");
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\appender\db\jpa\converter\ContextStackAttributeConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */