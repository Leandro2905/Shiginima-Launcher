/*    */ package org.apache.logging.log4j.core.lookup;
/*    */ 
/*    */ import java.util.Map;
/*    */ import org.apache.logging.log4j.core.LogEvent;
/*    */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*    */ import org.apache.logging.log4j.message.MapMessage;
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
/*    */ 
/*    */ @Plugin(name = "map", category = "Lookup")
/*    */ public class MapLookup
/*    */   implements StrLookup
/*    */ {
/*    */   private final Map<String, String> map;
/*    */   
/*    */   public MapLookup(Map<String, String> map) {
/* 41 */     this.map = map;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public MapLookup() {
/* 48 */     this.map = null;
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
/*    */   public String lookup(String key) {
/* 62 */     if (this.map == null) {
/* 63 */       return null;
/*    */     }
/* 65 */     String obj = this.map.get(key);
/* 66 */     if (obj == null) {
/* 67 */       return null;
/*    */     }
/* 69 */     return obj;
/*    */   }
/*    */ 
/*    */   
/*    */   public String lookup(LogEvent event, String key) {
/* 74 */     if (this.map == null && !(event.getMessage() instanceof MapMessage)) {
/* 75 */       return null;
/*    */     }
/* 77 */     if (this.map != null && this.map.containsKey(key)) {
/* 78 */       String obj = this.map.get(key);
/* 79 */       if (obj != null) {
/* 80 */         return obj;
/*    */       }
/*    */     } 
/* 83 */     if (event.getMessage() instanceof MapMessage) {
/* 84 */       return ((MapMessage)event.getMessage()).get(key);
/*    */     }
/* 86 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\lookup\MapLookup.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */