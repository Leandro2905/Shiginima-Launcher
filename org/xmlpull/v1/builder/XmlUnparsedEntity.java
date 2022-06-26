package org.xmlpull.v1.builder;

public interface XmlUnparsedEntity {
  String getName();
  
  String getSystemIdentifier();
  
  String getPublicIdentifier();
  
  String getDeclarationBaseUri();
  
  String getNotationName();
  
  XmlNotation getNotation();
}


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\xmlpull\v1\builder\XmlUnparsedEntity.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */