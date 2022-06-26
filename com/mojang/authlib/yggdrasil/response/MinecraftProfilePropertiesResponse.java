/*    */ package com.mojang.authlib.yggdrasil.response;
/*    */ 
/*    */ import com.mojang.authlib.properties.PropertyMap;
/*    */ import java.util.UUID;
/*    */ 
/*    */ 
/*    */ public class MinecraftProfilePropertiesResponse
/*    */   extends Response
/*    */ {
/*    */   private UUID id;
/*    */   private String name;
/*    */   private PropertyMap properties;
/*    */   
/*    */   public UUID getId() {
/* 15 */     return this.id;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getName() {
/* 20 */     return this.name;
/*    */   }
/*    */ 
/*    */   
/*    */   public PropertyMap getProperties() {
/* 25 */     return this.properties;
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\mojang\authlib\yggdrasil\response\MinecraftProfilePropertiesResponse.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */