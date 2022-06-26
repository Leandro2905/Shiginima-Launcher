/*     */ package org.xmlpull.v1.builder.impl;
/*     */ 
/*     */ import org.xmlpull.v1.builder.XmlAttribute;
/*     */ import org.xmlpull.v1.builder.XmlElement;
/*     */ import org.xmlpull.v1.builder.XmlNamespace;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class XmlAttributeImpl
/*     */   implements XmlAttribute
/*     */ {
/*     */   private XmlElement owner_;
/*     */   private String prefix_;
/*     */   private XmlNamespace namespace_;
/*     */   private String name_;
/*     */   private String value_;
/*  19 */   private String type_ = "CDATA";
/*     */   private boolean default_;
/*     */   
/*     */   public Object clone() throws CloneNotSupportedException {
/*  23 */     XmlAttributeImpl cloned = (XmlAttributeImpl)super.clone();
/*  24 */     cloned.owner_ = null;
/*     */     
/*  26 */     cloned.prefix_ = this.prefix_;
/*  27 */     cloned.namespace_ = this.namespace_;
/*  28 */     cloned.name_ = this.name_;
/*  29 */     cloned.value_ = this.value_;
/*  30 */     cloned.default_ = this.default_;
/*  31 */     return cloned;
/*     */   }
/*     */ 
/*     */   
/*     */   XmlAttributeImpl(XmlElement owner, String name, String value) {
/*  36 */     this.owner_ = owner;
/*  37 */     this.name_ = name;
/*  38 */     if (value == null) throw new IllegalArgumentException("attribute value can not be null"); 
/*  39 */     this.value_ = value;
/*     */   }
/*     */ 
/*     */   
/*     */   XmlAttributeImpl(XmlElement owner, XmlNamespace namespace, String name, String value) {
/*  44 */     this(owner, name, value);
/*  45 */     this.namespace_ = namespace;
/*     */   }
/*     */ 
/*     */   
/*     */   XmlAttributeImpl(XmlElement owner, String type, XmlNamespace namespace, String name, String value) {
/*  50 */     this(owner, namespace, name, value);
/*  51 */     this.type_ = type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   XmlAttributeImpl(XmlElement owner, String type, XmlNamespace namespace, String name, String value, boolean specified) {
/*  61 */     this(owner, namespace, name, value);
/*  62 */     if (type == null) {
/*  63 */       throw new IllegalArgumentException("attribute type can not be null");
/*     */     }
/*     */     
/*  66 */     this.type_ = type;
/*  67 */     this.default_ = !specified;
/*     */   }
/*     */   
/*     */   public XmlElement getOwner() {
/*  71 */     return this.owner_;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public XmlNamespace getNamespace() {
/*  81 */     return this.namespace_;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getNamespaceName() {
/*  86 */     return (this.namespace_ != null) ? this.namespace_.getNamespaceName() : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getName() {
/*  91 */     return this.name_;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getValue() {
/*  96 */     return this.value_;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getType() {
/* 101 */     return this.type_;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSpecified() {
/* 106 */     return !this.default_;
/*     */   }
/*     */   
/*     */   public boolean equals(Object other) {
/* 110 */     if (other == this) return true; 
/* 111 */     if (other == null) return false; 
/* 112 */     if (!(other instanceof XmlAttribute)) return false; 
/* 113 */     XmlAttribute otherAttr = (XmlAttribute)other;
/* 114 */     return (getNamespaceName().equals(otherAttr.getNamespaceName()) && getName().equals(otherAttr.getName()) && getValue().equals(otherAttr.getValue()));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 120 */     return "name=" + this.name_ + " value=" + this.value_;
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\xmlpull\v1\builder\impl\XmlAttributeImpl.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */