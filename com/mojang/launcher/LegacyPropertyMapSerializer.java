/*    */ package com.mojang.launcher;
/*    */ 
/*    */ import com.google.gson.JsonArray;
/*    */ import com.google.gson.JsonElement;
/*    */ import com.google.gson.JsonObject;
/*    */ import com.google.gson.JsonPrimitive;
/*    */ import com.google.gson.JsonSerializationContext;
/*    */ import com.google.gson.JsonSerializer;
/*    */ import com.mojang.authlib.properties.Property;
/*    */ import com.mojang.authlib.properties.PropertyMap;
/*    */ import java.lang.reflect.Type;
/*    */ 
/*    */ 
/*    */ public class LegacyPropertyMapSerializer
/*    */   implements JsonSerializer<PropertyMap>
/*    */ {
/*    */   public JsonElement serialize(PropertyMap src, Type typeOfSrc, JsonSerializationContext context) {
/* 18 */     JsonObject result = new JsonObject();
/* 19 */     for (String key : src.keySet()) {
/*    */       
/* 21 */       JsonArray values = new JsonArray();
/* 22 */       for (Property property : src.get(key)) {
/* 23 */         values.add((JsonElement)new JsonPrimitive(property.getValue()));
/*    */       }
/* 25 */       result.add(key, (JsonElement)values);
/*    */     } 
/* 27 */     return (JsonElement)result;
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\mojang\launcher\LegacyPropertyMapSerializer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */