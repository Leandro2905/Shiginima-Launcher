/*    */ package com.mojang.launcher.updater;
/*    */ 
/*    */ import com.google.gson.Gson;
/*    */ import com.google.gson.TypeAdapter;
/*    */ import com.google.gson.TypeAdapterFactory;
/*    */ import com.google.gson.reflect.TypeToken;
/*    */ import com.google.gson.stream.JsonReader;
/*    */ import com.google.gson.stream.JsonToken;
/*    */ import com.google.gson.stream.JsonWriter;
/*    */ import java.io.IOException;
/*    */ import java.util.HashMap;
/*    */ import java.util.Locale;
/*    */ import java.util.Map;
/*    */ 
/*    */ public class LowerCaseEnumTypeAdapterFactory
/*    */   implements TypeAdapterFactory
/*    */ {
/*    */   public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
/* 19 */     Class<T> rawType = type.getRawType();
/* 20 */     if (!rawType.isEnum()) {
/* 21 */       return null;
/*    */     }
/*    */     
/* 24 */     final Map<String, T> lowercaseToConstant = new HashMap<>();
/* 25 */     for (T constant : rawType.getEnumConstants()) {
/* 26 */       lowercaseToConstant.put(toLowercase(constant), constant);
/*    */     }
/*    */     
/* 29 */     return new TypeAdapter<T>() {
/*    */         public void write(JsonWriter out, T value) throws IOException {
/* 31 */           if (value == null) {
/* 32 */             out.nullValue();
/*    */           } else {
/* 34 */             out.value(LowerCaseEnumTypeAdapterFactory.this.toLowercase(value));
/*    */           } 
/*    */         }
/*    */         
/*    */         public T read(JsonReader reader) throws IOException {
/* 39 */           if (reader.peek() == JsonToken.NULL) {
/* 40 */             reader.nextNull();
/* 41 */             return null;
/*    */           } 
/* 43 */           return (T)lowercaseToConstant.get(reader.nextString());
/*    */         }
/*    */       };
/*    */   }
/*    */ 
/*    */   
/*    */   private String toLowercase(Object o) {
/* 50 */     return o.toString().toLowerCase(Locale.US);
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\mojang\launche\\updater\LowerCaseEnumTypeAdapterFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */