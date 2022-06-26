/*     */ package com.google.gson.internal.bind;
/*     */ 
/*     */ import com.google.gson.Gson;
/*     */ import com.google.gson.TypeAdapter;
/*     */ import com.google.gson.TypeAdapterFactory;
/*     */ import com.google.gson.internal.LinkedTreeMap;
/*     */ import com.google.gson.reflect.TypeToken;
/*     */ import com.google.gson.stream.JsonReader;
/*     */ import com.google.gson.stream.JsonToken;
/*     */ import com.google.gson.stream.JsonWriter;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ObjectTypeAdapter
/*     */   extends TypeAdapter<Object>
/*     */ {
/*  38 */   public static final TypeAdapterFactory FACTORY = new TypeAdapterFactory()
/*     */     {
/*     */       public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
/*  41 */         if (type.getRawType() == Object.class) {
/*  42 */           return new ObjectTypeAdapter(gson);
/*     */         }
/*  44 */         return null;
/*     */       }
/*     */     };
/*     */   
/*     */   private final Gson gson;
/*     */   
/*     */   private ObjectTypeAdapter(Gson gson) {
/*  51 */     this.gson = gson;
/*     */   } public Object read(JsonReader in) throws IOException {
/*     */     List<Object> list;
/*     */     LinkedTreeMap<String, Object> linkedTreeMap;
/*  55 */     JsonToken token = in.peek();
/*  56 */     switch (token) {
/*     */       case BEGIN_ARRAY:
/*  58 */         list = new ArrayList();
/*  59 */         in.beginArray();
/*  60 */         while (in.hasNext()) {
/*  61 */           list.add(read(in));
/*     */         }
/*  63 */         in.endArray();
/*  64 */         return list;
/*     */       
/*     */       case BEGIN_OBJECT:
/*  67 */         linkedTreeMap = new LinkedTreeMap();
/*  68 */         in.beginObject();
/*  69 */         while (in.hasNext()) {
/*  70 */           linkedTreeMap.put(in.nextName(), read(in));
/*     */         }
/*  72 */         in.endObject();
/*  73 */         return linkedTreeMap;
/*     */       
/*     */       case STRING:
/*  76 */         return in.nextString();
/*     */       
/*     */       case NUMBER:
/*  79 */         return Double.valueOf(in.nextDouble());
/*     */       
/*     */       case BOOLEAN:
/*  82 */         return Boolean.valueOf(in.nextBoolean());
/*     */       
/*     */       case NULL:
/*  85 */         in.nextNull();
/*  86 */         return null;
/*     */     } 
/*     */     
/*  89 */     throw new IllegalStateException();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(JsonWriter out, Object value) throws IOException {
/*  95 */     if (value == null) {
/*  96 */       out.nullValue();
/*     */       
/*     */       return;
/*     */     } 
/* 100 */     TypeAdapter<Object> typeAdapter = this.gson.getAdapter(value.getClass());
/* 101 */     if (typeAdapter instanceof ObjectTypeAdapter) {
/* 102 */       out.beginObject();
/* 103 */       out.endObject();
/*     */       
/*     */       return;
/*     */     } 
/* 107 */     typeAdapter.write(out, value);
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\gson\internal\bind\ObjectTypeAdapter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */