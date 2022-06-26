/*     */ package org.jivesoftware.smack;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.lang.reflect.Field;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import org.jivesoftware.smack.compression.Java7ZlibInputOutputStream;
/*     */ import org.jivesoftware.smack.initializer.SmackInitializer;
/*     */ import org.jivesoftware.smack.provider.BindIQProvider;
/*     */ import org.jivesoftware.smack.provider.ProviderManager;
/*     */ import org.jivesoftware.smack.sasl.SASLMechanism;
/*     */ import org.jivesoftware.smack.sasl.core.SASLXOauth2Mechanism;
/*     */ import org.jivesoftware.smack.sasl.core.SCRAMSHA1Mechanism;
/*     */ import org.jivesoftware.smack.util.FileUtils;
/*     */ import org.xmlpull.v1.XmlPullParser;
/*     */ import org.xmlpull.v1.XmlPullParserException;
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
/*     */ public final class SmackInitialization
/*     */ {
/*     */   static final String SMACK_VERSION;
/*     */   private static final String DEFAULT_CONFIG_FILE = "classpath:org.jivesoftware.smack/smack-config.xml";
/*  48 */   private static final Logger LOGGER = Logger.getLogger(SmackInitialization.class.getName());
/*     */ 
/*     */ 
/*     */   
/*     */   static {
/*     */     String str1;
/*     */     InputStream configFileStream;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static {
/*     */     try {
/*  61 */       BufferedReader reader = new BufferedReader(new InputStreamReader(FileUtils.getStreamForUrl("classpath:org.jivesoftware.smack/version", null)));
/*  62 */       str1 = reader.readLine();
/*     */       try {
/*  64 */         reader.close();
/*  65 */       } catch (IOException e) {
/*  66 */         LOGGER.log(Level.WARNING, "IOException closing stream", e);
/*     */       } 
/*  68 */     } catch (Exception e) {
/*  69 */       LOGGER.log(Level.SEVERE, "Could not determine Smack version", e);
/*  70 */       str1 = "unkown";
/*     */     } 
/*  72 */     SMACK_VERSION = str1;
/*     */     
/*  74 */     String disabledClasses = System.getProperty("smack.disabledClasses");
/*  75 */     if (disabledClasses != null) {
/*  76 */       String[] splitDisabledClasses = disabledClasses.split(",");
/*  77 */       for (String s : splitDisabledClasses) SmackConfiguration.disabledSmackClasses.add(s); 
/*     */     } 
/*     */     try {
/*  80 */       FileUtils.addLines("classpath:org.jivesoftware.smack/disabledClasses", SmackConfiguration.disabledSmackClasses);
/*     */     }
/*  82 */     catch (Exception e) {
/*  83 */       throw new IllegalStateException(e);
/*     */     } 
/*     */     
/*     */     try {
/*  87 */       Class<?> c = Class.forName("org.jivesoftware.smack.CustomSmackConfiguration");
/*  88 */       Field f = c.getField("DISABLED_SMACK_CLASSES");
/*  89 */       String[] sa = (String[])f.get(null);
/*  90 */       if (sa != null) {
/*  91 */         LOGGER.warning("Using CustomSmackConfig is deprecated and will be removed in a future release");
/*  92 */         for (String s : sa) {
/*  93 */           SmackConfiguration.disabledSmackClasses.add(s);
/*     */         }
/*     */       } 
/*  96 */     } catch (ClassNotFoundException e1) {
/*     */     
/*  98 */     } catch (NoSuchFieldException e) {
/*     */     
/* 100 */     } catch (SecurityException e) {
/*     */     
/* 102 */     } catch (IllegalArgumentException e) {
/*     */     
/* 104 */     } catch (IllegalAccessException e) {}
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 109 */       configFileStream = FileUtils.getStreamForUrl("classpath:org.jivesoftware.smack/smack-config.xml", null);
/*     */     }
/* 111 */     catch (Exception e) {
/* 112 */       throw new IllegalStateException(e);
/*     */     } 
/*     */     
/*     */     try {
/* 116 */       processConfigFile(configFileStream, null);
/*     */     }
/* 118 */     catch (Exception e) {
/* 119 */       throw new IllegalStateException(e);
/*     */     } 
/*     */ 
/*     */     
/* 123 */     SmackConfiguration.compressionHandlers.add(new Java7ZlibInputOutputStream());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 130 */       if (Boolean.getBoolean("smack.debugEnabled")) {
/* 131 */         SmackConfiguration.DEBUG = true;
/*     */       }
/*     */     }
/* 134 */     catch (Exception e) {}
/*     */ 
/*     */ 
/*     */     
/* 138 */     SASLAuthentication.registerSASLMechanism((SASLMechanism)new SCRAMSHA1Mechanism());
/* 139 */     SASLAuthentication.registerSASLMechanism((SASLMechanism)new SASLXOauth2Mechanism());
/*     */     
/* 141 */     ProviderManager.addIQProvider("bind", "urn:ietf:params:xml:ns:xmpp-bind", new BindIQProvider());
/*     */     
/* 143 */     SmackConfiguration.smackInitialized = true;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void processConfigFile(InputStream cfgFileStream, Collection<Exception> exceptions) throws Exception {
/* 148 */     processConfigFile(cfgFileStream, exceptions, SmackInitialization.class.getClassLoader());
/*     */   }
/*     */ 
/*     */   
/*     */   public static void processConfigFile(InputStream cfgFileStream, Collection<Exception> exceptions, ClassLoader classLoader) throws Exception {
/* 153 */     XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
/* 154 */     parser.setFeature("http://xmlpull.org/v1/doc/features.html#process-namespaces", true);
/* 155 */     parser.setInput(cfgFileStream, "UTF-8");
/* 156 */     int eventType = parser.getEventType();
/*     */     do {
/* 158 */       if (eventType == 2) {
/* 159 */         if (parser.getName().equals("startupClasses")) {
/* 160 */           parseClassesToLoad(parser, false, exceptions, classLoader);
/*     */         }
/* 162 */         else if (parser.getName().equals("optionalStartupClasses")) {
/* 163 */           parseClassesToLoad(parser, true, exceptions, classLoader);
/*     */         } 
/*     */       }
/* 166 */       eventType = parser.next();
/*     */     }
/* 168 */     while (eventType != 1);
/*     */     try {
/* 170 */       cfgFileStream.close();
/*     */     }
/* 172 */     catch (IOException e) {
/* 173 */       LOGGER.log(Level.SEVERE, "Error while closing config file input stream", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static void parseClassesToLoad(XmlPullParser parser, boolean optional, Collection<Exception> exceptions, ClassLoader classLoader) throws XmlPullParserException, IOException, Exception {
/*     */     int eventType;
/* 180 */     String name, startName = parser.getName();
/*     */ 
/*     */     
/*     */     do {
/* 184 */       eventType = parser.next();
/* 185 */       name = parser.getName();
/* 186 */       if (eventType != 2 || !"className".equals(name))
/* 187 */         continue;  String classToLoad = parser.nextText();
/* 188 */       if (SmackConfiguration.isDisabledSmackClass(classToLoad)) {
/*     */         continue;
/*     */       }
/*     */       
/*     */       try {
/* 193 */         loadSmackClass(classToLoad, optional, classLoader);
/* 194 */       } catch (Exception e) {
/*     */ 
/*     */         
/* 197 */         if (exceptions != null) {
/* 198 */           exceptions.add(e);
/*     */         } else {
/* 200 */           throw e;
/*     */         }
/*     */       
/*     */       }
/*     */     
/* 205 */     } while (eventType != 3 || !startName.equals(name));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void loadSmackClass(String className, boolean optional, ClassLoader classLoader) throws Exception {
/*     */     Class<?> initClass;
/*     */     try {
/* 213 */       initClass = Class.forName(className, true, classLoader);
/*     */     }
/* 215 */     catch (ClassNotFoundException cnfe) {
/*     */       Level logLevel;
/* 217 */       if (optional) {
/* 218 */         logLevel = Level.FINE;
/*     */       } else {
/*     */         
/* 221 */         logLevel = Level.WARNING;
/*     */       } 
/* 223 */       LOGGER.log(logLevel, "A startup class '" + className + "' could not be loaded.");
/* 224 */       if (!optional) {
/* 225 */         throw cnfe;
/*     */       }
/*     */       
/*     */       return;
/*     */     } 
/* 230 */     if (SmackInitializer.class.isAssignableFrom(initClass)) {
/* 231 */       SmackInitializer initializer = (SmackInitializer)initClass.newInstance();
/* 232 */       List<Exception> exceptions = initializer.initialize();
/* 233 */       if (exceptions == null || exceptions.size() == 0) {
/* 234 */         LOGGER.log(Level.FINE, "Loaded SmackInitializer " + className);
/*     */       } else {
/* 236 */         for (Exception e : exceptions) {
/* 237 */           LOGGER.log(Level.SEVERE, "Exception in loadSmackClass", e);
/*     */         }
/*     */       } 
/*     */     } else {
/* 241 */       LOGGER.log(Level.FINE, "Loaded " + className);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jivesoftware\smack\SmackInitialization.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */