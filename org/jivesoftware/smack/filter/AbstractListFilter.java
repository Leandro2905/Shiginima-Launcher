/*    */ package org.jivesoftware.smack.filter;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Arrays;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
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
/*    */ public abstract class AbstractListFilter
/*    */   implements StanzaFilter
/*    */ {
/*    */   protected final List<StanzaFilter> filters;
/*    */   
/*    */   protected AbstractListFilter() {
/* 41 */     this.filters = new ArrayList<>();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected AbstractListFilter(StanzaFilter... filters) {
/* 50 */     Objects.requireNonNull(filters, "Parameter must not be null.");
/* 51 */     for (StanzaFilter filter : filters) {
/* 52 */       Objects.requireNonNull(filter, "Parameter must not be null.");
/*    */     }
/* 54 */     this.filters = new ArrayList<>(Arrays.asList(filters));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void addFilter(StanzaFilter filter) {
/* 64 */     Objects.requireNonNull(filter, "Parameter must not be null.");
/* 65 */     this.filters.add(filter);
/*    */   }
/*    */ 
/*    */   
/*    */   public final String toString() {
/* 70 */     StringBuilder sb = new StringBuilder();
/* 71 */     sb.append(getClass().getSimpleName());
/* 72 */     sb.append(": (");
/* 73 */     for (Iterator<StanzaFilter> it = this.filters.iterator(); it.hasNext(); ) {
/* 74 */       StanzaFilter filter = it.next();
/* 75 */       sb.append(filter.toString());
/* 76 */       if (it.hasNext()) {
/* 77 */         sb.append(", ");
/*    */       }
/*    */     } 
/* 80 */     sb.append(")");
/* 81 */     return sb.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jivesoftware\smack\filter\AbstractListFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */