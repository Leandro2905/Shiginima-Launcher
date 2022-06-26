/*     */ package org.apache.logging.log4j.core.config;
/*     */ 
/*     */ import org.apache.logging.log4j.Level;
/*     */ import org.apache.logging.log4j.core.Appender;
/*     */ import org.apache.logging.log4j.core.Filter;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.appender.AppenderLoggingException;
/*     */ import org.apache.logging.log4j.core.filter.AbstractFilterable;
/*     */ import org.apache.logging.log4j.core.filter.Filterable;
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
/*     */ public class AppenderControl
/*     */   extends AbstractFilterable
/*     */ {
/*  32 */   private final ThreadLocal<AppenderControl> recursive = new ThreadLocal<AppenderControl>();
/*     */ 
/*     */ 
/*     */   
/*     */   private final Appender appender;
/*     */ 
/*     */   
/*     */   private final Level level;
/*     */ 
/*     */   
/*     */   private final int intLevel;
/*     */ 
/*     */ 
/*     */   
/*     */   public AppenderControl(Appender appender, Level level, Filter filter) {
/*  47 */     super(filter);
/*  48 */     this.appender = appender;
/*  49 */     this.level = level;
/*  50 */     this.intLevel = (level == null) ? Level.ALL.intLevel() : level.intLevel();
/*  51 */     start();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Appender getAppender() {
/*  59 */     return this.appender;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void callAppender(LogEvent event) {
/*  67 */     if (getFilter() != null) {
/*  68 */       Filter.Result r = getFilter().filter(event);
/*  69 */       if (r == Filter.Result.DENY) {
/*     */         return;
/*     */       }
/*     */     } 
/*  73 */     if (this.level != null && this.intLevel < event.getLevel().intLevel()) {
/*     */       return;
/*     */     }
/*  76 */     if (this.recursive.get() != null) {
/*  77 */       this.appender.getHandler().error("Recursive call to appender " + this.appender.getName());
/*     */       return;
/*     */     } 
/*     */     try {
/*  81 */       this.recursive.set(this);
/*     */       
/*  83 */       if (!this.appender.isStarted()) {
/*  84 */         this.appender.getHandler().error("Attempted to append to non-started appender " + this.appender.getName());
/*     */         
/*  86 */         if (!this.appender.ignoreExceptions()) {
/*  87 */           throw new AppenderLoggingException("Attempted to append to non-started appender " + this.appender.getName());
/*     */         }
/*     */       } 
/*     */ 
/*     */       
/*  92 */       if (this.appender instanceof Filterable && ((Filterable)this.appender).isFiltered(event)) {
/*     */         return;
/*     */       }
/*     */       
/*     */       try {
/*  97 */         this.appender.append(event);
/*  98 */       } catch (RuntimeException ex) {
/*  99 */         this.appender.getHandler().error("An exception occurred processing Appender " + this.appender.getName(), ex);
/* 100 */         if (!this.appender.ignoreExceptions()) {
/* 101 */           throw ex;
/*     */         }
/* 103 */       } catch (Exception ex) {
/* 104 */         this.appender.getHandler().error("An exception occurred processing Appender " + this.appender.getName(), ex);
/* 105 */         if (!this.appender.ignoreExceptions()) {
/* 106 */           throw new AppenderLoggingException(ex);
/*     */         }
/*     */       } 
/*     */     } finally {
/* 110 */       this.recursive.set(null);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\config\AppenderControl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */