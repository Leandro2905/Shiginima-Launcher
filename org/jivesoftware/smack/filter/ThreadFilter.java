/*    */ package org.jivesoftware.smack.filter;
/*    */ 
/*    */ import org.jivesoftware.smack.packet.Message;
/*    */ import org.jivesoftware.smack.packet.Stanza;
/*    */ import org.jivesoftware.smack.util.StringUtils;
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
/*    */ 
/*    */ public class ThreadFilter
/*    */   extends FlexibleStanzaTypeFilter<Message>
/*    */   implements StanzaFilter
/*    */ {
/*    */   private final String thread;
/*    */   
/*    */   public ThreadFilter(String thread) {
/* 38 */     StringUtils.requireNotNullOrEmpty(thread, "Thread must not be null or empty.");
/* 39 */     this.thread = thread;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean acceptSpecific(Message message) {
/* 44 */     return this.thread.equals(message.getThread());
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 49 */     return getClass().getSimpleName() + ": thread=" + this.thread;
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jivesoftware\smack\filter\ThreadFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */