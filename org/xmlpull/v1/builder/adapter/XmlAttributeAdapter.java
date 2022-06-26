/*    */ package org.xmlpull.v1.builder.adapter;
/*    */ 
/*    */ import org.xmlpull.v1.builder.XmlAttribute;
/*    */ import org.xmlpull.v1.builder.XmlElement;
/*    */ import org.xmlpull.v1.builder.XmlNamespace;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class XmlAttributeAdapter
/*    */   implements XmlAttribute
/*    */ {
/*    */   private XmlAttribute target;
/*    */   
/*    */   public Object clone() throws CloneNotSupportedException {
/* 17 */     XmlAttributeAdapter ela = (XmlAttributeAdapter)super.clone();
/* 18 */     ela.target = (XmlAttribute)this.target.clone();
/* 19 */     return ela;
/*    */   }
/*    */ 
/*    */   
/*    */   public XmlAttributeAdapter(XmlAttribute target) {
/* 24 */     this.target = target;
/*    */   }
/*    */ 
/*    */   
/*    */   public XmlElement getOwner() {
/* 29 */     return this.target.getOwner();
/*    */   }
/*    */   
/*    */   public String getNamespaceName() {
/* 33 */     return this.target.getNamespaceName();
/*    */   }
/*    */   
/*    */   public XmlNamespace getNamespace() {
/* 37 */     return this.target.getNamespace();
/*    */   }
/*    */   
/*    */   public String getName() {
/* 41 */     return this.target.getName();
/*    */   }
/*    */   
/*    */   public String getValue() {
/* 45 */     return this.target.getValue();
/*    */   }
/*    */   
/*    */   public String getType() {
/* 49 */     return this.target.getType();
/*    */   }
/*    */   
/*    */   public boolean isSpecified() {
/* 53 */     return this.target.isSpecified();
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\xmlpull\v1\builder\adapter\XmlAttributeAdapter.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */