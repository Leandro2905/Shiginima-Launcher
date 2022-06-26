/*    */ package org.apache.logging.log4j.core;
/*    */ 
/*    */ import org.apache.logging.log4j.Logger;
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
/*    */ 
/*    */ public class AbstractLifeCycle
/*    */   implements LifeCycle
/*    */ {
/* 32 */   protected static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*    */   
/* 34 */   private volatile LifeCycle.State state = LifeCycle.State.INITIALIZED;
/*    */   
/*    */   public LifeCycle.State getState() {
/* 37 */     return this.state;
/*    */   }
/*    */   
/*    */   public boolean isInitialized() {
/* 41 */     return (this.state == LifeCycle.State.INITIALIZED);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isStarted() {
/* 46 */     return (this.state == LifeCycle.State.STARTED);
/*    */   }
/*    */   
/*    */   public boolean isStarting() {
/* 50 */     return (this.state == LifeCycle.State.STARTING);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isStopped() {
/* 55 */     return (this.state == LifeCycle.State.STOPPED);
/*    */   }
/*    */   
/*    */   public boolean isStopping() {
/* 59 */     return (this.state == LifeCycle.State.STOPPING);
/*    */   }
/*    */   
/*    */   protected void setStarted() {
/* 63 */     setState(LifeCycle.State.STARTED);
/*    */   }
/*    */   
/*    */   protected void setStarting() {
/* 67 */     setState(LifeCycle.State.STARTING);
/*    */   }
/*    */   
/*    */   protected void setState(LifeCycle.State newState) {
/* 71 */     this.state = newState;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void setStopped() {
/* 77 */     setState(LifeCycle.State.STOPPED);
/*    */   }
/*    */   
/*    */   protected void setStopping() {
/* 81 */     setState(LifeCycle.State.STOPPING);
/*    */   }
/*    */ 
/*    */   
/*    */   public void start() {
/* 86 */     setStarted();
/*    */   }
/*    */ 
/*    */   
/*    */   public void stop() {
/* 91 */     this.state = LifeCycle.State.STOPPED;
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\AbstractLifeCycle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */