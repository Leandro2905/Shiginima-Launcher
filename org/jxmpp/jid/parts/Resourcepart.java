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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Resourcepart
/*    */   extends Part
/*    */ {
/* 32 */   public static final Resourcepart EMPTY = new Resourcepart("");
/*    */   
/*    */   private Resourcepart(String resource) {
/* 35 */     super(resource);
/*    */   }
/*    */   
/*    */   public static Resourcepart from(String resource) throws XmppStringprepException {
/* 39 */     resource = XmppStringPrepUtil.resourceprep(resource);
/*    */     
/* 41 */     assertNotLongerThan1023BytesOrEmpty(resource);
/* 42 */     return new Resourcepart(resource);
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jxmpp\jid\parts\Resourcepart.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */