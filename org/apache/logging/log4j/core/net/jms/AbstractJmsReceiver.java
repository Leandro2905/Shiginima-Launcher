/*    */ package org.apache.logging.log4j.core.net.jms;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import javax.jms.JMSException;
/*    */ import javax.jms.Message;
/*    */ import javax.jms.MessageListener;
/*    */ import javax.jms.ObjectMessage;
/*    */ import javax.naming.Context;
/*    */ import javax.naming.NameNotFoundException;
/*    */ import javax.naming.NamingException;
/*    */ import org.apache.logging.log4j.LogManager;
/*    */ import org.apache.logging.log4j.Logger;
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
/*    */ public abstract class AbstractJmsReceiver
/*    */   extends LogEventListener
/*    */   implements MessageListener
/*    */ {
/* 40 */   protected Logger logger = LogManager.getLogger(getClass().getName());
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void onMessage(Message message) {
/*    */     try {
/* 49 */       if (message instanceof ObjectMessage) {
/* 50 */         ObjectMessage objectMessage = (ObjectMessage)message;
/* 51 */         Serializable object = objectMessage.getObject();
/* 52 */         if (object instanceof LogEvent) {
/* 53 */           log((LogEvent)object);
/*    */         } else {
/* 55 */           this.logger.warn("Received message is of type " + object.getClass().getName() + ", was expecting LogEvent.");
/*    */         } 
/*    */       } else {
/* 58 */         this.logger.warn("Received message is of type " + message.getJMSType() + ", was expecting ObjectMessage.");
/*    */       }
/*    */     
/* 61 */     } catch (JMSException jmse) {
/* 62 */       this.logger.error("Exception thrown while processing incoming message.", (Throwable)jmse);
/*    */     } 
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
/*    */   protected Object lookup(Context ctx, String name) throws NamingException {
/*    */     try {
/* 76 */       return ctx.lookup(name);
/* 77 */     } catch (NameNotFoundException e) {
/* 78 */       this.logger.error("Could not find name [" + name + "].");
/* 79 */       throw e;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\net\jms\AbstractJmsReceiver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */