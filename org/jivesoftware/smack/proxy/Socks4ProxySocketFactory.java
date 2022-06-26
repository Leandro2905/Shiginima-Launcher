/*     */ package org.jivesoftware.smack.proxy;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.net.InetAddress;
/*     */ import java.net.Socket;
/*     */ import java.net.UnknownHostException;
/*     */ import javax.net.SocketFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Socks4ProxySocketFactory
/*     */   extends SocketFactory
/*     */ {
/*     */   private ProxyInfo proxy;
/*     */   
/*     */   public Socks4ProxySocketFactory(ProxyInfo proxy) {
/*  40 */     this.proxy = proxy;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Socket createSocket(String host, int port) throws IOException, UnknownHostException {
/*  46 */     return socks4ProxifiedSocket(host, port);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Socket createSocket(String host, int port, InetAddress localHost, int localPort) throws IOException, UnknownHostException {
/*  54 */     return socks4ProxifiedSocket(host, port);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Socket createSocket(InetAddress host, int port) throws IOException {
/*  60 */     return socks4ProxifiedSocket(host.getHostAddress(), port);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Socket createSocket(InetAddress address, int port, InetAddress localAddress, int localPort) throws IOException {
/*  67 */     return socks4ProxifiedSocket(address.getHostAddress(), port);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Socket socks4ProxifiedSocket(String host, int port) throws IOException {
/*  75 */     Socket socket = null;
/*  76 */     InputStream in = null;
/*  77 */     OutputStream out = null;
/*  78 */     String proxy_host = this.proxy.getProxyAddress();
/*  79 */     int proxy_port = this.proxy.getProxyPort();
/*  80 */     String user = this.proxy.getProxyUsername();
/*     */ 
/*     */     
/*     */     try {
/*  84 */       socket = new Socket(proxy_host, proxy_port);
/*  85 */       in = socket.getInputStream();
/*  86 */       out = socket.getOutputStream();
/*  87 */       socket.setTcpNoDelay(true);
/*     */       
/*  89 */       byte[] buf = new byte[1024];
/*  90 */       int index = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 110 */       index = 0;
/* 111 */       buf[index++] = 4;
/* 112 */       buf[index++] = 1;
/*     */       
/* 114 */       buf[index++] = (byte)(port >>> 8);
/* 115 */       buf[index++] = (byte)(port & 0xFF);
/*     */ 
/*     */       
/*     */       try {
/* 119 */         InetAddress addr = InetAddress.getByName(host);
/* 120 */         byte[] byteAddress = addr.getAddress();
/* 121 */         for (int i = 0; i < byteAddress.length; i++)
/*     */         {
/* 123 */           buf[index++] = byteAddress[i];
/*     */         }
/*     */       }
/* 126 */       catch (UnknownHostException uhe) {
/*     */         
/* 128 */         throw new ProxyException(ProxyInfo.ProxyType.SOCKS4, uhe.toString(), uhe);
/*     */       } 
/*     */ 
/*     */       
/* 132 */       if (user != null) {
/*     */         
/* 134 */         System.arraycopy(user.getBytes(), 0, buf, index, user.length());
/* 135 */         index += user.length();
/*     */       } 
/* 137 */       buf[index++] = 0;
/* 138 */       out.write(buf, 0, index);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 167 */       int len = 6;
/* 168 */       int s = 0;
/* 169 */       while (s < len) {
/*     */         
/* 171 */         int i = in.read(buf, s, len - s);
/* 172 */         if (i <= 0)
/*     */         {
/* 174 */           throw new ProxyException(ProxyInfo.ProxyType.SOCKS4, "stream is closed");
/*     */         }
/*     */         
/* 177 */         s += i;
/*     */       } 
/* 179 */       if (buf[0] != 0)
/*     */       {
/* 181 */         throw new ProxyException(ProxyInfo.ProxyType.SOCKS4, "server returns VN " + buf[0]);
/*     */       }
/*     */       
/* 184 */       if (buf[1] != 90) {
/*     */ 
/*     */         
/*     */         try {
/* 188 */           socket.close();
/*     */         }
/* 190 */         catch (Exception eee) {}
/*     */ 
/*     */         
/* 193 */         String message = "ProxySOCKS4: server returns CD " + buf[1];
/* 194 */         throw new ProxyException(ProxyInfo.ProxyType.SOCKS4, message);
/*     */       } 
/* 196 */       byte[] temp = new byte[2];
/* 197 */       in.read(temp, 0, 2);
/* 198 */       return socket;
/*     */     }
/* 200 */     catch (RuntimeException e) {
/*     */       
/* 202 */       throw e;
/*     */     }
/* 204 */     catch (Exception e) {
/*     */ 
/*     */       
/*     */       try {
/* 208 */         if (socket != null) socket.close();
/*     */       
/* 210 */       } catch (Exception eee) {}
/*     */ 
/*     */       
/* 213 */       throw new ProxyException(ProxyInfo.ProxyType.SOCKS4, e.toString());
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jivesoftware\smack\proxy\Socks4ProxySocketFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */