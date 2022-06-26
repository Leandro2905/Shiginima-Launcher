/*     */ package org.apache.logging.log4j.core.config;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.net.URL;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.TreeSet;
/*     */ import java.util.concurrent.locks.Lock;
/*     */ import java.util.concurrent.locks.ReentrantLock;
/*     */ import org.apache.logging.log4j.Level;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.core.config.plugins.util.PluginManager;
/*     */ import org.apache.logging.log4j.core.config.plugins.util.PluginType;
/*     */ import org.apache.logging.log4j.core.lookup.Interpolator;
/*     */ import org.apache.logging.log4j.core.lookup.StrLookup;
/*     */ import org.apache.logging.log4j.core.lookup.StrSubstitutor;
/*     */ import org.apache.logging.log4j.core.util.FileUtils;
/*     */ import org.apache.logging.log4j.core.util.Loader;
/*     */ import org.apache.logging.log4j.status.StatusLogger;
/*     */ import org.apache.logging.log4j.util.PropertiesUtil;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class ConfigurationFactory
/*     */ {
/*     */   public static final String CONFIGURATION_FACTORY_PROPERTY = "log4j.configurationFactory";
/*     */   public static final String CONFIGURATION_FILE_PROPERTY = "log4j.configurationFile";
/*  84 */   protected static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static final String TEST_PREFIX = "log4j2-test";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static final String DEFAULT_PREFIX = "log4j2";
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String CLASS_LOADER_SCHEME = "classloader";
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String CLASS_PATH_SCHEME = "classpath";
/*     */ 
/*     */ 
/*     */   
/* 106 */   private static volatile List<ConfigurationFactory> factories = null;
/*     */   
/* 108 */   private static ConfigurationFactory configFactory = new Factory();
/*     */   
/* 110 */   protected final StrSubstitutor substitutor = new StrSubstitutor((StrLookup)new Interpolator());
/*     */   
/* 112 */   private static final Lock LOCK = new ReentrantLock();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ConfigurationFactory getInstance() {
/* 121 */     if (factories == null) {
/* 122 */       LOCK.lock();
/*     */       try {
/* 124 */         if (factories == null) {
/* 125 */           List<ConfigurationFactory> list = new ArrayList<ConfigurationFactory>();
/* 126 */           String factoryClass = PropertiesUtil.getProperties().getStringProperty("log4j.configurationFactory");
/* 127 */           if (factoryClass != null) {
/* 128 */             addFactory(list, factoryClass);
/*     */           }
/* 130 */           PluginManager manager = new PluginManager("ConfigurationFactory");
/* 131 */           manager.collectPlugins();
/* 132 */           Map<String, PluginType<?>> plugins = manager.getPlugins();
/* 133 */           Collection<WeightedFactory> ordered = new TreeSet<WeightedFactory>();
/* 134 */           for (PluginType<?> type : plugins.values()) {
/*     */             
/*     */             try {
/* 137 */               Class<ConfigurationFactory> clazz = type.getPluginClass();
/* 138 */               Order order = clazz.<Order>getAnnotation(Order.class);
/* 139 */               if (order != null) {
/* 140 */                 int weight = order.value();
/* 141 */                 ordered.add(new WeightedFactory(weight, clazz));
/*     */               } 
/* 143 */             } catch (Exception ex) {
/* 144 */               LOGGER.warn("Unable to add class {}", new Object[] { type.getPluginClass(), ex });
/*     */             } 
/*     */           } 
/* 147 */           for (WeightedFactory wf : ordered) {
/* 148 */             addFactory(list, wf.factoryClass);
/*     */           }
/*     */ 
/*     */           
/* 152 */           factories = Collections.unmodifiableList(list);
/*     */         } 
/*     */       } finally {
/* 155 */         LOCK.unlock();
/*     */       } 
/*     */     } 
/*     */     
/* 159 */     LOGGER.debug("Using configurationFactory {}", new Object[] { configFactory });
/* 160 */     return configFactory;
/*     */   }
/*     */ 
/*     */   
/*     */   private static void addFactory(Collection<ConfigurationFactory> list, String factoryClass) {
/*     */     try {
/* 166 */       addFactory(list, Loader.loadClass(factoryClass));
/* 167 */     } catch (Exception ex) {
/* 168 */       LOGGER.error("Unable to load class {}", new Object[] { factoryClass, ex });
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static void addFactory(Collection<ConfigurationFactory> list, Class<ConfigurationFactory> factoryClass) {
/*     */     try {
/* 175 */       list.add(factoryClass.getConstructor(new Class[0]).newInstance(new Object[0]));
/* 176 */     } catch (Exception ex) {
/* 177 */       LOGGER.error("Unable to create instance of {}", new Object[] { factoryClass.getName(), ex });
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setConfigurationFactory(ConfigurationFactory factory) {
/* 186 */     configFactory = factory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void resetConfigurationFactory() {
/* 194 */     configFactory = new Factory();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void removeConfigurationFactory(ConfigurationFactory factory) {
/* 202 */     if (configFactory == factory) {
/* 203 */       configFactory = new Factory();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isActive() {
/* 210 */     return true;
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
/*     */   public Configuration getConfiguration(String name, URI configLocation) {
/* 222 */     if (!isActive()) {
/* 223 */       return null;
/*     */     }
/* 225 */     if (configLocation != null) {
/* 226 */       ConfigurationSource source = getInputFromUri(configLocation);
/* 227 */       if (source != null) {
/* 228 */         return getConfiguration(source);
/*     */       }
/*     */     } 
/* 231 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ConfigurationSource getInputFromUri(URI configLocation) {
/* 240 */     File configFile = FileUtils.fileFromUri(configLocation);
/* 241 */     if (configFile != null && configFile.exists() && configFile.canRead()) {
/*     */       try {
/* 243 */         return new ConfigurationSource(new FileInputStream(configFile), configFile);
/* 244 */       } catch (FileNotFoundException ex) {
/* 245 */         LOGGER.error("Cannot locate file {}", new Object[] { configLocation.getPath(), ex });
/*     */       } 
/*     */     }
/* 248 */     String scheme = configLocation.getScheme();
/* 249 */     boolean isClassLoaderScheme = (scheme != null && scheme.equals("classloader"));
/* 250 */     boolean isClassPathScheme = (scheme != null && !isClassLoaderScheme && scheme.equals("classpath"));
/* 251 */     if (scheme == null || isClassLoaderScheme || isClassPathScheme) {
/* 252 */       String path; ClassLoader loader = Loader.getThreadContextClassLoader();
/*     */       
/* 254 */       if (isClassLoaderScheme || isClassPathScheme) {
/* 255 */         path = configLocation.getSchemeSpecificPart();
/*     */       } else {
/* 257 */         path = configLocation.getPath();
/*     */       } 
/* 259 */       ConfigurationSource source = getInputFromResource(path, loader);
/* 260 */       if (source != null) {
/* 261 */         return source;
/*     */       }
/*     */     } 
/* 264 */     if (!configLocation.isAbsolute()) {
/* 265 */       LOGGER.error("File not found in file system or classpath: {}", new Object[] { configLocation.toString() });
/* 266 */       return null;
/*     */     } 
/*     */     try {
/* 269 */       return new ConfigurationSource(configLocation.toURL().openStream(), configLocation.toURL());
/* 270 */     } catch (MalformedURLException ex) {
/* 271 */       LOGGER.error("Invalid URL {}", new Object[] { configLocation.toString(), ex });
/* 272 */     } catch (Exception ex) {
/* 273 */       LOGGER.error("Unable to access {}", new Object[] { configLocation.toString(), ex });
/*     */     } 
/* 275 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ConfigurationSource getInputFromString(String config, ClassLoader loader) {
/*     */     try {
/* 286 */       URL url = new URL(config);
/* 287 */       return new ConfigurationSource(url.openStream(), FileUtils.fileFromUri(url.toURI()));
/* 288 */     } catch (Exception ex) {
/* 289 */       ConfigurationSource source = getInputFromResource(config, loader);
/* 290 */       if (source == null) {
/*     */         try {
/* 292 */           File file = new File(config);
/* 293 */           return new ConfigurationSource(new FileInputStream(file), file);
/* 294 */         } catch (FileNotFoundException fnfe) {
/*     */           
/* 296 */           LOGGER.catching(Level.DEBUG, fnfe);
/*     */         } 
/*     */       }
/* 299 */       return source;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ConfigurationSource getInputFromResource(String resource, ClassLoader loader) {
/* 310 */     URL url = Loader.getResource(resource, loader);
/* 311 */     if (url == null) {
/* 312 */       return null;
/*     */     }
/* 314 */     InputStream is = null;
/*     */     try {
/* 316 */       is = url.openStream();
/* 317 */     } catch (IOException ioe) {
/* 318 */       LOGGER.catching(Level.DEBUG, ioe);
/* 319 */       return null;
/*     */     } 
/* 321 */     if (is == null) {
/* 322 */       return null;
/*     */     }
/*     */     
/* 325 */     if (FileUtils.isFile(url)) {
/*     */       try {
/* 327 */         return new ConfigurationSource(is, FileUtils.fileFromUri(url.toURI()));
/* 328 */       } catch (URISyntaxException ex) {
/*     */         
/* 330 */         LOGGER.catching(Level.DEBUG, ex);
/*     */       } 
/*     */     }
/* 333 */     return new ConfigurationSource(is, url);
/*     */   }
/*     */ 
/*     */   
/*     */   protected abstract String[] getSupportedTypes();
/*     */ 
/*     */   
/*     */   public abstract Configuration getConfiguration(ConfigurationSource paramConfigurationSource);
/*     */   
/*     */   private static class WeightedFactory
/*     */     implements Comparable<WeightedFactory>
/*     */   {
/*     */     private final int weight;
/*     */     private final Class<ConfigurationFactory> factoryClass;
/*     */     
/*     */     public WeightedFactory(int weight, Class<ConfigurationFactory> clazz) {
/* 349 */       this.weight = weight;
/* 350 */       this.factoryClass = clazz;
/*     */     }
/*     */ 
/*     */     
/*     */     public int compareTo(WeightedFactory wf) {
/* 355 */       int w = wf.weight;
/* 356 */       if (this.weight == w)
/* 357 */         return 0; 
/* 358 */       if (this.weight > w) {
/* 359 */         return -1;
/*     */       }
/* 361 */       return 1;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class Factory
/*     */     extends ConfigurationFactory
/*     */   {
/*     */     private Factory() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Configuration getConfiguration(String name, URI configLocation) {
/* 380 */       if (configLocation == null) {
/* 381 */         String str = this.substitutor.replace(PropertiesUtil.getProperties().getStringProperty("log4j.configurationFile"));
/*     */         
/* 383 */         if (str != null) {
/* 384 */           ConfigurationSource source = null;
/*     */           try {
/* 386 */             source = getInputFromUri(FileUtils.getCorrectedFilePathUri(str));
/* 387 */           } catch (Exception ex) {
/*     */             
/* 389 */             LOGGER.catching(Level.DEBUG, ex);
/*     */           } 
/* 391 */           if (source == null) {
/* 392 */             ClassLoader loader = getClass().getClassLoader();
/* 393 */             source = getInputFromString(str, loader);
/*     */           } 
/* 395 */           if (source != null) {
/* 396 */             for (ConfigurationFactory factory : ConfigurationFactory.factories) {
/* 397 */               String[] types = factory.getSupportedTypes();
/* 398 */               if (types != null) {
/* 399 */                 for (String type : types) {
/* 400 */                   if (type.equals("*") || str.endsWith(type)) {
/* 401 */                     Configuration c = factory.getConfiguration(source);
/* 402 */                     if (c != null) {
/* 403 */                       return c;
/*     */                     }
/*     */                   } 
/*     */                 } 
/*     */               }
/*     */             } 
/*     */           }
/*     */         } 
/*     */       } else {
/* 412 */         for (ConfigurationFactory factory : ConfigurationFactory.factories) {
/* 413 */           String[] types = factory.getSupportedTypes();
/* 414 */           if (types != null) {
/* 415 */             for (String type : types) {
/* 416 */               if (type.equals("*") || configLocation.toString().endsWith(type)) {
/* 417 */                 Configuration configuration = factory.getConfiguration(name, configLocation);
/* 418 */                 if (configuration != null) {
/* 419 */                   return configuration;
/*     */                 }
/*     */               } 
/*     */             } 
/*     */           }
/*     */         } 
/*     */       } 
/*     */       
/* 427 */       Configuration config = getConfiguration(true, name);
/* 428 */       if (config == null) {
/* 429 */         config = getConfiguration(true, (String)null);
/* 430 */         if (config == null) {
/* 431 */           config = getConfiguration(false, name);
/* 432 */           if (config == null) {
/* 433 */             config = getConfiguration(false, (String)null);
/*     */           }
/*     */         } 
/*     */       } 
/* 437 */       return (config != null) ? config : new DefaultConfiguration();
/*     */     }
/*     */     
/*     */     private Configuration getConfiguration(boolean isTest, String name) {
/* 441 */       boolean named = (name != null && name.length() > 0);
/* 442 */       ClassLoader loader = getClass().getClassLoader();
/* 443 */       for (ConfigurationFactory factory : ConfigurationFactory.factories) {
/*     */         
/* 445 */         String prefix = isTest ? "log4j2-test" : "log4j2";
/* 446 */         String[] types = factory.getSupportedTypes();
/* 447 */         if (types == null) {
/*     */           continue;
/*     */         }
/*     */         
/* 451 */         for (String suffix : types) {
/* 452 */           if (!suffix.equals("*")) {
/*     */ 
/*     */             
/* 455 */             String configName = named ? (prefix + name + suffix) : (prefix + suffix);
/*     */             
/* 457 */             ConfigurationSource source = getInputFromResource(configName, loader);
/* 458 */             if (source != null)
/* 459 */               return factory.getConfiguration(source); 
/*     */           } 
/*     */         } 
/*     */       } 
/* 463 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     public String[] getSupportedTypes() {
/* 468 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     public Configuration getConfiguration(ConfigurationSource source) {
/* 473 */       if (source != null) {
/* 474 */         String config = source.getLocation();
/* 475 */         for (ConfigurationFactory factory : ConfigurationFactory.factories) {
/* 476 */           String[] types = factory.getSupportedTypes();
/* 477 */           if (types != null) {
/* 478 */             for (String type : types) {
/* 479 */               if (type.equals("*") || (config != null && config.endsWith(type))) {
/* 480 */                 Configuration c = factory.getConfiguration(source);
/* 481 */                 if (c != null) {
/* 482 */                   LOGGER.debug("Loaded configuration from {}", new Object[] { source });
/* 483 */                   return c;
/*     */                 } 
/* 485 */                 LOGGER.error("Cannot determine the ConfigurationFactory to use for {}", new Object[] { config });
/* 486 */                 return null;
/*     */               } 
/*     */             } 
/*     */           }
/*     */         } 
/*     */       } 
/* 492 */       LOGGER.error("Cannot process configuration, input source is null");
/* 493 */       return null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\config\ConfigurationFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */