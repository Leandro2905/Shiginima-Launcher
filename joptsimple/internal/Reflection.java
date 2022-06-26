/*     */ package joptsimple.internal;
/*     */ 
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import joptsimple.ValueConverter;
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
/*     */ public final class Reflection
/*     */ {
/*     */   private Reflection() {
/*  45 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <V> ValueConverter<V> findConverter(Class<V> clazz) {
/*  56 */     Class<V> maybeWrapper = Classes.wrapperOf(clazz);
/*     */     
/*  58 */     ValueConverter<V> valueOf = valueOfConverter(maybeWrapper);
/*  59 */     if (valueOf != null) {
/*  60 */       return valueOf;
/*     */     }
/*  62 */     ValueConverter<V> constructor = constructorConverter(maybeWrapper);
/*  63 */     if (constructor != null) {
/*  64 */       return constructor;
/*     */     }
/*  66 */     throw new IllegalArgumentException(clazz + " is not a value type");
/*     */   }
/*     */   
/*     */   private static <V> ValueConverter<V> valueOfConverter(Class<V> clazz) {
/*     */     try {
/*  71 */       Method valueOf = clazz.getDeclaredMethod("valueOf", new Class[] { String.class });
/*  72 */       if (meetsConverterRequirements(valueOf, clazz)) {
/*  73 */         return new MethodInvokingValueConverter<V>(valueOf, clazz);
/*     */       }
/*  75 */       return null;
/*     */     }
/*  77 */     catch (NoSuchMethodException ignored) {
/*  78 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   private static <V> ValueConverter<V> constructorConverter(Class<V> clazz) {
/*     */     try {
/*  84 */       return new ConstructorInvokingValueConverter<V>(clazz.getConstructor(new Class[] { String.class }));
/*     */     }
/*  86 */     catch (NoSuchMethodException ignored) {
/*  87 */       return null;
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
/*     */   public static <T> T instantiate(Constructor<T> constructor, Object... args) {
/*     */     try {
/* 102 */       return constructor.newInstance(args);
/*     */     }
/* 104 */     catch (Exception ex) {
/* 105 */       throw reflectionException(ex);
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
/*     */   public static Object invoke(Method method, Object... args) {
/*     */     try {
/* 119 */       return method.invoke(null, args);
/*     */     }
/* 121 */     catch (Exception ex) {
/* 122 */       throw reflectionException(ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static <V> V convertWith(ValueConverter<V> converter, String raw) {
/* 128 */     return (converter == null) ? (V)raw : (V)converter.convert(raw);
/*     */   }
/*     */   
/*     */   private static boolean meetsConverterRequirements(Method method, Class<?> expectedReturnType) {
/* 132 */     int modifiers = method.getModifiers();
/* 133 */     return (Modifier.isPublic(modifiers) && Modifier.isStatic(modifiers) && expectedReturnType.equals(method.getReturnType()));
/*     */   }
/*     */   
/*     */   private static RuntimeException reflectionException(Exception ex) {
/* 137 */     if (ex instanceof IllegalArgumentException)
/* 138 */       return new ReflectionException(ex); 
/* 139 */     if (ex instanceof java.lang.reflect.InvocationTargetException)
/* 140 */       return new ReflectionException(ex.getCause()); 
/* 141 */     if (ex instanceof RuntimeException) {
/* 142 */       return (RuntimeException)ex;
/*     */     }
/* 144 */     return new ReflectionException(ex);
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\joptsimple\internal\Reflection.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */