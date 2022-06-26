/*    */ package org.jivesoftware.smack.compress.packet;
/*    */ 
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ import org.jivesoftware.smack.packet.ExtensionElement;
/*    */ import org.jivesoftware.smack.packet.FullStreamElement;
/*    */ import org.jivesoftware.smack.packet.NamedElement;
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
/*    */ public class Compress
/*    */   extends FullStreamElement
/*    */ {
/*    */   public static final String ELEMENT = "compress";
/*    */   public static final String NAMESPACE = "http://jabber.org/protocol/compress";
/*    */   public final String method;
/*    */   
/*    */   public Compress(String method) {
/* 34 */     this.method = method;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getElementName() {
/* 39 */     return "compress";
/*    */   }
/*    */ 
/*    */   
/*    */   public String getNamespace() {
/* 44 */     return "http://jabber.org/protocol/compress";
/*    */   }
/*    */ 
/*    */   
/*    */   public XmlStringBuilder toXML() {
/* 49 */     XmlStringBuilder xml = new XmlStringBuilder((ExtensionElement)this);
/* 50 */     xml.rightAngleBracket();
/* 51 */     xml.element("method", this.method);
/* 52 */     xml.closeElement((NamedElement)this);
/* 53 */     return xml;
/*    */   }
/*    */   
/*    */   public static class Feature
/*    */     implements ExtensionElement {
/*    */     public static final String ELEMENT = "compression";
/*    */     public final List<String> methods;
/*    */     
/*    */     public Feature(List<String> methods) {
/* 62 */       this.methods = methods;
/*    */     }
/*    */     
/*    */     public List<String> getMethods() {
/* 66 */       return Collections.unmodifiableList(this.methods);
/*    */     }
/*    */ 
/*    */     
/*    */     public String getNamespace() {
/* 71 */       return "http://jabber.org/protocol/compress";
/*    */     }
/*    */ 
/*    */     
/*    */     public String getElementName() {
/* 76 */       return "compression";
/*    */     }
/*    */ 
/*    */     
/*    */     public XmlStringBuilder toXML() {
/* 81 */       XmlStringBuilder xml = new XmlStringBuilder(this);
/* 82 */       xml.rightAngleBracket();
/* 83 */       for (String method : this.methods) {
/* 84 */         xml.element("method", method);
/*    */       }
/* 86 */       xml.closeElement((NamedElement)this);
/* 87 */       return xml;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jivesoftware\smack\compress\packet\Compress.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */