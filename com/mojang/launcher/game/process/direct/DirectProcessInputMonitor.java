/*    */ package com.mojang.launcher.game.process.direct;
/*    */ 
/*    */ import com.mojang.launcher.events.GameOutputLogProcessor;
/*    */ import com.mojang.launcher.game.process.GameProcess;
/*    */ import com.mojang.launcher.game.process.GameProcessRunnable;
/*    */ import java.io.BufferedReader;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStreamReader;
/*    */ import org.apache.commons.io.IOUtils;
/*    */ import org.apache.logging.log4j.LogManager;
/*    */ import org.apache.logging.log4j.Logger;
/*    */ 
/*    */ 
/*    */ public class DirectProcessInputMonitor
/*    */   extends Thread
/*    */ {
/* 17 */   private static final Logger LOGGER = LogManager.getLogger();
/*    */   
/*    */   private final DirectGameProcess process;
/*    */   private final GameOutputLogProcessor logProcessor;
/*    */   
/*    */   public DirectProcessInputMonitor(DirectGameProcess process, GameOutputLogProcessor logProcessor) {
/* 23 */     this.process = process;
/* 24 */     this.logProcessor = logProcessor;
/*    */   }
/*    */ 
/*    */   
/*    */   public void run() {
/* 29 */     InputStreamReader reader = new InputStreamReader(this.process.getRawProcess().getInputStream());
/* 30 */     BufferedReader buf = new BufferedReader(reader);
/* 31 */     String line = null;
/* 32 */     while (this.process.isRunning()) {
/*    */       
/*    */       try {
/* 35 */         while ((line = buf.readLine()) != null)
/*    */         {
/* 37 */           this.logProcessor.onGameOutput((GameProcess)this.process, line);
/* 38 */           if (this.process.getSysOutFilter().apply(line) == Boolean.TRUE.booleanValue()) {
/* 39 */             this.process.getSysOutLines().add(line);
/*    */           }
/*    */         }
/*    */       
/* 43 */       } catch (IOException ex) {
/*    */         
/* 45 */         LOGGER.error(ex);
/*    */       }
/*    */       finally {
/*    */         
/* 49 */         IOUtils.closeQuietly(reader);
/*    */       } 
/*    */     } 
/* 52 */     GameProcessRunnable onExit = this.process.getExitRunnable();
/* 53 */     if (onExit != null)
/* 54 */       onExit.onGameProcessEnded((GameProcess)this.process); 
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\mojang\launcher\game\process\direct\DirectProcessInputMonitor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */