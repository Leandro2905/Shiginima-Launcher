/*     */ package com.google.common.base;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import java.io.Serializable;
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
/*     */ 
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
/*     */ public abstract class Equivalence<T>
/*     */ {
/*     */   public final boolean equivalent(@Nullable T a, @Nullable T b) {
/*  65 */     if (a == b) {
/*  66 */       return true;
/*     */     }
/*  68 */     if (a == null || b == null) {
/*  69 */       return false;
/*     */     }
/*  71 */     return doEquivalent(a, b);
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
/*     */   protected abstract boolean doEquivalent(T paramT1, T paramT2);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final int hash(@Nullable T t) {
/* 101 */     if (t == null) {
/* 102 */       return 0;
/*     */     }
/* 104 */     return doHash(t);
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
/*     */   protected abstract int doHash(T paramT);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final <F> Equivalence<F> onResultOf(Function<F, ? extends T> function) {
/* 140 */     return new FunctionalEquivalence<F, T>(function, this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final <S extends T> Wrapper<S> wrap(@Nullable S reference) {
/* 151 */     return new Wrapper<S>(this, reference);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final class Wrapper<T>
/*     */     implements Serializable
/*     */   {
/*     */     private final Equivalence<? super T> equivalence;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     private final T reference;
/*     */ 
/*     */ 
/*     */     
/*     */     private static final long serialVersionUID = 0L;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private Wrapper(Equivalence<? super T> equivalence, @Nullable T reference) {
/* 177 */       this.equivalence = Preconditions.<Equivalence<? super T>>checkNotNull(equivalence);
/* 178 */       this.reference = reference;
/*     */     }
/*     */     
/*     */     @Nullable
/*     */     public T get() {
/* 183 */       return this.reference;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean equals(@Nullable Object obj) {
/* 192 */       if (obj == this) {
/* 193 */         return true;
/*     */       }
/* 195 */       if (obj instanceof Wrapper) {
/* 196 */         Wrapper<?> that = (Wrapper)obj;
/*     */         
/* 198 */         if (this.equivalence.equals(that.equivalence)) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 204 */           Equivalence<Object> equivalence = (Equivalence)this.equivalence;
/* 205 */           return equivalence.equivalent(this.reference, that.reference);
/*     */         } 
/*     */       } 
/* 208 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 215 */       return this.equivalence.hash(this.reference);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String toString() {
/* 223 */       String str1 = String.valueOf(String.valueOf(this.equivalence)), str2 = String.valueOf(String.valueOf(this.reference)); return (new StringBuilder(7 + str1.length() + str2.length())).append(str1).append(".wrap(").append(str2).append(")").toString();
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
/*     */   @GwtCompatible(serializable = true)
/*     */   public final <S extends T> Equivalence<Iterable<S>> pairwise() {
/* 244 */     return new PairwiseEquivalence<S>(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Beta
/*     */   public final Predicate<T> equivalentTo(@Nullable T target) {
/* 255 */     return new EquivalentToPredicate<T>(this, target);
/*     */   }
/*     */   
/*     */   private static final class EquivalentToPredicate<T> implements Predicate<T>, Serializable { private final Equivalence<T> equivalence;
/*     */     @Nullable
/*     */     private final T target;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     EquivalentToPredicate(Equivalence<T> equivalence, @Nullable T target) {
/* 264 */       this.equivalence = Preconditions.<Equivalence<T>>checkNotNull(equivalence);
/* 265 */       this.target = target;
/*     */     }
/*     */     
/*     */     public boolean apply(@Nullable T input) {
/* 269 */       return this.equivalence.equivalent(input, this.target);
/*     */     }
/*     */     
/*     */     public boolean equals(@Nullable Object obj) {
/* 273 */       if (this == obj) {
/* 274 */         return true;
/*     */       }
/* 276 */       if (obj instanceof EquivalentToPredicate) {
/* 277 */         EquivalentToPredicate<?> that = (EquivalentToPredicate)obj;
/* 278 */         return (this.equivalence.equals(that.equivalence) && Objects.equal(this.target, that.target));
/*     */       } 
/*     */       
/* 281 */       return false;
/*     */     }
/*     */     
/*     */     public int hashCode() {
/* 285 */       return Objects.hashCode(new Object[] { this.equivalence, this.target });
/*     */     }
/*     */     
/*     */     public String toString() {
/* 289 */       String str1 = String.valueOf(String.valueOf(this.equivalence)), str2 = String.valueOf(String.valueOf(this.target)); return (new StringBuilder(15 + str1.length() + str2.length())).append(str1).append(".equivalentTo(").append(str2).append(")").toString();
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
/*     */   
/*     */   public static Equivalence<Object> equals() {
/* 306 */     return Equals.INSTANCE;
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
/*     */   public static Equivalence<Object> identity() {
/* 318 */     return Identity.INSTANCE;
/*     */   }
/*     */   
/*     */   static final class Equals
/*     */     extends Equivalence<Object>
/*     */     implements Serializable {
/* 324 */     static final Equals INSTANCE = new Equals(); private static final long serialVersionUID = 1L;
/*     */     
/*     */     protected boolean doEquivalent(Object a, Object b) {
/* 327 */       return a.equals(b);
/*     */     }
/*     */     protected int doHash(Object o) {
/* 330 */       return o.hashCode();
/*     */     }
/*     */     
/*     */     private Object readResolve() {
/* 334 */       return INSTANCE;
/*     */     }
/*     */   }
/*     */   
/*     */   static final class Identity
/*     */     extends Equivalence<Object>
/*     */     implements Serializable
/*     */   {
/* 342 */     static final Identity INSTANCE = new Identity(); private static final long serialVersionUID = 1L;
/*     */     
/*     */     protected boolean doEquivalent(Object a, Object b) {
/* 345 */       return false;
/*     */     }
/*     */     
/*     */     protected int doHash(Object o) {
/* 349 */       return System.identityHashCode(o);
/*     */     }
/*     */     
/*     */     private Object readResolve() {
/* 353 */       return INSTANCE;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\base\Equivalence.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */