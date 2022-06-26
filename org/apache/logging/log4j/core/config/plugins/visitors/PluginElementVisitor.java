/*     */ package org.apache.logging.log4j.core.config.plugins.visitors;
/*     */ 
/*     */ import java.lang.reflect.Array;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.config.Node;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginElement;
/*     */ import org.apache.logging.log4j.core.config.plugins.util.PluginType;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PluginElementVisitor
/*     */   extends AbstractPluginVisitor<PluginElement>
/*     */ {
/*     */   public PluginElementVisitor() {
/*  37 */     super(PluginElement.class);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object visit(Configuration configuration, Node node, LogEvent event, StringBuilder log) {
/*  43 */     String name = this.annotation.value();
/*  44 */     if (this.conversionType.isArray()) {
/*  45 */       setConversionType(this.conversionType.getComponentType());
/*  46 */       List<Object> values = new ArrayList();
/*  47 */       Collection<Node> used = new ArrayList<Node>();
/*  48 */       log.append("={");
/*  49 */       boolean first = true;
/*  50 */       for (Node child : node.getChildren()) {
/*  51 */         PluginType<?> childType = child.getType();
/*  52 */         if (name.equalsIgnoreCase(childType.getElementName()) || this.conversionType.isAssignableFrom(childType.getPluginClass())) {
/*     */           
/*  54 */           if (!first) {
/*  55 */             log.append(", ");
/*     */           }
/*  57 */           first = false;
/*  58 */           used.add(child);
/*  59 */           Object childObject = child.getObject();
/*  60 */           if (childObject == null) {
/*  61 */             LOGGER.error("Null object returned for {} in {}.", new Object[] { child.getName(), node.getName() });
/*     */             continue;
/*     */           } 
/*  64 */           if (childObject.getClass().isArray()) {
/*  65 */             log.append(Arrays.toString((Object[])childObject)).append('}');
/*  66 */             return childObject;
/*     */           } 
/*  68 */           log.append(child.toString());
/*  69 */           values.add(childObject);
/*     */         } 
/*     */       } 
/*  72 */       log.append('}');
/*     */       
/*  74 */       if (!values.isEmpty() && !this.conversionType.isAssignableFrom(values.get(0).getClass())) {
/*  75 */         LOGGER.error("Attempted to assign attribute {} to list of type {} which is incompatible with {}.", new Object[] { name, values.get(0).getClass(), this.conversionType });
/*     */         
/*  77 */         return null;
/*     */       } 
/*  79 */       node.getChildren().removeAll(used);
/*     */       
/*  81 */       Object[] array = (Object[])Array.newInstance(this.conversionType, values.size());
/*  82 */       for (int i = 0; i < array.length; i++) {
/*  83 */         array[i] = values.get(i);
/*     */       }
/*  85 */       return array;
/*     */     } 
/*  87 */     Node namedNode = findNamedNode(name, node.getChildren());
/*  88 */     if (namedNode == null) {
/*  89 */       log.append("null");
/*  90 */       return null;
/*     */     } 
/*  92 */     log.append(namedNode.getName()).append('(').append(namedNode.toString()).append(')');
/*  93 */     node.getChildren().remove(namedNode);
/*  94 */     return namedNode.getObject();
/*     */   }
/*     */   
/*     */   private Node findNamedNode(String name, Iterable<Node> children) {
/*  98 */     for (Node child : children) {
/*  99 */       PluginType<?> childType = child.getType();
/* 100 */       if (name.equalsIgnoreCase(childType.getElementName()) || this.conversionType.isAssignableFrom(childType.getPluginClass()))
/*     */       {
/*     */ 
/*     */         
/* 104 */         return child;
/*     */       }
/*     */     } 
/* 107 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\config\plugins\visitors\PluginElementVisitor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */