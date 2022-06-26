/*    */ package org.jivesoftware.smack.filter;
/*    */ 
/*    */ import org.jivesoftware.smack.packet.Presence;
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
/*    */ public class PresenceTypeFilter
/*    */   extends FlexibleStanzaTypeFilter<Presence>
/*    */ {
/* 29 */   public static final PresenceTypeFilter AVAILABLE = new PresenceTypeFilter(Presence.Type.available);
/* 30 */   public static final PresenceTypeFilter UNAVAILABLE = new PresenceTypeFilter(Presence.Type.unavailable);
/* 31 */   public static final PresenceTypeFilter SUBSCRIBE = new PresenceTypeFilter(Presence.Type.subscribe);
/* 32 */   public static final PresenceTypeFilter SUBSCRIBED = new PresenceTypeFilter(Presence.Type.subscribed);
/* 33 */   public static final PresenceTypeFilter UNSUBSCRIBE = new PresenceTypeFilter(Presence.Type.unsubscribe);
/* 34 */   public static final PresenceTypeFilter UNSUBSCRIBED = new PresenceTypeFilter(Presence.Type.unsubscribed);
/* 35 */   public static final PresenceTypeFilter ERROR = new PresenceTypeFilter(Presence.Type.error);
/* 36 */   public static final PresenceTypeFilter PROBE = new PresenceTypeFilter(Presence.Type.probe);
/*    */   
/*    */   private final Presence.Type type;
/*    */   
/*    */   private PresenceTypeFilter(Presence.Type type) {
/* 41 */     super(Presence.class);
/* 42 */     this.type = (Presence.Type)Objects.requireNonNull(type, "type must not be null");
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean acceptSpecific(Presence presence) {
/* 47 */     return (presence.getType() == this.type);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 52 */     return getClass().getSimpleName() + ": type=" + this.type;
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jivesoftware\smack\filter\PresenceTypeFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */