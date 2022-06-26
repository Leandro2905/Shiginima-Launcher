/*     */ package org.apache.logging.log4j.core.net.jms;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.InputStreamReader;
/*     */ import java.nio.charset.Charset;
/*     */ import javax.jms.JMSException;
/*     */ import javax.jms.Topic;
/*     */ import javax.jms.TopicConnection;
/*     */ import javax.jms.TopicConnectionFactory;
/*     */ import javax.jms.TopicSession;
/*     */ import javax.jms.TopicSubscriber;
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
/*     */ public class JmsTopicReceiver
/*     */   extends AbstractJmsReceiver
/*     */ {
/*     */   public JmsTopicReceiver(String tcfBindingName, String topicBindingName, String username, String password) {
/*     */     try {
/*  50 */       Context ctx = new InitialContext();
/*     */       
/*  52 */       TopicConnectionFactory topicConnectionFactory = (TopicConnectionFactory)lookup(ctx, tcfBindingName);
/*  53 */       TopicConnection topicConnection = topicConnectionFactory.createTopicConnection(username, password);
/*  54 */       topicConnection.start();
/*  55 */       TopicSession topicSession = topicConnection.createTopicSession(false, 1);
/*  56 */       Topic topic = (Topic)ctx.lookup(topicBindingName);
/*  57 */       TopicSubscriber topicSubscriber = topicSession.createSubscriber(topic);
/*  58 */       topicSubscriber.setMessageListener(this);
/*  59 */     } catch (JMSException e) {
/*  60 */       this.logger.error("Could not read JMS message.", (Throwable)e);
/*  61 */     } catch (NamingException e) {
/*  62 */       this.logger.error("Could not read JMS message.", e);
/*  63 */     } catch (RuntimeException e) {
/*  64 */       this.logger.error("Could not read JMS message.", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void main(String[] args) throws Exception {
/*     */     String line;
/*  74 */     if (args.length != 4) {
/*  75 */       usage("Wrong number of arguments.");
/*     */     }
/*     */     
/*  78 */     String tcfBindingName = args[0];
/*  79 */     String topicBindingName = args[1];
/*  80 */     String username = args[2];
/*  81 */     String password = args[3];
/*     */     
/*  83 */     new JmsTopicReceiver(tcfBindingName, topicBindingName, username, password);
/*     */     
/*  85 */     Charset enc = Charset.defaultCharset();
/*  86 */     BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in, enc));
/*     */     
/*  88 */     System.out.println("Type \"exit\" to quit JmsTopicReceiver.");
/*     */     do {
/*  90 */       line = stdin.readLine();
/*  91 */     } while (line != null && !line.equalsIgnoreCase("exit"));
/*  92 */     System.out.println("Exiting. Kill the application if it does not exit due to daemon threads.");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void usage(String msg) {
/* 100 */     System.err.println(msg);
/* 101 */     System.err.println("Usage: java " + JmsTopicReceiver.class.getName() + " TopicConnectionFactoryBindingName TopicBindingName username password");
/*     */     
/* 103 */     System.exit(1);
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\net\jms\JmsTopicReceiver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */