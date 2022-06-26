/*    */ package org.apache.logging.log4j.core.async;
/*    */ 
/*    */ import java.net.URI;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ import org.apache.logging.log4j.core.LoggerContext;
/*    */ import org.apache.logging.log4j.core.selector.ContextSelector;
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
/*    */ public class AsyncLoggerContextSelector
/*    */   implements ContextSelector
/*    */ {
/* 33 */   private static final AsyncLoggerContext CONTEXT = new AsyncLoggerContext("AsyncLoggerContext@" + AsyncLoggerContext.class.hashCode());
/*    */ 
/*    */ 
/*    */   
/*    */   public LoggerContext getContext(String fqcn, ClassLoader loader, boolean currentContext) {
/* 38 */     return CONTEXT;
/*    */   }
/*    */ 
/*    */   
/*    */   public List<LoggerContext> getLoggerContexts() {
/* 43 */     List<LoggerContext> list = new ArrayList<LoggerContext>();
/* 44 */     list.add(CONTEXT);
/* 45 */     return Collections.unmodifiableList(list);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public LoggerContext getContext(String fqcn, ClassLoader loader, boolean currentContext, URI configLocation) {
/* 51 */     return CONTEXT;
/*    */   }
/*    */   
/*    */   public void removeContext(LoggerContext context) {}
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\async\AsyncLoggerContextSelector.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */