package org.jivesoftware.smack.debugger;

import java.io.Reader;
import java.io.Writer;
import org.jivesoftware.smack.StanzaListener;
import org.jxmpp.jid.FullJid;

public interface SmackDebugger {
  void userHasLogged(FullJid paramFullJid);
  
  Reader getReader();
  
  Writer getWriter();
  
  Reader newConnectionReader(Reader paramReader);
  
  Writer newConnectionWriter(Writer paramWriter);
  
  StanzaListener getReaderListener();
  
  StanzaListener getWriterListener();
}


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jivesoftware\smack\debugger\SmackDebugger.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */