/*    */ package com.google.common.base;
/*    */ 
/*    */ import java.util.Collections;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import javax.annotation.Nullable;
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
/*    */ public final class Defaults
/*    */ {
/*    */   private static final Map<Class<?>, Object> DEFAULTS;
/*    */   
/*    */   static {
/* 40 */     Map<Class<?>, Object> map = new HashMap<Class<?>, Object>();
/* 41 */     put(map, boolean.class, Boolean.valueOf(false));
/* 42 */     put(map, char.class, Character.valueOf(false));
/* 43 */     put(map, byte.class, Byte.valueOf((byte)0));
/* 44 */     put(map, short.class, Short.valueOf((short)0));
/* 45 */     put(map, int.class, Integer.valueOf(0));
/* 46 */     put(map, long.class, Long.valueOf(0L));
/* 47 */     put(map, float.class, Float.valueOf(0.0F));
/* 48 */     put(map, double.class, Double.valueOf(0.0D));
/* 49 */     DEFAULTS = Collections.unmodifiableMap(map);
/*    */   }
/*    */   
/*    */   private static <T> void put(Map<Class<?>, Object> map, Class<T> type, T value) {
/* 53 */     map.put(type, value);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public static <T> T defaultValue(Class<T> type) {
/* 65 */     T t = (T)DEFAULTS.get(Preconditions.checkNotNull(type));
/* 66 */     return t;
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\base\Defaults.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */