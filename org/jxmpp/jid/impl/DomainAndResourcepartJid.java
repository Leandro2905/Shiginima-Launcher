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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DomainAndResourcepartJid
/*     */   extends AbstractJid
/*     */   implements DomainFullJid
/*     */ {
/*     */   private final DomainBareJid domainBareJid;
/*     */   private final Resourcepart resource;
/*     */   private String cache;
/*     */   
/*     */   DomainAndResourcepartJid(String domain, String resource) throws XmppStringprepException {
/*  47 */     this.domainBareJid = new DomainpartJid(domain);
/*  48 */     this.resource = Resourcepart.from(resource);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  53 */     if (this.cache != null) {
/*  54 */       return this.cache;
/*     */     }
/*  56 */     this.cache = this.domainBareJid.toString() + '/' + this.resource;
/*  57 */     return this.cache;
/*     */   }
/*     */ 
/*     */   
/*     */   public DomainBareJid asDomainBareJid() {
/*  62 */     return this.domainBareJid;
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean hasNoResource() {
/*  67 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public BareJid asBareJidIfPossible() {
/*  72 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public FullJid asFullJidIfPossible() {
/*  77 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public DomainBareJid asDomainBareJidIfPossible() {
/*  82 */     return asDomainBareJid();
/*     */   }
/*     */ 
/*     */   
/*     */   public DomainFullJid asDomainFullJidIfPossible() {
/*  87 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Resourcepart getResourceOrNull() {
/*  92 */     return getResourcepart();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isParentOf(BareJid bareJid) {
/*  97 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isParentOf(FullJid fullJid) {
/* 102 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isParentOf(DomainBareJid domainBareJid) {
/* 107 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isParentOf(DomainFullJid domainFullJid) {
/* 112 */     return (this.domainBareJid.equals((CharSequence)domainFullJid.getDomain()) && this.resource.equals(domainFullJid.getResourcepart()));
/*     */   }
/*     */ 
/*     */   
/*     */   public Resourcepart getResourcepart() {
/* 117 */     return this.resource;
/*     */   }
/*     */ 
/*     */   
/*     */   public Jid withoutResource() {
/* 122 */     return (Jid)asDomainBareJid();
/*     */   }
/*     */ 
/*     */   
/*     */   public Domainpart getDomain() {
/* 127 */     return this.domainBareJid.getDomain();
/*     */   }
/*     */ 
/*     */   
/*     */   public String asUnescapedString() {
/* 132 */     return toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public Localpart maybeGetLocalpart() {
/* 137 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public Resourcepart maybeGetResourcepart() {
/* 142 */     return getResourcepart();
/*     */   }
/*     */ 
/*     */   
/*     */   public JidWithLocalpart asJidWithLocalpartIfPossible() {
/* 147 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public JidWithResource asJidWithResourcepartIfPossible() {
/* 152 */     return (JidWithResource)this;
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jxmpp\jid\impl\DomainAndResourcepartJid.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */