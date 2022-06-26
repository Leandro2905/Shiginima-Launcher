/*    */ package org.apache.logging.log4j.core.lookup;
/*    */ 
/*    */ import java.text.DateFormat;
/*    */ import java.text.SimpleDateFormat;
/*    */ import java.util.Date;
/*    */ import org.apache.logging.log4j.Logger;
/*    */ import org.apache.logging.log4j.core.LogEvent;
/*    */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*    */ import org.apache.logging.log4j.status.StatusLogger;
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
/*    */ @Plugin(name = "date", category = "Lookup")
/*    */ public class DateLookup
/*    */   implements StrLookup
/*    */ {
/* 34 */   private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String lookup(String key) {
/* 42 */     return formatDate(System.currentTimeMillis(), key);
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
/* 53 */     return formatDate(event.getTimeMillis(), key);
/*    */   }
/*    */   
/*    */   private String formatDate(long date, String format) {
/* 57 */     DateFormat dateFormat = null;
/* 58 */     if (format != null) {
/*    */       try {
/* 60 */         dateFormat = new SimpleDateFormat(format);
/* 61 */       } catch (Exception ex) {
/* 62 */         LOGGER.error("Invalid date format: \"" + format + "\", using default", ex);
/*    */       } 
/*    */     }
/* 65 */     if (dateFormat == null) {
/* 66 */       dateFormat = DateFormat.getInstance();
/*    */     }
/* 68 */     return dateFormat.format(new Date(date));
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\lookup\DateLookup.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */