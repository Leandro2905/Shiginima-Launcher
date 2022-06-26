/*     */ package org.xmlpull.v1.builder.impl;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import org.xmlpull.v1.builder.Iterable;
/*     */ import org.xmlpull.v1.builder.XmlBuilderException;
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
/*     */ public class XmlDocumentImpl
/*     */   implements XmlDocument
/*     */ {
/*  21 */   private List children = new ArrayList();
/*     */   private XmlElement root;
/*     */   private String version;
/*     */   private Boolean standalone;
/*     */   private String characterEncoding;
/*     */   
/*     */   public Object clone() throws CloneNotSupportedException {
/*  28 */     XmlDocumentImpl cloned = (XmlDocumentImpl)super.clone();
/*     */     
/*  30 */     cloned.root = null;
/*  31 */     cloned.children = cloneList(cloned, this.children);
/*  32 */     int pos = cloned.findDocumentElement();
/*  33 */     if (pos >= 0) {
/*  34 */       cloned.root = cloned.children.get(pos);
/*  35 */       cloned.root.setParent((XmlContainer)cloned);
/*     */     } 
/*  37 */     return cloned;
/*     */   }
/*     */   
/*     */   private List cloneList(XmlDocumentImpl cloned, List list) throws CloneNotSupportedException {
/*  41 */     if (list == null) {
/*  42 */       return null;
/*     */     }
/*  44 */     List newList = new ArrayList(list.size());
/*     */     
/*  46 */     for (int i = 0; i < list.size(); i++) {
/*     */       
/*  48 */       Object newMember, member = list.get(i);
/*     */       
/*  50 */       if (member instanceof XmlElement) {
/*  51 */         XmlElement el = (XmlElement)member;
/*  52 */         newMember = el.clone();
/*  53 */       } else if (member instanceof Cloneable) {
/*     */ 
/*     */         
/*     */         try {
/*  57 */           newMember = member.getClass().getMethod("clone", null).invoke(member, null);
/*  58 */         } catch (Exception e) {
/*  59 */           throw new CloneNotSupportedException("failed to call clone() on  " + member + e);
/*     */         } 
/*     */       } else {
/*  62 */         throw new CloneNotSupportedException("could not clone " + member + " of " + ((member != null) ? member.getClass().toString() : ""));
/*     */       } 
/*     */       
/*  65 */       newList.add(newMember);
/*     */     } 
/*  67 */     return newList;
/*     */   }
/*     */   
/*     */   public XmlDocumentImpl(String version, Boolean standalone, String characterEncoding) {
/*  71 */     this.version = version;
/*  72 */     this.standalone = standalone;
/*  73 */     this.characterEncoding = characterEncoding;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getVersion() {
/*  80 */     return this.version;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Boolean isStandalone() {
/*  87 */     return this.standalone;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getCharacterEncodingScheme() {
/*  95 */     return this.characterEncoding;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setCharacterEncodingScheme(String characterEncoding) {
/* 100 */     this.characterEncoding = characterEncoding;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public XmlProcessingInstruction newProcessingInstruction(String target, String content) {
/* 108 */     throw new XmlBuilderException("not implemented");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public XmlProcessingInstruction addProcessingInstruction(String target, String content) {
/* 115 */     throw new XmlBuilderException("not implemented");
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
/*     */   public Iterable children() {
/* 130 */     return new Iterable(this) {
/*     */         public Iterator iterator() {
/* 132 */           return this.this$0.children.iterator();
/*     */         }
/*     */ 
/*     */         
/*     */         private final XmlDocumentImpl this$0;
/*     */       };
/*     */   }
/*     */   
/*     */   public void removeAllUnparsedEntities() {
/* 141 */     throw new XmlBuilderException("not implemented");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDocumentElement(XmlElement rootElement) {
/* 148 */     int pos = findDocumentElement();
/* 149 */     if (pos >= 0) {
/* 150 */       this.children.set(pos, rootElement);
/*     */     } else {
/* 152 */       this.children.add(rootElement);
/*     */     } 
/* 154 */     this.root = rootElement;
/* 155 */     rootElement.setParent((XmlContainer)this);
/*     */   }
/*     */ 
/*     */   
/*     */   private int findDocumentElement() {
/* 160 */     for (int i = 0; i < this.children.size(); i++) {
/*     */       
/* 162 */       Object element = this.children.get(i);
/* 163 */       if (element instanceof XmlElement) {
/* 164 */         return i;
/*     */       }
/*     */     } 
/* 167 */     return -1;
/*     */   }
/*     */   
/*     */   public XmlElement requiredElement(XmlNamespace n, String name) {
/* 171 */     XmlElement el = element(n, name);
/* 172 */     if (el == null) {
/* 173 */       throw new XmlBuilderException("document does not contain element with name " + name + " in namespace " + n.getNamespaceName());
/*     */     }
/*     */     
/* 176 */     return el;
/*     */   }
/*     */   
/*     */   public XmlElement element(XmlNamespace n, String name) {
/* 180 */     return element(n, name, false);
/*     */   }
/*     */   
/*     */   public XmlElement element(XmlNamespace namespace, String name, boolean create) {
/* 184 */     XmlElement e = getDocumentElement();
/* 185 */     if (e == null) {
/* 186 */       return null;
/*     */     }
/* 188 */     String eNamespaceName = (e.getNamespace() != null) ? e.getNamespace().getNamespaceName() : null;
/* 189 */     if (namespace != null) {
/* 190 */       if (name.equals(e.getName()) && eNamespaceName != null && eNamespaceName.equals(namespace.getNamespaceName()))
/*     */       {
/*     */         
/* 193 */         return e;
/*     */       }
/*     */     }
/* 196 */     else if (name.equals(e.getName()) && eNamespaceName == null) {
/*     */ 
/*     */       
/* 199 */       return e;
/*     */     } 
/*     */     
/* 202 */     if (create) {
/* 203 */       return addDocumentElement(namespace, name);
/*     */     }
/* 205 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void insertChild(int pos, Object child) {
/* 214 */     throw new XmlBuilderException("not implemented");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public XmlComment addComment(String content) {
/* 222 */     XmlComment comment = new XmlCommentImpl((XmlContainer)this, content);
/* 223 */     this.children.add(comment);
/* 224 */     return comment;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public XmlDoctype newDoctype(String systemIdentifier, String publicIdentifier) {
/* 232 */     throw new XmlBuilderException("not implemented");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Iterable unparsedEntities() {
/* 241 */     throw new XmlBuilderException("not implemented");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeAllChildren() {
/* 249 */     throw new XmlBuilderException("not implemented");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public XmlComment newComment(String content) {
/* 257 */     return new XmlCommentImpl(null, content);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeAllNotations() {
/* 266 */     throw new XmlBuilderException("not implemented");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public XmlDoctype addDoctype(String systemIdentifier, String publicIdentifier) {
/* 274 */     throw new XmlBuilderException("not implemented");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addChild(Object child) {
/* 282 */     throw new XmlBuilderException("not implemented");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public XmlNotation addNotation(String name, String systemIdentifier, String publicIdentifier, String declarationBaseUri) {
/* 291 */     throw new XmlBuilderException("not implemented");
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
/*     */   public String getBaseUri() {
/* 305 */     throw new XmlBuilderException("not implemented");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Iterable notations() {
/* 314 */     throw new XmlBuilderException("not implemented");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public XmlElement addDocumentElement(String name) {
/* 321 */     return addDocumentElement(null, name);
/*     */   }
/*     */ 
/*     */   
/*     */   public XmlElement addDocumentElement(XmlNamespace namespace, String name) {
/* 326 */     XmlElement el = new XmlElementImpl(namespace, name);
/* 327 */     if (getDocumentElement() != null) {
/* 328 */       throw new XmlBuilderException("document already has root element");
/*     */     }
/* 330 */     setDocumentElement(el);
/* 331 */     return el;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isAllDeclarationsProcessed() {
/* 340 */     throw new XmlBuilderException("not implemented");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public XmlElement getDocumentElement() {
/* 348 */     return this.root;
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\xmlpull\v1\builder\impl\XmlDocumentImpl.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */