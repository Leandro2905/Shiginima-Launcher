package org.xmlpull.v1.builder;

public interface XmlDocument extends XmlContainer, Cloneable {
  Object clone() throws CloneNotSupportedException;
  
  Iterable children();
  
  XmlElement getDocumentElement();
  
  XmlElement requiredElement(XmlNamespace paramXmlNamespace, String paramString);
  
  XmlElement element(XmlNamespace paramXmlNamespace, String paramString);
  
  XmlElement element(XmlNamespace paramXmlNamespace, String paramString, boolean paramBoolean);
  
  Iterable notations();
  
  Iterable unparsedEntities();
  
  String getBaseUri();
  
  String getCharacterEncodingScheme();
  
  void setCharacterEncodingScheme(String paramString);
  
  Boolean isStandalone();
  
  String getVersion();
  
  boolean isAllDeclarationsProcessed();
  
  void setDocumentElement(XmlElement paramXmlElement);
  
  void addChild(Object paramObject);
  
  void insertChild(int paramInt, Object paramObject);
  
  void removeAllChildren();
  
  XmlComment newComment(String paramString);
  
  XmlComment addComment(String paramString);
  
  XmlDoctype newDoctype(String paramString1, String paramString2);
  
  XmlDoctype addDoctype(String paramString1, String paramString2);
  
  XmlElement addDocumentElement(String paramString);
  
  XmlElement addDocumentElement(XmlNamespace paramXmlNamespace, String paramString);
  
  XmlProcessingInstruction newProcessingInstruction(String paramString1, String paramString2);
  
  XmlProcessingInstruction addProcessingInstruction(String paramString1, String paramString2);
  
  void removeAllUnparsedEntities();
  
  XmlNotation addNotation(String paramString1, String paramString2, String paramString3, String paramString4);
  
  void removeAllNotations();
}


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\xmlpull\v1\builder\XmlDocument.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */