/*    */ package joptsimple.internal;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
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
/*    */ public final class Classes
/*    */ {
/* 35 */   private static final Map<Class<?>, Class<?>> WRAPPERS = new HashMap<Class<?>, Class<?>>(13);
/*    */   
/*    */   static {
/* 38 */     WRAPPERS.put(boolean.class, Boolean.class);
/* 39 */     WRAPPERS.put(byte.class, Byte.class);
/* 40 */     WRAPPERS.put(char.class, Character.class);
/* 41 */     WRAPPERS.put(double.class, Double.class);
/* 42 */     WRAPPERS.put(float.class, Float.class);
/* 43 */     WRAPPERS.put(int.class, Integer.class);
/* 44 */     WRAPPERS.put(long.class, Long.class);
/* 45 */     WRAPPERS.put(short.class, Short.class);
/* 46 */     WRAPPERS.put(void.class, Void.class);
/*    */   }
/*    */   
/*    */   private Classes() {
/* 50 */     throw new UnsupportedOperationException();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static String shortNameOf(String className) {
/* 60 */     return className.substring(className.lastIndexOf('.') + 1);
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
/*    */   public static <T> Class<T> wrapperOf(Class<T> clazz) {
/* 72 */     return clazz.isPrimitive() ? (Class<T>)WRAPPERS.get(clazz) : clazz;
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\joptsimple\internal\Classes.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */