/*     */ package com.google.common.base.internal;
/*     */ 
/*     */ import java.lang.ref.PhantomReference;
/*     */ import java.lang.ref.Reference;
/*     */ import java.lang.ref.ReferenceQueue;
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Finalizer
/*     */   implements Runnable
/*     */ {
/*  51 */   private static final Logger logger = Logger.getLogger(Finalizer.class.getName());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String FINALIZABLE_REFERENCE = "com.google.common.base.FinalizableReference";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final WeakReference<Class<?>> finalizableReferenceClassReference;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final PhantomReference<Object> frqReference;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final ReferenceQueue<Object> queue;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void startFinalizer(Class<?> finalizableReferenceClass, ReferenceQueue<Object> queue, PhantomReference<Object> frqReference) {
/*  80 */     if (!finalizableReferenceClass.getName().equals("com.google.common.base.FinalizableReference")) {
/*  81 */       throw new IllegalArgumentException("Expected com.google.common.base.FinalizableReference.");
/*     */     }
/*     */ 
/*     */     
/*  85 */     Finalizer finalizer = new Finalizer(finalizableReferenceClass, queue, frqReference);
/*  86 */     Thread thread = new Thread(finalizer);
/*  87 */     thread.setName(Finalizer.class.getName());
/*  88 */     thread.setDaemon(true);
/*     */     
/*     */     try {
/*  91 */       if (inheritableThreadLocals != null) {
/*  92 */         inheritableThreadLocals.set(thread, (Object)null);
/*     */       }
/*  94 */     } catch (Throwable t) {
/*  95 */       logger.log(Level.INFO, "Failed to clear thread local values inherited by reference finalizer thread.", t);
/*     */     } 
/*     */ 
/*     */     
/*  99 */     thread.start();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 106 */   private static final Field inheritableThreadLocals = getInheritableThreadLocalsField();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Finalizer(Class<?> finalizableReferenceClass, ReferenceQueue<Object> queue, PhantomReference<Object> frqReference) {
/* 114 */     this.queue = queue;
/*     */     
/* 116 */     this.finalizableReferenceClassReference = new WeakReference<Class<?>>(finalizableReferenceClass);
/*     */ 
/*     */ 
/*     */     
/* 120 */     this.frqReference = frqReference;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void run() {
/*     */     while (true) {
/*     */       try {
/*     */         do {
/*     */         
/* 131 */         } while (cleanUp(this.queue.remove()));
/*     */         
/*     */         break;
/* 134 */       } catch (InterruptedException e) {}
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean cleanUp(Reference<?> reference) {
/* 144 */     Method finalizeReferentMethod = getFinalizeReferentMethod();
/* 145 */     if (finalizeReferentMethod == null) {
/* 146 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     while (true) {
/* 153 */       reference.clear();
/*     */       
/* 155 */       if (reference == this.frqReference)
/*     */       {
/*     */ 
/*     */ 
/*     */         
/* 160 */         return false;
/*     */       }
/*     */       
/*     */       try {
/* 164 */         finalizeReferentMethod.invoke(reference, new Object[0]);
/* 165 */       } catch (Throwable t) {
/* 166 */         logger.log(Level.SEVERE, "Error cleaning up after reference.", t);
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 173 */       if ((reference = this.queue.poll()) == null) {
/* 174 */         return true;
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private Method getFinalizeReferentMethod() {
/* 181 */     Class<?> finalizableReferenceClass = this.finalizableReferenceClassReference.get();
/*     */     
/* 183 */     if (finalizableReferenceClass == null)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 192 */       return null;
/*     */     }
/*     */     try {
/* 195 */       return finalizableReferenceClass.getMethod("finalizeReferent", new Class[0]);
/* 196 */     } catch (NoSuchMethodException e) {
/* 197 */       throw new AssertionError(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static Field getInheritableThreadLocalsField() {
/*     */     try {
/* 203 */       Field inheritableThreadLocals = Thread.class.getDeclaredField("inheritableThreadLocals");
/*     */       
/* 205 */       inheritableThreadLocals.setAccessible(true);
/* 206 */       return inheritableThreadLocals;
/* 207 */     } catch (Throwable t) {
/* 208 */       logger.log(Level.INFO, "Couldn't access Thread.inheritableThreadLocals. Reference finalizer threads will inherit thread local values.");
/*     */ 
/*     */       
/* 211 */       return null;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\base\internal\Finalizer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */