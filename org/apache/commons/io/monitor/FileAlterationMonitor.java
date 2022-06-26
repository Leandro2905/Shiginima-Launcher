/*     */ package org.apache.commons.io.monitor;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.concurrent.CopyOnWriteArrayList;
/*     */ import java.util.concurrent.ThreadFactory;
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
/*     */ public final class FileAlterationMonitor
/*     */   implements Runnable
/*     */ {
/*     */   private final long interval;
/*  34 */   private final List<FileAlterationObserver> observers = new CopyOnWriteArrayList<FileAlterationObserver>();
/*  35 */   private Thread thread = null;
/*     */   
/*     */   private ThreadFactory threadFactory;
/*     */   
/*     */   private volatile boolean running = false;
/*     */ 
/*     */   
/*     */   public FileAlterationMonitor() {
/*  43 */     this(10000L);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FileAlterationMonitor(long interval) {
/*  53 */     this.interval = interval;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FileAlterationMonitor(long interval, FileAlterationObserver... observers) {
/*  64 */     this(interval);
/*  65 */     if (observers != null) {
/*  66 */       for (FileAlterationObserver observer : observers) {
/*  67 */         addObserver(observer);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getInterval() {
/*  78 */     return this.interval;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void setThreadFactory(ThreadFactory threadFactory) {
/*  87 */     this.threadFactory = threadFactory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addObserver(FileAlterationObserver observer) {
/*  96 */     if (observer != null) {
/*  97 */       this.observers.add(observer);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeObserver(FileAlterationObserver observer) {
/* 107 */     if (observer != null) {
/* 108 */       while (this.observers.remove(observer));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Iterable<FileAlterationObserver> getObservers() {
/* 120 */     return this.observers;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void start() throws Exception {
/* 129 */     if (this.running) {
/* 130 */       throw new IllegalStateException("Monitor is already running");
/*     */     }
/* 132 */     for (FileAlterationObserver observer : this.observers) {
/* 133 */       observer.initialize();
/*     */     }
/* 135 */     this.running = true;
/* 136 */     if (this.threadFactory != null) {
/* 137 */       this.thread = this.threadFactory.newThread(this);
/*     */     } else {
/* 139 */       this.thread = new Thread(this);
/*     */     } 
/* 141 */     this.thread.start();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void stop() throws Exception {
/* 150 */     stop(this.interval);
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
/*     */   public synchronized void stop(long stopInterval) throws Exception {
/* 162 */     if (!this.running) {
/* 163 */       throw new IllegalStateException("Monitor is not running");
/*     */     }
/* 165 */     this.running = false;
/*     */     try {
/* 167 */       this.thread.join(stopInterval);
/* 168 */     } catch (InterruptedException e) {
/* 169 */       Thread.currentThread().interrupt();
/*     */     } 
/* 171 */     for (FileAlterationObserver observer : this.observers) {
/* 172 */       observer.destroy();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void run() {
/* 180 */     while (this.running) {
/* 181 */       for (FileAlterationObserver observer : this.observers) {
/* 182 */         observer.checkAndNotify();
/*     */       }
/* 184 */       if (!this.running) {
/*     */         break;
/*     */       }
/*     */       try {
/* 188 */         Thread.sleep(this.interval);
/* 189 */       } catch (InterruptedException ignored) {}
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\commons\io\monitor\FileAlterationMonitor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */