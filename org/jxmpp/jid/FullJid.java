package org.jxmpp.jid;

public interface FullJid extends Jid, JidWithResource, JidWithLocalpart {
  BareJid asBareJid();
  
  String asBareJidString();
}


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jxmpp\jid\FullJid.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */