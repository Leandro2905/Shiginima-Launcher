/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.util.concurrent.Executors;
/*     */ import java.util.concurrent.ThreadFactory;
/*     */ import java.util.concurrent.atomic.AtomicLong;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ThreadFactoryBuilder
/*     */ {
/*  46 */   private String nameFormat = null;
/*  47 */   private Boolean daemon = null;
/*  48 */   private Integer priority = null;
/*  49 */   private Thread.UncaughtExceptionHandler uncaughtExceptionHandler = null;
/*  50 */   private ThreadFactory backingThreadFactory = null;
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
/*     */   public ThreadFactoryBuilder setNameFormat(String nameFormat) {
/*  71 */     String.format(nameFormat, new Object[] { Integer.valueOf(0) });
/*  72 */     this.nameFormat = nameFormat;
/*  73 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ThreadFactoryBuilder setDaemon(boolean daemon) {
/*  84 */     this.daemon = Boolean.valueOf(daemon);
/*  85 */     return this;
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
/*     */   public ThreadFactoryBuilder setPriority(int priority) {
/*  98 */     Preconditions.checkArgument((priority >= 1), "Thread priority (%s) must be >= %s", new Object[] { Integer.valueOf(priority), Integer.valueOf(1) });
/*     */     
/* 100 */     Preconditions.checkArgument((priority <= 10), "Thread priority (%s) must be <= %s", new Object[] { Integer.valueOf(priority), Integer.valueOf(10) });
/*     */     
/* 102 */     this.priority = Integer.valueOf(priority);
/* 103 */     return this;
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
/*     */   public ThreadFactoryBuilder setUncaughtExceptionHandler(Thread.UncaughtExceptionHandler uncaughtExceptionHandler) {
/* 116 */     this.uncaughtExceptionHandler = (Thread.UncaughtExceptionHandler)Preconditions.checkNotNull(uncaughtExceptionHandler);
/* 117 */     return this;
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
/*     */ 
/*     */   
/*     */   public ThreadFactoryBuilder setThreadFactory(ThreadFactory backingThreadFactory) {
/* 133 */     this.backingThreadFactory = (ThreadFactory)Preconditions.checkNotNull(backingThreadFactory);
/* 134 */     return this;
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
/*     */   public ThreadFactory build() {
/* 146 */     return build(this);
/*     */   }
/*     */   
/*     */   private static ThreadFactory build(ThreadFactoryBuilder builder) {
/* 150 */     final String nameFormat = builder.nameFormat;
/* 151 */     final Boolean daemon = builder.daemon;
/* 152 */     final Integer priority = builder.priority;
/* 153 */     final Thread.UncaughtExceptionHandler uncaughtExceptionHandler = builder.uncaughtExceptionHandler;
/*     */     
/* 155 */     final ThreadFactory backingThreadFactory = (builder.backingThreadFactory != null) ? builder.backingThreadFactory : Executors.defaultThreadFactory();
/*     */ 
/*     */ 
/*     */     
/* 159 */     final AtomicLong count = (nameFormat != null) ? new AtomicLong(0L) : null;
/* 160 */     return new ThreadFactory() {
/*     */         public Thread newThread(Runnable runnable) {
/* 162 */           Thread thread = backingThreadFactory.newThread(runnable);
/* 163 */           if (nameFormat != null) {
/* 164 */             thread.setName(String.format(nameFormat, new Object[] { Long.valueOf(this.val$count.getAndIncrement()) }));
/*     */           }
/* 166 */           if (daemon != null) {
/* 167 */             thread.setDaemon(daemon.booleanValue());
/*     */           }
/* 169 */           if (priority != null) {
/* 170 */             thread.setPriority(priority.intValue());
/*     */           }
/* 172 */           if (uncaughtExceptionHandler != null) {
/* 173 */             thread.setUncaughtExceptionHandler(uncaughtExceptionHandler);
/*     */           }
/* 175 */           return thread;
/*     */         }
/*     */       };
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\commo\\util\concurrent\ThreadFactoryBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */