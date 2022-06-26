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
/*    */ 
/*    */ @Deprecated
/*    */ public class PacketExtensionFilter
/*    */   implements StanzaFilter
/*    */ {
/*    */   private final String elementName;
/*    */   private final String namespace;
/*    */   
/*    */   public PacketExtensionFilter(String elementName, String namespace) {
/* 45 */     StringUtils.requireNotNullOrEmpty(namespace, "namespace must not be null or empty");
/*    */     
/* 47 */     this.elementName = elementName;
/* 48 */     this.namespace = namespace;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public PacketExtensionFilter(String namespace) {
/* 58 */     this(null, namespace);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public PacketExtensionFilter(ExtensionElement packetExtension) {
/* 67 */     this(packetExtension.getElementName(), packetExtension.getNamespace());
/*    */   }
/*    */   
/*    */   public boolean accept(Stanza packet) {
/* 71 */     return packet.hasExtension(this.elementName, this.namespace);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 76 */     return getClass().getSimpleName() + ": element=" + this.elementName + " namespace=" + this.namespace;
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jivesoftware\smack\filter\PacketExtensionFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */