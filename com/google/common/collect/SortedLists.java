/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
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
/*     */ @GwtCompatible
/*     */ @Beta
/*     */ final class SortedLists
/*     */ {
/*     */   public enum KeyPresentBehavior
/*     */   {
/*  53 */     ANY_PRESENT
/*     */     {
/*     */       <E> int resultIndex(Comparator<? super E> comparator, E key, List<? extends E> list, int foundIndex)
/*     */       {
/*  57 */         return foundIndex;
/*     */       }
/*     */     },
/*     */ 
/*     */ 
/*     */     
/*  63 */     LAST_PRESENT
/*     */     {
/*     */ 
/*     */       
/*     */       <E> int resultIndex(Comparator<? super E> comparator, E key, List<? extends E> list, int foundIndex)
/*     */       {
/*  69 */         int lower = foundIndex;
/*  70 */         int upper = list.size() - 1;
/*     */         
/*  72 */         while (lower < upper) {
/*  73 */           int middle = lower + upper + 1 >>> 1;
/*  74 */           int c = comparator.compare(list.get(middle), key);
/*  75 */           if (c > 0) {
/*  76 */             upper = middle - 1; continue;
/*     */           } 
/*  78 */           lower = middle;
/*     */         } 
/*     */         
/*  81 */         return lower;
/*     */       }
/*     */     },
/*     */ 
/*     */ 
/*     */     
/*  87 */     FIRST_PRESENT
/*     */     {
/*     */ 
/*     */       
/*     */       <E> int resultIndex(Comparator<? super E> comparator, E key, List<? extends E> list, int foundIndex)
/*     */       {
/*  93 */         int lower = 0;
/*  94 */         int upper = foundIndex;
/*     */ 
/*     */         
/*  97 */         while (lower < upper) {
/*  98 */           int middle = lower + upper >>> 1;
/*  99 */           int c = comparator.compare(list.get(middle), key);
/* 100 */           if (c < 0) {
/* 101 */             lower = middle + 1; continue;
/*     */           } 
/* 103 */           upper = middle;
/*     */         } 
/*     */         
/* 106 */         return lower;
/*     */       }
/*     */     },
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 113 */     FIRST_AFTER
/*     */     {
/*     */       public <E> int resultIndex(Comparator<? super E> comparator, E key, List<? extends E> list, int foundIndex)
/*     */       {
/* 117 */         return LAST_PRESENT.<E>resultIndex(comparator, key, list, foundIndex) + 1;
/*     */       }
/*     */     },
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 124 */     LAST_BEFORE
/*     */     {
/*     */       public <E> int resultIndex(Comparator<? super E> comparator, E key, List<? extends E> list, int foundIndex)
/*     */       {
/* 128 */         return FIRST_PRESENT.<E>resultIndex(comparator, key, list, foundIndex) - 1;
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     abstract <E> int resultIndex(Comparator<? super E> param1Comparator, E param1E, List<? extends E> param1List, int param1Int);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public enum KeyAbsentBehavior
/*     */   {
/* 144 */     NEXT_LOWER
/*     */     {
/*     */       int resultIndex(int higherIndex) {
/* 147 */         return higherIndex - 1;
/*     */       }
/*     */     },
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 154 */     NEXT_HIGHER
/*     */     {
/*     */       public int resultIndex(int higherIndex) {
/* 157 */         return higherIndex;
/*     */       }
/*     */     },
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 172 */     INVERTED_INSERTION_INDEX
/*     */     {
/*     */       public int resultIndex(int higherIndex) {
/* 175 */         return higherIndex ^ 0xFFFFFFFF;
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     abstract int resultIndex(int param1Int);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E extends Comparable> int binarySearch(List<? extends E> list, E e, KeyPresentBehavior presentBehavior, KeyAbsentBehavior absentBehavior) {
/* 191 */     Preconditions.checkNotNull(e);
/* 192 */     return binarySearch(list, (E)Preconditions.checkNotNull(e), Ordering.natural(), presentBehavior, absentBehavior);
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
/*     */   public static <E, K extends Comparable> int binarySearch(List<E> list, Function<? super E, K> keyFunction, @Nullable K key, KeyPresentBehavior presentBehavior, KeyAbsentBehavior absentBehavior) {
/* 205 */     return binarySearch(list, keyFunction, key, Ordering.natural(), presentBehavior, absentBehavior);
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
/*     */   public static <E, K> int binarySearch(List<E> list, Function<? super E, K> keyFunction, @Nullable K key, Comparator<? super K> keyComparator, KeyPresentBehavior presentBehavior, KeyAbsentBehavior absentBehavior) {
/* 228 */     return binarySearch(Lists.transform(list, keyFunction), key, keyComparator, presentBehavior, absentBehavior);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> int binarySearch(List<? extends E> list, @Nullable E key, Comparator<? super E> comparator, KeyPresentBehavior presentBehavior, KeyAbsentBehavior absentBehavior) {
/* 258 */     Preconditions.checkNotNull(comparator);
/* 259 */     Preconditions.checkNotNull(list);
/* 260 */     Preconditions.checkNotNull(presentBehavior);
/* 261 */     Preconditions.checkNotNull(absentBehavior);
/* 262 */     if (!(list instanceof java.util.RandomAccess)) {
/* 263 */       list = Lists.newArrayList(list);
/*     */     }
/*     */ 
/*     */     
/* 267 */     int lower = 0;
/* 268 */     int upper = list.size() - 1;
/*     */     
/* 270 */     while (lower <= upper) {
/* 271 */       int middle = lower + upper >>> 1;
/* 272 */       int c = comparator.compare(key, list.get(middle));
/* 273 */       if (c < 0) {
/* 274 */         upper = middle - 1; continue;
/* 275 */       }  if (c > 0) {
/* 276 */         lower = middle + 1; continue;
/*     */       } 
/* 278 */       return lower + presentBehavior.<E>resultIndex(comparator, key, list.subList(lower, upper + 1), middle - lower);
/*     */     } 
/*     */ 
/*     */     
/* 282 */     return absentBehavior.resultIndex(lower);
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\collect\SortedLists.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */