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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class CachedClock
/*    */   implements Clock
/*    */ {
/*    */   private static final int UPDATE_THRESHOLD = 1023;
/* 31 */   private static final CachedClock instance = new CachedClock();
/* 32 */   private volatile long millis = System.currentTimeMillis();
/* 33 */   private volatile short count = 0;
/*    */   
/*    */   private CachedClock() {
/* 36 */     Thread updater = new Thread(new Runnable()
/*    */         {
/*    */           public void run() {
/*    */             while (true) {
/* 40 */               long time = System.currentTimeMillis();
/* 41 */               CachedClock.this.millis = time;
/*    */ 
/*    */               
/* 44 */               LockSupport.parkNanos(1000000L);
/*    */             } 
/*    */           }
/*    */         },  "Clock Updater Thread");
/* 48 */     updater.setDaemon(true);
/* 49 */     updater.start();
/*    */   }
/*    */   
/*    */   public static CachedClock instance() {
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
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public long currentTimeMillis() {
/* 69 */     if (((this.count = (short)(this.count + 1)) & 0x3FF) == 1023) {
/* 70 */       this.millis = System.currentTimeMillis();
/*    */     }
/* 72 */     return this.millis;
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\cor\\util\CachedClock.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */