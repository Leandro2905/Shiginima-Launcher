/*    */ package org.jivesoftware.smack.packet;
/*    */ 
/*    */ import org.jivesoftware.smack.util.XmlStringBuilder;
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
/*    */ 
/*    */ public class Session
/*    */   extends SimpleIQ
/*    */ {
/*    */   public static final String ELEMENT = "session";
/*    */   public static final String NAMESPACE = "urn:ietf:params:xml:ns:xmpp-session";
/*    */   
/*    */   public Session() {
/* 41 */     super("session", "urn:ietf:params:xml:ns:xmpp-session");
/* 42 */     setType(IQ.Type.set);
/*    */   }
/*    */   
/*    */   public static class Feature
/*    */     implements ExtensionElement
/*    */   {
/*    */     public static final String OPTIONAL_ELEMENT = "optional";
/*    */     private final boolean optional;
/*    */     
/*    */     public Feature(boolean optional) {
/* 52 */       this.optional = optional;
/*    */     }
/*    */     
/*    */     public boolean isOptional() {
/* 56 */       return this.optional;
/*    */     }
/*    */ 
/*    */     
/*    */     public String getElementName() {
/* 61 */       return "session";
/*    */     }
/*    */ 
/*    */     
/*    */     public String getNamespace() {
/* 66 */       return "urn:ietf:params:xml:ns:xmpp-session";
/*    */     }
/*    */ 
/*    */     
/*    */     public XmlStringBuilder toXML() {
/* 71 */       XmlStringBuilder xml = new XmlStringBuilder(this);
/* 72 */       if (this.optional) {
/* 73 */         xml.rightAngleBracket();
/* 74 */         xml.emptyElement("optional");
/* 75 */         xml.closeElement(this);
/*    */       } else {
/* 77 */         xml.closeEmptyElement();
/*    */       } 
/* 79 */       return xml;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jivesoftware\smack\packet\Session.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */