/*    */ package org.jivesoftware.smack.packet;
/*    */ 
/*    */ import org.jivesoftware.smack.util.Objects;
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
/*    */ public class ErrorIQ
/*    */   extends SimpleIQ
/*    */ {
/*    */   public static final String ELEMENT = "error";
/*    */   
/*    */   public ErrorIQ(XMPPError xmppError) {
/* 33 */     super("error", (String)null);
/* 34 */     Objects.requireNonNull(xmppError, "XMPPError must not be null");
/* 35 */     setType(IQ.Type.error);
/* 36 */     setError(xmppError);
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jivesoftware\smack\packet\ErrorIQ.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */