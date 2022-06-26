/*     */ package org.apache.logging.log4j.core.net;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.net.DatagramPacket;
/*     */ import java.net.DatagramSocket;
/*     */ import java.net.InetAddress;
/*     */ import java.net.SocketException;
/*     */ import java.net.UnknownHostException;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.core.appender.AppenderLoggingException;
/*     */ import org.apache.logging.log4j.status.StatusLogger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DatagramOutputStream
/*     */   extends OutputStream
/*     */ {
/*  39 */   protected static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*     */   
/*     */   private static final int SHIFT_1 = 8;
/*     */   
/*     */   private static final int SHIFT_2 = 16;
/*     */   
/*     */   private static final int SHIFT_3 = 24;
/*     */   
/*     */   private DatagramSocket ds;
/*     */   
/*     */   private final InetAddress address;
/*     */   
/*     */   private final int port;
/*     */   
/*     */   private byte[] data;
/*     */   
/*     */   private final byte[] header;
/*     */   
/*     */   private final byte[] footer;
/*     */   
/*     */   public DatagramOutputStream(String host, int port, byte[] header, byte[] footer) {
/*  60 */     this.port = port;
/*  61 */     this.header = header;
/*  62 */     this.footer = footer;
/*     */     try {
/*  64 */       this.address = InetAddress.getByName(host);
/*  65 */     } catch (UnknownHostException ex) {
/*  66 */       String msg = "Could not find host " + host;
/*  67 */       LOGGER.error(msg, ex);
/*  68 */       throw new AppenderLoggingException(msg, ex);
/*     */     } 
/*     */     
/*     */     try {
/*  72 */       this.ds = new DatagramSocket();
/*  73 */     } catch (SocketException ex) {
/*  74 */       String msg = "Could not instantiate DatagramSocket to " + host;
/*  75 */       LOGGER.error(msg, ex);
/*  76 */       throw new AppenderLoggingException(msg, ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void write(byte[] bytes, int offset, int length) throws IOException {
/*  82 */     copy(bytes, offset, length);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void write(int i) throws IOException {
/*  87 */     copy(new byte[] { (byte)(i >>> 24), (byte)(i >>> 16), (byte)(i >>> 8), (byte)i }, 0, 4);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void write(byte[] bytes) throws IOException {
/*  92 */     copy(bytes, 0, bytes.length);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void flush() throws IOException {
/*     */     try {
/*  98 */       if (this.data != null && this.ds != null && this.address != null) {
/*  99 */         if (this.footer != null) {
/* 100 */           copy(this.footer, 0, this.footer.length);
/*     */         }
/* 102 */         DatagramPacket packet = new DatagramPacket(this.data, this.data.length, this.address, this.port);
/* 103 */         this.ds.send(packet);
/*     */       } 
/*     */     } finally {
/* 106 */       this.data = null;
/* 107 */       if (this.header != null) {
/* 108 */         copy(this.header, 0, this.header.length);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void close() throws IOException {
/* 115 */     if (this.ds != null) {
/* 116 */       if (this.data != null) {
/* 117 */         flush();
/*     */       }
/* 119 */       this.ds.close();
/* 120 */       this.ds = null;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void copy(byte[] bytes, int offset, int length) {
/* 125 */     int index = (this.data == null) ? 0 : this.data.length;
/* 126 */     byte[] copy = new byte[length + index];
/* 127 */     if (this.data != null) {
/* 128 */       System.arraycopy(this.data, 0, copy, 0, this.data.length);
/*     */     }
/* 130 */     System.arraycopy(bytes, offset, copy, index, length);
/* 131 */     this.data = copy;
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\net\DatagramOutputStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */