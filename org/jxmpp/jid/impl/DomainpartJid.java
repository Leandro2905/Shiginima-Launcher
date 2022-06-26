/*     */ package org.jxmpp.jid.impl;
/*     */ 
/*     */ import org.jxmpp.jid.BareJid;
/*     */ import org.jxmpp.jid.DomainBareJid;
/*     */ import org.jxmpp.jid.DomainFullJid;
/*     */ import org.jxmpp.jid.FullJid;
/*     */ import org.jxmpp.jid.Jid;
/*     */ import org.jxmpp.jid.JidWithLocalpart;
/*     */ import org.jxmpp.jid.JidWithResource;
/*     */ import org.jxmpp.jid.parts.Domainpart;
/*     */ import org.jxmpp.jid.parts.Localpart;
/*     */ import org.jxmpp.jid.parts.Resourcepart;
/*     */ import org.jxmpp.stringprep.XmppStringprepException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DomainpartJid
/*     */   extends AbstractJid
/*     */   implements DomainBareJid
/*     */ {
/*     */   protected final Domainpart domain;
/*     */   
/*     */   DomainpartJid(String domain) throws XmppStringprepException {
/*  36 */     this(Domainpart.from(domain));
/*     */   }
/*     */   
/*     */   DomainpartJid(Domainpart domain) {
/*  40 */     this.domain = domain;
/*     */   }
/*     */ 
/*     */   
/*     */   public Domainpart getDomain() {
/*  45 */     return this.domain;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  50 */     return this.domain.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String asUnescapedString() {
/*  56 */     return toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public DomainBareJid asDomainBareJid() {
/*  61 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasNoResource() {
/*  66 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public BareJid asBareJidIfPossible() {
/*  71 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public FullJid asFullJidIfPossible() {
/*  76 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public DomainBareJid asDomainBareJidIfPossible() {
/*  81 */     return asDomainBareJid();
/*     */   }
/*     */ 
/*     */   
/*     */   public DomainFullJid asDomainFullJidIfPossible() {
/*  86 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isParentOf(BareJid bareJid) {
/*  91 */     return this.domain.equals(bareJid.getDomain());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isParentOf(FullJid fullJid) {
/*  96 */     return this.domain.equals(fullJid.getDomain());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isParentOf(DomainBareJid domainBareJid) {
/* 101 */     return this.domain.equals(domainBareJid.getDomain());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isParentOf(DomainFullJid domainFullJid) {
/* 106 */     return this.domain.equals(domainFullJid.getDomain());
/*     */   }
/*     */ 
/*     */   
/*     */   public Jid withoutResource() {
/* 111 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Localpart maybeGetLocalpart() {
/* 116 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public Resourcepart maybeGetResourcepart() {
/* 121 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public JidWithLocalpart asJidWithLocalpartIfPossible() {
/* 126 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public JidWithResource asJidWithResourcepartIfPossible() {
/* 131 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jxmpp\jid\impl\DomainpartJid.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */