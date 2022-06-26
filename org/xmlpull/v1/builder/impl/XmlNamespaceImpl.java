/*    */ package org.xmlpull.v1.builder.impl;
/*    */ 
/*    */ import org.xmlpull.v1.builder.XmlBuilderException;
/*    */ import org.xmlpull.v1.builder.XmlNamespace;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class XmlNamespaceImpl
/*    */   implements XmlNamespace
/*    */ {
/*    */   private String namespaceName;
/*    */   private String prefix;
/*    */   
/*    */   XmlNamespaceImpl(String namespaceName) {
/* 20 */     if (namespaceName == null) {
/* 21 */       throw new XmlBuilderException("namespace name can not be null");
/*    */     }
/* 23 */     this.namespaceName = namespaceName;
/*    */   }
/*    */   
/*    */   XmlNamespaceImpl(String prefix, String namespaceName) {
/* 27 */     this.prefix = prefix;
/* 28 */     if (namespaceName == null) {
/* 29 */       throw new XmlBuilderException("namespace name can not be null");
/*    */     }
/* 31 */     if (prefix != null && 
/* 32 */       prefix.indexOf(':') != -1) {
/* 33 */       throw new XmlBuilderException("prefix '" + prefix + "' for namespace '" + namespaceName + "' can not contain colon (:)");
/*    */     }
/*    */ 
/*    */     
/* 37 */     this.namespaceName = namespaceName;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getPrefix() {
/* 42 */     return this.prefix;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getNamespaceName() {
/* 47 */     return this.namespaceName;
/*    */   }
/*    */   
/*    */   public boolean equals(Object other) {
/* 51 */     if (other == this) return true; 
/* 52 */     if (other == null) return false; 
/* 53 */     if (!(other instanceof XmlNamespace)) return false; 
/* 54 */     XmlNamespace otherNamespace = (XmlNamespace)other;
/* 55 */     return getNamespaceName().equals(otherNamespace.getNamespaceName());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 62 */     return "{prefix='" + this.prefix + "',namespaceName='" + this.namespaceName + "'}";
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\xmlpull\v1\builder\impl\XmlNamespaceImpl.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */