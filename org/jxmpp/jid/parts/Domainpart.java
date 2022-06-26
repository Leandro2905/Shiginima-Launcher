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
/*    */ public class Domainpart
/*    */   extends Part
/*    */ {
/*    */   private Domainpart(String domain) {
/* 25 */     super(domain);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static Domainpart from(String domain) throws XmppStringprepException {
/* 32 */     if (domain.length() > 0 && domain.charAt(domain.length() - 1) == '.') {
/* 33 */       domain = domain.substring(0, domain.length() - 1);
/*    */     }
/* 35 */     domain = XmppStringPrepUtil.domainprep(domain);
/*    */     
/* 37 */     assertNotLongerThan1023BytesOrEmpty(domain);
/* 38 */     return new Domainpart(domain);
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jxmpp\jid\parts\Domainpart.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */