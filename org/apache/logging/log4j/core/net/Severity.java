/*     */ package org.apache.logging.log4j.core.net;
/*     */ 
/*     */ import org.apache.logging.log4j.Level;
/*     */ import org.apache.logging.log4j.spi.StandardLevel;
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
/*     */ public enum Severity
/*     */ {
/*  65 */   EMERG(0),
/*     */   
/*  67 */   ALERT(1),
/*     */   
/*  69 */   CRITICAL(2),
/*     */   
/*  71 */   ERROR(3),
/*     */   
/*  73 */   WARNING(4),
/*     */   
/*  75 */   NOTICE(5),
/*     */   
/*  77 */   INFO(6),
/*     */   
/*  79 */   DEBUG(7);
/*     */   
/*     */   private final int code;
/*     */   
/*     */   Severity(int code) {
/*  84 */     this.code = code;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getCode() {
/*  92 */     return this.code;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEqual(String name) {
/* 101 */     return name().equalsIgnoreCase(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Severity getSeverity(Level level) {
/* 110 */     switch (level.getStandardLevel()) {
/*     */       case ALL:
/* 112 */         return DEBUG;
/*     */       case TRACE:
/* 114 */         return DEBUG;
/*     */       case DEBUG:
/* 116 */         return DEBUG;
/*     */       case INFO:
/* 118 */         return INFO;
/*     */       case WARN:
/* 120 */         return WARNING;
/*     */       case ERROR:
/* 122 */         return ERROR;
/*     */       case FATAL:
/* 124 */         return ALERT;
/*     */       case OFF:
/* 126 */         return EMERG;
/*     */     } 
/* 128 */     return DEBUG;
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\net\Severity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */