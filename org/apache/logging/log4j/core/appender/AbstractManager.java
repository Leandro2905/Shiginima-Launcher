/*     */ package org.apache.logging.log4j.core.appender;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.locks.Lock;
/*     */ import java.util.concurrent.locks.ReentrantLock;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.status.StatusLogger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractManager
/*     */ {
/*  35 */   protected static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*     */ 
/*     */ 
/*     */   
/*  39 */   private static final Map<String, AbstractManager> MAP = new HashMap<String, AbstractManager>();
/*     */   
/*  41 */   private static final Lock LOCK = new ReentrantLock();
/*     */ 
/*     */   
/*     */   protected int count;
/*     */ 
/*     */   
/*     */   private final String name;
/*     */ 
/*     */   
/*     */   protected AbstractManager(String name) {
/*  51 */     this.name = name;
/*  52 */     LOGGER.debug("Starting {} {}", new Object[] { getClass().getSimpleName(), name });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <M extends AbstractManager, T> M getManager(String name, ManagerFactory<M, T> factory, T data) {
/*  66 */     LOCK.lock();
/*     */     
/*     */     try {
/*  69 */       AbstractManager abstractManager = MAP.get(name);
/*  70 */       if (abstractManager == null) {
/*  71 */         abstractManager = (AbstractManager)factory.createManager(name, data);
/*  72 */         if (abstractManager == null) {
/*  73 */           throw new IllegalStateException("Unable to create a manager");
/*     */         }
/*  75 */         MAP.put(name, abstractManager);
/*     */       } 
/*  77 */       abstractManager.count++;
/*  78 */       return (M)abstractManager;
/*     */     } finally {
/*  80 */       LOCK.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean hasManager(String name) {
/*  90 */     LOCK.lock();
/*     */     try {
/*  92 */       return MAP.containsKey(name);
/*     */     } finally {
/*  94 */       LOCK.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void releaseSub() {}
/*     */ 
/*     */ 
/*     */   
/*     */   protected int getCount() {
/* 106 */     return this.count;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void release() {
/* 113 */     LOCK.lock();
/*     */     try {
/* 115 */       this.count--;
/* 116 */       if (this.count <= 0) {
/* 117 */         MAP.remove(this.name);
/* 118 */         LOGGER.debug("Shutting down {} {}", new Object[] { getClass().getSimpleName(), getName() });
/* 119 */         releaseSub();
/*     */       } 
/*     */     } finally {
/* 122 */       LOCK.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/* 131 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, String> getContentFormat() {
/* 142 */     return new HashMap<String, String>();
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\appender\AbstractManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */