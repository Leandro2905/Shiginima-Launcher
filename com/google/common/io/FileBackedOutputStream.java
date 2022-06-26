/*     */ package com.google.common.io;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Beta
/*     */ public final class FileBackedOutputStream
/*     */   extends OutputStream
/*     */ {
/*     */   private final int fileThreshold;
/*     */   private final boolean resetOnFinalize;
/*     */   private final ByteSource source;
/*     */   private OutputStream out;
/*     */   private MemoryOutput memory;
/*     */   private File file;
/*     */   
/*     */   private static class MemoryOutput
/*     */     extends ByteArrayOutputStream
/*     */   {
/*     */     private MemoryOutput() {}
/*     */     
/*     */     byte[] getBuffer() {
/*  54 */       return this.buf;
/*     */     }
/*     */     
/*     */     int getCount() {
/*  58 */       return this.count;
/*     */     }
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   synchronized File getFile() {
/*  64 */     return this.file;
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
/*     */   public FileBackedOutputStream(int fileThreshold) {
/*  76 */     this(fileThreshold, false);
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
/*     */   public FileBackedOutputStream(int fileThreshold, boolean resetOnFinalize) {
/*  91 */     this.fileThreshold = fileThreshold;
/*  92 */     this.resetOnFinalize = resetOnFinalize;
/*  93 */     this.memory = new MemoryOutput();
/*  94 */     this.out = this.memory;
/*     */     
/*  96 */     if (resetOnFinalize) {
/*  97 */       this.source = new ByteSource()
/*     */         {
/*     */           public InputStream openStream() throws IOException {
/* 100 */             return FileBackedOutputStream.this.openInputStream();
/*     */           }
/*     */           
/*     */           protected void finalize() {
/*     */             try {
/* 105 */               FileBackedOutputStream.this.reset();
/* 106 */             } catch (Throwable t) {
/* 107 */               t.printStackTrace(System.err);
/*     */             } 
/*     */           }
/*     */         };
/*     */     } else {
/* 112 */       this.source = new ByteSource()
/*     */         {
/*     */           public InputStream openStream() throws IOException {
/* 115 */             return FileBackedOutputStream.this.openInputStream();
/*     */           }
/*     */         };
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ByteSource asByteSource() {
/* 128 */     return this.source;
/*     */   }
/*     */   
/*     */   private synchronized InputStream openInputStream() throws IOException {
/* 132 */     if (this.file != null) {
/* 133 */       return new FileInputStream(this.file);
/*     */     }
/* 135 */     return new ByteArrayInputStream(this.memory.getBuffer(), 0, this.memory.getCount());
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
/*     */   public synchronized void reset() throws IOException {
/*     */     try {
/* 149 */       close();
/*     */     } finally {
/* 151 */       if (this.memory == null) {
/* 152 */         this.memory = new MemoryOutput();
/*     */       } else {
/* 154 */         this.memory.reset();
/*     */       } 
/* 156 */       this.out = this.memory;
/* 157 */       if (this.file != null) {
/* 158 */         File deleteMe = this.file;
/* 159 */         this.file = null;
/* 160 */         if (!deleteMe.delete()) {
/* 161 */           String str = String.valueOf(String.valueOf(deleteMe)); throw new IOException((new StringBuilder(18 + str.length())).append("Could not delete: ").append(str).toString());
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public synchronized void write(int b) throws IOException {
/* 168 */     update(1);
/* 169 */     this.out.write(b);
/*     */   }
/*     */   
/*     */   public synchronized void write(byte[] b) throws IOException {
/* 173 */     write(b, 0, b.length);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void write(byte[] b, int off, int len) throws IOException {
/* 178 */     update(len);
/* 179 */     this.out.write(b, off, len);
/*     */   }
/*     */   
/*     */   public synchronized void close() throws IOException {
/* 183 */     this.out.close();
/*     */   }
/*     */   
/*     */   public synchronized void flush() throws IOException {
/* 187 */     this.out.flush();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void update(int len) throws IOException {
/* 195 */     if (this.file == null && this.memory.getCount() + len > this.fileThreshold) {
/* 196 */       File temp = File.createTempFile("FileBackedOutputStream", null);
/* 197 */       if (this.resetOnFinalize)
/*     */       {
/*     */         
/* 200 */         temp.deleteOnExit();
/*     */       }
/* 202 */       FileOutputStream transfer = new FileOutputStream(temp);
/* 203 */       transfer.write(this.memory.getBuffer(), 0, this.memory.getCount());
/* 204 */       transfer.flush();
/*     */ 
/*     */       
/* 207 */       this.out = transfer;
/* 208 */       this.file = temp;
/* 209 */       this.memory = null;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\io\FileBackedOutputStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */