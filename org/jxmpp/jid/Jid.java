package org.jxmpp.jid;

import org.jxmpp.jid.parts.Domainpart;
import org.jxmpp.jid.parts.Localpart;
import org.jxmpp.jid.parts.Resourcepart;

public interface Jid extends Comparable<Jid>, CharSequence {
  Domainpart getDomain();
  
  Localpart maybeGetLocalpart();
  
  Resourcepart maybeGetResourcepart();
  
  String toString();
  
  String asUnescapedString();
  
  boolean isBareOrFullJid();
  
  boolean isBareJid();
  
  boolean isFullJid();
  
  boolean isDomainBareJid();
  
  boolean isDomainFullJid();
  
  boolean hasNoResource();
  
  boolean hasResource();
  
  boolean hasLocalpart();
  
  BareJid asBareJidIfPossible();
  
  FullJid asFullJidIfPossible();
  
  JidWithLocalpart asJidWithLocalpartIfPossible();
  
  JidWithResource asJidWithResourcepartIfPossible();
  
  @Deprecated
  DomainBareJid asDomainBareJidIfPossible();
  
  DomainBareJid asDomainBareJid();
  
  String asDomainBareJidString();
  
  DomainFullJid asDomainFullJidIfPossible();
  
  Resourcepart getResourceOrNull();
  
  Localpart getLocalpartOrNull();
  
  Jid withoutResource();
  
  boolean isParentOf(Jid paramJid);
  
  boolean isParentOf(BareJid paramBareJid);
  
  boolean isParentOf(FullJid paramFullJid);
  
  boolean isParentOf(DomainBareJid paramDomainBareJid);
  
  boolean isParentOf(DomainFullJid paramDomainFullJid);
  
  <T extends Jid> T downcast();
  
  boolean equals(CharSequence paramCharSequence);
  
  boolean equals(String paramString);
}


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jxmpp\jid\Jid.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */