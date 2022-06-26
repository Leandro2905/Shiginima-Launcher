/*     */ package org.apache.logging.log4j.core.selector;
/*     */ 
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.net.URI;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import org.apache.logging.log4j.core.LoggerContext;
/*     */ import org.apache.logging.log4j.core.impl.ContextAnchor;
/*     */ import org.apache.logging.log4j.core.impl.ReflectiveCallerClassUtility;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ClassLoaderContextSelector
/*     */   implements ContextSelector
/*     */ {
/*  49 */   private static final AtomicReference<LoggerContext> CONTEXT = new AtomicReference<LoggerContext>();
/*     */   
/*     */   private static final PrivateSecurityManager SECURITY_MANAGER;
/*     */   
/*  53 */   private static final StatusLogger LOGGER = StatusLogger.getLogger();
/*     */   
/*  55 */   private static final ConcurrentMap<String, AtomicReference<WeakReference<LoggerContext>>> CONTEXT_MAP = new ConcurrentHashMap<String, AtomicReference<WeakReference<LoggerContext>>>();
/*     */ 
/*     */   
/*     */   static {
/*  59 */     if (ReflectiveCallerClassUtility.isSupported()) {
/*  60 */       SECURITY_MANAGER = null;
/*     */     } else {
/*     */       PrivateSecurityManager privateSecurityManager;
/*     */       try {
/*  64 */         privateSecurityManager = new PrivateSecurityManager();
/*  65 */         if (privateSecurityManager.getCaller(ClassLoaderContextSelector.class.getName()) == null) {
/*     */           
/*  67 */           privateSecurityManager = null;
/*  68 */           LOGGER.error("Unable to obtain call stack from security manager.");
/*     */         } 
/*  70 */       } catch (Exception e) {
/*  71 */         privateSecurityManager = null;
/*  72 */         LOGGER.debug("Unable to install security manager", e);
/*     */       } 
/*  74 */       SECURITY_MANAGER = privateSecurityManager;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public LoggerContext getContext(String fqcn, ClassLoader loader, boolean currentContext) {
/*  80 */     return getContext(fqcn, loader, currentContext, null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public LoggerContext getContext(String fqcn, ClassLoader loader, boolean currentContext, URI configLocation) {
/*  86 */     if (currentContext) {
/*  87 */       LoggerContext ctx = ContextAnchor.THREAD_CONTEXT.get();
/*  88 */       if (ctx != null) {
/*  89 */         return ctx;
/*     */       }
/*  91 */       return getDefault();
/*  92 */     }  if (loader != null) {
/*  93 */       return locateContext(loader, configLocation);
/*     */     }
/*  95 */     if (ReflectiveCallerClassUtility.isSupported()) {
/*     */       try {
/*  97 */         Class<?> clazz = Class.class;
/*  98 */         boolean bool = false;
/*  99 */         for (int index = 2; clazz != null; index++) {
/* 100 */           clazz = ReflectiveCallerClassUtility.getCaller(index);
/* 101 */           if (clazz == null) {
/*     */             break;
/*     */           }
/* 104 */           if (clazz.getName().equals(fqcn)) {
/* 105 */             bool = true;
/*     */           
/*     */           }
/* 108 */           else if (bool) {
/*     */             break;
/*     */           } 
/*     */         } 
/* 112 */         if (clazz != null) {
/* 113 */           return locateContext(clazz.getClassLoader(), configLocation);
/*     */         }
/* 115 */       } catch (Exception ex) {}
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 120 */     if (SECURITY_MANAGER != null) {
/* 121 */       Class<?> clazz = SECURITY_MANAGER.getCaller(fqcn);
/* 122 */       if (clazz != null) {
/* 123 */         ClassLoader ldr = (clazz.getClassLoader() != null) ? clazz.getClassLoader() : ClassLoader.getSystemClassLoader();
/*     */         
/* 125 */         return locateContext(ldr, configLocation);
/*     */       } 
/*     */     } 
/*     */     
/* 129 */     Throwable t = new Throwable();
/* 130 */     boolean next = false;
/* 131 */     String name = null;
/* 132 */     for (StackTraceElement element : t.getStackTrace()) {
/* 133 */       if (element.getClassName().equals(fqcn)) {
/* 134 */         next = true;
/*     */       
/*     */       }
/* 137 */       else if (next) {
/* 138 */         name = element.getClassName();
/*     */         break;
/*     */       } 
/*     */     } 
/* 142 */     if (name != null) {
/*     */       try {
/* 144 */         return locateContext(Loader.loadClass(name).getClassLoader(), configLocation);
/* 145 */       } catch (ClassNotFoundException ignore) {}
/*     */     }
/*     */ 
/*     */     
/* 149 */     LoggerContext lc = ContextAnchor.THREAD_CONTEXT.get();
/* 150 */     if (lc != null) {
/* 151 */       return lc;
/*     */     }
/* 153 */     return getDefault();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeContext(LoggerContext context) {
/* 159 */     for (Map.Entry<String, AtomicReference<WeakReference<LoggerContext>>> entry : CONTEXT_MAP.entrySet()) {
/* 160 */       LoggerContext ctx = ((WeakReference<LoggerContext>)((AtomicReference<WeakReference<LoggerContext>>)entry.getValue()).get()).get();
/* 161 */       if (ctx == context) {
/* 162 */         CONTEXT_MAP.remove(entry.getKey());
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public List<LoggerContext> getLoggerContexts() {
/* 169 */     List<LoggerContext> list = new ArrayList<LoggerContext>();
/* 170 */     Collection<AtomicReference<WeakReference<LoggerContext>>> coll = CONTEXT_MAP.values();
/* 171 */     for (AtomicReference<WeakReference<LoggerContext>> ref : coll) {
/* 172 */       LoggerContext ctx = ((WeakReference<LoggerContext>)ref.get()).get();
/* 173 */       if (ctx != null) {
/* 174 */         list.add(ctx);
/*     */       }
/*     */     } 
/* 177 */     return Collections.unmodifiableList(list);
/*     */   }
/*     */ 
/*     */   
/*     */   private LoggerContext locateContext(ClassLoader loaderOrNull, URI configLocation) {
/* 182 */     ClassLoader loader = (loaderOrNull != null) ? loaderOrNull : ClassLoader.getSystemClassLoader();
/* 183 */     String name = loader.toString();
/* 184 */     AtomicReference<WeakReference<LoggerContext>> ref = CONTEXT_MAP.get(name);
/* 185 */     if (ref == null) {
/* 186 */       if (configLocation == null) {
/* 187 */         ClassLoader parent = loader.getParent();
/* 188 */         while (parent != null) {
/*     */           
/* 190 */           ref = CONTEXT_MAP.get(parent.toString());
/* 191 */           if (ref != null) {
/* 192 */             WeakReference<LoggerContext> weakReference = ref.get();
/* 193 */             LoggerContext loggerContext1 = weakReference.get();
/* 194 */             if (loggerContext1 != null) {
/* 195 */               return loggerContext1;
/*     */             }
/*     */           } 
/* 198 */           parent = parent.getParent();
/*     */         } 
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 218 */       LoggerContext loggerContext = new LoggerContext(name, null, configLocation);
/* 219 */       AtomicReference<WeakReference<LoggerContext>> atomicReference = new AtomicReference<WeakReference<LoggerContext>>();
/*     */       
/* 221 */       atomicReference.set(new WeakReference<LoggerContext>(loggerContext));
/* 222 */       CONTEXT_MAP.putIfAbsent(name, atomicReference);
/* 223 */       loggerContext = ((WeakReference<LoggerContext>)((AtomicReference<WeakReference<LoggerContext>>)CONTEXT_MAP.get(name)).get()).get();
/* 224 */       return loggerContext;
/*     */     } 
/* 226 */     WeakReference<LoggerContext> r = ref.get();
/* 227 */     LoggerContext ctx = r.get();
/* 228 */     if (ctx != null) {
/* 229 */       if (ctx.getConfigLocation() == null && configLocation != null) {
/* 230 */         LOGGER.debug("Setting configuration to {}", new Object[] { configLocation });
/* 231 */         ctx.setConfigLocation(configLocation);
/* 232 */       } else if (ctx.getConfigLocation() != null && configLocation != null && !ctx.getConfigLocation().equals(configLocation)) {
/*     */         
/* 234 */         LOGGER.warn("locateContext called with URI {}. Existing LoggerContext has URI {}", new Object[] { configLocation, ctx.getConfigLocation() });
/*     */       } 
/*     */       
/* 237 */       return ctx;
/*     */     } 
/* 239 */     ctx = new LoggerContext(name, null, configLocation);
/* 240 */     ref.compareAndSet(r, new WeakReference<LoggerContext>(ctx));
/* 241 */     return ctx;
/*     */   }
/*     */   
/*     */   private LoggerContext getDefault() {
/* 245 */     LoggerContext ctx = CONTEXT.get();
/* 246 */     if (ctx != null) {
/* 247 */       return ctx;
/*     */     }
/* 249 */     CONTEXT.compareAndSet(null, new LoggerContext("Default"));
/* 250 */     return CONTEXT.get();
/*     */   }
/*     */   
/*     */   private static class PrivateSecurityManager
/*     */     extends SecurityManager
/*     */   {
/*     */     private PrivateSecurityManager() {}
/*     */     
/*     */     public Class<?> getCaller(String fqcn) {
/* 259 */       Class<?>[] classes = getClassContext();
/* 260 */       boolean next = false;
/* 261 */       for (Class<?> clazz : classes) {
/* 262 */         if (clazz.getName().equals(fqcn)) {
/* 263 */           next = true;
/*     */         
/*     */         }
/* 266 */         else if (next) {
/* 267 */           return clazz;
/*     */         } 
/*     */       } 
/* 270 */       return null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\selector\ClassLoaderContextSelector.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */