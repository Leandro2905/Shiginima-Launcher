/*     */ package org.apache.logging.log4j.core.config;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import java.util.concurrent.locks.Lock;
/*     */ import java.util.concurrent.locks.ReentrantLock;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FileConfigurationMonitor
/*     */   implements ConfigurationMonitor
/*     */ {
/*     */   private static final int MASK = 15;
/*     */   private static final int MIN_INTERVAL = 5;
/*     */   private static final int MILLIS_PER_SECOND = 1000;
/*     */   private final File file;
/*     */   private long lastModified;
/*     */   private final List<ConfigurationListener> listeners;
/*     */   private final int interval;
/*     */   private long nextCheck;
/*  47 */   private final AtomicInteger counter = new AtomicInteger(0);
/*     */   
/*  49 */   private static final Lock LOCK = new ReentrantLock();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final Reconfigurable reconfigurable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FileConfigurationMonitor(Reconfigurable reconfigurable, File file, List<ConfigurationListener> listeners, int interval) {
/*  63 */     this.reconfigurable = reconfigurable;
/*  64 */     this.file = file;
/*  65 */     this.lastModified = file.lastModified();
/*  66 */     this.listeners = listeners;
/*  67 */     this.interval = ((interval < 5) ? 5 : interval) * 1000;
/*  68 */     this.nextCheck = System.currentTimeMillis() + interval;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void checkConfiguration() {
/*  76 */     long current = System.currentTimeMillis();
/*  77 */     if ((this.counter.incrementAndGet() & 0xF) == 0 && current >= this.nextCheck) {
/*  78 */       LOCK.lock();
/*     */       try {
/*  80 */         this.nextCheck = current + this.interval;
/*  81 */         if (this.file.lastModified() > this.lastModified) {
/*  82 */           this.lastModified = this.file.lastModified();
/*  83 */           for (ConfigurationListener listener : this.listeners) {
/*  84 */             Thread thread = new Thread(new ReconfigurationWorker(listener, this.reconfigurable));
/*  85 */             thread.setDaemon(true);
/*  86 */             thread.start();
/*     */           } 
/*     */         } 
/*     */       } finally {
/*  90 */         LOCK.unlock();
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private class ReconfigurationWorker
/*     */     implements Runnable {
/*     */     private final ConfigurationListener listener;
/*     */     private final Reconfigurable reconfigurable;
/*     */     
/*     */     public ReconfigurationWorker(ConfigurationListener listener, Reconfigurable reconfigurable) {
/* 101 */       this.listener = listener;
/* 102 */       this.reconfigurable = reconfigurable;
/*     */     }
/*     */ 
/*     */     
/*     */     public void run() {
/* 107 */       this.listener.onChange(this.reconfigurable);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\config\FileConfigurationMonitor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */