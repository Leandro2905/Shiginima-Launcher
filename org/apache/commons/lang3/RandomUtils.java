/*     */ package org.apache.commons.lang3;
/*     */ 
/*     */ import java.util.Random;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RandomUtils
/*     */ {
/*  34 */   private static final Random RANDOM = new Random();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] nextBytes(int count) {
/*  63 */     Validate.isTrue((count >= 0), "Count cannot be negative.", new Object[0]);
/*     */     
/*  65 */     byte[] result = new byte[count];
/*  66 */     RANDOM.nextBytes(result);
/*  67 */     return result;
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
/*     */   public static int nextInt(int startInclusive, int endExclusive) {
/*  85 */     Validate.isTrue((endExclusive >= startInclusive), "Start value must be smaller or equal to end value.", new Object[0]);
/*     */     
/*  87 */     Validate.isTrue((startInclusive >= 0), "Both range values must be non-negative.", new Object[0]);
/*     */     
/*  89 */     if (startInclusive == endExclusive) {
/*  90 */       return startInclusive;
/*     */     }
/*     */     
/*  93 */     return startInclusive + RANDOM.nextInt(endExclusive - startInclusive);
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
/*     */   public static long nextLong(long startInclusive, long endExclusive) {
/* 111 */     Validate.isTrue((endExclusive >= startInclusive), "Start value must be smaller or equal to end value.", new Object[0]);
/*     */     
/* 113 */     Validate.isTrue((startInclusive >= 0L), "Both range values must be non-negative.", new Object[0]);
/*     */     
/* 115 */     if (startInclusive == endExclusive) {
/* 116 */       return startInclusive;
/*     */     }
/*     */     
/* 119 */     return (long)nextDouble(startInclusive, endExclusive);
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
/*     */   public static double nextDouble(double startInclusive, double endInclusive) {
/* 138 */     Validate.isTrue((endInclusive >= startInclusive), "Start value must be smaller or equal to end value.", new Object[0]);
/*     */     
/* 140 */     Validate.isTrue((startInclusive >= 0.0D), "Both range values must be non-negative.", new Object[0]);
/*     */     
/* 142 */     if (startInclusive == endInclusive) {
/* 143 */       return startInclusive;
/*     */     }
/*     */     
/* 146 */     return startInclusive + (endInclusive - startInclusive) * RANDOM.nextDouble();
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
/*     */   public static float nextFloat(float startInclusive, float endInclusive) {
/* 164 */     Validate.isTrue((endInclusive >= startInclusive), "Start value must be smaller or equal to end value.", new Object[0]);
/*     */     
/* 166 */     Validate.isTrue((startInclusive >= 0.0F), "Both range values must be non-negative.", new Object[0]);
/*     */     
/* 168 */     if (startInclusive == endInclusive) {
/* 169 */       return startInclusive;
/*     */     }
/*     */     
/* 172 */     return startInclusive + (endInclusive - startInclusive) * RANDOM.nextFloat();
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\commons\lang3\RandomUtils.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */