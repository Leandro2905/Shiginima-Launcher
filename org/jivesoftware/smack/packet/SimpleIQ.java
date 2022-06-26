/*    */ package org.jivesoftware.smack.packet;
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
/*    */ public abstract class SimpleIQ
/*    */   extends IQ
/*    */ {
/*    */   protected SimpleIQ(String childElementName, String childElementNamespace) {
/* 29 */     super(childElementName, childElementNamespace);
/*    */   }
/*    */ 
/*    */   
/*    */   protected IQ.IQChildElementXmlStringBuilder getIQChildElementBuilder(IQ.IQChildElementXmlStringBuilder xml) {
/* 34 */     xml.setEmptyElement();
/* 35 */     return xml;
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jivesoftware\smack\packet\SimpleIQ.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */