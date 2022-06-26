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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MessageTypeFilter
/*    */   extends FlexibleStanzaTypeFilter<Message>
/*    */ {
/* 32 */   public static final StanzaFilter NORMAL = new MessageTypeFilter(Message.Type.normal);
/* 33 */   public static final StanzaFilter CHAT = new MessageTypeFilter(Message.Type.chat);
/* 34 */   public static final StanzaFilter GROUPCHAT = new MessageTypeFilter(Message.Type.groupchat);
/* 35 */   public static final StanzaFilter HEADLINE = new MessageTypeFilter(Message.Type.headline);
/* 36 */   public static final StanzaFilter ERROR = new MessageTypeFilter(Message.Type.error);
/* 37 */   public static final StanzaFilter NORMAL_OR_CHAT = new OrFilter(new StanzaFilter[] { NORMAL, CHAT });
/* 38 */   public static final StanzaFilter NORMAL_OR_CHAT_OR_HEADLINE = new OrFilter(new StanzaFilter[] { NORMAL_OR_CHAT, HEADLINE });
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private final Message.Type type;
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private MessageTypeFilter(Message.Type type) {
/* 49 */     super(Message.class);
/* 50 */     this.type = type;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean acceptSpecific(Message message) {
/* 55 */     return (message.getType() == this.type);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 60 */     return getClass().getSimpleName() + ": type=" + this.type;
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jivesoftware\smack\filter\MessageTypeFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */