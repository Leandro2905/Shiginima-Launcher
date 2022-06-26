/*    */ package org.jivesoftware.smack.provider;
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
/*    */ abstract class AbstractProviderInfo
/*    */ {
/*    */   private String element;
/*    */   private String ns;
/*    */   private Object provider;
/*    */   
/*    */   AbstractProviderInfo(String elementName, String namespace, Object iqOrExtProvider) {
/* 25 */     this.element = elementName;
/* 26 */     this.ns = namespace;
/* 27 */     this.provider = iqOrExtProvider;
/*    */   }
/*    */   
/*    */   public String getElementName() {
/* 31 */     return this.element;
/*    */   }
/*    */   
/*    */   public String getNamespace() {
/* 35 */     return this.ns;
/*    */   }
/*    */   
/*    */   Object getProvider() {
/* 39 */     return this.provider;
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jivesoftware\smack\provider\AbstractProviderInfo.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */