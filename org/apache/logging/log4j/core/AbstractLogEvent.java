/*     */ package org.apache.logging.log4j.core;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.Map;
/*     */ import org.apache.logging.log4j.Level;
/*     */ import org.apache.logging.log4j.Marker;
/*     */ import org.apache.logging.log4j.ThreadContext;
/*     */ import org.apache.logging.log4j.core.impl.ThrowableProxy;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractLogEvent
/*     */   implements LogEvent
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   
/*     */   public Map<String, String> getContextMap() {
/*  42 */     return Collections.emptyMap();
/*     */   }
/*     */ 
/*     */   
/*     */   public ThreadContext.ContextStack getContextStack() {
/*  47 */     return (ThreadContext.ContextStack)ThreadContext.EMPTY_STACK;
/*     */   }
/*     */ 
/*     */   
/*     */   public Level getLevel() {
/*  52 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getLoggerFqcn() {
/*  57 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getLoggerName() {
/*  62 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public Marker getMarker() {
/*  67 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public Message getMessage() {
/*  72 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public StackTraceElement getSource() {
/*  77 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getThreadName() {
/*  82 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public Throwable getThrown() {
/*  87 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public ThrowableProxy getThrownProxy() {
/*  92 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getTimeMillis() {
/*  97 */     return 0L;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEndOfBatch() {
/* 102 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isIncludeLocation() {
/* 107 */     return false;
/*     */   }
/*     */   
/*     */   public void setEndOfBatch(boolean endOfBatch) {}
/*     */   
/*     */   public void setIncludeLocation(boolean locationRequired) {}
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\AbstractLogEvent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */