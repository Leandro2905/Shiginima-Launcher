/*    */ package org.apache.logging.log4j.core.layout;
/*    */ 
/*    */ import java.util.Collections;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*    */ import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
/*    */ import org.apache.logging.log4j.core.config.plugins.PluginElement;
/*    */ import org.apache.logging.log4j.core.config.plugins.PluginFactory;
/*    */ import org.apache.logging.log4j.core.util.KeyValuePair;
/*    */ import org.apache.logging.log4j.message.StructuredDataId;
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
/*    */ @Plugin(name = "LoggerFields", category = "Core", printObject = true)
/*    */ public final class LoggerFields
/*    */ {
/*    */   private final Map<String, String> map;
/*    */   private final String sdId;
/*    */   private final String enterpriseId;
/*    */   private final boolean discardIfAllFieldsAreEmpty;
/*    */   
/*    */   private LoggerFields(Map<String, String> map, String sdId, String enterpriseId, boolean discardIfAllFieldsAreEmpty) {
/* 43 */     this.sdId = sdId;
/* 44 */     this.enterpriseId = enterpriseId;
/* 45 */     this.map = Collections.unmodifiableMap(map);
/* 46 */     this.discardIfAllFieldsAreEmpty = discardIfAllFieldsAreEmpty;
/*    */   }
/*    */   
/*    */   public Map<String, String> getMap() {
/* 50 */     return this.map;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 55 */     return this.map.toString();
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @PluginFactory
/*    */   public static LoggerFields createLoggerFields(@PluginElement("LoggerFields") KeyValuePair[] keyValuePairs, @PluginAttribute("sdId") String sdId, @PluginAttribute("enterpriseId") String enterpriseId, @PluginAttribute(value = "discardIfAllFieldsAreEmpty", defaultBoolean = false) boolean discardIfAllFieldsAreEmpty) {
/* 77 */     Map<String, String> map = new HashMap<String, String>();
/*    */     
/* 79 */     for (KeyValuePair keyValuePair : keyValuePairs) {
/* 80 */       map.put(keyValuePair.getKey(), keyValuePair.getValue());
/*    */     }
/*    */     
/* 83 */     return new LoggerFields(map, sdId, enterpriseId, discardIfAllFieldsAreEmpty);
/*    */   }
/*    */   
/*    */   public StructuredDataId getSdId() {
/* 87 */     if (this.enterpriseId == null || this.sdId == null) {
/* 88 */       return null;
/*    */     }
/* 90 */     int eId = Integer.parseInt(this.enterpriseId);
/* 91 */     return new StructuredDataId(this.sdId, eId, null, null);
/*    */   }
/*    */   
/*    */   public boolean getDiscardIfAllFieldsAreEmpty() {
/* 95 */     return this.discardIfAllFieldsAreEmpty;
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\layout\LoggerFields.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */