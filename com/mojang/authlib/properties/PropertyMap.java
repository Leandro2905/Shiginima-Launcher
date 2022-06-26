/*    */ package com.mojang.authlib.properties;
/*    */ 
/*    */ import com.google.common.collect.ForwardingMultimap;
/*    */ import com.google.common.collect.LinkedHashMultimap;
/*    */ import com.google.common.collect.Multimap;
/*    */ import com.google.gson.JsonArray;
/*    */ import com.google.gson.JsonDeserializationContext;
/*    */ import com.google.gson.JsonDeserializer;
/*    */ import com.google.gson.JsonElement;
/*    */ import com.google.gson.JsonObject;
/*    */ import com.google.gson.JsonParseException;
/*    */ import com.google.gson.JsonSerializationContext;
/*    */ import com.google.gson.JsonSerializer;
/*    */ import java.lang.reflect.Type;
/*    */ import java.util.Iterator;
/*    */ import java.util.Map;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PropertyMap
/*    */   extends ForwardingMultimap<String, Property>
/*    */ {
/* 26 */   private final Multimap<String, Property> properties = (Multimap<String, Property>)LinkedHashMultimap.create();
/*    */ 
/*    */   
/*    */   protected Multimap<String, Property> delegate() {
/* 30 */     return this.properties;
/*    */   }
/*    */ 
/*    */   
/*    */   public static class Serializer
/*    */     implements JsonSerializer<PropertyMap>, JsonDeserializer<PropertyMap>
/*    */   {
/*    */     public PropertyMap deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
/* 38 */       PropertyMap result = new PropertyMap();
/*    */ 
/*    */       
/* 41 */       if (json instanceof JsonObject) {
/* 42 */         JsonObject object = (JsonObject)json;
/*    */         
/* 44 */         for (Iterator<Map.Entry<String, JsonElement>> iter = object.entrySet().iterator(); iter.hasNext(); ) {
/* 45 */           Map.Entry<String, JsonElement> entry = iter.next();
/* 46 */           if (entry.getValue() instanceof JsonArray) {
/* 47 */             for (JsonElement element : entry.getValue()) {
/* 48 */               result.put(entry.getKey(), new Property(entry
/* 49 */                     .getKey(), element
/* 50 */                     .getAsString()));
/*    */             }
/*    */           }
/*    */         } 
/* 54 */       } else if (json instanceof JsonArray) {
/* 55 */         for (JsonElement element : json) {
/* 56 */           if (element instanceof JsonObject) {
/* 57 */             JsonObject object = (JsonObject)element;
/*    */             
/* 59 */             String name = object.getAsJsonPrimitive("name").getAsString();
/*    */             
/* 61 */             String value = object.getAsJsonPrimitive("value").getAsString();
/*    */             
/* 63 */             if (object.has("signature")) {
/* 64 */               result.put(name, new Property(name, value, object
/* 65 */                     .getAsJsonPrimitive("signature")
/* 66 */                     .getAsString())); continue;
/*    */             } 
/* 68 */             result.put(name, new Property(name, value));
/*    */           } 
/*    */         } 
/*    */       } 
/*    */       
/* 73 */       return result;
/*    */     }
/*    */     
/*    */     public JsonElement serialize(PropertyMap src, Type typeOfSrc, JsonSerializationContext context) {
/* 77 */       JsonArray result = new JsonArray();
/* 78 */       for (Property property : src.values()) {
/* 79 */         JsonObject object = new JsonObject();
/*    */         
/* 81 */         object.addProperty("name", property.getName());
/* 82 */         object.addProperty("value", property.getValue());
/* 83 */         if (property.hasSignature()) {
/* 84 */           object.addProperty("signature", property.getSignature());
/*    */         }
/* 86 */         result.add((JsonElement)object);
/*    */       } 
/* 88 */       return (JsonElement)result;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\mojang\authlib\properties\PropertyMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */