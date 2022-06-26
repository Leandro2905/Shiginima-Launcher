/*     */ package org.apache.logging.log4j.core.filter;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ import org.apache.logging.log4j.core.AbstractLifeCycle;
/*     */ import org.apache.logging.log4j.core.Filter;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractFilterable
/*     */   extends AbstractLifeCycle
/*     */   implements Filterable
/*     */ {
/*     */   private volatile Filter filter;
/*     */   
/*     */   protected AbstractFilterable(Filter filter) {
/*  36 */     this.filter = filter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected AbstractFilterable() {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter getFilter() {
/*  48 */     return this.filter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void addFilter(Filter filter) {
/*  57 */     if (this.filter == null) {
/*  58 */       this.filter = filter;
/*  59 */     } else if (filter instanceof CompositeFilter) {
/*  60 */       this.filter = ((CompositeFilter)this.filter).addFilter(filter);
/*     */     } else {
/*  62 */       Filter[] filters = { this.filter, filter };
/*  63 */       this.filter = CompositeFilter.createFilters(filters);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void removeFilter(Filter filter) {
/*  73 */     if (this.filter == filter) {
/*  74 */       this.filter = null;
/*  75 */     } else if (filter instanceof CompositeFilter) {
/*  76 */       CompositeFilter composite = (CompositeFilter)filter;
/*  77 */       composite = composite.removeFilter(filter);
/*  78 */       if (composite.size() > 1) {
/*  79 */         this.filter = composite;
/*  80 */       } else if (composite.size() == 1) {
/*  81 */         Iterator<Filter> iter = composite.iterator();
/*  82 */         this.filter = iter.next();
/*     */       } else {
/*  84 */         this.filter = null;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasFilter() {
/*  95 */     return (this.filter != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void start() {
/* 103 */     setStarting();
/* 104 */     if (this.filter != null) {
/* 105 */       this.filter.start();
/*     */     }
/* 107 */     setStarted();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void stop() {
/* 115 */     setStopping();
/* 116 */     if (this.filter != null) {
/* 117 */       this.filter.stop();
/*     */     }
/* 119 */     setStopped();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isFiltered(LogEvent event) {
/* 129 */     return (this.filter != null && this.filter.filter(event) == Filter.Result.DENY);
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\filter\AbstractFilterable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */