/*     */ package org.apache.logging.log4j.core.net;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.net.InetAddress;
/*     */ import java.net.Socket;
/*     */ import java.net.UnknownHostException;
/*     */ import javax.net.ssl.SSLSocket;
/*     */ import javax.net.ssl.SSLSocketFactory;
/*     */ import org.apache.logging.log4j.Level;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.core.Layout;
/*     */ import org.apache.logging.log4j.core.appender.ManagerFactory;
/*     */ import org.apache.logging.log4j.core.net.ssl.SslConfiguration;
/*     */ import org.apache.logging.log4j.util.Strings;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SslSocketManager
/*     */   extends TcpSocketManager
/*     */ {
/*     */   public static final int DEFAULT_PORT = 6514;
/*  41 */   private static final SslSocketManagerFactory FACTORY = new SslSocketManagerFactory();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final SslConfiguration sslConfig;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SslSocketManager(String name, OutputStream os, Socket sock, SslConfiguration sslConfig, InetAddress inetAddress, String host, int port, int delay, boolean immediateFail, Layout<? extends Serializable> layout) {
/*  59 */     super(name, os, sock, inetAddress, host, port, delay, immediateFail, layout);
/*  60 */     this.sslConfig = sslConfig;
/*     */   }
/*     */   
/*     */   private static class SslFactoryData
/*     */   {
/*     */     protected SslConfiguration sslConfig;
/*     */     private final String host;
/*     */     private final int port;
/*     */     private final int delay;
/*     */     private final boolean immediateFail;
/*     */     private final Layout<? extends Serializable> layout;
/*     */     
/*     */     public SslFactoryData(SslConfiguration sslConfig, String host, int port, int delay, boolean immediateFail, Layout<? extends Serializable> layout) {
/*  73 */       this.host = host;
/*  74 */       this.port = port;
/*  75 */       this.delay = delay;
/*  76 */       this.immediateFail = immediateFail;
/*  77 */       this.layout = layout;
/*  78 */       this.sslConfig = sslConfig;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static SslSocketManager getSocketManager(SslConfiguration sslConfig, String host, int port, int delay, boolean immediateFail, Layout<? extends Serializable> layout) {
/*  84 */     if (Strings.isEmpty(host)) {
/*  85 */       throw new IllegalArgumentException("A host name is required");
/*     */     }
/*  87 */     if (port <= 0) {
/*  88 */       port = 6514;
/*     */     }
/*  90 */     if (delay == 0) {
/*  91 */       delay = 30000;
/*     */     }
/*  93 */     return (SslSocketManager)getManager("TLS:" + host + ':' + port, new SslFactoryData(sslConfig, host, port, delay, immediateFail, layout), FACTORY);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected Socket createSocket(String host, int port) throws IOException {
/*  99 */     SSLSocketFactory socketFactory = createSslSocketFactory(this.sslConfig);
/* 100 */     return socketFactory.createSocket(host, port);
/*     */   }
/*     */ 
/*     */   
/*     */   private static SSLSocketFactory createSslSocketFactory(SslConfiguration sslConf) {
/*     */     SSLSocketFactory socketFactory;
/* 106 */     if (sslConf != null) {
/* 107 */       socketFactory = sslConf.getSslSocketFactory();
/*     */     } else {
/* 109 */       socketFactory = (SSLSocketFactory)SSLSocketFactory.getDefault();
/*     */     } 
/*     */     
/* 112 */     return socketFactory;
/*     */   }
/*     */   
/*     */   private static class SslSocketManagerFactory implements ManagerFactory<SslSocketManager, SslFactoryData> {
/*     */     private SslSocketManagerFactory() {}
/*     */     
/*     */     private class TlsSocketManagerFactoryException extends Exception {
/*     */       private static final long serialVersionUID = 1L;
/*     */       
/*     */       private TlsSocketManagerFactoryException() {}
/*     */     }
/*     */     
/*     */     public SslSocketManager createManager(String name, SslSocketManager.SslFactoryData data) {
/* 125 */       InetAddress inetAddress = null;
/* 126 */       OutputStream os = null;
/* 127 */       Socket socket = null;
/*     */       
/*     */       try {
/* 130 */         inetAddress = resolveAddress(data.host);
/* 131 */         socket = createSocket(data);
/* 132 */         os = socket.getOutputStream();
/* 133 */         checkDelay(data.delay, os);
/*     */       }
/* 135 */       catch (IOException e) {
/* 136 */         SslSocketManager.LOGGER.error("SslSocketManager ({})", new Object[] { name, e });
/* 137 */         os = new ByteArrayOutputStream();
/*     */       }
/* 139 */       catch (TlsSocketManagerFactoryException e) {
/* 140 */         SslSocketManager.LOGGER.catching(Level.DEBUG, e);
/* 141 */         return null;
/*     */       } 
/* 143 */       return createManager(name, os, socket, data.sslConfig, inetAddress, data.host, data.port, data.delay, data.immediateFail, data.layout);
/*     */     }
/*     */ 
/*     */     
/*     */     private InetAddress resolveAddress(String hostName) throws TlsSocketManagerFactoryException {
/*     */       InetAddress address;
/*     */       try {
/* 150 */         address = InetAddress.getByName(hostName);
/* 151 */       } catch (UnknownHostException ex) {
/* 152 */         SslSocketManager.LOGGER.error("Could not find address of {}", new Object[] { hostName, ex });
/* 153 */         throw new TlsSocketManagerFactoryException();
/*     */       } 
/*     */       
/* 156 */       return address;
/*     */     }
/*     */     
/*     */     private void checkDelay(int delay, OutputStream os) throws TlsSocketManagerFactoryException {
/* 160 */       if (delay == 0 && os == null) {
/* 161 */         throw new TlsSocketManagerFactoryException();
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private Socket createSocket(SslSocketManager.SslFactoryData data) throws IOException {
/* 169 */       SSLSocketFactory socketFactory = SslSocketManager.createSslSocketFactory(data.sslConfig);
/* 170 */       SSLSocket socket = (SSLSocket)socketFactory.createSocket(data.host, data.port);
/* 171 */       return socket;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     private SslSocketManager createManager(String name, OutputStream os, Socket socket, SslConfiguration sslConfig, InetAddress inetAddress, String host, int port, int delay, boolean immediateFail, Layout<? extends Serializable> layout) {
/* 177 */       return new SslSocketManager(name, os, socket, sslConfig, inetAddress, host, port, delay, immediateFail, layout);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\net\SslSocketManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */