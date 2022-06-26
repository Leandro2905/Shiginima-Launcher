/*    */ package org.apache.logging.log4j.core.net.server;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import org.apache.logging.log4j.core.net.ssl.SslConfiguration;
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
/*    */ public class SecureTcpSocketServer<T extends InputStream>
/*    */   extends TcpSocketServer<T>
/*    */ {
/*    */   public SecureTcpSocketServer(int port, LogEventBridge<T> logEventInput, SslConfiguration sslConfig) throws IOException {
/* 34 */     super(port, logEventInput, sslConfig.getSslServerSocketFactory().createServerSocket(port));
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\net\server\SecureTcpSocketServer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */