/*     */ package com.google.common.base;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import java.io.Serializable;
/*     */ import java.util.Map;
/*     */ import javax.annotation.Nullable;
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
/*     */ @GwtCompatible
/*     */ public final class Functions
/*     */ {
/*     */   public static Function<Object, String> toStringFunction() {
/*  56 */     return ToStringFunction.INSTANCE;
/*     */   }
/*     */   
/*     */   private enum ToStringFunction
/*     */     implements Function<Object, String> {
/*  61 */     INSTANCE;
/*     */ 
/*     */     
/*     */     public String apply(Object o) {
/*  65 */       Preconditions.checkNotNull(o);
/*  66 */       return o.toString();
/*     */     }
/*     */     
/*     */     public String toString() {
/*  70 */       return "toString";
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> Function<E, E> identity() {
/*  80 */     return IdentityFunction.INSTANCE;
/*     */   }
/*     */   
/*     */   private enum IdentityFunction
/*     */     implements Function<Object, Object> {
/*  85 */     INSTANCE;
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public Object apply(@Nullable Object o) {
/*  90 */       return o;
/*     */     }
/*     */     
/*     */     public String toString() {
/*  94 */       return "identity";
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
/*     */   public static <K, V> Function<K, V> forMap(Map<K, V> map) {
/* 108 */     return new FunctionForMapNoDefault<K, V>(map);
/*     */   }
/*     */   
/*     */   private static class FunctionForMapNoDefault<K, V> implements Function<K, V>, Serializable {
/*     */     final Map<K, V> map;
/*     */     
/*     */     FunctionForMapNoDefault(Map<K, V> map) {
/* 115 */       this.map = Preconditions.<Map<K, V>>checkNotNull(map);
/*     */     }
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     public V apply(@Nullable K key) {
/* 120 */       V result = this.map.get(key);
/* 121 */       Preconditions.checkArgument((result != null || this.map.containsKey(key)), "Key '%s' not present in map", new Object[] { key });
/* 122 */       return result;
/*     */     }
/*     */     
/*     */     public boolean equals(@Nullable Object o) {
/* 126 */       if (o instanceof FunctionForMapNoDefault) {
/* 127 */         FunctionForMapNoDefault<?, ?> that = (FunctionForMapNoDefault<?, ?>)o;
/* 128 */         return this.map.equals(that.map);
/*     */       } 
/* 130 */       return false;
/*     */     }
/*     */     
/*     */     public int hashCode() {
/* 134 */       return this.map.hashCode();
/*     */     }
/*     */     
/*     */     public String toString() {
/* 138 */       String str = String.valueOf(String.valueOf(this.map)); return (new StringBuilder(8 + str.length())).append("forMap(").append(str).append(")").toString();
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
/*     */   public static <K, V> Function<K, V> forMap(Map<K, ? extends V> map, @Nullable V defaultValue) {
/* 155 */     return new ForMapWithDefault<K, V>(map, defaultValue);
/*     */   }
/*     */   
/*     */   private static class ForMapWithDefault<K, V> implements Function<K, V>, Serializable { final Map<K, ? extends V> map;
/*     */     final V defaultValue;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     ForMapWithDefault(Map<K, ? extends V> map, @Nullable V defaultValue) {
/* 163 */       this.map = Preconditions.<Map<K, ? extends V>>checkNotNull(map);
/* 164 */       this.defaultValue = defaultValue;
/*     */     }
/*     */ 
/*     */     
/*     */     public V apply(@Nullable K key) {
/* 169 */       V result = this.map.get(key);
/* 170 */       return (result != null || this.map.containsKey(key)) ? result : this.defaultValue;
/*     */     }
/*     */     
/*     */     public boolean equals(@Nullable Object o) {
/* 174 */       if (o instanceof ForMapWithDefault) {
/* 175 */         ForMapWithDefault<?, ?> that = (ForMapWithDefault<?, ?>)o;
/* 176 */         return (this.map.equals(that.map) && Objects.equal(this.defaultValue, that.defaultValue));
/*     */       } 
/* 178 */       return false;
/*     */     }
/*     */     
/*     */     public int hashCode() {
/* 182 */       return Objects.hashCode(new Object[] { this.map, this.defaultValue });
/*     */     }
/*     */     
/*     */     public String toString() {
/* 186 */       String str1 = String.valueOf(String.valueOf(this.map)), str2 = String.valueOf(String.valueOf(this.defaultValue)); return (new StringBuilder(23 + str1.length() + str2.length())).append("forMap(").append(str1).append(", defaultValue=").append(str2).append(")").toString();
/*     */     } }
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
/*     */   public static <A, B, C> Function<A, C> compose(Function<B, C> g, Function<A, ? extends B> f) {
/* 202 */     return new FunctionComposition<A, B, C>(g, f);
/*     */   }
/*     */   
/*     */   private static class FunctionComposition<A, B, C> implements Function<A, C>, Serializable { private final Function<B, C> g;
/*     */     private final Function<A, ? extends B> f;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     public FunctionComposition(Function<B, C> g, Function<A, ? extends B> f) {
/* 210 */       this.g = Preconditions.<Function<B, C>>checkNotNull(g);
/* 211 */       this.f = Preconditions.<Function<A, ? extends B>>checkNotNull(f);
/*     */     }
/*     */ 
/*     */     
/*     */     public C apply(@Nullable A a) {
/* 216 */       return this.g.apply(this.f.apply(a));
/*     */     }
/*     */     
/*     */     public boolean equals(@Nullable Object obj) {
/* 220 */       if (obj instanceof FunctionComposition) {
/* 221 */         FunctionComposition<?, ?, ?> that = (FunctionComposition<?, ?, ?>)obj;
/* 222 */         return (this.f.equals(that.f) && this.g.equals(that.g));
/*     */       } 
/* 224 */       return false;
/*     */     }
/*     */     
/*     */     public int hashCode() {
/* 228 */       return this.f.hashCode() ^ this.g.hashCode();
/*     */     }
/*     */     
/*     */     public String toString() {
/* 232 */       String str1 = String.valueOf(String.valueOf(this.g)), str2 = String.valueOf(String.valueOf(this.f)); return (new StringBuilder(2 + str1.length() + str2.length())).append(str1).append("(").append(str2).append(")").toString();
/*     */     } }
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
/*     */   public static <T> Function<T, Boolean> forPredicate(Predicate<T> predicate) {
/* 245 */     return new PredicateFunction<T>(predicate);
/*     */   }
/*     */   
/*     */   private static class PredicateFunction<T> implements Function<T, Boolean>, Serializable {
/*     */     private final Predicate<T> predicate;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     private PredicateFunction(Predicate<T> predicate) {
/* 253 */       this.predicate = Preconditions.<Predicate<T>>checkNotNull(predicate);
/*     */     }
/*     */ 
/*     */     
/*     */     public Boolean apply(@Nullable T t) {
/* 258 */       return Boolean.valueOf(this.predicate.apply(t));
/*     */     }
/*     */     
/*     */     public boolean equals(@Nullable Object obj) {
/* 262 */       if (obj instanceof PredicateFunction) {
/* 263 */         PredicateFunction<?> that = (PredicateFunction)obj;
/* 264 */         return this.predicate.equals(that.predicate);
/*     */       } 
/* 266 */       return false;
/*     */     }
/*     */     
/*     */     public int hashCode() {
/* 270 */       return this.predicate.hashCode();
/*     */     }
/*     */     
/*     */     public String toString() {
/* 274 */       String str = String.valueOf(String.valueOf(this.predicate)); return (new StringBuilder(14 + str.length())).append("forPredicate(").append(str).append(")").toString();
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
/*     */   public static <E> Function<Object, E> constant(@Nullable E value) {
/* 287 */     return new ConstantFunction<E>(value);
/*     */   }
/*     */   
/*     */   private static class ConstantFunction<E> implements Function<Object, E>, Serializable { private final E value;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     public ConstantFunction(@Nullable E value) {
/* 294 */       this.value = value;
/*     */     }
/*     */ 
/*     */     
/*     */     public E apply(@Nullable Object from) {
/* 299 */       return this.value;
/*     */     }
/*     */     
/*     */     public boolean equals(@Nullable Object obj) {
/* 303 */       if (obj instanceof ConstantFunction) {
/* 304 */         ConstantFunction<?> that = (ConstantFunction)obj;
/* 305 */         return Objects.equal(this.value, that.value);
/*     */       } 
/* 307 */       return false;
/*     */     }
/*     */     
/*     */     public int hashCode() {
/* 311 */       return (this.value == null) ? 0 : this.value.hashCode();
/*     */     }
/*     */     
/*     */     public String toString() {
/* 315 */       String str = String.valueOf(String.valueOf(this.value)); return (new StringBuilder(10 + str.length())).append("constant(").append(str).append(")").toString();
/*     */     } }
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
/*     */   @Beta
/*     */   public static <T> Function<Object, T> forSupplier(Supplier<T> supplier) {
/* 329 */     return new SupplierFunction<T>(supplier);
/*     */   }
/*     */   
/*     */   private static class SupplierFunction<T>
/*     */     implements Function<Object, T>, Serializable {
/*     */     private final Supplier<T> supplier;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     private SupplierFunction(Supplier<T> supplier) {
/* 338 */       this.supplier = Preconditions.<Supplier<T>>checkNotNull(supplier);
/*     */     }
/*     */     
/*     */     public T apply(@Nullable Object input) {
/* 342 */       return this.supplier.get();
/*     */     }
/*     */     
/*     */     public boolean equals(@Nullable Object obj) {
/* 346 */       if (obj instanceof SupplierFunction) {
/* 347 */         SupplierFunction<?> that = (SupplierFunction)obj;
/* 348 */         return this.supplier.equals(that.supplier);
/*     */       } 
/* 350 */       return false;
/*     */     }
/*     */     
/*     */     public int hashCode() {
/* 354 */       return this.supplier.hashCode();
/*     */     }
/*     */     
/*     */     public String toString() {
/* 358 */       String str = String.valueOf(String.valueOf(this.supplier)); return (new StringBuilder(13 + str.length())).append("forSupplier(").append(str).append(")").toString();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\base\Functions.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */