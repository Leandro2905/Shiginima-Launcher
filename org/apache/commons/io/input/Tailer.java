/*     */ package org.apache.commons.io.input;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.io.RandomAccessFile;
/*     */ import org.apache.commons.io.FileUtils;
/*     */ import org.apache.commons.io.IOUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Tailer
/*     */   implements Runnable
/*     */ {
/*     */   private static final int DEFAULT_DELAY_MILLIS = 1000;
/*     */   private static final String RAF_MODE = "r";
/*     */   private static final int DEFAULT_BUFSIZE = 4096;
/*     */   private final byte[] inbuf;
/*     */   private final File file;
/*     */   private final long delayMillis;
/*     */   private final boolean end;
/*     */   private final TailerListener listener;
/*     */   private final boolean reOpen;
/*     */   private volatile boolean run = true;
/*     */   
/*     */   public Tailer(File file, TailerListener listener) {
/* 156 */     this(file, listener, 1000L);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Tailer(File file, TailerListener listener, long delayMillis) {
/* 166 */     this(file, listener, delayMillis, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Tailer(File file, TailerListener listener, long delayMillis, boolean end) {
/* 177 */     this(file, listener, delayMillis, end, 4096);
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
/*     */   public Tailer(File file, TailerListener listener, long delayMillis, boolean end, boolean reOpen) {
/* 189 */     this(file, listener, delayMillis, end, reOpen, 4096);
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
/*     */   public Tailer(File file, TailerListener listener, long delayMillis, boolean end, int bufSize) {
/* 201 */     this(file, listener, delayMillis, end, false, bufSize);
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
/*     */   public Tailer(File file, TailerListener listener, long delayMillis, boolean end, boolean reOpen, int bufSize) {
/* 214 */     this.file = file;
/* 215 */     this.delayMillis = delayMillis;
/* 216 */     this.end = end;
/*     */     
/* 218 */     this.inbuf = new byte[bufSize];
/*     */ 
/*     */     
/* 221 */     this.listener = listener;
/* 222 */     listener.init(this);
/* 223 */     this.reOpen = reOpen;
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
/*     */   public static Tailer create(File file, TailerListener listener, long delayMillis, boolean end, int bufSize) {
/* 237 */     Tailer tailer = new Tailer(file, listener, delayMillis, end, bufSize);
/* 238 */     Thread thread = new Thread(tailer);
/* 239 */     thread.setDaemon(true);
/* 240 */     thread.start();
/* 241 */     return tailer;
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
/*     */   public static Tailer create(File file, TailerListener listener, long delayMillis, boolean end, boolean reOpen, int bufSize) {
/* 257 */     Tailer tailer = new Tailer(file, listener, delayMillis, end, reOpen, bufSize);
/* 258 */     Thread thread = new Thread(tailer);
/* 259 */     thread.setDaemon(true);
/* 260 */     thread.start();
/* 261 */     return tailer;
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
/*     */   public static Tailer create(File file, TailerListener listener, long delayMillis, boolean end) {
/* 274 */     return create(file, listener, delayMillis, end, 4096);
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
/*     */   public static Tailer create(File file, TailerListener listener, long delayMillis, boolean end, boolean reOpen) {
/* 288 */     return create(file, listener, delayMillis, end, reOpen, 4096);
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
/*     */   public static Tailer create(File file, TailerListener listener, long delayMillis) {
/* 300 */     return create(file, listener, delayMillis, false);
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
/*     */   public static Tailer create(File file, TailerListener listener) {
/* 312 */     return create(file, listener, 1000L, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public File getFile() {
/* 321 */     return this.file;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getDelay() {
/* 330 */     return this.delayMillis;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void run() {
/* 337 */     RandomAccessFile reader = null;
/*     */     try {
/* 339 */       long last = 0L;
/* 340 */       long position = 0L;
/*     */       
/* 342 */       while (this.run && reader == null) {
/*     */         try {
/* 344 */           reader = new RandomAccessFile(this.file, "r");
/* 345 */         } catch (FileNotFoundException e) {
/* 346 */           this.listener.fileNotFound();
/*     */         } 
/*     */         
/* 349 */         if (reader == null) {
/*     */           try {
/* 351 */             Thread.sleep(this.delayMillis);
/* 352 */           } catch (InterruptedException e) {}
/*     */           
/*     */           continue;
/*     */         } 
/* 356 */         position = this.end ? this.file.length() : 0L;
/* 357 */         last = System.currentTimeMillis();
/* 358 */         reader.seek(position);
/*     */       } 
/*     */ 
/*     */       
/* 362 */       while (this.run) {
/*     */         
/* 364 */         boolean newer = FileUtils.isFileNewer(this.file, last);
/*     */ 
/*     */         
/* 367 */         long length = this.file.length();
/*     */         
/* 369 */         if (length < position) {
/*     */ 
/*     */           
/* 372 */           this.listener.fileRotated();
/*     */ 
/*     */ 
/*     */           
/*     */           try {
/* 377 */             RandomAccessFile save = reader;
/* 378 */             reader = new RandomAccessFile(this.file, "r");
/* 379 */             position = 0L;
/*     */             
/* 381 */             IOUtils.closeQuietly(save);
/* 382 */           } catch (FileNotFoundException e) {
/*     */             
/* 384 */             this.listener.fileNotFound();
/*     */           } 
/*     */ 
/*     */           
/*     */           continue;
/*     */         } 
/*     */ 
/*     */         
/* 392 */         if (length > position) {
/*     */ 
/*     */           
/* 395 */           position = readLines(reader);
/* 396 */           last = System.currentTimeMillis();
/*     */         }
/* 398 */         else if (newer) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 404 */           position = 0L;
/* 405 */           reader.seek(position);
/*     */ 
/*     */           
/* 408 */           position = readLines(reader);
/* 409 */           last = System.currentTimeMillis();
/*     */         } 
/*     */         
/* 412 */         if (this.reOpen) {
/* 413 */           IOUtils.closeQuietly(reader);
/*     */         }
/*     */         try {
/* 416 */           Thread.sleep(this.delayMillis);
/* 417 */         } catch (InterruptedException e) {}
/*     */         
/* 419 */         if (this.run && this.reOpen) {
/* 420 */           reader = new RandomAccessFile(this.file, "r");
/* 421 */           reader.seek(position);
/*     */         }
/*     */       
/*     */       } 
/* 425 */     } catch (Exception e) {
/*     */       
/* 427 */       this.listener.handle(e);
/*     */     } finally {
/*     */       
/* 430 */       IOUtils.closeQuietly(reader);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void stop() {
/* 438 */     this.run = false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private long readLines(RandomAccessFile reader) throws IOException {
/* 449 */     StringBuilder sb = new StringBuilder();
/*     */     
/* 451 */     long pos = reader.getFilePointer();
/* 452 */     long rePos = pos;
/*     */ 
/*     */     
/* 455 */     boolean seenCR = false; int num;
/* 456 */     while (this.run && (num = reader.read(this.inbuf)) != -1) {
/* 457 */       for (int i = 0; i < num; i++) {
/* 458 */         byte ch = this.inbuf[i];
/* 459 */         switch (ch) {
/*     */           case 10:
/* 461 */             seenCR = false;
/* 462 */             this.listener.handle(sb.toString());
/* 463 */             sb.setLength(0);
/* 464 */             rePos = pos + i + 1L;
/*     */             break;
/*     */           case 13:
/* 467 */             if (seenCR) {
/* 468 */               sb.append('\r');
/*     */             }
/* 470 */             seenCR = true;
/*     */             break;
/*     */           default:
/* 473 */             if (seenCR) {
/* 474 */               seenCR = false;
/* 475 */               this.listener.handle(sb.toString());
/* 476 */               sb.setLength(0);
/* 477 */               rePos = pos + i + 1L;
/*     */             } 
/* 479 */             sb.append((char)ch);
/*     */             break;
/*     */         } 
/*     */       } 
/* 483 */       pos = reader.getFilePointer();
/*     */     } 
/*     */     
/* 486 */     reader.seek(rePos);
/* 487 */     return rePos;
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\commons\io\input\Tailer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */