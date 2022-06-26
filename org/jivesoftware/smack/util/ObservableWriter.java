/*     */ package org.jivesoftware.smack.util;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.Writer;
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
/*     */ public class ObservableWriter
/*     */   extends Writer
/*     */ {
/*  32 */   Writer wrappedWriter = null;
/*  33 */   List<WriterListener> listeners = new ArrayList<>();
/*     */   
/*     */   public ObservableWriter(Writer wrappedWriter) {
/*  36 */     this.wrappedWriter = wrappedWriter;
/*     */   }
/*     */   
/*     */   public void write(char[] cbuf, int off, int len) throws IOException {
/*  40 */     this.wrappedWriter.write(cbuf, off, len);
/*  41 */     String str = new String(cbuf, off, len);
/*  42 */     notifyListeners(str);
/*     */   }
/*     */   
/*     */   public void flush() throws IOException {
/*  46 */     this.wrappedWriter.flush();
/*     */   }
/*     */   
/*     */   public void close() throws IOException {
/*  50 */     this.wrappedWriter.close();
/*     */   }
/*     */   
/*     */   public void write(int c) throws IOException {
/*  54 */     this.wrappedWriter.write(c);
/*     */   }
/*     */   
/*     */   public void write(char[] cbuf) throws IOException {
/*  58 */     this.wrappedWriter.write(cbuf);
/*  59 */     String str = new String(cbuf);
/*  60 */     notifyListeners(str);
/*     */   }
/*     */   
/*     */   public void write(String str) throws IOException {
/*  64 */     this.wrappedWriter.write(str);
/*  65 */     notifyListeners(str);
/*     */   }
/*     */   
/*     */   public void write(String str, int off, int len) throws IOException {
/*  69 */     this.wrappedWriter.write(str, off, len);
/*  70 */     str = str.substring(off, off + len);
/*  71 */     notifyListeners(str);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void notifyListeners(String str) {
/*  80 */     WriterListener[] writerListeners = null;
/*  81 */     synchronized (this.listeners) {
/*  82 */       writerListeners = new WriterListener[this.listeners.size()];
/*  83 */       this.listeners.toArray(writerListeners);
/*     */     } 
/*  85 */     for (int i = 0; i < writerListeners.length; i++) {
/*  86 */       writerListeners[i].write(str);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addWriterListener(WriterListener writerListener) {
/*  97 */     if (writerListener == null) {
/*     */       return;
/*     */     }
/* 100 */     synchronized (this.listeners) {
/* 101 */       if (!this.listeners.contains(writerListener)) {
/* 102 */         this.listeners.add(writerListener);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeWriterListener(WriterListener writerListener) {
/* 113 */     synchronized (this.listeners) {
/* 114 */       this.listeners.remove(writerListener);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jivesoftware\smac\\util\ObservableWriter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */