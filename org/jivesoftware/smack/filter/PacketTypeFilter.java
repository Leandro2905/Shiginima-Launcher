/*    */ package org.jivesoftware.smack.filter;
/*    */ 
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
/*    */ 
/*    */ @Deprecated
/*    */ public class PacketTypeFilter
/*    */   implements StanzaFilter
/*    */ {
/* 39 */   public static final PacketTypeFilter PRESENCE = new PacketTypeFilter((Class)Presence.class);
/* 40 */   public static final PacketTypeFilter MESSAGE = new PacketTypeFilter((Class)Message.class);
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private final Class<? extends Stanza> packetType;
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public PacketTypeFilter(Class<? extends Stanza> packetType) {
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


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jivesoftware\smack\filter\PacketTypeFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */