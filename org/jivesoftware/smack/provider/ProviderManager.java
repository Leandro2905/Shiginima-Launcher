/*     */ package org.jivesoftware.smack.provider;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.jivesoftware.smack.SmackConfiguration;
/*     */ import org.jivesoftware.smack.packet.ExtensionElement;
/*     */ import org.jivesoftware.smack.packet.IQ;
/*     */ import org.jivesoftware.smack.util.StringUtils;
/*     */ import org.jxmpp.util.XmppStringUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ProviderManager
/*     */ {
/* 112 */   private static final Map<String, ExtensionElementProvider<ExtensionElement>> extensionProviders = new ConcurrentHashMap<>();
/* 113 */   private static final Map<String, IQProvider<IQ>> iqProviders = new ConcurrentHashMap<>();
/* 114 */   private static final Map<String, ExtensionElementProvider<ExtensionElement>> streamFeatureProviders = new ConcurrentHashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static {
/* 121 */     SmackConfiguration.getVersion();
/*     */   }
/*     */ 
/*     */   
/*     */   public static void addLoader(ProviderLoader loader) {
/* 126 */     if (loader.getIQProviderInfo() != null) {
/* 127 */       for (IQProviderInfo info : loader.getIQProviderInfo()) {
/* 128 */         addIQProvider(info.getElementName(), info.getNamespace(), info.getProvider());
/*     */       }
/*     */     }
/*     */     
/* 132 */     if (loader.getExtensionProviderInfo() != null) {
/* 133 */       for (ExtensionProviderInfo info : loader.getExtensionProviderInfo()) {
/* 134 */         addExtensionProvider(info.getElementName(), info.getNamespace(), info.getProvider());
/*     */       }
/*     */     }
/*     */     
/* 138 */     if (loader.getStreamFeatureProviderInfo() != null) {
/* 139 */       for (StreamFeatureProviderInfo info : loader.getStreamFeatureProviderInfo()) {
/* 140 */         addStreamFeatureProvider(info.getElementName(), info.getNamespace(), (ExtensionElementProvider<ExtensionElement>)info.getProvider());
/*     */       }
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
/*     */   public static IQProvider<IQ> getIQProvider(String elementName, String namespace) {
/* 167 */     String key = getKey(elementName, namespace);
/* 168 */     return iqProviders.get(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static List<IQProvider<IQ>> getIQProviders() {
/* 179 */     List<IQProvider<IQ>> providers = new ArrayList<>(iqProviders.size());
/* 180 */     providers.addAll(iqProviders.values());
/* 181 */     return providers;
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
/*     */   public static void addIQProvider(String elementName, String namespace, Object provider) {
/* 197 */     validate(elementName, namespace);
/*     */     
/* 199 */     String key = removeIQProvider(elementName, namespace);
/* 200 */     if (provider instanceof IQProvider) {
/* 201 */       iqProviders.put(key, (IQProvider<IQ>)provider);
/*     */     } else {
/* 203 */       throw new IllegalArgumentException("Provider must be an IQProvider");
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
/*     */   public static String removeIQProvider(String elementName, String namespace) {
/* 217 */     String key = getKey(elementName, namespace);
/* 218 */     iqProviders.remove(key);
/* 219 */     return key;
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
/*     */   public static ExtensionElementProvider<ExtensionElement> getExtensionProvider(String elementName, String namespace) {
/* 242 */     String key = getKey(elementName, namespace);
/* 243 */     return extensionProviders.get(key);
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
/*     */   public static void addExtensionProvider(String elementName, String namespace, Object provider) {
/* 259 */     validate(elementName, namespace);
/*     */     
/* 261 */     String key = removeExtensionProvider(elementName, namespace);
/* 262 */     if (provider instanceof ExtensionElementProvider) {
/* 263 */       extensionProviders.put(key, (ExtensionElementProvider<ExtensionElement>)provider);
/*     */     } else {
/* 265 */       throw new IllegalArgumentException("Provider must be a PacketExtensionProvider");
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
/*     */   public static String removeExtensionProvider(String elementName, String namespace) {
/* 279 */     String key = getKey(elementName, namespace);
/* 280 */     extensionProviders.remove(key);
/* 281 */     return key;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static List<ExtensionElementProvider<ExtensionElement>> getExtensionProviders() {
/* 292 */     List<ExtensionElementProvider<ExtensionElement>> providers = new ArrayList<>(extensionProviders.size());
/* 293 */     providers.addAll(extensionProviders.values());
/* 294 */     return providers;
/*     */   }
/*     */   
/*     */   public static ExtensionElementProvider<ExtensionElement> getStreamFeatureProvider(String elementName, String namespace) {
/* 298 */     String key = getKey(elementName, namespace);
/* 299 */     return streamFeatureProviders.get(key);
/*     */   }
/*     */   
/*     */   public static void addStreamFeatureProvider(String elementName, String namespace, ExtensionElementProvider<ExtensionElement> provider) {
/* 303 */     validate(elementName, namespace);
/* 304 */     String key = getKey(elementName, namespace);
/* 305 */     streamFeatureProviders.put(key, provider);
/*     */   }
/*     */   
/*     */   public static void removeStreamFeatureProvider(String elementName, String namespace) {
/* 309 */     String key = getKey(elementName, namespace);
/* 310 */     streamFeatureProviders.remove(key);
/*     */   }
/*     */   
/*     */   private static String getKey(String elementName, String namespace) {
/* 314 */     return XmppStringUtils.generateKey(elementName, namespace);
/*     */   }
/*     */   
/*     */   private static void validate(String elementName, String namespace) {
/* 318 */     if (StringUtils.isNullOrEmpty(elementName)) {
/* 319 */       throw new IllegalArgumentException("elementName must not be null or empty");
/*     */     }
/* 321 */     if (StringUtils.isNullOrEmpty(namespace))
/* 322 */       throw new IllegalArgumentException("namespace must not be null or empty"); 
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jivesoftware\smack\provider\ProviderManager.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */