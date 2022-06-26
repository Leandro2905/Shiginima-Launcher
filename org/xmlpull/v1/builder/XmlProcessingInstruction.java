package org.xmlpull.v1.builder;

public interface XmlProcessingInstruction {
  String getTarget();
  
  String getContent();
  
  String getBaseUri();
  
  XmlNotation getNotation();
  
  XmlContainer getParent();
}


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\xmlpull\v1\builder\XmlProcessingInstruction.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */