package org.jivesoftware.smack.packet;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Deprecated
public interface Packet extends TopLevelStreamElement {
  public static final String TEXT = "text";
  
  public static final String ITEM = "item";
  
  String getStanzaId();
  
  @Deprecated
  String getPacketID();
  
  void setStanzaId(String paramString);
  
  @Deprecated
  void setPacketID(String paramString);
  
  String getTo();
  
  void setTo(String paramString);
  
  String getFrom();
  
  void setFrom(String paramString);
  
  XMPPError getError();
  
  void setError(XMPPError paramXMPPError);
  
  String getLanguage();
  
  void setLanguage(String paramString);
  
  List<ExtensionElement> getExtensions();
  
  Set<ExtensionElement> getExtensions(String paramString1, String paramString2);
  
  ExtensionElement getExtension(String paramString);
  
  <PE extends ExtensionElement> PE getExtension(String paramString1, String paramString2);
  
  void addExtension(ExtensionElement paramExtensionElement);
  
  void addExtensions(Collection<ExtensionElement> paramCollection);
  
  boolean hasExtension(String paramString1, String paramString2);
  
  boolean hasExtension(String paramString);
  
  ExtensionElement removeExtension(String paramString1, String paramString2);
  
  ExtensionElement removeExtension(ExtensionElement paramExtensionElement);
  
  String toString();
}


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jivesoftware\smack\packet\Packet.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */