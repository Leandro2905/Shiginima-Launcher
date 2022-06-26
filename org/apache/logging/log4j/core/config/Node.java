/*     */ package org.apache.logging.log4j.core.config;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
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
/*     */ 
/*     */ public class Node
/*     */ {
/*     */   private final Node parent;
/*     */   private final String name;
/*     */   private String value;
/*     */   private final PluginType<?> type;
/*  35 */   private final Map<String, String> attributes = new HashMap<String, String>();
/*  36 */   private final List<Node> children = new ArrayList<Node>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Object object;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Node(Node parent, String name, PluginType<?> type) {
/*  49 */     this.parent = parent;
/*  50 */     this.name = name;
/*  51 */     this.type = type;
/*     */   }
/*     */   
/*     */   public Node() {
/*  55 */     this.parent = null;
/*  56 */     this.name = null;
/*  57 */     this.type = null;
/*     */   }
/*     */   
/*     */   public Node(Node node) {
/*  61 */     this.parent = node.parent;
/*  62 */     this.name = node.name;
/*  63 */     this.type = node.type;
/*  64 */     this.attributes.putAll(node.getAttributes());
/*  65 */     this.value = node.getValue();
/*  66 */     for (Node child : node.getChildren()) {
/*  67 */       this.children.add(new Node(child));
/*     */     }
/*  69 */     this.object = node.object;
/*     */   }
/*     */   
/*     */   public Map<String, String> getAttributes() {
/*  73 */     return this.attributes;
/*     */   }
/*     */   
/*     */   public List<Node> getChildren() {
/*  77 */     return this.children;
/*     */   }
/*     */   
/*     */   public boolean hasChildren() {
/*  81 */     return !this.children.isEmpty();
/*     */   }
/*     */   
/*     */   public String getValue() {
/*  85 */     return this.value;
/*     */   }
/*     */   
/*     */   public void setValue(String value) {
/*  89 */     this.value = value;
/*     */   }
/*     */   
/*     */   public Node getParent() {
/*  93 */     return this.parent;
/*     */   }
/*     */   
/*     */   public String getName() {
/*  97 */     return this.name;
/*     */   }
/*     */   
/*     */   public boolean isRoot() {
/* 101 */     return (this.parent == null);
/*     */   }
/*     */   
/*     */   public void setObject(Object obj) {
/* 105 */     this.object = obj;
/*     */   }
/*     */   
/*     */   public Object getObject() {
/* 109 */     return this.object;
/*     */   }
/*     */   
/*     */   public PluginType<?> getType() {
/* 113 */     return this.type;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 118 */     if (this.object == null) {
/* 119 */       return "null";
/*     */     }
/* 121 */     return this.type.isObjectPrintable() ? this.object.toString() : (this.type.getPluginClass().getName() + " with name " + this.name);
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\config\Node.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */