package org.jxmpp.jid;

import org.jxmpp.jid.parts.Localpart;

public interface JidWithLocalpart extends Jid {
  Localpart getLocalpart();
  
  BareJid asBareJid();
}


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jxmpp\jid\JidWithLocalpart.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */