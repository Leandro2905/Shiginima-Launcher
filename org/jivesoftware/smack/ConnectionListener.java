package org.jivesoftware.smack;

public interface ConnectionListener {
  void connected(XMPPConnection paramXMPPConnection);
  
  void authenticated(XMPPConnection paramXMPPConnection, boolean paramBoolean);
  
  void connectionClosed();
  
  void connectionClosedOnError(Exception paramException);
  
  void reconnectionSuccessful();
  
  void reconnectingIn(int paramInt);
  
  void reconnectionFailed(Exception paramException);
}


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jivesoftware\smack\ConnectionListener.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */