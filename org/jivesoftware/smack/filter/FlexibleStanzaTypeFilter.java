/*    */ package org.jivesoftware.smack.filter;
/*    */ 
/*    */ import java.lang.reflect.ParameterizedType;
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
/*    */ public abstract class FlexibleStanzaTypeFilter<S extends Stanza>
/*    */   implements StanzaFilter
/*    */ {
/*    */   protected final Class<S> stanzaType;
/*    */   
/*    */   public FlexibleStanzaTypeFilter(Class<S> packetType) {
/* 35 */     this.stanzaType = (Class<S>)Objects.requireNonNull(packetType, "Type must not be null");
/*    */   }
/*    */ 
/*    */   
/*    */   public FlexibleStanzaTypeFilter() {
/* 40 */     this.stanzaType = (Class<S>)((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public final boolean accept(Stanza packet) {
/* 46 */     if (this.stanzaType.isInstance(packet)) {
/* 47 */       return acceptSpecific((S)packet);
/*    */     }
/* 49 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   protected abstract boolean acceptSpecific(S paramS);
/*    */   
/*    */   public String toString() {
/* 56 */     return getClass().getSimpleName() + ": " + this.stanzaType.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jivesoftware\smack\filter\FlexibleStanzaTypeFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */