/*    */ package org.jivesoftware.smack.filter;
/*    */ 
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AndFilter
/*    */   extends AbstractListFilter
/*    */   implements StanzaFilter
/*    */ {
/*    */   public AndFilter() {}
/*    */   
/*    */   public AndFilter(StanzaFilter... filters) {
/* 44 */     super(filters);
/*    */   }
/*    */   
/*    */   public boolean accept(Stanza packet) {
/* 48 */     for (StanzaFilter filter : this.filters) {
/* 49 */       if (!filter.accept(packet)) {
/* 50 */         return false;
/*    */       }
/*    */     } 
/* 53 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jivesoftware\smack\filter\AndFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */