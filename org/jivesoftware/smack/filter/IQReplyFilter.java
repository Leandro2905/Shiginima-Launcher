/*     */ package org.jivesoftware.smack.filter;
/*     */ 
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import org.jivesoftware.smack.XMPPConnection;
/*     */ import org.jivesoftware.smack.packet.IQ;
/*     */ import org.jivesoftware.smack.packet.Stanza;
/*     */ import org.jxmpp.jid.DomainBareJid;
/*     */ import org.jxmpp.jid.FullJid;
/*     */ import org.jxmpp.jid.Jid;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class IQReplyFilter
/*     */   implements StanzaFilter
/*     */ {
/*  53 */   private static final Logger LOGGER = Logger.getLogger(IQReplyFilter.class.getName());
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final StanzaFilter iqAndIdFilter;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final OrFilter fromFilter;
/*     */ 
/*     */ 
/*     */   
/*     */   private final Jid to;
/*     */ 
/*     */ 
/*     */   
/*     */   private final FullJid local;
/*     */ 
/*     */ 
/*     */   
/*     */   private final DomainBareJid server;
/*     */ 
/*     */ 
/*     */   
/*     */   private final String packetId;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public IQReplyFilter(IQ iqPacket, XMPPConnection conn) {
/*  85 */     if (!iqPacket.isRequestIQ()) {
/*  86 */       throw new IllegalArgumentException("IQ must be a request IQ, i.e. of type 'get' or 'set'.");
/*     */     }
/*  88 */     this.to = iqPacket.getTo();
/*  89 */     this.local = conn.getUser();
/*  90 */     if (this.local == null) {
/*  91 */       throw new IllegalArgumentException("Must have a local (user) JID set. Either you didn't configure one or you where not connected at least once");
/*     */     }
/*     */     
/*  94 */     this.server = conn.getServiceName();
/*  95 */     this.packetId = iqPacket.getStanzaId();
/*     */     
/*  97 */     StanzaFilter iqFilter = new OrFilter(new StanzaFilter[] { IQTypeFilter.ERROR, IQTypeFilter.RESULT });
/*  98 */     StanzaFilter idFilter = new StanzaIdFilter((Stanza)iqPacket);
/*  99 */     this.iqAndIdFilter = new AndFilter(new StanzaFilter[] { iqFilter, idFilter });
/* 100 */     this.fromFilter = new OrFilter();
/* 101 */     this.fromFilter.addFilter(FromMatchesFilter.createFull(this.to));
/* 102 */     if (this.to == null) {
/* 103 */       this.fromFilter.addFilter(FromMatchesFilter.createBare((Jid)this.local));
/* 104 */       this.fromFilter.addFilter(FromMatchesFilter.createFull((Jid)this.server));
/*     */     }
/* 106 */     else if (this.to.equals((CharSequence)this.local.asBareJid())) {
/* 107 */       this.fromFilter.addFilter(FromMatchesFilter.createFull(null));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean accept(Stanza packet) {
/* 114 */     if (!this.iqAndIdFilter.accept(packet)) {
/* 115 */       return false;
/*     */     }
/*     */     
/* 118 */     if (this.fromFilter.accept(packet)) {
/* 119 */       return true;
/*     */     }
/* 121 */     String msg = String.format("Rejected potentially spoofed reply to IQ-packet. Filter settings: packetId=%s, to=%s, local=%s, server=%s. Received packet with from=%s", new Object[] { this.packetId, this.to, this.local, this.server, packet.getFrom() });
/*     */ 
/*     */     
/* 124 */     LOGGER.log(Level.WARNING, msg, packet);
/* 125 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 131 */     StringBuilder sb = new StringBuilder();
/* 132 */     sb.append(getClass().getSimpleName());
/* 133 */     sb.append(": iqAndIdFilter (").append(this.iqAndIdFilter.toString()).append("), ");
/* 134 */     sb.append(": fromFilter (").append(this.fromFilter.toString()).append(')');
/* 135 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jivesoftware\smack\filter\IQReplyFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */