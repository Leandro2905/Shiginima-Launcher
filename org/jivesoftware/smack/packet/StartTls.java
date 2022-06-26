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
/*    */ public class StartTls
/*    */   extends FullStreamElement
/*    */ {
/*    */   public static final String ELEMENT = "starttls";
/*    */   public static final String NAMESPACE = "urn:ietf:params:xml:ns:xmpp-tls";
/*    */   private final boolean required;
/*    */   
/*    */   public StartTls() {
/* 29 */     this(false);
/*    */   }
/*    */   
/*    */   public StartTls(boolean required) {
/* 33 */     this.required = required;
/*    */   }
/*    */   
/*    */   public boolean required() {
/* 37 */     return this.required;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getElementName() {
/* 42 */     return "starttls";
/*    */   }
/*    */ 
/*    */   
/*    */   public String getNamespace() {
/* 47 */     return "urn:ietf:params:xml:ns:xmpp-tls";
/*    */   }
/*    */ 
/*    */   
/*    */   public XmlStringBuilder toXML() {
/* 52 */     XmlStringBuilder xml = new XmlStringBuilder(this);
/* 53 */     xml.rightAngleBracket();
/* 54 */     xml.condEmptyElement(this.required, "required");
/* 55 */     xml.closeElement(this);
/* 56 */     return xml;
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jivesoftware\smack\packet\StartTls.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */