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
/*     */ public class Socks5ProxySocketFactory
/*     */   extends SocketFactory
/*     */ {
/*     */   private ProxyInfo proxy;
/*     */   
/*     */   public Socks5ProxySocketFactory(ProxyInfo proxy) {
/*  39 */     this.proxy = proxy;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Socket createSocket(String host, int port) throws IOException, UnknownHostException {
/*  45 */     return socks5ProxifiedSocket(host, port);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Socket createSocket(String host, int port, InetAddress localHost, int localPort) throws IOException, UnknownHostException {
/*  53 */     return socks5ProxifiedSocket(host, port);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Socket createSocket(InetAddress host, int port) throws IOException {
/*  61 */     return socks5ProxifiedSocket(host.getHostAddress(), port);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Socket createSocket(InetAddress address, int port, InetAddress localAddress, int localPort) throws IOException {
/*  70 */     return socks5ProxifiedSocket(address.getHostAddress(), port);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Socket socks5ProxifiedSocket(String host, int port) throws IOException {
/*  77 */     Socket socket = null;
/*  78 */     InputStream in = null;
/*  79 */     OutputStream out = null;
/*  80 */     String proxy_host = this.proxy.getProxyAddress();
/*  81 */     int proxy_port = this.proxy.getProxyPort();
/*  82 */     String user = this.proxy.getProxyUsername();
/*  83 */     String passwd = this.proxy.getProxyPassword();
/*     */ 
/*     */     
/*     */     try {
/*  87 */       socket = new Socket(proxy_host, proxy_port);
/*  88 */       in = socket.getInputStream();
/*  89 */       out = socket.getOutputStream();
/*     */       
/*  91 */       socket.setTcpNoDelay(true);
/*     */       
/*  93 */       byte[] buf = new byte[1024];
/*  94 */       int index = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 117 */       buf[index++] = 5;
/*     */       
/* 119 */       buf[index++] = 2;
/* 120 */       buf[index++] = 0;
/* 121 */       buf[index++] = 2;
/*     */       
/* 123 */       out.write(buf, 0, index);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 136 */       fill(in, buf, 2);
/*     */       
/* 138 */       boolean check = false;
/* 139 */       switch (buf[1] & 0xFF) {
/*     */         
/*     */         case 0:
/* 142 */           check = true;
/*     */           break;
/*     */         case 2:
/* 145 */           if (user == null || passwd == null) {
/*     */             break;
/*     */           }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 169 */           index = 0;
/* 170 */           buf[index++] = 1;
/* 171 */           buf[index++] = (byte)user.length();
/* 172 */           System.arraycopy(user.getBytes(), 0, buf, index, user.length());
/*     */           
/* 174 */           index += user.length();
/* 175 */           buf[index++] = (byte)passwd.length();
/* 176 */           System.arraycopy(passwd.getBytes(), 0, buf, index, passwd.length());
/*     */           
/* 178 */           index += passwd.length();
/*     */           
/* 180 */           out.write(buf, 0, index);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 197 */           fill(in, buf, 2);
/* 198 */           if (buf[1] == 0)
/*     */           {
/* 200 */             check = true;
/*     */           }
/*     */           break;
/*     */       } 
/*     */ 
/*     */       
/* 206 */       if (!check) {
/*     */ 
/*     */         
/*     */         try {
/* 210 */           socket.close();
/*     */         }
/* 212 */         catch (Exception eee) {}
/*     */ 
/*     */         
/* 215 */         throw new ProxyException(ProxyInfo.ProxyType.SOCKS5, "fail in SOCKS5 proxy");
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 245 */       index = 0;
/* 246 */       buf[index++] = 5;
/* 247 */       buf[index++] = 1;
/* 248 */       buf[index++] = 0;
/*     */       
/* 250 */       byte[] hostb = host.getBytes();
/* 251 */       int len = hostb.length;
/* 252 */       buf[index++] = 3;
/* 253 */       buf[index++] = (byte)len;
/* 254 */       System.arraycopy(hostb, 0, buf, index, len);
/* 255 */       index += len;
/* 256 */       buf[index++] = (byte)(port >>> 8);
/* 257 */       buf[index++] = (byte)(port & 0xFF);
/*     */       
/* 259 */       out.write(buf, 0, index);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 297 */       fill(in, buf, 4);
/*     */       
/* 299 */       if (buf[1] != 0) {
/*     */ 
/*     */         
/*     */         try {
/* 303 */           socket.close();
/*     */         }
/* 305 */         catch (Exception eee) {}
/*     */ 
/*     */         
/* 308 */         throw new ProxyException(ProxyInfo.ProxyType.SOCKS5, "server returns " + buf[1]);
/*     */       } 
/*     */ 
/*     */       
/* 312 */       switch (buf[3] & 0xFF) {
/*     */ 
/*     */         
/*     */         case 1:
/* 316 */           fill(in, buf, 6);
/*     */           break;
/*     */         
/*     */         case 3:
/* 320 */           fill(in, buf, 1);
/*     */           
/* 322 */           fill(in, buf, (buf[0] & 0xFF) + 2);
/*     */           break;
/*     */         
/*     */         case 4:
/* 326 */           fill(in, buf, 18);
/*     */           break;
/*     */       } 
/*     */       
/* 330 */       return socket;
/*     */     
/*     */     }
/* 333 */     catch (RuntimeException e) {
/*     */       
/* 335 */       throw e;
/*     */     }
/* 337 */     catch (Exception e) {
/*     */ 
/*     */       
/*     */       try {
/* 341 */         if (socket != null)
/*     */         {
/* 343 */           socket.close();
/*     */         }
/*     */       }
/* 346 */       catch (Exception eee) {}
/*     */ 
/*     */       
/* 349 */       String message = "ProxySOCKS5: " + e.toString();
/* 350 */       if (e instanceof Throwable)
/*     */       {
/* 352 */         throw new ProxyException(ProxyInfo.ProxyType.SOCKS5, message, e);
/*     */       }
/*     */       
/* 355 */       throw new IOException(message);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void fill(InputStream in, byte[] buf, int len) throws IOException {
/* 362 */     int s = 0;
/* 363 */     while (s < len) {
/*     */       
/* 365 */       int i = in.read(buf, s, len - s);
/* 366 */       if (i <= 0)
/*     */       {
/* 368 */         throw new ProxyException(ProxyInfo.ProxyType.SOCKS5, "stream is closed");
/*     */       }
/*     */       
/* 371 */       s += i;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jivesoftware\smack\proxy\Socks5ProxySocketFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */