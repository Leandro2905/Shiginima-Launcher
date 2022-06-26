package org.jivesoftware.smack;

import org.jivesoftware.smack.packet.Stanza;

public interface StanzaListener {
  void processPacket(Stanza paramStanza) throws SmackException.NotConnectedException, InterruptedException;
}


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jivesoftware\smack\StanzaListener.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */