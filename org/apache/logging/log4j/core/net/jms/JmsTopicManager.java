/*     */ package org.apache.logging.log4j.core.net.jms;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import javax.jms.JMSException;
/*     */ import javax.jms.MessageProducer;
/*     */ import javax.jms.Session;
/*     */ import javax.jms.Topic;
/*     */ import javax.jms.TopicConnection;
/*     */ import javax.jms.TopicConnectionFactory;
/*     */ import javax.jms.TopicPublisher;
/*     */ import javax.jms.TopicSession;
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
/*     */ public class JmsTopicManager
/*     */   extends AbstractJmsManager
/*     */ {
/*  38 */   private static final JMSTopicManagerFactory FACTORY = new JMSTopicManagerFactory();
/*     */ 
/*     */   
/*     */   private TopicInfo info;
/*     */ 
/*     */   
/*     */   private final String factoryBindingName;
/*     */ 
/*     */   
/*     */   private final String topicBindingName;
/*     */ 
/*     */   
/*     */   private final String userName;
/*     */ 
/*     */   
/*     */   private final String password;
/*     */   
/*     */   private final Context context;
/*     */ 
/*     */   
/*     */   protected JmsTopicManager(String name, Context context, String factoryBindingName, String topicBindingName, String userName, String password, TopicInfo info) {
/*  59 */     super(name);
/*  60 */     this.context = context;
/*  61 */     this.factoryBindingName = factoryBindingName;
/*  62 */     this.topicBindingName = topicBindingName;
/*  63 */     this.userName = userName;
/*  64 */     this.password = password;
/*  65 */     this.info = info;
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
/*     */   public static JmsTopicManager getJmsTopicManager(String factoryName, String providerURL, String urlPkgPrefixes, String securityPrincipalName, String securityCredentials, String factoryBindingName, String topicBindingName, String userName, String password) {
/*  88 */     if (factoryBindingName == null) {
/*  89 */       LOGGER.error("No factory name provided for JmsTopicManager");
/*  90 */       return null;
/*     */     } 
/*  92 */     if (topicBindingName == null) {
/*  93 */       LOGGER.error("No topic name provided for JmsTopicManager");
/*  94 */       return null;
/*     */     } 
/*     */     
/*  97 */     String name = "JMSTopic:" + factoryBindingName + '.' + topicBindingName;
/*  98 */     return (JmsTopicManager)getManager(name, FACTORY, new FactoryData(factoryName, providerURL, urlPkgPrefixes, securityPrincipalName, securityCredentials, factoryBindingName, topicBindingName, userName, password));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void send(Serializable object) throws Exception {
/* 105 */     if (this.info == null) {
/* 106 */       this.info = connect(this.context, this.factoryBindingName, this.topicBindingName, this.userName, this.password, false);
/*     */     }
/*     */     try {
/* 109 */       send(object, (Session)this.info.session, (MessageProducer)this.info.publisher);
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
/*     */     private final String topicBindingName;
/*     */     private final String userName;
/*     */     private final String password;
/*     */     
/*     */     public FactoryData(String factoryName, String providerURL, String urlPkgPrefixes, String securityPrincipalName, String securityCredentials, String factoryBindingName, String topicBindingName, String userName, String password) {
/* 159 */       this.factoryName = factoryName;
/* 160 */       this.providerURL = providerURL;
/* 161 */       this.urlPkgPrefixes = urlPkgPrefixes;
/* 162 */       this.securityPrincipalName = securityPrincipalName;
/* 163 */       this.securityCredentials = securityCredentials;
/* 164 */       this.factoryBindingName = factoryBindingName;
/* 165 */       this.topicBindingName = topicBindingName;
/* 166 */       this.userName = userName;
/* 167 */       this.password = password;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static TopicInfo connect(Context context, String factoryBindingName, String queueBindingName, String userName, String password, boolean suppress) throws Exception {
/*     */     try {
/*     */       TopicConnection conn;
/* 175 */       TopicConnectionFactory factory = (TopicConnectionFactory)lookup(context, factoryBindingName);
/*     */       
/* 177 */       if (userName != null) {
/* 178 */         conn = factory.createTopicConnection(userName, password);
/*     */       } else {
/* 180 */         conn = factory.createTopicConnection();
/*     */       } 
/* 182 */       TopicSession sess = conn.createTopicSession(false, 1);
/* 183 */       Topic topic = (Topic)lookup(context, queueBindingName);
/* 184 */       TopicPublisher publisher = sess.createPublisher(topic);
/* 185 */       conn.start();
/* 186 */       return new TopicInfo(conn, sess, publisher);
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
/*     */   private static class TopicInfo
/*     */   {
/*     */     private final TopicConnection conn;
/*     */     private final TopicSession session;
/*     */     private final TopicPublisher publisher;
/*     */     
/*     */     public TopicInfo(TopicConnection conn, TopicSession session, TopicPublisher publisher) {
/* 208 */       this.conn = conn;
/* 209 */       this.session = session;
/* 210 */       this.publisher = publisher;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static class JMSTopicManagerFactory
/*     */     implements ManagerFactory<JmsTopicManager, FactoryData>
/*     */   {
/*     */     private JMSTopicManagerFactory() {}
/*     */     
/*     */     public JmsTopicManager createManager(String name, JmsTopicManager.FactoryData data) {
/*     */       try {
/* 222 */         Context ctx = AbstractJmsManager.createContext(data.factoryName, data.providerURL, data.urlPkgPrefixes, data.securityPrincipalName, data.securityCredentials);
/*     */         
/* 224 */         JmsTopicManager.TopicInfo info = JmsTopicManager.connect(ctx, data.factoryBindingName, data.topicBindingName, data.userName, data.password, true);
/*     */         
/* 226 */         return new JmsTopicManager(name, ctx, data.factoryBindingName, data.topicBindingName, data.userName, data.password, info);
/*     */       }
/* 228 */       catch (NamingException ex) {
/* 229 */         JmsTopicManager.LOGGER.error("Unable to locate resource", ex);
/* 230 */       } catch (Exception ex) {
/* 231 */         JmsTopicManager.LOGGER.error("Unable to connect", ex);
/*     */       } 
/*     */       
/* 234 */       return null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\net\jms\JmsTopicManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */