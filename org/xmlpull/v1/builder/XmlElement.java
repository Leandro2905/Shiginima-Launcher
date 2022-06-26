package org.xmlpull.v1.builder;

import java.util.Iterator;

public interface XmlElement extends XmlContainer, XmlContained, Cloneable {
  public static final String NO_NAMESPACE = "";
  
  Object clone() throws CloneNotSupportedException;
  
  String getBaseUri();
  
  void setBaseUri(String paramString);
  
  XmlContainer getRoot();
  
  XmlContainer getParent();
  
  void setParent(XmlContainer paramXmlContainer);
  
  XmlNamespace getNamespace();
  
  String getNamespaceName();
  
  void setNamespace(XmlNamespace paramXmlNamespace);
  
  String getName();
  
  void setName(String paramString);
  
  Iterator attributes();
  
  XmlAttribute addAttribute(XmlAttribute paramXmlAttribute);
  
  XmlAttribute addAttribute(String paramString1, String paramString2);
  
  XmlAttribute addAttribute(XmlNamespace paramXmlNamespace, String paramString1, String paramString2);
  
  XmlAttribute addAttribute(String paramString1, XmlNamespace paramXmlNamespace, String paramString2, String paramString3);
  
  XmlAttribute addAttribute(String paramString1, XmlNamespace paramXmlNamespace, String paramString2, String paramString3, boolean paramBoolean);
  
  XmlAttribute addAttribute(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, boolean paramBoolean);
  
  void ensureAttributeCapacity(int paramInt);
  
  String getAttributeValue(String paramString1, String paramString2);
  
  XmlAttribute attribute(String paramString);
  
  XmlAttribute attribute(XmlNamespace paramXmlNamespace, String paramString);
  
  XmlAttribute findAttribute(String paramString1, String paramString2);
  
  boolean hasAttributes();
  
  void removeAttribute(XmlAttribute paramXmlAttribute);
  
  void removeAllAttributes();
  
  Iterator namespaces();
  
  XmlNamespace declareNamespace(String paramString1, String paramString2);
  
  XmlNamespace declareNamespace(XmlNamespace paramXmlNamespace);
  
  void ensureNamespaceDeclarationsCapacity(int paramInt);
  
  boolean hasNamespaceDeclarations();
  
  XmlNamespace lookupNamespaceByPrefix(String paramString);
  
  XmlNamespace lookupNamespaceByName(String paramString);
  
  XmlNamespace newNamespace(String paramString);
  
  XmlNamespace newNamespace(String paramString1, String paramString2);
  
  void removeAllNamespaceDeclarations();
  
  Iterator children();
  
  void addChild(Object paramObject);
  
  void addChild(int paramInt, Object paramObject);
  
  XmlElement addElement(XmlElement paramXmlElement);
  
  XmlElement addElement(int paramInt, XmlElement paramXmlElement);
  
  XmlElement addElement(String paramString);
  
  XmlElement addElement(XmlNamespace paramXmlNamespace, String paramString);
  
  boolean hasChildren();
  
  boolean hasChild(Object paramObject);
  
  void ensureChildrenCapacity(int paramInt);
  
  XmlElement findElementByName(String paramString);
  
  XmlElement findElementByName(String paramString1, String paramString2);
  
  XmlElement findElementByName(String paramString, XmlElement paramXmlElement);
  
  XmlElement findElementByName(String paramString1, String paramString2, XmlElement paramXmlElement);
  
  XmlElement element(int paramInt);
  
  XmlElement requiredElement(XmlNamespace paramXmlNamespace, String paramString) throws XmlBuilderException;
  
  XmlElement element(XmlNamespace paramXmlNamespace, String paramString);
  
  XmlElement element(XmlNamespace paramXmlNamespace, String paramString, boolean paramBoolean);
  
  Iterable elements(XmlNamespace paramXmlNamespace, String paramString);
  
  void insertChild(int paramInt, Object paramObject);
  
  XmlElement newElement(String paramString);
  
  XmlElement newElement(XmlNamespace paramXmlNamespace, String paramString);
  
  XmlElement newElement(String paramString1, String paramString2);
  
  void removeAllChildren();
  
  void removeChild(Object paramObject);
  
  void replaceChild(Object paramObject1, Object paramObject2);
  
  Iterable requiredElementContent();
  
  String requiredTextContent();
  
  void replaceChildrenWithText(String paramString);
}


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\xmlpull\v1\builder\XmlElement.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */