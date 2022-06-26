/*     */ package org.apache.commons.lang3.concurrent;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MultiBackgroundInitializer
/*     */   extends BackgroundInitializer<MultiBackgroundInitializer.MultiBackgroundInitializerResults>
/*     */ {
/* 101 */   private final Map<String, BackgroundInitializer<?>> childInitializers = new HashMap<String, BackgroundInitializer<?>>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MultiBackgroundInitializer(ExecutorService exec) {
/* 119 */     super(exec);
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
/*     */   public void addInitializer(String name, BackgroundInitializer<?> init) {
/* 135 */     if (name == null) {
/* 136 */       throw new IllegalArgumentException("Name of child initializer must not be null!");
/*     */     }
/*     */     
/* 139 */     if (init == null) {
/* 140 */       throw new IllegalArgumentException("Child initializer must not be null!");
/*     */     }
/*     */ 
/*     */     
/* 144 */     synchronized (this) {
/* 145 */       if (isStarted()) {
/* 146 */         throw new IllegalStateException("addInitializer() must not be called after start()!");
/*     */       }
/*     */       
/* 149 */       this.childInitializers.put(name, init);
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
/*     */   protected int getTaskCount() {
/* 165 */     int result = 1;
/*     */     
/* 167 */     for (BackgroundInitializer<?> bi : this.childInitializers.values()) {
/* 168 */       result += bi.getTaskCount();
/*     */     }
/*     */     
/* 171 */     return result;
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
/*     */   protected MultiBackgroundInitializerResults initialize() throws Exception {
/*     */     Map<String, BackgroundInitializer<?>> inits;
/* 187 */     synchronized (this) {
/*     */       
/* 189 */       inits = new HashMap<String, BackgroundInitializer<?>>(this.childInitializers);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 194 */     ExecutorService exec = getActiveExecutor();
/* 195 */     for (BackgroundInitializer<?> bi : inits.values()) {
/* 196 */       if (bi.getExternalExecutor() == null)
/*     */       {
/* 198 */         bi.setExternalExecutor(exec);
/*     */       }
/* 200 */       bi.start();
/*     */     } 
/*     */ 
/*     */     
/* 204 */     Map<String, Object> results = new HashMap<String, Object>();
/* 205 */     Map<String, ConcurrentException> excepts = new HashMap<String, ConcurrentException>();
/* 206 */     for (Map.Entry<String, BackgroundInitializer<?>> e : inits.entrySet()) {
/*     */       try {
/* 208 */         results.put(e.getKey(), ((BackgroundInitializer)e.getValue()).get());
/* 209 */       } catch (ConcurrentException cex) {
/* 210 */         excepts.put(e.getKey(), cex);
/*     */       } 
/*     */     } 
/*     */     
/* 214 */     return new MultiBackgroundInitializerResults(inits, results, excepts);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MultiBackgroundInitializer() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class MultiBackgroundInitializerResults
/*     */   {
/*     */     private final Map<String, BackgroundInitializer<?>> initializers;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final Map<String, Object> resultObjects;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final Map<String, ConcurrentException> exceptions;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private MultiBackgroundInitializerResults(Map<String, BackgroundInitializer<?>> inits, Map<String, Object> results, Map<String, ConcurrentException> excepts) {
/* 250 */       this.initializers = inits;
/* 251 */       this.resultObjects = results;
/* 252 */       this.exceptions = excepts;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public BackgroundInitializer<?> getInitializer(String name) {
/* 264 */       return checkName(name);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Object getResultObject(String name) {
/* 280 */       checkName(name);
/* 281 */       return this.resultObjects.get(name);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean isException(String name) {
/* 293 */       checkName(name);
/* 294 */       return this.exceptions.containsKey(name);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ConcurrentException getException(String name) {
/* 308 */       checkName(name);
/* 309 */       return this.exceptions.get(name);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Set<String> initializerNames() {
/* 320 */       return Collections.unmodifiableSet(this.initializers.keySet());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean isSuccessful() {
/* 330 */       return this.exceptions.isEmpty();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private BackgroundInitializer<?> checkName(String name) {
/* 343 */       BackgroundInitializer<?> init = this.initializers.get(name);
/* 344 */       if (init == null) {
/* 345 */         throw new NoSuchElementException("No child initializer with name " + name);
/*     */       }
/*     */ 
/*     */       
/* 349 */       return init;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\commons\lang3\concurrent\MultiBackgroundInitializer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */