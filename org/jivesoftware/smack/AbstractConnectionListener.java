package org.jivesoftware.smack;

public class AbstractConnectionListener implements ConnectionListener {
  public void connected(XMPPConnection connection) {}
  
  public void authenticated(XMPPConnection connection, boolean resumed) {}
  
  public void connectionClosed() {}
  
  public void connectionClosedOnError(Exception e) {}
  
  public void reconnectingIn(int seconds) {}
  
  public void reconnectionFailed(Exception e) {}
  
  public void reconnectionSuccessful() {}
}


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jivesoftware\smack\AbstractConnectionListener.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */