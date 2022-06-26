/*    */ package com.mojang.authlib;
/*    */ 
/*    */ public class Agent
/*    */ {
/*  5 */   public static final Agent MINECRAFT = new Agent("Minecraft", 1);
/*  6 */   public static final Agent SCROLLS = new Agent("Scrolls", 1);
/*    */   
/*    */   private final String name;
/*    */   private final int version;
/*    */   
/*    */   public Agent(String name, int version) {
/* 12 */     this.name = name;
/* 13 */     this.version = version;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getName() {
/* 18 */     return this.name;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getVersion() {
/* 23 */     return this.version;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 28 */     return "Agent{name='" + this.name + '\'' + ", version=" + this.version + '}';
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\mojang\authlib\Agent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */