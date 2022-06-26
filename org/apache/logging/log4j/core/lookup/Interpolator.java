/*     */ package org.apache.logging.log4j.core.lookup;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.config.plugins.util.PluginManager;
/*     */ import org.apache.logging.log4j.core.config.plugins.util.PluginType;
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
/*     */ public class Interpolator
/*     */   implements StrLookup
/*     */ {
/*  34 */   private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*     */ 
/*     */   
/*     */   private static final char PREFIX_SEPARATOR = ':';
/*     */   
/*  39 */   private final Map<String, StrLookup> lookups = new HashMap<String, StrLookup>();
/*     */   
/*     */   private final StrLookup defaultLookup;
/*     */   
/*     */   public Interpolator(StrLookup defaultLookup) {
/*  44 */     this.defaultLookup = (defaultLookup == null) ? new MapLookup(new HashMap<String, String>()) : defaultLookup;
/*  45 */     PluginManager manager = new PluginManager("Lookup");
/*  46 */     manager.collectPlugins();
/*  47 */     Map<String, PluginType<?>> plugins = manager.getPlugins();
/*     */     
/*  49 */     for (Map.Entry<String, PluginType<?>> entry : plugins.entrySet()) {
/*     */       
/*  51 */       Class<? extends StrLookup> clazz = ((PluginType)entry.getValue()).getPluginClass();
/*     */       try {
/*  53 */         this.lookups.put(entry.getKey(), clazz.getConstructor(new Class[0]).newInstance(new Object[0]));
/*  54 */       } catch (Exception ex) {
/*  55 */         LOGGER.error("Unable to create Lookup for {}", new Object[] { entry.getKey(), ex });
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Interpolator() {
/*  64 */     this((Map<String, String>)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Interpolator(Map<String, String> properties) {
/*  71 */     this.defaultLookup = new MapLookup((properties == null) ? new HashMap<String, String>() : properties);
/*     */     
/*  73 */     this.lookups.put("sys", new SystemPropertiesLookup());
/*  74 */     this.lookups.put("env", new EnvironmentLookup());
/*     */     
/*     */     try {
/*  77 */       this.lookups.put("jndi", (StrLookup)Class.forName("org.apache.logging.log4j.core.lookup.JndiLookup").newInstance());
/*     */     }
/*  79 */     catch (Throwable e) {
/*     */       
/*  81 */       LOGGER.warn("JNDI lookup class is not available because this JRE does not support JNDI. JNDI string lookups will not be available, continuing configuration.", e);
/*     */     } 
/*     */ 
/*     */     
/*  85 */     this.lookups.put("date", new DateLookup());
/*  86 */     this.lookups.put("ctx", new ContextMapLookup());
/*  87 */     if (Loader.isClassAvailable("javax.servlet.ServletContext")) {
/*     */       try {
/*  89 */         this.lookups.put("web", Loader.newCheckedInstanceOf("org.apache.logging.log4j.web.WebLookup", StrLookup.class));
/*     */       }
/*  91 */       catch (Exception ignored) {
/*  92 */         LOGGER.info("Log4j appears to be running in a Servlet environment, but there's no log4j-web module available. If you want better web container support, please add the log4j-web JAR to your web archive or server lib directory.");
/*     */       }
/*     */     
/*     */     } else {
/*     */       
/*  97 */       LOGGER.debug("Not in a ServletContext environment, thus not loading WebLookup plugin.");
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
/*     */   public String lookup(String var) {
/* 115 */     return lookup(null, var);
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
/*     */   public String lookup(LogEvent event, String var) {
/* 133 */     if (var == null) {
/* 134 */       return null;
/*     */     }
/*     */     
/* 137 */     int prefixPos = var.indexOf(':');
/* 138 */     if (prefixPos >= 0) {
/* 139 */       String prefix = var.substring(0, prefixPos);
/* 140 */       String name = var.substring(prefixPos + 1);
/* 141 */       StrLookup lookup = this.lookups.get(prefix);
/* 142 */       String value = null;
/* 143 */       if (lookup != null) {
/* 144 */         value = (event == null) ? lookup.lookup(name) : lookup.lookup(event, name);
/*     */       }
/*     */       
/* 147 */       if (value != null) {
/* 148 */         return value;
/*     */       }
/* 150 */       var = var.substring(prefixPos + 1);
/*     */     } 
/* 152 */     if (this.defaultLookup != null) {
/* 153 */       return (event == null) ? this.defaultLookup.lookup(var) : this.defaultLookup.lookup(event, var);
/*     */     }
/* 155 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 160 */     StringBuilder sb = new StringBuilder();
/* 161 */     for (String name : this.lookups.keySet()) {
/* 162 */       if (sb.length() == 0) {
/* 163 */         sb.append('{');
/*     */       } else {
/* 165 */         sb.append(", ");
/*     */       } 
/*     */       
/* 168 */       sb.append(name);
/*     */     } 
/* 170 */     if (sb.length() > 0) {
/* 171 */       sb.append('}');
/*     */     }
/* 173 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\lookup\Interpolator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */