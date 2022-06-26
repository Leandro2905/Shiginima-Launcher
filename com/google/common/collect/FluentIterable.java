/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.base.Joiner;
/*     */ import com.google.common.base.Optional;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Predicate;
/*     */ import java.util.Arrays;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.SortedSet;
/*     */ import javax.annotation.CheckReturnValue;
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
/*     */ @GwtCompatible(emulated = true)
/*     */ public abstract class FluentIterable<E>
/*     */   implements Iterable<E>
/*     */ {
/*     */   private final Iterable<E> iterable;
/*     */   
/*     */   protected FluentIterable() {
/*  80 */     this.iterable = this;
/*     */   }
/*     */   
/*     */   FluentIterable(Iterable<E> iterable) {
/*  84 */     this.iterable = (Iterable<E>)Preconditions.checkNotNull(iterable);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> FluentIterable<E> from(final Iterable<E> iterable) {
/*  92 */     return (iterable instanceof FluentIterable) ? (FluentIterable<E>)iterable : new FluentIterable<E>(iterable)
/*     */       {
/*     */         public Iterator<E> iterator()
/*     */         {
/*  96 */           return iterable.iterator();
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
/*     */   
/*     */   @Deprecated
/*     */   public static <E> FluentIterable<E> from(FluentIterable<E> iterable) {
/* 111 */     return (FluentIterable<E>)Preconditions.checkNotNull(iterable);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Beta
/*     */   public static <E> FluentIterable<E> of(E[] elements) {
/* 121 */     return from(Lists.newArrayList(elements));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 130 */     return Iterables.toString(this.iterable);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final int size() {
/* 137 */     return Iterables.size(this.iterable);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean contains(@Nullable Object element) {
/* 145 */     return Iterables.contains(this.iterable, element);
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
/*     */   @CheckReturnValue
/*     */   public final FluentIterable<E> cycle() {
/* 163 */     return from(Iterables.cycle(this.iterable));
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
/*     */   @CheckReturnValue
/*     */   @Beta
/*     */   public final FluentIterable<E> append(Iterable<? extends E> other) {
/* 178 */     return from(Iterables.concat(this.iterable, other));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CheckReturnValue
/*     */   @Beta
/*     */   public final FluentIterable<E> append(E... elements) {
/* 190 */     return from(Iterables.concat(this.iterable, Arrays.asList(elements)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CheckReturnValue
/*     */   public final FluentIterable<E> filter(Predicate<? super E> predicate) {
/* 199 */     return from(Iterables.filter(this.iterable, predicate));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CheckReturnValue
/*     */   @GwtIncompatible("Class.isInstance")
/*     */   public final <T> FluentIterable<T> filter(Class<T> type) {
/* 210 */     return from(Iterables.filter(this.iterable, type));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean anyMatch(Predicate<? super E> predicate) {
/* 217 */     return Iterables.any(this.iterable, predicate);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean allMatch(Predicate<? super E> predicate) {
/* 225 */     return Iterables.all(this.iterable, predicate);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Optional<E> firstMatch(Predicate<? super E> predicate) {
/* 236 */     return Iterables.tryFind(this.iterable, predicate);
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
/*     */   public final <T> FluentIterable<T> transform(Function<? super E, T> function) {
/* 248 */     return from(Iterables.transform(this.iterable, function));
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
/*     */   public <T> FluentIterable<T> transformAndConcat(Function<? super E, ? extends Iterable<? extends T>> function) {
/* 264 */     return from(Iterables.concat(transform(function)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Optional<E> first() {
/* 275 */     Iterator<E> iterator = this.iterable.iterator();
/* 276 */     return iterator.hasNext() ? Optional.of(iterator.next()) : Optional.absent();
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
/*     */   public final Optional<E> last() {
/* 292 */     if (this.iterable instanceof List) {
/* 293 */       List<E> list = (List<E>)this.iterable;
/* 294 */       if (list.isEmpty()) {
/* 295 */         return Optional.absent();
/*     */       }
/* 297 */       return Optional.of(list.get(list.size() - 1));
/*     */     } 
/* 299 */     Iterator<E> iterator = this.iterable.iterator();
/* 300 */     if (!iterator.hasNext()) {
/* 301 */       return Optional.absent();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 309 */     if (this.iterable instanceof SortedSet) {
/* 310 */       SortedSet<E> sortedSet = (SortedSet<E>)this.iterable;
/* 311 */       return Optional.of(sortedSet.last());
/*     */     } 
/*     */     
/*     */     while (true) {
/* 315 */       E current = iterator.next();
/* 316 */       if (!iterator.hasNext()) {
/* 317 */         return Optional.of(current);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CheckReturnValue
/*     */   public final FluentIterable<E> skip(int numberToSkip) {
/* 341 */     return from(Iterables.skip(this.iterable, numberToSkip));
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
/*     */   @CheckReturnValue
/*     */   public final FluentIterable<E> limit(int size) {
/* 356 */     return from(Iterables.limit(this.iterable, size));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean isEmpty() {
/* 363 */     return !this.iterable.iterator().hasNext();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final ImmutableList<E> toList() {
/* 373 */     return ImmutableList.copyOf(this.iterable);
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
/*     */   public final ImmutableList<E> toSortedList(Comparator<? super E> comparator) {
/* 386 */     return Ordering.<E>from(comparator).immutableSortedCopy(this.iterable);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final ImmutableSet<E> toSet() {
/* 396 */     return ImmutableSet.copyOf(this.iterable);
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
/*     */   public final ImmutableSortedSet<E> toSortedSet(Comparator<? super E> comparator) {
/* 410 */     return ImmutableSortedSet.copyOf(comparator, this.iterable);
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
/*     */   public final <V> ImmutableMap<E, V> toMap(Function<? super E, V> valueFunction) {
/* 424 */     return Maps.toMap(this.iterable, valueFunction);
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
/*     */   public final <K> ImmutableListMultimap<K, E> index(Function<? super E, K> keyFunction) {
/* 446 */     return Multimaps.index(this.iterable, keyFunction);
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
/*     */   public final <K> ImmutableMap<K, E> uniqueIndex(Function<? super E, K> keyFunction) {
/* 462 */     return Maps.uniqueIndex(this.iterable, keyFunction);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible("Array.newArray(Class, int)")
/*     */   public final E[] toArray(Class<E> type) {
/* 474 */     return Iterables.toArray(this.iterable, type);
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
/*     */   public final <C extends java.util.Collection<? super E>> C copyInto(C collection) {
/* 486 */     Preconditions.checkNotNull(collection);
/* 487 */     if (this.iterable instanceof java.util.Collection) {
/* 488 */       collection.addAll(Collections2.cast(this.iterable));
/*     */     } else {
/* 490 */       for (E item : this.iterable) {
/* 491 */         collection.add(item);
/*     */       }
/*     */     } 
/* 494 */     return collection;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Beta
/*     */   public final String join(Joiner joiner) {
/* 505 */     return joiner.join(this);
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
/*     */   public final E get(int position) {
/* 517 */     return Iterables.get(this.iterable, position);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class FromIterableFunction<E>
/*     */     implements Function<Iterable<E>, FluentIterable<E>>
/*     */   {
/*     */     public FluentIterable<E> apply(Iterable<E> fromObject) {
/* 527 */       return FluentIterable.from(fromObject);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\collect\FluentIterable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */