/*    */ package org.jivesoftware.smack.packet;
/*    */ 
/*    */ import java.util.Collection;
/*    */ import java.util.Collections;
/*    */ import java.util.LinkedList;
/*    */ import java.util.List;
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
/*    */ public class Mechanisms
/*    */   implements ExtensionElement
/*    */ {
/*    */   public static final String ELEMENT = "mechanisms";
/*    */   public static final String NAMESPACE = "urn:ietf:params:xml:ns:xmpp-sasl";
/* 31 */   public final List<String> mechanisms = new LinkedList<>();
/*    */   
/*    */   public Mechanisms(String mechanism) {
/* 34 */     this.mechanisms.add(mechanism);
/*    */   }
/*    */   
/*    */   public Mechanisms(Collection<String> mechanisms) {
/* 38 */     this.mechanisms.addAll(mechanisms);
/*    */   }
/*    */ 
/*    */   
/*    */   public String getElementName() {
/* 43 */     return "mechanisms";
/*    */   }
/*    */ 
/*    */   
/*    */   public String getNamespace() {
/* 48 */     return "urn:ietf:params:xml:ns:xmpp-sasl";
/*    */   }
/*    */   
/*    */   public List<String> getMechanisms() {
/* 52 */     return Collections.unmodifiableList(this.mechanisms);
/*    */   }
/*    */ 
/*    */   
/*    */   public XmlStringBuilder toXML() {
/* 57 */     XmlStringBuilder xml = new XmlStringBuilder(this);
/* 58 */     xml.rightAngleBracket();
/* 59 */     for (String mechanism : this.mechanisms) {
/* 60 */       xml.element("mechanism", mechanism);
/*    */     }
/* 62 */     xml.closeElement(this);
/* 63 */     return xml;
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jivesoftware\smack\packet\Mechanisms.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */