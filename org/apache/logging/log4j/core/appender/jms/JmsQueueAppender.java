/*     */ package org.apache.logging.log4j.core.appender.jms;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import org.apache.logging.log4j.core.Filter;
/*     */ import org.apache.logging.log4j.core.Layout;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.appender.AbstractAppender;
/*     */ import org.apache.logging.log4j.core.appender.AppenderLoggingException;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginElement;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginFactory;
/*     */ import org.apache.logging.log4j.core.layout.SerializedLayout;
/*     */ import org.apache.logging.log4j.core.net.jms.JmsQueueManager;
/*     */ import org.apache.logging.log4j.core.util.Booleans;
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
/*     */ @Plugin(name = "JMSQueue", category = "Core", elementType = "appender", printObject = true)
/*     */ public final class JmsQueueAppender
/*     */   extends AbstractAppender
/*     */ {
/*     */   private final JmsQueueManager manager;
/*     */   
/*     */   private JmsQueueAppender(String name, Filter filter, Layout<? extends Serializable> layout, JmsQueueManager manager, boolean ignoreExceptions) {
/*  44 */     super(name, filter, layout, ignoreExceptions);
/*  45 */     this.manager = manager;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void append(LogEvent event) {
/*     */     try {
/*  56 */       this.manager.send(getLayout().toSerializable(event));
/*  57 */     } catch (Exception ex) {
/*  58 */       throw new AppenderLoggingException(ex);
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
/*     */   @PluginFactory
/*     */   public static JmsQueueAppender createAppender(@PluginAttribute("name") String name, @PluginAttribute("factoryName") String factoryName, @PluginAttribute("providerURL") String providerURL, @PluginAttribute("urlPkgPrefixes") String urlPkgPrefixes, @PluginAttribute("securityPrincipalName") String securityPrincipalName, @PluginAttribute("securityCredentials") String securityCredentials, @PluginAttribute("factoryBindingName") String factoryBindingName, @PluginAttribute("queueBindingName") String queueBindingName, @PluginAttribute("userName") String userName, @PluginAttribute("password") String password, @PluginElement("Layout") Layout<? extends Serializable> layout, @PluginElement("Filter") Filter filter, @PluginAttribute("ignoreExceptions") String ignore) {
/*     */     SerializedLayout serializedLayout;
/*  96 */     if (name == null) {
/*  97 */       LOGGER.error("No name provided for JmsQueueAppender");
/*  98 */       return null;
/*     */     } 
/* 100 */     boolean ignoreExceptions = Booleans.parseBoolean(ignore, true);
/* 101 */     JmsQueueManager manager = JmsQueueManager.getJmsQueueManager(factoryName, providerURL, urlPkgPrefixes, securityPrincipalName, securityCredentials, factoryBindingName, queueBindingName, userName, password);
/*     */     
/* 103 */     if (manager == null) {
/* 104 */       return null;
/*     */     }
/* 106 */     if (layout == null) {
/* 107 */       serializedLayout = SerializedLayout.createLayout();
/*     */     }
/* 109 */     return new JmsQueueAppender(name, filter, (Layout<? extends Serializable>)serializedLayout, manager, ignoreExceptions);
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\appender\jms\JmsQueueAppender.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */