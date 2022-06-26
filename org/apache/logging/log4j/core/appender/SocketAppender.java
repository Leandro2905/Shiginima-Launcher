/*     */ package org.apache.logging.log4j.core.appender;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.apache.logging.log4j.core.Filter;
/*     */ import org.apache.logging.log4j.core.Layout;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginElement;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginFactory;
/*     */ import org.apache.logging.log4j.core.layout.SerializedLayout;
/*     */ import org.apache.logging.log4j.core.net.AbstractSocketManager;
/*     */ import org.apache.logging.log4j.core.net.Advertiser;
/*     */ import org.apache.logging.log4j.core.net.DatagramSocketManager;
/*     */ import org.apache.logging.log4j.core.net.Protocol;
/*     */ import org.apache.logging.log4j.core.net.SslSocketManager;
/*     */ import org.apache.logging.log4j.core.net.TcpSocketManager;
/*     */ import org.apache.logging.log4j.core.net.ssl.SslConfiguration;
/*     */ import org.apache.logging.log4j.core.util.Booleans;
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
/*     */ @Plugin(name = "Socket", category = "Core", elementType = "appender", printObject = true)
/*     */ public class SocketAppender
/*     */   extends AbstractOutputStreamAppender<AbstractSocketManager>
/*     */ {
/*     */   private Object advertisement;
/*     */   private final Advertiser advertiser;
/*     */   
/*     */   protected SocketAppender(String name, Layout<? extends Serializable> layout, Filter filter, AbstractSocketManager manager, boolean ignoreExceptions, boolean immediateFlush, Advertiser advertiser) {
/*  53 */     super(name, layout, filter, ignoreExceptions, immediateFlush, manager);
/*  54 */     if (advertiser != null) {
/*  55 */       Map<String, String> configuration = new HashMap<String, String>(layout.getContentFormat());
/*  56 */       configuration.putAll(manager.getContentFormat());
/*  57 */       configuration.put("contentType", layout.getContentType());
/*  58 */       configuration.put("name", name);
/*  59 */       this.advertisement = advertiser.advertise(configuration);
/*     */     } 
/*  61 */     this.advertiser = advertiser;
/*     */   }
/*     */ 
/*     */   
/*     */   public void stop() {
/*  66 */     super.stop();
/*  67 */     if (this.advertiser != null) {
/*  68 */       this.advertiser.unadvertise(this.advertisement);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */   public static SocketAppender createAppender(@PluginAttribute("host") String host, @PluginAttribute("port") String portNum, @PluginAttribute("protocol") String protocolStr, @PluginElement("SSL") SslConfiguration sslConfig, @PluginAttribute("reconnectionDelay") String delay, @PluginAttribute("immediateFail") String immediateFail, @PluginAttribute("name") String name, @PluginAttribute("immediateFlush") String immediateFlush, @PluginAttribute("ignoreExceptions") String ignore, @PluginElement("Layout") Layout<? extends Serializable> layout, @PluginElement("Filter") Filter filter, @PluginAttribute("advertise") String advertise, @PluginConfiguration Configuration config) {
/*     */     SerializedLayout serializedLayout;
/* 120 */     boolean isFlush = Booleans.parseBoolean(immediateFlush, true);
/* 121 */     boolean isAdvertise = Boolean.parseBoolean(advertise);
/* 122 */     boolean ignoreExceptions = Booleans.parseBoolean(ignore, true);
/* 123 */     boolean fail = Booleans.parseBoolean(immediateFail, true);
/* 124 */     int reconnectDelay = AbstractAppender.parseInt(delay, 0);
/* 125 */     int port = AbstractAppender.parseInt(portNum, 0);
/* 126 */     if (layout == null) {
/* 127 */       serializedLayout = SerializedLayout.createLayout();
/*     */     }
/*     */     
/* 130 */     if (name == null) {
/* 131 */       LOGGER.error("No name provided for SocketAppender");
/* 132 */       return null;
/*     */     } 
/*     */     
/* 135 */     Protocol protocol = (Protocol)EnglishEnums.valueOf(Protocol.class, (protocolStr != null) ? protocolStr : Protocol.TCP.name());
/*     */     
/* 137 */     if (protocol == Protocol.UDP) {
/* 138 */       isFlush = true;
/*     */     }
/*     */     
/* 141 */     AbstractSocketManager manager = createSocketManager(name, protocol, host, port, sslConfig, reconnectDelay, fail, (Layout<? extends Serializable>)serializedLayout);
/*     */ 
/*     */     
/* 144 */     return new SocketAppender(name, (Layout<? extends Serializable>)serializedLayout, filter, manager, ignoreExceptions, isFlush, isAdvertise ? config.getAdvertiser() : null);
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
/*     */   protected static AbstractSocketManager createSocketManager(String name, Protocol protocol, String host, int port, SslConfiguration sslConfig, int delay, boolean immediateFail, Layout<? extends Serializable> layout) {
/* 157 */     if (protocol == Protocol.TCP && sslConfig != null)
/*     */     {
/* 159 */       protocol = Protocol.SSL;
/*     */     }
/* 161 */     if (protocol != Protocol.SSL && sslConfig != null) {
/* 162 */       LOGGER.info("Appender {} ignoring SSL configuration for {} protocol", new Object[] { name, protocol });
/*     */     }
/* 164 */     switch (protocol) {
/*     */       case TCP:
/* 166 */         return (AbstractSocketManager)TcpSocketManager.getSocketManager(host, port, delay, immediateFail, layout);
/*     */       case UDP:
/* 168 */         return (AbstractSocketManager)DatagramSocketManager.getSocketManager(host, port, layout);
/*     */       case SSL:
/* 170 */         return (AbstractSocketManager)SslSocketManager.getSocketManager(sslConfig, host, port, delay, immediateFail, layout);
/*     */     } 
/* 172 */     throw new IllegalArgumentException(protocol.toString());
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\appender\SocketAppender.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */