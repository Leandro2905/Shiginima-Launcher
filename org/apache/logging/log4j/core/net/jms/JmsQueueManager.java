/*     */ package org.apache.logging.log4j.core.net.jms;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import javax.jms.JMSException;
/*     */ import javax.jms.MessageProducer;
/*     */ import javax.jms.Queue;
/*     */ import javax.jms.QueueConnection;
/*     */ import javax.jms.QueueConnectionFactory;
/*     */ import javax.jms.QueueSender;
/*     */ import javax.jms.QueueSession;
/*     */ import javax.jms.Session;
/*     */ import javax.naming.Context;
/*     */ import javax.naming.NamingException;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.core.appender.ManagerFactory;
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
/*     */ public class JmsQueueManager
/*     */   extends AbstractJmsManager
/*     */ {
/*  38 */   private static final JMSQueueManagerFactory FACTORY = new JMSQueueManagerFactory();
/*     */ 
/*     */   
/*     */   private QueueInfo info;
/*     */ 
/*     */   
/*     */   private final String factoryBindingName;
/*     */ 
/*     */   
/*     */   private final String queueBindingName;
/*     */ 
/*     */   
/*     */   private final String userName;
/*     */ 
/*     */   
/*     */   private final String password;
/*     */ 
/*     */   
/*     */   private final Context context;
/*     */ 
/*     */   
/*     */   protected JmsQueueManager(String name, Context context, String factoryBindingName, String queueBindingName, String userName, String password, QueueInfo info) {
/*  60 */     super(name);
/*  61 */     this.context = context;
/*  62 */     this.factoryBindingName = factoryBindingName;
/*  63 */     this.queueBindingName = queueBindingName;
/*  64 */     this.userName = userName;
/*  65 */     this.password = password;
/*  66 */     this.info = info;
/*     */   }
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
/*     */   public static JmsQueueManager getJmsQueueManager(String factoryName, String providerURL, String urlPkgPrefixes, String securityPrincipalName, String securityCredentials, String factoryBindingName, String queueBindingName, String userName, String password) {
/*  89 */     if (factoryBindingName == null) {
/*  90 */       LOGGER.error("No factory name provided for JmsQueueManager");
/*  91 */       return null;
/*     */     } 
/*  93 */     if (queueBindingName == null) {
/*  94 */       LOGGER.error("No topic name provided for JmsQueueManager");
/*  95 */       return null;
/*     */     } 
/*     */     
/*  98 */     String name = "JMSQueue:" + factoryBindingName + '.' + queueBindingName;
/*  99 */     return (JmsQueueManager)getManager(name, FACTORY, new FactoryData(factoryName, providerURL, urlPkgPrefixes, securityPrincipalName, securityCredentials, factoryBindingName, queueBindingName, userName, password));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void send(Serializable object) throws Exception {
/* 105 */     if (this.info == null) {
/* 106 */       this.info = connect(this.context, this.factoryBindingName, this.queueBindingName, this.userName, this.password, false);
/*     */     }
/*     */     try {
/* 109 */       send(object, (Session)this.info.session, (MessageProducer)this.info.sender);
/* 110 */     } catch (Exception ex) {
/* 111 */       cleanup(true);
/* 112 */       throw ex;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void releaseSub() {
/* 118 */     if (this.info != null) {
/* 119 */       cleanup(false);
/*     */     }
/*     */   }
/*     */   
/*     */   private void cleanup(boolean quiet) {
/*     */     try {
/* 125 */       this.info.session.close();
/* 126 */     } catch (Exception e) {
/* 127 */       if (!quiet) {
/* 128 */         LOGGER.error("Error closing session for " + getName(), e);
/*     */       }
/*     */     } 
/*     */     try {
/* 132 */       this.info.conn.close();
/* 133 */     } catch (Exception e) {
/* 134 */       if (!quiet) {
/* 135 */         LOGGER.error("Error closing connection for " + getName(), e);
/*     */       }
/*     */     } 
/* 138 */     this.info = null;
/*     */   }
/*     */ 
/*     */   
/*     */   private static class FactoryData
/*     */   {
/*     */     private final String factoryName;
/*     */     
/*     */     private final String providerURL;
/*     */     
/*     */     private final String urlPkgPrefixes;
/*     */     
/*     */     private final String securityPrincipalName;
/*     */     
/*     */     private final String securityCredentials;
/*     */     private final String factoryBindingName;
/*     */     private final String queueBindingName;
/*     */     private final String userName;
/*     */     private final String password;
/*     */     
/*     */     public FactoryData(String factoryName, String providerURL, String urlPkgPrefixes, String securityPrincipalName, String securityCredentials, String factoryBindingName, String queueBindingName, String userName, String password) {
/* 159 */       this.factoryName = factoryName;
/* 160 */       this.providerURL = providerURL;
/* 161 */       this.urlPkgPrefixes = urlPkgPrefixes;
/* 162 */       this.securityPrincipalName = securityPrincipalName;
/* 163 */       this.securityCredentials = securityCredentials;
/* 164 */       this.factoryBindingName = factoryBindingName;
/* 165 */       this.queueBindingName = queueBindingName;
/* 166 */       this.userName = userName;
/* 167 */       this.password = password;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static QueueInfo connect(Context context, String factoryBindingName, String queueBindingName, String userName, String password, boolean suppress) throws Exception {
/*     */     try {
/*     */       QueueConnection conn;
/* 175 */       QueueConnectionFactory factory = (QueueConnectionFactory)lookup(context, factoryBindingName);
/*     */       
/* 177 */       if (userName != null) {
/* 178 */         conn = factory.createQueueConnection(userName, password);
/*     */       } else {
/* 180 */         conn = factory.createQueueConnection();
/*     */       } 
/* 182 */       QueueSession sess = conn.createQueueSession(false, 1);
/* 183 */       Queue queue = (Queue)lookup(context, queueBindingName);
/* 184 */       QueueSender sender = sess.createSender(queue);
/* 185 */       conn.start();
/* 186 */       return new QueueInfo(conn, sess, sender);
/* 187 */     } catch (NamingException ex) {
/* 188 */       LOGGER.warn("Unable to locate connection factory " + factoryBindingName, ex);
/* 189 */       if (!suppress) {
/* 190 */         throw ex;
/*     */       }
/* 192 */     } catch (JMSException ex) {
/* 193 */       LOGGER.warn("Unable to create connection to queue " + queueBindingName, (Throwable)ex);
/* 194 */       if (!suppress) {
/* 195 */         throw ex;
/*     */       }
/*     */     } 
/* 198 */     return null;
/*     */   }
/*     */   
/*     */   private static class QueueInfo
/*     */   {
/*     */     private final QueueConnection conn;
/*     */     private final QueueSession session;
/*     */     private final QueueSender sender;
/*     */     
/*     */     public QueueInfo(QueueConnection conn, QueueSession session, QueueSender sender) {
/* 208 */       this.conn = conn;
/* 209 */       this.session = session;
/* 210 */       this.sender = sender;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static class JMSQueueManagerFactory
/*     */     implements ManagerFactory<JmsQueueManager, FactoryData>
/*     */   {
/*     */     private JMSQueueManagerFactory() {}
/*     */     
/*     */     public JmsQueueManager createManager(String name, JmsQueueManager.FactoryData data) {
/*     */       try {
/* 222 */         Context ctx = AbstractJmsManager.createContext(data.factoryName, data.providerURL, data.urlPkgPrefixes, data.securityPrincipalName, data.securityCredentials);
/*     */         
/* 224 */         JmsQueueManager.QueueInfo info = JmsQueueManager.connect(ctx, data.factoryBindingName, data.queueBindingName, data.userName, data.password, true);
/*     */         
/* 226 */         return new JmsQueueManager(name, ctx, data.factoryBindingName, data.queueBindingName, data.userName, data.password, info);
/*     */       }
/* 228 */       catch (NamingException ex) {
/* 229 */         JmsQueueManager.LOGGER.error("Unable to locate resource", ex);
/* 230 */       } catch (Exception ex) {
/* 231 */         JmsQueueManager.LOGGER.error("Unable to connect", ex);
/*     */       } 
/*     */       
/* 234 */       return null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\net\jms\JmsQueueManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */