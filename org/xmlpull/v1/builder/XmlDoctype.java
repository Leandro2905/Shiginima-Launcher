package org.xmlpull.v1.builder;

import java.util.Iterator;

public interface XmlDoctype extends XmlContainer {
  String getSystemIdentifier();
  
  String getPublicIdentifier();
  
  Iterator children();
  
  XmlDocument getParent();
  
  XmlProcessingInstruction addProcessingInstruction(String paramString1, String paramString2);
  
  void removeAllProcessingInstructions();
}


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\xmlpull\v1\builder\XmlDoctype.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */