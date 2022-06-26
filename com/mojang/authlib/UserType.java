/*    */ package com.mojang.authlib;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ 
/*    */ public enum UserType
/*    */ {
/*  8 */   LEGACY("legacy"), MOJANG("mojang");
/*    */   
/*    */   private final String name;
/*    */   
/*    */   private static final Map<String, UserType> BY_NAME;
/*    */   
/*    */   UserType(String name) {
/* 15 */     this.name = name;
/*    */   }
/*    */ 
/*    */   
/*    */   public static UserType byName(String name) {
/* 20 */     return BY_NAME.get(name.toLowerCase());
/*    */   }
/*    */ 
/*    */   
/*    */   public String getName() {
/* 25 */     return this.name;
/*    */   }
/*    */ 
/*    */   
/*    */   static {
/* 30 */     BY_NAME = new HashMap<>();
/* 31 */     for (UserType type : values())
/* 32 */       BY_NAME.put(type.name, type); 
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\mojang\authlib\UserType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */