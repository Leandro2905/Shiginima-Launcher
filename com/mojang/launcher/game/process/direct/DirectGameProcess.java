/*    */ package com.mojang.launcher.game.process.direct;
/*    */ 
/*    */ import com.google.common.base.Objects;
/*    */ import com.google.common.base.Predicate;
/*    */ import com.google.common.collect.EvictingQueue;
/*    */ import com.mojang.launcher.events.GameOutputLogProcessor;
/*    */ import com.mojang.launcher.game.process.AbstractGameProcess;
/*    */ import java.util.Collection;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ public class DirectGameProcess
/*    */   extends AbstractGameProcess
/*    */ {
/*    */   private static final int MAX_SYSOUT_LINES = 5;
/*    */   private final Process process;
/*    */   protected final DirectProcessInputMonitor monitor;
/* 18 */   private final Collection<String> sysOutLines = (Collection<String>)EvictingQueue.create(5);
/*    */ 
/*    */   
/*    */   public DirectGameProcess(List<String> commands, Process process, Predicate<String> sysOutFilter, GameOutputLogProcessor logProcessor) {
/* 22 */     super(commands, sysOutFilter);
/* 23 */     this.process = process;
/* 24 */     this.monitor = new DirectProcessInputMonitor(this, logProcessor);
/*    */     
/* 26 */     this.monitor.start();
/*    */   }
/*    */ 
/*    */   
/*    */   public Process getRawProcess() {
/* 31 */     return this.process;
/*    */   }
/*    */ 
/*    */   
/*    */   public Collection<String> getSysOutLines() {
/* 36 */     return this.sysOutLines;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isRunning() {
/*    */     try {
/* 43 */       this.process.exitValue();
/*    */     }
/* 45 */     catch (IllegalThreadStateException ex) {
/*    */       
/* 47 */       return true;
/*    */     } 
/* 49 */     return false;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public int getExitCode() {
/*    */     try {
/* 56 */       return this.process.exitValue();
/*    */     }
/* 58 */     catch (IllegalThreadStateException ex) {
/*    */       
/* 60 */       ex.fillInStackTrace();
/* 61 */       throw ex;
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 67 */     return Objects.toStringHelper(this).add("process", this.process).add("monitor", this.monitor).toString();
/*    */   }
/*    */ 
/*    */   
/*    */   public void stop() {
/* 72 */     this.process.destroy();
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\mojang\launcher\game\process\direct\DirectGameProcess.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */