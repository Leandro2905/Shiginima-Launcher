/*    */ package org.apache.logging.log4j.core.selector;
/*    */ 
/*    */ import java.net.URI;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ import org.apache.logging.log4j.core.LoggerContext;
/*    */ import org.apache.logging.log4j.core.impl.ContextAnchor;
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
/*    */ public class BasicContextSelector
/*    */   implements ContextSelector
/*    */ {
/* 32 */   private static final LoggerContext CONTEXT = new LoggerContext("Default");
/*    */ 
/*    */ 
/*    */   
/*    */   public LoggerContext getContext(String fqcn, ClassLoader loader, boolean currentContext) {
/* 37 */     LoggerContext ctx = ContextAnchor.THREAD_CONTEXT.get();
/* 38 */     return (ctx != null) ? ctx : CONTEXT;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public LoggerContext getContext(String fqcn, ClassLoader loader, boolean currentContext, URI configLocation) {
/* 46 */     LoggerContext ctx = ContextAnchor.THREAD_CONTEXT.get();
/* 47 */     return (ctx != null) ? ctx : CONTEXT;
/*    */   }
/*    */   
/*    */   public LoggerContext locateContext(String name, String configLocation) {
/* 51 */     return CONTEXT;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void removeContext(LoggerContext context) {}
/*    */ 
/*    */   
/*    */   public List<LoggerContext> getLoggerContexts() {
/* 60 */     List<LoggerContext> list = new ArrayList<LoggerContext>();
/* 61 */     list.add(CONTEXT);
/* 62 */     return Collections.unmodifiableList(list);
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\selector\BasicContextSelector.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */