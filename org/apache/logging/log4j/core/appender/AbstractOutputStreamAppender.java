/*     */ package org.apache.logging.log4j.core.appender;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.concurrent.locks.Lock;
/*     */ import java.util.concurrent.locks.ReadWriteLock;
/*     */ import java.util.concurrent.locks.ReentrantReadWriteLock;
/*     */ import org.apache.logging.log4j.core.Filter;
/*     */ import org.apache.logging.log4j.core.Layout;
/*     */ import org.apache.logging.log4j.core.LogEvent;
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
/*     */ public abstract class AbstractOutputStreamAppender<M extends OutputStreamManager>
/*     */   extends AbstractAppender
/*     */ {
/*     */   protected final boolean immediateFlush;
/*     */   private final M manager;
/*  48 */   private final ReadWriteLock rwLock = new ReentrantReadWriteLock();
/*  49 */   private final Lock readLock = this.rwLock.readLock();
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
/*     */   protected AbstractOutputStreamAppender(String name, Layout<? extends Serializable> layout, Filter filter, boolean ignoreExceptions, boolean immediateFlush, M manager) {
/*  62 */     super(name, filter, layout, ignoreExceptions);
/*  63 */     this.manager = manager;
/*  64 */     this.immediateFlush = immediateFlush;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public M getManager() {
/*  73 */     return this.manager;
/*     */   }
/*     */ 
/*     */   
/*     */   public void start() {
/*  78 */     if (getLayout() == null) {
/*  79 */       LOGGER.error("No layout set for the appender named [" + getName() + "].");
/*     */     }
/*  81 */     if (this.manager == null) {
/*  82 */       LOGGER.error("No OutputStreamManager set for the appender named [" + getName() + "].");
/*     */     }
/*  84 */     super.start();
/*     */   }
/*     */ 
/*     */   
/*     */   public void stop() {
/*  89 */     super.stop();
/*  90 */     this.manager.release();
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
/*     */   public void append(LogEvent event) {
/* 102 */     this.readLock.lock();
/*     */     try {
/* 104 */       byte[] bytes = getLayout().toByteArray(event);
/* 105 */       if (bytes.length > 0) {
/* 106 */         this.manager.write(bytes);
/* 107 */         if (this.immediateFlush || event.isEndOfBatch()) {
/* 108 */           this.manager.flush();
/*     */         }
/*     */       } 
/* 111 */     } catch (AppenderLoggingException ex) {
/* 112 */       error("Unable to write to stream " + this.manager.getName() + " for appender " + getName());
/* 113 */       throw ex;
/*     */     } finally {
/* 115 */       this.readLock.unlock();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\appender\AbstractOutputStreamAppender.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */