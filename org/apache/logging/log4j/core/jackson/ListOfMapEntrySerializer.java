/*    */ package org.apache.logging.log4j.core.jackson;
/*    */ 
/*    */ import com.fasterxml.jackson.core.JsonGenerationException;
/*    */ import com.fasterxml.jackson.core.JsonGenerator;
/*    */ import com.fasterxml.jackson.databind.SerializerProvider;
/*    */ import com.fasterxml.jackson.databind.ser.std.StdSerializer;
/*    */ import java.io.IOException;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
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
/*    */ public class ListOfMapEntrySerializer
/*    */   extends StdSerializer<Map>
/*    */ {
/*    */   protected ListOfMapEntrySerializer() {
/* 38 */     super(Map.class);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void serialize(Map<String, String> map, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
/* 44 */     Set<Map.Entry<String, String>> entrySet = map.entrySet();
/* 45 */     MapEntry[] pairs = new MapEntry[entrySet.size()];
/* 46 */     int i = 0;
/* 47 */     for (Map.Entry<String, String> entry : entrySet) {
/* 48 */       pairs[i++] = new MapEntry(entry.getKey(), entry.getValue());
/*    */     }
/* 50 */     jgen.writeObject(pairs);
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\jackson\ListOfMapEntrySerializer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */