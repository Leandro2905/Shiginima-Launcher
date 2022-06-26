/*     */ package com.google.gson.internal.bind;
/*     */ 
/*     */ import com.google.gson.FieldNamingStrategy;
/*     */ import com.google.gson.Gson;
/*     */ import com.google.gson.JsonSyntaxException;
/*     */ import com.google.gson.TypeAdapter;
/*     */ import com.google.gson.TypeAdapterFactory;
/*     */ import com.google.gson.annotations.SerializedName;
/*     */ import com.google.gson.internal.;
/*     */ import com.google.gson.internal.ConstructorConstructor;
/*     */ import com.google.gson.internal.Excluder;
/*     */ import com.google.gson.internal.ObjectConstructor;
/*     */ import com.google.gson.internal.Primitives;
/*     */ import com.google.gson.reflect.TypeToken;
/*     */ import com.google.gson.stream.JsonReader;
/*     */ import com.google.gson.stream.JsonToken;
/*     */ import com.google.gson.stream.JsonWriter;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.LinkedHashMap;
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
/*     */ public final class ReflectiveTypeAdapterFactory
/*     */   implements TypeAdapterFactory
/*     */ {
/*     */   private final ConstructorConstructor constructorConstructor;
/*     */   private final FieldNamingStrategy fieldNamingPolicy;
/*     */   private final Excluder excluder;
/*     */   
/*     */   public ReflectiveTypeAdapterFactory(ConstructorConstructor constructorConstructor, FieldNamingStrategy fieldNamingPolicy, Excluder excluder) {
/*  50 */     this.constructorConstructor = constructorConstructor;
/*  51 */     this.fieldNamingPolicy = fieldNamingPolicy;
/*  52 */     this.excluder = excluder;
/*     */   }
/*     */   
/*     */   public boolean excludeField(Field f, boolean serialize) {
/*  56 */     return (!this.excluder.excludeClass(f.getType(), serialize) && !this.excluder.excludeField(f, serialize));
/*     */   }
/*     */   
/*     */   private String getFieldName(Field f) {
/*  60 */     SerializedName serializedName = f.<SerializedName>getAnnotation(SerializedName.class);
/*  61 */     return (serializedName == null) ? this.fieldNamingPolicy.translateName(f) : serializedName.value();
/*     */   }
/*     */   
/*     */   public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
/*  65 */     Class<? super T> raw = type.getRawType();
/*     */     
/*  67 */     if (!Object.class.isAssignableFrom(raw)) {
/*  68 */       return null;
/*     */     }
/*     */     
/*  71 */     ObjectConstructor<T> constructor = this.constructorConstructor.get(type);
/*  72 */     return new Adapter<T>(constructor, getBoundFields(gson, type, raw));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private BoundField createBoundField(final Gson context, final Field field, String name, final TypeToken<?> fieldType, boolean serialize, boolean deserialize) {
/*  78 */     final boolean isPrimitive = Primitives.isPrimitive(fieldType.getRawType());
/*     */ 
/*     */     
/*  81 */     return new BoundField(name, serialize, deserialize) {
/*  82 */         final TypeAdapter<?> typeAdapter = context.getAdapter(fieldType);
/*     */ 
/*     */         
/*     */         void write(JsonWriter writer, Object value) throws IOException, IllegalAccessException {
/*  86 */           Object fieldValue = field.get(value);
/*  87 */           TypeAdapter t = new TypeAdapterRuntimeTypeWrapper(context, this.typeAdapter, fieldType.getType());
/*     */           
/*  89 */           t.write(writer, fieldValue);
/*     */         }
/*     */         
/*     */         void read(JsonReader reader, Object value) throws IOException, IllegalAccessException {
/*  93 */           Object fieldValue = this.typeAdapter.read(reader);
/*  94 */           if (fieldValue != null || !isPrimitive) {
/*  95 */             field.set(value, fieldValue);
/*     */           }
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   private Map<String, BoundField> getBoundFields(Gson context, TypeToken<?> type, Class<?> raw) {
/* 102 */     Map<String, BoundField> result = new LinkedHashMap<String, BoundField>();
/* 103 */     if (raw.isInterface()) {
/* 104 */       return result;
/*     */     }
/*     */     
/* 107 */     Type declaredType = type.getType();
/* 108 */     while (raw != Object.class) {
/* 109 */       Field[] fields = raw.getDeclaredFields();
/* 110 */       for (Field field : fields) {
/* 111 */         boolean serialize = excludeField(field, true);
/* 112 */         boolean deserialize = excludeField(field, false);
/* 113 */         if (serialize || deserialize) {
/*     */ 
/*     */           
/* 116 */           field.setAccessible(true);
/* 117 */           Type fieldType = .Gson.Types.resolve(type.getType(), raw, field.getGenericType());
/* 118 */           BoundField boundField = createBoundField(context, field, getFieldName(field), TypeToken.get(fieldType), serialize, deserialize);
/*     */           
/* 120 */           BoundField previous = result.put(boundField.name, boundField);
/* 121 */           if (previous != null) {
/* 122 */             throw new IllegalArgumentException(declaredType + " declares multiple JSON fields named " + previous.name);
/*     */           }
/*     */         } 
/*     */       } 
/* 126 */       type = TypeToken.get(.Gson.Types.resolve(type.getType(), raw, raw.getGenericSuperclass()));
/* 127 */       raw = type.getRawType();
/*     */     } 
/* 129 */     return result;
/*     */   }
/*     */   
/*     */   static abstract class BoundField {
/*     */     final String name;
/*     */     final boolean serialized;
/*     */     final boolean deserialized;
/*     */     
/*     */     protected BoundField(String name, boolean serialized, boolean deserialized) {
/* 138 */       this.name = name;
/* 139 */       this.serialized = serialized;
/* 140 */       this.deserialized = deserialized;
/*     */     }
/*     */     
/*     */     abstract void write(JsonWriter param1JsonWriter, Object param1Object) throws IOException, IllegalAccessException;
/*     */     
/*     */     abstract void read(JsonReader param1JsonReader, Object param1Object) throws IOException, IllegalAccessException; }
/*     */   
/*     */   public static final class Adapter<T> extends TypeAdapter<T> {
/*     */     private final ObjectConstructor<T> constructor;
/*     */     private final Map<String, ReflectiveTypeAdapterFactory.BoundField> boundFields;
/*     */     
/*     */     private Adapter(ObjectConstructor<T> constructor, Map<String, ReflectiveTypeAdapterFactory.BoundField> boundFields) {
/* 152 */       this.constructor = constructor;
/* 153 */       this.boundFields = boundFields;
/*     */     }
/*     */     
/*     */     public T read(JsonReader in) throws IOException {
/* 157 */       if (in.peek() == JsonToken.NULL) {
/* 158 */         in.nextNull();
/* 159 */         return null;
/*     */       } 
/*     */       
/* 162 */       T instance = (T)this.constructor.construct();
/*     */       
/*     */       try {
/* 165 */         in.beginObject();
/* 166 */         while (in.hasNext()) {
/* 167 */           String name = in.nextName();
/* 168 */           ReflectiveTypeAdapterFactory.BoundField field = this.boundFields.get(name);
/* 169 */           if (field == null || !field.deserialized) {
/* 170 */             in.skipValue(); continue;
/*     */           } 
/* 172 */           field.read(in, instance);
/*     */         }
/*     */       
/* 175 */       } catch (IllegalStateException e) {
/* 176 */         throw new JsonSyntaxException(e);
/* 177 */       } catch (IllegalAccessException e) {
/* 178 */         throw new AssertionError(e);
/*     */       } 
/* 180 */       in.endObject();
/* 181 */       return instance;
/*     */     }
/*     */     
/*     */     public void write(JsonWriter out, T value) throws IOException {
/* 185 */       if (value == null) {
/* 186 */         out.nullValue();
/*     */         
/*     */         return;
/*     */       } 
/* 190 */       out.beginObject();
/*     */       try {
/* 192 */         for (ReflectiveTypeAdapterFactory.BoundField boundField : this.boundFields.values()) {
/* 193 */           if (boundField.serialized) {
/* 194 */             out.name(boundField.name);
/* 195 */             boundField.write(out, value);
/*     */           } 
/*     */         } 
/* 198 */       } catch (IllegalAccessException e) {
/* 199 */         throw new AssertionError();
/*     */       } 
/* 201 */       out.endObject();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\gson\internal\bind\ReflectiveTypeAdapterFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */