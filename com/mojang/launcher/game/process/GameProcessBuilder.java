/*    */ package com.mojang.launcher.game.process;
/*    */ 
/*    */ import com.google.common.base.Objects;
/*    */ import com.google.common.base.Predicate;
/*    */ import com.google.common.base.Predicates;
/*    */ import com.google.common.collect.Lists;
/*    */ import com.mojang.launcher.OperatingSystem;
/*    */ import com.mojang.launcher.events.GameOutputLogProcessor;
/*    */ import java.io.File;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Arrays;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ public class GameProcessBuilder
/*    */ {
/*    */   private final String processPath;
/* 18 */   private final List<String> arguments = Lists.newArrayList();
/* 19 */   private Predicate<String> sysOutFilter = Predicates.alwaysTrue();
/* 20 */   private GameOutputLogProcessor logProcessor = new GameOutputLogProcessor()
/*    */     {
/*    */       public void onGameOutput(GameProcess process, String logLine) {}
/*    */     };
/*    */   
/*    */   private File directory;
/*    */   
/*    */   public GameProcessBuilder(String processPath) {
/* 28 */     if (processPath == null) {
/* 29 */       processPath = OperatingSystem.getCurrentPlatform().getJavaDir();
/*    */     }
/* 31 */     this.processPath = processPath;
/*    */   }
/*    */ 
/*    */   
/*    */   public List<String> getFullCommands() {
/* 36 */     List<String> result = new ArrayList<>(this.arguments);
/* 37 */     result.add(0, getProcessPath());
/* 38 */     return result;
/*    */   }
/*    */ 
/*    */   
/*    */   public GameProcessBuilder withArguments(String... commands) {
/* 43 */     this.arguments.addAll(Arrays.asList(commands));
/* 44 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public List<String> getArguments() {
/* 49 */     return this.arguments;
/*    */   }
/*    */ 
/*    */   
/*    */   public GameProcessBuilder directory(File directory) {
/* 54 */     this.directory = directory;
/* 55 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public File getDirectory() {
/* 60 */     return this.directory;
/*    */   }
/*    */ 
/*    */   
/*    */   public GameProcessBuilder withSysOutFilter(Predicate<String> predicate) {
/* 65 */     this.sysOutFilter = predicate;
/* 66 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public GameProcessBuilder withLogProcessor(GameOutputLogProcessor logProcessor) {
/* 71 */     this.logProcessor = logProcessor;
/* 72 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public Predicate<String> getSysOutFilter() {
/* 77 */     return this.sysOutFilter;
/*    */   }
/*    */ 
/*    */   
/*    */   protected String getProcessPath() {
/* 82 */     return this.processPath;
/*    */   }
/*    */ 
/*    */   
/*    */   public GameOutputLogProcessor getLogProcessor() {
/* 87 */     return this.logProcessor;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 92 */     return Objects.toStringHelper(this).add("processPath", this.processPath).add("arguments", this.arguments).add("sysOutFilter", this.sysOutFilter).add("directory", this.directory).add("logProcessor", this.logProcessor).toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\mojang\launcher\game\process\GameProcessBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */