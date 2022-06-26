/*     */ package org.apache.logging.log4j.status;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import org.apache.logging.log4j.Level;
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
/*     */ public class StatusConsoleListener
/*     */   implements StatusListener
/*     */ {
/*  30 */   private Level level = Level.FATAL;
/*  31 */   private String[] filters = null;
/*     */ 
/*     */   
/*     */   private final PrintStream stream;
/*     */ 
/*     */ 
/*     */   
/*     */   public StatusConsoleListener(Level level) {
/*  39 */     this(level, System.out);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StatusConsoleListener(Level level, PrintStream stream) {
/*  50 */     if (stream == null) {
/*  51 */       throw new IllegalArgumentException("You must provide a stream to use for this listener.");
/*     */     }
/*  53 */     this.level = level;
/*  54 */     this.stream = stream;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLevel(Level level) {
/*  62 */     this.level = level;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Level getStatusLevel() {
/*  71 */     return this.level;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void log(StatusData data) {
/*  80 */     if (!filtered(data)) {
/*  81 */       this.stream.println(data.getFormattedStatus());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFilters(String... filters) {
/*  90 */     this.filters = filters;
/*     */   }
/*     */   
/*     */   private boolean filtered(StatusData data) {
/*  94 */     if (this.filters == null) {
/*  95 */       return false;
/*     */     }
/*  97 */     String caller = data.getStackTraceElement().getClassName();
/*  98 */     for (String filter : this.filters) {
/*  99 */       if (caller.startsWith(filter)) {
/* 100 */         return true;
/*     */       }
/*     */     } 
/* 103 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 109 */     if (this.stream != System.out && this.stream != System.err)
/* 110 */       this.stream.close(); 
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\status\StatusConsoleListener.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */