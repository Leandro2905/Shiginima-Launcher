/*     */ package org.jivesoftware.smack.packet;
/*     */ 
/*     */ import org.jivesoftware.smack.util.StringUtils;
/*     */ import org.jivesoftware.smack.util.XmlStringBuilder;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class StreamOpen
/*     */   extends FullStreamElement
/*     */ {
/*     */   public static final String ELEMENT = "stream:stream";
/*     */   public static final String CLIENT_NAMESPACE = "jabber:client";
/*     */   public static final String SERVER_NAMESPACE = "jabber:server";
/*     */   public static final String VERSION = "1.0";
/*     */   private final String from;
/*     */   private final String to;
/*     */   private final String id;
/*     */   private final String lang;
/*     */   private final String contentNamespace;
/*     */   
/*     */   public StreamOpen(CharSequence to) {
/*  64 */     this(to, null, null, null, StreamContentNamespace.client);
/*     */   }
/*     */   
/*     */   public StreamOpen(CharSequence to, CharSequence from, String id) {
/*  68 */     this(to, from, id, "en", StreamContentNamespace.client);
/*     */   }
/*     */   
/*     */   public StreamOpen(CharSequence to, CharSequence from, String id, String lang, StreamContentNamespace ns) {
/*  72 */     this.to = StringUtils.maybeToString(to);
/*  73 */     this.from = StringUtils.maybeToString(from);
/*  74 */     this.id = id;
/*  75 */     this.lang = lang;
/*  76 */     switch (ns) {
/*     */       case client:
/*  78 */         this.contentNamespace = "jabber:client";
/*     */         return;
/*     */       case server:
/*  81 */         this.contentNamespace = "jabber:server";
/*     */         return;
/*     */     } 
/*  84 */     throw new IllegalStateException();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getNamespace() {
/*  90 */     return this.contentNamespace;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getElementName() {
/*  95 */     return "stream:stream";
/*     */   }
/*     */ 
/*     */   
/*     */   public XmlStringBuilder toXML() {
/* 100 */     XmlStringBuilder xml = new XmlStringBuilder(this);
/* 101 */     xml.attribute("to", this.to);
/* 102 */     xml.attribute("xmlns:stream", "http://etherx.jabber.org/streams");
/* 103 */     xml.attribute("version", "1.0");
/* 104 */     xml.optAttribute("from", this.from);
/* 105 */     xml.optAttribute("id", this.id);
/* 106 */     xml.xmllangAttribute(this.lang);
/* 107 */     xml.rightAngleBracket();
/* 108 */     return xml;
/*     */   }
/*     */   
/*     */   public enum StreamContentNamespace {
/* 112 */     client,
/* 113 */     server;
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jivesoftware\smack\packet\StreamOpen.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */