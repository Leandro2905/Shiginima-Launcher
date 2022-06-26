/*    */ package com.mojang.authlib.yggdrasil.response;
/*    */ 
/*    */ import com.mojang.authlib.minecraft.MinecraftProfileTexture;
/*    */ import java.util.Map;
/*    */ import java.util.UUID;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MinecraftTexturesPayload
/*    */ {
/*    */   private long timestamp;
/*    */   private UUID profileId;
/*    */   private String profileName;
/*    */   private boolean isPublic;
/*    */   private Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> textures;
/*    */   
/*    */   public long getTimestamp() {
/* 18 */     return this.timestamp;
/*    */   }
/*    */ 
/*    */   
/*    */   public UUID getProfileId() {
/* 23 */     return this.profileId;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getProfileName() {
/* 28 */     return this.profileName;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isPublic() {
/* 33 */     return this.isPublic;
/*    */   }
/*    */ 
/*    */   
/*    */   public Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> getTextures() {
/* 38 */     return this.textures;
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\mojang\authlib\yggdrasil\response\MinecraftTexturesPayload.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */