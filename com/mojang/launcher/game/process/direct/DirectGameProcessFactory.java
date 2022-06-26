/*    */ package com.mojang.launcher.game.process.direct;
/*    */ 
/*    */ import com.mojang.launcher.game.process.GameProcess;
/*    */ import com.mojang.launcher.game.process.GameProcessBuilder;
/*    */ import com.mojang.launcher.game.process.GameProcessFactory;
/*    */ import java.io.IOException;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DirectGameProcessFactory
/*    */   implements GameProcessFactory
/*    */ {
/*    */   public GameProcess startGame(GameProcessBuilder builder) throws IOException {
/* 15 */     List<String> full = builder.getFullCommands();
/* 16 */     return (GameProcess)new DirectGameProcess(full, (new ProcessBuilder(full)).directory(builder.getDirectory()).redirectErrorStream(true).start(), builder.getSysOutFilter(), builder.getLogProcessor());
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\mojang\launcher\game\process\direct\DirectGameProcessFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */