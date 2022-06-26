/*     */ package org.apache.logging.log4j.core.appender.rolling;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.RandomAccessFile;
/*     */ import java.io.Serializable;
/*     */ import java.nio.ByteBuffer;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.core.Layout;
/*     */ import org.apache.logging.log4j.core.appender.AppenderLoggingException;
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
/*     */ public class RollingRandomAccessFileManager
/*     */   extends RollingFileManager
/*     */ {
/*     */   public static final int DEFAULT_BUFFER_SIZE = 262144;
/*  41 */   private static final RollingRandomAccessFileManagerFactory FACTORY = new RollingRandomAccessFileManagerFactory();
/*     */   
/*     */   private final boolean isImmediateFlush;
/*     */   private RandomAccessFile randomAccessFile;
/*     */   private final ByteBuffer buffer;
/*  46 */   private final ThreadLocal<Boolean> isEndOfBatch = new ThreadLocal<Boolean>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RollingRandomAccessFileManager(RandomAccessFile raf, String fileName, String pattern, OutputStream os, boolean append, boolean immediateFlush, int bufferSize, long size, long time, TriggeringPolicy policy, RolloverStrategy strategy, String advertiseURI, Layout<? extends Serializable> layout) {
/*  53 */     super(fileName, pattern, os, append, size, time, policy, strategy, advertiseURI, layout, bufferSize);
/*  54 */     this.isImmediateFlush = immediateFlush;
/*  55 */     this.randomAccessFile = raf;
/*  56 */     this.isEndOfBatch.set(Boolean.FALSE);
/*  57 */     this.buffer = ByteBuffer.allocate(bufferSize);
/*  58 */     writeHeader();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeHeader() {
/*  65 */     if (this.layout == null) {
/*     */       return;
/*     */     }
/*  68 */     byte[] header = this.layout.getHeader();
/*  69 */     if (header == null) {
/*     */       return;
/*     */     }
/*     */     
/*     */     try {
/*  74 */       this.randomAccessFile.write(header, 0, header.length);
/*  75 */     } catch (IOException ioe) {
/*  76 */       LOGGER.error("Unable to write header", ioe);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static RollingRandomAccessFileManager getRollingRandomAccessFileManager(String fileName, String filePattern, boolean isAppend, boolean immediateFlush, int bufferSize, TriggeringPolicy policy, RolloverStrategy strategy, String advertiseURI, Layout<? extends Serializable> layout) {
/*  84 */     return (RollingRandomAccessFileManager)getManager(fileName, new FactoryData(filePattern, isAppend, immediateFlush, bufferSize, policy, strategy, advertiseURI, layout), FACTORY);
/*     */   }
/*     */ 
/*     */   
/*     */   public Boolean isEndOfBatch() {
/*  89 */     return this.isEndOfBatch.get();
/*     */   }
/*     */   
/*     */   public void setEndOfBatch(boolean isEndOfBatch) {
/*  93 */     this.isEndOfBatch.set(Boolean.valueOf(isEndOfBatch));
/*     */   }
/*     */ 
/*     */   
/*     */   protected synchronized void write(byte[] bytes, int offset, int length) {
/*  98 */     super.write(bytes, offset, length);
/*     */     
/* 100 */     int chunk = 0;
/*     */     do {
/* 102 */       if (length > this.buffer.remaining()) {
/* 103 */         flush();
/*     */       }
/* 105 */       chunk = Math.min(length, this.buffer.remaining());
/* 106 */       this.buffer.put(bytes, offset, chunk);
/* 107 */       offset += chunk;
/* 108 */       length -= chunk;
/* 109 */     } while (length > 0);
/*     */     
/* 111 */     if (this.isImmediateFlush || this.isEndOfBatch.get() == Boolean.TRUE) {
/* 112 */       flush();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createFileAfterRollover() throws IOException {
/* 118 */     this.randomAccessFile = new RandomAccessFile(getFileName(), "rw");
/* 119 */     if (isAppend()) {
/* 120 */       this.randomAccessFile.seek(this.randomAccessFile.length());
/*     */     }
/* 122 */     writeHeader();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void flush() {
/* 127 */     this.buffer.flip();
/*     */     try {
/* 129 */       this.randomAccessFile.write(this.buffer.array(), 0, this.buffer.limit());
/* 130 */     } catch (IOException ex) {
/* 131 */       String msg = "Error writing to RandomAccessFile " + getName();
/* 132 */       throw new AppenderLoggingException(msg, ex);
/*     */     } 
/* 134 */     this.buffer.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void close() {
/* 139 */     flush();
/*     */     try {
/* 141 */       this.randomAccessFile.close();
/* 142 */     } catch (IOException ex) {
/* 143 */       LOGGER.error("Unable to close RandomAccessFile " + getName() + ". " + ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getBufferSize() {
/* 153 */     return this.buffer.capacity();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class RollingRandomAccessFileManagerFactory
/*     */     implements ManagerFactory<RollingRandomAccessFileManager, FactoryData>
/*     */   {
/*     */     private RollingRandomAccessFileManagerFactory() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public RollingRandomAccessFileManager createManager(String name, RollingRandomAccessFileManager.FactoryData data) {
/* 170 */       File file = new File(name);
/* 171 */       File parent = file.getParentFile();
/* 172 */       if (null != parent && !parent.exists()) {
/* 173 */         parent.mkdirs();
/*     */       }
/*     */       
/* 176 */       if (!data.append) {
/* 177 */         file.delete();
/*     */       }
/* 179 */       long size = data.append ? file.length() : 0L;
/* 180 */       long time = file.exists() ? file.lastModified() : System.currentTimeMillis();
/*     */       
/* 182 */       RandomAccessFile raf = null;
/*     */       try {
/* 184 */         raf = new RandomAccessFile(name, "rw");
/* 185 */         if (data.append) {
/* 186 */           long length = raf.length();
/* 187 */           RollingRandomAccessFileManager.LOGGER.trace("RandomAccessFile {} seek to {}", new Object[] { name, Long.valueOf(length) });
/* 188 */           raf.seek(length);
/*     */         } else {
/* 190 */           RollingRandomAccessFileManager.LOGGER.trace("RandomAccessFile {} set length to 0", new Object[] { name });
/* 191 */           raf.setLength(0L);
/*     */         } 
/* 193 */         return new RollingRandomAccessFileManager(raf, name, data.pattern, new RollingRandomAccessFileManager.DummyOutputStream(), data.append, data.immediateFlush, data.bufferSize, size, time, data.policy, data.strategy, data.advertiseURI, data.layout);
/*     */       
/*     */       }
/* 196 */       catch (IOException ex) {
/* 197 */         RollingRandomAccessFileManager.LOGGER.error("Cannot access RandomAccessFile {}) " + ex);
/* 198 */         if (raf != null) {
/*     */           try {
/* 200 */             raf.close();
/* 201 */           } catch (IOException e) {
/* 202 */             RollingRandomAccessFileManager.LOGGER.error("Cannot close RandomAccessFile {}", new Object[] { name, e });
/*     */           } 
/*     */         }
/*     */         
/* 206 */         return null;
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static class DummyOutputStream
/*     */     extends OutputStream
/*     */   {
/*     */     public void write(int b) throws IOException {}
/*     */ 
/*     */ 
/*     */     
/*     */     public void write(byte[] b, int off, int len) throws IOException {}
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
/*     */     private final boolean immediateFlush;
/*     */ 
/*     */     
/*     */     private final int bufferSize;
/*     */ 
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
/*     */     public FactoryData(String pattern, boolean append, boolean immediateFlush, int bufferSize, TriggeringPolicy policy, RolloverStrategy strategy, String advertiseURI, Layout<? extends Serializable> layout) {
/* 249 */       this.pattern = pattern;
/* 250 */       this.append = append;
/* 251 */       this.immediateFlush = immediateFlush;
/* 252 */       this.bufferSize = bufferSize;
/* 253 */       this.policy = policy;
/* 254 */       this.strategy = strategy;
/* 255 */       this.advertiseURI = advertiseURI;
/* 256 */       this.layout = layout;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\appender\rolling\RollingRandomAccessFileManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */