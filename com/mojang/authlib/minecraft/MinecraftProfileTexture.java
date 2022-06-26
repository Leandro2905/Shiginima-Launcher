/*    */ package com.mojang.authlib.minecraft;
/*    */ 
/*    */ import java.util.Map;
/*    */ import javax.annotation.Nullable;
/*    */ import org.apache.commons.io.FilenameUtils;
/*    */ import org.apache.commons.lang3.builder.ToStringBuilder;
/*    */ 
/*    */ public class MinecraftProfileTexture
/*    */ {
/*    */   private final String url;
/*    */   private final Map<String, String> metadata;
/*    */   
/*    */   public enum Type
/*    */   {
/* 15 */     SKIN, CAPE;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public MinecraftProfileTexture(String url, Map<String, String> metadata) {
/* 22 */     this.url = url;
/* 23 */     this.metadata = metadata;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getUrl() {
/* 28 */     return this.url;
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public String getMetadata(String key) {
/* 34 */     if (this.metadata == null) {
/* 35 */       return null;
/*    */     }
/* 37 */     return this.metadata.get(key);
/*    */   }
/*    */ 
/*    */   
/*    */   public String getHash() {
/* 42 */     return FilenameUtils.getBaseName(this.url);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 47 */     return (new ToStringBuilder(this)).append("url", this.url).append("hash", getHash()).toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\mojang\authlib\minecraft\MinecraftProfileTexture.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */