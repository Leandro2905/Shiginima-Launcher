/*    */ package org.jivesoftware.smack.filter;
/*    */ 
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
/*    */ 
/*    */ 
/*    */ public class StanzaIdFilter
/*    */   implements StanzaFilter
/*    */ {
/*    */   private final String stanzaId;
/*    */   
/*    */   public StanzaIdFilter(Stanza stanza) {
/* 38 */     this(stanza.getStanzaId());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public StanzaIdFilter(String stanzaID) {
/* 47 */     this.stanzaId = (String)StringUtils.requireNotNullOrEmpty(stanzaID, "Stanza ID must not be null or empty.");
/*    */   }
/*    */   
/*    */   public boolean accept(Stanza stanza) {
/* 51 */     return this.stanzaId.equals(stanza.getStanzaId());
/*    */   }
/*    */   
/*    */   public String toString() {
/* 55 */     return getClass().getSimpleName() + ": id=" + this.stanzaId;
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jivesoftware\smack\filter\StanzaIdFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */