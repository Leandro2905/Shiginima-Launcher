/*     */ package org.apache.logging.log4j.core.appender.db;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.appender.AbstractManager;
/*     */ import org.apache.logging.log4j.core.appender.ManagerFactory;
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
/*     */ public abstract class AbstractDatabaseManager
/*     */   extends AbstractManager
/*     */ {
/*     */   private final ArrayList<LogEvent> buffer;
/*     */   private final int bufferSize;
/*     */   private boolean running = false;
/*     */   
/*     */   protected AbstractDatabaseManager(String name, int bufferSize) {
/*  42 */     super(name);
/*  43 */     this.bufferSize = bufferSize;
/*  44 */     this.buffer = new ArrayList<LogEvent>(bufferSize + 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract void startupInternal() throws Exception;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final synchronized void startup() {
/*  60 */     if (!isRunning()) {
/*     */       try {
/*  62 */         startupInternal();
/*  63 */         this.running = true;
/*  64 */       } catch (Exception e) {
/*  65 */         LOGGER.error("Could not perform database startup operations using logging manager [{}].", new Object[] { getName(), e });
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract void shutdownInternal() throws Exception;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final synchronized void shutdown() {
/*  85 */     flush();
/*  86 */     if (isRunning()) {
/*     */       try {
/*  88 */         shutdownInternal();
/*  89 */       } catch (Exception e) {
/*  90 */         LOGGER.warn("Error while performing database shutdown operations using logging manager [{}].", new Object[] { getName(), e });
/*     */       } finally {
/*     */         
/*  93 */         this.running = false;
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean isRunning() {
/* 105 */     return this.running;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract void connectAndStart();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract void writeInternal(LogEvent paramLogEvent);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract void commitAndClose();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final synchronized void flush() {
/* 136 */     if (isRunning() && this.buffer.size() > 0) {
/* 137 */       connectAndStart();
/*     */       try {
/* 139 */         for (LogEvent event : this.buffer) {
/* 140 */           writeInternal(event);
/*     */         }
/*     */       } finally {
/* 143 */         commitAndClose();
/*     */         
/* 145 */         this.buffer.clear();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final synchronized void write(LogEvent event) {
/* 156 */     if (this.bufferSize > 0) {
/* 157 */       this.buffer.add(event);
/* 158 */       if (this.buffer.size() >= this.bufferSize || event.isEndOfBatch()) {
/* 159 */         flush();
/*     */       }
/*     */     } else {
/* 162 */       connectAndStart();
/*     */       try {
/* 164 */         writeInternal(event);
/*     */       } finally {
/* 166 */         commitAndClose();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final void releaseSub() {
/* 173 */     shutdown();
/*     */   }
/*     */ 
/*     */   
/*     */   public final String toString() {
/* 178 */     return getName();
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
/*     */ 
/*     */   
/*     */   protected static <M extends AbstractDatabaseManager, T extends AbstractFactoryData> M getManager(String name, T data, ManagerFactory<M, T> factory) {
/* 196 */     return (M)AbstractManager.getManager(name, factory, data);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static abstract class AbstractFactoryData
/*     */   {
/*     */     private final int bufferSize;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected AbstractFactoryData(int bufferSize) {
/* 212 */       this.bufferSize = bufferSize;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int getBufferSize() {
/* 221 */       return this.bufferSize;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\appender\db\AbstractDatabaseManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */