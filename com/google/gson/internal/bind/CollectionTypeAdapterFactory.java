/*    */ package com.google.gson.internal.bind;
/*    */ 
/*    */ import com.google.gson.Gson;
/*    */ import com.google.gson.TypeAdapter;
/*    */ import com.google.gson.TypeAdapterFactory;
/*    */ import com.google.gson.internal.;
/*    */ import com.google.gson.internal.ConstructorConstructor;
/*    */ import com.google.gson.internal.ObjectConstructor;
/*    */ import com.google.gson.reflect.TypeToken;
/*    */ import com.google.gson.stream.JsonReader;
/*    */ import com.google.gson.stream.JsonToken;
/*    */ import com.google.gson.stream.JsonWriter;
/*    */ import java.io.IOException;
/*    */ import java.lang.reflect.Type;
/*    */ import java.util.Collection;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class CollectionTypeAdapterFactory
/*    */   implements TypeAdapterFactory
/*    */ {
/*    */   private final ConstructorConstructor constructorConstructor;
/*    */   
/*    */   public CollectionTypeAdapterFactory(ConstructorConstructor constructorConstructor) {
/* 40 */     this.constructorConstructor = constructorConstructor;
/*    */   }
/*    */   
/*    */   public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
/* 44 */     Type type = typeToken.getType();
/*    */     
/* 46 */     Class<? super T> rawType = typeToken.getRawType();
/* 47 */     if (!Collection.class.isAssignableFrom(rawType)) {
/* 48 */       return null;
/*    */     }
/*    */     
/* 51 */     Type elementType = .Gson.Types.getCollectionElementType(type, rawType);
/* 52 */     TypeAdapter<?> elementTypeAdapter = gson.getAdapter(TypeToken.get(elementType));
/* 53 */     ObjectConstructor<T> constructor = this.constructorConstructor.get(typeToken);
/*    */ 
/*    */     
/* 56 */     TypeAdapter<T> result = new Adapter(gson, elementType, elementTypeAdapter, (ObjectConstructor)constructor);
/* 57 */     return result;
/*    */   }
/*    */   
/*    */   private static final class Adapter<E>
/*    */     extends TypeAdapter<Collection<E>>
/*    */   {
/*    */     private final TypeAdapter<E> elementTypeAdapter;
/*    */     private final ObjectConstructor<? extends Collection<E>> constructor;
/*    */     
/*    */     public Adapter(Gson context, Type elementType, TypeAdapter<E> elementTypeAdapter, ObjectConstructor<? extends Collection<E>> constructor) {
/* 67 */       this.elementTypeAdapter = new TypeAdapterRuntimeTypeWrapper<E>(context, elementTypeAdapter, elementType);
/*    */       
/* 69 */       this.constructor = constructor;
/*    */     }
/*    */     
/*    */     public Collection<E> read(JsonReader in) throws IOException {
/* 73 */       if (in.peek() == JsonToken.NULL) {
/* 74 */         in.nextNull();
/* 75 */         return null;
/*    */       } 
/*    */       
/* 78 */       Collection<E> collection = (Collection<E>)this.constructor.construct();
/* 79 */       in.beginArray();
/* 80 */       while (in.hasNext()) {
/* 81 */         E instance = (E)this.elementTypeAdapter.read(in);
/* 82 */         collection.add(instance);
/*    */       } 
/* 84 */       in.endArray();
/* 85 */       return collection;
/*    */     }
/*    */     
/*    */     public void write(JsonWriter out, Collection<E> collection) throws IOException {
/* 89 */       if (collection == null) {
/* 90 */         out.nullValue();
/*    */         
/*    */         return;
/*    */       } 
/* 94 */       out.beginArray();
/* 95 */       for (E element : collection) {
/* 96 */         this.elementTypeAdapter.write(out, element);
/*    */       }
/* 98 */       out.endArray();
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\gson\internal\bind\CollectionTypeAdapterFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */