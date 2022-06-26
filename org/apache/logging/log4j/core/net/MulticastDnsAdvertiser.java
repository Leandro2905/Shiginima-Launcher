/*     */ package org.apache.logging.log4j.core.net;
/*     */ 
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.HashMap;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Map;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.util.Integers;
/*     */ import org.apache.logging.log4j.core.util.Loader;
/*     */ import org.apache.logging.log4j.status.StatusLogger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Plugin(name = "multicastdns", category = "Core", elementType = "advertiser", printObject = false)
/*     */ public class MulticastDnsAdvertiser
/*     */   implements Advertiser
/*     */ {
/*  40 */   protected static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*  41 */   private static Object jmDNS = initializeJmDns();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Class<?> jmDNSClass;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Class<?> serviceInfoClass;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object advertise(Map<String, String> properties) {
/*  66 */     Map<String, String> truncatedProperties = new HashMap<String, String>();
/*  67 */     for (Map.Entry<String, String> entry : properties.entrySet()) {
/*     */       
/*  69 */       if (((String)entry.getKey()).length() <= 255 && ((String)entry.getValue()).length() <= 255)
/*     */       {
/*  71 */         truncatedProperties.put(entry.getKey(), entry.getValue());
/*     */       }
/*     */     } 
/*  74 */     String protocol = truncatedProperties.get("protocol");
/*  75 */     String zone = "._log4j._" + ((protocol != null) ? protocol : "tcp") + ".local.";
/*     */     
/*  77 */     String portString = truncatedProperties.get("port");
/*  78 */     int port = Integers.parseInt(portString, 4555);
/*     */     
/*  80 */     String name = truncatedProperties.get("name");
/*     */ 
/*     */     
/*  83 */     if (jmDNS != null) {
/*     */       Object serviceInfo;
/*  85 */       boolean isVersion3 = false;
/*     */       
/*     */       try {
/*  88 */         jmDNSClass.getMethod("create", new Class[0]);
/*  89 */         isVersion3 = true;
/*  90 */       } catch (NoSuchMethodException e) {}
/*     */ 
/*     */ 
/*     */       
/*  94 */       if (isVersion3) {
/*  95 */         serviceInfo = buildServiceInfoVersion3(zone, port, name, truncatedProperties);
/*     */       } else {
/*  97 */         serviceInfo = buildServiceInfoVersion1(zone, port, name, truncatedProperties);
/*     */       } 
/*     */       
/*     */       try {
/* 101 */         Method method = jmDNSClass.getMethod("registerService", new Class[] { serviceInfoClass });
/* 102 */         method.invoke(jmDNS, new Object[] { serviceInfo });
/* 103 */       } catch (IllegalAccessException e) {
/* 104 */         LOGGER.warn("Unable to invoke registerService method", e);
/* 105 */       } catch (NoSuchMethodException e) {
/* 106 */         LOGGER.warn("No registerService method", e);
/* 107 */       } catch (InvocationTargetException e) {
/* 108 */         LOGGER.warn("Unable to invoke registerService method", e);
/*     */       } 
/* 110 */       return serviceInfo;
/*     */     } 
/* 112 */     LOGGER.warn("JMDNS not available - will not advertise ZeroConf support");
/* 113 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void unadvertise(Object serviceInfo) {
/* 122 */     if (jmDNS != null) {
/*     */       try {
/* 124 */         Method method = jmDNSClass.getMethod("unregisterService", new Class[] { serviceInfoClass });
/* 125 */         method.invoke(jmDNS, new Object[] { serviceInfo });
/* 126 */       } catch (IllegalAccessException e) {
/* 127 */         LOGGER.warn("Unable to invoke unregisterService method", e);
/* 128 */       } catch (NoSuchMethodException e) {
/* 129 */         LOGGER.warn("No unregisterService method", e);
/* 130 */       } catch (InvocationTargetException e) {
/* 131 */         LOGGER.warn("Unable to invoke unregisterService method", e);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static Object createJmDnsVersion1() {
/*     */     try {
/* 139 */       return jmDNSClass.getConstructor(new Class[0]).newInstance(new Object[0]);
/* 140 */     } catch (InstantiationException e) {
/* 141 */       LOGGER.warn("Unable to instantiate JMDNS", e);
/* 142 */     } catch (IllegalAccessException e) {
/* 143 */       LOGGER.warn("Unable to instantiate JMDNS", e);
/* 144 */     } catch (NoSuchMethodException e) {
/* 145 */       LOGGER.warn("Unable to instantiate JMDNS", e);
/* 146 */     } catch (InvocationTargetException e) {
/* 147 */       LOGGER.warn("Unable to instantiate JMDNS", e);
/*     */     } 
/* 149 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   private static Object createJmDnsVersion3() {
/*     */     try {
/* 155 */       Method jmDNSCreateMethod = jmDNSClass.getMethod("create", new Class[0]);
/* 156 */       return jmDNSCreateMethod.invoke(null, (Object[])null);
/* 157 */     } catch (IllegalAccessException e) {
/* 158 */       LOGGER.warn("Unable to invoke create method", e);
/* 159 */     } catch (NoSuchMethodException e) {
/* 160 */       LOGGER.warn("Unable to get create method", e);
/* 161 */     } catch (InvocationTargetException e) {
/* 162 */       LOGGER.warn("Unable to invoke create method", e);
/*     */     } 
/* 164 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Object buildServiceInfoVersion1(String zone, int port, String name, Map<String, String> properties) {
/* 173 */     Hashtable<String, String> hashtableProperties = new Hashtable<String, String>(properties);
/*     */     try {
/* 175 */       return serviceInfoClass.getConstructor(new Class[] { String.class, String.class, int.class, int.class, int.class, Hashtable.class }).newInstance(new Object[] { zone, name, Integer.valueOf(port), Integer.valueOf(0), Integer.valueOf(0), hashtableProperties });
/*     */     
/*     */     }
/* 178 */     catch (IllegalAccessException e) {
/* 179 */       LOGGER.warn("Unable to construct ServiceInfo instance", e);
/* 180 */     } catch (NoSuchMethodException e) {
/* 181 */       LOGGER.warn("Unable to get ServiceInfo constructor", e);
/* 182 */     } catch (InstantiationException e) {
/* 183 */       LOGGER.warn("Unable to construct ServiceInfo instance", e);
/* 184 */     } catch (InvocationTargetException e) {
/* 185 */       LOGGER.warn("Unable to construct ServiceInfo instance", e);
/*     */     } 
/* 187 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Object buildServiceInfoVersion3(String zone, int port, String name, Map<String, String> properties) {
/*     */     try {
/* 195 */       return serviceInfoClass.getMethod("create", new Class[] { String.class, String.class, int.class, int.class, int.class, Map.class }).invoke(null, new Object[] { zone, name, Integer.valueOf(port), Integer.valueOf(0), Integer.valueOf(0), properties });
/*     */     
/*     */     }
/* 198 */     catch (IllegalAccessException e) {
/* 199 */       LOGGER.warn("Unable to invoke create method", e);
/* 200 */     } catch (NoSuchMethodException e) {
/* 201 */       LOGGER.warn("Unable to find create method", e);
/* 202 */     } catch (InvocationTargetException e) {
/* 203 */       LOGGER.warn("Unable to invoke create method", e);
/*     */     } 
/* 205 */     return null;
/*     */   }
/*     */   
/*     */   private static Object initializeJmDns() {
/*     */     try {
/* 210 */       jmDNSClass = Loader.loadClass("javax.jmdns.JmDNS");
/* 211 */       serviceInfoClass = Loader.loadClass("javax.jmdns.ServiceInfo");
/*     */       
/* 213 */       boolean isVersion3 = false;
/*     */       
/*     */       try {
/* 216 */         jmDNSClass.getMethod("create", new Class[0]);
/* 217 */         isVersion3 = true;
/* 218 */       } catch (NoSuchMethodException e) {}
/*     */ 
/*     */ 
/*     */       
/* 222 */       if (isVersion3) {
/* 223 */         return createJmDnsVersion3();
/*     */       }
/* 225 */       return createJmDnsVersion1();
/* 226 */     } catch (ClassNotFoundException e) {
/* 227 */       LOGGER.warn("JmDNS or serviceInfo class not found", e);
/* 228 */     } catch (ExceptionInInitializerError e2) {
/* 229 */       LOGGER.warn("JmDNS or serviceInfo class not found", e2);
/*     */     } 
/* 231 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\net\MulticastDnsAdvertiser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */