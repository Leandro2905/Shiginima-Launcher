/*     */ package org.apache.logging.log4j.core.net.jms;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.InputStreamReader;
/*     */ import java.nio.charset.Charset;
/*     */ import javax.jms.JMSException;
/*     */ import javax.jms.Queue;
/*     */ import javax.jms.QueueConnection;
/*     */ import javax.jms.QueueConnectionFactory;
/*     */ import javax.jms.QueueReceiver;
/*     */ import javax.jms.QueueSession;
/*     */ import javax.naming.Context;
/*     */ import javax.naming.InitialContext;
/*     */ import javax.naming.NamingException;
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
/*     */ public class JmsQueueReceiver
/*     */   extends AbstractJmsReceiver
/*     */ {
/*     */   public JmsQueueReceiver(String qcfBindingName, String queueBindingName, String username, String password) {
/*     */     try {
/*  51 */       Context ctx = new InitialContext();
/*     */       
/*  53 */       QueueConnectionFactory queueConnectionFactory = (QueueConnectionFactory)lookup(ctx, qcfBindingName);
/*  54 */       QueueConnection queueConnection = queueConnectionFactory.createQueueConnection(username, password);
/*  55 */       queueConnection.start();
/*  56 */       QueueSession queueSession = queueConnection.createQueueSession(false, 1);
/*  57 */       Queue queue = (Queue)ctx.lookup(queueBindingName);
/*  58 */       QueueReceiver queueReceiver = queueSession.createReceiver(queue);
/*  59 */       queueReceiver.setMessageListener(this);
/*  60 */     } catch (JMSException e) {
/*  61 */       this.logger.error("Could not read JMS message.", (Throwable)e);
/*  62 */     } catch (NamingException e) {
/*  63 */       this.logger.error("Could not read JMS message.", e);
/*  64 */     } catch (RuntimeException e) {
/*  65 */       this.logger.error("Could not read JMS message.", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void main(String[] args) throws Exception {
/*     */     String line;
/*  75 */     if (args.length != 4) {
/*  76 */       usage("Wrong number of arguments.");
/*     */     }
/*     */     
/*  79 */     String qcfBindingName = args[0];
/*  80 */     String queueBindingName = args[1];
/*  81 */     String username = args[2];
/*  82 */     String password = args[3];
/*     */     
/*  84 */     new JmsQueueReceiver(qcfBindingName, queueBindingName, username, password);
/*     */     
/*  86 */     Charset enc = Charset.defaultCharset();
/*  87 */     BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in, enc));
/*     */     
/*  89 */     System.out.println("Type \"exit\" to quit JmsQueueReceiver.");
/*     */     do {
/*  91 */       line = stdin.readLine();
/*  92 */     } while (line != null && !line.equalsIgnoreCase("exit"));
/*  93 */     System.out.println("Exiting. Kill the application if it does not exit due to daemon threads.");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void usage(String msg) {
/* 102 */     System.err.println(msg);
/* 103 */     System.err.println("Usage: java " + JmsQueueReceiver.class.getName() + " QueueConnectionFactoryBindingName QueueBindingName username password");
/*     */     
/* 105 */     System.exit(1);
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\net\jms\JmsQueueReceiver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */