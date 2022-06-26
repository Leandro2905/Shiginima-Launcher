/*     */ package org.jivesoftware.smack;
/*     */ 
/*     */ import java.io.Reader;
/*     */ import java.io.Writer;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import javax.net.ssl.HostnameVerifier;
/*     */ import org.jivesoftware.smack.compression.XMPPInputOutputStream;
/*     */ import org.jivesoftware.smack.debugger.ReflectionDebuggerFactory;
/*     */ import org.jivesoftware.smack.debugger.SmackDebugger;
/*     */ import org.jivesoftware.smack.debugger.SmackDebuggerFactory;
/*     */ import org.jivesoftware.smack.parsing.ExceptionThrowingCallback;
/*     */ import org.jivesoftware.smack.parsing.ParsingExceptionCallback;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class SmackConfiguration
/*     */ {
/*  54 */   private static int defaultPacketReplyTimeout = 5000;
/*  55 */   private static int packetCollectorSize = 5000;
/*     */   
/*  57 */   private static List<String> defaultMechs = new ArrayList<>();
/*     */   
/*  59 */   static Set<String> disabledSmackClasses = new HashSet<>();
/*     */   
/*  61 */   static final List<XMPPInputOutputStream> compressionHandlers = new ArrayList<>(2);
/*     */   
/*     */   static boolean smackInitialized = false;
/*     */   
/*  65 */   private static SmackDebuggerFactory debuggerFactory = (SmackDebuggerFactory)new ReflectionDebuggerFactory();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean DEBUG = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  86 */   private static ParsingExceptionCallback defaultCallback = (ParsingExceptionCallback)new ExceptionThrowingCallback();
/*     */ 
/*     */ 
/*     */   
/*     */   private static HostnameVerifier defaultHostnameVerififer;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getVersion() {
/*  96 */     return SmackInitialization.SMACK_VERSION;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getDefaultPacketReplyTimeout() {
/* 107 */     if (defaultPacketReplyTimeout <= 0) {
/* 108 */       defaultPacketReplyTimeout = 5000;
/*     */     }
/* 110 */     return defaultPacketReplyTimeout;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setDefaultPacketReplyTimeout(int timeout) {
/* 120 */     if (timeout <= 0) {
/* 121 */       throw new IllegalArgumentException();
/*     */     }
/* 123 */     defaultPacketReplyTimeout = timeout;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getPacketCollectorSize() {
/* 133 */     return packetCollectorSize;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setPacketCollectorSize(int collectorSize) {
/* 143 */     packetCollectorSize = collectorSize;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void addSaslMech(String mech) {
/* 152 */     if (!defaultMechs.contains(mech)) {
/* 153 */       defaultMechs.add(mech);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void addSaslMechs(Collection<String> mechs) {
/* 163 */     for (String mech : mechs) {
/* 164 */       addSaslMech(mech);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setDebuggerFactory(SmackDebuggerFactory debuggerFactory) {
/* 174 */     SmackConfiguration.debuggerFactory = debuggerFactory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static SmackDebuggerFactory getDebuggerFactory() {
/* 181 */     return debuggerFactory;
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
/*     */   public static SmackDebugger createDebugger(XMPPConnection connection, Writer writer, Reader reader) {
/* 195 */     SmackDebuggerFactory factory = getDebuggerFactory();
/* 196 */     if (factory == null) {
/* 197 */       return null;
/*     */     }
/* 199 */     return factory.create(connection, writer, reader);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void removeSaslMech(String mech) {
/* 209 */     defaultMechs.remove(mech);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void removeSaslMechs(Collection<String> mechs) {
/* 218 */     defaultMechs.removeAll(mechs);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static List<String> getSaslMechs() {
/* 229 */     return Collections.unmodifiableList(defaultMechs);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setDefaultParsingExceptionCallback(ParsingExceptionCallback callback) {
/* 239 */     defaultCallback = callback;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ParsingExceptionCallback getDefaultParsingExceptionCallback() {
/* 249 */     return defaultCallback;
/*     */   }
/*     */   
/*     */   public static void addCompressionHandler(XMPPInputOutputStream xmppInputOutputStream) {
/* 253 */     compressionHandlers.add(xmppInputOutputStream);
/*     */   }
/*     */   
/*     */   public static List<XMPPInputOutputStream> getCompresionHandlers() {
/* 257 */     List<XMPPInputOutputStream> res = new ArrayList<>(compressionHandlers.size());
/* 258 */     for (XMPPInputOutputStream ios : compressionHandlers) {
/* 259 */       if (ios.isSupported()) {
/* 260 */         res.add(ios);
/*     */       }
/*     */     } 
/* 263 */     return res;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setDefaultHostnameVerifier(HostnameVerifier verifier) {
/* 273 */     defaultHostnameVerififer = verifier;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void addDisabledSmackClass(Class<?> clz) {
/* 282 */     addDisabledSmackClass(clz.getName());
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
/*     */   public static void addDisabledSmackClass(String className) {
/* 295 */     disabledSmackClasses.add(className);
/*     */   }
/*     */   
/*     */   public static boolean isDisabledSmackClass(String className) {
/* 299 */     for (String disabledClassOrPackage : disabledSmackClasses) {
/* 300 */       if (disabledClassOrPackage.equals(className)) {
/* 301 */         return true;
/*     */       }
/* 303 */       int lastDotIndex = disabledClassOrPackage.lastIndexOf('.');
/*     */       
/* 305 */       if (disabledClassOrPackage.length() > lastDotIndex && !Character.isUpperCase(disabledClassOrPackage.charAt(lastDotIndex + 1)) && className.startsWith(disabledClassOrPackage))
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 311 */         return true;
/*     */       }
/*     */     } 
/* 314 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isSmackInitialized() {
/* 323 */     return smackInitialized;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static HostnameVerifier getDefaultHostnameVerifier() {
/* 332 */     return defaultHostnameVerififer;
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jivesoftware\smack\SmackConfiguration.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */