/*     */ package org.apache.logging.log4j.core.net.jms;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.Properties;
/*     */ import javax.jms.JMSException;
/*     */ import javax.jms.Message;
/*     */ import javax.jms.MessageProducer;
/*     */ import javax.jms.ObjectMessage;
/*     */ import javax.jms.Session;
/*     */ import javax.jms.TextMessage;
/*     */ import javax.naming.Context;
/*     */ import javax.naming.InitialContext;
/*     */ import javax.naming.NameNotFoundException;
/*     */ import javax.naming.NamingException;
/*     */ import org.apache.logging.log4j.core.appender.AbstractManager;
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
/*     */ public abstract class AbstractJmsManager
/*     */   extends AbstractManager
/*     */ {
/*     */   public AbstractJmsManager(String name) {
/*  45 */     super(name);
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
/*     */   protected static Context createContext(String factoryName, String providerURL, String urlPkgPrefixes, String securityPrincipalName, String securityCredentials) throws NamingException {
/*  64 */     Properties props = getEnvironment(factoryName, providerURL, urlPkgPrefixes, securityPrincipalName, securityCredentials);
/*     */     
/*  66 */     return new InitialContext(props);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static Object lookup(Context ctx, String name) throws NamingException {
/*     */     try {
/*  78 */       return ctx.lookup(name);
/*  79 */     } catch (NameNotFoundException e) {
/*  80 */       LOGGER.warn("Could not find name [{}].", new Object[] { name });
/*  81 */       throw e;
/*     */     } 
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
/*     */   protected static Properties getEnvironment(String factoryName, String providerURL, String urlPkgPrefixes, String securityPrincipalName, String securityCredentials) {
/*  98 */     Properties props = new Properties();
/*  99 */     if (factoryName != null) {
/* 100 */       props.put("java.naming.factory.initial", factoryName);
/* 101 */       if (providerURL != null) {
/* 102 */         props.put("java.naming.provider.url", providerURL);
/*     */       } else {
/* 104 */         LOGGER.warn("The InitialContext factory name has been provided without a ProviderURL. This is likely to cause problems");
/*     */       } 
/*     */       
/* 107 */       if (urlPkgPrefixes != null) {
/* 108 */         props.put("java.naming.factory.url.pkgs", urlPkgPrefixes);
/*     */       }
/* 110 */       if (securityPrincipalName != null) {
/* 111 */         props.put("java.naming.security.principal", securityPrincipalName);
/* 112 */         if (securityCredentials != null) {
/* 113 */           props.put("java.naming.security.credentials", securityCredentials);
/*     */         } else {
/* 115 */           LOGGER.warn("SecurityPrincipalName has been set without SecurityCredentials. This is likely to cause problems.");
/*     */         } 
/*     */       } 
/*     */       
/* 119 */       return props;
/*     */     } 
/* 121 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract void send(Serializable paramSerializable) throws Exception;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void send(Serializable object, Session session, MessageProducer producer) throws Exception {
/*     */     try {
/*     */       ObjectMessage objectMessage;
/* 142 */       if (object instanceof String) {
/* 143 */         TextMessage textMessage = session.createTextMessage();
/* 144 */         textMessage.setText((String)object);
/*     */       } else {
/* 146 */         objectMessage = session.createObjectMessage();
/* 147 */         objectMessage.setObject(object);
/*     */       } 
/* 149 */       producer.send((Message)objectMessage);
/* 150 */     } catch (JMSException ex) {
/* 151 */       LOGGER.error("Could not publish message via JMS {}", new Object[] { getName() });
/* 152 */       throw ex;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\net\jms\AbstractJmsManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */