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
/*    */ public class UnparsedIQ
/*    */   extends IQ
/*    */ {
/*    */   private final CharSequence content;
/*    */   
/*    */   public UnparsedIQ(String element, String namespace, CharSequence content) {
/* 25 */     super(element, namespace);
/* 26 */     this.content = content;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public CharSequence getContent() {
/* 32 */     return this.content;
/*    */   }
/*    */ 
/*    */   
/*    */   protected IQ.IQChildElementXmlStringBuilder getIQChildElementBuilder(IQ.IQChildElementXmlStringBuilder xml) {
/* 37 */     throw new UnsupportedOperationException();
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jivesoftware\smack\packet\UnparsedIQ.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */