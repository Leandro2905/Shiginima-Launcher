/*    */ package org.jivesoftware.smack.util;
/*    */ 
/*    */ import java.util.Collection;
/*    */ import org.jivesoftware.smack.packet.ExtensionElement;
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
/*    */ public class PacketUtil
/*    */ {
/*    */   @Deprecated
/*    */   public static <PE extends ExtensionElement> PE packetExtensionfromCollection(Collection<ExtensionElement> collection, String element, String namespace) {
/* 38 */     return extensionElementFrom(collection, element, namespace);
/*    */   }
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
/*    */   public static <PE extends ExtensionElement> PE extensionElementFrom(Collection<ExtensionElement> collection, String element, String namespace) {
/* 52 */     for (ExtensionElement packetExtension : collection) {
/* 53 */       if ((element == null || packetExtension.getElementName().equals(element)) && packetExtension.getNamespace().equals(namespace))
/*    */       {
/*    */         
/* 56 */         return (PE)packetExtension;
/*    */       }
/*    */     } 
/* 59 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jivesoftware\smac\\util\PacketUtil.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */