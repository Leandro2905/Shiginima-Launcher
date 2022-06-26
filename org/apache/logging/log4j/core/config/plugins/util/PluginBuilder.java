/*     */ package org.apache.logging.log4j.core.config.plugins.util;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.apache.logging.log4j.Level;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.config.Node;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginAliases;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginFactory;
/*     */ import org.apache.logging.log4j.core.config.plugins.visitors.PluginVisitor;
/*     */ import org.apache.logging.log4j.core.config.plugins.visitors.PluginVisitors;
/*     */ import org.apache.logging.log4j.core.util.Assert;
/*     */ import org.apache.logging.log4j.core.util.Builder;
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
/*     */ public class PluginBuilder<T>
/*     */   implements Builder<T>
/*     */ {
/*  50 */   private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*     */ 
/*     */   
/*     */   private final PluginType<T> pluginType;
/*     */   
/*     */   private final Class<T> clazz;
/*     */   
/*     */   private Configuration configuration;
/*     */   
/*     */   private Node node;
/*     */   
/*     */   private LogEvent event;
/*     */ 
/*     */   
/*     */   public PluginBuilder(PluginType<T> pluginType) {
/*  65 */     this.pluginType = pluginType;
/*  66 */     this.clazz = pluginType.getPluginClass();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PluginBuilder<T> withConfiguration(Configuration configuration) {
/*  76 */     this.configuration = configuration;
/*  77 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PluginBuilder<T> withConfigurationNode(Node node) {
/*  87 */     this.node = node;
/*  88 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PluginBuilder<T> forLogEvent(LogEvent event) {
/*  98 */     this.event = event;
/*  99 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public T build() {
/* 109 */     verify();
/*     */     
/*     */     try {
/* 112 */       LOGGER.debug("Building Plugin[name={}, class={}]. Searching for builder factory method...", new Object[] { this.pluginType.getElementName(), this.pluginType.getPluginClass().getName() });
/*     */       
/* 114 */       Builder<T> builder = createBuilder(this.clazz);
/* 115 */       if (builder != null) {
/* 116 */         injectFields(builder);
/* 117 */         T result = (T)builder.build();
/* 118 */         LOGGER.debug("Built Plugin[name={}] OK from builder factory method.", new Object[] { this.pluginType.getElementName() });
/* 119 */         return result;
/*     */       } 
/* 121 */     } catch (Exception e) {
/* 122 */       LOGGER.catching(Level.ERROR, e);
/* 123 */       LOGGER.error("Unable to inject fields into builder class for plugin type {}, element {}.", new Object[] { this.clazz, this.node.getName() });
/*     */     } 
/*     */ 
/*     */     
/*     */     try {
/* 128 */       LOGGER.debug("Still building Plugin[name={}, class={}]. Searching for factory method...", new Object[] { this.pluginType.getElementName(), this.pluginType.getPluginClass().getName() });
/*     */       
/* 130 */       Method factory = findFactoryMethod(this.clazz);
/* 131 */       Object[] params = generateParameters(factory);
/*     */       
/* 133 */       T plugin = (T)factory.invoke((Object)null, params);
/* 134 */       LOGGER.debug("Built Plugin[name={}] OK from factory method.", new Object[] { this.pluginType.getElementName() });
/* 135 */       return plugin;
/* 136 */     } catch (Exception e) {
/* 137 */       LOGGER.catching(Level.ERROR, e);
/* 138 */       LOGGER.error("Unable to invoke factory method in class {} for element {}.", new Object[] { this.clazz, this.node.getName() });
/* 139 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void verify() {
/* 144 */     Assert.requireNonNull(this.configuration, "No Configuration object was set.");
/* 145 */     Assert.requireNonNull(this.node, "No Node object was set.");
/*     */   }
/*     */ 
/*     */   
/*     */   private static <T> Builder<T> createBuilder(Class<T> clazz) throws InvocationTargetException, IllegalAccessException {
/* 150 */     for (Method method : clazz.getDeclaredMethods()) {
/* 151 */       if (method.isAnnotationPresent((Class)PluginBuilderFactory.class) && Modifier.isStatic(method.getModifiers())) {
/*     */ 
/*     */         
/* 154 */         Builder<T> builder = (Builder<T>)method.invoke((Object)null, new Object[0]);
/* 155 */         LOGGER.debug("Found builder factory method [{}]: {}.", new Object[] { method.getName(), method });
/* 156 */         return builder;
/*     */       } 
/*     */     } 
/* 159 */     LOGGER.debug("No builder factory method found in class {}. Going to try finding a factory method instead.", new Object[] { clazz.getName() });
/*     */     
/* 161 */     return null;
/*     */   }
/*     */   
/*     */   private void injectFields(Builder<T> builder) throws IllegalAccessException {
/* 165 */     Field[] fields = builder.getClass().getDeclaredFields();
/* 166 */     StringBuilder log = new StringBuilder();
/* 167 */     for (Field field : fields) {
/* 168 */       log.append((log.length() == 0) ? "with params(" : ", ");
/* 169 */       field.setAccessible(true);
/* 170 */       Annotation[] annotations = field.getDeclaredAnnotations();
/* 171 */       String[] aliases = extractPluginAliases(annotations);
/* 172 */       for (Annotation a : annotations) {
/* 173 */         if (!(a instanceof PluginAliases)) {
/*     */ 
/*     */           
/* 176 */           PluginVisitor<? extends Annotation> visitor = PluginVisitors.findVisitor(a.annotationType());
/*     */           
/* 178 */           if (visitor != null) {
/* 179 */             Object value = visitor.setAliases(aliases).setAnnotation(a).setConversionType(field.getType()).setStrSubstitutor(this.configuration.getStrSubstitutor()).setMember(field).visit(this.configuration, this.node, this.event, log);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 186 */             if (value != null)
/* 187 */               field.set(builder, value); 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 192 */     if (log.length() > 0) {
/* 193 */       log.append(')');
/*     */     }
/* 195 */     LOGGER.debug("Calling build() on class {} for element {} {}", new Object[] { builder.getClass(), this.node.getName(), log.toString() });
/*     */     
/* 197 */     checkForRemainingAttributes();
/* 198 */     verifyNodeChildrenUsed();
/*     */   }
/*     */   
/*     */   private static <T> Method findFactoryMethod(Class<T> clazz) {
/* 202 */     for (Method method : clazz.getDeclaredMethods()) {
/* 203 */       if (method.isAnnotationPresent((Class)PluginFactory.class) && Modifier.isStatic(method.getModifiers())) {
/*     */         
/* 205 */         LOGGER.debug("Found factory method [{}]: {}.", new Object[] { method.getName(), method });
/* 206 */         return method;
/*     */       } 
/*     */     } 
/* 209 */     LOGGER.debug("No factory method found in class {}.", new Object[] { clazz.getName() });
/* 210 */     return null;
/*     */   }
/*     */   
/*     */   private Object[] generateParameters(Method factory) {
/* 214 */     StringBuilder log = new StringBuilder();
/* 215 */     Class<?>[] types = factory.getParameterTypes();
/* 216 */     Annotation[][] annotations = factory.getParameterAnnotations();
/* 217 */     Object[] args = new Object[annotations.length];
/* 218 */     for (int i = 0; i < annotations.length; i++) {
/* 219 */       log.append((log.length() == 0) ? "with params(" : ", ");
/* 220 */       String[] aliases = extractPluginAliases(annotations[i]);
/* 221 */       for (Annotation a : annotations[i]) {
/* 222 */         if (!(a instanceof PluginAliases)) {
/*     */ 
/*     */           
/* 225 */           PluginVisitor<? extends Annotation> visitor = PluginVisitors.findVisitor(a.annotationType());
/*     */           
/* 227 */           if (visitor != null) {
/* 228 */             args[i] = visitor.setAliases(aliases).setAnnotation(a).setConversionType(types[i]).setStrSubstitutor(this.configuration.getStrSubstitutor()).setMember(factory).visit(this.configuration, this.node, this.event, log);
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 237 */     if (log.length() > 0) {
/* 238 */       log.append(')');
/*     */     }
/* 240 */     checkForRemainingAttributes();
/* 241 */     verifyNodeChildrenUsed();
/* 242 */     LOGGER.debug("Calling {} on class {} for element {} {}", new Object[] { factory.getName(), this.clazz.getName(), this.node.getName(), log.toString() });
/*     */     
/* 244 */     return args;
/*     */   }
/*     */   
/*     */   private static String[] extractPluginAliases(Annotation... parmTypes) {
/* 248 */     String[] aliases = null;
/* 249 */     for (Annotation a : parmTypes) {
/* 250 */       if (a instanceof PluginAliases) {
/* 251 */         aliases = ((PluginAliases)a).value();
/*     */       }
/*     */     } 
/* 254 */     return aliases;
/*     */   }
/*     */   
/*     */   private void checkForRemainingAttributes() {
/* 258 */     Map<String, String> attrs = this.node.getAttributes();
/* 259 */     if (!attrs.isEmpty()) {
/* 260 */       StringBuilder sb = new StringBuilder();
/* 261 */       for (String key : attrs.keySet()) {
/* 262 */         if (sb.length() == 0) {
/* 263 */           sb.append(this.node.getName());
/* 264 */           sb.append(" contains ");
/* 265 */           if (attrs.size() == 1) {
/* 266 */             sb.append("an invalid element or attribute ");
/*     */           } else {
/* 268 */             sb.append("invalid attributes ");
/*     */           } 
/*     */         } else {
/* 271 */           sb.append(", ");
/*     */         } 
/* 273 */         sb.append('"');
/* 274 */         sb.append(key);
/* 275 */         sb.append('"');
/*     */       } 
/*     */       
/* 278 */       LOGGER.error(sb.toString());
/*     */     } 
/*     */   }
/*     */   
/*     */   private void verifyNodeChildrenUsed() {
/* 283 */     List<Node> children = this.node.getChildren();
/* 284 */     if (!this.pluginType.isDeferChildren() && !children.isEmpty())
/* 285 */       for (Node child : children) {
/* 286 */         String nodeType = this.node.getType().getElementName();
/* 287 */         String start = nodeType.equals(this.node.getName()) ? this.node.getName() : (nodeType + ' ' + this.node.getName());
/* 288 */         LOGGER.error("{} has no parameter that matches element {}", new Object[] { start, child.getName() });
/*     */       }  
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\config\plugin\\util\PluginBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */