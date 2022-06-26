/*    */ package com.mojang.authlib.yggdrasil.response;
/*    */ 
/*    */ import com.google.gson.JsonDeserializationContext;
/*    */ import com.google.gson.JsonDeserializer;
/*    */ import com.google.gson.JsonElement;
/*    */ import com.google.gson.JsonObject;
/*    */ import com.google.gson.JsonParseException;
/*    */ import com.mojang.authlib.GameProfile;
/*    */ import java.lang.reflect.Type;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ProfileSearchResultsResponse
/*    */   extends Response
/*    */ {
/*    */   private GameProfile[] profiles;
/*    */   
/*    */   public GameProfile[] getProfiles() {
/* 19 */     return this.profiles;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static class Serializer
/*    */     implements JsonDeserializer<ProfileSearchResultsResponse>
/*    */   {
/*    */     public ProfileSearchResultsResponse deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
/* 28 */       ProfileSearchResultsResponse result = new ProfileSearchResultsResponse();
/* 29 */       if (json instanceof JsonObject) {
/*    */         
/* 31 */         JsonObject object = (JsonObject)json;
/* 32 */         if (object.has("error")) {
/* 33 */           result.setError(object.getAsJsonPrimitive("error").getAsString());
/*    */         }
/* 35 */         if (object.has("errorMessage")) {
/* 36 */           result.setError(object.getAsJsonPrimitive("errorMessage").getAsString());
/*    */         }
/* 38 */         if (object.has("cause")) {
/* 39 */           result.setError(object.getAsJsonPrimitive("cause").getAsString());
/*    */         
/*    */         }
/*    */       }
/*    */       else {
/*    */         
/* 45 */         result.profiles = (GameProfile[])context.deserialize(json, GameProfile[].class);
/*    */       } 
/* 47 */       return result;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\mojang\authlib\yggdrasil\response\ProfileSearchResultsResponse.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */