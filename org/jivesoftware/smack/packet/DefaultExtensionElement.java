/*     */ package org.jivesoftware.smack.packet;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.jivesoftware.smack.util.XmlStringBuilder;
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
/*     */ public class DefaultExtensionElement
/*     */   implements ExtensionElement
/*     */ {
/*     */   private String elementName;
/*     */   private String namespace;
/*     */   private Map<String, String> map;
/*     */   
/*     */   public DefaultExtensionElement(String elementName, String namespace) {
/*  62 */     this.elementName = elementName;
/*  63 */     this.namespace = namespace;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getElementName() {
/*  72 */     return this.elementName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getNamespace() {
/*  81 */     return this.namespace;
/*     */   }
/*     */ 
/*     */   
/*     */   public CharSequence toXML() {
/*  86 */     XmlStringBuilder buf = new XmlStringBuilder();
/*  87 */     buf.halfOpenElement(this.elementName).xmlnsAttribute(this.namespace).rightAngleBracket();
/*  88 */     for (String name : getNames()) {
/*  89 */       String value = getValue(name);
/*  90 */       buf.element(name, value);
/*     */     } 
/*  92 */     buf.closeElement(this.elementName);
/*  93 */     return (CharSequence)buf;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized Collection<String> getNames() {
/* 103 */     if (this.map == null) {
/* 104 */       return Collections.emptySet();
/*     */     }
/* 106 */     return Collections.unmodifiableSet((new HashMap<>(this.map)).keySet());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized String getValue(String name) {
/* 116 */     if (this.map == null) {
/* 117 */       return null;
/*     */     }
/* 119 */     return this.map.get(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void setValue(String name, String value) {
/* 129 */     if (this.map == null) {
/* 130 */       this.map = new HashMap<>();
/*     */     }
/* 132 */     this.map.put(name, value);
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jivesoftware\smack\packet\DefaultExtensionElement.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */