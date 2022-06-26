/*     */ package org.apache.logging.log4j.core.appender.db.jpa;
/*     */ 
/*     */ import java.util.Map;
/*     */ import javax.persistence.Basic;
/*     */ import javax.persistence.Convert;
/*     */ import javax.persistence.MappedSuperclass;
/*     */ import javax.persistence.Transient;
/*     */ import org.apache.logging.log4j.Level;
/*     */ import org.apache.logging.log4j.Marker;
/*     */ import org.apache.logging.log4j.ThreadContext;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.appender.db.jpa.converter.ContextMapAttributeConverter;
/*     */ import org.apache.logging.log4j.core.appender.db.jpa.converter.ContextStackAttributeConverter;
/*     */ import org.apache.logging.log4j.core.appender.db.jpa.converter.LevelAttributeConverter;
/*     */ import org.apache.logging.log4j.core.appender.db.jpa.converter.MarkerAttributeConverter;
/*     */ import org.apache.logging.log4j.core.appender.db.jpa.converter.MessageAttributeConverter;
/*     */ import org.apache.logging.log4j.core.appender.db.jpa.converter.StackTraceElementAttributeConverter;
/*     */ import org.apache.logging.log4j.core.appender.db.jpa.converter.ThrowableAttributeConverter;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @MappedSuperclass
/*     */ public abstract class BasicLogEventEntity
/*     */   extends AbstractLogEventWrapperEntity
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   
/*     */   public BasicLogEventEntity() {}
/*     */   
/*     */   public BasicLogEventEntity(LogEvent wrappedEvent) {
/*  88 */     super(wrappedEvent);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Convert(converter = LevelAttributeConverter.class)
/*     */   public Level getLevel() {
/*  99 */     return getWrappedEvent().getLevel();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Basic
/*     */   public String getLoggerName() {
/* 110 */     return getWrappedEvent().getLoggerName();
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
/*     */   @Convert(converter = StackTraceElementAttributeConverter.class)
/*     */   public StackTraceElement getSource() {
/* 123 */     return getWrappedEvent().getSource();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Convert(converter = MessageAttributeConverter.class)
/*     */   public Message getMessage() {
/* 135 */     return getWrappedEvent().getMessage();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Convert(converter = MarkerAttributeConverter.class)
/*     */   public Marker getMarker() {
/* 147 */     return getWrappedEvent().getMarker();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Basic
/*     */   public String getThreadName() {
/* 158 */     return getWrappedEvent().getThreadName();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Basic
/*     */   public long getTimeMillis() {
/* 169 */     return getWrappedEvent().getTimeMillis();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Convert(converter = ThrowableAttributeConverter.class)
/*     */   public Throwable getThrown() {
/* 181 */     return getWrappedEvent().getThrown();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Transient
/*     */   public ThrowableProxy getThrownProxy() {
/* 193 */     return getWrappedEvent().getThrownProxy();
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
/*     */   @Convert(converter = ContextMapAttributeConverter.class)
/*     */   public Map<String, String> getContextMap() {
/* 206 */     return getWrappedEvent().getContextMap();
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
/*     */   @Convert(converter = ContextStackAttributeConverter.class)
/*     */   public ThreadContext.ContextStack getContextStack() {
/* 219 */     return getWrappedEvent().getContextStack();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Basic
/*     */   public String getLoggerFqcn() {
/* 230 */     return getWrappedEvent().getLoggerFqcn();
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\appender\db\jpa\BasicLogEventEntity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */