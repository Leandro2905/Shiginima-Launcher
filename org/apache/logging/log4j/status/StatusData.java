/*     */ package org.apache.logging.log4j.status;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.io.Serializable;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Date;
/*     */ import org.apache.logging.log4j.Level;
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
/*     */ public class StatusData
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -4341916115118014017L;
/*     */   private final long timestamp;
/*     */   private final StackTraceElement caller;
/*     */   private final Level level;
/*     */   private final Message msg;
/*     */   private final Throwable throwable;
/*     */   
/*     */   public StatusData(StackTraceElement caller, Level level, Message msg, Throwable t) {
/*  48 */     this.timestamp = System.currentTimeMillis();
/*  49 */     this.caller = caller;
/*  50 */     this.level = level;
/*  51 */     this.msg = msg;
/*  52 */     this.throwable = t;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getTimestamp() {
/*  60 */     return this.timestamp;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StackTraceElement getStackTraceElement() {
/*  68 */     return this.caller;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Level getLevel() {
/*  76 */     return this.level;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Message getMessage() {
/*  84 */     return this.msg;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Throwable getThrowable() {
/*  92 */     return this.throwable;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFormattedStatus() {
/*     */     Throwable t;
/* 100 */     StringBuilder sb = new StringBuilder();
/* 101 */     SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");
/* 102 */     sb.append(format.format(new Date(this.timestamp)));
/* 103 */     sb.append(' ');
/* 104 */     sb.append(this.level.toString());
/* 105 */     sb.append(' ');
/* 106 */     sb.append(this.msg.getFormattedMessage());
/* 107 */     Object[] params = this.msg.getParameters();
/*     */     
/* 109 */     if (this.throwable == null && params != null && params[params.length - 1] instanceof Throwable) {
/* 110 */       t = (Throwable)params[params.length - 1];
/*     */     } else {
/* 112 */       t = this.throwable;
/*     */     } 
/* 114 */     if (t != null) {
/* 115 */       sb.append(' ');
/* 116 */       ByteArrayOutputStream baos = new ByteArrayOutputStream();
/* 117 */       t.printStackTrace(new PrintStream(baos));
/* 118 */       sb.append(baos.toString());
/*     */     } 
/* 120 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\status\StatusData.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */