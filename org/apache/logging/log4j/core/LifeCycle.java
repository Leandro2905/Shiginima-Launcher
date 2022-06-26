/*    */ package org.apache.logging.log4j.core;
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
/*    */ public interface LifeCycle
/*    */ {
/*    */   void start();
/*    */   
/*    */   void stop();
/*    */   
/*    */   boolean isStarted();
/*    */   
/*    */   boolean isStopped();
/*    */   
/*    */   public enum State
/*    */   {
/* 30 */     INITIALIZED,
/*    */     
/* 32 */     STARTING,
/*    */     
/* 34 */     STARTED,
/*    */     
/* 36 */     STOPPING,
/*    */     
/* 38 */     STOPPED;
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\LifeCycle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */