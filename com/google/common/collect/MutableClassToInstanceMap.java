/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.primitives.Primitives;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class MutableClassToInstanceMap<B>
/*    */   extends MapConstraints.ConstrainedMap<Class<? extends B>, B>
/*    */   implements ClassToInstanceMap<B>
/*    */ {
/*    */   public static <B> MutableClassToInstanceMap<B> create() {
/* 45 */     return new MutableClassToInstanceMap<B>(new HashMap<Class<? extends B>, B>());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static <B> MutableClassToInstanceMap<B> create(Map<Class<? extends B>, B> backingMap) {
/* 56 */     return new MutableClassToInstanceMap<B>(backingMap);
/*    */   }
/*    */   
/*    */   private MutableClassToInstanceMap(Map<Class<? extends B>, B> delegate) {
/* 60 */     super(delegate, (MapConstraint)VALUE_CAN_BE_CAST_TO_KEY);
/*    */   }
/*    */   
/* 63 */   private static final MapConstraint<Class<?>, Object> VALUE_CAN_BE_CAST_TO_KEY = new MapConstraint<Class<?>, Object>()
/*    */     {
/*    */       public void checkKeyValue(Class<?> key, Object value)
/*    */       {
/* 67 */         MutableClassToInstanceMap.cast((Class)key, (B)value);
/*    */       }
/*    */     };
/*    */ 
/*    */   
/*    */   public <T extends B> T putInstance(Class<T> type, T value) {
/* 73 */     return cast(type, put(type, (B)value));
/*    */   }
/*    */   private static final long serialVersionUID = 0L;
/*    */   
/*    */   public <T extends B> T getInstance(Class<T> type) {
/* 78 */     return cast(type, get(type));
/*    */   }
/*    */   
/*    */   private static <B, T extends B> T cast(Class<T> type, B value) {
/* 82 */     return Primitives.wrap(type).cast(value);
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\collect\MutableClassToInstanceMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */