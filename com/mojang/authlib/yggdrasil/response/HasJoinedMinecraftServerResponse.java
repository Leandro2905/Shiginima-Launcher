/*    */ package com.mojang.authlib.yggdrasil.response;
/*    */ 
/*    */ import com.mojang.authlib.properties.PropertyMap;
/*    */ import java.util.UUID;
/*    */ 
/*    */ 
/*    */ public class HasJoinedMinecraftServerResponse
/*    */   extends Response
/*    */ {
/*    */   private UUID id;
/*    */   private PropertyMap properties;
/*    */   
/*    */   public UUID getId() {
/* 14 */     return this.id;
/*    */   }
/*    */ 
/*    */   
/*    */   public PropertyMap getProperties() {
/* 19 */     return this.properties;
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\mojang\authlib\yggdrasil\response\HasJoinedMinecraftServerResponse.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */