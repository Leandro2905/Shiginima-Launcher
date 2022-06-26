/*     */ package org.apache.logging.log4j.core.config.plugins.processor;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import javax.annotation.processing.AbstractProcessor;
/*     */ import javax.annotation.processing.RoundEnvironment;
/*     */ import javax.annotation.processing.SupportedAnnotationTypes;
/*     */ import javax.lang.model.SourceVersion;
/*     */ import javax.lang.model.element.Element;
/*     */ import javax.lang.model.element.ElementVisitor;
/*     */ import javax.lang.model.element.TypeElement;
/*     */ import javax.lang.model.util.Elements;
/*     */ import javax.lang.model.util.SimpleElementVisitor6;
/*     */ import javax.tools.Diagnostic;
/*     */ import javax.tools.FileObject;
/*     */ import javax.tools.StandardLocation;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginAliases;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @SupportedAnnotationTypes({"org.apache.logging.log4j.core.config.plugins.*"})
/*     */ public class PluginProcessor
/*     */   extends AbstractProcessor
/*     */ {
/*     */   public static final String PLUGIN_CACHE_FILE = "META-INF/org/apache/logging/log4j/core/config/plugins/Log4j2Plugins.dat";
/*  59 */   private final PluginCache pluginCache = new PluginCache();
/*     */ 
/*     */   
/*     */   public SourceVersion getSupportedSourceVersion() {
/*  63 */     return SourceVersion.latest();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
/*     */     try {
/*  69 */       Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith((Class)Plugin.class);
/*  70 */       if (elements.isEmpty()) {
/*  71 */         return false;
/*     */       }
/*  73 */       collectPlugins(elements);
/*  74 */       writeCacheFile(elements.<Element>toArray(new Element[elements.size()]));
/*  75 */       return true;
/*  76 */     } catch (IOException e) {
/*  77 */       error(e.getMessage());
/*  78 */       return false;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void error(CharSequence message) {
/*  83 */     this.processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, message);
/*     */   }
/*     */   
/*     */   private void collectPlugins(Iterable<? extends Element> elements) {
/*  87 */     Elements elementUtils = this.processingEnv.getElementUtils();
/*  88 */     ElementVisitor<PluginEntry, Plugin> pluginVisitor = new PluginElementVisitor(elementUtils);
/*     */     
/*  90 */     ElementVisitor<Collection<PluginEntry>, Plugin> pluginAliasesVisitor = new PluginAliasesElementVisitor(elementUtils);
/*     */     
/*  92 */     for (Element element : elements) {
/*  93 */       Plugin plugin = element.<Plugin>getAnnotation(Plugin.class);
/*  94 */       PluginEntry entry = element.<PluginEntry, Plugin>accept(pluginVisitor, plugin);
/*  95 */       ConcurrentMap<String, PluginEntry> category = this.pluginCache.getCategory(entry.getCategory());
/*  96 */       category.put(entry.getKey(), entry);
/*  97 */       Collection<PluginEntry> entries = element.<Collection<PluginEntry>, Plugin>accept(pluginAliasesVisitor, plugin);
/*  98 */       for (PluginEntry pluginEntry : entries) {
/*  99 */         category.put(pluginEntry.getKey(), pluginEntry);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private void writeCacheFile(Element... elements) throws IOException {
/* 105 */     FileObject fo = this.processingEnv.getFiler().createResource(StandardLocation.CLASS_OUTPUT, "", "META-INF/org/apache/logging/log4j/core/config/plugins/Log4j2Plugins.dat", elements);
/*     */     
/* 107 */     OutputStream out = fo.openOutputStream();
/*     */     try {
/* 109 */       this.pluginCache.writeCache(out);
/*     */     } finally {
/* 111 */       out.close();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static class PluginElementVisitor
/*     */     extends SimpleElementVisitor6<PluginEntry, Plugin>
/*     */   {
/*     */     private final Elements elements;
/*     */ 
/*     */     
/*     */     private PluginElementVisitor(Elements elements) {
/* 123 */       this.elements = elements;
/*     */     }
/*     */ 
/*     */     
/*     */     public PluginEntry visitType(TypeElement e, Plugin plugin) {
/* 128 */       if (plugin == null) {
/* 129 */         throw new NullPointerException("Plugin annotation is null.");
/*     */       }
/* 131 */       PluginEntry entry = new PluginEntry();
/* 132 */       entry.setKey(plugin.name().toLowerCase());
/* 133 */       entry.setClassName(this.elements.getBinaryName(e).toString());
/* 134 */       entry.setName("".equals(plugin.elementType()) ? plugin.name() : plugin.elementType());
/* 135 */       entry.setPrintable(plugin.printObject());
/* 136 */       entry.setDefer(plugin.deferChildren());
/* 137 */       entry.setCategory(plugin.category());
/* 138 */       return entry;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static class PluginAliasesElementVisitor
/*     */     extends SimpleElementVisitor6<Collection<PluginEntry>, Plugin>
/*     */   {
/*     */     private final Elements elements;
/*     */ 
/*     */     
/*     */     private PluginAliasesElementVisitor(Elements elements) {
/* 150 */       super(Collections.emptyList());
/* 151 */       this.elements = elements;
/*     */     }
/*     */ 
/*     */     
/*     */     public Collection<PluginEntry> visitType(TypeElement e, Plugin plugin) {
/* 156 */       PluginAliases aliases = e.<PluginAliases>getAnnotation(PluginAliases.class);
/* 157 */       if (aliases == null) {
/* 158 */         return this.DEFAULT_VALUE;
/*     */       }
/* 160 */       Collection<PluginEntry> entries = new ArrayList<PluginEntry>((aliases.value()).length);
/* 161 */       for (String alias : aliases.value()) {
/* 162 */         PluginEntry entry = new PluginEntry();
/* 163 */         entry.setKey(alias.toLowerCase());
/* 164 */         entry.setClassName(this.elements.getBinaryName(e).toString());
/* 165 */         entry.setName("".equals(plugin.elementType()) ? alias : plugin.elementType());
/* 166 */         entry.setPrintable(plugin.printObject());
/* 167 */         entry.setDefer(plugin.deferChildren());
/* 168 */         entry.setCategory(plugin.category());
/* 169 */         entries.add(entry);
/*     */       } 
/* 171 */       return entries;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\config\plugins\processor\PluginProcessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */