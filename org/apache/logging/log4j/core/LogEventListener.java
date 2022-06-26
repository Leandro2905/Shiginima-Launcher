/*    */ package org.apache.logging.log4j.core;
/*    */ 
/*    */ import java.util.EventListener;
/*    */ import org.apache.logging.log4j.LogManager;
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
/*    */ public class LogEventListener
/*    */   implements EventListener
/*    */ {
/* 31 */   private final LoggerContext context = (LoggerContext)LogManager.getContext(false);
/*    */ 
/*    */   
/*    */   public void log(LogEvent event) {
/* 35 */     if (event == null) {
/*    */       return;
/*    */     }
/* 38 */     Logger logger = this.context.getLogger(event.getLoggerName());
/* 39 */     if (logger.config.filter(event.getLevel(), event.getMarker(), event.getMessage(), event.getThrown()))
/* 40 */       logger.config.logEvent(event); 
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\LogEventListener.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */