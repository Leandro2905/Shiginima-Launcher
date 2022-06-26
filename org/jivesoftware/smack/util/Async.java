/*    */ package org.jivesoftware.smack.util;
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
/*    */ public class Async
/*    */ {
/*    */   public static Thread go(Runnable runnable) {
/* 28 */     Thread thread = daemonThreadFrom(runnable);
/* 29 */     thread.start();
/* 30 */     return thread;
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
/*    */   public static Thread go(Runnable runnable, String threadName) {
/* 42 */     Thread thread = daemonThreadFrom(runnable);
/* 43 */     thread.setName(threadName);
/* 44 */     thread.start();
/* 45 */     return thread;
/*    */   }
/*    */   
/*    */   public static Thread daemonThreadFrom(Runnable runnable) {
/* 49 */     Thread thread = new Thread(runnable);
/* 50 */     thread.setDaemon(true);
/* 51 */     return thread;
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jivesoftware\smac\\util\Async.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */