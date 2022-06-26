/*    */ package org.apache.logging.log4j.core.pattern;
/*    */ 
/*    */ import java.util.Map;
/*    */ import java.util.Set;
/*    */ import java.util.TreeSet;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ @Plugin(name = "MapPatternConverter", category = "Converter")
/*    */ @ConverterKeys({"K", "map", "MAP"})
/*    */ public final class MapPatternConverter
/*    */   extends LogEventPatternConverter
/*    */ {
/*    */   private final String key;
/*    */   
/*    */   private MapPatternConverter(String[] options) {
/* 47 */     super((options != null && options.length > 0) ? ("MAP{" + options[0] + '}') : "MAP", "map");
/* 48 */     this.key = (options != null && options.length > 0) ? options[0] : null;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static MapPatternConverter newInstance(String[] options) {
/* 58 */     return new MapPatternConverter(options);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void format(LogEvent event, StringBuilder toAppendTo) {
/*    */     MapMessage msg;
/* 67 */     if (event.getMessage() instanceof MapMessage) {
/* 68 */       msg = (MapMessage)event.getMessage();
/*    */     } else {
/*    */       return;
/*    */     } 
/* 72 */     Map<String, String> map = msg.getData();
/*    */ 
/*    */     
/* 75 */     if (this.key == null) {
/* 76 */       if (map.isEmpty()) {
/* 77 */         toAppendTo.append("{}");
/*    */         return;
/*    */       } 
/* 80 */       StringBuilder sb = new StringBuilder("{");
/* 81 */       Set<String> keys = new TreeSet<String>(map.keySet());
/* 82 */       for (String key : keys) {
/* 83 */         if (sb.length() > 1) {
/* 84 */           sb.append(", ");
/*    */         }
/* 86 */         sb.append(key).append('=').append(map.get(key));
/*    */       } 
/*    */       
/* 89 */       sb.append('}');
/* 90 */       toAppendTo.append(sb);
/*    */     } else {
/*    */       
/* 93 */       String val = map.get(this.key);
/*    */       
/* 95 */       if (val != null)
/* 96 */         toAppendTo.append(val); 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\pattern\MapPatternConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */