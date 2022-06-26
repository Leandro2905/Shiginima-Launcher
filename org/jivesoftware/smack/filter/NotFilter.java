/*    */ package org.jivesoftware.smack.filter;
/*    */ 
/*    */ import org.jivesoftware.smack.packet.Stanza;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class NotFilter
/*    */   implements StanzaFilter
/*    */ {
/*    */   private final StanzaFilter filter;
/*    */   
/*    */   public NotFilter(StanzaFilter filter) {
/* 39 */     this.filter = (StanzaFilter)Objects.requireNonNull(filter, "Parameter must not be null.");
/*    */   }
/*    */   
/*    */   public boolean accept(Stanza packet) {
/* 43 */     return !this.filter.accept(packet);
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jivesoftware\smack\filter\NotFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */