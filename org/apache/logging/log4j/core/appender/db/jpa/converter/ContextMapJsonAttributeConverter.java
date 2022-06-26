/*    */ package org.apache.logging.log4j.core.appender.db.jpa.converter;
/*    */ 
/*    */ import com.fasterxml.jackson.core.type.TypeReference;
/*    */ import com.fasterxml.jackson.databind.ObjectMapper;
/*    */ import java.io.IOException;
/*    */ import java.util.Map;
/*    */ import javax.persistence.AttributeConverter;
/*    */ import javax.persistence.Converter;
/*    */ import javax.persistence.PersistenceException;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Converter(autoApply = false)
/*    */ public class ContextMapJsonAttributeConverter
/*    */   implements AttributeConverter<Map<String, String>, String>
/*    */ {
/* 41 */   static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
/*    */ 
/*    */   
/*    */   public String convertToDatabaseColumn(Map<String, String> contextMap) {
/* 45 */     if (contextMap == null) {
/* 46 */       return null;
/*    */     }
/*    */     
/*    */     try {
/* 50 */       return OBJECT_MAPPER.writeValueAsString(contextMap);
/* 51 */     } catch (IOException e) {
/* 52 */       throw new PersistenceException("Failed to convert map to JSON string.", e);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public Map<String, String> convertToEntityAttribute(String s) {
/* 58 */     if (Strings.isEmpty(s)) {
/* 59 */       return null;
/*    */     }
/*    */     try {
/* 62 */       return (Map<String, String>)OBJECT_MAPPER.readValue(s, new TypeReference<Map<String, String>>() {  });
/* 63 */     } catch (IOException e) {
/* 64 */       throw new PersistenceException("Failed to convert JSON string to map.", e);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\appender\db\jpa\converter\ContextMapJsonAttributeConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */