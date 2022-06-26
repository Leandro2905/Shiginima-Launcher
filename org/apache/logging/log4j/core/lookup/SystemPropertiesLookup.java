/*    */ package org.apache.logging.log4j.core.lookup;
/*    */ 
/*    */ import org.apache.logging.log4j.core.LogEvent;
/*    */ import org.apache.logging.log4j.core.config.plugins.Plugin;
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
/*    */ @Plugin(name = "sys", category = "Lookup")
/*    */ public class SystemPropertiesLookup
/*    */   implements StrLookup
/*    */ {
/*    */   public String lookup(String key) {
/*    */     try {
/* 36 */       return System.getProperty(key);
/* 37 */     } catch (Exception ex) {
/*    */       
/* 39 */       return null;
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String lookup(LogEvent event, String key) {
/*    */     try {
/* 52 */       return System.getProperty(key);
/* 53 */     } catch (Exception ex) {
/*    */       
/* 55 */       return null;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\lookup\SystemPropertiesLookup.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */