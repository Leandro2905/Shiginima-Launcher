/*    */ package org.apache.logging.log4j.core.util;
/*    */ 
/*    */ import java.util.concurrent.locks.LockSupport;
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
/*    */ public final class CoarseCachedClock
/*    */   implements Clock
/*    */ {
/* 26 */   private static final CoarseCachedClock instance = new CoarseCachedClock();
/*    */   
/* 28 */   private volatile long millis = System.currentTimeMillis();
/*    */   
/* 30 */   private final Thread updater = new Thread("Clock Updater Thread")
/*    */     {
/*    */       public void run() {
/*    */         while (true) {
/* 34 */           CoarseCachedClock.this.millis = System.currentTimeMillis();
/*    */ 
/*    */           
/* 37 */           LockSupport.parkNanos(1000000L);
/*    */         } 
/*    */       }
/*    */     };
/*    */   
/*    */   private CoarseCachedClock() {
/* 43 */     this.updater.setDaemon(true);
/* 44 */     this.updater.start();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static CoarseCachedClock instance() {
/* 53 */     return instance;
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
/*    */   public long currentTimeMillis() {
/* 65 */     return this.millis;
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\cor\\util\CoarseCachedClock.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */