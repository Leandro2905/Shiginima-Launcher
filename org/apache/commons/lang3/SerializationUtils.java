/*     */ package org.apache.commons.lang3;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.ObjectStreamClass;
/*     */ import java.io.OutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.HashMap;
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
/*     */ public class SerializationUtils
/*     */ {
/*     */   public static <T extends Serializable> T clone(T object) {
/*  79 */     if (object == null) {
/*  80 */       return null;
/*     */     }
/*  82 */     byte[] objectData = serialize((Serializable)object);
/*  83 */     ByteArrayInputStream bais = new ByteArrayInputStream(objectData);
/*     */     
/*  85 */     ClassLoaderAwareObjectInputStream in = null;
/*     */     
/*     */     try {
/*  88 */       in = new ClassLoaderAwareObjectInputStream(bais, object.getClass().getClassLoader());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  95 */       Serializable serializable = (Serializable)in.readObject();
/*  96 */       return (T)serializable;
/*     */     }
/*  98 */     catch (ClassNotFoundException ex) {
/*  99 */       throw new SerializationException("ClassNotFoundException while reading cloned object data", ex);
/* 100 */     } catch (IOException ex) {
/* 101 */       throw new SerializationException("IOException while reading cloned object data", ex);
/*     */     } finally {
/*     */       try {
/* 104 */         if (in != null) {
/* 105 */           in.close();
/*     */         }
/* 107 */       } catch (IOException ex) {
/* 108 */         throw new SerializationException("IOException on closing cloned object data InputStream.", ex);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T extends Serializable> T roundtrip(T msg) {
/* 125 */     return (T)deserialize(serialize((Serializable)msg));
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void serialize(Serializable obj, OutputStream outputStream) {
/* 146 */     if (outputStream == null) {
/* 147 */       throw new IllegalArgumentException("The OutputStream must not be null");
/*     */     }
/* 149 */     ObjectOutputStream out = null;
/*     */     
/*     */     try {
/* 152 */       out = new ObjectOutputStream(outputStream);
/* 153 */       out.writeObject(obj);
/*     */     }
/* 155 */     catch (IOException ex) {
/* 156 */       throw new SerializationException(ex);
/*     */     } finally {
/*     */       try {
/* 159 */         if (out != null) {
/* 160 */           out.close();
/*     */         }
/* 162 */       } catch (IOException ex) {}
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
/*     */   public static byte[] serialize(Serializable obj) {
/* 177 */     ByteArrayOutputStream baos = new ByteArrayOutputStream(512);
/* 178 */     serialize(obj, baos);
/* 179 */     return baos.toByteArray();
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
/*     */   public static <T> T deserialize(InputStream inputStream) {
/* 215 */     if (inputStream == null) {
/* 216 */       throw new IllegalArgumentException("The InputStream must not be null");
/*     */     }
/* 218 */     ObjectInputStream in = null;
/*     */     
/*     */     try {
/* 221 */       in = new ObjectInputStream(inputStream);
/*     */       
/* 223 */       T obj = (T)in.readObject();
/* 224 */       return obj;
/*     */     }
/* 226 */     catch (ClassCastException ex) {
/* 227 */       throw new SerializationException(ex);
/* 228 */     } catch (ClassNotFoundException ex) {
/* 229 */       throw new SerializationException(ex);
/* 230 */     } catch (IOException ex) {
/* 231 */       throw new SerializationException(ex);
/*     */     } finally {
/*     */       try {
/* 234 */         if (in != null) {
/* 235 */           in.close();
/*     */         }
/* 237 */       } catch (IOException ex) {}
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
/*     */   public static <T> T deserialize(byte[] objectData) {
/* 264 */     if (objectData == null) {
/* 265 */       throw new IllegalArgumentException("The byte[] must not be null");
/*     */     }
/* 267 */     return deserialize(new ByteArrayInputStream(objectData));
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
/*     */   static class ClassLoaderAwareObjectInputStream
/*     */     extends ObjectInputStream
/*     */   {
/* 284 */     private static final Map<String, Class<?>> primitiveTypes = new HashMap<String, Class<?>>();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final ClassLoader classLoader;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ClassLoaderAwareObjectInputStream(InputStream in, ClassLoader classLoader) throws IOException {
/* 296 */       super(in);
/* 297 */       this.classLoader = classLoader;
/*     */       
/* 299 */       primitiveTypes.put("byte", byte.class);
/* 300 */       primitiveTypes.put("short", short.class);
/* 301 */       primitiveTypes.put("int", int.class);
/* 302 */       primitiveTypes.put("long", long.class);
/* 303 */       primitiveTypes.put("float", float.class);
/* 304 */       primitiveTypes.put("double", double.class);
/* 305 */       primitiveTypes.put("boolean", boolean.class);
/* 306 */       primitiveTypes.put("char", char.class);
/* 307 */       primitiveTypes.put("void", void.class);
/*     */     }
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
/*     */     protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
/* 320 */       String name = desc.getName();
/*     */       try {
/* 322 */         return Class.forName(name, false, this.classLoader);
/* 323 */       } catch (ClassNotFoundException ex) {
/*     */         try {
/* 325 */           return Class.forName(name, false, Thread.currentThread().getContextClassLoader());
/* 326 */         } catch (ClassNotFoundException cnfe) {
/* 327 */           Class<?> cls = primitiveTypes.get(name);
/* 328 */           if (cls != null) {
/* 329 */             return cls;
/*     */           }
/* 331 */           throw cnfe;
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\commons\lang3\SerializationUtils.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */