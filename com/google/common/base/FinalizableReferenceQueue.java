/*     */ package com.google.common.base;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import java.io.Closeable;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.lang.ref.PhantomReference;
/*     */ import java.lang.ref.Reference;
/*     */ import java.lang.ref.ReferenceQueue;
/*     */ import java.lang.reflect.Method;
/*     */ import java.net.URL;
/*     */ import java.net.URLClassLoader;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.annotation.Nullable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FinalizableReferenceQueue
/*     */   implements Closeable
/*     */ {
/* 131 */   private static final Logger logger = Logger.getLogger(FinalizableReferenceQueue.class.getName());
/*     */   
/*     */   private static final String FINALIZER_CLASS_NAME = "com.google.common.base.internal.Finalizer";
/*     */   
/*     */   private static final Method startFinalizer;
/*     */   
/*     */   static {
/* 138 */     Class<?> finalizer = loadFinalizer(new FinalizerLoader[] { new SystemLoader(), new DecoupledLoader(), new DirectLoader() });
/*     */     
/* 140 */     startFinalizer = getStartFinalizer(finalizer);
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
/* 160 */   final ReferenceQueue<Object> queue = new ReferenceQueue();
/* 161 */   final PhantomReference<Object> frqRef = new PhantomReference(this, this.queue); public FinalizableReferenceQueue() {
/* 162 */     boolean threadStarted = false;
/*     */     try {
/* 164 */       startFinalizer.invoke(null, new Object[] { FinalizableReference.class, this.queue, this.frqRef });
/* 165 */       threadStarted = true;
/* 166 */     } catch (IllegalAccessException impossible) {
/* 167 */       throw new AssertionError(impossible);
/* 168 */     } catch (Throwable t) {
/* 169 */       logger.log(Level.INFO, "Failed to start reference finalizer thread. Reference cleanup will only occur when new references are created.", t);
/*     */     } 
/*     */ 
/*     */     
/* 173 */     this.threadStarted = threadStarted;
/*     */   }
/*     */   final boolean threadStarted;
/*     */   
/*     */   public void close() {
/* 178 */     this.frqRef.enqueue();
/* 179 */     cleanUp();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void cleanUp() {
/* 188 */     if (this.threadStarted) {
/*     */       return;
/*     */     }
/*     */     
/*     */     Reference<?> reference;
/* 193 */     while ((reference = this.queue.poll()) != null) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 198 */       reference.clear();
/*     */       try {
/* 200 */         ((FinalizableReference)reference).finalizeReferent();
/* 201 */       } catch (Throwable t) {
/* 202 */         logger.log(Level.SEVERE, "Error cleaning up after reference.", t);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Class<?> loadFinalizer(FinalizerLoader... loaders) {
/* 213 */     for (FinalizerLoader loader : loaders) {
/* 214 */       Class<?> finalizer = loader.loadFinalizer();
/* 215 */       if (finalizer != null) {
/* 216 */         return finalizer;
/*     */       }
/*     */     } 
/*     */     
/* 220 */     throw new AssertionError();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static interface FinalizerLoader
/*     */   {
/*     */     @Nullable
/*     */     Class<?> loadFinalizer();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static class SystemLoader
/*     */     implements FinalizerLoader
/*     */   {
/*     */     @VisibleForTesting
/*     */     static boolean disabled;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Class<?> loadFinalizer() {
/*     */       ClassLoader systemLoader;
/* 249 */       if (disabled) {
/* 250 */         return null;
/*     */       }
/*     */       
/*     */       try {
/* 254 */         systemLoader = ClassLoader.getSystemClassLoader();
/* 255 */       } catch (SecurityException e) {
/* 256 */         FinalizableReferenceQueue.logger.info("Not allowed to access system class loader.");
/* 257 */         return null;
/*     */       } 
/* 259 */       if (systemLoader != null) {
/*     */         try {
/* 261 */           return systemLoader.loadClass("com.google.common.base.internal.Finalizer");
/* 262 */         } catch (ClassNotFoundException e) {
/*     */           
/* 264 */           return null;
/*     */         } 
/*     */       }
/* 267 */       return null;
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
/*     */   static class DecoupledLoader
/*     */     implements FinalizerLoader
/*     */   {
/*     */     private static final String LOADING_ERROR = "Could not load Finalizer in its own class loader.Loading Finalizer in the current class loader instead. As a result, you will not be ableto garbage collect this class loader. To support reclaiming this class loader, eitherresolve the underlying issue, or move Google Collections to your system class path.";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Class<?> loadFinalizer() {
/*     */       try {
/* 295 */         ClassLoader finalizerLoader = newLoader(getBaseUrl());
/* 296 */         return finalizerLoader.loadClass("com.google.common.base.internal.Finalizer");
/* 297 */       } catch (Exception e) {
/* 298 */         FinalizableReferenceQueue.logger.log(Level.WARNING, "Could not load Finalizer in its own class loader.Loading Finalizer in the current class loader instead. As a result, you will not be ableto garbage collect this class loader. To support reclaiming this class loader, eitherresolve the underlying issue, or move Google Collections to your system class path.", e);
/* 299 */         return null;
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     URL getBaseUrl() throws IOException {
/* 308 */       String finalizerPath = String.valueOf("com.google.common.base.internal.Finalizer".replace('.', '/')).concat(".class");
/* 309 */       URL finalizerUrl = getClass().getClassLoader().getResource(finalizerPath);
/* 310 */       if (finalizerUrl == null) {
/* 311 */         throw new FileNotFoundException(finalizerPath);
/*     */       }
/*     */ 
/*     */       
/* 315 */       String urlString = finalizerUrl.toString();
/* 316 */       if (!urlString.endsWith(finalizerPath)) {
/* 317 */         String.valueOf(urlString); throw new IOException((String.valueOf(urlString).length() != 0) ? "Unsupported path style: ".concat(String.valueOf(urlString)) : new String("Unsupported path style: "));
/*     */       } 
/* 319 */       urlString = urlString.substring(0, urlString.length() - finalizerPath.length());
/* 320 */       return new URL(finalizerUrl, urlString);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     URLClassLoader newLoader(URL base) {
/* 328 */       return new URLClassLoader(new URL[] { base }, null);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static class DirectLoader
/*     */     implements FinalizerLoader
/*     */   {
/*     */     public Class<?> loadFinalizer() {
/*     */       try {
/* 340 */         return Class.forName("com.google.common.base.internal.Finalizer");
/* 341 */       } catch (ClassNotFoundException e) {
/* 342 */         throw new AssertionError(e);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static Method getStartFinalizer(Class<?> finalizer) {
/*     */     try {
/* 352 */       return finalizer.getMethod("startFinalizer", new Class[] { Class.class, ReferenceQueue.class, PhantomReference.class });
/*     */ 
/*     */ 
/*     */     
/*     */     }
/* 357 */     catch (NoSuchMethodException e) {
/* 358 */       throw new AssertionError(e);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\base\FinalizableReferenceQueue.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */