/*    */ package org.apache.logging.log4j.core.appender.db.jpa.converter;
/*    */ 
/*    */ import javax.persistence.AttributeConverter;
/*    */ import javax.persistence.Converter;
/*    */ import org.apache.logging.log4j.Level;
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
/*    */ @Converter(autoApply = false)
/*    */ public class LevelAttributeConverter
/*    */   implements AttributeConverter<Level, String>
/*    */ {
/*    */   public String convertToDatabaseColumn(Level level) {
/* 33 */     if (level == null) {
/* 34 */       return null;
/*    */     }
/* 36 */     return level.name();
/*    */   }
/*    */ 
/*    */   
/*    */   public Level convertToEntityAttribute(String s) {
/* 41 */     if (Strings.isEmpty(s)) {
/* 42 */       return null;
/*    */     }
/*    */     
/* 45 */     return Level.toLevel(s, null);
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\appender\db\jpa\converter\LevelAttributeConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */