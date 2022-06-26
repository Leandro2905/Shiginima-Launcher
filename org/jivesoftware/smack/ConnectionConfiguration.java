/*     */ package org.jivesoftware.smack;
/*     */ 
/*     */ import javax.net.SocketFactory;
/*     */ import javax.net.ssl.HostnameVerifier;
/*     */ import javax.net.ssl.SSLContext;
/*     */ import javax.security.auth.callback.CallbackHandler;
/*     */ import org.jivesoftware.smack.proxy.ProxyInfo;
/*     */ import org.jxmpp.jid.DomainBareJid;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class ConnectionConfiguration
/*     */ {
/*     */   protected final DomainBareJid serviceName;
/*     */   protected final String host;
/*     */   protected final int port;
/*     */   private final String keystorePath;
/*     */   private final String keystoreType;
/*     */   private final String pkcs11Library;
/*     */   private final SSLContext customSSLContext;
/*     */   private final CallbackHandler callbackHandler;
/*     */   private final boolean debuggerEnabled;
/*     */   private final SocketFactory socketFactory;
/*     */   private final CharSequence username;
/*     */   private final String password;
/*     */   private final String resource;
/*     */   private final boolean sendPresence;
/*     */   private final boolean legacySessionDisabled;
/*     */   private final SecurityMode securityMode;
/*     */   private final String[] enabledSSLProtocols;
/*     */   private final String[] enabledSSLCiphers;
/*     */   private final HostnameVerifier hostnameVerifier;
/*     */   protected final ProxyInfo proxy;
/*     */   protected final boolean allowNullOrEmptyUsername;
/*     */   
/*     */   static {
/*  39 */     SmackConfiguration.getVersion();
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
/*     */   protected ConnectionConfiguration(Builder<?, ?> builder) {
/*  97 */     this.username = builder.username;
/*  98 */     this.password = builder.password;
/*  99 */     this.callbackHandler = builder.callbackHandler;
/*     */ 
/*     */     
/* 102 */     this.resource = builder.resource;
/*     */     
/* 104 */     this.serviceName = builder.serviceName;
/* 105 */     if (this.serviceName == null) {
/* 106 */       throw new IllegalArgumentException("Must provide XMPP service name");
/*     */     }
/* 108 */     this.host = builder.host;
/* 109 */     this.port = builder.port;
/*     */     
/* 111 */     this.proxy = builder.proxy;
/* 112 */     if (this.proxy != null) {
/* 113 */       if (builder.socketFactory != null) {
/* 114 */         throw new IllegalArgumentException("Can not use proxy together with custom socket factory");
/*     */       }
/* 116 */       this.socketFactory = this.proxy.getSocketFactory();
/*     */     } else {
/* 118 */       this.socketFactory = builder.socketFactory;
/*     */     } 
/*     */     
/* 121 */     this.securityMode = builder.securityMode;
/* 122 */     this.keystoreType = builder.keystoreType;
/* 123 */     this.keystorePath = builder.keystorePath;
/* 124 */     this.pkcs11Library = builder.pkcs11Library;
/* 125 */     this.customSSLContext = builder.customSSLContext;
/* 126 */     this.enabledSSLProtocols = builder.enabledSSLProtocols;
/* 127 */     this.enabledSSLCiphers = builder.enabledSSLCiphers;
/* 128 */     this.hostnameVerifier = builder.hostnameVerifier;
/* 129 */     this.sendPresence = builder.sendPresence;
/* 130 */     this.legacySessionDisabled = builder.legacySessionDisabled;
/* 131 */     this.debuggerEnabled = builder.debuggerEnabled;
/* 132 */     this.allowNullOrEmptyUsername = builder.allowEmptyOrNullUsername;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DomainBareJid getServiceName() {
/* 141 */     return this.serviceName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SecurityMode getSecurityMode() {
/* 151 */     return this.securityMode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getKeystorePath() {
/* 162 */     return this.keystorePath;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getKeystoreType() {
/* 171 */     return this.keystoreType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPKCS11Library() {
/* 181 */     return this.pkcs11Library;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SSLContext getCustomSSLContext() {
/* 191 */     return this.customSSLContext;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getEnabledSSLProtocols() {
/* 200 */     return this.enabledSSLProtocols;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getEnabledSSLCiphers() {
/* 209 */     return this.enabledSSLCiphers;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HostnameVerifier getHostnameVerifier() {
/* 220 */     if (this.hostnameVerifier != null)
/* 221 */       return this.hostnameVerifier; 
/* 222 */     return SmackConfiguration.getDefaultHostnameVerifier();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isDebuggerEnabled() {
/* 232 */     return this.debuggerEnabled;
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
/*     */   @Deprecated
/*     */   public boolean isLegacySessionDisabled() {
/* 246 */     return this.legacySessionDisabled;
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
/*     */   public CallbackHandler getCallbackHandler() {
/* 259 */     return this.callbackHandler;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SocketFactory getSocketFactory() {
/* 269 */     return this.socketFactory;
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
/*     */   public enum SecurityMode
/*     */   {
/* 283 */     required,
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 294 */     ifpossible,
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 301 */     disabled;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CharSequence getUsername() {
/* 310 */     return this.username;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPassword() {
/* 319 */     return this.password;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getResource() {
/* 328 */     return this.resource;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isSendPresence() {
/* 337 */     return this.sendPresence;
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
/*     */   public boolean isCompressionEnabled() {
/* 350 */     return false;
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
/*     */   public static abstract class Builder<B extends Builder<B, C>, C extends ConnectionConfiguration>
/*     */   {
/* 369 */     private ConnectionConfiguration.SecurityMode securityMode = ConnectionConfiguration.SecurityMode.ifpossible;
/* 370 */     private String keystorePath = System.getProperty("javax.net.ssl.keyStore");
/* 371 */     private String keystoreType = "jks";
/* 372 */     private String pkcs11Library = "pkcs11.config";
/*     */     private SSLContext customSSLContext;
/*     */     private String[] enabledSSLProtocols;
/*     */     private String[] enabledSSLCiphers;
/*     */     private HostnameVerifier hostnameVerifier;
/*     */     private CharSequence username;
/*     */     private String password;
/* 379 */     private String resource = "Smack";
/*     */     private boolean sendPresence = true;
/*     */     private boolean legacySessionDisabled = false;
/*     */     private ProxyInfo proxy;
/*     */     private CallbackHandler callbackHandler;
/* 384 */     private boolean debuggerEnabled = SmackConfiguration.DEBUG;
/*     */     private SocketFactory socketFactory;
/*     */     private DomainBareJid serviceName;
/*     */     private String host;
/* 388 */     private int port = 5222;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private boolean allowEmptyOrNullUsername = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public B setUsernameAndPassword(CharSequence username, String password) {
/* 406 */       this.username = username;
/* 407 */       this.password = password;
/* 408 */       return getThis();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public B setServiceName(DomainBareJid serviceName) {
/* 418 */       this.serviceName = serviceName;
/* 419 */       return getThis();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public B setResource(String resource) {
/* 433 */       this.resource = resource;
/* 434 */       return getThis();
/*     */     }
/*     */     
/*     */     public B setHost(String host) {
/* 438 */       this.host = host;
/* 439 */       return getThis();
/*     */     }
/*     */     
/*     */     public B setPort(int port) {
/* 443 */       this.port = port;
/* 444 */       return getThis();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public B setCallbackHandler(CallbackHandler callbackHandler) {
/* 458 */       this.callbackHandler = callbackHandler;
/* 459 */       return getThis();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public B setSecurityMode(ConnectionConfiguration.SecurityMode securityMode) {
/* 470 */       this.securityMode = securityMode;
/* 471 */       return getThis();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public B setKeystorePath(String keystorePath) {
/* 483 */       this.keystorePath = keystorePath;
/* 484 */       return getThis();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public B setKeystoreType(String keystoreType) {
/* 494 */       this.keystoreType = keystoreType;
/* 495 */       return getThis();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public B setPKCS11Library(String pkcs11Library) {
/* 506 */       this.pkcs11Library = pkcs11Library;
/* 507 */       return getThis();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public B setCustomSSLContext(SSLContext context) {
/* 521 */       this.customSSLContext = context;
/* 522 */       return getThis();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public B setEnabledSSLProtocols(String[] enabledSSLProtocols) {
/* 532 */       this.enabledSSLProtocols = enabledSSLProtocols;
/* 533 */       return getThis();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public B setEnabledSSLCiphers(String[] enabledSSLCiphers) {
/* 543 */       this.enabledSSLCiphers = enabledSSLCiphers;
/* 544 */       return getThis();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public B setHostnameVerifier(HostnameVerifier verifier) {
/* 555 */       this.hostnameVerifier = verifier;
/* 556 */       return getThis();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Deprecated
/*     */     public B setLegacySessionDisabled(boolean legacySessionDisabled) {
/* 576 */       this.legacySessionDisabled = legacySessionDisabled;
/* 577 */       return getThis();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public B setSendPresence(boolean sendPresence) {
/* 590 */       this.sendPresence = sendPresence;
/* 591 */       return getThis();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public B setDebuggerEnabled(boolean debuggerEnabled) {
/* 602 */       this.debuggerEnabled = debuggerEnabled;
/* 603 */       return getThis();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public B setSocketFactory(SocketFactory socketFactory) {
/* 614 */       this.socketFactory = socketFactory;
/* 615 */       return getThis();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public B allowEmptyOrNullUsernames() {
/* 627 */       this.allowEmptyOrNullUsername = true;
/* 628 */       return getThis();
/*     */     }
/*     */     
/*     */     public abstract C build();
/*     */     
/*     */     protected abstract B getThis();
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jivesoftware\smack\ConnectionConfiguration.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */