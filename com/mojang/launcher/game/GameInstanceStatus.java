/*    */ package com.mojang.launcher.game;
/*    */ 
/*    */ public enum GameInstanceStatus
/*    */ {
/*  5 */   PREPARING("Preparing..."), DOWNLOADING("Downloading..."), INSTALLING("Installing..."), LAUNCHING("Launching..."), PLAYING("Playing..."), IDLE("Idle");
/*    */   
/*    */   private final String name;
/*    */ 
/*    */   
/*    */   GameInstanceStatus(String name) {
/* 11 */     this.name = name;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getName() {
/* 16 */     return this.name;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 21 */     return this.name;
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\mojang\launcher\game\GameInstanceStatus.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */