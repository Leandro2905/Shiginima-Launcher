/*     */ package org.jivesoftware.smack.util;
/*     */ 
/*     */ import java.security.KeyManagementException;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.SecureRandom;
/*     */ import java.security.cert.CertificateException;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import javax.net.ssl.HostnameVerifier;
/*     */ import javax.net.ssl.SSLContext;
/*     */ import javax.net.ssl.SSLSession;
/*     */ import javax.net.ssl.SSLSocket;
/*     */ import javax.net.ssl.TrustManager;
/*     */ import javax.net.ssl.X509TrustManager;
/*     */ import org.jivesoftware.smack.SmackException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TLSUtils
/*     */ {
/*     */   public static final String SSL = "SSL";
/*     */   public static final String TLS = "TLS";
/*     */   public static final String PROTO_SSL3 = "SSLv3";
/*     */   public static final String PROTO_TLSV1 = "TLSv1";
/*     */   public static final String PROTO_TLSV1_1 = "TLSv1.1";
/*     */   public static final String PROTO_TLSV1_2 = "TLSv1.2";
/*     */   
/*     */   public static <B extends org.jivesoftware.smack.ConnectionConfiguration.Builder<B, ?>> B setTLSOnly(B builder) {
/*  61 */     builder.setEnabledSSLProtocols(new String[] { "TLSv1.2", "TLSv1.1", "TLSv1" });
/*  62 */     return builder;
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
/*     */   public static <B extends org.jivesoftware.smack.ConnectionConfiguration.Builder<B, ?>> B setSSLv3AndTLSOnly(B builder) {
/*  78 */     builder.setEnabledSSLProtocols(new String[] { "TLSv1.2", "TLSv1.1", "TLSv1", "SSLv3" });
/*  79 */     return builder;
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
/*     */   public static <B extends org.jivesoftware.smack.ConnectionConfiguration.Builder<B, ?>> B acceptAllCertificates(B builder) throws NoSuchAlgorithmException, KeyManagementException {
/*  96 */     SSLContext context = SSLContext.getInstance("TLS");
/*  97 */     context.init(null, new TrustManager[] { new AcceptAllTrustManager() }, new SecureRandom());
/*  98 */     builder.setCustomSSLContext(context);
/*  99 */     return builder;
/*     */   }
/*     */   
/* 102 */   private static final HostnameVerifier DOES_NOT_VERIFY_VERIFIER = new HostnameVerifier()
/*     */     {
/*     */       public boolean verify(String hostname, SSLSession session)
/*     */       {
/* 106 */         return true;
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <B extends org.jivesoftware.smack.ConnectionConfiguration.Builder<B, ?>> B disableHostnameVerificationForTlsCertificicates(B builder) {
/* 122 */     builder.setHostnameVerifier(DOES_NOT_VERIFY_VERIFIER);
/* 123 */     return builder;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setEnabledProtocolsAndCiphers(SSLSocket sslSocket, String[] enabledProtocols, String[] enabledCiphers) throws SmackException.SecurityNotPossibleException {
/* 129 */     if (enabledProtocols != null) {
/* 130 */       Set<String> enabledProtocolsSet = new HashSet<>(Arrays.asList(enabledProtocols));
/* 131 */       Set<String> supportedProtocolsSet = new HashSet<>(Arrays.asList(sslSocket.getSupportedProtocols()));
/*     */       
/* 133 */       Set<String> protocolsIntersection = new HashSet<>(supportedProtocolsSet);
/* 134 */       protocolsIntersection.retainAll(enabledProtocolsSet);
/* 135 */       if (protocolsIntersection.isEmpty()) {
/* 136 */         throw new SmackException.SecurityNotPossibleException("Request to enable SSL/TLS protocols '" + StringUtils.collectionToString(enabledProtocolsSet) + "', but only '" + StringUtils.collectionToString(supportedProtocolsSet) + "' are supported.");
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 144 */       enabledProtocols = new String[protocolsIntersection.size()];
/* 145 */       enabledProtocols = protocolsIntersection.<String>toArray(enabledProtocols);
/* 146 */       sslSocket.setEnabledProtocols(enabledProtocols);
/*     */     } 
/*     */     
/* 149 */     if (enabledCiphers != null) {
/* 150 */       Set<String> enabledCiphersSet = new HashSet<>(Arrays.asList(enabledCiphers));
/* 151 */       Set<String> supportedCiphersSet = new HashSet<>(Arrays.asList(sslSocket.getEnabledCipherSuites()));
/*     */       
/* 153 */       Set<String> ciphersIntersection = new HashSet<>(supportedCiphersSet);
/* 154 */       ciphersIntersection.retainAll(enabledCiphersSet);
/* 155 */       if (ciphersIntersection.isEmpty()) {
/* 156 */         throw new SmackException.SecurityNotPossibleException("Request to enable SSL/TLS ciphers '" + StringUtils.collectionToString(enabledCiphersSet) + "', but only '" + StringUtils.collectionToString(supportedCiphersSet) + "' are supported.");
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 163 */       enabledCiphers = new String[ciphersIntersection.size()];
/* 164 */       enabledCiphers = ciphersIntersection.<String>toArray(enabledCiphers);
/* 165 */       sslSocket.setEnabledCipherSuites(enabledCiphers);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class AcceptAllTrustManager
/*     */     implements X509TrustManager
/*     */   {
/*     */     public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public X509Certificate[] getAcceptedIssuers() {
/* 193 */       return new X509Certificate[0];
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jivesoftware\smac\\util\TLSUtils.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */