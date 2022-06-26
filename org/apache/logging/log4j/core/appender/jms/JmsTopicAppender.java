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
/*     */ import org.apache.logging.log4j.core.net.jms.JmsTopicManager;
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
/*     */ @Plugin(name = "JMSTopic", category = "Core", elementType = "appender", printObject = true)
/*     */ public final class JmsTopicAppender
/*     */   extends AbstractAppender
/*     */ {
/*     */   private final JmsTopicManager manager;
/*     */   
/*     */   private JmsTopicAppender(String name, Filter filter, Layout<? extends Serializable> layout, JmsTopicManager manager, boolean ignoreExceptions) {
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
/*     */   
/*     */   @PluginFactory
/*     */   public static JmsTopicAppender createAppender(@PluginAttribute("name") String name, @PluginAttribute("factoryName") String factoryName, @PluginAttribute("providerURL") String providerURL, @PluginAttribute("urlPkgPrefixes") String urlPkgPrefixes, @PluginAttribute("securityPrincipalName") String securityPrincipalName, @PluginAttribute("securityCredentials") String securityCredentials, @PluginAttribute("factoryBindingName") String factoryBindingName, @PluginAttribute("topicBindingName") String topicBindingName, @PluginAttribute("userName") String userName, @PluginAttribute("password") String password, @PluginElement("Layout") Layout<? extends Serializable> layout, @PluginElement("Filter") Filter filter, @PluginAttribute("ignoreExceptions") String ignore) {
/*     */     SerializedLayout serializedLayout;
/*  97 */     if (name == null) {
/*  98 */       LOGGER.error("No name provided for JmsQueueAppender");
/*  99 */       return null;
/*     */     } 
/* 101 */     boolean ignoreExceptions = Booleans.parseBoolean(ignore, true);
/* 102 */     JmsTopicManager manager = JmsTopicManager.getJmsTopicManager(factoryName, providerURL, urlPkgPrefixes, securityPrincipalName, securityCredentials, factoryBindingName, topicBindingName, userName, password);
/*     */     
/* 104 */     if (manager == null) {
/* 105 */       return null;
/*     */     }
/* 107 */     if (layout == null) {
/* 108 */       serializedLayout = SerializedLayout.createLayout();
/*     */     }
/* 110 */     return new JmsTopicAppender(name, filter, (Layout<? extends Serializable>)serializedLayout, manager, ignoreExceptions);
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\appender\jms\JmsTopicAppender.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */