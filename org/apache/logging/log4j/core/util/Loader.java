/*     */ package org.apache.logging.log4j.core.util;
/*     */ 
/*     */ import java.io.InputStream;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.ReflectPermission;
/*     */ import java.net.URL;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.status.StatusLogger;
/*     */ import org.apache.logging.log4j.util.LoaderUtil;
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
/*     */ public final class Loader
/*     */ {
/*     */   private static boolean ignoreTCL = false;
/*  37 */   private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*     */   
/*     */   private static final String TSTR = "Caught Exception while in Loader.getResource. This may be innocuous.";
/*     */   
/*     */   static {
/*  42 */     String ignoreTCLProp = PropertiesUtil.getProperties().getStringProperty("log4j.ignoreTCL", null);
/*  43 */     if (ignoreTCLProp != null) {
/*  44 */       ignoreTCL = OptionConverter.toBoolean(ignoreTCLProp, true);
/*     */     }
/*  46 */     SecurityManager sm = System.getSecurityManager();
/*  47 */     if (sm != null) {
/*  48 */       sm.checkPermission(new RuntimePermission("getStackTrace"));
/*  49 */       sm.checkPermission(new ReflectPermission("suppressAccessChecks"));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ClassLoader getClassLoader() {
/*  59 */     return getClassLoader(Loader.class, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ClassLoader getThreadContextClassLoader() {
/*  69 */     return LoaderUtil.getThreadContextClassLoader();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static ClassLoader getClassLoader(Class<?> class1, Class<?> class2) {
/*  75 */     ClassLoader threadContextClassLoader = null;
/*     */     try {
/*  77 */       threadContextClassLoader = getTcl();
/*  78 */     } catch (Exception ex) {
/*  79 */       LOGGER.warn("Caught exception locating thread ClassLoader {}", new Object[] { ex.getMessage() });
/*     */     } 
/*  81 */     ClassLoader loader1 = (class1 == null) ? null : class1.getClassLoader();
/*  82 */     ClassLoader loader2 = (class2 == null) ? null : class2.getClassLoader();
/*     */     
/*  84 */     if (isChild(threadContextClassLoader, loader1)) {
/*  85 */       return isChild(threadContextClassLoader, loader2) ? threadContextClassLoader : loader2;
/*     */     }
/*  87 */     return isChild(loader1, loader2) ? loader1 : loader2;
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
/*     */   public static URL getResource(String resource, ClassLoader defaultLoader) {
/*     */     try {
/* 114 */       ClassLoader classLoader = getTcl();
/* 115 */       if (classLoader != null) {
/* 116 */         LOGGER.trace("Trying to find [{}] using context class loader {}.", new Object[] { resource, classLoader });
/* 117 */         URL url = classLoader.getResource(resource);
/* 118 */         if (url != null) {
/* 119 */           return url;
/*     */         }
/*     */       } 
/*     */ 
/*     */       
/* 124 */       classLoader = Loader.class.getClassLoader();
/* 125 */       if (classLoader != null) {
/* 126 */         LOGGER.trace("Trying to find [{}] using {} class loader.", new Object[] { resource, classLoader });
/* 127 */         URL url = classLoader.getResource(resource);
/* 128 */         if (url != null) {
/* 129 */           return url;
/*     */         }
/*     */       } 
/*     */       
/* 133 */       if (defaultLoader != null) {
/* 134 */         LOGGER.trace("Trying to find [{}] using {} class loader.", new Object[] { resource, defaultLoader });
/* 135 */         URL url = defaultLoader.getResource(resource);
/* 136 */         if (url != null) {
/* 137 */           return url;
/*     */         }
/*     */       } 
/* 140 */     } catch (Throwable t) {
/*     */ 
/*     */ 
/*     */       
/* 144 */       LOGGER.warn("Caught Exception while in Loader.getResource. This may be innocuous.", t);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 151 */     LOGGER.trace("Trying to find [{}] using ClassLoader.getSystemResource().", new Object[] { resource });
/* 152 */     return ClassLoader.getSystemResource(resource);
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
/*     */   public static InputStream getResourceAsStream(String resource, ClassLoader defaultLoader) {
/*     */     try {
/* 179 */       ClassLoader classLoader = getTcl();
/*     */       
/* 181 */       if (classLoader != null) {
/* 182 */         LOGGER.trace("Trying to find [{}] using context class loader {}.", new Object[] { resource, classLoader });
/* 183 */         InputStream is = classLoader.getResourceAsStream(resource);
/* 184 */         if (is != null) {
/* 185 */           return is;
/*     */         }
/*     */       } 
/*     */ 
/*     */       
/* 190 */       classLoader = Loader.class.getClassLoader();
/* 191 */       if (classLoader != null) {
/* 192 */         LOGGER.trace("Trying to find [{}] using {} class loader.", new Object[] { resource, classLoader });
/* 193 */         InputStream is = classLoader.getResourceAsStream(resource);
/* 194 */         if (is != null) {
/* 195 */           return is;
/*     */         }
/*     */       } 
/*     */ 
/*     */       
/* 200 */       if (defaultLoader != null) {
/* 201 */         LOGGER.trace("Trying to find [{}] using {} class loader.", new Object[] { resource, defaultLoader });
/* 202 */         InputStream is = defaultLoader.getResourceAsStream(resource);
/* 203 */         if (is != null) {
/* 204 */           return is;
/*     */         }
/*     */       } 
/* 207 */     } catch (Throwable t) {
/*     */ 
/*     */ 
/*     */       
/* 211 */       LOGGER.warn("Caught Exception while in Loader.getResource. This may be innocuous.", t);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 218 */     LOGGER.trace("Trying to find [{}] using ClassLoader.getSystemResource().", new Object[] { resource });
/* 219 */     return ClassLoader.getSystemResourceAsStream(resource);
/*     */   }
/*     */   
/*     */   private static ClassLoader getTcl() {
/* 223 */     return LoaderUtil.getThreadContextClassLoader();
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
/*     */   private static boolean isChild(ClassLoader loader1, ClassLoader loader2) {
/* 235 */     if (loader1 != null && loader2 != null) {
/* 236 */       ClassLoader parent = loader1.getParent();
/* 237 */       while (parent != null && parent != loader2) {
/* 238 */         parent = parent.getParent();
/*     */       }
/*     */       
/* 241 */       return (parent != null);
/*     */     } 
/* 243 */     return (loader1 != null);
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
/*     */   public static Class<?> loadClass(String className) throws ClassNotFoundException {
/* 257 */     if (ignoreTCL) {
/* 258 */       LOGGER.trace("Ignoring TCCL. Trying Class.forName({}).", new Object[] { className });
/* 259 */       return loadClassWithDefaultClassLoader(className);
/*     */     } 
/*     */     try {
/* 262 */       LOGGER.trace("Trying TCCL for class {}.", new Object[] { className });
/*     */       
/* 264 */       return Class.forName(className, true, getTcl());
/* 265 */     } catch (Throwable e) {
/* 266 */       LOGGER.trace("TCCL didn't work for class {}: {}.", new Object[] { className, e.toString() });
/* 267 */       return loadClassWithDefaultClassLoader(className);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static Class<?> loadClassWithDefaultClassLoader(String className) throws ClassNotFoundException {
/* 272 */     return Class.forName(className);
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
/*     */   public static Class<?> initializeClass(String className, ClassLoader loader) throws ClassNotFoundException {
/* 285 */     return Class.forName(className, true, loader);
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
/*     */   public static Class<?> loadSystemClass(String className) throws ClassNotFoundException {
/*     */     try {
/* 298 */       return Class.forName(className, true, ClassLoader.getSystemClassLoader());
/* 299 */     } catch (Throwable t) {
/* 300 */       LOGGER.trace("Couldn't use SystemClassLoader. Trying Class.forName({}).", new Object[] { className, t });
/* 301 */       return Class.forName(className);
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
/*     */   public static Object newInstanceOf(String className) throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
/* 322 */     Class<?> clazz = loadClass(className);
/*     */     try {
/* 324 */       return clazz.getConstructor(new Class[0]).newInstance(new Object[0]);
/* 325 */     } catch (NoSuchMethodException e) {
/*     */ 
/*     */       
/* 328 */       return clazz.newInstance();
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
/*     */   public static <T> T newCheckedInstanceOf(String className, Class<T> clazz) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
/* 352 */     return clazz.cast(newInstanceOf(className));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isClassAvailable(String className) {
/*     */     try {
/* 363 */       Class<?> clazz = loadClass(className);
/* 364 */       return (clazz != null);
/* 365 */     } catch (ClassNotFoundException e) {
/* 366 */       return false;
/* 367 */     } catch (Throwable e) {
/* 368 */       LOGGER.trace("Unknown error checking for existence of class [{}].", new Object[] { className, e });
/* 369 */       return false;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\cor\\util\Loader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */