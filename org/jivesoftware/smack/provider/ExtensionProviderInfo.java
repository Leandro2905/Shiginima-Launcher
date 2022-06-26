/*    */ package org.jivesoftware.smack.provider;
/*    */ 
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
/*    */ 
/*    */ public final class ExtensionProviderInfo
/*    */   extends AbstractProviderInfo
/*    */ {
/*    */   public ExtensionProviderInfo(String elementName, String namespace, ExtensionElementProvider<ExtensionElement> extProvider) {
/* 38 */     super(elementName, namespace, extProvider);
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jivesoftware\smack\provider\ExtensionProviderInfo.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */