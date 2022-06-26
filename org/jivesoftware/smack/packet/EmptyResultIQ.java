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
/*    */ public class EmptyResultIQ
/*    */   extends IQ
/*    */ {
/*    */   public EmptyResultIQ() {
/* 22 */     super((String)null, (String)null);
/* 23 */     setType(IQ.Type.result);
/*    */   }
/*    */   
/*    */   public EmptyResultIQ(IQ request) {
/* 27 */     this();
/* 28 */     if (request.getType() != IQ.Type.get && request.getType() != IQ.Type.set) {
/* 29 */       throw new IllegalArgumentException("IQ must be of type 'set' or 'get'. Original IQ: " + request.toXML());
/*    */     }
/*    */     
/* 32 */     setStanzaId(request.getStanzaId());
/* 33 */     setFrom(request.getTo());
/* 34 */     setTo(request.getFrom());
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected IQ.IQChildElementXmlStringBuilder getIQChildElementBuilder(IQ.IQChildElementXmlStringBuilder xml) {
/* 40 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jivesoftware\smack\packet\EmptyResultIQ.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */