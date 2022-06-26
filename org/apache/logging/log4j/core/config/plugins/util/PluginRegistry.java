/*     */ package org.apache.logging.log4j.core.config.plugins.util;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PluginRegistry<T extends Serializable>
/*     */ {
/*  32 */   private final ConcurrentMap<String, ConcurrentMap<String, T>> categories = new ConcurrentHashMap<String, ConcurrentMap<String, T>>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ConcurrentMap<String, T> getCategory(String category) {
/*  45 */     if (category == null) {
/*  46 */       throw new IllegalArgumentException("Category name cannot be null.");
/*     */     }
/*  48 */     String key = category.toLowerCase();
/*  49 */     this.categories.putIfAbsent(key, new ConcurrentHashMap<String, T>());
/*  50 */     return this.categories.get(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getCategoryCount() {
/*  59 */     return this.categories.size();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/*  69 */     return this.categories.isEmpty();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() {
/*  76 */     this.categories.clear();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasCategory(String category) {
/*  87 */     if (category == null) {
/*  88 */       throw new IllegalArgumentException("Category name cannot be null.");
/*     */     }
/*  90 */     String key = category.toLowerCase();
/*  91 */     return (this.categories.containsKey(key) && !((ConcurrentMap)this.categories.get(key)).isEmpty());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<Map.Entry<String, ConcurrentMap<String, T>>> getCategories() {
/* 100 */     return this.categories.entrySet();
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\config\plugin\\util\PluginRegistry.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */