/*    */ package org.jivesoftware.smack.util;
/*    */ 
/*    */ import java.util.concurrent.ThreadFactory;
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
/*    */ public final class SmackExecutorThreadFactory
/*    */   implements ThreadFactory
/*    */ {
/*    */   private final int connectionCounterValue;
/*    */   private final String name;
/* 28 */   private int count = 0;
/*    */   
/*    */   public SmackExecutorThreadFactory(int connectionCounterValue, String name) {
/* 31 */     this.connectionCounterValue = connectionCounterValue;
/* 32 */     this.name = name;
/*    */   }
/*    */ 
/*    */   
/*    */   public Thread newThread(Runnable runnable) {
/* 37 */     Thread thread = new Thread(runnable);
/* 38 */     thread.setName("Smack-" + this.name + ' ' + this.count++ + " (" + this.connectionCounterValue + ")");
/* 39 */     thread.setDaemon(true);
/* 40 */     return thread;
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jivesoftware\smac\\util\SmackExecutorThreadFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */