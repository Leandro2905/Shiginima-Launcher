/*    */ package org.jivesoftware.smack.filter;
/*    */ 
/*    */ import org.jivesoftware.smack.packet.IQ;
/*    */ import org.jivesoftware.smack.packet.Message;
/*    */ import org.jivesoftware.smack.packet.Presence;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class StanzaTypeFilter
/*    */   implements StanzaFilter
/*    */ {
/* 38 */   public static final StanzaTypeFilter PRESENCE = new StanzaTypeFilter((Class)Presence.class);
/* 39 */   public static final StanzaTypeFilter MESSAGE = new StanzaTypeFilter((Class)Message.class);
/* 40 */   public static final StanzaTypeFilter IQ = new StanzaTypeFilter((Class)IQ.class);
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private final Class<? extends Stanza> packetType;
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public StanzaTypeFilter(Class<? extends Stanza> packetType) {
/* 51 */     this.packetType = packetType;
/*    */   }
/*    */   
/*    */   public boolean accept(Stanza packet) {
/* 55 */     return this.packetType.isInstance(packet);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 60 */     return getClass().getSimpleName() + ": " + this.packetType.getName();
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jivesoftware\smack\filter\StanzaTypeFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */