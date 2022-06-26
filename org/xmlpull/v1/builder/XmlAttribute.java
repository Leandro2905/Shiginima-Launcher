package org.xmlpull.v1.builder;

public interface XmlAttribute extends Cloneable {
  Object clone() throws CloneNotSupportedException;
  
  XmlElement getOwner();
  
  String getNamespaceName();
  
  XmlNamespace getNamespace();
  
  String getName();
  
  String getValue();
  
  String getType();
  
  boolean isSpecified();
}


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\xmlpull\v1\builder\XmlAttribute.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */