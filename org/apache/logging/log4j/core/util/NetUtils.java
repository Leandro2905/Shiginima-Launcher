/*    */ package org.apache.logging.log4j.core.util;
/*    */ 
/*    */ import java.net.InetAddress;
/*    */ import java.net.NetworkInterface;
/*    */ import java.net.SocketException;
/*    */ import java.net.UnknownHostException;
/*    */ import java.util.Enumeration;
/*    */ import org.apache.logging.log4j.Logger;
/*    */ import org.apache.logging.log4j.status.StatusLogger;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class NetUtils
/*    */ {
/* 33 */   private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static String getLocalHostname() {
/*    */     try {
/* 47 */       InetAddress addr = InetAddress.getLocalHost();
/* 48 */       return addr.getHostName();
/* 49 */     } catch (UnknownHostException uhe) {
/*    */       try {
/* 51 */         Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
/* 52 */         while (interfaces.hasMoreElements()) {
/* 53 */           NetworkInterface nic = interfaces.nextElement();
/* 54 */           Enumeration<InetAddress> addresses = nic.getInetAddresses();
/* 55 */           while (addresses.hasMoreElements()) {
/* 56 */             InetAddress address = addresses.nextElement();
/* 57 */             if (!address.isLoopbackAddress()) {
/* 58 */               String hostname = address.getHostName();
/* 59 */               if (hostname != null) {
/* 60 */                 return hostname;
/*    */               }
/*    */             } 
/*    */           } 
/*    */         } 
/* 65 */       } catch (SocketException se) {
/* 66 */         LOGGER.error("Could not determine local host name", uhe);
/* 67 */         return "UNKNOWN_LOCALHOST";
/*    */       } 
/* 69 */       LOGGER.error("Could not determine local host name", uhe);
/* 70 */       return "UNKNOWN_LOCALHOST";
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\cor\\util\NetUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */