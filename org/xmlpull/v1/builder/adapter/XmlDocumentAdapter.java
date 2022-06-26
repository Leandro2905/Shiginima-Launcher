/*     */ package org.xmlpull.v1.builder.adapter;
/*     */ 
/*     */ import org.xmlpull.v1.builder.Iterable;
/*     */ import org.xmlpull.v1.builder.XmlComment;
/*     */ import org.xmlpull.v1.builder.XmlContainer;
/*     */ import org.xmlpull.v1.builder.XmlDoctype;
/*     */ import org.xmlpull.v1.builder.XmlDocument;
/*     */ import org.xmlpull.v1.builder.XmlElement;
/*     */ import org.xmlpull.v1.builder.XmlNamespace;
/*     */ import org.xmlpull.v1.builder.XmlNotation;
/*     */ import org.xmlpull.v1.builder.XmlProcessingInstruction;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class XmlDocumentAdapter
/*     */   implements XmlDocument
/*     */ {
/*     */   private XmlDocument target;
/*     */   
/*     */   public Object clone() throws CloneNotSupportedException {
/*  22 */     XmlDocumentAdapter ela = (XmlDocumentAdapter)super.clone();
/*  23 */     ela.target = (XmlDocument)this.target.clone();
/*  24 */     return ela;
/*     */   }
/*     */ 
/*     */   
/*     */   public XmlDocumentAdapter(XmlDocument target) {
/*  29 */     this.target = target;
/*     */     
/*  31 */     fixImportedChildParent(target.getDocumentElement());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void fixImportedChildParent(Object child) {
/*  37 */     if (child instanceof XmlElement) {
/*  38 */       XmlElement childEl = (XmlElement)child;
/*  39 */       XmlContainer childElParent = childEl.getParent();
/*  40 */       if (childElParent == this.target) {
/*  41 */         childEl.setParent((XmlContainer)this);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public Iterable children() {
/*  47 */     return this.target.children();
/*     */   }
/*     */   
/*     */   public XmlElement getDocumentElement() {
/*  51 */     return this.target.getDocumentElement();
/*     */   }
/*     */   
/*     */   public XmlElement requiredElement(XmlNamespace n, String name) {
/*  55 */     return this.target.requiredElement(n, name);
/*     */   }
/*     */   
/*     */   public XmlElement element(XmlNamespace n, String name) {
/*  59 */     return this.target.element(n, name);
/*     */   }
/*     */   
/*     */   public XmlElement element(XmlNamespace n, String name, boolean create) {
/*  63 */     return this.target.element(n, name, create);
/*     */   }
/*     */   
/*     */   public Iterable notations() {
/*  67 */     return this.target.notations();
/*     */   }
/*     */   
/*     */   public Iterable unparsedEntities() {
/*  71 */     return this.target.unparsedEntities();
/*     */   }
/*     */   
/*     */   public String getBaseUri() {
/*  75 */     return this.target.getBaseUri();
/*     */   }
/*     */   
/*     */   public String getCharacterEncodingScheme() {
/*  79 */     return this.target.getCharacterEncodingScheme();
/*     */   }
/*     */   
/*     */   public void setCharacterEncodingScheme(String characterEncoding) {
/*  83 */     this.target.setCharacterEncodingScheme(characterEncoding);
/*     */   }
/*     */   
/*     */   public Boolean isStandalone() {
/*  87 */     return this.target.isStandalone();
/*     */   }
/*     */   
/*     */   public String getVersion() {
/*  91 */     return this.target.getVersion();
/*     */   }
/*     */   
/*     */   public boolean isAllDeclarationsProcessed() {
/*  95 */     return this.target.isAllDeclarationsProcessed();
/*     */   }
/*     */   
/*     */   public void setDocumentElement(XmlElement rootElement) {
/*  99 */     this.target.setDocumentElement(rootElement);
/*     */   }
/*     */   
/*     */   public void addChild(Object child) {
/* 103 */     this.target.addChild(child);
/*     */   }
/*     */   
/*     */   public void insertChild(int pos, Object child) {
/* 107 */     this.target.insertChild(pos, child);
/*     */   }
/*     */   
/*     */   public void removeAllChildren() {
/* 111 */     this.target.removeAllChildren();
/*     */   }
/*     */   
/*     */   public XmlComment newComment(String content) {
/* 115 */     return this.target.newComment(content);
/*     */   }
/*     */   
/*     */   public XmlComment addComment(String content) {
/* 119 */     return this.target.addComment(content);
/*     */   }
/*     */   
/*     */   public XmlDoctype newDoctype(String systemIdentifier, String publicIdentifier) {
/* 123 */     return this.target.newDoctype(systemIdentifier, publicIdentifier);
/*     */   }
/*     */   
/*     */   public XmlDoctype addDoctype(String systemIdentifier, String publicIdentifier) {
/* 127 */     return this.target.addDoctype(systemIdentifier, publicIdentifier);
/*     */   }
/*     */   
/*     */   public XmlElement addDocumentElement(String name) {
/* 131 */     return this.target.addDocumentElement(name);
/*     */   }
/*     */   
/*     */   public XmlElement addDocumentElement(XmlNamespace namespace, String name) {
/* 135 */     return this.target.addDocumentElement(namespace, name);
/*     */   }
/*     */   
/*     */   public XmlProcessingInstruction newProcessingInstruction(String target, String content) {
/* 139 */     return this.target.newProcessingInstruction(target, content);
/*     */   }
/*     */   
/*     */   public XmlProcessingInstruction addProcessingInstruction(String target, String content) {
/* 143 */     return this.target.addProcessingInstruction(target, content);
/*     */   }
/*     */   
/*     */   public void removeAllUnparsedEntities() {
/* 147 */     this.target.removeAllUnparsedEntities();
/*     */   }
/*     */ 
/*     */   
/*     */   public XmlNotation addNotation(String name, String systemIdentifier, String publicIdentifier, String declarationBaseUri) {
/* 152 */     return this.target.addNotation(name, systemIdentifier, publicIdentifier, declarationBaseUri);
/*     */   }
/*     */   
/*     */   public void removeAllNotations() {
/* 156 */     this.target.removeAllNotations();
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\xmlpull\v1\builder\adapter\XmlDocumentAdapter.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */