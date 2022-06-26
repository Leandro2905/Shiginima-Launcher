/*    */ package org.apache.logging.log4j.core.async;
/*    */ 
/*    */ import java.util.concurrent.ThreadFactory;
/*    */ import java.util.concurrent.atomic.AtomicInteger;
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
/*    */ public class DaemonThreadFactory
/*    */   implements ThreadFactory
/*    */ {
/*    */   final ThreadGroup group;
/* 28 */   final AtomicInteger threadNumber = new AtomicInteger(1);
/*    */   final String threadNamePrefix;
/*    */   
/*    */   public DaemonThreadFactory(String threadNamePrefix) {
/* 32 */     this.threadNamePrefix = threadNamePrefix;
/* 33 */     SecurityManager securityManager = System.getSecurityManager();
/* 34 */     this.group = (securityManager != null) ? securityManager.getThreadGroup() : Thread.currentThread().getThreadGroup();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Thread newThread(Runnable runnable) {
/* 40 */     Thread thread = new Thread(this.group, runnable, this.threadNamePrefix + this.threadNumber.getAndIncrement(), 0L);
/*    */     
/* 42 */     if (!thread.isDaemon()) {
/* 43 */       thread.setDaemon(true);
/*    */     }
/* 45 */     if (thread.getPriority() != 5) {
/* 46 */       thread.setPriority(5);
/*    */     }
/* 48 */     return thread;
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\async\DaemonThreadFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */