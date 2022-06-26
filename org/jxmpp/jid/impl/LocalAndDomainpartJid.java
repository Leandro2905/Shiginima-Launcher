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
/*     */ import org.jxmpp.util.XmppStringUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LocalAndDomainpartJid
/*     */   extends AbstractJid
/*     */   implements BareJid
/*     */ {
/*     */   private final DomainBareJid domainBareJid;
/*     */   private final Localpart localpart;
/*     */   private String cache;
/*     */   private String unescapedCache;
/*     */   
/*     */   LocalAndDomainpartJid(String localpart, String domain) throws XmppStringprepException {
/*  42 */     this.domainBareJid = new DomainpartJid(domain);
/*  43 */     this.localpart = Localpart.from(localpart);
/*     */   }
/*     */   
/*     */   LocalAndDomainpartJid(Localpart localpart, Domainpart domain) {
/*  47 */     this.localpart = localpart;
/*  48 */     this.domainBareJid = new DomainpartJid(domain);
/*     */   }
/*     */ 
/*     */   
/*     */   public final Localpart getLocalpart() {
/*  53 */     return this.localpart;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  58 */     if (this.cache != null) {
/*  59 */       return this.cache;
/*     */     }
/*  61 */     this.cache = getLocalpart().toString() + '@' + this.domainBareJid.toString();
/*  62 */     return this.cache;
/*     */   }
/*     */ 
/*     */   
/*     */   public String asUnescapedString() {
/*  67 */     if (this.unescapedCache != null) {
/*  68 */       return this.unescapedCache;
/*     */     }
/*  70 */     this.unescapedCache = XmppStringUtils.unescapeLocalpart(getLocalpart().toString()) + '@' + this.domainBareJid.toString();
/*  71 */     return this.unescapedCache;
/*     */   }
/*     */ 
/*     */   
/*     */   public BareJid asBareJidIfPossible() {
/*  76 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public FullJid asFullJidIfPossible() {
/*  81 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public DomainBareJid asDomainBareJidIfPossible() {
/*  86 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public DomainFullJid asDomainFullJidIfPossible() {
/*  91 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isParentOf(BareJid bareJid) {
/*  96 */     return (this.domainBareJid.equals((CharSequence)bareJid.getDomain()) && this.localpart.equals(bareJid.getLocalpart()));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isParentOf(FullJid fullJid) {
/* 101 */     return isParentOf(fullJid.asBareJid());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isParentOf(DomainBareJid domainBareJid) {
/* 106 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isParentOf(DomainFullJid domainFullJid) {
/* 111 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public DomainBareJid asDomainBareJid() {
/* 116 */     return this.domainBareJid;
/*     */   }
/*     */ 
/*     */   
/*     */   public Domainpart getDomain() {
/* 121 */     return this.domainBareJid.getDomain();
/*     */   }
/*     */ 
/*     */   
/*     */   public Jid withoutResource() {
/* 126 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasNoResource() {
/* 131 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public Localpart maybeGetLocalpart() {
/* 136 */     return getLocalpart();
/*     */   }
/*     */ 
/*     */   
/*     */   public Resourcepart maybeGetResourcepart() {
/* 141 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public JidWithLocalpart asJidWithLocalpartIfPossible() {
/* 146 */     return (JidWithLocalpart)this;
/*     */   }
/*     */ 
/*     */   
/*     */   public JidWithResource asJidWithResourcepartIfPossible() {
/* 151 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public BareJid asBareJid() {
/* 156 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jxmpp\jid\impl\LocalAndDomainpartJid.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */