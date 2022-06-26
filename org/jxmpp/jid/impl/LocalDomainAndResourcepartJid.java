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
/*     */ public class LocalDomainAndResourcepartJid
/*     */   extends AbstractJid
/*     */   implements FullJid
/*     */ {
/*     */   private final BareJid bareJid;
/*     */   private final Resourcepart resource;
/*     */   private String cache;
/*     */   private String unescapedCache;
/*     */   
/*     */   LocalDomainAndResourcepartJid(String localpart, String domain, String resource) throws XmppStringprepException {
/*  40 */     this(new LocalAndDomainpartJid(localpart, domain), Resourcepart.from(resource));
/*     */   }
/*     */   
/*     */   LocalDomainAndResourcepartJid(BareJid bareJid, Resourcepart resource) {
/*  44 */     this.bareJid = bareJid;
/*  45 */     this.resource = resource;
/*     */   }
/*     */   
/*     */   public final Resourcepart getResource() {
/*  49 */     return this.resource;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  54 */     if (this.cache != null) {
/*  55 */       return this.cache;
/*     */     }
/*  57 */     this.cache = this.bareJid.toString() + '/' + this.resource;
/*  58 */     return this.cache;
/*     */   }
/*     */ 
/*     */   
/*     */   public String asUnescapedString() {
/*  63 */     if (this.unescapedCache != null) {
/*  64 */       return this.unescapedCache;
/*     */     }
/*  66 */     this.unescapedCache = this.bareJid.asUnescapedString() + '/' + this.resource;
/*  67 */     return this.unescapedCache;
/*     */   }
/*     */ 
/*     */   
/*     */   public BareJid asBareJid() {
/*  72 */     return this.bareJid;
/*     */   }
/*     */ 
/*     */   
/*     */   public String asBareJidString() {
/*  77 */     return asBareJid().toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean hasNoResource() {
/*  82 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public BareJid asBareJidIfPossible() {
/*  87 */     return asBareJid();
/*     */   }
/*     */ 
/*     */   
/*     */   public FullJid asFullJidIfPossible() {
/*  92 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public DomainBareJid asDomainBareJidIfPossible() {
/*  97 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public DomainFullJid asDomainFullJidIfPossible() {
/* 102 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public Localpart getLocalpartOrNull() {
/* 107 */     return this.bareJid.getLocalpart();
/*     */   }
/*     */ 
/*     */   
/*     */   public Resourcepart getResourceOrNull() {
/* 112 */     return getResource();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isParentOf(BareJid bareJid) {
/* 117 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isParentOf(FullJid fullJid) {
/* 122 */     return equals((CharSequence)fullJid);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isParentOf(DomainBareJid domainBareJid) {
/* 127 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isParentOf(DomainFullJid domainFullJid) {
/* 132 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public DomainBareJid asDomainBareJid() {
/* 137 */     return this.bareJid.asDomainBareJid();
/*     */   }
/*     */ 
/*     */   
/*     */   public Resourcepart getResourcepart() {
/* 142 */     return this.resource;
/*     */   }
/*     */ 
/*     */   
/*     */   public Jid withoutResource() {
/* 147 */     return (Jid)asBareJid();
/*     */   }
/*     */ 
/*     */   
/*     */   public Domainpart getDomain() {
/* 152 */     return this.bareJid.getDomain();
/*     */   }
/*     */ 
/*     */   
/*     */   public Localpart maybeGetLocalpart() {
/* 157 */     return this.bareJid.getLocalpart();
/*     */   }
/*     */ 
/*     */   
/*     */   public Resourcepart maybeGetResourcepart() {
/* 162 */     return getResource();
/*     */   }
/*     */ 
/*     */   
/*     */   public Localpart getLocalpart() {
/* 167 */     return this.bareJid.getLocalpart();
/*     */   }
/*     */ 
/*     */   
/*     */   public JidWithLocalpart asJidWithLocalpartIfPossible() {
/* 172 */     return (JidWithLocalpart)this;
/*     */   }
/*     */ 
/*     */   
/*     */   public JidWithResource asJidWithResourcepartIfPossible() {
/* 177 */     return (JidWithResource)this;
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jxmpp\jid\impl\LocalDomainAndResourcepartJid.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */