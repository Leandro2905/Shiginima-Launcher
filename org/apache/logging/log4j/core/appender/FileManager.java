/*     */ package org.apache.logging.log4j.core.appender;
/*     */ 
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.nio.channels.FileChannel;
/*     */ import java.nio.channels.FileLock;
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
/*     */ public class FileManager
/*     */   extends OutputStreamManager
/*     */ {
/*  39 */   private static final FileManagerFactory FACTORY = new FileManagerFactory();
/*     */   
/*     */   private final boolean isAppend;
/*     */   
/*     */   private final boolean isLocking;
/*     */   private final String advertiseURI;
/*     */   private final int bufferSize;
/*     */   
/*     */   protected FileManager(String fileName, OutputStream os, boolean append, boolean locking, String advertiseURI, Layout<? extends Serializable> layout, int bufferSize) {
/*  48 */     super(os, fileName, layout);
/*  49 */     this.isAppend = append;
/*  50 */     this.isLocking = locking;
/*  51 */     this.advertiseURI = advertiseURI;
/*  52 */     this.bufferSize = bufferSize;
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
/*     */   public static FileManager getFileManager(String fileName, boolean append, boolean locking, boolean bufferedIo, String advertiseUri, Layout<? extends Serializable> layout, int bufferSize) {
/*  70 */     if (locking && bufferedIo) {
/*  71 */       locking = false;
/*     */     }
/*  73 */     return (FileManager)getManager(fileName, new FactoryData(append, locking, bufferedIo, bufferSize, advertiseUri, layout), FACTORY);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected synchronized void write(byte[] bytes, int offset, int length) {
/*  80 */     if (this.isLocking) {
/*  81 */       FileChannel channel = ((FileOutputStream)getOutputStream()).getChannel();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       try {
/*  90 */         FileLock lock = channel.lock(0L, Long.MAX_VALUE, false);
/*     */         try {
/*  92 */           super.write(bytes, offset, length);
/*     */         } finally {
/*  94 */           lock.release();
/*     */         } 
/*  96 */       } catch (IOException ex) {
/*  97 */         throw new AppenderLoggingException("Unable to obtain lock on " + getName(), ex);
/*     */       } 
/*     */     } else {
/*     */       
/* 101 */       super.write(bytes, offset, length);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFileName() {
/* 110 */     return getName();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isAppend() {
/* 118 */     return this.isAppend;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isLocking() {
/* 126 */     return this.isLocking;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getBufferSize() {
/* 135 */     return this.bufferSize;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, String> getContentFormat() {
/* 145 */     Map<String, String> result = new HashMap<String, String>(super.getContentFormat());
/* 146 */     result.put("fileURI", this.advertiseURI);
/* 147 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class FactoryData
/*     */   {
/*     */     private final boolean append;
/*     */ 
/*     */     
/*     */     private final boolean locking;
/*     */ 
/*     */     
/*     */     private final boolean bufferedIO;
/*     */ 
/*     */     
/*     */     private final int bufferSize;
/*     */     
/*     */     private final String advertiseURI;
/*     */     
/*     */     private final Layout<? extends Serializable> layout;
/*     */ 
/*     */     
/*     */     public FactoryData(boolean append, boolean locking, boolean bufferedIO, int bufferSize, String advertiseURI, Layout<? extends Serializable> layout) {
/* 171 */       this.append = append;
/* 172 */       this.locking = locking;
/* 173 */       this.bufferedIO = bufferedIO;
/* 174 */       this.bufferSize = bufferSize;
/* 175 */       this.advertiseURI = advertiseURI;
/* 176 */       this.layout = layout;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class FileManagerFactory
/*     */     implements ManagerFactory<FileManager, FactoryData>
/*     */   {
/*     */     private FileManagerFactory() {}
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public FileManager createManager(String name, FileManager.FactoryData data) {
/* 193 */       File file = new File(name);
/* 194 */       File parent = file.getParentFile();
/* 195 */       if (null != parent && !parent.exists()) {
/* 196 */         parent.mkdirs();
/*     */       }
/*     */ 
/*     */       
/*     */       try {
/* 201 */         OutputStream os = new FileOutputStream(name, data.append);
/* 202 */         int bufferSize = data.bufferSize;
/* 203 */         if (data.bufferedIO) {
/* 204 */           os = new BufferedOutputStream(os, bufferSize);
/*     */         } else {
/* 206 */           bufferSize = -1;
/*     */         } 
/* 208 */         return new FileManager(name, os, data.append, data.locking, data.advertiseURI, data.layout, bufferSize);
/* 209 */       } catch (FileNotFoundException ex) {
/* 210 */         AbstractManager.LOGGER.error("FileManager (" + name + ") " + ex);
/*     */         
/* 212 */         return null;
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\appender\FileManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */