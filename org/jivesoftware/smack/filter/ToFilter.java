/*    */ package org.jivesoftware.smack.filter;
/*    */ 
/*    */ import org.jivesoftware.smack.packet.Stanza;
/*    */ import org.jxmpp.jid.Jid;
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
/*    */ public class ToFilter
/*    */   implements StanzaFilter
/*    */ {
/*    */   private final Jid to;
/*    */   
/*    */   public ToFilter(Jid to) {
/* 27 */     this.to = to;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean accept(Stanza packet) {
/* 32 */     Jid packetTo = packet.getTo();
/* 33 */     if (packetTo == null) {
/* 34 */       return false;
/*    */     }
/* 36 */     return packetTo.equals((CharSequence)this.to);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 41 */     return getClass().getSimpleName() + ": to=" + this.to;
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jivesoftware\smack\filter\ToFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */