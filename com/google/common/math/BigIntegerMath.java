/*     */ package com.google.common.math;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.math.BigDecimal;
/*     */ import java.math.BigInteger;
/*     */ import java.math.RoundingMode;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public final class BigIntegerMath
/*     */ {
/*     */   @VisibleForTesting
/*     */   static final int SQRT2_PRECOMPUTE_THRESHOLD = 256;
/*     */   
/*     */   public static boolean isPowerOfTwo(BigInteger x) {
/*  56 */     Preconditions.checkNotNull(x);
/*  57 */     return (x.signum() > 0 && x.getLowestSetBit() == x.bitLength() - 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int log2(BigInteger x, RoundingMode mode) {
/*     */     BigInteger x2;
/*     */     int logX2Floor;
/*  70 */     MathPreconditions.checkPositive("x", (BigInteger)Preconditions.checkNotNull(x));
/*  71 */     int logFloor = x.bitLength() - 1;
/*  72 */     switch (mode) {
/*     */       case UNNECESSARY:
/*  74 */         MathPreconditions.checkRoundingUnnecessary(isPowerOfTwo(x));
/*     */       case DOWN:
/*     */       case FLOOR:
/*  77 */         return logFloor;
/*     */       
/*     */       case UP:
/*     */       case CEILING:
/*  81 */         return isPowerOfTwo(x) ? logFloor : (logFloor + 1);
/*     */       
/*     */       case HALF_DOWN:
/*     */       case HALF_UP:
/*     */       case HALF_EVEN:
/*  86 */         if (logFloor < 256) {
/*  87 */           BigInteger halfPower = SQRT2_PRECOMPUTED_BITS.shiftRight(256 - logFloor);
/*     */           
/*  89 */           if (x.compareTo(halfPower) <= 0) {
/*  90 */             return logFloor;
/*     */           }
/*  92 */           return logFloor + 1;
/*     */         } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 101 */         x2 = x.pow(2);
/* 102 */         logX2Floor = x2.bitLength() - 1;
/* 103 */         return (logX2Floor < 2 * logFloor + 1) ? logFloor : (logFloor + 1);
/*     */     } 
/*     */     
/* 106 */     throw new AssertionError();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/* 117 */   static final BigInteger SQRT2_PRECOMPUTED_BITS = new BigInteger("16a09e667f3bcc908b2fb1366ea957d3e3adec17512775099da2f590b0667322a", 16);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible("TODO")
/*     */   public static int log10(BigInteger x, RoundingMode mode) {
/*     */     BigInteger x2, halfPowerSquared;
/* 130 */     MathPreconditions.checkPositive("x", x);
/* 131 */     if (fitsInLong(x)) {
/* 132 */       return LongMath.log10(x.longValue(), mode);
/*     */     }
/*     */     
/* 135 */     int approxLog10 = (int)(log2(x, RoundingMode.FLOOR) * LN_2 / LN_10);
/* 136 */     BigInteger approxPow = BigInteger.TEN.pow(approxLog10);
/* 137 */     int approxCmp = approxPow.compareTo(x);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 144 */     if (approxCmp > 0) {
/*     */ 
/*     */       
/*     */       do {
/*     */ 
/*     */ 
/*     */         
/* 151 */         approxLog10--;
/* 152 */         approxPow = approxPow.divide(BigInteger.TEN);
/* 153 */         approxCmp = approxPow.compareTo(x);
/* 154 */       } while (approxCmp > 0);
/*     */     } else {
/* 156 */       BigInteger nextPow = BigInteger.TEN.multiply(approxPow);
/* 157 */       int nextCmp = nextPow.compareTo(x);
/* 158 */       while (nextCmp <= 0) {
/* 159 */         approxLog10++;
/* 160 */         approxPow = nextPow;
/* 161 */         approxCmp = nextCmp;
/* 162 */         nextPow = BigInteger.TEN.multiply(approxPow);
/* 163 */         nextCmp = nextPow.compareTo(x);
/*     */       } 
/*     */     } 
/*     */     
/* 167 */     int floorLog = approxLog10;
/* 168 */     BigInteger floorPow = approxPow;
/* 169 */     int floorCmp = approxCmp;
/*     */     
/* 171 */     switch (mode) {
/*     */       case UNNECESSARY:
/* 173 */         MathPreconditions.checkRoundingUnnecessary((floorCmp == 0));
/*     */       
/*     */       case DOWN:
/*     */       case FLOOR:
/* 177 */         return floorLog;
/*     */       
/*     */       case UP:
/*     */       case CEILING:
/* 181 */         return floorPow.equals(x) ? floorLog : (floorLog + 1);
/*     */ 
/*     */       
/*     */       case HALF_DOWN:
/*     */       case HALF_UP:
/*     */       case HALF_EVEN:
/* 187 */         x2 = x.pow(2);
/* 188 */         halfPowerSquared = floorPow.pow(2).multiply(BigInteger.TEN);
/* 189 */         return (x2.compareTo(halfPowerSquared) <= 0) ? floorLog : (floorLog + 1);
/*     */     } 
/* 191 */     throw new AssertionError();
/*     */   }
/*     */ 
/*     */   
/* 195 */   private static final double LN_10 = Math.log(10.0D);
/* 196 */   private static final double LN_2 = Math.log(2.0D);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible("TODO")
/*     */   public static BigInteger sqrt(BigInteger x, RoundingMode mode) {
/*     */     int sqrtFloorInt;
/*     */     boolean sqrtFloorIsExact;
/*     */     BigInteger halfSquare;
/* 208 */     MathPreconditions.checkNonNegative("x", x);
/* 209 */     if (fitsInLong(x)) {
/* 210 */       return BigInteger.valueOf(LongMath.sqrt(x.longValue(), mode));
/*     */     }
/* 212 */     BigInteger sqrtFloor = sqrtFloor(x);
/* 213 */     switch (mode) {
/*     */       case UNNECESSARY:
/* 215 */         MathPreconditions.checkRoundingUnnecessary(sqrtFloor.pow(2).equals(x));
/*     */       case DOWN:
/*     */       case FLOOR:
/* 218 */         return sqrtFloor;
/*     */       case UP:
/*     */       case CEILING:
/* 221 */         sqrtFloorInt = sqrtFloor.intValue();
/* 222 */         sqrtFloorIsExact = (sqrtFloorInt * sqrtFloorInt == x.intValue() && sqrtFloor.pow(2).equals(x));
/*     */ 
/*     */         
/* 225 */         return sqrtFloorIsExact ? sqrtFloor : sqrtFloor.add(BigInteger.ONE);
/*     */       case HALF_DOWN:
/*     */       case HALF_UP:
/*     */       case HALF_EVEN:
/* 229 */         halfSquare = sqrtFloor.pow(2).add(sqrtFloor);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 235 */         return (halfSquare.compareTo(x) >= 0) ? sqrtFloor : sqrtFloor.add(BigInteger.ONE);
/*     */     } 
/* 237 */     throw new AssertionError();
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
/*     */   @GwtIncompatible("TODO")
/*     */   private static BigInteger sqrtFloor(BigInteger x) {
/*     */     BigInteger sqrt0;
/* 263 */     int log2 = log2(x, RoundingMode.FLOOR);
/* 264 */     if (log2 < 1023) {
/* 265 */       sqrt0 = sqrtApproxWithDoubles(x);
/*     */     } else {
/* 267 */       int shift = log2 - 52 & 0xFFFFFFFE;
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 272 */       sqrt0 = sqrtApproxWithDoubles(x.shiftRight(shift)).shiftLeft(shift >> 1);
/*     */     } 
/* 274 */     BigInteger sqrt1 = sqrt0.add(x.divide(sqrt0)).shiftRight(1);
/* 275 */     if (sqrt0.equals(sqrt1)) {
/* 276 */       return sqrt0;
/*     */     }
/*     */     while (true) {
/* 279 */       sqrt0 = sqrt1;
/* 280 */       sqrt1 = sqrt0.add(x.divide(sqrt0)).shiftRight(1);
/* 281 */       if (sqrt1.compareTo(sqrt0) >= 0)
/* 282 */         return sqrt0; 
/*     */     } 
/*     */   }
/*     */   @GwtIncompatible("TODO")
/*     */   private static BigInteger sqrtApproxWithDoubles(BigInteger x) {
/* 287 */     return DoubleMath.roundToBigInteger(Math.sqrt(DoubleUtils.bigToDouble(x)), RoundingMode.HALF_EVEN);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible("TODO")
/*     */   public static BigInteger divide(BigInteger p, BigInteger q, RoundingMode mode) {
/* 299 */     BigDecimal pDec = new BigDecimal(p);
/* 300 */     BigDecimal qDec = new BigDecimal(q);
/* 301 */     return pDec.divide(qDec, 0, mode).toBigIntegerExact();
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
/*     */   public static BigInteger factorial(int n) {
/* 317 */     MathPreconditions.checkNonNegative("n", n);
/*     */ 
/*     */     
/* 320 */     if (n < LongMath.factorials.length) {
/* 321 */       return BigInteger.valueOf(LongMath.factorials[n]);
/*     */     }
/*     */ 
/*     */     
/* 325 */     int approxSize = IntMath.divide(n * IntMath.log2(n, RoundingMode.CEILING), 64, RoundingMode.CEILING);
/* 326 */     ArrayList<BigInteger> bignums = new ArrayList<BigInteger>(approxSize);
/*     */ 
/*     */     
/* 329 */     int startingNumber = LongMath.factorials.length;
/* 330 */     long product = LongMath.factorials[startingNumber - 1];
/*     */     
/* 332 */     int shift = Long.numberOfTrailingZeros(product);
/* 333 */     product >>= shift;
/*     */ 
/*     */     
/* 336 */     int productBits = LongMath.log2(product, RoundingMode.FLOOR) + 1;
/* 337 */     int bits = LongMath.log2(startingNumber, RoundingMode.FLOOR) + 1;
/*     */     
/* 339 */     int nextPowerOfTwo = 1 << bits - 1;
/*     */     
/*     */     long num;
/* 342 */     for (num = startingNumber; num <= n; num++) {
/*     */       
/* 344 */       if ((num & nextPowerOfTwo) != 0L) {
/* 345 */         nextPowerOfTwo <<= 1;
/* 346 */         bits++;
/*     */       } 
/*     */       
/* 349 */       int tz = Long.numberOfTrailingZeros(num);
/* 350 */       long normalizedNum = num >> tz;
/* 351 */       shift += tz;
/*     */       
/* 353 */       int normalizedBits = bits - tz;
/*     */       
/* 355 */       if (normalizedBits + productBits >= 64) {
/* 356 */         bignums.add(BigInteger.valueOf(product));
/* 357 */         product = 1L;
/* 358 */         productBits = 0;
/*     */       } 
/* 360 */       product *= normalizedNum;
/* 361 */       productBits = LongMath.log2(product, RoundingMode.FLOOR) + 1;
/*     */     } 
/*     */     
/* 364 */     if (product > 1L) {
/* 365 */       bignums.add(BigInteger.valueOf(product));
/*     */     }
/*     */     
/* 368 */     return listProduct(bignums).shiftLeft(shift);
/*     */   }
/*     */   
/*     */   static BigInteger listProduct(List<BigInteger> nums) {
/* 372 */     return listProduct(nums, 0, nums.size());
/*     */   }
/*     */   
/*     */   static BigInteger listProduct(List<BigInteger> nums, int start, int end) {
/* 376 */     switch (end - start) {
/*     */       case 0:
/* 378 */         return BigInteger.ONE;
/*     */       case 1:
/* 380 */         return nums.get(start);
/*     */       case 2:
/* 382 */         return ((BigInteger)nums.get(start)).multiply(nums.get(start + 1));
/*     */       case 3:
/* 384 */         return ((BigInteger)nums.get(start)).multiply(nums.get(start + 1)).multiply(nums.get(start + 2));
/*     */     } 
/*     */     
/* 387 */     int m = end + start >>> 1;
/* 388 */     return listProduct(nums, start, m).multiply(listProduct(nums, m, end));
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
/*     */   public static BigInteger binomial(int n, int k) {
/* 401 */     MathPreconditions.checkNonNegative("n", n);
/* 402 */     MathPreconditions.checkNonNegative("k", k);
/* 403 */     Preconditions.checkArgument((k <= n), "k (%s) > n (%s)", new Object[] { Integer.valueOf(k), Integer.valueOf(n) });
/* 404 */     if (k > n >> 1) {
/* 405 */       k = n - k;
/*     */     }
/* 407 */     if (k < LongMath.biggestBinomials.length && n <= LongMath.biggestBinomials[k]) {
/* 408 */       return BigInteger.valueOf(LongMath.binomial(n, k));
/*     */     }
/*     */     
/* 411 */     BigInteger accum = BigInteger.ONE;
/*     */     
/* 413 */     long numeratorAccum = n;
/* 414 */     long denominatorAccum = 1L;
/*     */     
/* 416 */     int bits = LongMath.log2(n, RoundingMode.CEILING);
/*     */     
/* 418 */     int numeratorBits = bits;
/*     */     
/* 420 */     for (int i = 1; i < k; i++) {
/* 421 */       int p = n - i;
/* 422 */       int q = i + 1;
/*     */ 
/*     */ 
/*     */       
/* 426 */       if (numeratorBits + bits >= 63) {
/*     */ 
/*     */         
/* 429 */         accum = accum.multiply(BigInteger.valueOf(numeratorAccum)).divide(BigInteger.valueOf(denominatorAccum));
/*     */ 
/*     */         
/* 432 */         numeratorAccum = p;
/* 433 */         denominatorAccum = q;
/* 434 */         numeratorBits = bits;
/*     */       } else {
/*     */         
/* 437 */         numeratorAccum *= p;
/* 438 */         denominatorAccum *= q;
/* 439 */         numeratorBits += bits;
/*     */       } 
/*     */     } 
/* 442 */     return accum.multiply(BigInteger.valueOf(numeratorAccum)).divide(BigInteger.valueOf(denominatorAccum));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible("TODO")
/*     */   static boolean fitsInLong(BigInteger x) {
/* 450 */     return (x.bitLength() <= 63);
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\math\BigIntegerMath.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */