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
/*    */ 
/*    */ 
/*    */ @Deprecated
/*    */ public class PacketIDFilter
/*    */   implements StanzaFilter
/*    */ {
/*    */   private final String packetID;
/*    */   
/*    */   @Deprecated
/*    */   public PacketIDFilter(Stanza packet) {
/* 42 */     this(packet.getStanzaId());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Deprecated
/*    */   public PacketIDFilter(String packetID) {
/* 53 */     StringUtils.requireNotNullOrEmpty(packetID, "Packet ID must not be null or empty.");
/* 54 */     this.packetID = packetID;
/*    */   }
/*    */   
/*    */   public boolean accept(Stanza packet) {
/* 58 */     return this.packetID.equals(packet.getStanzaId());
/*    */   }
/*    */   
/*    */   public String toString() {
/* 62 */     return getClass().getSimpleName() + ": id=" + this.packetID;
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jivesoftware\smack\filter\PacketIDFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */