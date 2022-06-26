/*     */ package org.xmlpull.v1.builder.impl;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import org.xmlpull.v1.builder.Iterable;
/*     */ import org.xmlpull.v1.builder.XmlAttribute;
/*     */ import org.xmlpull.v1.builder.XmlBuilderException;
/*     */ import org.xmlpull.v1.builder.XmlCharacters;
/*     */ import org.xmlpull.v1.builder.XmlContained;
/*     */ import org.xmlpull.v1.builder.XmlContainer;
/*     */ import org.xmlpull.v1.builder.XmlDocument;
/*     */ import org.xmlpull.v1.builder.XmlElement;
/*     */ import org.xmlpull.v1.builder.XmlNamespace;
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
/*     */ public class XmlElementImpl
/*     */   implements XmlElement
/*     */ {
/*     */   private XmlContainer parent;
/*     */   private XmlNamespace namespace;
/*     */   private String name;
/*     */   private List attrs;
/*     */   private List nsList;
/*     */   private List children;
/*     */   
/*     */   public Object clone() throws CloneNotSupportedException {
/*  36 */     XmlElementImpl cloned = (XmlElementImpl)super.clone();
/*  37 */     cloned.parent = null;
/*     */     
/*  39 */     cloned.attrs = cloneList(cloned, this.attrs);
/*  40 */     cloned.nsList = cloneList(cloned, this.nsList);
/*  41 */     cloned.children = cloneList(cloned, this.children);
/*     */ 
/*     */     
/*  44 */     if (cloned.children != null) {
/*  45 */       for (int i = 0; i < cloned.children.size(); i++) {
/*     */         
/*  47 */         Object member = cloned.children.get(i);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  55 */         if (member instanceof XmlContained) {
/*  56 */           XmlContained contained = (XmlContained)member;
/*  57 */           if (contained.getParent() == this) {
/*  58 */             contained.setParent(null);
/*  59 */             contained.setParent((XmlContainer)cloned);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*  66 */     return cloned;
/*     */   }
/*     */   
/*     */   private List cloneList(XmlElementImpl cloned, List list) throws CloneNotSupportedException {
/*  70 */     if (list == null) {
/*  71 */       return null;
/*     */     }
/*  73 */     List newList = new ArrayList(list.size());
/*     */     
/*  75 */     for (int i = 0; i < list.size(); i++) {
/*     */       
/*  77 */       Object newMember, member = list.get(i);
/*     */       
/*  79 */       if (member instanceof XmlNamespace || member instanceof String) {
/*     */         
/*  81 */         newMember = member;
/*  82 */       } else if (member instanceof XmlElement) {
/*     */ 
/*     */         
/*  85 */         XmlElement el = (XmlElement)member;
/*  86 */         newMember = el.clone();
/*  87 */       } else if (member instanceof XmlAttribute) {
/*  88 */         XmlAttribute attr = (XmlAttribute)member;
/*  89 */         newMember = new XmlAttributeImpl(cloned, attr.getType(), attr.getNamespace(), attr.getName(), attr.getValue(), attr.isSpecified());
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       }
/*  95 */       else if (member instanceof Cloneable) {
/*     */ 
/*     */         
/*     */         try {
/*  99 */           newMember = member.getClass().getMethod("clone", null).invoke(member, null);
/* 100 */         } catch (Exception e) {
/* 101 */           throw new CloneNotSupportedException("failed to call clone() on  " + member + e);
/*     */         } 
/*     */       } else {
/* 104 */         throw new CloneNotSupportedException();
/*     */       } 
/* 106 */       newList.add(newMember);
/*     */     } 
/* 108 */     return newList;
/*     */   }
/*     */ 
/*     */   
/*     */   XmlElementImpl(String name) {
/* 113 */     this.name = name;
/*     */   }
/*     */   
/*     */   XmlElementImpl(XmlNamespace namespace, String name) {
/* 117 */     this.namespace = namespace;
/* 118 */     this.name = name;
/*     */   }
/*     */   
/*     */   XmlElementImpl(String namespaceName, String name) {
/* 122 */     if (namespaceName != null) {
/* 123 */       this.namespace = new XmlNamespaceImpl(null, namespaceName);
/*     */     }
/* 125 */     this.name = name;
/*     */   }
/*     */   
/*     */   public XmlContainer getRoot() {
/*     */     XmlContainer xmlContainer;
/* 130 */     XmlElementImpl xmlElementImpl = this;
/*     */     
/* 132 */     while (xmlElementImpl instanceof XmlElement) {
/*     */ 
/*     */       
/* 135 */       XmlElement el = xmlElementImpl;
/* 136 */       if (el.getParent() != null) {
/* 137 */         xmlContainer = el.getParent();
/*     */         continue;
/*     */       } 
/*     */       break;
/*     */     } 
/* 142 */     return xmlContainer;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public XmlContainer getParent() {
/* 148 */     return this.parent;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setParent(XmlContainer parent) {
/* 153 */     if (parent != null)
/*     */     {
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
/* 171 */       if (parent instanceof XmlDocument) {
/* 172 */         XmlDocument doc = (XmlDocument)parent;
/* 173 */         if (doc.getDocumentElement() != this) {
/* 174 */           throw new XmlBuilderException("this element must be root document element to have document set as parent but already different element is set as root document element");
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 180 */     this.parent = parent;
/*     */   }
/*     */   
/*     */   public XmlNamespace getNamespace() {
/* 184 */     return this.namespace;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getNamespaceName() {
/* 189 */     return (this.namespace != null) ? this.namespace.getNamespaceName() : null;
/*     */   }
/*     */   
/*     */   public void setNamespace(XmlNamespace namespace) {
/* 193 */     this.namespace = namespace;
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
/* 205 */     return this.name;
/*     */   }
/*     */   
/*     */   public void setName(String name) {
/* 209 */     this.name = name;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 214 */     return "name[" + this.name + "]" + ((this.namespace != null) ? (" namespace[" + this.namespace.getNamespaceName() + "]") : "");
/*     */   }
/*     */ 
/*     */   
/*     */   public String getBaseUri() {
/* 219 */     throw new XmlBuilderException("not implemented");
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBaseUri(String baseUri) {
/* 224 */     throw new XmlBuilderException("not implemented");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Iterator attributes() {
/* 231 */     if (this.attrs == null) {
/* 232 */       return EMPTY_ITERATOR;
/*     */     }
/* 234 */     return this.attrs.iterator();
/*     */   }
/*     */   
/*     */   public XmlAttribute addAttribute(XmlAttribute attributeValueToAdd) {
/* 238 */     if (this.attrs == null) ensureAttributeCapacity(5);
/*     */     
/* 240 */     this.attrs.add(attributeValueToAdd);
/* 241 */     return attributeValueToAdd;
/*     */   }
/*     */   
/*     */   public XmlAttribute addAttribute(XmlNamespace namespace, String name, String value) {
/* 245 */     return addAttribute("CDATA", namespace, name, value, false);
/*     */   }
/*     */   
/*     */   public XmlAttribute addAttribute(String name, String value) {
/* 249 */     return addAttribute("CDATA", null, name, value, false);
/*     */   }
/*     */   
/*     */   public XmlAttribute addAttribute(String attributeType, XmlNamespace namespace, String name, String value) {
/* 253 */     return addAttribute(attributeType, namespace, name, value, false);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public XmlAttribute addAttribute(String attributeType, XmlNamespace namespace, String name, String value, boolean specified) {
/* 259 */     XmlAttribute a = new XmlAttributeImpl(this, attributeType, namespace, name, value, specified);
/* 260 */     return addAttribute(a);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public XmlAttribute addAttribute(String attributeType, String attributePrefix, String attributeNamespace, String attributeName, String attributeValue, boolean specified) {
/* 270 */     XmlNamespace n = newNamespace(attributePrefix, attributeNamespace);
/* 271 */     return addAttribute(attributeType, n, attributeName, attributeValue, specified);
/*     */   }
/*     */   
/*     */   public void ensureAttributeCapacity(int minCapacity) {
/* 275 */     if (this.attrs == null) {
/* 276 */       this.attrs = new ArrayList(minCapacity);
/*     */     } else {
/* 278 */       ((ArrayList)this.attrs).ensureCapacity(minCapacity);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getAttributeValue(String attributeNamespaceName, String attributeName) {
/* 285 */     XmlAttribute xat = findAttribute(attributeNamespaceName, attributeName);
/* 286 */     if (xat != null) {
/* 287 */       return xat.getValue();
/*     */     }
/* 289 */     return null;
/*     */   }
/*     */   
/*     */   public boolean hasAttributes() {
/* 293 */     return (this.attrs != null && this.attrs.size() > 0);
/*     */   }
/*     */   
/*     */   public XmlAttribute attribute(String attributeName) {
/* 297 */     return attribute(null, attributeName);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public XmlAttribute attribute(XmlNamespace attributeNamespace, String attributeName) {
/* 303 */     return findAttribute((attributeNamespace != null) ? attributeNamespace.getNamespaceName() : null, attributeName);
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
/*     */   public XmlAttribute findAttribute(String attributeNamespace, String attributeName) {
/* 315 */     if (attributeName == null) {
/* 316 */       throw new IllegalArgumentException("attribute name ca not ber null");
/*     */     }
/*     */ 
/*     */     
/* 320 */     if (this.attrs == null) {
/* 321 */       return null;
/*     */     }
/*     */     
/* 324 */     int length = this.attrs.size();
/*     */     
/* 326 */     for (int i = 0; i < length; i++) {
/*     */       
/* 328 */       XmlAttribute a = this.attrs.get(i);
/* 329 */       String aName = a.getName();
/* 330 */       if (aName == attributeName || attributeName.equals(aName))
/*     */       {
/*     */         
/* 333 */         if (attributeNamespace != null) {
/* 334 */           String aNamespace = a.getNamespaceName();
/* 335 */           if (attributeNamespace.equals(aNamespace))
/* 336 */             return a; 
/* 337 */           if (attributeNamespace == "" && aNamespace == null) {
/* 338 */             return a;
/*     */ 
/*     */ 
/*     */           
/*     */           }
/*     */ 
/*     */         
/*     */         }
/*     */         else {
/*     */ 
/*     */ 
/*     */           
/* 350 */           if (a.getNamespace() == null)
/*     */           {
/* 352 */             return a;
/*     */           }
/* 354 */           if (a.getNamespace().getNamespaceName() == "") {
/* 355 */             return a;
/*     */           }
/*     */         } 
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 362 */     return null;
/*     */   }
/*     */   
/*     */   public void removeAllAttributes() {
/* 366 */     this.attrs = null;
/*     */   }
/*     */   
/*     */   public void removeAttribute(XmlAttribute attr) {
/* 370 */     if (this.attrs == null) {
/* 371 */       throw new XmlBuilderException("this element has no attributes to remove");
/*     */     }
/* 373 */     for (int i = 0; i < this.attrs.size(); i++) {
/*     */       
/* 375 */       if (this.attrs.get(i).equals(attr)) {
/* 376 */         this.attrs.remove(i);
/*     */         break;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public XmlNamespace declareNamespace(String prefix, String namespaceName) {
/* 386 */     if (prefix == null) {
/* 387 */       throw new XmlBuilderException("namespace added to element must have not null prefix");
/*     */     }
/* 389 */     XmlNamespace n = newNamespace(prefix, namespaceName);
/* 390 */     return declareNamespace(n);
/*     */   }
/*     */   
/*     */   public XmlNamespace declareNamespace(XmlNamespace n) {
/* 394 */     if (n.getPrefix() == null) {
/* 395 */       throw new XmlBuilderException("namespace added to element must have not null prefix");
/*     */     }
/* 397 */     if (this.nsList == null) ensureNamespaceDeclarationsCapacity(5);
/*     */     
/* 399 */     this.nsList.add(n);
/* 400 */     return n;
/*     */   }
/*     */   
/*     */   public boolean hasNamespaceDeclarations() {
/* 404 */     return (this.nsList != null && this.nsList.size() > 0);
/*     */   }
/*     */   
/*     */   public XmlNamespace lookupNamespaceByPrefix(String namespacePrefix) {
/* 408 */     if (namespacePrefix == null) {
/* 409 */       throw new IllegalArgumentException("namespace prefix can not be null");
/*     */     }
/* 411 */     if (hasNamespaceDeclarations()) {
/* 412 */       int length = this.nsList.size();
/* 413 */       for (int i = 0; i < length; i++) {
/*     */         
/* 415 */         XmlNamespace n = this.nsList.get(i);
/* 416 */         if (namespacePrefix.equals(n.getPrefix())) {
/* 417 */           return n;
/*     */         }
/*     */       } 
/*     */     } 
/* 421 */     if (this.parent != null && this.parent instanceof XmlElement) {
/* 422 */       return ((XmlElement)this.parent).lookupNamespaceByPrefix(namespacePrefix);
/*     */     }
/* 424 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public XmlNamespace lookupNamespaceByName(String namespaceName) {
/* 429 */     if (namespaceName == null) {
/* 430 */       throw new IllegalArgumentException("namespace name can not ber null");
/*     */     }
/* 432 */     if (hasNamespaceDeclarations()) {
/* 433 */       int length = this.nsList.size();
/* 434 */       for (int i = 0; i < length; i++) {
/*     */         
/* 436 */         XmlNamespace n = this.nsList.get(i);
/* 437 */         if (namespaceName.equals(n.getNamespaceName())) {
/* 438 */           return n;
/*     */         }
/*     */       } 
/*     */     } 
/* 442 */     if (this.parent != null && this.parent instanceof XmlElement) {
/* 443 */       return ((XmlElement)this.parent).lookupNamespaceByName(namespaceName);
/*     */     }
/* 445 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator namespaces() {
/* 450 */     if (this.nsList == null) {
/* 451 */       return EMPTY_ITERATOR;
/*     */     }
/* 453 */     return this.nsList.iterator();
/*     */   }
/*     */ 
/*     */   
/*     */   public XmlNamespace newNamespace(String namespaceName) {
/* 458 */     return newNamespace(null, namespaceName);
/*     */   }
/*     */   
/*     */   public XmlNamespace newNamespace(String prefix, String namespaceName) {
/* 462 */     return new XmlNamespaceImpl(prefix, namespaceName);
/*     */   }
/*     */ 
/*     */   
/*     */   public void ensureNamespaceDeclarationsCapacity(int minCapacity) {
/* 467 */     if (this.nsList == null) {
/* 468 */       this.nsList = new ArrayList(minCapacity);
/*     */     } else {
/* 470 */       ((ArrayList)this.nsList).ensureCapacity(minCapacity);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void removeAllNamespaceDeclarations() {
/* 475 */     this.nsList = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addChild(Object child) {
/* 482 */     if (child == null) throw new NullPointerException(); 
/* 483 */     if (this.children == null) ensureChildrenCapacity(1);
/*     */     
/* 485 */     this.children.add(child);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addChild(int index, Object child) {
/* 490 */     if (this.children == null) ensureChildrenCapacity(1);
/*     */     
/* 492 */     this.children.add(index, child);
/*     */   }
/*     */ 
/*     */   
/*     */   private void checkChildParent(Object child) {
/* 497 */     if (child instanceof XmlContainer) {
/* 498 */       if (child instanceof XmlElement) {
/* 499 */         XmlElement elChild = (XmlElement)child;
/* 500 */         XmlContainer childParent = elChild.getParent();
/* 501 */         if (childParent != null && 
/* 502 */           childParent != this.parent) {
/* 503 */           throw new XmlBuilderException("child must have no parent to be added to this node");
/*     */         
/*     */         }
/*     */       }
/* 507 */       else if (child instanceof XmlDocument) {
/* 508 */         throw new XmlBuilderException("docuemet can not be stored as element child");
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private void setChildParent(Object child) {
/* 514 */     if (child instanceof XmlElement) {
/* 515 */       XmlElement elChild = (XmlElement)child;
/* 516 */       elChild.setParent((XmlContainer)this);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public XmlElement addElement(XmlElement element) {
/* 522 */     checkChildParent(element);
/* 523 */     addChild(element);
/* 524 */     setChildParent(element);
/* 525 */     return element;
/*     */   }
/*     */ 
/*     */   
/*     */   public XmlElement addElement(int pos, XmlElement element) {
/* 530 */     checkChildParent(element);
/* 531 */     addChild(pos, element);
/* 532 */     setChildParent(element);
/* 533 */     return element;
/*     */   }
/*     */   
/*     */   public XmlElement addElement(XmlNamespace namespace, String name) {
/* 537 */     XmlElement el = newElement(namespace, name);
/* 538 */     addChild(el);
/* 539 */     setChildParent(el);
/* 540 */     return el;
/*     */   }
/*     */   
/*     */   public XmlElement addElement(String name) {
/* 544 */     return addElement((XmlNamespace)null, name);
/*     */   }
/*     */   
/*     */   public Iterator children() {
/* 548 */     if (this.children == null) {
/* 549 */       return EMPTY_ITERATOR;
/*     */     }
/* 551 */     return this.children.iterator();
/*     */   }
/*     */   
/*     */   public Iterable requiredElementContent() {
/* 555 */     if (this.children == null) {
/* 556 */       return EMPTY_ITERABLE;
/*     */     }
/* 558 */     return new Iterable(this) { private final XmlElementImpl this$0;
/*     */         public Iterator iterator() {
/* 560 */           return new XmlElementImpl.RequiredElementContentIterator(this.this$0.children.iterator());
/*     */         } }
/*     */       ;
/*     */   }
/*     */   
/*     */   public String requiredTextContent() {
/* 566 */     if (this.children == null)
/*     */     {
/*     */       
/* 569 */       return "";
/*     */     }
/* 571 */     if (this.children.size() == 0)
/* 572 */       return ""; 
/* 573 */     if (this.children.size() == 1) {
/* 574 */       Object child = this.children.get(0);
/* 575 */       if (child instanceof String)
/* 576 */         return child.toString(); 
/* 577 */       if (child instanceof XmlCharacters) {
/* 578 */         return ((XmlCharacters)child).getText();
/*     */       }
/* 580 */       throw new XmlBuilderException("expected text content and not " + ((child != null) ? child.getClass() : null) + " with '" + child + "'");
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 585 */     Iterator i = children();
/* 586 */     StringBuffer buf = new StringBuffer();
/* 587 */     while (i.hasNext()) {
/* 588 */       Object child = i.next();
/* 589 */       if (child instanceof String) {
/* 590 */         buf.append(child.toString()); continue;
/* 591 */       }  if (child instanceof XmlCharacters) {
/* 592 */         buf.append(((XmlCharacters)child).getText()); continue;
/*     */       } 
/* 594 */       throw new XmlBuilderException("expected text content and not " + child.getClass() + " with '" + child + "'");
/*     */     } 
/*     */ 
/*     */     
/* 598 */     return buf.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public void ensureChildrenCapacity(int minCapacity) {
/* 603 */     if (this.children == null) {
/* 604 */       this.children = new ArrayList(minCapacity);
/*     */     } else {
/* 606 */       ((ArrayList)this.children).ensureCapacity(minCapacity);
/*     */     } 
/*     */   }
/*     */   
/*     */   public XmlElement element(int position) {
/* 611 */     if (this.children == null) return null; 
/* 612 */     int length = this.children.size();
/* 613 */     int count = 0;
/* 614 */     if (position >= 0 && position < length + 1) {
/* 615 */       int pos = 0;
/* 616 */       while (pos < length) {
/* 617 */         Object child = this.children.get(pos);
/*     */         
/* 619 */         count++;
/* 620 */         if (child instanceof XmlElement && count == position) {
/* 621 */           return (XmlElement)child;
/*     */         }
/*     */         
/* 624 */         pos++;
/*     */       } 
/*     */     } else {
/* 627 */       throw new IndexOutOfBoundsException("position " + position + " bigger or equal to " + length + " children");
/*     */     } 
/*     */     
/* 630 */     throw new IndexOutOfBoundsException("position " + position + " too big as only " + count + " element(s) available");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public XmlElement requiredElement(XmlNamespace n, String name) throws XmlBuilderException {
/* 636 */     XmlElement el = element(n, name);
/* 637 */     if (el == null) {
/* 638 */       throw new XmlBuilderException("could not find element with name " + name + " in namespace " + ((n != null) ? n.getNamespaceName() : null));
/*     */     }
/*     */     
/* 641 */     return el;
/*     */   }
/*     */   
/*     */   public XmlElement element(XmlNamespace n, String name) {
/* 645 */     return element(n, name, false);
/*     */   }
/*     */   public XmlElement element(XmlNamespace n, String name, boolean create) {
/* 648 */     XmlElement e = (n != null) ? findElementByName(n.getNamespaceName(), name) : findElementByName(name);
/* 649 */     if (e != null) {
/* 650 */       return e;
/*     */     }
/*     */     
/* 653 */     if (create) {
/* 654 */       return addElement(n, name);
/*     */     }
/* 656 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterable elements(XmlNamespace n, String name) {
/* 661 */     return new Iterable(this, n, name) { private final XmlNamespace val$n;
/*     */         public Iterator iterator() {
/* 663 */           return new XmlElementImpl.ElementsSimpleIterator(this.this$0, this.val$n, this.val$name, this.this$0.children());
/*     */         }
/*     */         private final String val$name; private final XmlElementImpl this$0; }
/*     */       ;
/*     */   }
/*     */   public XmlElement findElementByName(String name) {
/* 669 */     if (this.children == null) return null; 
/* 670 */     int length = this.children.size();
/* 671 */     for (int i = 0; i < length; i++) {
/*     */       
/* 673 */       Object child = this.children.get(i);
/* 674 */       if (child instanceof XmlElement) {
/* 675 */         XmlElement childEl = (XmlElement)child;
/* 676 */         if (name.equals(childEl.getName())) {
/* 677 */           return childEl;
/*     */         }
/*     */       } 
/*     */     } 
/* 681 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public XmlElement findElementByName(String namespaceName, String name, XmlElement elementToStartLooking) {
/* 687 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public XmlElement findElementByName(String name, XmlElement elementToStartLooking) {
/* 694 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public XmlElement findElementByName(String namespaceName, String name) {
/* 698 */     if (this.children == null) return null; 
/* 699 */     int length = this.children.size();
/* 700 */     for (int i = 0; i < length; i++) {
/*     */       
/* 702 */       Object child = this.children.get(i);
/* 703 */       if (child instanceof XmlElement) {
/* 704 */         XmlElement childEl = (XmlElement)child;
/* 705 */         XmlNamespace namespace = childEl.getNamespace();
/* 706 */         if (namespace != null) {
/* 707 */           if (name.equals(childEl.getName()) && namespaceName.equals(namespace.getNamespaceName())) {
/* 708 */             return childEl;
/*     */           }
/*     */         }
/* 711 */         else if (name.equals(childEl.getName()) && namespaceName == null) {
/* 712 */           return childEl;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 718 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasChild(Object child) {
/* 723 */     if (this.children == null) {
/* 724 */       return false;
/*     */     }
/* 726 */     for (int i = 0; i < this.children.size(); i++) {
/*     */       
/* 728 */       if (this.children.get(i) == child) {
/* 729 */         return true;
/*     */       }
/*     */     } 
/* 732 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasChildren() {
/* 737 */     return (this.children != null && this.children.size() > 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public void insertChild(int pos, Object childToInsert) {
/* 742 */     if (this.children == null) ensureChildrenCapacity(1); 
/* 743 */     this.children.add(pos, childToInsert);
/*     */   }
/*     */   
/*     */   public XmlElement newElement(String name) {
/* 747 */     return newElement((XmlNamespace)null, name);
/*     */   }
/*     */   
/*     */   public XmlElement newElement(String namespace, String name) {
/* 751 */     return new XmlElementImpl(namespace, name);
/*     */   }
/*     */   
/*     */   public XmlElement newElement(XmlNamespace namespace, String name) {
/* 755 */     return new XmlElementImpl(namespace, name);
/*     */   }
/*     */   
/*     */   public void replaceChild(Object newChild, Object oldChild) {
/* 759 */     if (newChild == null) {
/* 760 */       throw new IllegalArgumentException("new child to replace can not be null");
/*     */     }
/* 762 */     if (oldChild == null) {
/* 763 */       throw new IllegalArgumentException("old child to replace can not be null");
/*     */     }
/* 765 */     if (!hasChildren()) {
/* 766 */       throw new XmlBuilderException("no children available for replacement");
/*     */     }
/* 768 */     int pos = this.children.indexOf(oldChild);
/* 769 */     if (pos == -1) {
/* 770 */       throw new XmlBuilderException("could not find child to replace");
/*     */     }
/* 772 */     this.children.set(pos, newChild);
/*     */   }
/*     */   
/*     */   public void removeAllChildren() {
/* 776 */     this.children = null;
/*     */   }
/*     */   
/*     */   public void removeChild(Object child) {
/* 780 */     if (child == null) {
/* 781 */       throw new IllegalArgumentException("child to remove can not be null");
/*     */     }
/* 783 */     if (!hasChildren()) {
/* 784 */       throw new XmlBuilderException("no children to remove");
/*     */     }
/* 786 */     int pos = this.children.indexOf(child);
/* 787 */     if (pos != -1) {
/* 788 */       this.children.remove(pos);
/*     */     }
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
/*     */   public void replaceChildrenWithText(String textContent) {
/* 801 */     removeAllChildren();
/* 802 */     addChild(textContent);
/*     */   }
/*     */ 
/*     */   
/*     */   private static final boolean isWhiteSpace(String txt) {
/* 807 */     for (int i = 0; i < txt.length(); i++) {
/*     */       
/* 809 */       if (txt.charAt(i) != ' ' && txt.charAt(i) != '\n' && txt.charAt(i) != '\t' && txt.charAt(i) != '\r')
/*     */       {
/*     */ 
/*     */ 
/*     */         
/* 814 */         return false;
/*     */       }
/*     */     } 
/*     */     
/* 818 */     return true;
/*     */   }
/*     */   
/*     */   private class ElementsSimpleIterator
/*     */     implements Iterator {
/*     */     private Iterator children;
/*     */     private XmlElement currentEl;
/*     */     private XmlNamespace n;
/*     */     private String name;
/*     */     private final XmlElementImpl this$0;
/*     */     
/*     */     ElementsSimpleIterator(XmlElementImpl this$0, XmlNamespace n, String name, Iterator children) {
/* 830 */       this.this$0 = this$0;
/* 831 */       this.children = children;
/* 832 */       this.n = n;
/* 833 */       this.name = name;
/* 834 */       findNextEl();
/*     */     }
/*     */     
/*     */     private void findNextEl() {
/* 838 */       this.currentEl = null;
/* 839 */       while (this.children.hasNext()) {
/* 840 */         Object child = this.children.next();
/* 841 */         if (child instanceof XmlElement) {
/* 842 */           XmlElement el = (XmlElement)child;
/* 843 */           if ((this.name == null || el.getName() == this.name || this.name.equals(el.getName())) && (this.n == null || el.getNamespace() == this.n || this.n.equals(el.getNamespace()))) {
/*     */ 
/*     */ 
/*     */             
/* 847 */             this.currentEl = el;
/*     */             break;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/*     */     public boolean hasNext() {
/* 855 */       return (this.currentEl != null);
/*     */     }
/*     */     
/*     */     public Object next() {
/* 859 */       if (this.currentEl == null) {
/* 860 */         throw new XmlBuilderException("this iterator has no content and next() is not allowed");
/*     */       }
/*     */       
/* 863 */       XmlElement el = this.currentEl;
/* 864 */       findNextEl();
/* 865 */       return el;
/*     */     }
/*     */     
/*     */     public void remove() {
/* 869 */       throw new XmlBuilderException("this element iterator does nto support remove()");
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static class RequiredElementContentIterator
/*     */     implements Iterator
/*     */   {
/*     */     private Iterator children;
/*     */     private XmlElement currentEl;
/*     */     
/*     */     RequiredElementContentIterator(Iterator children) {
/* 881 */       this.children = children;
/* 882 */       findNextEl();
/*     */     }
/*     */     
/*     */     private void findNextEl() {
/* 886 */       this.currentEl = null;
/* 887 */       while (this.children.hasNext()) {
/* 888 */         Object child = this.children.next();
/* 889 */         if (child instanceof XmlElement) {
/* 890 */           this.currentEl = (XmlElement)child; break;
/*     */         } 
/* 892 */         if (child instanceof String) {
/* 893 */           String s = child.toString();
/* 894 */           if (false == XmlElementImpl.isWhiteSpace(s))
/* 895 */             throw new XmlBuilderException("only whitespace string children allowed for non mixed element content"); 
/*     */           continue;
/*     */         } 
/* 898 */         if (child instanceof XmlCharacters) {
/* 899 */           XmlCharacters xc = (XmlCharacters)child;
/* 900 */           if (!Boolean.TRUE.equals(xc.isWhitespaceContent()) || false == XmlElementImpl.isWhiteSpace(xc.getText()))
/*     */           {
/*     */             
/* 903 */             throw new XmlBuilderException("only whitespace characters children allowed for non mixed element content");
/*     */           }
/*     */           
/*     */           continue;
/*     */         } 
/* 908 */         throw new XmlBuilderException("only whitespace characters and element children allowed for non mixed element content and not " + child.getClass());
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean hasNext() {
/* 916 */       return (this.currentEl != null);
/*     */     }
/*     */     
/*     */     public Object next() {
/* 920 */       if (this.currentEl == null) {
/* 921 */         throw new XmlBuilderException("this iterator has no content and next() is not allowed");
/*     */       }
/*     */       
/* 924 */       XmlElement el = this.currentEl;
/* 925 */       findNextEl();
/* 926 */       return el;
/*     */     }
/*     */     
/*     */     public void remove() {
/* 930 */       throw new XmlBuilderException("this iterator does nto support remove()");
/*     */     } }
/*     */   
/*     */   private static class EmptyIterator implements Iterator {
/*     */     private EmptyIterator() {}
/*     */     
/*     */     public boolean hasNext() {
/* 937 */       return false;
/*     */     }
/*     */     
/*     */     public Object next() {
/* 941 */       throw new XmlBuilderException("this iterator has no content and next() is not allowed");
/*     */     }
/*     */ 
/*     */     
/*     */     public void remove() {
/* 946 */       throw new XmlBuilderException("this iterator has no content and remove() is not allowed");
/*     */     }
/*     */   }
/*     */ 
/*     */   
/* 951 */   private static final Iterator EMPTY_ITERATOR = new EmptyIterator();
/*     */   
/* 953 */   private static final Iterable EMPTY_ITERABLE = new Iterable()
/*     */     {
/*     */       public Iterator iterator() {
/* 956 */         return XmlElementImpl.EMPTY_ITERATOR;
/*     */       }
/*     */     };
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\xmlpull\v1\builder\impl\XmlElementImpl.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */