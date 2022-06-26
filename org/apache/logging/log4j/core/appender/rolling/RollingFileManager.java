/*     */ package org.apache.logging.log4j.core.appender.rolling;
/*     */ 
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.concurrent.Semaphore;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.core.Layout;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.appender.FileManager;
/*     */ import org.apache.logging.log4j.core.appender.ManagerFactory;
/*     */ import org.apache.logging.log4j.core.appender.rolling.action.AbstractAction;
/*     */ import org.apache.logging.log4j.core.appender.rolling.action.Action;
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
/*     */ public class RollingFileManager
/*     */   extends FileManager
/*     */ {
/*  40 */   private static RollingFileManagerFactory factory = new RollingFileManagerFactory();
/*     */   
/*     */   private long size;
/*     */   private long initialTime;
/*     */   private final PatternProcessor patternProcessor;
/*  45 */   private final Semaphore semaphore = new Semaphore(1);
/*     */   
/*     */   private final TriggeringPolicy triggeringPolicy;
/*     */   
/*     */   private final RolloverStrategy rolloverStrategy;
/*     */ 
/*     */   
/*     */   protected RollingFileManager(String fileName, String pattern, OutputStream os, boolean append, long size, long time, TriggeringPolicy triggeringPolicy, RolloverStrategy rolloverStrategy, String advertiseURI, Layout<? extends Serializable> layout, int bufferSize) {
/*  53 */     super(fileName, os, append, false, advertiseURI, layout, bufferSize);
/*  54 */     this.size = size;
/*  55 */     this.initialTime = time;
/*  56 */     this.triggeringPolicy = triggeringPolicy;
/*  57 */     this.rolloverStrategy = rolloverStrategy;
/*  58 */     this.patternProcessor = new PatternProcessor(pattern);
/*  59 */     triggeringPolicy.initialize(this);
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
/*     */ 
/*     */   
/*     */   public static RollingFileManager getFileManager(String fileName, String pattern, boolean append, boolean bufferedIO, TriggeringPolicy policy, RolloverStrategy strategy, String advertiseURI, Layout<? extends Serializable> layout, int bufferSize) {
/*  79 */     return (RollingFileManager)getManager(fileName, new FactoryData(pattern, append, bufferedIO, policy, strategy, advertiseURI, layout, bufferSize), factory);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected synchronized void write(byte[] bytes, int offset, int length) {
/*  85 */     this.size += length;
/*  86 */     super.write(bytes, offset, length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getFileSize() {
/*  94 */     return this.size;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getFileTime() {
/* 102 */     return this.initialTime;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void checkRollover(LogEvent event) {
/* 110 */     if (this.triggeringPolicy.isTriggeringEvent(event) && rollover(this.rolloverStrategy)) {
/*     */       try {
/* 112 */         this.size = 0L;
/* 113 */         this.initialTime = System.currentTimeMillis();
/* 114 */         createFileAfterRollover();
/* 115 */       } catch (IOException ex) {
/* 116 */         LOGGER.error("FileManager (" + getFileName() + ") " + ex);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   protected void createFileAfterRollover() throws IOException {
/* 122 */     OutputStream os = new FileOutputStream(getFileName(), isAppend());
/* 123 */     if (getBufferSize() > 0) {
/* 124 */       setOutputStream(new BufferedOutputStream(os, getBufferSize()));
/*     */     } else {
/* 126 */       setOutputStream(os);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PatternProcessor getPatternProcessor() {
/* 135 */     return this.patternProcessor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TriggeringPolicy getTriggeringPolicy() {
/* 143 */     return this.triggeringPolicy;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RolloverStrategy getRolloverStrategy() {
/* 151 */     return this.rolloverStrategy;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean rollover(RolloverStrategy strategy) {
/*     */     try {
/* 158 */       this.semaphore.acquire();
/* 159 */     } catch (InterruptedException ie) {
/* 160 */       LOGGER.error("Thread interrupted while attempting to check rollover", ie);
/* 161 */       return false;
/*     */     } 
/*     */     
/* 164 */     boolean success = false;
/* 165 */     Thread thread = null;
/*     */     
/*     */     try {
/* 168 */       RolloverDescription descriptor = strategy.rollover(this);
/* 169 */       if (descriptor != null) {
/* 170 */         writeFooter();
/* 171 */         close();
/* 172 */         if (descriptor.getSynchronous() != null) {
/* 173 */           LOGGER.debug("RollingFileManager executing synchronous {}", new Object[] { descriptor.getSynchronous() });
/*     */           try {
/* 175 */             success = descriptor.getSynchronous().execute();
/* 176 */           } catch (Exception ex) {
/* 177 */             LOGGER.error("Error in synchronous task", ex);
/*     */           } 
/*     */         } 
/*     */         
/* 181 */         if (success && descriptor.getAsynchronous() != null) {
/* 182 */           LOGGER.debug("RollingFileManager executing async {}", new Object[] { descriptor.getAsynchronous() });
/* 183 */           thread = new Thread((Runnable)new AsyncAction(descriptor.getAsynchronous(), this));
/* 184 */           thread.start();
/*     */         } 
/* 186 */         return true;
/*     */       } 
/* 188 */       return false;
/*     */     } finally {
/* 190 */       if (thread == null || !thread.isAlive()) {
/* 191 */         this.semaphore.release();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class AsyncAction
/*     */     extends AbstractAction
/*     */   {
/*     */     private final Action action;
/*     */ 
/*     */ 
/*     */     
/*     */     private final RollingFileManager manager;
/*     */ 
/*     */ 
/*     */     
/*     */     public AsyncAction(Action act, RollingFileManager manager) {
/* 211 */       this.action = act;
/* 212 */       this.manager = manager;
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
/*     */     public boolean execute() throws IOException {
/*     */       try {
/* 226 */         return this.action.execute();
/*     */       } finally {
/* 228 */         this.manager.semaphore.release();
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void close() {
/* 237 */       this.action.close();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean isComplete() {
/* 247 */       return this.action.isComplete();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class FactoryData
/*     */   {
/*     */     private final String pattern;
/*     */ 
/*     */     
/*     */     private final boolean append;
/*     */ 
/*     */     
/*     */     private final boolean bufferedIO;
/*     */ 
/*     */     
/*     */     private final int bufferSize;
/*     */     
/*     */     private final TriggeringPolicy policy;
/*     */     
/*     */     private final RolloverStrategy strategy;
/*     */     
/*     */     private final String advertiseURI;
/*     */     
/*     */     private final Layout<? extends Serializable> layout;
/*     */ 
/*     */     
/*     */     public FactoryData(String pattern, boolean append, boolean bufferedIO, TriggeringPolicy policy, RolloverStrategy strategy, String advertiseURI, Layout<? extends Serializable> layout, int bufferSize) {
/* 276 */       this.pattern = pattern;
/* 277 */       this.append = append;
/* 278 */       this.bufferedIO = bufferedIO;
/* 279 */       this.bufferSize = bufferSize;
/* 280 */       this.policy = policy;
/* 281 */       this.strategy = strategy;
/* 282 */       this.advertiseURI = advertiseURI;
/* 283 */       this.layout = layout;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class RollingFileManagerFactory
/*     */     implements ManagerFactory<RollingFileManager, FactoryData>
/*     */   {
/*     */     private RollingFileManagerFactory() {}
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public RollingFileManager createManager(String name, RollingFileManager.FactoryData data) {
/* 300 */       File file = new File(name);
/* 301 */       File parent = file.getParentFile();
/* 302 */       if (null != parent && !parent.exists()) {
/* 303 */         parent.mkdirs();
/*     */       }
/*     */       try {
/* 306 */         file.createNewFile();
/* 307 */       } catch (IOException ioe) {
/* 308 */         RollingFileManager.LOGGER.error("Unable to create file " + name, ioe);
/* 309 */         return null;
/*     */       } 
/* 311 */       long size = data.append ? file.length() : 0L;
/*     */ 
/*     */       
/*     */       try {
/* 315 */         OutputStream os = new FileOutputStream(name, data.append);
/* 316 */         int bufferSize = data.bufferSize;
/* 317 */         if (data.bufferedIO) {
/* 318 */           os = new BufferedOutputStream(os, bufferSize);
/*     */         } else {
/* 320 */           bufferSize = -1;
/*     */         } 
/* 322 */         long time = file.lastModified();
/* 323 */         return new RollingFileManager(name, data.pattern, os, data.append, size, time, data.policy, data.strategy, data.advertiseURI, data.layout, bufferSize);
/*     */       }
/* 325 */       catch (FileNotFoundException ex) {
/* 326 */         RollingFileManager.LOGGER.error("FileManager (" + name + ") " + ex);
/*     */         
/* 328 */         return null;
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\appender\rolling\RollingFileManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */