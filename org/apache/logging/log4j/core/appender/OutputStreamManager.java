/*     */ package org.apache.logging.log4j.core.appender;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
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
/*     */ public class OutputStreamManager
/*     */   extends AbstractManager
/*     */ {
/*     */   private volatile OutputStream os;
/*     */   protected final Layout<?> layout;
/*     */   
/*     */   protected OutputStreamManager(OutputStream os, String streamName, Layout<?> layout) {
/*  34 */     super(streamName);
/*  35 */     this.os = os;
/*  36 */     this.layout = layout;
/*  37 */     if (layout != null) {
/*  38 */       byte[] header = layout.getHeader();
/*  39 */       if (header != null) {
/*     */         try {
/*  41 */           this.os.write(header, 0, header.length);
/*  42 */         } catch (IOException ioe) {
/*  43 */           LOGGER.error("Unable to write header", ioe);
/*     */         } 
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> OutputStreamManager getManager(String name, T data, ManagerFactory<? extends OutputStreamManager, T> factory) {
/*  60 */     return AbstractManager.<OutputStreamManager, T>getManager(name, (ManagerFactory)factory, data);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void releaseSub() {
/*  68 */     writeFooter();
/*  69 */     close();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeFooter() {
/*  76 */     if (this.layout == null) {
/*     */       return;
/*     */     }
/*  79 */     byte[] footer = this.layout.getFooter();
/*  80 */     if (footer != null) {
/*  81 */       write(footer);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isOpen() {
/*  90 */     return (getCount() > 0);
/*     */   }
/*     */   
/*     */   protected OutputStream getOutputStream() {
/*  94 */     return this.os;
/*     */   }
/*     */   
/*     */   protected void setOutputStream(OutputStream os) {
/*  98 */     byte[] header = this.layout.getHeader();
/*  99 */     if (header != null) {
/*     */       try {
/* 101 */         os.write(header, 0, header.length);
/* 102 */         this.os = os;
/* 103 */       } catch (IOException ioe) {
/* 104 */         LOGGER.error("Unable to write header", ioe);
/*     */       } 
/*     */     } else {
/* 107 */       this.os = os;
/*     */     } 
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
/*     */   protected synchronized void write(byte[] bytes, int offset, int length) {
/*     */     try {
/* 122 */       this.os.write(bytes, offset, length);
/* 123 */     } catch (IOException ex) {
/* 124 */       String msg = "Error writing to stream " + getName();
/* 125 */       throw new AppenderLoggingException(msg, ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void write(byte[] bytes) {
/* 136 */     write(bytes, 0, bytes.length);
/*     */   }
/*     */   
/*     */   protected synchronized void close() {
/* 140 */     OutputStream stream = this.os;
/* 141 */     if (stream == System.out || stream == System.err) {
/*     */       return;
/*     */     }
/*     */     try {
/* 145 */       stream.close();
/* 146 */     } catch (IOException ex) {
/* 147 */       LOGGER.error("Unable to close stream " + getName() + ". " + ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void flush() {
/*     */     try {
/* 156 */       this.os.flush();
/* 157 */     } catch (IOException ex) {
/* 158 */       String msg = "Error flushing stream " + getName();
/* 159 */       throw new AppenderLoggingException(msg, ex);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\appender\OutputStreamManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */