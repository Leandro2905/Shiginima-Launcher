/*     */ package org.jivesoftware.smack;
/*     */ 
/*     */ import org.jivesoftware.smack.filter.StanzaFilter;
/*     */ import org.jivesoftware.smack.iqrequest.IQRequestHandler;
/*     */ import org.jivesoftware.smack.packet.IQ;
/*     */ import org.jivesoftware.smack.packet.PlainStreamElement;
/*     */ import org.jivesoftware.smack.packet.Stanza;
/*     */ import org.jxmpp.jid.DomainBareJid;
/*     */ import org.jxmpp.jid.FullJid;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public interface XMPPConnection
/*     */ {
/*     */   DomainBareJid getServiceName();
/*     */   
/*     */   String getHost();
/*     */   
/*     */   int getPort();
/*     */   
/*     */   FullJid getUser();
/*     */   
/*     */   String getStreamId();
/*     */   
/*     */   boolean isConnected();
/*     */   
/*     */   boolean isAuthenticated();
/*     */   
/*     */   boolean isAnonymous();
/*     */   
/*     */   boolean isSecureConnection();
/*     */   
/*     */   boolean isUsingCompression();
/*     */   
/*     */   @Deprecated
/*     */   void sendPacket(Stanza paramStanza) throws SmackException.NotConnectedException, InterruptedException;
/*     */   
/*     */   void sendStanza(Stanza paramStanza) throws SmackException.NotConnectedException, InterruptedException;
/*     */   
/*     */   void send(PlainStreamElement paramPlainStreamElement) throws SmackException.NotConnectedException, InterruptedException;
/*     */   
/*     */   void addConnectionListener(ConnectionListener paramConnectionListener);
/*     */   
/*     */   void removeConnectionListener(ConnectionListener paramConnectionListener);
/*     */   
/*     */   PacketCollector createPacketCollectorAndSend(IQ paramIQ) throws SmackException.NotConnectedException, InterruptedException;
/*     */   
/*     */   PacketCollector createPacketCollectorAndSend(StanzaFilter paramStanzaFilter, Stanza paramStanza) throws SmackException.NotConnectedException, InterruptedException;
/*     */   
/*     */   PacketCollector createPacketCollector(StanzaFilter paramStanzaFilter);
/*     */   
/*     */   PacketCollector createPacketCollector(PacketCollector.Configuration paramConfiguration);
/*     */   
/*     */   void removePacketCollector(PacketCollector paramPacketCollector);
/*     */   
/*     */   @Deprecated
/*     */   void addPacketListener(StanzaListener paramStanzaListener, StanzaFilter paramStanzaFilter);
/*     */   
/*     */   @Deprecated
/*     */   boolean removePacketListener(StanzaListener paramStanzaListener);
/*     */   
/*     */   void addSyncStanzaListener(StanzaListener paramStanzaListener, StanzaFilter paramStanzaFilter);
/*     */   
/*     */   boolean removeSyncStanzaListener(StanzaListener paramStanzaListener);
/*     */   
/*     */   void addAsyncStanzaListener(StanzaListener paramStanzaListener, StanzaFilter paramStanzaFilter);
/*     */   
/*     */   boolean removeAsyncStanzaListener(StanzaListener paramStanzaListener);
/*     */   
/*     */   void addPacketSendingListener(StanzaListener paramStanzaListener, StanzaFilter paramStanzaFilter);
/*     */   
/*     */   void removePacketSendingListener(StanzaListener paramStanzaListener);
/*     */   
/*     */   void addPacketInterceptor(StanzaListener paramStanzaListener, StanzaFilter paramStanzaFilter);
/*     */   
/*     */   void removePacketInterceptor(StanzaListener paramStanzaListener);
/*     */   
/*     */   long getPacketReplyTimeout();
/*     */   
/*     */   void setPacketReplyTimeout(long paramLong);
/*     */   
/*     */   int getConnectionCounter();
/*     */   
/*     */   void setFromMode(FromMode paramFromMode);
/*     */   
/*     */   FromMode getFromMode();
/*     */   
/*     */   <F extends org.jivesoftware.smack.packet.ExtensionElement> F getFeature(String paramString1, String paramString2);
/*     */   
/*     */   boolean hasFeature(String paramString1, String paramString2);
/*     */   
/*     */   void sendStanzaWithResponseCallback(Stanza paramStanza, StanzaFilter paramStanzaFilter, StanzaListener paramStanzaListener) throws SmackException.NotConnectedException, InterruptedException;
/*     */   
/*     */   void sendStanzaWithResponseCallback(Stanza paramStanza, StanzaFilter paramStanzaFilter, StanzaListener paramStanzaListener, ExceptionCallback paramExceptionCallback) throws SmackException.NotConnectedException, InterruptedException;
/*     */   
/*     */   void sendStanzaWithResponseCallback(Stanza paramStanza, StanzaFilter paramStanzaFilter, StanzaListener paramStanzaListener, ExceptionCallback paramExceptionCallback, long paramLong) throws SmackException.NotConnectedException, InterruptedException;
/*     */   
/*     */   void sendIqWithResponseCallback(IQ paramIQ, StanzaListener paramStanzaListener) throws SmackException.NotConnectedException, InterruptedException;
/*     */   
/*     */   void sendIqWithResponseCallback(IQ paramIQ, StanzaListener paramStanzaListener, ExceptionCallback paramExceptionCallback) throws SmackException.NotConnectedException, InterruptedException;
/*     */   
/*     */   void sendIqWithResponseCallback(IQ paramIQ, StanzaListener paramStanzaListener, ExceptionCallback paramExceptionCallback, long paramLong) throws SmackException.NotConnectedException, InterruptedException;
/*     */   
/*     */   void addOneTimeSyncCallback(StanzaListener paramStanzaListener, StanzaFilter paramStanzaFilter);
/*     */   
/*     */   IQRequestHandler registerIQRequestHandler(IQRequestHandler paramIQRequestHandler);
/*     */   
/*     */   IQRequestHandler unregisterIQRequestHandler(IQRequestHandler paramIQRequestHandler);
/*     */   
/*     */   IQRequestHandler unregisterIQRequestHandler(String paramString1, String paramString2, IQ.Type paramType);
/*     */   
/*     */   long getLastStanzaReceived();
/*     */   
/*     */   public enum FromMode
/*     */   {
/* 424 */     UNCHANGED,
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 430 */     OMITTED,
/*     */ 
/*     */ 
/*     */     
/* 434 */     USER;
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jivesoftware\smack\XMPPConnection.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */