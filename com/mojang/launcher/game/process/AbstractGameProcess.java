/*    */ package com.mojang.launcher.game.process;
/*    */ 
/*    */ import com.google.common.base.Predicate;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ public abstract class AbstractGameProcess
/*    */   implements GameProcess
/*    */ {
/*    */   protected final List<String> arguments;
/*    */   protected final Predicate<String> sysOutFilter;
/*    */   private GameProcessRunnable onExit;
/*    */   
/*    */   public AbstractGameProcess(List<String> arguments, Predicate<String> sysOutFilter) {
/* 15 */     this.arguments = arguments;
/* 16 */     this.sysOutFilter = sysOutFilter;
/*    */   }
/*    */ 
/*    */   
/*    */   public Predicate<String> getSysOutFilter() {
/* 21 */     return this.sysOutFilter;
/*    */   }
/*    */ 
/*    */   
/*    */   public List<String> getStartupArguments() {
/* 26 */     return this.arguments;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setExitRunnable(GameProcessRunnable runnable) {
/* 31 */     this.onExit = runnable;
/* 32 */     if (!isRunning() && runnable != null) {
/* 33 */       runnable.onGameProcessEnded(this);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public GameProcessRunnable getExitRunnable() {
/* 39 */     return this.onExit;
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\mojang\launcher\game\process\AbstractGameProcess.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */