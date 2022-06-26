/*     */ package org.apache.commons.lang3.concurrent;
/*     */ 
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
/*     */ public class BasicThreadFactory
/*     */   implements ThreadFactory
/*     */ {
/*     */   private final AtomicLong threadCounter;
/*     */   private final ThreadFactory wrappedFactory;
/*     */   private final Thread.UncaughtExceptionHandler uncaughtExceptionHandler;
/*     */   private final String namingPattern;
/*     */   private final Integer priority;
/*     */   private final Boolean daemonFlag;
/*     */   
/*     */   private BasicThreadFactory(Builder builder) {
/* 116 */     if (builder.wrappedFactory == null) {
/* 117 */       this.wrappedFactory = Executors.defaultThreadFactory();
/*     */     } else {
/* 119 */       this.wrappedFactory = builder.wrappedFactory;
/*     */     } 
/*     */     
/* 122 */     this.namingPattern = builder.namingPattern;
/* 123 */     this.priority = builder.priority;
/* 124 */     this.daemonFlag = builder.daemonFlag;
/* 125 */     this.uncaughtExceptionHandler = builder.exceptionHandler;
/*     */     
/* 127 */     this.threadCounter = new AtomicLong();
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
/*     */   public final ThreadFactory getWrappedFactory() {
/* 139 */     return this.wrappedFactory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String getNamingPattern() {
/* 149 */     return this.namingPattern;
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
/*     */   public final Boolean getDaemonFlag() {
/* 161 */     return this.daemonFlag;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Integer getPriority() {
/* 171 */     return this.priority;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Thread.UncaughtExceptionHandler getUncaughtExceptionHandler() {
/* 181 */     return this.uncaughtExceptionHandler;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getThreadCount() {
/* 192 */     return this.threadCounter.get();
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
/*     */   public Thread newThread(Runnable r) {
/* 205 */     Thread t = getWrappedFactory().newThread(r);
/* 206 */     initializeThread(t);
/*     */     
/* 208 */     return t;
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
/*     */   private void initializeThread(Thread t) {
/* 221 */     if (getNamingPattern() != null) {
/* 222 */       Long count = Long.valueOf(this.threadCounter.incrementAndGet());
/* 223 */       t.setName(String.format(getNamingPattern(), new Object[] { count }));
/*     */     } 
/*     */     
/* 226 */     if (getUncaughtExceptionHandler() != null) {
/* 227 */       t.setUncaughtExceptionHandler(getUncaughtExceptionHandler());
/*     */     }
/*     */     
/* 230 */     if (getPriority() != null) {
/* 231 */       t.setPriority(getPriority().intValue());
/*     */     }
/*     */     
/* 234 */     if (getDaemonFlag() != null) {
/* 235 */       t.setDaemon(getDaemonFlag().booleanValue());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class Builder
/*     */     implements org.apache.commons.lang3.builder.Builder<BasicThreadFactory>
/*     */   {
/*     */     private ThreadFactory wrappedFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private Thread.UncaughtExceptionHandler exceptionHandler;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private String namingPattern;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private Integer priority;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private Boolean daemonFlag;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder wrappedFactory(ThreadFactory factory) {
/* 283 */       if (factory == null) {
/* 284 */         throw new NullPointerException("Wrapped ThreadFactory must not be null!");
/*     */       }
/*     */ 
/*     */       
/* 288 */       this.wrappedFactory = factory;
/* 289 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder namingPattern(String pattern) {
/* 301 */       if (pattern == null) {
/* 302 */         throw new NullPointerException("Naming pattern must not be null!");
/*     */       }
/*     */ 
/*     */       
/* 306 */       this.namingPattern = pattern;
/* 307 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder daemon(boolean f) {
/* 319 */       this.daemonFlag = Boolean.valueOf(f);
/* 320 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder priority(int prio) {
/* 331 */       this.priority = Integer.valueOf(prio);
/* 332 */       return this;
/*     */     }
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
/*     */     public Builder uncaughtExceptionHandler(Thread.UncaughtExceptionHandler handler) {
/* 346 */       if (handler == null) {
/* 347 */         throw new NullPointerException("Uncaught exception handler must not be null!");
/*     */       }
/*     */ 
/*     */       
/* 351 */       this.exceptionHandler = handler;
/* 352 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void reset() {
/* 362 */       this.wrappedFactory = null;
/* 363 */       this.exceptionHandler = null;
/* 364 */       this.namingPattern = null;
/* 365 */       this.priority = null;
/* 366 */       this.daemonFlag = null;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public BasicThreadFactory build() {
/* 378 */       BasicThreadFactory factory = new BasicThreadFactory(this);
/* 379 */       reset();
/* 380 */       return factory;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\commons\lang3\concurrent\BasicThreadFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */