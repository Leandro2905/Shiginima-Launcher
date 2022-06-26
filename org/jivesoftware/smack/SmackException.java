/*     */ package org.jivesoftware.smack;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.jivesoftware.smack.filter.StanzaFilter;
/*     */ import org.jivesoftware.smack.util.dns.HostAddress;
/*     */ import org.jxmpp.jid.Jid;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SmackException
/*     */   extends Exception
/*     */ {
/*     */   private static final long serialVersionUID = 1844674365368214457L;
/*     */   
/*     */   public SmackException(Throwable wrappedThrowable) {
/*  44 */     super(wrappedThrowable);
/*     */   }
/*     */   
/*     */   public SmackException(String message) {
/*  48 */     super(message);
/*     */   }
/*     */   
/*     */   public SmackException(String message, Throwable wrappedThrowable) {
/*  52 */     super(message, wrappedThrowable);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected SmackException() {}
/*     */ 
/*     */ 
/*     */   
/*     */   public static class NoResponseException
/*     */     extends SmackException
/*     */   {
/*     */     private static final long serialVersionUID = -6523363748984543636L;
/*     */ 
/*     */     
/*     */     private final StanzaFilter filter;
/*     */ 
/*     */ 
/*     */     
/*     */     private NoResponseException(String message, StanzaFilter filter) {
/*  72 */       super(message);
/*  73 */       this.filter = filter;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public StanzaFilter getFilter() {
/*  82 */       return this.filter;
/*     */     }
/*     */     
/*     */     public static NoResponseException newWith(XMPPConnection connection) {
/*  86 */       return newWith(connection, (StanzaFilter)null);
/*     */     }
/*     */ 
/*     */     
/*     */     public static NoResponseException newWith(XMPPConnection connection, PacketCollector collector) {
/*  91 */       return newWith(connection, collector.getStanzaFilter());
/*     */     }
/*     */     
/*     */     public static NoResponseException newWith(XMPPConnection connection, StanzaFilter filter) {
/*  95 */       long replyTimeout = connection.getPacketReplyTimeout();
/*  96 */       StringBuilder sb = new StringBuilder(256);
/*  97 */       sb.append("No response received within reply timeout. Timeout was " + replyTimeout + "ms (~" + (replyTimeout / 1000L) + "s). Used filter: ");
/*     */ 
/*     */       
/* 100 */       if (filter != null) {
/* 101 */         sb.append(filter.toString());
/*     */       } else {
/*     */         
/* 104 */         sb.append("No filter used or filter was 'null'");
/*     */       } 
/* 106 */       sb.append('.');
/* 107 */       return new NoResponseException(sb.toString(), filter);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static class NotLoggedInException
/*     */     extends SmackException
/*     */   {
/*     */     private static final long serialVersionUID = 3216216839100019278L;
/*     */ 
/*     */     
/*     */     public NotLoggedInException() {
/* 120 */       super("Client is not logged in");
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static class AlreadyLoggedInException
/*     */     extends SmackException
/*     */   {
/*     */     private static final long serialVersionUID = 5011416918049935231L;
/*     */ 
/*     */     
/*     */     public AlreadyLoggedInException() {
/* 132 */       super("Client is already logged in");
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static class AlreadyConnectedException
/*     */     extends SmackException
/*     */   {
/*     */     private static final long serialVersionUID = 5011416918049135231L;
/*     */ 
/*     */     
/*     */     public AlreadyConnectedException() {
/* 144 */       super("Client is already connected");
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static class NotConnectedException
/*     */     extends SmackException
/*     */   {
/*     */     private static final long serialVersionUID = 9197980400776001173L;
/*     */ 
/*     */     
/*     */     public NotConnectedException() {
/* 156 */       super("Client is not, or no longer, connected");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class IllegalStateChangeException
/*     */     extends SmackException
/*     */   {
/*     */     private static final long serialVersionUID = -1766023961577168927L;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static abstract class SecurityRequiredException
/*     */     extends SmackException
/*     */   {
/*     */     private static final long serialVersionUID = 384291845029773545L;
/*     */ 
/*     */ 
/*     */     
/*     */     public SecurityRequiredException(String message) {
/* 179 */       super(message);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static class SecurityRequiredByClientException
/*     */     extends SecurityRequiredException
/*     */   {
/*     */     private static final long serialVersionUID = 2395325821201543159L;
/*     */     
/*     */     public SecurityRequiredByClientException() {
/* 190 */       super("SSL/TLS required by client but not supported by server");
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static class SecurityRequiredByServerException
/*     */     extends SecurityRequiredException
/*     */   {
/*     */     private static final long serialVersionUID = 8268148813117631819L;
/*     */     
/*     */     public SecurityRequiredByServerException() {
/* 201 */       super("SSL/TLS required by server but disabled in client");
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static class SecurityNotPossibleException
/*     */     extends SmackException
/*     */   {
/*     */     private static final long serialVersionUID = -6836090872690331336L;
/*     */ 
/*     */     
/*     */     public SecurityNotPossibleException(String message) {
/* 213 */       super(message);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class ConnectionException
/*     */     extends SmackException
/*     */   {
/*     */     private static final long serialVersionUID = 1686944201672697996L;
/*     */ 
/*     */ 
/*     */     
/*     */     private final List<HostAddress> failedAddresses;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ConnectionException(Throwable wrappedThrowable) {
/* 233 */       super(wrappedThrowable);
/* 234 */       this.failedAddresses = new ArrayList<>(0);
/*     */     }
/*     */     
/*     */     private ConnectionException(String message, List<HostAddress> failedAddresses) {
/* 238 */       super(message);
/* 239 */       this.failedAddresses = failedAddresses;
/*     */     }
/*     */     
/*     */     public static ConnectionException from(List<HostAddress> failedAddresses) {
/* 243 */       String DELIMITER = ", ";
/* 244 */       StringBuilder sb = new StringBuilder("The following addresses failed: ");
/* 245 */       for (HostAddress hostAddress : failedAddresses) {
/* 246 */         sb.append(hostAddress.getErrorMessage());
/* 247 */         sb.append(", ");
/*     */       } 
/*     */       
/* 250 */       sb.setLength(sb.length() - ", ".length());
/* 251 */       return new ConnectionException(sb.toString(), failedAddresses);
/*     */     }
/*     */     
/*     */     public List<HostAddress> getFailedAddresses() {
/* 255 */       return this.failedAddresses;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static class FeatureNotSupportedException
/*     */     extends SmackException
/*     */   {
/*     */     private static final long serialVersionUID = 4713404802621452016L;
/*     */     
/*     */     private final String feature;
/*     */     
/*     */     private final Jid jid;
/*     */     
/*     */     public FeatureNotSupportedException(String feature) {
/* 270 */       this(feature, null);
/*     */     }
/*     */     
/*     */     public FeatureNotSupportedException(String feature, Jid jid) {
/* 274 */       super(feature + " not supported" + ((jid == null) ? "" : (" by '" + jid + "'")));
/* 275 */       this.jid = jid;
/* 276 */       this.feature = feature;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getFeature() {
/* 285 */       return this.feature;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Jid getJid() {
/* 295 */       return this.jid;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static class ResourceBindingNotOfferedException
/*     */     extends SmackException
/*     */   {
/*     */     private static final long serialVersionUID = 2346934138253437571L;
/*     */ 
/*     */     
/*     */     public ResourceBindingNotOfferedException() {
/* 307 */       super("Resource binding was not offered by server");
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jivesoftware\smack\SmackException.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */