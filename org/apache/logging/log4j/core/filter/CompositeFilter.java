/*     */ package org.apache.logging.log4j.core.filter;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import org.apache.logging.log4j.Level;
/*     */ import org.apache.logging.log4j.Marker;
/*     */ import org.apache.logging.log4j.core.AbstractLifeCycle;
/*     */ import org.apache.logging.log4j.core.Filter;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.Logger;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginElement;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginFactory;
/*     */ import org.apache.logging.log4j.message.Message;
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
/*     */ @Plugin(name = "filters", category = "Core", printObject = true)
/*     */ public final class CompositeFilter
/*     */   extends AbstractLifeCycle
/*     */   implements Iterable<Filter>, Filter
/*     */ {
/*     */   private final List<Filter> filters;
/*     */   
/*     */   private CompositeFilter() {
/*  45 */     this.filters = new ArrayList<Filter>();
/*     */   }
/*     */   
/*     */   private CompositeFilter(List<Filter> filters) {
/*  49 */     if (filters == null) {
/*  50 */       this.filters = Collections.unmodifiableList(new ArrayList<Filter>());
/*     */       return;
/*     */     } 
/*  53 */     this.filters = Collections.unmodifiableList(filters);
/*     */   }
/*     */   
/*     */   public CompositeFilter addFilter(Filter filter) {
/*  57 */     if (filter == null)
/*     */     {
/*  59 */       return this;
/*     */     }
/*  61 */     List<Filter> filterList = new ArrayList<Filter>(this.filters);
/*  62 */     filterList.add(filter);
/*  63 */     return new CompositeFilter(Collections.unmodifiableList(filterList));
/*     */   }
/*     */   
/*     */   public CompositeFilter removeFilter(Filter filter) {
/*  67 */     if (filter == null)
/*     */     {
/*  69 */       return this;
/*     */     }
/*  71 */     List<Filter> filterList = new ArrayList<Filter>(this.filters);
/*  72 */     filterList.remove(filter);
/*  73 */     return new CompositeFilter(Collections.unmodifiableList(filterList));
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<Filter> iterator() {
/*  78 */     return this.filters.iterator();
/*     */   }
/*     */   
/*     */   public List<Filter> getFilters() {
/*  82 */     return this.filters;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/*  91 */     return this.filters.isEmpty();
/*     */   }
/*     */   
/*     */   public int size() {
/*  95 */     return this.filters.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public void start() {
/* 100 */     setStarting();
/* 101 */     for (Filter filter : this.filters) {
/* 102 */       filter.start();
/*     */     }
/* 104 */     setStarted();
/*     */   }
/*     */ 
/*     */   
/*     */   public void stop() {
/* 109 */     setStopping();
/* 110 */     for (Filter filter : this.filters) {
/* 111 */       filter.stop();
/*     */     }
/* 113 */     setStopped();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result getOnMismatch() {
/* 123 */     return Filter.Result.NEUTRAL;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result getOnMatch() {
/* 133 */     return Filter.Result.NEUTRAL;
/*     */   }
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
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object... params) {
/* 154 */     Filter.Result result = Filter.Result.NEUTRAL;
/* 155 */     for (Filter filter : this.filters) {
/* 156 */       result = filter.filter(logger, level, marker, msg, params);
/* 157 */       if (result == Filter.Result.ACCEPT || result == Filter.Result.DENY) {
/* 158 */         return result;
/*     */       }
/*     */     } 
/* 161 */     return result;
/*     */   }
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
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, Object msg, Throwable t) {
/* 182 */     Filter.Result result = Filter.Result.NEUTRAL;
/* 183 */     for (Filter filter : this.filters) {
/* 184 */       result = filter.filter(logger, level, marker, msg, t);
/* 185 */       if (result == Filter.Result.ACCEPT || result == Filter.Result.DENY) {
/* 186 */         return result;
/*     */       }
/*     */     } 
/* 189 */     return result;
/*     */   }
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
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, Message msg, Throwable t) {
/* 210 */     Filter.Result result = Filter.Result.NEUTRAL;
/* 211 */     for (Filter filter : this.filters) {
/* 212 */       result = filter.filter(logger, level, marker, msg, t);
/* 213 */       if (result == Filter.Result.ACCEPT || result == Filter.Result.DENY) {
/* 214 */         return result;
/*     */       }
/*     */     } 
/* 217 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(LogEvent event) {
/* 229 */     Filter.Result result = Filter.Result.NEUTRAL;
/* 230 */     for (Filter filter : this.filters) {
/* 231 */       result = filter.filter(event);
/* 232 */       if (result == Filter.Result.ACCEPT || result == Filter.Result.DENY) {
/* 233 */         return result;
/*     */       }
/*     */     } 
/* 236 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 241 */     StringBuilder sb = new StringBuilder();
/* 242 */     for (Filter filter : this.filters) {
/* 243 */       if (sb.length() == 0) {
/* 244 */         sb.append('{');
/*     */       } else {
/* 246 */         sb.append(", ");
/*     */       } 
/* 248 */       sb.append(filter.toString());
/*     */     } 
/* 250 */     if (sb.length() > 0) {
/* 251 */       sb.append('}');
/*     */     }
/* 253 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @PluginFactory
/*     */   public static CompositeFilter createFilters(@PluginElement("Filters") Filter[] filters) {
/* 265 */     List<Filter> filterList = (filters == null || filters.length == 0) ? new ArrayList<Filter>() : Arrays.<Filter>asList(filters);
/*     */     
/* 267 */     return new CompositeFilter(filterList);
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\filter\CompositeFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */