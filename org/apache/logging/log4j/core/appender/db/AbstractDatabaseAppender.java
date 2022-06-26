/*     */ package org.apache.logging.log4j.core.appender.db;
/*     */ 
/*     */ import java.util.concurrent.locks.Lock;
/*     */ import java.util.concurrent.locks.ReadWriteLock;
/*     */ import java.util.concurrent.locks.ReentrantReadWriteLock;
/*     */ import org.apache.logging.log4j.LoggingException;
/*     */ import org.apache.logging.log4j.core.Filter;
/*     */ import org.apache.logging.log4j.core.Layout;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.appender.AbstractAppender;
/*     */ import org.apache.logging.log4j.core.appender.AppenderLoggingException;
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
/*     */ public abstract class AbstractDatabaseAppender<T extends AbstractDatabaseManager>
/*     */   extends AbstractAppender
/*     */ {
/*  39 */   private final ReadWriteLock lock = new ReentrantReadWriteLock();
/*  40 */   private final Lock readLock = this.lock.readLock();
/*  41 */   private final Lock writeLock = this.lock.writeLock();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private T manager;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected AbstractDatabaseAppender(String name, Filter filter, boolean ignoreExceptions, T manager) {
/*  56 */     super(name, filter, null, ignoreExceptions);
/*  57 */     this.manager = manager;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Layout<LogEvent> getLayout() {
/*  68 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final T getManager() {
/*  77 */     return this.manager;
/*     */   }
/*     */ 
/*     */   
/*     */   public final void start() {
/*  82 */     if (getManager() == null) {
/*  83 */       LOGGER.error("No AbstractDatabaseManager set for the appender named [{}].", new Object[] { getName() });
/*     */     }
/*  85 */     super.start();
/*  86 */     if (getManager() != null) {
/*  87 */       getManager().startup();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public final void stop() {
/*  93 */     super.stop();
/*  94 */     if (getManager() != null) {
/*  95 */       getManager().release();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public final void append(LogEvent event) {
/* 101 */     this.readLock.lock();
/*     */     try {
/* 103 */       getManager().write(event);
/* 104 */     } catch (LoggingException e) {
/* 105 */       LOGGER.error("Unable to write to database [{}] for appender [{}].", new Object[] { getManager().getName(), getName(), e });
/*     */       
/* 107 */       throw e;
/* 108 */     } catch (Exception e) {
/* 109 */       LOGGER.error("Unable to write to database [{}] for appender [{}].", new Object[] { getManager().getName(), getName(), e });
/*     */       
/* 111 */       throw new AppenderLoggingException("Unable to write to database in appender: " + e.getMessage(), e);
/*     */     } finally {
/* 113 */       this.readLock.unlock();
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
/*     */   protected final void replaceManager(T manager) {
/* 125 */     this.writeLock.lock();
/*     */     try {
/* 127 */       T old = getManager();
/* 128 */       if (!manager.isRunning()) {
/* 129 */         manager.startup();
/*     */       }
/* 131 */       this.manager = manager;
/* 132 */       old.release();
/*     */     } finally {
/* 134 */       this.writeLock.unlock();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\appender\db\AbstractDatabaseAppender.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */