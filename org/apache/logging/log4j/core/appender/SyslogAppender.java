/*     */ package org.apache.logging.log4j.core.appender;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.nio.charset.Charset;
/*     */ import org.apache.logging.log4j.core.Filter;
/*     */ import org.apache.logging.log4j.core.Layout;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginElement;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginFactory;
/*     */ import org.apache.logging.log4j.core.layout.LoggerFields;
/*     */ import org.apache.logging.log4j.core.layout.Rfc5424Layout;
/*     */ import org.apache.logging.log4j.core.layout.SyslogLayout;
/*     */ import org.apache.logging.log4j.core.net.AbstractSocketManager;
/*     */ import org.apache.logging.log4j.core.net.Advertiser;
/*     */ import org.apache.logging.log4j.core.net.Facility;
/*     */ import org.apache.logging.log4j.core.net.Protocol;
/*     */ import org.apache.logging.log4j.core.net.ssl.SslConfiguration;
/*     */ import org.apache.logging.log4j.util.EnglishEnums;
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
/*     */ @Plugin(name = "Syslog", category = "Core", elementType = "appender", printObject = true)
/*     */ public class SyslogAppender
/*     */   extends SocketAppender
/*     */ {
/*     */   protected static final String RFC5424 = "RFC5424";
/*     */   
/*     */   protected SyslogAppender(String name, Layout<? extends Serializable> layout, Filter filter, boolean ignoreExceptions, boolean immediateFlush, AbstractSocketManager manager, Advertiser advertiser) {
/*  51 */     super(name, layout, filter, manager, ignoreExceptions, immediateFlush, advertiser);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @PluginFactory
/*     */   public static SyslogAppender createAppender(@PluginAttribute("host") String host, @PluginAttribute(value = "port", defaultInt = 0) int port, @PluginAttribute("protocol") String protocolStr, @PluginElement("SSL") SslConfiguration sslConfig, @PluginAttribute(value = "reconnectionDelay", defaultInt = 0) int reconnectionDelay, @PluginAttribute(value = "immediateFail", defaultBoolean = true) boolean immediateFail, @PluginAttribute("name") String name, @PluginAttribute(value = "immediateFlush", defaultBoolean = true) boolean immediateFlush, @PluginAttribute(value = "ignoreExceptions", defaultBoolean = true) boolean ignoreExceptions, @PluginAttribute(value = "facility", defaultString = "LOCAL0") Facility facility, @PluginAttribute("id") String id, @PluginAttribute(value = "enterpriseNumber", defaultInt = 18060) int enterpriseNumber, @PluginAttribute(value = "includeMdc", defaultBoolean = true) boolean includeMdc, @PluginAttribute("mdcId") String mdcId, @PluginAttribute("mdcPrefix") String mdcPrefix, @PluginAttribute("eventPrefix") String eventPrefix, @PluginAttribute(value = "newLine", defaultBoolean = false) boolean newLine, @PluginAttribute("newLineEscape") String escapeNL, @PluginAttribute("appName") String appName, @PluginAttribute("messageId") String msgId, @PluginAttribute("mdcExcludes") String excludes, @PluginAttribute("mdcIncludes") String includes, @PluginAttribute("mdcRequired") String required, @PluginAttribute("format") String format, @PluginElement("Filter") Filter filter, @PluginConfiguration Configuration config, @PluginAttribute(value = "charset", defaultString = "UTF-8") Charset charsetName, @PluginAttribute("exceptionPattern") String exceptionPattern, @PluginElement("LoggerFields") LoggerFields[] loggerFields, @PluginAttribute(value = "advertise", defaultBoolean = false) boolean advertise) {
/* 128 */     Protocol protocol = (Protocol)EnglishEnums.valueOf(Protocol.class, protocolStr);
/* 129 */     boolean useTlsMessageFormat = (sslConfig != null || protocol == Protocol.SSL);
/* 130 */     Layout<? extends Serializable> layout = "RFC5424".equalsIgnoreCase(format) ? (Layout<? extends Serializable>)Rfc5424Layout.createLayout(facility, id, enterpriseNumber, includeMdc, mdcId, mdcPrefix, eventPrefix, newLine, escapeNL, appName, msgId, excludes, includes, required, exceptionPattern, useTlsMessageFormat, loggerFields, config) : (Layout<? extends Serializable>)SyslogLayout.createLayout(facility, newLine, escapeNL, charsetName);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 136 */     if (name == null) {
/* 137 */       LOGGER.error("No name provided for SyslogAppender");
/* 138 */       return null;
/*     */     } 
/* 140 */     AbstractSocketManager manager = createSocketManager(name, protocol, host, port, sslConfig, reconnectionDelay, immediateFail, layout);
/*     */ 
/*     */     
/* 143 */     return new SyslogAppender(name, layout, filter, ignoreExceptions, immediateFlush, manager, advertise ? config.getAdvertiser() : null);
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\appender\SyslogAppender.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */