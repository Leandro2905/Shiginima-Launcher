/*    */ package org.jxmpp.jid.parts;
/*    */ 
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
/*    */ public abstract class Part
/*    */   implements CharSequence
/*    */ {
/*    */   private final String part;
/*    */   
/*    */   protected Part(String part) {
/* 26 */     this.part = part;
/*    */   }
/*    */ 
/*    */   
/*    */   public final int length() {
/* 31 */     return this.part.length();
/*    */   }
/*    */ 
/*    */   
/*    */   public final char charAt(int index) {
/* 36 */     return this.part.charAt(index);
/*    */   }
/*    */ 
/*    */   
/*    */   public final CharSequence subSequence(int start, int end) {
/* 41 */     return this.part.subSequence(start, end);
/*    */   }
/*    */ 
/*    */   
/*    */   public final String toString() {
/* 46 */     return this.part;
/*    */   }
/*    */ 
/*    */   
/*    */   public final boolean equals(Object other) {
/* 51 */     if (this == other) {
/* 52 */       return true;
/*    */     }
/* 54 */     return this.part.equals(other.toString());
/*    */   }
/*    */ 
/*    */   
/*    */   public final int hashCode() {
/* 59 */     return this.part.hashCode();
/*    */   }
/*    */   
/*    */   protected static void assertNotLongerThan1023BytesOrEmpty(String string) throws XmppStringprepException {
/* 63 */     char[] bytes = string.toCharArray();
/*    */ 
/*    */ 
/*    */     
/* 67 */     if (bytes.length > 1023)
/* 68 */       throw new XmppStringprepException(string, "Given string is longer then 1023 bytes"); 
/* 69 */     if (bytes.length == 0)
/* 70 */       throw new XmppStringprepException(string, "Argument can't be the empty string"); 
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jxmpp\jid\parts\Part.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */