/*    */ package org.apache.logging.log4j.core.appender.db.jpa.converter;
/*    */ 
/*    */ import javax.persistence.AttributeConverter;
/*    */ import javax.persistence.Converter;
/*    */ import org.apache.logging.log4j.Marker;
/*    */ import org.apache.logging.log4j.MarkerManager;
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
/*    */ public class MarkerAttributeConverter
/*    */   implements AttributeConverter<Marker, String>
/*    */ {
/*    */   public String convertToDatabaseColumn(Marker marker) {
/* 34 */     if (marker == null) {
/* 35 */       return null;
/*    */     }
/* 37 */     return marker.toString();
/*    */   }
/*    */ 
/*    */   
/*    */   public Marker convertToEntityAttribute(String s) {
/* 42 */     if (Strings.isEmpty(s)) {
/* 43 */       return null;
/*    */     }
/*    */     
/* 46 */     int bracket = s.indexOf("[");
/*    */     
/* 48 */     return (bracket < 1) ? MarkerManager.getMarker(s) : MarkerManager.getMarker(s.substring(0, bracket));
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\appender\db\jpa\converter\MarkerAttributeConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */