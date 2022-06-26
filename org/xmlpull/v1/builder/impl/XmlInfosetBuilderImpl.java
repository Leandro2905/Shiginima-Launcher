/*     */ package org.xmlpull.v1.builder.impl;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import org.xmlpull.v1.XmlPullParser;
/*     */ import org.xmlpull.v1.XmlPullParserException;
/*     */ import org.xmlpull.v1.XmlSerializer;
/*     */ import org.xmlpull.v1.builder.XmlAttribute;
/*     */ import org.xmlpull.v1.builder.XmlBuilderException;
/*     */ import org.xmlpull.v1.builder.XmlCharacters;
/*     */ import org.xmlpull.v1.builder.XmlComment;
/*     */ import org.xmlpull.v1.builder.XmlContainer;
/*     */ import org.xmlpull.v1.builder.XmlDocument;
/*     */ import org.xmlpull.v1.builder.XmlElement;
/*     */ import org.xmlpull.v1.builder.XmlInfosetBuilder;
/*     */ import org.xmlpull.v1.builder.XmlNamespace;
/*     */ import org.xmlpull.v1.builder.XmlSerializable;
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
/*     */ public class XmlInfosetBuilderImpl
/*     */   extends XmlInfosetBuilder
/*     */ {
/*     */   private static final String PROPERTY_XMLDECL_STANDALONE = "http://xmlpull.org/v1/doc/properties.html#xmldecl-standalone";
/*     */   private static final String PROPERTY_XMLDECL_VERSION = "http://xmlpull.org/v1/doc/properties.html#xmldecl-version";
/*     */   
/*     */   public XmlDocument newDocument(String version, Boolean standalone, String characterEncoding) {
/*  45 */     return new XmlDocumentImpl(version, standalone, characterEncoding);
/*     */   }
/*     */ 
/*     */   
/*     */   public XmlElement newFragment(String elementName) {
/*  50 */     return new XmlElementImpl((XmlNamespace)null, elementName);
/*     */   }
/*     */ 
/*     */   
/*     */   public XmlElement newFragment(String elementNamespaceName, String elementName) {
/*  55 */     return new XmlElementImpl(elementNamespaceName, elementName);
/*     */   }
/*     */ 
/*     */   
/*     */   public XmlElement newFragment(XmlNamespace elementNamespace, String elementName) {
/*  60 */     return new XmlElementImpl(elementNamespace, elementName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public XmlNamespace newNamespace(String namespaceName) {
/*  68 */     return new XmlNamespaceImpl(null, namespaceName);
/*     */   }
/*     */ 
/*     */   
/*     */   public XmlNamespace newNamespace(String prefix, String namespaceName) {
/*  73 */     return new XmlNamespaceImpl(prefix, namespaceName);
/*     */   }
/*     */ 
/*     */   
/*     */   public XmlDocument parse(XmlPullParser pp) {
/*  78 */     XmlDocument doc = parseDocumentStart(pp);
/*  79 */     XmlElement root = parseFragment(pp);
/*  80 */     doc.setDocumentElement(root);
/*     */     
/*  82 */     return doc;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object parseItem(XmlPullParser pp) {
/*     */     try {
/*  88 */       int eventType = pp.getEventType();
/*  89 */       if (eventType == 2)
/*  90 */         return parseStartTag(pp); 
/*  91 */       if (eventType == 4)
/*  92 */         return pp.getText(); 
/*  93 */       if (eventType == 0) {
/*  94 */         return parseDocumentStart(pp);
/*     */       }
/*  96 */       throw new XmlBuilderException("currently unsupported event type " + XmlPullParser.TYPES[eventType] + pp.getPositionDescription());
/*     */     
/*     */     }
/*  99 */     catch (XmlPullParserException e) {
/* 100 */       throw new XmlBuilderException("could not parse XML item", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private XmlDocument parseDocumentStart(XmlPullParser pp) {
/* 110 */     XmlDocument doc = null;
/*     */     try {
/* 112 */       if (pp.getEventType() != 0) {
/* 113 */         throw new XmlBuilderException("parser must be positioned on beginning of document and not " + pp.getPositionDescription());
/*     */       }
/*     */ 
/*     */       
/* 117 */       pp.next();
/* 118 */       String xmlDeclVersion = (String)pp.getProperty("http://xmlpull.org/v1/doc/properties.html#xmldecl-version");
/* 119 */       Boolean xmlDeclStandalone = (Boolean)pp.getProperty("http://xmlpull.org/v1/doc/properties.html#xmldecl-standalone");
/* 120 */       String characterEncoding = pp.getInputEncoding();
/* 121 */       doc = new XmlDocumentImpl(xmlDeclVersion, xmlDeclStandalone, characterEncoding);
/* 122 */     } catch (XmlPullParserException e) {
/* 123 */       throw new XmlBuilderException("could not parse XML document prolog", e);
/* 124 */     } catch (IOException e) {
/* 125 */       throw new XmlBuilderException("could not read XML document prolog", e);
/*     */     } 
/* 127 */     return doc;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public XmlElement parseFragment(XmlPullParser pp) {
/*     */     try {
/* 135 */       int depth = pp.getDepth();
/* 136 */       int eventType = pp.getEventType();
/* 137 */       if (eventType != 2) {
/* 138 */         throw new XmlBuilderException("expected parser to be on start tag and not " + XmlPullParser.TYPES[eventType] + pp.getPositionDescription());
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 143 */       XmlElement curElem = parseStartTag(pp);
/*     */       while (true) {
/* 145 */         eventType = pp.next();
/* 146 */         if (eventType == 2) {
/* 147 */           XmlElement child = parseStartTag(pp);
/* 148 */           curElem.addElement(child);
/* 149 */           curElem = child; continue;
/* 150 */         }  if (eventType == 3) {
/* 151 */           XmlContainer parent = curElem.getParent();
/* 152 */           if (parent == null) {
/* 153 */             if (pp.getDepth() != depth) {
/* 154 */               throw new XmlBuilderException("unbalanced input" + pp.getPositionDescription());
/*     */             }
/*     */             
/* 157 */             return curElem;
/*     */           } 
/* 159 */           curElem = (XmlElement)parent;
/*     */           continue;
/*     */         } 
/* 162 */         if (eventType == 4) {
/* 163 */           curElem.addChild(pp.getText());
/*     */         }
/*     */       } 
/* 166 */     } catch (XmlPullParserException e) {
/* 167 */       throw new XmlBuilderException("could not build tree from XML", e);
/* 168 */     } catch (IOException e) {
/* 169 */       throw new XmlBuilderException("could not read XML tree content", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public XmlElement parseStartTag(XmlPullParser pp) {
/*     */     try {
/* 177 */       if (pp.getEventType() != 2) {
/* 178 */         throw new XmlBuilderException("parser must be on START_TAG and not " + pp.getPositionDescription());
/*     */       }
/*     */ 
/*     */       
/* 182 */       String elNsPrefix = pp.getPrefix();
/* 183 */       XmlNamespace elementNs = new XmlNamespaceImpl(elNsPrefix, pp.getNamespace());
/* 184 */       XmlElement el = new XmlElementImpl(elementNs, pp.getName());
/*     */       
/* 186 */       int i = pp.getNamespaceCount(pp.getDepth() - 1);
/* 187 */       for (; i < pp.getNamespaceCount(pp.getDepth()); i++) {
/*     */ 
/*     */         
/* 190 */         String prefix = pp.getNamespacePrefix(i);
/* 191 */         el.declareNamespace((prefix == null) ? "" : prefix, pp.getNamespaceUri(i));
/*     */       } 
/*     */       
/* 194 */       for (i = 0; i < pp.getAttributeCount(); i++)
/*     */       {
/* 196 */         el.addAttribute(pp.getAttributeType(i), pp.getAttributePrefix(i), pp.getAttributeNamespace(i), pp.getAttributeName(i), pp.getAttributeValue(i), !pp.isAttributeDefault(i));
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 203 */       return el;
/* 204 */     } catch (XmlPullParserException e) {
/* 205 */       throw new XmlBuilderException("could not parse XML start tag", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public XmlDocument parseLocation(String locationUrl) {
/* 214 */     URL url = null;
/*     */     try {
/* 216 */       url = new URL(locationUrl);
/* 217 */     } catch (MalformedURLException e) {
/* 218 */       throw new XmlBuilderException("could not parse URL " + locationUrl, e);
/*     */     } 
/*     */     try {
/* 221 */       return parseInputStream(url.openStream());
/* 222 */     } catch (IOException e) {
/* 223 */       throw new XmlBuilderException("could not open connection to URL " + locationUrl, e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void serialize(Object item, XmlSerializer serializer) {
/* 229 */     if (item instanceof Collection) {
/* 230 */       Collection c = (Collection)item;
/* 231 */       for (Iterator i = c.iterator(); i.hasNext();) {
/* 232 */         serialize(i.next(), serializer);
/*     */       }
/*     */     }
/* 235 */     else if (item instanceof XmlContainer) {
/* 236 */       serializeContainer((XmlContainer)item, serializer);
/*     */     } else {
/* 238 */       serializeItem(item, serializer);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void serializeContainer(XmlContainer node, XmlSerializer serializer) {
/* 244 */     if (node instanceof XmlSerializable) {
/*     */       try {
/* 246 */         ((XmlSerializable)node).serialize(serializer);
/* 247 */       } catch (IOException e) {
/* 248 */         throw new XmlBuilderException("could not serialize node " + node + ": " + e, e);
/*     */       } 
/* 250 */     } else if (node instanceof XmlDocument) {
/* 251 */       serializeDocument((XmlDocument)node, serializer);
/* 252 */     } else if (node instanceof XmlElement) {
/* 253 */       serializeFragment((XmlElement)node, serializer);
/*     */     } else {
/* 255 */       throw new IllegalArgumentException("could not serialzie unknown XML container " + node.getClass());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void serializeItem(Object item, XmlSerializer ser) {
/*     */     try {
/* 263 */       if (item instanceof XmlSerializable) {
/*     */         
/*     */         try {
/* 266 */           ((XmlSerializable)item).serialize(ser);
/* 267 */         } catch (IOException e) {
/* 268 */           throw new XmlBuilderException("could not serialize item " + item + ": " + e, e);
/*     */         } 
/* 270 */       } else if (item instanceof String) {
/* 271 */         ser.text(item.toString());
/* 272 */       } else if (item instanceof XmlCharacters) {
/* 273 */         ser.text(((XmlCharacters)item).getText());
/* 274 */       } else if (item instanceof XmlComment) {
/* 275 */         ser.comment(((XmlComment)item).getContent());
/*     */       } else {
/* 277 */         throw new IllegalArgumentException("could not serialize " + ((item != null) ? item.getClass() : item));
/*     */       } 
/* 279 */     } catch (IOException e) {
/* 280 */       throw new XmlBuilderException("serializing XML start tag failed", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void serializeStartTag(XmlElement el, XmlSerializer ser) {
/*     */     try {
/* 287 */       XmlNamespace elNamespace = el.getNamespace();
/* 288 */       String elPrefix = (elNamespace != null) ? elNamespace.getPrefix() : "";
/* 289 */       if (elPrefix == null) {
/* 290 */         elPrefix = "";
/*     */       }
/* 292 */       String nToDeclare = null;
/* 293 */       if (el.hasNamespaceDeclarations()) {
/* 294 */         Iterator iter = el.namespaces();
/* 295 */         while (iter.hasNext()) {
/*     */           
/* 297 */           XmlNamespace n = iter.next();
/* 298 */           String nPrefix = n.getPrefix();
/* 299 */           if (!elPrefix.equals(nPrefix)) {
/* 300 */             ser.setPrefix(nPrefix, n.getNamespaceName()); continue;
/*     */           } 
/* 302 */           nToDeclare = n.getNamespaceName();
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/* 307 */       if (nToDeclare != null) {
/* 308 */         ser.setPrefix(elPrefix, nToDeclare);
/*     */       }
/* 310 */       else if (elNamespace != null) {
/*     */         
/* 312 */         String namespaceName = elNamespace.getNamespaceName();
/* 313 */         if (namespaceName == null) {
/* 314 */           namespaceName = "";
/*     */         }
/*     */         
/* 317 */         String serPrefix = null;
/* 318 */         if (namespaceName.length() > 0) {
/* 319 */           ser.getPrefix(namespaceName, false);
/*     */         }
/* 321 */         if (serPrefix == null) {
/* 322 */           serPrefix = "";
/*     */         }
/* 324 */         if (serPrefix != elPrefix && !serPrefix.equals(elPrefix))
/*     */         {
/* 326 */           ser.setPrefix(elPrefix, namespaceName);
/*     */         }
/*     */       } 
/*     */ 
/*     */       
/* 331 */       ser.startTag(el.getNamespaceName(), el.getName());
/* 332 */       if (el.hasAttributes()) {
/* 333 */         Iterator iter = el.attributes();
/* 334 */         while (iter.hasNext())
/*     */         {
/* 336 */           XmlAttribute a = iter.next();
/* 337 */           if (a instanceof XmlSerializable) {
/* 338 */             ((XmlSerializable)a).serialize(ser); continue;
/*     */           } 
/* 340 */           ser.attribute(a.getNamespaceName(), a.getName(), a.getValue());
/*     */         }
/*     */       
/*     */       } 
/* 344 */     } catch (IOException e) {
/* 345 */       throw new XmlBuilderException("serializing XML start tag failed", e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void serializeEndTag(XmlElement el, XmlSerializer ser) {
/*     */     try {
/* 351 */       ser.endTag(el.getNamespaceName(), el.getName());
/* 352 */     } catch (IOException e) {
/* 353 */       throw new XmlBuilderException("serializing XML end tag failed", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void serializeDocument(XmlDocument doc, XmlSerializer ser) {
/*     */     try {
/* 362 */       ser.startDocument(doc.getCharacterEncodingScheme(), doc.isStandalone());
/* 363 */     } catch (IOException e) {
/* 364 */       throw new XmlBuilderException("serializing XML document start failed", e);
/*     */     } 
/* 366 */     if (doc.getDocumentElement() != null) {
/* 367 */       serializeFragment(doc.getDocumentElement(), ser);
/*     */     } else {
/* 369 */       throw new XmlBuilderException("could not serialize document without root element " + doc + ": ");
/*     */     } 
/*     */     try {
/* 372 */       ser.endDocument();
/* 373 */     } catch (IOException e) {
/* 374 */       throw new XmlBuilderException("serializing XML document end failed", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void serializeFragment(XmlElement el, XmlSerializer ser) {
/* 380 */     serializeStartTag(el, ser);
/*     */     
/* 382 */     if (el.hasChildren()) {
/* 383 */       Iterator iter = el.children();
/* 384 */       while (iter.hasNext()) {
/*     */         
/* 386 */         Object child = iter.next();
/* 387 */         if (child instanceof XmlSerializable) {
/*     */           
/*     */           try {
/* 390 */             ((XmlSerializable)child).serialize(ser);
/* 391 */           } catch (IOException e) {
/* 392 */             throw new XmlBuilderException("could not serialize item " + child + ": " + e, e);
/*     */           }  continue;
/*     */         } 
/* 395 */         if (child instanceof XmlElement) {
/* 396 */           serializeFragment((XmlElement)child, ser); continue;
/*     */         } 
/* 398 */         serializeItem(child, ser);
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 405 */     serializeEndTag(el, ser);
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\xmlpull\v1\builder\impl\XmlInfosetBuilderImpl.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */