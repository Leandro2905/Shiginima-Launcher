/*    */ package org.jxmpp.stringprep;
/*    */ 
/*    */ import java.io.IOException;
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
/*    */ public class XmppStringprepException
/*    */   extends IOException
/*    */ {
/*    */   private static final long serialVersionUID = -8491853210107124624L;
/*    */   private final String causingString;
/*    */   
/*    */   public XmppStringprepException(String causingString, Exception exception) {
/* 31 */     super("XmppStringprepException caused by '" + causingString + "': " + exception);
/* 32 */     initCause(exception);
/* 33 */     this.causingString = causingString;
/*    */   }
/*    */   
/*    */   public XmppStringprepException(String causingString, String message) {
/* 37 */     super(message);
/* 38 */     this.causingString = causingString;
/*    */   }
/*    */   
/*    */   public String getCausingString() {
/* 42 */     return this.causingString;
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jxmpp\stringprep\XmppStringprepException.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */