/*     */ package org.jxmpp.jid.impl;
/*     */ 
/*     */ import org.jxmpp.jid.BareJid;
/*     */ import org.jxmpp.jid.DomainBareJid;
/*     */ import org.jxmpp.jid.DomainFullJid;
/*     */ import org.jxmpp.jid.FullJid;
/*     */ import org.jxmpp.jid.Jid;
/*     */ import org.jxmpp.jid.parts.Domainpart;
/*     */ import org.jxmpp.jid.parts.Localpart;
/*     */ import org.jxmpp.jid.parts.Resourcepart;
/*     */ import org.jxmpp.stringprep.XmppStringprepException;
/*     */ import org.jxmpp.util.XmppStringUtils;
/*     */ import org.jxmpp.util.cache.Cache;
/*     */ import org.jxmpp.util.cache.LruCache;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JidCreate
/*     */ {
/*  34 */   private static final Cache<String, Jid> JID_CACHE = (Cache<String, Jid>)new LruCache(100);
/*  35 */   private static final Cache<String, BareJid> BAREJID_CACHE = (Cache<String, BareJid>)new LruCache(100);
/*  36 */   private static final Cache<String, FullJid> FULLJID_CACHE = (Cache<String, FullJid>)new LruCache(100);
/*  37 */   private static final Cache<String, DomainBareJid> DOMAINJID_CACHE = (Cache<String, DomainBareJid>)new LruCache(100);
/*  38 */   private static final Cache<String, DomainFullJid> DOMAINRESOURCEJID_CACHE = (Cache<String, DomainFullJid>)new LruCache(100);
/*     */ 
/*     */   
/*     */   public static Jid from(CharSequence localpart, CharSequence domainpart, CharSequence resource) throws XmppStringprepException {
/*  42 */     return from(localpart.toString(), domainpart.toString(), resource.toString());
/*     */   }
/*     */   
/*     */   public static Jid from(String localpart, String domainpart, String resource) throws XmppStringprepException {
/*  46 */     String jidString = XmppStringUtils.completeJidFrom(localpart, domainpart, resource);
/*  47 */     Jid jid = (Jid)JID_CACHE.get(jidString);
/*  48 */     if (jid != null) {
/*  49 */       return jid;
/*     */     }
/*  51 */     if (localpart.length() > 0 && domainpart.length() > 0 && resource.length() > 0) {
/*  52 */       jid = new LocalDomainAndResourcepartJid(localpart, domainpart, resource);
/*  53 */     } else if (localpart.length() > 0 && domainpart.length() > 0 && resource.length() == 0) {
/*  54 */       jid = new LocalAndDomainpartJid(localpart, domainpart);
/*  55 */     } else if (localpart.length() == 0 && domainpart.length() > 0 && resource.length() == 0) {
/*  56 */       jid = new DomainpartJid(domainpart);
/*  57 */     } else if (localpart.length() == 0 && domainpart.length() > 0 && resource.length() > 0) {
/*  58 */       jid = new DomainAndResourcepartJid(domainpart, resource);
/*     */     } else {
/*  60 */       throw new IllegalArgumentException("Not a valid combination of localpart, domainpart and resource");
/*     */     } 
/*  62 */     JID_CACHE.put(jidString, jid);
/*  63 */     return jid;
/*     */   }
/*     */   
/*     */   public static Jid from(CharSequence jid) throws XmppStringprepException {
/*  67 */     return from(jid.toString());
/*     */   }
/*     */   
/*     */   public static Jid from(String jidString) throws XmppStringprepException {
/*  71 */     String localpart = XmppStringUtils.parseLocalpart(jidString);
/*  72 */     String domainpart = XmppStringUtils.parseDomain(jidString);
/*  73 */     String resource = XmppStringUtils.parseResource(jidString);
/*     */     try {
/*  75 */       return from(localpart, domainpart, resource);
/*  76 */     } catch (XmppStringprepException e) {
/*  77 */       throw new XmppStringprepException(jidString, e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static Jid fromUnescaped(CharSequence unescapedJid) throws XmppStringprepException {
/*  82 */     return fromUnescaped(unescapedJid.toString());
/*     */   }
/*     */   
/*     */   public static Jid fromUnescaped(String unescapedJidString) throws XmppStringprepException {
/*  86 */     String localpart = XmppStringUtils.parseLocalpart(unescapedJidString);
/*     */     
/*  88 */     localpart = XmppStringUtils.escapeLocalpart(localpart);
/*     */     
/*  90 */     String domainpart = XmppStringUtils.parseDomain(unescapedJidString);
/*  91 */     String resource = XmppStringUtils.parseResource(unescapedJidString);
/*     */     try {
/*  93 */       return from(localpart, domainpart, resource);
/*  94 */     } catch (XmppStringprepException e) {
/*  95 */       throw new XmppStringprepException(unescapedJidString, e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static BareJid bareFrom(CharSequence jid) throws XmppStringprepException {
/* 100 */     return bareFrom(jid.toString());
/*     */   }
/*     */   
/*     */   public static BareJid bareFrom(String jid) throws XmppStringprepException {
/* 104 */     BareJid bareJid = (BareJid)BAREJID_CACHE.get(jid);
/* 105 */     if (bareJid != null) {
/* 106 */       return bareJid;
/*     */     }
/*     */     
/* 109 */     String localpart = XmppStringUtils.parseLocalpart(jid);
/* 110 */     String domainpart = XmppStringUtils.parseDomain(jid);
/*     */     try {
/* 112 */       bareJid = new LocalAndDomainpartJid(localpart, domainpart);
/* 113 */     } catch (XmppStringprepException e) {
/* 114 */       throw new XmppStringprepException(jid, e);
/*     */     } 
/* 116 */     BAREJID_CACHE.put(jid, bareJid);
/* 117 */     return bareJid;
/*     */   }
/*     */   
/*     */   public static BareJid bareFrom(Localpart localpart, Domainpart domain) {
/* 121 */     return new LocalAndDomainpartJid(localpart, domain);
/*     */   }
/*     */   
/*     */   public static FullJid fullFrom(CharSequence jid) throws XmppStringprepException {
/* 125 */     return fullFrom(jid.toString());
/*     */   }
/*     */   
/*     */   public static FullJid fullFrom(String jid) throws XmppStringprepException {
/* 129 */     FullJid fullJid = (FullJid)FULLJID_CACHE.get(jid);
/* 130 */     if (fullJid != null) {
/* 131 */       return fullJid;
/*     */     }
/*     */     
/* 134 */     String localpart = XmppStringUtils.parseLocalpart(jid);
/* 135 */     String domainpart = XmppStringUtils.parseDomain(jid);
/* 136 */     String resource = XmppStringUtils.parseResource(jid);
/*     */     try {
/* 138 */       fullJid = fullFrom(localpart, domainpart, resource);
/* 139 */     } catch (XmppStringprepException e) {
/* 140 */       throw new XmppStringprepException(jid, e);
/*     */     } 
/* 142 */     FULLJID_CACHE.put(jid, fullJid);
/* 143 */     return fullJid;
/*     */   }
/*     */   
/*     */   public static FullJid fullFrom(String localpart, String domainpart, String resource) throws XmppStringprepException {
/*     */     FullJid fullJid;
/*     */     try {
/* 149 */       fullJid = new LocalDomainAndResourcepartJid(localpart, domainpart, resource);
/* 150 */     } catch (XmppStringprepException e) {
/* 151 */       throw new XmppStringprepException(localpart + '@' + domainpart + '/' + resource, e);
/*     */     } 
/* 153 */     return fullJid;
/*     */   }
/*     */   
/*     */   public static FullJid fullFrom(BareJid bareJid, Resourcepart resource) {
/* 157 */     return new LocalDomainAndResourcepartJid(bareJid, resource);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static DomainBareJid serverBareFrom(String jid) throws XmppStringprepException {
/* 169 */     return domainBareFrom(jid);
/*     */   }
/*     */   
/*     */   public static DomainBareJid domainBareFrom(CharSequence jid) throws XmppStringprepException {
/* 173 */     return domainBareFrom(jid.toString());
/*     */   }
/*     */   
/*     */   public static DomainBareJid domainBareFrom(String jid) throws XmppStringprepException {
/* 177 */     DomainBareJid domainJid = (DomainBareJid)DOMAINJID_CACHE.get(jid);
/* 178 */     if (domainJid != null) {
/* 179 */       return domainJid;
/*     */     }
/*     */     
/* 182 */     String domain = XmppStringUtils.parseDomain(jid);
/*     */     try {
/* 184 */       domainJid = new DomainpartJid(domain);
/* 185 */     } catch (XmppStringprepException e) {
/* 186 */       throw new XmppStringprepException(jid, e);
/*     */     } 
/* 188 */     DOMAINJID_CACHE.put(jid, domainJid);
/* 189 */     return domainJid;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static DomainFullJid serverFullFrom(String jid) throws XmppStringprepException {
/* 201 */     return donmainFullFrom(jid);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static DomainFullJid donmainFullFrom(String jid) throws XmppStringprepException {
/* 213 */     return domainFullFrom(jid);
/*     */   }
/*     */   
/*     */   public static DomainFullJid domainFullFrom(CharSequence jid) throws XmppStringprepException {
/* 217 */     return domainFullFrom(jid.toString());
/*     */   }
/*     */   
/*     */   public static DomainFullJid domainFullFrom(String jid) throws XmppStringprepException {
/* 221 */     DomainFullJid domainResourceJid = (DomainFullJid)DOMAINRESOURCEJID_CACHE.get(jid);
/* 222 */     if (domainResourceJid != null) {
/* 223 */       return domainResourceJid;
/*     */     }
/*     */     
/* 226 */     String domain = XmppStringUtils.parseDomain(jid);
/* 227 */     String resource = XmppStringUtils.parseResource(jid);
/*     */     try {
/* 229 */       domainResourceJid = new DomainAndResourcepartJid(domain, resource);
/* 230 */     } catch (XmppStringprepException e) {
/* 231 */       throw new XmppStringprepException(jid, e);
/*     */     } 
/* 233 */     DOMAINRESOURCEJID_CACHE.put(jid, domainResourceJid);
/* 234 */     return domainResourceJid;
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jxmpp\jid\impl\JidCreate.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */