/*     */ package org.apache.logging.log4j.core.appender;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import org.apache.logging.log4j.core.Appender;
/*     */ import org.apache.logging.log4j.core.ErrorHandler;
/*     */ import org.apache.logging.log4j.core.Filter;
/*     */ import org.apache.logging.log4j.core.Layout;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.filter.AbstractFilterable;
/*     */ import org.apache.logging.log4j.core.util.Integers;
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
/*     */ public abstract class AbstractAppender
/*     */   extends AbstractFilterable
/*     */   implements Appender
/*     */ {
/*     */   private final boolean ignoreExceptions;
/*  37 */   private ErrorHandler handler = new DefaultErrorHandler(this);
/*     */   
/*     */   private final Layout<? extends Serializable> layout;
/*     */   
/*     */   private final String name;
/*     */   
/*     */   public static int parseInt(String s, int defaultValue) {
/*     */     try {
/*  45 */       return Integers.parseInt(s, defaultValue);
/*  46 */     } catch (NumberFormatException e) {
/*  47 */       LOGGER.error("Could not parse \"{}\" as an integer,  using default value {}: {}", new Object[] { s, Integer.valueOf(defaultValue), e });
/*  48 */       return defaultValue;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected AbstractAppender(String name, Filter filter, Layout<? extends Serializable> layout) {
/*  59 */     this(name, filter, layout, true);
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
/*     */   protected AbstractAppender(String name, Filter filter, Layout<? extends Serializable> layout, boolean ignoreExceptions) {
/*  72 */     super(filter);
/*  73 */     this.name = name;
/*  74 */     this.layout = layout;
/*  75 */     this.ignoreExceptions = ignoreExceptions;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void error(String msg) {
/*  83 */     this.handler.error(msg);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void error(String msg, LogEvent event, Throwable t) {
/*  93 */     this.handler.error(msg, event, t);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void error(String msg, Throwable t) {
/* 102 */     this.handler.error(msg, t);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ErrorHandler getHandler() {
/* 111 */     return this.handler;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Layout<? extends Serializable> getLayout() {
/* 120 */     return this.layout;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/* 129 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean ignoreExceptions() {
/* 140 */     return this.ignoreExceptions;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setHandler(ErrorHandler handler) {
/* 149 */     if (handler == null) {
/* 150 */       LOGGER.error("The handler cannot be set to null");
/*     */     }
/* 152 */     if (isStarted()) {
/* 153 */       LOGGER.error("The handler cannot be changed once the appender is started");
/*     */       return;
/*     */     } 
/* 156 */     this.handler = handler;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 161 */     return this.name;
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\appender\AbstractAppender.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */