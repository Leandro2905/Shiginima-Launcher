/*    */ package org.jivesoftware.smack.parsing;
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
/*    */ public class UnparsablePacket
/*    */ {
/*    */   private final CharSequence content;
/*    */   private final Exception e;
/*    */   
/*    */   public UnparsablePacket(CharSequence content, Exception e) {
/* 32 */     this.content = content;
/* 33 */     this.e = e;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Exception getParsingException() {
/* 41 */     return this.e;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public CharSequence getContent() {
/* 50 */     return this.content;
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jivesoftware\smack\parsing\UnparsablePacket.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */