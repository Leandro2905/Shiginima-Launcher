/*    */ package org.jivesoftware.smack.filter;
/*    */ 
/*    */ import org.jivesoftware.smack.packet.Message;
/*    */ import org.jivesoftware.smack.packet.Stanza;
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
/*    */ public class MessageWithBodiesFilter
/*    */   extends FlexibleStanzaTypeFilter<Message>
/*    */ {
/* 27 */   public static final StanzaFilter INSTANCE = new MessageWithBodiesFilter();
/*    */   
/*    */   private MessageWithBodiesFilter() {
/* 30 */     super(Message.class);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected boolean acceptSpecific(Message message) {
/* 36 */     return !message.getBodies().isEmpty();
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 41 */     return getClass().getSimpleName();
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jivesoftware\smack\filter\MessageWithBodiesFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */