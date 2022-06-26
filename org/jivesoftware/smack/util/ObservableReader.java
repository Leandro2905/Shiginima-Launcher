/*     */ package org.jivesoftware.smack.util;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ObservableReader
/*     */   extends Reader
/*     */ {
/*  33 */   Reader wrappedReader = null;
/*  34 */   List<ReaderListener> listeners = new ArrayList<>();
/*     */   
/*     */   public ObservableReader(Reader wrappedReader) {
/*  37 */     this.wrappedReader = wrappedReader;
/*     */   }
/*     */   
/*     */   public int read(char[] cbuf, int off, int len) throws IOException {
/*  41 */     int count = this.wrappedReader.read(cbuf, off, len);
/*  42 */     if (count > 0) {
/*  43 */       String str = new String(cbuf, off, count);
/*     */       
/*  45 */       ReaderListener[] readerListeners = null;
/*  46 */       synchronized (this.listeners) {
/*  47 */         readerListeners = new ReaderListener[this.listeners.size()];
/*  48 */         this.listeners.toArray(readerListeners);
/*     */       } 
/*  50 */       for (int i = 0; i < readerListeners.length; i++) {
/*  51 */         readerListeners[i].read(str);
/*     */       }
/*     */     } 
/*  54 */     return count;
/*     */   }
/*     */   
/*     */   public void close() throws IOException {
/*  58 */     this.wrappedReader.close();
/*     */   }
/*     */   
/*     */   public int read() throws IOException {
/*  62 */     return this.wrappedReader.read();
/*     */   }
/*     */   
/*     */   public int read(char[] cbuf) throws IOException {
/*  66 */     return this.wrappedReader.read(cbuf);
/*     */   }
/*     */   
/*     */   public long skip(long n) throws IOException {
/*  70 */     return this.wrappedReader.skip(n);
/*     */   }
/*     */   
/*     */   public boolean ready() throws IOException {
/*  74 */     return this.wrappedReader.ready();
/*     */   }
/*     */   
/*     */   public boolean markSupported() {
/*  78 */     return this.wrappedReader.markSupported();
/*     */   }
/*     */   
/*     */   public void mark(int readAheadLimit) throws IOException {
/*  82 */     this.wrappedReader.mark(readAheadLimit);
/*     */   }
/*     */   
/*     */   public void reset() throws IOException {
/*  86 */     this.wrappedReader.reset();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addReaderListener(ReaderListener readerListener) {
/*  96 */     if (readerListener == null) {
/*     */       return;
/*     */     }
/*  99 */     synchronized (this.listeners) {
/* 100 */       if (!this.listeners.contains(readerListener)) {
/* 101 */         this.listeners.add(readerListener);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeReaderListener(ReaderListener readerListener) {
/* 112 */     synchronized (this.listeners) {
/* 113 */       this.listeners.remove(readerListener);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jivesoftware\smac\\util\ObservableReader.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */