/*     */ package com.google.common.base;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import java.io.Serializable;
/*     */ import java.util.concurrent.TimeUnit;
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
/*     */ @GwtCompatible
/*     */ public final class Suppliers
/*     */ {
/*     */   public static <F, T> Supplier<T> compose(Function<? super F, T> function, Supplier<F> supplier) {
/*  51 */     Preconditions.checkNotNull(function);
/*  52 */     Preconditions.checkNotNull(supplier);
/*  53 */     return new SupplierComposition<F, T>(function, supplier);
/*     */   }
/*     */   
/*     */   private static class SupplierComposition<F, T> implements Supplier<T>, Serializable {
/*     */     final Function<? super F, T> function;
/*     */     final Supplier<F> supplier;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     SupplierComposition(Function<? super F, T> function, Supplier<F> supplier) {
/*  62 */       this.function = function;
/*  63 */       this.supplier = supplier;
/*     */     }
/*     */     
/*     */     public T get() {
/*  67 */       return this.function.apply(this.supplier.get());
/*     */     }
/*     */     
/*     */     public boolean equals(@Nullable Object obj) {
/*  71 */       if (obj instanceof SupplierComposition) {
/*  72 */         SupplierComposition<?, ?> that = (SupplierComposition<?, ?>)obj;
/*  73 */         return (this.function.equals(that.function) && this.supplier.equals(that.supplier));
/*     */       } 
/*  75 */       return false;
/*     */     }
/*     */     
/*     */     public int hashCode() {
/*  79 */       return Objects.hashCode(new Object[] { this.function, this.supplier });
/*     */     }
/*     */     
/*     */     public String toString() {
/*  83 */       String str1 = String.valueOf(String.valueOf(this.function)), str2 = String.valueOf(String.valueOf(this.supplier)); return (new StringBuilder(21 + str1.length() + str2.length())).append("Suppliers.compose(").append(str1).append(", ").append(str2).append(")").toString();
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
/*     */   public static <T> Supplier<T> memoize(Supplier<T> delegate) {
/* 103 */     return (delegate instanceof MemoizingSupplier) ? delegate : new MemoizingSupplier<T>(Preconditions.<Supplier<T>>checkNotNull(delegate));
/*     */   }
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   static class MemoizingSupplier<T>
/*     */     implements Supplier<T>, Serializable
/*     */   {
/*     */     final Supplier<T> delegate;
/*     */     volatile transient boolean initialized;
/*     */     transient T value;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     MemoizingSupplier(Supplier<T> delegate) {
/* 117 */       this.delegate = delegate;
/*     */     }
/*     */ 
/*     */     
/*     */     public T get() {
/* 122 */       if (!this.initialized) {
/* 123 */         synchronized (this) {
/* 124 */           if (!this.initialized) {
/* 125 */             T t = this.delegate.get();
/* 126 */             this.value = t;
/* 127 */             this.initialized = true;
/* 128 */             return t;
/*     */           } 
/*     */         } 
/*     */       }
/* 132 */       return this.value;
/*     */     }
/*     */     
/*     */     public String toString() {
/* 136 */       String str = String.valueOf(String.valueOf(this.delegate)); return (new StringBuilder(19 + str.length())).append("Suppliers.memoize(").append(str).append(")").toString();
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
/*     */   public static <T> Supplier<T> memoizeWithExpiration(Supplier<T> delegate, long duration, TimeUnit unit) {
/* 162 */     return new ExpiringMemoizingSupplier<T>(delegate, duration, unit);
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   static class ExpiringMemoizingSupplier<T>
/*     */     implements Supplier<T>, Serializable {
/*     */     final Supplier<T> delegate;
/*     */     final long durationNanos;
/*     */     volatile transient T value;
/*     */     volatile transient long expirationNanos;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     ExpiringMemoizingSupplier(Supplier<T> delegate, long duration, TimeUnit unit) {
/* 175 */       this.delegate = Preconditions.<Supplier<T>>checkNotNull(delegate);
/* 176 */       this.durationNanos = unit.toNanos(duration);
/* 177 */       Preconditions.checkArgument((duration > 0L));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public T get() {
/* 187 */       long nanos = this.expirationNanos;
/* 188 */       long now = Platform.systemNanoTime();
/* 189 */       if (nanos == 0L || now - nanos >= 0L) {
/* 190 */         synchronized (this) {
/* 191 */           if (nanos == this.expirationNanos) {
/* 192 */             T t = this.delegate.get();
/* 193 */             this.value = t;
/* 194 */             nanos = now + this.durationNanos;
/*     */ 
/*     */             
/* 197 */             this.expirationNanos = (nanos == 0L) ? 1L : nanos;
/* 198 */             return t;
/*     */           } 
/*     */         } 
/*     */       }
/* 202 */       return this.value;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public String toString() {
/* 208 */       String str = String.valueOf(String.valueOf(this.delegate)); long l = this.durationNanos; return (new StringBuilder(62 + str.length())).append("Suppliers.memoizeWithExpiration(").append(str).append(", ").append(l).append(", NANOS)").toString();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> Supplier<T> ofInstance(@Nullable T instance) {
/* 219 */     return new SupplierOfInstance<T>(instance);
/*     */   }
/*     */   
/*     */   private static class SupplierOfInstance<T> implements Supplier<T>, Serializable {
/*     */     final T instance;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     SupplierOfInstance(@Nullable T instance) {
/* 227 */       this.instance = instance;
/*     */     }
/*     */     
/*     */     public T get() {
/* 231 */       return this.instance;
/*     */     }
/*     */     
/*     */     public boolean equals(@Nullable Object obj) {
/* 235 */       if (obj instanceof SupplierOfInstance) {
/* 236 */         SupplierOfInstance<?> that = (SupplierOfInstance)obj;
/* 237 */         return Objects.equal(this.instance, that.instance);
/*     */       } 
/* 239 */       return false;
/*     */     }
/*     */     
/*     */     public int hashCode() {
/* 243 */       return Objects.hashCode(new Object[] { this.instance });
/*     */     }
/*     */     
/*     */     public String toString() {
/* 247 */       String str = String.valueOf(String.valueOf(this.instance)); return (new StringBuilder(22 + str.length())).append("Suppliers.ofInstance(").append(str).append(")").toString();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> Supplier<T> synchronizedSupplier(Supplier<T> delegate) {
/* 258 */     return new ThreadSafeSupplier<T>(Preconditions.<Supplier<T>>checkNotNull(delegate));
/*     */   }
/*     */   
/*     */   private static class ThreadSafeSupplier<T> implements Supplier<T>, Serializable {
/*     */     final Supplier<T> delegate;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     ThreadSafeSupplier(Supplier<T> delegate) {
/* 266 */       this.delegate = delegate;
/*     */     }
/*     */     
/*     */     public T get() {
/* 270 */       synchronized (this.delegate) {
/* 271 */         return this.delegate.get();
/*     */       } 
/*     */     }
/*     */     
/*     */     public String toString() {
/* 276 */       String str = String.valueOf(String.valueOf(this.delegate)); return (new StringBuilder(32 + str.length())).append("Suppliers.synchronizedSupplier(").append(str).append(")").toString();
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
/*     */   @Beta
/*     */   public static <T> Function<Supplier<T>, T> supplierFunction() {
/* 291 */     SupplierFunction<T> sf = SupplierFunctionImpl.INSTANCE;
/* 292 */     return sf;
/*     */   }
/*     */   
/*     */   private static interface SupplierFunction<T> extends Function<Supplier<T>, T> {}
/*     */   
/*     */   private enum SupplierFunctionImpl implements SupplierFunction<Object> {
/* 298 */     INSTANCE;
/*     */ 
/*     */     
/*     */     public Object apply(Supplier<Object> input) {
/* 302 */       return input.get();
/*     */     }
/*     */     
/*     */     public String toString() {
/* 306 */       return "Suppliers.supplierFunction()";
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\base\Suppliers.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */