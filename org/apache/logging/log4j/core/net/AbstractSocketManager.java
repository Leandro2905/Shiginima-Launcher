/*    */ package org.apache.logging.log4j.core.net;
/*    */ 
/*    */ import java.io.OutputStream;
/*    */ import java.io.Serializable;
/*    */ import java.net.InetAddress;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import org.apache.logging.log4j.core.Layout;
/*    */ import org.apache.logging.log4j.core.appender.OutputStreamManager;
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
/*    */ public abstract class AbstractSocketManager
/*    */   extends OutputStreamManager
/*    */ {
/*    */   protected final InetAddress inetAddress;
/*    */   protected final String host;
/*    */   protected final int port;
/*    */   
/*    */   public AbstractSocketManager(String name, OutputStream os, InetAddress inetAddress, String host, int port, Layout<? extends Serializable> layout) {
/* 58 */     super(os, name, layout);
/* 59 */     this.inetAddress = inetAddress;
/* 60 */     this.host = host;
/* 61 */     this.port = port;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Map<String, String> getContentFormat() {
/* 73 */     Map<String, String> result = new HashMap<String, String>(super.getContentFormat());
/* 74 */     result.put("port", Integer.toString(this.port));
/* 75 */     result.put("address", this.inetAddress.getHostAddress());
/*    */     
/* 77 */     return result;
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\net\AbstractSocketManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */