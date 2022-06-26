/*    */ package org.jivesoftware.smack.filter;
/*    */ 
/*    */ import org.jivesoftware.smack.packet.ExtensionElement;
/*    */ import org.jivesoftware.smack.packet.Stanza;
/*    */ import org.jivesoftware.smack.util.StringUtils;
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
/*    */ public class StanzaExtensionFilter
/*    */   implements StanzaFilter
/*    */ {
/*    */   private final String elementName;
/*    */   private final String namespace;
/*    */   
/*    */   public StanzaExtensionFilter(String elementName, String namespace) {
/* 43 */     StringUtils.requireNotNullOrEmpty(namespace, "namespace must not be null or empty");
/*    */     
/* 45 */     this.elementName = elementName;
/* 46 */     this.namespace = namespace;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public StanzaExtensionFilter(String namespace) {
/* 56 */     this(null, namespace);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public StanzaExtensionFilter(ExtensionElement packetExtension) {
/* 65 */     this(packetExtension.getElementName(), packetExtension.getNamespace());
/*    */   }
/*    */   
/*    */   public boolean accept(Stanza packet) {
/* 69 */     return packet.hasExtension(this.elementName, this.namespace);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 74 */     return getClass().getSimpleName() + ": element=" + this.elementName + " namespace=" + this.namespace;
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jivesoftware\smack\filter\StanzaExtensionFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */