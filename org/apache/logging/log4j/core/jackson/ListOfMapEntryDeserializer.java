/*    */ package org.apache.logging.log4j.core.jackson;
/*    */ 
/*    */ import com.fasterxml.jackson.core.JsonParser;
/*    */ import com.fasterxml.jackson.core.JsonProcessingException;
/*    */ import com.fasterxml.jackson.core.type.TypeReference;
/*    */ import com.fasterxml.jackson.databind.DeserializationContext;
/*    */ import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
/*    */ import java.io.IOException;
/*    */ import java.util.HashMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
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
/*    */ public class ListOfMapEntryDeserializer
/*    */   extends StdDeserializer<Map<String, String>>
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   ListOfMapEntryDeserializer() {
/* 40 */     super(Map.class);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Map<String, String> deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
/* 46 */     List<MapEntry> list = (List<MapEntry>)jp.readValueAs(new TypeReference<List<MapEntry>>() {
/*    */         
/*    */         });
/* 49 */     HashMap<String, String> map = new HashMap<String, String>(list.size());
/* 50 */     for (MapEntry mapEntry : list) {
/* 51 */       map.put(mapEntry.getKey(), mapEntry.getValue());
/*    */     }
/* 53 */     return map;
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\jackson\ListOfMapEntryDeserializer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */