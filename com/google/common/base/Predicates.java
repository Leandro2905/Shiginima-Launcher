/*     */ package com.google.common.base;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.regex.Pattern;
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
/*     */ @GwtCompatible(emulated = true)
/*     */ public final class Predicates
/*     */ {
/*     */   @GwtCompatible(serializable = true)
/*     */   public static <T> Predicate<T> alwaysTrue() {
/*  59 */     return ObjectPredicate.ALWAYS_TRUE.withNarrowedType();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtCompatible(serializable = true)
/*     */   public static <T> Predicate<T> alwaysFalse() {
/*  67 */     return ObjectPredicate.ALWAYS_FALSE.withNarrowedType();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtCompatible(serializable = true)
/*     */   public static <T> Predicate<T> isNull() {
/*  76 */     return ObjectPredicate.IS_NULL.withNarrowedType();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtCompatible(serializable = true)
/*     */   public static <T> Predicate<T> notNull() {
/*  85 */     return ObjectPredicate.NOT_NULL.withNarrowedType();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> Predicate<T> not(Predicate<T> predicate) {
/*  93 */     return new NotPredicate<T>(predicate);
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
/*     */   public static <T> Predicate<T> and(Iterable<? extends Predicate<? super T>> components) {
/* 107 */     return new AndPredicate<T>(defensiveCopy(components));
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
/*     */   public static <T> Predicate<T> and(Predicate<? super T>... components) {
/* 120 */     return new AndPredicate<T>(defensiveCopy(components));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> Predicate<T> and(Predicate<? super T> first, Predicate<? super T> second) {
/* 131 */     return new AndPredicate<T>(asList(Preconditions.<Predicate>checkNotNull(first), Preconditions.<Predicate>checkNotNull(second)));
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
/*     */   public static <T> Predicate<T> or(Iterable<? extends Predicate<? super T>> components) {
/* 146 */     return new OrPredicate<T>(defensiveCopy(components));
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
/*     */   public static <T> Predicate<T> or(Predicate<? super T>... components) {
/* 159 */     return new OrPredicate<T>(defensiveCopy(components));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> Predicate<T> or(Predicate<? super T> first, Predicate<? super T> second) {
/* 170 */     return new OrPredicate<T>(asList(Preconditions.<Predicate>checkNotNull(first), Preconditions.<Predicate>checkNotNull(second)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> Predicate<T> equalTo(@Nullable T target) {
/* 179 */     return (target == null) ? isNull() : new IsEqualToPredicate<T>(target);
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
/*     */   @GwtIncompatible("Class.isInstance")
/*     */   public static Predicate<Object> instanceOf(Class<?> clazz) {
/* 201 */     return new InstanceOfPredicate(clazz);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible("Class.isAssignableFrom")
/*     */   @Beta
/*     */   public static Predicate<Class<?>> assignableFrom(Class<?> clazz) {
/* 214 */     return new AssignableFromPredicate(clazz);
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
/*     */   public static <T> Predicate<T> in(Collection<? extends T> target) {
/* 231 */     return new InPredicate<T>(target);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <A, B> Predicate<A> compose(Predicate<B> predicate, Function<A, ? extends B> function) {
/* 242 */     return new CompositionPredicate<A, Object>(predicate, function);
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
/*     */   @GwtIncompatible("java.util.regex.Pattern")
/*     */   public static Predicate<CharSequence> containsPattern(String pattern) {
/* 256 */     return new ContainsPatternFromStringPredicate(pattern);
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
/*     */   @GwtIncompatible("java.util.regex.Pattern")
/*     */   public static Predicate<CharSequence> contains(Pattern pattern) {
/* 269 */     return new ContainsPatternPredicate(pattern);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   enum ObjectPredicate
/*     */     implements Predicate<Object>
/*     */   {
/* 277 */     ALWAYS_TRUE {
/*     */       public boolean apply(@Nullable Object o) {
/* 279 */         return true;
/*     */       }
/*     */       public String toString() {
/* 282 */         return "Predicates.alwaysTrue()";
/*     */       }
/*     */     },
/*     */     
/* 286 */     ALWAYS_FALSE {
/*     */       public boolean apply(@Nullable Object o) {
/* 288 */         return false;
/*     */       }
/*     */       public String toString() {
/* 291 */         return "Predicates.alwaysFalse()";
/*     */       }
/*     */     },
/*     */     
/* 295 */     IS_NULL {
/*     */       public boolean apply(@Nullable Object o) {
/* 297 */         return (o == null);
/*     */       }
/*     */       public String toString() {
/* 300 */         return "Predicates.isNull()";
/*     */       }
/*     */     },
/*     */     
/* 304 */     NOT_NULL {
/*     */       public boolean apply(@Nullable Object o) {
/* 306 */         return (o != null);
/*     */       }
/*     */       public String toString() {
/* 309 */         return "Predicates.notNull()";
/*     */       }
/*     */     };
/*     */ 
/*     */     
/*     */     <T> Predicate<T> withNarrowedType() {
/* 315 */       return this;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class NotPredicate<T> implements Predicate<T>, Serializable {
/*     */     final Predicate<T> predicate;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     NotPredicate(Predicate<T> predicate) {
/* 324 */       this.predicate = Preconditions.<Predicate<T>>checkNotNull(predicate);
/*     */     }
/*     */     
/*     */     public boolean apply(@Nullable T t) {
/* 328 */       return !this.predicate.apply(t);
/*     */     }
/*     */     public int hashCode() {
/* 331 */       return this.predicate.hashCode() ^ 0xFFFFFFFF;
/*     */     }
/*     */     public boolean equals(@Nullable Object obj) {
/* 334 */       if (obj instanceof NotPredicate) {
/* 335 */         NotPredicate<?> that = (NotPredicate)obj;
/* 336 */         return this.predicate.equals(that.predicate);
/*     */       } 
/* 338 */       return false;
/*     */     }
/*     */     public String toString() {
/* 341 */       String str = String.valueOf(String.valueOf(this.predicate.toString())); return (new StringBuilder(16 + str.length())).append("Predicates.not(").append(str).append(")").toString();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/* 346 */   private static final Joiner COMMA_JOINER = Joiner.on(',');
/*     */   
/*     */   private static class AndPredicate<T> implements Predicate<T>, Serializable {
/*     */     private final List<? extends Predicate<? super T>> components;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     private AndPredicate(List<? extends Predicate<? super T>> components) {
/* 353 */       this.components = components;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean apply(@Nullable T t) {
/* 358 */       for (int i = 0; i < this.components.size(); i++) {
/* 359 */         if (!((Predicate<T>)this.components.get(i)).apply(t)) {
/* 360 */           return false;
/*     */         }
/*     */       } 
/* 363 */       return true;
/*     */     }
/*     */     
/*     */     public int hashCode() {
/* 367 */       return this.components.hashCode() + 306654252;
/*     */     }
/*     */     public boolean equals(@Nullable Object obj) {
/* 370 */       if (obj instanceof AndPredicate) {
/* 371 */         AndPredicate<?> that = (AndPredicate)obj;
/* 372 */         return this.components.equals(that.components);
/*     */       } 
/* 374 */       return false;
/*     */     }
/*     */     public String toString() {
/* 377 */       String str = String.valueOf(String.valueOf(Predicates.COMMA_JOINER.join(this.components))); return (new StringBuilder(16 + str.length())).append("Predicates.and(").append(str).append(")").toString();
/*     */     }
/*     */   }
/*     */   
/*     */   private static class OrPredicate<T>
/*     */     implements Predicate<T>, Serializable {
/*     */     private final List<? extends Predicate<? super T>> components;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     private OrPredicate(List<? extends Predicate<? super T>> components) {
/* 387 */       this.components = components;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean apply(@Nullable T t) {
/* 392 */       for (int i = 0; i < this.components.size(); i++) {
/* 393 */         if (((Predicate<T>)this.components.get(i)).apply(t)) {
/* 394 */           return true;
/*     */         }
/*     */       } 
/* 397 */       return false;
/*     */     }
/*     */     
/*     */     public int hashCode() {
/* 401 */       return this.components.hashCode() + 87855567;
/*     */     }
/*     */     public boolean equals(@Nullable Object obj) {
/* 404 */       if (obj instanceof OrPredicate) {
/* 405 */         OrPredicate<?> that = (OrPredicate)obj;
/* 406 */         return this.components.equals(that.components);
/*     */       } 
/* 408 */       return false;
/*     */     }
/*     */     public String toString() {
/* 411 */       String str = String.valueOf(String.valueOf(Predicates.COMMA_JOINER.join(this.components))); return (new StringBuilder(15 + str.length())).append("Predicates.or(").append(str).append(")").toString();
/*     */     }
/*     */   }
/*     */   
/*     */   private static class IsEqualToPredicate<T>
/*     */     implements Predicate<T>, Serializable
/*     */   {
/*     */     private final T target;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     private IsEqualToPredicate(T target) {
/* 422 */       this.target = target;
/*     */     }
/*     */     
/*     */     public boolean apply(T t) {
/* 426 */       return this.target.equals(t);
/*     */     }
/*     */     public int hashCode() {
/* 429 */       return this.target.hashCode();
/*     */     }
/*     */     public boolean equals(@Nullable Object obj) {
/* 432 */       if (obj instanceof IsEqualToPredicate) {
/* 433 */         IsEqualToPredicate<?> that = (IsEqualToPredicate)obj;
/* 434 */         return this.target.equals(that.target);
/*     */       } 
/* 436 */       return false;
/*     */     }
/*     */     public String toString() {
/* 439 */       String str = String.valueOf(String.valueOf(this.target)); return (new StringBuilder(20 + str.length())).append("Predicates.equalTo(").append(str).append(")").toString();
/*     */     }
/*     */   }
/*     */   
/*     */   @GwtIncompatible("Class.isInstance")
/*     */   private static class InstanceOfPredicate
/*     */     implements Predicate<Object>, Serializable
/*     */   {
/*     */     private final Class<?> clazz;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     private InstanceOfPredicate(Class<?> clazz) {
/* 451 */       this.clazz = Preconditions.<Class<?>>checkNotNull(clazz);
/*     */     }
/*     */     
/*     */     public boolean apply(@Nullable Object o) {
/* 455 */       return this.clazz.isInstance(o);
/*     */     }
/*     */     public int hashCode() {
/* 458 */       return this.clazz.hashCode();
/*     */     }
/*     */     public boolean equals(@Nullable Object obj) {
/* 461 */       if (obj instanceof InstanceOfPredicate) {
/* 462 */         InstanceOfPredicate that = (InstanceOfPredicate)obj;
/* 463 */         return (this.clazz == that.clazz);
/*     */       } 
/* 465 */       return false;
/*     */     }
/*     */     public String toString() {
/* 468 */       String str = String.valueOf(String.valueOf(this.clazz.getName())); return (new StringBuilder(23 + str.length())).append("Predicates.instanceOf(").append(str).append(")").toString();
/*     */     }
/*     */   }
/*     */   
/*     */   @GwtIncompatible("Class.isAssignableFrom")
/*     */   private static class AssignableFromPredicate
/*     */     implements Predicate<Class<?>>, Serializable
/*     */   {
/*     */     private final Class<?> clazz;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     private AssignableFromPredicate(Class<?> clazz) {
/* 480 */       this.clazz = Preconditions.<Class<?>>checkNotNull(clazz);
/*     */     }
/*     */     
/*     */     public boolean apply(Class<?> input) {
/* 484 */       return this.clazz.isAssignableFrom(input);
/*     */     }
/*     */     public int hashCode() {
/* 487 */       return this.clazz.hashCode();
/*     */     }
/*     */     public boolean equals(@Nullable Object obj) {
/* 490 */       if (obj instanceof AssignableFromPredicate) {
/* 491 */         AssignableFromPredicate that = (AssignableFromPredicate)obj;
/* 492 */         return (this.clazz == that.clazz);
/*     */       } 
/* 494 */       return false;
/*     */     }
/*     */     public String toString() {
/* 497 */       String str = String.valueOf(String.valueOf(this.clazz.getName())); return (new StringBuilder(27 + str.length())).append("Predicates.assignableFrom(").append(str).append(")").toString();
/*     */     }
/*     */   }
/*     */   
/*     */   private static class InPredicate<T>
/*     */     implements Predicate<T>, Serializable {
/*     */     private final Collection<?> target;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     private InPredicate(Collection<?> target) {
/* 507 */       this.target = Preconditions.<Collection>checkNotNull(target);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean apply(@Nullable T t) {
/*     */       try {
/* 513 */         return this.target.contains(t);
/* 514 */       } catch (NullPointerException e) {
/* 515 */         return false;
/* 516 */       } catch (ClassCastException e) {
/* 517 */         return false;
/*     */       } 
/*     */     }
/*     */     
/*     */     public boolean equals(@Nullable Object obj) {
/* 522 */       if (obj instanceof InPredicate) {
/* 523 */         InPredicate<?> that = (InPredicate)obj;
/* 524 */         return this.target.equals(that.target);
/*     */       } 
/* 526 */       return false;
/*     */     }
/*     */     
/*     */     public int hashCode() {
/* 530 */       return this.target.hashCode();
/*     */     }
/*     */     
/*     */     public String toString() {
/* 534 */       String str = String.valueOf(String.valueOf(this.target)); return (new StringBuilder(15 + str.length())).append("Predicates.in(").append(str).append(")").toString();
/*     */     }
/*     */   }
/*     */   
/*     */   private static class CompositionPredicate<A, B>
/*     */     implements Predicate<A>, Serializable
/*     */   {
/*     */     final Predicate<B> p;
/*     */     final Function<A, ? extends B> f;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     private CompositionPredicate(Predicate<B> p, Function<A, ? extends B> f) {
/* 546 */       this.p = Preconditions.<Predicate<B>>checkNotNull(p);
/* 547 */       this.f = Preconditions.<Function<A, ? extends B>>checkNotNull(f);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean apply(@Nullable A a) {
/* 552 */       return this.p.apply(this.f.apply(a));
/*     */     }
/*     */     
/*     */     public boolean equals(@Nullable Object obj) {
/* 556 */       if (obj instanceof CompositionPredicate) {
/* 557 */         CompositionPredicate<?, ?> that = (CompositionPredicate<?, ?>)obj;
/* 558 */         return (this.f.equals(that.f) && this.p.equals(that.p));
/*     */       } 
/* 560 */       return false;
/*     */     }
/*     */     
/*     */     public int hashCode() {
/* 564 */       return this.f.hashCode() ^ this.p.hashCode();
/*     */     }
/*     */     
/*     */     public String toString() {
/* 568 */       String str1 = String.valueOf(String.valueOf(this.p.toString())), str2 = String.valueOf(String.valueOf(this.f.toString())); return (new StringBuilder(2 + str1.length() + str2.length())).append(str1).append("(").append(str2).append(")").toString();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   @GwtIncompatible("Only used by other GWT-incompatible code.")
/*     */   private static class ContainsPatternPredicate
/*     */     implements Predicate<CharSequence>, Serializable
/*     */   {
/*     */     final Pattern pattern;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     ContainsPatternPredicate(Pattern pattern) {
/* 581 */       this.pattern = Preconditions.<Pattern>checkNotNull(pattern);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean apply(CharSequence t) {
/* 586 */       return this.pattern.matcher(t).find();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 593 */       return Objects.hashCode(new Object[] { this.pattern.pattern(), Integer.valueOf(this.pattern.flags()) });
/*     */     }
/*     */     
/*     */     public boolean equals(@Nullable Object obj) {
/* 597 */       if (obj instanceof ContainsPatternPredicate) {
/* 598 */         ContainsPatternPredicate that = (ContainsPatternPredicate)obj;
/*     */ 
/*     */ 
/*     */         
/* 602 */         return (Objects.equal(this.pattern.pattern(), that.pattern.pattern()) && Objects.equal(Integer.valueOf(this.pattern.flags()), Integer.valueOf(that.pattern.flags())));
/*     */       } 
/*     */       
/* 605 */       return false;
/*     */     }
/*     */     
/*     */     public String toString() {
/* 609 */       String patternString = Objects.toStringHelper(this.pattern).add("pattern", this.pattern.pattern()).add("pattern.flags", this.pattern.flags()).toString();
/*     */ 
/*     */ 
/*     */       
/* 613 */       String str1 = String.valueOf(String.valueOf(patternString)); return (new StringBuilder(21 + str1.length())).append("Predicates.contains(").append(str1).append(")").toString();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   @GwtIncompatible("Only used by other GWT-incompatible code.")
/*     */   private static class ContainsPatternFromStringPredicate
/*     */     extends ContainsPatternPredicate
/*     */   {
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     ContainsPatternFromStringPredicate(String string) {
/* 625 */       super(Pattern.compile(string));
/*     */     }
/*     */     
/*     */     public String toString() {
/* 629 */       String str = String.valueOf(String.valueOf(this.pattern.pattern())); return (new StringBuilder(28 + str.length())).append("Predicates.containsPattern(").append(str).append(")").toString();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static <T> List<Predicate<? super T>> asList(Predicate<? super T> first, Predicate<? super T> second) {
/* 638 */     return Arrays.asList((Predicate<? super T>[])new Predicate[] { first, second });
/*     */   }
/*     */   
/*     */   private static <T> List<T> defensiveCopy(T... array) {
/* 642 */     return defensiveCopy(Arrays.asList(array));
/*     */   }
/*     */   
/*     */   static <T> List<T> defensiveCopy(Iterable<T> iterable) {
/* 646 */     ArrayList<T> list = new ArrayList<T>();
/* 647 */     for (T element : iterable) {
/* 648 */       list.add(Preconditions.checkNotNull(element));
/*     */     }
/* 650 */     return list;
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\base\Predicates.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */