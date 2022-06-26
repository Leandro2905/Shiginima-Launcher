package org.jxmpp.stringprep;

public interface XmppStringprep {
  String localprep(String paramString) throws XmppStringprepException;
  
  String domainprep(String paramString) throws XmppStringprepException;
  
  String resourceprep(String paramString) throws XmppStringprepException;
}


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jxmpp\stringprep\XmppStringprep.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */