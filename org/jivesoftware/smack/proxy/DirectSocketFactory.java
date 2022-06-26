/*    */ package org.jivesoftware.smack.proxy;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.net.InetAddress;
/*    */ import java.net.InetSocketAddress;
/*    */ import java.net.Proxy;
/*    */ import java.net.Socket;
/*    */ import java.net.UnknownHostException;
/*    */ import javax.net.SocketFactory;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class DirectSocketFactory
/*    */   extends SocketFactory
/*    */ {
/*    */   public Socket createSocket(String host, int port) throws IOException, UnknownHostException {
/* 43 */     Socket newSocket = new Socket(Proxy.NO_PROXY);
/* 44 */     newSocket.connect(new InetSocketAddress(host, port));
/* 45 */     return newSocket;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Socket createSocket(String host, int port, InetAddress localHost, int localPort) throws IOException, UnknownHostException {
/* 52 */     return new Socket(host, port, localHost, localPort);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Socket createSocket(InetAddress host, int port) throws IOException {
/* 58 */     Socket newSocket = new Socket(Proxy.NO_PROXY);
/* 59 */     newSocket.connect(new InetSocketAddress(host, port));
/* 60 */     return newSocket;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Socket createSocket(InetAddress address, int port, InetAddress localAddress, int localPort) throws IOException {
/* 67 */     return new Socket(address, port, localAddress, localPort);
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jivesoftware\smack\proxy\DirectSocketFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */