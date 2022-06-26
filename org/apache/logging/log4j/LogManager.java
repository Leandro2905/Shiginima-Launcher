/*     */ package org.apache.logging.log4j;
/*     */ 
/*     */ import java.net.URI;
/*     */ import java.util.Map;
/*     */ import java.util.SortedMap;
/*     */ import java.util.TreeMap;
/*     */ import org.apache.logging.log4j.message.MessageFactory;
/*     */ import org.apache.logging.log4j.message.StringFormatterMessageFactory;
/*     */ import org.apache.logging.log4j.simple.SimpleLoggerContextFactory;
/*     */ import org.apache.logging.log4j.spi.LoggerContext;
/*     */ import org.apache.logging.log4j.spi.LoggerContextFactory;
/*     */ import org.apache.logging.log4j.spi.Provider;
/*     */ import org.apache.logging.log4j.status.StatusLogger;
/*     */ import org.apache.logging.log4j.util.LoaderUtil;
/*     */ import org.apache.logging.log4j.util.PropertiesUtil;
/*     */ import org.apache.logging.log4j.util.ProviderUtil;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LogManager
/*     */ {
/*     */   private static volatile LoggerContextFactory factory;
/*     */   public static final String FACTORY_PROPERTY_NAME = "log4j2.loggerContextFactory";
/*  49 */   private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String ROOT_LOGGER_NAME = "";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static {
/*  62 */     PropertiesUtil managerProps = PropertiesUtil.getProperties();
/*  63 */     String factoryClassName = managerProps.getStringProperty("log4j2.loggerContextFactory");
/*  64 */     ClassLoader cl = LoaderUtil.getThreadContextClassLoader();
/*  65 */     if (factoryClassName != null) {
/*     */       try {
/*  67 */         Class<?> clazz = cl.loadClass(factoryClassName);
/*  68 */         if (LoggerContextFactory.class.isAssignableFrom(clazz)) {
/*  69 */           factory = (LoggerContextFactory)clazz.newInstance();
/*     */         }
/*  71 */       } catch (ClassNotFoundException cnfe) {
/*  72 */         LOGGER.error("Unable to locate configured LoggerContextFactory {}", new Object[] { factoryClassName });
/*  73 */       } catch (Exception ex) {
/*  74 */         LOGGER.error("Unable to create configured LoggerContextFactory {}", new Object[] { factoryClassName, ex });
/*     */       } 
/*     */     }
/*     */     
/*  78 */     if (factory == null) {
/*  79 */       SortedMap<Integer, LoggerContextFactory> factories = new TreeMap<Integer, LoggerContextFactory>();
/*     */       
/*  81 */       if (ProviderUtil.hasProviders()) {
/*  82 */         for (Provider provider : ProviderUtil.getProviders()) {
/*  83 */           Class<? extends LoggerContextFactory> factoryClass = provider.loadLoggerContextFactory();
/*  84 */           if (factoryClass != null) {
/*     */             try {
/*  86 */               factories.put(provider.getPriority(), factoryClass.newInstance());
/*  87 */             } catch (Exception e) {
/*  88 */               LOGGER.error("Unable to create class {} specified in {}", new Object[] { factoryClass.getName(), provider.getUrl().toString(), e });
/*     */             } 
/*     */           }
/*     */         } 
/*     */ 
/*     */         
/*  94 */         if (factories.isEmpty()) {
/*  95 */           LOGGER.error("Unable to locate a logging implementation, using SimpleLogger");
/*  96 */           factory = (LoggerContextFactory)new SimpleLoggerContextFactory();
/*     */         } else {
/*  98 */           StringBuilder sb = new StringBuilder("Multiple logging implementations found: \n");
/*  99 */           for (Map.Entry<Integer, LoggerContextFactory> entry : factories.entrySet()) {
/* 100 */             sb.append("Factory: ").append(((LoggerContextFactory)entry.getValue()).getClass().getName());
/* 101 */             sb.append(", Weighting: ").append(entry.getKey()).append('\n');
/*     */           } 
/* 103 */           factory = factories.get(factories.lastKey());
/* 104 */           sb.append("Using factory: ").append(factory.getClass().getName());
/* 105 */           LOGGER.warn(sb.toString());
/*     */         } 
/*     */       } else {
/*     */         
/* 109 */         LOGGER.error("Unable to locate a logging implementation, using SimpleLogger");
/* 110 */         factory = (LoggerContextFactory)new SimpleLoggerContextFactory();
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
/*     */   public static boolean exists(String name) {
/* 124 */     return getContext().hasLogger(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String getClassName(int depth) {
/* 134 */     return (new Throwable()).getStackTrace()[depth].getClassName();
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
/*     */   public static LoggerContext getContext() {
/* 146 */     return factory.getContext(LogManager.class.getName(), null, null, true);
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
/*     */   public static LoggerContext getContext(boolean currentContext) {
/* 159 */     return factory.getContext(LogManager.class.getName(), null, null, currentContext, null, null);
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
/*     */   public static LoggerContext getContext(ClassLoader loader, boolean currentContext) {
/* 174 */     return factory.getContext(LogManager.class.getName(), loader, null, currentContext);
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
/*     */   public static LoggerContext getContext(ClassLoader loader, boolean currentContext, Object externalContext) {
/* 191 */     return factory.getContext(LogManager.class.getName(), loader, externalContext, currentContext);
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
/*     */   public static LoggerContext getContext(ClassLoader loader, boolean currentContext, URI configLocation) {
/* 208 */     return factory.getContext(LogManager.class.getName(), loader, null, currentContext, configLocation, null);
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
/*     */   public static LoggerContext getContext(ClassLoader loader, boolean currentContext, Object externalContext, URI configLocation) {
/* 227 */     return factory.getContext(LogManager.class.getName(), loader, externalContext, currentContext, configLocation, null);
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
/*     */   public static LoggerContext getContext(ClassLoader loader, boolean currentContext, Object externalContext, URI configLocation, String name) {
/* 249 */     return factory.getContext(LogManager.class.getName(), loader, externalContext, currentContext, configLocation, name);
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
/*     */   protected static LoggerContext getContext(String fqcn, boolean currentContext) {
/* 263 */     return factory.getContext(fqcn, null, null, currentContext);
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
/*     */   protected static LoggerContext getContext(String fqcn, ClassLoader loader, boolean currentContext) {
/* 279 */     return factory.getContext(fqcn, loader, null, currentContext);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static LoggerContextFactory getFactory() {
/* 287 */     return factory;
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
/*     */   public static void setFactory(LoggerContextFactory factory) {
/* 305 */     LogManager.factory = factory;
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
/*     */   public static Logger getFormatterLogger(Class<?> clazz) {
/* 335 */     return getLogger((clazz != null) ? clazz.getName() : getClassName(2), (MessageFactory)StringFormatterMessageFactory.INSTANCE);
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
/*     */   public static Logger getFormatterLogger(Object value) {
/* 365 */     return getLogger((value != null) ? value.getClass().getName() : getClassName(2), (MessageFactory)StringFormatterMessageFactory.INSTANCE);
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
/*     */   public static Logger getFormatterLogger(String name) {
/* 395 */     return getLogger((name != null) ? name : getClassName(2), (MessageFactory)StringFormatterMessageFactory.INSTANCE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Logger getLogger() {
/* 403 */     return getLogger(getClassName(2));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Logger getLogger(Class<?> clazz) {
/* 413 */     return getLogger((clazz != null) ? clazz.getName() : getClassName(2));
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
/*     */   public static Logger getLogger(Class<?> clazz, MessageFactory messageFactory) {
/* 425 */     return getLogger((clazz != null) ? clazz.getName() : getClassName(2), messageFactory);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Logger getLogger(MessageFactory messageFactory) {
/* 435 */     return getLogger(getClassName(2), messageFactory);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Logger getLogger(Object value) {
/* 445 */     return getLogger((value != null) ? value.getClass().getName() : getClassName(2));
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
/*     */   public static Logger getLogger(Object value, MessageFactory messageFactory) {
/* 457 */     return getLogger((value != null) ? value.getClass().getName() : getClassName(2), messageFactory);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Logger getLogger(String name) {
/* 467 */     String actualName = (name != null) ? name : getClassName(2);
/* 468 */     return (Logger)factory.getContext(LogManager.class.getName(), null, null, false).getLogger(actualName);
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
/*     */   public static Logger getLogger(String name, MessageFactory messageFactory) {
/* 480 */     String actualName = (name != null) ? name : getClassName(2);
/* 481 */     return (Logger)factory.getContext(LogManager.class.getName(), null, null, false).getLogger(actualName, messageFactory);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static Logger getLogger(String fqcn, String name) {
/* 492 */     return (Logger)factory.getContext(fqcn, null, null, false).getLogger(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Logger getRootLogger() {
/* 501 */     return getLogger("");
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\LogManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */