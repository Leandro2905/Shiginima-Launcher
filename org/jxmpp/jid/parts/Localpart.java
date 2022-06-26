/*    */ package org.jxmpp.jid.parts;
/*    */ 
/*    */ import org.jxmpp.stringprep.XmppStringPrepUtil;
/*    */ import org.jxmpp.stringprep.XmppStringprepException;
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
/*    */ public class Localpart
/*    */   extends Part
/*    */ {
/*    */   private Localpart(String localpart) {
/* 25 */     super(localpart);
/*    */   }
/*    */   
/*    */   public static Localpart from(String localpart) throws XmppStringprepException {
/* 29 */     localpart = XmppStringPrepUtil.localprep(localpart);
/*    */     
/* 31 */     assertNotLongerThan1023BytesOrEmpty(localpart);
/* 32 */     return new Localpart(localpart);
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jxmpp\jid\parts\Localpart.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */