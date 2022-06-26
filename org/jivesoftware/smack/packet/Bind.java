/*    */ package org.jivesoftware.smack.packet;
/*    */ 
/*    */ import org.jxmpp.jid.FullJid;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Bind
/*    */   extends IQ
/*    */ {
/*    */   public static final String ELEMENT = "bind";
/*    */   public static final String NAMESPACE = "urn:ietf:params:xml:ns:xmpp-bind";
/*    */   private final String resource;
/*    */   private final FullJid jid;
/*    */   
/*    */   public Bind(String resource, FullJid jid) {
/* 42 */     super("bind", "urn:ietf:params:xml:ns:xmpp-bind");
/* 43 */     this.resource = resource;
/* 44 */     this.jid = jid;
/*    */   }
/*    */   
/*    */   public String getResource() {
/* 48 */     return this.resource;
/*    */   }
/*    */   
/*    */   public FullJid getJid() {
/* 52 */     return this.jid;
/*    */   }
/*    */   
/*    */   public static Bind newSet(String resource) {
/* 56 */     Bind bind = new Bind(resource, null);
/* 57 */     bind.setType(IQ.Type.set);
/* 58 */     return bind;
/*    */   }
/*    */   
/*    */   public static Bind newResult(FullJid jid) {
/* 62 */     return new Bind(null, jid);
/*    */   }
/*    */ 
/*    */   
/*    */   protected IQ.IQChildElementXmlStringBuilder getIQChildElementBuilder(IQ.IQChildElementXmlStringBuilder xml) {
/* 67 */     xml.rightAngleBracket();
/* 68 */     xml.optElement("resource", this.resource);
/* 69 */     xml.optElement("jid", (CharSequence)this.jid);
/* 70 */     return xml;
/*    */   }
/*    */   
/*    */   public static class Feature
/*    */     implements ExtensionElement {
/* 75 */     public static final Feature INSTANCE = new Feature();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     public String getElementName() {
/* 82 */       return "bind";
/*    */     }
/*    */ 
/*    */     
/*    */     public String getNamespace() {
/* 87 */       return "urn:ietf:params:xml:ns:xmpp-bind";
/*    */     }
/*    */ 
/*    */     
/*    */     public String toXML() {
/* 92 */       return "<bind xmlns='urn:ietf:params:xml:ns:xmpp-bind'/>";
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jivesoftware\smack\packet\Bind.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */