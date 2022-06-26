/*    */ package org.apache.logging.log4j.core.async;
/*    */ 
/*    */ import com.lmax.disruptor.EventTranslator;
/*    */ import java.util.Map;
/*    */ import org.apache.logging.log4j.Level;
/*    */ import org.apache.logging.log4j.Marker;
/*    */ import org.apache.logging.log4j.ThreadContext;
/*    */ import org.apache.logging.log4j.message.Message;
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
/*    */ public class RingBufferLogEventTranslator
/*    */   implements EventTranslator<RingBufferLogEvent>
/*    */ {
/*    */   private AsyncLogger asyncLogger;
/*    */   private String loggerName;
/*    */   private Marker marker;
/*    */   private String fqcn;
/*    */   private Level level;
/*    */   private Message message;
/*    */   private Throwable thrown;
/*    */   private Map<String, String> contextMap;
/*    */   private ThreadContext.ContextStack contextStack;
/*    */   private String threadName;
/*    */   private StackTraceElement location;
/*    */   private long currentTimeMillis;
/*    */   
/*    */   public void translateTo(RingBufferLogEvent event, long sequence) {
/* 53 */     event.setValues(this.asyncLogger, this.loggerName, this.marker, this.fqcn, this.level, this.message, this.thrown, this.contextMap, this.contextStack, this.threadName, this.location, this.currentTimeMillis);
/*    */ 
/*    */     
/* 56 */     clear();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private void clear() {
/* 64 */     setValues(null, null, null, null, null, null, null, null, null, null, null, 0L);
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
/*    */   public void setValues(AsyncLogger asyncLogger, String loggerName, Marker marker, String fqcn, Level level, Message message, Throwable thrown, Map<String, String> contextMap, ThreadContext.ContextStack contextStack, String threadName, StackTraceElement location, long currentTimeMillis) {
/* 84 */     this.asyncLogger = asyncLogger;
/* 85 */     this.loggerName = loggerName;
/* 86 */     this.marker = marker;
/* 87 */     this.fqcn = fqcn;
/* 88 */     this.level = level;
/* 89 */     this.message = message;
/* 90 */     this.thrown = thrown;
/* 91 */     this.contextMap = contextMap;
/* 92 */     this.contextStack = contextStack;
/* 93 */     this.threadName = threadName;
/* 94 */     this.location = location;
/* 95 */     this.currentTimeMillis = currentTimeMillis;
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\async\RingBufferLogEventTranslator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */