/*     */ package org.xmlpull.v1.builder.adapter;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ import org.xmlpull.v1.builder.Iterable;
/*     */ import org.xmlpull.v1.builder.XmlAttribute;
/*     */ import org.xmlpull.v1.builder.XmlBuilderException;
/*     */ import org.xmlpull.v1.builder.XmlContainer;
/*     */ import org.xmlpull.v1.builder.XmlDocument;
/*     */ import org.xmlpull.v1.builder.XmlElement;
/*     */ import org.xmlpull.v1.builder.XmlNamespace;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class XmlElementAdapter
/*     */   implements XmlElement
/*     */ {
/*     */   private XmlElementAdapter topAdapter;
/*     */   private XmlElement target;
/*     */   private XmlContainer parent;
/*     */   
/*     */   public XmlElementAdapter(XmlElement target) {
/*  23 */     setTarget(target);
/*     */   }
/*     */   
/*     */   private void setTarget(XmlElement target) {
/*  27 */     this.target = target;
/*  28 */     if (target.getParent() != null) {
/*     */ 
/*     */ 
/*     */       
/*  32 */       this.parent = target.getParent();
/*  33 */       if (this.parent instanceof XmlDocument) {
/*  34 */         XmlDocument doc = (XmlDocument)this.parent;
/*  35 */         doc.setDocumentElement(this);
/*  36 */       }  if (this.parent instanceof XmlElement) {
/*  37 */         XmlElement parentEl = (XmlElement)this.parent;
/*  38 */         parentEl.replaceChild(this, target);
/*     */       } 
/*     */     } 
/*     */     
/*  42 */     Iterator iter = target.children();
/*  43 */     while (iter.hasNext()) {
/*     */       
/*  45 */       Object child = iter.next();
/*  46 */       fixImportedChildParent(child);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object clone() throws CloneNotSupportedException {
/*  54 */     XmlElementAdapter ela = (XmlElementAdapter)super.clone();
/*  55 */     ela.parent = null;
/*  56 */     ela.target = (XmlElement)this.target.clone();
/*  57 */     return ela;
/*     */   }
/*     */   public XmlElement getTarget() {
/*  60 */     return this.target;
/*     */   }
/*     */   public XmlElementAdapter getTopAdapter() {
/*  63 */     return (this.topAdapter != null) ? this.topAdapter : this;
/*     */   }
/*     */   
/*     */   public void setTopAdapter(XmlElementAdapter adapter) {
/*  67 */     this.topAdapter = adapter;
/*  68 */     if (this.target instanceof XmlElementAdapter) {
/*  69 */       ((XmlElementAdapter)this.target).setTopAdapter(adapter);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static XmlElementAdapter castOrWrap(XmlElement el, Class adapterClass) {
/*  75 */     if (el == null) {
/*  76 */       throw new IllegalArgumentException("null element can not be wrapped");
/*     */     }
/*  78 */     if (!XmlElementAdapter.class.isAssignableFrom(adapterClass)) {
/*  79 */       throw new IllegalArgumentException("class for cast/wrap must extend " + XmlElementAdapter.class);
/*     */     }
/*  81 */     if (el instanceof XmlElementAdapter) {
/*  82 */       XmlElementAdapter currentAdap = (XmlElementAdapter)el;
/*  83 */       Class currentAdapClass = currentAdap.getClass();
/*  84 */       if (adapterClass.isAssignableFrom(currentAdapClass)) {
/*  85 */         return currentAdap;
/*     */       }
/*     */       
/*  88 */       XmlElementAdapter topAdapter = currentAdap = currentAdap.getTopAdapter();
/*  89 */       while (currentAdap.topAdapter != null) {
/*  90 */         currentAdapClass = currentAdap.getClass();
/*  91 */         if (currentAdapClass.isAssignableFrom(adapterClass)) {
/*  92 */           return currentAdap;
/*     */         }
/*  94 */         if (currentAdap.target instanceof XmlElementAdapter) {
/*  95 */           currentAdap = (XmlElementAdapter)currentAdap.target;
/*     */         }
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/*     */       try {
/* 102 */         currentAdap.topAdapter = adapterClass.getConstructor(new Class[] { XmlElement.class }).newInstance(new Object[] { topAdapter });
/*     */ 
/*     */         
/* 105 */         currentAdap.topAdapter.setTopAdapter(currentAdap.topAdapter);
/* 106 */         return currentAdap.topAdapter;
/* 107 */       } catch (Exception e) {
/* 108 */         throw new XmlBuilderException("could not create wrapper of " + adapterClass, e);
/*     */       } 
/*     */     } 
/*     */     
/*     */     try {
/* 113 */       XmlElementAdapter t = adapterClass.getConstructor(new Class[] { XmlElement.class }).newInstance(new Object[] { el });
/*     */ 
/*     */       
/* 116 */       return t;
/* 117 */     } catch (Exception e) {
/* 118 */       throw new XmlBuilderException("could not wrap element " + el, e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void fixImportedChildParent(Object child) {
/* 124 */     if (child instanceof XmlElement) {
/* 125 */       XmlElement childEl = (XmlElement)child;
/* 126 */       XmlContainer childElParent = childEl.getParent();
/* 127 */       if (childElParent == this.target) {
/* 128 */         childEl.setParent((XmlContainer)this);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private XmlElement fixElementParent(XmlElement el) {
/* 134 */     el.setParent((XmlContainer)this);
/* 135 */     return el;
/*     */   }
/*     */   public XmlContainer getRoot() {
/*     */     XmlElementAdapter xmlElementAdapter;
/* 139 */     XmlContainer root = this.target.getRoot();
/* 140 */     if (root == this.target) {
/* 141 */       xmlElementAdapter = this;
/*     */     }
/* 143 */     return (XmlContainer)xmlElementAdapter;
/*     */   }
/*     */   
/*     */   public XmlContainer getParent() {
/* 147 */     return this.parent;
/*     */   }
/*     */   
/*     */   public void setParent(XmlContainer parent) {
/* 151 */     this.parent = parent;
/*     */   }
/*     */ 
/*     */   
/*     */   public XmlNamespace newNamespace(String prefix, String namespaceName) {
/* 156 */     return this.target.newNamespace(prefix, namespaceName);
/*     */   }
/*     */   
/*     */   public XmlAttribute attribute(String attributeName) {
/* 160 */     return this.target.attribute(attributeName);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public XmlAttribute attribute(XmlNamespace attributeNamespaceName, String attributeName) {
/* 166 */     return this.target.attribute(attributeNamespaceName, attributeName);
/*     */   }
/*     */ 
/*     */   
/*     */   public XmlAttribute findAttribute(String attributeNamespaceName, String attributeName) {
/* 171 */     return this.target.findAttribute(attributeNamespaceName, attributeName);
/*     */   }
/*     */   
/*     */   public Iterator attributes() {
/* 175 */     return this.target.attributes();
/*     */   }
/*     */   
/*     */   public void removeAllChildren() {
/* 179 */     this.target.removeAllChildren();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public XmlAttribute addAttribute(String attributeType, String attributePrefix, String attributeNamespace, String attributeName, String attributeValue, boolean specified) {
/* 189 */     return this.target.addAttribute(attributeType, attributePrefix, attributeNamespace, attributeName, attributeValue, specified);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getAttributeValue(String attributeNamespaceName, String attributeName) {
/* 200 */     return this.target.getAttributeValue(attributeNamespaceName, attributeName);
/*     */   }
/*     */ 
/*     */   
/*     */   public XmlAttribute addAttribute(XmlNamespace namespace, String name, String value) {
/* 205 */     return this.target.addAttribute(namespace, name, value);
/*     */   }
/*     */   
/*     */   public String getNamespaceName() {
/* 209 */     return this.target.getNamespaceName();
/*     */   }
/*     */   
/*     */   public void ensureChildrenCapacity(int minCapacity) {
/* 213 */     this.target.ensureChildrenCapacity(minCapacity);
/*     */   }
/*     */   
/*     */   public Iterator namespaces() {
/* 217 */     return this.target.namespaces();
/*     */   }
/*     */   
/*     */   public void removeAllAttributes() {
/* 221 */     this.target.removeAllAttributes();
/*     */   }
/*     */   
/*     */   public XmlNamespace getNamespace() {
/* 225 */     return this.target.getNamespace();
/*     */   }
/*     */   
/*     */   public String getBaseUri() {
/* 229 */     return this.target.getBaseUri();
/*     */   }
/*     */   
/*     */   public void removeAttribute(XmlAttribute attr) {
/* 233 */     this.target.removeAttribute(attr);
/*     */   }
/*     */   
/*     */   public XmlNamespace declareNamespace(String prefix, String namespaceName) {
/* 237 */     return this.target.declareNamespace(prefix, namespaceName);
/*     */   }
/*     */   
/*     */   public void removeAllNamespaceDeclarations() {
/* 241 */     this.target.removeAllNamespaceDeclarations();
/*     */   }
/*     */   
/*     */   public boolean hasAttributes() {
/* 245 */     return this.target.hasAttributes();
/*     */   }
/*     */   
/*     */   public XmlAttribute addAttribute(String type, XmlNamespace namespace, String name, String value, boolean specified) {
/* 249 */     return this.target.addAttribute(type, namespace, name, value, specified);
/*     */   }
/*     */   
/*     */   public XmlNamespace declareNamespace(XmlNamespace namespace) {
/* 253 */     return this.target.declareNamespace(namespace);
/*     */   }
/*     */   
/*     */   public XmlAttribute addAttribute(String name, String value) {
/* 257 */     return this.target.addAttribute(name, value);
/*     */   }
/*     */   
/*     */   public boolean hasNamespaceDeclarations() {
/* 261 */     return this.target.hasNamespaceDeclarations();
/*     */   }
/*     */   
/*     */   public XmlNamespace lookupNamespaceByName(String namespaceName) {
/* 265 */     XmlNamespace ns = this.target.lookupNamespaceByName(namespaceName);
/* 266 */     if (ns == null) {
/* 267 */       XmlContainer p = getParent();
/* 268 */       if (p instanceof XmlElement) {
/* 269 */         XmlElement e = (XmlElement)p;
/* 270 */         return e.lookupNamespaceByName(namespaceName);
/*     */       } 
/*     */     } 
/* 273 */     return ns;
/*     */   }
/*     */   
/*     */   public XmlNamespace lookupNamespaceByPrefix(String namespacePrefix) {
/* 277 */     XmlNamespace ns = this.target.lookupNamespaceByPrefix(namespacePrefix);
/* 278 */     if (ns == null) {
/* 279 */       XmlContainer p = getParent();
/* 280 */       if (p instanceof XmlElement) {
/* 281 */         XmlElement e = (XmlElement)p;
/* 282 */         return e.lookupNamespaceByPrefix(namespacePrefix);
/*     */       } 
/*     */     } 
/* 285 */     return ns;
/*     */   }
/*     */ 
/*     */   
/*     */   public XmlNamespace newNamespace(String namespaceName) {
/* 290 */     return this.target.newNamespace(namespaceName);
/*     */   }
/*     */   
/*     */   public void setBaseUri(String baseUri) {
/* 294 */     this.target.setBaseUri(baseUri);
/*     */   }
/*     */   
/*     */   public void setNamespace(XmlNamespace namespace) {
/* 298 */     this.target.setNamespace(namespace);
/*     */   }
/*     */   
/*     */   public void ensureNamespaceDeclarationsCapacity(int minCapacity) {
/* 302 */     this.target.ensureNamespaceDeclarationsCapacity(minCapacity);
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
/*     */   public String getName() {
/* 314 */     return this.target.getName();
/*     */   }
/*     */   
/*     */   public void setName(String name) {
/* 318 */     this.target.setName(name);
/*     */   }
/*     */   
/*     */   public XmlAttribute addAttribute(String type, XmlNamespace namespace, String name, String value) {
/* 322 */     return this.target.addAttribute(type, namespace, name, value);
/*     */   }
/*     */   
/*     */   public void ensureAttributeCapacity(int minCapacity) {
/* 326 */     this.target.ensureAttributeCapacity(minCapacity);
/*     */   }
/*     */   
/*     */   public XmlAttribute addAttribute(XmlAttribute attributeValueToAdd) {
/* 330 */     return this.target.addAttribute(attributeValueToAdd);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public XmlElement element(int position) {
/* 336 */     return this.target.element(position);
/*     */   }
/*     */   
/*     */   public XmlElement requiredElement(XmlNamespace n, String name) {
/* 340 */     return this.target.requiredElement(n, name);
/*     */   }
/*     */   
/*     */   public XmlElement element(XmlNamespace n, String name) {
/* 344 */     return this.target.element(n, name);
/*     */   }
/*     */   
/*     */   public XmlElement element(XmlNamespace n, String name, boolean create) {
/* 348 */     return this.target.element(n, name, create);
/*     */   }
/*     */   
/*     */   public Iterable elements(XmlNamespace n, String name) {
/* 352 */     return this.target.elements(n, name);
/*     */   }
/*     */   
/*     */   public XmlElement findElementByName(String name, XmlElement elementToStartLooking) {
/* 356 */     return this.target.findElementByName(name, elementToStartLooking);
/*     */   }
/*     */   
/*     */   public XmlElement newElement(XmlNamespace namespace, String name) {
/* 360 */     return this.target.newElement(namespace, name);
/*     */   }
/*     */ 
/*     */   
/*     */   public XmlElement addElement(XmlElement child) {
/* 365 */     return fixElementParent(this.target.addElement(child));
/*     */   }
/*     */   
/*     */   public XmlElement addElement(int pos, XmlElement child) {
/* 369 */     return fixElementParent(this.target.addElement(pos, child));
/*     */   }
/*     */ 
/*     */   
/*     */   public XmlElement addElement(String name) {
/* 374 */     return fixElementParent(this.target.addElement(name));
/*     */   }
/*     */   
/*     */   public XmlElement findElementByName(String namespaceName, String name) {
/* 378 */     return this.target.findElementByName(namespaceName, name);
/*     */   }
/*     */   
/*     */   public void addChild(Object child) {
/* 382 */     this.target.addChild(child);
/* 383 */     fixImportedChildParent(child);
/*     */   }
/*     */   
/*     */   public void insertChild(int pos, Object childToInsert) {
/* 387 */     this.target.insertChild(pos, childToInsert);
/* 388 */     fixImportedChildParent(childToInsert);
/*     */   }
/*     */   
/*     */   public XmlElement findElementByName(String name) {
/* 392 */     return this.target.findElementByName(name);
/*     */   }
/*     */ 
/*     */   
/*     */   public XmlElement findElementByName(String namespaceName, String name, XmlElement elementToStartLooking) {
/* 397 */     return this.target.findElementByName(namespaceName, name, elementToStartLooking);
/*     */   }
/*     */   
/*     */   public void removeChild(Object child) {
/* 401 */     this.target.removeChild(child);
/*     */   }
/*     */   
/*     */   public Iterator children() {
/* 405 */     return this.target.children();
/*     */   }
/*     */   
/*     */   public Iterable requiredElementContent() {
/* 409 */     return this.target.requiredElementContent();
/*     */   }
/*     */   
/*     */   public String requiredTextContent() {
/* 413 */     return this.target.requiredTextContent();
/*     */   }
/*     */   
/*     */   public boolean hasChild(Object child) {
/* 417 */     return this.target.hasChild(child);
/*     */   }
/*     */   
/*     */   public XmlElement newElement(String namespaceName, String name) {
/* 421 */     return this.target.newElement(namespaceName, name);
/*     */   }
/*     */   
/*     */   public XmlElement addElement(XmlNamespace namespace, String name) {
/* 425 */     return fixElementParent(this.target.addElement(namespace, name));
/*     */   }
/*     */   
/*     */   public boolean hasChildren() {
/* 429 */     return this.target.hasChildren();
/*     */   }
/*     */   
/*     */   public void addChild(int pos, Object child) {
/* 433 */     this.target.addChild(pos, child);
/* 434 */     fixImportedChildParent(child);
/*     */   }
/*     */   
/*     */   public void replaceChild(Object newChild, Object oldChild) {
/* 438 */     this.target.replaceChild(newChild, oldChild);
/* 439 */     fixImportedChildParent(newChild);
/*     */   }
/*     */   
/*     */   public XmlElement newElement(String name) {
/* 443 */     return this.target.newElement(name);
/*     */   }
/*     */   
/*     */   public void replaceChildrenWithText(String textContent) {
/* 447 */     this.target.replaceChildrenWithText(textContent);
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\xmlpull\v1\builder\adapter\XmlElementAdapter.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */