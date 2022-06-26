/*     */ package org.jivesoftware.smack.proxy;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.StringReader;
/*     */ import java.net.InetAddress;
/*     */ import java.net.Socket;
/*     */ import java.net.UnknownHostException;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import javax.net.SocketFactory;
/*     */ import org.jivesoftware.smack.util.stringencoder.Base64;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class HTTPProxySocketFactory
/*     */   extends SocketFactory
/*     */ {
/*     */   private ProxyInfo proxy;
/*     */   
/*     */   public HTTPProxySocketFactory(ProxyInfo proxy) {
/*  48 */     this.proxy = proxy;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Socket createSocket(String host, int port) throws IOException, UnknownHostException {
/*  54 */     return httpProxifiedSocket(host, port);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Socket createSocket(String host, int port, InetAddress localHost, int localPort) throws IOException, UnknownHostException {
/*  61 */     return httpProxifiedSocket(host, port);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Socket createSocket(InetAddress host, int port) throws IOException {
/*  67 */     return httpProxifiedSocket(host.getHostAddress(), port);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Socket createSocket(InetAddress address, int port, InetAddress localAddress, int localPort) throws IOException {
/*  75 */     return httpProxifiedSocket(address.getHostAddress(), port);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private Socket httpProxifiedSocket(String host, int port) throws IOException {
/*  81 */     String proxyLine, proxyhost = this.proxy.getProxyAddress();
/*  82 */     int proxyPort = this.proxy.getProxyPort();
/*     */     
/*  84 */     Socket socket = new Socket(proxyhost, proxyPort);
/*  85 */     String hostport = "CONNECT " + host + ":" + port;
/*     */     
/*  87 */     String username = this.proxy.getProxyUsername();
/*  88 */     if (username == null) {
/*     */       
/*  90 */       proxyLine = "";
/*     */     }
/*     */     else {
/*     */       
/*  94 */       String password = this.proxy.getProxyPassword();
/*  95 */       proxyLine = "\r\nProxy-Authorization: Basic " + Base64.encode(username + ":" + password);
/*     */     } 
/*  97 */     socket.getOutputStream().write((hostport + " HTTP/1.1\r\nHost: " + hostport + proxyLine + "\r\n\r\n").getBytes("UTF-8"));
/*     */ 
/*     */     
/* 100 */     InputStream in = socket.getInputStream();
/* 101 */     StringBuilder got = new StringBuilder(100);
/* 102 */     int nlchars = 0;
/*     */ 
/*     */     
/*     */     do {
/* 106 */       char c = (char)in.read();
/* 107 */       got.append(c);
/* 108 */       if (got.length() > 1024)
/*     */       {
/* 110 */         throw new ProxyException(ProxyInfo.ProxyType.HTTP, "Recieved header of >1024 characters from " + proxyhost + ", cancelling connection");
/*     */       }
/*     */ 
/*     */       
/* 114 */       if (c == -1)
/*     */       {
/* 116 */         throw new ProxyException(ProxyInfo.ProxyType.HTTP);
/*     */       }
/* 118 */       if ((nlchars == 0 || nlchars == 2) && c == '\r') {
/*     */         
/* 120 */         nlchars++;
/*     */       }
/* 122 */       else if ((nlchars == 1 || nlchars == 3) && c == '\n') {
/*     */         
/* 124 */         nlchars++;
/*     */       }
/*     */       else {
/*     */         
/* 128 */         nlchars = 0;
/*     */       } 
/* 130 */     } while (nlchars != 4);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 136 */     if (nlchars != 4)
/*     */     {
/* 138 */       throw new ProxyException(ProxyInfo.ProxyType.HTTP, "Never received blank line from " + proxyhost + ", cancelling connection");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 143 */     String gotstr = got.toString();
/*     */     
/* 145 */     BufferedReader br = new BufferedReader(new StringReader(gotstr));
/* 146 */     String response = br.readLine();
/*     */     
/* 148 */     if (response == null)
/*     */     {
/* 150 */       throw new ProxyException(ProxyInfo.ProxyType.HTTP, "Empty proxy response from " + proxyhost + ", cancelling");
/*     */     }
/*     */ 
/*     */     
/* 154 */     Matcher m = RESPONSE_PATTERN.matcher(response);
/* 155 */     if (!m.matches())
/*     */     {
/* 157 */       throw new ProxyException(ProxyInfo.ProxyType.HTTP, "Unexpected proxy response from " + proxyhost + ": " + response);
/*     */     }
/*     */ 
/*     */     
/* 161 */     int code = Integer.parseInt(m.group(1));
/*     */     
/* 163 */     if (code != 200)
/*     */     {
/* 165 */       throw new ProxyException(ProxyInfo.ProxyType.HTTP);
/*     */     }
/*     */     
/* 168 */     return socket;
/*     */   }
/*     */   
/* 171 */   private static final Pattern RESPONSE_PATTERN = Pattern.compile("HTTP/\\S+\\s(\\d+)\\s(.*)\\s*");
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jivesoftware\smack\proxy\HTTPProxySocketFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */