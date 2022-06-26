/*     */ package com.google.gson.internal.bind;
/*     */ 
/*     */ import com.google.gson.Gson;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonPrimitive;
/*     */ import com.google.gson.JsonSyntaxException;
/*     */ import com.google.gson.TypeAdapter;
/*     */ import com.google.gson.TypeAdapterFactory;
/*     */ import com.google.gson.internal.;
/*     */ import com.google.gson.internal.ConstructorConstructor;
/*     */ import com.google.gson.internal.JsonReaderInternalAccess;
/*     */ import com.google.gson.internal.ObjectConstructor;
/*     */ import com.google.gson.internal.Streams;
/*     */ import com.google.gson.reflect.TypeToken;
/*     */ import com.google.gson.stream.JsonReader;
/*     */ import com.google.gson.stream.JsonToken;
/*     */ import com.google.gson.stream.JsonWriter;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
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
/*     */ public final class MapTypeAdapterFactory
/*     */   implements TypeAdapterFactory
/*     */ {
/*     */   private final ConstructorConstructor constructorConstructor;
/*     */   private final boolean complexMapKeySerialization;
/*     */   
/*     */   public MapTypeAdapterFactory(ConstructorConstructor constructorConstructor, boolean complexMapKeySerialization) {
/* 111 */     this.constructorConstructor = constructorConstructor;
/* 112 */     this.complexMapKeySerialization = complexMapKeySerialization;
/*     */   }
/*     */   
/*     */   public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
/* 116 */     Type type = typeToken.getType();
/*     */     
/* 118 */     Class<? super T> rawType = typeToken.getRawType();
/* 119 */     if (!Map.class.isAssignableFrom(rawType)) {
/* 120 */       return null;
/*     */     }
/*     */     
/* 123 */     Class<?> rawTypeOfSrc = .Gson.Types.getRawType(type);
/* 124 */     Type[] keyAndValueTypes = .Gson.Types.getMapKeyAndValueTypes(type, rawTypeOfSrc);
/* 125 */     TypeAdapter<?> keyAdapter = getKeyAdapter(gson, keyAndValueTypes[0]);
/* 126 */     TypeAdapter<?> valueAdapter = gson.getAdapter(TypeToken.get(keyAndValueTypes[1]));
/* 127 */     ObjectConstructor<T> constructor = this.constructorConstructor.get(typeToken);
/*     */ 
/*     */ 
/*     */     
/* 131 */     TypeAdapter<T> result = (TypeAdapter)new Adapter<Object, Object>(gson, keyAndValueTypes[0], keyAdapter, keyAndValueTypes[1], valueAdapter, (ObjectConstructor)constructor);
/*     */     
/* 133 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private TypeAdapter<?> getKeyAdapter(Gson context, Type keyType) {
/* 140 */     return (keyType == boolean.class || keyType == Boolean.class) ? TypeAdapters.BOOLEAN_AS_STRING : context.getAdapter(TypeToken.get(keyType));
/*     */   }
/*     */ 
/*     */   
/*     */   private final class Adapter<K, V>
/*     */     extends TypeAdapter<Map<K, V>>
/*     */   {
/*     */     private final TypeAdapter<K> keyTypeAdapter;
/*     */     
/*     */     private final TypeAdapter<V> valueTypeAdapter;
/*     */     private final ObjectConstructor<? extends Map<K, V>> constructor;
/*     */     
/*     */     public Adapter(Gson context, Type keyType, TypeAdapter<K> keyTypeAdapter, Type valueType, TypeAdapter<V> valueTypeAdapter, ObjectConstructor<? extends Map<K, V>> constructor) {
/* 153 */       this.keyTypeAdapter = new TypeAdapterRuntimeTypeWrapper<K>(context, keyTypeAdapter, keyType);
/*     */       
/* 155 */       this.valueTypeAdapter = new TypeAdapterRuntimeTypeWrapper<V>(context, valueTypeAdapter, valueType);
/*     */       
/* 157 */       this.constructor = constructor;
/*     */     }
/*     */     
/*     */     public Map<K, V> read(JsonReader in) throws IOException {
/* 161 */       JsonToken peek = in.peek();
/* 162 */       if (peek == JsonToken.NULL) {
/* 163 */         in.nextNull();
/* 164 */         return null;
/*     */       } 
/*     */       
/* 167 */       Map<K, V> map = (Map<K, V>)this.constructor.construct();
/*     */       
/* 169 */       if (peek == JsonToken.BEGIN_ARRAY) {
/* 170 */         in.beginArray();
/* 171 */         while (in.hasNext()) {
/* 172 */           in.beginArray();
/* 173 */           K key = (K)this.keyTypeAdapter.read(in);
/* 174 */           V value = (V)this.valueTypeAdapter.read(in);
/* 175 */           V replaced = map.put(key, value);
/* 176 */           if (replaced != null) {
/* 177 */             throw new JsonSyntaxException("duplicate key: " + key);
/*     */           }
/* 179 */           in.endArray();
/*     */         } 
/* 181 */         in.endArray();
/*     */       } else {
/* 183 */         in.beginObject();
/* 184 */         while (in.hasNext()) {
/* 185 */           JsonReaderInternalAccess.INSTANCE.promoteNameToValue(in);
/* 186 */           K key = (K)this.keyTypeAdapter.read(in);
/* 187 */           V value = (V)this.valueTypeAdapter.read(in);
/* 188 */           V replaced = map.put(key, value);
/* 189 */           if (replaced != null) {
/* 190 */             throw new JsonSyntaxException("duplicate key: " + key);
/*     */           }
/*     */         } 
/* 193 */         in.endObject();
/*     */       } 
/* 195 */       return map;
/*     */     }
/*     */     public void write(JsonWriter out, Map<K, V> map) throws IOException {
/*     */       int i;
/* 199 */       if (map == null) {
/* 200 */         out.nullValue();
/*     */         
/*     */         return;
/*     */       } 
/* 204 */       if (!MapTypeAdapterFactory.this.complexMapKeySerialization) {
/* 205 */         out.beginObject();
/* 206 */         for (Map.Entry<K, V> entry : map.entrySet()) {
/* 207 */           out.name(String.valueOf(entry.getKey()));
/* 208 */           this.valueTypeAdapter.write(out, entry.getValue());
/*     */         } 
/* 210 */         out.endObject();
/*     */         
/*     */         return;
/*     */       } 
/* 214 */       boolean hasComplexKeys = false;
/* 215 */       List<JsonElement> keys = new ArrayList<JsonElement>(map.size());
/*     */       
/* 217 */       List<V> values = new ArrayList<V>(map.size());
/* 218 */       for (Map.Entry<K, V> entry : map.entrySet()) {
/* 219 */         JsonElement keyElement = this.keyTypeAdapter.toJsonTree(entry.getKey());
/* 220 */         keys.add(keyElement);
/* 221 */         values.add(entry.getValue());
/* 222 */         i = hasComplexKeys | ((keyElement.isJsonArray() || keyElement.isJsonObject()) ? 1 : 0);
/*     */       } 
/*     */       
/* 225 */       if (i != 0) {
/* 226 */         out.beginArray();
/* 227 */         for (int j = 0; j < keys.size(); j++) {
/* 228 */           out.beginArray();
/* 229 */           Streams.write(keys.get(j), out);
/* 230 */           this.valueTypeAdapter.write(out, values.get(j));
/* 231 */           out.endArray();
/*     */         } 
/* 233 */         out.endArray();
/*     */       } else {
/* 235 */         out.beginObject();
/* 236 */         for (int j = 0; j < keys.size(); j++) {
/* 237 */           JsonElement keyElement = keys.get(j);
/* 238 */           out.name(keyToString(keyElement));
/* 239 */           this.valueTypeAdapter.write(out, values.get(j));
/*     */         } 
/* 241 */         out.endObject();
/*     */       } 
/*     */     }
/*     */     
/*     */     private String keyToString(JsonElement keyElement) {
/* 246 */       if (keyElement.isJsonPrimitive()) {
/* 247 */         JsonPrimitive primitive = keyElement.getAsJsonPrimitive();
/* 248 */         if (primitive.isNumber())
/* 249 */           return String.valueOf(primitive.getAsNumber()); 
/* 250 */         if (primitive.isBoolean())
/* 251 */           return Boolean.toString(primitive.getAsBoolean()); 
/* 252 */         if (primitive.isString()) {
/* 253 */           return primitive.getAsString();
/*     */         }
/* 255 */         throw new AssertionError();
/*     */       } 
/* 257 */       if (keyElement.isJsonNull()) {
/* 258 */         return "null";
/*     */       }
/* 260 */       throw new AssertionError();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\gson\internal\bind\MapTypeAdapterFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */