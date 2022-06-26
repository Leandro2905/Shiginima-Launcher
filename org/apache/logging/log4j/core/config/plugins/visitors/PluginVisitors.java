/*    */ package org.apache.logging.log4j.core.config.plugins.visitors;
/*    */ 
/*    */ import org.apache.logging.log4j.Logger;
/*    */ import org.apache.logging.log4j.core.config.plugins.PluginVisitorStrategy;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class PluginVisitors
/*    */ {
/* 31 */   private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
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
/*    */   public static <A extends java.lang.annotation.Annotation> PluginVisitor<A> findVisitor(Class<A> annotation) {
/* 47 */     PluginVisitorStrategy strategy = annotation.<PluginVisitorStrategy>getAnnotation(PluginVisitorStrategy.class);
/* 48 */     if (strategy == null) {
/* 49 */       LOGGER.debug("No PluginVisitorStrategy found on annotation [{}]. Ignoring.", new Object[] { annotation });
/* 50 */       return null;
/*    */     } 
/* 52 */     Class<? extends PluginVisitor<A>> visitorClass = strategy.value();
/*    */     try {
/* 54 */       return visitorClass.newInstance();
/* 55 */     } catch (Exception e) {
/* 56 */       LOGGER.error("Error loading PluginVisitor [{}] for annotation [{}].", new Object[] { visitorClass, annotation, e });
/* 57 */       return null;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\config\plugins\visitors\PluginVisitors.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */