/*     */ package com.mojang.launcher.updater.download;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Queue;
/*     */ import java.util.concurrent.ConcurrentLinkedQueue;
/*     */ import java.util.concurrent.ThreadPoolExecutor;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import org.apache.commons.lang3.time.StopWatch;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class DownloadJob
/*     */ {
/*  17 */   private static final Logger LOGGER = LogManager.getLogger();
/*     */   private static final int MAX_ATTEMPTS_PER_FILE = 5;
/*     */   private static final int ASSUMED_AVERAGE_FILE_SIZE = 5242880;
/*  20 */   private final Queue<Downloadable> remainingFiles = new ConcurrentLinkedQueue<>();
/*  21 */   private final List<Downloadable> allFiles = Collections.synchronizedList(new ArrayList<>());
/*  22 */   private final List<Downloadable> failures = Collections.synchronizedList(new ArrayList<>());
/*  23 */   private final List<Downloadable> successful = Collections.synchronizedList(new ArrayList<>());
/*     */   private final DownloadListener listener;
/*     */   private final String name;
/*     */   private final boolean ignoreFailures;
/*  27 */   private final AtomicInteger remainingThreads = new AtomicInteger();
/*  28 */   private final StopWatch stopWatch = new StopWatch();
/*     */   
/*     */   private boolean started;
/*     */   
/*     */   public DownloadJob(String name, boolean ignoreFailures, DownloadListener listener, Collection<Downloadable> files) {
/*  33 */     this.name = name;
/*  34 */     this.ignoreFailures = ignoreFailures;
/*  35 */     this.listener = listener;
/*  36 */     if (files != null) {
/*  37 */       addDownloadables(files);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public DownloadJob(String name, boolean ignoreFailures, DownloadListener listener) {
/*  43 */     this(name, ignoreFailures, listener, null);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addDownloadables(Collection<Downloadable> downloadables) {
/*  48 */     if (this.started) {
/*  49 */       throw new IllegalStateException("Cannot add to download job that has already started");
/*     */     }
/*  51 */     this.allFiles.addAll(downloadables);
/*  52 */     this.remainingFiles.addAll(downloadables);
/*  53 */     for (Downloadable downloadable : downloadables) {
/*     */       
/*  55 */       if (downloadable.getExpectedSize() == 0L) {
/*  56 */         downloadable.getMonitor().setTotal(5242880L);
/*     */       } else {
/*  58 */         downloadable.getMonitor().setTotal(downloadable.getExpectedSize());
/*     */       } 
/*  60 */       downloadable.getMonitor().setJob(this);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void addDownloadables(Downloadable... downloadables) {
/*  66 */     if (this.started) {
/*  67 */       throw new IllegalStateException("Cannot add to download job that has already started");
/*     */     }
/*  69 */     for (Downloadable downloadable : downloadables) {
/*     */       
/*  71 */       this.allFiles.add(downloadable);
/*  72 */       this.remainingFiles.add(downloadable);
/*  73 */       if (downloadable.getExpectedSize() == 0L) {
/*  74 */         downloadable.getMonitor().setTotal(5242880L);
/*     */       } else {
/*  76 */         downloadable.getMonitor().setTotal(downloadable.getExpectedSize());
/*     */       } 
/*  78 */       downloadable.getMonitor().setJob(this);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void startDownloading(ThreadPoolExecutor executorService) {
/*  84 */     if (this.started) {
/*  85 */       throw new IllegalStateException("Cannot start download job that has already started");
/*     */     }
/*  87 */     this.started = true;
/*  88 */     this.stopWatch.start();
/*  89 */     if (this.allFiles.isEmpty()) {
/*     */       
/*  91 */       LOGGER.info("Download job '" + this.name + "' skipped as there are no files to download");
/*  92 */       this.listener.onDownloadJobFinished(this);
/*     */     }
/*     */     else {
/*     */       
/*  96 */       int threads = executorService.getMaximumPoolSize();
/*  97 */       this.remainingThreads.set(threads);
/*  98 */       LOGGER.info("Download job '" + this.name + "' started (" + threads + " threads, " + this.allFiles.size() + " files)");
/*  99 */       for (int i = 0; i < threads; i++) {
/* 100 */         executorService.submit(new Runnable()
/*     */             {
/*     */               public void run()
/*     */               {
/* 104 */                 DownloadJob.this.popAndDownload();
/*     */               }
/*     */             });
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void popAndDownload() {
/*     */     Downloadable downloadable;
/* 114 */     while ((downloadable = this.remainingFiles.poll()) != null) {
/*     */       
/* 116 */       if (downloadable.getStartTime() == 0L) {
/* 117 */         downloadable.setStartTime(System.currentTimeMillis());
/*     */       }
/* 119 */       if (downloadable.getNumAttempts() > 5) {
/*     */         
/* 121 */         if (!this.ignoreFailures) {
/* 122 */           this.failures.add(downloadable);
/*     */         }
/* 124 */         LOGGER.error("Gave up trying to download " + downloadable.getUrl() + " for job '" + this.name + "'");
/*     */         
/*     */         continue;
/*     */       } 
/*     */       
/*     */       try {
/* 130 */         LOGGER.info("Attempting to download " + downloadable.getTarget() + " for job '" + this.name + "'... (try " + downloadable.getNumAttempts() + ")");
/* 131 */         String result = downloadable.download();
/* 132 */         this.successful.add(downloadable);
/* 133 */         downloadable.setEndTime(System.currentTimeMillis());
/* 134 */         downloadable.getMonitor().setCurrent(downloadable.getMonitor().getTotal());
/* 135 */         LOGGER.info("Finished downloading " + downloadable.getTarget() + " for job '" + this.name + "': " + result);
/*     */       }
/* 137 */       catch (Throwable t) {
/*     */         
/* 139 */         LOGGER.warn("Couldn't download " + downloadable.getUrl() + " for job '" + this.name + "'", t);
/* 140 */         downloadable.getMonitor().setCurrent(downloadable.getMonitor().getTotal());
/* 141 */         this.remainingFiles.add(downloadable);
/*     */       } 
/*     */     } 
/*     */     
/* 145 */     if (this.remainingThreads.decrementAndGet() <= 0) {
/* 146 */       this.listener.onDownloadJobFinished(this);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldIgnoreFailures() {
/* 152 */     return this.ignoreFailures;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isStarted() {
/* 157 */     return this.started;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isComplete() {
/* 162 */     return (this.started && this.remainingFiles.isEmpty() && this.remainingThreads.get() == 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getFailures() {
/* 167 */     return this.failures.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSuccessful() {
/* 172 */     return this.successful.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getName() {
/* 177 */     return this.name;
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateProgress() {
/* 182 */     this.listener.onDownloadJobProgressChanged(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<Downloadable> getAllFiles() {
/* 187 */     return this.allFiles;
/*     */   }
/*     */ 
/*     */   
/*     */   public StopWatch getStopWatch() {
/* 192 */     return this.stopWatch;
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\mojang\launche\\updater\download\DownloadJob.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */