/*    */ package org.apache.logging.log4j.core.net.server;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.io.ObjectInputStream;
/*    */ import org.apache.logging.log4j.core.LogEvent;
/*    */ import org.apache.logging.log4j.core.LogEventListener;
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
/*    */ public class ObjectInputStreamLogEventBridge
/*    */   extends AbstractLogEventBridge<ObjectInputStream>
/*    */ {
/*    */   public void logEvents(ObjectInputStream inputStream, LogEventListener logEventListener) throws IOException {
/*    */     try {
/* 35 */       logEventListener.log((LogEvent)inputStream.readObject());
/* 36 */     } catch (ClassNotFoundException e) {
/* 37 */       throw new IOException(e);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public ObjectInputStream wrapStream(InputStream inputStream) throws IOException {
/* 43 */     return new ObjectInputStream(inputStream);
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\net\server\ObjectInputStreamLogEventBridge.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */