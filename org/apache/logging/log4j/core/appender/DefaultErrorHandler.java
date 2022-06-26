/*    */ package org.apache.logging.log4j.core.appender;
/*    */ 
/*    */ import org.apache.logging.log4j.Logger;
/*    */ import org.apache.logging.log4j.core.Appender;
/*    */ import org.apache.logging.log4j.core.ErrorHandler;
/*    */ import org.apache.logging.log4j.core.LogEvent;
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
/*    */ public class DefaultErrorHandler
/*    */   implements ErrorHandler
/*    */ {
/* 30 */   private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*    */   
/*    */   private static final int MAX_EXCEPTIONS = 3;
/*    */   
/*    */   private static final int EXCEPTION_INTERVAL = 300000;
/*    */   
/* 36 */   private int exceptionCount = 0;
/*    */   
/*    */   private long lastException;
/*    */   
/*    */   private final Appender appender;
/*    */   
/*    */   public DefaultErrorHandler(Appender appender) {
/* 43 */     this.appender = appender;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void error(String msg) {
/* 53 */     long current = System.currentTimeMillis();
/* 54 */     if (this.lastException + 300000L < current || this.exceptionCount++ < 3) {
/* 55 */       LOGGER.error(msg);
/*    */     }
/* 57 */     this.lastException = current;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void error(String msg, Throwable t) {
/* 67 */     long current = System.currentTimeMillis();
/* 68 */     if (this.lastException + 300000L < current || this.exceptionCount++ < 3) {
/* 69 */       LOGGER.error(msg, t);
/*    */     }
/* 71 */     this.lastException = current;
/* 72 */     if (!this.appender.ignoreExceptions() && t != null && !(t instanceof AppenderLoggingException)) {
/* 73 */       throw new AppenderLoggingException(msg, t);
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
/*    */   public void error(String msg, LogEvent event, Throwable t) {
/* 85 */     long current = System.currentTimeMillis();
/* 86 */     if (this.lastException + 300000L < current || this.exceptionCount++ < 3) {
/* 87 */       LOGGER.error(msg, t);
/*    */     }
/* 89 */     this.lastException = current;
/* 90 */     if (!this.appender.ignoreExceptions() && t != null && !(t instanceof AppenderLoggingException))
/* 91 */       throw new AppenderLoggingException(msg, t); 
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\appender\DefaultErrorHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */