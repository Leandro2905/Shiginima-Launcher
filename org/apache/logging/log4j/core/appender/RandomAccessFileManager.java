/*     */ package org.apache.logging.log4j.core.appender;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.RandomAccessFile;
/*     */ import java.io.Serializable;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.apache.logging.log4j.core.Layout;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RandomAccessFileManager
/*     */   extends OutputStreamManager
/*     */ {
/*     */   static final int DEFAULT_BUFFER_SIZE = 262144;
/*  38 */   private static final RandomAccessFileManagerFactory FACTORY = new RandomAccessFileManagerFactory();
/*     */   
/*     */   private final boolean isImmediateFlush;
/*     */   private final String advertiseURI;
/*     */   private final RandomAccessFile randomAccessFile;
/*     */   private final ByteBuffer buffer;
/*  44 */   private final ThreadLocal<Boolean> isEndOfBatch = new ThreadLocal<Boolean>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected RandomAccessFileManager(RandomAccessFile file, String fileName, OutputStream os, boolean immediateFlush, int bufferSize, String advertiseURI, Layout<? extends Serializable> layout) {
/*  50 */     super(os, fileName, layout);
/*  51 */     this.isImmediateFlush = immediateFlush;
/*  52 */     this.randomAccessFile = file;
/*  53 */     this.advertiseURI = advertiseURI;
/*  54 */     this.isEndOfBatch.set(Boolean.FALSE);
/*  55 */     this.buffer = ByteBuffer.allocate(bufferSize);
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
/*     */   public static RandomAccessFileManager getFileManager(String fileName, boolean append, boolean isFlush, int bufferSize, String advertiseURI, Layout<? extends Serializable> layout) {
/*  74 */     return (RandomAccessFileManager)getManager(fileName, new FactoryData(append, isFlush, bufferSize, advertiseURI, layout), FACTORY);
/*     */   }
/*     */ 
/*     */   
/*     */   public Boolean isEndOfBatch() {
/*  79 */     return this.isEndOfBatch.get();
/*     */   }
/*     */   
/*     */   public void setEndOfBatch(boolean isEndOfBatch) {
/*  83 */     this.isEndOfBatch.set(Boolean.valueOf(isEndOfBatch));
/*     */   }
/*     */ 
/*     */   
/*     */   protected synchronized void write(byte[] bytes, int offset, int length) {
/*  88 */     super.write(bytes, offset, length);
/*     */     
/*  90 */     int chunk = 0;
/*     */     do {
/*  92 */       if (length > this.buffer.remaining()) {
/*  93 */         flush();
/*     */       }
/*  95 */       chunk = Math.min(length, this.buffer.remaining());
/*  96 */       this.buffer.put(bytes, offset, chunk);
/*  97 */       offset += chunk;
/*  98 */       length -= chunk;
/*  99 */     } while (length > 0);
/*     */     
/* 101 */     if (this.isImmediateFlush || this.isEndOfBatch.get() == Boolean.TRUE) {
/* 102 */       flush();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void flush() {
/* 108 */     this.buffer.flip();
/*     */     try {
/* 110 */       this.randomAccessFile.write(this.buffer.array(), 0, this.buffer.limit());
/* 111 */     } catch (IOException ex) {
/* 112 */       String msg = "Error writing to RandomAccessFile " + getName();
/* 113 */       throw new AppenderLoggingException(msg, ex);
/*     */     } 
/* 115 */     this.buffer.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void close() {
/* 120 */     flush();
/*     */     try {
/* 122 */       this.randomAccessFile.close();
/* 123 */     } catch (IOException ex) {
/* 124 */       LOGGER.error("Unable to close RandomAccessFile " + getName() + ". " + ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFileName() {
/* 135 */     return getName();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getBufferSize() {
/* 143 */     return this.buffer.capacity();
/*     */   }
/*     */ 
/*     */ 
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
/*     */ 
/*     */     
/*     */     public void write(byte[] b, int off, int len) throws IOException {}
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, String> getContentFormat() {
/* 166 */     Map<String, String> result = new HashMap<String, String>(super.getContentFormat());
/*     */     
/* 168 */     result.put("fileURI", this.advertiseURI);
/* 169 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class FactoryData
/*     */   {
/*     */     private final boolean append;
/*     */ 
/*     */     
/*     */     private final boolean immediateFlush;
/*     */ 
/*     */     
/*     */     private final int bufferSize;
/*     */     
/*     */     private final String advertiseURI;
/*     */     
/*     */     private final Layout<? extends Serializable> layout;
/*     */ 
/*     */     
/*     */     public FactoryData(boolean append, boolean immediateFlush, int bufferSize, String advertiseURI, Layout<? extends Serializable> layout) {
/* 190 */       this.append = append;
/* 191 */       this.immediateFlush = immediateFlush;
/* 192 */       this.bufferSize = bufferSize;
/* 193 */       this.advertiseURI = advertiseURI;
/* 194 */       this.layout = layout;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class RandomAccessFileManagerFactory
/*     */     implements ManagerFactory<RandomAccessFileManager, FactoryData>
/*     */   {
/*     */     private RandomAccessFileManagerFactory() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public RandomAccessFileManager createManager(String name, RandomAccessFileManager.FactoryData data) {
/* 213 */       File file = new File(name);
/* 214 */       File parent = file.getParentFile();
/* 215 */       if (null != parent && !parent.exists()) {
/* 216 */         parent.mkdirs();
/*     */       }
/* 218 */       if (!data.append) {
/* 219 */         file.delete();
/*     */       }
/*     */       
/* 222 */       OutputStream os = new RandomAccessFileManager.DummyOutputStream();
/*     */       
/*     */       try {
/* 225 */         RandomAccessFile raf = new RandomAccessFile(name, "rw");
/* 226 */         if (data.append) {
/* 227 */           raf.seek(raf.length());
/*     */         } else {
/* 229 */           raf.setLength(0L);
/*     */         } 
/* 231 */         return new RandomAccessFileManager(raf, name, os, data.immediateFlush, data.bufferSize, data.advertiseURI, data.layout);
/*     */       }
/* 233 */       catch (Exception ex) {
/* 234 */         AbstractManager.LOGGER.error("RandomAccessFileManager (" + name + ") " + ex);
/*     */         
/* 236 */         return null;
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\appender\RandomAccessFileManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */