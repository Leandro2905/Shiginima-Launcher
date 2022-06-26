/*     */ package org.jivesoftware.smack.provider;
/*     */ 
/*     */ import java.io.InputStream;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import org.jivesoftware.smack.packet.ExtensionElement;
/*     */ import org.jivesoftware.smack.packet.IQ;
/*     */ import org.xmlpull.v1.XmlPullParser;
/*     */ import org.xmlpull.v1.XmlPullParserFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ProviderFileLoader
/*     */   implements ProviderLoader
/*     */ {
/*  40 */   private static final Logger LOGGER = Logger.getLogger(ProviderFileLoader.class.getName());
/*     */   
/*  42 */   private final Collection<IQProviderInfo> iqProviders = new LinkedList<>();
/*  43 */   private final Collection<ExtensionProviderInfo> extProviders = new LinkedList<>();
/*  44 */   private final Collection<StreamFeatureProviderInfo> sfProviders = new LinkedList<>();
/*     */   
/*  46 */   private List<Exception> exceptions = new LinkedList<>();
/*     */   
/*     */   public ProviderFileLoader(InputStream providerStream) {
/*  49 */     this(providerStream, ProviderFileLoader.class.getClassLoader());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ProviderFileLoader(InputStream providerStream, ClassLoader classLoader) {
/*     */     try {
/*  56 */       XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
/*  57 */       parser.setFeature("http://xmlpull.org/v1/doc/features.html#process-namespaces", true);
/*  58 */       parser.setInput(providerStream, "UTF-8");
/*  59 */       int eventType = parser.getEventType();
/*     */       do {
/*  61 */         if (eventType == 2) {
/*  62 */           String typeName = parser.getName();
/*     */           
/*     */           try {
/*  65 */             if (!"smackProviders".equals(typeName)) {
/*  66 */               parser.next();
/*  67 */               parser.next();
/*  68 */               String elementName = parser.nextText();
/*  69 */               parser.next();
/*  70 */               parser.next();
/*  71 */               String namespace = parser.nextText();
/*  72 */               parser.next();
/*  73 */               parser.next();
/*  74 */               String className = parser.nextText();
/*     */               
/*     */               try {
/*  77 */                 Class<?> provider = classLoader.loadClass(className);
/*  78 */                 switch (typeName) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */                   
/*     */                   case "iqProvider":
/*  85 */                     if (IQProvider.class.isAssignableFrom(provider)) {
/*  86 */                       this.iqProviders.add(new IQProviderInfo(elementName, namespace, (IQProvider<IQ>)provider.newInstance()));
/*     */                       break;
/*     */                     } 
/*  89 */                     this.exceptions.add(new IllegalArgumentException(className + " is not a IQProvider"));
/*     */                     break;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */                   
/*     */                   case "extensionProvider":
/*  98 */                     if (ExtensionElementProvider.class.isAssignableFrom(provider)) {
/*  99 */                       this.extProviders.add(new ExtensionProviderInfo(elementName, namespace, (ExtensionElementProvider<ExtensionElement>)provider.newInstance()));
/*     */                       break;
/*     */                     } 
/* 102 */                     this.exceptions.add(new IllegalArgumentException(className + " is not a PacketExtensionProvider"));
/*     */                     break;
/*     */ 
/*     */                   
/*     */                   case "streamFeatureProvider":
/* 107 */                     this.sfProviders.add(new StreamFeatureProviderInfo(elementName, namespace, (ExtensionElementProvider<ExtensionElement>)provider.newInstance()));
/*     */                     break;
/*     */ 
/*     */                   
/*     */                   default:
/* 112 */                     LOGGER.warning("Unknown provider type: " + typeName);
/*     */                     break;
/*     */                 } 
/* 115 */               } catch (ClassNotFoundException cnfe) {
/* 116 */                 LOGGER.log(Level.SEVERE, "Could not find provider class", cnfe);
/* 117 */                 this.exceptions.add(cnfe);
/*     */               }
/* 119 */               catch (InstantiationException ie) {
/* 120 */                 LOGGER.log(Level.SEVERE, "Could not instanciate " + className, ie);
/* 121 */                 this.exceptions.add(ie);
/*     */               }
/*     */             
/*     */             } 
/* 125 */           } catch (IllegalArgumentException illExc) {
/* 126 */             LOGGER.log(Level.SEVERE, "Invalid provider type found [" + typeName + "] when expecting iqProvider or extensionProvider", illExc);
/* 127 */             this.exceptions.add(illExc);
/*     */           } 
/*     */         } 
/* 130 */         eventType = parser.next();
/*     */       }
/* 132 */       while (eventType != 1);
/*     */     }
/* 134 */     catch (Exception e) {
/* 135 */       LOGGER.log(Level.SEVERE, "Unknown error occurred while parsing provider file", e);
/* 136 */       this.exceptions.add(e);
/*     */     } finally {
/*     */       
/*     */       try {
/* 140 */         providerStream.close();
/*     */       }
/* 142 */       catch (Exception e) {}
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Collection<IQProviderInfo> getIQProviderInfo() {
/* 150 */     return this.iqProviders;
/*     */   }
/*     */ 
/*     */   
/*     */   public Collection<ExtensionProviderInfo> getExtensionProviderInfo() {
/* 155 */     return this.extProviders;
/*     */   }
/*     */ 
/*     */   
/*     */   public Collection<StreamFeatureProviderInfo> getStreamFeatureProviderInfo() {
/* 160 */     return this.sfProviders;
/*     */   }
/*     */   
/*     */   public List<Exception> getLoadingExceptions() {
/* 164 */     return Collections.unmodifiableList(this.exceptions);
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jivesoftware\smack\provider\ProviderFileLoader.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */