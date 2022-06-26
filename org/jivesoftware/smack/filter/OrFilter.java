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
/*    */ public class OrFilter
/*    */   extends AbstractListFilter
/*    */   implements StanzaFilter
/*    */ {
/*    */   public OrFilter() {}
/*    */   
/*    */   public OrFilter(StanzaFilter... filters) {
/* 44 */     super(filters);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean accept(Stanza packet) {
/* 49 */     for (StanzaFilter filter : this.filters) {
/* 50 */       if (filter.accept(packet)) {
/* 51 */         return true;
/*    */       }
/*    */     } 
/* 54 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jivesoftware\smack\filter\OrFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */