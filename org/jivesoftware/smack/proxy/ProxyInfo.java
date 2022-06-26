/*     */ package org.jivesoftware.smack.proxy;
/*     */ 
/*     */ import javax.net.SocketFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ProxyInfo
/*     */ {
/*     */   private String proxyAddress;
/*     */   private int proxyPort;
/*     */   private String proxyUsername;
/*     */   private String proxyPassword;
/*     */   private ProxyType proxyType;
/*     */   
/*     */   public enum ProxyType
/*     */   {
/*  32 */     NONE,
/*  33 */     HTTP,
/*  34 */     SOCKS4,
/*  35 */     SOCKS5;
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
/*     */   public ProxyInfo(ProxyType pType, String pHost, int pPort, String pUser, String pPass) {
/*  47 */     this.proxyType = pType;
/*  48 */     this.proxyAddress = pHost;
/*  49 */     this.proxyPort = pPort;
/*  50 */     this.proxyUsername = pUser;
/*  51 */     this.proxyPassword = pPass;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static ProxyInfo forHttpProxy(String pHost, int pPort, String pUser, String pPass) {
/*  57 */     return new ProxyInfo(ProxyType.HTTP, pHost, pPort, pUser, pPass);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static ProxyInfo forSocks4Proxy(String pHost, int pPort, String pUser, String pPass) {
/*  63 */     return new ProxyInfo(ProxyType.SOCKS4, pHost, pPort, pUser, pPass);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static ProxyInfo forSocks5Proxy(String pHost, int pPort, String pUser, String pPass) {
/*  69 */     return new ProxyInfo(ProxyType.SOCKS5, pHost, pPort, pUser, pPass);
/*     */   }
/*     */ 
/*     */   
/*     */   public static ProxyInfo forNoProxy() {
/*  74 */     return new ProxyInfo(ProxyType.NONE, null, 0, null, null);
/*     */   }
/*     */ 
/*     */   
/*     */   public static ProxyInfo forDefaultProxy() {
/*  79 */     return new ProxyInfo(ProxyType.NONE, null, 0, null, null);
/*     */   }
/*     */ 
/*     */   
/*     */   public ProxyType getProxyType() {
/*  84 */     return this.proxyType;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getProxyAddress() {
/*  89 */     return this.proxyAddress;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getProxyPort() {
/*  94 */     return this.proxyPort;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getProxyUsername() {
/*  99 */     return this.proxyUsername;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getProxyPassword() {
/* 104 */     return this.proxyPassword;
/*     */   }
/*     */ 
/*     */   
/*     */   public SocketFactory getSocketFactory() {
/* 109 */     if (this.proxyType == ProxyType.NONE)
/*     */     {
/* 111 */       return new DirectSocketFactory();
/*     */     }
/* 113 */     if (this.proxyType == ProxyType.HTTP)
/*     */     {
/* 115 */       return new HTTPProxySocketFactory(this);
/*     */     }
/* 117 */     if (this.proxyType == ProxyType.SOCKS4)
/*     */     {
/* 119 */       return new Socks4ProxySocketFactory(this);
/*     */     }
/* 121 */     if (this.proxyType == ProxyType.SOCKS5)
/*     */     {
/* 123 */       return new Socks5ProxySocketFactory(this);
/*     */     }
/*     */ 
/*     */     
/* 127 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jivesoftware\smack\proxy\ProxyInfo.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */