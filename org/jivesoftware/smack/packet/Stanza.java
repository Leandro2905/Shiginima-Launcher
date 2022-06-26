/*     */ package org.jivesoftware.smack.packet;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Set;
/*     */ import org.jivesoftware.smack.packet.id.StanzaIdUtil;
/*     */ import org.jivesoftware.smack.util.MultiMap;
/*     */ import org.jivesoftware.smack.util.PacketUtil;
/*     */ import org.jivesoftware.smack.util.StringUtils;
/*     */ import org.jivesoftware.smack.util.XmlStringBuilder;
/*     */ import org.jxmpp.jid.Jid;
/*     */ import org.jxmpp.jid.impl.JidCreate;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class Stanza
/*     */   implements TopLevelStreamElement
/*     */ {
/*     */   public static final String TEXT = "text";
/*     */   public static final String ITEM = "item";
/*  56 */   protected static final String DEFAULT_LANGUAGE = Locale.getDefault().getLanguage().toLowerCase(Locale.US);
/*     */ 
/*     */   
/*  59 */   private final MultiMap<String, ExtensionElement> packetExtensions = new MultiMap();
/*     */   
/*  61 */   private String id = null;
/*     */   private Jid to;
/*     */   private Jid from;
/*  64 */   private XMPPError error = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String language;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Stanza() {
/*  79 */     this(StanzaIdUtil.newStanzaId());
/*     */   }
/*     */   
/*     */   protected Stanza(String stanzaId) {
/*  83 */     setStanzaId(stanzaId);
/*     */   }
/*     */   
/*     */   protected Stanza(Stanza p) {
/*  87 */     this.id = p.getStanzaId();
/*  88 */     this.to = p.getTo();
/*  89 */     this.from = p.getFrom();
/*  90 */     this.error = p.error;
/*     */ 
/*     */     
/*  93 */     for (ExtensionElement pe : p.getExtensions()) {
/*  94 */       addExtension(pe);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getStanzaId() {
/* 104 */     return this.id;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public String getPacketID() {
/* 114 */     return getStanzaId();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setStanzaId(String id) {
/* 124 */     if (id != null) {
/* 125 */       StringUtils.requireNotNullOrEmpty(id, "id must either be null or not the empty String");
/*     */     }
/* 127 */     this.id = id;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void setPacketID(String packetID) {
/* 137 */     setStanzaId(packetID);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasStanzaIdSet() {
/* 149 */     return (this.id != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Jid getTo() {
/* 161 */     return this.to;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void setTo(String to) {
/*     */     Jid jid;
/*     */     try {
/* 176 */       jid = JidCreate.from(to);
/*     */     }
/* 178 */     catch (XmppStringprepException e) {
/* 179 */       throw new IllegalArgumentException(e);
/*     */     } 
/* 181 */     setTo(jid);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTo(Jid to) {
/* 191 */     this.to = to;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Jid getFrom() {
/* 203 */     return this.from;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void setFrom(String from) {
/*     */     Jid jid;
/*     */     try {
/* 219 */       jid = JidCreate.from(from);
/*     */     }
/* 221 */     catch (XmppStringprepException e) {
/* 222 */       throw new IllegalArgumentException(e);
/*     */     } 
/* 224 */     setFrom(jid);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFrom(Jid from) {
/* 235 */     this.from = from;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public XMPPError getError() {
/* 245 */     return this.error;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setError(XMPPError error) {
/* 254 */     this.error = error;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getLanguage() {
/* 263 */     return this.language;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLanguage(String language) {
/* 272 */     this.language = language;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<ExtensionElement> getExtensions() {
/* 281 */     synchronized (this.packetExtensions) {
/*     */       
/* 283 */       return this.packetExtensions.values();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<ExtensionElement> getExtensions(String elementName, String namespace) {
/* 299 */     StringUtils.requireNotNullOrEmpty(elementName, "elementName must not be null or empty");
/* 300 */     StringUtils.requireNotNullOrEmpty(namespace, "namespace must not be null or empty");
/* 301 */     String key = XmppStringUtils.generateKey(elementName, namespace);
/* 302 */     return this.packetExtensions.getAll(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ExtensionElement getExtension(String namespace) {
/* 315 */     return PacketUtil.extensionElementFrom(getExtensions(), null, namespace);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <PE extends ExtensionElement> PE getExtension(String elementName, String namespace) {
/*     */     ExtensionElement packetExtension;
/* 335 */     if (namespace == null) {
/* 336 */       return null;
/*     */     }
/* 338 */     String key = XmppStringUtils.generateKey(elementName, namespace);
/*     */     
/* 340 */     synchronized (this.packetExtensions) {
/* 341 */       packetExtension = (ExtensionElement)this.packetExtensions.getFirst(key);
/*     */     } 
/* 343 */     if (packetExtension == null) {
/* 344 */       return null;
/*     */     }
/* 346 */     return (PE)packetExtension;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addExtension(ExtensionElement extension) {
/* 355 */     if (extension == null)
/* 356 */       return;  String key = XmppStringUtils.generateKey(extension.getElementName(), extension.getNamespace());
/* 357 */     synchronized (this.packetExtensions) {
/* 358 */       this.packetExtensions.put(key, extension);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addExtensions(Collection<ExtensionElement> extensions) {
/* 368 */     if (extensions == null)
/* 369 */       return;  for (ExtensionElement packetExtension : extensions) {
/* 370 */       addExtension(packetExtension);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasExtension(String elementName, String namespace) {
/* 385 */     if (elementName == null) {
/* 386 */       return hasExtension(namespace);
/*     */     }
/* 388 */     String key = XmppStringUtils.generateKey(elementName, namespace);
/* 389 */     synchronized (this.packetExtensions) {
/* 390 */       return this.packetExtensions.containsKey(key);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasExtension(String namespace) {
/* 401 */     synchronized (this.packetExtensions) {
/* 402 */       for (ExtensionElement packetExtension : this.packetExtensions.values()) {
/* 403 */         if (packetExtension.getNamespace().equals(namespace)) {
/* 404 */           return true;
/*     */         }
/*     */       } 
/*     */     } 
/* 408 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ExtensionElement removeExtension(String elementName, String namespace) {
/* 419 */     String key = XmppStringUtils.generateKey(elementName, namespace);
/* 420 */     synchronized (this.packetExtensions) {
/* 421 */       return (ExtensionElement)this.packetExtensions.remove(key);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ExtensionElement removeExtension(ExtensionElement extension) {
/* 432 */     return removeExtension(extension.getElementName(), extension.getNamespace());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 438 */     return toXML().toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final XmlStringBuilder getExtensionsXML() {
/* 449 */     XmlStringBuilder xml = new XmlStringBuilder();
/*     */     
/* 451 */     for (ExtensionElement extension : getExtensions()) {
/* 452 */       xml.append(extension.toXML());
/*     */     }
/* 454 */     return xml;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getDefaultLanguage() {
/* 463 */     return DEFAULT_LANGUAGE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addCommonAttributes(XmlStringBuilder xml) {
/* 472 */     xml.optAttribute("to", (CharSequence)getTo());
/* 473 */     xml.optAttribute("from", (CharSequence)getFrom());
/* 474 */     xml.optAttribute("id", getStanzaId());
/* 475 */     xml.xmllangAttribute(getLanguage());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void appendErrorIfExists(XmlStringBuilder xml) {
/* 484 */     XMPPError error = getError();
/* 485 */     if (error != null)
/* 486 */       xml.append(error.toXML()); 
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jivesoftware\smack\packet\Stanza.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */