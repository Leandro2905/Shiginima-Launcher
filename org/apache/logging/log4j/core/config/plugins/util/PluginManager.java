/*     */ package org.apache.logging.log4j.core.config.plugins.util;
/*     */ 
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.URI;
/*     */ import java.net.URL;
/*     */ import java.text.DecimalFormat;
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import java.util.concurrent.CopyOnWriteArrayList;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginAliases;
/*     */ import org.apache.logging.log4j.core.util.ClassLoaderResourceLoader;
/*     */ import org.apache.logging.log4j.core.util.Closer;
/*     */ import org.apache.logging.log4j.core.util.Loader;
/*     */ import org.apache.logging.log4j.core.util.ResourceLoader;
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
/*     */ public class PluginManager
/*     */ {
/*  48 */   private static final PluginRegistry<PluginType<?>> REGISTRY = new PluginRegistry<PluginType<?>>();
/*  49 */   private static final CopyOnWriteArrayList<String> PACKAGES = new CopyOnWriteArrayList<String>();
/*     */   
/*     */   private static final String LOG4J_PACKAGES = "org.apache.logging.log4j.core";
/*  52 */   private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*     */   
/*  54 */   private Map<String, PluginType<?>> plugins = new HashMap<String, PluginType<?>>();
/*     */ 
/*     */ 
/*     */   
/*     */   private final String category;
/*     */ 
/*     */ 
/*     */   
/*     */   public PluginManager(String category) {
/*  63 */     this.category = category;
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
/*     */   @Deprecated
/*     */   public static void main(String[] args) {
/*  77 */     System.err.println("WARNING: this tool is superseded by the annotation processor included in log4j-core.");
/*  78 */     System.exit(-1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void addPackage(String p) {
/*  87 */     if (PACKAGES.addIfAbsent(p))
/*     */     {
/*  89 */       REGISTRY.clear();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PluginType<?> getPluginType(String name) {
/* 100 */     return this.plugins.get(name.toLowerCase());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, PluginType<?>> getPlugins() {
/* 109 */     return this.plugins;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void collectPlugins() {
/* 116 */     collectPlugins(true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void collectPlugins(boolean preLoad) {
/* 126 */     if (REGISTRY.hasCategory(this.category)) {
/* 127 */       this.plugins = REGISTRY.getCategory(this.category);
/* 128 */       preLoad = false;
/*     */     } 
/* 130 */     long start = System.nanoTime();
/* 131 */     if (preLoad) {
/* 132 */       ClassLoaderResourceLoader classLoaderResourceLoader = new ClassLoaderResourceLoader(Loader.getClassLoader());
/* 133 */       loadPlugins((ResourceLoader)classLoaderResourceLoader);
/*     */     } 
/* 135 */     this.plugins = REGISTRY.getCategory(this.category);
/* 136 */     loadFromPackages(start, preLoad);
/*     */     
/* 138 */     long elapsed = System.nanoTime() - start;
/* 139 */     reportPluginLoadDuration(preLoad, elapsed);
/*     */   }
/*     */ 
/*     */   
/*     */   private void loadFromPackages(long start, boolean preLoad) {
/* 144 */     if ((this.plugins == null || this.plugins.size() == 0) && 
/* 145 */       !PACKAGES.contains("org.apache.logging.log4j.core")) {
/* 146 */       PACKAGES.add("org.apache.logging.log4j.core");
/*     */     }
/*     */     
/* 149 */     ResolverUtil resolver = new ResolverUtil();
/* 150 */     ClassLoader classLoader = Loader.getClassLoader();
/* 151 */     if (classLoader != null) {
/* 152 */       resolver.setClassLoader(classLoader);
/*     */     }
/* 154 */     Class<?> cls = null;
/* 155 */     ResolverUtil.Test test = new PluginTest(cls);
/* 156 */     for (String pkg : PACKAGES) {
/* 157 */       resolver.findInPackage(test, pkg);
/*     */     }
/* 159 */     for (Class<?> clazz : resolver.getClasses()) {
/* 160 */       Plugin plugin = clazz.<Plugin>getAnnotation(Plugin.class);
/* 161 */       String pluginCategory = plugin.category();
/* 162 */       Map<String, PluginType<?>> map = REGISTRY.getCategory(pluginCategory);
/* 163 */       String type = plugin.elementType().equals("") ? plugin.name() : plugin.elementType();
/* 164 */       PluginType<?> pluginType = new PluginType(clazz, type, plugin.printObject(), plugin.deferChildren());
/* 165 */       map.put(plugin.name().toLowerCase(), pluginType);
/* 166 */       PluginAliases pluginAliases = clazz.<PluginAliases>getAnnotation(PluginAliases.class);
/* 167 */       if (pluginAliases != null) {
/* 168 */         for (String alias : pluginAliases.value()) {
/* 169 */           type = plugin.elementType().equals("") ? alias : plugin.elementType();
/* 170 */           pluginType = new PluginType(clazz, type, plugin.printObject(), plugin.deferChildren());
/* 171 */           map.put(alias.trim().toLowerCase(), pluginType);
/*     */         } 
/*     */       }
/*     */     } 
/* 175 */     this.plugins = REGISTRY.getCategory(this.category);
/*     */   }
/*     */   
/*     */   private void reportPluginLoadDuration(boolean preLoad, long elapsed) {
/* 179 */     StringBuilder sb = new StringBuilder("Generated plugins in ");
/* 180 */     DecimalFormat numFormat = new DecimalFormat("#0.000000");
/* 181 */     double seconds = elapsed / 1.0E9D;
/* 182 */     sb.append(numFormat.format(seconds)).append(" seconds, packages: ");
/* 183 */     sb.append(PACKAGES);
/* 184 */     sb.append(", preload: ");
/* 185 */     sb.append(preLoad);
/* 186 */     sb.append(".");
/* 187 */     LOGGER.debug(sb.toString());
/*     */   }
/*     */   
/*     */   public static void loadPlugins(ResourceLoader loader) {
/* 191 */     PluginRegistry<PluginType<?>> registry = decode(loader);
/* 192 */     if (registry != null) {
/* 193 */       for (Map.Entry<String, ConcurrentMap<String, PluginType<?>>> entry : registry.getCategories()) {
/* 194 */         REGISTRY.getCategory(entry.getKey()).putAll(entry.getValue());
/*     */       }
/*     */     } else {
/* 197 */       LOGGER.info("Plugin preloads not available from class loader {}", new Object[] { loader });
/*     */     } 
/*     */   }
/*     */   
/*     */   private static PluginRegistry<PluginType<?>> decode(ResourceLoader loader) {
/*     */     Enumeration<URL> resources;
/*     */     try {
/* 204 */       resources = loader.getResources("META-INF/org/apache/logging/log4j/core/config/plugins/Log4j2Plugins.dat");
/* 205 */       if (resources == null) {
/* 206 */         return null;
/*     */       }
/* 208 */     } catch (IOException ioe) {
/* 209 */       LOGGER.warn("Unable to preload plugins", ioe);
/* 210 */       return null;
/*     */     } 
/* 212 */     PluginRegistry<PluginType<?>> map = new PluginRegistry<PluginType<?>>();
/* 213 */     while (resources.hasMoreElements()) {
/* 214 */       InputStream is; URL url = resources.nextElement();
/* 215 */       LOGGER.debug("Found Plugin Map at {}", new Object[] { url.toExternalForm() });
/*     */       
/*     */       try {
/* 218 */         is = url.openStream();
/* 219 */       } catch (IOException e) {
/* 220 */         LOGGER.warn("Unable to open {}", new Object[] { url.toExternalForm(), e });
/*     */         continue;
/*     */       } 
/* 223 */       DataInputStream dis = new DataInputStream(new BufferedInputStream(is));
/*     */       try {
/* 225 */         int count = dis.readInt();
/* 226 */         for (int j = 0; j < count; j++) {
/* 227 */           String category = dis.readUTF();
/* 228 */           int entries = dis.readInt();
/* 229 */           Map<String, PluginType<?>> types = map.getCategory(category);
/* 230 */           for (int i = 0; i < entries; i++) {
/* 231 */             String key = dis.readUTF();
/* 232 */             String className = dis.readUTF();
/* 233 */             String name = dis.readUTF();
/* 234 */             boolean printable = dis.readBoolean();
/* 235 */             boolean defer = dis.readBoolean();
/*     */             try {
/* 237 */               Class<?> clazz = loader.loadClass(className);
/*     */               
/* 239 */               PluginType<?> pluginType = new PluginType(clazz, name, printable, defer);
/* 240 */               types.put(key, pluginType);
/* 241 */             } catch (ClassNotFoundException e) {
/* 242 */               LOGGER.info("Plugin [{}] could not be loaded due to missing classes.", new Object[] { className, e });
/* 243 */             } catch (VerifyError e) {
/* 244 */               LOGGER.info("Plugin [{}] could not be loaded due to verification error.", new Object[] { className, e });
/*     */             } 
/*     */           } 
/*     */         } 
/* 248 */       } catch (IOException ex) {
/* 249 */         LOGGER.warn("Unable to preload plugins", ex);
/*     */       } finally {
/* 251 */         Closer.closeSilently(dis);
/*     */       } 
/*     */     } 
/* 254 */     return map.isEmpty() ? null : map;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class PluginTest
/*     */     implements ResolverUtil.Test
/*     */   {
/*     */     private final Class<?> isA;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public PluginTest(Class<?> isA) {
/* 269 */       this.isA = isA;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean matches(Class<?> type) {
/* 279 */       return (type != null && type.isAnnotationPresent((Class)Plugin.class) && (this.isA == null || this.isA.isAssignableFrom(type)));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public String toString() {
/* 285 */       StringBuilder msg = new StringBuilder("annotated with @" + Plugin.class.getSimpleName());
/* 286 */       if (this.isA != null) {
/* 287 */         msg.append(" is assignable to " + this.isA.getSimpleName());
/*     */       }
/* 289 */       return msg.toString();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean matches(URI resource) {
/* 294 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean doesMatchClass() {
/* 299 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean doesMatchResource() {
/* 304 */       return false;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\config\plugin\\util\PluginManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */