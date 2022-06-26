/*    */ package org.apache.logging.log4j.core.lookup;
/*    */ 
/*    */ import java.util.MissingResourceException;
/*    */ import java.util.ResourceBundle;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Plugin(name = "bundle", category = "Lookup")
/*    */ public class ResourceBundleLookup
/*    */   implements StrLookup
/*    */ {
/*    */   public String lookup(String key) {
/* 42 */     if (key == null) {
/* 43 */       return null;
/*    */     }
/* 45 */     String[] keys = key.split(":");
/* 46 */     int keyLen = keys.length;
/* 47 */     if (keyLen != 2)
/*    */     {
/*    */       
/* 50 */       return null;
/*    */     }
/* 52 */     String bundleName = keys[0];
/* 53 */     String bundleKey = keys[1];
/*    */     
/*    */     try {
/* 56 */       return ResourceBundle.getBundle(bundleName).getString(bundleKey);
/* 57 */     } catch (MissingResourceException e) {
/*    */       
/* 59 */       return null;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String lookup(LogEvent event, String key) {
/* 76 */     return lookup(key);
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\lookup\ResourceBundleLookup.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */