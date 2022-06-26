/*     */ package com.google.common.base;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import java.io.Serializable;
/*     */ import java.util.Iterator;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Beta
/*     */ @GwtCompatible
/*     */ public abstract class Converter<A, B>
/*     */   implements Function<A, B>
/*     */ {
/*     */   private final boolean handleNullAutomatically;
/*     */   private transient Converter<B, A> reverse;
/*     */   
/*     */   protected Converter() {
/* 103 */     this(true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Converter(boolean handleNullAutomatically) {
/* 110 */     this.handleNullAutomatically = handleNullAutomatically;
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
/*     */   protected abstract B doForward(A paramA);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract A doBackward(B paramB);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public final B convert(@Nullable A a) {
/* 147 */     return correctedDoForward(a);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   B correctedDoForward(@Nullable A a) {
/* 152 */     if (this.handleNullAutomatically)
/*     */     {
/* 154 */       return (a == null) ? null : Preconditions.<B>checkNotNull(doForward(a));
/*     */     }
/* 156 */     return doForward(a);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   A correctedDoBackward(@Nullable B b) {
/* 162 */     if (this.handleNullAutomatically)
/*     */     {
/* 164 */       return (b == null) ? null : Preconditions.<A>checkNotNull(doBackward(b));
/*     */     }
/* 166 */     return doBackward(b);
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
/*     */   public Iterable<B> convertAll(final Iterable<? extends A> fromIterable) {
/* 179 */     Preconditions.checkNotNull(fromIterable, "fromIterable");
/* 180 */     return new Iterable<B>() {
/*     */         public Iterator<B> iterator() {
/* 182 */           return new Iterator<B>() {
/* 183 */               private final Iterator<? extends A> fromIterator = fromIterable.iterator();
/*     */ 
/*     */               
/*     */               public boolean hasNext() {
/* 187 */                 return this.fromIterator.hasNext();
/*     */               }
/*     */ 
/*     */               
/*     */               public B next() {
/* 192 */                 return (B)Converter.this.convert(this.fromIterator.next());
/*     */               }
/*     */ 
/*     */               
/*     */               public void remove() {
/* 197 */                 this.fromIterator.remove();
/*     */               }
/*     */             };
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Converter<B, A> reverse() {
/* 212 */     Converter<B, A> result = this.reverse;
/* 213 */     return (result == null) ? (this.reverse = new ReverseConverter<A, B>(this)) : result;
/*     */   }
/*     */   
/*     */   private static final class ReverseConverter<A, B> extends Converter<B, A> implements Serializable {
/*     */     final Converter<A, B> original;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     ReverseConverter(Converter<A, B> original) {
/* 221 */       this.original = original;
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
/*     */     protected A doForward(B b) {
/* 233 */       throw new AssertionError();
/*     */     }
/*     */ 
/*     */     
/*     */     protected B doBackward(A a) {
/* 238 */       throw new AssertionError();
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     A correctedDoForward(@Nullable B b) {
/* 244 */       return this.original.correctedDoBackward(b);
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     B correctedDoBackward(@Nullable A a) {
/* 250 */       return this.original.correctedDoForward(a);
/*     */     }
/*     */ 
/*     */     
/*     */     public Converter<A, B> reverse() {
/* 255 */       return this.original;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(@Nullable Object object) {
/* 260 */       if (object instanceof ReverseConverter) {
/* 261 */         ReverseConverter<?, ?> that = (ReverseConverter<?, ?>)object;
/* 262 */         return this.original.equals(that.original);
/*     */       } 
/* 264 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 269 */       return this.original.hashCode() ^ 0xFFFFFFFF;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 274 */       String str = String.valueOf(String.valueOf(this.original)); return (new StringBuilder(10 + str.length())).append(str).append(".reverse()").toString();
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
/*     */   public final <C> Converter<A, C> andThen(Converter<B, C> secondConverter) {
/* 288 */     return doAndThen(secondConverter);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   <C> Converter<A, C> doAndThen(Converter<B, C> secondConverter) {
/* 295 */     return new ConverterComposition<A, B, C>(this, Preconditions.<Converter<B, C>>checkNotNull(secondConverter));
/*     */   }
/*     */   
/*     */   private static final class ConverterComposition<A, B, C> extends Converter<A, C> implements Serializable {
/*     */     final Converter<A, B> first;
/*     */     final Converter<B, C> second;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     ConverterComposition(Converter<A, B> first, Converter<B, C> second) {
/* 304 */       this.first = first;
/* 305 */       this.second = second;
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
/*     */     protected C doForward(A a) {
/* 317 */       throw new AssertionError();
/*     */     }
/*     */ 
/*     */     
/*     */     protected A doBackward(C c) {
/* 322 */       throw new AssertionError();
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     C correctedDoForward(@Nullable A a) {
/* 328 */       return this.second.correctedDoForward(this.first.correctedDoForward(a));
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     A correctedDoBackward(@Nullable C c) {
/* 334 */       return this.first.correctedDoBackward(this.second.correctedDoBackward(c));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(@Nullable Object object) {
/* 339 */       if (object instanceof ConverterComposition) {
/* 340 */         ConverterComposition<?, ?, ?> that = (ConverterComposition<?, ?, ?>)object;
/* 341 */         return (this.first.equals(that.first) && this.second.equals(that.second));
/*     */       } 
/*     */       
/* 344 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 349 */       return 31 * this.first.hashCode() + this.second.hashCode();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 354 */       String str1 = String.valueOf(String.valueOf(this.first)), str2 = String.valueOf(String.valueOf(this.second)); return (new StringBuilder(10 + str1.length() + str2.length())).append(str1).append(".andThen(").append(str2).append(")").toString();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   @Nullable
/*     */   public final B apply(@Nullable A a) {
/* 367 */     return convert(a);
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
/*     */   public boolean equals(@Nullable Object object) {
/* 383 */     return super.equals(object);
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
/*     */   public static <A, B> Converter<A, B> from(Function<? super A, ? extends B> forwardFunction, Function<? super B, ? extends A> backwardFunction) {
/* 405 */     return new FunctionBasedConverter<A, B>(forwardFunction, backwardFunction);
/*     */   }
/*     */   
/*     */   private static final class FunctionBasedConverter<A, B>
/*     */     extends Converter<A, B>
/*     */     implements Serializable
/*     */   {
/*     */     private final Function<? super A, ? extends B> forwardFunction;
/*     */     private final Function<? super B, ? extends A> backwardFunction;
/*     */     
/*     */     private FunctionBasedConverter(Function<? super A, ? extends B> forwardFunction, Function<? super B, ? extends A> backwardFunction) {
/* 416 */       this.forwardFunction = Preconditions.<Function<? super A, ? extends B>>checkNotNull(forwardFunction);
/* 417 */       this.backwardFunction = Preconditions.<Function<? super B, ? extends A>>checkNotNull(backwardFunction);
/*     */     }
/*     */ 
/*     */     
/*     */     protected B doForward(A a) {
/* 422 */       return this.forwardFunction.apply(a);
/*     */     }
/*     */ 
/*     */     
/*     */     protected A doBackward(B b) {
/* 427 */       return this.backwardFunction.apply(b);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(@Nullable Object object) {
/* 432 */       if (object instanceof FunctionBasedConverter) {
/* 433 */         FunctionBasedConverter<?, ?> that = (FunctionBasedConverter<?, ?>)object;
/* 434 */         return (this.forwardFunction.equals(that.forwardFunction) && this.backwardFunction.equals(that.backwardFunction));
/*     */       } 
/*     */       
/* 437 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 442 */       return this.forwardFunction.hashCode() * 31 + this.backwardFunction.hashCode();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 447 */       String str1 = String.valueOf(String.valueOf(this.forwardFunction)), str2 = String.valueOf(String.valueOf(this.backwardFunction)); return (new StringBuilder(18 + str1.length() + str2.length())).append("Converter.from(").append(str1).append(", ").append(str2).append(")").toString();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> Converter<T, T> identity() {
/* 456 */     return IdentityConverter.INSTANCE;
/*     */   }
/*     */ 
/*     */   
/*     */   private static final class IdentityConverter<T>
/*     */     extends Converter<T, T>
/*     */     implements Serializable
/*     */   {
/* 464 */     static final IdentityConverter INSTANCE = new IdentityConverter();
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     protected T doForward(T t) {
/* 468 */       return t;
/*     */     }
/*     */ 
/*     */     
/*     */     protected T doBackward(T t) {
/* 473 */       return t;
/*     */     }
/*     */ 
/*     */     
/*     */     public IdentityConverter<T> reverse() {
/* 478 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     <S> Converter<T, S> doAndThen(Converter<T, S> otherConverter) {
/* 483 */       return Preconditions.<Converter<T, S>>checkNotNull(otherConverter, "otherConverter");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String toString() {
/* 493 */       return "Converter.identity()";
/*     */     }
/*     */     
/*     */     private Object readResolve() {
/* 497 */       return INSTANCE;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\base\Converter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */