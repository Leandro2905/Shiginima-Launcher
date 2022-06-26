/*     */ package com.google.common.collect;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.lang.reflect.Field;
/*     */ import java.util.Collection;
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
/*     */ final class Serialization
/*     */ {
/*     */   static int readCount(ObjectInputStream stream) throws IOException {
/*  50 */     return stream.readInt();
/*     */   }
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
/*     */   static <K, V> void writeMap(Map<K, V> map, ObjectOutputStream stream) throws IOException {
/*  63 */     stream.writeInt(map.size());
/*  64 */     for (Map.Entry<K, V> entry : map.entrySet()) {
/*  65 */       stream.writeObject(entry.getKey());
/*  66 */       stream.writeObject(entry.getValue());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <K, V> void populateMap(Map<K, V> map, ObjectInputStream stream) throws IOException, ClassNotFoundException {
/*  76 */     int size = stream.readInt();
/*  77 */     populateMap(map, stream, size);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <K, V> void populateMap(Map<K, V> map, ObjectInputStream stream, int size) throws IOException, ClassNotFoundException {
/*  87 */     for (int i = 0; i < size; i++) {
/*     */       
/*  89 */       K key = (K)stream.readObject();
/*     */       
/*  91 */       V value = (V)stream.readObject();
/*  92 */       map.put(key, value);
/*     */     } 
/*     */   }
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
/*     */   static <E> void writeMultiset(Multiset<E> multiset, ObjectOutputStream stream) throws IOException {
/* 106 */     int entryCount = multiset.entrySet().size();
/* 107 */     stream.writeInt(entryCount);
/* 108 */     for (Multiset.Entry<E> entry : multiset.entrySet()) {
/* 109 */       stream.writeObject(entry.getElement());
/* 110 */       stream.writeInt(entry.getCount());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <E> void populateMultiset(Multiset<E> multiset, ObjectInputStream stream) throws IOException, ClassNotFoundException {
/* 121 */     int distinctElements = stream.readInt();
/* 122 */     populateMultiset(multiset, stream, distinctElements);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <E> void populateMultiset(Multiset<E> multiset, ObjectInputStream stream, int distinctElements) throws IOException, ClassNotFoundException {
/* 133 */     for (int i = 0; i < distinctElements; i++) {
/*     */       
/* 135 */       E element = (E)stream.readObject();
/* 136 */       int count = stream.readInt();
/* 137 */       multiset.add(element, count);
/*     */     } 
/*     */   }
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
/*     */   static <K, V> void writeMultimap(Multimap<K, V> multimap, ObjectOutputStream stream) throws IOException {
/* 153 */     stream.writeInt(multimap.asMap().size());
/* 154 */     for (Map.Entry<K, Collection<V>> entry : (Iterable<Map.Entry<K, Collection<V>>>)multimap.asMap().entrySet()) {
/* 155 */       stream.writeObject(entry.getKey());
/* 156 */       stream.writeInt(((Collection)entry.getValue()).size());
/* 157 */       for (V value : entry.getValue()) {
/* 158 */         stream.writeObject(value);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <K, V> void populateMultimap(Multimap<K, V> multimap, ObjectInputStream stream) throws IOException, ClassNotFoundException {
/* 170 */     int distinctKeys = stream.readInt();
/* 171 */     populateMultimap(multimap, stream, distinctKeys);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <K, V> void populateMultimap(Multimap<K, V> multimap, ObjectInputStream stream, int distinctKeys) throws IOException, ClassNotFoundException {
/* 182 */     for (int i = 0; i < distinctKeys; i++) {
/*     */       
/* 184 */       K key = (K)stream.readObject();
/* 185 */       Collection<V> values = multimap.get(key);
/* 186 */       int valueCount = stream.readInt();
/* 187 */       for (int j = 0; j < valueCount; j++) {
/*     */         
/* 189 */         V value = (V)stream.readObject();
/* 190 */         values.add(value);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static <T> FieldSetter<T> getFieldSetter(Class<T> clazz, String fieldName) {
/*     */     try {
/* 199 */       Field field = clazz.getDeclaredField(fieldName);
/* 200 */       return new FieldSetter<T>(field);
/* 201 */     } catch (NoSuchFieldException e) {
/* 202 */       throw new AssertionError(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   static final class FieldSetter<T>
/*     */   {
/*     */     private final Field field;
/*     */     
/*     */     private FieldSetter(Field field) {
/* 211 */       this.field = field;
/* 212 */       field.setAccessible(true);
/*     */     }
/*     */     
/*     */     void set(T instance, Object value) {
/*     */       try {
/* 217 */         this.field.set(instance, value);
/* 218 */       } catch (IllegalAccessException impossible) {
/* 219 */         throw new AssertionError(impossible);
/*     */       } 
/*     */     }
/*     */     
/*     */     void set(T instance, int value) {
/*     */       try {
/* 225 */         this.field.set(instance, Integer.valueOf(value));
/* 226 */       } catch (IllegalAccessException impossible) {
/* 227 */         throw new AssertionError(impossible);
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\collect\Serialization.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */