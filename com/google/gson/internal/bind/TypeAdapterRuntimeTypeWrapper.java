/*    */ package com.google.gson.internal.bind;
/*    */ 
/*    */ import com.google.gson.Gson;
/*    */ import com.google.gson.TypeAdapter;
/*    */ import com.google.gson.reflect.TypeToken;
/*    */ import com.google.gson.stream.JsonReader;
/*    */ import com.google.gson.stream.JsonWriter;
/*    */ import java.io.IOException;
/*    */ import java.lang.reflect.Type;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ final class TypeAdapterRuntimeTypeWrapper<T>
/*    */   extends TypeAdapter<T>
/*    */ {
/*    */   private final Gson context;
/*    */   private final TypeAdapter<T> delegate;
/*    */   private final Type type;
/*    */   
/*    */   TypeAdapterRuntimeTypeWrapper(Gson context, TypeAdapter<T> delegate, Type type) {
/* 33 */     this.context = context;
/* 34 */     this.delegate = delegate;
/* 35 */     this.type = type;
/*    */   }
/*    */ 
/*    */   
/*    */   public T read(JsonReader in) throws IOException {
/* 40 */     return (T)this.delegate.read(in);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void write(JsonWriter out, T value) throws IOException {
/* 52 */     TypeAdapter<T> chosen = this.delegate;
/* 53 */     Type runtimeType = getRuntimeTypeIfMoreSpecific(this.type, value);
/* 54 */     if (runtimeType != this.type) {
/* 55 */       TypeAdapter<T> runtimeTypeAdapter = this.context.getAdapter(TypeToken.get(runtimeType));
/* 56 */       if (!(runtimeTypeAdapter instanceof ReflectiveTypeAdapterFactory.Adapter)) {
/*    */         
/* 58 */         chosen = runtimeTypeAdapter;
/* 59 */       } else if (!(this.delegate instanceof ReflectiveTypeAdapterFactory.Adapter)) {
/*    */ 
/*    */         
/* 62 */         chosen = this.delegate;
/*    */       } else {
/*    */         
/* 65 */         chosen = runtimeTypeAdapter;
/*    */       } 
/*    */     } 
/* 68 */     chosen.write(out, value);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private Type getRuntimeTypeIfMoreSpecific(Type<?> type, Object value) {
/* 75 */     if (value != null && (type == Object.class || type instanceof java.lang.reflect.TypeVariable || type instanceof Class))
/*    */     {
/* 77 */       type = value.getClass();
/*    */     }
/* 79 */     return type;
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\gson\internal\bind\TypeAdapterRuntimeTypeWrapper.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */