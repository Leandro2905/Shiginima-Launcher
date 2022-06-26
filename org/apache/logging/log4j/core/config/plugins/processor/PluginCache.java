/*     */ package org.apache.logging.log4j.core.config.plugins.processor;
/*     */ 
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.net.URL;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import org.apache.logging.log4j.core.config.plugins.util.PluginRegistry;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PluginCache
/*     */ {
/*  37 */   private final transient PluginRegistry<PluginEntry> pluginCategories = new PluginRegistry();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ConcurrentMap<String, PluginEntry> getCategory(String category) {
/*  46 */     return this.pluginCategories.getCategory(category);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void writeCache(OutputStream os) throws IOException {
/*  56 */     DataOutputStream out = new DataOutputStream(new BufferedOutputStream(os));
/*     */     try {
/*  58 */       out.writeInt(this.pluginCategories.getCategoryCount());
/*  59 */       for (Map.Entry<String, ConcurrentMap<String, PluginEntry>> category : (Iterable<Map.Entry<String, ConcurrentMap<String, PluginEntry>>>)this.pluginCategories.getCategories()) {
/*  60 */         out.writeUTF(category.getKey());
/*  61 */         Map<String, PluginEntry> m = category.getValue();
/*  62 */         out.writeInt(m.size());
/*  63 */         for (Map.Entry<String, PluginEntry> entry : m.entrySet()) {
/*  64 */           PluginEntry plugin = entry.getValue();
/*  65 */           out.writeUTF(plugin.getKey());
/*  66 */           out.writeUTF(plugin.getClassName());
/*  67 */           out.writeUTF(plugin.getName());
/*  68 */           out.writeBoolean(plugin.isPrintable());
/*  69 */           out.writeBoolean(plugin.isDefer());
/*     */         } 
/*     */       } 
/*     */     } finally {
/*  73 */       out.close();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void loadCacheFiles(Enumeration<URL> resources) throws IOException {
/*  84 */     this.pluginCategories.clear();
/*  85 */     while (resources.hasMoreElements()) {
/*  86 */       URL url = resources.nextElement();
/*  87 */       DataInputStream in = new DataInputStream(new BufferedInputStream(url.openStream()));
/*     */       try {
/*  89 */         int count = in.readInt();
/*  90 */         for (int i = 0; i < count; i++) {
/*  91 */           String category = in.readUTF();
/*  92 */           ConcurrentMap<String, PluginEntry> m = this.pluginCategories.getCategory(category);
/*  93 */           int entries = in.readInt();
/*  94 */           for (int j = 0; j < entries; j++) {
/*  95 */             PluginEntry entry = new PluginEntry();
/*  96 */             entry.setKey(in.readUTF());
/*  97 */             entry.setClassName(in.readUTF());
/*  98 */             entry.setName(in.readUTF());
/*  99 */             entry.setPrintable(in.readBoolean());
/* 100 */             entry.setDefer(in.readBoolean());
/* 101 */             entry.setCategory(category);
/* 102 */             m.putIfAbsent(entry.getKey(), entry);
/*     */           } 
/*     */         } 
/*     */       } finally {
/* 106 */         in.close();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int size() {
/* 117 */     return this.pluginCategories.getCategoryCount();
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\config\plugins\processor\PluginCache.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */