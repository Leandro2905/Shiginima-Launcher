/*     */ package org.yaml.snakeyaml;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.yaml.snakeyaml.nodes.Tag;
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
/*     */ public final class TypeDescription
/*     */ {
/*     */   private final Class<? extends Object> type;
/*     */   private Tag tag;
/*     */   private Map<String, Class<? extends Object>> listProperties;
/*     */   private Map<String, Class<? extends Object>> keyProperties;
/*     */   private Map<String, Class<? extends Object>> valueProperties;
/*     */   
/*     */   public TypeDescription(Class<? extends Object> clazz, Tag tag) {
/*  35 */     this.type = clazz;
/*  36 */     this.tag = tag;
/*  37 */     this.listProperties = new HashMap<String, Class<? extends Object>>();
/*  38 */     this.keyProperties = new HashMap<String, Class<? extends Object>>();
/*  39 */     this.valueProperties = new HashMap<String, Class<? extends Object>>();
/*     */   }
/*     */   
/*     */   public TypeDescription(Class<? extends Object> clazz, String tag) {
/*  43 */     this(clazz, new Tag(tag));
/*     */   }
/*     */   
/*     */   public TypeDescription(Class<? extends Object> clazz) {
/*  47 */     this(clazz, (Tag)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Tag getTag() {
/*  57 */     return this.tag;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTag(Tag tag) {
/*  67 */     this.tag = tag;
/*     */   }
/*     */   
/*     */   public void setTag(String tag) {
/*  71 */     setTag(new Tag(tag));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Class<? extends Object> getType() {
/*  80 */     return this.type;
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
/*     */   public void putListPropertyType(String property, Class<? extends Object> type) {
/*  92 */     this.listProperties.put(property, type);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Class<? extends Object> getListPropertyType(String property) {
/* 103 */     return this.listProperties.get(property);
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
/*     */   public void putMapPropertyType(String property, Class<? extends Object> key, Class<? extends Object> value) {
/* 118 */     this.keyProperties.put(property, key);
/* 119 */     this.valueProperties.put(property, value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Class<? extends Object> getMapKeyType(String property) {
/* 130 */     return this.keyProperties.get(property);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Class<? extends Object> getMapValueType(String property) {
/* 141 */     return this.valueProperties.get(property);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 146 */     return "TypeDescription for " + getType() + " (tag='" + getTag() + "')";
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\yaml\snakeyaml\TypeDescription.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */